package transaction;

import dbconnection.Connector;

public class WriteTransaction extends Transaction {

	public WriteTransaction(Connector dbconnection) {
		super(dbconnection);
		// TODO Auto-generated constructor stub
	}
	
	public boolean writeLocal() {
		return true;
	}
	
	public boolean writeGlobal() {
		return true;
	}
}
