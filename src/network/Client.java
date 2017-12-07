package network;
import view.MainFrame;
import controller.Controller;
import dbconnection.Connector;

import model.Entity;
import model.Node;
import java.io.DataInputStream;
import java.io.DataOutputStream;
 import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import com.sun.rowset.CachedRowSetImpl;

import java.net.ServerSocket;

public class Client {
	public static final String FIRST_NODE = "EuropeAmerica";
	public static final String SECOND_NODE = "AsiaAfrica";
	public static final String THIRD_NODE = "AllRegions";
	
	private ServerSocket servSocket; // shared socket
	private String server; // server IP address
	private String clientName;
	private String dbName;
	private int sharedPort = 12342; // change to shared port
	private int portNo;
	
	private volatile HashMap<String, ArrayList<Entity>> rsMap = new HashMap<>(); //results set of the query
	private volatile HashMap<String, Connection> connectionsMap = new HashMap<>(); 
	private volatile HashMap<String, String> writeMap = new HashMap<>();
	private volatile HashMap<String, Boolean> writeFinishMap = new HashMap<>();
	private CachedRowSetImpl rsw;
	private volatile int nRunningTransactions;
	private String password;					//password for yung sql nila
	private MainFrame mainFrame;// gui per client
	private Controller controller;
	
	public Client(String server, String clientName, Controller controller) throws IOException {
		
				this.mainFrame = new MainFrame();
		 		this.server = server;
		 		this.clientName = clientName;
				this.controller = controller;
				
				servSocket = new ServerSocket(1237);
				servSocket.setSoTimeout(150000);
				
				if(clientName.equals("EuropeAmerica")) {
					dbName = "europe_america";
					portNo = 12352;
					//servSocket = new ServerSocket(1237);
					//servSocket.setSoTimeout(150000);
				} else if(clientName.equals("AsiaAfrica")) {
					dbName = "asia_africa";
					portNo = 12353;
					//servSocket = new ServerSocket(1238);
					//servSocket.setSoTimeout(150000);
				} else if(clientName.equals("AllRegions")) {
					dbName = "all_regions";
					portNo = 12354;
					//servSocket = new ServerSocket(1239);
					//servSocket.setSoTimeout(150000);
				}
				
				new Thread(new NewThread()).start();
				Socket inputSocket = new Socket(server, sharedPort);
				DataOutputStream dos = new DataOutputStream(inputSocket.getOutputStream());
				dos.writeUTF(clientName);
				//dos.close();
				inputSocket.close();
				
				mainFrame.setLabelNode(clientName + " (" + server + ")");
				controller.setClient(this);
				controller.setMainFrame(mainFrame);
				
				System.out.println("New node " + clientName + " with IP Address: " + server + " added to server.");
				
				
			}
			
			public void setPassword(String password) {
				this.password = password;
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
			
			public void case1(ArrayList<String> transactions) throws Exception {
//				nRunningTransactions = 0;
				ArrayList<Thread> threads = new ArrayList<>();
				for(String cur: transactions){
					System.out.println("Running: "+cur);
					Thread t = new Thread(new TransactionThread(cur));
					threads.add(t);
					t.start();
				}
//				while(nRunningTransactions<transactions.size());
				for(Thread t: threads){
					t.join();
				}
				doneWriting();
			}
			
			class TransactionThread implements Runnable {
				private String cur;
				
				public TransactionThread(String t){
					cur = t;
				}
				
				
				
				@Override
				public void run() {
					try{
						String[] split = cur.split("@");
						if(split.length>=2 && split[1].startsWith("SELECT")){
							if(clientName.equals(split[2]) || "AllRegions".equals(clientName)){
								ResultSet rs = executeRead(split[1]);
								putIntoMap(split[3], rs);
							} else {
								Socket s = new Socket(server, sharedPort);
								DataOutputStream dout = new DataOutputStream(s.getOutputStream());
								dout.writeUTF(cur);
								dout.close();
								s.close();
								
								while(!rsMap.containsKey(split[3]));
								System.out.println("Received data!");
								s.close();
							}
						} else if(!"AllRegions".equals(split[0]) && split[1].startsWith("UPDATE")) {
							Connection connection;
							if(password == null) {
								connection = new Connector(dbName).getConnection();
							} else {
								connection = new Connector(dbName, password).getConnection();
							}
							Statement statement = null;
							int[] results = null;
							try {
								statement = connection.createStatement();
								statement.addBatch("Start transaction;");
								statement.addBatch(split[1]);
								results = statement.executeBatch();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							System.out.println("Finished Writing!");
							
							if(results[1]>=1){
								Socket sk = new Socket(server, sharedPort);
								DataOutputStream dos = new DataOutputStream(sk.getOutputStream());
								dos.writeUTF(cur+"@"+clientName);
								dos.close();
								sk.close();
								
								putIntoMap(split[2], connection);
								writeFinishMap.put(split[2], false);
							} else {
								System.out.println("Global write. Sending to server...");
								Socket sk = new Socket(server, sharedPort);
								DataOutputStream dos = new DataOutputStream(sk.getOutputStream());
								dos.writeUTF(cur+"@"+theOther(clientName));
								dos.close();
								sk.close();
								writeFinishMap.put(split[2], false);
							}
						} else {
							System.out.println("AllRegions update");
							Socket sk = new Socket(server, sharedPort);
							DataOutputStream dos = new DataOutputStream(sk.getOutputStream());
							dos.writeUTF(cur+"@Central");
							dos.close();
							sk.close();
							writeFinishMap.put(split[2], false);
						}
					} catch (Exception e){
						e.printStackTrace();
					}
					addTransaction();
				}

				private String theOther(String client) {
					switch(client){
						case "EuropeAmerica": return "AsiaAfrica";
						case "AsiaAfrica": return "EuropeAmerica";
					}
					return null;
				}
				
			}
			
			ResultSet executeRead(String query){
				Connection connection;
				if(password == null) {
					connection = new Connector(dbName).getConnection();
				} else {
					connection = new Connector(dbName, password).getConnection();
				}
				PreparedStatement statement = null;
				ResultSet resultSet = null;
				try {
					statement = connection.prepareStatement(query);
					resultSet = statement.executeQuery();
					return resultSet;
//					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return null;
			}
			
//			boolean executeWrite(String query){
//				try {
//					Connection connection = new DBManager(dbName).getConnection();
//					PreparedStatement statement = null;
//					statement = connection.prepareStatement(query);
//					int res = statement.executeUpdate();
//					connection.close();
//					return true;
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//				return false;
//			}
			
			class IncomingThread implements Runnable{

				@Override
				public void run() {
					while (true) {
						try {
							Socket s = servSocket.accept();
							DataInputStream din = new DataInputStream(s.getInputStream());
							try {
								String msgin = din.readUTF();
								System.out.println("Received " + msgin);
								String[] split = msgin.split("@");
								if ("CentralWrite".equals(split[0])) {
									System.out.println("Writing to AllRegions");
									Connection connection;
									if (password == null) {
										connection = new Connector(dbName).getConnection();
									} else {
										connection = new Connector(dbName, password).getConnection();
									}
									Statement stmt = connection.createStatement();
									int res = stmt.executeUpdate(split[1]);
									if (res > 0) {
										Socket sk = new Socket(server, portNo);
										DataOutputStream dos = new DataOutputStream(sk.getOutputStream());
										dos.writeUTF("OK");
										dos.close();
										sk.close();
									} else {
										Socket sk = new Socket(server, portNo);
										DataOutputStream dos = new DataOutputStream(sk.getOutputStream());
										dos.writeUTF("GG");
										dos.close();
										sk.close();
			    					}
			                    }else if(split[0].equals("Unable to read")){
			                    	putNullResult(split[1]);
			                    } else if(split[0].startsWith("Sending data")){
			                    	System.out.println("Receiving data...");
			                    	s = servSocket.accept();
			                    	ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
									try {
											rsw = (CachedRowSetImpl) ois.readObject();
											ResultSet rs = rsw.getOriginal();
											putIntoMap(split[1], rs);
									} catch (ClassNotFoundException e1) {
										e1.printStackTrace();
									}
									System.out.println("Unlocked Result Set");
			                    } else if("Merge".equals(split[0])){
			                    	System.out.println("Receiving data...");
			                    	s = servSocket.accept();
			                    	ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
									try {
											rsw = (CachedRowSetImpl) ois.readObject();
											ResultSet rs = rsw.getOriginal();
											putIntoMap(split[4], rs);
									} catch (ClassNotFoundException e1) {
										e1.printStackTrace();
									}
									System.out.println("Unlocked Result Set");
			                    	
									ResultSet rs = null;
									System.out.println(split[2]);
			                    	if(split[2].contains("asia_africa")) {
			                    		rs = executeRead(split[2].replaceAll("asia_africa", "europe_america"));
			                    	} else if(split[2].contains("europe_america")) {
			                    		rs = executeRead(split[2].replaceAll("europe_america", "asia_africa"));
			                    	}
									ArrayList<Entity> entities = new ArrayList<Entity>();
									entities.addAll(rsMap.get(split[4]));
									
									try {
										while(rs.next()){
											Entity cur = new Entity(
													rs.getString(1),
													rs.getString(2),
													rs.getString(3),
													rs.getString(4),
													rs.getString(5),
													rs.getInt(6),
													rs.getInt(7));
											entities.add(cur);
										}
										System.out.println("MAY RESULTSSSS");
									} catch (SQLException e1) {
										e1.printStackTrace();
									}
									
									rsMap.put(split[4], entities);
									System.out.println(getById(split[4]).size());
									System.out.println(getById(split[4]));
			                    } else if (split[0].startsWith("OK")){
			                    	Connection c = connectionsMap.get(split[1]);
			                    	c.createStatement().execute("commit;");
			                    	c.close();
			                    	connectionsMap.remove(split[1]);
			                    	writeMap.put(split[1], "Update successful!");
			                    	writeFinishMap.put(split[1], true);
			                    	doneWriting();
			                    } else if (split[0].startsWith("GG")){
			                    	Connection c = connectionsMap.get(split[1]);
			                    	c.createStatement().execute("rollback;");
			                    	c.close();
			                    	connectionsMap.remove(split[1]);
			                    	writeMap.put(split[1], "Update failed!");
			                    	writeFinishMap.put(split[1], true);
			                    	doneWriting();
			                    } else {
				                    if(split[1].startsWith("SELECT")){
				                    	ResultSet rs = executeRead(split[1]);
				                    	CachedRowSetImpl rsw = new CachedRowSetImpl();
				                    	rsw.populate(rs);
				                    	
				                    	Socket data = new Socket(server, portNo);
										ObjectOutputStream oos = new ObjectOutputStream(data.getOutputStream());
										oos.writeObject(rsw);
										oos.close();
										data.close();
				                    } else { 
				                    	System.out.println("Starting write");
				                    	boolean success = true;
				                    	Connection connection;
				    					if(password == null) {
				    						connection = new Connector(dbName).getConnection();
				    					} else {
				    						connection = new Connector(dbName, password).getConnection();
				    					}
				    					Statement statement = connection.createStatement();
				    					int[] results = null;
				    					try {
				    						statement.addBatch("Start transaction;");
				    						statement.addBatch(split[1]);
				    						results = statement.executeBatch();
				    					} catch (SQLException e) {
				    						e.printStackTrace();
				    						success = false;
				    					}
				    					if("Central".equals(split[0])){
				    						if(success){
				    							System.out.println("Transaction success!");
					                    		Socket sk = new Socket(server, portNo);
					                    		DataOutputStream dos = new DataOutputStream(sk.getOutputStream());
					                    		dos.writeUTF(results[1]+"");
					                    		dos.close();
					                    		sk.close();
					                    		if(results[1]==0){
					                    			split[4] = "auto";
					                    		}
				    						} else {
				    							System.out.println("Transaction fail!");
					                    		Socket sk = new Socket(server, portNo);
					                    		DataOutputStream dos = new DataOutputStream(sk.getOutputStream());
					                    		dos.writeUTF("0");
					                    		dos.close();
					                    		sk.close();
				    						}
				    					}
				    					else {
					                    	if(success){
					                    		System.out.println("Transaction success!");
					                    		Socket sk = new Socket(server, portNo);
					                    		DataOutputStream dos = new DataOutputStream(sk.getOutputStream());
					                    		dos.writeUTF("OK");
					                    		dos.close();
					                    		sk.close();
					                    	} else {
					                    		System.out.println("Transaction fail!");
					                    		Socket sk = new Socket(server, portNo);
					                    		DataOutputStream dos = new DataOutputStream(sk.getOutputStream());
					                    		dos.writeUTF("GG");
					                    		dos.close();
					                    		sk.close();
					                    	}
				    					}
				                    	
				                    	if("dontauto".equals(split[4])){
				                    		Socket skt = servSocket.accept();
				                    		DataInputStream dis = new DataInputStream(skt.getInputStream());
				                    		String result = dis.readUTF();
				                    		System.out.println("Received commit command "+result);
				                    		dis.close();
				                    		skt.close();
				                    		
				                    		if("Commit".equals(result)){
				                    			System.out.println("Committing...");
					                    		statement.execute("commit;");
				                    		} else {
				                    			System.out.println("Rolling back...");
				                    			statement.execute("rollback;");
				                    		}
				                    	} else {
				                    		statement.execute("commit;");
				                    	}
				                    	System.out.println("Closing connection...");
				                    	connection.close();
				                    }
			                    }
							} catch(Exception e){
								e.printStackTrace();
							}
							s.close();
		                	}catch(Exception e){
		                	}
							
		                } 
				}
				
				
			}
			
			private synchronized void putNullResult(String id) {
				rsMap.put(id, null);
			}
			
			private synchronized void putIntoMap(String id, ResultSet rs){
				ArrayList<Entity> e = new ArrayList<>();
				try {
					while(rs.next()){
						Entity cur = new Entity(
								rs.getString(1),
								rs.getString(2),
								rs.getString(3),
								rs.getString(4),
								rs.getString(5),
								rs.getInt(6),
								rs.getInt(7));
								
						e.add(cur);
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				rsMap.put(id, e);
			}
			
			public synchronized void putIntoMap(String id, Connection c){
				connectionsMap.put(id, c);
			}
			
			public synchronized void addTransaction(){
				nRunningTransactions++;
			}
			
			public ArrayList<Entity> getById(String id){
				return rsMap.get(id);
			}
			
			public String getWriteStatusById(String id) {
				return writeMap.get(id);
			}
			
			public void sendCrashMessage() throws UnknownHostException, IOException {
				Socket sk = new Socket(server, sharedPort);
				DataOutputStream dos = new DataOutputStream(sk.getOutputStream());
				dos.writeUTF(clientName + " has died.");
				dos.close();
				sk.close();
			}

			public void doneWriting() {
				boolean doneWriting = true;
				for(String key : writeFinishMap.keySet()) {
					if(!writeFinishMap.get(key)) {
						doneWriting = false;
						break;
					}
				}
				if(doneWriting) {
					for(String key : writeFinishMap.keySet()) {
						writeFinishMap.remove(key);
					}
					//mainFrame.enableComboBox();
				}
			}

}
