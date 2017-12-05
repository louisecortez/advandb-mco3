package network;
import view.MainFrame;

import java.io.IOException;
import java.net.ServerSocket;

public class Client {
	public static final String FIRST_NODE = "EuropeAmerica";
	public static final String SECOND_NODE = "AsiaAfrica";
	public static final String THIRD_NODE = "AllRegions";
	
	private ServerSocket socket;
	private String server; // server IP address
	private String clientName;
	private String dbName;
	private int sharedPort = 1234; // change to shared port
	private int portNo;
	
	private MainFrame mainFrame;// gui per client
	
	public Client(MainFrame mainFrame, String server, String clientName) throws IOException {
		this.mainFrame = mainFrame;
		this.server = server;
		this.clientName = clientName;
		
		socket = new ServerSocket(1234); // change port number
		socket.setSoTimeout(10000); // 10 secon
	}
}
