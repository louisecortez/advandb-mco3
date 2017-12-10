package transaction;

import network.Client;

public class QueryGenerator {
	public static String generateRead(String table, String region) {
		String query = "SELECT * FROM " + table;
		
		if(region.equals(Client.FIRST_NODE)) {
			query += ".europe_america";
		} else if(region.equals(Client.SECOND_NODE)) {
			query += ".asia_africa";
		} else if(region.equals(Client.THIRD_NODE)) {
			query += ".all_regions";
		}
		
		return query;
	}
	
	public static String generateUpdate(String table, String country, String series, int year, double data) {
		if(table.equals(Client.FIRST_NODE)) {
			table = "dbmco3.europe_america";
		} else if(table.equals(Client.SECOND_NODE)) {
			table = "dbmco3.asia_africa";
		} else if(table.equals(Client.THIRD_NODE)) {
			table = "dbmco3.all_regions";
		}
		
		String query = "UPDATE " + table +
				" SET `Data` = " + data + 
				" WHERE CountryName = '" + country + 
				"' AND SeriesName = '" + series + 
				"' AND YearC = " + year + ";";
		
		return query;
	}
	
	public static String generateInsert(String table, String country, String series, int year, double data) {
		if(table.equals(Client.FIRST_NODE)) {
			table = "dbmco3.europe_america";
		} else if(table.equals(Client.SECOND_NODE)) {
			table = "dbmco3.asia_africa";
		} else if(table.equals(Client.THIRD_NODE)) {
			table = "dbmco3.all_regions";
		}
		
		String query = "INSERT INTO " + table +
				"(CountryCode, CountryName, Region, SeriesCode, SeriesName, YearC, `Data`) " + 
				"VALUES ('AAA', '" + country + "', 'Sample Region', 'ISO.AWC.XAYS.AQ', '" + series + "', " +
				year + ", " + data + ");";
		
		return query;
	}
	
	public static String generateDelete(String table, String country, String series, int year, double data) {
		if(table.equals(Client.FIRST_NODE)) {
			table = "dbmco3.europe_america";
		} else if(table.equals(Client.SECOND_NODE)) {
			table = "dbmco3.asia_africa";
		} else if(table.equals(Client.THIRD_NODE)) {
			table = "dbmco3.all_regions";
		}
		
		String query = "DELETE FROM " + table +
				" WHERE CountryName = '" + country + 
				"' AND SeriesName = '" + series + 
				"' AND YearC = " + year + ";";
		
		return query;
	}
}