package com.BJ.javabean;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "t_phonearray")
public class PhoneArray {
	@Column
	private String[] phones;
	@Column
	private Integer user_id;
	public String[] getPhones() {
		return phones;
	}
	public void setPhones(String[] phones) {
		this.phones = phones;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	
}
