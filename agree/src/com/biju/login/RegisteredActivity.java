package com.biju.login;

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
import android.widget.TextView;
import android.widget.Toast;

import com.BJ.javabean.Registeredback;
import com.BJ.javabean.User;
import com.BJ.utils.Ifwifi;
import com.BJ.utils.InitHead;
import com.BJ.utils.Utils;
import com.biju.Interface;
import com.biju.Interface.regNewAccountListenner;
import com.biju.MainActivity;
import com.biju.R;
import com.biju.APP.MyApplication;
import com.github.volley_examples.utils.GsonUtils;
import com.tencent.upload.UploadManager;
import com.tencent.upload.task.ITask.TaskState;
import com.tencent.upload.task.IUploadTaskListener;
import com.tencent.upload.task.UploadTask;
import com.tencent.upload.task.data.FileInfo;
import com.tencent.upload.task.impl.PhotoUploadTask;

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

	private UploadManager mUploadManager;
	private TextView mTextView;
	private Interface mRegistered_Inter;
	private boolean isHead;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registered);
		// get4Sign();
		initUI();
		initUpload();
	}

	private void get4Sign() {
		Interface interface2 = Interface.getInstance();
		User user = new User();
		interface2.getPicSign(RegisteredActivity.this, user);
	}

	private void initUpload() {
		// 注册签名
		SIGN = RegisteredActivity.getSIGN();
		UploadManager.authorize(APPID, USERID, SIGN);
		mUploadManager = new UploadManager(RegisteredActivity.this,
				"persistenceId");

	}

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

					int returndata = registered.getReturnData();
					Log.e("RegisteredActivity", "returndata" + returndata);
					SharedPreferences sp = getSharedPreferences("Registered", 0);
					Editor editor = sp.edit();
					editor.putInt("returndata", returndata);
					editor.putBoolean("isRegistered_one", true);
					editor.commit();
					// 跳转至主界面
					Intent intent = new Intent(RegisteredActivity.this,
							MainActivity.class);
					startActivity(intent);
					finish();
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registered, menu);
		return true;
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
			SharedPreferences sp = getSharedPreferences("isLogin", 0);
			Editor editor = sp.edit();
			editor.putBoolean("Login", false);
			editor.commit();

			// 把昵称传到接口
			String nickname = mNickname.getText().toString().trim();
			String jpush_id = MyApplication.getRegId();
			User user = new User();
			user.setNickname(nickname);
			user.setJpush_id(jpush_id);
			if (isHead) {
				// 上传图片
				upload(user);
			} else {
				mRegistered_Inter.regNewAccount(RegisteredActivity.this, user);
			}
		} else {
			Toast.makeText(RegisteredActivity.this, "网络异常，请检查网络!",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void upload(final User user) {
		UploadTask task = new PhotoUploadTask(mFilePath,
				new IUploadTaskListener() {
					@Override
					public void onUploadSucceed(final FileInfo result) {
						Log.e("上传结果", "upload succeed: " + result.fileId);
						mTextView.post(new Runnable() {

							@Override
							public void run() {
								mTextView.setVisibility(View.GONE);
							}
						});
						// 上传完成后注册
						user.setAvatar_path(result.fileId);
						mRegistered_Inter.regNewAccount(
								RegisteredActivity.this, user);
					}

					@Override
					public void onUploadStateChange(TaskState state) {
					}

					@Override
					public void onUploadProgress(long totalSize, long sendSize) {
						final long p = (long) ((sendSize * 100) / (totalSize * 1.0f));
						// Log.e("上传进度", "上传进度: " + p + "%");
						mTextView.post(new Runnable() {

							@Override
							public void run() {
								mTextView.setVisibility(View.VISIBLE);
								mTextView.setText(p + "%");
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
		mUploadManager.upload(task); // 开始上传

	}

	private void registered_back() {
		SharedPreferences sp = getSharedPreferences("isLogin", 0);
		Editor editor = sp.edit();
		editor.putBoolean("Login", false);
		editor.commit();
		finish();
		Intent intent = new Intent(RegisteredActivity.this, LoginActivity.class);
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
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			mFilePath = cursor.getString(columnIndex);
			cursor.close();
			Bitmap bmp = Utils.decodeSampledBitmap(mFilePath, 2);
			InitHead.initHead(bmp);// 画圆形头像
			isHead = !isHead;
		} catch (Exception e) {
			Log.e("Demo", "choose file error!", e);
		}
	}
}
