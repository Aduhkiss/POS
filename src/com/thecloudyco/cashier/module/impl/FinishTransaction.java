package com.thecloudyco.cashier.module.impl;

import java.util.Scanner;

import com.thecloudyco.cashier.Register;
import com.thecloudyco.cashier.module.CModule;
import com.thecloudyco.cashier.util.ConsoleUtil;

public class FinishTransaction extends CModule {

	public FinishTransaction() {
		super("FINISH/CLEAR TRANSACTION", false);
	}

	@Override
	public void execute(String[] args, Scanner sc) {
		
		// Make sure that there is no remaining balance when trying to finish a transaction
		if(Register.access().getBalance() > 0.00) {
			ConsoleUtil.Print("ERROR", "There is still a remaining balance");
			return;
		}
		
		ConsoleUtil.Print("** THANK YOU **", "** COME AGAIN **");
		//TODO: Print Recipt
		//TODO: Clear the list of items that the customer is purchasing (or just completely reset it)
		Register.access().setBalance(0.00);
		return;
	}

}
