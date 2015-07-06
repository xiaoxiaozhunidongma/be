package com.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.BJ.javabean.IDs;
import com.BJ.javabean.Party;
import com.BJ.javabean.Partyback;
import com.biju.Interface;
import com.biju.Interface.UserInterface;
import com.biju.R;
import com.biju.function.GroupActivity;
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
public class ScheduleFragment extends Fragment {

	private View mLayout;
	private int returndata;
	private boolean login;
	private boolean isRegistered_one;
	private IDs ids;
	private Interface scheduleInterface;
	private RelativeLayout mSchedule_prompt_layout;
	private RelativeLayout mSchedule_list_layout;
	private ListView mSchedule_listView;
	private ArrayList<Party> partylist = new ArrayList<Party>();
	private MyAdapter adapter = null;
	private PullToRefreshListView mPull_refresh_list;

	public ScheduleFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayout = inflater
				.inflate(R.layout.fragment_schedule, container, false);
		SharedPreferences sp = getActivity().getSharedPreferences("Registered",
				0);
		isRegistered_one = sp.getBoolean("isRegistered_one", false);
		returndata = sp.getInt("returndata", returndata);
		SharedPreferences sp1 = getActivity()
				.getSharedPreferences("isLogin", 0);
		login = sp1.getBoolean("Login", false);
		initInterface();
		initreadUserGroupParty();
		initUI();
		return mLayout;
	}

	private void initUI() {
		mSchedule_prompt_layout = (RelativeLayout) mLayout
				.findViewById(R.id.Schedule_prompt_layout);// 提示
		mPull_refresh_list = (PullToRefreshListView) mLayout
				.findViewById(R.id.pull_refresh_list);
		mPull_refresh_list
				.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
						String label = sdf.format(new Date());
						// 显示最后更新的时间
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);
						if (partylist.size() > 0) {
							new GetDataTask().execute();
						} else {
							Toast.makeText(getActivity(), "暂无更新",
									Toast.LENGTH_SHORT).show();
							mPull_refresh_list.onRefreshComplete();
						}
					}
				});

		// 下拉刷新动画
		ILoadingLayout iLoadingLayout = mPull_refresh_list
				.getLoadingLayoutProxy();
		iLoadingLayout.setPullLabel("下拉刷新");
		iLoadingLayout.setRefreshingLabel("正在刷新...");
		iLoadingLayout.setReleaseLabel("放开即可刷新");

		mSchedule_listView = mPull_refresh_list.getRefreshableView();
		adapter = new MyAdapter();
		mSchedule_listView.setAdapter(adapter);
		mSchedule_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int pos = arg2 - mSchedule_listView.getHeaderViewsCount();
				if (pos >= 0) {
					Log.e("ScheduleFragment", "所点击中的行数" + arg2);
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
	}

	class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return partylist.size();
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
				inflater.setTag(holder);
			} else {
				inflater = convertView;
				holder = (ViewHolder) inflater.getTag();
			}
			Party party = partylist.get(position);
			String time = party.getBegin_time();
			Log.e("ScheduleFragment", "时间的长度====" + time.length());
			String yuars_month = time.substring(0, 10);
			String times = time.substring(11, 16);
			holder.years_month.setText(yuars_month);
			holder.name.setText(party.getName());
			holder.times.setText(times);
			holder.address.setText(party.getLocation());

			return inflater;
		}
	}

	private void initInterface() {
		scheduleInterface = new Interface();
		scheduleInterface.setPostListener(new UserInterface() {

			@Override
			public void success(String A) {
				partylist.clear();
				Partyback partybackInterface = GsonUtils.parseJson(A,
						Partyback.class);
				Integer statusMsg = partybackInterface.getStatusMsg();
				if (statusMsg == 1) {
					List<Party> partys = partybackInterface.getReturnData();
					if (partys.size() > 0) {
						for (int i = 0; i < partys.size(); i++) {
							Party schedule = partys.get(i);
							partylist.add(schedule);
						}
					}
					Log.e("ScheduleFragment", "读取出小组中的聚会信息===" + A);
				}
				if (partylist.size() > 0) {
					mSchedule_prompt_layout.setVisibility(View.GONE);
					mPull_refresh_list.setVisibility(View.VISIBLE);
				} else {
					mSchedule_prompt_layout.setVisibility(View.VISIBLE);
					mPull_refresh_list.setVisibility(View.GONE);
				}
				adapter.notifyDataSetChanged();
			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	private void initreadUserGroupParty() {
		Integer id_group = GroupActivity.pk_group;
		Integer id_user_group = GroupActivity.pk_group_user;
		if (isRegistered_one) {
			ids = new IDs(id_group, returndata, id_user_group);

		} else {
			if (login) {
				int pk_user = LoginActivity.pk_user;
				ids = new IDs(id_group, pk_user, id_user_group);
				Log.e("ScheduleFragment", "id_group====" + id_group);
				Log.e("ScheduleFragment", "pk_user====" + pk_user);
				Log.e("ScheduleFragment", "id_user_group====" + id_user_group);
			} else {
				ids = new IDs(id_group, returndata, id_user_group);
			}
		}
		scheduleInterface.readUserGroupParty(getActivity(), ids);
	}

}
