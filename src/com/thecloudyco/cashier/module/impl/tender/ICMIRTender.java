package com.thecloudyco.cashier.module.impl.tender;

import java.util.Scanner;

import com.thecloudyco.cashier.Register;
import com.thecloudyco.cashier.module.CModule;
import com.thecloudyco.cashier.transaction.Tender;
import com.thecloudyco.cashier.util.ConsoleUtil;
import com.thecloudyco.override.api.ManagerAPI;
import com.thecloudyco.override.common.OverrideType;

public class ICMIRTender extends CModule {
	
	public ICMIRTender() {
		super("I CAN MAKE IT RIGHT TENDER");
	}

	@Override
	public void execute(String[] args, Scanner sc) {
		
		if(Register.access().getTransaction() != null) {
		} else {
			ConsoleUtil.Print("ERROR", "There is no active transaction");
			return;
		}
		
		double bal = Register.access().getBalance();
		
		if(bal >= 20.00) {
			// Require a managers override if the balance is bigger then $20.00
			ConsoleUtil.Print("WARNING", "Requires MGR Override");
			String override = sc.nextLine();
			
			ManagerAPI mAPI = new ManagerAPI();
			
			boolean flag = false;
			try {
				flag = mAPI.isAuthorized(OverrideType.PIC, override);
			} catch (Exception e) {}
			
			if(!flag) {
				ConsoleUtil.Print("ERROR", "Not Authorized");
				return;
			} else {
				ConsoleUtil.Print("** THANK YOU **", "** FOR MAKING IT RIGHT **");
				Register.access().getTransaction().addTender(new Tender("ICMIR Tender", (Register.access().getReadableBalance() * -1)));
				Register.access().setBalance(0.00);
				Register.access().voidTransaction();
				return;
			}
		} else {
			ConsoleUtil.Print("** THANK YOU **", "** FOR MAKING IT RIGHT **");
			Register.access().getTransaction().addTender(new Tender("ICMIR Tender", (Register.access().getReadableBalance() * -1)));
			Register.access().setBalance(0.00);
			Register.access().voidTransaction();
			return;
		}
				
	}

}
