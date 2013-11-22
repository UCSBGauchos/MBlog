package MBlogWithPaxos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

//Phase 2 in Paxos
//This thread is for sending accept MSG to all the other servers in the system
//The msg should be "prepare bal" 
public class PaxosAccept implements Runnable{
	BallotNum sendBal;
	public PaxosAccept(BallotNum _sendBal){
		this.sendBal = _sendBal;
	}
	public void run(){
		try{
			Socket socket = new Socket("0.0.0.0", 7777);
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());  
			//String TCPMsg = "prepare|"+sendBal.balNumber+"|"+sendBal.PID;
			//out.writeUTF(TCPMsg);
			System.out.println("recv! "+in);
		}
		catch(IOException e){
			System.out.println("cannot connect to the server, please check your server status");
		}
	}
}