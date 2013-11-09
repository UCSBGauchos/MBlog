package MBlogWithPaxos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	public void clientFunction(String message){
		Socket socket = null;
		DataInputStream in = null;
		DataOutputStream out = null;
		try{
			//while(true){
			socket = new Socket("localhost", 7777);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());  
			out.writeUTF(message);
			String feedback = in.readUTF();
			System.out.println("The application gets feedBack "+feedback);
			//}
		}
		catch(IOException e){
			System.out.println("cannot connect to the server, please check your server status");
		}
	}
}
