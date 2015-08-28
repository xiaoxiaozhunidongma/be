package com.biju.function;

import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
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
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.BJ.javabean.Group_User;
import com.BJ.javabean.Groupuserback;
import com.BJ.utils.RefreshActivity;
import com.BJ.utils.SdPkUser;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.readUserGroupRelationListenner;
import com.biju.R;
import com.fragment.ChatFragment;
import com.fragment.PhotoFragment;
import com.fragment.PhotoFragment2;
import com.fragment.ScheduleFragment;
import com.github.volley_examples.utils.GsonUtils;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class GroupActivity extends FragmentActivity implements OnClickListener,OnTouchListener, OnGestureListener {

	private FragmentTabHost mTabhost;
	public static int pk_group;

	public static int getPk_group() {
		return pk_group;
	}

	public static void setPk_group(int pk_group) {
		GroupActivity.pk_group = pk_group;
	}

	private Interface groupInterface;
	public static Integer pk_group_user;

	public static Integer getPk_group_user() {
		return pk_group_user;
	}

	public static void setPk_group_user(Integer pk_group_user) {
		GroupActivity.pk_group_user = pk_group_user;
	}

	private boolean photo;
	private boolean partyDetails;

	private GestureDetector mGestureDetector;
	private int verticalMinDistance = 180;
	private int minVelocity = 0;
	
	private Integer sD_pk_user;
	private int currentTab;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_tab);
		//获取sd卡中的pk_user
		sD_pk_user = SdPkUser.getsD_pk_user();
		Log.e("GroupActivity", "从SD卡中获取到的Pk_user" + sD_pk_user);

		initUI();
		Intent intent = getIntent();
		pk_group = intent.getIntExtra(IConstant.HomePk_group, pk_group);

		SharedPreferences PartyDetails_sp = getSharedPreferences(IConstant.IsPartyDetails_, 0);
		partyDetails = PartyDetails_sp.getBoolean(IConstant.PartyDetails, false);
		if (partyDetails) {
			mTabhost.setCurrentTab(1);
			currentTab = 1;
		}

		initInterface();
		initreadUserGroupRelation();
		Log.e("GroupActivity", "进入了=========+onCreate");
		initGesture();
	}

	@SuppressWarnings("deprecation")
	private void initGesture() {
		mGestureDetector = new GestureDetector((OnGestureListener) this);
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > verticalMinDistance
				&& Math.abs(velocityX) > minVelocity) {
			// 切换Activity
			if(currentTab<2)
			{
				currentTab++;
				int tab=currentTab%3;
				switch (tab) {
				case 0:
					mTabhost.setCurrentTab(0);
					break;
				case 1:
					mTabhost.setCurrentTab(1);
					break;
				case 2:
					mTabhost.setCurrentTab(2);
					break;
				default:
					break;
				}
			}else
			{
				currentTab=2;
			}
		} else if (e2.getX() - e1.getX() > verticalMinDistance
				&& Math.abs(velocityX) > minVelocity) {
			if(currentTab>0)
			{
				currentTab--;
				int tab=currentTab%3;
				switch (tab) {
				case 0:
					mTabhost.setCurrentTab(0);
					break;
				case 1:
					mTabhost.setCurrentTab(1);
					break;
				case 2:
					mTabhost.setCurrentTab(2);
					break;
				default:
					break;
				}		
				}
			}else
			{
				currentTab=0;
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
		if (photo) {
			mTabhost.setCurrentTab(2);
			currentTab = 2;
		}
		super.onStart();
	}

	private void initInterface() {
		groupInterface = Interface.getInstance();
		groupInterface.setPostListener(new readUserGroupRelationListenner() {
			@Override
			public void success(String A) {
				Groupuserback groupuserback = GsonUtils.parseJson(A,Groupuserback.class);
				Integer statusMsg = groupuserback.getStatusMsg();
				if (statusMsg == 1) {
					Log.e("GroupActivity", "返回小组关系ID====" + A);
					List<Group_User> groupuser_returnData = groupuserback.getReturnData();
					if (groupuser_returnData.size() > 0) {
						Group_User group_user = groupuser_returnData.get(0);
						pk_group_user = group_user.getPk_group_user();

					}
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
		group_User.setFk_user(sD_pk_user);
		groupInterface.readUserGroupRelation(GroupActivity.this, group_User);

	}

	private void initUI() {
		mTabhost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabhost.setup(GroupActivity.this, getSupportFragmentManager(),R.id.realtabcontent);
		AddTab("1", "聊天", ChatFragment.class);
		AddTab("2", "日程", ScheduleFragment.class);
		AddTab("3", "相册", PhotoFragment2.class);
		currentTab = mTabhost.getCurrentTab();

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
		//tabhost的监听
				mTabhost.setOnTabChangedListener(new OnTabChangeListener() {
					
					@Override
					public void onTabChanged(String tabId) {
						int TabId=Integer.valueOf(tabId);
						switch (TabId) {
						case 1:
							currentTab=0;
							break;
						case 2:
							currentTab=1;
							break;
						case 3:
							currentTab=2;
							break;
						default:
							break;
						}
					}
				});
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
		Intent intent = new Intent(GroupActivity.this,TeamSettingActivity.class);
		intent.putExtra(IConstant.Group, pk_group);
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

		SharedPreferences PartyDetails_sp = getSharedPreferences(IConstant.IsPartyDetails_, 0);
		Editor PartyDetails_editor = PartyDetails_sp.edit();
		PartyDetails_editor.putBoolean(IConstant.PartyDetails, false);
		PartyDetails_editor.commit();
		
		SharedPreferences refresh_sp=getSharedPreferences(IConstant.AddRefresh, 0);
		Editor editor2=refresh_sp.edit();
		editor2.putBoolean(IConstant.IsAddRefresh, false);
		editor2.commit();
		super.onStop();
	}

}
