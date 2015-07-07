package com.biju.function;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.BJ.javabean.Group;
import com.BJ.javabean.Groupback;
import com.BJ.javabean.User;
import com.BJ.utils.PreferenceUtils;
import com.BJ.utils.homeImageLoaderUtils;
import com.biju.Interface;
import com.biju.R;
import com.biju.Interface.UserInterface;
import com.biju.login.LoginActivity;
import com.github.volley_examples.utils.GsonUtils;
import com.tencent.download.core.strategy.DownloadGlobalStrategy.StrategyInfo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

public class NewPartyActivity extends Activity implements OnClickListener{

	private GridView newparty_gridview;
	private int returndata;
	private boolean login;
	private boolean isRegistered_one;
	private List<Group> users;
	private ArrayList<Group> list = new ArrayList<Group>();
	private String beginStr = "http://201139.image.myqcloud.com/201139/0/";
	private String endStr = "/original";
	// 完整路径completeURL=beginStr+result.filepath+endStr;
	private String completeURL = "";
	private MyGridviewAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_party);
		SharedPreferences sp = getSharedPreferences(
				"Registered", 0);
		isRegistered_one = sp.getBoolean("isRegistered_one", false);
		returndata = sp.getInt("returndata", returndata);
		SharedPreferences sp1 = getSharedPreferences("isLogin", 0);
		login = sp1.getBoolean("Login", false);
		
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
		if (isRegistered_one) {
			ReadTeam(returndata);
		} else {
			if(login)
			{
				int pk_user = LoginActivity.pk_user;
				ReadTeam(pk_user);
			}else
			{
				ReadTeam(returndata);
			}
		}
	}

	private void ReadTeam(int pk_user) {
		Interface homeInterface = new Interface();
		User homeuser = new User();
		homeuser.setPk_user(pk_user);
		homeInterface.readUserGroupMsg(NewPartyActivity.this, homeuser);
		homeInterface.setPostListener(new UserInterface() {

			@Override
			public void success(String A) {
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
				SharedPreferences sp=getSharedPreferences("isParty_fk_group", 0);
				Editor editor=sp.edit();
				editor.putInt("fk_group", fk_group);
				editor.commit();
				Intent intent=new Intent(NewPartyActivity.this, MapActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
	class MyGridviewAdapter extends BaseAdapter
	{

		private ImageView mNewParty_item_head;
		private TextView mNewParty_item_name;

		@Override
		public int getCount() {
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
			String newpartyAvatar_path = newparty_gridview
					.getAvatar_path();
			String homenickname = newparty_gridview.getName();
			mNewParty_item_name.setText(homenickname);
			completeURL = beginStr + newpartyAvatar_path + endStr;
			PreferenceUtils.saveImageCache(NewPartyActivity.this,
					completeURL);
			homeImageLoaderUtils.getInstance().LoadImage(
					NewPartyActivity.this, completeURL, mNewParty_item_head);
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
