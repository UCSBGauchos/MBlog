package MBlogWithPaxos;

import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Application {
	
	//call client function, which is used to send msg to replication. Only need to send to local machine
	//post command to the server
	public void post(Message msg){
		
		String TCPMsg = msg.MSGKeyWord+' '+msg.MSGContent;
		Client c = new Client();
		String result = c.clientFunction(msg.destIP, TCPMsg);
		System.out.println(result);
	}
	//read command to the server
	public void read(Message msg){	
		String TCPMsg = msg.MSGKeyWord;
		Client c = new Client();
		String result = c.clientFunction(msg.destIP, TCPMsg);
		System.out.println(result);
	}
	//fail
	public void fail(){
		System.out.println("Begin fail process");
		String TCPMsg = "fail";
		Client c = new Client();
		String result = c.clientFunction("localhost", TCPMsg);
                System.out.println(result);
	}
	//unfail
	public void unfail(){
		System.out.println("Begin unfail process");
		String TCPMsg = "unfail";
		Client c = new Client();
		String result = c.clientFunction("localhost", TCPMsg);
                System.out.println(result);
	}
	
	public void commandLineInterface(String inputCommand){
		//create four patterns for the input
		Pattern PostPattern = Pattern.compile("post\\((.)+\\)");
		Pattern PostSplitPattern = Pattern.compile("\\(|\\)");
		Pattern ReadPattern = Pattern.compile("read");
		Pattern FailPattern = Pattern.compile("fail");
		Pattern UnfailPattern = Pattern.compile("unfail");
		//create four matcher for the pattern
		
		
		Matcher PostMatcher = PostPattern.matcher(inputCommand);
		Matcher ReadMatcher = ReadPattern.matcher(inputCommand);
		Matcher FailMatcher = FailPattern.matcher(inputCommand);
		Matcher UnfailMatcher = UnfailPattern.matcher(inputCommand);
		
		Common commonFunc = new Common();
		
		//it is fine, just send to its local
		if(PostMatcher.matches()){
			String[] result = PostSplitPattern.split(inputCommand);
			String microBlog = result[1];
			Message newMessage = new Message("post", microBlog, commonFunc.getLocalIPAddress(), "127.0.0.1", null, null, 0, 0);
			post(newMessage);
		}else if(ReadMatcher.matches()){
			Message newMessage = new Message("read", null, commonFunc.getLocalIPAddress(), "127.0.0.1", null, null, 0, 0);
			read(newMessage);
		}else if(FailMatcher.matches()){
			fail();
		}else if(UnfailMatcher.matches()){
			unfail();
		}
		else{
			System.out.println("Please input the correct command!");
		}
	}
	
	
	public static void main(String [] args){
		Application appInstance = new Application();
		while(true){
			System.out.println("Please input the command");
			Scanner in = new Scanner(System.in);
			String command = in.nextLine();
			appInstance.commandLineInterface(command);
		}
	}
}
