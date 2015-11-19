package com.BJ.utils;

import java.util.ArrayList;
import java.util.List;

import com.BJ.javabean.Group;
import com.BJ.javabean.Group_ReadAllUser;
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
	public static ArrayList<User> User;
	public static ArrayList<User> getUser() {
		return User;
	}

	public static void setUser(ArrayList<User> user) {
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
	
	//从聊天室界面点击头像传User到头像界面
	public static User ClickUser;
	public static User getClickUser() {
		return ClickUser;
	}

	public static void setClickUser(User clickUser) {
		ClickUser = clickUser;
	}
	
	//从群聊界面点击头像传User到头像界面
	public static Group_ReadAllUser GroupChatUser;
	
	
	public static Group_ReadAllUser getGroupChatUser() {
		return GroupChatUser;
	}

	public static void setGroupChatUser(Group_ReadAllUser groupChatUser) {
		GroupChatUser = groupChatUser;
	}

	//从聊天界面点击头像传User到头像界面,群聊的
	public static List<Group_ReadAllUser> HomeClickUser;
	

	public static List<Group_ReadAllUser> getHomeClickUser() {
		return HomeClickUser;
	}

	public static void setHomeClickUser(List<Group_ReadAllUser> homeClickUser) {
		HomeClickUser = homeClickUser;
	}

	//判断是从群聊来的还是聊天室来的
	public static int GetSource;
	
	public static int getGetSource() {
		return GetSource;
	}

	public static void setGetSource(int getSource) {
		GetSource = getSource;
	}

	//新建聚会传pk_party
	public static String Getpk_party;
	public static String getGetpk_party() {
		return Getpk_party;
	}

	public static void setGetpk_party(String getpk_party) {
		Getpk_party = getpk_party;
	}
	
	//判断是分享的回调还是登陆的回调
	public static boolean GetWeSource;
	public static boolean isGetWeSource() {
		return GetWeSource;
	}

	public static void setGetWeSource(boolean getWeSource) {
		GetWeSource = getWeSource;
	}
	
	//新建小组添加好友传list
	public static List<String> TeamFriendsList;
	public static List<String> getTeamFriendsList() {
		return TeamFriendsList;
	}

	public static void setTeamFriendsList(List<String> teamFriendsList) {
		TeamFriendsList = teamFriendsList;
	}
	
	//传一个小组中的所有成员list
	public static List<Group_ReadAllUser> GetGroup_ReadAllUser;
	public static List<Group_ReadAllUser> getGetGroup_ReadAllUser() {
		return GetGroup_ReadAllUser;
	}

	public static void setGetGroup_ReadAllUser(
			List<Group_ReadAllUser> getGroup_ReadAllUser) {
		GetGroup_ReadAllUser = getGroup_ReadAllUser;
	}
	
	//群聊好友信息界面传是否打开信息
	public static boolean GetOpen;
	public static boolean isGetOpen() {
		return GetOpen;
	}

	public static void setGetOpen(boolean getOpen) {
		GetOpen = getOpen;
	}
	
	//群聊传对象给好友信息界面
	public static Group_ReadAllUser group_ReadAllUser;
	public static Group_ReadAllUser getGroup_ReadAllUser() {
		return group_ReadAllUser;
	}

	public static void setGroup_ReadAllUser(Group_ReadAllUser group_ReadAllUser) {
		SdPkUser.group_ReadAllUser = group_ReadAllUser;
	}
	
	//聊天室好友信息界面传是否打开信息
	public static boolean GetChatRoomOpen;
	public static boolean isGetChatRoomOpen() {
		return GetChatRoomOpen;
	}

	public static void setGetChatRoomOpen(boolean getChatRoomOpen) {
		GetChatRoomOpen = getChatRoomOpen;
	}
	
	//聊天室传对象给好友信息界面
	public static User ChatRoomUser;
	public static User getChatRoomUser() {
		return ChatRoomUser;
	}

	public static void setChatRoomUser(User chatRoomUser) {
		ChatRoomUser = chatRoomUser;
	}
}
