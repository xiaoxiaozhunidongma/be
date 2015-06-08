package com.BJ.javabean;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Loginback {

    @Expose
    private Integer statusMsg;
    @Expose
    private List<User> returnData = new ArrayList<User>();
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
    public List<User> getReturnData() {
        return returnData;
    }

    /**
     * 
     * @param returnData
     *     The returnData
     */
    public void setReturnData(List<User> returnData) {
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