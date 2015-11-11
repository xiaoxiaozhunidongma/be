package com.biju.chatroom;

import java.util.ArrayList;

import com.BJ.utils.DensityUtil;
import com.BJ.utils.ImageLoaderUtils4Photos;
import com.biju.HackyViewPager;
import com.biju.R;

import uk.co.senab.photoview.PhotoView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class MyGalleryActivity extends Activity implements OnClickListener {

	private HackyViewPager mViewPager;
	private static final String ISLOCKED_ARG = "isLocked";
	public static ArrayList<String> netFullpath=new ArrayList<String>();
	private static int curposition;
	private ImageView iv_jianhao;
	private int iv_jianhao_height;
	private RelativeLayout rela_slideup;
	private RelativeLayout rela_translucent;
	private int windowHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		
		initUI();
		 Intent intent = getIntent();
		 curposition = intent.getIntExtra("position", -1);

			mViewPager.setAdapter(new SamplePagerAdapter());
			if (savedInstanceState != null) {
				boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
				((HackyViewPager) mViewPager).setLocked(isLocked);
			}
			
			mViewPager.setCurrentItem(curposition);
			Log.e("MyGalleryActivity", "netFullpath=="+netFullpath);
	}
	
	private void initUI() {
		 mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
		iv_jianhao = (ImageView) findViewById(R.id.iv_jianhao);
		iv_jianhao.setOnClickListener(this);
		iv_jianhao_height = iv_jianhao.getHeight();
		rela_slideup = (RelativeLayout) findViewById(R.id.rela_slideup);
		findViewById(R.id.comment).setOnClickListener(this);//弹出评论
		rela_translucent = (RelativeLayout) findViewById(R.id.rela_translucent);//大布局
		rela_translucent.setOnClickListener(this);
	}

	static class SamplePagerAdapter extends PagerAdapter {


		@Override
		public int getCount() {
			return netFullpath.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
//			photoView.setImageResource(sDrawables[position]);
			String url = netFullpath.get(position);
			Log.e("MyGalleryActivity", "url=="+url);
//			Log.e("MyGalleryActivity", "netFullpath.get(curposition)=="+netFullpath.get(curposition));
//			if(position==curposition){//asdfadsfgsdgsdfjhkabhfigsdruirg
//				ImageLoaderUtils4Photos.getInstance().LoadImage2(netFullpath.get(0), photoView);
//			}else{
				ImageLoaderUtils4Photos.getInstance().LoadImage2(url, photoView);
//			}

			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			ViewParent parent = container.getParent();
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_jianhao:
			hide();
			break;
		case R.id.comment:
			popupComment();
			break;
		case R.id.rela_translucent:
			hide();
			break;

		default:
			break;
		}
	}

	private void popupComment() {
		rela_translucent.setVisibility(View.VISIBLE);
		Animation animation=new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(500);
		iv_jianhao.startAnimation(animation);
		
		android.util.DisplayMetrics metric = new android.util.DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		windowHeight = metric.heightPixels;
		
		//从下往上滑动
		Animation animation2=new TranslateAnimation(0, 0, windowHeight, 0);
		animation2.setDuration(500);
		rela_slideup.startAnimation(animation2); 
	}

	private void hide() {
		Animation animation=new AlphaAnimation(1.0f, 0.0f);
		animation.setDuration(500);
//		iv_jianhao.setAnimation(animation);
		iv_jianhao.startAnimation(animation);
		
		int dip2px = DensityUtil.dip2px(this, 35);
		int popdown=windowHeight-dip2px-iv_jianhao_height;
		//从上往下滑动
		Animation animation2=new TranslateAnimation(0, 0, 0, popdown);
		animation2.setDuration(500);
		rela_slideup.startAnimation(animation2); 
		
		rela_slideup.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				rela_translucent.setVisibility(View.GONE);	
			}
		}, 510);
		

	}

	protected void hideSoftInputView() {
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			View currentFocus = getCurrentFocus();
			if (currentFocus != null) {
				manager.hideSoftInputFromWindow(currentFocus.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}
}
