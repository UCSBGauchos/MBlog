package MBlogWithPaxos;

import java.util.ArrayList;
import java.util.Hashtable;

public class PaxosVariables {
	BallotNum promiseBal;
	BallotNum acceptBal;
	String acceptVal;
	int recvAckCount;
	int recvAccCount;
	Boolean firstTimeSendAcc;
	Boolean isDecided;
	Boolean needSendMyOwnValue;
	ArrayList<String> allAckMsg;
	ArrayList<String> ackIPs;
	ArrayList<String> acceptIPs;
	
	
	public PaxosVariables(){
		this.promiseBal = new BallotNum(0,0);
		this.acceptBal = new BallotNum(0,0);
		this.acceptVal = null;
		this.recvAckCount = 0;
		this.recvAccCount = 0;
		this.firstTimeSendAcc = true;
		this.isDecided = false;
		this.needSendMyOwnValue = false;
		this.allAckMsg = new ArrayList<String>();
		this.acceptIPs = new ArrayList<String>();
		this.ackIPs = new ArrayList<String>();
	}
}
