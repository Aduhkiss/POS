package com.thecloudyco.pos.tebex;

import java.io.IOException;

import com.thecloudyco.pos.util.HttpUtil;
import com.thecloudyco.pos.util.TebexUtil;

public class TebexAPI {
	public static TebexPackage get(String plu) {
		
		// HARDCODED FOR TESTING!! Remove asap
		String result;
		try {
			result = HttpUtil.get("https://plugin.tebex.io/package/" + plu, "4baffd7350c392477d7103da12402a49b7d171d0");
		} catch (IOException e) {
			return null;
		}
		
		return TebexUtil.convertToObject(result);
	}
}
