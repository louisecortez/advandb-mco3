package network;
import view.MainFrame;
import controller.Controller;
import model.Node;
 
import java.io.DataInputStream;
import java.io.DataOutputStream;
 import java.io.IOException;
import java.net.Socket;
 import java.net.ServerSocket;

public class Client {
	public static final String FIRST_NODE = "EuropeAmerica";
	public static final String SECOND_NODE = "AsiaAfrica";
	public static final String THIRD_NODE = "AllRegions";
	
	private ServerSocket servSocket; // shared socket
	private String server; // server IP address
	private String clientName;
	private String dbName;
	private int sharedPort = 1234; // change to shared port
	private int portNo;
	
	private MainFrame mainFrame;// gui per client
	private Controller controller;
	
	public Client(String server, String clientName, Controller controller) throws IOException {
				this.mainFrame = new MainFrame();
		 		this.server = server;
		 		this.clientName = clientName;
				this.controller = controller;
				
				servSocket = new ServerSocket(1237);
				servSocket.setSoTimeout(1500000);
				//portNo = 6790;
				
				new Thread(new NewThread()).start();
				Socket inputSocket = new Socket(server, sharedPort);
				DataOutputStream dos = new DataOutputStream(inputSocket.getOutputStream());
				dos.writeUTF(clientName);
				dos.close();
				inputSocket.close();
				
				mainFrame.setLabelNode(clientName + " (" + server + ")");
				controller.setClient(this);
				controller.setMainFrame(mainFrame);
				
				System.out.println("New node " + clientName + " with IP Address: " + server + " added to server.");
			}
			
			class NewThread implements Runnable {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					while(true) {
						try {
							Socket socket = servSocket.accept();
							DataInputStream dis = new DataInputStream(socket.getInputStream());
						} catch(IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
}
