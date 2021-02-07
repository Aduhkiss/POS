package com.thecloudyco.override.common;

public enum OverrideType {
	
	MASTER(100),
	FULL(10),
	PIC(5),
	DISCORD(2),
	CASHIER(1),
	DISABLED(0);
	
	private int power;
	
	OverrideType(int power) {
		this.power = power;
	}
	
	public int getPower() {
		return power;
	}
}
