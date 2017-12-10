package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
	private Socket socket = null;
	String message;
	String type;
	
	public ServerThread(Socket socket) {
		super("ServerThread");
		this.socket = socket;
	}
	
	static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	public void run() {
		try {
			PrintWriter ppwout = new PrintWriter(socket.getOutputStream(), true);
			//BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			
			
			message = convertStreamToString(socket.getInputStream());
			
			//while((message = br.readLine()) != null) {
				//ppwout.println(message);
			//}
			
			//ppwout.println("ServerThread running");
			
			System.out.println(message + "!!!!!!!!!");
			
		 
			//socket.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public int getPortNo() {
		return socket.getPort();
	}
	
	public String getMessage() {
		return message;
	}
}