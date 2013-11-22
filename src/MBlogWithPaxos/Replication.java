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
	
	Queue<String> cacheLog = new LinkedList<String>();
	BallotNum promisBal;
	BallotNum accBal;
	String accValue;
	int recvAckCount;
	ArrayList<String> allAckMsg;
	
	
	public Replication() {
		this.cacheLog = new LinkedList<String>();
		this.promisBal = new BallotNum(0,0);
		this.accBal = new BallotNum(0,0);
		this.accValue = null;
		this.recvAckCount = 0;
		this.allAckMsg = new ArrayList<String>();
	}

	public static void main(String [] args){
		Replication localRep = new Replication();
		TCPServer server = new TCPServer(localRep);
		server.serverFunction();
	}
}
