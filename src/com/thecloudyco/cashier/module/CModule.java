package com.thecloudyco.cashier.module;

import java.util.Scanner;

public abstract class CModule {
	
	private String name;
	private boolean mgrOverrideNeeded;
	
	public CModule(String name) {
		this.name = name;
		this.mgrOverrideNeeded = false;
	}
	
	public CModule(String name, boolean mgrOverrideNeeded) {
		this.name = name;
		this.mgrOverrideNeeded = mgrOverrideNeeded;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean requiresMgrOverride() {
		return mgrOverrideNeeded;
	}
	
	public abstract void execute(String[] args, Scanner sc);
}
