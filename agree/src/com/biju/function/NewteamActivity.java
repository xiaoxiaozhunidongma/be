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
	private final int IMAGE_CODE = 0; // �����IMAGE_CODE���Լ����ⶨ���
	private TextView newteam_tv_head;
	private ProgressBar newteam_progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newteam);
		initUI();
		initUpload();
		newteam_tv_head.setVisibility(View.VISIBLE);//��ʾС��ͷ��ѡ��
		mNewteam_head.setVisibility(View.GONE);
	}

	private void initUpload() {
		// ע��ǩ��
		UploadManager.authorize(APPID, USERID, SIGN);
		uploadManager = new UploadManager(NewteamActivity.this, "persistenceId");

	}

	private void initUI() {
		mNewteam_name = (EditText) findViewById(R.id.newteam_name);// С������
		mNewteam_head = (ImageView) findViewById(R.id.newteam_head);// ��ʾС��ͷ��
		newteam_tv_head = (TextView) findViewById(R.id.newteam_tv_head);
		newteam_tv_head.setOnClickListener(this);// ѡ��С��ͷ��
		findViewById(R.id.newteam_requsetcode).setOnClickListener(this);// С��������
		findViewById(R.id.newteam_back_layout).setOnClickListener(this);// ����
		findViewById(R.id.newteam_back).setOnClickListener(this);// ����
		findViewById(R.id.newteam_OK_layout).setOnClickListener(this);// ���
		findViewById(R.id.newteam_OK_layout).setOnClickListener(this);// ���
		newteam_progressBar = (ProgressBar) findViewById(R.id.newteam_progressBar);//ͼƬ�ϴ�����
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
		// ʹ��intent����ϵͳ�ṩ����Ṧ�ܣ�ʹ��startActivityForResult��Ϊ�˻�ȡ�û�ѡ���ͼƬ
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
						Log.e("�ϴ����", "upload succeed: " + result.fileId);
						 //�ϴ���ɺ�ע��
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
						 Log.e("�ϴ�����", "�ϴ�����: " + p + "%");
						 newteam_progressBar.setProgress((int)p);
					}

					@Override
					public void onUploadFailed(final int errorCode,
							final String errorMsg) {
						Log.e("Demo", "�ϴ����:ʧ��! ret:" + errorCode + " msg:"
								+ errorMsg);
					}
				});
		uploadManager.upload(task); // ��ʼ�ϴ�

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
                newteam_tv_head.setVisibility(View.GONE);//��ʾС��ͷ��ѡ��
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
