package com.biju.function;

import com.biju.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class FriendsDataActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends_data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friends_data, menu);
		return true;
	}

}
