package com.biju.function;

import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.BJ.javabean.Group;
import com.BJ.javabean.Group_Code;
import com.BJ.javabean.Groupback;
import com.BJ.utils.SdPkUser;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.useRequestCode2JoinListenner;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;

public class RequestCode2Activity extends Activity implements OnClickListener {

	private Interface requestcode_interface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_code);
		initUI();
		initInterface();
	}

	private void initInterface() {
		requestcode_interface = Interface.getInstance();
		requestcode_interface.setPostListener(new useRequestCode2JoinListenner() {

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
						SdPkUser.setGetgroup(readhomeuser);
						SharedPreferences requestcode2_sp=getSharedPreferences(IConstant.IsRefresh, 0);
						Editor editor=requestcode2_sp.edit();
						editor.putBoolean(IConstant.IsCode2, true);
						editor.commit();
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
		findViewById(R.id.Requestcode2_OK_layout).setOnClickListener(this);
		findViewById(R.id.Requestcode2_OK).setOnClickListener(this);
		findViewById(R.id.Requestcode2_verifyCodeView).setOnClickListener(this);
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
		case R.id.Requestcode2_OK_layout:
		case R.id.Requestcode2_OK:
			Requestcode2_OK();
			break;
		case R.id.Requestcode2_verifyCodeView:
			Requestcode2_verifyCodeView();
			break;
		default:
			break;
		}
	}

	private void Requestcode2_verifyCodeView() {
		
	}

	private void Requestcode2_OK() {
		String code = SdPkUser.getCode;
		Group_Code group_Code = new Group_Code();
		group_Code.setPk_group_code(code);
		requestcode_interface.useRequestCode2Join(RequestCode2Activity.this,
				group_Code);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return super.onTouchEvent(event);
	}
}
