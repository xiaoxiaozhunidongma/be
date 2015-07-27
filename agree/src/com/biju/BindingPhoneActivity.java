package com.biju;

import com.BJ.javabean.User;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biju.Interface.requestVerCodeListenner;

public class BindingPhoneActivity extends Activity implements OnClickListener{

	private User user;
	private EditText mBinding_phone_phone;
	private RelativeLayout mBinding_phone_before;
	private RelativeLayout mBinding_phone_after;
	private EditText mBinding_phone_code;
	private TextView mBinding_phone_send;
	private Interface mBinding_phone_interface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_binding_phone);
		Intent intent = getIntent();
		user = (User) intent.getSerializableExtra("UserData");
		initUI();
		initInterface();
	}

	private void initInterface() {
		mBinding_phone_interface = Interface.getInstance();
		mBinding_phone_interface.setPostListener(new requestVerCodeListenner() {
			
			@Override
			public void success(String A) {
				Log.e("BindingPhoneActivity", "返回验证码是否发送成功======"+A);
			}
			
			@Override
			public void defail(Object B) {
			}
		});
		
	}

	private void initUI() {
		mBinding_phone_phone = (EditText) findViewById(R.id.binding_phone_phone);//输入手机号
		mBinding_phone_phone.setInputType(EditorInfo.TYPE_CLASS_PHONE);//点击电话号码时直接弹出数字键盘
		mBinding_phone_send = (TextView) findViewById(R.id.binding_phone_send);
		mBinding_phone_send.setOnClickListener(this);//发送验证码
		mBinding_phone_before = (RelativeLayout) findViewById(R.id.binding_phone_before);//发送前
		mBinding_phone_after = (RelativeLayout) findViewById(R.id.binding_phone_after);//发送后
		mBinding_phone_code = (EditText) findViewById(R.id.binding_phone_code);//输入验证码
		findViewById(R.id.binding_phone_OK).setOnClickListener(this);//完成验证
		
		mBinding_phone_phone_listener();
	}

	private String phone;
	private void mBinding_phone_phone_listener() {
		mBinding_phone_phone.addTextChangedListener(new TextWatcher() {
			

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				phone = s.toString();
				if("".equals(phone))
				{
					mBinding_phone_send.setEnabled(false);
					mBinding_phone_send.setTextColor(Color.GRAY);
				}else
				{
					mBinding_phone_send.setEnabled(true);
					mBinding_phone_send.setTextColor(Color.RED);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.binding_phone, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.binding_phone_send:
			binding_phone_send();
			break;

		default:
			break;
		}
	}

	private void binding_phone_send() {
		if("".equals(phone))
		{
			Toast.makeText(BindingPhoneActivity.this, "请先输入电话号码", Toast.LENGTH_SHORT).show();
		}else
		{
			mBinding_phone_interface.requestVerCode(BindingPhoneActivity.this, new User());
		}
	}

}
