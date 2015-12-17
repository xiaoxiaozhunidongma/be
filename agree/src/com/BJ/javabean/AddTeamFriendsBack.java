package com.BJ.javabean;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddTeamFriendsBack {

	@Expose
	private Integer statusMsg;
	@Expose
	private List<AddTeamFriends> returnData = new ArrayList<AddTeamFriends>();
	@SerializedName("interface")
	@Expose
	private String _interface;

	public Integer getStatusMsg() {
		return statusMsg;
	}

	public void setStatusMsg(Integer statusMsg) {
		this.statusMsg = statusMsg;
	}

	public List<AddTeamFriends> getReturnData() {
		return returnData;
	}

	public void setReturnData(List<AddTeamFriends> returnData) {
		this.returnData = returnData;
	}

	public String get_interface() {
		return _interface;
	}

	public void set_interface(String _interface) {
		this._interface = _interface;
	}

}