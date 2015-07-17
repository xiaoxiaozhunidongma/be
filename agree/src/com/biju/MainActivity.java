package com.biju;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.fragment.FriendsFragment;
import com.fragment.HomeFragment;
import com.fragment.PartyFragment;
import com.fragment.SettingFragment;

public class MainActivity extends FragmentActivity {
	private FragmentTabHost mTabhost;
	private int tab_imagelist[] = new int[] { R.drawable.tab_home_selector,
			R.drawable.tab_party_selector, R.drawable.tab_friends_selector,
			R.drawable.tab_setting_selector };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_tabs);
		initUI();// 初始化Tabhost
	}

	private void initUI() {
		mTabhost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		AddTab("1", "小组", 0, HomeFragment.class);
		AddTab("2", "聚会", 1, PartyFragment.class);
		AddTab("3", "好友", 2, FriendsFragment.class);
		AddTab("4", "我", 3, SettingFragment.class);
	}

	private void AddTab(String tag, String title, int i, Class cls) {
		TabSpec tabSpec = mTabhost.newTabSpec(tag);
		View view = getLayoutInflater().inflate(R.layout.tabhost_item, null);
		ImageView tab_image = (ImageView) view.findViewById(R.id.tab_image);
		TextView tab_text = (TextView) view.findViewById(R.id.tab_name);
		tab_text.setText(title);
		tab_text.setTextSize(10);
		tab_image.setImageResource(tab_imagelist[i]);
		mTabhost.addTab(tabSpec.setIndicator(view), cls, null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onStop() {
		SharedPreferences sp = getSharedPreferences("Registered", 0);
		Editor editor = sp.edit();
		editor.putBoolean("isRegistered_one", false);
		editor.commit();

		SharedPreferences login_sp = getSharedPreferences("Logout", 0);
		Editor login_editor = login_sp.edit();
		login_editor.putBoolean("isLogout", false);
		login_editor.commit();
		
		SharedPreferences sp1=getSharedPreferences("isPhoto", 0);
		Editor editor1=sp.edit();
		editor1.putBoolean("Photo", false);
		editor1.commit();
		super.onStop();
	}

}
