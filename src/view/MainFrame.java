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
		headerPanel.setPreferredSize(new Dimension(dimensions.width, 50));
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
		JComboBox<String> cbSeries = new JComboBox<String>(new String[] {"dskjfshk", "kunin nalang", "from db"});
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
		JLabel lblLogs = new JLabel("Log");
		cbReadRegion.setPreferredSize(new Dimension(200, 30));
		cbReadRegion.setMaximumSize(new Dimension(200, 30));
		btnAddRead.setPreferredSize(new Dimension(200, 30));
		lblRead.setBorder(BorderFactory.createEmptyBorder(20, 0, 0 , 0));
		lblLogs.setBorder(BorderFactory.createEmptyBorder(40, 0, 0 ,0));
		btnAddRead.setMaximumSize(new Dimension(200, 30));
		writePanel.add(lblRead);
		writePanel.add(cbReadRegion);
		writePanel.add(btnAddRead);
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
		addTableRow();
		tableResults = new JTable(model);
		
		resultsPanel.add(tableResults);
		pane.add(resultsPanel);
		
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
				
				taLogs.append("Read " + log + "\n");
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
	
	public void addTableRow() {
		// Dynamic table
		model.addRow(new Object[] {"ABW", "Aruba", "Latin America & Caribbean",
				"IS.SHP.GCNW.XQ", "Liner shipping connectivity index (maximum value in 2004 = 100)",
				"2004", "7.370000000000000"});
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
