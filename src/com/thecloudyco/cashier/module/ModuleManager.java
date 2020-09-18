package com.thecloudyco.cashier.module;

import java.util.HashMap;
import java.util.Map;

import com.thecloudyco.cashier.module.impl.CustRelations;
import com.thecloudyco.cashier.module.impl.FinishTransaction;
import com.thecloudyco.cashier.module.impl.RefreshUPC;
import com.thecloudyco.cashier.module.impl.ShowBalance;
import com.thecloudyco.cashier.module.impl.VoidTotal;
import com.thecloudyco.cashier.module.impl.tender.CashTender;

public class ModuleManager {
	private static Map<String, CModule> Modules = new HashMap<>();
	
	public static void registerModules() {
		// Called from the main class when we start everything up
		// this method will be where we add everything to the hashmap for the program to access
		
		// Numeric Modules
		Modules.put("6667", new CustRelations());
		Modules.put("4444", new ShowBalance());
		Modules.put("1102", new FinishTransaction());
		Modules.put("2001", new RefreshUPC());
		
		// uhh.. not numeric modules
		Modules.put("VOIDTOTAL", new VoidTotal());
		
		// Tender Modules
		Modules.put("CASH", new CashTender());
	}
	
	public static CModule getModule(String id) {
		if(Modules.get(id) != null) {
			return Modules.get(id);
		}
		return null;
	}
}