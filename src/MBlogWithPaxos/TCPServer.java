package MBlogWithPaxos;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;

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
		    	System.out.println("The client is "+socket.getInetAddress().getHostAddress());
		    	String str = in.readUTF();
		    	if(str!=null){
		    		if(str.substring(0, 4).equals("post")){
		    			String recvMicroBlog = str.substring(5);
				    	localRep.log.add(recvMicroBlog);
				    	String returnMsg = "SUCCESS";
				    	out.writeUTF(returnMsg);
		    		}else if(str.substring(0,4).equals("read")){
		    			String returnMsg="";
		    			for(String blog: localRep.log){
		    				returnMsg+=blog;
				    	}
		    			out.writeUTF(returnMsg);
		    		}else if(str.equals("prepare")){
		    			System.out.println("prepare after post!");
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
