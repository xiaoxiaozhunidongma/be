package com.BJ.javabean;

import java.io.Serializable;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table (name="t_party")
public class Party2 implements Serializable{
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
	private String tip_time;
	@Column
	private Double longitude;
	@Column
	private Double latitude;
	@Column
	private String location;
	@Column
	private Integer status;
	@Column
	private Integer relationship;
	@Column
	private Integer pk_party_user;
	@Column
	private Integer inNum;
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
	public String getTip_time() {
		return tip_time;
	}
	public void setTip_time(String tip_time) {
		this.tip_time = tip_time;
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
	public Integer getRelationship() {
		return relationship;
	}
	public void setRelationship(Integer relationship) {
		this.relationship = relationship;
	}
	public Integer getPk_party_user() {
		return pk_party_user;
	}
	public void setPk_party_user(Integer pk_party_user) {
		this.pk_party_user = pk_party_user;
	}
	public Integer getInNum() {
		return inNum;
	}
	public void setInNum(Integer inNum) {
		this.inNum = inNum;
	}
	@Override
	public String toString() {
		return "Party2 [pk_party=" + pk_party + ", fk_group=" + fk_group
				+ ", fk_user=" + fk_user + ", name=" + name + ", remark="
				+ remark + ", begin_time=" + begin_time + ", end_time="
				+ end_time + ", tip_time=" + tip_time + ", longitude="
				+ longitude + ", latitude=" + latitude + ", location="
				+ location + ", status=" + status + ", relationship="
				+ relationship + ", pk_party_user=" + pk_party_user
				+ ", inNum=" + inNum + "]";
	}

	
}