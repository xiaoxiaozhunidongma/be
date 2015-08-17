package com.biju.function;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.BJ.javabean.Exitback;
import com.BJ.javabean.Group;
import com.BJ.javabean.GroupCodeback;
import com.BJ.javabean.GroupCodeback2;
import com.BJ.javabean.Group_Code2;
import com.BJ.javabean.Group_ReadAllUser;
import com.BJ.javabean.Group_ReadAllUserback;
import com.BJ.javabean.Group_User;
import com.BJ.javabean.Groupuserback;
import com.BJ.javabean.Teamupdateback;
import com.BJ.utils.GridViewWithHeaderAndFooter;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.PreferenceUtils;
import com.BJ.utils.RefreshActivity;
import com.BJ.utils.SdPkUser;
import com.biju.Interface;
import com.biju.Interface.produceRequestCodeListenner;
import com.biju.Interface.readAllPerRelationListenner;
import com.biju.Interface.readUserGroupRelationListenner;
import com.biju.Interface.updateGroupSetListenner;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;

@SuppressLint("InlinedApi")
public class TeamSettingActivity extends Activity implements OnClickListener,
		SwipeRefreshLayout.OnRefreshListener {

	private String beginStr = "http://201139.image.myqcloud.com/201139/0/";
	private String endStr = "/original";
	private String useravatar_path;
	private String completeURL;
	private String TestcompleteURL = beginStr
			+ "1ddff6cf-35ac-446b-8312-10f4083ee13d" + endStr;
	private int pk_group;
	private TextView mTeamSetting_requestcode;
	private Interface readuserinter;
	private TextView mTeamSetting_message;
	private TextView mTeamSetting_chat;
	private TextView mTeamSetting_phone;
	private int ismessage;
	private int ischat;
	private int isphone;
	private boolean isMessage;
	private boolean isChat;
	private boolean isPhone;
	private int message = 0;
	private int chat = 0;
	private int phone = 0;
	private boolean isExit;
	private ArrayList<Group_ReadAllUser> group_readalluser_list = new ArrayList<Group_ReadAllUser>();
	private SwipeRefreshLayout mTeamsetting_swipe_refresh;
	private GridViewWithHeaderAndFooter mTeamsetting_gridview;
	private MyAdapter adapter;
	private View mHeadView;
	private Integer sD_pk_user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_setting);
		
		//获取sd卡中的pk_user
		sD_pk_user = SdPkUser.getsD_pk_user();
		Log.e("TeamSettingActivity", "从SD卡中获取到的Pk_user" + sD_pk_user);
		
		Intent intent = getIntent();
		pk_group = intent.getIntExtra("Group", pk_group);

		ReadUser();
		initreadUserGroupRelation1();
		returndata();
		initUI();
		Log.e("TeamSettingActivity", "进入了=========+onCreate()");
	}
	
	@Override
	protected void onStart() {
		Log.e("TeamSettingActivity", "进入了=========+onStart()");
		super.onStart();
	}

	private void initreadUserGroupRelation1() {
		Group_User group_User = new Group_User();
		group_User.setFk_group(pk_group);
		group_User.setFk_user(sD_pk_user);
		readuserinter.readUserGroupRelation(TeamSettingActivity.this,
				group_User);
	}

	private void initSwitch() {
		if (ismessage == 0) {
			isMessage = false;
			mTeamSetting_message.setText("已关闭");
			message = 0;
		} else {
			isMessage = true;
			mTeamSetting_message.setText("已开启");
			message = 1;
		}

		if (ischat == 0) {
			isChat = false;
			mTeamSetting_chat.setText("已关闭");
			chat = 0;
		} else {
			isChat = true;
			mTeamSetting_chat.setText("已开启");
			chat = 1;
		}

		if (isphone == 0) {
			isPhone = false;
			mTeamSetting_phone.setText("未公开");
			phone = 0;
		} else {
			isPhone = true;
			mTeamSetting_phone.setText("已公开");
			phone = 1;
		}
	}

	private void returndata() {
		Group readAllPerRelation_group = new Group();
		readAllPerRelation_group.setPk_group(pk_group);
		readuserinter.readAllPerRelation(TeamSettingActivity.this,
				readAllPerRelation_group);
	}

	private void ReadUser() {
		readuserinter = Interface.getInstance();
		readuserinter.setPostListener(new updateGroupSetListenner() {

			@Override
			public void success(String A) {
				if (isExit) {
					Exitback exitback = GsonUtils.parseJson(A, Exitback.class);
					Integer exitstatus = exitback.getStatusMsg();
					if (exitstatus == 1) {
						Log.e("TeamSettingActivity", "返回是否退出成功的结果-------" + A);
						// 发广播进行更新gridview
						Intent intent = new Intent();
						intent.setAction("isRefresh");
						intent.putExtra("refresh", true);
						sendBroadcast(intent);

						//关闭GroupActivity界面
						for (int i = 0; i < RefreshActivity.activList_1.size(); i++) {
							RefreshActivity.activList_1.get(i).finish();
						}
						finish();
					}
				} else {
					Teamupdateback teamupdateback = GsonUtils.parseJson(A,
							Teamupdateback.class);
					int statusmsg = teamupdateback.getStatusMsg();
					if (statusmsg == 1) {
						Log.e("TeamSettingActivity", "更新完的返回结果" + A);
						SharedPreferences teamsetting_sp = getSharedPreferences(
								"Setting", 0);
						Editor editor = teamsetting_sp.edit();
						editor.putBoolean("setting", true);
						editor.commit();
						finish();
					} else {
						Toast.makeText(TeamSettingActivity.this,
								"更新设置失败，请重新再试!", Toast.LENGTH_SHORT).show();
					}
				}

			}

			@Override
			public void defail(Object B) {

			}
		});
		readuserinter.setPostListener(new produceRequestCodeListenner() {

			@Override
			public void success(String A) {

				Log.e("TeamSettingActivity", "=========" + A);
				try {
					JSONObject jsonObject = new JSONObject(A);
					Object object = jsonObject.get("returnData");
					if (object.toString().length() > 4) {
						java.lang.reflect.Type type = new TypeToken<GroupCodeback2>() {
						}.getType();
						GroupCodeback2 groupcodeback2 = GsonUtils
								.parseJsonArray(A, type);
						int Group_statusmsg2 = groupcodeback2.getStatusMsg();
						List<Group_Code2> grouplsit = (List<Group_Code2>) groupcodeback2
								.getReturnData();
						if (Group_statusmsg2 == 1) {
							Group_Code2 groupcode = grouplsit.get(0);
							String requestcode = groupcode.getPk_group_code();
							mTeamSetting_requestcode.setText(requestcode);
						}
					} else {
						GroupCodeback groupcodeback = GsonUtils.parseJson(A,
								GroupCodeback.class);
						int Group_statusmsg = groupcodeback.getStatusMsg();
						if (Group_statusmsg == 1) {
							String requestcode = groupcodeback.getReturnData();
							mTeamSetting_requestcode.setText(requestcode);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void defail(Object B) {

			}
		});

		readuserinter.setPostListener(new readAllPerRelationListenner() {

			@Override
			public void success(String A) {
				Group_ReadAllUserback group_ReadAllUserback = GsonUtils
						.parseJson(A, Group_ReadAllUserback.class);
				int status = group_ReadAllUserback.getStatusMsg();
				if (status == 1) {
					Log.e("TeamSettingActivity", "读取出小组中的所有用户========" + A);
					List<Group_ReadAllUser> allUsers = group_ReadAllUserback
							.getReturnData();
					if (allUsers.size() > 0) {
						for (int i = 0; i < allUsers.size(); i++) {
							Group_ReadAllUser readAllUser = allUsers.get(i);
							group_readalluser_list.add(readAllUser);
						}
						Log.e("TeamSettingActivity", "加入到list中的东西====="
								+ group_readalluser_list.toString());
					}
					if (group_readalluser_list.size() > 0) {
						mTeamsetting_gridview.setAdapter(adapter);
					}
				}
			}

			@Override
			public void defail(Object B) {

			}
		});

		readuserinter.setPostListener(new readUserGroupRelationListenner() {
			@Override
			public void success(String A) {
				Groupuserback groupuserback = GsonUtils.parseJson(A,
						Groupuserback.class);
				Integer statusMsg = groupuserback.getStatusMsg();
				if (statusMsg == 1) {
					Log.e("GroupActivity", "返回小组关系ID====" + A);
					List<Group_User> groupuser_returnData = groupuserback
							.getReturnData();
					if (groupuser_returnData.size() > 0) {
						Group_User group_user = groupuser_returnData.get(0);
						ischat = group_user.getMessage_warn();
						ismessage = group_user.getParty_warn();
						isphone = group_user.getPublic_phone();
						Log.e("GroupActivity", "小组的聚会信息的提醒--------" + ismessage);
						Log.e("GroupActivity", "小组的聊天信息的提醒--------" + ischat);
						Log.e("GroupActivity", "小组的公开手机号码--------" + isphone);
						initSwitch();
					}
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	private void initUI() {
		findViewById(R.id.TeamSetting_back_layout).setOnClickListener(this);// 返回
		findViewById(R.id.TeamSetting_back).setOnClickListener(this);
		findViewById(R.id.TeamSetting_save_layout).setOnClickListener(this);// 保存
		findViewById(R.id.TeamSetting_save).setOnClickListener(this);

		mTeamsetting_swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.teamsetting_swipe_refresh);
		mTeamsetting_swipe_refresh.setOnRefreshListener(this);
		// 顶部刷新的样式
		mTeamsetting_swipe_refresh.setColorSchemeResources(
				android.R.color.holo_red_light,
				android.R.color.holo_green_light,
				android.R.color.holo_blue_bright,
				android.R.color.holo_orange_light);

		mTeamsetting_gridview = (GridViewWithHeaderAndFooter) findViewById(R.id.teamsetting_gridview);
		mHeadView = View.inflate(TeamSettingActivity.this,
				R.layout.teamsetting_foot_item, null);
		mTeamsetting_gridview.addFooterView(mHeadView);
		mTeamSetting_requestcode = (TextView) mHeadView
				.findViewById(R.id.TeamSetting_requestcode);// 生成邀请码
		mTeamSetting_requestcode.setOnClickListener(this);
		mTeamSetting_message = (TextView) mHeadView
				.findViewById(R.id.TeamSetting_message);// 聚会信息提醒开关
		mTeamSetting_message.setOnClickListener(this);
		mTeamSetting_chat = (TextView) mHeadView
				.findViewById(R.id.TeamSetting_chat);// 聊天信息开关
		mTeamSetting_chat.setOnClickListener(this);
		mTeamSetting_phone = (TextView) mHeadView
				.findViewById(R.id.TeamSetting_phone);// 公开手机号码开关
		mTeamSetting_phone.setOnClickListener(this);
		mHeadView.findViewById(R.id.TeamSetting_exit).setOnClickListener(this);// 退出小组

		adapter = new MyAdapter();
		// mTeamsetting_gridview.setAdapter(adapter);

	}

	class ViewHolder {
		ImageView TeamSetting_head;
		TextView TeamSetting_number;
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return group_readalluser_list.size();
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
				LayoutInflater layoutInflater = getLayoutInflater();
				inflater = layoutInflater.inflate(
						R.layout.teamsetting_gridview_item, null);
				holder.TeamSetting_head = (ImageView) inflater
						.findViewById(R.id.TeamSetting_head);
				holder.TeamSetting_number = (TextView) inflater
						.findViewById(R.id.TeamSetting_number);
				inflater.setTag(holder);
			} else {
				inflater = convertView;
				holder = (ViewHolder) inflater.getTag();
			}

			Log.e("TeamSettingActivity", "容器的长度========"
					+ group_readalluser_list.size());
			if (group_readalluser_list.size() > 0) {
				Group_ReadAllUser group_ReadAllUser = group_readalluser_list
						.get(position);
				Integer pk_user = group_ReadAllUser.getPk_user();
				holder.TeamSetting_number.setText(pk_user + "");
				useravatar_path = group_ReadAllUser.getAvatar_path();
				completeURL = beginStr + useravatar_path + endStr;
				PreferenceUtils.saveImageCache(TeamSettingActivity.this,
						completeURL);// 存SP
				ImageLoaderUtils.getInstance().LoadImage(
						TeamSettingActivity.this, completeURL,
						holder.TeamSetting_head);
			}
			return inflater;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.team_setting, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.TeamSetting_back_layout:
		case R.id.TeamSetting_back:
			TeamSetting_back();
			break;
		case R.id.TeamSetting_save_layout:
		case R.id.TeamSetting_save:
			TeamSetting_save();
			break;
		case R.id.TeamSetting_requestcode:
			TeamSetting_requestcode();
			break;
		case R.id.TeamSetting_message:
			TeamSetting_message();
			break;
		case R.id.TeamSetting_chat:
			TeamSetting_chat();
			break;
		case R.id.TeamSetting_phone:
			TeamSetting_phone();
			break;
		case R.id.TeamSetting_exit:
			TeamSetting_exit();
			break;
		default:
			break;
		}
	}

	private void TeamSetting_exit() {
		isExit = true;
		Group_User group_user = new Group_User();
		group_user.setPk_group_user(GroupActivity.pk_group_user);
		group_user.setFk_user(sD_pk_user);
		group_user.setFk_group(pk_group);
		group_user.setRole(0);
		group_user.setStatus(0);
		readuserinter.updateGroupSet(TeamSettingActivity.this, group_user);
	}

	private void TeamSetting_phone() {
		isPhone = !isPhone;
		if (!isPhone) {
			mTeamSetting_phone.setText("未公开");
			phone = 0;
		} else {
			mTeamSetting_phone.setText("已公开");
			phone = 1;
		}
	}

	private void TeamSetting_chat() {
		isChat = !isChat;
		if (!isChat) {
			mTeamSetting_chat.setText("已关闭");
			chat = 0;
		} else {
			mTeamSetting_chat.setText("已开启");
			chat = 1;
		}
	}

	private void TeamSetting_message() {
		isMessage = !isMessage;
		if (!isMessage) {
			mTeamSetting_message.setText("已关闭");
			message = 0;
		} else {
			mTeamSetting_message.setText("已开启");
			message = 1;
		}
	}

	private void TeamSetting_requestcode() {
		Group Group_teamsetting = new Group();
		Group_teamsetting.setPk_group(pk_group);
		Log.e("TeamSettingActivity", "Group_teamsetting" + pk_group);
		readuserinter.produceRequestCode(TeamSettingActivity.this,
				Group_teamsetting);
	}

	// 更新小组设置
	private void TeamSetting_save() {
		Group_User group_user = new Group_User();
		group_user.setPk_group_user(GroupActivity.pk_group_user);
		group_user.setFk_user(sD_pk_user);
		group_user.setFk_group(pk_group);
		group_user.setParty_warn(message);// 聚会信息
		Log.e("TeamSettingActivity", "需要传入的聚会信息ID--------" + message);
		group_user.setPublic_phone(phone);
		Log.e("TeamSettingActivity", "需要传入的电话ID--------" + phone);
		group_user.setMessage_warn(chat);// 聊天信息
		Log.e("TeamSettingActivity", "需要传入的聊天信息ID--------" + chat);
		group_user.setParty_update(1);
		group_user.setPhoto_update(1);
		group_user.setChat_update(1);
		group_user.setRole(1);
		group_user.setStatus(1);
		readuserinter.updateGroupSet(TeamSettingActivity.this, group_user);
	}

	private void TeamSetting_back() {
		finish();
	}

	@Override
	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				adapter.notifyDataSetChanged();
				mTeamsetting_swipe_refresh.setRefreshing(false);
			}
		}, 3000);
	}

}
