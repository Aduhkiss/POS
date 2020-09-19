package com.thecloudyco.cashier;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import com.thecloudyco.cashier.config.Config;
import com.thecloudyco.cashier.items.Item;
import com.thecloudyco.cashier.items.ItemManager;
import com.thecloudyco.cashier.module.CModule;
import com.thecloudyco.cashier.module.ModuleManager;
import com.thecloudyco.cashier.user.Operator;
import com.thecloudyco.cashier.util.ConsoleUtil;
import com.thecloudyco.cashier.util.StringUtil;
import com.thecloudyco.override.api.ManagerAPI;
import com.thecloudyco.override.common.OverrideType;
import com.thecloudyco.override.ent.ManagerProfile;

public class Main {

	public static void main(String[] args) {
		ModuleManager.registerModules();
		ManagerAPI mAPI = new ManagerAPI();
		Register r = Register.access();
		r.setBalance(0.00);
		
		try {
			ItemManager.downloadUPCs();
		} catch (IOException e1) {
			ConsoleUtil.Print("ERROR: Downloading UPCs! Please Contact your PIC!", e1.getMessage());
			//TODO: Automatically notify PIC's that this register crashed
			System.exit(1);
		}
		
		Scanner sc = new Scanner(System.in);
		
		for(int a = 0; a < 3; a++) {
			// Check if the current register is logged in
			if(!Register.access().isLoggedIn()) {
				System.out.println("** CLOSED **");
				String open_id = sc.nextLine();
				
				boolean flag = false;
				try {
					flag = mAPI.isAuthorized(OverrideType.CASHIER, open_id);
				} catch (Exception e) {}
				
				if(!flag) {
					ConsoleUtil.Print("ERROR", "Login Not Authorized");
				} else {
					System.out.println("** ENTER PASSWORD **");
					String password = sc.nextLine();
					ManagerProfile profile = null;
					try {
						profile = mAPI.getProfile(open_id);
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
					
					String theRealPassword = profile.getPassword();
					
					if(password.equals(theRealPassword)) {
						
						Register.access().setLoggedIn(new Operator(profile.getFirstName(), profile.getLastName(), open_id));
						
						ConsoleUtil.Print(Config.WELCOME_MESSAGE_LINE_ONE, Config.WELCOME_MESSAGE_LINE_TWO);
						System.out.println("** WELCOME " + profile.getFirstName().toUpperCase() + " **");
						
					} else {
						ConsoleUtil.Print("ERROR", "Check Password");
						return;
					}
				}
			}
		}
		
		for(int i = 0; i < 3; i++) {
			// Ask for input, then look for ITEMS or UPC's from the backend
			System.out.println("ENTER ITEM: ");
			String input = sc.nextLine();
			String[] origin = StringUtil.toArray(input);
			String[] arg = Arrays.copyOfRange(origin, 1, origin.length);
			
			CModule m = ModuleManager.getModule(origin[0].toUpperCase());
			
			if(m == null) {
				// Now run a UPC search
				boolean found = false;
				for(Item item : ItemManager.ItemList) {
					if(item.getUPC().equals(origin[0].toUpperCase())) {
						
						System.out.print(item.getName() + " = $" + item.getPrice());
						System.out.println("\n");
						Register.access().addBalance(item.getPrice());
						found = true;
					}
				}
				
				if(!found) {
					ConsoleUtil.Print("ITEM NOT FOUND", origin[0].toUpperCase());
				}
				
			} else {
				if(m.requiresMgrOverride()) {
					ConsoleUtil.Print("WARNING", "Requires MGR Override");
					String override = sc.nextLine();
					
					// Then check the override that was scanned
					boolean flag = false;
					try {
						flag = mAPI.isAuthorized(OverrideType.PIC, override);
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
