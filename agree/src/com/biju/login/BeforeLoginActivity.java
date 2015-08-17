package com.biju.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.BJ.utils.SdPkUser;
import com.biju.R;

public class BeforeLoginActivity extends Activity implements OnClickListener{
	public static BeforeLoginActivity BeforeLogin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_before_login);
		initUI();
		BeforeLogin=this;
	}

	private void initUI() {
		findViewById(R.id.BeforeLogin_text_login).setOnClickListener(this);
		findViewById(R.id.BeforeLogin_text_registered).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.before_login, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.BeforeLogin_text_login:
			BeforeLogin_text_login();
			break;
		case R.id.BeforeLogin_text_registered:
			BeforeLogin_text_registered();
			break;
		default:
			break;
		}
	}

	private void BeforeLogin_text_registered() {
		Intent intent=new Intent(BeforeLoginActivity.this, LoginJumpActivity.class);
		intent.putExtra("registered", 1);
		startActivity(intent);
	}

	private void BeforeLogin_text_login() {
		Intent intent=new Intent(BeforeLoginActivity.this, LoginJumpActivity.class);
		startActivity(intent);
		
	}
}
