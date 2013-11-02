package MBlogWithPaxos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class client {
	public static void main(String [] args){
		Socket socket = null;
		DataInputStream in = null;
		DataOutputStream out = null;
		try{
			while(true){
				socket = new Socket("localhost", 7777);
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());  
				System.out.println("Pleas input the messge which you want to send");
				Scanner scan = new Scanner(System.in);  
				String str = scan.nextLine();
				out.writeUTF(str);
			}
		}
		catch(IOException e){
			System.out.println("cannot connect");
		}
	}
}
