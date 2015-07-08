package com.biju.function;

import com.BJ.javabean.Party;
import com.BJ.javabean.Party_User;
import com.biju.Interface;
import com.biju.Interface.UserInterface;
import com.biju.login.LoginActivity;
import com.biju.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class PartyDetailsActivity extends Activity implements OnClickListener {

	private TextView mPartyDetails_name;
	private TextView mPartyDetails_time;
	private TextView mPartyDetails_tv_partake;
	private TextView mPartyDetails_tv_refuse;
	private TextView mPartyDetails_did_not_say;
	private TextView mPartyDetails_partake;
	private TextView mPartyDetails_refuse;
	private int returndata;
	private boolean isRegistered_one;
	private boolean login;
	private int fk_group;
	private Interface readpartyInterface;
	private boolean iscreateParty;
	private boolean isreadparty;
	private String uUID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_party_details);
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
		initOneParty();
		initInterface();
//		initcreatePartyRelation(uUID);
		initReadParty(uUID);
	}

	private void initcreatePartyRelation(String pk_party) {
		isreadparty=false;
		Party_User readuserparty = new Party_User();
		readuserparty.setFk_party(pk_party);
		if(isRegistered_one)
		{
			readuserparty.setFk_user(returndata);
		}else
		{
			if(login)
			{
				int pk_user = LoginActivity.pk_user;
				readuserparty.setFk_user(pk_user);
			}else
			{
				readuserparty.setFk_user(returndata);
			}
		}
		readuserparty.setType(1);
		readuserparty.setStatus(1);
		readpartyInterface.createPartyRelation(PartyDetailsActivity.this, readuserparty);
	}

	private void initInterface() {
		readpartyInterface = new Interface();
		readpartyInterface.setPostListener(new UserInterface() {

			@Override
			public void success(String A) {
				if(isreadparty)
				{
					Log.e("PartyDetailsActivity", "返回的用户参与信息"+A);
					
				}else
				{
					Log.e("PartyDetailsActivity", "返回新建立的聚会关系"+A);
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	private void initReadParty(String pk_party) {
		isreadparty=true;
		Party readparty = new Party();
		readparty.setPk_party(pk_party);
		if(isRegistered_one)
		{
			readparty.setFk_user(returndata);
		}else
		{
			if(login)
			{
				int pk_user = LoginActivity.pk_user;
				readparty.setFk_user(pk_user);
			}else
			{
				readparty.setFk_user(returndata);
			}
		}
		readparty.setFk_group(fk_group);
		readparty.setStatus(1);
		readpartyInterface.readPartyJoinMsg(PartyDetailsActivity.this, readparty);
		initcreatePartyRelation(uUID);
	}

	private void initOneParty() {
		Intent intent = getIntent();
		Party oneParty = (Party) intent.getSerializableExtra("oneParty");
		uUID = oneParty.getPk_party();
		mPartyDetails_name.setText(oneParty.getName());
		mPartyDetails_time.setText(oneParty.getBegin_time());
	}

	private void initUI() {
		mPartyDetails_name = (TextView) findViewById(R.id.PartyDetails_name);// 聚会名称
		mPartyDetails_time = (TextView) findViewById(R.id.PartyDetails_time);// 聚会时间
		mPartyDetails_tv_partake = (TextView) findViewById(R.id.PartyDetails_tv_partake);// 参与数量
		mPartyDetails_tv_refuse = (TextView) findViewById(R.id.PartyDetails_tv_refuse);// 拒绝数量
		mPartyDetails_did_not_say = (TextView) findViewById(R.id.PartyDetails_did_not_say);// 未表态
		mPartyDetails_partake = (TextView) findViewById(R.id.PartyDetails_partake);// 选择参与
		mPartyDetails_partake.setOnClickListener(this);
		mPartyDetails_refuse = (TextView) findViewById(R.id.PartyDetails_refuse);// 选择拒绝
		mPartyDetails_refuse.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.party_details, menu);
		return true;
	}

	@Override
	public void onClick(View v) {

	}

}
