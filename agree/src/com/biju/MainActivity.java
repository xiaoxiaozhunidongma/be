package com.biju;

import java.util.ArrayList;

import leanchatlib.controller.ChatManager;
import leanchatlib.utils.LogUtils;
import android.app.Activity;
import android.content.Intent;
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
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.BJ.utils.InitHead;
import com.BJ.utils.LimitLong;
import com.BJ.utils.Path2Bitmap;
import com.BJ.utils.PicCutter;
import com.BJ.utils.RefreshActivity;
import com.BJ.utils.SdPkUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.fragment.FriendsFragment;
import com.fragment.HomeFragment;
import com.fragment.PartyFragment;
import com.fragment.SettingFragment;

public class MainActivity extends FragmentActivity  {
	
	//����ʱ��Ҫ�̳е�implements OnTouchListener,OnGestureListener
	private FragmentTabHost mTabhost;
	private int tab_imagelist[] = new int[] { R.drawable.tab_home_selector,
			R.drawable.tab_party_selector, R.drawable.tab_friends_selector,
			R.drawable.tab_setting_selector };

	private String mFilePath;
	private Integer SD_pk_user;
	private Bitmap convertToBitmap;
	private Bitmap limitLongScaleBitmap;
	private Bitmap centerSquareScaleBitmap;

//	@SuppressWarnings("unused")//����
//	private GestureDetector mGestureDetector;
//	private int verticalMinDistance = 180;
//	private int minVelocity = 0;
//	private int currentTab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_tabs);

//		homeImageLoaderUtils.clearCache();
//		ImageLoaderUtils4Photos.clearCache();
//		ImageLoaderUtils.clearCache();
		
		// �ر�֮ǰ�Ľ���
		for (int i = 0; i < RefreshActivity.activList_3.size(); i++) {
			RefreshActivity.activList_3.get(i).finish();
		}
 
		initUI();// ��ʼ��Tabhost
		SD_pk_user = SdPkUser.getsD_pk_user();
		chatUserlogin();
	}
	
	private void chatUserlogin() {
		Log.e("MainActivity", "SD_pk_user=="+SD_pk_user);
		ChatManager chatManager = ChatManager.getInstance();
		chatManager.setupManagerWithUserId(String.valueOf(SD_pk_user));
		chatManager.openClient(new AVIMClientCallback() {



			@Override
			public void done(AVIMClient avimClient, AVIMException e) {
				if (e != null) {
					LogUtils.logException(e);
					Log.e("MainActivity", "lean�û���¼ʧ��");
				}else{
					Log.e("MainActivity", "lean�û���¼�ɹ�");
				}
			}
		});
	}

	private void initUI() {
		mTabhost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		AddTab("1", "С��", 0, HomeFragment.class);
		AddTab("2", "�ۻ�", 1, PartyFragment.class);
		AddTab("3", "����", 2, FriendsFragment.class);
		AddTab("4", "��", 3, SettingFragment.class);
//		currentTab = mTabhost.getCurrentTab();//����
//		Log.e("MainActivity", "��ǰ��currentTab============" + currentTab);
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
		//tabhost�ļ��������ļ���
//		mTabhost.setOnTabChangedListener(new OnTabChangeListener() {
//			
//			@Override
//			public void onTabChanged(String tabId) {
//				Log.e("MainActivity", "��ǰ��tabId============" + tabId);
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
		// �ر�֮ǰ�Ľ���
		for (int i = 0; i < RefreshActivity.activList_3.size(); i++) {
			RefreshActivity.activList_3.get(i).finish();
		}
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
		//�ж�С������޸Ĺ���false
		SdPkUser.setRefreshTeam(false);
		SdPkUser.setExit(false);
		//�½����ճ̺��
		SharedPreferences refresh_sp=getSharedPreferences(IConstant.AddRefresh, 0);
		Editor editor2=refresh_sp.edit();
		editor2.putBoolean(IConstant.IsAddRefresh,false);
		editor2.commit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("MainActivity", "�Ƿ��н���========================");
		if (resultCode != Activity.RESULT_OK || data == null)
			return;
		try {
//			Uri selectedImage = data.getData();
////			long parseId = ContentUris.parseId(selectedImage);
//			String[] filePathColumn = { MediaStore.Images.Media.DATA };
//			Cursor cursor = MainActivity.this.getContentResolver().query(
//					selectedImage, filePathColumn, null, null, null);
//			if (cursor != null) {
//				cursor.moveToFirst();
//				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//				mFilePath = cursor.getString(columnIndex);
//				cursor.close();
//				cursor = null;
//
//			} else {
//				
//				File file = new File(selectedImage.getPath());
//				mFilePath = file.getAbsolutePath();
//				if (!file.exists()) {
//					Toast toast = Toast.makeText(this, "�Ҳ���ͼƬ",Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
//					return;
//				}
//			}
//			//����ͼ·��
//			        Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(MainActivity.this.getContentResolver(), parseId, Images.Thumbnails.MICRO_KIND,  
//					                null); 
			        
			@SuppressWarnings("unchecked")
			ArrayList<String> mSelectedImageList = (ArrayList<String>) data.getSerializableExtra("mSelectedImageList");
			mFilePath=mSelectedImageList.get(0);
			Log.e("MainActivity", "mSelectedImageList.size()======" + mSelectedImageList.size());
			Log.e("MainActivity", "mFilePath======" + mFilePath);
			
			convertToBitmap = Path2Bitmap.convertToBitmap(mFilePath);
			
			limitLongScaleBitmap = LimitLong.limitLongScaleBitmap(
					convertToBitmap, 1080);
			centerSquareScaleBitmap = PicCutter.centerSquareScaleBitmap(
					limitLongScaleBitmap, 600);
			
//			Bitmap bmp = MyBimp.revitionImageSize(mFilePath);
			InitHead.initHead(centerSquareScaleBitmap);
			Log.e("MainActivity", "��ȡ��ͼƬ·��=======" + mFilePath);
			SdPkUser.setGetFilePath(mFilePath);
		} catch (Exception e) {
			Log.e("", "catch:" + e.getMessage());
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(convertToBitmap!=null){
			convertToBitmap.recycle();
		}
		if(limitLongScaleBitmap!=null){
			limitLongScaleBitmap.recycle();
		}
		if(centerSquareScaleBitmap!=null){
			centerSquareScaleBitmap.recycle();
		}
	       System.gc();  //����ϵͳ��ʱ����
		super.onDestroy();
	}

	// ��������
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
//			// �л�Activity
//			if(currentTab<3)
//			{
//				currentTab++;
//				int tab = currentTab % 4;
//				Log.e("MainActivity", "��ǰ��currentTab++============" + currentTab);
//				Log.e("MainActivity", "��ǰ��tab++=============" + tab);
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
//				Log.e("MainActivity", "��ǰ��currentTab--============" + currentTab);
//				Log.e("MainActivity", "��ǰ��tab--=============" + tab);
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
