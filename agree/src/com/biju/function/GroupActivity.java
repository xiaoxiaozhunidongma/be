package com.biju.function;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BJ.javabean.Group_ReadAllUser;
import com.BJ.javabean.Group_User;
import com.BJ.javabean.Groupuserback;
import com.BJ.utils.SdPkUser;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.readUserGroupRelationListenner;
import com.biju.R;
import com.biju.function.SlidingActivity.MyMemBerAdapter;
import com.fragment.CommonFragment;
import com.fragment.PhotoFragment2;
import com.github.volley_examples.utils.GsonUtils;

@SuppressWarnings("unused")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class GroupActivity extends FragmentActivity implements OnClickListener {
	public static int pk_group;

	public static int getPk_group() {
		return pk_group;
	}

	public static void setPk_group(int pk_group) {
		GroupActivity.pk_group = pk_group;
	}

	private Interface groupInterface;
	public static Integer pk_group_user;

	public static Integer getPk_group_user() {
		return pk_group_user;
	}

	public static void setPk_group_user(Integer pk_group_user) {
		GroupActivity.pk_group_user = pk_group_user;
	}

	private boolean photo;
	private boolean partyDetails;

	private GestureDetector mGestureDetector;
	private int verticalMinDistance = 180;
	private int minVelocity = 0;

	private Integer sD_pk_user;
	private TextView mGroup_setting;
	private TextView mGroup_setting_title;

	public static GetSliding getSliding;

	private String name;
	private ListView mSlidingMenu_member_listView;
	private ArrayList<Group_ReadAllUser> Group_Readalluser_List = new ArrayList<Group_ReadAllUser>();
	private MyMemBerAdapter adapter;

	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "";
	private String completeURL;
	private String TestcompleteURL = beginStr
			+ "1ddff6cf-35ac-446b-8312-10f4083ee13d" + endStr;
	private TextView mSlidingMenu_requestcode_code;
	private RelativeLayout mSlidingMenu_requestcode;
	private RelativeLayout mSlidingMenu_Team_Setting;
	
	private boolean isCode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_tab);
		// 获取sd卡中的pk_user
		sD_pk_user = SdPkUser.getsD_pk_user();
		Log.e("GroupActivity", "从SD卡中获取到的Pk_user" + sD_pk_user);

		initFragment();
		Intent intent = getIntent();
		pk_group = intent.getIntExtra(IConstant.HomePk_group, pk_group);
		name = intent.getStringExtra(IConstant.HomeGroupName);
		initUI();

		SharedPreferences PartyDetails_sp = getSharedPreferences(IConstant.IsPartyDetails_, 0);
		partyDetails = PartyDetails_sp.getBoolean(IConstant.PartyDetails, false);

		initInterface();// 监听
		initreadUserGroupRelation();// 获取小组的关系ID
		initSliding();
	}

	@SuppressWarnings("static-access")
	private void initSliding() {
		GetSliding getSliding = new GetSliding() {

			@Override
			public void SlidingClick() {
				mGroup_setting.setText("成员");
			}
		};
		this.getSliding = getSliding;
	}

	private void initUI() {
		findViewById(R.id.group_back_layout).setOnClickListener(this);// 返回
		findViewById(R.id.group_back).setOnClickListener(this);
		findViewById(R.id.group_setting_layout).setOnClickListener(this);
		mGroup_setting = (TextView) findViewById(R.id.group_setting);
		mGroup_setting.setOnClickListener(this);// 设置
		mGroup_setting_title = (TextView) findViewById(R.id.group_setting_title);// 小组名称
		mGroup_setting_title.setText(name);
	}

	private void initFragment() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		CommonFragment commonFragment = new CommonFragment();
		ft.add(R.id.container, commonFragment);
		ft.commit();
	}

	private void initInterface() {
		groupInterface = Interface.getInstance();
		groupInterface.setPostListener(new readUserGroupRelationListenner() {
			@Override
			public void success(String A) {
				Groupuserback groupuserback = GsonUtils.parseJson(A,Groupuserback.class);
				Integer statusMsg = groupuserback.getStatusMsg();
				if (statusMsg == 1) {
					Log.e("GroupActivity", "返回小组关系ID====" + A);
					List<Group_User> groupuser_returnData = groupuserback.getReturnData();
					if (groupuser_returnData.size() > 0) {
						Group_User group_user = groupuser_returnData.get(0);
						pk_group_user = group_user.getPk_group_user();

					}
				}
			}

			@Override
			public void defail(Object B) {

			}
		});

	}

	private void initreadUserGroupRelation() {
		Group_User group_User = new Group_User();
		group_User.setFk_group(pk_group);
		group_User.setFk_user(sD_pk_user);
		groupInterface.readUserGroupRelation(GroupActivity.this, group_User);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.group_back_layout:
		case R.id.group_back:
			group_back();
			break;
		case R.id.group_setting_layout:
		case R.id.group_setting:
			group_setting();
			break;

		default:
			break;
		}
	}

	private void group_setting() {
		mGroup_setting.setText("关闭");
		Intent intent = new Intent(GroupActivity.this, SlidingActivity.class);
		intent.putExtra("group_group", pk_group);
		startActivity(intent);
		overridePendingTransition(R.anim.in_item, R.anim.out_item);
	}

	public void group_back() {
		finish();
	}

	@Override
	protected void onStop() {
		super.onStop();
		SharedPreferences sp = getSharedPreferences("isPhoto", 0);
		Editor editor = sp.edit();
		editor.putBoolean("Photo", false);
		editor.commit();

		SharedPreferences PartyDetails_sp = getSharedPreferences(IConstant.IsPartyDetails_, 0);
		Editor PartyDetails_editor = PartyDetails_sp.edit();
		PartyDetails_editor.putBoolean(IConstant.PartyDetails, false);
		PartyDetails_editor.commit();

		SharedPreferences refresh_sp = getSharedPreferences(IConstant.AddRefresh, 0);
		Editor editor2 = refresh_sp.edit();
		editor2.putBoolean(IConstant.IsAddRefresh, false);
		editor2.commit();
		//退出当前小组后，在Sliding中的点击效果也要取消掉,而且重新读取小组中的所有用户
		SharedPreferences Sliding_sp=getSharedPreferences(IConstant.SlidingClick, 0);
		Editor Sliding_editor=Sliding_sp.edit();
		Sliding_editor.putInt(IConstant.Click, 0);
		Sliding_editor.commit();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("GroupActivity", "调用了这个onActivityResult");
		Log.e("GroupActivity", "调用了这个requestCode=="+requestCode);
		Log.e("GroupActivity", "调用了这个data=="+data);
		PhotoFragment2.onActivityResultInterface.onActivityResult(requestCode, resultCode, data);
	}

	public interface GetSliding {
		void SlidingClick();
	}
}
