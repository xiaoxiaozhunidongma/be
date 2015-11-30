package com.biju.login;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import com.BJ.utils.Person;
import com.BJ.utils.SdPkUser;
import com.biju.IConstant;
import com.biju.MainActivity;
import com.biju.R;

public class WelComeActivity extends Activity {

	private String fileName = getSDPath() + "/" + "saveData";

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
	}

	private void initActivity() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				SharedPreferences Welcome_sp = getSharedPreferences("WelCome",
						0);
				boolean welcome = Welcome_sp.getBoolean("welcome", false);
				if (welcome) {
					FileInputStream fis;
					try {
						fis = new FileInputStream(fileName);
						ObjectInputStream ois = new ObjectInputStream(fis);
						Person person = (Person) ois.readObject();
						final Integer SD_pk_user = person.pk_user;
						if (SD_pk_user != null) {
							// 跳转主界面
							SdPkUser.setsD_pk_user(SD_pk_user);
							Intent intent = new Intent(WelComeActivity.this,
									MainActivity.class);
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
					Intent intent = new Intent(WelComeActivity.this,
							BeforeLoginActivity.class);
					startActivity(intent);
				}
				finish();
			}
		}, 1000);

	}

}
