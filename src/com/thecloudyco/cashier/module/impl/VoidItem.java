package com.thecloudyco.cashier.module.impl;

import java.io.IOException;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.thecloudyco.cashier.Main;
import com.thecloudyco.cashier.Register;
import com.thecloudyco.cashier.items.Item;
import com.thecloudyco.cashier.module.CModule;
import com.thecloudyco.cashier.util.ConsoleUtil;
import com.thecloudyco.override.common.HttpUtil;

public class VoidItem extends CModule {
	public VoidItem() {
		super("VOID ITEM");
	}

	@Override
	public void execute(String[] args, Scanner sc) {
		// Ask the operator to give the item to void from the transaction
		ConsoleUtil.Print("PLEASE SCAN OR KEY", "ITEM TO BE VOIDED");
		System.out.println("\n");
		String item_upc = sc.nextLine();
		// Okay, now that we should have the UPC, make sure it exists on the transaction
		try {
			if(!Register.access().getTransaction().exists(item_upc)) {
				ConsoleUtil.Print("ERROR", "No Item exists with that UPC on Transaction");
				return;
			}
			
			// Remove the item from the transaction
			Gson gson = new Gson();
			Item i = null;
			try {
				i = gson.fromJson(HttpUtil.get(Main.getConfig().getControllerAddress() + "/item/?upc=" + item_upc), Item.class);
			} catch (JsonSyntaxException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Register.access().getTransaction().voidItem(i);
			Register.access().removeBalance(i.getPrice());
			
			System.out.println("[VOID] " + i.getName() + " | -$" + i.getPrice());
			return;
		} catch(NullPointerException ex) {
			ConsoleUtil.Print("ERROR", "No Item exists with that UPC on Transaction");
			return;
		}
	}
}
