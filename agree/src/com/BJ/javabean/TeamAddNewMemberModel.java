package com.BJ.javabean;

public class TeamAddNewMemberModel {
	Group_User[] members;

	public Group_User[] getMembers() {
		return members;
	}

	public void setMembers(Group_User[] members) {
		this.members = members;
	}

	public TeamAddNewMemberModel(Group_User[] members) {
		super();
		this.members = members;
	}
	
	
	
}
