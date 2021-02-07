package com.thecloudyco.override.api;

import java.io.IOException;

import com.google.gson.Gson;
import com.thecloudyco.override.common.HttpUtil;
import com.thecloudyco.override.common.OverrideType;
import com.thecloudyco.override.ent.ManagerProfile;
import com.thecloudyco.pos.Main;
import com.thecloudyco.pos.user.Operator;
import com.thecloudyco.pos.util.StringUtil;

public class ManagerAPI {
	public ManagerAPI() {
	}
	
	public ManagerProfile getProfile(String key) throws IOException, InterruptedException {
        Gson gson = new Gson();
        // HttpUtil.get(theConfig.getControllerAddress() + "/item/?upc=" + origin[0])
        ManagerProfile profile = gson.fromJson(HttpUtil.get(Main.getConfig().getControllerAddress() + "/override/?supervisor=" + key), ManagerProfile.class);
        //profile.setPassword(key.substring(key.length() - 2, key.length()));
        return profile;
	}
	
	public boolean authenticate(Operator operator, String password) throws IOException {
		String result = HttpUtil.get(Main.getConfig().getControllerAddress() + "/authenticate/?operator=" + operator.getOperatorId() + "&password=" + password);
		if(result.equals("Success")) {
			return true;
		} return false;
	}
	
	public boolean authenticate(String operator, String password) throws IOException {
		String result = HttpUtil.get(Main.getConfig().getControllerAddress() + "/authenticate/?operator=" + operator + "&password=" + password);
		if(result.equals("Success")) {
			return true;
		} return false;
	}
	
	public boolean isAuthorized(OverrideType needed, String key) throws IOException, InterruptedException {
		ManagerProfile profile = getProfile(StringUtil.removeLeadingZeroes(key));
		if(profile == null) {
			return false;
		}
		if(profile.getOverrideType().getPower() >= needed.getPower()) {
			return true;
		}
		return false;
	}
}
