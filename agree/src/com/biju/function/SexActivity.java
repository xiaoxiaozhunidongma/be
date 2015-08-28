package com.biju.function;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;

import com.BJ.javabean.User;
import com.BJ.javabean.updateback;
import com.BJ.utils.SdPkUser;
import com.biju.Interface;
import com.biju.R;
import com.biju.Interface.updateUserListenner;
import com.github.volley_examples.utils.GsonUtils;

public class SexActivity extends Activity implements OnClickListener{

	private int sex;
	private Interface sexInterface;
	private Integer sD_pk_user;
	private User user;
	private RelativeLayout mSet_man;
	private RelativeLayout mSet_woman;
	private RelativeLayout mSet_other;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sex);
		sD_pk_user = SdPkUser.getsD_pk_user();
		user = SdPkUser.getSexUser;
		sex=user.getSex();
		initUI();
		initInterface();
	}

	private void initInterface() {
		sexInterface = Interface.getInstance();
		sexInterface.setPostListener(new updateUserListenner() {

			@Override
			public void success(String A) {
				// 更新用户资料成功
				updateback usersetting_updateback = GsonUtils.parseJson(A,
						updateback.class);
				int a = usersetting_updateback.getStatusMsg();
				if (a == 1) {
					Log.e("SexActivity", "更新成功" + A);
					finish();
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
		
	}

	private void initUI() {
		findViewById(R.id.Set_back).setOnClickListener(this);
		findViewById(R.id.Set_back_layout).setOnClickListener(this);//返回
		findViewById(R.id.Set_OK).setOnClickListener(this);
		findViewById(R.id.Set_OK_layout).setOnClickListener(this);//完成
		mSet_man = (RelativeLayout) findViewById(R.id.Set_man);
		mSet_man.setOnClickListener(this);//男
		mSet_woman = (RelativeLayout) findViewById(R.id.Set_woman);
		mSet_woman.setOnClickListener(this);//女
		mSet_other = (RelativeLayout) findViewById(R.id.Set_other);
		mSet_other.setOnClickListener(this);//其他
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sex, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Set_back:
		case R.id.Set_back_layout:
			Set_back();
			break;
		case R.id.Set_OK:
		case R.id.Set_OK_layout:
			Set_OK();
			break;
		case R.id.Set_man:
			Set_man();
			break;
		case R.id.Set_woman:
			Set_woman();
			break;
		case R.id.Set_other:
			Set_other();
			break;
		default:
			break;
		}
	}

	//其他
	private void Set_other() {
		sex=0;
		mSet_man.setBackgroundResource(R.color.white);
		mSet_woman.setBackgroundResource(R.color.white);
		mSet_other.setBackgroundResource(R.color.lightgray1);
	}

	//女
	private void Set_woman() {
		sex=2;
		mSet_man.setBackgroundResource(R.color.white);
		mSet_woman.setBackgroundResource(R.color.lightgray1);
		mSet_other.setBackgroundResource(R.color.white);
	}

	//男
	private void Set_man() {
		sex=1;
		mSet_man.setBackgroundResource(R.color.lightgray1);
		mSet_woman.setBackgroundResource(R.color.white);
		mSet_other.setBackgroundResource(R.color.white);
	}

	//完成
	private void Set_OK() {
		User usersetting = new User();
		usersetting.setPk_user(sD_pk_user);
		usersetting.setJpush_id(user.getJpush_id());
		usersetting.setNickname(user.getNickname());
		usersetting.setPassword(user.getPassword());
		usersetting.setSex(sex);
		usersetting.setStatus(1);
		usersetting.setPhone(user.getPhone());
		usersetting.setWechat_id(user.getWechat_id());
		usersetting.setSetup_time(user.getSetup_time());
		usersetting.setLast_login_time(user.getLast_login_time());
		usersetting.setAvatar_path(user.getAvatar_path());
		sexInterface.updateUser(SexActivity.this, usersetting);
	}
	//返回
	private void Set_back() {
		finish();
	}

}
