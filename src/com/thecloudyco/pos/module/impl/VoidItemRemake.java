package com.thecloudyco.pos.module.impl;

import java.util.ConcurrentModificationException;
import java.util.Scanner;

import com.thecloudyco.pos.Register;
import com.thecloudyco.pos.items.Item;
import com.thecloudyco.pos.module.CModule;
import com.thecloudyco.pos.util.ConsoleUtil;

public class VoidItemRemake extends CModule {
	
	public VoidItemRemake() {
		super("VOID ITEM REMAKE");
	}

	@Override
	public void execute(String[] args, Scanner sc) {
		ConsoleUtil.Print("PLEASE SCAN OR KEY", "ITEM TO BE VOIDED");
		System.out.println("\n");
		String item_upc = sc.nextLine();
		
		boolean found_it = false;
		
		try {
			for(Item it : Register.access().getTransaction().getItems()) {
				if(it.getUpc().equals(item_upc)) {
					found_it = true;
					// Remove this item from the transaction entirely
					
					Register.access().getTransaction().voidItem(it);
					Register.access().removeBalance(it.getMyPrice());
					//System.out.println("[DEBUG] Removed: " + it.getMyPrice() + " from balance.");
					
					System.out.println("[VOID] " + it.getName() + " | -$" + it.getMyPrice());
				}
			}
		} catch(ConcurrentModificationException ex) {}
		
		if(!found_it) {
			ConsoleUtil.Print("B005", "NO ITEM FOUND");
			System.out.println("\n");
		}
		
		return;
	}

}
