package com.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BJ.javabean.IDs;
import com.BJ.javabean.Party2;
import com.BJ.javabean.PartyRelationshipback;
import com.BJ.javabean.Party_User;
import com.BJ.javabean.Partyback;
import com.BJ.utils.SdPkUser;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.createPartyRelationListenner;
import com.biju.Interface.readUserGroupPartyListenner;
import com.biju.Interface.updateUserJoinMsgListenner;
import com.biju.R;
import com.biju.function.GroupActivity;
import com.biju.function.PartyDetailsActivity;
import com.github.volley_examples.utils.GsonUtils;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class ScheduleFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

	private View mLayout;
	private Interface scheduleInterface;
	private RelativeLayout mSchedule_prompt_layout;
	private ListView mSchedule_listView;
	private ArrayList<Party2> partylist = new ArrayList<Party2>();
	private MyAdapter adapter = null;
	private Integer pk_user_1;
	private Party2 scheduleparty;
	private Integer sD_pk_user;
	private SwipeRefreshLayout mSchedule_swipe_refresh;

	public ScheduleFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayout = inflater.inflate(R.layout.fragment_schedule, container, false);
		sD_pk_user = SdPkUser.getsD_pk_user();
		initInterface();
		initreadUserGroupParty();
		initUI();
		
		mSchedule_swipe_refresh = (SwipeRefreshLayout) mLayout.findViewById(R.id.schedule_swipe_refresh);
		mSchedule_swipe_refresh.setOnRefreshListener(this);

		// 顶部刷新的样式
		mSchedule_swipe_refresh.setColorSchemeResources(
				android.R.color.holo_red_light,
				android.R.color.holo_green_light,
				android.R.color.holo_blue_bright,
				android.R.color.holo_orange_light);
		return mLayout;
	}

	@Override
	public void onStart() {
		SharedPreferences PartyDetails_sp = getActivity().getSharedPreferences("isPartyDetails_", 0);
		boolean PartyDetails=PartyDetails_sp.getBoolean("PartyDetails", false);
		if(PartyDetails)
		{
			initreadUserGroupParty();
			adapter.notifyDataSetChanged();
		}
		
		super.onStart();
	}
	
	private void initUI() {
		mSchedule_prompt_layout = (RelativeLayout) mLayout.findViewById(R.id.Schedule_prompt_layout);// 提示
		mSchedule_listView=(ListView) mLayout.findViewById(R.id.schedule_listview);
		adapter = new MyAdapter();
		mSchedule_listView.setAdapter(adapter);
		mSchedule_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int pos = arg2 - mSchedule_listView.getHeaderViewsCount();
				if (pos >= 0) {
					Log.e("ScheduleFragment", "所点击中的行数" + arg2);
					scheduleparty = partylist.get(pos);
					Integer relatonship = scheduleparty.getRelationship();
					Integer fk_user = scheduleparty.getFk_user();
					String pk_party = scheduleparty.getPk_party();
					Integer pk_party_user = scheduleparty.getPk_party_user();
					if (relatonship == null) {
						initcreatePartyRelation(fk_user, pk_party,pk_party_user);
					} else {
						Intent intent = new Intent(getActivity(),PartyDetailsActivity.class);
						intent.putExtra(IConstant.OneParty, scheduleparty);
						startActivity(intent);
					}
				}
			}
		});

	}

	private void initcreatePartyRelation(Integer fk_user, String pk_party,Integer pk_party_user) {
		Party_User readuserparty = new Party_User();
		readuserparty.setPk_party_user(pk_party_user);
		readuserparty.setFk_party(pk_party);
		readuserparty.setFk_user(fk_user);
		readuserparty.setStatus(1);
		scheduleInterface.createPartyRelation(getActivity(), readuserparty);
	}

	class ViewHolder {
		TextView years_month;
		TextView address;
		TextView name;
		TextView times;
		ImageView party_unread_tag;
		TextView inNum;
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
				LayoutInflater layoutInflater = getActivity().getLayoutInflater();
				inflater = layoutInflater.inflate(R.layout.party_item, null);
				holder.years_month = (TextView) inflater.findViewById(R.id.years_month);
				holder.name = (TextView) inflater.findViewById(R.id.name);
				holder.times = (TextView) inflater.findViewById(R.id.times);
				holder.address = (TextView) inflater.findViewById(R.id.address);
				holder.party_unread_tag = (ImageView) inflater.findViewById(R.id.party_unread_tag);
				holder.inNum=(TextView) inflater.findViewById(R.id.inNum);
				inflater.setTag(holder);
			} else {
				inflater = convertView;
				holder = (ViewHolder) inflater.getTag();
			}
			if (partylist.size() > 0) {
				Party2 party = partylist.get(position);
				String time = party.getBegin_time();
				Log.e("ScheduleFragment", "时间的长度====" + time.length());
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
				if(party.getInNum()!=null)
				{
					holder.inNum.setText(party.getInNum()+"");
				}else
				{
					holder.inNum.setText("0");
				}
				Integer ralationship = party.getRelationship();
				if (ralationship == null) {
					holder.party_unread_tag.setVisibility(View.VISIBLE);
				} else {
					holder.party_unread_tag.setVisibility(View.GONE);
				}
			}

			return inflater;
		}
	}

	private void initInterface() {
		scheduleInterface = Interface.getInstance();
		scheduleInterface.setPostListener(new readUserGroupPartyListenner() {

			@Override
			public void success(String A) {
				partylist.clear();
				Partyback partybackInterface = GsonUtils.parseJson(A,Partyback.class);
				Integer statusMsg = partybackInterface.getStatusMsg();
				if (statusMsg == 1) {
					List<Party2> partys = partybackInterface.getReturnData();
					if (partys.size() > 0) {
						for (int i = 0; i < partys.size(); i++) {
							Party2 schedule = partys.get(i);
							partylist.add(schedule);
						}
					}
					Log.e("ScheduleFragment", "读取出小组中的聚会信息===" + A);
				}
				if (partylist.size() > 0) {
					mSchedule_prompt_layout.setVisibility(View.GONE);
				} else {
					mSchedule_prompt_layout.setVisibility(View.VISIBLE);
				}
				adapter.notifyDataSetChanged();
			}

			@Override
			public void defail(Object B) {

			}
		});

		scheduleInterface.setPostListener(new createPartyRelationListenner() {

			@Override
			public void success(String A) {
				PartyRelationshipback partyRelationshipback = GsonUtils
						.parseJson(A, PartyRelationshipback.class);
				Integer statusMsg = partyRelationshipback.getStatusMsg();
				if (statusMsg == 1) {
					Log.e("ScheduleFragment", "得到的关系结果===="+A);
					Integer pk_party_user = partyRelationshipback.getReturnData();
					Log.e("ScheduleFragment", "得到的pk_party_user111111===="+pk_party_user);
					SdPkUser.setGetPk_party_user(pk_party_user);
					Party_User party_user = new Party_User();
					party_user.setPk_party_user(pk_party_user);
					party_user.setRelationship(0);
					party_user.setFk_party(scheduleparty.getPk_party());
					party_user.setStatus(1);
					scheduleInterface.updateUserJoinMsg(getActivity(),party_user);
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
		
		scheduleInterface.setPostListener(new updateUserJoinMsgListenner() {

			@Override
			public void success(String A) {
				Log.e("ScheduleFragment", "返回的是否更新成功" + A);
				Intent intent = new Intent(getActivity(),PartyDetailsActivity.class);
				intent.putExtra(IConstant.IsRelationship, true);
				intent.putExtra(IConstant.OneParty, scheduleparty);
				startActivity(intent);
			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	private void initreadUserGroupParty() {
		Integer id_group = GroupActivity.getPk_group();
		Integer id_user_group = GroupActivity.getPk_group_user();
		IDs ids = new IDs(id_group, sD_pk_user, id_user_group);
		pk_user_1 = sD_pk_user;
		scheduleInterface.readUserGroupParty(getActivity(), ids);
		SharedPreferences sp = getActivity().getSharedPreferences("isPk_user",0);
		Editor editor = sp.edit();
		editor.putInt("Pk_user", pk_user_1);
		editor.commit();
	}

	//下拉刷新
	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				initreadUserGroupParty();
				mSchedule_swipe_refresh.setRefreshing(false);
				adapter.notifyDataSetChanged();
			}
		}, 3000);
	}
}
