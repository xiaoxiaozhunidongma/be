package com.BJ.javabean;


public class IDs {
	
	private Integer group_id;
	private Integer user_id;
	private Integer pk_group_user;
	public Integer getgroup_id() {
		return group_id;
	}
	public void setgroup_id(Integer group_id) {
		this.group_id = group_id;
	}
	public Integer getuser_id() {
		return user_id;
	}
	public void setuser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public Integer getpk_group_user() {
		return pk_group_user;
	}
	public void setpk_group_user(Integer pk_group_user) {
		this.pk_group_user = pk_group_user;
	}
	
	//构造方法
	public IDs(Integer group_id,Integer user_id,Integer pk_group_user){
		this.group_id = group_id;
		this.user_id = user_id;
		this.pk_group_user = pk_group_user;
	}
	
}
