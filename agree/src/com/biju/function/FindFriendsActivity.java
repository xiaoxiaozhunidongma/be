package com.biju.function;

import java.util.List;
import java.util.zip.Inflater;

import com.BJ.javabean.Loginback;
import com.BJ.javabean.User;
import com.BJ.javabean.User_User;
import com.biju.Interface;
import com.biju.Interface.UserInterface;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FindFriendsActivity extends Activity implements OnClickListener {

	private EditText mFindfriends_number;
	private RelativeLayout mFindfriends_before;
	private RelativeLayout mFindfriends_after;
	private ImageView mFindfriends_head;
	private TextView mFindfriends_nickname;
	private Interface findfriends_inter_before;
	private int i = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_friends);
		initUI();
		initInterface();
	}

	private void initInterface() {
		findfriends_inter_before = new Interface();
		findfriends_inter_before.setPostListener(new UserInterface() {

			@Override
			public void success(String A) {
				Loginback findfriends_statusmsg = GsonUtils.parseJson(A,
						Loginback.class);
				int statusmsg = findfriends_statusmsg.getStatusMsg();
				if (statusmsg == 1) {
					mFindfriends_before.setVisibility(View.GONE);
					mFindfriends_after.setVisibility(View.VISIBLE);
					Log.e("FindFriendsActivity", "查找好友" + A + "第" + i + "次");
					i++;
					// 取第一个Users[0]
					List<User> Users = findfriends_statusmsg.getReturnData();
					if (Users.size() >= 1) {
						User user = Users.get(0);
						mFindfriends_nickname.setText(user.getNickname());
						Log.e("FindFriendsActivity",
								"图片路径" + user.getAvatar_path());
					}

				} else {
					Toast.makeText(FindFriendsActivity.this, "查找好友失败，请重新查找!",
							Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	private void initUI() {
		findViewById(R.id.findfriends_back_layout).setOnClickListener(this);// 返回
		findViewById(R.id.findfriends_back).setOnClickListener(this);
		findViewById(R.id.findfriends_find).setOnClickListener(this);// 查找
		mFindfriends_number = (EditText) findViewById(R.id.findfriends_number);// 输入必聚号或者手机号
		mFindfriends_before = (RelativeLayout) findViewById(R.id.findfriends_before);// 查找之前
		mFindfriends_after = (RelativeLayout) findViewById(R.id.findfriends_after);// 查找之后
		findViewById(R.id.findfriends_sendrequest).setOnClickListener(this);// 发送好友请求
		findViewById(R.id.findfriends_againfind).setOnClickListener(this);// 重新查找
		mFindfriends_head = (ImageView) findViewById(R.id.findfriends_head);// 好友头像
		mFindfriends_nickname = (TextView) findViewById(R.id.findfriends_nickname);// 好友必聚号或者手机号
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
		int returndata = sp.getInt("returndata", 0);
		User_User user_User = new User_User();
		user_User.setFk_user_from(returndata);
		user_User.setFk_user_to(Integer.valueOf(pk_user));
		findfriends_inter_before.addFriend(FindFriendsActivity.this, user_User);
	}

	private void findfriends_againfind() {
		mFindfriends_before.setVisibility(View.VISIBLE);
		mFindfriends_after.setVisibility(View.GONE);
	}

	private void findfriends_find() {
		String pk_user = mFindfriends_number.getText().toString().trim();
		User user = new User();
		user.setPhone(pk_user);
		findfriends_inter_before.findUser(FindFriendsActivity.this, user);
	}

	private void findfriends_back() {
		finish();
	}

}
