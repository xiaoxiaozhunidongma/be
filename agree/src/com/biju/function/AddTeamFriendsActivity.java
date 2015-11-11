package com.biju.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.BJ.javabean.Group_ReadAllUser;
import com.BJ.javabean.Loginback;
import com.BJ.javabean.User;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.SdPkUser;
import com.biju.Interface;
import com.biju.R;
import com.biju.Interface.MyAllfriendsListenner;
import com.biju.function.TeamFriendsActivity.ViewHolder;
import com.github.volley_examples.utils.GsonUtils;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AddTeamFriendsActivity extends Activity implements OnClickListener,OnItemClickListener{

	private ListView mAddTeamFriendsListView;
	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "@!";
	
	private List<User> userList = new ArrayList<User>();
	private MyAddTeamFriendsAdapter adapter;
	
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, Boolean> isSelectMap=new HashMap<Integer, Boolean>();
	private boolean isSelected;
	private List<User> AddTeamFriendsList=new ArrayList<User>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_team_friends);
		initUI();
		ReadAllfriends();
	}

	private void ReadAllfriends() {
		Interface instance = Interface.getInstance();
		User user = new User();
		Integer getsD_pk_user = SdPkUser.getsD_pk_user();
		user.setPk_user(getsD_pk_user);
		instance.readMyAllfriend(this, user);
		instance.setPostListener(new MyAllfriendsListenner() {

			@Override
			public void success(String A) {
				List<Group_ReadAllUser> mGroup_ReadAllUserList = SdPkUser.GetGroup_ReadAllUser;
				Log.e("AddTeamFriendsActivity", "mGroup_ReadAllUserList的长度======="+mGroup_ReadAllUserList.size());
				AddTeamFriendsList.clear();
				userList.clear();
				Log.e("AddTeamFriendsActivity", "返回结果" + A);
				Loginback loginbackread = GsonUtils.parseJson(A,Loginback.class);
				Integer status=loginbackread.getStatusMsg();
				if(1==status){
					userList = loginbackread.getReturnData();
//					Log.e("AddTeamFriendsActivity", "userList.size()111111=====" + userList.size());
					if(userList.size()>0){
							for (int j = 0; j < mGroup_ReadAllUserList.size(); j++) {
								Group_ReadAllUser group_user=mGroup_ReadAllUserList.get(j);
								Integer group_pk_user=group_user.getFk_user();
//								Log.e("AddTeamFriendsActivity", "当前小组的好友ID=====" + group_pk_user);
								for (int i = 0; i <userList.size(); i++) {
									User all_user=userList.get(i);
									Integer all_pk_user=all_user.getPk_user();
//									Log.e("AddTeamFriendsActivity", "所有的好友ID=====" + all_pk_user+ "   "+i);
								if(String.valueOf(all_pk_user).equals(String.valueOf(group_pk_user))){
//									Log.e("AddTeamFriendsActivity", "删除的ID=====" + all_user.getPk_user());
//									Log.e("AddTeamFriendsActivity", "删除的名称=====" + all_user.getNickname());
									userList.remove(userList.get(i));
								}
							}
						}
					}
					adapter.notifyDataSetChanged();
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	private void initUI() {
		findViewById(R.id.AddTeamFriendsBack).setOnClickListener(this);//返回
		findViewById(R.id.AddTeamFriendsOK).setOnClickListener(this);//完成
		mAddTeamFriendsListView = (ListView) findViewById(R.id.AddTeamFriendsListView);
		mAddTeamFriendsListView.setDividerHeight(0);
		adapter = new MyAddTeamFriendsAdapter();
		mAddTeamFriendsListView.setAdapter(adapter);
		mAddTeamFriendsListView.setOnItemClickListener(this);
	}

	class ViewHolder {
		ImageView ReadUserAllFriends_head;
		TextView ReadUserAllFriends_name;
		TextView ReadUserAllFriendsLine1;
		TextView ReadUserAllFriendsLine2;
	}

	class MyAddTeamFriendsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return userList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View inflater = null;
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater layoutInflater = getLayoutInflater();
				inflater = layoutInflater.inflate(R.layout.readuserallfriends_item, null);
				holder.ReadUserAllFriends_head = (ImageView) inflater.findViewById(R.id.ReadUserAllFriends_head);
				holder.ReadUserAllFriends_name = (TextView) inflater.findViewById(R.id.ReadUserAllFriends_name);
				holder.ReadUserAllFriendsLine1 = (TextView) inflater.findViewById(R.id.ReadUserAllFriendsLine1);
				holder.ReadUserAllFriendsLine2 = (TextView) inflater.findViewById(R.id.ReadUserAllFriendsLine2);
				inflater.setTag(holder);
			} else {
				inflater = convertView;
				holder = (ViewHolder) inflater.getTag();
			}

			isSelectMap.put(position, false);
			User user = userList.get(position);
			String nickname = user.getNickname();
			String avatar_path = user.getAvatar_path();
			holder.ReadUserAllFriends_name.setText(nickname);
			String completeURL = beginStr + avatar_path + endStr+ "mini-avatar";
			ImageLoaderUtils.getInstance().LoadImageCricular(AddTeamFriendsActivity.this, completeURL,
					holder.ReadUserAllFriends_head);
			if (position == userList.size() - 1) {
				holder.ReadUserAllFriendsLine1.setVisibility(View.VISIBLE);
				holder.ReadUserAllFriendsLine2.setVisibility(View.GONE);
			} else {
				holder.ReadUserAllFriendsLine1.setVisibility(View.GONE);
				holder.ReadUserAllFriendsLine2.setVisibility(View.VISIBLE);
			}
			return inflater;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.AddTeamFriendsBack:
			AddTeamFriendsBack();
			break;

		default:
			break;
		}
	}


	private void AddTeamFriendsBack() {
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		isSelected=isSelectMap.get(position);
		isSelected=!isSelected;
		isSelectMap.put(position, isSelected);
		User user=userList.get(position);
		if(isSelected){
			view.findViewById(R.id.ReadUserAllFriends_choose).setVisibility(View.VISIBLE);
		}else {
			view.findViewById(R.id.ReadUserAllFriends_choose).setVisibility(View.GONE);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finish();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

}
