package com.biju.chatroom;

import java.util.ArrayList;

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
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;


public class MyGalleryActivity extends Activity {

	private HackyViewPager mViewPager;
	private static final String ISLOCKED_ARG = "isLocked";
	public static ArrayList<String> netpath=new ArrayList<String>();
	public static ArrayList<String> netFullpath=new ArrayList<String>();
	private static int curposition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		 mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
//			setContentView(mViewPager);
		 Intent intent = getIntent();
		 curposition = intent.getIntExtra("position", -1);

			mViewPager.setAdapter(new SamplePagerAdapter());
			if (savedInstanceState != null) {
				boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
				((HackyViewPager) mViewPager).setLocked(isLocked);
			}
			
			mViewPager.setCurrentItem(curposition);
//			mViewPager.setOffscreenPageLimit(2);
//			mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
//				
//				@Override
//				public void onPageSelected(int arg0) {
//					// TODO Auto-generated method stub
//					
//				}
//				
//				@Override
//				public void onPageScrolled(int arg0, float arg1, int arg2) {
//					// TODO Auto-generated method stub
//					
//				}
//				
//				@Override
//				public void onPageScrollStateChanged(int arg0) {
//					// TODO Auto-generated method stub
//					
//				}
//			});
			Log.e("MyGalleryActivity", "netFullpath=="+netFullpath);
	}
	
	static class SamplePagerAdapter extends PagerAdapter {


		@Override
		public int getCount() {
			return netpath.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
//			photoView.setImageResource(sDrawables[position]);
			String url = netpath.get(position);
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

}
