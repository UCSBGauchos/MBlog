package MBlogWithPaxos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//can recv: post, read, fail ,unfail, prepare, ack, accept,decide
public class WorkingServer implements Runnable  {
	
	Socket socket;
	Replication localRep;
	WorkingServer(Socket s, Replication log){
		this.socket = s;
		this.localRep = log;
	}

	public void run() {
		System.out.println("Create a new server thread to process the listened message");
		DataInputStream in;
		DataOutputStream out;
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			String str = in.readUTF();
	    	if(str!=null){
	    		if(str.substring(0, 4).equals("post")){
	    			String recvMicroBlog = str.substring(5);
	    			localRep.promisBal.balNumber++;
	    		    new Thread(new PaxosPrepare(localRep.promisBal)).start();
			    	localRep.log.add(recvMicroBlog);
			    	String returnMsg = "SUCCESS";
			    	out.writeUTF(returnMsg);
	    		}else if(str.substring(0,4).equals("read")){
	    			String returnMsg="";
	    			for(String blog: localRep.log){
	    				String patternBlog = blog+":";
	    				returnMsg+=patternBlog;
			    	}
	    			if(!returnMsg.equals("")){
	    				returnMsg = returnMsg.substring(0, returnMsg.length()-1);
	    			}
	    			out.writeUTF(returnMsg);
	    		}else if(str.substring(0, 7).equals("prepare")){
	    			String sendMsg = str.substring(8);
	    			Common commonFunc = new Common();
	    			if(commonFunc.isBalBigger(sendMsg, localRep.promisBal)){
	    				System.out.println("Send is bigger than promise, should return ack");
	    				localRep.promisBal.balNumber = commonFunc.getPromiseNum(sendMsg);
	    				localRep.promisBal.PID = commonFunc.getPromisePID(sendMsg);
	    				String TCPMsg = "ack|"+localRep.promisBal.balNumber+"|"+localRep.promisBal.PID+"|"+localRep.accBal.balNumber+"|"+localRep.accBal.PID+"!"+localRep.accValue;
	    				out.writeUTF(TCPMsg);
	    			}else{
	    				System.out.println("Send is not bigger than promise, should not return ack");
	    			}
	    		}else if(str.substring(0,3).equals("ack")){
	    			localRep.recvAckCount++;
	    			//first assume there are just one node in the system, so the majority is 1
	    			//majority case
	    			if(localRep.recvAckCount>0){
	    				//call another thread to send accept msg to all threads
	    			}
	    		}
	    	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
	}
}
