package com.BJ.utils;

import java.util.List;

import com.BJ.javabean.Group;
import com.BJ.javabean.User;

public class SdPkUser {
	//传从登录或者注册来的pk_user
	private static Integer sD_pk_user;
	public static Integer getsD_pk_user() {
		return sD_pk_user;
	}

	public static void setsD_pk_user(Integer sD_pk_user) {
		SdPkUser.sD_pk_user = sD_pk_user;
	}

	//传注册来的一个true做判断布局的作用
	private static boolean isRegistered_one;
	public static boolean isRegistered_one() {
		return isRegistered_one;
	}

	public static void setRegistered_one(boolean isRegistered_one) {
		SdPkUser.isRegistered_one = isRegistered_one;
	}
	//微信绑定
	private static boolean getweixinBinding;
	
	public static boolean isGetweixinBinding() {
		return getweixinBinding;
	}

	public static void setGetweixinBinding(boolean getweixinBinding) {
		SdPkUser.getweixinBinding = getweixinBinding;
	}
	
	//传Pk_party_user
	private static Integer getPk_party_user;
	public static Integer getGetPk_party_user() {
		return getPk_party_user;
	}

	public static void setGetPk_party_user(Integer getPk_party_user) {
		SdPkUser.getPk_party_user = getPk_party_user;
	}
	
	//使用邀请码加入小组时进行对象的传值
	public static Group getgroup;
	public static Group getGetgroup() {
		return getgroup;
	}

	public static void setGetgroup(Group getgroup) {
		SdPkUser.getgroup = getgroup;
	}
	
	//绑定微信传user进行更新
	public static User getUser;
	public static User getGetUser() {
		return getUser;
	}

	public static void setGetUser(User getUser) {
		SdPkUser.getUser = getUser;
	}
	//main到setting进行传图片路径
	public static String getFilePath;
	public static String getGetFilePath() {
		return getFilePath;
	}

	public static void setGetFilePath(String getFilePath) {
		SdPkUser.getFilePath = getFilePath;
	}
	
	//从setting到nickname传user
	public static User getNicknameUser;
	public static User getGetNicknameUser() {
		return getNicknameUser;
	}

	public static void setGetNicknameUser(User getNicknameUser) {
		SdPkUser.getNicknameUser = getNicknameUser;
	}
	
	//从setting到sex传user
	public static User getSexUser;
	public static User getGetSexUser() {
		return getSexUser;
	}

	public static void setGetSexUser(User getSexUser) {
		SdPkUser.getSexUser = getSexUser;
	}
	
	//从VerifyCodeView到bindingphone传验证码
	public static String getCode;
	public static String getGetCode() {
		return getCode;
	}

	public static void setGetCode(String getCode) {
		SdPkUser.getCode = getCode;
	}
	
	//微信注册的时候用
	public static boolean weixinRegistered;
	public static boolean isWeixinRegistered() {
		return weixinRegistered;
	}

	public static void setWeixinRegistered(boolean weixinRegistered) {
		SdPkUser.weixinRegistered = weixinRegistered;
	}
	
	//微信注册时传openid
	public static String openid;
	public static String getOpenid() {
		return openid;
	}

	public static void setOpenid(String openid) {
		SdPkUser.openid = openid;
	}
	
	//传home中的小组对象
	public static Group TeamSettinggroup;
	public static Group getTeamSettinggroup() {
		return TeamSettinggroup;
	}

	public static void setTeamSettinggroup(Group teamSettinggroup) {
		TeamSettinggroup = teamSettinggroup;
	}
	
	//退出登录
	public static boolean Exit;
	public static boolean isExit() {
		return Exit;
	}

	public static void setExit(boolean exit) {
		Exit = exit;
	}
	
	//退出小组
	public static boolean RefreshTeam;
	public static boolean isRefreshTeam() {
		return RefreshTeam;
	}

	public static void setRefreshTeam(boolean refreshTeam) {
		RefreshTeam = refreshTeam;
	}
	
	//小组邀请码
	public static boolean requestcode;
	public static boolean isRequestcode() {
		return requestcode;
	}

	public static void setRequestcode(boolean requestcode) {
		SdPkUser.requestcode = requestcode;
	}
	//绑定手机验证码
	public static String bindingphonecode;
	public static String getBindingphonecode() {
		return bindingphonecode;
	}

	public static void setBindingphonecode(String bindingphonecode) {
		SdPkUser.bindingphonecode = bindingphonecode;
	}
	
	//聊天室界面传User给成员界面
	public static List<User> User;
	public static List<User> getUser() {
		return User;
	}

	public static void setUser(List<User> user) {
		User = user;
	}
	//聊天室界面传创建者ID给成员界面
	public static String Creator;
	public static String getCreator() {
		return Creator;
	}

	public static void setCreator(String creator) {
		Creator = creator;
	}
}
