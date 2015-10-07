package com.biju.function;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.List;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.BJ.javabean.CreateGroup;
import com.BJ.javabean.Group;
import com.BJ.javabean.Group_User;
import com.BJ.javabean.Groupback;
import com.BJ.javabean.Newteamback;
import com.BJ.utils.ByteOrBitmap;
import com.BJ.utils.LimitLong;
import com.BJ.javabean.PicSignBack;
import com.BJ.javabean.RequestCodeback;
import com.BJ.javabean.User;
import com.BJ.utils.ByteOrBitmap;
import com.BJ.utils.LimitLong;
import com.BJ.utils.MyBimp;
import com.BJ.utils.Path2Bitmap;
import com.BJ.utils.PicCutter;
import com.BJ.utils.SdPkUser;
import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.alibaba.sdk.android.oss.storage.OSSBucket;
import com.alibaba.sdk.android.oss.storage.OSSData;
import com.biju.Interface;
import com.biju.Interface.createGroupListenner;
import com.biju.Interface.readUserGroupMsgListenner;
import com.biju.Interface.userJoin2gourpListenner;
import com.biju.R;
import com.biju.APP.MyApplication;
import com.github.volley_examples.utils.GsonUtils;

@SuppressLint("SimpleDateFormat")
public class NewteamActivity extends Activity implements OnClickListener {

	private EditText mNewteam_name;
	private ImageButton mNewteam_head;
	public static String APP_VERSION = "1.0.0";
	public static String APPID = "201139";
	public static String USERID = "";
	public static String SIGN;
	// private UploadManager uploadManager;
	protected String mFilePath = null;
	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0; // 这里的IMAGE_CODE是自己任意定义的
	private TextView mNewTeam_tv_head;
	private Interface cregrouInter;
	private String format1;
	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "";
	// 完整路径completeURL=beginStr+result.filepath+endStr;
	private String completeURL = "";
	private boolean read_requestcode2;
	private Group readhomeuser;
	private ArrayList<Group> readuesrlist = new ArrayList<Group>();

	private String newteam_name;
	private String sDpath;
	private Group group;
	private Integer sD_pk_user;
	private Integer pk_group;
	private OSSData ossData;
	private OSSService ossService;
	private OSSBucket sampleBucket;
	private byte[] bitmap2Bytes;
	private String uUid;
	private boolean isreaduser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newteam);
		// 获取sd卡中的pk_user
		sD_pk_user = SdPkUser.getsD_pk_user();
		Log.e("NewteamActivity", "从SD卡中获取到的Pk_user" + sD_pk_user);

		initUI();
		// 获取ossService和sampleBucket
		ossService = MyApplication.getOssService();
		sampleBucket = MyApplication.getSampleBucket();

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

	private void readUser() {
		ReadTeam(sD_pk_user);
	}


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
						if (String.valueOf(pk_group).equals(String.valueOf(readhomeuser.getPk_group()))) {
							isreaduser = true;
						}
					}
				}

			}

			@Override
			public void defail(Object B) {

			}
		});

		// 读取用户小组信息使用邀请码添加后的监听
		cregrouInter.setPostListener(new userJoin2gourpListenner() {

			@Override
			public void success(String A) {
				RequestCodeback requestCodeback = GsonUtils.parseJson(A,
						RequestCodeback.class);
				Integer status = requestCodeback.getStatusMsg();
				if (status == 1) {
					Log.e("NewteamActivity", "读取用户小组信息使用邀请码添加后的===" + A);
					SdPkUser.setRefreshTeam(true);
					finish();
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
	}

//	private void initUpload() {
//		// 注册签名
//		UploadManager.authorize(APPID, USERID, SIGN);
//		uploadManager = new UploadManager(NewteamActivity.this, "persistenceId");
//
//	}

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
		mNewteam_name.setFocusable(true);
		mNewteam_name.setFocusableInTouchMode(true);
		mNewteam_name.requestFocus();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				InputMethodManager inputManager = (InputMethodManager) mNewteam_name
						.getContext().getSystemService(NewteamActivity.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(mNewteam_name, 0);
			}
		}, 998);
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
		
		//上传OSS
		OSSupload(ossData, bitmap2Bytes,uUid);

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
			Log.e("NewteamActivity", "mFilePath======"+mFilePath);
			//OSS上传~
//			Bitmap bmp = MyBimp.revitionImageSize(mFilePath);
			Bitmap convertToBitmap = Path2Bitmap.convertToBitmap(mFilePath);
			Bitmap limitLongScaleBitmap = LimitLong.limitLongScaleBitmap(convertToBitmap, 1080);// 最长边限制为1080
			Bitmap centerSquareScaleBitmap = PicCutter.centerSquareScaleBitmap(limitLongScaleBitmap, 600);// 截取中间正方形
			bitmap2Bytes = ByteOrBitmap.Bitmap2Bytes(centerSquareScaleBitmap);
			UUID randomUUID = UUID.randomUUID();
			uUid = randomUUID.toString();

			mNewTeam_tv_head.setVisibility(View.GONE);// 显示小组头像选择
			mNewteam_head.setVisibility(View.VISIBLE);
			mNewteam_head.setImageBitmap(centerSquareScaleBitmap);
		} catch (Exception e) {
			Log.e("Demo", "choose file error!", e);
		}
	}

	private void OSSupload(OSSData ossData, byte[] data, String UUid) {
		ossData = ossService.getOssData(sampleBucket, UUid);
		ossData.setData(data, "jpg"); // 指定需要上传的数据和它的类型
		ossData.enableUploadCheckMd5sum(); // 开启上传MD5校验
		ossData.uploadInBackground(new SaveCallback() {
			@Override
			public void onSuccess(String objectKey) {
				Log.e("", "图片上传成功");
				Log.e("Main", "objectKey==" + objectKey);
				// list.add("http://picstyle.beagree.com/"+objectKey);
				runOnUiThread(new Runnable() {
					public void run() {
						// myAdapter.notifyDataSetChanged();
					}
				});

				group.setAvatar_path(objectKey);
				// 创建CreatGroup
				Group_User group_User = new Group_User();
				group_User.setFk_user(sD_pk_user);
				group_User.setRole(1);
				group.setStatus(1);
				Group_User[] members = { group_User };
				CreateGroup creatGroup = new CreateGroup(members, group);
				Log.e("NewteamActivity", "group:" + group.toString());
				cregrouInter.createGroup(NewteamActivity.this, creatGroup);// 测试
			}

			@Override
			public void onProgress(String objectKey, int byteCount,
					int totalSize) {
				final long p = (long) ((byteCount * 100) / (totalSize * 1.0f));
		    }

			@Override
			public void onFailure(String objectKey, OSSException ossException) {
				Log.e("", "图片上传失败" + ossException.toString());
			}
		});
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
