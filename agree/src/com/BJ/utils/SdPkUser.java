package com.BJ.utils;

import com.BJ.javabean.Group;




public class SdPkUser {
	//���ӵ�¼����ע������pk_user
	private static Integer sD_pk_user;
	public static Integer getsD_pk_user() {
		return sD_pk_user;
	}

	public static void setsD_pk_user(Integer sD_pk_user) {
		SdPkUser.sD_pk_user = sD_pk_user;
	}

	//��ע������һ��true���жϲ��ֵ�����
	private static boolean isRegistered_one;
	public static boolean isRegistered_one() {
		return isRegistered_one;
	}

	public static void setRegistered_one(boolean isRegistered_one) {
		SdPkUser.isRegistered_one = isRegistered_one;
	}
	//΢�ŵ�¼��true
	private static boolean getweixinLogin;
	public static boolean isGetweixinLogin() {
		return getweixinLogin;
	}

	public static void setGetweixinLogin(boolean getweixinLogin) {
		SdPkUser.getweixinLogin = getweixinLogin;
	}
	
	//��opendi
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
	
	//ʹ�����������С��ʱ���ж���Ĵ�ֵ
	public static Group getgroup;
	public static Group getGetgroup() {
		return getgroup;
	}

	public static void setGetgroup(Group getgroup) {
		SdPkUser.getgroup = getgroup;
	}
}
