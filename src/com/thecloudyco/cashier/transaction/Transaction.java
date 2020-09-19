package com.thecloudyco.cashier.transaction;

import java.util.ArrayList;
import java.util.List;

import com.thecloudyco.cashier.items.Item;
import com.thecloudyco.cashier.items.ItemManager;
import com.thecloudyco.cashier.user.Operator;

public class Transaction {
	
	private Operator operator;
	private List<Item> Items;
	private List<Tender> Tenders;
	
	public Transaction(Operator operator) {
		this.operator = operator;
		this.Items = new ArrayList<>();
		this.Tenders = new ArrayList<>();
	}
	
	public List<Tender> getTenders() {
		return Tenders;
	}
	
	public void addTender(Tender t) {
		Tenders.add(t);
	}

	public Operator getOperator() {
		return operator;
	}

	public List<Item> getItems() {
		return Items;
	}
	
	public void addItem(Item item) {
		Items.add(item);
	}
	
	public void addItem(String UPC) {
		addItem(ItemManager.getItemFromUPC(UPC));
	}
	
	public void voidItem(Item item) {
		Items.remove(item);
	}
	
	public void voidItem(String UPC) {
		voidItem(ItemManager.getItemFromUPC(UPC));
	}
	
	public boolean exists(String UPC) {
		for(Item item : Items) {
			if(item.getUPC().equals(UPC)) {
				return true;
			}
		}
		return false;
	}
}
