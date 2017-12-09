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
	
	public Connector(String dbName) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		this.driver = "com.mysql.jdbc.Connector"; //com.mysql.jdbc.DriverManager
		this.url = "jdbc:mysql://localhost:3306/"; //jdbc.mysql://127.0.0.1:3306/
		this.schema = dbName; //db_hpq
		this.username = "root"; //root
		this.password = "1234";
	}
	
	
	public Connector(String url, String schema, String driver, String username, String password) {
		this.url = url;
		this.schema = schema;
		this.driver = driver;
		this.username = username;
		this.password = password;
	}
	
	public Connector(String schema, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Connection success!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		this.schema = "com.mysql.jdbc.Connector"; //com.mysql.jdbc.DriverManager
		this.url = "jdbc:mysql://localhost:3306/"; //jdbc.mysql://127.0.0.1:3306/
		this.schema = schema; //db_hpq
		this.username = "root"; //root
		this.password = password;
	}

	
	public static Connection getConnection() {
		Connection connection = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
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
		System.out.println("in executeQuery()");
		
		try {
			Statement st = connection.createStatement();
			result = st.executeQuery(query);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public String getDriver() {
		return driver;
	}

	public String getUrl() {
		return url;
	}

	public String getSchema() {
		return schema;
	}

	public String getUsername() {
		return username;
	}
}