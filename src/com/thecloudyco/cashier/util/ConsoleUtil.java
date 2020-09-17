package com.thecloudyco.cashier.util;

public class ConsoleUtil {
	public static void Print(String line_one, String line_two) {
		// First Clear the entire page
		Clear();
		System.out.println(line_one + "\n" + line_two);
	}
	
	public static void Clear() {
		for(int i = 0; i < 100; i++) {
			System.out.println("\n");
		}
	}
}
