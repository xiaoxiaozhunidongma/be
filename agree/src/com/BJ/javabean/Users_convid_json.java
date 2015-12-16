package com.BJ.javabean;

import org.json.JSONArray;

public class Users_convid_json {
	JSONArray users;
	String con_id;
	public JSONArray getUsers() {
		return users;
	}
	public void setUsers(JSONArray users) {
		this.users = users;
	}
	public String getCon_id() {
		return con_id;
	}
	public void setCon_id(String con_id) {
		this.con_id = con_id;
	}
	public Users_convid_json(JSONArray users, String con_id) {
		super();
		this.users = users;
		this.con_id = con_id;
	}
	
	
}
