package com.BJ.javabean;

import java.sql.Date;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table (name="t_user")
public class Group_Code {
	
	@Column
	private String pk_group_code;
	@Column
	private Integer fk_group;
	@Column
	private Date create_time;
}
