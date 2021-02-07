package com.thecloudyco.pos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.google.gson.Gson;
import com.thecloudyco.override.api.ManagerAPI;
import com.thecloudyco.override.common.HttpUtil;
import com.thecloudyco.override.common.OverrideType;
import com.thecloudyco.override.ent.ManagerProfile;
import com.thecloudyco.pos.config.ConfigFile;
import com.thecloudyco.pos.gui.util.POSGui;
import com.thecloudyco.pos.items.Item;
import com.thecloudyco.pos.module.CModule;
import com.thecloudyco.pos.module.ModuleManager;
import com.thecloudyco.pos.secure.Secure;
import com.thecloudyco.pos.tebex.TebexAPI;
import com.thecloudyco.pos.tebex.TebexPackage;
import com.thecloudyco.pos.user.Operator;
import com.thecloudyco.pos.util.ConsoleUtil;
import com.thecloudyco.pos.util.LinuxUtil;
import com.thecloudyco.pos.util.SoundUtils;
import com.thecloudyco.pos.util.StringUtil;

public class Main {
	
	private static ConfigFile theConfig;

	public static void main(String[] args) throws IOException {
		
		ConsoleUtil.Clear();
		Secure.timer = 0;
		
		ConsoleUtil.Print("CONTACTING", "CONTROLLER");
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
		try {
			if(!HttpUtil.get(theConfig.getControllerAddress() + "/?action=hello").equals("Hello World!")) {
			}
		} catch(ConnectException ex) {
			ConsoleUtil.Print("ERROR", "Unable to communicate with POS Controller!");
			System.exit(1);
		}
		
		ConsoleUtil.Clear();
		
		ModuleManager.registerModules();
		ManagerAPI mAPI = new ManagerAPI();
		Register r = Register.access();
		r.setBalance(0.00);

		if(System.getProperty("os.name").indexOf("Linux") >= 0) {
			LinuxUtil.lockSystem();
		}
		
		// Security System (Atticus)
		new Thread() {
			public void run() {
				if(Register.access().isLoggedIn() && !Secure.isSecured() && Register.access().getTransaction() == null) {
					int current = Secure.timer;
					Secure.timer = current + 1;
					//System.out.println(Secure.timer);
					
					if(Secure.timer >= 60) {
						Secure.secureTerminal();
						Operator op = Register.access().getLoggedIn();
						System.out.println("  **  TERMINAL SECURED  **  ");
						System.out.println("   " + op.getOperatorId() + "   (" + op.getFirstName().toUpperCase() + ")");
					}
				}
				try {
					Thread.sleep(1000);
					this.run();
				} catch (InterruptedException | StackOverflowError e) {
					//e.printStackTrace();
				}
				
			}
		}.start();
		
		Scanner sc = new Scanner(System.in);
		
		login(sc, mAPI);
		
		for(int i = 0; i < 200; i++) {
			// Always check to make sure you are logged in
			if(!Register.access().isLoggedIn()) {
				login(sc, mAPI);
			} else {
				
				if(Secure.isSecured()) {
					String input = sc.nextLine();
					Operator op = Register.access().getLoggedIn();
					ManagerProfile profile = null;
					try {
						profile = mAPI.getProfile(op.getOperatorId());
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
					
					// Check if the operator is trying to override
					if(input.equalsIgnoreCase("OVERRIDE") || input.equalsIgnoreCase("SIGNOUT")) {
						
						SoundUtils.beep();
						ConsoleUtil.Clear();
						ConsoleUtil.Print("B003", "REQUIRES MGR OVERRIDE. PLEASE SCAN OVERRIDE CARD");
						System.out.println("\n");
						
						boolean flag = false;
						try {
							flag = mAPI.isAuthorized(OverrideType.PIC, sc.nextLine());
						} catch (Exception e) {}
						
						if(!flag) {
							ConsoleUtil.Print("B911", "Not Authorized");
						} else {
							Register.access().setBalance(0.00);
							Register.access().loggedInOperator = null;
							ConsoleUtil.Clear();
							
							String open_id = Register.access().getLoggedIn().getOperatorId();
							try {
								HttpUtil.get(Main.getConfig().getControllerAddress() + "/session/disconnect.php?operator=" + open_id + "&terminal=" + Main.getConfig().getTerminalNumber());
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						
					} else {
						if(mAPI.authenticate(op, input)) {
							Secure.unlockTerminal();
						} else {
							ConsoleUtil.Print("ERR", "Check Password");
							System.out.println("\n");
							Operator oop = Register.access().getLoggedIn();
							System.out.println("  **  TERMINAL SECURED  **  ");
							System.out.println("   " + oop.getOperatorId() + "   (" + oop.getFirstName().toUpperCase() + ")");
						}
					}
				} else {

					// Ask for input, then look for ITEMS or UPC's from the backend
					System.out.println("ENTER ITEM:   ");
					String input = sc.nextLine();
					if(Secure.isSecured()) {
						Operator op = Register.access().getLoggedIn();
						ManagerProfile profile = null;
						try {
							profile = mAPI.getProfile(op.getOperatorId());
						} catch (IOException | InterruptedException e) {
							e.printStackTrace();
						}
						
						if(mAPI.authenticate(op, input)) {
							Secure.unlockTerminal();
						} else {
							ConsoleUtil.Print("ERR", "Check Password");
							System.out.println("\n");
							Operator oop = Register.access().getLoggedIn();
							System.out.println("  **  TERMINAL SECURED  **  ");
							System.out.println("   " + oop.getOperatorId() + "   (" + oop.getFirstName().toUpperCase() + ")");
						}
					} else {
						// Reset the security timer
						Secure.timer = 0;
						
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
								
								boolean allowed = false;
								if(item.isRestricted() == 1) {
									SoundUtils.beep();
									ConsoleUtil.Print("MO", "RESTRICTED PLU/UPC LIMIT CHECK");
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
										
										try {
											cQTY = sc.nextInt();
										} catch(InputMismatchException ex) {
											
										}
										
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
							
							
						} else {
							if(m.requiresMgrOverride()) {
								SoundUtils.beep();
								ConsoleUtil.Clear();
								ConsoleUtil.Print("MO", "REQUIRES MGR OVERRIDE. PLEASE SCAN OVERRIDE CARD");
								System.out.println("\n");
								String override = sc.nextLine();
								
								// Then check the override that was scanned
								boolean flag = false;
								try {
									flag = mAPI.isAuthorized(OverrideType.PIC, override);
								} catch (Exception e) {}
								
								if(!flag) {
									ConsoleUtil.Print("ERR", "Not Authorized");
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
		}
	}
	
	public static ConfigFile getConfig() {
		return theConfig;
	}
	
	public static void login(Scanner sc, ManagerAPI mAPI) throws IOException {
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
					ConsoleUtil.Print("ERR", "Operator ID Not Recognized");
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
					
					if(mAPI.authenticate(open_id, password)) {
						
						
						// Check if the cashier is logged into another register on the same controller, if they are, we want to stop them from logging in here
						// tell them which one they're on, but also give them a chance to override it, if they can
						String activity = HttpUtil.get(theConfig.getControllerAddress() + "/session?operator=" + open_id);
						if(activity.equals("INACTIVE")) {
							Register.access().setLoggedIn(new Operator(profile.getFirstName(), profile.getLastName(), open_id));
							HttpUtil.get(theConfig.getControllerAddress() + "/session/connect.php?operator=" + open_id + "&terminal=" + theConfig.getTerminalNumber());
							
							try {
								ConsoleUtil.Print(HttpUtil.get(theConfig.getControllerAddress() + "/?action=info&data=WELCOME_MESSAGE_LINE_ONE"), HttpUtil.get(theConfig.getControllerAddress() + "/?action=info&data=WELCOME_MESSAGE_LINE_TWO"));
							} catch (IOException e) {
								e.printStackTrace();
							}
							System.out.println("\n** WELCOME " + profile.getFirstName().toUpperCase() + " **");
							Secure.unlockTerminal();
						} else {
							ConsoleUtil.Print("MO", "OPERATOR STILL ACTIVE ON " + activity);
							System.out.println("\n");
							boolean da = false;
							try {
								da = mAPI.isAuthorized(OverrideType.PIC, sc.nextLine());
							} catch (Exception e) {}
							
							if(!da) {
								ConsoleUtil.Print("ERR", "Not Authorized");
								System.out.println("\n");
							} else {
								
								Register.access().setLoggedIn(new Operator(profile.getFirstName(), profile.getLastName(), open_id));
								HttpUtil.get(theConfig.getControllerAddress() + "/session/connect.php?operator=" + open_id + "&terminal=" + theConfig.getTerminalNumber());
								try {
									ConsoleUtil.Print(HttpUtil.get(theConfig.getControllerAddress() + "/?action=info&data=WELCOME_MESSAGE_LINE_ONE"), HttpUtil.get(theConfig.getControllerAddress() + "/?action=info&data=WELCOME_MESSAGE_LINE_TWO"));
								} catch (IOException e) {
									e.printStackTrace();
								}
								System.out.println("\n** WELCOME " + profile.getFirstName().toUpperCase() + " **");
								Secure.unlockTerminal();
							}
						}
					} else {
						ConsoleUtil.Print("ERR", "Check Password");
						System.out.println("\n");
					}
				}
			}
		}
	}

}
