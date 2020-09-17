package com.thecloudyco.cashier.module.impl;
import java.util.Scanner;

import com.thecloudyco.cashier.Register;
import com.thecloudyco.cashier.module.CModule;

public class ShowBalance extends CModule {
	public ShowBalance() {
		super("SHOW BALANCE", false);
	}
	
	@Override
	public void execute(String[] args, Scanner sc) {
		double bal = Register.access().getBalance();
		
		if(bal > 0.00) {
			System.out.println("REMAINING BALANCE: " + bal);
		} else {
			System.out.println("CHANGE: " + Math.abs(bal));
		}
		return;
	}
}
