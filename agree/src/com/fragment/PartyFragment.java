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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BJ.javabean.Party_User;
import com.BJ.javabean.User;
import com.BJ.javabean.UserAllParty;
import com.BJ.javabean.UserAllPartyback;
import com.BJ.utils.SdPkUser;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.readUserAllPartyListenner;
import com.biju.Interface.updateUserJoinMsgListenner;
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
			Log.e("PartyFragment", "������onCreateView========");
			mTab_party_swipe_refresh = (SwipeRefreshLayout) mLayout.findViewById(R.id.tab_party_swipe_refresh);
			mTab_party_swipe_refresh.setOnRefreshListener(this);
			
			// ����ˢ�µ���ʽ
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
		SharedPreferences refresh_sp=getActivity().getSharedPreferences(IConstant.AddRefresh, 0);
		boolean isaddrefresh=refresh_sp.getBoolean(IConstant.IsAddRefresh, false);
		Log.e("PartyFragment", "������onResume========"+isaddrefresh);
		if(isaddrefresh)
		{
			initParty();
			adapter.notifyDataSetChanged();
		}
		super.onResume();
	}
	
	private void initInterface() {
		tab_party_interface = Interface.getInstance();
		tab_party_interface.setPostListener(new readUserAllPartyListenner() {

			@Override
			public void success(String A) {
				Log.e("PartyFragment", "��ȡ���û��������ճ�====" + A);
				userAllPartieList.clear();
				UserAllPartyback allPartyback = GsonUtils.parseJson(A,UserAllPartyback.class);
				int status = allPartyback.getStatusMsg();
				if (status == 1) {
					List<UserAllParty> AllList = allPartyback.getReturnData();
					if (AllList.size() > 0) {
						for (int i = 0; i < AllList.size(); i++) {
							UserAllParty userAllParty = AllList.get(i);
							userAllPartieList.add(userAllParty);
						}
						if(userAllPartieList.size()>0)
						{
							mTab_party_prompt_layout.setVisibility(View.GONE);
							mTab_party_swipe_refresh.setVisibility(View.VISIBLE);
							mParty_listView.setVisibility(View.VISIBLE);
							adapter.notifyDataSetChanged();
						} 
					}
					Log.e("PartyFragment", "userAllPartieList.size()====="+userAllPartieList.size());
				}else if(status==0){
					mTab_party_prompt_layout.setVisibility(View.VISIBLE);
					mParty_listView.setVisibility(View.GONE);
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
		//����
		tab_party_interface.setPostListener(new updateUserJoinMsgListenner() {

			@Override
			public void success(String A) {
				Log.e("PartyFragment", "���ص��Ƿ���³ɹ�" + A);
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
		Log.e("PartyFragment", "��SD���л�ȡ����Pk_user" + SD_pk_user);
		tab_party_interface.readUserAllParty(getActivity(), partyuser);
	}

	private void initUI() {
		mLayout.findViewById(R.id.tab_party_new_layout).setOnClickListener(this);// �½�
		mLayout.findViewById(R.id.tab_party_new).setOnClickListener(this);
		mTab_party_prompt_layout = (RelativeLayout) mLayout.findViewById(R.id.tab_party_prompt_layout);// ��ʾ����

		mParty_listView =(ListView) mLayout.findViewById(R.id.tab_party_listview);
		mParty_listView.setDividerHeight(0);//����listview��itemֱ�ӵļ�϶Ϊ0

		//listview�ĵ������
		mParty_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int pos = arg2 - mParty_listView.getHeaderViewsCount();
				if (pos >= 0) {
					UserAllParty UserAllParty = userAllPartieList.get(pos);
					Integer relationship=UserAllParty.getRelationship();
					Log.e("PartyFragment", "���õ���relationship===========" + relationship+"          "+pos);
					if(relationship==0)
					{
						Party_User party_user = new Party_User();
						party_user.setPk_party_user(UserAllParty.getPk_party_user());
						party_user.setRelationship(3);
						party_user.setType(1);
						tab_party_interface.updateUserJoinMsg(getActivity(),party_user);
					}
					
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
					Log.e("PartyFragment", "���õ���UserAllParty.getPk_party_user()===========" + UserAllParty.getPk_party_user());
					Log.e("PartyFragment", "���õ���UserAllParty.getPk_party()===========" + UserAllParty.getPk_party());
					Log.e("PartyFragment", "���õ���UserAllParty.getFk_group()===========" + UserAllParty.getFk_group());
				}
			}
		});
		adapter = new MyAdapter();
		mParty_listView.setAdapter(adapter);
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
				holder.years_month = (TextView) inflater.findViewById(R.id.Party_item_years_month);//�ۻ�����
				holder.name = (TextView) inflater.findViewById(R.id.Party_item_name);//�ۻ�����
				holder.times = (TextView) inflater.findViewById(R.id.Party_item_time);//ʱ��
				holder.address = (TextView) inflater.findViewById(R.id.Party_item_address);//��ַ
				holder.Party_item_background=(RelativeLayout) inflater.findViewById(R.id.Party_item_background);//������ɫ
				holder.Party_item_redprompt=(ImageView) inflater.findViewById(R.id.Party_item_redprompt);//С�����ʾ
				holder.Party_item_inNum=(TextView) inflater.findViewById(R.id.Party_item_inNum);//��������
				holder.Party_item_payment=(TextView) inflater.findViewById(R.id.Party_item_payment);//���ʽ
				holder.Party_item_prompt_1=(TextView) inflater.findViewById(R.id.Party_item_prompt_1);//�»���ʾ��
				holder.Party_item_prompt_2=(TextView) inflater.findViewById(R.id.Party_item_prompt_2);//�»���ʾ��
				inflater.setTag(holder);
			} else {
				inflater = convertView;
				holder = (ViewHolder) inflater.getTag();
			}
			if (userAllPartieList.size() > 0) {
				UserAllParty party = userAllPartieList.get(position);
				String time = party.getBegin_time();
				String yuars_month = time.substring(0, 10);
//				String years = yuars_month.substring(0, 4);
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
				Integer relationship=party.getRelationship();
				if(relationship==4){
					holder.name.setTextColor(holder.name.getResources().getColor(R.drawable.Party_partake_nickname_color));//�����������ɫ��ɫ
					holder.Party_item_background.setBackgroundResource(R.drawable.party_green_corners);//�������ۻ��򱳾�Ϊ��ɫ
					holder.years_month.setTextColor(holder.years_month.getResources().getColor(R.color.white));
					holder.times.setTextColor(holder.times.getResources().getColor(R.color.white));
					holder.address.setTextColor(holder.address.getResources().getColor(R.drawable.Party_partake_address_color));//������ַ��ɫ���
					holder.Party_item_payment.setTextColor(holder.Party_item_payment.getResources().getColor(R.drawable.Party_partake_pay_color));//����󸶿ʽ����ɫ
				}else{
					holder.Party_item_background.setBackgroundResource(R.drawable.party_partake_background);//�������ۻ��򱳾�Ϊ��ɫ
					holder.name.setTextColor(holder.name.getResources().getColor(R.drawable.Party_notpartake_nickname_color));//δ�����������ɫ���
					holder.address.setTextColor(holder.address.getResources().getColor(R.drawable.Party_notpartake_address_color));//δ������ַ��ɫǳ��
					holder.years_month.setTextColor(holder.years_month.getResources().getColor(R.color.party_time_background));
					holder.times.setTextColor(holder.times.getResources().getColor(R.color.party_time_background));
				}
				
				holder.years_month.setText(times);
				holder.name.setText(party.getName());
				holder.times.setText(datetimes);
				holder.address.setText(party.getLocation());
				if(position==userAllPartieList.size()-1){
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
	
	//����ˢ��
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
