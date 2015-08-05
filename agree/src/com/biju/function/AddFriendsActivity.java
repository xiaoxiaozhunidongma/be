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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.BJ.javabean.CheckFriends;
import com.BJ.javabean.CheckFriendsback;
import com.BJ.javabean.PhoneArray;
import com.BJ.javabean.User_User;
import com.BJ.utils.ContactBean;
import com.BJ.utils.DensityUtil;
import com.BJ.utils.ImageLoaderUtils;
import com.biju.Interface;
import com.biju.Interface.becomeFriendListenner;
import com.biju.Interface.mateComBookListenner;
import com.biju.R;
import com.biju.login.LoginActivity;
import com.github.volley_examples.utils.GsonUtils;

public class AddFriendsActivity extends Activity implements OnClickListener,
		SwipeRefreshLayout.OnRefreshListener {

	private ListView contactList;
	private AsyncQueryHandler asyncQueryHandler; // 异步查询数据库类对象
	private Map<Integer, ContactBean> contactIdMap = null;
	private ArrayList<String> phonelist = new ArrayList<String>();
	private ArrayList<String> phonelist2 = new ArrayList<String>();
	private ArrayList<String> namelist = new ArrayList<String>();
	private ArrayList<String> Namelist = new ArrayList<String>();
	private ArrayList<String> NickNamelist = new ArrayList<String>();
	private ArrayList<String> Avatar_pathlist = new ArrayList<String>();
	private Interface add_Interface;
	private String phonenumber;
	private boolean isRegistered_one;
	private boolean login;
	private int returndata;
	private ArrayList<CheckFriends> contact_list = new ArrayList<CheckFriends>();
	private ArrayList<CheckFriends> AddThe_list = new ArrayList<CheckFriends>();
	private ArrayList<CheckFriends> ByAdd_list = new ArrayList<CheckFriends>();
	private String[] phoneArrays;
	private MyAdapter adapter;

	private String beginStr = "http://201139.image.myqcloud.com/201139/0/";
	private String endStr = "/original";
	private String TestcompleteURL = beginStr
			+ "1ddff6cf-35ac-446b-8312-10f4083ee13d" + endStr;
	private View mContact_head;
	private ListView mContact_head_listview;
	private MyContactAdapter contactAdapter;
	private boolean isHeadview;
	private SwipeRefreshLayout mContact_swipe_refresh;
	private boolean isAddThe;
	private View mContact_head_2;
	private MyContactAdapter2 contactAdapter2;
	private ListView mContact_head_listview_2;
	private boolean isADD;
	private RelativeLayout mContact_head_listview_2_layout;
	private RelativeLayout mContact_head_layout;
	private Integer fk_user_from;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_buddy);
		SharedPreferences sp = getSharedPreferences("Registered", 0);
		isRegistered_one = sp.getBoolean("isRegistered_one", false);
		returndata = sp.getInt("returndata", returndata);
		SharedPreferences sp1 = getSharedPreferences("isLogin", 0);
		login = sp1.getBoolean("Login", false);
		// 实例化
		asyncQueryHandler = new MyAsyncQueryHandler(getContentResolver());
		init();
		initUI();
		initInterface();

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
				contact_list.clear();
				Namelist.clear();
				NickNamelist.clear();
				Avatar_pathlist.clear();
				AddThe_list.clear();
				ByAdd_list.clear();
				CheckFriendsback contactback = GsonUtils.parseJson(A,
						CheckFriendsback.class);
				Integer status = contactback.getStatusMsg();
				if (status == 1) {
					Log.e("AddFriendsActivity", "返回回来的匹配结果======" + A);
					List<CheckFriends> contactlist = contactback
							.getReturnData();
					if (contactlist.size() > 0) {
						for (int i = 0; i < contactlist.size(); i++) {
							CheckFriends contact = contactlist.get(i);
							contact_list.add(contact);
						}
					}

					for (int i = 0; i < contact_list.size(); i++) {
						CheckFriends checkFriends = contact_list.get(i);
						if ("1".equals(String.valueOf(checkFriends
								.getRelationship()))) {
							AddThe_list.add(checkFriends);
							isAddThe = true;
							isADD = true;
							Log.e("AddFriendsActivity", "得到的发出邀请的用户==="
									+ AddThe_list.size());
						} else if ("2".equals(String.valueOf(checkFriends.getRelationship()))) {
							ByAdd_list.add(checkFriends);
							isADD = true;
							Log.e("AddFriendsActivity", "得到的被邀请的用户==="
									+ ByAdd_list.size());
						} else if("3".equals(String.valueOf(checkFriends.getRelationship())))
						{
							
						}else{
							String contact_list_phone = checkFriends.getPhone();

							for (int j = 0; j < phonelist2.size(); j++) {
								String contactphone = phonelist2.get(j);
								if (contact_list_phone.equals(contactphone)) {
									Log.e("AddFriendsActivity",
											"得到的相同号码1111==="
													+ contact_list_phone);
									Log.e("AddFriendsActivity",
											"得到的相同号码2222===" + contactphone);
									String name = namelist.get(j);
									Namelist.add(name);
									String nickname = contact_list.get(i)
											.getNickname();
									NickNamelist.add(nickname);
									String avatar_path = contact_list.get(i)
											.getAvatar_path();
									Avatar_pathlist.add(avatar_path);
									isHeadview = true;
									Log.e("AddFriendsActivity", "得到用户名称==="
											+ nickname);
								}
							}
						}
					}

					if (isHeadview) {
						float height1 = (float) ((Namelist.size()) * 60.0);
						mContact_head_layout
								.setLayoutParams(new RelativeLayout.LayoutParams(
										LayoutParams.MATCH_PARENT,
										DensityUtil.dip2px(
												AddFriendsActivity.this,
												height1)));
						mContact_head_listview.setAdapter(contactAdapter);
					}

					if (isADD) {
						if (isAddThe) {
							float height = (float) ((AddThe_list.size()) * 60.0);
							mContact_head_listview_2_layout
									.setLayoutParams(new RelativeLayout.LayoutParams(
											LayoutParams.MATCH_PARENT,
											DensityUtil.dip2px(
													AddFriendsActivity.this,
													height)));
						} else {
							float height = (float) ((ByAdd_list.size()) * 60.0);
							mContact_head_listview_2_layout
									.setLayoutParams(new RelativeLayout.LayoutParams(
											LayoutParams.MATCH_PARENT,
											DensityUtil.dip2px(
													AddFriendsActivity.this,
													height)));
						}
						mContact_head_listview_2.setAdapter(contactAdapter2);
					}
				}

			}

			@Override
			public void defail(Object B) {

			}
		});

		add_Interface.setPostListener(new becomeFriendListenner() {

			@Override
			public void success(String A) {
				Log.e("AddFriendsActivity", "返回成为好友关系后的结果=====" + A);
			}

			@Override
			public void defail(Object B) {
			}
		});

	}

	private void initPhoneData() {
		for (int i = 0; i < phonelist.size(); i++) {
			phonenumber = phonelist.get(i);
			char phone_one = phonenumber.charAt(0);
			// Log.e("ContactListActivity+number", "截取后的电话号码的第一个数为========="
			// + phone_one);
			if ("1".equals(String.valueOf(phone_one))) {
				if (phonenumber.length() > 11) {
					String number1 = phonenumber.substring(0, 3);
					String number2 = phonenumber.substring(4, 8);
					String number3 = phonenumber.substring(9, 13);
					String number4 = number1 + number2 + number3;
					// Log.e("ContactListActivity+number", "截取后的电话号码========="
					// + number4);
					phonelist2.add(number4);
				} else {
					phonelist2.add(phonenumber);
				}
			} else {
				namelist.remove(i);
			}
		}

		if (phonelist2.size() > 0) {
			contactList.setAdapter(adapter);
		}
		// Log.e("ContactListActivity+number", "截取后的电话号码list========="
		// + phonelist2.toString());
		final int size = phonelist2.size();
		phoneArrays = (String[]) phonelist2.toArray(new String[size]);
		for (int i = 0; i < phoneArrays.length; i++) {
			Log.e("AddFriendsActivity", "所获取到的电话号码数组 的每一个=========="
					+ phoneArrays[i]);
		}
		// Log.e("AddFriendsActivity",
		// "所获取到的电话号码数组==========" + phoneArrays.toString());
		PhoneArray phonearray = new PhoneArray();
		phonearray.setPhones(phoneArrays);
		if (isRegistered_one) {
			phonearray.setUser_id(returndata);
			fk_user_from = returndata;
		} else {
			if (login) {
				Integer user_id = LoginActivity.getPk_user();
				phonearray.setUser_id(user_id);
				fk_user_from = user_id;
			} else {
				phonearray.setUser_id(returndata);
				fk_user_from = returndata;
			}
		}
		add_Interface.mateComBook(AddFriendsActivity.this, phonearray);
	}

	private void initUI() {
		findViewById(R.id.addbuddy_findfriends_layout).setOnClickListener(this);// 查找好友
		findViewById(R.id.addbuddy_findfriends).setOnClickListener(this);
		findViewById(R.id.addbuddy_back_layout).setOnClickListener(this);// 返回
		findViewById(R.id.addbuddy_back).setOnClickListener(this);

		contactList = (ListView) findViewById(R.id.contact_list);
		contactList.setSelector(new ColorDrawable(Color.TRANSPARENT));// 去除ListView点击后的背景颜色

		mContact_head = View.inflate(AddFriendsActivity.this,
				R.layout.contact_head, null);
		mContact_head_listview = (ListView) mContact_head
				.findViewById(R.id.contact_head_listview);
		mContact_head_layout = (RelativeLayout) mContact_head
				.findViewById(R.id.contact_head_layout);
		mContact_head_listview
				.setSelector(new ColorDrawable(Color.TRANSPARENT));// 去除ListView点击后的背景颜色

		mContact_head_2 = View.inflate(AddFriendsActivity.this,
				R.layout.contact_head_2, null);
		mContact_head_listview_2 = (ListView) mContact_head_2
				.findViewById(R.id.contact_head_listview_2);
		mContact_head_listview_2_layout = (RelativeLayout) mContact_head_2
				.findViewById(R.id.contact_head_listview_2_layout);
		mContact_head_listview_2.setSelector(new ColorDrawable(
				Color.TRANSPARENT));// 去除ListView点击后的背景颜色

		contactList.addHeaderView(mContact_head_2);
		contactList.addHeaderView(mContact_head);

		contactAdapter2 = new MyContactAdapter2();
		contactAdapter = new MyContactAdapter();
		adapter = new MyAdapter();
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
	@SuppressLint({ "UseSparseArrays", "HandlerLeak" })
	private class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			if (cursor != null && cursor.getCount() > 0) {
				contactIdMap = new HashMap<Integer, ContactBean>();
				cursor.moveToFirst(); // 游标移动到第一项
				for (int i = 0; i < cursor.getCount(); i++) {
					cursor.moveToPosition(i);
					String name = cursor.getString(1);
					namelist.add(name);
					Log.e("ContactListActivity+name", name);
					String number = cursor.getString(2);
					Log.e("ContactListActivity+number", number);
					Log.e("ContactListActivity+number", "获取到的电话号码的字符长度"
							+ number.length());
					phonelist.add(number);
				}
				initPhoneData();// 获取到的电话号码数组
				// if (phonelist2.size() > 0) {
				// contactList.setAdapter(adapter);
				// }
			}

			super.onQueryComplete(token, cookie, cursor);
		}

	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return namelist.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			View inflater = null;
			if (convertView == null) {
				LayoutInflater layoutInflater = getLayoutInflater();
				inflater = layoutInflater.inflate(R.layout.contact_list_item,
						null);
				holder = new ViewHolder();
				holder.name = (TextView) inflater.findViewById(R.id.name);
				inflater.setTag(holder);
			} else {
				inflater = convertView;
				holder = (ViewHolder) inflater.getTag();
			}
			String name = namelist.get(position);
			holder.name.setText(name);
			return inflater;
		}

	}

	private static class ViewHolder {
		TextView contact_list_item_1_name;
		ImageView contact_list_head;
		TextView contact_list_item_1_name_1;
		TextView name;
		ImageView contact_list_2_head;
		TextView contact_list_item_2_name;
		TextView contact_list_2_OK;
	}

	class MyContactAdapter2 extends BaseAdapter {

		private CheckFriends byaddcontact_user;

		@Override
		public int getCount() {
			if (isAddThe) {
				return AddThe_list.size();

			} else {
				return ByAdd_list.size();
			}
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			View inflater = null;
			if (convertView == null) {
				LayoutInflater layoutInflater = getLayoutInflater();
				inflater = layoutInflater.inflate(R.layout.contact_list_item_2,
						null);
				holder = new ViewHolder();
				holder.contact_list_item_2_name = (TextView) inflater
						.findViewById(R.id.contact_list_item_2_name);
				holder.contact_list_2_head = (ImageView) inflater
						.findViewById(R.id.contact_list_2_head);
				holder.contact_list_2_OK = (TextView) inflater
						.findViewById(R.id.contact_list_2_OK);
				inflater.setTag(holder);
			} else {
				inflater = convertView;
				holder = (ViewHolder) convertView.getTag();
			}

			if (isAddThe) {
				CheckFriends Addthecontact_user = AddThe_list.get(position);
				holder.contact_list_item_2_name.setText(Addthecontact_user
						.getNickname());
				holder.contact_list_2_OK.setText("已发送邀请");
				holder.contact_list_2_OK.setEnabled(false);
				String avatar_path = Addthecontact_user.getAvatar_path();
				String completeURL = beginStr + avatar_path + endStr;
				ImageLoaderUtils.getInstance().LoadImage(
						AddFriendsActivity.this, completeURL,
						holder.contact_list_2_head);

			} else {
				byaddcontact_user = ByAdd_list.get(position);
				holder.contact_list_item_2_name.setText(byaddcontact_user
						.getNickname());
				holder.contact_list_2_OK.setText("同意添加");
				holder.contact_list_2_OK
						.setBackgroundResource(R.drawable.ok_click_selector);
				String avatar_path = byaddcontact_user.getAvatar_path();
				String completeURL = beginStr + avatar_path + endStr;
				ImageLoaderUtils.getInstance().LoadImage(
						AddFriendsActivity.this, completeURL,
						holder.contact_list_2_head);

				holder.contact_list_2_OK
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Toast.makeText(AddFriendsActivity.this, "添加成功",
										Toast.LENGTH_SHORT).show();
								User_User user_User = new User_User();
								user_User.setFk_user_to(byaddcontact_user
										.getFk_user_to());
								user_User.setFk_user_from(fk_user_from);
								add_Interface.becomeFriend(
										AddFriendsActivity.this, user_User);
							}
						});
			}

			return inflater;
		}

	}

	class MyContactAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			Log.e("AddFriendsActivity", "所得到的通讯录匹配的有绑定的电话号码的长度"+Namelist.size());
			return Namelist.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			View inflater = null;
			if (convertView == null) {
				LayoutInflater layoutInflater = getLayoutInflater();
				inflater = layoutInflater.inflate(R.layout.contact_list_item_1,
						null);
				holder = new ViewHolder();
				holder.contact_list_item_1_name = (TextView) inflater
						.findViewById(R.id.contact_list_item_1_name);
				holder.contact_list_item_1_name_1 = (TextView) inflater
						.findViewById(R.id.contact_list_item_1_name_1);
				holder.contact_list_head = (ImageView) inflater
						.findViewById(R.id.contact_list_head);
				inflater.setTag(holder);
			} else {
				inflater = convertView;
				holder = (ViewHolder) convertView.getTag();
			}
			holder.contact_list_item_1_name_1.setText("来自通讯录的名称:"
					+ Namelist.get(position));
			holder.contact_list_item_1_name.setText(NickNamelist.get(position));
			String avatar_path = Avatar_pathlist.get(position);
			String completeURL = beginStr + avatar_path + endStr;
			ImageLoaderUtils.getInstance().LoadImage(AddFriendsActivity.this,
					completeURL, holder.contact_list_head);
			return inflater;
		}

	}

	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				adapter.notifyDataSetChanged();
				mContact_swipe_refresh.setRefreshing(false);
			}
		}, 5000);
	}
}
