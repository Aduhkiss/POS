package com.thecloudyco.pos.items;

public class Item {
	
	private String upc;
	private String name;
	private double price;
	private int custom_price;
	private int weight;
	private int quantity;
	private double price_per_pound;
	private int transaction_limit;
	private int restricted;
	
	private double myPrice;
	
	// If the item is a tebex package, this will store the IGN of the player that this package will be applied to
	private boolean tebex;
	private String username;
	
	public Item(String upc, String name, double price, int custom_price, int weight, int quantity, double price_per_pound, int transaction_limit, int restricted) {
		this.upc = upc;
		this.name = name;
		this.price = price;
		this.custom_price = custom_price;
		this.weight = weight;
		this.quantity = quantity;
		this.price_per_pound = price_per_pound;
		this.transaction_limit = transaction_limit;
		this.restricted = restricted;
	}
	
	public Item(String upc, String name, double price, int custom_price, int weight, int quantity, double price_per_pound, int transaction_limit, boolean restricted, boolean tebex) {
		this.upc = upc;
		this.name = name;
		this.price = price;
		this.custom_price = custom_price;
		this.weight = weight;
		this.quantity = quantity;
		this.price_per_pound = price_per_pound;
		this.transaction_limit = transaction_limit;
		if(restricted) {
			this.restricted = 1;
		} else {
			this.restricted = 0;
		}
		this.tebex = tebex;
	}
	
	public String getUpc() {
		return upc;
	}
	public String getName() {
		return name;
	}
	public double getPrice() {
		return price;
	}
	public int isCustom_price() {
		return custom_price;
	}
	public int isWeight() {
		return weight;
	}
	public int isQuantity() {
		return quantity;
	}
	public double getPrice_per_pound() {
		return price_per_pound;
	}
	public int getTransaction_limit() {
		return transaction_limit;
	}
	public int isRestricted() {
		return restricted;
	}
	public void setMyPrice(double a) {
		myPrice = a;
	}
	public void setMyPrice(String a) {
		myPrice = Double.valueOf(a);
	}
	public double getMyPrice() {
		return myPrice;
	}
	public boolean isTebex() {
		return tebex;
	}
	public void setUsername(String name) {
		this.username = name;
	}
	public String getUsername() {
		return username;
	}
}
