package MBlogWithPaxos;

import java.util.ArrayList;
import java.util.Scanner;


//The TCP server on replication keep listening. It will get different command from the client
//The human command will contain post, read ,fail unfail


public class Replication {
	ArrayList<String> log = new ArrayList<String>();
	public static void main(String [] args){
		TCPServer server = new TCPServer();
		server.serverFunction();
	}
}
