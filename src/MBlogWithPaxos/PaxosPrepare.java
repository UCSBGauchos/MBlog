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
			int currentInstancePaxos = localRep.paxosInstance; 
			while(localRep.paxosHistory[currentInstancePaxos].isDecided!=true){
				localRep.paxosHistory[currentInstancePaxos].promiseBal.balNumber++;
				System.out.println("Not decided yet");
				try{
					Socket socket1 = new Socket("54.219.46.244", 7777);
                                	DataInputStream in1 = new DataInputStream(socket1.getInputStream());
                                	DataOutputStream out1 = new DataOutputStream(socket1.getOutputStream());
					 String TCPMsg = currentInstancePaxos+"|prepare|"+localRep.paxosHistory[currentInstancePaxos].promiseBal.balNumber+"|"+localRep.paxosHistory[currentInstancePaxos].promiseBal.PID+"|54.245.185.75";
					out1.writeUTF(TCPMsg);
				}
				catch(IOException e){
					System.out.println("port California is not avaliable");
				}
				try{
					Socket socket2 = new Socket("localhost", 7777);
                                	DataInputStream in2 = new DataInputStream(socket2.getInputStream());
                                	DataOutputStream out2 = new DataOutputStream(socket2.getOutputStream());
					 String TCPMsg = currentInstancePaxos+"|prepare|"+localRep.paxosHistory[currentInstancePaxos].promiseBal.balNumber+"|"+localRep.paxosHistory[currentInstancePaxos].promiseBal.PID+"|54.245.185.75";
					out2.writeUTF(TCPMsg);
				}
				catch(IOException e){
                                        System.out.println("port Oregon is not avaliable");
                                }
				try{
					Socket socket3 = new Socket("54.204.249.32", 7777);
                                	DataInputStream in3 = new DataInputStream(socket3.getInputStream());
                                	DataOutputStream out3 = new DataOutputStream(socket3.getOutputStream());
 					String TCPMsg = currentInstancePaxos+"|prepare|"+localRep.paxosHistory[currentInstancePaxos].promiseBal.balNumber+"|"+localRep.paxosHistory[currentInstancePaxos].promiseBal.PID+"|54.245.185.75";
					out3.writeUTF(TCPMsg);
				}
				catch(IOException e){
                                        System.out.println("port Virginia is not avaliable");
                                }
				Thread.sleep(5000);
			}
			
			
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			// 			e.printStackTrace();
		}
	}
}
