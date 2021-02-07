package com.thecloudyco.pos.module.impl;

import java.io.IOException;
import java.util.Scanner;

import com.thecloudyco.override.common.HttpUtil;
import com.thecloudyco.pos.Main;
import com.thecloudyco.pos.Register;
import com.thecloudyco.pos.module.CModule;
import com.thecloudyco.pos.util.ConsoleUtil;

public class Signout extends CModule {
	
	public Signout() {
		super("SIGNOUT REGISTER");
	}

	@Override
	public void execute(String[] args, Scanner sc) {
		
		
		
		if(Register.access().getTransaction() != null) {
			ConsoleUtil.Print("ERROR", "Cannot Signout while a transaction is in progress");
			return;
		}
		
		String open_id = Register.access().getLoggedIn().getOperatorId();
		try {
			HttpUtil.get(Main.getConfig().getControllerAddress() + "/session/disconnect.php?operator=" + open_id + "&terminal=" + Main.getConfig().getTerminalNumber());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Register.access().setBalance(0.00);
		Register.access().loggedInOperator = null;
		
		ConsoleUtil.Clear();
	}

}
