package com.thecloudyco.cashier.module.impl;

import java.util.Scanner;

import com.thecloudyco.cashier.Register;
import com.thecloudyco.cashier.module.CModule;
import com.thecloudyco.cashier.util.ConsoleUtil;
import com.thecloudyco.override.api.ManagerAPI;
import com.thecloudyco.override.common.OverrideType;

public class VoidTotal extends CModule {
	public VoidTotal() {
		super("VOID TRANSACTION");
	}

	@Override
	public void execute(String[] args, Scanner sc) {
		double balance = Register.access().getBalance();
		
		// If the balance is over $20, we require a manager override
		if(balance >= 20.00) {
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
				ConsoleUtil.Print("** VOID TRANSACTION **", "** THANK YOU **");
				Register.access().setBalance(0.00);
				return;
			}
			
		} else {
			ConsoleUtil.Print("** VOID TRANSACTION **", "** THANK YOU **");
			Register.access().setBalance(0.00);
			return;
		}
	}

}
