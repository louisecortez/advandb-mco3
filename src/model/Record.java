/* 
 * This class represents a column
 * in the schema to be used 
 * for the system.
 */

package model;

public class Record {
	public static final int COLUMN_COUNT = 9;
	public static final String[] columns = {"CountryCode", "CountryName", "Region", "income", "SeriesCategory",
			"SeriesCode", "SeriesName", "YearC", "Data"};
	
	private String countryCode;
	private String countryName;
	private String region;
	private String income;
	private String seriesCategory;
	private String seriesName;
	private int year;
	private double data;
	
	public Record(String countryCode, String countryName, String region, String income, String seriesCategory,
			String seriesName, int year, double data) {
		this.countryCode = countryCode;
		this.countryName = countryName;
		this.region = region;
		this.income = income;
		this.seriesCategory = seriesCategory;
		this.seriesName = seriesName;
		this.year = year;
		this.data = data;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getSeriesCategory() {
		return seriesCategory;
	}

	public void setSeriesCategory(String seriesCategory) {
		this.seriesCategory = seriesCategory;
	}

	public String getSeriesName() {
		return seriesName;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public double getData() {
		return data;
	}

	public void setData(double data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Record [countryCode=" + countryCode + ", countryName=" + countryName + ", region=" + region
				+ ", income=" + income + ", seriesCategory=" + seriesCategory + ", seriesName=" + seriesName + ", year="
				+ year + ", data=" + data + "]";
	}
}
