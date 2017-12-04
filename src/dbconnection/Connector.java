package dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;



public class Connector {
	private static String url;
	private static String schema;
	private static String driver;
	private static String username;
	private static String password;
	
	public Connector(String url, String schema, String driver, String username, String password) {
		this.url = url;
		this.schema = schema;
		this.driver = driver;
		this.username = username;
		this.password = password;
	}
	
	public static Connection getConnection() {
		Connection connection = null;
		
		try {
			Class.forName(driver).newInstance();
			connection = DriverManager.getConnection(url + schema, username, password);
			return connection;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return connection;
	}
	
	public static void executeStatement(String statement) {
		Connection connection = getConnection();
		
		try {
			PreparedStatement pst = connection.prepareStatement(statement);
			pst.execute(statement);
			connection.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ResultSet executeQuery(String query) {
		ResultSet result = null;
		Connection connection = Connector.getConnection();
		
		try {
			Statement st = connection.createStatement();
			result = st.executeQuery(query);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
