package MBlogWithPaxos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

//Decide phase in paxos
//This thread is for deciding the value
//The msg should be "decide value"
//after deciding the value, move to the next entrance
public class PaxosDecide implements Runnable{
	int paxosInstance;
	String decideValue;
	Replication localRep;
	public PaxosDecide(int _paxosInstance, String _decideValue, Replication _locaRep){
		this.decideValue = _decideValue;
		this.localRep = _locaRep;
		this.paxosInstance = _paxosInstance;
	}
	//send to all
	public void run(){
		try{
			Socket socket = new Socket("54.219.46.244", 7777);
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			String TCPMsg = paxosInstance+"|decide|"+decideValue;
			out.writeUTF(TCPMsg);
		}
		catch(IOException e){
			System.out.println("cannot connect to the server, please check your server status");
		}
	}
}
