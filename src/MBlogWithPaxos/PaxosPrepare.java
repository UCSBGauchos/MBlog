package MBlogWithPaxos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

//Phase 1 in paxos
//This thread is for sending prepare MSG to all the other servers in the system
//The msg should be "prepare bal" 
public class PaxosPrepare implements Runnable {
	Common commonFunc = new Common();
	Replication localRep;
	public PaxosPrepare(Replication _localRep){
		this.localRep = _localRep;
	}
	//it should be modified, send to different servers(IP)
	public void run(){
		try{
			Socket socket = new Socket("54.219.46.244", 7777);
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());  
			String TCPMsg = localRep.paxosInstance+"|prepare|"+localRep.paxosHistory[localRep.paxosInstance].promiseBal.balNumber+"|"+localRep.paxosHistory[localRep.paxosInstance].promiseBal.PID+"|54.245.185.75";
			out.writeUTF(TCPMsg);
			
		}
		catch(IOException e){
			System.out.println("cannot connect to the server, please check your server status");
		}
	}
}
