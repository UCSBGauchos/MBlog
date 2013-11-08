package MBlogWithPaxos;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Application {
	//call client function, which is used to send msg to replication
	public void post(Message msg){
		String TCPMsg = msg.operation+' '+msg.value;
		Client c = new Client();
		c.clientFunction(TCPMsg);
	}
	public void read(){	
		
	}
	public void fail(){
		System.out.println("Begin fail process");
	}
	public void unfail(){
		System.out.println("Begin unfail process");
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
		
		if(PostMatcher.matches()){
			String[] result = PostSplitPattern.split(inputCommand);
			String microBlog = result[1];
			Message newMessage = new Message("post", microBlog);
			post(newMessage);
		}else if(ReadMatcher.matches()){
			read();
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
