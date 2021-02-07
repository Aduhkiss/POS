package com.thecloudyco.pos.config;

public class ConfigFile {
	
	private String controllerServerAddress;
	private String terminal_number;
	
	public ConfigFile(String controllerServerAddress, String terminal_number) {
		this.controllerServerAddress = controllerServerAddress;
		this.terminal_number = terminal_number;
	}
	
	public String getControllerAddress() {
		return controllerServerAddress;
	}
	
	public String getTerminalNumber() {
		return terminal_number;
	}
}
