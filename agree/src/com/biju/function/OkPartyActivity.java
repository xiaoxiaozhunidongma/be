package com.biju.function;

import com.biju.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class OkPartyActivity extends Activity implements OnClickListener{

	private TextView mOkParty_address;
	private TextView mOkParty_time;
	private TextView mOkParty_feedback;
	private boolean isFeedback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ok_party);
		initUI();
		initData();
		SharedPreferences sp=getSharedPreferences("isFeedback", 0);
		boolean feedback=sp.getBoolean("feedback", false);
		if(!feedback)
		{
			mOkParty_feedback.setText("关闭");
		}else
		{
			mOkParty_feedback.setText("开启");
		}
	}

	private void initData() {
		SharedPreferences sp=getSharedPreferences("isParty", 0);
		String address=sp.getString("isAddress", "");
		String isCalendar=sp.getString("isCalendar", "");
		int hour=sp.getInt("hour", 0);
		int minute=sp.getInt("minute", 0);
		
		
		mOkParty_time.setText(isCalendar+"    "+hour+":"+minute);
		mOkParty_address.setText(address);
	}

	private void initUI() {
		mOkParty_address = (TextView) findViewById(R.id.OkParty_address);//显示地址
		mOkParty_time = (TextView) findViewById(R.id.OkParty_time);//显示年月日和时间
		mOkParty_feedback = (TextView) findViewById(R.id.OkParty_feedback);//反馈设置
		mOkParty_feedback.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ok_party, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.OkParty_feedback:
			OkParty_feedback();
			break;

		default:
			break;
		}
	}

	private void OkParty_feedback() {
		isFeedback=!isFeedback;
		if(isFeedback)
		{
			mOkParty_feedback.setText("关闭");
		}else
		{
			mOkParty_feedback.setText("开启");
		}
	}
	
	@Override
	protected void onStop() {
		SharedPreferences sp=getSharedPreferences("isFeedback", 0);
		Editor editor=sp.edit();
		editor.putBoolean("feedback", isFeedback);
		editor.commit();
		super.onStop();
	} 

}
