package com.BJ.javabean;

import java.util.Date;

public class Order {
	private Integer pk_order;
	private Date create_time;
	private Date arrival_time;
	private Integer amount;
	private Integer fk_payment_account;
	private String remark;
	private Integer status;
	public Integer getPk_order() {
		return pk_order;
	}
	public void setPk_order(Integer pk_order) {
		this.pk_order = pk_order;
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
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Integer getFk_payment_account() {
		return fk_payment_account;
	}
	public void setFk_payment_account(Integer fk_payment_account) {
		this.fk_payment_account = fk_payment_account;
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
	
}
