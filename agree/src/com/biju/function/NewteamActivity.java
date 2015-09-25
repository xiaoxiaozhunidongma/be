package com.biju.function;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.BJ.javabean.CreateGroup;
import com.BJ.javabean.Group;
import com.BJ.javabean.Group_User;
import com.BJ.javabean.Groupback;
import com.BJ.javabean.Newteamback;
import com.BJ.javabean.RequestCodeback;
import com.BJ.javabean.User;
import com.BJ.utils.ByteOrBitmap;
import com.BJ.utils.LimitLong;
import com.BJ.utils.MyBimp;
import com.BJ.utils.Path2Bitmap;
import com.BJ.utils.PicCutter;
import com.BJ.utils.PreferenceUtils;
import com.BJ.utils.SdPkUser;
import com.BJ.utils.homeImageLoaderUtils;
import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.alibaba.sdk.android.oss.storage.OSSBucket;
import com.alibaba.sdk.android.oss.storage.OSSData;
import com.biju.IConstant;
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
	private ImageView mNewteam_head;
	public static String APP_VERSION = "1.0.0";
	public static String APPID = "201139";
	public static String USERID = "";
	public static String SIGN;
//	private UploadManager uploadManager;
	protected String mFilePath = null;
	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0; // 这里的IMAGE_CODE是自己任意定义的
	private TextView newteam_tv_head;
	private ProgressBar newteam_progressBar;
	private Interface cregrouInter;
	private String format1;
	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "";
	// 完整路径completeURL=beginStr+result.filepath+endStr;
	private String completeURL = "";
	private boolean read_requestcode2;
	private Group readhomeuser;
	private ArrayList<Group> readuesrlist = new ArrayList<Group>();

	private String tmpFilePath;
	private String newteam_name;
	private String sDpath;
	private Group group;
	private Integer pk_group;
	private OSSData ossData;
	private OSSService ossService;
	private OSSBucket sampleBucket;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newteam);
		// 获取sd卡中的pk_user
		sD_pk_user = SdPkUser.getsD_pk_user();
		Log.e("NewteamActivity", "从SD卡中获取到的Pk_user" + sD_pk_user);

		initUI();
		//获取ossService和sampleBucket
		ossService = MyApplication.getOssService();
		sampleBucket = MyApplication.getSampleBucket();

		newteam_tv_head.setVisibility(View.VISIBLE);// 显示小组头像选择
		mNewteam_head.setVisibility(View.GONE);
		Interface();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-m-d HH:MM:ss");
		format1 = sdf.format(new Date());
	}
	
	@Override
	protected void onResume() {
		SharedPreferences requestcode2_sp=getSharedPreferences(IConstant.IsRefresh, 0);
		read_requestcode2=requestcode2_sp.getBoolean(IConstant.IsCode2, false);
		if(read_requestcode2)
		{
			newteam_tv_head.setVisibility(View.GONE);// 显示小组头像选择
			mNewteam_head.setVisibility(View.VISIBLE);
			readhomeuser = SdPkUser.getgroup;
			mNewteam_name.setText(readhomeuser.getName());
			completeURL = beginStr + readhomeuser.getAvatar_path() + endStr;
			PreferenceUtils.saveImageCache(NewteamActivity.this,completeURL);
			homeImageLoaderUtils.getInstance().LoadImage(NewteamActivity.this, completeURL, mNewteam_head);
			readUser();
		}
		super.onResume();
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

	private boolean isreaduser;
	private Integer sD_pk_user;
	private byte[] bitmap2Bytes;
	private String uUid;

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
		if (read_requestcode2) {
			if (!isreaduser) {
				Integer fk_group = readhomeuser.getPk_group();
				Group_User group_User = new Group_User();
				group_User.setFk_group(fk_group);
				group_User.setFk_user(sD_pk_user);
				group_User.setRole(2);
				group_User.setStatus(1);
				cregrouInter.userJoin2gourp(NewteamActivity.this, group_User);
			} else {
				Toast.makeText(NewteamActivity.this, "已经加入过该小组",Toast.LENGTH_SHORT).show();
			}
		} else {
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
			
//			Interface interface1 = Interface.getInstance();
//			interface1.getPicSign(this, new User());
//			interface1.setPostListener(new getPicSignListenner() {
//
//				@Override
//				public void success(String A) {
//					PicSignBack picSignBack = GsonUtils.parseJson(A,PicSignBack.class);
//					String returnData = picSignBack.getReturnData();
//					SIGN = returnData;
//					initUpload();
//					upload(group);
//
//				}
//
//				@Override
//				public void defail(Object B) {
//
//				}
//			});
		}
	}

//	private void upload(final Group group) {
//		tmpFilePath = Environment.getExternalStorageDirectory()
//				.getAbsolutePath() + "/compress.tmp";
//		UploadTask task = new PhotoUploadTask(mFilePath,
//				new IUploadTaskListener() {
//
//					@SuppressLint("NewApi")
//					@Override
//					public void onUploadSucceed(final FileInfo result) {
//						Log.e("上传结果", "upload succeed: " + result.fileId);
//						// 上传完成后注册
//						Log.e("图片路径", "result.url" + result.url);
////						 上传完成后删除SD中图片
////						deleteMybitmap(sDpath);
//						group.setAvatar_path(result.fileId);
//						// 创建CreatGroup
//						Group_User group_User = new Group_User();
//						group_User.setFk_user(sD_pk_user);
//						group_User.setRole(1);
//						group.setStatus(1);
//						Group_User[] members = { group_User };
//						CreateGroup creatGroup = new CreateGroup(members, group);
//						Log.e("NewteamActivity", "group:" + group.toString());
//						cregrouInter.createGroup(NewteamActivity.this,creatGroup);// 测试
////						finish();
//					}
//
//					@Override
//					public void onUploadStateChange(TaskState state) {
//					}
//
//					@Override
//					public void onUploadProgress(long totalSize, long sendSize) {
//						final long p = (long) ((sendSize * 100) / (totalSize * 1.0f));
//						// Log.e("上传进度", "上传进度: " + p + "%");
//						newteam_progressBar.setProgress((int) p);
//					}
//
//					@Override
//					public void onUploadFailed(final int errorCode,
//							final String errorMsg) {
//						Log.e("Demo", "上传结果:失败! ret:" + errorCode + " msg:"+ errorMsg);
//					}
//				});
//		uploadManager.upload(task); // 开始上传
//
//	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK || data == null)
			return;
		try {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = NewteamActivity.this.getContentResolver().query(
					selectedImage, filePathColumn, null, null, null);
			if(cursor!=null){
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				mFilePath = cursor.getString(columnIndex);
				cursor.close();
				cursor = null;
				
			}else{
				File file = new File(selectedImage.getPath());
				mFilePath=file.getAbsolutePath();
				if (!file.exists()) {
					Toast toast = Toast.makeText(this, "找不到图片", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					return;
				}
			}
			Log.e("NewteamActivity", "mFilePath======"+mFilePath);
			//OSS上传~
//			Bitmap bmp = MyBimp.revitionImageSize(mFilePath);
			Bitmap convertToBitmap = Path2Bitmap.convertToBitmap(mFilePath);
			Bitmap limitLongScaleBitmap = LimitLong.limitLongScaleBitmap(convertToBitmap, 1080);//最长边限制为1080
			Bitmap centerSquareScaleBitmap = PicCutter.centerSquareScaleBitmap(limitLongScaleBitmap, 600);//截取中间正方形
			bitmap2Bytes = ByteOrBitmap.Bitmap2Bytes(centerSquareScaleBitmap);
			UUID randomUUID = UUID.randomUUID();
			uUid = randomUUID.toString();

			newteam_tv_head.setVisibility(View.GONE);// 显示小组头像选择
			mNewteam_head.setVisibility(View.VISIBLE);
			newteam_progressBar.setVisibility(View.VISIBLE);
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
		    	Log.e("Main", "objectKey=="+objectKey);
//		    	list.add("http://picstyle.beagree.com/"+objectKey);
		    	runOnUiThread( new Runnable() {
					public void run() {
//						myAdapter.notifyDataSetChanged();
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
				cregrouInter.createGroup(NewteamActivity.this,creatGroup);// 测试
		    }

		    @Override
		    public void onProgress(String objectKey, int byteCount, int totalSize) {
				final long p = (long) ((byteCount * 100) / (totalSize * 1.0f));
				// Log.e("上传进度", "上传进度: " + p + "%");
				newteam_progressBar.setProgress((int) p);
		    }

		    @Override
		    public void onFailure(String objectKey, OSSException ossException) {
		    	Log.e("", "图片上传失败"+ossException.toString());
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

	private void newteam_back() {
		finish();
		Finish();
	}

	private void Finish() {
		SharedPreferences requestcode2_sp=getSharedPreferences(IConstant.IsRefresh, 0);
		Editor editor=requestcode2_sp.edit();
		editor.putBoolean(IConstant.IsCode2, false);
		editor.commit();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Finish();
	}
}
