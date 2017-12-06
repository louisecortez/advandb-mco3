package view;
import controller.Controller;
import network.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class FrameIPAddress extends JFrame {
	private static final int WIDTH = 500;
    private static final int HEIGHT = 150;
    
    private Container pane; 
    private Controller controller;
    
    private JButton btnConfirm;
    private JTextField tfIPAddress;
    private JComboBox ddNodes;
    
    public FrameIPAddress(Controller controller) {
    	pane = getContentPane();
		pane.setLayout(new GridLayout(3, 1));
		pane.setBackground(Color.WHITE);
		
		btnConfirm = new JButton("OK");
		tfIPAddress = new JTextField(20);
		ddNodes = new JComboBox<String>(new String[] {Client.FIRST_NODE, Client.SECOND_NODE, Client.THIRD_NODE});
		
		tfIPAddress.setSize(350, 30);
		ddNodes.setSize(350, 30);
		btnConfirm.setSize(100, 30);
		
		btnConfirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
					
				if (controller != null)
				
				controller.initializeNode(tfIPAddress.getText().toString(), ddNodes.getSelectedItem().toString());
				else
					System.out.println("WOO THERE IS NO CONTROLLER");
			}
		});
		
		add(tfIPAddress);
		add(ddNodes);
		add(btnConfirm);
		
		setTitle("Configure IP Address and Node");
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public void setController(Controller controller) {
		this.controller = controller;
	}
}