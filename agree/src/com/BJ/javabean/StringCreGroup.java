package com.BJ.javabean;

import org.json.JSONArray;
import org.json.JSONObject;

public class StringCreGroup {
	JSONArray member;
	JSONObject group;
	public JSONArray getMember() {
		return member;
	}
	public void setMember(JSONArray member) {
		this.member = member;
	}
	public JSONObject getGroup() {
		return group;
	}
	public void setGroup(JSONObject group) {
		this.group = group;
	}
	public StringCreGroup(JSONArray array, JSONObject jsonObject) {
		super();
		this.member = array;
		this.group = jsonObject;
	}
	
}
