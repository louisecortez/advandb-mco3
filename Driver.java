package driver;

import java.io.IOException;

import controller.Controller;
import view.FrameIPAddress;
import network.*; 
 
public class Driver {
 	public static void main(String[] args) {

 		try {
			Server s = new Server();
			FrameIPAddress frameIP = new FrameIPAddress();
			Controller controller = new Controller(frameIP, s);
			frameIP.setController(controller);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
 	}
 }