package com.BJ.javabean;

import java.io.Serializable;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table (name="t_party")
public class Party implements Serializable{
	@Column
	private String pk_party;
	@Column
	private Integer fk_group;
	@Column
	private Integer fk_user;
	@Column
	private String name;
	@Column
	private String remark;
	@Column
	private String begin_time;
	@Column
	private String end_time;
	@Column
	private Double longitude;
	@Column
	private Double latitude;
	@Column
	private String location;
	@Column
	private Integer status;
	
	
	public String getPk_party() {
		return pk_party;
	}
	public void setPk_party(String pk_party) {
		this.pk_party = pk_party;
	}
	public Integer getFk_group() {
		return fk_group;
	}
	public void setFk_group(Integer fk_group) {
		this.fk_group = fk_group;
	}
	public Integer getFk_user() {
		return fk_user;
	}
	public void setFk_user(Integer fk_user) {
		this.fk_user = fk_user;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getBegin_time() {
		return begin_time;
	}
	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "Party [pk_party=" + pk_party + ", fk_group=" + fk_group
				+ ", fk_user=" + fk_user + ", name=" + name + ", remark="
				+ remark + ", begin_time=" + begin_time + ", end_time="
				+ end_time + ", longitude=" + longitude + ", latitude="
				+ latitude + ", location=" + location + ", status=" + status
				+ "]";
	}
	
	

}
