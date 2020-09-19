package com.thecloudyco.cashier.module.impl;
import java.util.Scanner;

import com.thecloudyco.cashier.Register;
import com.thecloudyco.cashier.module.CModule;
import com.thecloudyco.cashier.util.StringUtil;

public class ShowBalance extends CModule {
	public ShowBalance() {
		super("SHOW BALANCE", false);
	}
	
	@Override
	public void execute(String[] args, Scanner sc) {
		double bal = Register.access().getReadableBalance();
		
		if(bal > 0.00) {
			System.out.println("BALANCE: " + bal);
		} else {
			System.out.println("CHANGE: " + Math.abs(bal));
		}
		return;
	}
}
