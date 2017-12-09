package view;
import controller.Controller;
import jdk.nashorn.internal.ir.Labels;
import network.Client;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.SQLException;

public class MainFrame extends JFrame {
	private Dimension dimensions = Toolkit.getDefaultToolkit().getScreenSize();
	
	private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;
    
    private JPanel writePanel, readPanel, headerPanel, transactionPanel, logPanel, resultsPanel;
    private JLabel lblNode;
	private JSplitPane contentPanel;
	private Container pane; 
	
	private JTable tableResults;
	private String[] columns = {"CountryCode", "CountryName", "Region", "SeriesCode",
			"SeriesName", "YearC", "Data"};
	private DefaultTableModel model;
	
	private Controller controller;
	
	public MainFrame() throws IOException {
		pane = getContentPane();
		pane.setLayout(new FlowLayout(FlowLayout.LEFT));
		pane.setBackground(Color.WHITE);
		
		headerPanel = new JPanel();
		lblNode = new JLabel("Node");
		
		readPanel = new JPanel();
		writePanel = new JPanel();
		transactionPanel = new JPanel();
		resultsPanel = new JPanel();
		logPanel = new JPanel();
		
		// Header Panel
		headerPanel.setPreferredSize(new Dimension(dimensions.width, 30));
		headerPanel.add(lblNode);
		pane.add(headerPanel, BorderLayout.PAGE_START);
		headerPanel.setBackground(Color.white);
		
		transactionPanel.setLayout(new GridLayout(0, 1));
		pane.add(transactionPanel);
		
		// Write Panel
		writePanel.setLayout(new FlowLayout());
		writePanel.setPreferredSize(new Dimension(200, 500));
		JLabel lblWrite = new JLabel("Write");
		JComboBox<String> cbWrite = new JComboBox<String>(new String[] {"Update", "Insert", "Delete"});
		JComboBox<String> cbCountry = new JComboBox<String>(new String[] {"\"Bahamas, The\"", "\"Congo, Dem. Rep.\"", "\"Congo, Rep.\"",
				"\"Egypt, Arab Rep.\"", "\"Gambia, The\"", "\"Hong Kong SAR, China\"", "\"Iran, Islamic Rep.\"",
				"\"Korea, Dem. Peoples Rep.\"", "\"Korea, Rep.\"", "\"Macao SAR, China\"", "\"Macedonia, FYR\"",
				"\"Micronesia, Fed. Sts.\"", "\"Venezuela, RB\"", "\"Yemen, Rep.\"", "Afghanistan", "Albania",
				"Algeria", "American Samoa", "Andorra", "Angola", "Antigua and Barbuda", "Argentina", "Armenia",
				"Aruba", "Australia", "Austria", "Azerbaijan", "Bahrain", "Bangladesh", "Barbados", "Belarus",
				"Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegovina",
				"Botswana", "Brazil", "British Virgin Islands", "Brunei Darussalam", "Bulgaria", "Burkina Faso",
				"Burundi", "Cabo Verde", "Cambodia", "Cameroon", "Canada", "Cayman Islands", "Central African Republic",
				"Chad", "Channel Islands", "Chile", "China", "Colombia", "Comoros", "Costa Rica", "Cote d'Ivoire",
				"Croatia", "Cuba", "Curacao", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", 
				"Dominican Republic", "Ecuador", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia",
				"Faroe Islands", "Fiji", "Finland", "France", "French Polynesia", "Gabon", "Georgia", "Germany",
				"Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guam", "Guatemala", "Guinea", "Guinea-Bissau",
				"Guyana", "Haiti", "Honduras", "Hungary", "Iceland", "India", "Indonesia", "Iraq", "Ireland",
				"Isle of Man", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati",
				"Kosovo", "Kuwait", "Kyrgyz Republic", "Lao PDR", "Latvia", "Lebanon", "Lesotho", "Liberia",
				"Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Madagascar", "Malawi", "Malaysia", "Maldives",
				"Mali", "Malta", "Marshall Islands", "Mauritania", "Mauritius", "Mexico", "Moldova", "Monaco",
				"Mongolia", "Montenegro", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal",
				"Netherlands", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Northern Mariana Islands",
				"Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines",
				"Poland", "Portugal", "Puerto Rico", "Qatar", "Romania", "Russian Federation", "Rwanda", "Samoa", 
				"San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone",
				"Singapore", "Sint Maarten (Dutch part)", "Slovak Republic", "Slovenia", "Solomon Islands", "Somalia",
				"South Africa", "South Sudan", "Spain", "Sri Lanka", "St. Kitts and Nevis", "St. Lucia", "St. Martin (French part)",
				"St. Vincent and the Grenadines", "Sudan", "Suriname", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic",
				"Tajikistan", "Tanzania", "Thailand", "Timor-Leste", "Togo", "Tonga", "Trinidad and Tobago", "Tunisia",
				"Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates",
				"United Kingdom", "United States", "Uruguay", "Uzbekistan", "Vanuatu", "Vietnam", "Virgin Islands (U.S.)",
				"West Bank and Gaza", "Zambia", "Zimbabwe"});
		JComboBox<String> cbSeries = new JComboBox<String>(new String[] {"\"Adjusted net enrollment rate, primary, female (% of primary school age children)\"",
					"\"Adjusted net enrollment rate, primary, male (% of primary school age children)\"", "\"Adjusted net enrolment rate, primary, both sexes (%)\"",
					"\"Adolescents out of school, female (% of female lower secondary school age)\"", "\"Adolescents out of school, male (% of male lower secondary school age)\"",
					"\"Adult literacy rate, population 15+ years, both sexes (%)\"", "\"Age dependency ratio, old (% of working-age population)\"", 
					"\"Age dependency ratio, young (% of working-age population)\"", "\"Air transport, freight (million ton-km)\"",
					"\"Air transport, passengers carried\"", "\"Air transport, registered carrier departures worldwide\"",
					"\"All education staff compensation, primary (% of total expenditure in primary public institutions)\"",
					"\"All education staff compensation, secondary (% of total expenditure in secondary public institutions)\"",
					"\"All education staff compensation, tertiary (% of total expenditure in tertiary public institutions)\"",
					"\"All education staff compensation, total (% of total expenditure in public institutions)\"",
					"\"Annualized average growth rate in per capita real survey mean consumption or income, bottom 40% of population (%)\"",
					"\"Annualized average growth rate in per capita real survey mean consumption or income, total population (%)\"",
					"\"Average working hours of children, study and work, ages 7-14 (hours per week)\"",
					"\"Average working hours of children, study and work, female, ages 7-14 (hours per week)\"",
					"\"Average working hours of children, study and work, male, ages 7-14 (hours per week)\"",
					"\"Average working hours of children, working only, ages 7-14 (hours per week)\"",
					"\"Average working hours of children, working only, female, ages 7-14 (hours per week)\"", 
					"\"Average working hours of children, working only, male, ages 7-14 (hours per week)\"",
					"\"Birth rate, crude (per 1,000 people)\"", "\"Burden of customs procedure, WEF (1=extremely inefficient to 7=extremely efficient)\"",
					"\"Child employment in agriculture, female (% of female economically active children ages 7-14)\"",
					"\"Child employment in agriculture, male (% of male economically active children ages 7-14)\"",
					"\"Child employment in manufacturing, female (% of female economically active children ages 7-14)\"",
					"\"Child employment in manufacturing, male (% of male economically active children ages 7-14)\"",
					"\"Child employment in services, female (% of female economically active children ages 7-14)\"",
					"\"Child employment in services, male (% of male economically active children ages 7-14)\"",
					"\"Children in employment, female (% of female children ages 7-14)\"",
					"\"Children in employment, male (% of male children ages 7-14)\"",
					"\"Children in employment, self-employed (% of children in employment, ages 7-14)\"",
					"\"Children in employment, self-employed, female (% of female children in employment, ages 7-14)\"",
					"\"Children in employment, self-employed, male (% of male children in employment, ages 7-14)\"",
					"\"Children in employment, study and work (% of children in employment, ages 7-14)\"",
					"\"Children in employment, study and work, female (% of female children in employment, ages 7-14)\"",
					"\"Children in employment, study and work, male (% of male children in employment, ages 7-14)\"",
					"\"Children in employment, total (% of children ages 7-14)\"", "\"Children in employment, unpaid family workers (% of children in employment, ages 7-14)\"",
					"\"Children in employment, unpaid family workers, female (% of female children in employment, ages 7-14)\"",
					"\"Children in employment, unpaid family workers, male (% of male children in employment, ages 7-14)\"",
					"\"Children in employment, wage workers (% of children in employment, ages 7-14)\"",
					"\"Children in employment, wage workers, female (% of female children in employment, ages 7-14)\"",
					"\"Children in employment, wage workers, male (% of male children in employment, ages 7-14)\"",
					"\"Children in employment, work only (% of children in employment, ages 7-14)\"",
					"\"Children in employment, work only, female (% of female children in employment, ages 7-14)\"",
					"\"Children in employment, work only, male (% of male children in employment, ages 7-14)\"",
					"\"Children out of school, female (% of female primary school age)\"", 
					"\"Children out of school, male (% of male primary school age)\"", "\"Children out of school, primary, female\"",
					"\"Children out of school, primary, male\"", "\"Completeness of birth registration, rural (%)\"",
					"\"Completeness of birth registration, urban (%)\"", "\"Contributing family workers, female (% of females employed)\"",
					"\"Contributing family workers, male (% of males employed)\"", "\"Contributing family workers, total (% of total employed)\"",
					"\"Current education expenditure, primary (% of total expenditure in primary public institutions)\"",
					"\"Current education expenditure, secondary (% of total expenditure in secondary public institutions)\"",
					"\"Current education expenditure, tertiary (% of total expenditure in tertiary public institutions)\"",
					"\"Current education expenditure, total (% of total expenditure in public institutions)\"",
					"\"Death rate, crude (per 1,000 people)\"", "\"Educational attainment, at least competed lower secondary, population 25+, female (%) (cumulative)\"",
					"\"Educational attainment, at least competed lower secondary, population 25+, male (%) (cumulative)\"",
					"\"Educational attainment, at least competed lower secondary, population 25+, total (%) (cumulative)\"",
					"\"Educational attainment, at least competed post-secondary, population 25+, female (%) (cumulative)\"",
					"\"Educational attainment, at least competed post-secondary, population 25+, male (%) (cumulative)\"",
					"\"Educational attainment, at least competed post-secondary, population 25+, total (%) (cumulative)\"",
					"\"Educational attainment, at least competed upper secondary, population 25+, female (%) (cumulative)\"",
					"\"Educational attainment, at least competed upper secondary, population 25+, male (%) (cumulative)\"",
					"\"Educational attainment, at least competed upper secondary, population 25+, total (%) (cumulative)\"",
					"\"Educational attainment, at least completed primary, population 25+ years, female (%) (cumulative)\"",
					"\"Educational attainment, at least completed primary, population 25+ years, male (%) (cumulative)\"",
					"\"Educational attainment, at least completed primary, population 25+ years, total (%) (cumulative)\"",
					"\"Educational attainment, competed at least Bachelor's or equivalent, population 25+, female (%) (cumulative)\"",
					"\"Educational attainment, competed at least Bachelor's or equivalent, population 25+, male (%) (cumulative)\"",
					"\"Educational attainment, competed at least Bachelor's or equivalent, population 25+, total (%) (cumulative)\"",
					"\"Educational attainment, competed at least Master's or equivalent, population 25+, female (%) (cumulative)\"",
					"\"Educational attainment, competed at least Master's or equivalent, population 25+, male (%) (cumulative)\"",
					"\"Educational attainment, competed at least Master's or equivalent, population 25+, total (%) (cumulative)\"",
					"\"Educational attainment, competed at least short-cycle tertiary, population 25+, female (%) (cumulative)\"",
					"\"Educational attainment, competed at least short-cycle tertiary, population 25+, male (%) (cumulative)\"",
					"\"Educational attainment, competed at least short-cycle tertiary, population 25+, total (%) (cumulative)\"",
					"\"Educational attainment, competed Doctoral or equivalent, population 25+, female (%) (cumulative)\"",
					"\"Educational attainment, competed Doctoral or equivalent, population 25+, male (%) (cumulative)\"",
					"\"Educational attainment, competed Doctoral or equivalent, population 25+, total (%) (cumulative)\"",
					"\"Effective transition rate from primary to lower secondary general education, both sexes (%)\"",
					"\"Employers, female (% of employment)\"", "\"Employers, male (% of employment)\"", "\"Employers, total (% of employment)\"",
					"\"Employment in agriculture, female (% of female employment)\"", "\"Employment in agriculture, male (% of male employment)\"",
					"\"Employment in industry, female (% of female employment)\"", "\"Employment in industry, male (% of male employment)\"",
					"\"Employment in services, female (% of female employment)\"", "\"Employment in services, male (% of male employment)\"",
					"\"Employment to population ratio, 15+, female (%) (modeled ILO estimate)\"",
					"\"Employment to population ratio, 15+, female (%) (national estimate)\"",
					"\"Employment to population ratio, 15+, male (%) (modeled ILO estimate)\"",
					"\"Employment to population ratio, 15+, male (%) (national estimate)\"",
					"\"Employment to population ratio, 15+, total (%) (modeled ILO estimate)\"",
					"\"Employment to population ratio, 15+, total (%) (national estimate)\"",
					"\"Employment to population ratio, ages 15-24, female (%) (modeled ILO estimate)\"",
					"\"Employment to population ratio, ages 15-24, female (%) (national estimate)\"",
					"\"Employment to population ratio, ages 15-24, male (%) (modeled ILO estimate)\"",
					"\"Employment to population ratio, ages 15-24, male (%) (national estimate)\"",
					"\"Employment to population ratio, ages 15-24, total (%) (modeled ILO estimate)\"",
					"\"Employment to population ratio, ages 15-24, total (%) (national estimate)\"",
					"\"Enrolment in primary education, both sexes (number)\"",
					"\"Enrolment in secondary education, both sexes (number)\"",
					"\"Enrolment in secondary general, both sexes (number)\"",
					"\"Enrolment in secondary vocational, both sexes (number)\"",
					"\"Government expenditure on education, total (% of GDP)\"",
					"\"Government expenditure per student, primary (% of GDP per capita)\"",
					"\"Government expenditure per student, secondary (% of GDP per capita)\"",
					"\"Gross enrollment ratio, primary, both sexes (%)\"",
					"\"Gross enrolment ratio, pre-primary, both sexes (%)\"",
					"\"Gross enrolment ratio, pre-primary, female (%)\"",
					"\"Gross enrolment ratio, pre-primary, male (%)\"",
					"\"Gross enrolment ratio, secondary, both sexes (%)\"",
					"\"Gross enrolment ratio, tertiary, both sexes (%)\"",
					"\"Gross intake ratio to Grade 1 of primary education, both sexes (%)\"",
					"\"Gross intake ratio to Grade 1 of primary education, female (%)\"",
					"\"Gross intake ratio to Grade 1 of primary education, male (%)\"",
					"\"Informal employment, female (% of total non-agricultural employment)\"",
					"\"Informal employment, male (% of total non-agricultural employment)\"",
					"\"International migrant stock, total\"",
					"\"Labor force participation rate for ages 15-24, female (%) (modeled ILO estimate)\"",
					"\"Labor force participation rate for ages 15-24, female (%) (national estimate)\"",
					"\"Labor force participation rate for ages 15-24, male (%) (modeled ILO estimate)\"",
					"\"Labor force participation rate for ages 15-24, male (%) (national estimate)\"",
					"\"Labor force participation rate for ages 15-24, total (%) (modeled ILO estimate)\"",
					"\"Labor force participation rate for ages 15-24, total (%) (national estimate)\"",
					"\"Labor force participation rate, female (% of female population ages 15+) (modeled ILO estimate)\"",
					"\"Labor force participation rate, female (% of female population ages 15+) (national estimate)\"",
					"\"Labor force participation rate, female (% of female population ages 15-64) (modeled ILO estimate)\"",
					"\"Labor force participation rate, male (% of male population ages 15+) (modeled ILO estimate)\"",
					"\"Labor force participation rate, male (% of male population ages 15+) (national estimate)\"",
					"\"Labor force participation rate, male (% of male population ages 15-64) (modeled ILO estimate)\"",
					"\"Labor force participation rate, total (% of total population ages 15+) (modeled ILO estimate)\"",
					"\"Labor force participation rate, total (% of total population ages 15+) (national estimate)\"",
					"\"Labor force participation rate, total (% of total population ages 15-64) (modeled ILO estimate)\"",
					"\"Labor force with primary education, female (% of female labor force)\"",
					"\"Labor force with primary education, male (% of male labor force)\"",
					"\"Labor force with secondary education, female (% of female labor force)\"",
					"\"Labor force with secondary education, male (% of male labor force)\"",
					"\"Labor force with tertiary education, female (% of female labor force)\"",
					"\"Labor force with tertiary education, male (% of male labor force)\"",
					"\"Labor force, female (% of total labor force)\"",
					"\"Labor force, total\"",
					"\"Life expectancy at birth, female (years)\"",
					"\"Life expectancy at birth, male (years)\"",
					"\"Life expectancy at birth, total (years)\"",
					"\"Literacy rate, adult female (% of females ages 15 and above)\"",
					"\"Literacy rate, adult male (% of males ages 15 and above)\"",
					"\"Literacy rate, youth (ages 15-24), gender parity index (GPI)\"",
					"\"Literacy rate, youth female (% of females ages 15-24)\"",
					"\"Literacy rate, youth male (% of males ages 15-24)\"",
					"\"Long-term unemployment, female (% of female unemployment)\"",
					"\"Long-term unemployment, male (% of male unemployment)\"",
					"\"Lower secondary completion rate, female (% of relevant age group)\"",
					"\"Lower secondary completion rate, male (% of relevant age group)\"",
					"\"Lower secondary completion rate, total (% of relevant age group)\"",
					"\"Mortality rate, adult, female (per 1,000 female adults)\"",
					"\"Mortality rate, adult, male (per 1,000 male adults)\"",
					"\"Mortality rate, infant (per 1,000 live births)\"",
					"\"Mortality rate, infant, female (per 1,000 live births)\"",
					"\"Mortality rate, infant, male (per 1,000 live births)\"",
					"\"Mortality rate, neonatal (per 1,000 live births)\"",
					"\"Mortality rate, under-5 (per 1,000 live births)\"",
					"\"Mortality rate, under-5, female (per 1,000 live births)\"",
					"\"Mortality rate, under-5, male (per 1,000 live births)\"",
					"\"Net enrolment rate, primary, both sexes (%)\"",
					"\"Net enrolment rate, secondary, both sexes (%)\"",
					"\"Net intake rate in grade 1, female (% of official school-age population)\"",
					"\"Net intake rate in grade 1, male (% of official school-age population)\"",
					"\"Net intake rate to Grade 1 of primary education, both sexes (%)\"",
					"\"Out-of-school children of primary school age, both sexes (number)\"",
					"\"Over-age students, primary (% of enrollment)\"",
					"\"Over-age students, primary, female (% of female enrollment)\"",
					"\"Over-age students, primary, male (% of male enrollment)\"",
					"\"Part time employment, female (% of total female employment)\"",
					"\"Part time employment, female (% of total part time employment)\"",
					"\"Part time employment, male (% of total male employment)\"",
					"\"Part time employment, total (% of total employment)\"",
					"\"Percentage of female teachers in primary education who are trained, female (%)\"",
					"\"Percentage of female teachers in secondary education who are trained, female (%)\"",
					"\"Percentage of male teachers in primary education who are trained, male (%)\"",
					"\"Percentage of male teachers in secondary education who are trained, male (%)\"",
					"\"Percentage of repeaters in primary education, all grades, both sexes (%)\"",
					"\"Percentage of repeaters in primary education, all grades, female (%)\"",
					"\"Percentage of repeaters in primary education, all grades, male (%)\"",
					"\"Percentage of teachers in primary education who are trained, both sexes (%)\"",
					"\"Percentage of teachers in secondary education who are trained, both sexes (%)\"",
					"\"Persistence to grade 5, female (% of cohort)\"",
					"\"Persistence to grade 5, male (% of cohort)\"",
					"\"Population, female (% of total)\"",
					"\"Population, total\"",
					"\"Preprimary education, duration (years)\"",
					"\"Primary completion rate, female (% of relevant age group)\"",
					"\"Primary completion rate, male (% of relevant age group)\"",
					"\"Primary completion rate, total (% of relevant age group)\"",
					"\"Primary education, pupils (% female)\"",
					"\"Primary education, teachers (% female)\"",
					"\"Progression to secondary school, female (%)\"",
					"\"Progression to secondary school, male (%)\"",
					"\"Pupil-teacher ratio, lower secondary\"",
					"\"Pupil-teacher ratio, tertiary\"",
					"\"Pupil-teacher ratio, upper secondary\"",
					"\"Quality of port infrastructure, WEF (1=extremely underdeveloped to 7=well developed and efficient by international standards)\"",
					"\"Railways, goods transported (million ton-km)\"",
					"\"Railways, passengers carried (million passenger-km)\"",
					"\"School enrollment, primary (gross), gender parity index (GPI)\"",
					"\"School enrollment, primary and secondary (gross), gender parity index (GPI)\"",
					"\"School enrollment, primary, female (% gross)\"",
					"\"School enrollment, primary, female (% net)\"",
					"\"School enrollment, primary, male (% gross)\"",
					"\"School enrollment, primary, male (% net)\"",
					"\"School enrollment, secondary (gross), gender parity index (GPI)\"",
					"\"School enrollment, secondary, female (% gross)\"",
					"\"School enrollment, secondary, female (% net)\"",
					"\"School enrollment, secondary, male (% gross)\"",
					"\"School enrollment, secondary, male (% net)\"",
					"\"School enrollment, tertiary (gross), gender parity index (GPI)\"",
					"\"School enrollment, tertiary, female (% gross)\"",
					"\"School enrollment, tertiary, male (% gross)\"",
					"\"Secondary education, pupils (% female)\"",
					"\"Secondary education, teachers (% female)\"",
					"\"Self-employed, female (% of females employed)\"",
					"\"Self-employed, male (% of males employed)\"",
					"\"Self-employed, total (% of total employed)\"",
					"\"Share of youth not in education, employment, or training, female (% of female youth population)\"",
					"\"Share of youth not in education, employment, or training, male (% of male youth population)\"",
					"\"Share of youth not in education, employment, or training, total (% of youth population)\"",
					"\"Survey mean consumption or income per capita, bottom 40% of population (2011 PPP $ per day)\"",
					"\"Survey mean consumption or income per capita, total population (2011 PPP $ per day)\"",
					"\"Survival rate to Grade 5 of primary education, both sexes (%)\"",
					"\"Survival rate to the last grade of primary education, both sexes (%)\"",
					"\"Survival rate to the last grade of primary education, female (%)\"",
					"\"Survival rate to the last grade of primary education, male (%)\"",
					"\"Survival to age 65, female (% of cohort)\"",
					"\"Survival to age 65, male (% of cohort)\"",
					"\"Teachers in primary education, both sexes (number)\"",
					"\"Teachers in secondary education, both sexes (number)\"",
					"\"Teachers in secondary education, female (number)\"",
					"\"Tertiary education, academic staff (% female)\"",
					"\"Trained teachers in lower secondary education, female (% of female teachers)\"",
					"\"Trained teachers in lower secondary education, male (% of male teachers)\"",
					"\"Trained teachers in preprimary education, female (% of female teachers)\"",
					"\"Trained teachers in preprimary education, male (% of male teachers)\"",
					"\"Trained teachers in upper secondary education, female (% of female teachers)\"",
					"\"Trained teachers in upper secondary education, male (% of male teachers)\"",
					"\"Unemployment with primary education, female (% of female unemployment)\"",
					"\"Unemployment with primary education, male (% of male unemployment)\"",
					"\"Unemployment with secondary education, female (% of female unemployment)\"",
					"\"Unemployment with secondary education, male (% of male unemployment)\"",
					"\"Unemployment with tertiary education, female (% of female unemployment)\"",
					"\"Unemployment with tertiary education, male (% of male unemployment)\"",
					"\"Unemployment, female (% of female labor force) (modeled ILO estimate)\"",
					"\"Unemployment, female (% of female labor force) (national estimate)\"",
					"\"Unemployment, male (% of male labor force) (modeled ILO estimate)\"",
					"\"Unemployment, male (% of male labor force) (national estimate)\"",
					"\"Unemployment, total (% of total labor force) (modeled ILO estimate)\"",
					"\"Unemployment, total (% of total labor force) (national estimate)\"",
					"\"Unemployment, youth female (% of female labor force ages 15-24) (modeled ILO estimate)\"",
					"\"Unemployment, youth female (% of female labor force ages 15-24) (national estimate)\"",
					"\"Unemployment, youth male (% of male labor force ages 15-24) (modeled ILO estimate)\"",
					"\"Unemployment, youth male (% of male labor force ages 15-24) (national estimate)\"",
					"\"Unemployment, youth total (% of total labor force ages 15-24) (modeled ILO estimate)\"",
					"\"Unemployment, youth total (% of total labor force ages 15-24) (national estimate)\"",
					"\"Vulnerable employment, female (% of female employment)\"",
					"\"Vulnerable employment, male (% of male employment)\"",
					"\"Vulnerable employment, total (% of total employment)\"",
					"\"Wage and salaried workers, female (% of females employed)\"",
					"\"Wage and salaried workers, total (% of total employed)\"",
					"\"Wage and salary workers, male (% of males employed)\"",
					"\"Youth literacy rate, population 15-24 years, both sexes (%)\""});
		
		JLabel lblYear = new JLabel("Year");
		JTextField tfYear = new JTextField();
		JLabel lblData = new JLabel("Data");
		JTextField tfData = new JTextField();
		JComboBox<String> cbType = new JComboBox<String>(new String[] {"Local", "Global"});
		JButton btnAddTrans = new JButton("Add Write");
		cbWrite.setPreferredSize(new Dimension(200, 30));
		cbWrite.setMaximumSize(new Dimension(200, 30));
		cbCountry.setPreferredSize(new Dimension(200, 30));
		cbCountry.setMaximumSize(new Dimension(200, 30));
		cbSeries.setPreferredSize(new Dimension(200, 30));
		cbSeries.setMaximumSize(new Dimension(200, 30));
		cbType.setPreferredSize(new Dimension(200, 30));
		cbType.setMaximumSize(new Dimension(200, 30));
		btnAddTrans.setPreferredSize(new Dimension(200, 30));
		btnAddTrans.setMaximumSize(new Dimension(200, 30));
		tfYear.setPreferredSize(new Dimension(200, 30));
		tfYear.setMaximumSize(new Dimension(200, 30));
		tfData.setPreferredSize(new Dimension(200, 30));
		tfData.setMaximumSize(new Dimension(200, 30));
		writePanel.add(lblWrite);
		writePanel.add(cbWrite);
		writePanel.add(cbCountry);
		writePanel.add(cbSeries);
		writePanel.add(lblYear);
		writePanel.add(tfYear);
		writePanel.add(lblData);
		writePanel.add(tfData);
		writePanel.add(cbType);
		writePanel.add(btnAddTrans);
		JLabel lblRead = new JLabel("Read");
		JComboBox<String> cbReadRegion = new JComboBox<String>(new String[] {"All Regions", "Europe and America", "Asia and Africa"});
		JButton btnAddRead = new JButton("Add Read");
		JComboBox<String> cbTypeR = new JComboBox<String>(new String[] {"Local Main", "Local Replica", "Global"});
		cbTypeR.setPreferredSize(new Dimension(200, 30));
		cbTypeR.setMaximumSize(new Dimension(200, 30));
		JLabel lblLogs = new JLabel("Log");
		cbReadRegion.setPreferredSize(new Dimension(200, 30));
		cbReadRegion.setMaximumSize(new Dimension(200, 30));
		btnAddRead.setPreferredSize(new Dimension(200, 30));
		btnAddRead.setMaximumSize(new Dimension(200, 30));
		JButton btnCommit = new JButton("Commit");
		btnCommit.setPreferredSize(new Dimension(200, 30));
		btnCommit.setMaximumSize(new Dimension(200, 30));
		cbReadRegion.setEnabled(false);
		writePanel.add(lblRead);
		writePanel.add(cbReadRegion);
		writePanel.add(cbTypeR);
		writePanel.add(btnAddRead);
		writePanel.add(btnCommit);
		writePanel.add(lblLogs);
		writePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		writePanel.setBackground(Color.WHITE);
		pane.add(writePanel);

		// Log Panel
		logPanel.setLayout(new FlowLayout());
		JTextArea taLogs = new JTextArea("Write PHILIPPINES, Series, 2018, DATA\n", 9, 200);
		taLogs.setEditable(false);
		JScrollPane spLogs = new JScrollPane(taLogs);
		logPanel.add(spLogs);
		pane.add(logPanel);
		
		// Results Panel
		model = new DefaultTableModel() ;
		for(int i = 0; i < columns.length; i++) {
	    	model.addColumn(columns[i]);
	    }
		tableResults = new JTable(model);
		tableResults.setBounds(30,40,200,300);     
		JScrollPane spTable = new JScrollPane(tableResults);
		spTable.setPreferredSize(new Dimension(1100, 500));
		resultsPanel.setBackground(Color.WHITE);
		resultsPanel.add(spTable);
		pane.add(resultsPanel, FlowLayout.LEADING);
		
		// Action Listeners
		cbWrite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(cbWrite.getSelectedIndex() == 0 || cbWrite.getSelectedIndex() == 1) {
					cbCountry.setEditable(true);
					cbSeries.setEditable(true);
					tfYear.setEditable(true);
					tfData.setEditable(true);
				} else {
					tfData.setEditable(false);
				}
			}
		});
		
		cbTypeR.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(cbTypeR.getSelectedIndex() == 0 || cbTypeR.getSelectedIndex() == 1) {
					cbReadRegion.setEnabled(false);;
				} else {
					cbReadRegion.setEnabled(true);
				}
			}
		});
		
		btnAddTrans.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String log = cbType.getSelectedItem().toString() + " "
						+ cbWrite.getSelectedItem().toString() + " " 
						+ cbCountry.getSelectedItem().toString() + ", "
						+ cbSeries.getSelectedItem().toString() + ", "
						+ tfYear.getText();
				
				if(cbWrite.getSelectedItem().toString().equals("Update")
						|| cbWrite.getSelectedItem().toString().equals("Insert")) {
					log = log + " with Data: " + tfData.getText();
				}
				
				log += "\n";
				
				taLogs.append(log);
				
				if(cbType.getSelectedItem().toString().equals("Local")) {
					// controller.localWrite
				} else {
					// controller.globalRead
				}
			}
		});
		
		btnAddRead.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String log = cbReadRegion.getSelectedItem().toString();
				
				if(cbTypeR.getSelectedItem().toString().equals("Local Main") || 
						cbTypeR.getSelectedItem().toString().equals("Local Replica")) {
					log = "";
				}
				
				taLogs.append(cbTypeR.getSelectedItem().toString() + " Read " + log + "\n");
				
				if(controller == null)
					System.out.println("HELLO NULL");
				
				if(cbTypeR.getSelectedItem().toString().equals("Local Main")) {
					try {
						controller.localRead("wdidb");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if(cbTypeR.getSelectedItem().toString().equals("Local Replica")){
					// local read replica
				} else {
					// global read
				}
			}
		});
		
		btnCommit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				taLogs.append("------------------------------------------\n");
			}
		});
		
		setTitle("Distributed Databases");
		setSize(dimensions.width, dimensions.height);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		 addWindowListener(new WindowClose());	
	}
	
	public void setController(Controller controller) {
		this.controller = controller;
	}
	
	public void setLabelNode(String text) {
		lblNode.setText(text);
	}
	
	public void clearAllRows() {
		DefaultTableModel model = (DefaultTableModel) tableResults.getModel();
		model.setRowCount(0);
	}
	
	public void addTableRow(Object[] rowData) {
		// Dynamic table
//		model.addRow(new Object[] {"ABW", "Aruba", "Latin America & Caribbean",
//				"IS.SHP.GCNW.XQ", "Liner shipping connectivity index (maximum value in 2004 = 100)",
//				"2004", "7.370000000000000"});
//		
		model.addRow(rowData);
	}
	
	class WindowClose extends WindowAdapter{
	    @Override
	    public void windowClosing(WindowEvent e){
	        e.getWindow().dispose();
	    }
	     
	}
	
	public static void main(String[] args) {
		try {
			new MainFrame();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
