package com.BJ.javabean;

import java.io.Serializable;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "group_team")
public class GroupHome implements Serializable {

	/**
	 * extends Model
	 */

	@Column
	private Integer pk_group_user;
	@Column
	private Integer fk_group;
	@Column
	private Integer role;
	@Column
	private Integer party_update;
	@Column
	private Integer chat_update;
	@Column
	private Integer photo_update;
	@Column
	private Integer pk_group;
	@Column
	private String em_id;
	@Column
	private String name;
	@Column
	private String setup_time;
	@Column
	private String last_post_time;
	@Column
	private String last_post_message;
	@Column
	private String avatar_path;
	@Column
	private String remark;
	@Column
	private Integer status;
	@Column
	private Integer creater;

	public GroupHome() {
	}

	public Integer getPk_group_user() {
		return pk_group_user;
	}

	public void setPk_group_user(Integer pk_group_user) {
		this.pk_group_user = pk_group_user;
	}

	public Integer getFk_group() {
		return fk_group;
	}

	public void setFk_group(Integer fk_group) {
		this.fk_group = fk_group;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public Integer getParty_update() {
		return party_update;
	}

	public void setParty_update(Integer party_update) {
		this.party_update = party_update;
	}

	public Integer getChat_update() {
		return chat_update;
	}

	public void setChat_update(Integer chat_update) {
		this.chat_update = chat_update;
	}

	public Integer getPhoto_update() {
		return photo_update;
	}

	public void setPhoto_update(Integer photo_update) {
		this.photo_update = photo_update;
	}

	public Integer getPk_group() {
		return pk_group;
	}

	public void setPk_group(Integer pk_group) {
		this.pk_group = pk_group;
	}

	public String getEm_id() {
		return em_id;
	}

	public void setEm_id(String em_id) {
		this.em_id = em_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSetup_time() {
		return setup_time;
	}

	public void setSetup_time(String setup_time) {
		this.setup_time = setup_time;
	}

	public String getLast_post_time() {
		return last_post_time;
	}

	public void setLast_post_time(String last_post_time) {
		this.last_post_time = last_post_time;
	}

	public String getLast_post_message() {
		return last_post_message;
	}

	public void setLast_post_message(String last_post_message) {
		this.last_post_message = last_post_message;
	}

	public String getAvatar_path() {
		return avatar_path;
	}

	public void setAvatar_path(String avatar_path) {
		this.avatar_path = avatar_path;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCreater() {
		return creater;
	}

	public void setCreater(Integer creater) {
		this.creater = creater;
	}


}
