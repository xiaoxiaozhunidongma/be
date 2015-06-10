package com.biju.function;

import com.BJ.javabean.User;
import com.BJ.utils.Utils;
import com.biju.Interface;
import com.biju.MainActivity;
import com.biju.R;
import com.biju.login.RegisteredActivity;
import com.tencent.upload.UploadManager;
import com.tencent.upload.task.IUploadTaskListener;
import com.tencent.upload.task.UploadTask;
import com.tencent.upload.task.ITask.TaskState;
import com.tencent.upload.task.data.FileInfo;
import com.tencent.upload.task.impl.PhotoUploadTask;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView.FindListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class NewteamActivity extends Activity implements OnClickListener {

	private EditText mNewteam_name;
	private ImageView mNewteam_head;
	public static String APP_VERSION = "1.0.0";
	public static String APPID = "201139";
	public static String USERID = "";
	public static String SIGN = "3lXtRSAlZuWqzRczFPIjqrcHJCBhPTIwMTEzOSZrPUFLSUQ5eUFramtVTUhFQzFJTGREbFlvMndmaW1mOThUaUltRyZlPTE0MzY0OTk2NjcmdD0xNDMzOTA3NjY3JnI9MTk5MDE3ODExNSZ1PSZmPQ==";
	private UploadManager uploadManager;
	protected String mFilePath = null;
	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0; // 这里的IMAGE_CODE是自己任意定义的
	private TextView newteam_tv_head;
	private ProgressBar newteam_progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newteam);
		initUI();
		initUpload();
		newteam_tv_head.setVisibility(View.VISIBLE);//显示小组头像选择
		mNewteam_head.setVisibility(View.GONE);
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
		newteam_progressBar = (ProgressBar) findViewById(R.id.newteam_progressBar);//图片上传进度
		newteam_progressBar.setMax(100);
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
		default:
			break;
		}
	}

	private void newteam_tv_head() {
		// 使用intent调用系统提供的相册功能，使用startActivityForResult是为了获取用户选择的图片
		Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
		getAlbum.setType(IMAGE_TYPE);
		startActivityForResult(getAlbum, IMAGE_CODE);
	}

	private void newteam_OK() {
		String newteam_name = mNewteam_name.getText().toString().trim();
		User user = new User();
		
		upload(user);
	}

	private void upload(final User user) {
		UploadTask task = new PhotoUploadTask(mFilePath,
				new IUploadTaskListener() {
					@Override
					public void onUploadSucceed(final FileInfo result) {
						Log.e("上传结果", "upload succeed: " + result.fileId);
						 //上传完成后注册
						 user.setAvatar_path(result.fileId);
						 Interface regInter=new Interface();
						 regInter.createGroup(NewteamActivity.this, user);
						 finish();
					}

					@Override
					public void onUploadStateChange(TaskState state) {
					}

					@Override
					public void onUploadProgress(long totalSize, long sendSize) {
						final long p = (long) ((sendSize * 100) / (totalSize * 1.0f));
						 Log.e("上传进度", "上传进度: " + p + "%");
						 newteam_progressBar.setProgress((int)p);
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
            try
            {
            	Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = NewteamActivity.this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mFilePath = cursor.getString(columnIndex);
                cursor.close();
                Bitmap bmp = Utils.decodeSampledBitmap(mFilePath, 2);
                newteam_tv_head.setVisibility(View.GONE);//显示小组头像选择
				mNewteam_head.setVisibility(View.VISIBLE);
				newteam_progressBar.setVisibility(View.VISIBLE);
                mNewteam_head.setImageBitmap(bmp);
            }
            catch (Exception e)
            {
                Log.e("Demo", "choose file error!", e);
            }
	}
	
	private void newteam_back() {
		finish();
	}
}
