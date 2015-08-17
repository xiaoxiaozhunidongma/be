package com.BJ.utils;

import java.io.Serializable;

public class Person implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8980386032436705885L;
	public Integer pk_user;
	@Override
	public String toString() {
		return "Person [pk_user=" + pk_user + "]";
	}
	public Person(Integer pk_user) {
		super();
		this.pk_user = pk_user;
	}
	
}
