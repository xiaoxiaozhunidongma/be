package com.BJ.javabean;

import java.io.Serializable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "jpushnosee")
public class JPushNoSee extends Model implements Serializable{
	//未读过的信息的数据库的表
	public JPushNoSee(){}
	
	@Column
	private Integer pk_group;
	@Column
	private Integer type_tag;
	@Column
	private Integer party_update;
	public Integer getPk_group() {
		return pk_group;
	}
	public void setPk_group(Integer pk_group) {
		this.pk_group = pk_group;
	}
	public Integer getType_tag() {
		return type_tag;
	}
	public void setType_tag(Integer type_tag) {
		this.type_tag = type_tag;
	}
	public Integer getParty_update() {
		return party_update;
	}
	public void setParty_update(Integer party_update) {
		this.party_update = party_update;
	}
	@Override
	public String toString() {
		return "JPushNoSee [pk_group=" + pk_group + ", type_tag=" + type_tag
				+ ", party_update=" + party_update + "]";
	}
	public JPushNoSee(Integer pk_group, Integer type_tag, Integer party_update) {
		super();
		this.pk_group = pk_group;
		this.type_tag = type_tag;
		this.party_update = party_update;
	}
	
	
	
}
