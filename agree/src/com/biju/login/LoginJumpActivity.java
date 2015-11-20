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
import android.widget.Toast;

import com.BJ.utils.RefreshActivity;
import com.BJ.utils.SdPkUser;
import com.biju.R;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class LoginJumpActivity extends Activity implements OnClickListener{

	private TextView mLoginJump_phone;
	private TextView mLoginJump_weixin;
	private int registered;
	
	 public static IWXAPI api;
	
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
			if(api==null)
			{
				api = WXAPIFactory.createWXAPI(this, "wx2ffba147560de2ff", false);
			}
			
			if (!api.isWXAppInstalled()) {
	            //�����û�û�а���΢��
				Toast.makeText(LoginJumpActivity.this, "��û�а�װ΢��,���Ȱ�װ΢��!", Toast.LENGTH_SHORT).show();
	            return;
	        }
			
			api.registerApp("wx2ffba147560de2ff");
			
			//��ת΢��ע�����
			final SendAuth.Req req = new SendAuth.Req();
			req.scope = "snsapi_userinfo";
			req.state = "agree_weixin_login";
			api.sendReq(req);
			
			SdPkUser.setWeixinRegistered(true);
			SdPkUser.setGetWeSource(false);//˵����΢��ע���ȥ��
		}else
		{
			Log.e("LoginJumpActivity", "�н�����΢�ŵİ�ť");
			if(api==null)
			{
				api = WXAPIFactory.createWXAPI(this, "wx2ffba147560de2ff", false);
				Log.e("LoginJumpActivity", "�н�����΢�ŵİ�ť1111111111");
			}
			
			if (!api.isWXAppInstalled()) {
				Log.e("LoginJumpActivity", "�н�����΢�ŵİ�ť2222222222222");
	            //�����û�û�а���΢��
				Toast.makeText(LoginJumpActivity.this, "��û�а�װ΢��,���Ȱ�װ΢��!", Toast.LENGTH_SHORT).show();
	            return;
	        }
			
			api.registerApp("wx2ffba147560de2ff");
			//��ת΢�ŵ�¼����
			final SendAuth.Req req = new SendAuth.Req();
			req.scope = "snsapi_userinfo";
			req.state = "agree_weixin_login";
			api.sendReq(req);
//			Log.e("LoginJumpActivity", "��ʱ��ķ���ֵsendReq========"+sendReq);
			SdPkUser.setGetweixinBinding(false);
			SdPkUser.setWeixinRegistered(true);
			SdPkUser.setGetWeSource(false);//˵����΢�ŵ�¼��ȥ��
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
