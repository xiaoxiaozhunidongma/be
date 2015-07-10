package com.biju.function;

import com.biju.R;
import com.biju.R.layout;
import com.biju.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class BigMapActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_big_map);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.big_map, menu);
		return true;
	}

}
