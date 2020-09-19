package com.thecloudyco.cashier.items;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thecloudyco.cashier.config.Config;
import com.thecloudyco.override.common.HttpUtil;

public class ItemManager {
	public static List<Item> ItemList = new ArrayList<Item>();
	
	public static void downloadUPCs() throws IOException {
		Gson gson = new Gson();
		String json = HttpUtil.get(Config.UPC_DOWNLOAD_SERVER);
		ItemList = gson.fromJson(json, new TypeToken<List<Item>>(){}.getType());
	}
	
	public static Item getItemFromUPC(String UPC) {
		for(Item item : ItemList) {
			if(item.getUPC().equals(UPC)) {
				return item;
			}
		}
		return null;
	}
}
