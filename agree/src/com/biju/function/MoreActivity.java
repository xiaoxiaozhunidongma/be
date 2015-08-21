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
import android.widget.Toast;

import com.BJ.javabean.Moreback;
import com.BJ.javabean.Party;
import com.BJ.javabean.Party2;
import com.BJ.javabean.UserAllParty;
import com.BJ.utils.RefreshActivity;
import com.BJ.utils.SdPkUser;
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
	private Integer sD_pk_user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		sD_pk_user = SdPkUser.getsD_pk_user();
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
					if(userAll)
					{
						finish();
						for (int i = 0; i < RefreshActivity.activList_1.size(); i++) {
							RefreshActivity.activList_1.get(i).finish();
						}
					}else
					{
						Intent intent=new Intent(MoreActivity.this, GroupActivity.class);
						startActivity(intent);
					}
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
			Integer fk_user=userAllParty.getFk_user();
			Log.e("MoreActivity", "当前的fk_user11111========="+fk_user);
			Log.e("MoreActivity", "当前的sD_pk_user11111111========="+sD_pk_user);
			if(String.valueOf(fk_user).equals(String.valueOf(sD_pk_user)))
			{
				more_party.setPk_party(userAllParty.getPk_party());
				moreinterface.userCancleParty(MoreActivity.this, more_party);
			}else
			{
				Toast.makeText(MoreActivity.this, "你没有删除该日程的权限", Toast.LENGTH_SHORT).show();
			}
		}else
		{
			Integer fk_user=moreparty.getFk_user();
			Log.e("MoreActivity", "当前的fk_user2222222========="+fk_user);
			Log.e("MoreActivity", "当前的sD_pk_user222222222========="+sD_pk_user);
			if(String.valueOf(fk_user).equals(String.valueOf(sD_pk_user)))
			{
				more_party.setPk_party(moreparty.getPk_party());
				moreinterface.userCancleParty(MoreActivity.this, more_party);
			}else
			{
				Toast.makeText(MoreActivity.this, "你没有删除该日程的权限", Toast.LENGTH_SHORT).show();
			}
		}
	}

}
