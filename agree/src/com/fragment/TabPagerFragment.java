package com.fragment;

import java.util.ArrayList;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabWidget;
import android.widget.TextView;

import com.biju.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
public class TabPagerFragment extends Fragment implements TabContentFactory,
		OnTabChangeListener, OnPageChangeListener {
	private TabHost mTabHost;
	private ViewPager mPager;
	private ArrayList<Fragment> fragments=new ArrayList<Fragment>();
	private String[] labels=new String[]{};
	private int[] tabIcons=new int[]{};
	private ImageView mImgScrollbar;

	public void setArg(String[] labels, int[] tabIcons,
			ArrayList<Fragment> fragments) {
		this.labels = labels;
		this.tabIcons = tabIcons;
		this.fragments = fragments;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_main, null);
		mImgScrollbar = (ImageView) layout.findViewById(R.id.img_scrollbar);
		initTab(inflater, layout);
		initPager(layout);
		return layout;
	}

	private void initTab(LayoutInflater inflater, View layout) {
		mTabHost = (TabHost) layout.findViewById(android.R.id.tabhost);
		mTabHost.setup();

		for (int i = 0; i < labels.length; i++) {
			View tabIndicator = inflater.inflate(R.layout.tabhost_item, null);
			ImageView tabIcon = (ImageView) tabIndicator
					.findViewById(R.id.tab_image);
			tabIcon.setImageResource(tabIcons[i]);
			TextView tabTitle = (TextView) tabIndicator
					.findViewById(R.id.tab_name);
			tabTitle.setText(labels[i]);
			mTabHost.addTab(mTabHost.newTabSpec("tab" + i)
					.setIndicator(tabIndicator).setContent(this));
		}
		mTabHost.setOnTabChangedListener(this);
	}

	public void setItem(int posin) {
		mPager.setCurrentItem(posin);
	}

	private void initPager(View layout) {
		mPager = (ViewPager) layout.findViewById(R.id.pager);
		// ChildFragmentMananger
		FragmentManager fm = getChildFragmentManager();
		mPager.setAdapter(new MyPagerAdapter(fm));
		mPager.setOnPageChangeListener(this);
		mPager.setOffscreenPageLimit(0);
	}
	
	
	class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}
 
		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);

		}

		@Override
		public int getCount() {
			return fragments.size();
		}
	}

	@Override
	public View createTabContent(String tag) {
		TextView tv = new TextView(getActivity());
		return tv;
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public void onTabChanged(String tabId) {
		int position = mTabHost.getCurrentTab();
		mPager.setCurrentItem(position);
		
		if(position==1)
		{
			SharedPreferences tab_sp=getActivity().getSharedPreferences("TabParge", 0);
			Editor editor=tab_sp.edit();
			editor.putBoolean("tabpager", true);
			editor.commit();
		}
		
		WindowManager windowMgr = (WindowManager) getActivity()
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowMgr.getDefaultDisplay();
		int width = display.getWidth();
		int offset = width / labels.length;
		ObjectAnimator ofFloat = ObjectAnimator.ofFloat(mImgScrollbar,
				"translationX", lastOffset, position * offset);
		ofFloat.setInterpolator(new DecelerateInterpolator());
		ofFloat.setDuration(300).start();
		lastOffset = position * offset;
	}

	private int lastOffset;

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		TabWidget widget = mTabHost.getTabWidget();
		int oldFocusability = widget.getDescendantFocusability();
		widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		mTabHost.setCurrentTab(position);
		Log.e("TabPagerFragment", "进入后的显示的是哪一个fragment"+position);
		widget.setDescendantFocusability(oldFocusability);

	}

	public TabHost GetTabhost() {
		return mTabHost;

	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub

	}

}
