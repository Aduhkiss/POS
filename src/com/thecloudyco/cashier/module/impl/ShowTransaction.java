package com.thecloudyco.cashier.module.impl;

import java.io.IOException;
import java.util.Scanner;

import com.thecloudyco.cashier.Main;
import com.thecloudyco.cashier.Register;
import com.thecloudyco.cashier.items.Item;
import com.thecloudyco.cashier.module.CModule;
import com.thecloudyco.cashier.transaction.Tender;
import com.thecloudyco.cashier.transaction.Transaction;
import com.thecloudyco.cashier.util.ConsoleUtil;
import com.thecloudyco.override.common.HttpUtil;

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
		
		double bal = Register.access().getReadableBalance();
		
		ConsoleUtil.Clear();
		System.out.println("==========================================================");
		try {
			System.out.println("       " + HttpUtil.get(Main.getConfig().getControllerAddress() + "/?action=info&data=RECIPT_HEADER"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("YOUR CASHIER WAS " + t.getOperator().getFirstName().toUpperCase());
		System.out.println("\n");
		for(Item i : t.getItems()) {
			System.out.print("[" + i.getUpc() + "] ");
			System.out.print(i.getName() + " | ");
			System.out.print("$" + i.getMyPrice() + "\n");
		}
		System.out.println("\n");
		for(Tender tender : t.getTenders()) {
			System.out.print(tender.getType() + " | ");
			System.out.print("$" + tender.getAmount() + "\n");
		}
		if(bal > 0.00) {
			System.out.println("BALANCE: " + bal);
		} else {
			System.out.println("CHANGE: " + Math.abs(bal));
		}
		System.out.println("                 END OF TRANSACTION REPORT");
		System.out.println("==========================================================");
		return;
	}

}
