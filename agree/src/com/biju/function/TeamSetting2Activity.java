package com.biju.function;

import java.util.List;

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
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.BJ.javabean.Exitback;
import com.BJ.javabean.Group;
import com.BJ.javabean.Group_User;
import com.BJ.javabean.Groupuserback;
import com.BJ.javabean.Teamupdateback;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.PreferenceUtils;
import com.BJ.utils.SdPkUser;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.readUserGroupRelationListenner;
import com.biju.Interface.updateGroupSetListenner;
import com.biju.MainActivity;
import com.biju.R;
import com.biju.switchutils.ChatSwitchView;
import com.biju.switchutils.MessageSwitchView;
import com.biju.switchutils.PhoneSwitchView;
import com.github.volley_examples.utils.GsonUtils;

public class TeamSetting2Activity extends Activity implements OnClickListener{

	private ImageView mTeamSetting2_head;
	private TextView mTeamSetting2_User_nickname;
	private TextView mTeamSetting2_Tean_name;
	private Integer sD_pk_user;
	private Interface teamSetting2_interface;
	private Group teamsetting_group;
	
	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "";
	private String completeURL;
	private String TestcompleteURL = beginStr
			+ "1ddff6cf-35ac-446b-8312-10f4083ee13d" + endStr;
	private Integer pk_group;
	private boolean isExit;
	private boolean Message_checked;
	private boolean Chat_checked;
	private boolean Phone_checked;
	
	private int Message_ischecked;
	private int Chat_ischecked;
	private int Phone_ischecked;

	public static GetPhone getPhone;
	public static GetChat getChat;
	public static GetMessage getMessage;
	private View message_SwitchView1;
	private View chat_SwitchView1;
	private View phone_SwitchView1;
	
	private boolean Clickmessage;
	private boolean Clickchat;
	private boolean Clickphone;
	
	private Integer ischat;
	private Integer ismessage;
	private Integer isphone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_setting2);
		teamsetting_group = SdPkUser.getTeamSettinggroup();
		
		initUI();
		initInterFace();
		
		//获取sd卡中的pk_user
		sD_pk_user = SdPkUser.getsD_pk_user();
				
		//获取小组号
		Intent intent = getIntent();
		pk_group = intent.getIntExtra(IConstant.Group, 0);
		initreadUserGroupRelation(pk_group);
		initMessage();
		initChat();
		initPhone();
	}

	@SuppressWarnings("static-access")
	private void initPhone() {
		GetPhone getPhone=new GetPhone(){

			@Override
			public void Phone(int phone, boolean clickphone) {
				Phone_ischecked=phone;
				Clickphone=clickphone;
			}

		};
		this.getPhone=getPhone;
	}

	@SuppressWarnings("static-access")
	private void initChat() {
		GetChat getChat=new GetChat(){

			@Override
			public void Chat(int chat, boolean clickchat) {
				Chat_ischecked=chat;
				Clickchat=clickchat;
			}
		};
		this.getChat=getChat;
	}

	@SuppressWarnings("static-access")
	private void initMessage() {
		GetMessage getMessage=new GetMessage(){

			@Override
			public void Message(int message, boolean clickmessage) {
				Message_ischecked=message;
				Clickmessage=clickmessage;
			}

			
		};
		this.getMessage=getMessage;
	}

	private void initInterFace() {
		teamSetting2_interface = Interface.getInstance();
		//读取用户小组关系
		teamSetting2_interface.setPostListener(new readUserGroupRelationListenner() {

			@Override
			public void success(String A) {
				Groupuserback groupuserback = GsonUtils.parseJson(A,
						Groupuserback.class);
				Integer statusMsg = groupuserback.getStatusMsg();
				if (statusMsg == 1) {
					Log.e("GroupActivity", "返回小组关系ID====" + A);
					List<Group_User> groupuser_returnData = groupuserback.getReturnData();
					if (groupuser_returnData.size() > 0) {
						Group_User group_user = groupuser_returnData.get(0);
						ischat = group_user.getMessage_warn();
						ismessage = group_user.getParty_warn();
						isphone = group_user.getPublic_phone();
						switch (ischat) {
						case 0:
							Chat_checked=false;
							break;
						case 1:
							Chat_checked=true;
							break;

						default:
							break;
						}
						
						switch (ismessage) {
						case 0:
							Message_checked=false;
							break;
						case 1:
							Message_checked=true;
							break;
						default:
							break;
						}
						
						
						switch (isphone) {
						case 0:
							Phone_checked=false;
							break;
						case 1:
							Phone_checked=true;
							break;

						default:
							break;
						}
					}
					MessageSwitchView.changedListener.onChanged(message_SwitchView1, Message_checked);
					ChatSwitchView.changedListener.onChanged(chat_SwitchView1, Chat_checked);
					PhoneSwitchView.changedListener.onChanged(phone_SwitchView1, Phone_checked);
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
		//退出小组与更新小组监听
		teamSetting2_interface.setPostListener(new updateGroupSetListenner() {

			@Override
			public void success(String A) {
				if (isExit) {
					Exitback exitback = GsonUtils.parseJson(A, Exitback.class);
					Integer exitstatus = exitback.getStatusMsg();
					if (exitstatus == 1) {
						Log.e("TeamSettingActivity", "返回是否退出成功的结果-------" + A);
						SdPkUser.setRefreshTeam(true);//有对小组进行修改过后传true
						Intent intent=new Intent(TeamSetting2Activity.this, MainActivity.class);
						startActivity(intent);
					}
				} else {
					Teamupdateback teamupdateback = GsonUtils.parseJson(A,Teamupdateback.class);
					int statusmsg = teamupdateback.getStatusMsg();
					if (statusmsg == 1) {
						Log.e("TeamSettingActivity", "更新完的返回结果" + A);
//						SharedPreferences teamsetting_sp = getSharedPreferences("Setting", 0);
//						Editor editor = teamsetting_sp.edit();
//						editor.putBoolean("setting", true);
//						editor.commit();
//						finish();
					} else {
						Toast.makeText(TeamSetting2Activity.this,"更新设置失败，请重新再试!", Toast.LENGTH_SHORT).show();
					}
				}

			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	//读取用户小组关系
	private void initreadUserGroupRelation(Integer pk_group) {
		Group_User group_User = new Group_User();
		group_User.setFk_group(pk_group);
		group_User.setFk_user(sD_pk_user);
		teamSetting2_interface.readUserGroupRelation(TeamSetting2Activity.this,group_User);
	}

	private void initUI() {
		message_SwitchView1 = findViewById(R.id.Message_SwitchView1);
		chat_SwitchView1 = findViewById(R.id.Chat_SwitchView1);
		phone_SwitchView1 = findViewById(R.id.Phone_SwitchView1);
		
		findViewById(R.id.TeamSetting2_back_layout).setOnClickListener(this);//返回
		findViewById(R.id.TeamSetting2_back).setOnClickListener(this);
		findViewById(R.id.TeamSetting2_save_layout).setOnClickListener(this);//保存
		findViewById(R.id.TeamSetting2_save).setOnClickListener(this);
		mTeamSetting2_head = (ImageView) findViewById(R.id.TeamSetting2_head);//小组头像
		mTeamSetting2_User_nickname = (TextView) findViewById(R.id.TeamSetting2_User_nickname);//小组名称
		findViewById(R.id.TeamSetting2_Tean_name_layout).setOnClickListener(this);//小组名称修改
		mTeamSetting2_Tean_name = (TextView) findViewById(R.id.TeamSetting2_Tean_name);//显示小组名称
		findViewById(R.id.TeamSetting2_History_party_layout).setOnClickListener(this);//历史聚会
		findViewById(R.id.TeamSetting2_invite_friends_layout).setOnClickListener(this);//邀请好友
		
		findViewById(R.id.TeamSetting2_Message_prompt).setOnClickListener(this);//聚会信息提示
		findViewById(R.id.TeamSetting2_chat_prompt).setOnClickListener(this);//聊天信息提示
		findViewById(R.id.TeamSetting2_phone_prompt).setOnClickListener(this);//电话号码公开提示
		
		findViewById(R.id.TeamSetting2_Exit_layout).setOnClickListener(this);//退出小组
		
		//初始化显示头像和小组名称
		mTeamSetting2_User_nickname.setText(teamsetting_group.getName());
		mTeamSetting2_Tean_name.setText(teamsetting_group.getName());
		String useravatar_path = teamsetting_group.getAvatar_path();
		completeURL = beginStr + useravatar_path + endStr;
		PreferenceUtils.saveImageCache(TeamSetting2Activity.this, completeURL);// 存SP
		ImageLoaderUtils.getInstance().LoadImageSquare(TeamSetting2Activity.this,completeURL, mTeamSetting2_head);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.team_setting2, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.TeamSetting2_back:
		case R.id.TeamSetting2_back_layout:
			TeamSetting2_back();
			break;
		case R.id.TeamSetting2_Exit_layout:
			TeamSetting2_Exit_layout();
			break;
		case R.id.TeamSetting2_save_layout:
		case R.id.TeamSetting2_save:
			TeamSetting2_save();
			break;
		default:
			break;
		}
	}
	//修改小组资料
	private void TeamSetting2_save() {
		Group_User group_user = new Group_User();
		group_user.setPk_group_user(GroupActivity.pk_group_user);
		group_user.setFk_user(sD_pk_user);
		group_user.setFk_group(pk_group);
		if(Clickmessage)
		{
			group_user.setParty_warn(Message_ischecked);// 聚会信息
			Log.e("TeamSettingActivity", "需要传入的聚会信息ID--------" + Message_ischecked);
		}else
		{
			group_user.setParty_warn(ismessage);// 聚会信息
			Log.e("TeamSettingActivity", "需要传入的聚会信息ID--------" + ismessage);
		}
		if(Clickphone)
		{
			group_user.setPublic_phone(Phone_ischecked);
			Log.e("TeamSettingActivity", "需要传入的电话ID--------" + Phone_ischecked);
		}else
		{
			group_user.setPublic_phone(isphone);
			Log.e("TeamSettingActivity", "需要传入的电话ID--------" + isphone);
		}
		if(Clickchat)
		{
			group_user.setMessage_warn(Chat_ischecked);// 聊天信息
			Log.e("TeamSettingActivity", "需要传入的聊天信息ID--------" + Chat_ischecked);
		}else
		{
			group_user.setMessage_warn(ischat);// 聊天信息
			Log.e("TeamSettingActivity", "需要传入的聊天信息ID--------" + ischat);
		}
//		group_user.setParty_update(1);
//		group_user.setPhoto_update(1);
//		group_user.setChat_update(1);
//		group_user.setRole(1);
		group_user.setStatus(1);
		teamSetting2_interface.updateGroupSet(TeamSetting2Activity.this, group_user);
	}

	//退出小组
	private void TeamSetting2_Exit_layout() {
		Team_NiftyDialogBuilder();
	}

	private void Team_NiftyDialogBuilder() {
		final SweetAlertDialog sd = new SweetAlertDialog(TeamSetting2Activity.this,SweetAlertDialog.WARNING_TYPE);
		sd.setTitleText("提示");
		sd.setContentText("是否退出当前小组？");
		sd.setCancelText("取消");
		sd.setConfirmText("确定");
		sd.showCancelButton(true);
		sd.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sDialog) {
				sd.cancel();
			}
		}).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sDialog) {
				sd.cancel();
				isExit=true;
				Group_User group_user = new Group_User();
				group_user.setPk_group_user(GroupActivity.pk_group_user);
				group_user.setFk_user(sD_pk_user);
				group_user.setFk_group(pk_group);
				group_user.setRole(0);
				group_user.setStatus(0);
				teamSetting2_interface.updateGroupSet(TeamSetting2Activity.this, group_user);
			}
		}).show();
	}
	
	//返回
	private void TeamSetting2_back() {
		finish();
	}

	//回去回调的修改状态(聚会信息)
	public interface GetMessage
	{
		void Message(int message,boolean clickmessage);
	}
	//回去回调的修改状态(聊天信息)
	public interface GetChat
	{
		void Chat(int chat,boolean clickchat);
	}
	//回去回调的修改状态(电话信息)
	public interface GetPhone
	{
		void Phone(int phone,boolean clickphone);
	}
	
	
}
