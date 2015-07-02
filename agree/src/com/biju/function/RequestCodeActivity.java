package com.biju.function;

import java.util.List;

import com.BJ.javabean.Group;
import com.BJ.javabean.Group_Code;
import com.BJ.javabean.Groupback;
import com.biju.Interface;
import com.biju.Interface.UserInterface;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class RequestCodeActivity extends Activity implements OnClickListener {

	private EditText mRequest_edt_code;
	private TextView mRequest_tv_code;
	private TextView mRequest_OK;
	private Interface requestcode_interface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_code);
		initUI();
		initInterface();
	}

	private void initInterface() {
		requestcode_interface = new Interface();
		requestcode_interface.setPostListener(new UserInterface() {

			@Override
			public void success(String A) {
				Log.e("RequestCodeActivity", "使用邀请码加入的小组" + A);
				Groupback requestcode = GsonUtils.parseJson(A, Groupback.class);
				int StatusMsg = requestcode.getStatusMsg();
				if (StatusMsg == 1) {
					List<Group> users = requestcode.getReturnData();
					if(users.size()>0)
					{
						Group readhomeuser = users.get(0);
						Intent intent=new Intent();
						intent.setAction("isRefresh");
						intent.putExtra("isCode", true);
						intent.putExtra("readhomeuser", readhomeuser);
						sendBroadcast(intent);
						finish();
					}
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	private void initUI() {
		mRequest_tv_code = (TextView) findViewById(R.id.request_tv_code);
		mRequest_tv_code.setOnClickListener(this);
		mRequest_edt_code = (EditText) findViewById(R.id.request_edt_code);// 输入验证码
		mRequest_OK = (TextView) findViewById(R.id.request_OK);// 完成
		mRequest_OK.setOnClickListener(this);
		mRequest_edt_code.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if ("".equals(s.toString())) {
					mRequest_OK.setVisibility(View.GONE);
				} else {
					mRequest_OK.setVisibility(View.VISIBLE);
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
		getMenuInflater().inflate(R.menu.request_code, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.request_tv_code:
			request_tv_code();
			break;
		case R.id.request_OK:
			request_OK();
			break;
		default:
			break;
		}
	}

	private void request_OK() {
		String pk_group_code = mRequest_edt_code.getText().toString().trim();
		Group_Code group_Code = new Group_Code();
		group_Code.setPk_group_code(pk_group_code);
		requestcode_interface.useRequestCode2Join(RequestCodeActivity.this,
				group_Code);

	}

	private void request_tv_code() {
		mRequest_tv_code.setVisibility(View.GONE);
		mRequest_edt_code.setVisibility(View.VISIBLE);
		mRequest_OK.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return super.onTouchEvent(event);
	}
}
