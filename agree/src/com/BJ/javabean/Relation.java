package com.BJ.javabean;


public class Relation {

	private Integer pk_user;
	private String nickname;
	private String avatar_path;
	private String phone;
	private String password;
	private String setup_time;
	private String last_login_time;
	private String jpush_id;
	private String sex;
	private String device_id;
	private String status;
	private String wechat_id;
	private Integer relationship;
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
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getWechat_id() {
		return wechat_id;
	}
	public void setWechat_id(String wechat_id) {
		this.wechat_id = wechat_id;
	}
	public Integer getRelationship() {
		return relationship;
	}
	public void setRelationship(Integer relationship) {
		this.relationship = relationship;
	}
	@Override
	public String toString() {
		return "Relation [pk_user=" + pk_user + ", nickname=" + nickname
				+ ", avatar_path=" + avatar_path + ", phone=" + phone
				+ ", password=" + password + ", setup_time=" + setup_time
				+ ", last_login_time=" + last_login_time + ", jpush_id="
				+ jpush_id + ", sex=" + sex + ", device_id=" + device_id
				+ ", status=" + status + ", wechat_id=" + wechat_id
				+ ", relationship=" + relationship + "]";
	}

	
}
