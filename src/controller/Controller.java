package controller;
import view.*;
import dbconnection.Connector;
import network.*;
import transaction.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Controller {
	private FrameIPAddress frameIP;
	private MainFrame frameMain;
	private Connector connector;
	
	private Server server;
	private Client client;
	
	private String region;
	private ArrayList<Transaction> transQueue;
	
	public Controller() throws IOException {
		//this.server = new Server();
		transQueue = new ArrayList<Transaction>();
	}
	
	public Controller(FrameIPAddress frameIP) throws IOException {
		this.frameIP = frameIP;
		this.server = new Server();
		transQueue = new ArrayList<Transaction>();
	}
	
	public Controller (Server server) {
		this.server = server;
		transQueue = new ArrayList<Transaction>();
	}
	
	public Controller (Server server, Connector connector) {
		this.connector = connector;
		transQueue = new ArrayList<Transaction>();
	}
	
	public Controller(FrameIPAddress frameIP, Server server) throws IOException{
		this.frameIP = frameIP;
		this.server = server;
		//this.server.accept();
		frameIP.setController(this);
		transQueue = new ArrayList<Transaction>();
	}
	
	public Server getServer() {
		return server;
	}
	
	public void setMainFrame(MainFrame frameMain) {
		this.frameMain = frameMain;
	}
	
	public void setClient(Client client) {
		this.client = client;
	}
	
	public void initializeNode(String ipAddress, String region) {
		try {
			client = new Client(ipAddress, region, this);
			this.region = region;
			connector = new Connector("dbmco3", "1234");
			client.setConnector(connector);
			frameMain.setController(this);
			//frameIP.dispose();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addReadQuery(String table, String type) {
		String fromTable = null;
		
		if(type.equals("Local")) {
			fromTable = region;
		} else if(type.equals(Client.FIRST_NODE)) {
			fromTable = Client.FIRST_NODE;
		} else if(type.equals(Client.SECOND_NODE)) {
			fromTable = Client.SECOND_NODE;
		} else if(type.equals(Client.THIRD_NODE)) {
			fromTable = Client.THIRD_NODE;
		}
		
		String query = QueryGenerator.generateRead(table, fromTable);
		transQueue.add(new ReadTransaction(query, fromTable));
	}
	
	public void addWriteQuery(String table, String type, String writeType, String country, String series, 
			String year, String data) {		
		String query = null;
		int nYear = Integer.parseInt(year);
		double nData = Double.parseDouble(data);
		
		if(writeType.equals("Update")) {
			query = QueryGenerator.generateUpdate(table, country, series, nYear, nData);
		} else if(writeType.equals("Insert")) {
			query = QueryGenerator.generateInsert(table, country, series, nYear, nData);
		} else if(writeType.equals("Delete")) {
			query = QueryGenerator.generateDelete(table, country, series, nYear, nData);
		}
		
		if(type.equals("Local")) {
			transQueue.add(new WriteTransaction(query, region));
		} else {
			transQueue.add(new WriteTransaction(query, region));
			if(region.equals(Client.FIRST_NODE)) {
				transQueue.add(new WriteTransaction(query, Client.SECOND_NODE));
				transQueue.add(new WriteTransaction(query, Client.THIRD_NODE));
			} else if(region.equals(Client.SECOND_NODE)) {
				transQueue.add(new WriteTransaction(query, Client.FIRST_NODE));
				transQueue.add(new WriteTransaction(query, Client.THIRD_NODE));
			} else if(region.equals(Client.THIRD_NODE)) {
				transQueue.add(new WriteTransaction(query, Client.FIRST_NODE));
				transQueue.add(new WriteTransaction(query, Client.SECOND_NODE));
			}
		}
	}
	
	public void executeRead(String query) throws SQLException {
		ResultSet rs = connector.executeQuery(query);
		System.out.println("Query Read");
		
		frameMain.clearAllRows();
		
		while(rs.next()) {
			String cc = rs.getString("CountryCode");
			String cn = rs.getString("CountryName");
			String r = rs.getString("Region");
			String sc = rs.getString("SeriesCode");
			String sn = rs.getString("SeriesName");
			int y = rs.getInt("YearC");
			double d = rs.getDouble("Data");
			
			frameMain.addTableRow(new Object[] {cc, cn, r, sc, sn, y, d});
		}
	}
	
	public void executeWrite(String query) throws SQLException {
		System.out.println("Query Write");
		connector.executeStatement(query);
	}
	
	public void executeQueries() {
		String fromTable = null;
		
		// concurrency lock shiz here
		
		for(int i = 0; i < transQueue.size(); i++) {
			if(transQueue.get(i).getTransType().equals("Local")) {
				connector.setURL(client.getIP());
			} else if(transQueue.get(i).getTransType().equals(Client.FIRST_NODE)) {
				connector.setURL(server.getIpEuropeAmerica());
			} else if(transQueue.get(i).getTransType().equals(Client.SECOND_NODE)) {
				connector.setURL(server.getIpAsiaAfrica());
			} else if(transQueue.get(i).getTransType().equals(Client.THIRD_NODE)) {
				connector.setURL(server.getIpAllRegions());
			}
			
			if(transQueue.get(i) instanceof ReadTransaction) {
				try {
					executeRead(transQueue.get(i).getTransaction());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if(transQueue.get(i) instanceof WriteTransaction) {
				try {
					executeWrite(transQueue.get(i).getTransaction());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		transQueue = new ArrayList<Transaction>();
	}
}