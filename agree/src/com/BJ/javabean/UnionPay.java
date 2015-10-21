package com.BJ.javabean;

import java.util.Date;

import com.activeandroid.annotation.Column;

public class UnionPay {
	@Column
	private Integer pk_financial;
	@Column
	private String order_id;
	@Column
	private String order_sn;
	@Column
	private Integer order_type;
	@Column
	private Date create_time;
	@Column
	private Date arrival_time;
	@Column
	private Integer from_user;
	@Column
	private Integer to_user;
	@Column
	private String fk_party;
	@Column
	private Integer arrival_type;
	@Column
	private float amount;
	@Column
	private Integer status;
	public Integer getPk_financial() {
		return pk_financial;
	}
	public void setPk_financial(Integer pk_financial) {
		this.pk_financial = pk_financial;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getOrder_sn() {
		return order_sn;
	}
	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}
	public Integer getOrder_type() {
		return order_type;
	}
	public void setOrder_type(Integer order_type) {
		this.order_type = order_type;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Date getArrival_time() {
		return arrival_time;
	}
	public void setArrival_time(Date arrival_time) {
		this.arrival_time = arrival_time;
	}
	public Integer getFrom_user() {
		return from_user;
	}
	public void setFrom_user(Integer from_user) {
		this.from_user = from_user;
	}
	public Integer getTo_user() {
		return to_user;
	}
	public void setTo_user(Integer to_user) {
		this.to_user = to_user;
	}
	public String getFk_party() {
		return fk_party;
	}
	public void setFk_party(String fk_party) {
		this.fk_party = fk_party;
	}
	public Integer getArrival_type() {
		return arrival_type;
	}
	public void setArrival_type(Integer arrival_type) {
		this.arrival_type = arrival_type;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
