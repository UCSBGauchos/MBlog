package MBlogWithPaxos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

//Phase 2 in Paxos
//This thread is for sending accept MSG to all the other servers in the system
//The msg should be "accept bal value" 
public class PaxosAccept implements Runnable{
	String myValue;
	Replication localRep;
	public PaxosAccept(String _myValue, Replication _localRep){
		this.myValue = _myValue;
		this.localRep = _localRep;
	}
	public void run(){
		try{
			Socket socket = new Socket("0.0.0.0", 7777);
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());  
			String TCPMsg = "accept|"+localRep.promisBal.balNumber+"|"+localRep.promisBal.PID+"|"+myValue;
			out.writeUTF(TCPMsg);
		}
		catch(IOException e){
			System.out.println("cannot connect to the server, please check your server status");
		}
	}
}
