package com.biju.login;

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
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
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
	private String savePath = "/mnt/sdcard/data1.txt";
	private Person person;
	private Interface logininter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initUI();
		initData();
	}

	private void initData() {
		logininter = new Interface();
		logininter.setPostListener(new UserInterface() {

			@Override
			public void success(String A) {
				Log.e("账号ID", A);
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(intent);
				overridePendingTransition(0, 0);
				finish();
				 Loginback loginback = GsonUtils.parseJson(A,
				 Loginback.class);
				 //取第一个Users[0]
				 List<User> Users = loginback.getReturnData();
				 if(Users.size()>=1){
					 User user=Users.get(0);
					 Log.e("解析出来的", user.getPassword());
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
//		Interface logininter = new Interface();
		logininter.userLogin(LoginActivity.this, user);
	}

	@Override
	protected void onStop() {
		String pk_user = mLogin_account.getText().toString().trim();
		String password = mLogin_password.getText().toString().trim();
		Person person = new Person(pk_user, password);
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(savePath));
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
		FileInputStream fis;
		try {
			fis = new FileInputStream(savePath);
			ObjectInputStream ois = new ObjectInputStream(fis);
			person = (Person) ois.readObject();
			mLogin_account.setText(person.pk_user);
			 mLogin_password.setText(person.password);
			if (!("".equals(person.pk_user) && "".equals(person.password))) {
				//如果起个线程可以加个动画效果
//				mLogin_account.postDelayed(new Runnable() {
//					
//					@Override
//					public void run() {
						User user = new User();
						user.setPk_user(Integer.valueOf(person.pk_user));
						user.setPassword(person.password);
//						Interface logininter = new Interface();
						logininter.userLogin(LoginActivity.this, user);
//					}
//				}, 1000);
			}else {
				Intent intent = new Intent(LoginActivity.this,
						LoginActivity.class);
				startActivity(intent);
				overridePendingTransition(0, 0);
				finish();
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
