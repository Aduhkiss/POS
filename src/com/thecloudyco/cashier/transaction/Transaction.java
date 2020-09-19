package com.thecloudyco.cashier.transaction;

import java.util.ArrayList;
import java.util.List;

import com.thecloudyco.cashier.items.Item;
import com.thecloudyco.cashier.user.Operator;

public class Transaction {
	private Operator operator;
	private List<Item> Items;
	
	public Transaction(Operator operator) {
		this.operator = operator;
		this.Items = new ArrayList<>();
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
	
	public void voidItem(Item item) {
		Items.remove(item);
	}
}
