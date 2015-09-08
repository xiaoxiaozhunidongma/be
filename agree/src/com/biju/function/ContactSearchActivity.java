package com.biju.function;

import com.biju.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ContactSearchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_search);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact_search, menu);
		return true;
	}

}
