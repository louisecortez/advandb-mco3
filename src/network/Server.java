package network;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private ServerSocket ssSharedSocket;
	private ServerSocket ssEuropeAmerica;
	private ServerSocket ssAsiaAfrica;
	private ServerSocket ssAllRegions;
	
	private String ipEuropeAmerica = null;
	private String ipAsiaAfrica = null;
	private String ipAllRegions = null;
	
	private String SERVER_NAME = "Server";
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Server server = new Server();
		server.accept();
		server.start();
	}
	
	public Server() throws IOException {
		ssSharedSocket = new ServerSocket(1234);
		ssEuropeAmerica = new ServerSocket(1235);
		
		ssSharedSocket.setSoTimeout(20000);
		ssEuropeAmerica.setSoTimeout(20000);
	}
	
	public void accept() throws IOException {
		// accept up to 3 nodes for the distributed database
		int nodes = 0;
		Socket socket;
		
		System.out.println("Starting server...");
		
		while(nodes < 3) {
			socket = ssSharedSocket.accept();
			String ip = socket.getInetAddress().getHostAddress();
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			String clientName = dis.readUTF();
			
			System.out.println(clientName + " " + ip + " has connected to the server.");
			nodes++;
		}
	}
	
	public void start() throws ClassNotFoundException {
		
	}
}
