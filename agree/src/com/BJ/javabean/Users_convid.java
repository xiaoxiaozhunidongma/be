package com.BJ.javabean;

import java.util.List;

public class Users_convid {
	/**
	 * List<String> members 剔除自己,不包含自己！！！！！
	 */
	List<String> members;
	String con_id;
	public List<String> getMembers() {
		return members;
	}
	public void setMembers(List<String> members) {
		this.members = members;
	}
	public String getCon_id() {
		return con_id;
	}
	public void setCon_id(String con_id) {
		this.con_id = con_id;
	}
	public Users_convid(List<String> members, String con_id) {
		super();
		this.members = members;
		this.con_id = con_id;
	}
	
	
	
}
