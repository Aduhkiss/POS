package com.thecloudyco.cashier.config;

public class ConfigFile {
	private String controllerServerAddress;
	
	public ConfigFile(String controllerServerAddress) {
		this.controllerServerAddress = controllerServerAddress;
	}
	
	public String getControllerAddress() {
		return controllerServerAddress;
	}
}
