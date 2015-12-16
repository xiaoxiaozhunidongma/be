package com.BJ.javabean;

import java.io.Serializable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "jpush")
public class JPush extends Model implements Serializable{
	public JPush(){}
	
	@Column
	private String pk_group;
	@Column
	private String type_tag;
	public String getPk_group() {
		return pk_group;
	}
	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}
	public String getType_tag() {
		return type_tag;
	}
	public void setType_tag(String type_tag) {
		this.type_tag = type_tag;
	}
	public JPush(String pk_group, String type_tag) {
		super();
		this.pk_group = pk_group;
		this.type_tag = type_tag;
	}
	@Override
	public String toString() {
		return "JPush [pk_group=" + pk_group + ", type_tag=" + type_tag + "]";
	}
	
	
	
}
