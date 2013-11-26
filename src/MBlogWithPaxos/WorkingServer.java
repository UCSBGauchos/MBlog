package MBlogWithPaxos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//This is the main thread to handle different socket request. It means that the rep server can handle
//different 
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
	    			localRep.cacheLog.add(recvMicroBlog);
	    			//initial all the paxos variables in the local instance(local instance)
	    			//just make sure that this location is not decided by majority. If it is decided, just skip it
	    			while(localRep.log[localRep.paxosInstance] != null){
	    				localRep.paxosInstance++;
	    			}
	    			localRep.paxosHistory[localRep.paxosInstance] = new PaxosVariables();
	    			localRep.paxosHistory[localRep.paxosInstance].promiseBal.balNumber++;
	    		    new Thread(new PaxosPrepare(localRep)).start();
			    	String returnMsg = "SUCCESS";
			    	out.writeUTF(returnMsg);
	    		}else if(str.substring(0,4).equals("read")){
	    			String returnMsg="";
	    			for(String blog: localRep.log){
	    				if(blog!=null){
	    					//only print those which are not null
	    					String patternBlog = blog+":";
		    				returnMsg+=patternBlog;
	    				}
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
	    			if(localRep.paxosHistory[paxosInstance] == null){
	    				System.out.println("It is null, need to create a new one!");
	    				localRep.paxosHistory[paxosInstance] = new PaxosVariables();
	    			}
	    			//here just for testing (equal also can send back ack). In the real project, it should be unique
	    			if(commonFunc.isBalBigger(sendMsg, localRep.paxosHistory[paxosInstance].promiseBal)){
	    				System.out.println("Send is bigger than promise, should return ack");
	    				localRep.paxosHistory[paxosInstance].promiseBal.balNumber = commonFunc.getPromiseNum(sendMsg);
	    				localRep.paxosHistory[paxosInstance].promiseBal.PID = commonFunc.getPromisePID(sendMsg);
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
	    			localRep.paxosHistory[paxosInstance].recvAckCount++;
	    			//majority***********************
	    			String ackContent = str.substring(6);
	    			localRep.paxosHistory[paxosInstance].allAckMsg.add(ackContent);
	    			if(localRep.paxosHistory[paxosInstance].recvAckCount>0){
	    				String myValue = null;
	    				int maxAccNum = 0;
	    				int maxAccPID = 0;
	    				//traverse all the majority, get the value with the bigest if it is not null, else use ite own
	    				for(String ackStr: localRep.paxosHistory[paxosInstance].allAckMsg){
	    					String accValue = commonFunc.getAccValue(ackStr);
	    					if(accValue.equals("null")){
	    						accValue = null;
	    					}
	    					int accNumber = commonFunc.getAccNum(ackStr);
	    					int accPID = commonFunc.getAccPID(ackStr);
//	    					here just for testing, in the real environment, it should be unique******************
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
	    				//if no value received, need to propose my own value;
	    				if(myValue == null){
	    					myValue = localRep.cacheLog.peek();
	    					localRep.paxosHistory[paxosInstance].needSendMyOwnValue = true;
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
	    			localRep.paxosHistory[paxosInstance].recvAccCount++;
	    			if(sendBalNum>=localRep.paxosHistory[paxosInstance].promiseBal.balNumber){
	    				//update its local acc bal and acc number
	    				localRep.paxosHistory[paxosInstance].promiseBal.balNumber = sendBalNum;
	    				localRep.paxosHistory[paxosInstance].promiseBal.PID = sendPID;
	    				localRep.paxosHistory[paxosInstance].acceptVal = sendValue;
	    			}
	    			if(localRep.paxosHistory[paxosInstance].firstTimeSendAcc == true){
	    				System.out.println("Has never sent accept msg, so I will send it now!");
	    				new Thread(new PaxosAccept(paxosInstance, localRep.paxosHistory[paxosInstance].acceptVal, localRep)).start();
	    			}
	    			//accept from majority********************************
	    			//decide v and start decide thread
	    			//add it to the instance location of the log
	    			if(localRep.paxosHistory[paxosInstance].recvAccCount>0){
	    				System.out.println("decide "+sendValue);
	    				localRep.paxosHistory[paxosInstance].isDecided = true;
	    				localRep.log[paxosInstance] = sendValue;
	    				localRep.paxosInstance++;
	    				if(localRep.paxosHistory[paxosInstance].needSendMyOwnValue == true){
	    					//move the value from cache to the local log, means it is decided
	    					System.out.println("Remove "+localRep.cacheLog.poll()+" in cache");
	    				}
	    				new Thread(new PaxosDecide(paxosInstance, sendValue, localRep)).start();
	    				//when one value is decided, check the local cache. If it still has value, need to start a new 
	    				//paxos instance to determin the entrance of it. OW, dont need to start paxos, wait for another
	    				//post
	    				if(localRep.cacheLog.size()!=0){
	    					new Thread(new PaxosPrepare(localRep)).start();
	    				}
	    			}
	    		}else if(str.substring(2,8).equals("decide")){
	    			Common commonFunc = new Common();
	    			int paxosInstance = commonFunc.getPaxosInstanceNumber(str);
	    			System.out.println("Now the paxos instance is "+paxosInstance);
	    			String decideValue = str.substring(9);
	    			if(localRep.paxosHistory[paxosInstance].isDecided == false){
	    				System.out.println("has not decided");
	    				System.out.println("decide "+decideValue);
	    				localRep.paxosHistory[paxosInstance].isDecided = true;
	    				localRep.log[paxosInstance] = decideValue;
	    				localRep.paxosInstance++;
	    				if(localRep.paxosHistory[paxosInstance].needSendMyOwnValue == true){
	    					System.out.println("Remove "+localRep.cacheLog.poll()+" in cache");
	    				}
	    				if(localRep.cacheLog.size()!=0){
	    					new Thread(new PaxosPrepare(localRep)).start();
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
