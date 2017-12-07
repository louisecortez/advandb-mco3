package model;

public class Entity {
	public static final int COLUMN_COUNT = 7;
	public static final String[] HEADERS = {"CountryCode", "CountryName", "Region", "SeriesCode", "SeriesName", "YearC", "Data"};
	private String countrycode;
	private String countryname;
	private String region;
	private String seriescode;
	private String seriesname;
	private int yearc;
	private int data;
	
	
	public Entity(String countrycode, String countryname, String region, String seriescode, String seriesname,
			int yearc, int data) {
		super();
		this.countrycode = countrycode;
		this.countryname = countryname;
		this.region = region;
		this.seriescode = seriescode;
		this.seriesname = seriesname;
		this.yearc = yearc;
		this.data = data;
	}
	
	/*
	public Entity(int id) {
		super();
		this.id = id;
	}
	 */
	public String getCountrycode() {
		return countrycode;
	}

	public void setCountrycode(String countrycode) {
		this.countrycode = countrycode;
	}

	public String getCountryname() {
		return countryname;
	}

	public void setCountryname(String countryname) {
		this.countryname = countryname;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getSeriescode() {
		return seriescode;
	}

	public void setSeriescode(String seriescode) {
		this.seriescode = seriescode;
	}

	public String getSeriesname() {
		return seriesname;
	}

	public void setSeriesname(String seriesname) {
		this.seriesname = seriesname;
	}

	public int getYearc() {
		return yearc;
	}

	public void setYearc(int yearc) {
		this.yearc = yearc;
	}

	public int getData() {
		return data;
	}

	public void setData(int data) {
		this.data = data;
	}

	/*
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	*/

	/*
	 * this.id = id;
		this.countrycode = countrycode;
		this.countryname = countryname;
		this.region = region;
		this.seriescode = seriescode;
		this.seriesname = seriesname;
		this.yearc = yearc;
		this.data = data;
	 * 
	 * */
	@Override
	public String toString() {
		return "Entity [countrycode=" + countrycode + ", countryname=" + countryname + ", region=" + region 
				+ ", seriescode=" + seriescode + ", seriesname=" + seriesname
				+ ", yearc=" + yearc + ", data=" + data;
	}

	public Object[] toArray() {
		return new Object[] { countrycode, countryname, region, seriescode, seriesname, yearc, data };
	}
}
