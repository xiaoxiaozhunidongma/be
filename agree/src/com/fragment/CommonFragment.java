package com.fragment;

import java.util.Arrays;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biju.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("ResourceAsColor")
public class CommonFragment extends Fragment implements OnClickListener {

	private int[] mTextviewResIds = new int[] { R.id.tv_1, R.id.tv_2, R.id.tv_3 };
	private View mLayout;
	private ViewPager mPager;
	// 保存上一次滑动的像素,为了判断当前viewpager是向左还是向右滑动
	private int lastoffset;

	public CommonFragment() {
		// Required empty public constructor
	}

	// 当前显示的index
	private int currIndex = 0;
	// 存储移动位置
	private int positions[];
	private int value1;
	private int value2;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayout = inflater.inflate(R.layout.fragment_common_fragment,container, false);
		TextView tv_1 = (TextView) mLayout.findViewById(R.id.tv_1);
		TextView tv_2 = (TextView) mLayout.findViewById(R.id.tv_2);
		TextView tv_3 = (TextView) mLayout.findViewById(R.id.tv_3);
		tv_1.setTextColor(tv_1.getResources().getColor(R.drawable.green_3));// 给字设置绿颜色
		tv_2.setTextColor(tv_2.getResources().getColor(R.drawable.lightgray2));// 给字设置灰颜色
		tv_3.setTextColor(tv_3.getResources().getColor(R.drawable.lightgray2));// 给字设置灰颜色
		Drawable drawable = getResources().getDrawable(R.drawable.red_yuan);
		/** 这一步必须要做,否则不会显示. */
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());// 对图片进行压缩
		/** 设置图片位置，四个参数分别方位是左上右下，都设置为null就表示不显示图片 */
		tv_1.setCompoundDrawables(drawable, null, null, null);
		initUI(mLayout);
		return mLayout;
	}

	// viewpaer滑动监听
	OnPageChangeListener mlistener = new OnPageChangeListener() {
		private int value3;

		@Override
		public void onPageSelected(int position) {
			for (int i = 0; i < mTextviewResIds.length; i++) {
				TextView textView = (TextView) mLayout.findViewById(mTextviewResIds[i]);
				if (position == i) { // 当position==i时显示红色
					textView.setTextColor(textView.getResources().getColor(R.drawable.green_3));
					Drawable drawable = getResources().getDrawable(R.drawable.red_yuan);
					/** 这一步必须要做,否则不会显示. */
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());// 对图片进行压缩
					/** 设置图片位置，四个参数分别方位是左上右下，都设置为null就表示不显示图片 */
					textView.setCompoundDrawables(drawable, null, null, null);
				} else {
					textView.setTextColor(textView.getResources().getColor(R.drawable.lightgray2));
					Drawable drawable = getResources().getDrawable(R.drawable.red_yuan);
					/** 这一步必须要做,否则不会显示. */
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());// 对图片进行压缩
					/** 设置图片位置，四个参数分别方位是左上右下，都设置为null就表示不显示图片 */
					textView.setCompoundDrawables(null, null, null, null);
				}
			}
		}

		@Override
		public void onPageScrolled(int position, float arg1, int arg2) {
			int id = mTextviewResIds[0];// 得到下标为0的textview
			TextView textview = (TextView) mLayout.findViewById(id);
			// 获取所关联的布局的属性，布局中 用什么布局，所导包要是所用的布局类型
			RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) textview.getLayoutParams();
			// 得到左边距
			int leftMargin = layoutParams.leftMargin;
			// 得到右边距
			int rihtMargin = layoutParams.rightMargin;
			// 得到整个textiew的宽度
			int width = textview.getWidth();
			// 移动一整个textview所需要的宽度
			int realwidth = width + leftMargin + rihtMargin;
			// 比例*整个textview所需要的宽度得到当前所需要移动的位移比例
			int value = (int) (arg1 * realwidth);
			// 第一个防止滑动速度过快 第二个是使最后一次的值为0， 第三个使像素为0
			if (Math.abs(lastoffset - arg2) >= 80 || lastoffset == 0
					|| arg2 == 0) {
				lastoffset = arg2;
				return;
			}
			
				Animation animation = new TranslateAnimation(realwidth*position,
						value+realwidth*position, 0, 0);
				currIndex = position;
				animation.setFillAfter(true);
				animation.setDuration(300);
				mCommon_ArticleIndicates.startAnimation(animation);
			
			
			for (int i = 0; i < mTextviewResIds.length; i++) {
				if(position==i)
				{
//					ObjectAnimator.ofFloat(mCommon_ArticleIndicates, "translationX", value, 0)
//					.setDuration(500)
//					.start();
//					Animation animation1 = AnimationUtils.loadAnimation(
//					getActivity(), R.anim.huakuai_translate_you);
//					animation1.setFillAfter(true);
//					mCommon_ArticleIndicates.startAnimation(animation1);
					
				}
			}
			lastoffset = arg2;
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	};
	private TextView mCommon_ArticleIndicates;

	private void initUI(View mLayout2) {
		mCommon_ArticleIndicates = (TextView) mLayout.findViewById(R.id.Common_ArticleIndicates);
		mPager = (ViewPager) mLayout.findViewById(R.id.pager);
		MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(getChildFragmentManager());
		mPager.setAdapter(myFragmentAdapter);
		// 查找textiew
		for (int i = 0; i < mTextviewResIds.length; i++) {
			int id = mTextviewResIds[i];
			TextView textView = (TextView) mLayout.findViewById(id);
			textView.setOnClickListener(this);
		}
		// viewpaer滑动监听
		mPager.setOnPageChangeListener(mlistener);
	}

	class MyFragmentAdapter extends FragmentPagerAdapter {

		public MyFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment mCurrFragment = null;
			if (position == 0) {
				mCurrFragment = new ChatFragment();
			} else if (position == 1) {
				mCurrFragment = new ScheduleFragment();
			} else if (position == 2) {
				mCurrFragment = new PhotoFragment2();
			}
			return mCurrFragment;
		}

		@Override
		public int getCount() {
			return 3;
		}

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		// 二分法查找
		int position = Arrays.binarySearch(mTextviewResIds, id);
		// 点击显示页面时不显示滑动效果
		mPager.setCurrentItem(position, false);
	}

}
