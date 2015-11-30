package com.biju.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.BJ.utils.RefreshActivity;
import com.biju.R;

public class BeforeLoginActivity extends Activity implements OnClickListener {

	private boolean exit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_before_login);
		initUI();
		RefreshActivity.activList_3.add(BeforeLoginActivity.this);
	}

	private void initUI() {
		findViewById(R.id.BeforeLogin_text_login).setOnClickListener(this);
		findViewById(R.id.BeforeLogin_text_registered).setOnClickListener(this);
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
		Intent intent = new Intent(BeforeLoginActivity.this,LoginJumpActivity.class);
		intent.putExtra("registered", 1);
		startActivity(intent);
	}

	private void BeforeLogin_text_login() {
		Intent intent = new Intent(BeforeLoginActivity.this,
				LoginJumpActivity.class);
		startActivity(intent);

	}
}
