package MBlogWithPaxos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	public String clientFunction(String ipAddress, String message){
		Socket socket = null;
		DataInputStream in = null;
		DataOutputStream out = null;
		String feedback = null;
		try{
			//create a new socket, we need to give it the dest ip address and the port
			socket = new Socket(ipAddress, 7777);
			//socket = new Socket("10.0.0.2", 7777);
			//socket = new Socket("10.0.0.2", 7777);
			//socket = new Socket("10.0.0.2", 7777);
			//socket = new Socket("10.0.0.2", 7777);

			//in for recv
			//out for send msg to the server
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());  
			out.writeUTF(message);
			feedback = in.readUTF();
		}
		catch(IOException e){
			System.out.println("cannot connect to the server, please check your server status");
		}
		return feedback;
	}
}
