package com.biju.function;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.BJ.javabean.CreateGroup;
import com.BJ.javabean.Group;
import com.BJ.javabean.Group_User;
import com.BJ.javabean.Newteamback;
import com.BJ.javabean.PicSignBack;
import com.BJ.javabean.User;
import com.BJ.utils.MyBimp;
import com.BJ.utils.SdPkUser;
import com.biju.Interface;
import com.biju.Interface.createGroupListenner;
import com.biju.Interface.getPicSignListenner;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;
import com.tencent.upload.UploadManager;
import com.tencent.upload.task.ITask.TaskState;
import com.tencent.upload.task.IUploadTaskListener;
import com.tencent.upload.task.UploadTask;
import com.tencent.upload.task.data.FileInfo;
import com.tencent.upload.task.impl.PhotoUploadTask;

@SuppressLint("SimpleDateFormat")
public class NewteamActivity extends Activity implements OnClickListener {

	private EditText mNewteam_name;
	private ImageButton mNewteam_head;
	public static String APP_VERSION = "1.0.0";
	public static String APPID = "201139";
	public static String USERID = "";
	public static String SIGN;
	private UploadManager uploadManager;
	protected String mFilePath = null;
	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0; // 这里的IMAGE_CODE是自己任意定义的
	private TextView mNewTeam_tv_head;
	private Interface cregrouInter;
	private String format1;

	private String tmpFilePath;
	private String newteam_name;
	private String sDpath;
	private Group group;
	private Integer sD_pk_user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newteam);
		// 获取sd卡中的pk_user
		sD_pk_user = SdPkUser.getsD_pk_user();
		Log.e("NewteamActivity", "从SD卡中获取到的Pk_user" + sD_pk_user);

		initUI();

		Interface();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-m-d HH:MM:ss");
		format1 = sdf.format(new Date());
	}

	private void Interface() {
		cregrouInter = Interface.getInstance();
		cregrouInter.setPostListener(new createGroupListenner() {

			@Override
			public void success(String A) {
				Newteamback newteamback = GsonUtils.parseJson(A,Newteamback.class);
				int newteamStatusMsg = newteamback.getStatusMsg();
				if (newteamStatusMsg == 1) {
					Log.e("NewteamActivity", "小组ID" + A);
					SdPkUser.setRefreshTeam(true);
					finish();
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
		uploadManager = new UploadManager(NewteamActivity.this, "persistenceId");

	}

	private void initUI() {
		mNewteam_name = (EditText) findViewById(R.id.NewTeam_teamname);// 小组名称
		mNewteam_head = (ImageButton) findViewById(R.id.NewTeam_head);// 显示小组头像
		mNewteam_head.setOnClickListener(this);
		mNewTeam_tv_head = (TextView) findViewById(R.id.NewTeam_tv_head);
		mNewTeam_tv_head.setOnClickListener(this);// 选择小组头像
		findViewById(R.id.NewTeam_back_layout).setOnClickListener(this);// 返回
		findViewById(R.id.NewTeam_back).setOnClickListener(this);// 返回
		findViewById(R.id.NewTeam_OK_layout).setOnClickListener(this);// 完成
		findViewById(R.id.NewTeam_OK_layout).setOnClickListener(this);// 完成
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.newteam, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.NewTeam_back_layout:
		case R.id.NewTeam_back:
			NewTeam_back();
			break;
		case R.id.NewTeam_OK_layout:
		case R.id.NewTeam_OK:
			NewTeam_OK();
			break;
		case R.id.NewTeam_head:
		case R.id.NewTeam_tv_head:
			NewTeam_head();
		default:
			break;
		}
	}

	private void NewTeam_head() {
		// 使用intent调用系统提供的相册功能，使用startActivityForResult是为了获取用户选择的图片
		Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
		getAlbum.setType(IMAGE_TYPE);
		startActivityForResult(getAlbum, IMAGE_CODE);
	}

	private void NewTeam_OK() {
		newteam_name = mNewteam_name.getText().toString().trim();
		group = new Group();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-m-d HH:MM:ss");
		String format2 = sdf.format(new Date());

		group.setSetup_time(format1);
		group.setLast_post_message("asdfsd");
		group.setStatus(1);
		group.setName(newteam_name);
		group.setLast_post_time(format2);

		Interface interface1 = Interface.getInstance();
		interface1.getPicSign(this, new User());
		interface1.setPostListener(new getPicSignListenner() {

			@Override
			public void success(String A) {
				PicSignBack picSignBack = GsonUtils.parseJson(A,PicSignBack.class);
				String returnData = picSignBack.getReturnData();
				SIGN = returnData;
				initUpload();
				upload(group);

			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	private void upload(final Group group) {
		tmpFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/compress.tmp";
		UploadTask task = new PhotoUploadTask(mFilePath,
				new IUploadTaskListener() {

					@SuppressLint("NewApi")
					@Override
					public void onUploadSucceed(final FileInfo result) {
						Log.e("上传结果", "upload succeed: " + result.fileId);
						// 上传完成后注册
						Log.e("图片路径", "result.url" + result.url);
						// 上传完成后删除SD中图片
						// deleteMybitmap(sDpath);
						group.setAvatar_path(result.fileId);
						// 创建CreatGroup
						Group_User group_User = new Group_User();
						group_User.setFk_user(sD_pk_user);
						group_User.setRole(1);
						group.setStatus(1);
						Group_User[] members = { group_User };
						CreateGroup creatGroup = new CreateGroup(members, group);
						Log.e("NewteamActivity", "group:" + group.toString());
						cregrouInter.createGroup(NewteamActivity.this,creatGroup);// 测试
						// finish();
					}

					@Override
					public void onUploadStateChange(TaskState state) {
					}

					@Override
					public void onUploadProgress(long totalSize, long sendSize) {
						final long p = (long) ((sendSize * 100) / (totalSize * 1.0f));
						// Log.e("上传进度", "上传进度: " + p + "%");
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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK || data == null)
			return;
		try {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = NewteamActivity.this.getContentResolver().query(
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
					Toast toast = Toast.makeText(this, "找不到图片",Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					return;
				}
			}
			Log.e("NewteamActivity", "mFilePath======" + mFilePath);
			// Bitmap bmp = Utils.decodeSampledBitmap(mFilePath, 2);
			// Bitmap bmp = Bimp.revitionImageSize(mFilePath);
			// 这个mFilePath不可以用缩略图路径
			Bitmap bmp = MyBimp.revitionImageSize(mFilePath);

			// saveMyBitmap(bmp, newteam_name);
			// mFilePath = sDpath;// 图片在SD卡中的路径

			mNewteam_head.setImageBitmap(bmp);
			mNewTeam_tv_head.setVisibility(View.GONE);// 隐藏小组头像字体
		} catch (Exception e) {
			Log.e("Demo", "choose file error!", e);
		}
	}

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

	public void saveMyBitmap(Bitmap mBitmap, String bitName) {
		sDpath = getSDPath() + "/" + bitName + ".png";
		Log.e("NewteamAc", "sDpath~~~" + sDpath);
		File f = new File(sDpath);
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteMybitmap(String path) {
		File f = new File(path);
		f.delete();
	}

	private void NewTeam_back() {
		finish();
	}

}
