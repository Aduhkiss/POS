package com.thecloudyco.cashier.module.impl;

import java.util.Scanner;

import com.thecloudyco.cashier.Register;
import com.thecloudyco.cashier.module.CModule;
import com.thecloudyco.cashier.util.ConsoleUtil;

public class Signout extends CModule {
	
	public Signout() {
		super("SIGNOUT REGISTER");
	}

	@Override
	public void execute(String[] args, Scanner sc) {
		if(Register.access().getTransaction() != null) {
			ConsoleUtil.Print("ERROR", "Cannot Signout while a transaction is in progress");
			return;
		}
		
		Register.access().setBalance(0.00);
		Register.access().loggedInOperator = null;
	}

}
