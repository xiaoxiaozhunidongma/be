package com.biju.function;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.BJ.javabean.User;
import com.BJ.javabean.updateback;
import com.BJ.utils.InitHead;
import com.BJ.utils.Utils;
import com.biju.Interface;
import com.biju.Interface.UserInterface;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;
import com.tencent.upload.UploadManager;
import com.tencent.upload.task.ITask.TaskState;
import com.tencent.upload.task.IUploadTaskListener;
import com.tencent.upload.task.UploadTask;
import com.tencent.upload.task.data.FileInfo;
import com.tencent.upload.task.impl.PhotoUploadTask;

public class UserSettingActivity extends Activity implements OnClickListener {

	public static ImageView mUsersetting_head;
	private TextView mUsersetting_nickname;
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
	private final int IMAGE_CODE = 0; // �����IMAGE_CODE���Լ����ⶨ���
	protected String mFilePath = null;
	private TextView mUsersetting_progress;
	private Interface usersetting_interface;
	private int returndata;
	private String password="";
	private RelativeLayout mUsersetting_phone_layout;
	private EditText mUsersetting_phone;
	private RelativeLayout mUsersetting_save_1_layout;
	private RelativeLayout mUsersetting_save_2_layout;
	private String userphone;
	private TextView mUsersetting_sex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_setting);
		SharedPreferences sp=getSharedPreferences("Registered", 0);
		returndata = sp.getInt("returndata", 0);
		initUI();
		mUsersetting_tv_password.setText("��������Ҫ���õ�����");
		initUpload();
		initInerface();
		ReadUser();
	}

	private void ReadUser() {
		Interface readuserinter=new Interface();
		User readuser=new User();
		readuser.setPk_user(returndata);
		readuserinter.readUser(UserSettingActivity.this, readuser);
		readuserinter.setPostListener(new UserInterface() {
			
			@Override
			public void success(String A) {
				Log.e("UserSettingActivity", "�û�����"+A);
			}
			
			@Override
			public void defail(Object B) {
				
			}
		});
	}

	private void initInerface() {
		usersetting_interface = new Interface();
		usersetting_interface.setPostListener(new UserInterface() {
			
			@Override
			public void success(String A) {
				Log.e("UserSettingActivity", "���³ɹ�"+A);
				updateback usersetting_updateback = GsonUtils.parseJson(A, updateback.class);
				int a=usersetting_updateback.getStatusMsg();
				if(a==1)
				{
					Log.e("UserSettingActivity", "���³ɹ�"+A);
					finish();
				}
			}
			
			@Override
			public void defail(Object B) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void initUpload() {
		// ע��ǩ��
		UploadManager.authorize(APPID, USERID, SIGN);
		uploadManager = new UploadManager(UserSettingActivity.this,
				"persistenceId");
	}

	private void initUI() {
		mUsersetting_head = (ImageView) findViewById(R.id.usersetting_head);// �޸��û�ͷ��
		mUsersetting_head.setOnClickListener(this);
		mUsersetting_nickname = (TextView) findViewById(R.id.usersetting_nickname);// �޸��û��ǳ�
		findViewById(R.id.usersetting_password).setOnClickListener(this);// ��ת�������������
		mUsersetting = (RelativeLayout) findViewById(R.id.usersetting);// ���ý���
		mUsersetting_password_layout = (RelativeLayout) findViewById(R.id.usersetting_password_layout);// �������ý���
		mUsersetting_tv_password = (TextView) findViewById(R.id.usersetting_tv_password);// ����������ʾ
		mUsersetting_edt_password_1 = (EditText) findViewById(R.id.usersetting_edt_password_1);// ��һ����������
		mUsersetting_edt_password_2 = (EditText) findViewById(R.id.usersetting_edt_password_2);// ������������
		mUsersetting_save_1_layout = (RelativeLayout) findViewById(R.id.usersetting_save_1_layout);
		mUsersetting_save_1_layout.setOnClickListener(this);// ����1
		findViewById(R.id.usersetting_save_1).setOnClickListener(this);
		mUsersetting_progress = (TextView) findViewById(R.id.usersetting_progress);// ��ʾ�ϴ�����
		mUsersetting_save_2_layout = (RelativeLayout) findViewById(R.id.usersetting_save_2_layout);
		mUsersetting_save_2_layout.setOnClickListener(this);//����2
		findViewById(R.id.usersetting_save_2).setOnClickListener(this);
		mUsersetting_phone_layout = (RelativeLayout) findViewById(R.id.usersetting_phone_layout);//�绰�������ý���
		mUsersetting_phone = (EditText) findViewById(R.id.usersetting_phone);//����绰����
		findViewById(R.id.usersetting_tv_phone).setOnClickListener(this);
		mUsersetting_sex = (TextView) findViewById(R.id.usersetting_sex);//�����Ա�
		mUsersetting_sex.setOnClickListener(this);
		findViewById(R.id.usersetting_back_layout).setOnClickListener(this);//����
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
	int i=0;
	private void usersetting_sex() {
		i++;
		int a=i%3;
		switch (a) {
		case 0:
			mUsersetting_sex.setText("�Ա�");
			break;
		case 1:
			mUsersetting_sex.setText("��");
			break;
		case 2:
			mUsersetting_sex.setText("Ů");
			break;
		default:
			break;
		}
	}

	private void usersetting_save_2() {
		userphone = mUsersetting_phone.getText().toString().trim();
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
		// ʹ��intent����ϵͳ�ṩ����Ṧ�ܣ�ʹ��startActivityForResult��Ϊ�˻�ȡ�û�ѡ���ͼƬ
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
			Cursor cursor = UserSettingActivity.this.getContentResolver().query(
					selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			mFilePath = cursor.getString(columnIndex);
			cursor.close();
			Bitmap bmp = Utils.decodeSampledBitmap(mFilePath, 2);
			InitHead.initHead(bmp);// ��Բ��ͷ��
		} catch (Exception e) {
			Log.e("Demo", "choose file error!", e);
		}
	}

	private void usersetting_save_1() {
		if (isSetting) {
			String nickname=mUsersetting_nickname.getText().toString().trim();
			String usersex=mUsersetting_sex.getText().toString().trim();
			if("�Ա�".equals(usersex))
			{
				Toast.makeText(UserSettingActivity.this, "��ѡ���Ա�", Toast.LENGTH_SHORT).show();
			}else
			{
				User user=new User();
				user.setPk_user(returndata);
				user.setPassword(password);
				user.setNickname(nickname);
				user.setPhone(userphone);
				user.setSex(1);
				upload(user);
				
			}
		} else {
			if (savepassword) {
				mUsersetting_tv_password.setText("��������һ������");
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
					Toast.makeText(UserSettingActivity.this, "���벻һ�£�����������",
							Toast.LENGTH_SHORT).show();
					mUsersetting_tv_password.setText("��������Ҫ���õ�����");
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
						Log.e("�ϴ����", "upload succeed: " + result.fileId);
						// �ϴ���ɺ�ע��
						user.setAvatar_path(result.fileId);
						usersetting_interface.updateUser(UserSettingActivity.this,user);
					}

					@Override
					public void onUploadStateChange(TaskState state) {
					}

					@Override
					public void onUploadProgress(long totalSize, long sendSize) {
						final long p = (long) ((sendSize * 100) / (totalSize * 1.0f));
						// Log.e("�ϴ�����", "�ϴ�����: " + p + "%");
						mUsersetting_progress.post(new Runnable() {

							@Override
							public void run() {
								mUsersetting_progress.setVisibility(View.VISIBLE);
								mUsersetting_progress.setText(p + "%");
							}
						});
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
	
}
