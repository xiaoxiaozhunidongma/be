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
import android.view.View.OnClickListener;
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
import com.biju.function.AddNewPartyActivity;
import com.biju.function.GroupActivity;
import com.biju.function.PartyDetailsActivity;
import com.github.volley_examples.utils.GsonUtils;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class ScheduleFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,OnClickListener{

	private View mLayout;
	private Interface scheduleInterface;
	private ListView mSchedule_listView;
	private ArrayList<Party2> partylist = new ArrayList<Party2>();
	private MyAdapter adapter = null;
	private Integer pk_user_1;
	private Party2 scheduleparty;
	private Integer sD_pk_user;
	private SwipeRefreshLayout mSchedule_swipe_refresh;
	private int num=0;
	private RelativeLayout mSchedule_new_party_layout;
	private TextView mSchedule_new_party;
	private RelativeLayout mSchedule_swipe_refresh_layout;
	private RelativeLayout mSchedule_prompt_layout;

	public ScheduleFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(mLayout==null){
			mLayout = inflater.inflate(R.layout.fragment_schedule, container, false);
			sD_pk_user = SdPkUser.getsD_pk_user();
			initInterface();
			initreadUserGroupParty();
			initUI();
			
			mSchedule_swipe_refresh = (SwipeRefreshLayout) mLayout.findViewById(R.id.schedule_swipe_refresh);
			mSchedule_swipe_refresh.setOnRefreshListener(this);
			
			// ����ˢ�µ���ʽ
			mSchedule_swipe_refresh.setColorSchemeResources(
					android.R.color.holo_red_light,
					android.R.color.holo_green_light,
					android.R.color.holo_blue_bright,
					android.R.color.holo_orange_light);
			Log.e("ScheduleFragment", "������onStart()=========");
		}
		return mLayout;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ViewGroup parent = (ViewGroup) mLayout.getParent();
		parent.removeView(mLayout);
	}
	
	@Override
	public void onResume() {
		Log.e("ScheduleFragment", "������onResume() =========");
		SharedPreferences PartyDetails_sp = getActivity().getSharedPreferences(IConstant.IsPartyDetails_, 0);
		boolean PartyDetails=PartyDetails_sp.getBoolean(IConstant.PartyDetails, false);
		if(PartyDetails)
		{
			initreadUserGroupParty();
			adapter.notifyDataSetChanged();
		}
		
		SharedPreferences refresh_sp=getActivity().getSharedPreferences(IConstant.AddRefresh, 0);
		boolean isaddrefresh=refresh_sp.getBoolean(IConstant.IsAddRefresh, false);
		if(isaddrefresh)
		{
			initreadUserGroupParty();
			adapter.notifyDataSetChanged();
			Log.e("ScheduleFragment", "������onResume() ��isaddrefresh========="+isaddrefresh);
		}
		
		SharedPreferences more_sp=getActivity().getSharedPreferences(IConstant.MoreRefresh, 0);
		boolean iscancle=more_sp.getBoolean(IConstant.Morecancle, false);
		if(iscancle)
		{
			initreadUserGroupParty();
			adapter.notifyDataSetChanged();
		}
		
		super.onResume();
	}
	private void initUI() {
		mLayout.findViewById(R.id.Schedule_prompt).setOnClickListener(this);//������д����»
		mSchedule_prompt_layout = (RelativeLayout) mLayout.findViewById(R.id.Schedule_prompt_layout);//û�оۻ�ʱ�������ʾ
		mSchedule_swipe_refresh_layout = (RelativeLayout) mLayout.findViewById(R.id.Schedule_swipe_refresh_layout);//�оۻ�ʱ����ʾ
		mSchedule_new_party_layout = (RelativeLayout) mLayout.findViewById(R.id.Schedule_new_party_layout);//����µľۻ�
		mSchedule_new_party = (TextView) mLayout.findViewById(R.id.Schedule_new_party);
		mSchedule_new_party_layout.setOnClickListener(this);
		mSchedule_new_party.setOnClickListener(this);
		
		
		mSchedule_listView=(ListView) mLayout.findViewById(R.id.schedule_listview);
		mSchedule_listView.setDividerHeight(0);//����listview��itemֱ�ӵļ�϶Ϊ0
		adapter = new MyAdapter();
		mSchedule_listView.setAdapter(adapter);
		mSchedule_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int pos = arg2 - mSchedule_listView.getHeaderViewsCount();
				if (pos >= 0) {
					Log.e("ScheduleFragment", "������е�����" + arg2);
					scheduleparty = partylist.get(pos);
					Integer relatonship = scheduleparty.getRelationship();
					String pk_party = scheduleparty.getPk_party();
					Log.e("ScheduleFragment", "��ȡ����pk_party ========="+pk_party);
					if (relatonship == null) {
						initcreatePartyRelation(sD_pk_user,pk_party);
						Log.e("ScheduleFragment", "���뵽relatonshipΪnull�ĵط�==========");
					}else if(relatonship==0)
					{
						Log.e("ScheduleFragment", "���뵽relatonshipΪ0�ĵط�==========");
						Party_User party_user = new Party_User();
						party_user.setPk_party_user(scheduleparty.getPk_party_user());
						party_user.setRelationship(3);
						party_user.setType(1);
						scheduleInterface.updateUserJoinMsg(getActivity(),party_user);
						
						Intent intent = new Intent(getActivity(),PartyDetailsActivity.class);
						intent.putExtra(IConstant.OneParty, scheduleparty);
						startActivity(intent);
						
						SharedPreferences Schedule_sp=getActivity().getSharedPreferences(IConstant.Schedule, 0);
						Editor editor=Schedule_sp.edit();
						editor.putInt(IConstant.Pk_party_user, scheduleparty.getPk_party_user());
						editor.putString(IConstant.Pk_party, scheduleparty.getPk_party());
						editor.putInt(IConstant.fk_group, scheduleparty.getFk_group());
						editor.commit();
						
					}else {
						Log.e("ScheduleFragment", "���뵽relatonship��Ϊnull�ĵط�==========");
						Intent intent = new Intent(getActivity(),PartyDetailsActivity.class);
						intent.putExtra(IConstant.OneParty, scheduleparty);
						startActivity(intent);
						
						SharedPreferences Schedule_sp=getActivity().getSharedPreferences(IConstant.Schedule, 0);
						Editor editor=Schedule_sp.edit();
						editor.putInt(IConstant.Pk_party_user, scheduleparty.getPk_party_user());
						editor.putString(IConstant.Pk_party, scheduleparty.getPk_party());
						editor.putInt(IConstant.fk_group, scheduleparty.getFk_group());
						editor.commit();
					}
				}
			}
		});

	}

	private void initcreatePartyRelation(Integer pk_user, String pk_party) {
		Party_User readuserparty = new Party_User();
		readuserparty.setFk_party(pk_party);
		readuserparty.setFk_user(pk_user);
		scheduleInterface.createPartyRelation(getActivity(), readuserparty);
	}

	class ViewHolder {
		TextView years_month;
		TextView address;
		TextView name;
		TextView times;
		RelativeLayout Party_item_background;
		ImageView Party_item_redprompt;
		TextView Party_item_inNum;
		TextView Party_item_payment;
		TextView Party_item_prompt_1;
		TextView Party_item_prompt_2;
	}

	class MyAdapter extends BaseAdapter {

		private String times;
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
				holder.years_month = (TextView) inflater.findViewById(R.id.Party_item_years_month);//�ۻ�����
				holder.name = (TextView) inflater.findViewById(R.id.Party_item_name);//�ۻ�����
				holder.times = (TextView) inflater.findViewById(R.id.Party_item_time);//ʱ��
				holder.address = (TextView) inflater.findViewById(R.id.Party_item_address);//��ַ
				holder.Party_item_background = (RelativeLayout) inflater.findViewById(R.id.Party_item_background);
				holder.Party_item_redprompt=(ImageView) inflater.findViewById(R.id.Party_item_redprompt);
				holder.Party_item_inNum=(TextView) inflater.findViewById(R.id.Party_item_inNum);//��������
				holder.Party_item_payment=(TextView) inflater.findViewById(R.id.Party_item_payment);//���ʽ
				holder.Party_item_prompt_1=(TextView) inflater.findViewById(R.id.Party_item_prompt_1);//�»���ʾ��
				holder.Party_item_prompt_2=(TextView) inflater.findViewById(R.id.Party_item_prompt_2);//�»���ʾ��
				inflater.setTag(holder);
			} else {
				inflater = convertView;
				holder = (ViewHolder) inflater.getTag();
			}
			if (partylist.size() > 0) {
				Party2 party = partylist.get(position);
				String time = party.getBegin_time();
				String yuars_month = time.substring(0, 10);
				String years = yuars_month.substring(0, 4);
				String months = yuars_month.substring(5, 7);
				String months_1=months.substring(0, 1);
				String months_2=months.substring(1, 2);
				String days = yuars_month.substring(8, 10);
				switch (Integer.valueOf(months_1)) {
				case 0:
					times = months_2 + "��" + days + "��";
					break;
				case 1:
					times = months + "��" + days + "��";
					break;
				default:
					break;
				}
				String datetimes = time.substring(11, 16);
				//�жϲ�������ͣ���ѻ���Ԥ֧��
				Integer pay_type=party.getPay_type();
				if(1==pay_type){
					holder.Party_item_payment.setText("���");
				}else{
					holder.Party_item_payment.setText("Ԥ����");
				}
				//�жϲ�����������ʾ
				Integer inNum=party.getInNum();
				if(inNum==null){
					holder.Party_item_inNum.setText("0");
				}else{
					holder.Party_item_inNum.setText(""+inNum);
				}
				//�ж��Ƿ�������ʾ
				Integer ralationship = party.getRelationship();
				if (ralationship == null) {
					holder.Party_item_background.setBackgroundResource(R.drawable.white);//���û�в���ۻ��򱳾�Ϊ��ɫ
					holder.name.setTextColor(holder.name.getResources().getColor(R.drawable.Party_notpartake_nickname_color));//δ�����������ɫ���
					holder.address.setTextColor(holder.address.getResources().getColor(R.drawable.Party_notpartake_address_color));//δ������ַ��ɫǳ��
					holder.years_month.setTextColor(holder.years_month.getResources().getColor(R.color.party_time_background));
					holder.times.setTextColor(holder.times.getResources().getColor(R.color.party_time_background));
					holder.Party_item_redprompt.setVisibility(View.VISIBLE);
				} else if(ralationship == 0){
					holder.Party_item_redprompt.setVisibility(View.GONE);
					holder.Party_item_background.setBackgroundResource(R.drawable.white);//���û�в���ۻ��򱳾�Ϊ��ɫ
					holder.name.setTextColor(holder.name.getResources().getColor(R.drawable.Party_notpartake_nickname_color));//δ�����������ɫ���
					holder.address.setTextColor(holder.address.getResources().getColor(R.drawable.Party_notpartake_address_color));//δ������ַ��ɫǳ��
					holder.years_month.setTextColor(holder.years_month.getResources().getColor(R.color.party_time_background));
					holder.times.setTextColor(holder.times.getResources().getColor(R.color.party_time_background));
				}else if(ralationship ==3){
					holder.Party_item_redprompt.setVisibility(View.GONE);
					holder.Party_item_background.setBackgroundResource(R.drawable.white);//���û�в���ۻ��򱳾�Ϊ��ɫ
					holder.name.setTextColor(holder.name.getResources().getColor(R.drawable.Party_notpartake_nickname_color));//δ�����������ɫ���
					holder.address.setTextColor(holder.address.getResources().getColor(R.drawable.Party_notpartake_address_color));//δ������ַ��ɫǳ��
					holder.years_month.setTextColor(holder.years_month.getResources().getColor(R.color.party_time_background));
					holder.times.setTextColor(holder.times.getResources().getColor(R.color.party_time_background));
				}else if(ralationship ==4){
					holder.Party_item_redprompt.setVisibility(View.GONE);
					holder.address.setTextColor(holder.address.getResources().getColor(R.drawable.Party_partake_address_color));//������ַ��ɫ���
					holder.name.setTextColor(holder.name.getResources().getColor(R.drawable.Party_partake_nickname_color));//�����������ɫ��ɫ
					holder.Party_item_background.setBackgroundResource(R.drawable.party_green_corners);//�������ۻ��򱳾�Ϊ��ɫ
					holder.years_month.setTextColor(holder.years_month.getResources().getColor(R.color.white));
					holder.times.setTextColor(holder.times.getResources().getColor(R.color.white));
					holder.Party_item_payment.setTextColor(holder.Party_item_payment.getResources().getColor(R.drawable.Party_partake_pay_color));//����󸶿ʽ����ɫ
				}
				holder.years_month.setText(times);
				holder.name.setText(party.getName());
				holder.times.setText(datetimes);
				holder.address.setText(party.getLocation());
				if(position==partylist.size()-1){
					holder.Party_item_prompt_1.setVisibility(View.GONE);
					holder.Party_item_prompt_2.setVisibility(View.VISIBLE);
				}else{
					holder.Party_item_prompt_1.setVisibility(View.VISIBLE);
					holder.Party_item_prompt_2.setVisibility(View.GONE);
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
					Log.e("ScheduleFragment", "��ȡ��С���еľۻ���Ϣ===" + A);
					List<Party2> partys = partybackInterface.getReturnData();
					if (partys.size() > 0) {
						for (int i = 0; i < partys.size(); i++) {
							Party2 schedule = partys.get(i);
							partylist.add(schedule);
						}
					}
				}
				if (partylist.size() > 0) {
					mSchedule_prompt_layout.setVisibility(View.GONE);
					mSchedule_swipe_refresh_layout.setVisibility(View.VISIBLE);
				} else {
					mSchedule_prompt_layout.setVisibility(View.VISIBLE);
					mSchedule_swipe_refresh_layout.setVisibility(View.GONE);
					Log.e("ScheduleFragment", "������û�оۻ�ʱ���������==========");
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
					Log.e("ScheduleFragment", "�õ��Ĺ�ϵ���===="+A);
					Integer pk_party_user = partyRelationshipback.getReturnData();
					Log.e("ScheduleFragment", "�õ���pk_party_user111111===="+pk_party_user);
					if(pk_party_user!=null){
						Intent intent = new Intent(getActivity(),PartyDetailsActivity.class);
						intent.putExtra(IConstant.IsRelationship, true);
						intent.putExtra(IConstant.OneParty, scheduleparty);
						startActivity(intent);
						
						SharedPreferences Schedule_sp=getActivity().getSharedPreferences(IConstant.Schedule, 0);
						Editor editor=Schedule_sp.edit();
						editor.putInt(IConstant.Pk_party_user, pk_party_user);
						editor.putString(IConstant.Pk_party, scheduleparty.getPk_party());
						editor.putInt(IConstant.fk_group, scheduleparty.getFk_group());
						editor.commit();
						Log.e("ScheduleFragment", "�õ��ĵ�һ��pk_party_user======="+pk_party_user);
					}
					Party_User party_user = new Party_User();
					party_user.setPk_party_user(pk_party_user);
					party_user.setRelationship(3);
					party_user.setType(1);
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
				Log.e("ScheduleFragment", "���ص��Ƿ���³ɹ�" + A);
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

	//����ˢ��
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Schedule_new_party:
		case R.id.Schedule_new_party_layout:
			Schedule_new_party();
			break;
		case R.id.Schedule_prompt:
			Schedule_prompt();
			break;

		default:
			break;
		}
	}

	//���½��ۻ�Ĳ���
	private void Schedule_prompt() {
		Schedule_new_party();
	}

	//�½��ۻ�
	private void Schedule_new_party() {
		Intent intent=new Intent(getActivity(), AddNewPartyActivity.class);
		intent.putExtra(IConstant.Fk_group, GroupActivity.getPk_group());
		intent.putExtra(IConstant.IsSchedule, true);
		startActivity(intent);
	}
}
