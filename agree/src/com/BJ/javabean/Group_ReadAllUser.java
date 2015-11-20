package com.BJ.javabean;

import java.io.Serializable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table (name="tre_group_readalluser")
public class Group_ReadAllUser extends Model implements Serializable{
	//无参数的构造！！
	public Group_ReadAllUser() {
		
	}
	
	@Column
	private Integer pk_group_user;
	@Column
	private Integer fk_user;
	@Column
	private Integer fk_group;
	@Column
	private Integer message_warn;
	@Column
	private Integer party_warn;
	@Column
	private Integer public_phone;
	@Column
	private String remarks_name;
	@Column
	private Integer role;
	@Column
	private Integer pk_user;
	@Column
	private String avatar_path;
	@Column
	private String nickname;
	@Column
	private String phone;
	@Column
	private String last_login_time;
	public Integer getPk_group_user() {
		return pk_group_user;
	}
	public void setPk_group_user(Integer pk_group_user) {
		this.pk_group_user = pk_group_user;
	}
	public Integer getFk_user() {
		return fk_user;
	}
	public void setFk_user(Integer fk_user) {
		this.fk_user = fk_user;
	}
	public Integer getFk_group() {
		return fk_group;
	}
	public void setFk_group(Integer fk_group) {
		this.fk_group = fk_group;
	}
	public Integer getMessage_warn() {
		return message_warn;
	}
	public void setMessage_warn(Integer message_warn) {
		this.message_warn = message_warn;
	}
	public Integer getParty_warn() {
		return party_warn;
	}
	public void setParty_warn(Integer party_warn) {
		this.party_warn = party_warn;
	}
	public Integer getPublic_phone() {
		return public_phone;
	}
	public void setPublic_phone(Integer public_phone) {
		this.public_phone = public_phone;
	}
	public String getRemarks_name() {
		return remarks_name;
	}
	public void setRemarks_name(String remarks_name) {
		this.remarks_name = remarks_name;
	}
	public Integer getRole() {
		return role;
	}
	public void setRole(Integer role) {
		this.role = role;
	}
	public Integer getPk_user() {
		return pk_user;
	}
	public void setPk_user(Integer pk_user) {
		this.pk_user = pk_user;
	}
	public String getAvatar_path() {
		return avatar_path;
	}
	public void setAvatar_path(String avatar_path) {
		this.avatar_path = avatar_path;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getLast_login_time() {
		return last_login_time;
	}
	public void setLast_login_time(String last_login_time) {
		this.last_login_time = last_login_time;
	}
	public Group_ReadAllUser(Integer pk_group_user, Integer fk_user,
			Integer fk_group, Integer message_warn, Integer party_warn,
			Integer public_phone, String remarks_name, Integer role,
			Integer pk_user, String avatar_path, String nickname, String phone,
			String last_login_time) {
		super();
		this.pk_group_user = pk_group_user;
		this.fk_user = fk_user;
		this.fk_group = fk_group;
		this.message_warn = message_warn;
		this.party_warn = party_warn;
		this.public_phone = public_phone;
		this.remarks_name = remarks_name;
		this.role = role;
		this.pk_user = pk_user;
		this.avatar_path = avatar_path;
		this.nickname = nickname;
		this.phone = phone;
		this.last_login_time = last_login_time;
	}

	
}
