package com.biju.function;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.BJ.javabean.CreateGroup;
import com.BJ.javabean.Group;
import com.BJ.javabean.Group_User;
import com.BJ.javabean.Groupback;
import com.BJ.javabean.Newteamback;
import com.BJ.javabean.PicSignBack;
import com.BJ.javabean.User;
import com.BJ.photo.Bimp;
import com.BJ.utils.PreferenceUtils;
import com.BJ.utils.SdPkUser;
import com.BJ.utils.Utils;
import com.BJ.utils.homeImageLoaderUtils;
import com.biju.Interface;
import com.biju.Interface.createGroupListenner;
import com.biju.Interface.getPicSignListenner;
import com.biju.Interface.readUserGroupMsgListenner;
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
	private ImageView mNewteam_head;
	public static String APP_VERSION = "1.0.0";
	public static String APPID = "201139";
	public static String USERID = "";
	// public static String SIGN =
	// "3lXtRSAlZuWqzRczFPIjqrcHJCBhPTIwMTEzOSZrPUFLSUQ5eUFramtVTUhFQzFJTGREbFlvMndmaW1mOThUaUltRyZlPTE0MzY0OTk2NjcmdD0xNDMzOTA3NjY3JnI9MTk5MDE3ODExNSZ1PSZmPQ==";
	public static String SIGN;
	private UploadManager uploadManager;
	protected String mFilePath = null;
	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0; // 这里的IMAGE_CODE是自己任意定义的
	private TextView newteam_tv_head;
	private ProgressBar newteam_progressBar;
	private Interface cregrouInter;
	private String format1;
	private String beginStr = "http://201139.image.myqcloud.com/201139/0/";
	private String endStr = "/original";
	// 完整路径completeURL=beginStr+result.filepath+endStr;
	private String completeURL = "";
	private boolean iscode;
	private Group readhomeuser;
	private ArrayList<Group> readuesrlist = new ArrayList<Group>();
	
	private String tmpFilePath;
	private String newteam_name;
	private String sDpath;
	private int returndata_1;
	private boolean isRegistered_one;
	private int flag;
	private Group group;
	private MyReceiver receiver;
	private Integer pk_group;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newteam);
		//提供做布局
		isRegistered_one=SdPkUser.isRegistered_one();
		
		//获取sd卡中的pk_user
		sD_pk_user = SdPkUser.getsD_pk_user();
		Log.e("NewteamActivity", "从SD卡中获取到的Pk_user" + sD_pk_user);

		initUI();
		// initUpload();
		IntentFilter filter = new IntentFilter();
		filter.addAction("isRefresh2");
		receiver = new MyReceiver();
		registerReceiver(receiver, filter);

		newteam_tv_head.setVisibility(View.VISIBLE);// 显示小组头像选择
		mNewteam_head.setVisibility(View.GONE);
		Interface();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-m-d HH:MM:ss");
		format1 = sdf.format(new Date());
	}

	private void Interface() {
		cregrouInter = Interface.getInstance();
		cregrouInter.setPostListener(new createGroupListenner() {

			@Override
			public void success(String A) {

				switch (flag) {
				case 0:
					Newteamback newteamback = GsonUtils.parseJson(A,
							Newteamback.class);
					int newteamStatusMsg = newteamback.getStatusMsg();
					if (newteamStatusMsg == 1) {
						Log.e("NewteamActivity", "小组ID" + A);
						// 发广播进行更新gridview
						Intent intent = new Intent();
						intent.setAction("isRefresh");
						intent.putExtra("refresh", true);
						sendBroadcast(intent);
						Log.e("NewteamActivity", "有广播发出");
					}

					break;
				// case 1:
				// PicSignBack picSignBack = GsonUtils.parseJson(A,
				// PicSignBack.class);
				// String returnData = picSignBack.getReturnData();
				// SIGN=returnData;
				// initUpload();
				// upload(group);
				//
				// break;

				default:
					break;
				}

			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			iscode = intent.getBooleanExtra("isCode2", false);
			if (iscode) {
				newteam_tv_head.setVisibility(View.GONE);// 显示小组头像选择
				mNewteam_head.setVisibility(View.VISIBLE);
				readhomeuser = (Group) intent
						.getSerializableExtra("readhomeuser");
				mNewteam_name.setText(readhomeuser.getName());
				completeURL = beginStr + readhomeuser.getAvatar_path() + endStr;
				PreferenceUtils.saveImageCache(NewteamActivity.this,
						completeURL);
				homeImageLoaderUtils.getInstance().LoadImage(
						NewteamActivity.this, completeURL, mNewteam_head);
				readUser();
			}
		}
	}

	private void readUser() {
		ReadTeam(sD_pk_user);
	}

	private boolean isreaduser;
	private Integer sD_pk_user;

	private void ReadTeam(int pk_user) {
		cregrouInter = Interface.getInstance();
		User homeuser = new User();
		homeuser.setPk_user(pk_user);
		cregrouInter.readUserGroupMsg(NewteamActivity.this, homeuser);
		cregrouInter.setPostListener(new readUserGroupMsgListenner() {

			@Override
			public void success(String A) {
				Groupback homeback = GsonUtils.parseJson(A, Groupback.class);
				int homeStatusMsg = homeback.getStatusMsg();
				if (homeStatusMsg == 1) {
					Log.e("NewteamActivity", "读取出的用户小组信息==========" + A);
					List<Group> users = homeback.getReturnData();
					if (users.size() > 0) {
						for (int i = 0; i < users.size(); i++) {
							Group readhomeuser_1 = users.get(i);
							readuesrlist.add(readhomeuser_1);
						}
					}
					for (int i = 0; i < readuesrlist.size(); i++) {
						pk_group = readuesrlist.get(i).getPk_group();
						Log.e("NewteamActivity", "读取的pk_group======="
								+ pk_group);
						Log.e("NewteamActivity",
								"读取的readhomeuser.getPk_group()======="
										+ readhomeuser.getPk_group());

						// Log.e("NewteamActivity",
						// "读取的String.valueOf(pk_group)======="+String.valueOf(pk_group));
						// Log.e("NewteamActivity",
						// "读取的String.valueOf(readhomeuser.getPk_group())======="+String.valueOf(readhomeuser.getPk_group()));
						if (String.valueOf(pk_group).equals(
								String.valueOf(readhomeuser.getPk_group()))) {
							isreaduser = true;
							Log.e("NewteamActivity", "有进入说明两个数据相等了=========");
						}
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
		uploadManager = new UploadManager(NewteamActivity.this, "persistenceId");

	}

	private void initUI() {
		mNewteam_name = (EditText) findViewById(R.id.newteam_name);// 小组名称
		mNewteam_head = (ImageView) findViewById(R.id.newteam_head);// 显示小组头像
		newteam_tv_head = (TextView) findViewById(R.id.newteam_tv_head);
		newteam_tv_head.setOnClickListener(this);// 选择小组头像
		findViewById(R.id.newteam_requsetcode).setOnClickListener(this);// 小组邀请码
		findViewById(R.id.newteam_back_layout).setOnClickListener(this);// 返回
		findViewById(R.id.newteam_back).setOnClickListener(this);// 返回
		findViewById(R.id.newteam_OK_layout).setOnClickListener(this);// 完成
		findViewById(R.id.newteam_OK_layout).setOnClickListener(this);// 完成
		newteam_progressBar = (ProgressBar) findViewById(R.id.newteam_progressBar);// 图片上传进度
		newteam_progressBar.setMax(100);
		findViewById(R.id.newteam_requsetcode).setOnClickListener(this);// 跳转至邀请码搜索界面
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
		case R.id.newteam_back_layout:
		case R.id.newteam_back:
			newteam_back();
			break;
		case R.id.newteam_OK_layout:
		case R.id.newteam_OK:
			newteam_OK();
			break;
		case R.id.newteam_tv_head:
			newteam_tv_head();
			break;
		case R.id.newteam_requsetcode:
			newteam_requsetcode();
			break;
		default:
			break;
		}
	}

	private void newteam_requsetcode() {
		// 跳转至邀请码搜索界面
		Intent intent = new Intent(NewteamActivity.this,
				RequestCode2Activity.class);
		startActivity(intent);
	}

	private void newteam_tv_head() {
		// 使用intent调用系统提供的相册功能，使用startActivityForResult是为了获取用户选择的图片
		Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
		getAlbum.setType(IMAGE_TYPE);
		startActivityForResult(getAlbum, IMAGE_CODE);
	}

	private void newteam_OK() {
		if (iscode) {
			if (!isreaduser) {
				Log.e("NewteamActivity", "相等后进入这里说明错了=========");
				Intent intent = new Intent();
				intent.setAction("isRefresh");
				intent.putExtra("isCode", true);
				intent.putExtra("readhomeuser", readhomeuser);
				sendBroadcast(intent);
				finish();
			} else {
				Log.e("NewteamActivity", "相等后进入这里说明对了=========");
				Toast.makeText(NewteamActivity.this, "已经加入过该小组",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			newteam_name = mNewteam_name.getText().toString().trim();
			group = new Group();
			group.setName(newteam_name);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-m-d HH:MM:ss");
			String format2 = sdf.format(new Date());

			group.setSetup_time(format1);
			group.setLast_post_time(format2);
			group.setLast_post_message("asdfsd");

			Interface interface1 = Interface.getInstance();
			interface1.getPicSign(this, new User());
			interface1.setPostListener(new getPicSignListenner() {

				@Override
				public void success(String A) {
					PicSignBack picSignBack = GsonUtils.parseJson(A,
							PicSignBack.class);
					String returnData = picSignBack.getReturnData();
					SIGN = returnData;
					initUpload();
					upload(group);

				}

				@Override
				public void defail(Object B) {

				}
			});
			// flag = 1;
			// initUpload();
			// // 上传图片
			// upload(group);
		}
	}

	private void upload(final Group group) {
		tmpFilePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/compress.tmp";
		UploadTask task = new PhotoUploadTask(mFilePath,
				new IUploadTaskListener() {

					@SuppressLint("NewApi")
					@Override
					public void onUploadSucceed(final FileInfo result) {
						Log.e("上传结果", "upload succeed: " + result.fileId);
						// 上传完成后注册
						Log.e("图片路径", "result.url" + result.url);
//						// 上传完成后删除SD中图片
//						deleteMybitmap(sDpath);
						group.setAvatar_path(result.fileId);
						// 创建CreatGroup
						Group_User group_User = new Group_User();
						group_User.setFk_user(sD_pk_user);
						group_User.setRole(1);
						group.setStatus(1);
						Group_User[] members = { group_User };
						CreateGroup creatGroup = new CreateGroup(members, group);
						Log.e("NewteamActivity", "group:" + group.toString());
						cregrouInter.createGroup(NewteamActivity.this,
								creatGroup);// 测试
						flag = 0;
						finish();
					}

					@Override
					public void onUploadStateChange(TaskState state) {
					}

					@Override
					public void onUploadProgress(long totalSize, long sendSize) {
						final long p = (long) ((sendSize * 100) / (totalSize * 1.0f));
						// Log.e("上传进度", "上传进度: " + p + "%");
						newteam_progressBar.setProgress((int) p);
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
//			String[] thumbColumns = { MediaStore.Images.Thumbnails.DATA};  

			Cursor cursor = NewteamActivity.this.getContentResolver().query(
					selectedImage, filePathColumn, null, null, null);
//			Cursor cursor = NewteamActivity.this.getContentResolver().query(
//					Thumbnails.EXTERNAL_CONTENT_URI, thumbColumns, null, null, null);
			
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			mFilePath = cursor.getString(columnIndex);
			Log.e("NewteamActivity", "图路径====="+mFilePath);
			cursor.close();
//			Bitmap bmp = Utils.decodeSampledBitmap(mFilePath, 2);
			Bitmap bmp = Bimp.revitionImageSize(mFilePath);
			//缩略图
			Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bmp, 400, 800, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
			bmp.recycle();//回收
			System.gc();
//			saveMyBitmap(bmp, newteam_name);
//			mFilePath = sDpath;// 图片在SD卡中的路径
			
			newteam_tv_head.setVisibility(View.GONE);// 显示小组头像选择
			mNewteam_head.setVisibility(View.VISIBLE);
			newteam_progressBar.setVisibility(View.VISIBLE);
			mNewteam_head.setImageBitmap(thumbnail);
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

	private void newteam_back() {
		finish();
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
		if (iscode) {
			unregisterReceiver(receiver);
		}
		super.onStop();
	}
}
