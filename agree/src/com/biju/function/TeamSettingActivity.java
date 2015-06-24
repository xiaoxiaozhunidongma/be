package com.biju.function;

import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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
import com.biju.login.LoginActivity;
import com.github.volley_examples.utils.GsonUtils;

public class TeamSettingActivity extends Activity implements OnClickListener {

	private ImageView teamSetting_head;
	private TextView teamSetting_number;
	private String beginStr = "http://201139.image.myqcloud.com/201139/0/";
	private String endStr = "/original";
	private String useravatar_path;
	private String completeURL;
	private String TestcompleteURL = beginStr
			+ "1ddff6cf-35ac-446b-8312-10f4083ee13d" + endStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_setting);
		initUI();
		boolean isWIFI = Ifwifi.getNetworkConnected(TeamSettingActivity.this);
		if (isWIFI) {
			returndata();
		} else {
			ImageLoaderUtils.getInstance().LoadImage(TeamSettingActivity.this,
					completeURL, teamSetting_head);
		}
	}

	private void returndata() {
		SharedPreferences sp = getSharedPreferences("Registered", 0);
		int returndata_1 = sp.getInt("returndata", 0);
		boolean isRegistered_one = sp.getBoolean("isRegistered_one", false);
		if (isRegistered_one) {
			teamSetting_number.setText("" + returndata_1);
			ReadUser(returndata_1);
		} else {
			int returndata_2 = LoginActivity.pk_user;
			teamSetting_number.setText("" + returndata_2);
			ReadUser(returndata_2);
		}
	}

	private void ReadUser(int returndata) {
		Interface readuserinter = new Interface();
		User readuser = new User();
		readuser.setPk_user(returndata);
		readuserinter.readUser(TeamSettingActivity.this, readuser);
		readuserinter.setPostListener(new UserInterface() {

			@Override
			public void success(String A) {
				// 读取用户资料成功
				Loginback usersettingback = GsonUtils.parseJson(A,
						Loginback.class);
				int userStatusmsg = usersettingback.getStatusMsg();
				if (userStatusmsg == 1) {
					List<User> Users = usersettingback.getReturnData();
					if (Users.size() >= 1) {
						User readuser = Users.get(0);
						useravatar_path = readuser.getAvatar_path();
					}
					completeURL = beginStr + useravatar_path + endStr;
					PreferenceUtils.saveImageCache(TeamSettingActivity.this,
							completeURL);// 存SP
					ImageLoaderUtils.getInstance().LoadImage(
							TeamSettingActivity.this, completeURL,
							teamSetting_head);
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	private void initUI() {
		findViewById(R.id.TeamSetting_back_layout).setOnClickListener(this);// 返回
		findViewById(R.id.TeamSetting_back).setOnClickListener(this);
		findViewById(R.id.TeamSetting_save_layout).setOnClickListener(this);// 保存
		findViewById(R.id.TeamSetting_save).setOnClickListener(this);
		teamSetting_head = (ImageView) findViewById(R.id.TeamSetting_head);// 头像
		teamSetting_number = (TextView) findViewById(R.id.TeamSetting_number);// 必聚号
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.team_setting, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.TeamSetting_back_layout:
		case R.id.TeamSetting_back:
			TeamSetting_back();
			break;
		case R.id.TeamSetting_save_layout:
		case R.id.TeamSetting_save:
			TeamSetting_save();
			break;
		default:
			break;
		}
	}

	private void TeamSetting_save() {

	}

	private void TeamSetting_back() {
		finish();
	}

}
