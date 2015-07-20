package com.biju.function;

import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.BJ.javabean.Group_User;
import com.BJ.javabean.Groupuserback;
import com.biju.Interface;
import com.biju.Interface.UserInterface;
import com.biju.R;
import com.biju.login.LoginActivity;
import com.fragment.ChatFragment;
import com.fragment.PhotoFragment;
import com.fragment.ScheduleFragment;
import com.github.volley_examples.utils.GsonUtils;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class GroupActivity extends FragmentActivity implements OnClickListener {

	private FragmentTabHost mTabhost;
	public static int pk_group;

	public static int getPk_group() {
		return pk_group;
	}

	public static void setPk_group(int pk_group) {
		GroupActivity.pk_group = pk_group;
	}

	private int returndata;
	private boolean login;
	private boolean isRegistered_one;
	private Interface groupInterface;
	public static Integer pk_group_user;

	public static Integer getPk_group_user() {
		return pk_group_user;
	}

	public static void setPk_group_user(Integer pk_group_user) {
		GroupActivity.pk_group_user = pk_group_user;
	}

	private int isChat;
	private int isMessage;
	private int isPhone;
	private MyReceiver receiver;
	private boolean finish_1;
	private boolean update = false;
	private boolean photo;
	private Bundle savedInstanceState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_tab);
		savedInstanceState=savedInstanceState;
		SharedPreferences sp = getSharedPreferences("Registered", 0);
		isRegistered_one = sp.getBoolean("isRegistered_one", false);
		Log.e("HomeFragment", "isRegistered_one===" + isRegistered_one);
		returndata = sp.getInt("returndata", returndata);
		SharedPreferences sp1 = getSharedPreferences("isLogin", 0);
		login = sp1.getBoolean("Login", false);

		initUI();
		Intent intent = getIntent();
		pk_group = intent.getIntExtra("pk_group", pk_group);
		
		initInterface();
		if (!finish_1) {
			initreadUserGroupRelation();
		}
		initFinish();
		Log.e("GroupActivity", "进入了=========+onCreate");
	}

	@Override
	protected void onStart() {
		Log.e("GroupActivity", "进入了=========+onStart");
		SharedPreferences sp=getSharedPreferences("isPhoto", 0);
		photo = sp.getBoolean("Photo", false);
		super.onStart();
	}

	private void initFinish() {
		IntentFilter filter = new IntentFilter();
		filter.addAction("isFinish");
		receiver = new MyReceiver();
		registerReceiver(receiver, filter);
	}
	//注销广播
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(receiver);
		super.onDestroy();
	}
	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			finish_1 = intent.getBooleanExtra("finish", false);
			if (finish_1) {
				group_back();
				Intent intent1 = new Intent(GroupActivity.this,
						GroupActivity.class);
				startActivity(intent1);
				overridePendingTransition(0, 0);
				initreadUserGroupRelation1();
			}
		}

	}

	private void initreadUserGroupRelation1() {
		SharedPreferences Group_sp = getSharedPreferences("group", 0);
		Integer pk_group = Group_sp.getInt("pk_group", 0);
		Group_User group_User = new Group_User();
		group_User.setFk_group(pk_group);
		if (isRegistered_one) {
			group_User.setFk_user(returndata);
		} else {
			if (login) {
				int pk_user = LoginActivity.getPk_user();
				group_User.setFk_user(pk_user);
			} else {
				group_User.setFk_user(returndata);
			}
		}
		groupInterface.readUserGroupRelation(GroupActivity.this, group_User);
	}

	private void initInterface() {
		groupInterface = new Interface();
		groupInterface.setPostListener(new UserInterface() {
			@Override
			public void success(String A) {
				Groupuserback groupuserback = GsonUtils.parseJson(A,
						Groupuserback.class);
				Integer statusMsg = groupuserback.getStatusMsg();
				if (statusMsg == 1) {
					Log.e("GroupActivity", "返回小组关系ID====" + A);
					List<Group_User> groupuser_returnData = groupuserback
							.getReturnData();
					if (groupuser_returnData.size() > 0) {
						Group_User group_user = groupuser_returnData.get(0);
						pk_group_user = group_user.getPk_group_user();
						int ischat = group_user.getMessage_warn();
						int ismessage = group_user.getParty_warn();
						int isphone = group_user.getPublic_phone();
						Log.e("GroupActivity", "小组的聚会信息的提醒--------" + ismessage);
						Log.e("GroupActivity", "小组的聊天信息的提醒--------" + ischat);
						Log.e("GroupActivity", "小组的公开手机号码--------" + isphone);
						isChat = ischat;
						isMessage = ismessage;
						isPhone = isphone;
						if(photo)
						{
							mTabhost.setCurrentTab(2);
						}
						
						if (savedInstanceState != null) {  
							mTabhost.setCurrentTabByTag(savedInstanceState.getString("tab"));  
				        }  
				        Intent intent1 = getIntent();  
				        int id = intent1.getIntExtra("fragid",-1);  
				        if (id==3){  
				        	mTabhost.setCurrentTab(1);  
				        }
					}
					SharedPreferences sp = getSharedPreferences("Switch", 0);
					Editor editor = sp.edit();
					editor.putInt("ismessage", isMessage);
					editor.putInt("ischat", isChat);
					editor.putInt("isphone", isPhone);
					editor.commit();
				}
			}

			@Override
			public void defail(Object B) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void initreadUserGroupRelation() {
		Group_User group_User = new Group_User();
		group_User.setFk_group(pk_group);
		if (isRegistered_one) {
			group_User.setFk_user(returndata);
		} else {
			if (login) {
				int pk_user = LoginActivity.getPk_user();
				group_User.setFk_user(pk_user);
			} else {
				group_User.setFk_user(returndata);
			}
		}
		groupInterface.readUserGroupRelation(GroupActivity.this, group_User);

	}

	private void initUI() {
		mTabhost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabhost.setup(GroupActivity.this, getSupportFragmentManager(),
				R.id.realtabcontent);
		AddTab("1", "聊天", ChatFragment.class);
		AddTab("2", "日程", ScheduleFragment.class);
		AddTab("3", "相册", PhotoFragment.class);

		findViewById(R.id.group_back_layout).setOnClickListener(this);// 返回
		findViewById(R.id.group_back).setOnClickListener(this);
		findViewById(R.id.group_setting_layout).setOnClickListener(this);
		findViewById(R.id.group_setting).setOnClickListener(this);// 设置
	}

	@SuppressWarnings("rawtypes")
	private void AddTab(String tag, String title, Class clas) {
		TabSpec tabSpec = mTabhost.newTabSpec(tag);
		View view = getLayoutInflater().inflate(R.layout.group_tab_item, null);
		TextView tab_text = (TextView) view.findViewById(R.id.group_tab_name);
		tab_text.setText(title);
		tab_text.setTextSize(17);
		mTabhost.addTab(tabSpec.setIndicator(view), clas, null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.group_back_layout:
		case R.id.group_back:
			group_back();
			break;
		case R.id.group_setting_layout:
		case R.id.group_setting:
			group_setting();
			break;
		default:
			break;
		}
	}

	private void group_setting() {
		Intent intent = new Intent(GroupActivity.this,
				TeamSettingActivity.class);
		intent.putExtra("Group", pk_group);
		startActivity(intent);
	}

	public void group_back() {
		finish();
	}
	
	@Override
	protected void onStop() {
		SharedPreferences sp=getSharedPreferences("isPhoto", 0);
		Editor editor=sp.edit();
		editor.putBoolean("Photo", false);
		editor.commit();
		super.onStop();
	}
}
