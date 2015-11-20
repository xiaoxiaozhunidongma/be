package com.biju.pay;

import java.util.Timer;
import java.util.TimerTask;

import com.biju.IConstant;
import com.biju.R;
import com.biju.function.NewteamActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class LimitNumberActivity extends Activity implements OnClickListener{

	private EditText mLimitNumberEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_limit_number);
		initUI();
	}

	private void initUI() {
		findViewById(R.id.LimitNumberBackLayout).setOnClickListener(this);
		findViewById(R.id.LimitNumberBack).setOnClickListener(this);
		findViewById(R.id.LimitNumberOKLayout).setOnClickListener(this);
		findViewById(R.id.LimitNumberOK).setOnClickListener(this);
		mLimitNumberEditText = (EditText) findViewById(R.id.LimitNumberEditText);
		mLimitNumberEditText.setOnClickListener(this);
		mLimitNumberEditText.setInputType(EditorInfo.TYPE_CLASS_PHONE);// 点击电话号码时直接弹出数字键盘
		mLimitNumberEditText.setFocusable(true);
		mLimitNumberEditText.setFocusableInTouchMode(true);
		mLimitNumberEditText.requestFocus();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				InputMethodManager inputManager = (InputMethodManager) mLimitNumberEditText
						.getContext().getSystemService(NewteamActivity.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(mLimitNumberEditText, 0);
			}
		}, 998);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.LimitNumberBackLayout:
		case R.id.LimitNumberBack:
			LimitNumberBack();
			break;
		case R.id.LimitNumberOKLayout:
		case R.id.LimitNumberOK:
			LimitNumberOK();
			break;
		default:
			break;
		}
	}

	private void LimitNumberOK() {
		String limitnumber=mLimitNumberEditText.getText().toString().trim();
		if(!("".equals(limitnumber))){
			SharedPreferences Limit_sp=getSharedPreferences(IConstant.LimitNumber, 0);
			Editor editor=Limit_sp.edit();
			editor.putBoolean(IConstant.IsNumber, true);
			editor.putString(IConstant.Number, limitnumber);
			editor.commit();
		}else {
			SharedPreferences Limit_sp=getSharedPreferences(IConstant.LimitNumber, 0);
			Editor editor=Limit_sp.edit();
			editor.putBoolean(IConstant.IsNumber, true);
			editor.putString(IConstant.Number, "0");
			editor.commit();
		}
		finish();
	}

	private void LimitNumberBack() {
		String limitnumber=mLimitNumberEditText.getText().toString().trim();
		if(!("".equals(limitnumber))){
			SharedPreferences Limit_sp=getSharedPreferences(IConstant.LimitNumber, 0);
			Editor editor=Limit_sp.edit();
			editor.putBoolean(IConstant.IsNumber, true);
			editor.putString(IConstant.Number, limitnumber);
			editor.commit();
		}else {
			SharedPreferences Limit_sp=getSharedPreferences(IConstant.LimitNumber, 0);
			Editor editor=Limit_sp.edit();
			editor.putBoolean(IConstant.IsNumber, true);
			editor.putString(IConstant.Number, "0");
			editor.commit();
		}
		finish();
	}

}
