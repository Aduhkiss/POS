package com.thecloudyco.cashier.receipt;

public class Receipt {
	
	private StringBuilder builder;
	
	public Receipt() {
		builder = new StringBuilder();
		/*
		 * I'm going to hold off on making receipts entirely because printing them
		 * looks like its going to take 8 billion years to figure out how to do.
		 */
		
		// Run the header
		builder.append(" ** T H E  C L O U D Y  C O ** ");
		builder.append("\n");
		builder.append("YOUR CASHIER WAS ATTICUS"); //TODO: When we add a user control system,
	}
	
	@Override
	public String toString() {
		return "";
	}
}
