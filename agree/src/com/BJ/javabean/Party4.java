package com.BJ.javabean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

public class Party4 implements Serializable{
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
	private float pay_amount;
	@Column
	private Integer pay_fk_user;
	@Column
	private Integer pay_interval;
	@Column
	private Integer status;
	@Column
    private List<Photo> photos = new ArrayList<Photo>();
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
	public float getPay_amount() {
		return pay_amount;
	}
	public void setPay_amount(float pay_amount) {
		this.pay_amount = pay_amount;
	}
	public Integer getPay_fk_user() {
		return pay_fk_user;
	}
	public void setPay_fk_user(Integer pay_fk_user) {
		this.pay_fk_user = pay_fk_user;
	}
	public Integer getPay_interval() {
		return pay_interval;
	}
	public void setPay_interval(Integer pay_interval) {
		this.pay_interval = pay_interval;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public List<Photo> getPhotos() {
		return photos;
	}
	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}
	public Party4(String pk_party, Integer fk_group, Integer fk_user,
			String name, String remark, String begin_time, String end_time,
			String tip_time, String sign_time, String sign_count,
			Double longitude, Double latitude, String location,
			Integer pay_type, Integer pay_amount, Integer pay_fk_user,
			Integer pay_interval, Integer status, List<Photo> photos) {
		super();
		this.pk_party = pk_party;
		this.fk_group = fk_group;
		this.fk_user = fk_user;
		this.name = name;
		this.remark = remark;
		this.begin_time = begin_time;
		this.end_time = end_time;
		this.tip_time = tip_time;
		this.sign_time = sign_time;
		this.sign_count = sign_count;
		this.longitude = longitude;
		this.latitude = latitude;
		this.location = location;
		this.pay_type = pay_type;
		this.pay_amount = pay_amount;
		this.pay_fk_user = pay_fk_user;
		this.pay_interval = pay_interval;
		this.status = status;
		this.photos = photos;
	}
	
	
	
	
	

}
