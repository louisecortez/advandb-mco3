package controller;
import view.*;
import network.*;

import java.io.IOException;

public class Controller {
	private FrameIPAddress frameIP;
	private MainFrame frameMain;
	
	private Server server;
	private Client client;
	
	public Controller(FrameIPAddress frameIP) {
		this.frameIP = frameIP;
	}
	
	public void setMainFrame(MainFrame frameMain) {
		this.frameMain = frameMain;
	}
	
	public void setClient(Client client) {
		this.client = client;
	}
	
	public void initializeNode(String ipAddress, String region) {
		try {
			client = new Client(ipAddress, region, this);
			frameIP.dispose();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
