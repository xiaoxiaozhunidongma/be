package com.biju.login;

import com.biju.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class LoginJumpActivity extends Activity implements OnClickListener{

	public static LoginJumpActivity LoginJump;
	private TextView mLoginJump_phone;
	private TextView mLoginJump_weixin;
	private int registered;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_jump);
		Intent intent = getIntent();
		registered = intent.getIntExtra("registered", 0);
		initUI();
		if(1==registered)
		{
			mLoginJump_phone.setText("手机注册");
			mLoginJump_weixin.setText("微信注册");
		}else
		{
			mLoginJump_phone.setText("手机登录");
			mLoginJump_weixin.setText("微信登录");
		}
		
		LoginJump=this;
	}

	private void initUI() {
		findViewById(R.id.LoginJump_phone_layout).setOnClickListener(this);
		mLoginJump_phone = (TextView) findViewById(R.id.LoginJump_phone);
		mLoginJump_phone.setOnClickListener(this);
		
		findViewById(R.id.LoginJump_weixin_layout).setOnClickListener(this);
		mLoginJump_weixin = (TextView) findViewById(R.id.LoginJump_weixin);
		mLoginJump_weixin.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_jump, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.LoginJump_phone_layout:
		case R.id.LoginJump_phone:
			LoginJump_phone();
			break;
		case R.id.LoginJump_weixin_layout:
		case R.id.LoginJump_weixin:
			LoginJump_weixin();
			break;
		default:
			break;
		}
	}

	private void LoginJump_weixin() {
		// TODO Auto-generated method stub
		
	}

	private void LoginJump_phone() {
		if(1==registered)
		{
			//跳转手机注册界面
			Intent intent=new Intent(LoginJumpActivity.this, PhoneRegisteredActivity.class);
			startActivity(intent);
			
		}else
		{
			//跳转手机登录界面
			Intent intent=new Intent(LoginJumpActivity.this, PhoneLoginActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return super.onTouchEvent(event);
	}
}
