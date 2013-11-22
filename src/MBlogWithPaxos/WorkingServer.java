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
			//for testing what msg has been recved
			//here we have diff ths for prepare, ack, accept and decide
			System.out.println("Recv "+str);
	    	if(str!=null){
	    		if(str.substring(0, 4).equals("post")){
	    			String recvMicroBlog = str.substring(5);
	    			localRep.promisBal.balNumber++;
	    		    new Thread(new PaxosPrepare(localRep)).start();
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
	    			//here just for testing (equal also can send back ack). In the real project, it should be unique
	    			if(commonFunc.isBalBigger(sendMsg, localRep.promisBal)){
	    				System.out.println("Send is bigger than promise, should return ack");
	    				localRep.promisBal.balNumber = commonFunc.getPromiseNum(sendMsg);
	    				localRep.promisBal.PID = commonFunc.getPromisePID(sendMsg);
	    				String souceIP = commonFunc.getSouceIP(sendMsg);
	    				new Thread(new PaxosAck(souceIP, localRep)).start();
	    			}else{
	    				System.out.println("Send is not bigger than promise, should not return ack");
	    				String TCPMsg = "deny";
	    			}
	    		}
	    	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
	}
}
