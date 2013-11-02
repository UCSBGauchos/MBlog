package MBlogWithPaxos;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
	public static void main(String [] args){
		ServerSocket server = null;
		Socket socket = null;
		DataOutputStream out = null;
	    DataInputStream in = null;
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
	    		System.out.println("The server receive "+str);	
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
