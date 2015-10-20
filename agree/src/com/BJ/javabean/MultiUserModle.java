package com.BJ.javabean;

import org.json.JSONArray;

public class MultiUserModle {
	JSONArray users;

	public JSONArray getUsers() {
		return users;
	}

	public void setUsers(JSONArray users) {
		this.users = users;
	}

	public MultiUserModle(JSONArray users) {
		super();
		this.users = users;
	}


	
}
