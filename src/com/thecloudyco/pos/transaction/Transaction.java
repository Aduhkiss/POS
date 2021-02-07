package com.thecloudyco.pos.transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.thecloudyco.override.api.ManagerAPI;
import com.thecloudyco.override.common.HttpUtil;
import com.thecloudyco.override.common.OverrideType;
import com.thecloudyco.pos.Main;
import com.thecloudyco.pos.barcode.AtticusBarcode;
import com.thecloudyco.pos.items.Item;
import com.thecloudyco.pos.user.Operator;
import com.thecloudyco.pos.util.ConsoleUtil;
import com.thecloudyco.pos.util.SoundUtils;

public class Transaction {
	
	private Operator operator;
	private String id;
	private List<Item> Items;
	private List<Tender> Tenders;
	
	public boolean voidLimitCheckBypassed;
	
	private double voidedMoney;
	
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
		voidedMoney = voidedMoney + item.getMyPrice();
	}
	
	public double getVoidedMoney() {
		return voidedMoney;
	}
	
	public void finish(boolean success) {
		// Gets called when the transaction is finished
		Gson gson = new Gson();
		String item_json = gson.toJson(Items);
		String tender_json = gson.toJson(Tenders);
		
		// Check to see if we've voided too much money
		if(voidedMoney >= 60.00 && (voidLimitCheckBypassed != true)) {
			SoundUtils.beep();
			
			ConsoleUtil.Print("B023", "VOID TRANSACTION LIMIT CHECK");
			System.out.println("\n");
			Scanner sc = new Scanner(System.in);
			
			String override = sc.nextLine();
			ManagerAPI mAPI = new ManagerAPI();
			
			boolean flag = false;
			try {
				flag = mAPI.isAuthorized(OverrideType.PIC, override);
			} catch (Exception e) {}
			
			if(!flag) {
				ConsoleUtil.Print("ERROR", "Not Authorized");
				return;
			} else {
				run(success, item_json, tender_json);
			}
		} else {
			run(success, item_json, tender_json);
		}
	}
	
	private void run(boolean success, String item_json, String tender_json) {
		StringBuilder tId = new StringBuilder();
		for(int i = 0; i < 20; i++) {
			tId.append((int)(Math.random() * (9 - 0 + 1) + 0));
		}
		
		AtticusBarcode transBarcode = new AtticusBarcode(tId.toString());
		
		//new TestGui(transBarcode);
		
		try {
			HttpUtil.get(Main.getConfig().getControllerAddress() + "/recordTrans?transactionId=" + tId.toString() + "&cashier=" + 
		operator.getOperatorId() + "&success=" + String.valueOf(success) + "&timestamp=" + System.currentTimeMillis() + "&items=" + 
					item_json + "&tender=" + tender_json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public double getFinalPrice() {
		double p = 0.00;
		for(Item i : getItems()) {
			p = p + i.getMyPrice();
		}
		return p;
	}
	
	public boolean exists(String UPC) {
		for(Item item : Items) {
			System.out.println("item: " + item.getUpc());
			if(item.getUpc().equals(UPC)) {
				return true;
			}
		}
		return false;
	}
}
