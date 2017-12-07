package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.sun.rowset.CachedRowSetImpl;

import network.Client;
 
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
		//server.accept();
		server.start();
		server.serve();
	}
	
	
	public Server() throws IOException {
		ssSharedSocket = new ServerSocket(12342);
		ssEuropeAmerica = new ServerSocket(12352);
		ssAsiaAfrica = new ServerSocket(12353);
		ssAllRegions = new ServerSocket(12354);
		
		ssSharedSocket.setSoTimeout(20000);
		ssEuropeAmerica.setSoTimeout(20000);
		ssAsiaAfrica.setSoTimeout(20000);
		ssAllRegions.setSoTimeout(20000);
	}
	
	public void start() throws IOException {
		// accept up to 3 nodes for the distributed database
		int nodes = 0;
		Socket socket;
		
		System.out.println("Starting server...");
		
		while(nodes < 3) {
			socket = ssSharedSocket.accept();
			String ip = socket.getInetAddress().getHostAddress();
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			String clientName = dis.readUTF();
			
			switch(clientName){
			case "EuropeAmerica":
				ipEuropeAmerica = ip;
				System.out.println("EuropeAmerica connected!");
				nodes++;
				break;
			case "AsiaAfrica":
				ipAsiaAfrica = ip;
				System.out.println("AsiaAfrica connected!");
				nodes++;
				break;
			case "AllRegions":
				ipAllRegions = ip;
				System.out.println("AllRegions connected!");
				nodes++;
				break;
			}
			System.out.println(clientName + " " + ip + " has connected to the server.");
			
		}
	}
	
	public void serve() throws ClassNotFoundException {
		Socket curr;
		while(true) {
			try {
				curr = ssSharedSocket.accept();
				DataInputStream dis = new DataInputStream(curr.getInputStream());
				String message = dis.readUTF();
				System.out.println("GOT " + message);
				if(message.equals("EuropeAmerica")) {
					ipEuropeAmerica = curr.getInetAddress().getHostAddress();
					System.out.println("EuropeAmerica connected!");
				} else if(message.equals("AsiaAfrica")) {
					ipAsiaAfrica = curr.getInetAddress().getHostAddress();
					System.out.println("ipAsiaAfrica connected!");
				} else if(message.equals("AllRegions")) {
					ipAllRegions = curr.getInetAddress().getHostAddress();
					System.out.println("Central connected!");
				} else if(message.contains("has died")) {
					if(message.startsWith("EuropeAmerica")) {
						ipEuropeAmerica = null;
					} else if(message.startsWith("AsiaAfrica")) {
						ipAsiaAfrica = null;
					} else if(message.startsWith("AllRegions")) {
						ipAllRegions = null;
					}
				} else {
					String[] split = message.split("@");
					switch(split[0]){
						case "EuropeAmerica":
							if(split[1].startsWith("SELECT")){
								boolean retrieveSuccess = false;
								if (ipAllRegions != null){ 
									// send a request for data to central
									Socket data = new Socket(ipAllRegions, 12352);
									DataOutputStream dos = new DataOutputStream(data.getOutputStream());
									dos.writeUTF(message);
									dos.close();
									data.close();
									
									// wait for the data
									try{
										System.out.println("Waiting for data from AllRegions...");
										data = ssAllRegions.accept();
										ObjectInputStream ois = new ObjectInputStream(data.getInputStream());
										CachedRowSetImpl rsw = (CachedRowSetImpl) ois.readObject();
										ois.close();
										data.close();
										
										// send data back to Palawan
										System.out.println("Got data! Sending back to requester...");
										data = new Socket(ipEuropeAmerica, 12352);
										dos = new DataOutputStream(data.getOutputStream());
										dos.writeUTF("Sending data@"+split[3]);
										dos.close();
										data.close();
										data = new Socket(ipEuropeAmerica, 12352);
										ObjectOutputStream oos = new ObjectOutputStream(data.getOutputStream());
										oos.writeObject(rsw);
										oos.close();
										data.close();
										retrieveSuccess = true;
									} catch (Exception e){
										System.out.println("Timed out. Attempting to retrieve data from AsiaAfrica...");
									}
								}
								
								if(!retrieveSuccess && ipAsiaAfrica != null){ 
									// send a request for data to AsiaAfrica
									Socket data = new Socket(ipAsiaAfrica, 12352);
									DataOutputStream dos = new DataOutputStream(data.getOutputStream());
									message = message.replaceAll("AllRegions", "AsiaAfrica");
									dos.writeUTF(message);
									dos.close();
									data.close();
									
									if("AsiaAfrica".equals(split[2])){
										// wait for the data
										try{
											System.out.println("Waiting for data...");
											data = ssAsiaAfrica.accept();
											ObjectInputStream ois = new ObjectInputStream(data.getInputStream());
											CachedRowSetImpl rsw = (CachedRowSetImpl) ois.readObject();
											ois.close();
											data.close();
											
											// send data back to EuropeAmerica
											System.out.println("Got data! Sending back to requester...");
											data = new Socket(ipEuropeAmerica, 12352);
											dos = new DataOutputStream(data.getOutputStream());
											dos.writeUTF("Sending data@"+split[3]);
											dos.close();
											data.close();
											data = new Socket(ipEuropeAmerica, 12352);
											ObjectOutputStream oos = new ObjectOutputStream(data.getOutputStream());
											oos.writeObject(rsw);
											oos.close();
											data.close();
											retrieveSuccess = true;
										} catch(Exception e){
											System.out.println("Timed out waiting for data from AsiaAfrica. Unable to read");
										}
									} else {
										// wait for the data
										try{
											System.out.println("Waiting for data...");
											data = ssAsiaAfrica.accept();
											//System.out.println("Accepted the data!");
											ObjectInputStream ois = new ObjectInputStream(data.getInputStream());
											CachedRowSetImpl rsw = (CachedRowSetImpl) ois.readObject();
											ois.close();
											data.close();
											
											// send data back to EuropeAmerica
											System.out.println("Got data! Sending back to requester...");
											data = new Socket(ipEuropeAmerica, 12352);
											dos = new DataOutputStream(data.getOutputStream());
											dos.writeUTF("Merge@"+message);
											dos.close();
											data.close();
											data = new Socket(ipEuropeAmerica, 12352);
											ObjectOutputStream oos = new ObjectOutputStream(data.getOutputStream());
											oos.writeObject(rsw);
											oos.close();
											data.close();
											retrieveSuccess = true;
										} catch(Exception e){
											System.out.println("Unable to read");
										}
									}
								} 
								if(!retrieveSuccess){ // both are dead
									System.out.println("Failed to retrieve from both sources.");
									Socket data = new Socket(ipEuropeAmerica, 12352);
									DataOutputStream dos = new DataOutputStream(data.getOutputStream());
									dos.writeUTF("Unable to read@"+split[3]);
									dos.close();
									data.close();
								}
							// code for writing
							} else if(split[1].startsWith("UPDATE")) {
								if(ipAllRegions != null){
									if("EuropeAmerica".equals(split[3])){
										System.out.println("Local write (EuropeAmerica)");
										// send update request to central 
										System.out.println("Sending request to AllRegions...");
										Socket data = new Socket(ipAllRegions, 12352);
										DataOutputStream dos = new DataOutputStream(data.getOutputStream());
										dos.writeUTF(message+"@auto");
										dos.close();
										data.close();
										
										// receive confirmation from central
										System.out.println("Waiting for confirmation from AllRegions...");
										data = ssAllRegions.accept();
										DataInputStream din = new DataInputStream(data.getInputStream());
										String ok = din.readUTF();
										din.close();
										data.close();
										
										// send ok to EuropeAmerica
										System.out.println("Received confirmation: "+ok);
										data = new Socket(ipEuropeAmerica, 12352);
										dos = new DataOutputStream(data.getOutputStream());
										dos.writeUTF(ok+"@"+split[2]);
										dos.close();
										data.close();
									} else {
										System.out.println("Global write (EuropeAmerica)");
										if(ipAllRegions!=null && ipAsiaAfrica!=null){
											String centralOk = "";
											String marinOk = "";
											
											try{
												// send update request to central 
												System.out.println("Sending request to AllRegions...");
												Socket data = new Socket(ipAllRegions, 12352);
												DataOutputStream dos = new DataOutputStream(data.getOutputStream());
												dos.writeUTF(message+"@dontauto");
												dos.close();
												data.close();
												
												// receive confirmation from central
												System.out.println("Waiting for confirmation from central...");
												data = ssAllRegions.accept();
												DataInputStream din = new DataInputStream(data.getInputStream());
												centralOk = din.readUTF();
												din.close();
												data.close();
											} catch(Exception e){
												System.out.println("Failed to write to AllRegions");
												centralOk = "fail";
											}
											
											if(!"fail".equals(centralOk)){
												try{
												// send update request to AsiaAfrica 
												System.out.println("Sending request to AsiaAfrica...");
												Socket data = new Socket(ipAsiaAfrica, 12352);
												DataOutputStream dos = new DataOutputStream(data.getOutputStream());
												dos.writeUTF(message+"@dontauto");
												dos.close();
												data.close();
												
												// receive confirmation from marinduque
												System.out.println("Waiting for confirmation from AsiaAfrica...");
												data = ssAsiaAfrica.accept();
												DataInputStream din = new DataInputStream(data.getInputStream());
												marinOk = din.readUTF();
												din.close();
												data.close();
												} catch(Exception e){
													System.out.println("Failed to write to AsiaAfrica");
													marinOk = "fail";
												}
											}
											String commitOrNot = "Rollback";
											System.out.println(centralOk);
											System.out.println(marinOk);
											if("OK".equals(centralOk) && "OK".equals(marinOk)){
												commitOrNot = "Commit";
											}
											if("OK".equals(centralOk)){
												// tell central to commit
												System.out.println("Sending commit command to AsiaAfrica...");
												Socket data = new Socket(ipAllRegions, 12352);
												DataOutputStream dos = new DataOutputStream(data.getOutputStream());
												dos.writeUTF(commitOrNot);
												dos.close();
												data.close();
											}
											
											if("OK".equals(marinOk)){
												// tell marinduque to commit
												System.out.println("Sending commit command to AsiaAfrica...");
												Socket data = new Socket(ipAsiaAfrica, 12352);
												DataOutputStream dos = new DataOutputStream(data.getOutputStream());
												dos.writeUTF(commitOrNot);
												dos.close();
												data.close();
											}
										}
										
									}
								} else { // central is dead
									Socket data = new Socket(ipEuropeAmerica, 12352);
									DataOutputStream dos = new DataOutputStream(data.getOutputStream());
									dos.writeUTF("GG@"+split[2]);
									dos.close();
									data.close();
								}
							}
							break;
						case "AsiaAfrica":
							if(split[1].startsWith("SELECT")){
								boolean retrieveSuccess = false;
								if (ipAllRegions != null){ 
									// send a request for data from Central
									Socket data = new Socket(ipAllRegions, 12352);
									DataOutputStream dos = new DataOutputStream(data.getOutputStream());
									dos.writeUTF(message);
									dos.close();
									data.close();
									
									// wait for the data
									try{
										System.out.println("Waiting for data from AllRegions...");
										data = ssAllRegions.accept();
										ObjectInputStream ois = new ObjectInputStream(data.getInputStream());
										CachedRowSetImpl rsw = (CachedRowSetImpl) ois.readObject();
										ois.close();
										data.close();
										
										// send data back to Marinduque
										System.out.println("Got data! Sending back to requester...");
										data = new Socket(ipAsiaAfrica, 12352);
										dos = new DataOutputStream(data.getOutputStream());
										dos.writeUTF("Sending data@"+split[3]);
										dos.close();
										data.close();
										data = new Socket(ipAsiaAfrica, 12352);
										ObjectOutputStream oos = new ObjectOutputStream(data.getOutputStream());
										oos.writeObject(rsw);
										oos.close();
										data.close();
										retrieveSuccess = true;
									} catch (Exception e){
										System.out.println("Timed out waiting for data from EuropeAmerica. Unable to read");
									}
								}
								if(!retrieveSuccess && ipEuropeAmerica != null){ 
									// send a request for data from Palawan
									Socket data = new Socket(ipEuropeAmerica, 12352);
									DataOutputStream dos = new DataOutputStream(data.getOutputStream());
									message = message.replaceAll("all_regions", "europe_america");
									dos.writeUTF(message);
									dos.close();
									data.close();
									
									if("EuropeAmerica".equals(split[2])) {
										// wait for the data
										try{
											System.out.println("Waiting for data...");
											data = ssEuropeAmerica.accept();
											ObjectInputStream ois = new ObjectInputStream(data.getInputStream());
											CachedRowSetImpl rsw = (CachedRowSetImpl) ois.readObject();
											ois.close();
											data.close();
											
											// send data back to Marinduque
											System.out.println("Got data! Sending back to requester...");
											data = new Socket(ipAsiaAfrica, 12352);
											dos = new DataOutputStream(data.getOutputStream());
											dos.writeUTF("Sending data@"+split[3]);
											dos.close();
											data.close();
											data = new Socket(ipAsiaAfrica, 12352);
											ObjectOutputStream oos = new ObjectOutputStream(data.getOutputStream());
											oos.writeObject(rsw);
											oos.close();
											data.close();
											retrieveSuccess = true;
										} catch(Exception e){
											System.out.println("Timed out waiting for data from AsiaAfrica. Unable to read");
										}
									} else {
										// wait for the data
										try{
											System.out.println("Waiting for data...");
											data = ssEuropeAmerica.accept();
											//System.out.println("Accepted the data!");
											ObjectInputStream ois = new ObjectInputStream(data.getInputStream());
											CachedRowSetImpl rsw = (CachedRowSetImpl) ois.readObject();
											ois.close();
											data.close();
											
											// send data back to AsiaAfrica
											System.out.println("Got data! Sending back to requester...");
											data = new Socket(ipAsiaAfrica, 12352);
											dos = new DataOutputStream(data.getOutputStream());
											dos.writeUTF("Merge@"+message);
											dos.close();
											data.close();
											data = new Socket(ipAsiaAfrica, 12352);
											ObjectOutputStream oos = new ObjectOutputStream(data.getOutputStream());
											oos.writeObject(rsw);
											oos.close();
											data.close();
											retrieveSuccess = true;
										} catch(Exception e){
											System.out.println("Unable to read");
										}
									}
								} 
								if(!retrieveSuccess){ // both are dead
									System.out.println("Failed to retrieve from both sources.");
									Socket data = new Socket(ipAsiaAfrica, 12352);
									DataOutputStream dos = new DataOutputStream(data.getOutputStream());
									dos.writeUTF("Unable to read@"+split[3]);
									dos.close();
									data.close();
								}
							// code for writing
							} else if(split[1].startsWith("UPDATE")) {
								if(ipAllRegions != null){
									if("AsiaAfrica".equals(split[3])){
										System.out.println("Local write (AsiaAfrica)");
										// send update request to central 
										System.out.println("Sending request to AllRegions...");
										Socket data = new Socket(ipAllRegions, 12352);
										DataOutputStream dos = new DataOutputStream(data.getOutputStream());
										dos.writeUTF(message+"@auto");
										dos.close();
										data.close();
										
										// receive confirmation from central
										System.out.println("Waiting for confirmation from AllRegions...");
										data = ssAllRegions.accept();
										DataInputStream din = new DataInputStream(data.getInputStream());
										String ok = din.readUTF();
										din.close();
										data.close();
										
										// send ok to Marinduque
										System.out.println("Received confirmation: "+ok);
										data = new Socket(ipAsiaAfrica, 12352);
										dos = new DataOutputStream(data.getOutputStream());
										dos.writeUTF(ok+"@"+split[2]);
										dos.close();
										data.close();
									} else {
										System.out.println("Global write (AsiaAfrica)");
										if(ipAllRegions!=null && ipEuropeAmerica!=null){
											String centralOk = "";
											String palawOk = "";
											
											try{
												// send update request to central 
												System.out.println("Sending request to AllRegions...");
												Socket data = new Socket(ipAllRegions, 12352);
												DataOutputStream dos = new DataOutputStream(data.getOutputStream());
												dos.writeUTF(message+"@dontauto");
												dos.close();
												data.close();
												
												// receive confirmation from central
												System.out.println("Waiting for confirmation from AllRegions...");
												data = ssAllRegions.accept();
												DataInputStream din = new DataInputStream(data.getInputStream());
												centralOk = din.readUTF();
												din.close();
												data.close();
											} catch(Exception e){
												System.out.println("Failed to write to AllRegions");
												centralOk = "fail";
											}
											
											if(!"fail".equals(centralOk)){
												try{
												// send update request to Palawan
												System.out.println("Sending request to EuropeAmerica...");
												Socket data = new Socket(ipEuropeAmerica, 12352);
												DataOutputStream dos = new DataOutputStream(data.getOutputStream());
												dos.writeUTF(message+"@dontauto");
												dos.close();
												data.close();
												
												// receive confirmation from Palawan
												System.out.println("Waiting for confirmation from EuropeAmerica...");
												data = ssEuropeAmerica.accept();
												DataInputStream din = new DataInputStream(data.getInputStream());
												palawOk = din.readUTF();
												din.close();
												data.close();
												} catch(Exception e){
													System.out.println("Failed to write to EuropeAmerica");
													palawOk = "fail";
												}
											}
											String commitOrNot = "Rollback";
											System.out.println(centralOk);
											System.out.println(palawOk);
											if("OK".equals(centralOk) && "OK".equals(palawOk)){
												commitOrNot = "Commit";
											}
											if("OK".equals(centralOk)){
												// tell central to commit
												System.out.println("Sending commit command to central...");
												Socket data = new Socket(ipAllRegions, 12352);
												DataOutputStream dos = new DataOutputStream(data.getOutputStream());
												dos.writeUTF(commitOrNot);
												dos.close();
												data.close();
											}
											
											if("OK".equals(palawOk)){
												// tell Palawan to commit
												System.out.println("Sending commit command to EuropeAmerica...");
												Socket data = new Socket(ipEuropeAmerica, 12352);
												DataOutputStream dos = new DataOutputStream(data.getOutputStream());
												dos.writeUTF(commitOrNot);
												dos.close();
												data.close();
											}
										}
										
									}
								} else { // central is dead
									Socket data = new Socket(ipEuropeAmerica, 12352);
									DataOutputStream dos = new DataOutputStream(data.getOutputStream());
									dos.writeUTF("GG@"+split[2]);
									dos.close();
									data.close();
								}
							}
							break;
						case "AllRegions":
							int nRowsP = 0;
							int nRowsM = 0;
							
							// attempt to update Palawan
							try {
								// send update request to palawan
								System.out.println("Sending request to Palawan...");
								Socket data = new Socket(ipEuropeAmerica, 12352);
								DataOutputStream dos = new DataOutputStream(data.getOutputStream());
								dos.writeUTF(message+"@dontauto");
								dos.close();
								data.close();
								
								// get rowcount from Palawan
								System.out.println("Waiting for rowcount from EuropeAmerica...");
								data = ssEuropeAmerica.accept();
								DataInputStream din = new DataInputStream(data.getInputStream());
								nRowsP = Integer.parseInt(din.readUTF());
								din.close();
								data.close();
							} catch(Exception e){
								System.out.println("EuropeAmerica timed out.");
							}
							
							if(nRowsP==0){
								// attempt to update Marinduque
								try {
									// send update request to Marinduque
									System.out.println("Sending request to AsiaAfrica...");
									Socket data = new Socket(ipAsiaAfrica, 12352);
									DataOutputStream dos = new DataOutputStream(data.getOutputStream());
									dos.writeUTF(message+"@dontauto");
									dos.close();
									data.close();
									
									// get rowcount from Marinduque
									System.out.println("Waiting for rowcount from AsiaAfrica...");
									data = ssAsiaAfrica.accept();
									DataInputStream din = new DataInputStream(data.getInputStream());
									nRowsM = Integer.parseInt(din.readUTF());
									din.close();
									data.close();
								} catch(Exception e){
									System.out.println("AsiaAfrica timed out.");
								}
							}
							
							if(nRowsP!=0){
								// tell central to write
								System.out.println("Writing to AllRegions...");
								String m = "CentralWrite@"+split[1]+"@"+split[2];
								Socket s = new Socket(ipAllRegions, 12352);
								DataOutputStream dout = new DataOutputStream(s.getOutputStream());
								dout.writeUTF(m);
								dout.close();
								s.close();
								
								// wait for response
								s = ssAllRegions.accept();
								dis = new DataInputStream(s.getInputStream());
								String res = dis.readUTF();
								dis.close();
								s.close();
								
								// send response back to Palawan
								if("OK".equals(res)){
									s = new Socket(ipEuropeAmerica, 12352);
									dout = new DataOutputStream(s.getOutputStream());
									dout.writeUTF("Commit");
									dout.close();
									s.close();
								}
							} else if(nRowsM!=0){
								// tell central to write
								System.out.println("Writing to AllRegions...");
								String m = "CentralWrite@"+split[1]+"@"+split[2];
								Socket s = new Socket(ipAllRegions, 12352);
								DataOutputStream dout = new DataOutputStream(s.getOutputStream());
								dout.writeUTF(m);
								dout.close();
								s.close();
								
								// wait for response
								s = ssAllRegions.accept();
								dis = new DataInputStream(s.getInputStream());
								String res = dis.readUTF();
								dis.close();
								s.close();
								
								// send response back to Marinduque
								if("OK".equals(res)){
									s = new Socket(ipAsiaAfrica, 12352);
									dout = new DataOutputStream(s.getOutputStream());
									dout.writeUTF("Commit");
									dout.close();
									s.close();
								}
							}
							break;
					}
				}
			} catch( IOException ioe ) {
				ioe.printStackTrace();
			}
		}
	}
	
	
}
