package com.biju.function;

import java.text.SimpleDateFormat;
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
import com.BJ.javabean.PicSignBack;
import com.BJ.javabean.User;
import com.BJ.javabean.updateback;
import com.BJ.utils.Ifwifi;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.InitHead;
import com.BJ.utils.PreferenceUtils;
import com.BJ.utils.SdPkUser;
import com.BJ.utils.Utils;
import com.biju.Interface;
import com.biju.APP.MyApplication;
import com.biju.Interface.getPicSignListenner;
import com.biju.Interface.readUserListenner;
import com.biju.Interface.updateUserListenner;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.upload.UploadManager;
import com.tencent.upload.task.ITask.TaskState;
import com.tencent.upload.task.IUploadTaskListener;
import com.tencent.upload.task.UploadTask;
import com.tencent.upload.task.data.FileInfo;
import com.tencent.upload.task.impl.PhotoUploadTask;

@SuppressLint("SimpleDateFormat")
public class UserSettingActivity extends Activity implements OnClickListener {

	public static UserSettingActivity UserSetting;
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
	public static String SIGN;

	public static String getSIGN() {
		return SIGN;
	}

	public static void setSIGN(String sIGN) {
		SIGN = sIGN;
	}

	private UploadManager uploadManager;
	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0; // �����IMAGE_CODE���Լ����ⶨ���
	protected String mFilePath = null;
	private TextView mUsersetting_progress;
	private String password = "";
	private RelativeLayout mUsersetting_save_1_layout;
	private TextView mUsersetting_sex;
	private Integer Userpk_user;
	private String Usernickname;
	private String Useravatar_path;
	private String Userphone;
	private String Userpassword;
	private int Usersex;
	private String Usersetup_time;
	private String Userlast_login_time;
	private String Userwechat_id;
	
	private boolean ishead;
	private Interface readuserinter;
	private boolean isRead;
	private String completeURL;
	private String beginStr = "http://201139.image.myqcloud.com/201139/0/";
	private String endStr = "/original";
	// ����·��completeURL=beginStr+result.filepath+endStr;
	private String TestcompleteURL = beginStr
			+ "1ddff6cf-35ac-446b-8312-10f4083ee13d" + endStr;
	private String setup_time;
	private String Userjpush_id;
	private User readuser;
	
	int i = 0;
	private boolean isRegistered_one;
	private int sex = 0;
	private TextView mUsersetting_tv_phone;
	private Integer sD_pk_user;
	private boolean weixin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_setting);
		UserSetting=this;
		//�������ж�
		isRegistered_one=SdPkUser.isRegistered_one();
		
		//��ȡsd���е�pk_user
		sD_pk_user = SdPkUser.getsD_pk_user();
		Log.e("UserSettingActivity", "��SD���л�ȡ����Pk_user" + sD_pk_user);
		
		String Cacheurl = PreferenceUtils.readImageCache(this);
		completeURL = Cacheurl;
		initUI();
		isWIFI();// �ж��Ƿ�������

		mUsersetting_tv_password.setText("��������Ҫ���õ�����");
		PicSign();// ��ͼƬǩ���ַ���
		
		Intent intent = getIntent();
		weixin = intent.getBooleanExtra("weixin", false);
	}
	
	public void getRefresh()
	{
		ReadUser(sD_pk_user);
	}

	private void PicSign() {
		readuserinter.setPostListener(new getPicSignListenner() {

			@Override
			public void success(String A) {
				Log.e("UserSettingActivity", "�µķ���ǩ���ַ�����" + A);
				PicSignBack picSignBack = GsonUtils.parseJson(A,
						PicSignBack.class);
				String returnData = picSignBack.getReturnData();
				UserSettingActivity.setSIGN(returnData);

				UploadManager.authorize(APPID, USERID, SIGN);
				uploadManager = new UploadManager(UserSettingActivity.this,
						"persistenceId");

			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	private void isWIFI() {
		boolean isWIFI = Ifwifi.getNetworkConnected(UserSettingActivity.this);
		Log.e("UserSettingActivity", "�ж��Ƿ�������" + isWIFI);
		if (isWIFI) {
			ReadUser(sD_pk_user);
			Log.e("UserSettingActivity", "�������������" + isWIFI);
		} else {
			ImageLoaderUtils.getInstance().LoadImage(UserSettingActivity.this,
					completeURL, mUsersetting_head);
		}
	}

	@Override
	protected void onStop() {
		if (isRegistered_one) {
			SharedPreferences sp = getSharedPreferences("Registered", 0);
			Editor editor = sp.edit();
			editor.putInt("returndata", sD_pk_user);
			editor.putBoolean("isRegistered_one", true);
			editor.commit();
		}

		super.onStop();
	}

	private void ReadUser(int returndata) {
		readuserinter = Interface.getInstance();
		User ReadUser = new User();
		ReadUser.setPk_user(returndata);
		readuserinter.readUser(UserSettingActivity.this, ReadUser);
		readuserinter.setPostListener(new readUserListenner() {

			@Override
			public void success(String A) {
				// ��ȡ�û����ϳɹ�
				Loginback usersettingback = GsonUtils.parseJson(A,
						Loginback.class);
				int userStatusmsg = usersettingback.getStatusMsg();
				if (userStatusmsg == 1) {
					Log.e("UserSettingActivity", "�û�����" + A);
					List<User> Users = usersettingback.getReturnData();
					if (Users.size() >= 1) {
						readuser = Users.get(0);
						Userpk_user = readuser.getPk_user();
						Usernickname = readuser.getNickname();
						Useravatar_path = readuser.getAvatar_path();
						Userphone = readuser.getPhone();
						Userjpush_id = readuser.getJpush_id();
						Usersetup_time = readuser.getSetup_time();
						Userlast_login_time = readuser.getLast_login_time();
						Userwechat_id = readuser.getWechat_id();
						Userpassword = readuser.getPassword();
						
						
						mUsersetting_nickname.setText(Usernickname);
						mUsersetting_edt_password_1.setText(Userpassword);
						mUsersetting_edt_password_2.setText(Userpassword);
						if("".equals(Userphone))
						{
							mUsersetting_tv_phone.setText("�󶨵绰����");
						}else
						{
							mUsersetting_tv_phone.setText(Userphone);
						}
						password = Userpassword;
						Usersex = readuser.getSex();
						Log.e("UserSettingActivity", "�Ա�" + Usersex);
						switch (Usersex) {
						case 0:
							mUsersetting_sex.setText("�Ա�");
							sex = Usersex;
							break;
						case 1:
							mUsersetting_sex.setText("��");
							sex = Usersex;
							break;
						case 2:
							mUsersetting_sex.setText("Ů");
							sex = Usersex;
							break;
						default:
							break;
						}
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd hh:mm:ss");
						setup_time = sdf.format(new Date());

						Log.e("UserSettingActivity", "�ǳ���ʾ" + "====="+ Usernickname);
						//��΢�Ÿ���
						if(weixin)
						{
//							String wechat_id=SdPkUser.getGetopenid();
//							Log.e("UserSettingActivity", "�õ���΢��Ψһʶ����===="+wechat_id);
//							String openid=SdPkUser.getGetopenid();
							User usersetting = new User();
							usersetting.setPk_user(sD_pk_user);
							usersetting.setJpush_id(Userjpush_id);
							usersetting.setNickname(Usernickname);
							usersetting.setPassword(Userpassword);
							usersetting.setSex(Usersex);
							usersetting.setStatus(1);
							usersetting.setPhone(Userphone);
							usersetting.setSetup_time(Usersetup_time);
							usersetting.setLast_login_time(Userlast_login_time);
							usersetting.setAvatar_path(Useravatar_path);
//							usersetting.setWechat_id(wechat_id);//΢�ŵ�Ψһʶ����
//							Log.e("UserSettingActivity", "�õ����û���Ϣ===="+sD_pk_user+"\n"+Userjpush_id+"\n"+
//									Usernickname+"\n"+Userpassword+"\n"+Usersex+"\n"+Userphone+"\n"+Usersetup_time+"\n"
//									+Userlast_login_time+"\n"+Useravatar_path+"\n"+wechat_id);
							readuserinter.updateUser(UserSettingActivity.this, usersetting);
						}
						
						
						
						
						
						
					}
					completeURL = beginStr + Useravatar_path + endStr;
					PreferenceUtils.saveImageCache(UserSettingActivity.this,
							completeURL);
					ImageLoaderUtils.getInstance().LoadImage(
							UserSettingActivity.this, completeURL,
							mUsersetting_head);
				}
			}

			@Override
			public void defail(Object B) {

			}
		});

		readuserinter.setPostListener(new updateUserListenner() {

			@Override
			public void success(String A) {
				// �����û����ϳɹ�
				updateback usersetting_updateback = GsonUtils.parseJson(A,
						updateback.class);
				int a = usersetting_updateback.getStatusMsg();
				if (a == 1) {
					Log.e("UserSettingActivity", "���³ɹ�" + A);
					finish();
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	private void initUpload() {
		readuserinter.getPicSign(this, new User());
	}

	private void initUI() {
		mUsersetting_head = (ImageView) findViewById(R.id.usersetting_head);// �޸��û�ͷ��
		mUsersetting_head.setOnClickListener(this);
		mUsersetting_nickname = (EditText) findViewById(R.id.usersetting_nickname);// �޸��û��ǳ�
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
		mUsersetting_tv_phone = (TextView) findViewById(R.id.usersetting_tv_phone);
		mUsersetting_tv_phone.setOnClickListener(this);
		mUsersetting_sex = (TextView) findViewById(R.id.usersetting_sex);// �����Ա�
		mUsersetting_sex.setOnClickListener(this);
		findViewById(R.id.usersetting_back_layout).setOnClickListener(this);// ����
		findViewById(R.id.usersetting_back).setOnClickListener(this);
		findViewById(R.id.usersetting_binding_weixin).setOnClickListener(this);//��΢��
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
		case R.id.usersetting_sex:
			usersetting_sex();
			break;
		case R.id.usersetting_back_layout:
		case R.id.usersetting_back:
			usersetting_back();
			break;
		case R.id.usersetting_binding_weixin:
			usersetting_binding_weixin();
			break;
		default:
			break;
		}
	}

	private void usersetting_binding_weixin() {
		finish();
		//��ת΢�Ű󶨽���
		final SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = "carjob_wx_login";
		MyApplication.api.sendReq(req);
	}

	private void usersetting_tv_phone() {
		if (!("".equals(Userphone))) {
			NiftyDialogBuilder();
		} else {
			Intent intent = new Intent(UserSettingActivity.this,
					BindingPhoneActivity.class);
			intent.putExtra("UserData", readuser);
			startActivity(intent);
		}
	}

	private void NiftyDialogBuilder() {
		final NiftyDialogBuilder niftyDialogBuilder = NiftyDialogBuilder.getInstance(this);
		Effectstype effectstype = Effectstype.Shake;
		niftyDialogBuilder.withTitle("��ʾ").withTitleColor("#000000")
				// ���ñ���������ɫ
				.withDividerColor("#ffffff")
				// ���öԻ��򱳾���ɫ
				.withMessage("���Ѿ������ֻ����룬�Ƿ����°���һ�����룿")
				// �Ի�����ʾ����
				.withMessageColor("#000000")
				// ��ʾ����������ɫ
				.withIcon(getResources().getDrawable(R.drawable.about_us))
				// ���öԻ�����ʾͼƬ
				.isCancelableOnTouchOutside(true).withDuration(700)
				// ����ʱ��
				.withEffect(effectstype).withButton1Text("ȡ��")
				.withButton2Text("ȷ��").setButton1Click(new OnClickListener() {

					@Override
					public void onClick(View v) {
						niftyDialogBuilder.cancel();
					}
				}).setButton2Click(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(UserSettingActivity.this,
								BindingPhoneActivity.class);
						intent.putExtra("UserData", readuser);
						startActivity(intent);
						niftyDialogBuilder.cancel();
					}
				}).show();

	}

	private void usersetting_back() {
		finish();
		overridePendingTransition(R.anim.in_item, R.anim.out_item);
	}

	private void usersetting_sex() {
		i++;
		int a = i % 3;
		switch (a) {
		case 0:
			mUsersetting_sex.setText("�Ա�");
			sex = 0;
			break;
		case 1:
			mUsersetting_sex.setText("��");
			sex = 1;
			break;
		case 2:
			mUsersetting_sex.setText("Ů");
			sex = 2;
			break;
		default:
			break;
		}
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
			Cursor cursor = UserSettingActivity.this.getContentResolver()
					.query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			mFilePath = cursor.getString(columnIndex);
			cursor.close();
			Bitmap bmp = Utils.decodeSampledBitmap(mFilePath, 2);
			InitHead.initHead(bmp);// ��Բ��ͷ��
			ishead = !ishead;
		} catch (Exception e) {
			Log.e("", "catch:" + e.getMessage());
		}
		// ��ʼ��ͼƬǩ��
		initUpload();
	}

	private void usersetting_save_1() {

		if (isSetting) {
			isRead = true;
			Usernickname = mUsersetting_nickname.getText().toString().trim();
			User usersetting = new User();
			usersetting.setPk_user(sD_pk_user);
			usersetting.setJpush_id(Userjpush_id);
			usersetting.setNickname(Usernickname);
			usersetting.setPassword(password);
			usersetting.setSex(sex);
			usersetting.setStatus(1);
			usersetting.setPhone(Userphone);
			usersetting.setWechat_id(Userwechat_id);
			Log.e("UserSettingActivity", "�绰����4===========  " + Userphone);
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			usersetting.setSetup_time(setup_time);
			usersetting.setLast_login_time(sdf1.format(new Date()));
			if (ishead) {
				upload(usersetting);// �ϴ�
			} else {
				usersetting.setAvatar_path(Useravatar_path);
				readuserinter.updateUser(UserSettingActivity.this, usersetting);
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
						readuserinter
								.updateUser(UserSettingActivity.this, user);
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
						Log.e("Demo", "�ϴ����:ʧ��! ret:" + errorCode + " msg:"
								+ errorMsg);
					}
				});
		uploadManager.upload(task); // ��ʼ�ϴ�

	}
}
