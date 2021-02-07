package com.thecloudyco.pos.module.impl;

import java.io.IOException;
import java.util.Scanner;

import com.thecloudyco.override.common.HttpUtil;
import com.thecloudyco.pos.Main;
import com.thecloudyco.pos.Register;
import com.thecloudyco.pos.items.Item;
import com.thecloudyco.pos.module.CModule;
import com.thecloudyco.pos.transaction.Tender;
import com.thecloudyco.pos.transaction.Transaction;
import com.thecloudyco.pos.util.ConsoleUtil;
import com.thecloudyco.pos.util.StringUtil;

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
		System.out.println("       YOUR CASHIER WAS " + t.getOperator().getFirstName().toUpperCase());
		System.out.println("\n");
		for(Item i : t.getItems()) {
			System.out.print("[" + i.getUpc() + "] ");
			System.out.print(i.getName() + " | ");
			System.out.print("$" + StringUtil.realBalance(String.valueOf(i.getMyPrice())) + "\n");
		}
		System.out.println("\n");
		for(Tender tender : t.getTenders()) {
			System.out.print(tender.getType() + " | ");
			System.out.print("$" + tender.getAmount() + "\n");
		}
		if(bal > 0.00) {
			System.out.println("BALANCE: " + StringUtil.realBalance(String.valueOf(bal)));
		} else {
			System.out.println("CHANGE: " + StringUtil.realBalance(String.valueOf(Math.abs(bal))));
			//System.out.println("CHANGE: " + Math.abs(bal));
		}
		System.out.println("                 END OF TRANSACTION REPORT");
		System.out.println("==========================================================");
		return;
	}

}
