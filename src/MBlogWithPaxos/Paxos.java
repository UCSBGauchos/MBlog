package MBlogWithPaxos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Paxos implements Runnable {
	public void run(){
		Socket socket = null;
		DataInputStream in = null;
		DataOutputStream out = null;
		String feedback = null;
		try{
			socket = new Socket("localhost", 7777);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());  
			out.writeUTF("prepare");
		}
		catch(IOException e){
			System.out.println("cannot connect to the server, please check your server status");
		}
	}
}
