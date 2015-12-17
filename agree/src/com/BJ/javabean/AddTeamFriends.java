package com.BJ.javabean;

import java.io.Serializable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "t_user")
public class AddTeamFriends extends Model implements Serializable {

	public AddTeamFriends() {

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
	private Integer sex;
	@Column
	private String wechat_id;
	@Column
	private String real_name;

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

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
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

}
