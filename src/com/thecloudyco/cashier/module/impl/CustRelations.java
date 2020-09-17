package com.thecloudyco.cashier.module.impl;

import java.util.Scanner;

import com.thecloudyco.cashier.Register;
import com.thecloudyco.cashier.module.CModule;
import com.thecloudyco.cashier.util.ConsoleUtil;

public class CustRelations extends CModule {

	public CustRelations() {
		super("CUSTOMER RELATIONS", true);
	}

	@Override
	public void execute(String[] args, Scanner sc) {
		ConsoleUtil.Print("PLEASE ENTER PRICE", "");
		double value = 0;
		try {
			value = Double.valueOf(sc.nextLine());
		} catch(IllegalArgumentException ex) {
			ConsoleUtil.Print("ERROR", "Invalid Number Given");
			return;
		}
		
		System.out.println("Done.");
		Register.access().removeBalance(value);
		return;
	}
	
}
