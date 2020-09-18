package com.thecloudyco.cashier.util;

import java.util.regex.PatternSyntaxException;

public class StringUtil {
	public static String[] toArray(String string) {
		String[] splitArray = null;
		try {
			splitArray = string.split("\\\\s+");
		} catch(PatternSyntaxException ex) {
		}
		return splitArray;
	}
	
	public static String combine(String[] arr, int startPos) {
        StringBuilder str = new StringBuilder();

        for(int i = startPos; i < arr.length; ++i) {
           str = str.append(arr[i] + " ");
        }
        return str.toString();
	}
	
	public static String realBalance(String value) {
		int v = value.indexOf(".");
		if(value.substring(v, value.length()).length() > 2) {
			return value.substring(0, v + 3);
		}
		return value;
	}
}
