package com.BJ.javabean;
import java.util.HashMap;
import java.util.Map;

public class Relation {

    private String pkUser;
    private String nickname;
    private String avatarPath;
    private String phone;
    private String password;
    private String setupTime;
    private String lastLoginTime;
    private String jpushId;
    private String sex;
    private String deviceId;
    private String status;
    private Object wechatId;
    private String relationship;

    /**
     * 
     * @return
     *     The pkUser
     */
    public String getPkUser() {
        return pkUser;
    }

    /**
     * 
     * @param pkUser
     *     The pk_user
     */
    public void setPkUser(String pkUser) {
        this.pkUser = pkUser;
    }

    /**
     * 
     * @return
     *     The nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 
     * @param nickname
     *     The nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 
     * @return
     *     The avatarPath
     */
    public String getAvatarPath() {
        return avatarPath;
    }

    /**
     * 
     * @param avatarPath
     *     The avatar_path
     */
    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    /**
     * 
     * @return
     *     The phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 
     * @param phone
     *     The phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 
     * @return
     *     The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 
     * @param password
     *     The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 
     * @return
     *     The setupTime
     */
    public String getSetupTime() {
        return setupTime;
    }

    /**
     * 
     * @param setupTime
     *     The setup_time
     */
    public void setSetupTime(String setupTime) {
        this.setupTime = setupTime;
    }

    /**
     * 
     * @return
     *     The lastLoginTime
     */
    public String getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * 
     * @param lastLoginTime
     *     The last_login_time
     */
    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    /**
     * 
     * @return
     *     The jpushId
     */
    public String getJpushId() {
        return jpushId;
    }

    /**
     * 
     * @param jpushId
     *     The jpush_id
     */
    public void setJpushId(String jpushId) {
        this.jpushId = jpushId;
    }

    /**
     * 
     * @return
     *     The sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * 
     * @param sex
     *     The sex
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * 
     * @return
     *     The deviceId
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * 
     * @param deviceId
     *     The device_id
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * 
     * @return
     *     The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The wechatId
     */
    public Object getWechatId() {
        return wechatId;
    }

    /**
     * 
     * @param wechatId
     *     The wechat_id
     */
    public void setWechatId(Object wechatId) {
        this.wechatId = wechatId;
    }

    /**
     * 
     * @return
     *     The relationship
     */
    public String getRelationship() {
        return relationship;
    }

    /**
     * 
     * @param relationship
     *     The relationship
     */
    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

	@Override
	public String toString() {
		return "Relation [pkUser=" + pkUser + ", nickname=" + nickname
				+ ", avatarPath=" + avatarPath + ", phone=" + phone
				+ ", password=" + password + ", setupTime=" + setupTime
				+ ", lastLoginTime=" + lastLoginTime + ", jpushId=" + jpushId
				+ ", sex=" + sex + ", deviceId=" + deviceId + ", status="
				+ status + ", wechatId=" + wechatId + ", relationship="
				+ relationship + "]";
	}




    
}
