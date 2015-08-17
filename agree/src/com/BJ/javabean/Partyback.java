package com.BJ.javabean;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Partyback {

    @Expose
    private Integer statusMsg;
    @Expose
    private List<Party2> returnData = new ArrayList<Party2>();
    @SerializedName("interface")
    @Expose
    private String _interface;
	public Integer getStatusMsg() {
		return statusMsg;
	}
	public void setStatusMsg(Integer statusMsg) {
		this.statusMsg = statusMsg;
	}
	public List<Party2> getReturnData() {
		return returnData;
	}
	public void setReturnData(List<Party2> returnData) {
		this.returnData = returnData;
	}
	public String get_interface() {
		return _interface;
	}
	public void set_interface(String _interface) {
		this._interface = _interface;
	}
	@Override
	public String toString() {
		return "Partyback [statusMsg=" + statusMsg + ", returnData="
				+ returnData + ", _interface=" + _interface + "]";
	}
	
   

}
