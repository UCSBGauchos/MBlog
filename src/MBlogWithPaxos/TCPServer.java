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
		    	String str = in.readUTF();
		    	if(str!=null){
		    		String recvMicroBlog = str.substring(5);
			    	localRep.log.add(recvMicroBlog);
			    	System.out.println("Now the local log has "+localRep.log.size()+" blogs");
			    	for(String blog: localRep.log){
			    		System.out.print(blog+" ");
			    	}
			    	System.out.println();
			    	String returnMsg = "SUCCESS";
			    	out.writeUTF(returnMsg);
		    	}else{
		    		String returnMsg = "FAIL";
			    	out.writeUTF(returnMsg);
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
