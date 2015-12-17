package com.biju.login;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.BJ.javabean.Loginback;
import com.BJ.javabean.User;
import com.BJ.javabean.updateback;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.Person;
import com.BJ.utils.PreferenceUtils;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.FindMultiUserListenner;
import com.biju.Interface.updateUserListenner;
import com.biju.APP.MyApplication;
import com.biju.function.NicknameActivity;
import com.biju.function.PartyDetailsActivity;
import com.biju.MainActivity;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;

public class WelComeActivity extends Activity {

	private String fileName = getSDPath() + "/" + "saveData";
	private Interface mWelcomeInterface;
	private List<String>  list=new ArrayList<String>();

	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		// 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		return sdDir.toString();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wel_come);
		initActivity();
		initInterface();
	}

	private void initInterface() {
		mWelcomeInterface = Interface.getInstance();
		mWelcomeInterface.setPostListener(new FindMultiUserListenner() {
			
			@Override
			public void success(String A) {
				Log.e("WelComeActivity", "返回的当前用户信息====" + A);
				Loginback findfriends_statusmsg = GsonUtils.parseJson(A,Loginback.class);
				int statusmsg = findfriends_statusmsg.getStatusMsg();
				if (statusmsg == 1) {
					// 取第一个Users[0]
					List<User> Users = findfriends_statusmsg.getReturnData();
					if (Users.size() >= 1) {
						User user = Users.get(0);
						
						User usersetting = new User();
						usersetting.setPk_user(user.getPk_user());
						usersetting.setJpush_id(MyApplication.getRegId());
						usersetting.setNickname(user.getNickname());
						usersetting.setPassword(user.getPassword());
						usersetting.setSex(user.getSex());
						usersetting.setStatus(1);
						usersetting.setPhone(user.getPhone());
						usersetting.setWechat_id(user.getWechat_id());
						usersetting.setSetup_time(user.getSetup_time());
						usersetting.setLast_login_time(user.getLast_login_time());
						usersetting.setAvatar_path(user.getAvatar_path());
						mWelcomeInterface.updateUser(WelComeActivity.this, usersetting);
					}
				}
			}
			
			@Override
			public void defail(Object B) {
				
			}
		});
		
		mWelcomeInterface.setPostListener(new updateUserListenner() {
			
			@Override
			public void success(String A) {
				// 更新用户资料成功
				updateback usersetting_updateback = GsonUtils.parseJson(A,updateback.class);
				int a = usersetting_updateback.getStatusMsg();
				if (a == 1) {
					Log.e("WelComeActivity", "更新成功=======" + A);
				}
			}
			
			@Override
			public void defail(Object B) {
				
			}
		});
	}

	private void initActivity() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				SharedPreferences Welcome_sp = getSharedPreferences("WelCome",0);
				boolean welcome = Welcome_sp.getBoolean("welcome", false);
				if (welcome) {
					FileInputStream fis;
					try {
						fis = new FileInputStream(fileName);
						ObjectInputStream ois = new ObjectInputStream(fis);
						Person person = (Person) ois.readObject();
						final Integer SD_pk_user = person.pk_user;
						if (SD_pk_user != null) {
							list.add(String.valueOf(SD_pk_user));
							mWelcomeInterface.findMultiUsers(WelComeActivity.this, list);
							
							Intent intent = new Intent(WelComeActivity.this,MainActivity.class);
							intent.putExtra(IConstant.Sdcard, true);
							startActivity(intent);
						}
						ois.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (StreamCorruptedException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				} else {
					Intent intent = new Intent(WelComeActivity.this,BeforeLoginActivity.class);
					startActivity(intent);
				}
				finish();
			}
		}, 1000);

	}

}
