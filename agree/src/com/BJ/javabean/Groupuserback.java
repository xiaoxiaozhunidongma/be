package com.BJ.javabean;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Groupuserback {

    @Expose
    private Integer statusMsg;
    @Expose
    private List<Group_User> returnData = new ArrayList<Group_User>();
    @SerializedName("interface")
    @Expose
    private String _interface;
	public Integer getStatusMsg() {
		return statusMsg;
	}
	public void setStatusMsg(Integer statusMsg) {
		this.statusMsg = statusMsg;
	}
	public List<Group_User> getReturnData() {
		return returnData;
	}
	public void setReturnData(List<Group_User> returnData) {
		this.returnData = returnData;
	}
	public String get_interface() {
		return _interface;
	}
	public void set_interface(String _interface) {
		this._interface = _interface;
	}

   

}
