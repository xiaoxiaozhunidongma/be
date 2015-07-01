package com.biju.function;

import java.util.ArrayList;
import java.util.List;

import com.biju.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TimeActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time);
		initUI();
	}
	


	private void initUI() {
		findViewById(R.id.time_back_layout).setOnClickListener(this);
		findViewById(R.id.time_back).setOnClickListener(this);//返回
		findViewById(R.id.time_next_layout).setOnClickListener(this);//下一步
		findViewById(R.id.time_next).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.time, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.time_back_layout:
		case R.id.time_back:
			time_back();
			break;
		case R.id.time_next_layout:
		case R.id.time_next:
			time_next();
			break;
		default:
			break;
		}
	}

	private void time_next() {
		// TODO Auto-generated method stub
		
	}

	private void time_back() {
		finish();
	}
	
	

}
