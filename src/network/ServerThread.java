package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
	private Socket socket = null;
	
	public ServerThread(Socket socket) {
		super("ServerThread");
		this.socket = socket;
	}
	
	public void run() {
		try {
			PrintWriter ppwout = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String message = null;
			
			/*while((message = br.readLine()) != null) {
				ppwout.println(message);
			}*/
			
			//ppwout.println("ServerThread running");
			
			socket.close();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public int getPortNo() {
		return socket.getPort();
	}
}
