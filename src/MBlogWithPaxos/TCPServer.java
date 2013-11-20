package MBlogWithPaxos;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;

//TCP server, when it get the post command from the application, it will run paxos consensus protocol and decide what value 
//can be written to the next log.
public class TCPServer {
	public void serverFunction(){
		ServerSocket server = null;
		Socket socket = null;
		DataOutputStream out = null;
	    DataInputStream in = null;
	    Replication localRep = new Replication();
	    try{
            server=new ServerSocket(7777);
	    }
	    catch(IOException e){
            System.out.println("ERRO:"+e);
        } 
	    try{
	    	while(true){
	    		socket = server.accept();
		    	in = new DataInputStream(socket.getInputStream());
		    	out = new DataOutputStream(socket.getOutputStream());
		    	//System.out.println("The client is "+socket.getInetAddress().getHostAddress());
		    	String str = in.readUTF();
		    	if(str!=null){
		    		if(str.substring(0, 4).equals("post")){
		    			String recvMicroBlog = str.substring(5);
		    			//should start a new thread 
		    			//String TCPMsg = "prepare";
		    		    new Thread(new Paxos()).start();
		    			//p.PaxosInstance("127.0.0.1", TCPMsg);
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
		    		}else if(str.equals("prepare")){
		    			System.out.println("Send prepare after post!");
		    		}
		    	}
		    	
	    	}
	    }catch(IOException e){
	    	System.out.println("ERRO:"+e);
	    }
	    try {
			socket.close();
		} catch (IOException e) {
			System.out.println("ERRO:"+e);
		}
	}
}
