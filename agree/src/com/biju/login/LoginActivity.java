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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.BJ.javabean.Group;
import com.BJ.javabean.Loginback;
import com.BJ.javabean.PicSignBack;
import com.BJ.javabean.User;
import com.BJ.utils.Ifwifi;
import com.BJ.utils.Person;
import com.biju.Interface;
import com.biju.Interface.getPicSignListenner;
import com.biju.Interface.readUserListenner;
import com.biju.Interface.userLoginListenner;
import com.biju.MainActivity;
import com.biju.R;
import com.fragment.HomeFragment;
import com.fragment.PartyFragment;
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
	private boolean onelogin = false;
	private Interface Login_ReadUserInter;
	private String mJpush_id;

	private String fileName = getSDPath() + "/" + "saveData";
	private PartyFragment mPartyFragment;

	public static ArrayList<Group> list = new ArrayList<Group>();

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
		mPartyFragment = new PartyFragment();
		initUI();
		mManually_login.setVisibility(View.VISIBLE);
		mAuto_login.setVisibility(View.GONE);
	}

	private void userLogin() {
		Login_ReadUserInter.setPostListener(new userLoginListenner() {

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
						mJpush_id = readuser.getJpush_id();
					}
					loadBaseNeedLoginMethod(pk_user);
					String mPassword = mLogin_password.getText().toString()
							.trim();
					if (mPassword.equals(mPassword)) {
						Loginback loginback = GsonUtils.parseJson(A,
								Loginback.class);
						// 说明是已经登录成功
						if (loginback.getStatusMsg() == 1) {
							Log.e("LoginActivity", "登录成功，账号ID" + A);
							mLogin_account.postDelayed(new Runnable() {

								@Override
								public void run() {
									if (drawable != null) {
										drawable.stop();
									}
									Intent intent = new Intent(
											LoginActivity.this,
											MainActivity.class);
									startActivity(intent);
									overridePendingTransition(0, 0);
									finish();
								}
							}, 1000);
							
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
							// 取第一个Users[0]
							// List<User> Users = loginback.getReturnData();
							// if (Users.size() >= 1) {
							// User user = Users.get(0);
							// Log.e("解析出来的",
							// user.getPhone() + "====" + user.getNickname());
							// }
						}
					} else {
						if (drawable != null) {
							drawable.stop();
						}
						mManually_login.setVisibility(View.VISIBLE);
						mAuto_login.setVisibility(View.GONE);
						Toast.makeText(LoginActivity.this, "账号或者密码错误!",
								Toast.LENGTH_SHORT).show();
					}
				}

			}

			@Override
			public void defail(Object B) {

			}
		});
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
						mJpush_id = readuser.getJpush_id();
					}
					loadBaseNeedLoginMethod(pk_user);
					String mPassword = mLogin_password.getText().toString()
							.trim();
					if (mPassword.equals(mPassword)) {
						Loginback loginback = GsonUtils.parseJson(A,
								Loginback.class);
						// 说明是已经登录成功
						if (loginback.getStatusMsg() == 1) {
							Log.e("LoginActivity", "登录成功，账号ID" + A);
							mLogin_account.postDelayed(new Runnable() {

								@Override
								public void run() {
									if (drawable != null) {
										drawable.stop();
									}
									Intent intent = new Intent(
											LoginActivity.this,
											MainActivity.class);
									startActivity(intent);
									overridePendingTransition(0, 0);
									finish();
								}
							}, 1000);
							
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
							// 取第一个Users[0]
							// List<User> Users = loginback.getReturnData();
							// if (Users.size() >= 1) {
							// User user = Users.get(0);
							// Log.e("解析出来的",
							// user.getPhone() + "====" + user.getNickname());
							// }
						}
					} else {
						if (drawable != null) {
							drawable.stop();
						}
						mManually_login.setVisibility(View.VISIBLE);
						mAuto_login.setVisibility(View.GONE);
						Toast.makeText(LoginActivity.this, "账号或者密码错误!",
								Toast.LENGTH_SHORT).show();
					}
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
			mHomeFragmen.prepareParty(true);
		}

	}

	private void initUI() {
		findViewById(R.id.Login_OK).setOnClickListener(this);
		findViewById(R.id.Login_registered).setOnClickListener(this);
		mLogin_account = (EditText) findViewById(R.id.Login_account);
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
		Login_ReadUserInter=Interface.getInstance();
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
			String mPassword2 = mLogin_password.getText().toString().trim();
			auto_ReadUser(Integer.valueOf(mUser));
			if (mPassword2.equals(mPassword)) {
				User loginuser = new User();
				loginuser.setPk_user(pk_user);
				loginuser.setAvatar_path(mAvatar_path);
				loginuser.setNickname(mNickname);
				loginuser.setPassword(mPassword);
				loginuser.setPhone(mPhone);
				loginuser.setJpush_id(mJpush_id);
				Login_ReadUserInter.userLogin(LoginActivity.this, loginuser);
			}

			SharedPreferences sp = getSharedPreferences("isLogin", 0);
			Editor editor = sp.edit();
			editor.putBoolean("Login", true);
			editor.commit();
		} else {
			Toast.makeText(LoginActivity.this, "网络异常，请检查网络!",
					Toast.LENGTH_SHORT).show();
		}
		userLogin();//登录监听
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
		if (!isLogout) {
			boolean isWIFI = Ifwifi.getNetworkConnected(LoginActivity.this);
			if (isWIFI) {
				FileInputStream fis;
				try {
					Log.e("LoginActivity", "sd卡路径" + fileName);
					fis = new FileInputStream(fileName);
					ObjectInputStream ois = new ObjectInputStream(fis);
					Person person = (Person) ois.readObject();
					mLogin_account.setText(person.pk_user);
					mLogin_password.setText(person.password);
					Log.e("person.pk_user", person.pk_user);
					Log.e("person.password", person.password);
					SharedPreferences sp = getSharedPreferences("isLogin", 0);
					onelogin = sp.getBoolean("Login", false);
					Log.e("LoginActivity", "进入到这里了11111111==========" );
					if (onelogin) {
						Log.e("LoginActivity", "进入到这里了222222==========" );
						mManually_login.setVisibility(View.GONE);
						mAuto_login.setVisibility(View.VISIBLE);
						drawable = (AnimationDrawable) mAuto_login_image
								.getDrawable();
						drawable.start();
						if (!("".equals(person.pk_user) && ""
								.equals(person.password))) {
							Log.e("LoginActivity", "进入到这里了333333==========" );
							auto_ReadUser(Integer.valueOf(person.pk_user));
							if (person.password.equals(mPassword)) {
								User autologinuser = new User();
								autologinuser.setPk_user(pk_user);
								autologinuser.setAvatar_path(mAvatar_path);
								autologinuser.setNickname(mNickname);
								autologinuser.setPassword(person.password);
								autologinuser.setPhone(mPhone);
								autologinuser.setJpush_id(mJpush_id);
								Login_ReadUserInter.userLogin(
										LoginActivity.this, autologinuser);
							}
						}
						userLogin();//登录监听
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
		} else {
			FileInputStream fis;
			try {
				fis = new FileInputStream(fileName);
				ObjectInputStream ois = new ObjectInputStream(fis);
				Person person = (Person) ois.readObject();
				mLogin_account.setText(person.pk_user);
				mLogin_password.setText(person.password);
				mManually_login.setVisibility(View.VISIBLE);
				mAuto_login.setVisibility(View.GONE);
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
		super.onStart();
	}

}
