package com.fragment;

import java.util.Arrays;

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
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biju.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class CommonFragment extends Fragment implements OnClickListener {

	private int[] mTextviewResIds = new int[] { R.id.tv_1, R.id.tv_2, R.id.tv_3 };
	private View mLayout1;
	private ViewPager mPager;
	public static GetOpen getOpen;
	public static GetClose getClose;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
			mLayout1 = inflater.inflate(R.layout.fragment_common_fragment,container, false);
			getScreenWidth();
			initUI();
			initPager();
			initOpen();//����ɫ�򿪵��ýӿ�
			initClose();//����ɫ�رյ��ýӿ�
		return mLayout1;
	}
	

	private void initClose() {
		GetClose getClose=new GetClose(){

			@Override
			public void Close() {
				mCommonBackground.setVisibility(View.GONE);
				Animation animation=new AlphaAnimation(1.0f,0.0f);
				animation.setDuration(500);
				mCommonBackground.startAnimation(animation);
			}
		};
		this.getClose=getClose;
	}

	private void initOpen() {
		GetOpen getOpen=new GetOpen(){

			@Override
			public void Open() {
				mCommonBackground.setVisibility(View.VISIBLE);
				Animation animation=new AlphaAnimation(0.5f,1.0f);
				animation.setDuration(300);
				mCommonBackground.startAnimation(animation);
			}
			
		};
		this.getOpen=getOpen;
	}

	// ��ȡ��Ļ���
	private int getScreenWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	private void initUI() {
		mCommon_ArticleIndicates = (TextView) mLayout1.findViewById(R.id.Common_ArticleIndicates);// ����
		TextView tv_1 = (TextView) mLayout1.findViewById(R.id.tv_1);
		TextView tv_2 = (TextView) mLayout1.findViewById(R.id.tv_2);
		TextView tv_3 = (TextView) mLayout1.findViewById(R.id.tv_3);
		tv_1.setTextColor(tv_1.getResources().getColor(R.drawable.Common_text_color_green));// ������������ɫ
		tv_2.setTextColor(tv_2.getResources().getColor(R.drawable.Common_text_color_gray));// �������û���ɫ
		tv_3.setTextColor(tv_3.getResources().getColor(R.drawable.Common_text_color_gray));// �������û���ɫ
		//���ú�ɫСԲ��
//		Drawable drawable = getResources().getDrawable(R.drawable.red_yuan);
//		/** ��һ������Ҫ��,���򲻻���ʾ. */
//		drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());// ��ͼƬ����ѹ��
//		/** ����ͼƬλ�ã��ĸ������ֱ�λ���������£�������Ϊnull�ͱ�ʾ����ʾͼƬ */
//		tv_1.setCompoundDrawables(drawable, null, null, null);
		current_realwidth = getScreenWidth();
		mCommon_ArticleIndicates.setWidth(current_realwidth / 3);// ���û���ĳ���
		
		mCommonBackground = (RelativeLayout) mLayout1.findViewById(R.id.CommonBackground);//����ɫ
		
	}

	// viewpaer��������
	OnPageChangeListener mlistener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			for (int i = 0; i < mTextviewResIds.length; i++) {
				TextView textView = (TextView) mLayout1.findViewById(mTextviewResIds[i]);
				if (position == i) { // ��position==iʱ��ʾ��ɫ
					//����������ɫ
					textView.setTextColor(textView.getResources().getColor(R.drawable.Common_text_color_green));
					//���ú�ɫСԲ��
//					Drawable drawable = getResources().getDrawable(R.drawable.red_yuan);
//					/** ��һ������Ҫ��,���򲻻���ʾ. */
//					drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());// ��ͼƬ����ѹ��
//					/** ����ͼƬλ�ã��ĸ������ֱ�λ���������£�������Ϊnull�ͱ�ʾ����ʾͼƬ */
//					textView.setCompoundDrawables(drawable, null, null, null);
					//���û��黬��
					Animation animation = new TranslateAnimation((current_realwidth / 3) * currIndex,(current_realwidth / 3) * (position), 0, 0);
					currIndex = position;
					animation.setFillAfter(true);
					animation.setDuration(200);
					mCommon_ArticleIndicates.startAnimation(animation);
				} else {
					//����������ɫ
					textView.setTextColor(textView.getResources().getColor(R.drawable.Common_text_color_gray));
					//���ú�ɫСԲ��
//					Drawable drawable = getResources().getDrawable(R.drawable.red_yuan);
//					/** ��һ������Ҫ��,���򲻻���ʾ. */
//					drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());// ��ͼƬ����ѹ��
//					/** ����ͼƬλ�ã��ĸ������ֱ�λ���������£�������Ϊnull�ͱ�ʾ����ʾͼƬ */
//					textView.setCompoundDrawables(null, null, null, null);
				}
			}

		}

		@Override
		public void onPageScrolled(int position, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	};
	private RelativeLayout mCommonBackground;

	private void initPager() {
			mPager = (ViewPager) mLayout1.findViewById(R.id.pager);
			MyFragmentAdapter myFragmentAdapter = new MyFragmentAdapter(getChildFragmentManager());
			mPager.setAdapter(myFragmentAdapter);
			// ����textiew
			for (int i = 0; i < mTextviewResIds.length; i++) {
				int id = mTextviewResIds[i];
				TextView textView = (TextView) mLayout1.findViewById(id);
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
				mCurrFragment = new PhotosPreviewFragment();
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

	public interface GetOpen{
		void Open();
	}
	public interface GetClose{
		void Close();
	}
	
	
}
