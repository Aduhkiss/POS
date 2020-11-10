package com.thecloudyco.cashier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import com.google.gson.Gson;
import com.thecloudyco.cashier.config.ConfigFile;
import com.thecloudyco.cashier.gui.util.POSGui;
import com.thecloudyco.cashier.items.Item;
import com.thecloudyco.cashier.module.CModule;
import com.thecloudyco.cashier.module.ModuleManager;
import com.thecloudyco.cashier.user.Operator;
import com.thecloudyco.cashier.util.ConsoleUtil;
import com.thecloudyco.cashier.util.LinuxUtil;
import com.thecloudyco.cashier.util.QuickMessage;
import com.thecloudyco.cashier.util.StringUtil;
import com.thecloudyco.override.api.ManagerAPI;
import com.thecloudyco.override.common.HttpUtil;
import com.thecloudyco.override.common.OverrideType;
import com.thecloudyco.override.ent.ManagerProfile;

public class Main {
	
	private static ConfigFile theConfig;

	public static void main(String[] args) throws IOException {
		ConsoleUtil.Clear();
		
		ConsoleUtil.Print("SEARCHING FOR", "SYSTEM CONFIG...");
		StringBuilder con = new StringBuilder();
		try {
			File configFile = new File("pos.config");
			Scanner sc = new Scanner(configFile);
			while(sc.hasNextLine()) {
				String data = sc.nextLine();
				con.append(data);
			}
			sc.close();
		} catch(FileNotFoundException ex) {
			System.out.println("Uh oh.... It looks like we were unable to find a config file! Make sure to download a sample from our website, and customize it to you're settings!");
			System.exit(1);
		}
		
		Gson gson = new Gson();
		
		theConfig = gson.fromJson(con.toString(), ConfigFile.class);
		
		// Connect to the PoS Controller Server to make sure everythings connected.
		if(!HttpUtil.get(theConfig.getControllerAddress() + "/?action=hello").equals("Hello World!")) {
			ConsoleUtil.Print("ERROR", "Unable to communicate with PoS Controller!");
			System.exit(1);
		}
		
		ConsoleUtil.Clear();
		
		// Check to see if the program is supposed to be started with the GUI
		try {
			if(args[0].equalsIgnoreCase("-gui")) {
				// Start the GUI
				POSGui.main();
			}
		} catch(Exception ex) {}
		
		ModuleManager.registerModules();
		ManagerAPI mAPI = new ManagerAPI();
		Register r = Register.access();
		r.setBalance(0.00);
		
		//System.out.println(System.getProperty("os.name"));
		if(System.getProperty("os.name").indexOf("Linux") >= 0) {
			LinuxUtil.lockSystem();
		}
		
//		try {
//			ItemManager.downloadUPCs();
//		} catch (IOException e1) {
//			ConsoleUtil.Print("ERROR: Downloading UPCs! Please Contact your PIC!", e1.getMessage());
//			//TODO: Automatically notify PIC's that this register crashed
//			System.exit(1);
//		}
		
		Scanner sc = new Scanner(System.in);
		
		login(sc, mAPI);
		
		for(int i = 0; i < 3; i++) {
			// Always check to make sure you are logged in
			if(!Register.access().isLoggedIn()) {
				login(sc, mAPI);
			} else {
				// Ask for input, then look for ITEMS or UPC's from the backend
				System.out.println("ENTER ITEM: ");
				String input = sc.nextLine();
				String[] origin = StringUtil.toArray(input);
				String[] arg = Arrays.copyOfRange(origin, 1, origin.length);
				
				CModule m = ModuleManager.getModule(origin[0].toUpperCase());
				
				if(m == null) {
					// Now run a UPC search
					
					// Ask the controller if this item exists
					if(HttpUtil.get(theConfig.getControllerAddress() + "/item/?upc=" + origin[0]).indexOf("C002 ") >= 0) {
						ConsoleUtil.Print("ITEM NOT FOUND", origin[0].toUpperCase());
					} else {
						Item item = gson.fromJson(HttpUtil.get(theConfig.getControllerAddress() + "/item/?upc=" + origin[0]), Item.class);
						
						System.out.println(item.isRestricted());
						
						boolean allowed = false;
						if(item.isRestricted() == 1) {
							ConsoleUtil.Print("B003", "RESTRICTED PLU/UPC LIMIT CHECK");
							System.out.println("\n");
							String override = sc.nextLine();
							
							// Then check the override that was scanned
							boolean flag = false;
							try {
								flag = mAPI.isAuthorized(OverrideType.PIC, override);
							} catch (Exception e) {}
							
							if(!flag) {
								ConsoleUtil.Print("ERROR", "Not Authorized");
							} else {
								allowed = true;
								ConsoleUtil.Clear();
							}
						} else {
							allowed = true;
						}
						
						if(allowed) {
							double cPrice = item.getPrice();
							int cQTY = 1;
							double cWeight = 0.00;
							
							if(item.isCustom_price() == 1) {
								ConsoleUtil.Print("ENTER ITEM PRICE", "");
								cPrice = sc.nextDouble();
								
								System.out.print(item.getName() + " | $" + StringUtil.realBalance((cPrice * 1)));
								System.out.println("\n");
								Register.access().addBalance(StringUtil.realBalance((cPrice * 1)));
								item.setMyPrice(StringUtil.realBalance((cPrice * 1)));
								if(Register.access().getTransaction() == null) {
									Register.access().createTransaction();
								}
								Register.access().getTransaction().addItem(item);
							}
							else if(item.isQuantity() == 1) {
								ConsoleUtil.Print("ENTER ITEM QUANTITY", "");
								cQTY = sc.nextInt();
								
								System.out.print("(" + cQTY + " @ " + cPrice + ") " + item.getName() + " | $" + StringUtil.realBalance((cPrice * cQTY)));
								System.out.println("\n");
								Register.access().addBalance(StringUtil.realBalance((cPrice * cQTY)));
								item.setMyPrice(StringUtil.realBalance((cPrice * cQTY)));
								if(Register.access().getTransaction() == null) {
									Register.access().createTransaction();
								}
								Register.access().getTransaction().addItem(item);
							}
							else if(item.isWeight() == 1) {
								ConsoleUtil.Print("ENTER ITEM WEIGHT", "");
								cWeight = sc.nextDouble();
								
								System.out.print("(" + cWeight + ") @ $" + item.getPrice_per_pound() + "/lb | " + item.getName() + " | $" + StringUtil.realBalance((item.getPrice_per_pound() * cWeight)));
								System.out.println("\n");
								Register.access().addBalance(StringUtil.realBalance((item.getPrice_per_pound() * cWeight)));
								item.setMyPrice(StringUtil.realBalance((item.getPrice_per_pound() * cWeight)));
								if(Register.access().getTransaction() == null) {
									Register.access().createTransaction();
								}
								Register.access().getTransaction().addItem(item);
							}
							
							else {								
								System.out.print(item.getName() + " | $" + StringUtil.realBalance((cPrice * 1)));
								System.out.println("\n");
								Register.access().addBalance(StringUtil.realBalance((cPrice * 1)));
								item.setMyPrice(StringUtil.realBalance((cPrice * 1)));
								if(Register.access().getTransaction() == null) {
									Register.access().createTransaction();
								}
								Register.access().getTransaction().addItem(item);
							}
						}
					}
					
					
//					for(Item item : ItemManager.ItemList) {
//						if(item.getUPC().equals(origin[0].toUpperCase())) {
//							
//							System.out.print(item.getName() + " | $" + item.getPrice());
//							System.out.println("\n");
//							Register.access().addBalance(item.getPrice());
//							if(Register.access().getTransaction() == null) {
//								Register.access().createTransaction();
//							}
//							Register.access().getTransaction().addItem(item);
//							found = true;
//						}
//					}
					
//					if(!found) {
//						ConsoleUtil.Print("ITEM NOT FOUND", origin[0].toUpperCase());
//					}
					
				} else {
					if(m.requiresMgrOverride()) {
						QuickMessage.mgrOverride();
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
							ConsoleUtil.Clear();
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
	
	public static ConfigFile getConfig() {
		return theConfig;
	}
	
	public static void login(Scanner sc, ManagerAPI mAPI) {
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
					ConsoleUtil.Print("B011", "Operator ID Not Recognized");
					System.out.println("\n");
				} else {
					ConsoleUtil.Clear();
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
						
						try {
							ConsoleUtil.Print(HttpUtil.get(theConfig.getControllerAddress() + "/?action=info&data=WELCOME_MESSAGE_LINE_ONE"), HttpUtil.get(theConfig.getControllerAddress() + "/?action=info&data=WELCOME_MESSAGE_LINE_TWO"));
						} catch (IOException e) {
							e.printStackTrace();
						}
						System.out.println("\n** WELCOME " + profile.getFirstName().toUpperCase() + " **");
						
					} else {
						ConsoleUtil.Print("B012", "Check Password");
						System.out.println("\n");
					}
				}
			}
		}
	}

}
