package com.thecloudyco.pos.module.impl;

import java.util.Scanner;

import com.thecloudyco.pos.Register;
import com.thecloudyco.pos.module.CModule;
import com.thecloudyco.pos.secure.Secure;
import com.thecloudyco.pos.user.Operator;
import com.thecloudyco.pos.util.ConsoleUtil;

public class SecureTerminal extends CModule {
	public SecureTerminal() {
		super("SECURE TERMINAL", false);
	}
	
	@Override
	public void execute(String[] args, Scanner sc) {
		
		if(Register.access().getTransaction() != null) {
			ConsoleUtil.Print("B005", "OPERATION NOT AVAILABLE AT THIS TIME");
			return;
		} else {
			Secure.secureTerminal();
			Operator op = Register.access().getLoggedIn();
			System.out.println("  **  TERMINAL SECURED  **  ");
			System.out.println("   " + op.getOperatorId() + "   (" + op.getFirstName().toUpperCase() + ")");
		}
	}
}
