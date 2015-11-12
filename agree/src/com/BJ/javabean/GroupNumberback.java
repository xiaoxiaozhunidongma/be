package com.BJ.javabean;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupNumberback {

    @Expose
    private Integer statusMsg;
    @Expose
    private List<GroupNumber> returnData = new ArrayList<GroupNumber>();
    @SerializedName("interface")
    @Expose
    private String _interface;
	public Integer getStatusMsg() {
		return statusMsg;
	}
	public void setStatusMsg(Integer statusMsg) {
		this.statusMsg = statusMsg;
	}
	public List<GroupNumber> getReturnData() {
		return returnData;
	}
	public void setReturnData(List<GroupNumber> returnData) {
		this.returnData = returnData;
	}
	public String get_interface() {
		return _interface;
	}
	public void set_interface(String _interface) {
		this._interface = _interface;
	}

   

}
