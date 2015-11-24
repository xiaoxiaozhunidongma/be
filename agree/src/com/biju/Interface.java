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

	//С������µĳ�Ա
	String KTeamAddFriends="391";
	//���ɲ��񶩵�
	String KCreateOrder="416";
	//����С������
	String kChangeGroupName = "312";
	// �鿴ͼƬ�����б�ӿ�
	String KCheckPicReview = "66";
	// ���ͼƬ���۽ӿ�
	String KAddPicReview = "65";
	// ��ȡ�ۻ��ͼ������
	String KReadGraphic = "418";
	// �û�������
	String KOrder = "116";
	// ɾ���û���ȡ���˻�
	String KDeletePayMentAccount = "117";
	// ����û���ȡ���˻�
	String KPayMentAccount = "114";
	// ��ȡ�û���ȡ���˻�
	String KReadPayMentAccount = "115";
	// �����û������
	String KBalance = "113";
	// ��ѯ����û�
	String KMultiUsers = "120";
	// �ҵ����к���
	String KMyAllfriends = "54";
	// ����֧��
	String KUnionPay = "9912";
	// ֧����֧��
	String KAliPay = "9901";
	// ΢��֧��
	String KWeChatPay = "9911";
	// ΢�ŵ�¼
	String KWeixinLogin = "1102";
	// ע���û�
	String kRegAccount = "11";
	// ����ǳ��ظ�
	String kCheckNickname = "111";
	// �û���¼
	String kUserLgin = "12";
	// �����û�������
	String kUpdateUser = "15";
	// ��ȡ�û�������
	String kReadUser = "17";
	// ���������������֤��
	String kRequestVerCode = "18";
	// �����ֻ��Ż����˻�ID�����û�
	String kFindUser = "19";//19
	// �½�С��
	String kCreateGroup = "31";
	// ��ȡ�û�С����Ϣ
	String kReadGroupMsg = "32";
	// ��ȡ�û�С���ϵ
	String kReadGroupRelation = "33";
	// �����û�С������
	String kUpdateGroupSet = "34";
	// ��ȡ����С���Ա�Ĺ�ϵ
	String kReadAllPerRelation = "36";
	// ����������
	String kProduceRequestCode = "37";
	// ʹ�����������С��
	String kUseRequestCode2join = "38";
	// �û�����С��
	String kUserJoin2gourp = "39";
	// ��ȡ�û����оۻ�
	String kReadUserAllParty = "41";
	// ��ȡ�û���С���е����оۻ�
	String kReadUserGroupParty = "43";
	// ��ȡ�û���С���е����оۻ��������
	String kReadUserGroupPartyAll = "64";
	// �����û����ھۻ�Ĳ�����Ϣ
	String kUpdateUserJoinMsg = "45";
	// �û�ȡ���ۻ�
	String kUserCancleParty = "46";
	// ���Ӿۻ�
	String kAddParty = "47";
	// ��ȡ�ۻ��û��Ĳ�����Ϣ
	String kReadPartyJoinMsg = "48";
	// ����һ���µľۻ��ϵ
	String kCreatePartyRelation = "49";
	// ���һ����������
	String kAddChatData = "52";
	// ��ȡС������
	String kReadPartyPhotos = "61";
	// �û��ϴ�ͼƬ
	String kUploadingPhoto = "62";
	// �û�ɾ��ͼƬ
	String kDeletePhoto = "63";
	// ��Ӻ��ѹ�ϵ
	String kAddfriend = "71";
	// ��ȡ���ѹ�ϵ
	String kReadfriend = "72";
	// ƥ��ͨѶ¼
	String kMateComBook = "73";
	// ��Ϊ���ѹ�ϵ
	String kBecomeFriend = "74";
	// �����ѹ�ϵ
	String kCheckFriend = "75";
	// ������ѹ�ϵ
	String kReleaseFriend = "76";
	// ����һ��˽����Ϣ
	String kAddChatMsg = "81";
	// ����
	String kFeedBack = "91";
	// ��ȡͼƬǩ��
	String kGetPictureSign = "101";
	// test�½�С��
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
		Log.e("Interface", "С��json:" + jsonObject.toString());
		params.put("request_data", jsonObject.toString());

		return params;
	}
	//С������³�Ա
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
		Log.e("Interface", "С��json:" + jsonObject.toString());
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
//		ImageTextList.clear();//�����
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
		Log.e("Interface", "С��json:" + jsonObject2.toString());
		params.put("request_data", jsonObject2.toString().replace("\\", ""));
		return params;
	}

	// ͨѶ¼��packParams����
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
		Log.e("Interface", "С��json:" + jsonObject.toString());
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

	// �½�С���packParams��������
	private StringCreGroup creGroup;
	ArrayList<String> list = new ArrayList<String>();

	public Map<String, String> packParams3(Object classObject,
			String interfaceType) {
		list.clear();// �����
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
		Log.e("Interface3", "С��json:" + jsonObject.toString().replace("\\", ""));
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
				"С��json:" + jsonObject.toString().replace("\\", ""));
		params.put("request_data", jsonObject.toString().replace("\\", ""));

		return params;
	}

	// test�½�С��
	public Map<String, String> packParams2(Object classObject,
			String interfaceType) {
		Map map = Bean2Map.ConvertObjToMap(classObject);
		JSONObject jsonObject = new JSONObject(map);
		Map<String, String> params = new HashMap<String, String>();
		params.put("request_type", interfaceType);
		try {
			JSONObject object = new JSONObject(testGroup);
			Log.e("Interface2", "С��json:" + object.toString());
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
	// Log.e("ʧ��", ""+error);
	// }
	// @Override
	// public void onResponse(String response) {
	// requestDone(response);
	// }
	// });
	// }

	//С������µĳ�Ա	
	private void TeamAddFriendsPost(Context context, Map<String, String> params) {
		
		MyVolley.post(context, url, params, new VolleyListenner() {
			
			@Override
			public void onErrorResponse(VolleyError error) {
				requestError55(error);
				Log.e("ʧ��", "" + error);
			}
			
			@Override
			public void onResponse(String response) {
				requestDone55(response);
			}
		});
	}
	
	//���ɲ��񶩵�	
	private void CreateOrderPost(Context context, Map<String, String> params) {
		
		MyVolley.post(context, url, params, new VolleyListenner() {
			
			@Override
			public void onErrorResponse(VolleyError error) {
				requestError54(error);
				Log.e("ʧ��", "" + error);
			}
			
			@Override
			public void onResponse(String response) {
				requestDone54(response);
			}
		});
	}
	
	//���ͼƬ���۽ӿ�	
	private void AddPicReviewPost(Context context, Map<String, String> params) {
		
		MyVolley.post(context, url, params, new VolleyListenner() {
			
			@Override
			public void onErrorResponse(VolleyError error) {
				requestError52(error);
				Log.e("ʧ��", "" + error);
			}
			
			@Override
			public void onResponse(String response) {
				requestDone52(response);
			}
		});
	}
	
	//�鿴ͼƬ�����б�ӿ�
	private void CheckPicReviewPost(Context context, Map<String, String> params) {
		
		MyVolley.post(context, url, params, new VolleyListenner() {
			
			@Override
			public void onErrorResponse(VolleyError error) {
				requestError53(error);
				Log.e("ʧ��", "" + error);
			}
			
			@Override
			public void onResponse(String response) {
				requestDone53(response);
			}
		});
	}
	// ����С�������
	private void ChangeGroupNamePost(Context context, Map<String, String> params) {
		
		MyVolley.post(context, url, params, new VolleyListenner() {
			
			@Override
			public void onErrorResponse(VolleyError error) {
				requestError51(error);
				Log.e("ʧ��", "" + error);
			}
			
			@Override
			public void onResponse(String response) {
				requestDone51(response);
			}
		});
	}
	
	// ��ȡ�ۻ��ͼ������
	private void ReadGraphicPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError50(error);
				Log.e("ʧ��", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone50(response);
			}
		});
	}

	// �û�������
	private void OrderPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError49(error);
				Log.e("ʧ��", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone49(response);
			}
		});
	}

	// ɾ���û���ȡ���˻�
	private void DeletePayMentAccountPost(Context context,
			Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError48(error);
				Log.e("ʧ��", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone48(response);
			}
		});
	}

	// ����û���ȡ���˻�
	private void PayMentAccountPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError47(error);
				Log.e("ʧ��", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone47(response);
			}
		});
	}

	// ��ȡ�û���ȡ���˻�
	private void ReadPayMentAccountPost(Context context,
			Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError46(error);
				Log.e("ʧ��", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone46(response);
			}
		});
	}

	// �����û������
	private void BalancePost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError44(error);
				Log.e("ʧ��", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone44(response);
			}
		});
	}

	// ��ѯ����û�
	private void MultiUserPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError43(error);
				Log.e("ʧ��", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone43(response);
			}
		});
	}

	// �ҵ����к���
	private void MyAllfriendPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError42(error);
				Log.e("ʧ��", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone42(response);
			}
		});
	}

	// ����֧��
	private void UnionPayPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError41(error);
				Log.e("ʧ��", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone41(response);
			}
		});
	}

	// ֧����֧��
	private void AliPayPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError40(error);
				Log.e("ʧ��", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone40(response);
			}
		});
	}

	// ΢��֧��
	private void WeChatPayPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError39(error);
				Log.e("ʧ��", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone39(response);
			}
		});
	}

	// ΢�ŵ�¼
	private void weixinLoginPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
				requestError38(error);
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone28(response);
			}
		});
	}

	private void updateGroupSetPost(Context context, Map<String, String> params) {

		MyVolley.post(context, url, params, new VolleyListenner() {
			// 26��������
			@Override
			public void onErrorResponse(VolleyError error) {
				requestError26(error);
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone20(response);
			}
		});
	}
	
	//��������
	private void readUserGroupPartyAllPost(Context context,
			Map<String, String> params) {
		
		MyVolley.post(context, url, params, new VolleyListenner() {
			
			@Override
			public void onErrorResponse(VolleyError error) {
				requestError45(error);
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
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
				Log.e("ʧ��", "" + error);
			}

			@Override
			public void onResponse(String response) {
				requestDone2(response);
			}
		});
	}

	// ����
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

	// С������µĳ�Ա
	public void TeamAddFriends(Context context, TeamAddNewMemberModel TeamAddNewMemberModel) {
		TeamAddFriendsPost(context, packParamsNewMembers(TeamAddNewMemberModel, KTeamAddFriends));
	}
	
	// ���ɲ��񶩵�
	public void CreateOrder(Context context, CreateOrder createOrder) {
		CreateOrderPost(context, packParams(createOrder, KCreateOrder));
	}
	
	// ���ͼƬ���۽ӿ�
	public void AddPicReview(Context context, Photo_Review photo_Review) {
		AddPicReviewPost(context, packParams(photo_Review, KAddPicReview));
	}
	
	// �鿴ͼƬ�����б�ӿ�
	public void CheckPicReview(Context context, Photo Photo) {
		CheckPicReviewPost(context, packParams(Photo, KCheckPicReview));
	}
	
	// ����С������
	public void ChangeGroupName(Context context, Group group) {
		ChangeGroupNamePost(context, packParams(group, kChangeGroupName));
	}
	
	// ��ȡ�ۻ��ͼ������
	public void ReadGraphic(Context context, Party party) {
		ReadGraphicPost(context, packParams(party, KReadGraphic));
	}

	// �û���ȡ��
	public void Order(Context context, Order user_order) {
		OrderPost(context, packParams(user_order, KOrder));
	}

	// ɾ���û���ȡ���˻�
	public void DeletePayMentAccount(Context context,PaymentAccount paymentAccount) {
		DeletePayMentAccountPost(context,packParams(paymentAccount, KDeletePayMentAccount));
	}

	// ����û���ȡ���˻�
	public void PayMentAccount(Context context, PaymentAccount paymentAccount) {
		PayMentAccountPost(context, packParams(paymentAccount, KPayMentAccount));
	}

	// ��ȡ�û���ȡ���˻�
	public void ReadPayMentAccount(Context context, User user) {
		ReadPayMentAccountPost(context, packParams(user, KReadPayMentAccount));
	}

	// �����û������
	public void Balance(Context context, User user) {
		BalancePost(context, packParams(user, KBalance));
	}

	// ��ѯ����û�
	public void findMultiUsers(Context context, List<String> list) {
		MultiUserPost(context, packParamsMulti(list, KMultiUsers));
	}

	// �ҵ����к���
	public void readMyAllfriend(Context context, User user) {
		MyAllfriendPost(context, packParams(user, KMyAllfriends));
	}

	// ����֧��
	public void UnionPay(Context context, UnionPay unionpay) {
		UnionPayPost(context, packParams(unionpay, KUnionPay));
	}

	// ֧����֧��
	public void AliPay(Context context, WeChatPay wechatpay) {
		AliPayPost(context, packParams(wechatpay, KAliPay));
	}

	// ΢��֧��
	public void WeChatPay(Context context, WeChatPay wechatpay) {
		WeChatPayPost(context, packParams(wechatpay, KWeChatPay));
	}

	// ΢�ŵ�¼
	public void weixinLogin(Context context, User user) {
		weixinLoginPost(context, packParams(user, KWeixinLogin));
	}

	// ע�����û�
	public void regNewAccount(Context context, User user) {
		regNewAccountPost(context, packParams(user, kRegAccount));
	}

	// ����ǳ��ظ�
	public void checkNickname(Context context, User user) {
		checkNicknamePost(context, packParams(user, kCheckNickname));
	}

	// �û���¼
	public void userLogin(Context context, User user) {
		userLoginPost(context, packParams(user, kUserLgin));
	}

	// �����û�������
	public void updateUser(Context context, User user) {
		updateUserPost(context, packParams(user, kUpdateUser));
	}

	// ��ȡ�û�������
	public void readUser(Context context, User user) {
		readUserPost(context, packParams(user, kReadUser));
	}

	// ���������������֤��
	/**
	 * @param context
	 * @param user
	 *            this user can be null!(�����������ǿյ�)
	 */
	public void requestVerCode(Context context, Phone phone) {// ����Ϊ??
		requestVerCodePost(context, packParams(phone, kRequestVerCode));
	}

	// �����ֻ��Ż����˻�ID�����û�
	public void findUser(Context context, User user) {
		findUserPost(context, packParams(user, kFindUser));
	}

	// �½�С��
	public void createGroup(Context context, CreateGroup creatGroup) {
		createGroupPost(context, packParams3(creatGroup, kCreateGroup));
	}

	// //test�½�С��
	// public void createGroup2(Context context,StringCreGroup creGroup) {
	// volleyPost(context,packParams2(creGroup, kCreateGroup));
	// }
	// ��ȡ�û�С����Ϣ
	public void readUserGroupMsg(Context context, User user) {
		readUserGroupMsgPost(context, packParams(user, kReadGroupMsg));
	}

	// ��ȡ�û�С���ϵ
	public void readUserGroupRelation(Context context, Group_User group_User) {
		readUserGroupRelationPost(context,packParams(group_User, kReadGroupRelation));
	}

	// �����û�С������
	public void updateGroupSet(Context context, Group_User group_User) {
		updateGroupSetPost(context, packParams(group_User, kUpdateGroupSet));
	}

	// ��ȡ����С���Ա�Ĺ�ϵ
	public void readAllPerRelation(Context context, Group group) {
		readAllPerRelationPost(context, packParams(group, kReadAllPerRelation));
	}

	// ����������
	public void produceRequestCode(Context context, Group group) {
		produceRequestCodePost(context, packParams(group, kProduceRequestCode));
	}

	// ʹ�����������С��
	public void useRequestCode2Join(Context context, Group_Code code) {
		useRequestCode2JoinPost(context, packParams(code, kUseRequestCode2join));
	}

	// �û�����С��
	public void userJoin2gourp(Context context, Group_User group_User) {
		userJoin2gourpPost(context, packParams(group_User, kUserJoin2gourp));
	}

	// ��ȡ�û����оۻ�
	public void readUserAllParty(Context context, User user) {
		readUserAllPartyPost(context, packParams(user, kReadUserAllParty));
	}

	// ��ȡ�û���С���е����оۻ�
	public void readUserGroupParty(Context context, IDs idsmap) {// ...........�����ֵ�
		readUserGroupPartyPost(context, packParams(idsmap, kReadUserGroupParty));
	}
	// ��ȡ�û���С���е����оۻ��������
	public void readUserGroupPartyAll(Context context, Group group) {
		readUserGroupPartyAllPost(context, packParams(group, kReadUserGroupPartyAll));
	}

	// �����û����ھۻ�Ĳ�����Ϣ
	public void updateUserJoinMsg(Context context, Party_User party_User) {
		updateUserJoinMsgPost(context,packParams(party_User, kUpdateUserJoinMsg));
	}

	// �û�ȡ���ۻ�
	public void userCancleParty(Context context, Party party) {
		userCanclePartyPost(context, packParams(party, kUserCancleParty));
	}

	// ���Ӿۻ�
	public void addParty(Context context, MapAddParty mapAddParty) {
		addPartyPost(context, packParamsAddParty(mapAddParty, kAddParty));
	}

	// ��ȡ�ۻ��û��Ĳ�����Ϣ
	public void readPartyJoinMsg(Context context, Party party) {
		readPartyJoinMsgPost(context, packParams(party, kReadPartyJoinMsg));
	}

	// ����һ���µľۻ��ϵ
	public void createPartyRelation(Context context, Party_User party_User) {
		createPartyRelationPost(context,packParams(party_User, kCreatePartyRelation));
	}

	// ���һ����������
	public void addChatdata(Context context, Chat chat) {
		addChatdataPost(context, packParams(chat, kAddChatData));
	}

	// ��ȡС������
	public void readPartyPhotos(Context context, Group group) {
		readPartyPhotosPost(context, packParams(group, kReadPartyPhotos));
	}

	// �û��ϴ�ͼƬ
	public void uploadingPhoto(Context context, Photo photo) {
		uploadingPhotoPost(context, packParams(photo, kUploadingPhoto));
	}

	// �û�ɾ��ͼƬ
	public void deletePhoto(Context context, Photo photo) {
		deletePhotoPost(context, packParams(photo, kDeletePhoto));
	}

	// ��Ӻ��ѹ�ϵ
	public void addFriend(Context context, User_User user_User) {
		addFriendPost(context, packParams(user_User, kAddfriend));
	}

	// ��ȡ���ѹ�ϵ
	public void readFriend(Context context, User user) {
		readFriendPost(context, packParams(user, kReadfriend));
	}

	// ƥ��ͨѶ¼
	/**
	 * 
	 * @param context
	 * @param user
	 *            this user can be null!(����������Կ����ǿյ�)
	 */
	public void mateComBook(Context context, PhoneArray phonearray) { // ........���룿����
		try {
			mateComBookPost(context, packParamsPhone(phonearray, kMateComBook));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// ��Ϊ���ѹ�ϵ
	public void becomeFriend(Context context, User_User user_User) {
		becomeFriendPost(context, packParams(user_User, kBecomeFriend));
	}

	// �����ѹ�ϵ
	public void checkFriend(Context context, User_User user_User) {
		checkFriendPost(context, packParams(user_User, kCheckFriend));
	}

	// ������ѹ�ϵ
	public void releaseFriend(Context context, User_User user_User) {
		releaseFriendPost(context, packParams(user_User, kReleaseFriend));
	}

	// ����һ��˽����Ϣ
	public void addChatMsg(Context context, User_Chat user_Chat) {
		addChatMsgPost(context, packParams(user_Chat, kAddChatMsg));
	}

	// ����
	public void feedBack(Context context, FeedBack feedBack) {
		feedBackPost(context, packParams(feedBack, kFeedBack));
	}

	// ��ȡͼƬǩ��
	/**
	 * 
	 * @param context
	 * @param user
	 *            this user can be null!(����������Կ����ǿյ�)
	 */
	public void getPicSign(Context context, User user) { // .....���룿��������
		getPicSigntPost(context, packParams(user, kGetPictureSign));
	}

	// �ӿڲ���
	// private static UserInterface listener;

	// С������µĳ�Ա
	private static TeamAddFriendsListenner teamaddfriendslistenner;
	// ���ɲ��񶩵�
	private static CreateOrderListenner createorderlistenner;
	// ���ͼƬ���۽ӿ�
	private static AddPicReviewListenner addPicReviewListenner;
	// �鿴ͼƬ�����б�ӿ�
	private static CheckPicReviewListenner checkPicReviewListenner;
	// ����С������
	private static ChangeGroupNameListenner changegroupnamelistenner;
	// ��ȡС�������оۻ��������
	private static ReadGroupPartyAlllistenner readGroupPartyAlllistenner;
	// ��ѯ����û�
	// ��ȡ�ۻ��ͼ������
	private static ReadGraphicListenner readgraphicListenner;
	// �û���ȡ��
	private static OrderListenner orderListenner;
	// ɾ���û���ȡ���˻�
	private static DeletePayMentAccountListenner deletepaymentaccountListenner;
	// ����û���ȡ���˻�
	private static PayMentAccountListenner paymentaccountListenner;
	// ��ȡ�û���ȡ���˻�
	private static ReadPayMentAccountListenner readpaymentaccountListenner;
	// �����û������
	private static BalanceListenner balanceListenner;
	// �ҵ����к���
	private static FindMultiUserListenner multiUserListenner;
	// �ҵ����к���
	private static MyAllfriendsListenner myAllfriendsListenner;
	// ΢�ŵ�¼
	private static weixinLoginListenner weixinloginListenner;
	// ΢��֧��
	private static WeChatPayListenner wechatpayListenner;
	// ֧����֧��
	private static AliPayListenner alipayListenner;
	// ����֧��
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

	// С������µĳ�Ա
	public interface TeamAddFriendsListenner {
		void success(String A);
		
		void defail(Object B);
	}
	
	// ���ɲ��񶩵�
	public interface CreateOrderListenner {
		void success(String A);
		
		void defail(Object B);
	}
	
	// ���ͼƬ���۽ӿ�
	public interface AddPicReviewListenner {
		void success(String A);
		
		void defail(Object B);
	}
	
	// �鿴ͼƬ�����б�ӿ�
	public interface CheckPicReviewListenner {
		void success(String A);
		
		void defail(Object B);
	}
	
	// ����С������
	public interface ChangeGroupNameListenner {
		void success(String A);
		
		void defail(Object B);
	}
	
	// ��ȡС�������оۻ��������
	public interface ReadGroupPartyAlllistenner {
		void success(String A);
		
		void defail(Object B);
	}
	
	// ��ȡ�ۻ��ͼ������
	public interface ReadGraphicListenner {
		void success(String A);

		void defail(Object B);
	}

	// �û���ȡ��
	public interface OrderListenner {
		void success(String A);

		void defail(Object B);
	}

	// ɾ���û���ȡ���˻�
	public interface DeletePayMentAccountListenner {
		void success(String A);

		void defail(Object B);
	}

	// ����û���ȡ���˻�
	public interface PayMentAccountListenner {
		void success(String A);

		void defail(Object B);
	}

	// ��ȡ�û���ȡ���˻�
	public interface ReadPayMentAccountListenner {
		void success(String A);

		void defail(Object B);
	}

	// �����û������
	public interface BalanceListenner {
		void success(String A);

		void defail(Object B);
	}

	// ��ѯ����û�
	public interface FindMultiUserListenner {
		void success(String A);

		void defail(Object B);
	}

	// �ҵ����к���
	public interface MyAllfriendsListenner {
		void success(String A);

		void defail(Object B);
	}

	// ����֧��
	public interface UnionPayListenner {
		void success(String A);

		void defail(Object B);
	}

	// ֧����֧��
	public interface AliPayListenner {
		void success(String A);

		void defail(Object B);
	}

	// ΢��֧��
	public interface WeChatPayListenner {
		void success(String A);

		void defail(Object B);
	}

	// ΢�ŵ�¼
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

	// //��������
	// public void setPostListener(UserInterface listener){
	// this.listener=listener;
	// }

	//С������µĳ�Ա
	public void setPostListener(TeamAddFriendsListenner listener) {
		this.teamaddfriendslistenner = listener;
	}
	
	//���ɲ��񶩵�
	public void setPostListener(CreateOrderListenner listener) {
		this.createorderlistenner = listener;
	}
	
	//���ͼƬ���۽ӿ�
	public void setPostListener(AddPicReviewListenner listener) {
		this.addPicReviewListenner = listener;
	}
	
	//�鿴ͼƬ�����б�ӿ�
	public void setPostListener(CheckPicReviewListenner listener) {
		this.checkPicReviewListenner = listener;
	}
	
	// ����С������
	public void setPostListener(ChangeGroupNameListenner listener) {
		this.changegroupnamelistenner = listener;
	}
	
	// ��ȡ�ۻ��ͼ������
	public void setPostListener(ReadGraphicListenner listener) {
		this.readgraphicListenner = listener;
	}

	// �û���ȡ��
	public void setPostListener(OrderListenner listener) {
		this.orderListenner = listener;
	}

	// ɾ���û���ȡ���˻�
	public void setPostListener(DeletePayMentAccountListenner listener) {
		this.deletepaymentaccountListenner = listener;
	}

	// ��ȡ�û���ȡ���˻�
	public void setPostListener(PayMentAccountListenner listener) {
		this.paymentaccountListenner = listener;
	}

	// ��ȡ�û���ȡ���˻�
	public void setPostListener(ReadPayMentAccountListenner listener) {
		this.readpaymentaccountListenner = listener;
	}

	// �����û������
	public void setPostListener(BalanceListenner listener) {
		this.balanceListenner = listener;
	}

	// ��ѯ����û�
	public void setPostListener(FindMultiUserListenner listener) {
		this.multiUserListenner = listener;
	}

	// �ҵ����к���
	public void setPostListener(MyAllfriendsListenner listener) {
		this.myAllfriendsListenner = listener;
	}

	// ����֧��
	public void setPostListener(UnionPayListenner listener) {
		this.unionpayListenner = listener;
	}

	// ֧����֧��
	public void setPostListener(AliPayListenner listener) {
		this.alipayListenner = listener;
	}

	// ΢��֧��
	public void setPostListener(WeChatPayListenner listener) {
		this.wechatpayListenner = listener;
	}

	// ΢�ŵ�¼
	public void setPostListener(weixinLoginListenner listener) {
		this.weixinloginListenner = listener;
	}

	// ע�����û�
	public void setPostListener(regNewAccountListenner listener) {
		this.newAccountListenner = listener;
	}

	// ����ǳ��ظ�
	public void setPostListener(checkNicknameListenner listener) {
		this.nicknameListenner = listener;
	}

	// �û���¼
	public void setPostListener(userLoginListenner listener) {
		this.loginListenner = listener;
	}

	// �����û�������
	public void setPostListener(updateUserListenner listener) {
		this.updateUserListenner = listener;
	}

	// ��ȡ�û�������
	public void setPostListener(readUserListenner listener) {
		this.readuserListenner = listener;
	}

	// ���������������֤��
	public void setPostListener(requestVerCodeListenner listener) {
		this.verCodeListenner = listener;
	}

	// �����ֻ��Ż����û�ID�����û�
	public void setPostListener(findUserListenner listener) {
		this.finduserListenner = listener;
	}

	// �½�С��
	public void setPostListener(createGroupListenner listener) {
		this.groupListenner = listener;
	}

	// ��ȡ�û�С����Ϣ
	public void setPostListener(readUserGroupMsgListenner listener) {
		this.userGroupMsgListenner = listener;
	}

	// ��ȡ�û�С���ϵ
	public void setPostListener(readUserGroupRelationListenner listener) {
		this.groupRelationListenner = listener;
	}

	// �����û�С������
	public void setPostListener(updateGroupSetListenner listener) {
		this.groupSetListenner = listener;
	}

	// ��ȡ����С���Ա�Ĺ�ϵ
	public void setPostListener(readAllPerRelationListenner listener) {
		this.allPerRelationListenner = listener;
	}

	// ����������
	public void setPostListener(produceRequestCodeListenner listener) {
		this.requestCodeListenner = listener;
	}

	// ʹ�����������С��
	public void setPostListener(useRequestCode2JoinListenner listener) {
		this.requestCode2JoinListenner = listener;
	}

	// �û�����С��
	public void setPostListener(userJoin2gourpListenner listener) {
		this.join2gourpListenner = listener;
	}

	// ��ȡ�û����оۻ�
	public void setPostListener(readUserAllPartyListenner listener) {
		this.allPartyListenner = listener;
	}

	// ��ȡ�û���С���е����Ծۻ�
	public void setPostListener(readUserGroupPartyListenner listener) {
		this.userGroupPartyListenner = listener;
	}
	// ��ȡ�û���С���е����оۻ��������
	public void setPostListener(ReadGroupPartyAlllistenner listener) {
		this.readGroupPartyAlllistenner = listener;
	}

	// �����û����ھۻ�Ĳ�����Ϣ
	public void setPostListener(updateUserJoinMsgListenner listener) {
		this.userJoinMsgListenner = listener;
	}

	// �û�ȡ���ۻ�
	public void setPostListener(userCanclePartyListenner listener) {
		this.canclePartyListenner = listener;
	}

	// ��Ӿۻ�
	public void setPostListener(addPartyListenner listener) {
		this.partyListenner = listener;
	}

	// ��ȡ�ۻ��û��Ĳ�����Ϣ
	public void setPostListener(readPartyJoinMsgListenner listener) {
		this.joinMsgListenner = listener;
	}

	// ����һ���µľۻ��ϵ
	public void setPostListener(createPartyRelationListenner listener) {
		this.partyRelationListenner = listener;
	}

	// ���һ����������
	public void setPostListener(addChatdataListenner listener) {
		this.chatdataListenner = listener;
	}

	// ��ȡС�����
	public void setPostListener(readPartyPhotosListenner listener) {
		this.partyPhotosListenner = listener;
	}

	// �û��ϴ�ͼƬ
	public void setPostListener(uploadingPhotoListenner listener) {
		this.photoListenner2 = listener;
	}

	// �û�ɾ��ͼƬ
	public void setPostListener(deletePhotoListenner listener) {
		this.photoListenner = listener;
	}

	// ��Ӻ��ѹ�ϵ
	public void setPostListener(addFriendListenner listener) {
		this.friendListenner5 = listener;
	}

	// ��ȡ���ѹ�ϵ
	public void setPostListener(readFriendListenner listener) {
		this.friendListenner4 = listener;
	}

	// ƥ��ͨѶ¼
	public void setPostListener(mateComBookListenner listener) {
		this.bookListenner = listener;
	}

	// ��Ϊ���ѹ�ϵ
	public void setPostListener(becomeFriendListenner listener) {
		this.friendListenner3 = listener;
	}

	// �����ѹ�ϵ
	public void setPostListener(checkFriendListenner listener) {
		this.friendListenner2 = listener;
	}

	// ������ѹ�ϵ
	public void setPostListener(releaseFriendListenner listener) {
		this.friendListenner = listener;
	}

	// ����һ��˽����Ϣ
	public void setPostListener(addChatMsgListenner listener) {
		this.chatMsgListenner = listener;
	}

	// ����
	public void setPostListener(feedBackListenner listener) {
		this.backListenner = listener;
	}

	// ��ȡͼƬǩ��
	public void setPostListener(getPicSignListenner listener) {
		this.signListenner = listener;
	}

	// public static void requestDone(String theObject) {
	// listener.success(theObject);
	// }
	// public static void requestError(VolleyError error) {
	// listener.defail(error);
	// }
	// ��ȡͼƬǩ��
	public static void requestDone2(String theObject) {
		signListenner.success(theObject);
	}

	public static void requestError2(VolleyError error) {
		signListenner.defail(error);
	}

	// ����
	public static void requestDone3(String theObject) {
		backListenner.success(theObject);
	}

	public static void requestError3(VolleyError error) {
		backListenner.defail(error);
	}

	// ����һ��˽����Ϣ
	public static void requestDone4(String theObject) {
		chatMsgListenner.success(theObject);
	}

	public static void requestError4(VolleyError error) {
		chatMsgListenner.defail(error);
	}

	// ������ѹ�ϵ
	public static void requestDone5(String theObject) {
		friendListenner.success(theObject);
	}

	public static void requestError5(VolleyError error) {
		friendListenner.defail(error);
	}

	// �����ѹ�ϵ
	public static void requestDone6(String theObject) {
		friendListenner2.success(theObject);
	}

	public static void requestError6(VolleyError error) {
		friendListenner2.defail(error);
	}

	// ��Ϊ���ѹ�ϵ
	public static void requestDone7(String theObject) {
		friendListenner3.success(theObject);
	}

	public static void requestError7(VolleyError error) {
		friendListenner3.defail(error);
	}

	// ƥ��ͨѶ¼
	public static void requestDone8(String theObject) {
		bookListenner.success(theObject);
	}

	public static void requestError8(VolleyError error) {
		bookListenner.defail(error);
	}

	// ��ȡ���ѹ�ϵ
	public static void requestDone9(String theObject) {
		friendListenner4.success(theObject);
	}

	public static void requestError9(VolleyError error) {
		friendListenner4.defail(error);
	}

	// ��Ӻ��ѹ�ϵ
	public static void requestDone10(String theObject) {
		friendListenner5.success(theObject);
	}

	public static void requestError10(VolleyError error) {
		friendListenner5.defail(error);
	}

	// �û�ɾ��ͼƬ
	public static void requestDone11(String theObject) {
		photoListenner.success(theObject);
	}

	public static void requestError11(VolleyError error) {
		photoListenner.defail(error);
	}

	// �û��ϴ�ͼƬ
	public static void requestDone12(String theObject) {
		photoListenner2.success(theObject);
	}

	public static void requestError12(VolleyError error) {
		photoListenner2.defail(error);
	}

	// ��ȡС�����
	public static void requestDone13(String theObject) {
		partyPhotosListenner.success(theObject);
	}

	public static void requestError13(VolleyError error) {
		partyPhotosListenner.defail(error);
	}

	// ���һ����������
	public static void requestDone14(String theObject) {
		chatdataListenner.success(theObject);
	}

	public static void requestError14(VolleyError error) {
		chatdataListenner.defail(error);
	}

	// ����һ���µľۻ��ϵ
	public static void requestDone15(String theObject) {
		partyRelationListenner.success(theObject);
	}

	public static void requestError15(VolleyError error) {
		partyRelationListenner.defail(error);
	}

	// ��ȡ�ۻ��û��Ĳ�����Ϣ
	public static void requestDone16(String theObject) {
		joinMsgListenner.success(theObject);
	}

	public static void requestError16(VolleyError error) {
		joinMsgListenner.defail(error);
	}

	// ��Ӿۻ�
	public static void requestDone17(String theObject) {
		partyListenner.success(theObject);
	}

	public static void requestError17(VolleyError error) {
		partyListenner.defail(error);
	}

	// �û�ȡ���ۻ�
	public static void requestDone18(String theObject) {
		canclePartyListenner.success(theObject);
	}

	public static void requestError18(VolleyError error) {
		canclePartyListenner.defail(error);
	}

	// �����û����ھۻ�Ĳ�����Ϣ
	public static void requestDone19(String theObject) {
		userJoinMsgListenner.success(theObject);
	}

	public static void requestError19(VolleyError error) {
		userJoinMsgListenner.defail(error);
	}

	// ��ȡ�û���С���е����Ծۻ�
	public static void requestDone20(String theObject) {
		userGroupPartyListenner.success(theObject);
	}

	public static void requestError20(VolleyError error) {
		userGroupPartyListenner.defail(error);
	}

	// ��ȡ�û����оۻ�
	public static void requestDone21(String theObject) {
		allPartyListenner.success(theObject);
	}

	public static void requestError21(VolleyError error) {
		allPartyListenner.defail(error);
	}

	// �û�����С��
	public static void requestDone22(String theObject) {
		join2gourpListenner.success(theObject);
	}

	public static void requestError22(VolleyError error) {
		join2gourpListenner.defail(error);
	}

	// ʹ�����������С��
	public static void requestDone23(String theObject) {
		requestCode2JoinListenner.success(theObject);
	}

	public static void requestError23(VolleyError error) {
		requestCode2JoinListenner.defail(error);
	}

	// ����������
	public static void requestDone24(String theObject) {
		requestCodeListenner.success(theObject);
	}

	public static void requestError24(VolleyError error) {
		requestCodeListenner.defail(error);
	}

	// ��ȡ����С���Ա�Ĺ�ϵ
	public static void requestDone25(String theObject) {
		allPerRelationListenner.success(theObject);
	}

	public static void requestError25(VolleyError error) {
		allPerRelationListenner.defail(error);
	}

	// �����û�С������
	public static void requestDone26(String theObject) {
		groupSetListenner.success(theObject);
	}

	public static void requestError26(VolleyError error) {
		groupSetListenner.defail(error);
	}

	// //��ȡ�û�С����Ϣ
	// public static void requestDone27(String theObject) {
	// userGroupMsgListenner.success(theObject);
	// }
	// public static void requestError27(VolleyError error) {
	// userGroupMsgListenner.defail(error);
	// }
	// ��ȡ�û�С���ϵ
	public static void requestDone28(String theObject) {
		groupRelationListenner.success(theObject);
	}

	public static void requestError28(VolleyError error) {
		groupRelationListenner.defail(error);
	}

	// ��ȡ�û�С����Ϣ
	public static void requestDone29(String theObject) {
		userGroupMsgListenner.success(theObject);
	}

	public static void requestError29(VolleyError error) {
		userGroupMsgListenner.defail(error);
	}

	// �½�С��
	public static void requestDone30(String theObject) {
		groupListenner.success(theObject);
	}

	public static void requestError30(VolleyError error) {
		groupListenner.defail(error);
	}

	// �����ֻ��������ID�����û�
	public static void requestDone31(String theObject) {
		finduserListenner.success(theObject);
	}

	public static void requestError31(VolleyError error) {
		finduserListenner.defail(error);
	}

	// ���������������֤��
	public static void requestDone32(String theObject) {
		verCodeListenner.success(theObject);
	}

	public static void requestError32(VolleyError error) {
		verCodeListenner.defail(error);
	}

	// ��ȡ�û�����
	public static void requestDone33(String theObject) {
		readuserListenner.success(theObject);
	}

	public static void requestError33(VolleyError error) {
		readuserListenner.defail(error);
	}

	// �����û�����
	public static void requestDone34(String theObject) {
		updateUserListenner.success(theObject);
	}

	public static void requestError34(VolleyError error) {
		updateUserListenner.defail(error);
	}

	// �û���¼
	public static void requestDone35(String theObject) {
		loginListenner.success(theObject);
	}

	public static void requestError35(VolleyError error) {
		loginListenner.defail(error);
	}

	// ����ǳ��ظ�
	public static void requestDone36(String theObject) {
		nicknameListenner.success(theObject);
	}

	public static void requestError36(VolleyError error) {
		nicknameListenner.defail(error);
	}

	// ע�����û�
	public static void requestDone37(String theObject) {
		newAccountListenner.success(theObject);
	}

	public static void requestError37(VolleyError error) {
		newAccountListenner.defail(error);
	}

	// ΢�ŵ�¼
	public static void requestDone38(String theObject) {
		weixinloginListenner.success(theObject);
	}

	public static void requestError38(VolleyError error) {
		weixinloginListenner.defail(error);
	}

	// ΢��֧��
	public static void requestDone39(String theObject) {
		wechatpayListenner.success(theObject);
	}

	public static void requestError39(VolleyError error) {
		wechatpayListenner.defail(error);
	}

	// ֧����֧��
	public static void requestDone40(String theObject) {
		alipayListenner.success(theObject);
	}

	public static void requestError40(VolleyError error) {
		alipayListenner.defail(error);
	}

	// ����֧��
	public static void requestDone41(String theObject) {
		unionpayListenner.success(theObject);
	}

	public static void requestError41(VolleyError error) {
		unionpayListenner.defail(error);
	}

	// �ҵ����к���
	public static void requestDone42(String theObject) {
		myAllfriendsListenner.success(theObject);
	}

	public static void requestError42(VolleyError error) {
		myAllfriendsListenner.defail(error);
	}

	// ��ѯ����û�
	public static void requestDone43(String theObject) {
		multiUserListenner.success(theObject);
	}

	public static void requestError43(VolleyError error) {
		multiUserListenner.defail(error);
	}
	// �������ڵ����оۻ�
	public static void requestDone45(String theObject) {
		readGroupPartyAlllistenner.success(theObject);
	}
	
	public static void requestError45(VolleyError error) {
		readGroupPartyAlllistenner.defail(error);
	}

	// �����û������
	public static void requestDone44(String theObject) {
		balanceListenner.success(theObject);
	}

	public static void requestError44(VolleyError error) {
		balanceListenner.defail(error);
	}

	// ��ȡ�û���ȡ���˻�
	public static void requestDone46(String theObject) {
		readpaymentaccountListenner.success(theObject);
	}

	public static void requestError46(VolleyError error) {
		readpaymentaccountListenner.defail(error);
	}

	// ����û���ȡ���˻�
	public static void requestDone47(String theObject) {
		paymentaccountListenner.success(theObject);
	}

	public static void requestError47(VolleyError error) {
		paymentaccountListenner.defail(error);
	}

	// ɾ���û���ȡ���˻�
	public static void requestDone48(String theObject) {
		deletepaymentaccountListenner.success(theObject);
	}

	public static void requestError48(VolleyError error) {
		deletepaymentaccountListenner.defail(error);
	}

	// �û���ȡ��
	public static void requestDone49(String theObject) {
		orderListenner.success(theObject);
	}

	public static void requestError49(VolleyError error) {
		orderListenner.defail(error);
	}

	// ��ȡ�ۻ��ͼ������
	public static void requestDone50(String theObject) {
		readgraphicListenner.success(theObject);
	}

	public static void requestError50(VolleyError error) {
		readgraphicListenner.defail(error);
	}
	
	// ����С������
	public static void requestDone51(String theObject) {
		changegroupnamelistenner.success(theObject);
	}
	
	public static void requestError51(VolleyError error) {
		changegroupnamelistenner.defail(error);
	}
	
	// ���ͼƬ���۽ӿ�
	public static void requestDone52(String theObject) {
		addPicReviewListenner.success(theObject);
	}
	
	public static void requestError52(VolleyError error) {
		addPicReviewListenner.defail(error);
	}
	
	//�鿴ͼƬ�����б�ӿ�
	public static void requestDone53(String theObject) {
		checkPicReviewListenner.success(theObject);
	}
	
	public static void requestError53(VolleyError error) {
		checkPicReviewListenner.defail(error);
	}
	
	//�鿴ͼƬ�����б�ӿ�
	public static void requestDone54(String theObject) {
		createorderlistenner.success(theObject);
	}
	
	public static void requestError54(VolleyError error) {
		createorderlistenner.defail(error);
	}
	
	//С������µĳ�Ա
	public static void requestDone55(String theObject) {
		teamaddfriendslistenner.success(theObject);
	}
	
	public static void requestError55(VolleyError error) {
		teamaddfriendslistenner.defail(error);
	}
}
