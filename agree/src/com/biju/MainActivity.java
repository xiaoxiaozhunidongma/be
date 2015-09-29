package com.biju;

import java.io.File;
import java.util.UUID;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.BJ.utils.ByteOrBitmap;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.ImageLoaderUtils4Photos;
import com.BJ.utils.InitHead;
import com.BJ.utils.LimitLong;
import com.BJ.utils.MyBimp;
import com.BJ.utils.Path2Bitmap;
import com.BJ.utils.PicCutter;
import com.BJ.utils.RefreshActivity;
import com.BJ.utils.SdPkUser;
import com.BJ.utils.homeImageLoaderUtils;
import com.fragment.FriendsFragment;
import com.fragment.HomeFragment;
import com.fragment.PartyFragment;
import com.fragment.SettingFragment;

public class MainActivity extends FragmentActivity  {
	
	//滑动时需要继承的implements OnTouchListener,OnGestureListener
	private FragmentTabHost mTabhost;
	private int tab_imagelist[] = new int[] { R.drawable.tab_home_selector,
			R.drawable.tab_party_selector, R.drawable.tab_friends_selector,
			R.drawable.tab_setting_selector };

	private String mFilePath;

//	@SuppressWarnings("unused")//滑动
//	private GestureDetector mGestureDetector;
//	private int verticalMinDistance = 180;
//	private int minVelocity = 0;
//	private int currentTab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_tabs);

		homeImageLoaderUtils.clearCache();
		ImageLoaderUtils4Photos.clearCache();
		ImageLoaderUtils.clearCache();
		
		// 关闭之前的界面
		for (int i = 0; i < RefreshActivity.activList_3.size(); i++) {
			RefreshActivity.activList_3.get(i).finish();
		}

		initUI();// 初始化Tabhost
//		initGesture();//滑动
	}

	private void initUI() {
		mTabhost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		AddTab("1", "小组", 0, HomeFragment.class);
		AddTab("2", "聚会", 1, PartyFragment.class);
		AddTab("3", "好友", 2, FriendsFragment.class);
		AddTab("4", "我", 3, SettingFragment.class);
//		currentTab = mTabhost.getCurrentTab();//滑动
//		Log.e("MainActivity", "当前的currentTab============" + currentTab);
	}

	private void AddTab(String tag, final String title, final int i, Class cls) {
		TabSpec tabSpec = mTabhost.newTabSpec(tag);
		View view = getLayoutInflater().inflate(R.layout.tabhost_item, null);
		ImageView tab_image = (ImageView) view.findViewById(R.id.tab_image);
		TextView tab_text = (TextView) view.findViewById(R.id.tab_name);
		tab_text.setText(title);
//		tab_text.setTextSize(15);
		tab_image.setImageResource(tab_imagelist[i]);
		mTabhost.addTab(tabSpec.setIndicator(view), cls, null);
		//tabhost的监听滑动的监听
//		mTabhost.setOnTabChangedListener(new OnTabChangeListener() {
//			
//			@Override
//			public void onTabChanged(String tabId) {
//				Log.e("MainActivity", "当前的tabId============" + tabId);
//				int TabId=Integer.valueOf(tabId);
//				switch (TabId) {
//				case 1:
//					currentTab=0;
//					break;
//				case 2:
//					currentTab=1;
//					break;
//				case 3:
//					currentTab=2;
//					break;
//				case 4:
//					currentTab=3;
//					break;
//
//				default:
//					break;
//				}
//			}
//		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		// 关闭之前的界面
		for (int i = 0; i < RefreshActivity.activList_3.size(); i++) {
			RefreshActivity.activList_3.get(i).finish();
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
		
		SharedPreferences sp = getSharedPreferences("isdate", 0);
		Editor editor = sp.edit();
		editor.putBoolean("date", false);
		editor.commit();
		
		SharedPreferences time_sp = getSharedPreferences(IConstant.IsTime, 0);
		Editor timeeditor = time_sp.edit();
		timeeditor.putBoolean(IConstant.IsTimeChoose, false);
		timeeditor.commit();

		SharedPreferences map_sp=getSharedPreferences(IConstant.IsMap, 0);
		Editor mapeditor=map_sp.edit();
		mapeditor.putBoolean(IConstant.IsMapChoose, false);
		mapeditor.commit();
		//有对小组进行修改过后传false
		SdPkUser.setRefreshTeam(false);
		SdPkUser.setExit(false);
		//新建完日程后的
		SharedPreferences refresh_sp=getSharedPreferences(IConstant.AddRefresh, 0);
		Editor editor2=refresh_sp.edit();
		editor2.putBoolean(IConstant.IsAddRefresh,false);
		editor2.commit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("MainActivity", "是否有进入========================");
		if (resultCode != Activity.RESULT_OK || data == null)
			return;
		try {
			Uri selectedImage = data.getData();
//			long parseId = ContentUris.parseId(selectedImage);
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = MainActivity.this.getContentResolver().query(
					selectedImage, filePathColumn, null, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				mFilePath = cursor.getString(columnIndex);
				cursor.close();
				cursor = null;

			} else {
				
				File file = new File(selectedImage.getPath());
				mFilePath = file.getAbsolutePath();
				if (!file.exists()) {
					Toast toast = Toast.makeText(this, "找不到图片",Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					return;
				}
			}
//			//缩略图路径
//			        Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(MainActivity.this.getContentResolver(), parseId, Images.Thumbnails.MICRO_KIND,  
//					                null); 
			        
			Log.e("MainActivity", "mFilePath======" + mFilePath);
			// Bitmap bmp = Utils.decodeSampledBitmap(mFilePath, 2);
			// Bitmap bmp = Bimp.revitionImageSize(mFilePath);
			// 这个mFilePath不可以用缩略图路径
			
			Bitmap convertToBitmap = Path2Bitmap.convertToBitmap(mFilePath);
//			Log.e("bitmap"+bitmap.getWidth()+"和"+bitmap.getHeight(), "convertToBitmap"+convertToBitmap.getWidth()+"和"+convertToBitmap.getHeight());
			
			Bitmap limitLongScaleBitmap = LimitLong.limitLongScaleBitmap(
					convertToBitmap, 1080);// 最长边限制为1080
			Bitmap centerSquareScaleBitmap = PicCutter.centerSquareScaleBitmap(
					limitLongScaleBitmap, 600);// 截取中间正方形
			
//			Bitmap bmp = MyBimp.revitionImageSize(mFilePath);
			InitHead.initHead(centerSquareScaleBitmap);
			Log.e("MainActivity", "获取的图片路径=======" + mFilePath);
			SdPkUser.setGetFilePath(mFilePath);
		} catch (Exception e) {
			Log.e("", "catch:" + e.getMessage());
		}
	}

	// 滑动过程
//	@SuppressWarnings("deprecation")
//	private void initGesture() {
//		mGestureDetector = new GestureDetector((OnGestureListener) this);
//	}
//
//	@Override
//	public boolean onDown(MotionEvent e) {
//		return false;
//	}
//
//	@Override
//	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//			float velocityY) {
//		if (e1.getX() - e2.getX() > verticalMinDistance&& Math.abs(velocityX) > minVelocity) {
//			// 切换Activity
//			if(currentTab<3)
//			{
//				currentTab++;
//				int tab = currentTab % 4;
//				Log.e("MainActivity", "当前的currentTab++============" + currentTab);
//				Log.e("MainActivity", "当前的tab++=============" + tab);
//				switch (tab) {
//				case 0:
//					mTabhost.setCurrentTab(0);
//					overridePendingTransition(R.anim.tab_left_in_item, R.anim.tab_left_out_item);
//					break;
//				case 1:
//					mTabhost.setCurrentTab(1);
//					overridePendingTransition(R.anim.tab_left_in_item, R.anim.tab_left_out_item);
//					break;
//				case 2:
//					mTabhost.setCurrentTab(2);
//					overridePendingTransition(R.anim.tab_left_in_item, R.anim.tab_left_out_item);
//					break;
//				case 3:
//					mTabhost.setCurrentTab(3);
//					overridePendingTransition(R.anim.tab_left_in_item, R.anim.tab_left_out_item);
//					break;
//					
//				default:
//					break;
//				}
//			}else
//			{
//				currentTab=3;
//			}
//		} else if (e2.getX() - e1.getX() > verticalMinDistance&& Math.abs(velocityX) > minVelocity) {
//			if(currentTab>0)
//			{
//				currentTab--;
//				int tab = currentTab % 4;
//				Log.e("MainActivity", "当前的currentTab--============" + currentTab);
//				Log.e("MainActivity", "当前的tab--=============" + tab);
//				switch (tab) {
//				case 0:
//					mTabhost.setCurrentTab(0);
//					break;
//				case 1:
//					mTabhost.setCurrentTab(1);
//					break;
//				case 2:
//					mTabhost.setCurrentTab(2);
//					break;
//				case 3:
//					mTabhost.setCurrentTab(3);
//					break;
//					
//				default:
//					break;
//				}
//			}else
//			{
//				currentTab=0;
//			}
//		}
//		return false;
//	}
//
//	@Override
//	public void onLongPress(MotionEvent e) {
//	}
//
//	@Override
//	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
//			float distanceY) {
//		return false;
//	}
//
//	@Override
//	public void onShowPress(MotionEvent e) {
//	}
//
//	@Override
//	public boolean onSingleTapUp(MotionEvent e) {
//		return false;
//	}
//
//	@Override
//	public boolean onTouch(View v, MotionEvent event) {
//		return mGestureDetector.onTouchEvent(event);
//	}
//
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//		mGestureDetector.onTouchEvent(ev);
//		return super.dispatchTouchEvent(ev);
//	}

}
