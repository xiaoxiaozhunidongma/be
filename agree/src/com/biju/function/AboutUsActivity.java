package com.biju.function;

import com.biju.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

public class AboutUsActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		initUI();
	}

	private void initUI() {
		findViewById(R.id.aboutus_back_layout).setOnClickListener(this);
		findViewById(R.id.aboutus_back).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about_us, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.aboutus_back_layout:
		case R.id.aboutus_back:
			aboutus_back();
			break;

		default:
			break;
		}
	}

	private void aboutus_back() {
		finish();
	}

}
