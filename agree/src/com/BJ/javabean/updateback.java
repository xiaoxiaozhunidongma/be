package com.BJ.javabean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class updateback {
	@Expose
	private Integer statusMsg;
	@Expose
	private boolean returnData;
	@SerializedName("interface")
	@Expose
	private String _interface;

	public Integer getStatusMsg() {
		return statusMsg;
	}

	public void setStatusMsg(Integer statusMsg) {
		this.statusMsg = statusMsg;
	}

	public boolean isReturnData() {
		return returnData;
	}

	public void setReturnData(boolean returnData) {
		this.returnData = returnData;
	}

	public String get_interface() {
		return _interface;
	}

	public void set_interface(String _interface) {
		this._interface = _interface;
	}

}
