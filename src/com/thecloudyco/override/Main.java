package com.thecloudyco.override;

import java.io.IOException;
import java.util.Scanner;

import com.thecloudyco.override.api.ManagerAPI;
import com.thecloudyco.override.common.OverrideType;
import com.thecloudyco.override.ent.ManagerProfile;

public class Main {

	public static void main(String[] args) {
		
		// TESTING
		ManagerAPI api = new ManagerAPI();
		Scanner sc = new Scanner(System.in);
		
		System.out.println("PLEASE SCAN SUPERVISOR KEY...");
		String key = sc.nextLine();
		
		boolean flag = false;
		try {
			flag = api.isAuthorized(OverrideType.FULL, key);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
		//System.out.println("IS AUTHORIZED: " + flag);
		ManagerProfile profile = null;
		try {
			profile = api.getProfile(key);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		// How to fetch someones name
		if(profile == null) {
			System.out.println("Key not found");
		} else System.out.println("Welcome, " + profile.getFirstName() + " " + profile.getLastName());
		
	}

}
