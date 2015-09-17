package com.biju.login;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.BJ.utils.Person;
import com.BJ.utils.RefreshActivity;
import com.BJ.utils.SdPkUser;
import com.biju.IConstant;
import com.biju.MainActivity;
import com.biju.R;

public class BeforeLoginActivity extends Activity implements OnClickListener{
	
	
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		boolean exit=SdPkUser.Exit;
		if(exit)
		{
			setContentView(R.layout.activity_before_login);
			initUI();
		}else
		{

			FileInputStream fis;
			try {
				fis = new FileInputStream(fileName);
				ObjectInputStream ois = new ObjectInputStream(fis);
				Person person = (Person) ois.readObject();
				Integer SD_pk_user = person.pk_user;
				if(SD_pk_user!=null)
				{
					//跳转主界面
					SdPkUser.setsD_pk_user(SD_pk_user);
					Intent intent=new Intent(BeforeLoginActivity.this, MainActivity.class);
					intent.putExtra(IConstant.Sdcard, true);
					startActivity(intent);
				}else
				{
					//跳转手机登录界面
					Intent intent=new Intent(BeforeLoginActivity.this, PhoneLoginActivity.class);
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
		
		}
		RefreshActivity.activList_3.add(BeforeLoginActivity.this);
	}

	private void initUI() {
		findViewById(R.id.BeforeLogin_text_login).setOnClickListener(this);
		findViewById(R.id.BeforeLogin_text_registered).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.before_login, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.BeforeLogin_text_login:
			BeforeLogin_text_login();
			break;
		case R.id.BeforeLogin_text_registered:
			BeforeLogin_text_registered();
			break;
		default:
			break;
		}
	}

	private void BeforeLogin_text_registered() {
		Intent intent=new Intent(BeforeLoginActivity.this, LoginJumpActivity.class);
		intent.putExtra("registered", 1);
		startActivity(intent);
	}

	private void BeforeLogin_text_login() {
		Intent intent=new Intent(BeforeLoginActivity.this, LoginJumpActivity.class);
		startActivity(intent);
		
	}
}
