package com.thecloudyco.cashier;

import com.thecloudyco.cashier.transaction.Transaction;
import com.thecloudyco.cashier.user.Operator;
import com.thecloudyco.cashier.util.StringUtil;

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
	public Operator loggedInOperator;
	private Transaction transaction;
	
	public Register() {
	}
	
	public Transaction getTransaction() {
		return transaction;
	}
	
	public void createTransaction() {
		transaction = new Transaction(loggedInOperator);
	}
	
	public void voidTransaction() {
		transaction = null;
	}
	
	public double getBalance() {
		return BALANCE;
	}
	
	public double getReadableBalance() {
		return Double.valueOf(StringUtil.realBalance(String.valueOf(BALANCE)));
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
	
	public void setLoggedIn(Operator operator) {
		loggedInOperator = operator;
	}
	
	public boolean isLoggedIn() {
		if(loggedInOperator != null) {
			return true;
		}
		return false;
	}
	
	public Operator getLoggedIn() {
		return loggedInOperator;
	}
}
