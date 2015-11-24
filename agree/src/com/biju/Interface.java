package com.biju;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.BJ.javabean.Chat;
import com.BJ.javabean.CreateGroup;
import com.BJ.javabean.CreateOrder;
import com.BJ.javabean.FeedBack;
import com.BJ.javabean.Group;
import com.BJ.javabean.Group_Code;
import com.BJ.javabean.Group_User;
import com.BJ.javabean.IDs;
import com.BJ.javabean.ImageText;
import com.BJ.javabean.JsonAddParty;
import com.BJ.javabean.MapAddParty;
import com.BJ.javabean.MultiUserModle;
import com.BJ.javabean.Order;
import com.BJ.javabean.Party;
import com.BJ.javabean.Party_User;
import com.BJ.javabean.PaymentAccount;
import com.BJ.javabean.Phone;
import com.BJ.javabean.PhoneArray;
import com.BJ.javabean.Photo;
import com.BJ.javabean.Photo_Review;
import com.BJ.javabean.StrPhoneArray;
import com.BJ.javabean.StringCreGroup;
import com.BJ.javabean.TeamAddNewMemberJson;
import com.BJ.javabean.TeamAddNewMemberModel;
import com.BJ.javabean.UnionPay;
import com.BJ.javabean.User;
import com.BJ.javabean.User_Chat;
import com.BJ.javabean.User_User;
import com.BJ.javabean.WeChatPay;
import com.BJ.utils.Bean2Map;
import com.android.volley.VolleyError;
import com.github.volley_examples.app.MyVolley;
import com.github.volley_examples.app.VolleyListenner;

public class Interface {

	// String url="http://120.25.218.3/webroot/sr_interface_android.php";
	String url = "http://120.26.118.226/app/app_interface/sr_interface.php";

	private static Interface Thisinterface = new Interface();

	private Interface() {

	}

	public static Interface getInstance() {
		return Thisinterface;
	}

	//小组添加新的成员
	String KTeamAddFriends="391";
	//生成财务订单
	String KCreateOrder="416";
	//更改小组名称
	String kChangeGroupName = "312";
	// 查看图片评论列表接口
	String KCheckPicReview = "66";
	// 添加图片评论接口
	String KAddPicReview = "65";
	// 获取聚会的图文详情
	String KReadGraphic = "418";
	// 用户的提现
	String KOrder = "116";
	// 删除用户的取现账户
	String KDeletePayMentAccount = "117";
	// 添加用户的取现账户
	String KPayMentAccount = "114";
	// 读取用户的取现账户
	String KReadPayMentAccount = "115";
	// 计算用户的余额
	String KBalance = "113";
	// 查询多个用户
	String KMultiUsers = "120";
	// 我的所有好友
	String KMyAllfriends = "54";
	// 银联支付
	String KUnionPay = "9912";
	// 支付宝支付
	String KAliPay = "9901";
	// 微信支付
	String KWeChatPay = "9911";
	// 微信登录
	String KWeixinLogin = "1102";
	// 注册用户
	String kRegAccount = "11";
	// 检查昵称重复
	String kCheckNickname = "111";
	// 用户登录
	String kUserLgin = "12";
	// 更新用户的资料
	String kUpdateUser = "15";
	// 读取用户的资料
	String kReadUser = "17";
	// 请求服务器发送验证码
	String kRequestVerCode = "18";
	// 根据手机号或者账户ID查找用户
	String kFindUser = "19";//19
	// 新建小组
	String kCreateGroup = "31";
	// 读取用户小组信息
	String kReadGroupMsg = "32";
	// 读取用户小组关系
	String kReadGroupRelation = "33";
	// 更新用户小组设置
	String kUpdateGroupSet = "34";
	// 读取所有小组成员的关系
	String kReadAllPerRelation = "36";
	// 生成邀请码
	String kProduceRequestCode = "37";
	// 使用邀请码加入小组
	String kUseRequestCode2join = "38";
	// 用户加入小组
	String kUserJoin2gourp = "39";
	// 读取用户所有聚会
	String kReadUserAllParty = "41";
	// 读取用户在小组中的所有聚会
	String kReadUserGroupParty = "43";
	// 读取用户在小组中的所有聚会包含过期
	String kReadUserGroupPartyAll = "64";
	// 更新用户对于聚会的参与信息
	String kUpdateUserJoinMsg = "45";
	// 用户取消聚会
	String kUserCancleParty = "46";
	// 增加聚会
	String kAddParty = "47";
	// 读取聚会用户的参与信息
	String kReadPartyJoinMsg = "48";
	// 建立一条新的聚会关系
	String kCreatePartyRelation = "49";
	// 添加一条聊天数据
	String kAddChatData = "52";
	// 读取小组的相册
	String kReadPartyPhotos = "61";
	// 用户上传图片
	String kUploadingPhoto = "62";
	// 用户删除图片
	String kDeletePhoto = "63";
	// 添加好友关系
	String kAddfriend = "71";
	// 读取好友关系
	String kReadfriend = "72";
	// 匹配通讯录
	String kMateComBook = "73";
	// 成为好友关系
	String kBecomeFriend = "74";
	// 检查好友关系
	String kCheckFriend = "75";
	// 解除好友关系
	String kReleaseFriend = "76";
	// 增加一条私聊信息
	String kAddChatMsg = "81";
	// 反馈
	String kFeedBack = "91";
	// 获取图片签名
	String kGetPictureSign = "101";
	// test新建小组
	String testGroup = "{'group':{'last_post_message':'U5c0fU7ec4U6210U7acbU5566!','last_post_time':'2015-06-11 11:43:58',"
			+ "'em_id':'1433994238427','name':'test','setup_time':'2015-06-11 11:43:58'},'member':[{'fk_user':30,'role':1}]}";
	// test
	String kTestInterface = "1101";

	public Map<String, String> packParams(Object classObject,
			String interfaceType) {
		Map map = Bean2Map.ConvertObjToMap(classObject);
		Log.e("Interface", "map------" + map);
		JSONObject jsonObject = new JSONObject(map);
		Map<String, String> params = new HashMap<String, String>();

		params.put("request_type", interfaceType);
		Log.e("Interface", "小组json:" + jsonObject.toString());
		params.put("request_data", jsonObject.toString());

		return params;
	}
	//小组添加新成员
	public Map<String, String> packParamsNewMembers(Object classObject,
			String interfaceType) {
		JSONArray jsonArray = new JSONArray();
		Group_User[] members = ((TeamAddNewMemberModel)classObject).getMembers();
		for (int i = 0; i < members.length; i++) {
			Group_User group_User = members[i];
			
			Map map = Bean2Map.ConvertObjToMap(group_User);
			Log.e("Interface", "map------" + map);
			JSONObject jsonObject = new JSONObject(map);
			jsonArray.put(jsonObject);
			
		}
		
		TeamAddNewMemberJson teamAddNewMemberJson = new TeamAddNewMemberJson(jsonArray);
		Map map = Bean2Map.ConvertObjToMap(teamAddNewMemberJson);
		JSONObject jsonObject = new JSONObject(map);
		
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("request_type", interfaceType);
		Log.e("Interface", "小组json:" + jsonObject.toString());
		params.put("request_data", jsonObject.toString());
		
		return params;
	}

	ArrayList<String> ImageTextList = new ArrayList<String>();

	public Map<String, String> packParamsAddParty(Object classObject,
			String interfaceType) {

		Map map = Bean2Map.ConvertObjToMap(((MapAddParty) classObject)
				.getParty());
		JSONObject jsonObject = new JSONObject(map);
		Log.e("Interface", "map------" + map);
		
		List<ImageText> remarkArray = ((MapAddParty) classObject).getRemarkArray();
		JSONArray jsonArray = new JSONArray();
//		ImageTextList.clear();//先清空
		for (int i = 0; i < remarkArray.size(); i++) {
			ImageText imageText = remarkArray.get(i);
			Map maps = Bean2Map.ConvertObjToMap(imageText);
			JSONObject jsonObjects = new JSONObject(maps);
			jsonArray.put(jsonObjects);
//			ImageTextList.add(imageText.toString());
		}
//		String string2 = ImageTextList.toString();
//		jsonArray = new JSONArray(ImageTextList);
		Log.e("Interface", "jsonArray------" + jsonArray);
		JsonAddParty jsonAddParty = new JsonAddParty(jsonArray, jsonObject);
		Map<String, String> params = new HashMap<String, String>();

		Map map2 = Bean2Map.ConvertObjToMap(jsonAddParty);
		Log.e("Interface", "map2------" + map2);
		JSONObject jsonObject2 = new JSONObject(map2);
		params.put("request_type", interfaceType);
		Log.e("Interface", "小组json:" + jsonObject2.toString());
		params.put("request_data", jsonObject2.toString().replace("\\", ""));
		return params;
	}

	// 通讯录的packParams方法
	ArrayList<String> string = new ArrayList<String>();

	public Map<String, String> packParamsPhone(Object classObject,
			String interfaceType) throws JSONException {

		for (int i = 0; i < ((PhoneArray) classObject).getPhones().length; i++) {
			String Strphone = ((PhoneArray) classObject).getPhones()[i];
			string.add(Strphone);
		}

		JSONArray array = new JSONArray(string.toString());
		Integer user_id = ((PhoneArray) classObject).getUser_id();
		StrPhoneArray strPhoneArray = new StrPhoneArray(user_id, array);

		Map map = Bean2Map.ConvertObjToMap(strPhoneArray);
		Log.e("Interface", "map------" + map);
		JSONObject jsonObject = new JSONObject(map);
		Map<String, String> params = new HashMap<String, String>();

		params.put("request_type", interfaceType);
		Log.e("Interface", "小组json:" + jsonObject.toString());
		params.put("request_data", jsonObject.toString());

		return params;
	}

	class MyData {
		private Integer fk_user;
		private Integer role;

		public MyData(Integer fk_user, Integer role) {
			super();
			this.fk_user = fk_user;
			this.role = role;
		}

		@Override
		public String toString() {
			return "{fk_user=" + fk_user + ", role=" + role + "}";
		}

	}

	// 新建小组的packParams（）方法
	private StringCreGroup creGroup;
	ArrayList<String> list = new ArrayList<String>();

	public Map<String, String> packParams3(Object classObject,
			String interfaceType) {
		list.clear();// 先清空
		try {
			for (int i = 0; i < ((CreateGroup) classObject).getMember().length; i++) {
				MyData myData = new MyData(
						((CreateGroup) classObject).getMember()[i].getFk_user(),
						((CreateGroup) classObject).getMember()[i].getRole());
				list.add(myData.toString());
			}
			JSONArray array = new JSONArray(list.toString());
			Map usemap = Bean2Map.ConvertObjToMap(((CreateGroup) classObject)
					.getGroup());
			JSONObject jsonObject = new JSONObject(usemap);
			creGroup = new StringCreGroup(array, jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		Map map = Bean2Map.ConvertObjToMap(creGroup);
		Log.e("Interface3", "map------" + map);
		JSONObject jsonObject = new JSONObject(map);
		Map<String, String> params = new HashMap<String, String>();
		params.put("request_type", interfaceType);
		Log.e("Interface3", "小组json:" + jsonObject.toString().replace("\\", ""));
		params.put("request_data", jsonObject.toString().replace("\\", ""));

		return params;
	}

	private MultiUserModle multiUserModle;

	public Map<String, String> packParamsMulti(List<String> list,
			String interfaceType) {
		try {
			JSONArray array = new JSONArray(list.toString());
			multiUserModle = new MultiUserModle(array);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		Map map = Bean2Map.ConvertObjToMap(multiUserModle);
		Log.e("InterfaceMulti", "map------" + map);
		JSONObject jsonObject = new JSONObject(map);
		Map<String, String> params = new HashMap<String, String>();
		params.put("request_type", interfaceType);
		Log.e("InterfaceMulti",
				"小组json:" + jsonObject.toString().replace("\\", ""));
		params.put("request_data", jsonObject.toString().replace("\\", ""));

		return params;
	}

	// test新建小组
	public Map<String, String> packParams2(Object classObject,
			String interfaceType) {
		Map map = Bean2Map.ConvertObjToMap(classObject);
		JSONObject jsonObject = new JSONObject(map);
		Map<String, String> params = new HashMap<String, String>();
		params.put("request_type", interfaceType);
		try {
			JSONObject object = new JSONObject(testGroup);
			Log.e("Interface2", "小组json:" + object.toString());
			params.put("request_data", object.toString());
			Log.e("", "4556645645646" + params.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return params;
	}

	// public void volleyPost(Context context,Map<String, String> params) {
	//
	// MyVolley.post(context, url, params, new VolleyListenner() {
	//
	// @Override
	// public void onErrorResponse(VolleyError error) {
	// requestError(error);
	// Log.e("失败", ""+error);
	// }
	// @Override
	// public void onResponse(String response) {
	// requestDone(response);
	// }
	// });
	// }

	//小组添加新的成员	
	private void TeamAddFriendsPost(Context context, Map<String, String> params) {
		
		MyVolley.post(context, url, params, new VolleyListenner() {
			
			@Override
			public void onErrorResponse(VolleyError error) {
				requestError55(error);
				Log.e("失败", "" + error);
			}
			
			@Override
			public void onResponse(String response) {
				requestDone55(response);
			}
		});
	}
	
	//生成财务订单	
	private void CreateOrderPost(Context context, Map<String, String> params) {
		
		MyVolley.post(context, url, params, new VolleyListenner() {
			
			@Override
			public void onErrorResponse(VolleyError error) {
				requestError54(error);
				Log.e("失败", "" + error);
			}
			
			@Override
			public void onResponse(String response) {
				requestDone54(response);
			}
		});
	}
	
	//添加图片评论接口	
	private void AddPicReviewPost(Context context, Map<String, String> params) {
		
		MyVolley.post(context, url, params, new VolleyListenner() {
			
			@Override
			public void onErrorResponse(VolleyError error) {
				requestError52(error);
				Log.e("失败", "" + error);
			}
			
			@Override
			public void onResponse(String response) {
				requestDone52(response);
			}
		});
	}
	
	//查看图片评论列表接口
	private void CheckPicReviewPost(Context context, Map<String, String> params) {
		
		MyVolley.post(context, url, params, new VolleyListenner() {
			
			@Override
			public void onErrorResponse(VolleyError error) {
				requestError53(error);
				Log.e("失败", "" + error);
			}
			
			@Override
			public void onResponse(String response) {
				requestDone53(response);
			}
		});
	}
	// 更改小组的名称
	private void ChangeGroupNamePost(Context context, Map<String, String> params) {
		
		MyVolley.post(context, url, params, new VolleyListenner() {
			
			@Override
			public void onErrorResponse(VolleyError error) {
				requestError51(error);
				Log.e("失败", "" + error);
			}
			
			@Override
			public void onResponse(String response) {
				requestDone51(response);
			}
		});
	}
	
	// 获取聚会的图文详情
	private void ReadGraphicPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError50(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone50(response);
			}
		});
	}

	// 用户的提现
	private void OrderPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError49(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone49(response);
			}
		});
	}

	// 删除用户的取现账户
	private void DeletePayMentAccountPost(Context context,
			Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError48(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone48(response);
			}
		});
	}

	// 添加用户的取现账户
	private void PayMentAccountPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError47(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone47(response);
			}
		});
	}

	// 读取用户的取现账户
	private void ReadPayMentAccountPost(Context context,
			Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError46(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone46(response);
			}
		});
	}

	// 计算用户的余额
	private void BalancePost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError44(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone44(response);
			}
		});
	}

	// 查询多个用户
	private void MultiUserPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError43(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone43(response);
			}
		});
	}

	// 我的所有好友
	private void MyAllfriendPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError42(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone42(response);
			}
		});
	}

	// 银联支付
	private void UnionPayPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError41(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone41(response);
			}
		});
	}

	// 支付宝支付
	private void AliPayPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError40(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone40(response);
			}
		});
	}

	// 微信支付
	private void WeChatPayPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError39(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone39(response);
			}
		});
	}

	// 微信登录
	private void weixinLoginPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError38(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone38(response);
			}
		});
	}

	private void regNewAccountPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError37(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone37(response);
			}
		});
	}

	private void checkNicknamePost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError36(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone36(response);
			}
		});
	}

	private void userLoginPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError35(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone35(response);
			}
		});
	}

	private void updateUserPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError34(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone34(response);
			}
		});
	}

	private void readUserPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError33(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone33(response);
			}
		});
	}

	private void requestVerCodePost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError32(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone32(response);
			}
		});
	}

	private void findUserPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError31(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone31(response);
			}
		});
	}

	private void createGroupPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError30(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone30(response);
			}
		});
	}

	private void readUserGroupMsgPost(Context context,
			Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError29(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone29(response);
			}
		});
	}

	private void readUserGroupRelationPost(Context context,
			Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError28(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone28(response);
			}
		});
	}

	private void updateGroupSetPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {
			// 26？？？？
			@Override
			public void onErrorResponse(VolleyError error) {
				requestError26(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone26(response);
			}
		});
	}

	private void readAllPerRelationPost(Context context,
			Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError25(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone25(response);
			}
		});
	}

	private void produceRequestCodePost(Context context,
			Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError24(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone24(response);
			}
		});
	}

	private void useRequestCode2JoinPost(Context context,
			Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError23(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone23(response);
			}
		});
	}

	private void userJoin2gourpPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError22(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone22(response);
			}
		});
	}

	private void readUserAllPartyPost(Context context,
			Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError21(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone21(response);
			}
		});
	}

	private void readUserGroupPartyPost(Context context,
			Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError20(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone20(response);
			}
		});
	}
	
	//包含过期
	private void readUserGroupPartyAllPost(Context context,
			Map<String, String> params) {
		
		MyVolley.post(context, url, params, new VolleyListenner() {
			
			@Override
			public void onErrorResponse(VolleyError error) {
				requestError45(error);
				Log.e("失败", "" + error);
			}
			
			@Override
			public void onResponse(String response) {
				requestDone45(response);
			}
		});
	}

	private void updateUserJoinMsgPost(Context context,
			Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError19(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone19(response);
			}
		});
	}

	private void userCanclePartyPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError18(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone18(response);
			}
		});
	}

	private void addPartyPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError17(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone17(response);
			}
		});
	}

	private void readPartyJoinMsgPost(Context context,
			Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError16(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone16(response);
			}
		});
	}

	private void createPartyRelationPost(Context context,
			Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError15(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone15(response);
			}
		});
	}

	private void addChatdataPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError14(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone14(response);
			}
		});
	}

	private void readPartyPhotosPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError13(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone13(response);
			}
		});
	}

	private void uploadingPhotoPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError12(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone12(response);
			}
		});
	}

	private void deletePhotoPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError11(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone11(response);
			}
		});
	}

	private void addFriendPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError10(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone10(response);
			}
		});
	}

	private void readFriendPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError9(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone9(response);
			}
		});
	}

	private void mateComBookPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError8(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone8(response);
			}
		});
	}

	private void becomeFriendPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError7(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone7(response);
			}
		});
	}

	private void checkFriendPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError6(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone6(response);
			}
		});
	}

	private void releaseFriendPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError5(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone5(response);
			}
		});
	}

	private void addChatMsgPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError4(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone4(response);
			}
		});
	}

	private void feedBackPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError3(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone3(response);
			}
		});
	}

	private void getPicSigntPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError2(error);
				Log.e("失败", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone2(response);
			}
		});
	}

	// 测试
	// public void testIf(Context context) {
	//
	// Map<String, String> testMap = new HashMap<String, String>();
	// testMap.put("name", "jim");
	// JSONObject jsonObject=new JSONObject(testMap);
	//
	// Map<String, String> per = new HashMap<String, String>();
	// per.put("request_data", jsonObject.toString());
	// per.put("request_type", kTestInterface);
	//
	// volleyPost(context,per);
	// }

	// 小组添加新的成员
	public void TeamAddFriends(Context context, TeamAddNewMemberModel TeamAddNewMemberModel) {
		TeamAddFriendsPost(context, packParamsNewMembers(TeamAddNewMemberModel, KTeamAddFriends));
	}
	
	// 生成财务订单
	public void CreateOrder(Context context, CreateOrder createOrder) {
		CreateOrderPost(context, packParams(createOrder, KCreateOrder));
	}
	
	// 添加图片评论接口
	public void AddPicReview(Context context, Photo_Review photo_Review) {
		AddPicReviewPost(context, packParams(photo_Review, KAddPicReview));
	}
	
	// 查看图片评论列表接口
	public void CheckPicReview(Context context, Photo Photo) {
		CheckPicReviewPost(context, packParams(Photo, KCheckPicReview));
	}
	
	// 更改小组名称
	public void ChangeGroupName(Context context, Group group) {
		ChangeGroupNamePost(context, packParams(group, kChangeGroupName));
	}
	
	// 获取聚会的图文详情
	public void ReadGraphic(Context context, Party party) {
		ReadGraphicPost(context, packParams(party, KReadGraphic));
	}

	// 用户的取现
	public void Order(Context context, Order user_order) {
		OrderPost(context, packParams(user_order, KOrder));
	}

	// 删除用户的取现账户
	public void DeletePayMentAccount(Context context,PaymentAccount paymentAccount) {
		DeletePayMentAccountPost(context,packParams(paymentAccount, KDeletePayMentAccount));
	}

	// 添加用户的取现账户
	public void PayMentAccount(Context context, PaymentAccount paymentAccount) {
		PayMentAccountPost(context, packParams(paymentAccount, KPayMentAccount));
	}

	// 读取用户的取现账户
	public void ReadPayMentAccount(Context context, User user) {
		ReadPayMentAccountPost(context, packParams(user, KReadPayMentAccount));
	}

	// 计算用户的余额
	public void Balance(Context context, User user) {
		BalancePost(context, packParams(user, KBalance));
	}

	// 查询多个用户
	public void findMultiUsers(Context context, List<String> list) {
		MultiUserPost(context, packParamsMulti(list, KMultiUsers));
	}

	// 我的所有好友
	public void readMyAllfriend(Context context, User user) {
		MyAllfriendPost(context, packParams(user, KMyAllfriends));
	}

	// 银联支付
	public void UnionPay(Context context, UnionPay unionpay) {
		UnionPayPost(context, packParams(unionpay, KUnionPay));
	}

	// 支付宝支付
	public void AliPay(Context context, WeChatPay wechatpay) {
		AliPayPost(context, packParams(wechatpay, KAliPay));
	}

	// 微信支付
	public void WeChatPay(Context context, WeChatPay wechatpay) {
		WeChatPayPost(context, packParams(wechatpay, KWeChatPay));
	}

	// 微信登录
	public void weixinLogin(Context context, User user) {
		weixinLoginPost(context, packParams(user, KWeixinLogin));
	}

	// 注册新用户
	public void regNewAccount(Context context, User user) {
		regNewAccountPost(context, packParams(user, kRegAccount));
	}

	// 检查昵称重复
	public void checkNickname(Context context, User user) {
		checkNicknamePost(context, packParams(user, kCheckNickname));
	}

	// 用户登录
	public void userLogin(Context context, User user) {
		userLoginPost(context, packParams(user, kUserLgin));
	}

	// 更新用户的资料
	public void updateUser(Context context, User user) {
		updateUserPost(context, packParams(user, kUpdateUser));
	}

	// 读取用户的资料
	public void readUser(Context context, User user) {
		readUserPost(context, packParams(user, kReadUser));
	}

	// 请求服务器发送验证码
	/**
	 * @param context
	 * @param user
	 *            this user can be null!(这个对象可以是空的)
	 */
	public void requestVerCode(Context context, Phone phone) {// 传入为??
		requestVerCodePost(context, packParams(phone, kRequestVerCode));
	}

	// 根据手机号或者账户ID查找用户
	public void findUser(Context context, User user) {
		findUserPost(context, packParams(user, kFindUser));
	}

	// 新建小组
	public void createGroup(Context context, CreateGroup creatGroup) {
		createGroupPost(context, packParams3(creatGroup, kCreateGroup));
	}

	// //test新建小组
	// public void createGroup2(Context context,StringCreGroup creGroup) {
	// volleyPost(context,packParams2(creGroup, kCreateGroup));
	// }
	// 读取用户小组信息
	public void readUserGroupMsg(Context context, User user) {
		readUserGroupMsgPost(context, packParams(user, kReadGroupMsg));
	}

	// 读取用户小组关系
	public void readUserGroupRelation(Context context, Group_User group_User) {
		readUserGroupRelationPost(context,packParams(group_User, kReadGroupRelation));
	}

	// 更新用户小组设置
	public void updateGroupSet(Context context, Group_User group_User) {
		updateGroupSetPost(context, packParams(group_User, kUpdateGroupSet));
	}

	// 读取所有小组成员的关系
	public void readAllPerRelation(Context context, Group group) {
		readAllPerRelationPost(context, packParams(group, kReadAllPerRelation));
	}

	// 生成邀请码
	public void produceRequestCode(Context context, Group group) {
		produceRequestCodePost(context, packParams(group, kProduceRequestCode));
	}

	// 使用邀请码加入小组
	public void useRequestCode2Join(Context context, Group_Code code) {
		useRequestCode2JoinPost(context, packParams(code, kUseRequestCode2join));
	}

	// 用户加入小组
	public void userJoin2gourp(Context context, Group_User group_User) {
		userJoin2gourpPost(context, packParams(group_User, kUserJoin2gourp));
	}

	// 读取用户所有聚会
	public void readUserAllParty(Context context, User user) {
		readUserAllPartyPost(context, packParams(user, kReadUserAllParty));
	}

	// 读取用户在小组中的所有聚会
	public void readUserGroupParty(Context context, IDs idsmap) {// ...........传入字典
		readUserGroupPartyPost(context, packParams(idsmap, kReadUserGroupParty));
	}
	// 读取用户在小组中的所有聚会包含过期
	public void readUserGroupPartyAll(Context context, Group group) {
		readUserGroupPartyAllPost(context, packParams(group, kReadUserGroupPartyAll));
	}

	// 更新用户对于聚会的参与信息
	public void updateUserJoinMsg(Context context, Party_User party_User) {
		updateUserJoinMsgPost(context,packParams(party_User, kUpdateUserJoinMsg));
	}

	// 用户取消聚会
	public void userCancleParty(Context context, Party party) {
		userCanclePartyPost(context, packParams(party, kUserCancleParty));
	}

	// 增加聚会
	public void addParty(Context context, MapAddParty mapAddParty) {
		addPartyPost(context, packParamsAddParty(mapAddParty, kAddParty));
	}

	// 读取聚会用户的参与信息
	public void readPartyJoinMsg(Context context, Party party) {
		readPartyJoinMsgPost(context, packParams(party, kReadPartyJoinMsg));
	}

	// 建立一条新的聚会关系
	public void createPartyRelation(Context context, Party_User party_User) {
		createPartyRelationPost(context,packParams(party_User, kCreatePartyRelation));
	}

	// 添加一条聊天数据
	public void addChatdata(Context context, Chat chat) {
		addChatdataPost(context, packParams(chat, kAddChatData));
	}

	// 读取小组的相册
	public void readPartyPhotos(Context context, Group group) {
		readPartyPhotosPost(context, packParams(group, kReadPartyPhotos));
	}

	// 用户上传图片
	public void uploadingPhoto(Context context, Photo photo) {
		uploadingPhotoPost(context, packParams(photo, kUploadingPhoto));
	}

	// 用户删除图片
	public void deletePhoto(Context context, Photo photo) {
		deletePhotoPost(context, packParams(photo, kDeletePhoto));
	}

	// 添加好友关系
	public void addFriend(Context context, User_User user_User) {
		addFriendPost(context, packParams(user_User, kAddfriend));
	}

	// 读取好友关系
	public void readFriend(Context context, User user) {
		readFriendPost(context, packParams(user, kReadfriend));
	}

	// 匹配通讯录
	/**
	 * 
	 * @param context
	 * @param user
	 *            this user can be null!(这个对象属性可以是空的)
	 */
	public void mateComBook(Context context, PhoneArray phonearray) { // ........传入？？？
		try {
			mateComBookPost(context, packParamsPhone(phonearray, kMateComBook));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 成为好友关系
	public void becomeFriend(Context context, User_User user_User) {
		becomeFriendPost(context, packParams(user_User, kBecomeFriend));
	}

	// 检查好友关系
	public void checkFriend(Context context, User_User user_User) {
		checkFriendPost(context, packParams(user_User, kCheckFriend));
	}

	// 解除好友关系
	public void releaseFriend(Context context, User_User user_User) {
		releaseFriendPost(context, packParams(user_User, kReleaseFriend));
	}

	// 增加一条私聊信息
	public void addChatMsg(Context context, User_Chat user_Chat) {
		addChatMsgPost(context, packParams(user_Chat, kAddChatMsg));
	}

	// 反馈
	public void feedBack(Context context, FeedBack feedBack) {
		feedBackPost(context, packParams(feedBack, kFeedBack));
	}

	// 获取图片签名
	/**
	 * 
	 * @param context
	 * @param user
	 *            this user can be null!(这个对象属性可以是空的)
	 */
	public void getPicSign(Context context, User user) { // .....传入？？？？？
		getPicSigntPost(context, packParams(user, kGetPictureSign));
	}

	// 接口部分
	// private static UserInterface listener;

	// 小组添加新的成员
	private static TeamAddFriendsListenner teamaddfriendslistenner;
	// 生成财务订单
	private static CreateOrderListenner createorderlistenner;
	// 添加图片评论接口
	private static AddPicReviewListenner addPicReviewListenner;
	// 查看图片评论列表接口
	private static CheckPicReviewListenner checkPicReviewListenner;
	// 更改小组名称
	private static ChangeGroupNameListenner changegroupnamelistenner;
	// 读取小组中所有聚会包含过期
	private static ReadGroupPartyAlllistenner readGroupPartyAlllistenner;
	// 查询多个用户
	// 获取聚会的图文详情
	private static ReadGraphicListenner readgraphicListenner;
	// 用户的取现
	private static OrderListenner orderListenner;
	// 删除用户的取现账户
	private static DeletePayMentAccountListenner deletepaymentaccountListenner;
	// 添加用户的取现账户
	private static PayMentAccountListenner paymentaccountListenner;
	// 读取用户的取现账户
	private static ReadPayMentAccountListenner readpaymentaccountListenner;
	// 计算用户的余额
	private static BalanceListenner balanceListenner;
	// 我的所有好友
	private static FindMultiUserListenner multiUserListenner;
	// 我的所有好友
	private static MyAllfriendsListenner myAllfriendsListenner;
	// 微信登录
	private static weixinLoginListenner weixinloginListenner;
	// 微信支付
	private static WeChatPayListenner wechatpayListenner;
	// 支付宝支付
	private static AliPayListenner alipayListenner;
	// 银联支付
	private static UnionPayListenner unionpayListenner;

	private static regNewAccountListenner newAccountListenner;
	private static checkNicknameListenner nicknameListenner;
	private static userLoginListenner loginListenner;
	private static updateUserListenner updateUserListenner;
	private static readUserListenner readuserListenner;
	private static requestVerCodeListenner verCodeListenner;
	private static findUserListenner finduserListenner;
	private static createGroupListenner groupListenner;
	private static readUserGroupMsgListenner userGroupMsgListenner;
	private static readUserGroupRelationListenner groupRelationListenner;
	private static updateGroupSetListenner groupSetListenner;
	private static readAllPerRelationListenner allPerRelationListenner;
	private static produceRequestCodeListenner requestCodeListenner;
	private static useRequestCode2JoinListenner requestCode2JoinListenner;
	private static userJoin2gourpListenner join2gourpListenner;
	private static readUserAllPartyListenner allPartyListenner;
	private static readUserGroupPartyListenner userGroupPartyListenner;
	private static updateUserJoinMsgListenner userJoinMsgListenner;
	private static userCanclePartyListenner canclePartyListenner;
	private static addPartyListenner partyListenner;
	private static readPartyJoinMsgListenner joinMsgListenner;
	private static createPartyRelationListenner partyRelationListenner;
	private static addChatdataListenner chatdataListenner;
	private static readPartyPhotosListenner partyPhotosListenner;
	private static uploadingPhotoListenner photoListenner2;
	private static deletePhotoListenner photoListenner;
	private static addFriendListenner friendListenner5;
	private static readFriendListenner friendListenner4;
	private static mateComBookListenner bookListenner;
	private static becomeFriendListenner friendListenner3;
	private static checkFriendListenner friendListenner2;
	private static releaseFriendListenner friendListenner;
	private static addChatMsgListenner chatMsgListenner;
	private static feedBackListenner backListenner;
	private static getPicSignListenner signListenner;

	// public interface UserInterface{
	// void success(String A);
	// void defail(Object B);
	// }

	// 小组添加新的成员
	public interface TeamAddFriendsListenner {
		void success(String A);
		
		void defail(Object B);
	}
	
	// 生成财务订单
	public interface CreateOrderListenner {
		void success(String A);
		
		void defail(Object B);
	}
	
	// 添加图片评论接口
	public interface AddPicReviewListenner {
		void success(String A);
		
		void defail(Object B);
	}
	
	// 查看图片评论列表接口
	public interface CheckPicReviewListenner {
		void success(String A);
		
		void defail(Object B);
	}
	
	// 更改小组名称
	public interface ChangeGroupNameListenner {
		void success(String A);
		
		void defail(Object B);
	}
	
	// 读取小组中所有聚会包含过期
	public interface ReadGroupPartyAlllistenner {
		void success(String A);
		
		void defail(Object B);
	}
	
	// 获取聚会的图文详情
	public interface ReadGraphicListenner {
		void success(String A);

		void defail(Object B);
	}

	// 用户的取现
	public interface OrderListenner {
		void success(String A);

		void defail(Object B);
	}

	// 删除用户的取现账户
	public interface DeletePayMentAccountListenner {
		void success(String A);

		void defail(Object B);
	}

	// 添加用户的取现账户
	public interface PayMentAccountListenner {
		void success(String A);

		void defail(Object B);
	}

	// 读取用户的取现账户
	public interface ReadPayMentAccountListenner {
		void success(String A);

		void defail(Object B);
	}

	// 计算用户的余额
	public interface BalanceListenner {
		void success(String A);

		void defail(Object B);
	}

	// 查询多个用户
	public interface FindMultiUserListenner {
		void success(String A);

		void defail(Object B);
	}

	// 我的所有好友
	public interface MyAllfriendsListenner {
		void success(String A);

		void defail(Object B);
	}

	// 银联支付
	public interface UnionPayListenner {
		void success(String A);

		void defail(Object B);
	}

	// 支付宝支付
	public interface AliPayListenner {
		void success(String A);

		void defail(Object B);
	}

	// 微信支付
	public interface WeChatPayListenner {
		void success(String A);

		void defail(Object B);
	}

	// 微信登录
	public interface weixinLoginListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface regNewAccountListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface checkNicknameListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface userLoginListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface updateUserListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface readUserListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface requestVerCodeListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface findUserListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface createGroupListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface readUserGroupMsgListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface readUserGroupRelationListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface updateGroupSetListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface readAllPerRelationListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface produceRequestCodeListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface useRequestCode2JoinListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface userJoin2gourpListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface readUserAllPartyListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface readUserGroupPartyListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface updateUserJoinMsgListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface userCanclePartyListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface addPartyListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface readPartyJoinMsgListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface createPartyRelationListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface addChatdataListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface readPartyPhotosListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface uploadingPhotoListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface deletePhotoListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface addFriendListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface readFriendListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface becomeFriendListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface mateComBookListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface checkFriendListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface releaseFriendListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface addChatMsgListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface feedBackListenner {
		void success(String A);

		void defail(Object B);
	}

	public interface getPicSignListenner {
		void success(String A);

		void defail(Object B);
	}

	// //开启监听
	// public void setPostListener(UserInterface listener){
	// this.listener=listener;
	// }

	//小组添加新的成员
	public void setPostListener(TeamAddFriendsListenner listener) {
		this.teamaddfriendslistenner = listener;
	}
	
	//生成财务订单
	public void setPostListener(CreateOrderListenner listener) {
		this.createorderlistenner = listener;
	}
	
	//添加图片评论接口
	public void setPostListener(AddPicReviewListenner listener) {
		this.addPicReviewListenner = listener;
	}
	
	//查看图片评论列表接口
	public void setPostListener(CheckPicReviewListenner listener) {
		this.checkPicReviewListenner = listener;
	}
	
	// 更改小组名称
	public void setPostListener(ChangeGroupNameListenner listener) {
		this.changegroupnamelistenner = listener;
	}
	
	// 获取聚会的图文详情
	public void setPostListener(ReadGraphicListenner listener) {
		this.readgraphicListenner = listener;
	}

	// 用户的取现
	public void setPostListener(OrderListenner listener) {
		this.orderListenner = listener;
	}

	// 删除用户的取现账户
	public void setPostListener(DeletePayMentAccountListenner listener) {
		this.deletepaymentaccountListenner = listener;
	}

	// 读取用户的取现账户
	public void setPostListener(PayMentAccountListenner listener) {
		this.paymentaccountListenner = listener;
	}

	// 读取用户的取现账户
	public void setPostListener(ReadPayMentAccountListenner listener) {
		this.readpaymentaccountListenner = listener;
	}

	// 计算用户的余额
	public void setPostListener(BalanceListenner listener) {
		this.balanceListenner = listener;
	}

	// 查询多个用户
	public void setPostListener(FindMultiUserListenner listener) {
		this.multiUserListenner = listener;
	}

	// 我的所有好友
	public void setPostListener(MyAllfriendsListenner listener) {
		this.myAllfriendsListenner = listener;
	}

	// 银联支付
	public void setPostListener(UnionPayListenner listener) {
		this.unionpayListenner = listener;
	}

	// 支付宝支付
	public void setPostListener(AliPayListenner listener) {
		this.alipayListenner = listener;
	}

	// 微信支付
	public void setPostListener(WeChatPayListenner listener) {
		this.wechatpayListenner = listener;
	}

	// 微信登录
	public void setPostListener(weixinLoginListenner listener) {
		this.weixinloginListenner = listener;
	}

	// 注册新用户
	public void setPostListener(regNewAccountListenner listener) {
		this.newAccountListenner = listener;
	}

	// 检查昵称重复
	public void setPostListener(checkNicknameListenner listener) {
		this.nicknameListenner = listener;
	}

	// 用户登录
	public void setPostListener(userLoginListenner listener) {
		this.loginListenner = listener;
	}

	// 更新用户的资料
	public void setPostListener(updateUserListenner listener) {
		this.updateUserListenner = listener;
	}

	// 读取用户的资料
	public void setPostListener(readUserListenner listener) {
		this.readuserListenner = listener;
	}

	// 请求服务器发送验证码
	public void setPostListener(requestVerCodeListenner listener) {
		this.verCodeListenner = listener;
	}

	// 根据手机号或者用户ID查找用户
	public void setPostListener(findUserListenner listener) {
		this.finduserListenner = listener;
	}

	// 新建小组
	public void setPostListener(createGroupListenner listener) {
		this.groupListenner = listener;
	}

	// 读取用户小组信息
	public void setPostListener(readUserGroupMsgListenner listener) {
		this.userGroupMsgListenner = listener;
	}

	// 读取用户小组关系
	public void setPostListener(readUserGroupRelationListenner listener) {
		this.groupRelationListenner = listener;
	}

	// 更新用户小组设置
	public void setPostListener(updateGroupSetListenner listener) {
		this.groupSetListenner = listener;
	}

	// 读取所有小组成员的关系
	public void setPostListener(readAllPerRelationListenner listener) {
		this.allPerRelationListenner = listener;
	}

	// 生成邀请码
	public void setPostListener(produceRequestCodeListenner listener) {
		this.requestCodeListenner = listener;
	}

	// 使用邀请码加入小组
	public void setPostListener(useRequestCode2JoinListenner listener) {
		this.requestCode2JoinListenner = listener;
	}

	// 用户加入小组
	public void setPostListener(userJoin2gourpListenner listener) {
		this.join2gourpListenner = listener;
	}

	// 读取用户所有聚会
	public void setPostListener(readUserAllPartyListenner listener) {
		this.allPartyListenner = listener;
	}

	// 读取用户在小组中的所以聚会
	public void setPostListener(readUserGroupPartyListenner listener) {
		this.userGroupPartyListenner = listener;
	}
	// 读取用户在小组中的所有聚会包含过期
	public void setPostListener(ReadGroupPartyAlllistenner listener) {
		this.readGroupPartyAlllistenner = listener;
	}

	// 更新用户对于聚会的参与信息
	public void setPostListener(updateUserJoinMsgListenner listener) {
		this.userJoinMsgListenner = listener;
	}

	// 用户取消聚会
	public void setPostListener(userCanclePartyListenner listener) {
		this.canclePartyListenner = listener;
	}

	// 添加聚会
	public void setPostListener(addPartyListenner listener) {
		this.partyListenner = listener;
	}

	// 读取聚会用户的参与信息
	public void setPostListener(readPartyJoinMsgListenner listener) {
		this.joinMsgListenner = listener;
	}

	// 创建一条新的聚会关系
	public void setPostListener(createPartyRelationListenner listener) {
		this.partyRelationListenner = listener;
	}

	// 添加一条聊天数据
	public void setPostListener(addChatdataListenner listener) {
		this.chatdataListenner = listener;
	}

	// 读取小组相册
	public void setPostListener(readPartyPhotosListenner listener) {
		this.partyPhotosListenner = listener;
	}

	// 用户上传图片
	public void setPostListener(uploadingPhotoListenner listener) {
		this.photoListenner2 = listener;
	}

	// 用户删除图片
	public void setPostListener(deletePhotoListenner listener) {
		this.photoListenner = listener;
	}

	// 添加好友关系
	public void setPostListener(addFriendListenner listener) {
		this.friendListenner5 = listener;
	}

	// 读取好友关系
	public void setPostListener(readFriendListenner listener) {
		this.friendListenner4 = listener;
	}

	// 匹配通讯录
	public void setPostListener(mateComBookListenner listener) {
		this.bookListenner = listener;
	}

	// 成为好友关系
	public void setPostListener(becomeFriendListenner listener) {
		this.friendListenner3 = listener;
	}

	// 检查好友关系
	public void setPostListener(checkFriendListenner listener) {
		this.friendListenner2 = listener;
	}

	// 解除好友关系
	public void setPostListener(releaseFriendListenner listener) {
		this.friendListenner = listener;
	}

	// 增加一条私聊信息
	public void setPostListener(addChatMsgListenner listener) {
		this.chatMsgListenner = listener;
	}

	// 反馈
	public void setPostListener(feedBackListenner listener) {
		this.backListenner = listener;
	}

	// 获取图片签名
	public void setPostListener(getPicSignListenner listener) {
		this.signListenner = listener;
	}

	// public static void requestDone(String theObject) {
	// listener.success(theObject);
	// }
	// public static void requestError(VolleyError error) {
	// listener.defail(error);
	// }
	// 获取图片签名
	public static void requestDone2(String theObject) {
		signListenner.success(theObject);
	}

	public static void requestError2(VolleyError error) {
		signListenner.defail(error);
	}

	// 反馈
	public static void requestDone3(String theObject) {
		backListenner.success(theObject);
	}

	public static void requestError3(VolleyError error) {
		backListenner.defail(error);
	}

	// 增加一条私聊信息
	public static void requestDone4(String theObject) {
		chatMsgListenner.success(theObject);
	}

	public static void requestError4(VolleyError error) {
		chatMsgListenner.defail(error);
	}

	// 解除好友关系
	public static void requestDone5(String theObject) {
		friendListenner.success(theObject);
	}

	public static void requestError5(VolleyError error) {
		friendListenner.defail(error);
	}

	// 检查好友关系
	public static void requestDone6(String theObject) {
		friendListenner2.success(theObject);
	}

	public static void requestError6(VolleyError error) {
		friendListenner2.defail(error);
	}

	// 成为好友关系
	public static void requestDone7(String theObject) {
		friendListenner3.success(theObject);
	}

	public static void requestError7(VolleyError error) {
		friendListenner3.defail(error);
	}

	// 匹配通讯录
	public static void requestDone8(String theObject) {
		bookListenner.success(theObject);
	}

	public static void requestError8(VolleyError error) {
		bookListenner.defail(error);
	}

	// 读取好友关系
	public static void requestDone9(String theObject) {
		friendListenner4.success(theObject);
	}

	public static void requestError9(VolleyError error) {
		friendListenner4.defail(error);
	}

	// 添加好友关系
	public static void requestDone10(String theObject) {
		friendListenner5.success(theObject);
	}

	public static void requestError10(VolleyError error) {
		friendListenner5.defail(error);
	}

	// 用户删除图片
	public static void requestDone11(String theObject) {
		photoListenner.success(theObject);
	}

	public static void requestError11(VolleyError error) {
		photoListenner.defail(error);
	}

	// 用户上传图片
	public static void requestDone12(String theObject) {
		photoListenner2.success(theObject);
	}

	public static void requestError12(VolleyError error) {
		photoListenner2.defail(error);
	}

	// 读取小组相册
	public static void requestDone13(String theObject) {
		partyPhotosListenner.success(theObject);
	}

	public static void requestError13(VolleyError error) {
		partyPhotosListenner.defail(error);
	}

	// 添加一条聊天数据
	public static void requestDone14(String theObject) {
		chatdataListenner.success(theObject);
	}

	public static void requestError14(VolleyError error) {
		chatdataListenner.defail(error);
	}

	// 创建一条新的聚会关系
	public static void requestDone15(String theObject) {
		partyRelationListenner.success(theObject);
	}

	public static void requestError15(VolleyError error) {
		partyRelationListenner.defail(error);
	}

	// 读取聚会用户的参与信息
	public static void requestDone16(String theObject) {
		joinMsgListenner.success(theObject);
	}

	public static void requestError16(VolleyError error) {
		joinMsgListenner.defail(error);
	}

	// 添加聚会
	public static void requestDone17(String theObject) {
		partyListenner.success(theObject);
	}

	public static void requestError17(VolleyError error) {
		partyListenner.defail(error);
	}

	// 用户取消聚会
	public static void requestDone18(String theObject) {
		canclePartyListenner.success(theObject);
	}

	public static void requestError18(VolleyError error) {
		canclePartyListenner.defail(error);
	}

	// 更新用户对于聚会的参与信息
	public static void requestDone19(String theObject) {
		userJoinMsgListenner.success(theObject);
	}

	public static void requestError19(VolleyError error) {
		userJoinMsgListenner.defail(error);
	}

	// 读取用户在小组中的所以聚会
	public static void requestDone20(String theObject) {
		userGroupPartyListenner.success(theObject);
	}

	public static void requestError20(VolleyError error) {
		userGroupPartyListenner.defail(error);
	}

	// 读取用户所有聚会
	public static void requestDone21(String theObject) {
		allPartyListenner.success(theObject);
	}

	public static void requestError21(VolleyError error) {
		allPartyListenner.defail(error);
	}

	// 用户加入小组
	public static void requestDone22(String theObject) {
		join2gourpListenner.success(theObject);
	}

	public static void requestError22(VolleyError error) {
		join2gourpListenner.defail(error);
	}

	// 使用邀请码加入小组
	public static void requestDone23(String theObject) {
		requestCode2JoinListenner.success(theObject);
	}

	public static void requestError23(VolleyError error) {
		requestCode2JoinListenner.defail(error);
	}

	// 生成邀请码
	public static void requestDone24(String theObject) {
		requestCodeListenner.success(theObject);
	}

	public static void requestError24(VolleyError error) {
		requestCodeListenner.defail(error);
	}

	// 读取所有小组成员的关系
	public static void requestDone25(String theObject) {
		allPerRelationListenner.success(theObject);
	}

	public static void requestError25(VolleyError error) {
		allPerRelationListenner.defail(error);
	}

	// 更新用户小组设置
	public static void requestDone26(String theObject) {
		groupSetListenner.success(theObject);
	}

	public static void requestError26(VolleyError error) {
		groupSetListenner.defail(error);
	}

	// //读取用户小组信息
	// public static void requestDone27(String theObject) {
	// userGroupMsgListenner.success(theObject);
	// }
	// public static void requestError27(VolleyError error) {
	// userGroupMsgListenner.defail(error);
	// }
	// 读取用户小组关系
	public static void requestDone28(String theObject) {
		groupRelationListenner.success(theObject);
	}

	public static void requestError28(VolleyError error) {
		groupRelationListenner.defail(error);
	}

	// 读取用户小组信息
	public static void requestDone29(String theObject) {
		userGroupMsgListenner.success(theObject);
	}

	public static void requestError29(VolleyError error) {
		userGroupMsgListenner.defail(error);
	}

	// 新建小组
	public static void requestDone30(String theObject) {
		groupListenner.success(theObject);
	}

	public static void requestError30(VolleyError error) {
		groupListenner.defail(error);
	}

	// 根据手机号码或者ID查找用户
	public static void requestDone31(String theObject) {
		finduserListenner.success(theObject);
	}

	public static void requestError31(VolleyError error) {
		finduserListenner.defail(error);
	}

	// 请求服务器发送验证码
	public static void requestDone32(String theObject) {
		verCodeListenner.success(theObject);
	}

	public static void requestError32(VolleyError error) {
		verCodeListenner.defail(error);
	}

	// 读取用户资料
	public static void requestDone33(String theObject) {
		readuserListenner.success(theObject);
	}

	public static void requestError33(VolleyError error) {
		readuserListenner.defail(error);
	}

	// 更新用户资料
	public static void requestDone34(String theObject) {
		updateUserListenner.success(theObject);
	}

	public static void requestError34(VolleyError error) {
		updateUserListenner.defail(error);
	}

	// 用户登录
	public static void requestDone35(String theObject) {
		loginListenner.success(theObject);
	}

	public static void requestError35(VolleyError error) {
		loginListenner.defail(error);
	}

	// 检查昵称重复
	public static void requestDone36(String theObject) {
		nicknameListenner.success(theObject);
	}

	public static void requestError36(VolleyError error) {
		nicknameListenner.defail(error);
	}

	// 注册新用户
	public static void requestDone37(String theObject) {
		newAccountListenner.success(theObject);
	}

	public static void requestError37(VolleyError error) {
		newAccountListenner.defail(error);
	}

	// 微信登录
	public static void requestDone38(String theObject) {
		weixinloginListenner.success(theObject);
	}

	public static void requestError38(VolleyError error) {
		weixinloginListenner.defail(error);
	}

	// 微信支付
	public static void requestDone39(String theObject) {
		wechatpayListenner.success(theObject);
	}

	public static void requestError39(VolleyError error) {
		wechatpayListenner.defail(error);
	}

	// 支付宝支付
	public static void requestDone40(String theObject) {
		alipayListenner.success(theObject);
	}

	public static void requestError40(VolleyError error) {
		alipayListenner.defail(error);
	}

	// 银联支付
	public static void requestDone41(String theObject) {
		unionpayListenner.success(theObject);
	}

	public static void requestError41(VolleyError error) {
		unionpayListenner.defail(error);
	}

	// 我的所有好友
	public static void requestDone42(String theObject) {
		myAllfriendsListenner.success(theObject);
	}

	public static void requestError42(VolleyError error) {
		myAllfriendsListenner.defail(error);
	}

	// 查询多个用户
	public static void requestDone43(String theObject) {
		multiUserListenner.success(theObject);
	}

	public static void requestError43(VolleyError error) {
		multiUserListenner.defail(error);
	}
	// 包含过期的所有聚会
	public static void requestDone45(String theObject) {
		readGroupPartyAlllistenner.success(theObject);
	}
	
	public static void requestError45(VolleyError error) {
		readGroupPartyAlllistenner.defail(error);
	}

	// 计算用户的余额
	public static void requestDone44(String theObject) {
		balanceListenner.success(theObject);
	}

	public static void requestError44(VolleyError error) {
		balanceListenner.defail(error);
	}

	// 读取用户的取现账户
	public static void requestDone46(String theObject) {
		readpaymentaccountListenner.success(theObject);
	}

	public static void requestError46(VolleyError error) {
		readpaymentaccountListenner.defail(error);
	}

	// 添加用户的取现账户
	public static void requestDone47(String theObject) {
		paymentaccountListenner.success(theObject);
	}

	public static void requestError47(VolleyError error) {
		paymentaccountListenner.defail(error);
	}

	// 删除用户的取现账户
	public static void requestDone48(String theObject) {
		deletepaymentaccountListenner.success(theObject);
	}

	public static void requestError48(VolleyError error) {
		deletepaymentaccountListenner.defail(error);
	}

	// 用户的取现
	public static void requestDone49(String theObject) {
		orderListenner.success(theObject);
	}

	public static void requestError49(VolleyError error) {
		orderListenner.defail(error);
	}

	// 获取聚会的图文详情
	public static void requestDone50(String theObject) {
		readgraphicListenner.success(theObject);
	}

	public static void requestError50(VolleyError error) {
		readgraphicListenner.defail(error);
	}
	
	// 更改小组名称
	public static void requestDone51(String theObject) {
		changegroupnamelistenner.success(theObject);
	}
	
	public static void requestError51(VolleyError error) {
		changegroupnamelistenner.defail(error);
	}
	
	// 添加图片评论接口
	public static void requestDone52(String theObject) {
		addPicReviewListenner.success(theObject);
	}
	
	public static void requestError52(VolleyError error) {
		addPicReviewListenner.defail(error);
	}
	
	//查看图片评论列表接口
	public static void requestDone53(String theObject) {
		checkPicReviewListenner.success(theObject);
	}
	
	public static void requestError53(VolleyError error) {
		checkPicReviewListenner.defail(error);
	}
	
	//查看图片评论列表接口
	public static void requestDone54(String theObject) {
		createorderlistenner.success(theObject);
	}
	
	public static void requestError54(VolleyError error) {
		createorderlistenner.defail(error);
	}
	
	//小组添加新的成员
	public static void requestDone55(String theObject) {
		teamaddfriendslistenner.success(theObject);
	}
	
	public static void requestError55(VolleyError error) {
		teamaddfriendslistenner.defail(error);
	}
}
