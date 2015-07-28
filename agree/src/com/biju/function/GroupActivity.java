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
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.BJ.javabean.Group_User;
import com.BJ.javabean.Groupuserback;
import com.biju.Interface;
import com.biju.Interface.readUserGroupRelationListenner;
import com.biju.R;
import com.biju.login.LoginActivity;
import com.fragment.ChatFragment;
import com.fragment.PhotoFragment;
import com.fragment.ScheduleFragment;
import com.github.volley_examples.utils.GsonUtils;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class GroupActivity extends FragmentActivity implements OnClickListener,
		OnTouchListener, OnGestureListener {

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
	private boolean partyDetails;

	private GestureDetector mGestureDetector;
	private int verticalMinDistance = 180;
	private int minVelocity = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_tab);
		SharedPreferences sp = getSharedPreferences("Registered", 0);
		isRegistered_one = sp.getBoolean("isRegistered_one", false);
		Log.e("HomeFragment", "isRegistered_one===" + isRegistered_one);
		returndata = sp.getInt("returndata", returndata);
		SharedPreferences sp1 = getSharedPreferences("isLogin", 0);
		login = sp1.getBoolean("Login", false);

		initUI();
		Intent intent = getIntent();
		pk_group = intent.getIntExtra("pk_group", pk_group);

		SharedPreferences PartyDetails_sp = getSharedPreferences(
				"isPartyDetails_", 0);
		partyDetails = PartyDetails_sp.getBoolean("PartyDetails", false);
		if (partyDetails) {
			mTabhost.setCurrentTab(1);
			i=1;
		}
		
		initInterface();
		if (!finish_1) {
			initreadUserGroupRelation();
		}
		initFinish();
		Log.e("GroupActivity", "进入了=========+onCreate");
		initGesture();
	}

	private void initGesture() {
		mGestureDetector = new GestureDetector((OnGestureListener) this);
	}

	private int i=0;
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > verticalMinDistance
				&& Math.abs(velocityX) > minVelocity) {
			// 切换Activity
			if(i<3)
			{
				i++;
				mTabhost.setCurrentTab(i);
			}
		} else if (e2.getX() - e1.getX() > verticalMinDistance
				&& Math.abs(velocityX) > minVelocity) {
			if(i>=0)
			{
				i--;
				mTabhost.setCurrentTab(i);
			}
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {

	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		return false;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		mGestureDetector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}

	@Override
	protected void onStart() {
		Log.e("GroupActivity", "进入了=========+onStart");
		SharedPreferences sp = getSharedPreferences("isPhoto", 0);
		photo = sp.getBoolean("Photo", false);

		super.onStart();
	}

	private void initFinish() {
		IntentFilter filter = new IntentFilter();
		filter.addAction("isFinish");
		receiver = new MyReceiver();
		registerReceiver(receiver, filter);
	}

	// 注销广播
	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		super.onDestroy();
	}

	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			finish_1 = intent.getBooleanExtra("finish", false);
			boolean isExitFinish=intent.getBooleanExtra("isExitFinish", false);
			if (finish_1) {
				group_back();
				Intent intent1 = new Intent(GroupActivity.this,
						GroupActivity.class);
				startActivity(intent1);
				overridePendingTransition(0, 0);
				initreadUserGroupRelation1();
			}
			if(isExitFinish)
			{
				group_back();
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
		groupInterface = Interface.getInstance();
		groupInterface.setPostListener(new readUserGroupRelationListenner() {
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
						if (photo) {
							mTabhost.setCurrentTab(2);
							i=2;
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
		SharedPreferences sp = getSharedPreferences("isPhoto", 0);
		Editor editor = sp.edit();
		editor.putBoolean("Photo", false);
		editor.commit();

		SharedPreferences PartyDetails_sp = getSharedPreferences(
				"isPartyDetails_", 0);
		Editor PartyDetails_editor = PartyDetails_sp.edit();
		PartyDetails_editor.putBoolean("PartyDetails", false);
		PartyDetails_editor.commit();
		super.onStop();
	}

}
