package com.thecloudyco.pos.module.impl.tender;

import java.util.Scanner;

import com.thecloudyco.override.api.ManagerAPI;
import com.thecloudyco.override.common.OverrideType;
import com.thecloudyco.pos.Register;
import com.thecloudyco.pos.module.CModule;
import com.thecloudyco.pos.transaction.Tender;
import com.thecloudyco.pos.util.ConsoleUtil;
import com.thecloudyco.pos.util.QuickMessage;

public class CashTender extends CModule {
	
	public CashTender() {
		super("CASH TENDER");
	}

	@Override
	public void execute(String[] args, Scanner sc) {
		
		if(Register.access().getTransaction() != null) {
		} else {
			ConsoleUtil.Print("ERROR", "There is no active transaction");
			return;
		}
		
		ConsoleUtil.Print("PLEASE ENTER", "CASH TENDER AMOUNT");
		double tender = 0.00;
		try {
			tender = Double.valueOf(sc.nextLine());
		} catch(IllegalArgumentException ex) { 
			ConsoleUtil.Print("ERROR", "Not a Number");
			return;
		}
		ManagerAPI mAPI = new ManagerAPI();
		
		// If the tender is larger then $120, we're going to need a Manager Override
		if(tender >= 120.00 || Register.access().getTransaction().getVoidedMoney() >= 50.00) {
			boolean flag = false;
			
			ConsoleUtil.Print("B003", "TR TRANSACTION LIMIT CHECK");
			System.out.println("\n");
			String override = sc.nextLine();
			
			try {
				flag = mAPI.isAuthorized(OverrideType.PIC, override);
			} catch (Exception e) {}
			
			if(!flag) {
				ConsoleUtil.Print("ERROR", "Not Authorized");
				return;
			} else {
				
				Register.access().getTransaction().voidLimitCheckBypassed = true;
				
				if(tender < 0.00) {
					ConsoleUtil.Print("ERROR", "Tender Amount must be positive number");
					return;
				}
				
				// TODO: Process and log the tender
				Register.access().removeBalance(tender);
				Register.access().getTransaction().addTender(new Tender("CASH TENDER", (tender * -1)));
				ConsoleUtil.Print("CASH TENDER PROCESSED", "$" + Register.access().getReadableBalance() + " REMAINING");				
				
				if(Register.access().getBalance() <= 0) {
					// Finish the transaction, and tell the cashier how much change to give the customer
					ConsoleUtil.Print("** THANK YOU **", "** COME AGAIN **");
					System.out.println("CHANGE OWED: " + Register.access().getReadableBalance());
					//TODO: Print Recipt
					//TODO: Clear the list of items that the customer is purchasing (or just completely reset it)
					Register.access().setBalance(0.00);
					Register.access().getTransaction().finish(true);
					Register.access().voidTransaction();
					return;
				}
			}
		} else {
			
			if(tender < 0.00) {
				ConsoleUtil.Print("ERROR", "Tender Amount must be positive number");
				return;
			}
			
			// TODO: Process and log the tender
			Register.access().removeBalance(tender);
			Register.access().getTransaction().addTender(new Tender("CASH TENDER", (tender * -1)));
			ConsoleUtil.Print("CASH TENDER PROCESSED", "$" + Register.access().getReadableBalance() + " REMAINING");
			
			
			if(Register.access().getBalance() <= 0) {
				// Finish the transaction, and tell the cashier how much change to give the customer
				ConsoleUtil.Print("** THANK YOU **", "** COME AGAIN **");
				System.out.println("CHANGE OWED: " + Register.access().getReadableBalance());
				//TODO: Print Recipt
				//TODO: Clear the list of items that the customer is purchasing (or just completely reset it)
				Register.access().setBalance(0.00);
				Register.access().getTransaction().finish(true);
				Register.access().voidTransaction();
				
				return;
			}
		}
	}

}
