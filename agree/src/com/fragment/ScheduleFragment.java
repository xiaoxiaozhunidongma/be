package com.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.BJ.javabean.IDs;
import com.BJ.javabean.Party;
import com.BJ.javabean.Partyback;
import com.biju.Interface;
import com.biju.Interface.UserInterface;
import com.biju.R;
import com.biju.function.GroupActivity;
import com.biju.login.LoginActivity;
import com.github.volley_examples.utils.GsonUtils;

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
	private MyAdapter adapter=null;

	public ScheduleFragment() {
		// Required empty public constructor
	}

//	@Override  
//	    public void onCreate(Bundle savedInstanceState) {  
//	        super.onCreate(savedInstanceState);  
//	          
//	        adapter = new MyAdapter (getActivity());  
//	        setListAdapter(adapter);    
//	    } 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayout = inflater
				.inflate(R.layout.fragment_schedule, container, false);
		initUI();
		SharedPreferences sp = getActivity().getSharedPreferences("Registered",
				0);
		isRegistered_one = sp.getBoolean("isRegistered_one", false);
		returndata = sp.getInt("returndata", returndata);
		SharedPreferences sp1 = getActivity()
				.getSharedPreferences("isLogin", 0);
		login = sp1.getBoolean("Login", false);
		initInterface();
		initreadUserGroupParty();
		return mLayout;
	}
	
//	@Override  
//    public void onListItemClick(ListView l, View v, int position, long id) {  
//        super.onListItemClick(l, v, position, id);  
//    }  

	private void initUI() {
		mSchedule_prompt_layout = (RelativeLayout) mLayout
				.findViewById(R.id.Schedule_prompt_layout);// 提示
		mSchedule_list_layout = (RelativeLayout) mLayout
				.findViewById(R.id.Schedule_list_layout);// listview布局
		mSchedule_listView = (ListView) mLayout
				.findViewById(R.id.Schedule_listView);
//		adapter = new MyAdapter();
//		mSchedule_listView.setAdapter(adapter);
		
	}

	class ViewHolder {
		TextView years_month;
		TextView address;
		TextView name;
	}

	class MyAdapter extends BaseAdapter {

//		private LayoutInflater mInflater = null;
//
//		public MyAdapter(Context context) {
//			super();
//			mInflater = (LayoutInflater) context
//					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		}

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
				holder.address = (TextView) inflater.findViewById(R.id.address);
				inflater.setTag(holder);
			} else {
				inflater = convertView;
				holder = (ViewHolder) inflater.getTag();
			}
			Party party = partylist.get(position);
			holder.years_month.setText(party.getBegin_time());
			holder.name.setText(party.getName());
			holder.address.setText(party.getLocation());

			return inflater;
		}

	}

	private void initInterface() {
		scheduleInterface = new Interface();
		scheduleInterface.setPostListener(new UserInterface() {

			@Override
			public void success(String A) {
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
				// adapter.notifyDataSetChanged();
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
