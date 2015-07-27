package com.biju;

import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.Window;

import com.fragment.FriendsFragment;
import com.fragment.HomeFragment;
import com.fragment.PartyFragment;
import com.fragment.SettingFragment;
import com.fragment.TabPagerFragment;

public class MainActivity extends FragmentActivity {
	private FragmentTabHost mTabhost;
	private TabPagerFragment fragment;
	private ArrayList<Fragment> fragments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		fragment = new com.fragment.TabPagerFragment();
		ft.add(R.id.container, fragment);
		fragments = new ArrayList<Fragment>();
		fragments.add(new HomeFragment());
		fragments.add(new PartyFragment());
		fragments.add(new FriendsFragment());
		fragments.add(new SettingFragment());
		String[] labels = new String[] { "小组", "聚会", "好友", "我" };
		int[] tabIcons = new int[] { R.drawable.tab_home_selector,
				R.drawable.tab_party_selector, R.drawable.tab_friends_selector,
				R.drawable.tab_setting_selector };
		Log.e("MainActivity", "有没有setlabels====有");
		fragment.setArg(labels, tabIcons, fragments);
		ft.commit();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		// Intent intent = getIntent();
		int id = intent.getIntExtra("HomeFragment", -1);
		if (id == 1) {
			fragment.setItem(0);
		}

		if (id == 2) {
			fragment.setItem(1);
		}
		if (id == 3) {
			fragment.setItem(2);
		}
		if (id == 4) {
			fragment.setItem(3);
		}

	}


	// private void initUI() {
	// mTabhost = (FragmentTabHost) findViewById(android.R.id.tabhost);
	// mTabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
	// AddTab("1", "小组", 0, HomeFragment.class);
	// AddTab("2", "聚会", 1, PartyFragment.class);
	// AddTab("3", "好友", 2, FriendsFragment.class);
	// AddTab("4", "我", 3, SettingFragment.class);
	// }

	// private void AddTab(String tag, String title, int i, Class cls) {
	// TabSpec tabSpec = mTabhost.newTabSpec(tag);
	// View view = getLayoutInflater().inflate(R.layout.tabhost_item, null);
	// ImageView tab_image = (ImageView) view.findViewById(R.id.tab_image);
	// TextView tab_text = (TextView) view.findViewById(R.id.tab_name);
	// tab_text.setText(title);
	// tab_text.setTextSize(10);
	// tab_image.setImageResource(tab_imagelist[i]);
	// mTabhost.addTab(tabSpec.setIndicator(view), cls, null);
	// }

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

		SharedPreferences sp1 = getSharedPreferences("isPhoto", 0);
		Editor editor1 = sp1.edit();
		editor1.putBoolean("Photo", false);
		editor1.commit();

		SharedPreferences PartyDetails_sp = getSharedPreferences(
				"isPartyDetails_", 0);
		Editor PartyDetails_editor = PartyDetails_sp.edit();
		PartyDetails_editor.putBoolean("PartyDetails", false);
		PartyDetails_editor.commit();
		
		SharedPreferences sp2 = getSharedPreferences("isLogin", 0);
		Editor editor2 = sp2.edit();
		editor2.putBoolean("Login", true);
		editor2.commit();
		super.onStop();
	}

}
