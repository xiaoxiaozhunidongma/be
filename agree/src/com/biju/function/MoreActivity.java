package com.biju.function;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.BJ.javabean.Moreback;
import com.BJ.javabean.Party;
import com.BJ.javabean.Party2;
import com.BJ.javabean.UserAllParty;
import com.BJ.utils.RefreshActivity;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.userCanclePartyListenner;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;

public class MoreActivity extends Activity implements OnClickListener {

	private Party2 moreparty;
	private Interface moreinterface;
	private UserAllParty userAllParty;
	private boolean userAll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		Intent intent = getIntent();
		userAll = intent.getBooleanExtra(IConstant.UserAll, false);
		if(userAll)
		{
			userAllParty = (UserAllParty) intent.getSerializableExtra(IConstant.UserAllUoreParty);
		}else
		{
			moreparty = (Party2) intent.getSerializableExtra(IConstant.MoreParty);
		}
		initUI();
		initInterface();
	}

	private void initInterface() {
		moreinterface =Interface.getInstance();
		moreinterface.setPostListener(new userCanclePartyListenner() {

			@Override
			public void success(String A) {
				Moreback moreback = GsonUtils.parseJson(A, Moreback.class);
				Integer status = moreback.getStatusMsg();
				if (status == 1) {
					Log.e("MoreActivity", "返回是否删除成功" + A);
					Intent intent=new Intent(MoreActivity.this, GroupActivity.class);
					startActivity(intent);
				}
			}

			@Override
			public void defail(Object B) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void initUI() {
		findViewById(R.id.more_cancel_layout).setOnClickListener(this);
		findViewById(R.id.more_cancel).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.more, menu);
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return super.onTouchEvent(event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.more_cancel_layout:
		case R.id.more_cancel:
			more_cancel_layout();
			break;

		default:
			break;
		}
	}

	private void more_cancel_layout() {
		Party more_party = new Party();
		if(userAll)
		{
			more_party.setPk_party(userAllParty.getPk_party());
		}else
		{
			more_party.setPk_party(moreparty.getPk_party());
		}
		moreinterface.userCancleParty(MoreActivity.this, more_party);
	}

}
