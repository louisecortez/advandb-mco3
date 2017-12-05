package view;
import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {
	private Dimension dimensions = Toolkit.getDefaultToolkit().getScreenSize();
	
	private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;
    
    private JPanel viewPanel, sidePanel;
	private JSplitPane contentPanel;
	private Container pane; 
	
	private Controller controller;
	
	public MainFrame() {
		pane = getContentPane();
		pane.setLayout(new BorderLayout());
		pane.setBackground(Color.WHITE);
		
		viewPanel = new JPanel();
		sidePanel = new JPanel();
		
		sidePanel.setMinimumSize(new Dimension(270, 550));
		sidePanel.setMaximumSize(new Dimension(270, 550));
		viewPanel.setMinimumSize(new Dimension(730, 550));
		viewPanel.setMaximumSize(new Dimension(730, 550));
		contentPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidePanel, viewPanel);
		
		contentPanel.setDividerLocation(270);
		contentPanel.setDividerSize(1);

		pane.add(contentPanel, BorderLayout.CENTER);
		
		sidePanel.setBackground(Color.white);
		viewPanel.setBackground(Color.white);
		
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
	
	class WindowClose extends WindowAdapter{
	    @Override
	    public void windowClosing(WindowEvent e){
	        e.getWindow().dispose();
	    }
	     
	}
}
