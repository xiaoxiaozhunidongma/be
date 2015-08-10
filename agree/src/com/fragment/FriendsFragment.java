package com.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

import com.BJ.javabean.ReadUserAllFriends;
import com.BJ.javabean.ReadUserAllFriendsback;
import com.BJ.javabean.User;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.Person;
import com.biju.Interface;
import com.biju.Interface.readFriendListenner;
import com.biju.R;
import com.biju.function.AddFriendsActivity;
import com.biju.function.FriendsDataActivity;
import com.biju.login.LoginActivity;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.example.huanxin.ChatActivity;
import com.github.volley_examples.utils.GsonUtils;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class FriendsFragment extends Fragment implements OnClickListener,
		SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

	private View mLayout;
	private SwipeRefreshLayout mFriends_swipe_refresh;
	private RelativeLayout mFriends_add_layout;
	private RelativeLayout mFriends_add_tishi_layout;
	private ListView mFriends_listview;
	private Interface addFriends_interface;
	private ArrayList<ReadUserAllFriends> AllFriends_List = new ArrayList<ReadUserAllFriends>();
	private String beginStr = "http://201139.image.myqcloud.com/201139/0/";
	private String endStr = "/original";
	private String TestcompleteURL = beginStr
			+ "1ddff6cf-35ac-446b-8312-10f4083ee13d" + endStr;
	private MyAdapter adapter;
	private Integer fk_user_from;
	
	private String fileName = getSDPath() + "/" + "saveData";
	private String pk_user;
	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		// �ж�sd���Ƿ����
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// ��ȡ��Ŀ¼
		}
		return sdDir.toString();

	}

	public FriendsFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayout = inflater.inflate(R.layout.fragment_friends, container, false);
//		SharedPreferences sp = getActivity().getSharedPreferences("Registered",
//				0);
//		isRegistered_one = sp.getBoolean("isRegistered_one", false);
//		returndata = sp.getInt("returndata", 0);
//		SharedPreferences sp1 = getActivity()
//				.getSharedPreferences("isLogin", 0);
//		login = sp1.getBoolean("Login", false);
		
		//��ȡSD���е�pk_user
				FileInputStream fis;
				try {
					fis = new FileInputStream(fileName);
					ObjectInputStream ois = new ObjectInputStream(fis);
					Person person = (Person) ois.readObject();
					pk_user = person.pk_user;
					Log.e("SettingFragment", "��sd���л�ȡ����pk_user" + pk_user);
					ois.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (StreamCorruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

		initUI();
		LoginHuanXin();
		initInterface();
		ReadUserAllFriends();
		mFriends_swipe_refresh = (SwipeRefreshLayout) mLayout
				.findViewById(R.id.friends_swipe_refresh);
		mFriends_swipe_refresh.setOnRefreshListener(this);

		// ����ˢ�µ���ʽ
		mFriends_swipe_refresh.setColorSchemeResources(
				android.R.color.holo_red_light,
				android.R.color.holo_green_light,
				android.R.color.holo_blue_bright,
				android.R.color.holo_orange_light);
		return mLayout;
	}


	//��Ҫ�첽������������������������������������������������������
	private void LoginHuanXin() {
		Log.e("FriendsFragment~~~~~", "������LoginHuanXin����");
		String str_pkuser = String.valueOf(Integer.valueOf(pk_user));
		
		Log.e("FriendsFragment~~~~~", "Integer.valueOf(pk_user)"+pk_user);
		
		if(!"".equals(str_pkuser)){
			
			EMChatManager.getInstance().login(str_pkuser,
					"paopian",new EMCallBack() {//�ص�
				@Override
				public void onSuccess() {
					EMGroupManager.getInstance().loadAllGroups();
					EMChatManager.getInstance().loadAllConversations();
					Log.e("FriendsFragment~~~~~", "��½����������ɹ���~~~~");		
				}
				
				@Override
				public void onProgress(int progress, String status) {
				}
				
				@Override
				public void onError(int code, String message) {
					Log.d("FriendsFragment~~~~~~", "��½���������ʧ�ܣ�~~~~");
				}
			});
		}
	}

	@Override
	public void onStart() {
		ReadUserAllFriends();
		super.onStart();
	}

	private void ReadUserAllFriends() {
		User user = new User();
		user.setPk_user(Integer.valueOf(pk_user));
		fk_user_from=Integer.valueOf(pk_user);
		addFriends_interface.readFriend(getActivity(), user);
	}

	private void initInterface() {
		addFriends_interface = Interface.getInstance();
		addFriends_interface.setPostListener(new readFriendListenner() {

			@Override
			public void success(String A) {
				AllFriends_List.clear();
				ReadUserAllFriendsback readUserAllFriendsback = GsonUtils
						.parseJson(A, ReadUserAllFriendsback.class);
				int status = readUserAllFriendsback.getStatusMsg();
				if (status == 1) {
					Log.e("FriendsFragment", "�����û�����������ӵĺ���========" + A);
					List<ReadUserAllFriends> readUserAllFriendslist = readUserAllFriendsback
							.getReturnData();
					if (readUserAllFriendslist.size() > 0) {
						for (int i = 0; i < readUserAllFriendslist.size(); i++) {
							ReadUserAllFriends userAllFriends = readUserAllFriendslist
									.get(i);
							AllFriends_List.add(userAllFriends);
						}
					}
					if(AllFriends_List.size()>0)
					{
						mFriends_add_layout.setVisibility(View.VISIBLE);
						mFriends_add_tishi_layout.setVisibility(View.GONE);
						mFriends_listview.setAdapter(adapter);
					}else
					{
						mFriends_add_layout.setVisibility(View.GONE);
						mFriends_add_tishi_layout.setVisibility(View.VISIBLE);
					}
				}
			}

			@Override
			public void defail(Object B) {
			}
		});
	}

	private void initUI() {
		mLayout.findViewById(R.id.tab_friends_addbuddy_layout)
				.setOnClickListener(this);
		mLayout.findViewById(R.id.tab_friends_addbuddy)
				.setOnClickListener(this);// ��Ӻ���
		mFriends_add_layout = (RelativeLayout) mLayout
				.findViewById(R.id.friends_add_layout);// �к��ѵ�ʱ��Ĳ���
		mFriends_add_tishi_layout = (RelativeLayout) mLayout
				.findViewById(R.id.friends_add_tishi_layout);// û�к��ѵ�ʱ�����ʾ����
		mFriends_listview = (ListView) mLayout
				.findViewById(R.id.friends_listview);// listview����
		adapter = new MyAdapter();
		
		mFriends_listview.setOnItemClickListener(this);
	}

	class ViewHolder
	{
		ImageView ReadUserAllFriends_head;
		TextView ReadUserAllFriends_name;
	}
	
	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return AllFriends_List.size();
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			View inflater=null;
			ViewHolder holder=null;
			if(convertView==null)
			{
				holder=new ViewHolder();
				LayoutInflater layoutInflater = getActivity().getLayoutInflater();
				inflater=layoutInflater.inflate(R.layout.readuserallfriends_item, null);
				holder.ReadUserAllFriends_head=(ImageView) inflater.findViewById(R.id.ReadUserAllFriends_head);
				holder.ReadUserAllFriends_name=(TextView) inflater.findViewById(R.id.ReadUserAllFriends_name);
				inflater.setTag(holder);
			}else
			{
				inflater=convertView;
				holder=(ViewHolder) inflater.getTag();
			}
			final ReadUserAllFriends allFriends=AllFriends_List.get(position);
			holder.ReadUserAllFriends_name.setText(allFriends.getNickname());
			String avatar_path = allFriends.getAvatar_path();
			String completeURL = beginStr + avatar_path + endStr;
			ImageLoaderUtils.getInstance().LoadImage(
					getActivity(), completeURL,
					holder.ReadUserAllFriends_head);
			
			holder.ReadUserAllFriends_head.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					Toast.makeText(getActivity(), "��ǰ��λ��"+position, Toast.LENGTH_SHORT).show();
					Intent intent=new Intent(getActivity(), FriendsDataActivity.class);
					intent.putExtra("ReadUserAllFriends", allFriends);
					intent.putExtra("fk_user_from", fk_user_from);
					startActivity(intent);
				}
			});
			
			return inflater;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab_friends_addbuddy_layout:
		case R.id.tab_friends_addbuddy:
			tab_friends_addbuddy();
			break;

		default:
			break;
		}
	}

	private void tab_friends_addbuddy() {
		Integer size=AllFriends_List.size();
		Intent intent = new Intent(getActivity(), AddFriendsActivity.class);
		intent.putExtra("size", size);
		startActivity(intent);
	}

	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				ReadUserAllFriends();
				mFriends_swipe_refresh.setRefreshing(false);
				adapter.notifyDataSetChanged();
			}
		}, 3000);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ReadUserAllFriends allFriends = AllFriends_List.get(position);
		Intent intent=new Intent(getActivity(), ChatActivity.class);
		intent.putExtra("allFriends", allFriends);
		startActivity(intent);
	}


}
