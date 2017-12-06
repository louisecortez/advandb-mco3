package transaction;
import dbconnection.Connector;

public class Transaction {
	// commmon methods here
	
	protected Connector dbconnection;
	
	public Transaction(Connector dbconnection) {
		this.dbconnection = dbconnection;
	}
}
