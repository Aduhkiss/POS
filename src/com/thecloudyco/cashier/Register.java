package com.thecloudyco.cashier;

public class Register {
	public static Register me;
	public static Register access() {
		if(me == null) {
			me = new Register();
		}
		return me;
	}
	
	// All the variables that we need to access in other parts of the program
	private double BALANCE;
	
	public Register() {
		
	}
	
	public double getBalance() {
		return BALANCE;
	}
	
	public void removeBalance(double money) {
		BALANCE = BALANCE - money;
	}
	
	public void addBalance(double money) {
		BALANCE = BALANCE + money;
	}
	
	public void setBalance(double money) {
		BALANCE = money;
	}
}
