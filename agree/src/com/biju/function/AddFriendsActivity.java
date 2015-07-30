package com.biju.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;

import com.BJ.javabean.PhoneArray;
import com.BJ.utils.ContactBean;
import com.BJ.utils.ContactListAdapter;
import com.BJ.utils.QuickAlphabeticBar;
import com.biju.Interface;
import com.biju.Interface.mateComBookListenner;
import com.biju.R;
import com.biju.login.LoginActivity;

public class AddFriendsActivity extends Activity implements OnClickListener {

	private ContactListAdapter adapter;
	private ListView contactList;
	private List<ContactBean> list;
	private AsyncQueryHandler asyncQueryHandler; // 异步查询数据库类对象
	private QuickAlphabeticBar alphabeticBar; // 快速索引条
	private Map<Integer, ContactBean> contactIdMap = null;
	private ArrayList<String> phonelist = new ArrayList<String>();
	private ArrayList<String> phonelist2 = new ArrayList<String>();
	private Interface add_Interface;
	private String phonenumber;
	private boolean isRegistered_one;
	private boolean login;
	private int returndata;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_buddy);
		SharedPreferences sp = getSharedPreferences(
				"Registered", 0);
		isRegistered_one = sp.getBoolean("isRegistered_one", false);
		returndata = sp.getInt("returndata", returndata);
		SharedPreferences sp1 = getSharedPreferences(
				"isLogin", 0);
		login = sp1.getBoolean("Login", false);
		initUI();
		contactList = (ListView) findViewById(R.id.contact_list);
		alphabeticBar = (QuickAlphabeticBar) findViewById(R.id.fast_scroller);

		// 实例化
		asyncQueryHandler = new MyAsyncQueryHandler(getContentResolver());
		init();
		initInterface();
	}

	private void initInterface() {
		add_Interface = Interface.getInstance();
		add_Interface.setPostListener(new mateComBookListenner() {

			@Override
			public void success(String A) {
				Log.e("AddFriendsActivity", "返回回来的匹配结果======" + A);
			}

			@Override
			public void defail(Object B) {

			}
		});

	}

	private void initPhoneData() {
		for (int i = 0; i < phonelist.size(); i++) {
			phonenumber = phonelist.get(i);
			char phone_one=phonenumber.charAt(0);
			Log.e("ContactListActivity+number", "截取后的电话号码的第一个数为========="+phone_one);
			if("1".equals(String.valueOf(phone_one)))
			{
				if(phonenumber.length()>11)
				{
					String number1=phonenumber.substring(0, 3);
					String number2=phonenumber.substring(4, 8);
					String number3=phonenumber.substring(9, 13);
					String number4=number1+number2+number3;
					Log.e("ContactListActivity+number", "截取后的电话号码========="+number4);
					phonelist2.add(number4);
				}else
				{
					phonelist2.add(phonenumber);
				}
			}
		}
		Log.e("ContactListActivity+number", "截取后的电话号码list========="+phonelist2.toString());
		final int size = phonelist2.size();
		String[] PhoneArray = (String[]) phonelist2.toArray(new String[size]);
		for (int i = 0; i < PhoneArray.length; i++) {
			Log.e("AddFriendsActivity", "所获取到的电话号码数组 的每一个==========" + PhoneArray[i]);
		}
		Log.e("AddFriendsActivity", "所获取到的电话号码数组==========" + PhoneArray.toString());
		PhoneArray phonearray=new PhoneArray();
		phonearray.setPhones(PhoneArray);
		if(isRegistered_one)
		{
			phonearray.setUser_id(returndata);
			
		}else
		{
			if(login)
			{
				Integer user_id=LoginActivity.getPk_user();
				phonearray.setUser_id(user_id);
			}else
			{
				phonearray.setUser_id(returndata);
			}
		}
		add_Interface.mateComBook(AddFriendsActivity.this, phonearray);
	}

	private void initUI() {
		findViewById(R.id.addbuddy_findfriends_layout).setOnClickListener(this);// 查找好友
		findViewById(R.id.addbuddy_findfriends).setOnClickListener(this);
		findViewById(R.id.addbuddy_back_layout).setOnClickListener(this);// 返回
		findViewById(R.id.addbuddy_back).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_buddy, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addbuddy_findfriends_layout:
		case R.id.addbuddy_findfriends:
			addbuddy_findfriends();
			break;
		case R.id.addbuddy_back_layout:
		case R.id.addbuddy_back:
			addbuddy_back();
			break;
		default:
			break;
		}
	}

	private void addbuddy_back() {
		finish();
	}

	private void addbuddy_findfriends() {
		Intent intent = new Intent(AddFriendsActivity.this,
				FindFriendsActivity.class);
		startActivity(intent);
	}

	/**
	 * 初始化数据库查询参数
	 */
	private void init() {
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人Uri；
		// 查询的字段
		String[] projection = { ContactsContract.CommonDataKinds.Phone._ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
				ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
				ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY, 
				ContactsContract.CommonDataKinds.Phone.TYPE,
		};
		// 按照sort_key升序查詢
		asyncQueryHandler.startQuery(0, null, uri, projection, null, null,
				"sort_key COLLATE LOCALIZED asc");

	}

	/**
	 * 
	 * @author Administrator
	 * 
	 */
	@SuppressLint({ "UseSparseArrays", "HandlerLeak" })
	private class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			if (cursor != null && cursor.getCount() > 0) {
				contactIdMap = new HashMap<Integer, ContactBean>();
				list = new ArrayList<ContactBean>();
				cursor.moveToFirst(); // 游标移动到第一项
				for (int i = 0; i < cursor.getCount(); i++) {
					cursor.moveToPosition(i);
					String name = cursor.getString(1);
					Log.e("ContactListActivity+name", name);
					String number = cursor.getString(2);
					Log.e("ContactListActivity+number", number);
					Log.e("ContactListActivity+number", "获取到的电话号码的字符长度"+number.length());
					phonelist.add(number);
					String sortKey = cursor.getString(3);
					Log.e("ContactListActivity+sortKey", sortKey);
					int contactId = cursor.getInt(4);
					Log.e("ContactListActivity+contactId", contactId + "");
					Long photoId = cursor.getLong(5);
					Log.e("ContactListActivity+photoId", photoId + "");
					String lookUpKey = cursor.getString(6);
					Log.e("ContactListActivity+lookUpKey", lookUpKey);

					if (contactIdMap.containsKey(contactId)) {
						// 无操作
					} else {
						// 创建联系人对象
						ContactBean contact = new ContactBean();
						contact.setDesplayName(name);
						contact.setPhoneNum(number);
						contact.setSortKey(sortKey);
						contact.setPhotoId(photoId);
						contact.setLookUpKey(lookUpKey);
						list.add(contact);

						contactIdMap.put(contactId, contact);
					}
				}
				initPhoneData();// 获取到的电话号码数组
				if (list.size() > 0) {
					setAdapter(list);
				}
			}

			super.onQueryComplete(token, cookie, cursor);
		}

	}

	private void setAdapter(List<ContactBean> list) {
		adapter = new ContactListAdapter(this, list, alphabeticBar);
		contactList.setAdapter(adapter);
		alphabeticBar.init(AddFriendsActivity.this);
		alphabeticBar.setListView(contactList);
		alphabeticBar.setHight(alphabeticBar.getHeight());
		alphabeticBar.setVisibility(View.VISIBLE);
	}

}
