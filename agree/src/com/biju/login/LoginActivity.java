package com.biju.login;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.BJ.javabean.Loginback;
import com.BJ.javabean.User;
import com.BJ.utils.Person;
import com.biju.Interface;
import com.biju.Interface.UserInterface;
import com.biju.MainActivity;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;

public class LoginActivity extends Activity implements OnClickListener {

	private EditText mLogin_account;
	private EditText mLogin_password;
	private Person person;
	private Interface logininter;
	private ImageView auto_login_image;
	private RelativeLayout manually_login;
	private RelativeLayout auto_login;
	private AnimationDrawable drawable;

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

	private String fileName = getSDPath() + "/" + "saveData";
	private boolean login = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initUI();
		initData();
		manually_login.setVisibility(View.VISIBLE);
		auto_login.setVisibility(View.GONE);
	}

	private void initData() {
		logininter = new Interface();
		logininter.setPostListener(new UserInterface() {

			@Override
			public void success(String A) {
				Loginback loginback = GsonUtils.parseJson(A, Loginback.class);
				// 说明是已经登录成功
				if (loginback.getStatusMsg() == 1) {
					Log.e("账号ID", A);
					mLogin_account.postDelayed(new Runnable() {

						@Override
						public void run() {
							if (drawable != null) {
								drawable.stop();
							}
							Intent intent = new Intent(LoginActivity.this,
									MainActivity.class);
							startActivity(intent);
							overridePendingTransition(0, 0);
							finish();
						}
					}, 1000);
					// 取第一个Users[0]
					List<User> Users = loginback.getReturnData();
					if (Users.size() >= 1) {
						User user = Users.get(0);
						Log.e("解析出来的", user.getPassword());
					}
				} else {
					if (drawable != null) {
						drawable.stop();
					}
					Toast.makeText(LoginActivity.this, "账号或者密码错误!",
							Toast.LENGTH_SHORT).show();
					manually_login.setVisibility(View.VISIBLE);
					auto_login.setVisibility(View.GONE);
				}
			}

			@Override
			public void defail(Object B) {
				Log.e("账号ID", "失败");
			}
		});

	}

	private void initUI() {
		findViewById(R.id.Login_OK).setOnClickListener(this);
		findViewById(R.id.Login_registered).setOnClickListener(this);
		mLogin_account = (EditText) findViewById(R.id.Login_account);
		mLogin_password = (EditText) findViewById(R.id.Login_password);
		auto_login_image = (ImageView) findViewById(R.id.Auto_login_image);
		manually_login = (RelativeLayout) findViewById(R.id.Manually_login);
		auto_login = (RelativeLayout) findViewById(R.id.Auto_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Login_OK:
			Login_OK();
			break;
		case R.id.Login_registered:
			Login_registered();
			break;
		default:
			break;
		}
	}

	private void Login_registered() {
		Intent intent = new Intent(LoginActivity.this, RegisteredActivity.class);
		startActivity(intent);
	}

	private void Login_OK() {
		String mUser = mLogin_account.getText().toString().trim();
		String mPassword = mLogin_password.getText().toString().trim();
		User user = new User();
		user.setPk_user(Integer.valueOf(mUser));
		user.setPassword(mPassword);
		logininter.userLogin(LoginActivity.this, user);

		SharedPreferences sp = getSharedPreferences("isLogin", 0);
		Editor editor = sp.edit();
		editor.putBoolean("Login", true);
		editor.commit();
	}

	@Override
	protected void onStop() {
		String pk_user = mLogin_account.getText().toString().trim();
		String password = mLogin_password.getText().toString().trim();
		Log.e("sd卡路径", fileName);
		Person person = new Person(pk_user, password);
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(fileName));
			oos.writeObject(person);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.onStop();
	}

	@Override
	protected void onStart() {

		Log.e("onStart() ", "有调用到");
		FileInputStream fis;
		try {
			fis = new FileInputStream(fileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Person person = (Person) ois.readObject();
			mLogin_account.setText(person.pk_user);
			mLogin_password.setText(person.password);
			Log.e("person.pk_user", person.pk_user);
			Log.e("person.password", person.password);
			SharedPreferences sp = getSharedPreferences("isLogin", 0);
			login = sp.getBoolean("Login", false);
			if (login) {
				if (!("".equals(person.pk_user) && "".equals(person.password))) {
					User user = new User();
					user.setPk_user(Integer.valueOf(person.pk_user));
					user.setPassword(person.password);
					logininter.userLogin(LoginActivity.this, user);
					manually_login.setVisibility(View.GONE);
					auto_login.setVisibility(View.VISIBLE);
					drawable = (AnimationDrawable) auto_login_image
							.getDrawable();
					drawable.start();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		super.onStart();
	}

}
