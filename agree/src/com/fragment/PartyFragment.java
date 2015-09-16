package com.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.BJ.utils.SdPkUser;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.readUserAllPartyListenner;
import com.biju.R;
import com.biju.function.NewPartyActivity;
import com.biju.function.PartyDetailsActivity;
import com.github.volley_examples.utils.GsonUtils;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
@SuppressLint("InlinedApi")
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class PartyFragment extends Fragment implements OnClickListener,SwipeRefreshLayout.OnRefreshListener {

	private View mLayout;
	private RelativeLayout mTab_party_prompt_layout;
	private Interface tab_party_interface;
	private ListView mParty_listView;
	private MyAdapter adapter;
	private ArrayList<UserAllParty> userAllPartieList = new ArrayList<UserAllParty>();
	private SwipeRefreshLayout mTab_party_swipe_refresh;

	public PartyFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(mLayout==null)
		{
			mLayout = inflater.inflate(R.layout.fragment_party, container, false);
			initUI();
			initInterface();
			initParty();
			
			mTab_party_swipe_refresh = (SwipeRefreshLayout) mLayout.findViewById(R.id.tab_party_swipe_refresh);
			mTab_party_swipe_refresh.setOnRefreshListener(this);
			
			// 顶部刷新的样式
			mTab_party_swipe_refresh.setColorSchemeResources(
					android.R.color.holo_red_light,
					android.R.color.holo_green_light,
					android.R.color.holo_blue_bright,
					android.R.color.holo_orange_light);
		}
		return mLayout;
	}

	@Override
	public void onResume() {
		if (userAllPartieList.size() > 0) {
			mParty_listView.setAdapter(adapter);
			mTab_party_prompt_layout.setVisibility(View.GONE);
			mTab_party_swipe_refresh.setVisibility(View.VISIBLE);
			mParty_listView.setVisibility(View.VISIBLE);
			adapter.notifyDataSetChanged();
		} else {
			mTab_party_prompt_layout.setVisibility(View.VISIBLE);
			mTab_party_swipe_refresh.setVisibility(View.GONE);
			mParty_listView.setVisibility(View.GONE);
		}
		super.onResume();
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
						if(userAllPartieList.size()>0)
						{
							mParty_listView.setAdapter(adapter);
							mTab_party_prompt_layout.setVisibility(View.GONE);
							mTab_party_swipe_refresh.setVisibility(View.VISIBLE);
							mParty_listView.setVisibility(View.VISIBLE);
							adapter.notifyDataSetChanged();
						}
					}
					Log.e("PartyFragment", "userAllPartieList.size()====="+userAllPartieList.size());
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	private void initParty() {
		User partyuser = new User();
		Integer SD_pk_user=SdPkUser.getsD_pk_user();
		partyuser.setPk_user(SD_pk_user);
		Log.e("PartyFragment", "从SD卡中获取到的Pk_user" + SD_pk_user);
		tab_party_interface.readUserAllParty(getActivity(), partyuser);
	}

	private void initUI() {
		mLayout.findViewById(R.id.tab_party_new_layout).setOnClickListener(this);// 新建
		mLayout.findViewById(R.id.tab_party_new).setOnClickListener(this);
		mTab_party_prompt_layout = (RelativeLayout) mLayout.findViewById(R.id.tab_party_prompt_layout);// 提示布局

		mParty_listView =(ListView) mLayout.findViewById(R.id.tab_party_listview);
		mParty_listView.setDividerHeight(0);//设置listview的item直接的间隙为0
		adapter = new MyAdapter();

		//listview的点击监听
		mParty_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int pos = arg2 - mParty_listView.getHeaderViewsCount();
				if (pos >= 0) {
					UserAllParty UserAllParty = userAllPartieList.get(pos);
					Intent intent = new Intent(getActivity(),PartyDetailsActivity.class);
					intent.putExtra(IConstant.UserAll, true);
					intent.putExtra(IConstant.UserAllParty, UserAllParty);
					getActivity().startActivity(intent);
					
					SharedPreferences PartyFragment_sp=getActivity().getSharedPreferences(IConstant.Partyfragmnet, 0);
					Editor editor=PartyFragment_sp.edit();
					editor.putInt(IConstant.Partyfragmnet_Pk_party_user, UserAllParty.getPk_party_user());
					editor.putString(IConstant.Partyfragmnet_Pk_party, UserAllParty.getPk_party());
					editor.putInt(IConstant.Partyfragmnet_fk_group, UserAllParty.getFk_group());
					editor.commit();
					Log.e("PartyFragment", "所得到的UserAllParty.getPk_party_user()===========" + UserAllParty.getPk_party_user());
					Log.e("PartyFragment", "所得到的UserAllParty.getPk_party()===========" + UserAllParty.getPk_party());
					Log.e("PartyFragment", "所得到的UserAllParty.getFk_group()===========" + UserAllParty.getFk_group());
				}
			}
		});
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
				LayoutInflater layoutInflater = getActivity().getLayoutInflater();
				inflater = layoutInflater.inflate(R.layout.party_item, null);
				holder.years_month = (TextView) inflater.findViewById(R.id.years_month);
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
	
	//下拉刷新
	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				initParty();
				mTab_party_swipe_refresh.setRefreshing(false);
			}
		}, 3000);
	}

	@Override
	public void onDestroyView() {
		ViewGroup parent = (ViewGroup) mLayout.getParent();
		parent.removeView(mLayout);
		super.onDestroyView();
	}
}
