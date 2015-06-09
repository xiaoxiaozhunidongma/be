package com.BJ.javabean;


import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table (name="t_user")
public class Group_Code {
	
	@Column
	private String pk_group_code;
	@Column
	private Integer fk_group;
	@Column
	private String create_time;
	public String getPk_group_code() {
		return pk_group_code;
	}
	public void setPk_group_code(String pk_group_code) {
		this.pk_group_code = pk_group_code;
	}
	public Integer getFk_group() {
		return fk_group;
	}
	public void setFk_group(Integer fk_group) {
		this.fk_group = fk_group;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	
}
