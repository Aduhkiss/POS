package com.thecloudyco.cashier.module.impl;

import java.io.IOException;
import java.util.Scanner;

import com.thecloudyco.cashier.items.ItemManager;
import com.thecloudyco.cashier.module.CModule;
import com.thecloudyco.cashier.util.ConsoleUtil;

public class RefreshUPC extends CModule {

	public RefreshUPC() {
		super("REFRESH UPCS");
	}

	@Override
	public void execute(String[] args, Scanner sc) {
		try {
			ItemManager.downloadUPCs();
		} catch (IOException e1) {
			ConsoleUtil.Print("ERROR: Downloading UPCs", e1.getMessage());
			System.exit(1);
			return;
		}
		return;
	}
}
