package MBlogWithPaxos;

import java.util.ArrayList;

public class PaxosVariables {
	BallotNum promiseBal;
	BallotNum acceptBal;
	String acceptVal;
	int recvAckCount;
	int recvAccCount;
	Boolean firstTimeSendAcc;
	Boolean isDecided;
	ArrayList<String> allAckMsg;
	
	
	public PaxosVariables(){
		this.promiseBal = new BallotNum(0,0);
		this.acceptBal = new BallotNum(0,0);
		this.acceptVal = null;
		this.recvAckCount = 0;
		this.recvAccCount = 0;
		this.firstTimeSendAcc = true;
		this.isDecided = false;
		this.allAckMsg = new ArrayList<String>();
	}
}
