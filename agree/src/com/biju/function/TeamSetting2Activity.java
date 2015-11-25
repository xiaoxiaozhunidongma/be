package com.biju.function;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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
import com.BJ.javabean.Groupback;
import com.BJ.javabean.Groupuserback;
import com.BJ.javabean.Teamupdateback;
import com.BJ.javabean.User;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.PreferenceUtils;
import com.BJ.utils.SdPkUser;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.readUserGroupMsgListenner;
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
//	private Group teamsetting_group;
	
	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "@!";
	private String completeURL;
	private String TestcompleteURL = beginStr
			+ "1ddff6cf-35ac-446b-8312-10f4083ee13d" + endStr;
	private Integer pk_group;
	private boolean isExit;
	private boolean Message_checked;
	private boolean Chat_checked;
	private boolean Phone_checked;
	
	private int Message_ischecked=-1;
	private int Chat_ischecked=-1;
	private int Phone_ischecked=-1;

	public static GetPhone getPhone;
	public static GetChat getChat;
	public static GetMessage getMessage;
	private View message_SwitchView1;
	private View chat_SwitchView1;
	private View phone_SwitchView1;
	
	private boolean Clickmessage;
	private boolean Clickchat;
	private boolean Clickphone;
	
	private Integer ischat=-1;
	private Integer ismessage=-1;
	private Integer isphone=-1;
	private int toastHeight;
	private Integer mGroupManager;
	private Integer pk_user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_setting2);
		initUI();
		initInterFace();
		
		//��ȡС���
		Intent intent = getIntent();
		pk_group = intent.getIntExtra(IConstant.Group, 0);
		mGroupManager = intent.getIntExtra("GroupManager", 0);
		
		//��ȡsd���е�pk_user
		sD_pk_user = SdPkUser.getsD_pk_user();
				
		initreadUserGroupRelation(pk_group);
		initMessage();
		initChat();
		initPhone();
		DisplayMetrics();
		initGroup();
	}
	
	private void initGroup() {
		User homeuser = new User();
		homeuser.setPk_user(sD_pk_user);
		teamSetting2_interface.readUserGroupMsg(TeamSetting2Activity.this, homeuser);
	}

	private void DisplayMetrics() {
		android.util.DisplayMetrics metric = new android.util.DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int height = metric.heightPixels;   // ��Ļ�߶ȣ����أ�
        toastHeight = height/4;
	}

	@Override
	protected void onRestart() {
		SharedPreferences ChangeName_sp=getSharedPreferences("ChangeGroupName", 0);
		boolean changeName = ChangeName_sp.getBoolean("ChangeName", false);
		if(changeName){
			initGroup();
		}
		super.onRestart();
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
					Log.e("TeamSetting2Activity", "����С���ϵID====" + A);
					List<Group_User> groupuser_returnData = groupuserback.getReturnData();
					if (groupuser_returnData.size() > 0) {
						Group_User group_user = groupuser_returnData.get(0);
						pk_user = group_user.getFk_user();
						ischat = group_user.getMessage_warn();
						ismessage = group_user.getParty_warn();
						isphone = group_user.getPublic_phone();
						Log.e("TeamSetting2Activity", "���ص�ischat=========" + ischat);
						Log.e("TeamSetting2Activity", "���ص�ismessage=========" + ismessage);
						Log.e("TeamSetting2Activity", "���ص�isphone=========" + isphone);
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
					Log.e("TeamSetting2Activity", "���ص�Chat_checked=========" + Chat_checked);
					Log.e("TeamSetting2Activity", "���ص�Message_checked=========" + Message_checked);
					Log.e("TeamSetting2Activity", "���ص�Phone_checked=========" + Phone_checked);
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
						//�˳���ǰС�����Sliding�еĵ��Ч��ҲҪ����,�������¶�ȡС���е������û�
						SharedPreferences Sliding_sp=getSharedPreferences(IConstant.SlidingClick, 0);
						Editor editor=Sliding_sp.edit();
						editor.putInt(IConstant.Click, 0);
						editor.commit();
						
						Toast();
					}
				} else {
					Teamupdateback teamupdateback = GsonUtils.parseJson(A,Teamupdateback.class);
					int statusmsg = teamupdateback.getStatusMsg();
					if (statusmsg == 1) {
						Log.e("TeamSettingActivity", "������ķ��ؽ��" + A);
						Toast();
					} else {
						Toast1();
					}
				}

			}
			@Override
			public void defail(Object B) {

			}
		});
		
		teamSetting2_interface.setPostListener(new readUserGroupMsgListenner() {
			
			@Override
			public void success(String A) {
				Log.e("TeamSettingActivity", "��ȡ����С��=====" + A);
				Groupback homeback = GsonUtils.parseJson(A, Groupback.class);
				int homeStatusMsg = homeback.getStatusMsg();
				if (homeStatusMsg == 1) {
					List<Group> users = homeback.getReturnData();
					if (users.size() > 0) {
						for (int i = 0; i < users.size(); i++) {
							Group readhomeuser_1 = users.get(i);
							Integer rea_pk_group=readhomeuser_1.getPk_group();
							if(String.valueOf(rea_pk_group).equals(String.valueOf(pk_group))){
								//��ʼ����ʾͷ���С������
								String name=readhomeuser_1.getName();
								mTeamSetting2_User_nickname.setText(name);
								mTeamSetting2_Tean_name.setText(name);
								String useravatar_path = readhomeuser_1.getAvatar_path();
								completeURL = beginStr + useravatar_path + endStr+"avatar";
								PreferenceUtils.saveImageCache(TeamSetting2Activity.this, completeURL);// ��SP
								ImageLoaderUtils.getInstance().LoadImageSquare(TeamSetting2Activity.this,completeURL, mTeamSetting2_head);
							}
						}
					}
				}
			}
			
			@Override
			public void defail(Object B) {
				
			}
		});
		
	}
	
	private void Toast1() {
		//�Զ���Toast
		View toastRoot = getLayoutInflater().inflate(R.layout.my_error_toast, null);
		Toast toast=new Toast(getApplicationContext());
		toast.setGravity(Gravity.TOP, 0, toastHeight);
		toast.setView(toastRoot);
		toast.setDuration(100);
		TextView tv=(TextView)toastRoot.findViewById(R.id.TextViewInfo);
		tv.setText("��������ʧ�ܣ�����������!");
		toast.show();
	}
	
	private void Toast() {
		//�Զ���Toast
		View toastRoot = getLayoutInflater().inflate(R.layout.my_toast, null);
		Toast toast=new Toast(getApplicationContext());
		toast.setGravity(Gravity.TOP, 0, toastHeight);
		toast.setView(toastRoot);
		toast.setDuration(100);
		TextView tv=(TextView)toastRoot.findViewById(R.id.TextViewInfo);
		if(isExit){
			tv.setText("�˳��ɹ�");
		}else {
			tv.setText("���³ɹ�");
		}
		toast.show();
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
		case R.id.TeamSetting2_Tean_name_layout:
			TeamSetting2_Tean_name_layout();
			break;
		case R.id.TeamSetting2_invite_friends_layout:
			TeamSetting2_invite_friends_layout();
			break;
		default:
			break;
		}
	}
	private void TeamSetting2_invite_friends_layout() {
		Intent intent=new Intent(TeamSetting2Activity.this, AddTeamFriendsActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.in_item, R.anim.out_item);
	}

	private void TeamSetting2_Tean_name_layout() {
		Log.e("TeamSettingActivity", "Ⱥ����ID=====" +mGroupManager);
		Log.e("TeamSettingActivity", "��ȡ��ǰ�˻���ID=====" +pk_user);
		
		if(String.valueOf(mGroupManager).equals(String.valueOf(pk_user))){
			Intent intent=new Intent(TeamSetting2Activity.this, ChangeGroupNameActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.in_item, R.anim.out_item);
		}else {
			SweetAlertDialog sd=new SweetAlertDialog(TeamSetting2Activity.this);
			sd.setTitleText("��ʾ");
			sd.setContentText("ֻ��С��Ĵ����߲��ܸ���С������Ŷ~");
			sd.show();
		}
	}

	//�޸�С������
	private void TeamSetting2_save() {
		Group_User group_user = new Group_User();
		group_user.setPk_group_user(GroupActivity.pk_group_user);
		group_user.setFk_user(sD_pk_user);
		group_user.setFk_group(pk_group);
		if(Clickmessage){
			group_user.setParty_warn(Message_ischecked);// �ۻ���Ϣ
			Log.e("TeamSettingActivity", "��Ҫ����ľۻ���ϢID--------" + Message_ischecked);
		}else{
			group_user.setParty_warn(ismessage);// �ۻ���Ϣ
			Log.e("TeamSettingActivity", "��Ҫ����ľۻ���ϢID--------" + ismessage);
		}
		if(Clickphone){
			group_user.setPublic_phone(Phone_ischecked);
			Log.e("TeamSettingActivity", "��Ҫ����ĵ绰ID--------" + Phone_ischecked);
		}else{
			group_user.setPublic_phone(isphone);
			Log.e("TeamSettingActivity", "��Ҫ����ĵ绰ID--------" + isphone);
		}
		if(Clickchat){
			group_user.setMessage_warn(Chat_ischecked);// ������Ϣ
			Log.e("TeamSettingActivity", "��Ҫ�����������ϢID--------" + Chat_ischecked);
		}else{
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
		overridePendingTransition(R.anim.left, R.anim.right);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			TeamSetting2_back();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
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
