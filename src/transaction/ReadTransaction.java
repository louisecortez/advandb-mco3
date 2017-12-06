package transaction;

import dbconnection.Connector;

public class ReadTransaction extends Transaction {

	public ReadTransaction(Connector dbconnection) {
		super(dbconnection);
		// TODO Auto-generated constructor stub
	}

	public boolean readLocal() {
		return true;
	}
	
	public boolean readGlobal() {
		return true;
	}
}
