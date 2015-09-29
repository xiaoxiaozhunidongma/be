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
		
		//��ȡsd���е�pk_user
		sD_pk_user = SdPkUser.getsD_pk_user();
				
		//��ȡС���
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
		//��ȡ�û�С���ϵ
		teamSetting2_interface.setPostListener(new readUserGroupRelationListenner() {

			@Override
			public void success(String A) {
				Groupuserback groupuserback = GsonUtils.parseJson(A,
						Groupuserback.class);
				Integer statusMsg = groupuserback.getStatusMsg();
				if (statusMsg == 1) {
					Log.e("GroupActivity", "����С���ϵID====" + A);
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
		//�˳�С�������С�����
		teamSetting2_interface.setPostListener(new updateGroupSetListenner() {

			@Override
			public void success(String A) {
				if (isExit) {
					Exitback exitback = GsonUtils.parseJson(A, Exitback.class);
					Integer exitstatus = exitback.getStatusMsg();
					if (exitstatus == 1) {
						Log.e("TeamSettingActivity", "�����Ƿ��˳��ɹ��Ľ��-------" + A);
						SdPkUser.setRefreshTeam(true);//�ж�С������޸Ĺ���true
						Intent intent=new Intent(TeamSetting2Activity.this, MainActivity.class);
						startActivity(intent);
					}
				} else {
					Teamupdateback teamupdateback = GsonUtils.parseJson(A,Teamupdateback.class);
					int statusmsg = teamupdateback.getStatusMsg();
					if (statusmsg == 1) {
						Log.e("TeamSettingActivity", "������ķ��ؽ��" + A);
//						SharedPreferences teamsetting_sp = getSharedPreferences("Setting", 0);
//						Editor editor = teamsetting_sp.edit();
//						editor.putBoolean("setting", true);
//						editor.commit();
//						finish();
					} else {
						Toast.makeText(TeamSetting2Activity.this,"��������ʧ�ܣ�����������!", Toast.LENGTH_SHORT).show();
					}
				}

			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	//��ȡ�û�С���ϵ
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
		
		findViewById(R.id.TeamSetting2_back_layout).setOnClickListener(this);//����
		findViewById(R.id.TeamSetting2_back).setOnClickListener(this);
		findViewById(R.id.TeamSetting2_save_layout).setOnClickListener(this);//����
		findViewById(R.id.TeamSetting2_save).setOnClickListener(this);
		mTeamSetting2_head = (ImageView) findViewById(R.id.TeamSetting2_head);//С��ͷ��
		mTeamSetting2_User_nickname = (TextView) findViewById(R.id.TeamSetting2_User_nickname);//С������
		findViewById(R.id.TeamSetting2_Tean_name_layout).setOnClickListener(this);//С�������޸�
		mTeamSetting2_Tean_name = (TextView) findViewById(R.id.TeamSetting2_Tean_name);//��ʾС������
		findViewById(R.id.TeamSetting2_History_party_layout).setOnClickListener(this);//��ʷ�ۻ�
		findViewById(R.id.TeamSetting2_invite_friends_layout).setOnClickListener(this);//�������
		
		findViewById(R.id.TeamSetting2_Message_prompt).setOnClickListener(this);//�ۻ���Ϣ��ʾ
		findViewById(R.id.TeamSetting2_chat_prompt).setOnClickListener(this);//������Ϣ��ʾ
		findViewById(R.id.TeamSetting2_phone_prompt).setOnClickListener(this);//�绰���빫����ʾ
		
		findViewById(R.id.TeamSetting2_Exit_layout).setOnClickListener(this);//�˳�С��
		
		//��ʼ����ʾͷ���С������
		mTeamSetting2_User_nickname.setText(teamsetting_group.getName());
		mTeamSetting2_Tean_name.setText(teamsetting_group.getName());
		String useravatar_path = teamsetting_group.getAvatar_path();
		completeURL = beginStr + useravatar_path + endStr;
		PreferenceUtils.saveImageCache(TeamSetting2Activity.this, completeURL);// ��SP
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
	//�޸�С������
	private void TeamSetting2_save() {
		Group_User group_user = new Group_User();
		group_user.setPk_group_user(GroupActivity.pk_group_user);
		group_user.setFk_user(sD_pk_user);
		group_user.setFk_group(pk_group);
		if(Clickmessage)
		{
			group_user.setParty_warn(Message_ischecked);// �ۻ���Ϣ
			Log.e("TeamSettingActivity", "��Ҫ����ľۻ���ϢID--------" + Message_ischecked);
		}else
		{
			group_user.setParty_warn(ismessage);// �ۻ���Ϣ
			Log.e("TeamSettingActivity", "��Ҫ����ľۻ���ϢID--------" + ismessage);
		}
		if(Clickphone)
		{
			group_user.setPublic_phone(Phone_ischecked);
			Log.e("TeamSettingActivity", "��Ҫ����ĵ绰ID--------" + Phone_ischecked);
		}else
		{
			group_user.setPublic_phone(isphone);
			Log.e("TeamSettingActivity", "��Ҫ����ĵ绰ID--------" + isphone);
		}
		if(Clickchat)
		{
			group_user.setMessage_warn(Chat_ischecked);// ������Ϣ
			Log.e("TeamSettingActivity", "��Ҫ�����������ϢID--------" + Chat_ischecked);
		}else
		{
			group_user.setMessage_warn(ischat);// ������Ϣ
			Log.e("TeamSettingActivity", "��Ҫ�����������ϢID--------" + ischat);
		}
//		group_user.setParty_update(1);
//		group_user.setPhoto_update(1);
//		group_user.setChat_update(1);
//		group_user.setRole(1);
		group_user.setStatus(1);
		teamSetting2_interface.updateGroupSet(TeamSetting2Activity.this, group_user);
	}

	//�˳�С��
	private void TeamSetting2_Exit_layout() {
		Team_NiftyDialogBuilder();
	}

	private void Team_NiftyDialogBuilder() {
		final SweetAlertDialog sd = new SweetAlertDialog(TeamSetting2Activity.this,SweetAlertDialog.WARNING_TYPE);
		sd.setTitleText("��ʾ");
		sd.setContentText("�Ƿ��˳���ǰС�飿");
		sd.setCancelText("ȡ��");
		sd.setConfirmText("ȷ��");
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
	
	//����
	private void TeamSetting2_back() {
		finish();
	}

	//��ȥ�ص����޸�״̬(�ۻ���Ϣ)
	public interface GetMessage
	{
		void Message(int message,boolean clickmessage);
	}
	//��ȥ�ص����޸�״̬(������Ϣ)
	public interface GetChat
	{
		void Chat(int chat,boolean clickchat);
	}
	//��ȥ�ص����޸�״̬(�绰��Ϣ)
	public interface GetPhone
	{
		void Phone(int phone,boolean clickphone);
	}
	
	
}
