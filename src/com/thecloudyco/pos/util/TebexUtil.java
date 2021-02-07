package com.thecloudyco.pos.util;

import com.google.gson.Gson;
import com.thecloudyco.pos.tebex.TebexPackage;

public class TebexUtil {
	public static TebexPackage convertToObject(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, TebexPackage.class);
	}
}
