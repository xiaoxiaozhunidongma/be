package com.example.testhuanxin;

import com.biju.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MorePictureActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_morepicture);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.more_picture, menu);
		return true;
	}

}
