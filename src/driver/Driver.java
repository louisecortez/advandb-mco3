package driver;

import java.io.IOException;

import controller.Controller;
import view.FrameIPAddress;
import network.*; 
 
public class Driver {
 	public static void main(String[] args) throws IOException {

 		
			Server s = new Server();
			
			new FrameIPAddress(new Controller(s));
			s.accept();
			
			/*
			Controller controller = new Controller(frameIP);
			if (controller != null) {
				System.out.println("Good controller");
				frameIP.setController(controller);
				s.accept();
			}
			*/	
			
			
		
 	}
 }