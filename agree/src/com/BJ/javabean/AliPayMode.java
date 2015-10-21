package com.BJ.javabean;

import com.activeandroid.annotation.Column;

public class AliPayMode {
	@Column
	private String partner;
	@Column
	private String seller;
	@Column
	private String privateKey;
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getSeller() {
		return seller;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	
}
