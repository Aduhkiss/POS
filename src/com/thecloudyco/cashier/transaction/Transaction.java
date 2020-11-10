package com.thecloudyco.cashier.transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.thecloudyco.cashier.Main;
import com.thecloudyco.cashier.barcode.AtticusBarcode;
import com.thecloudyco.cashier.items.Item;
import com.thecloudyco.cashier.user.Operator;
import com.thecloudyco.override.common.HttpUtil;

public class Transaction {
	
	private Operator operator;
	private String id;
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
	
	public void voidItem(Item item) {
		Items.remove(item);
	}
	
	public void finish(boolean success) {
		// Gets called when the transaction is finished
		Gson gson = new Gson();
		String item_json = gson.toJson(Items);
		String tender_json = gson.toJson(Tenders);
		
		StringBuilder tId = new StringBuilder();
		for(int i = 0; i < 20; i++) {
			tId.append((int)(Math.random() * (9 - 0 + 1) + 0));
		}
		
		AtticusBarcode transBarcode = new AtticusBarcode(tId.toString());
		
		//new TestGui(transBarcode);
		
		try {
		HttpUtil.get(Main.getConfig().getControllerAddress() + "/recordTrans?transactionId=" + tId.toString() + "&cashier=" + operator.getOperatorId() + "&success=" + String.valueOf(success) + "&timestamp=" + System.currentTimeMillis() + "&items=" + item_json + "&tender=" + tender_json);
	} catch (IOException e) {
		e.printStackTrace();
	}
		
//		try {
//			HttpUtil.get(Main.getConfig().getControllerAddress() + "/?action=recordTrans&transactionId=" + tId.toString() + "&cashier=" + operator.getFirstName() + "&success=" + String.valueOf(success) + "&timestamp=" + System.currentTimeMillis() + "&items=" + item_json);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	public boolean exists(String UPC) {
		for(Item item : Items) {
			if(item.getUpc().equals(UPC)) {
				return true;
			}
		}
		return false;
	}
}
