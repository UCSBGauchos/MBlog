package MBlogWithPaxos;

import java.util.ArrayList;
import java.util.Scanner;

public class Replication {
	ArrayList<String> log = new ArrayList<String>();
	public static void main(String [] args){
		TCPServer server = new TCPServer();
		server.serverFunction();
	}
}
