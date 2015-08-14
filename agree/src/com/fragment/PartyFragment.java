package com.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BJ.javabean.User;
import com.BJ.javabean.UserAllParty;
import com.BJ.javabean.UserAllPartyback;
import com.biju.Interface;
import com.biju.Interface.readUserAllPartyListenner;
import com.biju.R;
import com.biju.function.NewPartyActivity;
import com.biju.function.PartyDetailsActivity;
import com.biju.login.LoginActivity;
import com.github.volley_examples.utils.GsonUtils;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class PartyFragment extends Fragment implements OnClickListener {

	private View mLayout;
	private RelativeLayout mTab_party_prompt_layout;
	private RelativeLayout mTab_party_list_layout;
	private int returndata;
	private boolean isRegistered_one;
	private boolean login;
	private Interface tab_party_interface;
	private PullToRefreshListView mPull_refresh_list;
	private ListView mParty_listView;
	private MyAdapter adapter;
	private ArrayList<UserAllParty> userAllPartieList = new ArrayList<UserAllParty>();

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

	@Override
	public void onStart() {
		initParty();
		adapter.notifyDataSetChanged();
		super.onStart();
	}

	private void initInterface() {
		tab_party_interface = Interface.getInstance();
		tab_party_interface.setPostListener(new readUserAllPartyListenner() {

			@Override
			public void success(String A) {
				userAllPartieList.clear();
				UserAllPartyback allPartyback = GsonUtils.parseJson(A,
						UserAllPartyback.class);
				int status = allPartyback.getStatusMsg();
				if (status == 1) {
					Log.e("PartyFragment", "读取出用户的所有日程====" + A);
					List<UserAllParty> AllList = allPartyback.getReturnData();
					if (AllList.size() > 0) {
						for (int i = 0; i < AllList.size(); i++) {
							UserAllParty userAllParty = AllList.get(i);
							userAllPartieList.add(userAllParty);
						}
					}
					adapter.notifyDataSetChanged();
					if (userAllPartieList.size() > 0) {
						mTab_party_prompt_layout.setVisibility(View.GONE);
						mTab_party_list_layout.setVisibility(View.VISIBLE);
					} else {
						mTab_party_prompt_layout.setVisibility(View.VISIBLE);
						mTab_party_list_layout.setVisibility(View.GONE);
					}
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	private void initParty() {
		User partyuser = new User();
		if (isRegistered_one) {
			partyuser.setPk_user(returndata);
		} else {
			if (login) {
				int pk_user = LoginActivity.getPk_user();
				partyuser.setPk_user(pk_user);
				Log.e("PartyFragment", "pk_user====" + pk_user);
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

		mPull_refresh_list = (PullToRefreshListView) mLayout
				.findViewById(R.id.pull_refresh_list);
		mPull_refresh_list
				.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						new GetDataTask().execute();
					}
				});

		// 下拉刷新动画
		ILoadingLayout iLoadingLayout = mPull_refresh_list
				.getLoadingLayoutProxy();
		iLoadingLayout.setPullLabel("下拉刷新");
		iLoadingLayout.setRefreshingLabel("正在刷新...");
		iLoadingLayout.setReleaseLabel("放开即可刷新");

		mParty_listView = mPull_refresh_list.getRefreshableView();
		adapter = new MyAdapter();
		mParty_listView.setAdapter(adapter);

		mParty_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int pos = arg2 - mParty_listView.getHeaderViewsCount();
				if (pos >= 0) {
					UserAllParty UserAllParty = userAllPartieList.get(pos);
					Intent intent = new Intent(getActivity(),
							PartyDetailsActivity.class);
					intent.putExtra("UserAll", true);
					intent.putExtra("UserAllParty", UserAllParty);
					getActivity().startActivity(intent);
				}
			}
		});
	}

	// 下拉啦刷新异步
	class GetDataTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String mString = "消息来了";
			return mString;
		}

		@Override
		protected void onPostExecute(String result) {
			initParty();
			adapter.notifyDataSetChanged();
			mPull_refresh_list.onRefreshComplete();
			super.onPostExecute(result);
		}
	}

	class ViewHolder {
		TextView years_month;
		TextView address;
		TextView name;
		TextView times;
		TextView inNum;
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return userAllPartieList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View inflater = null;
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater layoutInflater = getActivity()
						.getLayoutInflater();
				inflater = layoutInflater.inflate(R.layout.party_item, null);
				holder.years_month = (TextView) inflater
						.findViewById(R.id.years_month);
				holder.name = (TextView) inflater.findViewById(R.id.name);
				holder.times = (TextView) inflater.findViewById(R.id.times);
				holder.address = (TextView) inflater.findViewById(R.id.address);
				holder.inNum = (TextView) inflater.findViewById(R.id.inNum);
				inflater.setTag(holder);
			} else {
				inflater = convertView;
				holder = (ViewHolder) inflater.getTag();
			}
			if (userAllPartieList.size() > 0) {
				UserAllParty party = userAllPartieList.get(position);
				String time = party.getBegin_time();
				String yuars_month = time.substring(0, 10);
				String years = yuars_month.substring(0, 4);
				String months = yuars_month.substring(5, 7);
				String days = yuars_month.substring(8, 10);
				String times = years + "年" + months + "月" + days + "日";
				String datetimes = time.substring(11, 16);
				holder.years_month.setText(times);
				holder.name.setText(party.getName());
				holder.times.setText(datetimes);
				holder.address.setText(party.getLocation());
				if (party.getInNum() != null) {
					holder.inNum.setText(party.getInNum() + "");
				} else {
					holder.inNum.setText("0");
				}
			}

			return inflater;
		}

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
