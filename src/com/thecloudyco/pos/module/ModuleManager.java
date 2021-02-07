package com.thecloudyco.pos.module;

import java.util.HashMap;
import java.util.Map;

import com.thecloudyco.pos.module.impl.CustRelations;
import com.thecloudyco.pos.module.impl.OpenGUI;
import com.thecloudyco.pos.module.impl.SecureTerminal;
import com.thecloudyco.pos.module.impl.ShowBalance;
import com.thecloudyco.pos.module.impl.ShowTransaction;
import com.thecloudyco.pos.module.impl.Signout;
import com.thecloudyco.pos.module.impl.VoidItemRemake;
import com.thecloudyco.pos.module.impl.VoidTotal;
import com.thecloudyco.pos.module.impl.tender.CashTender;
import com.thecloudyco.pos.module.impl.tender.CheckTender;
import com.thecloudyco.pos.module.impl.tender.ICMIRTender;
import com.thecloudyco.pos.module.impl.tender.OnlinePayTender;

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
		//Modules.put("VOID", new VoidItem());
		Modules.put("VOID", new VoidItemRemake());
		Modules.put("SIGNOUT", new Signout());
		Modules.put("SECURE", new SecureTerminal());
		
		// Tender Modules
		Modules.put("00/CASH", new CashTender());
		Modules.put("00/CHECK", new CheckTender());
		Modules.put("00/ICMIR", new ICMIRTender());
		Modules.put("00/ONLINE", new OnlinePayTender());
		
	}
	
	public static CModule getModule(String id) {
		if(Modules.get(id) != null) {
			return Modules.get(id);
		}
		return null;
	}
}