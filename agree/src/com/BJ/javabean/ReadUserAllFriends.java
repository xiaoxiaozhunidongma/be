package com.BJ.javabean;

import java.io.Serializable;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "t_checkfriends")
public class ReadUserAllFriends implements Serializable{

	@Column
	private Integer fk_user_to;
	@Column
	private Integer relationship;
	@Column
	private Integer chat_update;
	@Column
	private Integer relationship_update;
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
	private String wechat_id;

	public Integer getFk_user_to() {
		return fk_user_to;
	}

	public Integer getRelationship() {
		return relationship;
	}

	public void setRelationship(Integer relationship) {
		this.relationship = relationship;
	}

	public Integer getChat_update() {
		return chat_update;
	}

	public void setChat_update(Integer chat_update) {
		this.chat_update = chat_update;
	}

	public Integer getRelationship_update() {
		return relationship_update;
	}

	public void setRelationship_update(Integer relationship_update) {
		this.relationship_update = relationship_update;
	}

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

	public String getWechat_id() {
		return wechat_id;
	}

	public void setWechat_id(String wechat_id) {
		this.wechat_id = wechat_id;
	}

	public void setFk_user_to(Integer fk_user_to) {
		this.fk_user_to = fk_user_to;
	}

}
