package com.thecloudyco.cashier.transaction;

public class Tender {
	public String type;
	public double amount;
	
	public Tender(String type, double amount) {
		this.type = type;
		this.amount = amount;
	}

	public String getType() {
		return type;
	}

	public double getAmount() {
		return amount;
	}
}
