package MBlogWithPaxos;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;


//The TCP server on replication keep listening. It will get different command from the client
//The human command will contain post, read ,fail unfail
//Each replica server has its local log, promise ballot number, accepted ballot number and accepted value
//replica server main function, which starts the server function


public class Replication {
	
	int paxosInstance;
	Queue<String> cacheLog;
	String [] log ;
	PaxosVariables [] paxosHistory;
	
	
	
	
	public Replication() {
		this.paxosInstance = 0;
		this.cacheLog = new LinkedList<String>();
		this.log = new String [500];
		this.paxosHistory = new PaxosVariables [500];
	}
	
//	//to start a new instance, need to initial the node except the log
//	public void initialForNewPaxosInstance(){
//		this.promisBal = new BallotNum(0,0);
//		this.accBal = new BallotNum(0,0);
//		this.accValue = null;
//		this.recvAckCount = 0;
//		this.recvAccCount = 0;
//		this.allAckMsg = new ArrayList<String>();
//		this.firstTimeSendAcc = true;
//		this.isDecided = false;
//	}

	public static void main(String [] args){
		Replication localRep = new Replication();
		TCPServer server = new TCPServer(localRep);
		server.serverFunction();
	}
}
