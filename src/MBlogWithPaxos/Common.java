package MBlogWithPaxos;

import java.net.UnknownHostException;

//Some useful function for this project
public class Common {
	public boolean isBalBigger(String sendBalInMsg, BallotNum localBal){
		int localNumber = localBal.balNumber;
		int localPID = localBal.PID;
		String [] messageElements = sendBalInMsg.split("\\|");
		int sendNumber = stringToInt(messageElements[0]);
		int sendPID = stringToInt(messageElements[1]);
		if(sendNumber>localNumber){
			return true;
		}else if(sendNumber==localNumber&&sendPID>localPID){
			return true;
		}else if(sendNumber==localNumber&&sendPID==localPID){
			return true; // this equal is just for one machine testing! (should be removed in real project)*****
		}else{
			return false;
		}
	}
	
	public int getPaxosInstanceNumber(String sendBalInMsg){
		String [] messageElements = sendBalInMsg.split("\\|");
		int paxosInstanceNumber = stringToInt(messageElements[0]);
		return paxosInstanceNumber;
	}
	
	public int getPromiseNum(String sendBalInMsg){
		String [] messageElements = sendBalInMsg.split("\\|");
		int sendPID = stringToInt(messageElements[0]);
		return sendPID;
	}
	
	public int getPromisePID(String sendBalInMsg){
		String [] messageElements = sendBalInMsg.split("\\|");
		int sendNumber = stringToInt(messageElements[1]);
		return sendNumber;
	}
	
	public String getSouceIP(String sendBalInMsg){
		String [] messageElements = sendBalInMsg.split("\\|");
		String souceIP = messageElements[2];
		return souceIP;
	}
	
	public String getAccValue(String sendBalInMsg){
		String [] messageElements = sendBalInMsg.split("\\|");
		String value = messageElements[4];
		return value;
	}
	
	public int getAccNum(String sendBalInMsg){
		String [] messageElements = sendBalInMsg.split("\\|");
		int sendPID = stringToInt(messageElements[2]);
		return sendPID;
	}
	
	public int getAccPID(String sendBalInMsg){
		String [] messageElements = sendBalInMsg.split("\\|");
		int sendPID = stringToInt(messageElements[3]);
		return sendPID;
	}
	
	public String getSendValue(String sendBalInMsg){
		String [] messageElements = sendBalInMsg.split("\\|");
		String sendValue= messageElements[2];
		return sendValue;
	}
	
	
	
	public int stringToInt(String str){
		int number = 0;
		for(int i=0; i<str.length(); i++){
			number+=(str.charAt(i)-48)*Math.pow(10,(str.length()-i)-1);
		}
		return number;
	}
	
	public String getLocalIPAddress(){
		String sourceIP = null;
		try {
		    java.net.InetAddress ipAddress = java.net.InetAddress.getByName("localhost");
		    sourceIP = ipAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
		        e.printStackTrace();
		}
		return sourceIP;
	}
			
}
