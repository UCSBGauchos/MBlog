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
	//send to all
	public void run(){
	    if(myValue!=null){
			try{
				Socket socket1 = new Socket("54.219.46.244", 7777);
                        	DataInputStream in1 = new DataInputStream(socket1.getInputStream());
                        	DataOutputStream out1 = new DataOutputStream(socket1.getOutputStream());
				localRep.paxosHistory[paxosInstance].firstTimeSendAcc = false;
				String TCPMsg = paxosInstance+"|accept|"+localRep.paxosHistory[paxosInstance].promiseBal.balNumber+"|"+localRep.paxosHistory[paxosInstance].promiseBal.PID+"|"+myValue+"|54.245.185.75";
				out1.writeUTF(TCPMsg);
			}
			catch(IOException e){
                       		 System.out.println("port California is not avaliable");
               	        }
			try{
				 Socket socket2 = new Socket("54.245.185.75", 7777);
                        	 DataInputStream in2 = new DataInputStream(socket2.getInputStream());
                        	 DataOutputStream out2 = new DataOutputStream(socket2.getOutputStream());
				 localRep.paxosHistory[paxosInstance].firstTimeSendAcc = false; 
                                 String TCPMsg = paxosInstance+"|accept|"+localRep.paxosHistory[paxosInstance].promiseBal.balNumber+"|"+localRep.paxosHistory[paxosInstance].promiseBal.PID+"|"+myValue+"|54.245.185.75";
				 out2.writeUTF(TCPMsg);
			}
			catch(IOException e){
                                 System.out.println("port Oregon is not avaliable");
                        }
			try{
				Socket socket3 = new Socket("54.204.249.32", 7777);
                        	DataInputStream in3 = new DataInputStream(socket3.getInputStream());
                        	DataOutputStream out3 = new DataOutputStream(socket3.getOutputStream());
				localRep.paxosHistory[paxosInstance].firstTimeSendAcc = false;                                
                                String TCPMsg = paxosInstance+"|accept|"+localRep.paxosHistory[paxosInstance].promiseBal.balNumber+"|"+localRep.paxosHistory[paxosInstance].promiseBal.PID+"|"+myValue+"|54.245.185.75";
				 out3.writeUTF(TCPMsg);
			}
			catch(IOException e){
                                 System.out.println("port Virginia is not avaliable");
                        }
			try{
                                Socket socket4 = new Socket("54.216.164.157", 7777);
                                DataInputStream in4 = new DataInputStream(socket4.getInputStream());
                                DataOutputStream out4 = new DataOutputStream(socket4.getOutputStream());
                                localRep.paxosHistory[paxosInstance].firstTimeSendAcc = false;
                                String TCPMsg = paxosInstance+"|accept|"+localRep.paxosHistory[paxosInstance].promiseBal.balNumber+"|"+localRep.paxosHistory[paxosInstance].promiseBal.PID+"|"+myValue+"|54.245.185.75";
                                out4.writeUTF(TCPMsg);
                        }
                        catch(IOException e){
                                 System.out.println("port Ireland is not avaliable");
                        }
			try{
                                Socket socket5 = new Socket("54.251.231.42", 7777);
                                DataInputStream in5 = new DataInputStream(socket5.getInputStream());
                                DataOutputStream out5 = new DataOutputStream(socket5.getOutputStream());
                                localRep.paxosHistory[paxosInstance].firstTimeSendAcc = false;
                                String TCPMsg = paxosInstance+"|accept|"+localRep.paxosHistory[paxosInstance].promiseBal.balNumber+"|"+localRep.paxosHistory[paxosInstance].promiseBal.PID+"|"+myValue+"|54.245.185.75";
                                out5.writeUTF(TCPMsg);
                        }
                        catch(IOException e){
                                 System.out.println("port Singepore is not avaliable");
                        }


		      
	    }
	}
}
