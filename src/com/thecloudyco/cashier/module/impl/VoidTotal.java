package com.thecloudyco.cashier.module.impl;

import java.io.IOException;
import java.util.Scanner;

import com.thecloudyco.cashier.Register;
import com.thecloudyco.cashier.module.CModule;
import com.thecloudyco.cashier.util.ConsoleUtil;
import com.thecloudyco.cashier.util.QuickMessage;
import com.thecloudyco.override.api.ManagerAPI;
import com.thecloudyco.override.common.OverrideType;

public class VoidTotal extends CModule {
	public VoidTotal() {
		super("VOID TRANSACTION");
	}

	@Override
	public void execute(String[] args, Scanner sc) {
		double balance = Register.access().getBalance();
		
		// If the balance is over $40, we require a manager override
		if(balance >= 40.00) {
			QuickMessage.mgrOverride();
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
				ConsoleUtil.Print("** VOID TRANSACTION **", "** THANK YOU **");
				Register.access().getTransaction().finish(false);
				Register.access().voidTransaction();
				return;
			}
			
		} else {
			ConsoleUtil.Print("** VOID TRANSACTION **", "** THANK YOU **");
			Register.access().setBalance(0.00);
			Register.access().getTransaction().finish(false);
			Register.access().voidTransaction();
			return;
		}
	}

}
