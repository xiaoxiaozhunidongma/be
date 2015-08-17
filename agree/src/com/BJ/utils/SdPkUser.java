package com.BJ.utils;

import com.BJ.javabean.Group;




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
	//微信登录传true
	private static boolean getweixinLogin;
	public static boolean isGetweixinLogin() {
		return getweixinLogin;
	}

	public static void setGetweixinLogin(boolean getweixinLogin) {
		SdPkUser.getweixinLogin = getweixinLogin;
	}
	
	//传opendi
	private static String getopenid;
	public static String getGetopenid() {
		return getopenid;
	}

	public static void setGetopenid(String getopenid) {
		SdPkUser.getopenid = getopenid;
	}
	
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
}
