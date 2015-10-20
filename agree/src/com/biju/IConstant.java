package com.biju;

public interface IConstant {
	// 读取用户小组信息使用邀请码添加后的监听
	public String RequestCode = "requestcode";
	public String Refresh = "refresh";
	public String IsRefresh = "isRefresh2";
	public String IsCode2 = "isCode2";
	public String ReadHomeUser = "readhomeuser";
	public String HomePk_group = "pk_group";
	public String HomeGroupName = "homegroupname";

	// 使用在PartyFragment类中
	public String UserAll = "UserAll";
	public String UserAllParty = "UserAllParty";

	// 使用在ScheduleFragment类中
	public String OneParty = "oneParty";
	public String IsRelationship = "isRelationship";

	// 使用在PartyDetailsActivity类和CommentsListActivity中
	public String CommentsList = "CommentsList";
	public String Not_Say = "not_say";
	public String Refuse = "refuse";
	public String ParTake = "partake";
	public String UserAllUoreParty = "userAllmoreparty";
	public String MoreParty = "moreparty";
//	public String CommentsList_partake="commentslist_partake";
//	public String CommentsList_not_say="commentsList_not_say";

	// 使用在PartyDetailsActivity类，ScheduleFragment类，GroupActivity类的sp中
	public String IsPartyDetails_ = "isPartyDetails_";
	public String PartyDetails = "PartyDetails";

	// 使用在GroupActivity类中和TeamSettingActivity类中
	public String Group = "Group";

	// 使用在FriendsFragment类和FriendsDataActivity类和ChatActivity类中
	public String ReadUserAllFriends = "ReadUserAllFriends";
	public String Fk_user_from = "fk_user_from";
	public String Size = "size";
	public String AllFriends = "allFriends";

	// 使用在SettingFragment类和Bindingphone类中
	public String UserData = "UserData";
	public String Sdcard = "sdcard";

	// 从map到addnewparty
	public String IsMapChoose = "ismapchoose";
	public String IsAddress = "isAddress";
	public String MLng = "mLng";
	public String MLat = "mLat";
	public String IsMap = "ismap";

	// 从time到addnewparty
	public String IsTime = "istime";
	public String IsTimeChoose = "istimechoose";
	public String Hour = "hour";
	public String Minute = "minute";
	public String IsCalendar = "isCalendar";

	// 从ScheduleFragment到addnewparty
	public String Fk_group = "fk_group";
	public String AddRefresh = "addrefresh";
	public String IsAddRefresh = "isaddrefresh";
	public String IsSchedule = "isSchedule";

	// Schedule到partydetailes中
	public String Schedule = "Schedule";
	public String Pk_party_user = "pk_party_user";
	public String Pk_party = "pk_party";
	public String fk_group = "fk_group";

	// more到Schedule
	public String MoreRefresh = "morerefresh";
	public String Morecancle = "morecancle";

	// partyfragmnet到partydetails
	public String Partyfragmnet = "partyfragmnet";
	public String Partyfragmnet_Pk_party_user = "partyfragmnet_pk_party_user";
	public String Partyfragmnet_Pk_party = "partyfragmnet_pk_party";
	public String Partyfragmnet_fk_group = "partyfragmnet_fk_group";
	public String All_fk_group = "all_fk_group";
	public String Paymount="paymount";
	public String Payname="payname";
	
	
	//requestcode
	public String Requestcode_readhomeuser="requestcode_readhomeuser";
	
	//Sliding界面
	public String SlidingClick="slidingclick";
	public String Click="click";
	
	//Cost活动花费界面
	public String Cost="cost";
	public String IsCost="iscost";
	public String PayMoney="paymoney";
	
	//AddNewPartyActivity
	public String StartTimeChoose="starttimechoose";
	public String EndTimeChoose="endtimechoose";
	public String DeadlineTimeChoose="deadlinetimechoose";
	public String Time="time";
	public String StartMonths="startmonths";
	public String StartDay="startday";
	public String StartHour="starthour";
	public String StartTimeString="startTimestring";
	
	public String IsEndTimeChoose="isendtimechoose";
	public String EndTime="endtime";
	public String EndTimeDate="endtimedate";
	public String EndTimeHour="endtimehour";
	public String EndTimeMinute="endtimeminute";
	
	//LimitNumberActivity
	public String LimitNumber="limitnumber";
	public String IsNumber="isnumber";
	public String Number="number";
}
