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
				 Socket socket1 = new Socket("54.219.46.244", 7777);
                        	 DataInputStream in1 = new DataInputStream(socket1.getInputStream());
                        	 DataOutputStream out1 = new DataOutputStream(socket1.getOutputStream());
				 String TCPMsg = paxosInstance+"|decide|"+decideValue;
				 out1.writeUTF(TCPMsg);
			}
			catch(IOException e){
                        	System.out.println("port California is not avaliable");
                	}
			try{
				Socket socket2 = new Socket("localhost", 7777);
                        	DataInputStream in2 = new DataInputStream(socket2.getInputStream());
                        	DataOutputStream out2 = new DataOutputStream(socket2.getOutputStream());
				String TCPMsg = paxosInstance+"|decide|"+decideValue;
                                out2.writeUTF(TCPMsg);
			}
			catch(IOException e){
                                System.out.println("port Oregon is not avaliable");
                        }
			try{
				Socket socket3 = new Socket("54.204.249.32", 7777);
                        	DataInputStream in3 = new DataInputStream(socket3.getInputStream());
                        	DataOutputStream out3 = new DataOutputStream(socket3.getOutputStream());
				String TCPMsg = paxosInstance+"|decide|"+decideValue;
                                out3.writeUTF(TCPMsg);
			}
			catch(IOException e){
                                System.out.println("port Virginia  is not avaliable");
                        }
	}
}
