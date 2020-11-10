package com.thecloudyco.cashier.module.impl;

import java.util.Scanner;

import com.thecloudyco.cashier.gui.util.POSGui;
import com.thecloudyco.cashier.module.CModule;

public class OpenGUI extends CModule {
	public OpenGUI() {
		super("OPEN TERMINAL GUI", true);
	}
	
	@Override
	public void execute(String[] args, Scanner sc) {
		POSGui.main();
	}
}
