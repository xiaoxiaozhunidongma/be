package com.BJ.javabean;

import java.io.Serializable;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

public class Party3 implements Serializable{
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
	private String sign_time;
	@Column
	private String sign_count;
	@Column
	private Double longitude;
	@Column
	private Double latitude;
	@Column
	private String location;
	@Column
	private Integer pay_type;
	@Column
	private Integer amount;
	@Column
	private Integer pay_fk_user;
	@Column
	private String interval;
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
	public String getTip_time() {
		return tip_time;
	}
	public void setTip_time(String tip_time) {
		this.tip_time = tip_time;
	}
	public String getSign_time() {
		return sign_time;
	}
	public void setSign_time(String sign_time) {
		this.sign_time = sign_time;
	}
	public String getSign_count() {
		return sign_count;
	}
	public void setSign_count(String sign_count) {
		this.sign_count = sign_count;
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
	public Integer getPay_type() {
		return pay_type;
	}
	public void setPay_type(Integer pay_type) {
		this.pay_type = pay_type;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Integer getPay_fk_user() {
		return pay_fk_user;
	}
	public void setPay_fk_user(Integer pay_fk_user) {
		this.pay_fk_user = pay_fk_user;
	}
	public String getInterval() {
		return interval;
	}
	public void setInterval(String interval) {
		this.interval = interval;
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
				+ end_time + ", tip_time=" + tip_time + ", sign_time="
				+ sign_time + ", sign_count=" + sign_count + ", longitude="
				+ longitude + ", latitude=" + latitude + ", location="
				+ location + ", pay_type=" + pay_type + ", amount=" + amount
				+ ", pay_fk_user=" + pay_fk_user + ", interval=" + interval
				+ ", status=" + status + "]";
	}
	
	
	
	

}