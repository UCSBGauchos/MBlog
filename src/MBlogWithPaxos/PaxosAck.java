package MBlogWithPaxos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

//Phase 1 in paxos
//This thread is for sending ack MSG to all the other servers in the system
//The msg should be "ack promisebal(number, PID) acceptbal(number, PID) acceptVal" 
public class PaxosAck implements Runnable{
	String souceIP;
	Replication localRep;
	int paxosInstance;
	public PaxosAck(int _paxosInstance, String _souceIP, Replication _localRep){
		this.souceIP = _souceIP;
		this.localRep = _localRep;
		this.paxosInstance = _paxosInstance;
	}
	//send to the sender
	public void run(){
		try{
			Socket socket = new Socket(souceIP, 7777);
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			String TCPMsg = paxosInstance+"|ack|"+localRep.paxosHistory[paxosInstance].promiseBal.balNumber+"|"+localRep.paxosHistory[paxosInstance].promiseBal.PID+"|"+localRep.paxosHistory[paxosInstance].acceptBal.balNumber+"|"+localRep.paxosHistory[paxosInstance].acceptBal.PID+"|"+localRep.paxosHistory[paxosInstance].acceptVal;
			out.writeUTF(TCPMsg);
		}
		catch(IOException e){
			System.out.println("cannot connect to the server, please check your server status");
		}
	}
}
