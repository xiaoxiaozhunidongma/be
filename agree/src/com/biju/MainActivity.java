package com.biju;

import java.util.ArrayList;
import java.util.List;

import leanchatlib.controller.ChatManager;
import leanchatlib.utils.LogUtils;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.BJ.javabean.JPush;
import com.BJ.javabean.JPushTabNumber;
import com.BJ.utils.InitHead;
import com.BJ.utils.InitPkUser;
import com.BJ.utils.JPReceive;
import com.BJ.utils.LimitLong;
import com.BJ.utils.Path2Bitmap;
import com.BJ.utils.PicCutter;
import com.BJ.utils.RefreshActivity;
import com.BJ.utils.SdPkUser;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.fragment.FriendsFragment;
import com.fragment.HomeFragment;
import com.fragment.PartyFragment;
import com.fragment.SettingFragment;

public class MainActivity extends FragmentActivity {

	private FragmentTabHost mTabhost;
	private int tab_imagelist[] = new int[] { R.drawable.tab_home_selector,
			R.drawable.tab_party_selector, R.drawable.tab_friends_selector,
			R.drawable.tab_setting_selector };

	private String mFilePath;
	private Bitmap convertToBitmap;
	private Bitmap limitLongScaleBitmap;
	private Bitmap centerSquareScaleBitmap;
	private Integer init_pk_user;
	private List<JPush>  JpushList1=new ArrayList<JPush>();
	private List<JPushTabNumber>  JPushTabNumberList=new ArrayList<JPushTabNumber>();
	private RelativeLayout tab_prompt;
	private TextView tab_prompt_text;
	private MyReceiver receiver;
	private boolean isTabNO=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_tabs);
		// 关闭之前的界面
		for (int i = 0; i < RefreshActivity.activList_3.size(); i++) {
			RefreshActivity.activList_3.get(i).finish();
		}

		initUI();// 初始化Tabhost
		init_pk_user = InitPkUser.InitPkUser();
		chatUserlogin();
		initJPush();
		Log.e("MainActivity", "进入==onCreate" );
	}
	
	private void initJPush() {
		receiver = new MyReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction("JPush");
		registerReceiver(receiver, filter);
	}

	class MyReceiver extends BroadcastReceiver{

		private Integer oneNumber=0;
		private boolean isJpush=false;

		@Override
		public void onReceive(Context context, Intent intent) {
			isJpush = intent.getBooleanExtra("JPushTabNumber", false);
			if(isJpush){
				oneNumber = intent.getIntExtra("OneNumber", 0);
				//设置显示条数
				if(oneNumber>0){
					tab_prompt.setVisibility(View.VISIBLE);
					tab_prompt_text.setText(""+oneNumber);
					Log.e("MainActivity", "进入大于0111111==========");
				}else {
					Log.e("MainActivity", "进入小于011111111==========");
					tab_prompt.setVisibility(View.GONE);
				}
			}else {
				initDB();
			}
		}
		
	}
	
	private void chatUserlogin() {
		Log.e("MainActivity", "SD_pk_user==" + init_pk_user);
		ChatManager chatManager = ChatManager.getInstance();
		chatManager.setupManagerWithUserId(String.valueOf(init_pk_user));
		chatManager.openClient(new AVIMClientCallback() {

			@Override
			public void done(AVIMClient avimClient, AVIMException e) {
				if (e != null) {
					LogUtils.logException(e);
					Log.e("MainActivity", "lean用户登录失败");
				} else {
					Log.e("MainActivity", "lean用户登录成功");
				}
			}
		});
	}

	private void initUI() {
		mTabhost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		AddTab("1", "小组", 0, HomeFragment.class);
		AddTab("2", "聚会", 1, PartyFragment.class);
		AddTab("3", "好友", 2, FriendsFragment.class);
		AddTab("4", "我", 3, SettingFragment.class);
	}

	private void AddTab(String tag, final String title, final int i, Class cls) {
		TabSpec tabSpec = mTabhost.newTabSpec(tag);
		View view = getLayoutInflater().inflate(R.layout.tabhost_item, null);
		ImageView tab_image = (ImageView) view.findViewById(R.id.tab_image);
		TextView tab_text = (TextView) view.findViewById(R.id.tab_name);
		if(1==i){
			tab_prompt = (RelativeLayout) view.findViewById(R.id.tab_prompt);
			tab_prompt_text = (TextView) view.findViewById(R.id.tab_prompt_text);
		}
		tab_text.setText(title);
		// tab_text.setTextSize(15);
		tab_image.setImageResource(tab_imagelist[i]);
		mTabhost.addTab(tabSpec.setIndicator(view), cls, null);
		if(i==1){
			initDBTabnumber();
		}
		
		//tabhost的监听滑动的监听
		mTabhost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				Log.e("MainActivity", "当前的tabId============" + tabId);
				int TabId=Integer.valueOf(tabId);
				if(2==TabId){
					tab_prompt.setVisibility(View.GONE);
					new Delete().from(JPushTabNumber.class).execute();
				}
			}
		});
	}

	//刚进入界面时查表
	private void initDBTabnumber() {
		JPushTabNumberList.clear();
		//再查新表
		JPushTabNumberList=new Select().from(JPushTabNumber.class).execute();
		Log.e("MainActivity", "JPushTabNumberList的长度=========="+JPushTabNumberList.size());
		//设置显示条数
		if(JPushTabNumberList.size()>0){
			tab_prompt.setVisibility(View.VISIBLE);
			tab_prompt_text.setText(""+JPushTabNumberList.size());
			Log.e("MainActivity", "进入大于0==========");
		}else {
			Log.e("MainActivity", "进入小于0==========");
			tab_prompt.setVisibility(View.GONE);
		}
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		initDBTabnumber();
		Log.e("MainActivity", "进入onRestart()==========");
	}

	private void initDB() {
		JpushList1.clear();
		JPushTabNumberList.clear();
		//先删除表中的数据再重新加入
		new Delete().from(JPushTabNumber.class).execute();
		
		// 查表
		JpushList1 = new Select().from(JPush.class).execute();
		
		for (int i = 0; i < JpushList1.size(); i++) {
			JPush jPush=JpushList1.get(i);
			String pk_group=jPush.getPk_group();
			String type_tag=jPush.getType_tag();
			JPushTabNumber jPushTabNumber=new JPushTabNumber(pk_group, type_tag);
			jPushTabNumber.save();
		}
		
		//再查新表
		JPushTabNumberList=new Select().from(JPushTabNumber.class).execute();
		//设置显示条数
		if(JPushTabNumberList.size()>0){
			tab_prompt.setVisibility(View.VISIBLE);
			tab_prompt_text.setText(""+JPushTabNumberList.size());
		}else {
			tab_prompt.setVisibility(View.GONE);
		}
		Log.e("MainActivity", "在查表时得到的JpushList1====="+JpushList1.size());
		Log.e("MainActivity", "在查表时得到的JPushTabNumberList====="+JPushTabNumberList.size());
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		// 关闭之前的界面
		for (int i = 0; i < RefreshActivity.activList_3.size(); i++) {
			RefreshActivity.activList_3.get(i).finish();
		}
		Log.e("MainActivity", "进入==onStart" );
	}

	@Override
	protected void onStop() {
		super.onStop();
		SharedPreferences sp1 = getSharedPreferences("isPhoto", 0);
		Editor editor1 = sp1.edit();
		editor1.putBoolean("Photo", false);
		editor1.commit();

		SharedPreferences PartyDetails_sp = getSharedPreferences("isPartyDetails_", 0);
		Editor PartyDetails_editor = PartyDetails_sp.edit();
		PartyDetails_editor.putBoolean("PartyDetails", false);
		PartyDetails_editor.commit();

		SharedPreferences sp = getSharedPreferences("isdate", 0);
		Editor editor = sp.edit();
		editor.putBoolean("date", false);
		editor.commit();

		SharedPreferences time_sp = getSharedPreferences(IConstant.IsTime, 0);
		Editor timeeditor = time_sp.edit();
		timeeditor.putBoolean(IConstant.IsTimeChoose, false);
		timeeditor.commit();

		SharedPreferences map_sp = getSharedPreferences(IConstant.IsMap, 0);
		Editor mapeditor = map_sp.edit();
		mapeditor.putBoolean(IConstant.IsMapChoose, false);
		mapeditor.commit();
		// 有对小组进行修改过后传false
		SdPkUser.setRefreshTeam(false);
		SdPkUser.setExit(false);
		// 新建完日程后的
		SharedPreferences refresh_sp = getSharedPreferences(
				IConstant.AddRefresh, 0);
		Editor editor2 = refresh_sp.edit();
		editor2.putBoolean(IConstant.IsAddRefresh, false);
		editor2.commit();

		// 欢迎界面
		SharedPreferences Welcome_sp = getSharedPreferences("WelCome", 0);
		Editor Welcome_editor = Welcome_sp.edit();
		Welcome_editor.putBoolean("welcome", true);
		Welcome_editor.commit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("MainActivity", "是否有进入========================");
		if (resultCode != Activity.RESULT_OK || data == null)
			return;
		try {
			@SuppressWarnings("unchecked")
			ArrayList<String> mSelectedImageList = (ArrayList<String>) data.getSerializableExtra("mSelectedImageList");
			mFilePath = mSelectedImageList.get(0);
			convertToBitmap = Path2Bitmap.convertToBitmap(mFilePath);

			limitLongScaleBitmap = LimitLong.limitLongScaleBitmap(convertToBitmap, 1080);
			centerSquareScaleBitmap = PicCutter.centerSquareScaleBitmap(limitLongScaleBitmap, 600);
			InitHead.initHead(centerSquareScaleBitmap);
			Log.e("MainActivity", "获取的图片路径=======" + mFilePath);
			SdPkUser.setGetFilePath(mFilePath);
		} catch (Exception e) {
			Log.e("", "catch:" + e.getMessage());
		}
	}

	@Override
	protected void onDestroy() {
		if (convertToBitmap != null) {
			convertToBitmap.recycle();
		}
		if (limitLongScaleBitmap != null) {
			limitLongScaleBitmap.recycle();
		}
		if (centerSquareScaleBitmap != null) {
			centerSquareScaleBitmap.recycle();
		}
		System.gc(); // 提醒系统及时回收
		unregisterReceiver(receiver);//注销广播
		super.onDestroy();
	}
}
