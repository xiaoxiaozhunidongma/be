package com.BJ.utils;

import java.io.Serializable;

public class Person implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8980386032436705885L;
	public String pk_user;
	public String password;
	@Override
	public String toString() {
		return "Person [pk_user=" + pk_user + ", password=" + password + "]";
	}
	public Person(String pk_user, String password) {
		super();
		this.pk_user = pk_user;
		this.password = password;
	}
}
