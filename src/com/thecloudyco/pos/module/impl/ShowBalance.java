package com.thecloudyco.pos.module.impl;
import java.util.Scanner;

import com.thecloudyco.pos.Register;
import com.thecloudyco.pos.module.CModule;
import com.thecloudyco.pos.util.StringUtil;

public class ShowBalance extends CModule {
	public ShowBalance() {
		super("SHOW BALANCE", false);
	}
	
	@Override
	public void execute(String[] args, Scanner sc) {
		double bal = Register.access().getReadableBalance();
		
		if(bal > 0.00) {
			System.out.println("BALANCE: " + StringUtil.realBalance(String.valueOf(bal)));
		} else {
			System.out.println("CHANGE: " + StringUtil.realBalance(String.valueOf(Math.abs(bal))));
		}
		return;
	}
}
