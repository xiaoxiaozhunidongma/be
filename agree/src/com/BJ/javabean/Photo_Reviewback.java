package com.BJ.javabean;

import java.util.ArrayList;
import java.util.List;


public class Photo_Reviewback {
    private Integer statusMsg;
    private List<Photo_Review> returnData = new ArrayList<Photo_Review>();
	public Integer getStatusMsg() {
		return statusMsg;
	}
	public void setStatusMsg(Integer statusMsg) {
		this.statusMsg = statusMsg;
	}
	public List<Photo_Review> getReturnData() {
		return returnData;
	}
	public void setReturnData(List<Photo_Review> returnData) {
		this.returnData = returnData;
	}
    

}
