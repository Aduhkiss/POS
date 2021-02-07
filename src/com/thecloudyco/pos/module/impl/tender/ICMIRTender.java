package com.thecloudyco.pos.module.impl.tender;

import java.io.IOException;
import java.util.Scanner;

import com.thecloudyco.override.api.ManagerAPI;
import com.thecloudyco.override.common.OverrideType;
import com.thecloudyco.pos.Register;
import com.thecloudyco.pos.module.CModule;
import com.thecloudyco.pos.transaction.Tender;
import com.thecloudyco.pos.util.ConsoleUtil;
import com.thecloudyco.pos.util.QuickMessage;
import com.thecloudyco.pos.util.SoundUtils;

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
		
		if(bal >= 10.00 || Register.access().getTransaction().getVoidedMoney() >= 50.00) {
			// Require a managers override if the balance is bigger then $20.00
			SoundUtils.beep();
			ConsoleUtil.Print("B003", "TENDER AMOUNT LIMIT CHECK");
			System.out.println("\n");
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
				
				Register.access().getTransaction().voidLimitCheckBypassed = true;
				
				ConsoleUtil.Print("** THANK YOU **", "** FOR MAKING IT RIGHT **");
				Register.access().getTransaction().addTender(new Tender("ICMIR Tender", (Register.access().getReadableBalance() * -1)));
				Register.access().setBalance(0.00);
				Register.access().getTransaction().finish(true);
				Register.access().voidTransaction();
				return;
			}
		} else {
			ConsoleUtil.Print("** THANK YOU **", "** FOR MAKING IT RIGHT **");
			Register.access().getTransaction().addTender(new Tender("ICMIR Tender", (Register.access().getReadableBalance() * -1)));
			Register.access().setBalance(0.00);
			Register.access().getTransaction().finish(true);
			Register.access().voidTransaction();
			return;
		}
				
	}

}
