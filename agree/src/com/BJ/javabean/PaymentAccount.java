package com.BJ.javabean;

import java.io.Serializable;

public class PaymentAccount implements Serializable{
	private Integer pk_payment_account;
	private Integer fk_user;
	private String account;
	private Integer type;
	private String name;
	private String remark;
	private Integer status;
	public Integer getPk_payment_account() {
		return pk_payment_account;
	}
	public void setPk_payment_account(Integer pk_payment_account) {
		this.pk_payment_account = pk_payment_account;
	}
	public Integer getFk_user() {
		return fk_user;
	}
	public void setFk_user(Integer fk_user) {
		this.fk_user = fk_user;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
