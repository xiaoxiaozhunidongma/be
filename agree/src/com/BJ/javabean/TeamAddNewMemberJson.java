package com.BJ.javabean;

import org.json.JSONArray;

public class TeamAddNewMemberJson {
	JSONArray members;

	public JSONArray getMembers() {
		return members;
	}

	public void setMembers(JSONArray members) {
		this.members = members;
	}

	public TeamAddNewMemberJson(JSONArray members) {
		super();
		this.members = members;
	}
	
	
}
