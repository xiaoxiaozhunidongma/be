package com.BJ.javabean;

import org.json.JSONArray;

public class StrPhoneArray {
	private Integer user_id;
	private JSONArray phones;
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public JSONArray getPhoneArray() {
		return phones;
	}
	public void setPhoneArray(JSONArray phones) {
		this.phones = phones;
	}
	public StrPhoneArray(Integer user_id, JSONArray phones) {
		super();
		this.user_id = user_id;
		this.phones = phones;
	}
	
}
