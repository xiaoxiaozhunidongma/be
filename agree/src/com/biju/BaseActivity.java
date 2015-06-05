package com.biju;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public abstract class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.base, menu);
		return true;
	}

	public abstract void Response();
	public abstract void ErrorResponse();
	
	public abstract void ErrorResponse1();
}
