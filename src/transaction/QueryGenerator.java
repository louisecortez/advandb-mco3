package transaction;

import network.Client;

public class QueryGenerator {
/*
	public static String generate(Transaction transaction) {
		if(transaction instanceof ReadTransaction) {
			return generate((ReadTransaction) transaction);
		}else if(transaction instanceof WriteTransaction) {
			return generate((WriteTransaction) transaction);
		}
		
		return "";
	}
	
	private static String generate(ReadTransaction transaction) {
		String query = transaction.getBranchName()+
				"@SELECT H.id, "
				+ "H.prov, "
				+ "H.mun, "
				+ "H.zone, "
				+ "H.brgy, "
				+ "H.purok, "
				+ "H.calam1_hwmny, "
				+ "H.calam2_hwmny, "
				+ "H.calam3_hwmny, "
				+ "H.calam4_hwmny, "
				+ "H.calam5_hwmny, "
				+ "H.calam6_hwmny, "
				+ "H.calam7_hwmny, "
				+ "H.calam8_hwmny, "
				+ "H.calam9_hwmny "
				+ "FROM db_hpq.hpq_hh H "
				+ "WHERE ";
		
		if(Client.PALAWAN.equals(transaction.getDatabase())) {
			query += "prov=1 ";
		} else if(Client.MARINDUQUE.equals(transaction.getDatabase())) {
			query += "prov=2 ";
		} else if(Client.CENTRAL.equals(transaction.getDatabase())){
			query += "(prov=1 OR prov=2) ";
		}
		
		String harvest = "AND (";
		for(String h : transaction.getSliceAndDiceHarvest().toString().split(",")) {
			harvest += " OR H.u_low_harv = " + h.split(" ")[0].trim();
		}
		harvest = harvest.replaceFirst("OR", "").trim();
		harvest += ") ";
		
		String fish = "AND (";
		for(String f : transaction.getSliceAndDiceFish().toString().split(",")) {
			fish += " OR H.u_low_fish = " + f.split(" ")[0].trim();
		}
		fish = fish.replaceFirst("OR", "").trim();
		fish += ") ";
		
		String animal = "AND (";
		for(String a : transaction.getSliceAndDiceAnimal().toString().split(",")) {
			animal += " OR H.u_low_lve = " + a.split(" ")[0].trim();
		}
		animal = animal.replaceFirst("OR", "").trim();
		animal += ") ";
		
		if(!transaction.getSliceAndDiceHarvest().toString().isEmpty()) {
			query += harvest;
		}
		if(!transaction.getSliceAndDiceFish().toString().isEmpty()) {
			query += fish;
		}
		if(!transaction.getSliceAndDiceAnimal().toString().isEmpty()) {
			query += animal;
		}
		
		return (query+"@"+transaction.getDatabase()).replaceAll("db_hpq", "db_hpq_"+transaction.getDatabase().toLowerCase());
	}
	
	private static String generate(WriteTransaction transaction, String type) {
		
		switch(type) { 
		case "UPDATE":String query = transaction.getBranchName()+"@UPDATE hpq_hh "
						+ "SET calam" + transaction.getCalamity().split(" ")[0] + "_hwmny=calam" + transaction.getCalamity().split(" ")[0] + "_hwmny+" + transaction.getFrequency()
						+ " WHERE id=" + transaction.getHouseholdID() + " LIMIT 1;";
						return query;
		case "INSERT":String query1 = transaction.getBranchName()+"@INSERT hpq_hh "
						+ "SET calam" + transaction.getCalamity().split(" ")[0] + "_hwmny=calam" + transaction.getCalamity().split(" ")[0] + "_hwmny+" + transaction.getFrequency()
						+ " WHERE id=" + transaction.getHouseholdID() + " LIMIT 1;";
						return query1;
		case "DELETE":String query2 = transaction.getBranchName()+"@DELETE hpq_hh "
						+ "WHERE calam" + transaction.getCalamity().split(" ")[0] + "_hwmny=calam" + transaction.getCalamity().split(" ")[0] + "_hwmny+" + transaction.getFrequency()
						+ "  AND id=" + transaction.getHouseholdID() + " LIMIT 1;";
						return query2;
		
		}
	}
	*/
}

