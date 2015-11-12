package com.biju.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.widget.TextView;

import com.BJ.javabean.Loginback;
import com.BJ.javabean.User;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.SdPkUser;
import com.biju.Interface;
import com.biju.Interface.MyAllfriendsListenner;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;

public class TeamFriendsActivity extends Activity implements OnClickListener,OnItemClickListener {

	private ListView mTeamFriendsListView;
	private List<User> userList = new ArrayList<User>();
	private MyTeamFriendsAdapter adapter;

	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "@!";
	
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, Boolean> isSelectMap=new HashMap<Integer, Boolean>();
	private boolean isSelected;
	private ArrayList<String> UserList=new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_friends);
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
				Log.e("TeamFriendsActivity", "返回结果" + A);
				Loginback loginbackread = GsonUtils.parseJson(A,Loginback.class);
				userList = loginbackread.getReturnData();
				adapter.notifyDataSetChanged();// 刷新
			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	private void initUI() {
		findViewById(R.id.TeamFriendsOK).setOnClickListener(this);//完成
		findViewById(R.id.TeamFriendsBack).setOnClickListener(this);// 返回
		mTeamFriendsListView = (ListView) findViewById(R.id.TeamFriendsListView);
		mTeamFriendsListView.setDividerHeight(0);
		mTeamFriendsListView.setOnItemClickListener(this);
		adapter = new MyTeamFriendsAdapter();
		mTeamFriendsListView.setAdapter(adapter);
	}

	class ViewHolder {
		ImageView TeamFriends_head;
		TextView TeamFriends_name;
		TextView TeamFriendsLine1;
		TextView TeamFriendsLine2;
	}

	class MyTeamFriendsAdapter extends BaseAdapter {

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
			Integer pk_user = user.getPk_user();
			String nickname = user.getNickname();
			String avatar_path = user.getAvatar_path();
			holder.TeamFriends_name.setText(nickname);
			String completeURL = beginStr + avatar_path + endStr+ "mini-avatar";
			ImageLoaderUtils.getInstance().LoadImageCricular(TeamFriendsActivity.this, completeURL,
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
		case R.id.TeamFriendsBack:
			TeamFriendsBack();
			break;
		case R.id.TeamFriendsOK:
			TeamFriendsOK();
			break;
		default:
			break;
		}
	}

	//完成
	private void TeamFriendsOK() {
		finish();
		SdPkUser.setTeamFriendsList(UserList);
		SharedPreferences TeamFriends_sp=getSharedPreferences("TeamFriends", 0);
		Editor editor=TeamFriends_sp.edit();
		editor.putBoolean("AddTeamFriends", true);
		editor.commit();
	}

	private void TeamFriendsBack() {
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		isSelected=isSelectMap.get(position);
		isSelected=!isSelected;
		isSelectMap.put(position, isSelected);
		User user=userList.get(position);
		if(isSelected){
			view.findViewById(R.id.TeamFriends_choose).setVisibility(View.VISIBLE);
			UserList.add(String.valueOf(user.getPk_user()));
			Log.e("TeamFriendsActivity", "添加的成员===="+user.getPk_user());
		}else {
			view.findViewById(R.id.TeamFriends_choose).setVisibility(View.GONE);
			UserList.remove(String.valueOf(user.getPk_user()));
			Log.e("TeamFriendsActivity", "删除的成员===="+user.getPk_user());
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
