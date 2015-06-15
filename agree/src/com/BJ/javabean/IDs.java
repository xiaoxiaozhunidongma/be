package com.BJ.javabean;


public class IDs {
	
	private Integer id_group;
	private Integer id_user;
	private Integer id_user_group;
	public Integer getId_group() {
		return id_group;
	}
	public void setId_group(Integer id_group) {
		this.id_group = id_group;
	}
	public Integer getId_user() {
		return id_user;
	}
	public void setId_user(Integer id_user) {
		this.id_user = id_user;
	}
	public Integer getId_user_group() {
		return id_user_group;
	}
	public void setId_user_group(Integer id_user_group) {
		this.id_user_group = id_user_group;
	}
	
	//构造方法
	public IDs(Integer id_group,Integer id_user,Integer id_user_group){
		this.id_group = id_group;
		this.id_user = id_user;
		this.id_user_group = id_user_group;
	}
	
}
