package com.biju.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;

import com.BJ.javabean.CheckFriends;
import com.BJ.javabean.CheckFriendsback;
import com.BJ.javabean.PhoneArray;
import com.BJ.utils.ContactBean;
import com.BJ.utils.ContactListAdapter;
import com.BJ.utils.QuickAlphabeticBar;
import com.BJ.utils.SdPkUser;
import com.biju.Interface;
import com.biju.Interface.mateComBookListenner;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
@SuppressLint({ "InlinedApi", "UseSparseArrays", "HandlerLeak" })
public class AddFriends2Activity extends Activity implements OnClickListener,SwipeRefreshLayout.OnRefreshListener{

	private ContactListAdapter adapter;
	private ListView contactList;
	private List<ContactBean> list;
	private AsyncQueryHandler asyncQueryHandler; // 异步查询数据库类对象
	private QuickAlphabeticBar alphabeticBar; // 快速索引条
	private Map<Integer, ContactBean> contactIdMap = null;
	private SwipeRefreshLayout mContact_swipe_refresh;
	private Integer sD_pk_user;
	
	private ArrayList<String> phonelist = new ArrayList<String>();
	private ArrayList<String> phonelist2 = new ArrayList<String>();
	private ArrayList<CheckFriends> contact_list = new ArrayList<CheckFriends>();
	
	
	
	private String phonenumber;
	private String[] phoneArrays;
	private Integer fk_user_from;
	private Interface add_Interface;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_friends2);
		//获取pk_user的值
		sD_pk_user = SdPkUser.getsD_pk_user();
		
		// 通讯录实例化
		asyncQueryHandler = new MyAsyncQueryHandler(getContentResolver());
		init();
		initUI();
		initInterface();
		
		//刷新
		mContact_swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.contact_swipe_refresh);
		mContact_swipe_refresh.setOnRefreshListener(this);

		// 顶部刷新的样式
		mContact_swipe_refresh.setColorSchemeResources(
				android.R.color.holo_red_light,
				android.R.color.holo_green_light,
				android.R.color.holo_blue_bright,
				android.R.color.holo_orange_light);
	}

	private void initInterface() {
		add_Interface = Interface.getInstance();
		add_Interface.setPostListener(new mateComBookListenner() {
			
			@Override
			public void success(String A) {
				CheckFriendsback contactback = GsonUtils.parseJson(A,CheckFriendsback.class);
				Integer status = contactback.getStatusMsg();
				if (status == 1) {
					Log.e("AddFriends2Activity", "返回回来的匹配结果======" + A);
					List<CheckFriends> contactlist = contactback.getReturnData();
					if (contactlist.size() > 0) {
						for (int i = 0; i < contactlist.size(); i++) {
							CheckFriends contact = contactlist.get(i);
							contact_list.add(contact);
						}
					}
					
					if (list.size() > 0) {
						setAdapter(list);
					}
				}
			}
			
			@Override
			public void defail(Object B) {
				
			}
		});
	}

	private void initUI() {
		contactList = (ListView) findViewById(R.id.contact_list);
		alphabeticBar = (QuickAlphabeticBar) findViewById(R.id.fast_scroller);
		findViewById(R.id.addbuddy_back_layout).setOnClickListener(this);;
		findViewById(R.id.addbuddy_back).setOnClickListener(this);;
		findViewById(R.id.addbuddy_findfriends_layout).setOnClickListener(this);;
		findViewById(R.id.addbuddy_findfriends).setOnClickListener(this);
		//搜索布局
		View mHeadView=View.inflate(AddFriends2Activity.this, R.layout.contact_list_head_item, null);
		contactList.addHeaderView(mHeadView);
		mHeadView.findViewById(R.id.Contact_search_layout).setOnClickListener(this);
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
				ContactsContract.CommonDataKinds.Phone.TYPE, };
		// 按照sort_key升序查詢
		asyncQueryHandler.startQuery(0, null, uri, projection, null, null,
				"sort_key COLLATE LOCALIZED asc");

	}

	/**
	 * 
	 * @author Administrator
	 * 
	 */
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
					Log.e("AddFriends2Activity+name", name);
					String number = cursor.getString(2);
					Log.e("AddFriends2Activity+number", number);
					phonelist.add(number);
					String sortKey = cursor.getString(3);
					Log.e("AddFriends2Activity+sortKey", sortKey);
					int contactId = cursor.getInt(4);
					Log.e("AddFriends2Activity+contactId", contactId+"");
					Long photoId = cursor.getLong(5);
					Log.e("AddFriends2Activity+photoId", photoId+"");
					String lookUpKey = cursor.getString(6);
					Log.e("AddFriends2Activity+lookUpKey", lookUpKey);

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
			}

			super.onQueryComplete(token, cookie, cursor);
		}

	}
	//拼接电话号码数组
	private void initPhoneData() {
		for (int i = 0; i < phonelist.size(); i++) {
			phonenumber = phonelist.get(i);
			char phone_one = phonenumber.charAt(0);
			if ("1".equals(String.valueOf(phone_one))) {
				if (phonenumber.length() > 11) {
					String number1 = phonenumber.substring(0, 3);
					String number2 = phonenumber.substring(4, 8);
					String number3 = phonenumber.substring(9, 13);
					String number4 = number1 + number2 + number3;
					phonelist2.add(number4);
				} else {
					phonelist2.add(phonenumber);
				}
			} else {
				String name=list.get(i).getDesplayName();
				Log.e("AddFriends2Activity", "删除的名字========="+name);
				list.remove(name);
			}
		}

		if (phonelist2.size() > 0) {
			contactList.setAdapter(adapter);
		}
		final int size = phonelist2.size();
		phoneArrays = (String[]) phonelist2.toArray(new String[size]);
		for (int i = 0; i < phoneArrays.length; i++) {
			Log.e("AddFriends2Activity", "所获取到的电话号码数组 的每一个=========="+ phoneArrays[i]);
		}
		PhoneArray phonearray = new PhoneArray();
		phonearray.setPhones(phoneArrays);
		fk_user_from = sD_pk_user;
		phonearray.setUser_id(sD_pk_user);
		add_Interface.mateComBook(AddFriends2Activity.this, phonearray);
	}
	

	private void setAdapter(List<ContactBean> list) {
		adapter = new ContactListAdapter(this, list, alphabeticBar, contact_list, fk_user_from);
		contactList.setAdapter(adapter);
		alphabeticBar.init(AddFriends2Activity.this);
		alphabeticBar.setListView(contactList);
		alphabeticBar.setHight(alphabeticBar.getHeight());
		alphabeticBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				initData();
				adapter.notifyDataSetChanged();
				mContact_swipe_refresh.setRefreshing(false);
			}

		}, 3000);
	}

	public void initData() {
		PhoneArray phonearray = new PhoneArray();
		phonearray.setPhones(phoneArrays);
		phonearray.setUser_id(sD_pk_user);
		fk_user_from = sD_pk_user;
		add_Interface.mateComBook(AddFriends2Activity.this, phonearray);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addbuddy_back_layout:
		case R.id.addbuddy_back:
			addbuddy_back();
			break;
		case R.id.addbuddy_findfriends_layout:
		case R.id.addbuddy_findfriends:
			addbuddy_findfriends();
			break;
		case R.id.Contact_search_layout:
			Contact_search_layout();
			break;
		default:
			break;
		}
	}
	//搜索
	private void Contact_search_layout() {
		Intent intent=new Intent(AddFriends2Activity.this, ContactsActivity.class);
		startActivity(intent);
	}

	private void addbuddy_findfriends() {
		Intent intent = new Intent(AddFriends2Activity.this,FindFriendsActivity.class);
		startActivity(intent);
	}

	private void addbuddy_back() {
		finish();
	}
}
