package com.BJ.photo;

import java.util.ArrayList;
import java.util.List;

import com.biju.R;
import com.biju.function.GroupActivity;
import com.fragment.PhotoFragment;
import com.fragment.PhotoFragment2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * ��������ڽ���ͼƬ���ʱ�Ľ���
 *
 * @author king
 * @QQ:595163260
 * @version 2014��10��18�� ����11:47:53
 */
public class GalleryActivity extends Activity {
	private Intent intent;
	// // ���Ͱ�ť
//	private Button send_bt;
	// ɾ����ť
	private Button del_bt;
	// ������ʾԤ��ͼƬλ�õ�textview
	private TextView positionTextView;
	// ��ȡǰһ��activity��������position
	private int position;
	// ��ǰ��λ��
	private int location = 0;

	private ArrayList<View> listViews = null;
	private ViewPagerFixed pager;
	private MyPageAdapter adapter;

	public List<Bitmap> bmp = new ArrayList<Bitmap>();
	public List<String> drr = new ArrayList<String>();
	public List<String> del = new ArrayList<String>();

	private Context mContext;

	RelativeLayout photo_relativeLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plugin_camera_gallery);// ������������
//		PublicWay.activityList.add(this);
		mContext = this;
		// back_bt = (Button) findViewById(Res.getWidgetID("gallery_back"));
//		send_bt = (Button) findViewById(R.id.send_button);
		del_bt = (Button) findViewById(R.id.gallery_del);
		// back_bt.setOnClickListener(new BackListener());
//		send_bt.setOnClickListener(new GallerySendListener());
		del_bt.setOnClickListener(new DelListener());
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		position = Integer.parseInt(intent.getStringExtra("position"));
//		isShowOkBt();
		// Ϊ���Ͱ�ť��������
		pager = (ViewPagerFixed) findViewById(Res.getWidgetID("gallery01"));
		pager.setOnPageChangeListener(pageChangeListener);
		//PhotoFragment.bitmaps
		for (int i = 0; i < PhotoFragment2.bitmaps.size(); i++) {
			initListViews(PhotoFragment2.bitmaps.get(i));
		}

		adapter = new MyPageAdapter(listViews);
		pager.setAdapter(adapter);
		pager.setPageMargin((int) getResources().getDimensionPixelOffset(
				Res.getDimenID("ui_10_dip")));
		int id = intent.getIntExtra("ID", 0);
		pager.setCurrentItem(id);
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {
			location = arg0;
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageScrollStateChanged(int arg0) {

		}
	};

	private void initListViews(Bitmap bm) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		PhotoView img = new PhotoView(this);
		img.setBackgroundColor(0xff000000);
		img.setImageBitmap(bm);
		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		listViews.add(img);
	}

	// ɾ����ť��ӵļ�����
	private class DelListener implements OnClickListener {

		public void onClick(View v) {
//			if (listViews.size() == 1) {
////				PhotoFragment.bitmaps.clear();
//				Bimp.tempSelectBitmap.clear();
//				Bimp.max = 0;
//				Intent intent = new Intent("data.broadcast.action");
//				sendBroadcast(intent);
//				finish();
//			} else {
////				PhotoFragment.bitmaps.remove(location);
//				Bimp.tempSelectBitmap.remove(location);
//				Bimp.max--;
//				pager.removeAllViews();
//				listViews.remove(location);
//				adapter.setListViews(listViews);
//				adapter.notifyDataSetChanged();
//			}
		}
	}

	// ��ɰ�ť�ļ���
	@SuppressWarnings("unused")
	private class GallerySendListener implements OnClickListener {
		public void onClick(View v) {
			SharedPreferences sp = getSharedPreferences("isPhoto", 0);
			Editor editor = sp.edit();
			editor.putBoolean("Photo", true);
			editor.commit();
			finish();
		}

	}

//	public void isShowOkBt() {
//		if (Bimp.tempSelectBitmap.size() > 0) {
//			send_bt.setPressed(true);
//			send_bt.setClickable(true);
//			send_bt.setTextColor(Color.WHITE);
//		} else {
//			send_bt.setPressed(false);
//			send_bt.setClickable(false);
//			send_bt.setTextColor(Color.parseColor("#E1E0DE"));
//		}
//	}

	class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;

		private int size;
		public MyPageAdapter(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {
			try {
				((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);

			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}
}
