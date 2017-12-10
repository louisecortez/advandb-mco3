package dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class Connector {
	private static String url;
	private static String schema;
	private static String driver;
	private static String username;
	private static String password;
	
	private static String ipAllRegions = null;
	private static String ipEuropeAmerica = null;
	private static String ipAsiaAfrica = null;
	
	public Connector(String dbName) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		this.driver = "com.mysql.jdbc.Connector"; //com.mysql.jdbc.DriverManager
		//this.url = "jdbc:mysql://192.168.1.4:3306/"; //jdbc.mysql://127.0.0.1:3306/
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
		//this.url = "jdbc:mysql://192.168.1.4:3306/"; //jdbc.mysql://127.0.0.1:3306/
		this.schema = schema; //db_hpq
		this.username = "root"; //root
		this.password = password;
	}
	
	public void setURL(String url) {
		this.url = url;
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
	
	public static void executeStatement(String statement) throws SQLException {
		Connection connection = getConnection();
		System.out.println("in executeStatement()");
		
		PreparedStatement ps = connection.prepareStatement("SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;");
		connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		
		try {
			connection.setAutoCommit(false);
			PreparedStatement pst = connection.prepareStatement(statement);
			pst.execute(statement);
		} catch(Exception e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			try {
				connection.setAutoCommit(true);
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static ResultSet executeQuery(String query) {
		ResultSet result = null;
		Connection connection = Connector.getConnection();
		System.out.println("in executeQuery()");
		
		try {
			connection.setAutoCommit(false);
			Statement st = connection.createStatement();
			result = st.executeQuery(query);
		} catch(Exception e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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