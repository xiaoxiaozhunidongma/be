package com.biju.function;

import com.biju.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

public class AddFriendsActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_buddy);
		initUI();
	}

	private void initUI() {
		findViewById(R.id.addbuddy_findfriends_layout).setOnClickListener(this);//≤È’“∫√”—
		findViewById(R.id.addbuddy_findfriends).setOnClickListener(this);
		findViewById(R.id.addbuddy_back_layout).setOnClickListener(this);//∑µªÿ
		findViewById(R.id.addbuddy_back).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_buddy, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addbuddy_findfriends_layout:
		case R.id.addbuddy_findfriends:
			addbuddy_findfriends();
			break;
		case R.id.addbuddy_back_layout:
		case R.id.addbuddy_back:
			addbuddy_back();
			break;
		default:
			break;
		}
	}

	private void addbuddy_back() {
		finish();
	}

	private void addbuddy_findfriends() {
		Intent intent=new Intent(AddFriendsActivity.this, FindFriendsActivity.class);
		startActivity(intent);
	}

}
