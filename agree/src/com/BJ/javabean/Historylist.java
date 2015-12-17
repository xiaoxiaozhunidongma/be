package com.BJ.javabean;

import com.activeandroid.annotation.Column;

public class Historylist {
	@Column
	private Integer pk_payment_account;
	@Column
	private Integer type;
	@Column
	private String account;
	@Column
	private Integer pk_order;
	@Column
	private String create_time;
	@Column
	private String arrival_time;
	@Column
	private float amount;
	@Column
	private Integer fk_payment_account;
	@Column
	private String remark;
	@Column
	private Integer status;
	public Integer getPk_payment_account() {
		return pk_payment_account;
	}
	public void setPk_payment_account(Integer pk_payment_account) {
		this.pk_payment_account = pk_payment_account;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public Integer getPk_order() {
		return pk_order;
	}
	public void setPk_order(Integer pk_order) {
		this.pk_order = pk_order;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getArrival_time() {
		return arrival_time;
	}
	public void setArrival_time(String arrival_time) {
		this.arrival_time = arrival_time;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
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
