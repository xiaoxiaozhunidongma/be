package com.BJ.javabean;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonAddParty {
	
	JSONArray remarkArray;
	JSONObject party;
	public JSONArray getRemarkArray() {
		return remarkArray;
	}
	public void setRemarkArray(JSONArray remarkArray) {
		this.remarkArray = remarkArray;
	}
	public JSONObject getParty() {
		return party;
	}
	public void setParty(JSONObject party) {
		this.party = party;
	}
	public JsonAddParty(JSONArray remarkArray, JSONObject party) {
		super();
		this.remarkArray = remarkArray;
		this.party = party;
	}
	
}
