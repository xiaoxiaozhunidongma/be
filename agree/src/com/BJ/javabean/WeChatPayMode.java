package com.BJ.javabean;

import com.activeandroid.annotation.Column;


public class WeChatPayMode {
	@Column
	private String app_id;
	@Column
	private String app_secret;
	@Column
	private String mch_id;
	@Column
	private String partner_id;
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	public String getApp_secret() {
		return app_secret;
	}
	public void setApp_secret(String app_secret) {
		this.app_secret = app_secret;
	}
	public String getMch_id() {
		return mch_id;
	}
	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}
	public String getPartner_id() {
		return partner_id;
	}
	public void setPartner_id(String partner_id) {
		this.partner_id = partner_id;
	}

	
}
