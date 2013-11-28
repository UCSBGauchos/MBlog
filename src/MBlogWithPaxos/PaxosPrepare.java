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
	//important! preiodly
	public void run(){
		try{
			int currentInstancePaxos = localRep.paxosInstance;
			while(localRep.paxosHistory[currentInstancePaxos].isDecided!=true){
				Socket socket = new Socket("54.219.46.244", 7777);
				DataInputStream in = new DataInputStream(socket.getInputStream());
				DataOutputStream out = new DataOutputStream(socket.getOutputStream()); 
				String TCPMsg = currentInstancePaxos+"|prepare|"+localRep.paxosHistory[currentInstancePaxos].promiseBal.balNumber+"|"+localRep.paxosHistory[currentInstancePaxos].promiseBal.PID+"|54.245.185.75";
				localRep.paxosHistory[currentInstancePaxos].promiseBal.balNumber++;
				out.writeUTF(TCPMsg);
				Thread.sleep(5000);
			}
			
		}
		catch(IOException e){
			System.out.println("cannot connect to the server, please check your server status");
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
