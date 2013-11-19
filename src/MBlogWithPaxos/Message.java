package MBlogWithPaxos;

//message class
//keyword(NULL), content(NULL), sourceIP(NULL), destIP(NULL), sendBal(NULL), accBal(NULL), accValue(0), proposeValue 

public class Message {
	String MSGKeyWord;
	String MSGContent;
	String sourceIP;
	String destIP;
	BallotNum sendBal;
	BallotNum accBal;
	int accValue;
	int proposeValue;
	
	
	public Message (String _MSGKeyWord, String _MSGContent, String _sourceIP, String _destIP, BallotNum _sendBal, BallotNum _accBal, int _accValue, int _proposeValue){
		this.MSGKeyWord = _MSGKeyWord;
		this.MSGContent = _MSGContent;
		this.sourceIP = _sourceIP;
		this.destIP = _destIP;
		this.sendBal = _sendBal;
		this.accBal = _accBal;
		this.accValue = _accValue;
		this.proposeValue = _proposeValue;
	}
}
