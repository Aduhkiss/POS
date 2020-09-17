package com.thecloudyco.cashier;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import com.thecloudyco.cashier.config.Config;
import com.thecloudyco.cashier.items.Item;
import com.thecloudyco.cashier.items.ItemManager;
import com.thecloudyco.cashier.module.CModule;
import com.thecloudyco.cashier.module.ModuleManager;
import com.thecloudyco.cashier.util.ConsoleUtil;
import com.thecloudyco.cashier.util.StringUtil;
import com.thecloudyco.override.api.ManagerAPI;
import com.thecloudyco.override.common.OverrideType;

public class Main {

	public static void main(String[] args) {
		
		ConsoleUtil.Print(Config.WELCOME_MESSAGE_LINE_ONE, Config.WELCOME_MESSAGE_LINE_TWO);
		
		ModuleManager.registerModules();
		ManagerAPI mAPI = new ManagerAPI();
		Register r = Register.access();
		r.setBalance(0.00);
		
		try {
			ItemManager.downloadUPCs();
		} catch (IOException e1) {
			ConsoleUtil.Print("ERROR: Downloading UPCs", e1.getMessage());
			System.exit(1);
			//e1.printStackTrace();
		}
		
//		Gson gson = new Gson();
//		ItemManager.ItemList.add(new Item("Water Bottle", 2.46, "22222222"));
//		ItemManager.ItemList.add(new Item("Beef", 56.54, "22222222"));
//		ItemManager.ItemList.add(new Item("Steak", 3.65, "22222222"));
//		
//		System.out.println(gson.toJson(ItemManager.ItemList));
		
		//TODO: Setup how the config will work, and how it connects to the backend JSON server
		
		for(int i = 0; i < 3; i++) {
			Scanner sc = new Scanner(System.in);
			// Ask for input, then look for ITEMS or UPC's from the backend
			System.out.println("ENTER ITEM: ");
			String input = sc.nextLine();
			String[] origin = StringUtil.toArray(input);
			String[] arg = Arrays.copyOfRange(origin, 1, origin.length);
			
			CModule m = ModuleManager.getModule(origin[0]);
			
			if(m == null) {
				//
				
				// Now run a UPC search
				boolean found = false;
				for(Item item : ItemManager.ItemList) {
					if(item.getUPC().equals(origin[0])) {
						
						System.out.print(item.getName() + " = $" + item.getPrice());
						System.out.println("\n");
						Register.access().addBalance(item.getPrice());
						found = true;
					}
				}
				
				if(!found) {
					ConsoleUtil.Print("ITEM NOT FOUND", origin[0]);
				}
				
			} else {
				if(m.requiresMgrOverride()) {
					ConsoleUtil.Print("WARNING", "Requires MGR Override");
					String override = sc.nextLine();
					
					// Then check the override that was scanned
					boolean flag = false;
					try {
						flag = mAPI.isAuthorized(OverrideType.CASHIER, override);
					} catch (Exception e) {}
					
					if(!flag) {
						ConsoleUtil.Print("ERROR", "Not Authorized");
					} else {
						m.execute(arg, sc);
					}
				} else {
					m.execute(arg, sc);
				}
			}
			
			// To reset the variable so the for statement literally never ends LOL
			i = 0;
		}
	}

}
