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
	// ������һ�λ���������,Ϊ���жϵ�ǰviewpager�����������һ���
	private int lastoffset;

	public CommonFragment() {
		// Required empty public constructor
	}

	// ��ǰ��ʾ��index
	private int currIndex = 0;
	// �洢�ƶ�λ��
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
		tv_1.setTextColor(tv_1.getResources().getColor(R.drawable.green_3));// ������������ɫ
		tv_2.setTextColor(tv_2.getResources().getColor(R.drawable.lightgray2));// �������û���ɫ
		tv_3.setTextColor(tv_3.getResources().getColor(R.drawable.lightgray2));// �������û���ɫ
		Drawable drawable = getResources().getDrawable(R.drawable.red_yuan);
		/** ��һ������Ҫ��,���򲻻���ʾ. */
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());// ��ͼƬ����ѹ��
		/** ����ͼƬλ�ã��ĸ������ֱ�λ���������£�������Ϊnull�ͱ�ʾ����ʾͼƬ */
		tv_1.setCompoundDrawables(drawable, null, null, null);
		initUI(mLayout);
		return mLayout;
	}

	// viewpaer��������
	OnPageChangeListener mlistener = new OnPageChangeListener() {
		private int value3;

		@Override
		public void onPageSelected(int position) {
			for (int i = 0; i < mTextviewResIds.length; i++) {
				TextView textView = (TextView) mLayout.findViewById(mTextviewResIds[i]);
				if (position == i) { // ��position==iʱ��ʾ��ɫ
					textView.setTextColor(textView.getResources().getColor(R.drawable.green_3));
					Drawable drawable = getResources().getDrawable(R.drawable.red_yuan);
					/** ��һ������Ҫ��,���򲻻���ʾ. */
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());// ��ͼƬ����ѹ��
					/** ����ͼƬλ�ã��ĸ������ֱ�λ���������£�������Ϊnull�ͱ�ʾ����ʾͼƬ */
					textView.setCompoundDrawables(drawable, null, null, null);
				} else {
					textView.setTextColor(textView.getResources().getColor(R.drawable.lightgray2));
					Drawable drawable = getResources().getDrawable(R.drawable.red_yuan);
					/** ��һ������Ҫ��,���򲻻���ʾ. */
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());// ��ͼƬ����ѹ��
					/** ����ͼƬλ�ã��ĸ������ֱ�λ���������£�������Ϊnull�ͱ�ʾ����ʾͼƬ */
					textView.setCompoundDrawables(null, null, null, null);
				}
			}
		}

		@Override
		public void onPageScrolled(int position, float arg1, int arg2) {
			int id = mTextviewResIds[0];// �õ��±�Ϊ0��textview
			TextView textview = (TextView) mLayout.findViewById(id);
			// ��ȡ�������Ĳ��ֵ����ԣ������� ��ʲô���֣�������Ҫ�����õĲ�������
			RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) textview.getLayoutParams();
			// �õ���߾�
			int leftMargin = layoutParams.leftMargin;
			// �õ��ұ߾�
			int rihtMargin = layoutParams.rightMargin;
			// �õ�����textiew�Ŀ��
			int width = textview.getWidth();
			// �ƶ�һ����textview����Ҫ�Ŀ��
			int realwidth = width + leftMargin + rihtMargin;
			// ����*����textview����Ҫ�Ŀ�ȵõ���ǰ����Ҫ�ƶ���λ�Ʊ���
			int value = (int) (arg1 * realwidth);
			// ��һ����ֹ�����ٶȹ��� �ڶ�����ʹ���һ�ε�ֵΪ0�� ������ʹ����Ϊ0
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
		// ����textiew
		for (int i = 0; i < mTextviewResIds.length; i++) {
			int id = mTextviewResIds[i];
			TextView textView = (TextView) mLayout.findViewById(id);
			textView.setOnClickListener(this);
		}
		// viewpaer��������
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
		// ���ַ�����
		int position = Arrays.binarySearch(mTextviewResIds, id);
		// �����ʾҳ��ʱ����ʾ����Ч��
		mPager.setCurrentItem(position, false);
	}

}
