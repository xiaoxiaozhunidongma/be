package com.biju.login;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.BJ.javabean.CreateGroup;
import com.BJ.javabean.Group_User;
import com.BJ.javabean.PicSignBack;
import com.BJ.javabean.Registeredback;
import com.BJ.javabean.User;
import com.BJ.javabean.updateback;
import com.BJ.utils.ByteOrBitmap;
import com.BJ.utils.Ifwifi;
import com.BJ.utils.InitHead;
import com.BJ.utils.LimitLong;
import com.BJ.utils.MyBimp;
import com.BJ.utils.Path2Bitmap;
import com.BJ.utils.Person;
import com.BJ.utils.PicCutter;
import com.BJ.utils.RefreshActivity;
import com.BJ.utils.SdPkUser;
import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.alibaba.sdk.android.oss.storage.OSSBucket;
import com.alibaba.sdk.android.oss.storage.OSSData;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.getPicSignListenner;
import com.biju.Interface.regNewAccountListenner;
import com.biju.Interface.updateUserListenner;
import com.biju.function.NewteamActivity;
import com.biju.MainActivity;
import com.biju.R;
import com.biju.APP.MyApplication;
import com.github.volley_examples.utils.GsonUtils;

public class RegisteredActivity extends Activity implements OnClickListener {

	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0; // 这里的IMAGE_CODE是自己任意定义的
	public static ImageView mRegistered_head;
	private EditText mNickname;
	private TextView mRegistered_tv_nickname;
	protected String mFilePath = null;
	public static String APP_VERSION = "1.0.0";
	public static String APPID = "201139";
	public static String USERID = "";
	// public static String SIGN =
	// "3lXtRSAlZuWqzRczFPIjqrcHJCBhPTIwMTEzOSZrPUFLSUQ5eUFramtVTUhFQzFJTGREbFlvMndmaW1mOThUaUltRyZlPTE0MzY0OTk2NjcmdD0xNDMzOTA3NjY3JnI9MTk5MDE3ODExNSZ1PSZmPQ==";
	// public static String SIGN =
	// "x8gJ5EjEXIH8RyzKWX59Oia23oVhPTIwMTEzOSZrPUFLSUQ5eUFramtVTUhFQzFJTGREbFlvMndmaW1mOThUaUltRyZlPTE0Mzk0NDgzOTgmdD0xNDM2ODU2Mzk4JnI9ODk5OTcxNjM0JnU9JmY9";
	public static String SIGN;

	public static String getSIGN() {
		return SIGN;
	}

	public static void setSIGN(String sIGN) {
		SIGN = sIGN;
	}

	// private UploadManager mUploadManager;
	private TextView mTextView;
	private Interface mRegistered_Inter;
	private boolean isHead;
	private int returndata;
	private String phoneRegistered_phone;
	private boolean phoneLogin;

	private String fileName = getSDPath() + "/" + "saveData";
	private boolean weixinLogin;
	private OSSData ossData;
	private OSSService ossService;
	private OSSBucket sampleBucket;
	private byte[] bitmap2Bytes;
	private String uUid;

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
		setContentView(R.layout.activity_registered);
		RefreshActivity.activList_3.add(RegisteredActivity.this);
		Intent intent = getIntent();
		phoneRegistered_phone = intent.getStringExtra("phoneRegistered_phone");
		phoneLogin = intent.getBooleanExtra("PhoneLogin", false);
		weixinLogin = intent.getBooleanExtra("weixinLogin", false);
		// get4Sign();
		initUI();
		// 获取ossService和sampleBucket
		ossService = MyApplication.getOssService();
		sampleBucket = MyApplication.getSampleBucket();
		// initUpload();
	}

	// private void get4Sign() {
	// Interface getSigninter =Interface.getInstance();
	// getSigninter.setPostListener(new getPicSignListenner() {
	//
	// @Override
	// public void success(String A) {
	// Log.e("RegisteredActivity", "签名字符串："+A);
	// PicSignBack picSignBack = GsonUtils.parseJson(A, PicSignBack.class);
	// String returnData = picSignBack.getReturnData();
	// RegisteredActivity.setSIGN(returnData);
	// initUpload();
	// }
	//
	// @Override
	// public void defail(Object B) {
	//
	// }
	// });
	// getSigninter.getPicSign(this, new User());
	// }

	// private void initUpload() {
	// // 注册签名
	// SIGN = RegisteredActivity.getSIGN();
	// UploadManager.authorize(APPID, USERID, SIGN);
	// mUploadManager = new UploadManager(RegisteredActivity.this,
	// "persistenceId");
	//
	// }

	private void initUI() {
		mRegistered_head = (ImageView) findViewById(R.id.registered_head);
		mRegistered_head.setOnClickListener(this);
		mNickname = (EditText) findViewById(R.id.registered_nickname);
		findViewById(R.id.registered_back).setOnClickListener(this);
		findViewById(R.id.registered_OK).setOnClickListener(this);
		mRegistered_tv_nickname = (TextView) findViewById(R.id.registered_tv_nickname);
		mRegistered_tv_nickname.setOnClickListener(this);
		mRegistered_Inter = Interface.getInstance();
		mRegistered_Inter.setPostListener(new regNewAccountListenner() {

			@Override
			public void success(String A) {
				Log.e("RegisteredActivity", "注册成功" + A);
				Registeredback registered = GsonUtils.parseJson(A,
						Registeredback.class);
				int statusMsg = registered.getStatusMsg();
				if (statusMsg == 1) {

					returndata = registered.getReturnData();
					// 把当前注册成功的pk_user传给工具类
					SdPkUser.setsD_pk_user(returndata);
					SdPkUser.setRegistered_one(true);
					Log.e("RegisteredActivity", "returndata" + returndata);
					Person person = new Person(returndata);
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

					// 注册成功后进行登录并绑定手机号码
					updateLogin();

				} else {
					Toast.makeText(RegisteredActivity.this, "请重新注册!",
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
		mTextView = (TextView) findViewById(R.id.textView1);

		// 更新用户资料成功
		mRegistered_Inter.setPostListener(new updateUserListenner() {

			@Override
			public void success(String A) {

				updateback usersetting_updateback = GsonUtils.parseJson(A,
						updateback.class);
				int a = usersetting_updateback.getStatusMsg();
				if (a == 1) {
					Log.e("RegisteredActivity", "更新成功" + A);

					// 把当前注册成功的pk_user传给工具类
					SdPkUser.setsD_pk_user(returndata);
					SdPkUser.setRegistered_one(true);
					Person person = new Person(returndata);
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

					Intent intent = new Intent(RegisteredActivity.this,
							MainActivity.class);
					intent.putExtra(IConstant.Sdcard, true);
					startActivity(intent);
				}
			}

			@Override
			public void defail(Object B) {

			}
		});

	}

	// 用户进行登录
	private void updateLogin() {
		User usersetting = new User();
		usersetting.setPk_user(returndata);
		usersetting.setJpush_id(MyApplication.getRegId());
		if (phoneLogin) {
			usersetting.setPhone(phoneRegistered_phone);
		}
		if (weixinLogin) {
			String wechat_id = SdPkUser.getOpenid();
			usersetting.setWechat_id(wechat_id);
		}
		mRegistered_Inter.updateUser(RegisteredActivity.this, usersetting);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.registered_head:
			registered_head();
			break;
		case R.id.registered_back:
			registered_back();
			break;
		case R.id.registered_OK:
			registered_OK();
			break;
		case R.id.registered_tv_nickname:
			registered_tv_nickname();
			break;
		default:
			break;
		}
	}

	private void registered_tv_nickname() {
		mRegistered_tv_nickname.setVisibility(View.GONE);
		mNickname.setVisibility(View.VISIBLE);

	}

	private void registered_OK() {
		boolean isWIFI = Ifwifi.getNetworkConnected(RegisteredActivity.this);
		if (isWIFI) {
			// 把昵称传到接口
			String nickname = mNickname.getText().toString().trim();
			String jpush_id = MyApplication.getRegId();
			User user = new User();
			user.setNickname(nickname);
			user.setJpush_id(jpush_id);
			user.setStatus(1);
			if (isHead) {
				// 上传图片
//				upload(user);
				//OSS上传图片
				OSSupload(ossData, bitmap2Bytes, uUid, user);
			} else {
				mRegistered_Inter.regNewAccount(RegisteredActivity.this, user);
			}
		} else {
			Toast.makeText(RegisteredActivity.this, "网络异常，请检查网络!",
					Toast.LENGTH_SHORT).show();
		}
	}

	// private void upload(final User user) {
	// UploadTask task = new PhotoUploadTask(mFilePath,
	// new IUploadTaskListener() {
	// @Override
	// public void onUploadSucceed(final FileInfo result) {
	// Log.e("上传结果", "upload succeed: " + result.fileId);
	// mTextView.post(new Runnable() {
	//
	// @Override
	// public void run() {
	// mTextView.setVisibility(View.GONE);
	// }
	// });
	// // 上传完成后注册
	// user.setAvatar_path(result.fileId);
	// mRegistered_Inter.regNewAccount(RegisteredActivity.this, user);
	// }
	//
	// @Override
	// public void onUploadStateChange(TaskState state) {
	// }
	//
	// @Override
	// public void onUploadProgress(long totalSize, long sendSize) {
	// final long p = (long) ((sendSize * 100) / (totalSize * 1.0f));
	// // Log.e("上传进度", "上传进度: " + p + "%");
	// mTextView.post(new Runnable() {
	//
	// @Override
	// public void run() {
	// mTextView.setVisibility(View.VISIBLE);
	// mTextView.setText(p + "%");
	// }
	// });
	// }
	//
	// @Override
	// public void onUploadFailed(final int errorCode,
	// final String errorMsg) {
	// Log.e("Demo", "上传结果:失败! ret:" + errorCode + " msg:"
	// + errorMsg);
	// }
	// });
	// mUploadManager.upload(task); // 开始上传
	//
	// }

	private void registered_back() {
		Intent intent = new Intent(RegisteredActivity.this,
				BeforeLoginActivity.class);
		startActivity(intent);
	}

	// 打开图库，选择图片
	private void registered_head() {
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
			Cursor cursor = RegisteredActivity.this.getContentResolver().query(
					selectedImage, filePathColumn, null, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				mFilePath = cursor.getString(columnIndex);
				cursor.close();
				cursor = null;

			} else {
				File file = new File(selectedImage.getPath());
				mFilePath = file.getAbsolutePath();
				if (!file.exists()) {
					Toast toast = Toast.makeText(this, "找不到图片",
							Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					return;
				}
			}
			Log.e("NewteamActivity", "mFilePath======" + mFilePath);
			// OSS上传~
			// 这个mFilePath不可以用缩略图路径
			// Bitmap bmp = MyBimp.revitionImageSize(mFilePath);
			Bitmap convertToBitmap = Path2Bitmap.convertToBitmap(mFilePath);
			Bitmap limitLongScaleBitmap = LimitLong.limitLongScaleBitmap(
					convertToBitmap, 1080);// 最长边限制为1080
			Bitmap centerSquareScaleBitmap = PicCutter.centerSquareScaleBitmap(
					limitLongScaleBitmap, 180);// 截取中间正方形
			bitmap2Bytes = ByteOrBitmap.Bitmap2Bytes(centerSquareScaleBitmap);
			UUID randomUUID = UUID.randomUUID();
			uUid = randomUUID.toString();

			InitHead.initHead(centerSquareScaleBitmap);// 画圆形头像
			isHead = !isHead;
		} catch (Exception e) {
			Log.e("Demo", "choose file error!", e);
		}
	}

	private void OSSupload(OSSData ossData, byte[] data, String UUid,
			final User user) {
		ossData = ossService.getOssData(sampleBucket, UUid);
		ossData.setData(data, "jpg"); // 指定需要上传的数据和它的类型
		ossData.enableUploadCheckMd5sum(); // 开启上传MD5校验
		ossData.uploadInBackground(new SaveCallback() {
			@Override
			public void onSuccess(String objectKey) {
				Log.e("", "图片上传成功");
				Log.e("Main", "objectKey==" + objectKey);
				mTextView.post(new Runnable() {
					//
					@Override
					public void run() {
						mTextView.setVisibility(View.GONE);
					}
				});
				// 上传完成后注册
				user.setAvatar_path(objectKey);
				mRegistered_Inter.regNewAccount(RegisteredActivity.this, user);

			}

			@Override
			public void onProgress(String objectKey, int byteCount,
					int totalSize) {
			}

			@Override
			public void onFailure(String objectKey, OSSException ossException) {
				Log.e("", "图片上传失败" + ossException.toString());
			}
		});
	}
}
