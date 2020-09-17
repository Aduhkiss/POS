package com.thecloudyco.cashier.items;

public class Item {
	
	private String name;
	private double price;
	private String upc;
	
	public Item(String name, double price, String upc) {
		this.name = name;
		this.price = price;
		this.upc = upc;
	}
	
	public String getName() {
		return name;
	}
	
	public double getPrice() {
		return price;
	}
	
	public String getUPC() {
		return upc;
	}
}
