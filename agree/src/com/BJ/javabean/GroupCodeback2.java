package com.BJ.javabean;

import java.util.List;

import org.json.JSONArray;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupCodeback2 {

	@Expose
	private Integer statusMsg;
	@Expose
	private List<Group_Code2> returnData;
	@SerializedName("interface")
	@Expose
	private String _interface;

	public Integer getStatusMsg() {
		return statusMsg;
	}

	public void setStatusMsg(Integer statusMsg) {
		this.statusMsg = statusMsg;
	}

	public List<Group_Code2> getReturnData() {
		return returnData;
	}

	public void setReturnData(List<Group_Code2> returnData) {
		this.returnData = returnData;
	}

	public String get_interface() {
		return _interface;
	}

	public void set_interface(String _interface) {
		this._interface = _interface;
	}

}
