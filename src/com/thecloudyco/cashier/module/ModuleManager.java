package com.thecloudyco.cashier.module;

import java.util.HashMap;
import java.util.Map;

import com.thecloudyco.cashier.module.impl.CustRelations;
import com.thecloudyco.cashier.module.impl.OpenGUI;
import com.thecloudyco.cashier.module.impl.ShowBalance;
import com.thecloudyco.cashier.module.impl.ShowTransaction;
import com.thecloudyco.cashier.module.impl.Signout;
import com.thecloudyco.cashier.module.impl.VoidItem;
import com.thecloudyco.cashier.module.impl.VoidTotal;
import com.thecloudyco.cashier.module.impl.tender.CashTender;
import com.thecloudyco.cashier.module.impl.tender.ICMIRTender;

public class ModuleManager {
	private static Map<String, CModule> Modules = new HashMap<>();
	
	public static void registerModules() {
		// Called from the main class when we start everything up
		// this method will be where we add everything to the hashmap for the program to access
		
		// Numeric Modules
		Modules.put("6667", new CustRelations());
		Modules.put("", new ShowBalance());
		Modules.put("1500", new ShowTransaction());
		Modules.put("0001", new OpenGUI());
		
		// uhh.. not numeric modules
		Modules.put("VOIDTOTAL", new VoidTotal());
		Modules.put("VOID", new VoidItem());
		Modules.put("SIGNOUT", new Signout());
		
		// Tender Modules
		Modules.put("CASH", new CashTender());
		Modules.put("ICANMAKEITRIGHT", new ICMIRTender());
		
	}
	
	public static CModule getModule(String id) {
		if(Modules.get(id) != null) {
			return Modules.get(id);
		}
		return null;
	}
}