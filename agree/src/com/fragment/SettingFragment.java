package com.fragment;

import com.biju.R;
import com.biju.function.UserSettingActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class SettingFragment extends Fragment implements OnClickListener{

	private View mLayout;
	private TextView setting_number;
	private ImageView mSetting_head;
	

	public SettingFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(mLayout==null)
		{
			mLayout = inflater.inflate(R.layout.fragment_setting, container, false);
			initUI();
			returndata();
		}
		return mLayout;
	}
	private void returndata() {
		SharedPreferences sp=getActivity().getSharedPreferences("Registered", 0);
		int returndata=sp.getInt("returndata", 0);
		setting_number.setText("±Ø¾ÛºÅ:"+returndata);
	}

	private void initUI() {
		setting_number = (TextView) mLayout.findViewById(R.id.setting_number);
		mLayout.findViewById(R.id.setting_user).setOnClickListener(this);
		mSetting_head = (ImageView) mLayout.findViewById(R.id.setting_head);
		mSetting_head.setOnClickListener(this);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ViewGroup parent=(ViewGroup) mLayout.getParent();
		parent.removeView(mLayout);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_user:
			setting_user();
			break;

		default:
			break;
		}
	}

	private void setting_user() {
		Intent intent=new Intent(getActivity(), UserSettingActivity.class);
		startActivity(intent);
	}
}
