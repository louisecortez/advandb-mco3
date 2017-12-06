package driver;
import controller.Controller;
import view.FrameIPAddress;

public class Driver {
	public static void main(String[] args) {
		FrameIPAddress frameIP = new FrameIPAddress();
		Controller controller = new Controller(frameIP);
		frameIP.setController(controller);
	}
}
