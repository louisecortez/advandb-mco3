package controller;
import view.*;
import dbconnection.Connector;
import network.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Controller {
	private FrameIPAddress frameIP;
	private MainFrame frameMain;
	private Connector connector;
	
	private Server server;
	private Client client;
	
	public Controller() throws IOException {
		//this.server = new Server();
	}
	
	public Controller(FrameIPAddress frameIP) throws IOException {
		this.frameIP = frameIP;
		this.server = new Server();
	}
	
	public Controller (Server server) {
		this.server = server;
	}
	
	public Controller (Server server, Connector connector) {
		this.connector = connector;
	}
	
	public Controller(FrameIPAddress frameIP, Server server) throws IOException{
		this.frameIP = frameIP;
		this.server = server;
		//this.server.accept();
		frameIP.setController(this);
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
			connector = new Connector("wdidb", "1234");
			client.setConnector(connector);
			frameMain.setController(this);
			//frameIP.dispose();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void localRead(String table) throws SQLException {
		String query = "SELECT * FROM " + "wdidb.all_regions";
		ResultSet rs = connector.executeQuery(query);
		System.out.println("Query executed!");
		
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
}