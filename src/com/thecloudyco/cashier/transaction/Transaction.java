package com.thecloudyco.cashier.transaction;

import java.util.List;

import com.thecloudyco.cashier.items.Item;
import com.thecloudyco.cashier.user.Operator;

public class Transaction {
	private Operator operator;
	private List<Item> Items;
	
	public Transaction(Operator operator, List<Item> i) {
		this.operator = operator;
		this.Items = i;
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
