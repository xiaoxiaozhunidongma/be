package com.biju.function;

import com.biju.R;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class RequestCodeActivity extends Activity implements OnClickListener{

	private EditText mRequest_edt_code;
	private TextView mRequest_tv_code;
	private TextView mRequest_OK;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_code);
		initUI();
	}

	private void initUI() {
		mRequest_tv_code = (TextView) findViewById(R.id.request_tv_code);
		mRequest_tv_code.setOnClickListener(this);
		mRequest_edt_code = (EditText) findViewById(R.id.request_edt_code);//输入验证码
		mRequest_OK = (TextView) findViewById(R.id.request_OK);//完成
		mRequest_OK.setOnClickListener(this);
		mRequest_edt_code.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if("".equals(s.toString()))
				{
					mRequest_OK.setVisibility(View.GONE);
				}else
				{
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
