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
	int paxosInstance;
	public PaxosAccept(int _paxosInstance, String _myValue, Replication _localRep){
		this.myValue = _myValue;
		this.localRep = _localRep;
		this.paxosInstance = _paxosInstance;
	}
	public void run(){
		try{
			Socket socket = new Socket("0.0.0.0", 7777);
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());  
			String TCPMsg = paxosInstance+"|accept|"+localRep.paxosHistory[paxosInstance].promiseBal.balNumber+"|"+localRep.paxosHistory[paxosInstance].promiseBal.PID+"|"+myValue;
			//only the first time need to send accept broadcase
			localRep.paxosHistory[paxosInstance].firstTimeSendAcc = false;
			out.writeUTF(TCPMsg);
		}
		catch(IOException e){
			System.out.println("cannot connect to the server, please check your server status");
		}
	}
}
