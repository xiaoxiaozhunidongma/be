package com.biju.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.BJ.javabean.AddTeamBack;
import com.BJ.javabean.Group_ReadAllUser;
import com.BJ.javabean.Group_User;
import com.BJ.javabean.Loginback;
import com.BJ.javabean.TeamAddNewMemberModel;
import com.BJ.javabean.User;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.SdPkUser;
import com.biju.Interface;
import com.biju.Interface.MyAllfriendsListenner;
import com.biju.Interface.TeamAddFriendsListenner;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;

public class AddTeamFriendsActivity extends Activity implements OnClickListener,OnItemClickListener{

	private ListView mAddTeamFriendsListView;
	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "@!";
	
	private List<User> userList = new ArrayList<User>();
	private MyAddTeamFriendsAdapter adapter;
	private List<Integer> Pk_user_List=new ArrayList<Integer>();
	
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, Boolean> isSelectMap=new HashMap<Integer, Boolean>();
	private boolean isSelected;
	private List<User> AddTeamFriendsList=new ArrayList<User>();
	private List<Group_User> Group_UserList=new ArrayList<Group_User>();
	private Interface addTeamFriendsInterface;
	private Interface instance;
	private RelativeLayout mAddTeamFriendsNoShow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_team_friends);
		initUI();
		ReadAllfriends();
	}

	private void ReadAllfriends() {
		instance = Interface.getInstance();
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
					if(userList.size()>0){
							for (int j = 0; j < mGroup_ReadAllUserList.size(); j++) {
								Group_ReadAllUser group_user=mGroup_ReadAllUserList.get(j);
								Integer group_pk_user=group_user.getFk_user();
								for (int i = 0; i <userList.size(); i++) {
									User all_user=userList.get(i);
									Integer all_pk_user=all_user.getPk_user();
								if(String.valueOf(all_pk_user).equals(String.valueOf(group_pk_user))){
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
		
		instance.setPostListener(new TeamAddFriendsListenner() {
			
			@Override
			public void success(String A) {
				Log.e("AddTeamFriendsActivity", "返回结果===="+A);
				AddTeamBack addTeamBack=GsonUtils.parseJson(A, AddTeamBack.class);
				Integer status=addTeamBack.getStatusMsg();
				if(1==status){
					SlidingActivity.readGroupMember.ReadGroupMember();
				}
			}
			
			@Override
			public void defail(Object B) {
				
			}
		});
	}

	private void Dialog() {
		final SweetAlertDialog sd = new SweetAlertDialog(AddTeamFriendsActivity.this);
		sd.setTitleText("提示");
		sd.setContentText("是否要将选择的1个好友加入小组?");
		sd.setCancelText("不~~~");
		sd.setConfirmText("是的");
		sd.showCancelButton(true);
		sd.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sDialog) {
				sd.cancel();
				finish();
			}
		}).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sDialog) {
				sd.cancel();
				AddTeamFriendsOK();
				finish();
				
			}
		}).show();
	}
	
	private void initUI() {
		mAddTeamFriendsNoShow = (RelativeLayout) findViewById(R.id.AddTeamFriendsNoShow);
		findViewById(R.id.AddTeamFriendsBack).setOnClickListener(this);//返回
		findViewById(R.id.AddTeamFriendsOK).setOnClickListener(this);//完成
		mAddTeamFriendsListView = (ListView) findViewById(R.id.AddTeamFriendsListView);
		mAddTeamFriendsListView.setDividerHeight(0);
		adapter = new MyAddTeamFriendsAdapter();
		mAddTeamFriendsListView.setAdapter(adapter);
		mAddTeamFriendsListView.setOnItemClickListener(this);
	}

	class ViewHolder {
		ImageView TeamFriends_head;
		TextView TeamFriends_name;
		TextView TeamFriendsLine1;
		TextView TeamFriendsLine2;
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
				inflater = layoutInflater.inflate(R.layout.teamfriends_item, null);
				holder.TeamFriends_head = (ImageView) inflater.findViewById(R.id.TeamFriends_head);
				holder.TeamFriends_name = (TextView) inflater.findViewById(R.id.TeamFriends_name);
				holder.TeamFriendsLine1 = (TextView) inflater.findViewById(R.id.TeamFriendsLine1);
				holder.TeamFriendsLine2 = (TextView) inflater.findViewById(R.id.TeamFriendsLine2);
				inflater.setTag(holder);
			} else {
				inflater = convertView;
				holder = (ViewHolder) inflater.getTag();
			}

			isSelectMap.put(position, false);
			User user = userList.get(position);
			String nickname = user.getNickname();
			String avatar_path = user.getAvatar_path();
			holder.TeamFriends_name.setText(nickname);
			String completeURL = beginStr + avatar_path + endStr+ "mini-avatar";
			ImageLoaderUtils.getInstance().LoadImageCricular(AddTeamFriendsActivity.this, completeURL,
					holder.TeamFriends_head);
			if (position == userList.size() - 1) {
				holder.TeamFriendsLine1.setVisibility(View.VISIBLE);
				holder.TeamFriendsLine2.setVisibility(View.GONE);
			} else {
				holder.TeamFriendsLine1.setVisibility(View.GONE);
				holder.TeamFriendsLine2.setVisibility(View.VISIBLE);
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
		case R.id.AddTeamFriendsOK:
			mAddTeamFriendsNoShow.setVisibility(View.GONE);
			Dialog();
			break;
		default:
			break;
		}
	}


	//完成
	private void AddTeamFriendsOK() {
		if(Pk_user_List.size()>0){
			Integer fk_group=GroupActivity.getPk_group();
			for (int i = 0; i < Pk_user_List.size(); i++) {
				Integer pk_user=Pk_user_List.get(i);
				Group_User group_User=new Group_User();
				group_User.setFk_group(fk_group);
				group_User.setFk_user(pk_user);
				group_User.setMessage_warn(1);
				group_User.setParty_warn(1);
				group_User.setPublic_phone(0);
				group_User.setRole(2);
				group_User.setStatus(1);
				Group_UserList.add(group_User);
			}
			Group_User[] members=new Group_User[Group_UserList.size()];
			for (int i = 0; i < members.length; i++) {
				Group_User group_User = Group_UserList.get(i);
				members[i]=group_User;
			}
			TeamAddNewMemberModel TeamAddNewMemberModel=new TeamAddNewMemberModel(members);
			instance.TeamAddFriends(AddTeamFriendsActivity.this, TeamAddNewMemberModel);
			
		}
	}

	private void AddTeamFriendsBack() {
		finish();
		overridePendingTransition(R.anim.left, R.anim.right);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		isSelected=isSelectMap.get(position);
		isSelected=!isSelected;
		isSelectMap.put(position, isSelected);
		User user=userList.get(position);
		if(isSelected){
			Integer pk_user=user.getPk_user();
			Pk_user_List.add(pk_user);
			view.findViewById(R.id.TeamFriends_choose).setVisibility(View.VISIBLE);
		}else {
			Integer pk_user=user.getPk_user();
			Pk_user_List.remove(pk_user);
			view.findViewById(R.id.TeamFriends_choose).setVisibility(View.GONE);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			AddTeamFriendsBack();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

}
