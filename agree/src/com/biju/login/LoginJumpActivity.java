package com.biju.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.BJ.utils.RefreshActivity;
import com.BJ.utils.SdPkUser;
import com.biju.R;
import com.biju.APP.MyApplication;
import com.biju.wxapi.WXEntryActivity;
import com.tencent.mm.sdk.modelmsg.SendAuth;

public class LoginJumpActivity extends Activity implements OnClickListener{

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
			mLoginJump_phone.setText("�ֻ�ע��");
			mLoginJump_weixin.setText("΢��ע��");
		}else
		{
			mLoginJump_phone.setText("�ֻ���¼");
			mLoginJump_weixin.setText("΢�ŵ�¼");
		}
		RefreshActivity.activList_3.add(LoginJumpActivity.this);
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
		if(1==registered)
		{
			//��ת΢��ע�����
			final SendAuth.Req req = new SendAuth.Req();
			req.scope = "snsapi_userinfo";
			req.state = "wechat_sdk_demo_test";
			MyApplication.api.sendReq(req);
			
			SdPkUser.setWeixinRegistered(true);
			
		}else
		{
			Log.e("LoginJumpActivity", "�н�����΢�ŵİ�ť");
			//��ת΢�ŵ�¼����
			final SendAuth.Req req = new SendAuth.Req();
			req.scope = "snsapi_userinfo";
			req.state = "wechat_sdk_demo_test";
			MyApplication.api.sendReq(req);
			
			SdPkUser.setGetweixinBinding(false);
		}
	}

	private void LoginJump_phone() {
		if(1==registered)
		{
			//��ת�ֻ�ע�����
			Intent intent=new Intent(LoginJumpActivity.this, PhoneRegisteredActivity.class);
			startActivity(intent);
			
		}else
		{
			//��ת�ֻ���¼����
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
