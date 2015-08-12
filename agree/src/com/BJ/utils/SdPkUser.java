package com.BJ.utils;

import java.util.ArrayList;

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
	
}
