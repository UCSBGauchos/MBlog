package MBlogWithPaxos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//can recv: post, read, fail ,unfail, prepare, ack, accept,decide
public class WorkingServer implements Runnable  {
	
	Socket socket;
	Replication localRep;
	WorkingServer(Socket s, Replication log){
		this.socket = s;
		this.localRep = log;
	}

	public void run() {
		//System.out.println("Create a new server thread to process the listened message");
		DataInputStream in;
		DataOutputStream out;
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			String str = in.readUTF();
			//for testing what msg has been recved
			//here we have diff ths for prepare, ack, accept and decide
			System.out.println("Recv "+str);
	    	if(str!=null){
	    		//for post, just need to write the post content to the cache, here don't need to start the paxos
	    		//store each paxos's instance's paxos variables in the paxos history log
	    		if(str.substring(0, 4).equals("post")){
	    			String recvMicroBlog = str.substring(5);
	    			//localRep.promisBal.balNumber++;
	    			localRep.paxosHistory.add(localRep.paxosInstance, new PaxosVariables());
	    			localRep.paxosHistory.get(localRep.paxosInstance).promiseBal.balNumber++;
	    		    new Thread(new PaxosPrepare(localRep)).start();
			    	localRep.cacheLog.add(recvMicroBlog);
			    	String returnMsg = "SUCCESS";
			    	out.writeUTF(returnMsg);
	    		}else if(str.substring(0,4).equals("read")){
	    			String returnMsg="";
	    			for(String blog: localRep.log){
	    				String patternBlog = blog+":";
	    				returnMsg+=patternBlog;
			    	}
	    			if(!returnMsg.equals("")){
	    				returnMsg = returnMsg.substring(0, returnMsg.length()-1);
	    			}
	    			out.writeUTF(returnMsg);
	    		}else if(str.substring(2, 9).equals("prepare")){
	    			Common commonFunc = new Common();
	    			int paxosInstance = commonFunc.getPaxosInstanceNumber(str);
	    			String sendMsg = str.substring(10);
	    			System.out.println("Now the paxos instance is "+paxosInstance);
	    			//here just for testing (equal also can send back ack). In the real project, it should be unique
	    			if(commonFunc.isBalBigger(sendMsg, localRep.paxosHistory.get(paxosInstance).promiseBal)){
	    				System.out.println("Send is bigger than promise, should return ack");
	    				localRep.paxosHistory.get(paxosInstance).promiseBal.balNumber = commonFunc.getPromiseNum(sendMsg);
	    				localRep.paxosHistory.get(paxosInstance).promiseBal.PID = commonFunc.getPromisePID(sendMsg);
	    				String souceIP = commonFunc.getSouceIP(sendMsg);
	    				new Thread(new PaxosAck(paxosInstance, souceIP, localRep)).start();
	    			}else{
	    				System.out.println("Send is not bigger than promise, should not return ack");
	    				String TCPMsg = "deny";
	    			}
	    		}else if(str.substring(2,5).equals("ack")){
	    			Common commonFunc = new Common();
	    			int paxosInstance = commonFunc.getPaxosInstanceNumber(str);
	    			System.out.println("Now the paxos instance is "+paxosInstance);
	    			localRep.paxosHistory.get(paxosInstance).recvAckCount++;
	    			//majority, for this case, just use one
	    			String ackContent = str.substring(6);
	    			localRep.paxosHistory.get(paxosInstance).allAckMsg.add(ackContent);
	    			if(localRep.paxosHistory.get(paxosInstance).recvAckCount>0){
	    				String myValue = null;
	    				int maxAccNum = 0;
	    				int maxAccPID = 0;
	    				//traverse all the majority, get the value with the bigest if it is not null, else use ite own
	    				for(String ackStr: localRep.paxosHistory.get(paxosInstance).allAckMsg){
	    					String accValue = commonFunc.getAccValue(ackStr);
	    					if(accValue.equals("null")){
	    						accValue = null;
	    					}
	    					int accNumber = commonFunc.getAccNum(ackStr);
	    					int accPID = commonFunc.getAccPID(ackStr);
//	    					here just for testing, in the real environment, it should be unique
	    					if(accValue!=null){
	    						if(accNumber>=maxAccNum){
	    							maxAccNum = accNumber;
	    							myValue = accValue;
	    						}else if(accPID>=maxAccPID){
	    							maxAccPID = accPID;
	    							myValue = accValue;
	    						}
	    					}
	    				}
	    				//if no value received, use its own value in the cache to post
	    				if(myValue == null){
	    					myValue = localRep.cacheLog.peek();
	    				}
	    				System.out.println("send value is "+myValue);
	    				new Thread(new PaxosAccept(paxosInstance, myValue, localRep)).start();
	    			}
	    		}else if(str.substring(2,8).equals("accept")){
	    			Common commonFunc = new Common();
	    			int paxosInstance = commonFunc.getPaxosInstanceNumber(str);
	    			System.out.println("Now the paxos instance is "+paxosInstance);
	    			String acceptContent = str.substring(9);
	    			int sendBalNum = commonFunc.getPromiseNum(acceptContent);
	    			int sendBalPID = commonFunc.getPromisePID(acceptContent);
	    			int sendPID = commonFunc.getPromisePID(acceptContent);
	    			String sendValue = commonFunc.getSendValue(acceptContent);
	    			localRep.paxosHistory.get(paxosInstance).recvAccCount++;
	    			if(sendBalNum>=localRep.paxosHistory.get(paxosInstance).promiseBal.balNumber){
	    				//update its local acc bal and acc number
	    				localRep.paxosHistory.get(paxosInstance).promiseBal.balNumber = sendBalNum;
	    				localRep.paxosHistory.get(paxosInstance).promiseBal.PID = sendPID;
	    				localRep.paxosHistory.get(paxosInstance).acceptVal = sendValue;
	    			}
	    			//***************
	    			if(localRep.paxosHistory.get(paxosInstance).firstTimeSendAcc == true){
	    				System.out.println("Has never sent accept msg, so I will send it now!");
	    				new Thread(new PaxosAccept(paxosInstance, localRep.paxosHistory.get(paxosInstance).acceptVal, localRep)).start();
	    			}
	    			//accept from majority, here for testing, just need to be one
	    			//decide v and start decide thread
	    			//add it to the instance location of the log
	    			if(localRep.paxosHistory.get(paxosInstance).recvAccCount>0){
	    				System.out.println("decide "+sendValue);
	    				localRep.paxosHistory.get(paxosInstance).isDecided = true;
	    				localRep.log.add(localRep.paxosInstance, sendValue);
	    				localRep.paxosInstance++;
	    				if(localRep.cacheLog.peek().equals(sendValue)){
	    					System.out.println("Remove "+localRep.cacheLog.poll()+" in cache");
	    				}
	    				new Thread(new PaxosDecide(paxosInstance, sendValue, localRep)).start();
	    			}
	    		}else if(str.substring(2,8).equals("decide")){
	    			Common commonFunc = new Common();
	    			int paxosInstance = commonFunc.getPaxosInstanceNumber(str);
	    			System.out.println("Now the paxos instance is "+paxosInstance);
	    			String decideValue = str.substring(7);
	    			if(localRep.paxosHistory.get(paxosInstance).isDecided == false){
	    				System.out.println("has not decided");
	    				System.out.println("decide "+decideValue);
	    				localRep.paxosHistory.get(paxosInstance).isDecided = true;
	    				localRep.log.add(localRep.paxosInstance, decideValue);
	    				localRep.paxosInstance++;
	    				if(localRep.cacheLog.peek().equals(decideValue)){
	    					System.out.println("Remove "+localRep.cacheLog.poll()+" in cache");
	    				}
	    			}
	    		}
	    	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
	}
}
