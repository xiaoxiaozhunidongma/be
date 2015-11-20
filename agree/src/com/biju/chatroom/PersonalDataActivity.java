package com.biju.chatroom;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BJ.javabean.Group_ReadAllUser;
import com.BJ.javabean.User;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.PreferenceUtils;
import com.BJ.utils.SdPkUser;
import com.biju.MainActivity;
import com.biju.R;
import com.biju.function.GroupActivity;
import com.biju.function.SlidingActivity;
import com.example.testleabcloud.ChatActivityLean;
import com.fragment.CommonFragment;

public class PersonalDataActivity extends Activity implements OnClickListener{

	private ImageView mPersonalDataHead;
	private TextView mPersonalDataName;
	private User user;
	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "@!";
	private String completeURL;
	private String TestcompleteURL = beginStr
			+ "1ddff6cf-35ac-446b-8312-10f4083ee13d" + endStr;
	private Group_ReadAllUser group_user;
	private int SING;//1��Ⱥ�ģ�2��������,3��Ⱥ�ĵĳ�Ա�б�,4�������ҵĳ�Ա�б�
	private String phone;
	private RelativeLayout mPersonalDataBackground;
	public static GetClose getClose;
	public static GetRefreshData getRefreshData;
	public static GetChatRoomRefreshData chatRoomRefreshData;
	private Group_ReadAllUser group_ReadAllUser;
	private User chatroomuser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_data);
		SdPkUser.setGetOpen(true);//��������Ϣ�����ʱ��true
		SdPkUser.setGetChatRoomOpen(true);//��������Ϣ�����ʱ��true
		SING = SdPkUser.GetSource;
		switch (SING) {
		case 1:
			group_user = SdPkUser.getGroupChatUser();//��Դ��Ⱥ��
			break;
		case 2:
			user = SdPkUser.getClickUser();//��Դ��������
			break;
		case 3:
			group_ReadAllUser = SdPkUser.getGroup_ReadAllUser();//Ⱥ�ĵĳ�Ա�б�
			break;
		case 4:
			chatroomuser = SdPkUser.getChatRoomUser();//�����ҵĳ�Ա�б�
			break;
		default:
			break;
		}
		initUI();
		initClose();//����ɫ�رյ��ýӿ�
		initRefreshData();//Ⱥ�Ľ���û�ر�ʱˢ������
		initChatRoomRefreshData();//�����ҽ���û�ر�ʱˢ������
	}
	
	private void initChatRoomRefreshData() {
		GetChatRoomRefreshData chatRoomRefreshData=new GetChatRoomRefreshData(){

			@Override
			public void ChatRoomRefreshData(User user) {
				phone = user.getPhone();
				mPersonalDataName.setText(user.getNickname()+"");
				String mUserAvatar_path=user.getAvatar_path();
				completeURL = beginStr + mUserAvatar_path + endStr+"avatar";
				PreferenceUtils.saveImageCache(PersonalDataActivity.this, completeURL);// ��SP
				ImageLoaderUtils.getInstance().LoadImageCricular(PersonalDataActivity.this,completeURL, mPersonalDataHead);
				ChatActivityLean.memberChat.MemberChat();//�򿪺�����Ϣ����ʱ�ı���
			}
			
		};
		this.chatRoomRefreshData=chatRoomRefreshData;
	}

	private void initRefreshData() {
		GetRefreshData getRefreshData=new GetRefreshData(){

			@Override
			public void RefreshData(Group_ReadAllUser group_ReadAllUser) {
				phone = group_ReadAllUser.getPhone();
				mPersonalDataName.setText(group_ReadAllUser.getNickname()+"");
				String mUserAvatar_path=group_ReadAllUser.getAvatar_path();
				completeURL = beginStr + mUserAvatar_path + endStr+"avatar";
				PreferenceUtils.saveImageCache(PersonalDataActivity.this, completeURL);// ��SP
				ImageLoaderUtils.getInstance().LoadImageCricular(PersonalDataActivity.this,completeURL, mPersonalDataHead);
				GroupActivity.getSliding.SlidingClick();//����ʱ�ı���
			}
			
		};
		this.getRefreshData=getRefreshData;		
	}

	private void initClose() {
		GetClose getClose=new GetClose(){

			@Override
			public void Close() {
				mPersonalDataBackground.setVisibility(View.GONE);
				Animation animation=new AlphaAnimation(1.0f,0.0f);
				animation.setDuration(500);
				mPersonalDataBackground.startAnimation(animation);
			}
		};
		this.getClose=getClose;
	}

	private void initUI() {
		mPersonalDataBackground = (RelativeLayout) findViewById(R.id.PersonalDataBackground);
		findViewById(R.id.PersonalDataNoShowLayout).setOnClickListener(this);
		findViewById(R.id.PersonalDataShowLayout).setOnClickListener(this);
		findViewById(R.id.PersonalDataBack).setOnClickListener(this);//����
		findViewById(R.id.PersonalDataOK).setOnClickListener(this);//���
		mPersonalDataHead = (ImageView) findViewById(R.id.PersonalDataHead);//ͷ��
		mPersonalDataName = (TextView) findViewById(R.id.PersonalDataName);//����
		findViewById(R.id.PersonalDataPhoneBut).setOnClickListener(this);//����绰
		findViewById(R.id.PersonalDataMessageBut).setOnClickListener(this);//����Ϣ
		findViewById(R.id.PersonalDataDirectMessagesBut).setOnClickListener(this);//��˽��
		
		switch (SING) {
		case 1:
			phone = group_user.getPhone();
			mPersonalDataName.setText(group_user.getNickname()+"");
			String mUserAvatar_path1=group_user.getAvatar_path();
			completeURL = beginStr + mUserAvatar_path1 + endStr+"avatar";
			PreferenceUtils.saveImageCache(PersonalDataActivity.this, completeURL);// ��SP
			ImageLoaderUtils.getInstance().LoadImageCricular(PersonalDataActivity.this,completeURL, mPersonalDataHead);
			break;
		case 2:
			phone = user.getPhone();
			mPersonalDataName.setText(user.getNickname()+"");
			String mUserAvatar_path2=user.getAvatar_path();
			completeURL = beginStr + mUserAvatar_path2 + endStr+"avatar";
			PreferenceUtils.saveImageCache(PersonalDataActivity.this, completeURL);// ��SP
			ImageLoaderUtils.getInstance().LoadImageCricular(PersonalDataActivity.this,completeURL, mPersonalDataHead);
			break;
		case 3:
			phone = group_ReadAllUser.getPhone();
			mPersonalDataName.setText(group_ReadAllUser.getNickname()+"");
			String mUserAvatar_path3=group_ReadAllUser.getAvatar_path();
			completeURL = beginStr + mUserAvatar_path3 + endStr+"avatar";
			PreferenceUtils.saveImageCache(PersonalDataActivity.this, completeURL);// ��SP
			ImageLoaderUtils.getInstance().LoadImageCricular(PersonalDataActivity.this,completeURL, mPersonalDataHead);
			break;
		case 4:
			phone = chatroomuser.getPhone();
			mPersonalDataName.setText(chatroomuser.getNickname()+"");
			String mUserAvatar_path4=chatroomuser.getAvatar_path();
			completeURL = beginStr + mUserAvatar_path4 + endStr+"avatar";
			PreferenceUtils.saveImageCache(PersonalDataActivity.this, completeURL);// ��SP
			ImageLoaderUtils.getInstance().LoadImageCricular(PersonalDataActivity.this,completeURL, mPersonalDataHead);
			break;
		default:
			break;
		}
	}


	@Override
	public void onClick(View v) {
		switch ( v.getId()) {
		case R.id.PersonalDataPhoneBut:
			PersonalDataPhoneBut();
			break;
		case R.id.PersonalDataMessageBut:
			PersonalDataMessageBut();
			break;
		case R.id.PersonalDataDirectMessagesBut:
			PersonalDataDirectMessagesBut();
			break;
		case R.id.PersonalDataBack:
			PersonalDataBack();
			break;
		case R.id.PersonalDataOK:
			PersonalDataOK();
			break;
		case R.id.PersonalDataNoShowLayout:
			PersonalDataNoShowLayout();
			break;
		default:
			break;
		}
	}

	private void PersonalDataNoShowLayout() {
		switch (SING) {
		case 1:
			CommonFragment.getClose.Close();
			break;
		case 2:
			ChatActivityLean.getClose.Close();
			break;
		case 3:
			CommonFragment.getClose.Close();
			break;
		case 4:
			ChatActivityLean.getClose.Close();
			break;
		default:
			break;
		}
		finish();
		overridePendingTransition(R.anim.rightin_item, R.anim.rightout_item);
	}

	private void PersonalDataOK() {
		switch (SING) {
		case 1:
			GroupChat();
			break;
		case 2:
			ChatRoom();
			break;
		case 3:
			GroupChat();
			break;
		case 4:
			ChatRoom();
			break;
		default:
			break;
		}
	}

	private void ChatRoom() {
		mPersonalDataBackground.setVisibility(View.VISIBLE);
		Animation animation2=new AlphaAnimation(0.0f,1.0f);
		animation2.setDuration(500);
		mPersonalDataBackground.startAnimation(animation2);
		
		ChatActivityLean.chatRoomClickOK.ChatRoomClickOK();
		
		Intent intent2 = new Intent(PersonalDataActivity.this, MembersChatActivity.class);
		intent2.putExtra("PersonalData", true);
		startActivity(intent2);
		
		overridePendingTransition(R.anim.in_item, R.anim.out_item);
	}

	private void GroupChat() {
		mPersonalDataBackground.setVisibility(View.VISIBLE);
		Animation animation1=new AlphaAnimation(0.0f,1.0f);
		animation1.setDuration(500);
		mPersonalDataBackground.startAnimation(animation1);
		
		GroupActivity.clickOK.ClickOK();
		
		Intent intent1 = new Intent(PersonalDataActivity.this, SlidingActivity.class);
		intent1.putExtra("group_group", GroupActivity.getPk_group());
		intent1.putExtra("PersonalData", true);
		startActivity(intent1);
		overridePendingTransition(R.anim.in_item, R.anim.out_item);
	}

	private void PersonalDataBack() {
		finish();
		Intent intent = new Intent(PersonalDataActivity.this, MainActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.left, R.anim.right);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			PersonalDataNoShowLayout();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	//˽��
	private void PersonalDataDirectMessagesBut() {
		
	}

	//����Ϣ
	private void PersonalDataMessageBut() {
		Uri smsToUri = Uri.parse("smsto:"+phone );    
	    Intent mIntent = new Intent( android.content.Intent.ACTION_SENDTO, smsToUri );  
	    startActivity( mIntent );
	}

	//����绰
	private void PersonalDataPhoneBut() {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	
	public interface GetClose{
		void Close();
	}
	
	public interface GetRefreshData{
		void RefreshData(Group_ReadAllUser group_ReadAllUser);
	}
	public interface GetChatRoomRefreshData{
		void ChatRoomRefreshData(User user);
	}
	
}
