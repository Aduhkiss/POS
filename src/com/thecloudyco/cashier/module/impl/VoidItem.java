package com.thecloudyco.cashier.module.impl;

import java.util.Scanner;

import com.thecloudyco.cashier.Register;
import com.thecloudyco.cashier.items.Item;
import com.thecloudyco.cashier.items.ItemManager;
import com.thecloudyco.cashier.module.CModule;
import com.thecloudyco.cashier.util.ConsoleUtil;

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
		if(!Register.access().getTransaction().exists(item_upc)) {
			ConsoleUtil.Print("ERROR", "No Item exists with that UPC on Transaction");
			return;
		}
		
		// Remove the item from the transaction
		Item i = ItemManager.getItemFromUPC(item_upc);
		Register.access().getTransaction().voidItem(item_upc);
		Register.access().removeBalance(i.getPrice());
		
		System.out.println("[VOID] " + i.getName() + " | -$" + i.getPrice());
		return;
	}
}
