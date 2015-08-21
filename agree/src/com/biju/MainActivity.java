package com.biju;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.Window;

import com.BJ.photo.Bimp;
import com.BJ.utils.InitHead;
import com.BJ.utils.RefreshActivity;
import com.BJ.utils.SdPkUser;
import com.fragment.FriendsFragment;
import com.fragment.HomeFragment;
import com.fragment.PartyFragment;
import com.fragment.SettingFragment;
import com.fragment.TabPagerFragment;

public class MainActivity extends FragmentActivity {
	private TabPagerFragment fragment;
	private ArrayList<Fragment> fragments;
	String[] labels = new String[] { "小组", "聚会", "好友", "我" };
	int[] tabIcons = new int[] { R.drawable.tab_home_selector,
			R.drawable.tab_party_selector, R.drawable.tab_friends_selector,
			R.drawable.tab_setting_selector };

	private String mFilePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		// 关闭之前的界面
		for (int i = 0; i < RefreshActivity.activList_3.size(); i++) {
			RefreshActivity.activList_3.get(i).finish();
		}

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		fragment = new com.fragment.TabPagerFragment();
		ft.add(R.id.container, fragment);
		fragments = new ArrayList<Fragment>();
		fragments.add(new HomeFragment());
		fragments.add(new PartyFragment());
		fragments.add(new FriendsFragment());
		fragments.add(new SettingFragment());

		fragment.setArg(labels, tabIcons, fragments);
		ft.commit();
	}

	@Override
	protected void onStart() {
		// 关闭之前的界面
		for (int i = 0; i < RefreshActivity.activList_3.size(); i++) {
			RefreshActivity.activList_3.get(i).finish();
		}
		super.onStart();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("MainActivity", "是否有进入========================");
		if (resultCode != Activity.RESULT_OK || data == null)
			return;
		try {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = MainActivity.this.getContentResolver().query(
					selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			mFilePath = cursor.getString(columnIndex);
			cursor.close();
			Bitmap bm = Bimp.revitionImageSize(mFilePath);
			InitHead.initHead(bm);
			Log.e("MainActivity", "获取的图片路径=======" + mFilePath);
			SdPkUser.setGetFilePath(mFilePath);
		} catch (Exception e) {
			Log.e("", "catch:" + e.getMessage());
		}
	}

}
