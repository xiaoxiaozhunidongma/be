package com.biju.chatroom;

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
import com.example.testleabcloud.ChatActivityLean.GetClose;
import com.fragment.CommonFragment;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
	private boolean source;
	private String phone;
	private RelativeLayout mPersonalDataBackground;
	public static GetClose getClose;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_data);
		source = SdPkUser.GetSource;
		if(source){
			user = SdPkUser.getClickUser();//��Դ��������
		}else {
			group_user = SdPkUser.getGroupChatUser();//��Դ��Ⱥ��
		}
		initUI();
		initClose();//����ɫ�رյ��ýӿ�
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
		
		if(source){
			phone = user.getPhone();
			mPersonalDataName.setText(user.getNickname()+"");
			String mUserAvatar_path=user.getAvatar_path();
			completeURL = beginStr + mUserAvatar_path + endStr+"avatar";
			PreferenceUtils.saveImageCache(PersonalDataActivity.this, completeURL);// ��SP
			ImageLoaderUtils.getInstance().LoadImageCricular(PersonalDataActivity.this,completeURL, mPersonalDataHead);
		}else {
			phone = group_user.getPhone();
			mPersonalDataName.setText(group_user.getNickname()+"");
			String mUserAvatar_path=group_user.getAvatar_path();
			completeURL = beginStr + mUserAvatar_path + endStr+"avatar";
			PreferenceUtils.saveImageCache(PersonalDataActivity.this, completeURL);// ��SP
			ImageLoaderUtils.getInstance().LoadImageCricular(PersonalDataActivity.this,completeURL, mPersonalDataHead);
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
		if(source){
			ChatActivityLean.getClose.Close();
		}else {
			CommonFragment.getClose.Close();
		}
		finish();
		overridePendingTransition(R.anim.rightin_item, R.anim.rightout_item);
	}

	private void PersonalDataOK() {
		if(source){
			mPersonalDataBackground.setVisibility(View.VISIBLE);
			Animation animation=new AlphaAnimation(0.0f,1.0f);
			animation.setDuration(500);
			mPersonalDataBackground.startAnimation(animation);
			
			ChatActivityLean.chatRoomClickOK.ChatRoomClickOK();
			
			Intent intent = new Intent(PersonalDataActivity.this, MembersChatActivity.class);
			intent.putExtra("PersonalData", true);
			startActivity(intent);
			
			overridePendingTransition(R.anim.in_item, R.anim.out_item);
		}else {
			mPersonalDataBackground.setVisibility(View.VISIBLE);
			Animation animation=new AlphaAnimation(0.0f,1.0f);
			animation.setDuration(500);
			mPersonalDataBackground.startAnimation(animation);
			
			GroupActivity.clickOK.ClickOK();
			
			Intent intent = new Intent(PersonalDataActivity.this, SlidingActivity.class);
			intent.putExtra("group_group", GroupActivity.getPk_group());
			intent.putExtra("PersonalData", true);
			startActivity(intent);
			overridePendingTransition(R.anim.in_item, R.anim.out_item);
		}
	}

	private void PersonalDataBack() {
		finish();
		Intent intent = new Intent(PersonalDataActivity.this, MainActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.left, R.anim.right);
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

}
