package com.biju.login;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
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
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cn.jpush.android.service.PushReceiver;

import com.BJ.javabean.Group;
import com.BJ.javabean.Loginback;
import com.BJ.javabean.PicSignBack;
import com.BJ.javabean.User;
import com.BJ.javabean.updateback;
import com.BJ.utils.Ifwifi;
import com.BJ.utils.Person;
import com.biju.Interface;
import com.biju.Interface.getPicSignListenner;
import com.biju.Interface.readUserListenner;
import com.biju.Interface.updateUserListenner;
import com.biju.Interface.userLoginListenner;
import com.biju.MainActivity;
import com.biju.R;
import com.biju.APP.MyApplication;
import com.fragment.HomeFragment;
import com.github.volley_examples.utils.GsonUtils;

public class LoginActivity extends Activity implements OnClickListener {

	private EditText mLogin_account;
	private EditText mLogin_password;
	private ImageView mAuto_login_image;
	private RelativeLayout mManually_login;
	private RelativeLayout mAuto_login;
	private AnimationDrawable drawable;
	private HomeFragment mHomeFragmen;

	public static Integer pk_user;

	public static Integer getPk_user() {
		return pk_user;
	}

	public static void setPk_user(Integer pk_user) {
		LoginActivity.pk_user = pk_user;
	}

	private String mNickname;
	private String mAvatar_path;
	private String mPhone;
	private String mPassword;
	private Interface Login_ReadUserInter;
	private String device_id;
	private Integer status;

	private Integer sex;
	private String setup_time;
	private String last_login_time;
	private boolean isAuto;

	public static ArrayList<Group> list = new ArrayList<Group>();
	private Person person;

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
		setContentView(R.layout.activity_login);
		mHomeFragmen = new HomeFragment();
		initUI();
		mManually_login.setVisibility(View.VISIBLE);
		mAuto_login.setVisibility(View.GONE);
	}

	private void userLogin() {
		Login_ReadUserInter.setPostListener(new userLoginListenner() {

			@Override
			public void success(String A) {
				Log.e("LoginActivity", "用户资料" + A);
				Loginback loginback = GsonUtils.parseJson(A, Loginback.class);
				// 说明是已经登录成功
				if (loginback.getStatusMsg() == 1) {
					Log.e("LoginActivity", "登录成功，账号ID" + A);
					mLogin_account.postDelayed(new Runnable() {

						@Override
						public void run() {
							if (drawable != null) {
								drawable.stop();
							}

							SharedPreferences sp = getSharedPreferences(
									"isLogin", 0);
							Editor editor = sp.edit();
							editor.putBoolean("Login", true);
							editor.commit();

							Intent intent = new Intent(LoginActivity.this,
									MainActivity.class);
							startActivity(intent);
							overridePendingTransition(0, 0);
							finish();
						}
					}, 1000);

					// 每次登陆都更新用户的信息，主要是极光推送的ID
					updateLogin();

					String pk_user = mLogin_account.getText().toString().trim();
					String password = mLogin_password.getText().toString()
							.trim();
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
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	private void updateLogin() {
		User usersetting = new User();
		usersetting.setPk_user(pk_user);
		usersetting.setJpush_id(MyApplication.getRegId());
		usersetting.setNickname(mNickname);
		usersetting.setPassword(mPassword);
		usersetting.setSex(sex);
		usersetting.setStatus(status);
		usersetting.setPhone(mPhone);
		usersetting.setSetup_time(setup_time);
		usersetting.setLast_login_time(last_login_time);
		usersetting.setAvatar_path(mAvatar_path);
		usersetting.setDevice_id(device_id);
		Login_ReadUserInter.updateUser(LoginActivity.this, usersetting);
	}

	private void auto_ReadUser(Integer mpk_user) {
		Login_ReadUserInter = Interface.getInstance();
		User readuser = new User();
		readuser.setPk_user(mpk_user);
		Login_ReadUserInter.readUser(LoginActivity.this, readuser);
		Login_ReadUserInter.setPostListener(new readUserListenner() {

			@Override
			public void success(String A) {
				Log.e("LoginActivity", "用户资料" + A);
				Loginback loginbackread = GsonUtils.parseJson(A,
						Loginback.class);
				int aa = loginbackread.getStatusMsg();
				if (aa == 1) {
					// 取第一个Users[0]
					List<User> Users = loginbackread.getReturnData();
					if (Users.size() >= 1) {
						User readuser = Users.get(0);
						pk_user = readuser.getPk_user();
						mNickname = readuser.getNickname();
						mAvatar_path = readuser.getAvatar_path();
						mPhone = readuser.getPhone();
						mPassword = readuser.getPassword();
						sex = readuser.getSex();
						setup_time = readuser.getSetup_time();
						last_login_time = readuser.getLast_login_time();
						device_id = readuser.getDevice_id();
						status = readuser.getStatus();

					}
					loadBaseNeedLoginMethod(pk_user);

					if (!isAuto) {
						String mPassword_tv = mLogin_password.getText()
								.toString().trim();
						if (String.valueOf(mPassword_tv).equals(
								String.valueOf(mPassword))) {
							User loginuser = new User();
							loginuser.setPk_user(pk_user);
							loginuser.setPassword(mPassword);
							Login_ReadUserInter.userLogin(LoginActivity.this,
									loginuser);
						} else {
							mManually_login.setVisibility(View.VISIBLE);
							mAuto_login.setVisibility(View.GONE);
							Toast.makeText(LoginActivity.this, "账号或者密码错误!",
									Toast.LENGTH_SHORT).show();
						}
					} else {
						if (String.valueOf(person.password).equals(
								String.valueOf(mPassword))) {
							User autologinuser = new User();
							autologinuser.setPk_user(pk_user);
							autologinuser.setPassword(person.password);
							Login_ReadUserInter.userLogin(LoginActivity.this,
									autologinuser);
						} else {
							mManually_login.setVisibility(View.VISIBLE);
							mAuto_login.setVisibility(View.GONE);
							Toast.makeText(LoginActivity.this, "密码错误!",
									Toast.LENGTH_SHORT).show();
						}
					}

				}

			}

			@Override
			public void defail(Object B) {

			}
		});

		Login_ReadUserInter.setPostListener(new updateUserListenner() {

			@Override
			public void success(String A) {
				// 更新用户资料成功
				updateback usersetting_updateback = GsonUtils.parseJson(A,
						updateback.class);
				int a = usersetting_updateback.getStatusMsg();
				if (a == 1) {
					Log.e("LoginActivity", "更新成功" + A);
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	private void loadBaseNeedLoginMethod(Integer pk_user) {

		// 首页数据更新
		if (mHomeFragmen != null) {
			mHomeFragmen.prepareData(pk_user);
		}

	}

	private void initUI() {
		findViewById(R.id.Login_OK).setOnClickListener(this);
		findViewById(R.id.Login_registered).setOnClickListener(this);
		mLogin_account = (EditText) findViewById(R.id.Login_account);
		mLogin_account.setInputType(EditorInfo.TYPE_CLASS_PHONE);// 点击账号时直接弹出数字键盘
		mLogin_password = (EditText) findViewById(R.id.Login_password);
		mAuto_login_image = (ImageView) findViewById(R.id.Auto_login_image);
		mManually_login = (RelativeLayout) findViewById(R.id.Manually_login);
		mAuto_login = (RelativeLayout) findViewById(R.id.Auto_login);
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
		Login_ReadUserInter = Interface.getInstance();
		Login_ReadUserInter.setPostListener(new getPicSignListenner() {

			@Override
			public void success(String A) {
				Log.e("LoginActivity", "签名字符串：" + A);
				PicSignBack picSignBack = GsonUtils.parseJson(A,
						PicSignBack.class);
				String returnData = picSignBack.getReturnData();
				RegisteredActivity.setSIGN(returnData);

				Intent intent = new Intent(LoginActivity.this,
						RegisteredActivity.class);
				startActivity(intent);
				finish();
			}

			@Override
			public void defail(Object B) {

			}
		});
		Login_ReadUserInter.getPicSign(this, new User());
	}

	private void Login_OK() {
		boolean isWIFI = Ifwifi.getNetworkConnected(LoginActivity.this);
		if (isWIFI) {
			String mUser = mLogin_account.getText().toString().trim();
			auto_ReadUser(Integer.valueOf(mUser));
			userLogin();// 登录监听
		} else {
			Toast.makeText(LoginActivity.this, "网络异常，请检查网络!",
					Toast.LENGTH_SHORT).show();
		}
		userLogin();// 登录监听
	}

	@Override
	protected void onStop() {
		String pk_user = mLogin_account.getText().toString().trim();
		String password = mLogin_password.getText().toString().trim();
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
		SharedPreferences login_sp = getSharedPreferences("Logout", 0);
		boolean isLogout = login_sp.getBoolean("isLogout", false);
		if (isLogout) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(fileName);
				ObjectInputStream ois = new ObjectInputStream(fis);
				Person person = (Person) ois.readObject();
				mLogin_account.setText(person.pk_user);
				mLogin_password.setText(person.password);
				mManually_login.setVisibility(View.VISIBLE);
				mAuto_login.setVisibility(View.GONE);
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
			Auto_Login();
		}
		super.onStart();
	}

	private void Auto_Login() {
		boolean isWIFI = Ifwifi.getNetworkConnected(LoginActivity.this);
		if (isWIFI) {
			FileInputStream fis;
			try {
				SharedPreferences sp = getSharedPreferences("isLogin", 0);
				boolean onelogin = sp.getBoolean("Login", false);
				if (onelogin) {
					Log.e("LoginActivity", "sd卡路径" + fileName);
					fis = new FileInputStream(fileName);
					ObjectInputStream ois = new ObjectInputStream(fis);
					person = (Person) ois.readObject();
					mLogin_account.setText(person.pk_user);
					mLogin_password.setText(person.password);
					mManually_login.setVisibility(View.GONE);
					mAuto_login.setVisibility(View.VISIBLE);
					drawable = (AnimationDrawable) mAuto_login_image
							.getDrawable();
					drawable.start();
					if (!("".equals(person.pk_user) && ""
							.equals(person.password))) {
						Log.e("LoginActivity", "进入到这里了333333==========");
						auto_ReadUser(Integer.valueOf(person.pk_user));
						isAuto = true;
						userLogin();// 登录监听
					}
				} else {
					mManually_login.setVisibility(View.VISIBLE);
					mAuto_login.setVisibility(View.GONE);
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
		} else {
			mManually_login.setVisibility(View.VISIBLE);
			mAuto_login.setVisibility(View.GONE);
			Toast.makeText(LoginActivity.this, "网络异常，请检查网络!",
					Toast.LENGTH_SHORT).show();
		}
	}
}
