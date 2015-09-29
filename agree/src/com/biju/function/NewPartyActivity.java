package com.biju.function;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.BJ.javabean.Group;
import com.BJ.javabean.Groupback;
import com.BJ.javabean.User;
import com.BJ.utils.PreferenceUtils;
import com.BJ.utils.SdPkUser;
import com.BJ.utils.homeImageLoaderUtils;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.readUserGroupMsgListenner;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;

public class NewPartyActivity extends Activity implements OnClickListener{

	private GridView newparty_gridview;
	private List<Group> users;
	private ArrayList<Group> list = new ArrayList<Group>();
	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "@!";
	// 完整路径completeURL=beginStr+result.filepath+endStr;
	private String completeURL = "";
	private MyGridviewAdapter adapter;
	private Integer sD_pk_user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_party);
		//获取sd卡中的pk_user
		sD_pk_user = SdPkUser.getsD_pk_user();
		Log.e("NewPartyActivity", "从SD卡中获取到的Pk_user" + sD_pk_user);
		
		initUI();
		initNewTeam();
	}

	@Override
	protected void onStart() {
		initdate();
		super.onStart();
	}
	//当用户退回到小组选择时之前所选的地点和时间清除
	private void initdate() {
		SharedPreferences sp = getSharedPreferences("isdate", 0);
		Editor editor = sp.edit();
		editor.putBoolean("date", false);
		editor.commit();
	}

	private void initNewTeam() {
		ReadTeam(sD_pk_user);
	}

	private void ReadTeam(int pk_user) {
		Interface homeInterface = Interface.getInstance();
		User homeuser = new User();
		homeuser.setPk_user(pk_user);
		homeInterface.readUserGroupMsg(NewPartyActivity.this, homeuser);
		homeInterface.setPostListener(new readUserGroupMsgListenner() {

			@Override
			public void success(String A) {
				list.clear();
				Groupback homeback = GsonUtils.parseJson(A, Groupback.class);
				int homeStatusMsg = homeback.getStatusMsg();
				if (homeStatusMsg == 1) {
					users = homeback.getReturnData();
					if (users.size() > 0) {
						for (int i = 0; i < users.size(); i++) {
							Group readhomeuser = users.get(i);
							list.add(readhomeuser);
						}
						adapter.notifyDataSetChanged();
					}
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
		homeInterface.readUserGroupMsg(NewPartyActivity.this, homeuser);
	}

	private void initUI() {
		findViewById(R.id.newparty_back_layout).setOnClickListener(this);
		findViewById(R.id.newparty_back).setOnClickListener(this);
		newparty_gridview = (GridView) findViewById(R.id.newparty_gridview);
		newparty_gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));// 去除gridview点击后的背景颜色
		adapter = new MyGridviewAdapter();
		newparty_gridview.setAdapter(adapter);
		newparty_gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Group group = list.get(arg2);
				int fk_group=group.getPk_group();
				Intent intent=new Intent(NewPartyActivity.this, AddNewPartyActivity.class);
				intent.putExtra(IConstant.Fk_group, fk_group);
				intent.putExtra(IConstant.IsSchedule, false);
				startActivity(intent);
			}
		});
	}
	
	class MyGridviewAdapter extends BaseAdapter
	{

		private ImageView mNewParty_item_head;
		private TextView mNewParty_item_name;

		@Override
		public int getCount() {
			Log.e("NewPartyActivity", "list的长度======" + list.size());
			return list.size();
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
			View inflater=null;
			LayoutInflater layoutInflater = getLayoutInflater();
			inflater=layoutInflater.inflate(R.layout.home_gridview_item, null);
			mNewParty_item_head = (ImageView) inflater.findViewById(R.id.home_item_head);
			mNewParty_item_name = (TextView) inflater.findViewById(R.id.home_item_name);
			Group newparty_gridview = list.get(position);
			String newpartyAvatar_path = newparty_gridview.getAvatar_path();
			String homenickname = newparty_gridview.getName();
			mNewParty_item_name.setText(homenickname);
			completeURL = beginStr + newpartyAvatar_path + endStr+"group-front-cover";
			PreferenceUtils.saveImageCache(NewPartyActivity.this,completeURL);
			homeImageLoaderUtils.getInstance().LoadImage(NewPartyActivity.this, completeURL, mNewParty_item_head);
//			AsynImageLoader asynImageLoader = new AsynImageLoader();
//			asynImageLoader.showImageAsyn(mNewParty_item_head, completeURL, R.drawable.newteam,NewPartyActivity.this);
			return inflater;
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_party, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.newparty_back_layout:
		case R.id.newparty_back:
			newparty_back();
			break;

		default:
			break;
		}
	}

	private void newparty_back() {
		finish();
	}

}
