package transaction;

import dbconnection.Connector;

public class Transaction {
	protected String transaction;
	protected String transType;

	public Transaction(String transaction, String transType) {
		this.transaction = transaction;
		this.transType = transType;
	}
	
	public String getTransaction() {
		return transaction;
	}

	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}
}