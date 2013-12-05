package MBlogWithPaxos;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;

//TCP server, when it get the post command from the application, it will run paxos consensus protocol and decide what value 
//can be written to the next log.
public class TCPServer {
	
	 Replication localRep;
	static Boolean isWork;
	
	public TCPServer(Replication _localRep){
		this.localRep = _localRep;
		this.isWork = true;
	}
	
	
	public void serverFunction(){
		ServerSocket server = null;
		Socket socket = null;
	    try{
            server=new ServerSocket(7777);
	    }
	    catch(IOException e){
            System.out.println("ERRO:"+e);
        } 
	    //each time get a messages from the client (both application and the server). Create a new thread to handle it.
	    //Then different clients can send messages to one rep at the same time. (Different thread handle different messages).  
	    try{
	    	while(true){
			System.out.println(this.isWork);
			socket = server.accept();
                        new Thread(new WorkingServer(socket, localRep)).start();
	    	}
	    }catch(IOException e){
	    	System.out.println("ERRO:"+e);
	    }
	    try {
			socket.close();
		} catch (IOException e) {
			System.out.println("ERRO:"+e);
		}
	}
}
