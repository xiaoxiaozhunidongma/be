package com.BJ.javabean;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReadUserAllFriendsback {

    @Expose
    private Integer statusMsg;
    @Expose
    private List<ReadUserAllFriends> returnData = new ArrayList<ReadUserAllFriends>();
    @SerializedName("interface")
    @Expose
    private String _interface;

    /**
     * 
     * @return
     *     The statusMsg
     */
    public Integer getStatusMsg() {
        return statusMsg;
    }

    /**
     * 
     * @param statusMsg
     *     The statusMsg
     */
    public void setStatusMsg(Integer statusMsg) {
        this.statusMsg = statusMsg;
    }

    /**
     * 
     * @return
     *     The returnData
     */
    public List<ReadUserAllFriends> getReturnData() {
        return returnData;
    }

    /**
     * 
     * @param returnData
     *     The returnData
     */
    public void setReturnData(List<ReadUserAllFriends> returnData) {
        this.returnData = returnData;
    }

    /**
     * 
     * @return
     *     The _interface
     */
    public String getInterface() {
        return _interface;
    }

    /**
     * 
     * @param _interface
     *     The interface
     */
    public void setInterface(String _interface) {
        this._interface = _interface;
    }

}
