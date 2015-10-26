package com.BJ.javabean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeChatPayBack {

    @Expose
    private Integer statusMsg;
    @Expose
    private WeChatPayMode returnData =new WeChatPayMode();
    @SerializedName("interface")
    @Expose
    private String _interface;
	public Integer getStatusMsg() {
		return statusMsg;
	}
	public void setStatusMsg(Integer statusMsg) {
		this.statusMsg = statusMsg;
	}
	public WeChatPayMode getReturnData() {
		return returnData;
	}
	public void setReturnData(WeChatPayMode returnData) {
		this.returnData = returnData;
	}
	public String get_interface() {
		return _interface;
	}
	public void set_interface(String _interface) {
		this._interface = _interface;
	}

    
}