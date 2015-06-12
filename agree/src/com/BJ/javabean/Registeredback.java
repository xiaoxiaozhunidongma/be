package com.BJ.javabean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Registeredback {

    @Expose
    private Integer statusMsg;
    @Expose
    private Integer returnData ;
    @SerializedName("interface")
    @Expose
    private String _interface;
	public Integer getStatusMsg() {
		return statusMsg;
	}
	public void setStatusMsg(Integer statusMsg) {
		this.statusMsg = statusMsg;
	}
	public Integer getReturnData() {
		return returnData;
	}
	public void setReturnData(Integer returnData) {
		this.returnData = returnData;
	}
	public String get_interface() {
		return _interface;
	}
	public void set_interface(String _interface) {
		this._interface = _interface;
	}

    
}
