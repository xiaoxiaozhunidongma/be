package com.biju.function;

import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.BJ.javabean.Group;
import com.BJ.javabean.GroupCodeback;
import com.BJ.javabean.GroupCodeback2;
import com.BJ.javabean.Group_Code2;
import com.BJ.javabean.Group_User;
import com.BJ.javabean.Loginback;
import com.BJ.javabean.Teamupdateback;
import com.BJ.javabean.User;
import com.BJ.utils.Ifwifi;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.PreferenceUtils;
import com.biju.Interface;
import com.biju.Interface.UserInterface;
import com.biju.R;
import com.biju.login.LoginActivity;
import com.github.volley_examples.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;

public class TeamSettingActivity extends Activity implements OnClickListener {

	private ImageView mTeamSetting_head;
	private TextView mTeamSetting_number;
	private String beginStr = "http://201139.image.myqcloud.com/201139/0/";
	private String endStr = "/original";
	private String useravatar_path;
	private String completeURL;
	private String TestcompleteURL = beginStr
			+ "1ddff6cf-35ac-446b-8312-10f4083ee13d" + endStr;
	private int pk_group;
	private TextView mTeamSetting_requestcode;
	private Interface readuserinter;
	private boolean isProduce;
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
	private int returndata;
	private boolean isRegistered_one;
	private boolean login;
	private boolean isupdate;
	private boolean test;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_setting);
		// 得到fk_user
		SharedPreferences sp2 = getSharedPreferences("Registered", 0);
		isRegistered_one = sp2.getBoolean("isRegistered_one", false);
		returndata = sp2.getInt("returndata", returndata);
		SharedPreferences sp1 = getSharedPreferences("isLogin", 0);
		login = sp1.getBoolean("Login", false);

		Intent intent = getIntent();
		pk_group = intent.getIntExtra("Group", pk_group);
		initUI();
		boolean isWIFI = Ifwifi.getNetworkConnected(TeamSettingActivity.this);
		if (isWIFI) {
			returndata();
		} else {
			ImageLoaderUtils.getInstance().LoadImage(TeamSettingActivity.this,
					completeURL, mTeamSetting_head);
		}
		SharedPreferences sp = getSharedPreferences("Switch", 0);
		ismessage = sp.getInt("ismessage", 0);
		Log.e("TeamSettingActivity", "小组的聚会信息的提醒--------" + ismessage);
		ischat = sp.getInt("ischat", 0);
		Log.e("TeamSettingActivity", "小组的聊天信息的提醒--------" + ischat);
		isphone = sp.getInt("isphone", 0);
		Log.e("TeamSettingActivity", "小组的公开手机号码--------" + isphone);
		initSwitch();
	}

	private void initSwitch() {
		if (ismessage == 0) {
			isMessage = false;
			mTeamSetting_message.setText("已关闭");
			message=0;
		} else {
			isMessage = true;
			mTeamSetting_message.setText("已开启");
			message=1;
		}

		if (ischat == 0) {
			isChat = false;
			mTeamSetting_chat.setText("已关闭");
			chat=0;
		} else {
			isChat = true;
			mTeamSetting_chat.setText("已开启");
			chat=1;
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
		SharedPreferences sp = getSharedPreferences("Registered", 0);
		int returndata_1 = sp.getInt("returndata", 0);
		boolean isRegistered_one = sp.getBoolean("isRegistered_one", false);
		SharedPreferences sp1 = getSharedPreferences("isLogin", 0);
		boolean login = sp1.getBoolean("Login", false);
		if (isRegistered_one) {
			mTeamSetting_number.setText("" + returndata_1);
			ReadUser(returndata_1);
		} else {
			if (login) {
				int returndata_2 = LoginActivity.pk_user;
				mTeamSetting_number.setText("" + returndata_2);
				ReadUser(returndata_2);
			} else {
				mTeamSetting_number.setText("" + returndata_1);
				ReadUser(returndata_1);
			}
		}
	}

	private void ReadUser(int returndata) {
		readuserinter = new Interface();
		User readuser = new User();
		readuser.setPk_user(returndata);
		readuserinter.readUser(TeamSettingActivity.this, readuser);
		readuserinter.setPostListener(new UserInterface() {

			@Override
			public void success(String A) {
				if (isupdate) {
					Teamupdateback teamupdateback = GsonUtils.parseJson(A,
							Teamupdateback.class);
					int statusmsg = teamupdateback.getStatusMsg();
					if (statusmsg == 1) {
						Log.e("TeamSettingActivity", "更新完的返回结果" + A);
						SharedPreferences teamsetting_sp=getSharedPreferences("Setting", 0);
						Editor editor=teamsetting_sp.edit();
						editor.putBoolean("setting", true);
						editor.commit();
						finish();
					} else {
						Toast.makeText(TeamSettingActivity.this,
								"更新设置失败，请重新再试!", Toast.LENGTH_SHORT).show();
					}
					Log.e("TeamSettingActivity", "更新完的返回结果"+A);

				} else {
					if (!isProduce) {
						// 读取用户资料成功
						Loginback usersettingback = GsonUtils.parseJson(A,
								Loginback.class);
						int userStatusmsg = usersettingback.getStatusMsg();
						if (userStatusmsg == 1) {
							List<User> Users = usersettingback.getReturnData();
							if (Users.size() >= 1) {
								User readuser = Users.get(0);
								useravatar_path = readuser.getAvatar_path();
							}
							completeURL = beginStr + useravatar_path + endStr;
							PreferenceUtils.saveImageCache(
									TeamSettingActivity.this, completeURL);// 存SP
							ImageLoaderUtils.getInstance().LoadImage(
									TeamSettingActivity.this, completeURL,
									mTeamSetting_head);
						}
					} else {
						Log.e("TeamSettingActivity", "=========" + A);
						try {
							JSONObject jsonObject = new JSONObject(A);
							Object object = jsonObject.get("returnData");
							Log.e("TeamSettingActivity", "object====" + object);
							Log.e("TeamSettingActivity",
									"object.toString()====" + object.toString());
							Log.e("TeamSettingActivity",
									"object.toString().length()===="
											+ object.toString().length());
							if (object.toString().length() > 4) {
								Log.e("TeamSettingActivity", "进入第二次====");
								java.lang.reflect.Type type = new TypeToken<GroupCodeback2>() {
								}.getType();
								GroupCodeback2 groupcodeback2 = GsonUtils
										.parseJsonArray(A, type);
								int Group_statusmsg2 = groupcodeback2
										.getStatusMsg();
								List<Group_Code2> grouplsit = (List<Group_Code2>) groupcodeback2
										.getReturnData();
								if (Group_statusmsg2 == 1) {
									Group_Code2 groupcode = grouplsit.get(0);
									String requestcode = groupcode
											.getPk_group_code();
									mTeamSetting_requestcode
											.setText(requestcode);
								}
							} else {
								Log.e("TeamSettingActivity", "进入第一次====");
								GroupCodeback groupcodeback = GsonUtils
										.parseJson(A, GroupCodeback.class);
								int Group_statusmsg = groupcodeback
										.getStatusMsg();
								if (Group_statusmsg == 1) {
									String requestcode = groupcodeback
											.getReturnData();
									mTeamSetting_requestcode
											.setText(requestcode);
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

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
		mTeamSetting_head = (ImageView) findViewById(R.id.TeamSetting_head);// 头像
		mTeamSetting_number = (TextView) findViewById(R.id.TeamSetting_number);// 必聚号
		mTeamSetting_requestcode = (TextView) findViewById(R.id.TeamSetting_requestcode);// 生成邀请码
		mTeamSetting_requestcode.setOnClickListener(this);
		mTeamSetting_message = (TextView) findViewById(R.id.TeamSetting_message);// 聚会信息提醒开关
		mTeamSetting_message.setOnClickListener(this);
		mTeamSetting_chat = (TextView) findViewById(R.id.TeamSetting_chat);// 聊天信息开关
		mTeamSetting_chat.setOnClickListener(this);
		mTeamSetting_phone = (TextView) findViewById(R.id.TeamSetting_phone);// 公开手机号码开关
		mTeamSetting_phone.setOnClickListener(this);
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
		default:
			break;
		}
	}

	private void TeamSetting_phone() {
		isPhone = !isPhone;
		if (!isPhone) {
			mTeamSetting_phone.setText("未公开");
			phone=0;
		}  else {
			mTeamSetting_phone.setText("已公开");
			phone = 1;
		}
	}

	private void TeamSetting_chat() {
		isChat = !isChat;
		if (!isChat) {
			mTeamSetting_chat.setText("已关闭");
			chat=0;
		} else {
			mTeamSetting_chat.setText("已开启");
			chat=1;
		}
	}

	private void TeamSetting_message() {
		isMessage = !isMessage;
		if (!isMessage) {
			mTeamSetting_message.setText("已关闭");
			message=0;
		} else {
			mTeamSetting_message.setText("已开启");
			message=1;
		} 
	}

	private void TeamSetting_requestcode() {
		isProduce = true;
		Group Group_teamsetting = new Group();
		Group_teamsetting.setPk_group(pk_group);
		Log.e("TeamSettingActivity", "Group_teamsetting" + pk_group);
		readuserinter.produceRequestCode(TeamSettingActivity.this,
				Group_teamsetting);
	}

	//更新小组设置
	private void TeamSetting_save() {
		isupdate = true;
		Group_User group_user = new Group_User();
		if (isRegistered_one) {
			group_user.setFk_user(returndata);
		} else {
			if (login) {
				int fk_user = LoginActivity.pk_user;
				group_user.setFk_user(fk_user);
			} else {
				group_user.setFk_user(returndata);
			}
		}
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

}
