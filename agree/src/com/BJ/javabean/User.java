package com.BJ.javabean;

import java.io.Serializable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "t_user")
public class User extends Model implements Serializable{
	
	public User() {
		
	}
	
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
	public User(Integer pk_user, String nickname, String avatar_path,
			String phone, String password, String setup_time,
			String last_login_time, String jpush_id, Integer sex,
			String device_id, Integer status, float amount, String wechat_id,
			String real_name) {
		super();
		this.pk_user = pk_user;
		this.nickname = nickname;
		this.avatar_path = avatar_path;
		this.phone = phone;
		this.password = password;
		this.setup_time = setup_time;
		this.last_login_time = last_login_time;
		this.jpush_id = jpush_id;
		this.sex = sex;
		this.device_id = device_id;
		this.status = status;
		this.amount = amount;
		this.wechat_id = wechat_id;
		this.real_name = real_name;
	}
	
	
}
