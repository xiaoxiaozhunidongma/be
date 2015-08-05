package com.biju.function;

import com.BJ.javabean.ReadUserAllFriends;
import com.BJ.utils.ImageLoaderUtils;
import com.biju.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FriendsDataActivity extends Activity implements OnClickListener{

	private ImageView mFriendsData_head;
	private TextView mFriendsData_nickname;
	private TextView mFriendsData_phone;
	private ImageView mFriendsData_Callphone;
	private ImageView mFriendsData_SendMessages;
	private ImageView mFriendsData_Savecontact;
	private ImageView mFriendsData_DeleteFriends;
	private ImageView mFriendsData_PrivateChat;
	private ReadUserAllFriends mAllFriends;
	private String beginStr = "http://201139.image.myqcloud.com/201139/0/";
	private String endStr = "/original";
	private String TestcompleteURL = beginStr
			+ "1ddff6cf-35ac-446b-8312-10f4083ee13d" + endStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends_data);
		Intent intent = getIntent();
		mAllFriends = (ReadUserAllFriends) intent.getSerializableExtra("ReadUserAllFriends");
		initUI();
		initFriendsData();
	}

	private void initFriendsData() {
		String avatar_path = mAllFriends.getAvatar_path();
		String completeURL = beginStr + avatar_path + endStr;
		ImageLoaderUtils.getInstance().LoadImage(
				FriendsDataActivity.this, completeURL,
				mFriendsData_head);
		mFriendsData_nickname.setText(mAllFriends.getNickname());
		mFriendsData_phone.setText(mAllFriends.getPhone());
	}

	private void initUI() {
		mFriendsData_head = (ImageView) findViewById(R.id.FriendsData_head);//����ͷ��
		mFriendsData_nickname = (TextView) findViewById(R.id.FriendsData_nickname);//�����ǳ�
		mFriendsData_phone = (TextView) findViewById(R.id.FriendsData_phone);//���ѵ绰
		mFriendsData_Callphone = (ImageView) findViewById(R.id.FriendsData_Callphone);//����绰
		mFriendsData_Callphone.setOnClickListener(this);
		findViewById(R.id.FriendsData_Callphone_1).setOnClickListener(this);
		
		mFriendsData_SendMessages = (ImageView) findViewById(R.id.FriendsData_SendMessages);//����Ϣ
		mFriendsData_SendMessages.setOnClickListener(this);
		findViewById(R.id.FriendsData_SendMessages_1).setOnClickListener(this);
		
		mFriendsData_Savecontact = (ImageView) findViewById(R.id.FriendsData_Savecontact);//���浽ͨѶ¼
		mFriendsData_Savecontact.setOnClickListener(this);
		findViewById(R.id.FriendsData_Savecontact_1).setOnClickListener(this);
		
		mFriendsData_DeleteFriends = (ImageView) findViewById(R.id.FriendsData_DeleteFriends);//ɾ������
		mFriendsData_DeleteFriends.setOnClickListener(this);
		findViewById(R.id.FriendsData_DeleteFriends_1).setOnClickListener(this);
		
		mFriendsData_PrivateChat = (ImageView) findViewById(R.id.FriendsData_PrivateChat);//˽��
		mFriendsData_PrivateChat.setOnClickListener(this);
		findViewById(R.id.FriendsData_PrivateChat_1).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friends_data, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.FriendsData_Callphone:
		case R.id.FriendsData_Callphone_1:
			FriendsData_Callphone();
			break;
		case R.id.FriendsData_SendMessages:
		case R.id.FriendsData_SendMessages_1:
			FriendsData_SendMessages();
			break;
		case R.id.FriendsData_Savecontact:
		case R.id.FriendsData_Savecontact_1:
			FriendsData_Savecontact();
			break;
		case R.id.FriendsData_DeleteFriends:
		case R.id.FriendsData_DeleteFriends_1:
			FriendsData_DeleteFriends();
			break;
		case R.id.FriendsData_PrivateChat:
		case R.id.FriendsData_PrivateChat_1:
			FriendsData_PrivateChat();
			break;
		default:
			break;
		}
	}

	private void FriendsData_PrivateChat() {
		Toast.makeText(FriendsDataActivity.this, "˽��", Toast.LENGTH_SHORT).show();
	}

	private void FriendsData_DeleteFriends() {
		Toast.makeText(FriendsDataActivity.this, "ɾ������", Toast.LENGTH_SHORT).show();
	}

	private void FriendsData_Savecontact() {
		Toast.makeText(FriendsDataActivity.this, "��ӵ�ͨѶ¼", Toast.LENGTH_SHORT).show();
	}

	private void FriendsData_SendMessages() {
		Toast.makeText(FriendsDataActivity.this, "������", Toast.LENGTH_SHORT).show();
	}

	private void FriendsData_Callphone() {
		Toast.makeText(FriendsDataActivity.this, "����绰", Toast.LENGTH_SHORT).show();
	}

}
