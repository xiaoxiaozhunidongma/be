package com.biju.function;

import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
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

import com.BJ.javabean.Loginback;
import com.BJ.javabean.User;
import com.BJ.javabean.User_User;
import com.BJ.utils.Ifwifi;
import com.BJ.utils.ImageLoaderUtils;
import com.biju.Interface;
import com.biju.R;
import com.biju.login.LoginActivity;
import com.biju.Interface.findUserListenner;
import com.github.volley_examples.utils.GsonUtils;

public class FindFriendsActivity extends Activity implements OnClickListener {

	private EditText mFindfriends_number;
	private RelativeLayout mFindfriends_before;
	private RelativeLayout mFindfriends_after;
	private ImageView mFindfriends_head;
	private TextView mFindfriends_nickname;
	private Interface findfriends_inter_before;
	private int i = 1;
	private String beginStr = "http://201139.image.myqcloud.com/201139/0/";
	private String endStr = "/original";

	// ����·��completeURL=beginStr+result.filepath+endStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_friends);
		initUI();
		initInterface();
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
						Log.e("FindFriendsActivity",
								"ͼƬ·��" + user.getAvatar_path());

					}
					String completeURL = beginStr + userAvatar_path + endStr;
					ImageLoaderUtils.getInstance().LoadImage(
							FindFriendsActivity.this, completeURL,
							mFindfriends_head);

				} else {
					Toast.makeText(FindFriendsActivity.this, "���Һ���ʧ�ܣ������²���!",
							Toast.LENGTH_SHORT).show();
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
		findViewById(R.id.findfriends_sendrequest).setOnClickListener(this);// ���ͺ�������
		findViewById(R.id.findfriends_againfind).setOnClickListener(this);// ���²���
		mFindfriends_head = (ImageView) findViewById(R.id.findfriends_head);// ����ͷ��
		mFindfriends_nickname = (TextView) findViewById(R.id.findfriends_nickname);// ���ѱؾۺŻ����ֻ���
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.find_friends, menu);
		return true;
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
		String pk_user = mFindfriends_number.getText().toString().trim();
		SharedPreferences sp = getSharedPreferences("Registered", 0);
		boolean isRegistered_one = sp.getBoolean("isRegistered_one", false);
		int returndata = sp.getInt("returndata", 0);
		SharedPreferences sp1 = getSharedPreferences("isLogin", 0);
		boolean login = sp1.getBoolean("Login", false);
		// Log.e("FindFriendsActivity", "returndata"+returndata);
		User_User user_User = new User_User();
		if (isRegistered_one) {
			user_User.setFk_user_from(returndata);
		} else {
			if (login) {
				int user = LoginActivity.getPk_user();
				user_User.setFk_user_from(user);
			} else {
				user_User.setFk_user_from(returndata);
			}
		}
		user_User.setFk_user_to(Integer.valueOf(pk_user));
//		findfriends_inter_before.addFriend(FindFriendsActivity.this, user_User);
	}

	private void findfriends_againfind() {
		mFindfriends_before.setVisibility(View.VISIBLE);
		mFindfriends_after.setVisibility(View.GONE);
	}

	private void findfriends_find() {
		boolean isWIFI = Ifwifi.getNetworkConnected(FindFriendsActivity.this);
		if (isWIFI) {
			String pk_user = mFindfriends_number.getText().toString().trim();
			User user = new User();
			user.setPhone(pk_user);
			findfriends_inter_before.findUser(FindFriendsActivity.this, user);
		} else {
			Toast.makeText(FindFriendsActivity.this, "�����쳣����������!",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void findfriends_back() {
		finish();
	}

}
