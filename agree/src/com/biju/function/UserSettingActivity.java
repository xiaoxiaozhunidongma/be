package com.biju.function;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.BJ.javabean.updateback;
import com.BJ.utils.Ifwifi;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.InitHead;
import com.BJ.utils.PreferenceUtils;
import com.BJ.utils.Utils;
import com.biju.Interface;
import com.biju.Interface.UserInterface;
import com.biju.R;
import com.biju.login.LoginActivity;
import com.github.volley_examples.utils.GsonUtils;
import com.tencent.upload.UploadManager;
import com.tencent.upload.task.ITask.TaskState;
import com.tencent.upload.task.IUploadTaskListener;
import com.tencent.upload.task.UploadTask;
import com.tencent.upload.task.data.FileInfo;
import com.tencent.upload.task.impl.PhotoUploadTask;

@SuppressLint("SimpleDateFormat")
public class UserSettingActivity extends Activity implements OnClickListener {

	public static ImageView mUsersetting_head;
	private EditText mUsersetting_nickname;
	private RelativeLayout mUsersetting;
	private RelativeLayout mUsersetting_password_layout;
	private TextView mUsersetting_tv_password;
	private EditText mUsersetting_edt_password_1;
	private EditText mUsersetting_edt_password_2;
	private boolean savepassword = true;
	private String password_1 = "";
	private boolean isSetting = true;
	public static String APP_VERSION = "1.0.0";
	public static String APPID = "201139";
	public static String USERID = "";
	public static String SIGN = "3lXtRSAlZuWqzRczFPIjqrcHJCBhPTIwMTEzOSZrPUFLSUQ5eUFramtVTUhFQzFJTGREbFlvMndmaW1mOThUaUltRyZlPTE0MzY0OTk2NjcmdD0xNDMzOTA3NjY3JnI9MTk5MDE3ODExNSZ1PSZmPQ==";
	private UploadManager uploadManager;
	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0; // 这里的IMAGE_CODE是自己任意定义的
	protected String mFilePath = null;
	private TextView mUsersetting_progress;
	private String password = "";
	private RelativeLayout mUsersetting_phone_layout;
	private EditText mUsersetting_phone;
	private RelativeLayout mUsersetting_save_1_layout;
	private RelativeLayout mUsersetting_save_2_layout;
	private TextView mUsersetting_sex;
	private Integer Userpk_user;
	private String Usernickname;
	private String Useravatar_path;
	private String Userphone;
	private String Userpassword;
	private boolean ishead;
	private Interface readuserinter;
	private boolean isRead;
	private String completeURL;
	private String beginStr = "http://201139.image.myqcloud.com/201139/0/";
	private String endStr = "/original";
	// 完整路径completeURL=beginStr+result.filepath+endStr;
	private String TestcompleteURL = beginStr
			+ "1ddff6cf-35ac-446b-8312-10f4083ee13d" + endStr;
	private String setup_time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_setting);
		SharedPreferences sp = getSharedPreferences("Registered", 0);
		returndata_1 = sp.getInt("returndata", returndata_1);
		isRegistered_one = sp.getBoolean("isRegistered_one", false);

		SharedPreferences sp1 = getSharedPreferences("isLogin", 0);
		login = sp1.getBoolean("Login", false);
		String Cacheurl = PreferenceUtils.readImageCache(this);
		completeURL = Cacheurl;
		initUI();
		boolean isWIFI = Ifwifi.getNetworkConnected(UserSettingActivity.this);
		Log.e("UserSettingActivity", "判断是否有网络" + isWIFI);
		if (isWIFI) {
			if (isRegistered_one) {
				ReadUser(returndata_1);
				Log.e("UserSettingActivity", "进注册的");
			} else {
				if (login) {
					int returndata_2 = LoginActivity.pk_user;
					ReadUser(returndata_2);
					Log.e("UserSettingActivity", "进登录 的");
				} else {
					ReadUser(returndata_1);
				}
			}
			Log.e("UserSettingActivity", "有网络进入这里" + isWIFI);
		} else {
			ImageLoaderUtils.getInstance().LoadImage(UserSettingActivity.this,
					completeURL, mUsersetting_head);
		}

		mUsersetting_tv_password.setText("请输入想要设置的密码");
		initUpload();
	}

	@Override
	protected void onStop() {
		if (isRegistered_one) {
			SharedPreferences sp = getSharedPreferences("Registered", 0);
			Editor editor = sp.edit();
			editor.putInt("returndata", returndata_1);
			editor.putBoolean("isRegistered_one", true);
			editor.commit();
		}

		super.onStop();
	}

	private void ReadUser(int returndata) {
		readuserinter = new Interface();
		User readuser = new User();
		readuser.setPk_user(returndata);
		readuserinter.readUser(UserSettingActivity.this, readuser);
		readuserinter.setPostListener(new UserInterface() {

			@Override
			public void success(String A) {
				if (!isRead) {

					// 读取用户资料成功
					Loginback usersettingback = GsonUtils.parseJson(A,
							Loginback.class);
					int userStatusmsg = usersettingback.getStatusMsg();
					if (userStatusmsg == 1) {
						Log.e("UserSettingActivity", "用户资料" + A);
						List<User> Users = usersettingback.getReturnData();
						if (Users.size() >= 1) {
							User readuser = Users.get(0);
							Userpk_user = readuser.getPk_user();
							Usernickname = readuser.getNickname();
							Useravatar_path = readuser.getAvatar_path();
							Userphone = readuser.getPhone();
							Log.e("UserSettingActivity", "电话号码1 ==  "
									+ Userphone);
							Userpassword = readuser.getPassword();
							Log.e("UserSettingActivity", "读取出来的" + Userpk_user
									+ Usernickname + Useravatar_path
									+ Userphone + Userpassword);
							mUsersetting_nickname.setText(Usernickname);
							mUsersetting_edt_password_1.setText(Userpassword);
							mUsersetting_edt_password_2.setText(Userpassword);
							mUsersetting_phone.setText(Userphone);
							password = Userpassword;
							int Usersex = readuser.getSex();
							Log.e("UserSettingActivity", "性别" + Usersex);
							switch (Usersex) {
							case 0:
								mUsersetting_sex.setText("性别");
								sex = Usersex;
								break;
							case 1:
								mUsersetting_sex.setText("男");
								sex = Usersex;
								break;
							case 2:
								mUsersetting_sex.setText("女");
								sex = Usersex;
								break;
							default:
								break;
							}
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd hh:mm:ss");
							setup_time = sdf.format(new Date());

							Log.e("UserSettingActivity", "昵称显示" + "====="
									+ Usernickname);
						}
						completeURL = beginStr + Useravatar_path + endStr;
						PreferenceUtils.saveImageCache(
								UserSettingActivity.this, completeURL);
						ImageLoaderUtils.getInstance().LoadImage(
								UserSettingActivity.this, completeURL,
								mUsersetting_head);
					}
				} else {
					// 更新用户资料成功
					updateback usersetting_updateback = GsonUtils.parseJson(A,
							updateback.class);
					int a = usersetting_updateback.getStatusMsg();
					if (a == 1) {
						Log.e("UserSettingActivity", "更新成功" + A);
						finish();
					}
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	private void initUpload() {
		// 注册签名
		UploadManager.authorize(APPID, USERID, SIGN);
		uploadManager = new UploadManager(UserSettingActivity.this,
				"persistenceId");
	}

	private void initUI() {
		mUsersetting_head = (ImageView) findViewById(R.id.usersetting_head);// 修改用户头像
		mUsersetting_head.setOnClickListener(this);
		mUsersetting_nickname = (EditText) findViewById(R.id.usersetting_nickname);// 修改用户昵称
		findViewById(R.id.usersetting_password).setOnClickListener(this);// 跳转至设置密码界面
		mUsersetting = (RelativeLayout) findViewById(R.id.usersetting);// 设置界面
		mUsersetting_password_layout = (RelativeLayout) findViewById(R.id.usersetting_password_layout);// 密码设置界面
		mUsersetting_tv_password = (TextView) findViewById(R.id.usersetting_tv_password);// 输入密码提示
		mUsersetting_edt_password_1 = (EditText) findViewById(R.id.usersetting_edt_password_1);// 第一次输入密码
		mUsersetting_edt_password_2 = (EditText) findViewById(R.id.usersetting_edt_password_2);// 重新输入密码
		mUsersetting_save_1_layout = (RelativeLayout) findViewById(R.id.usersetting_save_1_layout);
		mUsersetting_save_1_layout.setOnClickListener(this);// 保存1
		findViewById(R.id.usersetting_save_1).setOnClickListener(this);
		mUsersetting_progress = (TextView) findViewById(R.id.usersetting_progress);// 显示上传进度
		mUsersetting_save_2_layout = (RelativeLayout) findViewById(R.id.usersetting_save_2_layout);
		mUsersetting_save_2_layout.setOnClickListener(this);// 保存2
		findViewById(R.id.usersetting_save_2).setOnClickListener(this);
		mUsersetting_phone_layout = (RelativeLayout) findViewById(R.id.usersetting_phone_layout);// 电话号码设置界面
		mUsersetting_phone = (EditText) findViewById(R.id.usersetting_phone);// 输入电话号码
		findViewById(R.id.usersetting_tv_phone).setOnClickListener(this);
		mUsersetting_sex = (TextView) findViewById(R.id.usersetting_sex);// 设置性别
		mUsersetting_sex.setOnClickListener(this);
		findViewById(R.id.usersetting_back_layout).setOnClickListener(this);// 返回
		findViewById(R.id.usersetting_back).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_setting, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.usersetting_password:
			usersetting_password();
			break;
		case R.id.usersetting_save_1_layout:
		case R.id.usersetting_save_1:
			usersetting_save_1();
			break;
		case R.id.usersetting_head:
			usersetting_head();
			break;
		case R.id.usersetting_tv_phone:
			usersetting_tv_phone();
			break;
		case R.id.usersetting_save_2_layout:
		case R.id.usersetting_save_2:
			usersetting_save_2();
			break;
		case R.id.usersetting_sex:
			usersetting_sex();
			break;
		case R.id.usersetting_back_layout:
		case R.id.usersetting_back:
			usersetting_back();
			break;
		default:
			break;
		}
	}

	private void usersetting_back() {
		finish();
	}

	int i = 0;
	private int returndata_1;
	private boolean isRegistered_one;
	private int sex = 0;
	private String phone;
	private boolean login;

	private void usersetting_sex() {
		i++;
		int a = i % 3;
		switch (a) {
		case 0:
			mUsersetting_sex.setText("性别");
			sex = 0;
			break;
		case 1:
			mUsersetting_sex.setText("男");
			sex = 1;
			break;
		case 2:
			mUsersetting_sex.setText("女");
			sex = 2;
			break;
		default:
			break;
		}
	}

	private void usersetting_save_2() {
		String userphone = mUsersetting_phone.getText().toString().trim();
		if (userphone.equals(Userphone)) {
			phone = userphone;
			Log.e("UserSettingActivity", "电话号码2 ==  " + phone);
		} else {
			phone = userphone;
			Log.e("UserSettingActivity", "电话号码3 ==  " + phone);
		}
		mUsersetting.setVisibility(View.VISIBLE);
		mUsersetting_save_1_layout.setVisibility(View.VISIBLE);
		mUsersetting_phone_layout.setVisibility(View.GONE);
		mUsersetting_save_2_layout.setVisibility(View.GONE);
	}

	private void usersetting_tv_phone() {
		mUsersetting.setVisibility(View.GONE);
		mUsersetting_save_1_layout.setVisibility(View.GONE);
		mUsersetting_phone_layout.setVisibility(View.VISIBLE);
		mUsersetting_save_2_layout.setVisibility(View.VISIBLE);
	}

	private void usersetting_head() {
		// 使用intent调用系统提供的相册功能，使用startActivityForResult是为了获取用户选择的图片
		Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
		getAlbum.setType(IMAGE_TYPE);
		startActivityForResult(getAlbum, IMAGE_CODE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK || data == null)
			return;
		try {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = UserSettingActivity.this.getContentResolver()
					.query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			mFilePath = cursor.getString(columnIndex);
			cursor.close();
			Bitmap bmp = Utils.decodeSampledBitmap(mFilePath, 2);
			InitHead.initHead(bmp);// 画圆形头像
			ishead = !ishead;
		} catch (Exception e) {
		}
	}

	private void usersetting_save_1() {
		if (isSetting) {
			isRead = true;
			Usernickname = mUsersetting_nickname.getText().toString().trim();
			User usersetting = new User();
			if (isRegistered_one) {
				usersetting.setPk_user(returndata_1);
			} else {
				if(login)
				{
					int returndata_2 = LoginActivity.pk_user;
					usersetting.setPk_user(returndata_2);
				}else
				{
					usersetting.setPk_user(returndata_1);
				}
			}
			usersetting.setNickname(Usernickname);
			usersetting.setPassword(password);
			usersetting.setSex(sex);
			Log.e("UserSettingActivity", "性别" + "  " + sex);
			usersetting.setPhone(phone);
			Log.e("UserSettingActivity", "电话号码4==  " + phone);
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			usersetting.setSetup_time(setup_time);
			usersetting.setLast_login_time(sdf1.format(new Date()));
			if (ishead) {
				upload(usersetting);
			} else {
				usersetting.setAvatar_path(Useravatar_path);
				readuserinter.updateUser(UserSettingActivity.this, usersetting);
			}

		} else {
			if (savepassword) {
				mUsersetting_tv_password.setText("请再输入一次密码");
				mUsersetting_edt_password_1.setVisibility(View.GONE);
				mUsersetting_edt_password_2.setVisibility(View.VISIBLE);
				password_1 = mUsersetting_edt_password_1.getText().toString()
						.trim();
				savepassword = !savepassword;
			} else {
				String password_2 = mUsersetting_edt_password_2.getText()
						.toString().trim();
				if (password_1.equals(password_2)) {
					mUsersetting.setVisibility(View.VISIBLE);
					mUsersetting_password_layout.setVisibility(View.GONE);
					Log.e("UserSettingActivity", "password_1" + password_1);
					Log.e("UserSettingActivity", "password_2" + password_2);
					password = password_2;
					isSetting = !isSetting;
				} else {
					Toast.makeText(UserSettingActivity.this, "密码不一致，请重新输入",
							Toast.LENGTH_SHORT).show();
					mUsersetting_tv_password.setText("请输入想要设置的密码");
					mUsersetting_edt_password_1.setVisibility(View.VISIBLE);
					mUsersetting_edt_password_2.setVisibility(View.GONE);
					mUsersetting_edt_password_1.setText("");
					mUsersetting_edt_password_2.setText("");
					savepassword = !savepassword;
				}
			}
		}
	}

	private void usersetting_password() {
		isSetting = !isSetting;
		mUsersetting.setVisibility(View.GONE);
		mUsersetting_password_layout.setVisibility(View.VISIBLE);
	}

	private void upload(final User user) {
		UploadTask task = new PhotoUploadTask(mFilePath,
				new IUploadTaskListener() {
					@Override
					public void onUploadSucceed(final FileInfo result) {
						Log.e("上传结果", "upload succeed: " + result.fileId);
						// 上传完成后注册
						user.setAvatar_path(result.fileId);
						readuserinter
								.updateUser(UserSettingActivity.this, user);
					}

					@Override
					public void onUploadStateChange(TaskState state) {
					}

					@Override
					public void onUploadProgress(long totalSize, long sendSize) {
						final long p = (long) ((sendSize * 100) / (totalSize * 1.0f));
						// Log.e("上传进度", "上传进度: " + p + "%");
						mUsersetting_progress.post(new Runnable() {

							@Override
							public void run() {
								mUsersetting_progress
										.setVisibility(View.VISIBLE);
								mUsersetting_progress
										.setVisibility(View.VISIBLE);
								mUsersetting_progress.setText(p + "%");
							}
						});
					}

					@Override
					public void onUploadFailed(final int errorCode,
							final String errorMsg) {
						Log.e("Demo", "上传结果:失败! ret:" + errorCode + " msg:"
								+ errorMsg);
					}
				});
		uploadManager.upload(task); // 开始上传

	}
}
