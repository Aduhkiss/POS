package com.thecloudyco.cashier.user;

public class Operator {
	private String first_name;
	private String last_name;
	private String operator_id;
	
	public Operator(String first_name, String last_name, String operator_id) {
		this.first_name = first_name;
		this.last_name = last_name;
		this.operator_id = operator_id;
	}
	
	public String getFirstName() {
		return first_name;
	}
	
	public String getLastName() {
		return last_name;
	}
	
	public String getOperatorId() {
		return operator_id;
	}
	
	public String getFullName() {
		return first_name + " " + last_name;
	}
}
