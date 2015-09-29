package com.fragment;

import java.util.Arrays;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
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
	// private int lastoffset;

	public CommonFragment() {
		// Required empty public constructor
	}

	// ��ǰ��ʾ��index
	private int currIndex = 0;
	private TextView mCommon_ArticleIndicates;
	private int current_realwidth;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayout = inflater.inflate(R.layout.fragment_common_fragment,container, false);
		getScreenWidth();
		initUI(mLayout);
		initPager(mLayout);
		return mLayout;
	}

	// ��ȡ��Ļ���
	private int getScreenWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	private void initUI(View mLayout2) {
		mCommon_ArticleIndicates = (TextView) mLayout.findViewById(R.id.Common_ArticleIndicates);// ����
		TextView tv_1 = (TextView) mLayout.findViewById(R.id.tv_1);
		TextView tv_2 = (TextView) mLayout.findViewById(R.id.tv_2);
		TextView tv_3 = (TextView) mLayout.findViewById(R.id.tv_3);
		tv_1.setTextColor(tv_1.getResources().getColor(R.drawable.Common_text_color_green));// ������������ɫ
		tv_2.setTextColor(tv_2.getResources().getColor(R.drawable.Common_text_color_gray));// �������û���ɫ
		tv_3.setTextColor(tv_3.getResources().getColor(R.drawable.Common_text_color_gray));// �������û���ɫ
		Drawable drawable = getResources().getDrawable(R.drawable.red_yuan);
		/** ��һ������Ҫ��,���򲻻���ʾ. */
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());// ��ͼƬ����ѹ��
		/** ����ͼƬλ�ã��ĸ������ֱ�λ���������£�������Ϊnull�ͱ�ʾ����ʾͼƬ */
		tv_1.setCompoundDrawables(drawable, null, null, null);
		current_realwidth = getScreenWidth();
		mCommon_ArticleIndicates.setWidth(current_realwidth / 3);// ���û���ĳ���
	}

	// viewpaer��������
	OnPageChangeListener mlistener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			for (int i = 0; i < mTextviewResIds.length; i++) {
				TextView textView = (TextView) mLayout.findViewById(mTextviewResIds[i]);
				if (position == i) { // ��position==iʱ��ʾ��ɫ
					textView.setTextColor(textView.getResources().getColor(R.drawable.Common_text_color_green));
					Drawable drawable = getResources().getDrawable(R.drawable.red_yuan);
					/** ��һ������Ҫ��,���򲻻���ʾ. */
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());// ��ͼƬ����ѹ��
					/** ����ͼƬλ�ã��ĸ������ֱ�λ���������£�������Ϊnull�ͱ�ʾ����ʾͼƬ */
					textView.setCompoundDrawables(drawable, null, null, null);
					Animation animation = new TranslateAnimation((current_realwidth / 3) * currIndex,(current_realwidth / 3) * (position), 0, 0);
					currIndex = position;
					animation.setFillAfter(true);
					animation.setDuration(200);
					mCommon_ArticleIndicates.startAnimation(animation);
				} else {
					textView.setTextColor(textView.getResources().getColor(R.drawable.Common_text_color_gray));
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
			// int id = mTextviewResIds[0];// �õ��±�Ϊ0��textview
			// TextView textview = (TextView) mLayout.findViewById(id);
			// // ��ȡ�������Ĳ��ֵ����ԣ������� ��ʲô���֣�������Ҫ�����õĲ�������
			// RelativeLayout.LayoutParams layoutParams =
			// (android.widget.RelativeLayout.LayoutParams)
			// textview.getLayoutParams();
			// // �õ���߾�
			// int leftMargin = layoutParams.leftMargin;
			// // �õ��ұ߾�
			// int rihtMargin = layoutParams.rightMargin;
			// // �õ�����textiew�Ŀ��
			// int width = textview.getWidth();
			// // �ƶ�һ����textview����Ҫ�Ŀ��
			// int realwidth = width + leftMargin + rihtMargin;
			// // ����*����textview����Ҫ�Ŀ�ȵõ���ǰ����Ҫ�ƶ���λ�Ʊ���
			// int value = (int) (arg1 * realwidth);
			// ��һ����ֹ�����ٶȹ��� �ڶ�����ʹ���һ�ε�ֵΪ0�� ������ʹ����Ϊ0
			// if (Math.abs(lastoffset - arg2) >= 80 || lastoffset == 0|| arg2
			// == 0) {
			// lastoffset = arg2;
			// return;
			// }
			// // �������󻬣�
			// if (lastoffset < arg2) {
			// Animation animation = new
			// TranslateAnimation((current_realwidth/3)*currIndex,(current_realwidth/3)*(position+1),
			// 0, 0);
			// currIndex = position;
			// animation.setFillAfter(true);
			// animation.setDuration(200);
			// mCommon_ArticleIndicates.startAnimation(animation);
			// }
			//
			// // �������һ���
			// else if (lastoffset > arg2) {
			// Animation animation = new
			// TranslateAnimation((current_realwidth/3)*(currIndex+1),(current_realwidth/3)*position,
			// 0, 0);
			// currIndex = position;
			// animation.setFillAfter(true);
			// animation.setDuration(200);
			// mCommon_ArticleIndicates.startAnimation(animation);
			// }
			// lastoffset = arg2;
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	};

	private void initPager(View mLayout2) {
		mPager = (ViewPager) mLayout.findViewById(R.id.pager);
		MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(
				getChildFragmentManager());
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
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("CommonFragment", "���������onActivityResult");
	}

}
