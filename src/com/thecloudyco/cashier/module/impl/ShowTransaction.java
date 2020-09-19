package com.thecloudyco.cashier.module.impl;

import java.util.Scanner;

import com.thecloudyco.cashier.Register;
import com.thecloudyco.cashier.config.Config;
import com.thecloudyco.cashier.items.Item;
import com.thecloudyco.cashier.module.CModule;
import com.thecloudyco.cashier.transaction.Tender;
import com.thecloudyco.cashier.transaction.Transaction;
import com.thecloudyco.cashier.util.ConsoleUtil;

public class ShowTransaction extends CModule {
	
	public ShowTransaction() {
		super("SHOW TRANSACTION");
	}

	@Override
	public void execute(String[] args, Scanner sc) {
		Transaction t = null;
		if(Register.access().getTransaction() != null) {
			t = Register.access().getTransaction();
		} else {
			ConsoleUtil.Print("ERROR", "There is no active transaction");
			return;
		}
		
		ConsoleUtil.Clear();
		System.out.println("==========================================================");
		System.out.println("       " + Config.RECIPT_HEADER);
		System.out.println("YOUR CASHIER WAS " + t.getOperator().getFirstName().toUpperCase());
		System.out.println("\n");
		for(Item i : t.getItems()) {
			System.out.print("[" + i.getUPC() + "] ");
			System.out.print(i.getName() + " | ");
			System.out.print("$" + i.getPrice() + "\n");
		}
		System.out.println("\n");
		for(Tender tender : t.getTenders()) {
			System.out.print(tender.getType() + " | ");
			System.out.print("$" + tender.getAmount() + "\n");
		}
		
		System.out.println("\n");
		System.out.println("                 END OF TRANSACTION REPORT");
		System.out.println("==========================================================");
		return;
	}

}
