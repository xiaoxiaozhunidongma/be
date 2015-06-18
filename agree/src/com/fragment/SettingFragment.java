package com.fragment;

import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.BJ.javabean.Loginback;
import com.BJ.javabean.User;
import com.BJ.utils.Ifwifi;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.PreferenceUtils;
import com.biju.Interface;
import com.biju.Interface.UserInterface;
import com.biju.R;
import com.biju.function.UserSettingActivity;
import com.biju.login.LoginActivity;
import com.github.volley_examples.utils.GsonUtils;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class SettingFragment extends Fragment implements OnClickListener {

	private View mLayout;
	private TextView setting_number;
	private ImageView mSetting_head;
	private String beginStr = "http://201139.image.myqcloud.com/201139/0/";
	private String endStr = "/original";
	private String useravatar_path;
	private String completeURL;
	private String TestcompleteURL=beginStr+"1ddff6cf-35ac-446b-8312-10f4083ee13d"+endStr;

	// 完整路径completeURL=beginStr+result.filepath+endStr;

	public SettingFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		String Cacheurl = PreferenceUtils.readImageCache(getActivity());
		completeURL=Cacheurl;
		if (mLayout == null) {
			mLayout = inflater.inflate(R.layout.fragment_setting, container,
					false);
			initUI();
			boolean isWIFI=Ifwifi.getNetworkConnected(getActivity());
			if(isWIFI)
			{
				returndata();
				Log.e("SettingFragment", "有网络进入这里"+isWIFI);
			}else
			{
				ImageLoaderUtils.getInstance().LoadImage(getActivity(),
						completeURL, mSetting_head);
				Log.e("SettingFragment", "没有网络进入这里"+isWIFI);
			}
		}
		return mLayout;
	}

	private void returndata() {
		SharedPreferences sp = getActivity().getSharedPreferences("Registered",
				0);
		int returndata_1 = sp.getInt("returndata", 0);
		boolean isRegistered_one = sp.getBoolean("isRegistered_one", false);
		if (isRegistered_one) {
			setting_number.setText("必聚号:" + returndata_1);
			ReadUser(returndata_1);
		} else {
			int returndata_2 = LoginActivity.pk_user;
			setting_number.setText("必聚号:" + returndata_2);
			ReadUser(returndata_2);
		}
	}

	private void ReadUser(int returndata) {
		Interface readuserinter = new Interface();
		User readuser = new User();
		readuser.setPk_user(returndata);
		readuserinter.readUser(getActivity(), readuser);
		readuserinter.setPostListener(new UserInterface() {


			@Override
			public void success(String A) {
				// 读取用户资料成功
				Loginback usersettingback = GsonUtils.parseJson(A,
						Loginback.class);
				int userStatusmsg = usersettingback.getStatusMsg();
				if (userStatusmsg == 1) {
					Log.e("UserSettingActivity", "用户资料" + A);
					List<User> Users = usersettingback.getReturnData();
					if (Users.size() >= 1) {
						User readuser = Users.get(0);
						useravatar_path = readuser.getAvatar_path();
					}
					completeURL = beginStr + useravatar_path + endStr;
					PreferenceUtils.saveImageCache(getActivity(), completeURL);//存SP
					ImageLoaderUtils.getInstance().LoadImage(getActivity(),
							completeURL, mSetting_head);
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
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
		ViewGroup parent = (ViewGroup) mLayout.getParent();
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
		Intent intent = new Intent(getActivity(), UserSettingActivity.class);
		startActivity(intent);
	}
}
