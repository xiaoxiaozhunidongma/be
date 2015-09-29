package com.biju.function;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;

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
	private LinearLayout mMore_creator_layout;
	private LinearLayout mMore_member_layout;
	private Integer fk_user;

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
			fk_user = userAllParty.getFk_user();
		}else
		{
			moreparty = (Party2) intent.getSerializableExtra(IConstant.MoreParty);
			fk_user=moreparty.getFk_user();
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
						SharedPreferences more_sp=getSharedPreferences(IConstant.MoreRefresh, 0);
						Editor editor=more_sp.edit();
						editor.putBoolean(IConstant.Morecancle, true);
						editor.commit();
						Intent intent=new Intent(MoreActivity.this, GroupActivity.class);
						startActivity(intent);
					}
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	private void initUI() {
		findViewById(R.id.More_creator_cancel_layout).setOnClickListener(this);//取消聚会
		findViewById(R.id.More_creator_cancel).setOnClickListener(this);
		mMore_creator_layout = (LinearLayout) findViewById(R.id.More_creator_layout);//创建者
		mMore_member_layout = (LinearLayout) findViewById(R.id.More_member_layout);//普通成员
		if(String.valueOf(fk_user).equals(String.valueOf(sD_pk_user)))
		{
			mMore_creator_layout.setVisibility(View.VISIBLE);
			mMore_member_layout.setVisibility(View.GONE);
		}else
		{
			mMore_creator_layout.setVisibility(View.GONE);
			mMore_member_layout.setVisibility(View.VISIBLE);
		}
		
		findViewById(R.id.More_title_cancel_layout).setOnClickListener(this);
		findViewById(R.id.More_title_cancel).setOnClickListener(this);//取消
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.more, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.More_creator_cancel_layout:
		case R.id.More_creator_cancel:
			more_cancel_layout();
			break;
		case R.id.More_title_cancel_layout:
		case R.id.More_title_cancel:
			More_title_cancel();
			break;
		default:
			break;
		}
	}

	private void More_title_cancel() {
		finish();
	}

	private void more_cancel_layout() {
		Party more_party = new Party();
		if(userAll)
		{
			more_party.setPk_party(userAllParty.getPk_party());
			moreinterface.userCancleParty(MoreActivity.this, more_party);
		}else
		{
			more_party.setPk_party(moreparty.getPk_party());
			moreinterface.userCancleParty(MoreActivity.this, more_party);
		}
	}

}
