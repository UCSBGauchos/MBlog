package MBlogWithPaxos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
	    			BallotNum sendBal = new BallotNum(localRep.promisBal.balNumber+1, localRep.promisBal.PID);//create the new balNum
	    		    new Thread(new PaxosPrepare(sendBal)).start();
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
	    			System.out.println(str);
	    		}
	    	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
	}
}
