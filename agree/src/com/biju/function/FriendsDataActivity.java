package com.biju.function;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.BJ.javabean.ReadUserAllFriends;
import com.BJ.javabean.User_User;
import com.BJ.utils.ImageLoaderUtils;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.releaseFriendListenner;
import com.biju.R;
import com.example.huanxin.ChatActivity;

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
	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "";
	private String TestcompleteURL = beginStr
			+ "1ddff6cf-35ac-446b-8312-10f4083ee13d" + endStr;
	private Interface mFriendsDataInterface;
	private Integer fk_user_from;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends_data);
		Intent intent = getIntent();
		mAllFriends = (ReadUserAllFriends) intent.getSerializableExtra(IConstant.ReadUserAllFriends);
		fk_user_from = intent.getIntExtra(IConstant.Fk_user_from, 0);
		initUI();
		initInterface();
		initFriendsData();
	}

	private void initInterface() {
		mFriendsDataInterface = Interface.getInstance();
		mFriendsDataInterface.setPostListener(new releaseFriendListenner() {
			
			@Override
			public void success(String A) {
				Log.e("FriendsDataActivity", "返回删除好友的结果======"+A);
				Toast.makeText(FriendsDataActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
				finish();
			}
			
			@Override
			public void defail(Object B) {
			}
		});
	}

	private void initFriendsData() {
		String avatar_path = mAllFriends.getAvatar_path();
		String completeURL = beginStr + avatar_path + endStr;
		ImageLoaderUtils.getInstance().LoadImageCricular(
				FriendsDataActivity.this, completeURL,
				mFriendsData_head);
		mFriendsData_nickname.setText(mAllFriends.getNickname());
		mFriendsData_phone.setText(mAllFriends.getPhone());
	}

	private void initUI() {
		mFriendsData_head = (ImageView) findViewById(R.id.FriendsData_head);//好友头像
		mFriendsData_nickname = (TextView) findViewById(R.id.FriendsData_nickname);//好友昵称
		mFriendsData_phone = (TextView) findViewById(R.id.FriendsData_phone);//好友电话
		mFriendsData_Callphone = (ImageView) findViewById(R.id.FriendsData_Callphone);//拨打电话
		mFriendsData_Callphone.setOnClickListener(this);
		findViewById(R.id.FriendsData_Callphone_1).setOnClickListener(this);
		
		mFriendsData_SendMessages = (ImageView) findViewById(R.id.FriendsData_SendMessages);//发信息
		mFriendsData_SendMessages.setOnClickListener(this);
		findViewById(R.id.FriendsData_SendMessages_1).setOnClickListener(this);
		
		mFriendsData_Savecontact = (ImageView) findViewById(R.id.FriendsData_Savecontact);//保存到通讯录
		mFriendsData_Savecontact.setOnClickListener(this);
		findViewById(R.id.FriendsData_Savecontact_1).setOnClickListener(this);
		
		mFriendsData_DeleteFriends = (ImageView) findViewById(R.id.FriendsData_DeleteFriends);//删除好友
		mFriendsData_DeleteFriends.setOnClickListener(this);
		findViewById(R.id.FriendsData_DeleteFriends_1).setOnClickListener(this);
		
		mFriendsData_PrivateChat = (ImageView) findViewById(R.id.FriendsData_PrivateChat);//私聊
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
			Toast.makeText(FriendsDataActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
			try {
				FriendsData_Savecontact();
			} catch (Exception e) {
				e.printStackTrace();
			}
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

	//私聊
	private void FriendsData_PrivateChat() {
		Intent intent=new Intent(FriendsDataActivity.this, ChatActivity.class);
		intent.putExtra(IConstant.AllFriends, mAllFriends);
		startActivity(intent);
	}

	//删除好友
	private void FriendsData_DeleteFriends() {
		User_User user_User=new User_User();
		user_User.setFk_user_to(mAllFriends.getFk_user_to());
		user_User.setFk_user_from(fk_user_from);
		mFriendsDataInterface.releaseFriend(FriendsDataActivity.this, user_User);
	}

	//收藏到通讯录
	private void FriendsData_Savecontact()  throws Exception {
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		ContentResolver resolver = FriendsDataActivity.this.getContentResolver();
		ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
		ContentProviderOperation op1 = ContentProviderOperation.newInsert(uri)
			.withValue("account_name", null)
			.build();
		operations.add(op1);
		
		uri = Uri.parse("content://com.android.contacts/data");
		ContentProviderOperation op2 = ContentProviderOperation.newInsert(uri)
			.withValueBackReference("raw_contact_id", 0)
			.withValue("mimetype", "vnd.android.cursor.item/name")
			.withValue("data2", mAllFriends.getNickname())
			.build();
		operations.add(op2);
		
		ContentProviderOperation op3 = ContentProviderOperation.newInsert(uri)
			.withValueBackReference("raw_contact_id", 0)
			.withValue("mimetype", "vnd.android.cursor.item/phone_v2")
			.withValue("data1", mAllFriends.getPhone())			
			.withValue("data2", "2")
			.build();
		operations.add(op3);
		
		resolver.applyBatch("com.android.contacts", operations);
	}

	//发短信
	private void FriendsData_SendMessages() {
//		Toast.makeText(FriendsDataActivity.this, "发短信", Toast.LENGTH_SHORT).show();
		Uri smsToUri = Uri.parse("smsto:"+ mAllFriends.getPhone());    
	    Intent mIntent = new Intent( android.content.Intent.ACTION_SENDTO, smsToUri );  
	    startActivity( mIntent );
	}

	//拨打电话
	private void FriendsData_Callphone() {
//		Toast.makeText(FriendsDataActivity.this, "拨打电话", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mAllFriends.getPhone()));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

}
