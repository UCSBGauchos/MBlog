package MBlogWithPaxos;

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
		}else{
			return false;
		}
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
	
	public int stringToInt(String str){
		int number = 0;
		for(int i=0; i<str.length(); i++){
			number+=(str.charAt(i)-48)*Math.pow(10,(str.length()-i)-1);
		}
		return number;
	}
}
