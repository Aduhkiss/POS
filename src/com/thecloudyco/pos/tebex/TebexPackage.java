package com.thecloudyco.pos.tebex;

public class TebexPackage {
	
	private int id;
	private String name;
	private double price;
	private String type;
	private boolean disabled;
	
	public TebexPackage(int id, String name, double price, String type, boolean disabled) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.type = type;
		this.disabled = disabled;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	public String getType() {
		return type;
	}

	public boolean isDisabled() {
		return disabled;
	}
	
}
