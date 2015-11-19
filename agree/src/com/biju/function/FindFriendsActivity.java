package com.biju.function;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.BJ.javabean.AddFriendsback;
import com.BJ.javabean.CheckFriends;
import com.BJ.javabean.CheckFriendsback;
import com.BJ.javabean.Loginback;
import com.BJ.javabean.User;
import com.BJ.javabean.User_User;
import com.BJ.utils.Ifwifi;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.SdPkUser;
import com.biju.Interface;
import com.biju.Interface.addFriendListenner;
import com.biju.Interface.checkFriendListenner;
import com.biju.Interface.findUserListenner;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;

public class FindFriendsActivity extends Activity implements OnClickListener {

	private EditText mFindfriends_number;
	private RelativeLayout mFindfriends_before;
	private RelativeLayout mFindfriends_after;
	private ImageView mFindfriends_head;
	private TextView mFindfriends_nickname;
	private Interface findfriends_inter_before;
	private int i = 1;
	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "";
	private String pk_user;
	private ArrayList<CheckFriends> checkfriendslist = new ArrayList<CheckFriends>();
	private TextView mFindfriends_sendrequest;

	// ����·��completeURL=beginStr+result.filepath+endStr;
	
	private Integer SD_pk_user;
	private boolean isRegistered_one;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_friends);
		
		//�ṩ�������ж�
		isRegistered_one=SdPkUser.isRegistered_one();
		
		//��ȡSD���е�pk_user
		SD_pk_user = SdPkUser.getsD_pk_user();
		Log.e("FindFriendsActivity", "��SD���л�ȡ����Pk_user" + SD_pk_user);

		initInterface();
		initUI();
	}

	private void initInterface() {
		findfriends_inter_before = Interface.getInstance();
		findfriends_inter_before.setPostListener(new findUserListenner() {

			private String userAvatar_path;

			@Override
			public void success(String A) {
				Loginback findfriends_statusmsg = GsonUtils.parseJson(A,
						Loginback.class);
				int statusmsg = findfriends_statusmsg.getStatusMsg();
				if (statusmsg == 1) {
					mFindfriends_before.setVisibility(View.GONE);
					mFindfriends_after.setVisibility(View.VISIBLE);
					Log.e("FindFriendsActivity", "���Һ���" + A + "��" + i + "��");
					i++;
					// ȡ��һ��Users[0]
					List<User> Users = findfriends_statusmsg.getReturnData();
					if (Users.size() >= 1) {
						User user = Users.get(0);
						mFindfriends_nickname.setText(user.getNickname());
						userAvatar_path = user.getAvatar_path();
						Log.e("FindFriendsActivity","ͼƬ·��" + user.getAvatar_path());

					}
					String completeURL = beginStr + userAvatar_path + endStr;
					ImageLoaderUtils.getInstance().LoadImage(
							FindFriendsActivity.this, completeURL,
							mFindfriends_head);
				} else {
					Toast.makeText(FindFriendsActivity.this, "���Һ���ʧ�ܣ������²���!",Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public void defail(Object B) {

			}
		});

		findfriends_inter_before.setPostListener(new addFriendListenner() {

			@Override
			public void success(String A) {
				AddFriendsback addFriendsback=GsonUtils.parseJson(A, AddFriendsback.class);
				int status=addFriendsback.getStatusMsg();
				if(status==1)
				{
					Log.e("FindFriendsActivity", "�����Ƿ���ӳɹ�=======" + A);
					finish();
				}
			}

			@Override
			public void defail(Object B) {
			}
		});

		findfriends_inter_before.setPostListener(new checkFriendListenner() {

			@Override
			public void success(String A) {
				CheckFriendsback checkFriendsback = GsonUtils.parseJson(A,
						CheckFriendsback.class);
				Integer status = checkFriendsback.getStatusMsg();
				if (status == 1) {
					Log.e("FindFriendsActivity", "���ؼ����ѵĽ��=======" + A);
					List<CheckFriends> friendsbacks = checkFriendsback
							.getReturnData();
					if (friendsbacks.size() > 0) {
						for (int i = 0; i < friendsbacks.size(); i++) {
							CheckFriends checkFriends = friendsbacks.get(i);
							Log.e("FindFriendsActivity", "pk_user======="+ pk_user);
							if (Integer.valueOf(pk_user) == checkFriends.getPk_user()) {
								Toast.makeText(FindFriendsActivity.this,"������ӹ��ú��ѣ�", Toast.LENGTH_SHORT).show();
								mFindfriends_before.setVisibility(View.VISIBLE);
								mFindfriends_after.setVisibility(View.GONE);
							}

						}
					}
				} else {
					Log.e("FindFriendsActivity", "�����˷��ؽ����Ϊ1��=======");
					// ���Һ�����Ϣ
					User user = new User();
					user.setPhone(pk_user);
					findfriends_inter_before.findUser(FindFriendsActivity.this,user);
				}
			}

			@Override
			public void defail(Object B) {
			}
		});
	}

	private void initUI() {
		findViewById(R.id.findfriends_back_layout).setOnClickListener(this);// ����
		findViewById(R.id.findfriends_back).setOnClickListener(this);
		findViewById(R.id.findfriends_find).setOnClickListener(this);// ����
		mFindfriends_number = (EditText) findViewById(R.id.findfriends_number);// ����ؾۺŻ����ֻ���
		mFindfriends_before = (RelativeLayout) findViewById(R.id.findfriends_before);// ����֮ǰ
		mFindfriends_after = (RelativeLayout) findViewById(R.id.findfriends_after);// ����֮��
		mFindfriends_sendrequest = (TextView) findViewById(R.id.findfriends_sendrequest);
		mFindfriends_sendrequest.setOnClickListener(this);// ���ͺ�������
		findViewById(R.id.findfriends_againfind).setOnClickListener(this);// ���²���
		mFindfriends_head = (ImageView) findViewById(R.id.findfriends_head);// ����ͷ��
		mFindfriends_nickname = (TextView) findViewById(R.id.findfriends_nickname);// ���ѱؾۺŻ����ֻ���
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.findfriends_back_layout:
		case R.id.findfriends_back:
			findfriends_back();
			break;
		case R.id.findfriends_find:
			findfriends_find();
			break;
		case R.id.findfriends_againfind:
			findfriends_againfind();
			break;
		case R.id.findfriends_sendrequest:
			findfriends_sendrequest();
			break;
		default:
			break;
		}
	}

	private void findfriends_sendrequest() {
		User_User user_User = new User_User();
		user_User.setFk_user_from(SD_pk_user);
		user_User.setFk_user_to(Integer.valueOf(pk_user));
		user_User.setRelationship(1);
		user_User.setStatus(1);
		findfriends_inter_before.addFriend(FindFriendsActivity.this, user_User);
		mFindfriends_sendrequest.setText("�ѷ��ͺ�������");
		mFindfriends_sendrequest.setEnabled(false);
		mFindfriends_sendrequest.setTextColor(R.color.lightslategray);
	}

	private void findfriends_againfind() {
		mFindfriends_before.setVisibility(View.VISIBLE);
		mFindfriends_after.setVisibility(View.GONE);
	}

	private void findfriends_find() {
		boolean isWIFI = Ifwifi.getNetworkConnected(FindFriendsActivity.this);
		if (isWIFI) {
			pk_user = mFindfriends_number.getText().toString().trim();
			if(isRegistered_one)
			{
				User user = new User();
				user.setPhone(pk_user);
				findfriends_inter_before.findUser(FindFriendsActivity.this,user);
			}else
			{
				// �����ѹ�ϵ
				User_User user_User = new User_User();
				user_User.setFk_user_from(SD_pk_user);
				user_User.setFk_user_to(Integer.valueOf(pk_user));
				findfriends_inter_before.checkFriend(FindFriendsActivity.this,user_User);
			}

		} else {
			Toast.makeText(FindFriendsActivity.this, "�����쳣����������!",Toast.LENGTH_SHORT).show();
		}
	}

	private void findfriends_back() {
		finish();
	}

}
