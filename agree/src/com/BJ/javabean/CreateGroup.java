package com.BJ.javabean;

import java.io.Serializable;
import java.util.Arrays;


public class CreateGroup  {
	/**
	 * 
	 */
	Group_User[] member;
	Group group;
	public Group_User[] getMember() {
		return member;
	}
	public void setMember(Group_User[] member) {
		this.member = member;
	}
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	@Override
	public String toString() {
		return "CreateGroup [member=" + Arrays.toString(member) + ", group="
				+ group + "]";
	}
	public CreateGroup(Group_User[] member, Group group) {
		super();
		this.member = member;
		this.group = group;
	}

	


	
	
	
}
