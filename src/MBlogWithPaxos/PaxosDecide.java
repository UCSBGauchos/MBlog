package MBlogWithPaxos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

//Decide phase in paxos
//This thread is for deciding the value
//The msg should be "decide value"
public class PaxosDecide implements Runnable{
	String decideValue;
	public PaxosDecide(String _decideValue){
		this.decideValue = _decideValue;
	}
	public void run(){
		try{
			Socket socket = new Socket("0.0.0.0", 7777);
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			String TCPMsg = "decide|"+decideValue;
			out.writeUTF(TCPMsg);
		}
		catch(IOException e){
			System.out.println("cannot connect to the server, please check your server status");
		}
	}
}
