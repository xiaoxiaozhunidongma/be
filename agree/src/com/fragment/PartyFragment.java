package com.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.BJ.javabean.User;
import com.biju.Interface;
import com.biju.Interface.UserInterface;
import com.biju.R;
import com.biju.function.NewPartyActivity;
import com.biju.login.LoginActivity;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class PartyFragment extends Fragment implements OnClickListener {

	private View mLayout;
	private RelativeLayout mTab_party_prompt_layout;
	private RelativeLayout mTab_party_list_layout;
	private ListView mTab_party_listView;
	private int returndata;
	private boolean isRegistered_one;
	private boolean login;
	private Interface tab_party_interface;

	public PartyFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayout = inflater.inflate(R.layout.fragment_party, container, false);
		initUI();
		initPk_user();
		initInterface();
		initParty();
		return mLayout;
	}

	private void initInterface() {
		tab_party_interface = new Interface();
		tab_party_interface.setPostListener(new UserInterface() {

			@Override
			public void success(String A) {
				Log.e("PartyFragment", "读取出用户的所有日程====" + A);
			}

			@Override
			public void defail(Object B) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void initParty() {
		User partyuser = new User();
		if (isRegistered_one) {
			partyuser.setPk_user(returndata);
		} else {
			if (login) {
				int pk_user = LoginActivity.pk_user;
				partyuser.setPk_user(pk_user);
				Log.e("PartyFragment", "pk_user===="+pk_user);
			} else {
				partyuser.setPk_user(returndata);
			}
		}
		tab_party_interface.readUserAllParty(getActivity(), partyuser);
	}

	private void initPk_user() {
		// 得到pk_user
		SharedPreferences sp = getActivity().getSharedPreferences("Registered",
				0);
		isRegistered_one = sp.getBoolean("isRegistered_one", false);
		returndata = sp.getInt("returndata", returndata);
		SharedPreferences sp1 = getActivity()
				.getSharedPreferences("isLogin", 0);
		login = sp1.getBoolean("Login", false);
	}

	private void initUI() {
		mLayout.findViewById(R.id.tab_party_new_layout)
				.setOnClickListener(this);// 新建
		mLayout.findViewById(R.id.tab_party_new).setOnClickListener(this);
		mTab_party_prompt_layout = (RelativeLayout) mLayout
				.findViewById(R.id.tab_party_prompt_layout);// 提示布局
		mTab_party_list_layout = (RelativeLayout) mLayout
				.findViewById(R.id.tab_party_list_layout);// listview布局
		mTab_party_listView = (ListView) mLayout
				.findViewById(R.id.tab_party_listView);// listview

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab_party_new_layout:
		case R.id.tab_party_new:
			tab_party_new();
			break;

		default:
			break;
		}
	}

	private void tab_party_new() {
		Intent intent = new Intent(getActivity(), NewPartyActivity.class);
		startActivity(intent);
	}

}
