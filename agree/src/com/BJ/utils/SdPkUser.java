package com.BJ.utils;

import java.util.List;

import com.BJ.javabean.Group;
import com.BJ.javabean.User;

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
	//΢�Ű�
	private static boolean getweixinBinding;
	
	public static boolean isGetweixinBinding() {
		return getweixinBinding;
	}

	public static void setGetweixinBinding(boolean getweixinBinding) {
		SdPkUser.getweixinBinding = getweixinBinding;
	}
	
	//��Pk_party_user
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
	
	//��΢�Ŵ�user���и���
	public static User getUser;
	public static User getGetUser() {
		return getUser;
	}

	public static void setGetUser(User getUser) {
		SdPkUser.getUser = getUser;
	}
	//main��setting���д�ͼƬ·��
	public static String getFilePath;
	public static String getGetFilePath() {
		return getFilePath;
	}

	public static void setGetFilePath(String getFilePath) {
		SdPkUser.getFilePath = getFilePath;
	}
	
	//��setting��nickname��user
	public static User getNicknameUser;
	public static User getGetNicknameUser() {
		return getNicknameUser;
	}

	public static void setGetNicknameUser(User getNicknameUser) {
		SdPkUser.getNicknameUser = getNicknameUser;
	}
	
	//��setting��sex��user
	public static User getSexUser;
	public static User getGetSexUser() {
		return getSexUser;
	}

	public static void setGetSexUser(User getSexUser) {
		SdPkUser.getSexUser = getSexUser;
	}
	
	//��VerifyCodeView��bindingphone����֤��
	public static String getCode;
	public static String getGetCode() {
		return getCode;
	}

	public static void setGetCode(String getCode) {
		SdPkUser.getCode = getCode;
	}
	
	//΢��ע���ʱ����
	public static boolean weixinRegistered;
	public static boolean isWeixinRegistered() {
		return weixinRegistered;
	}

	public static void setWeixinRegistered(boolean weixinRegistered) {
		SdPkUser.weixinRegistered = weixinRegistered;
	}
	
	//΢��ע��ʱ��openid
	public static String openid;
	public static String getOpenid() {
		return openid;
	}

	public static void setOpenid(String openid) {
		SdPkUser.openid = openid;
	}
	
	//��home�е�С�����
	public static Group TeamSettinggroup;
	public static Group getTeamSettinggroup() {
		return TeamSettinggroup;
	}

	public static void setTeamSettinggroup(Group teamSettinggroup) {
		TeamSettinggroup = teamSettinggroup;
	}
	
	//�˳���¼
	public static boolean Exit;
	public static boolean isExit() {
		return Exit;
	}

	public static void setExit(boolean exit) {
		Exit = exit;
	}
	
	//�˳�С��
	public static boolean RefreshTeam;
	public static boolean isRefreshTeam() {
		return RefreshTeam;
	}

	public static void setRefreshTeam(boolean refreshTeam) {
		RefreshTeam = refreshTeam;
	}
	
	//С��������
	public static boolean requestcode;
	public static boolean isRequestcode() {
		return requestcode;
	}

	public static void setRequestcode(boolean requestcode) {
		SdPkUser.requestcode = requestcode;
	}
	//���ֻ���֤��
	public static String bindingphonecode;
	public static String getBindingphonecode() {
		return bindingphonecode;
	}

	public static void setBindingphonecode(String bindingphonecode) {
		SdPkUser.bindingphonecode = bindingphonecode;
	}
	
	//�����ҽ��洫User����Ա����
	public static List<User> User;
	public static List<User> getUser() {
		return User;
	}

	public static void setUser(List<User> user) {
		User = user;
	}
	//�����ҽ��洫������ID����Ա����
	public static String Creator;
	public static String getCreator() {
		return Creator;
	}

	public static void setCreator(String creator) {
		Creator = creator;
	}
}
