package com.BJ.javabean;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "t_user")
public class UserBalance {
//	﻿{"statusMsg":1,"returnData":{"pk_user":"168",
//		"nickname":"\u60f3\u60f3\u968f\u4fbf",
//		"avatar_path":"97b5cc47-6c5e-4157-9f7a-ec1dd567b298",
//		"phone":"18059866229","password":"","setup_time":"0000-00-00 00:00:00",
//		"last_login_time":"0000-00-00 00:00:00","jpush_id":"020ffe99d00",
//		"sex":"1","device_id":"55cda5833d90e03dc5e7f9238903d8a5f5fee1138b199567d81f8cca6da8dc05",
//		"status":"1","amount":"0.00","wechat_id":"oyvhHxMNOdWJZikYsVtRfUAwJY58",
//		"real_name":null,"unArr_money":"3"},"interface":"113"}
	@Column
	private Integer pk_user;
	@Column
	private String nickname;
	@Column
	private String avatar_path;
	@Column
	private String phone;
	@Column
	private String password;
	@Column
	private String setup_time;
	@Column
	private String last_login_time;
	@Column
	private String jpush_id;
	@Column
	private Integer sex;
	@Column
	private String device_id;
	@Column
	private Integer status;
	@Column
	private float amount;
	@Column
	private String wechat_id ;
	@Column
	private String real_name ;
	@Column
	private float unArr_money;
	public Integer getPk_user() {
		return pk_user;
	}
	public void setPk_user(Integer pk_user) {
		this.pk_user = pk_user;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getAvatar_path() {
		return avatar_path;
	}
	public void setAvatar_path(String avatar_path) {
		this.avatar_path = avatar_path;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSetup_time() {
		return setup_time;
	}
	public void setSetup_time(String setup_time) {
		this.setup_time = setup_time;
	}
	public String getLast_login_time() {
		return last_login_time;
	}
	public void setLast_login_time(String last_login_time) {
		this.last_login_time = last_login_time;
	}
	public String getJpush_id() {
		return jpush_id;
	}
	public void setJpush_id(String jpush_id) {
		this.jpush_id = jpush_id;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public String getWechat_id() {
		return wechat_id;
	}
	public void setWechat_id(String wechat_id) {
		this.wechat_id = wechat_id;
	}
	public String getReal_name() {
		return real_name;
	}
	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}
	public float getUnArr_money() {
		return unArr_money;
	}
	public void setUnArr_money(float unArr_money) {
		this.unArr_money = unArr_money;
	}
	
	
}
