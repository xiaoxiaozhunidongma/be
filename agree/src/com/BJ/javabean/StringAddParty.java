package com.BJ.javabean;

import org.json.JSONObject;

public class StringAddParty {
	String remarkArray;
	JSONObject party;
	public String getRemarkArray() {
		return remarkArray;
	}
	public void setRemarkArray(String remarkArray) {
		this.remarkArray = remarkArray;
	}
	public JSONObject getParty() {
		return party;
	}
	public void setParty(JSONObject party) {
		this.party = party;
	}
	public StringAddParty(String remarkArray, JSONObject party) {
		super();
		this.remarkArray = remarkArray;
		this.party = party;
	}
	
}
