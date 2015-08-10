package com.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
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
import com.BJ.utils.Person;
import com.BJ.utils.PreferenceUtils;
import com.biju.Interface;
import com.biju.Interface.readUserListenner;
import com.biju.R;
import com.biju.function.AboutUsActivity;
import com.biju.function.FeedbackActivity;
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
	private String TestcompleteURL = beginStr
			+ "1ddff6cf-35ac-446b-8312-10f4083ee13d" + endStr;
	
	private String fileName = getSDPath() + "/" + "saveData";
	private String pk_user;
	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		// 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		return sdDir.toString();

	}

	// 完整路径completeURL=beginStr+result.filepath+endStr;

	public SettingFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayout = inflater.inflate(R.layout.fragment_setting, container, false);
		User4head();
		return mLayout;
	}

	private void User4head() {
		String Cacheurl = PreferenceUtils.readImageCache(getActivity());
		completeURL = Cacheurl;
		
		FileInputStream fis;
		try {
			fis = new FileInputStream(fileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Person person = (Person) ois.readObject();
			pk_user = person.pk_user;
			Log.e("SettingFragment", "从sd卡中获取到的pk_user" + pk_user);
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		initUI();
		boolean isWIFI = Ifwifi.getNetworkConnected(getActivity());
		if (isWIFI) {
			returndata();
			Log.e("SettingFragment", "有网络进入这里" + isWIFI);
		} else {
			ImageLoaderUtils.getInstance().LoadImage(getActivity(),
					completeURL, mSetting_head);
			Log.e("SettingFragment", "没有网络进入这里" + isWIFI);
		}
	}

	@Override
	public void onStart() {
		User4head();
		super.onStart();
	}

	private void returndata() {
			setting_number.setText("必聚号:" + pk_user);
			ReadUser(Integer.valueOf(pk_user));
	}

	private void ReadUser(int returndata) {
		Interface readuserinter = Interface.getInstance();
		User readuser = new User();
		readuser.setPk_user(returndata);
		readuserinter.readUser(getActivity(), readuser);
		readuserinter.setPostListener(new readUserListenner() {

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
					PreferenceUtils.saveImageCache(getActivity(), completeURL);// 存SP
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
		mLayout.findViewById(R.id.setting_feedback).setOnClickListener(this);
		mLayout.findViewById(R.id.setting_about_us).setOnClickListener(this);
		mLayout.findViewById(R.id.tab_setting_cancle_layout)
				.setOnClickListener(this);// 注销登录
		mLayout.findViewById(R.id.tab_setting_cancle).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_user:
			setting_user();
			break;
		case R.id.setting_feedback:
			setting_feedback();
			break;
		case R.id.setting_about_us:
			setting_about_us();
			break;
		case R.id.tab_setting_cancle_layout:
		case R.id.tab_setting_cancle:
			tab_setting_cancle();
			break;
		default:
			break;
		}
	}

	private void tab_setting_cancle() {
		SharedPreferences login_sp = getActivity().getSharedPreferences(
				"Logout", 0);
		Editor login_editor = login_sp.edit();
		login_editor.putBoolean("isLogout", true);
		login_editor.commit();
		Intent intent = new Intent(getActivity(), LoginActivity.class);
		startActivity(intent);
		getActivity().finish();

	}

	private void setting_about_us() {
		Intent intent = new Intent(getActivity(), AboutUsActivity.class);
		startActivity(intent);
	}

	private void setting_feedback() {
		Intent intent = new Intent(getActivity(), FeedbackActivity.class);
		startActivity(intent);
	}

	private void setting_user() {
		Intent intent = new Intent(getActivity(), UserSettingActivity.class);
		startActivity(intent);
	}
}
