package com.biju.function;

import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.BJ.javabean.Party;
import com.BJ.utils.Weeks;
import com.biju.Interface;
import com.biju.Interface.UserInterface;
import com.biju.R;
import com.biju.login.LoginActivity;

public class OkPartyActivity extends Activity implements OnClickListener {

	private TextView mOkParty_address;
	private TextView mOkParty_time;
	private TextView mOkParty_feedback;
	private boolean isFeedback;
	private EditText mOkParty_name;
	private EditText mOkParty_note;
	private int returndata;
	private boolean isRegistered_one;
	private boolean login;
	private int fk_group;
	private String address;
	private String isCalendar;
	private int hour;
	private int minute;
	private double mLng;
	private double mLat;
	private Interface okPartyInterface;
	private int y;
	private int m;
	private int d;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ok_party);
		// 得到fk_user
		SharedPreferences sp = getSharedPreferences("Registered", 0);
		isRegistered_one = sp.getBoolean("isRegistered_one", false);
		returndata = sp.getInt("returndata", returndata);
		SharedPreferences sp1 = getSharedPreferences("isLogin", 0);
		login = sp1.getBoolean("Login", false);

		// 得到fk_group
		SharedPreferences sp3 = getSharedPreferences("isParty_fk_group", 0);
		fk_group = sp3.getInt("fk_group", 0);

		initUI();
		initData();
		// 判断反馈设置是否开启
		SharedPreferences sp2 = getSharedPreferences("isFeedback", 0);
		isFeedback = sp2.getBoolean("feedback", false);
		getMyUUID();
		initInterface();
	}

	private void initInterface() {
		okPartyInterface = new Interface();
		okPartyInterface.setPostListener(new UserInterface() {

			@Override
			public void success(String A) {
				Log.e("OkPartyActivity", "日程是否创建成功======" + A);
			}

			@Override
			public void defail(Object B) {
				Log.e("OkPartyActivity", "日程是否创建成功dfsdfds======" + B);
				Log.e("OkPartyActivity", "日程是否创建成功dfsdfds======"
						+ B.toString().length());
				mOkParty_name.postDelayed(new Runnable() {

					@Override
					public void run() {
						finish();
					}
				}, 1000);
			}
		});
	}

	private static String getMyUUID() {
		UUID uuid = UUID.randomUUID();
		String uniqueId = uuid.toString();
		return uniqueId;
	}

	private void initData() {
		// 得到地址和经纬度
		SharedPreferences sp = getSharedPreferences("isParty", 0);
		address = sp.getString("isAddress", "");
		Log.e("OkPartyActivity", "address======"+address);
		float mLng_1 = sp.getFloat("mLng", 0);
		float mLat_1 = sp.getFloat("mLat", 0);
		mLng = mLng_1;
		mLat = mLat_1;
		// 得到年月日和选的时间
		isCalendar = sp.getString("isCalendar", "");
		String years = isCalendar.substring(0, 4);
		String months = isCalendar.substring(5, 7);
		String days = isCalendar.substring(8, 10);
//		String times = years + "年" + months + "月" + days + "日";
		// 计算星期几
		y = Integer.valueOf(years);
		m = Integer.valueOf(months);
		d = Integer.valueOf(days);
		// 调用计算星期几的方法
		Weeks.CaculateWeekDay(y, m, d);
		String week = Weeks.getweek();
		// 得到传过来的时间
		hour = sp.getInt("hour", 0);
		minute = sp.getInt("minute", 0);
		mOkParty_time.setText(years + "年" + months + "月" + days + "日" + " "
				+ week + " " + hour + ":" + minute);
		mOkParty_address.setText(address);
		if (isFeedback) {
			mOkParty_feedback.setText("关闭");
		} else {
			mOkParty_feedback.setText("开启");
		}
	}

	private void initUI() {
		mOkParty_address = (TextView) findViewById(R.id.OkParty_address);// 显示地址
		mOkParty_time = (TextView) findViewById(R.id.OkParty_time);// 显示年月日和时间
		mOkParty_feedback = (TextView) findViewById(R.id.OkParty_feedback);// 反馈设置
		mOkParty_feedback.setOnClickListener(this);
		mOkParty_name = (EditText) findViewById(R.id.OkParty_name);// 聚会名称
		mOkParty_note = (EditText) findViewById(R.id.OkParty_note);// 聚会备注
		findViewById(R.id.OkParty_complete_layout).setOnClickListener(this);
		findViewById(R.id.OkParty_complete).setOnClickListener(this);// 完成
		findViewById(R.id.OkParty_back_layout).setOnClickListener(this);// 返回
		findViewById(R.id.OkParty_back).setOnClickListener(this);
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
		case R.id.OkParty_complete_layout:
		case R.id.OkParty_complete:
			OkParty_complete();
			break;
		case R.id.OkParty_back_layout:
		case R.id.OkParty_back:
			OkParty_back();
			break;
		default:
			break;
		}
	}

	private void OkParty_back() {
		finish();
		Intent intent = new Intent(OkPartyActivity.this, TimeActivity.class);
		startActivity(intent);
	}

	private void OkParty_complete() {
		String pk_party = OkPartyActivity.getMyUUID();
		String name = mOkParty_name.getText().toString().trim();
		String remark = mOkParty_note.getText().toString().trim();

		Party party = new Party();
		party.setPk_party(pk_party);
		party.setFk_group(fk_group);
		if (isRegistered_one) {
			party.setFk_user(returndata);
			Log.e("OkPartyActivity", "新建日程的returndata=====" + returndata);
		} else {
			if (login) {
				int pk_user = LoginActivity.pk_user;
				party.setFk_user(pk_user);
				Log.e("OkPartyActivity", "新建日程的pk_user=====" + pk_user);
			} else {
				party.setFk_user(returndata);
			}
		}
		party.setName(name);
		party.setRemark(remark);
		party.setBegin_time(isCalendar + "   " + hour + ":" + minute);
		party.setLongitude(mLng);
		party.setLatitude(mLat);
		party.setLocation(address);
		party.setStatus(1);
		Log.e("OkPartyActivity", "新建日程的UUID=====" + pk_party);
		Log.e("OkPartyActivity", "新建日程的name=====" + name);
		Log.e("OkPartyActivity", "新建日程的remark=====" + remark);
		Log.e("OkPartyActivity", "新建日程的fk_group=====" + fk_group);
		Log.e("OkPartyActivity", "新建日程的时间=====" + isCalendar + "    " + hour
				+ ":" + minute);
		Log.e("OkPartyActivity", "新建日程的mLng=====" + mLng);
		Log.e("OkPartyActivity", "新建日程的mLat=====" + mLat);
		Log.e("OkPartyActivity", "新建日程的地址=====" + address);
		okPartyInterface.addParty(OkPartyActivity.this, party);
	}

	private void OkParty_feedback() {
		isFeedback = !isFeedback;
		if (isFeedback) {
			mOkParty_feedback.setText("关闭");
		} else {
			mOkParty_feedback.setText("开启");
		}
	}

	@Override
	protected void onStop() {
		SharedPreferences sp = getSharedPreferences("isFeedback", 0);
		Editor editor = sp.edit();
		editor.putBoolean("feedback", isFeedback);
		editor.commit();
		super.onStop();
	}

}
