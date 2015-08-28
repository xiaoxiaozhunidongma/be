package com.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.BJ.javabean.Loginback;
import com.BJ.javabean.PicSignBack;
import com.BJ.javabean.User;
import com.BJ.javabean.updateback;
import com.BJ.utils.AsynImageLoader;
import com.BJ.utils.Ifwifi;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.PreferenceUtils;
import com.BJ.utils.SdPkUser;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.getPicSignListenner;
import com.biju.Interface.readUserListenner;
import com.biju.Interface.updateUserListenner;
import com.biju.R;
import com.biju.APP.MyApplication;
import com.biju.function.AboutUsActivity;
import com.biju.function.BindingPhoneActivity;
import com.biju.function.FeedbackActivity;
import com.biju.function.NicknameActivity;
import com.biju.function.SexActivity;
import com.biju.login.BeforeLoginActivity;
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

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class SettingFragment extends Fragment implements OnClickListener {

	private View mLayout;
	public ImageView mSetting_head;
	public static ImageView mSetting_head_1;

	private String beginStr = "http://201139.image.myqcloud.com/201139/0/";
	private String endStr = "/original";
	private String mUserAvatar_path;
	private String completeURL;
	private String TestcompleteURL = beginStr+ "1ddff6cf-35ac-446b-8312-10f4083ee13d" + endStr;

	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0; // �����IMAGE_CODE���Լ����ⶨ���
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

	private Integer SD_pk_user;
	private TextView mSetting_User_ID;
	private RelativeLayout mSetting_Nickname_change;
	private RelativeLayout mSetting_Sex_change;
	private RelativeLayout mSetting_Binding_phone;
	private RelativeLayout mSetting_Binding_weixin;
	private RelativeLayout mSetting_Binding_weixin_wallet;
	private RelativeLayout mSetting_Binding_alipay;
	private RelativeLayout mSetting_feedback;
	private RelativeLayout mSetting_about_us;
	private RelativeLayout mSetting_Exit;

	private String mUserPhone;
	private String mUserNickname;
	private String mUserPassword;
	private String mUserSetup_time;
	private String mUserLast_login_time;
	private String mUserJpush_id;
	private Integer mUserSex;
	private String mUserDevice_id;
	private String mUserWechat_id;

	private User Setting_readuser;
	private Interface Setting_readuserinter;
	private TextView mSetting_progress;

	private boolean isShow;
	private TextView mSetting_Nickname;
	private TextView mSetting_Sex;
	private TextView mSetting_Phone;

	// ����·��completeURL=beginStr+result.filepath+endStr;

	public SettingFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayout = inflater.inflate(R.layout.fragment_setting, container, false);
		User4head();
		Log.e("SettingFragment", "������onCreateView()====================");
		return mLayout;
	}

	private void User4head() {
		String Cacheurl = PreferenceUtils.readImageCache(getActivity());
		completeURL = Cacheurl;
		// ��ȡSD���е�pk_user
		SD_pk_user = SdPkUser.getsD_pk_user();
		Log.e("SettingFragment", "��SD���л�ȡ����Pk_user" + SD_pk_user);

		initUI();
		boolean isWIFI = Ifwifi.getNetworkConnected(getActivity());
		if (isWIFI) {
			returndata();
		} else {
//			ImageLoaderUtils.getInstance().LoadImage(getActivity(),completeURL, mSetting_head);
			AsynImageLoader asynImageLoader = new AsynImageLoader();
			asynImageLoader.showRoudImageAsyn(mSetting_head, completeURL, R.drawable.login_1,getActivity());
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.e("SettingFragment", "������onStart() ʱ��isShowΪʲô===================="+ isShow);
		if (!isShow) {
			User4head();
		}
		Log.e("SettingFragment", "������onStart() ====================");
	}

	@Override
	public void onResume() {
		Log.e("SettingFragment", "������onResume() ====================");
		Log.e("SettingFragment", "��ʱ��isShowΪʲô111111 ===================="+ isShow);
		if (isShow) {
			// ��ʼ��ͼƬǩ��
			initUpload();
		}
		super.onResume();
	}

	private void returndata() {
		ReadUser(SD_pk_user);
	}

	private void ReadUser(int returndata) {
		Setting_readuserinter = Interface.getInstance();
		User readuser = new User();
		readuser.setPk_user(returndata);
		Setting_readuserinter.readUser(getActivity(), readuser);
		Setting_readuserinter.setPostListener(new readUserListenner() {

			@Override
			public void success(String A) {

				// ��ȡ�û����ϳɹ�
				Loginback usersettingback = GsonUtils.parseJson(A,Loginback.class);
				int userStatusmsg = usersettingback.getStatusMsg();
				if (userStatusmsg == 1) {
					Log.e("SettingFragment", "�û�����" + A);
					List<User> Users = usersettingback.getReturnData();
					if (Users.size() >= 1) {
						Setting_readuser = Users.get(0);
						mUserNickname = Setting_readuser.getNickname();
						mUserAvatar_path = Setting_readuser.getAvatar_path();
						mUserPhone = Setting_readuser.getPhone();
						mUserPassword = Setting_readuser.getPassword();
						mUserSetup_time = Setting_readuser.getSetup_time();
						mUserLast_login_time = Setting_readuser.getLast_login_time();
						mUserJpush_id = Setting_readuser.getJpush_id();
						mUserSex = Setting_readuser.getSex();
						mUserDevice_id = Setting_readuser.getDevice_id();
						mUserWechat_id = Setting_readuser.getWechat_id();

						Log.e("SettingFragment", "��һ�εõ����û���Ϣ11111111===="
								+ SD_pk_user + "\n" + mUserJpush_id + "\n"
								+ mUserNickname + "\n" + mUserPassword + "\n"
								+ mUserSex + "\n" + mUserPhone + "\n"
								+ mUserSetup_time + "\n" + mUserLast_login_time
								+ "\n" + mUserAvatar_path + "\n"
								+ mUserWechat_id);

					}
					mSetting_User_ID.setText("" + SD_pk_user);
					mSetting_Nickname.setText(mUserNickname);
					switch (mUserSex) {
					case 0:
						mSetting_Sex.setText("����");
						break;
					case 1:
						mSetting_Sex.setText("��");
						break;
					case 2:
						mSetting_Sex.setText("Ů");
						break;

					default:
						break;
					}
					mSetting_Phone.setText(mUserPhone);
					completeURL = beginStr + mUserAvatar_path + endStr;
					PreferenceUtils.saveImageCache(getActivity(), completeURL);// ��SP
//					ImageLoaderUtils.getInstance().LoadImage(getActivity(),
//							completeURL, mSetting_head);
					AsynImageLoader asynImageLoader = new AsynImageLoader();
					asynImageLoader.showRoudImageAsyn(mSetting_head, completeURL, R.drawable.login_1,getActivity());
				}
			}

			@Override
			public void defail(Object B) {

			}
		});

		// ͼ��ǩ����ʼ��
		Setting_readuserinter.setPostListener(new getPicSignListenner() {

			@Override
			public void success(String A) {
				Log.e("SettingFragment", "�µķ���ǩ���ַ�����" + A);
				PicSignBack picSignBack = GsonUtils.parseJson(A,PicSignBack.class);
				Integer status = picSignBack.getStatusMsg();
				if (status == 1) {
					String returnData = picSignBack.getReturnData();
					SettingFragment.setSIGN(returnData);

					UploadManager.authorize(APPID, USERID, SIGN);
					uploadManager = new UploadManager(getActivity(),"persistenceId");
					Log.e("SettingFragment","�µķ���ǩ���ַ���returnData=================="+ returnData);
					if (!("".equals(returnData))) {
						// ��ȡ��ͼƬ��ֱ�ӽ����ϴ�
						User usersetting = new User();
						usersetting.setPk_user(SD_pk_user);
						usersetting.setJpush_id(mUserJpush_id);
						usersetting.setNickname(mUserNickname);
						usersetting.setPassword(mUserPassword);
						usersetting.setSex(mUserSex);
						usersetting.setStatus(1);
						usersetting.setPhone(mUserPhone);
						usersetting.setWechat_id(mUserWechat_id);
						usersetting.setSetup_time(mUserSetup_time);
						usersetting.setLast_login_time(mUserLast_login_time);
						upload(usersetting);// �ϴ�
						Log.e("SettingFragment","�����˸��µķ����� ====================");
					} else {
						Toast.makeText(getActivity(), "����ͷ��ʧ�ܣ������¸���",Toast.LENGTH_SHORT).show();
					}
				}
			}

			@Override
			public void defail(Object B) {

			}
		});

		// ���µļ���
		Setting_readuserinter.setPostListener(new updateUserListenner() {

			@Override
			public void success(String A) {
				// �����û����ϳɹ�
				updateback usersetting_updateback = GsonUtils.parseJson(A,updateback.class);
				int a = usersetting_updateback.getStatusMsg();
				if (a == 1) {
					Log.e("SettingFragment", "���³ɹ�" + A);
					isShow = false;
					User readuser = new User();
					readuser.setPk_user(SD_pk_user);
					Setting_readuserinter.readUser(getActivity(), readuser);
					mSetting_head.setVisibility(View.VISIBLE);
					mSetting_head_1.setVisibility(View.GONE);
				}
			}

			@Override
			public void defail(Object B) {

			}
		});

	}

	private void initUI() {
		mSetting_Phone = (TextView) mLayout.findViewById(R.id.Setting_Phone);//�û����ֻ�������ʾ
		mSetting_Sex = (TextView) mLayout.findViewById(R.id.Setting_Sex);//�û����Ա���ʾ
		mSetting_Nickname = (TextView) mLayout.findViewById(R.id.Setting_Nickname);//�û����ǳ���ʾ
		mSetting_head_1 = (ImageView) mLayout.findViewById(R.id.Setting_head_1);// �ϴ�ͼƬʱ��ʾ
		mSetting_head = (ImageView) mLayout.findViewById(R.id.Setting_head);
		mSetting_progress = (TextView) mLayout.findViewById(R.id.Setting_progress);// �ϴ�ͼƬʱ �Ľ���
		mSetting_User_ID = (TextView) mLayout.findViewById(R.id.Setting_User_id);// �û���ID
		mSetting_Nickname_change = (RelativeLayout) mLayout.findViewById(R.id.Setting_Nickname_change);// �û��ǳƽ����޸�
		mSetting_Sex_change = (RelativeLayout) mLayout.findViewById(R.id.Setting_Sex_change);// �û����Ա��޸�
		mSetting_Binding_phone = (RelativeLayout) mLayout.findViewById(R.id.Setting_Binding_phone);// �û����ֻ�
		mSetting_Binding_weixin = (RelativeLayout) mLayout.findViewById(R.id.Setting_Binding_weixin);// ��΢��
		mSetting_Binding_weixin_wallet = (RelativeLayout) mLayout.findViewById(R.id.Setting_Binding_weixin_wallet);// ��΢��Ǯ��
		mSetting_Binding_alipay = (RelativeLayout) mLayout.findViewById(R.id.Setting_Binding_alipay);// ��֧����
		mSetting_feedback = (RelativeLayout) mLayout.findViewById(R.id.Setting_feedback);// �û�����
		mSetting_about_us = (RelativeLayout) mLayout.findViewById(R.id.Setting_about_us);// ��������
		mSetting_Exit = (RelativeLayout) mLayout.findViewById(R.id.Setting_Exit);// �˳���¼
		mSetting_head.setOnClickListener(this);
		mSetting_Nickname_change.setOnClickListener(this);
		mSetting_Sex_change.setOnClickListener(this);
		mSetting_Binding_phone.setOnClickListener(this);
		mSetting_Binding_weixin.setOnClickListener(this);
		mSetting_Binding_weixin_wallet.setOnClickListener(this);
		mSetting_Binding_alipay.setOnClickListener(this);
		mSetting_feedback.setOnClickListener(this);
		mSetting_about_us.setOnClickListener(this);
		mSetting_Exit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Setting_feedback:
			Setting_feedback();
			break;
		case R.id.Setting_about_us:
			Setting_about_us();
			break;
		case R.id.Setting_Exit:
			Setting_Exit();
			break;
		case R.id.Setting_Binding_phone:
			Setting_Binding_phone();
			break;
		case R.id.Setting_Binding_weixin:
			Setting_Binding_weixin();
			break;
		case R.id.Setting_head:
			Setting_head();
			break;
		case R.id.Setting_Nickname_change:
			Setting_Nickname_change();
			break;
		case R.id.Setting_Sex_change:
			Setting_Sex_change();
			break;
		default:
			break;
		}
	}

	//�޸��Ա�
	private void Setting_Sex_change() {
		SdPkUser.setGetSexUser(Setting_readuser);
		Intent intent=new Intent(getActivity(), SexActivity.class);
		startActivity(intent);
	}

	//�û����ǳ��޸�
	private void Setting_Nickname_change() {
		SdPkUser.setGetNicknameUser(Setting_readuser);
		Intent intent=new Intent(getActivity(), NicknameActivity.class);
		startActivity(intent);
	}

	private void Setting_head() {
		isShow = true;
		Log.e("SettingFragment", "�յ���ʱ��isShowΪʲô222222222 ===================="+ isShow);
		// ʹ��intent����ϵͳ�ṩ����Ṧ�ܣ�ʹ��startActivityForResult��Ϊ�˻�ȡ�û�ѡ���ͼƬ
		Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
		getAlbum.setType(IMAGE_TYPE);
		startActivityForResult(getAlbum, IMAGE_CODE);
		mSetting_head.setVisibility(View.GONE);
		mSetting_head_1.setVisibility(View.VISIBLE);
	}

	// ��΢���˺�
	private void Setting_Binding_weixin() {
		// ��ת΢�Ű󶨽���
		final SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = "carjob_wx_login";
		MyApplication.api.sendReq(req);
		SdPkUser.setGetUser(Setting_readuser);
		SdPkUser.setGetweixinBinding(true);
	}

	// ���ֻ�
	private void Setting_Binding_phone() {
		if (!("".equals(mUserPhone))) {
			Phone_NiftyDialogBuilder();
		} else {
			Intent intent = new Intent(getActivity(),BindingPhoneActivity.class);
			intent.putExtra(IConstant.UserData, Setting_readuser);
			startActivity(intent);
		}
	}

	// �˳���¼
	private void Setting_Exit() {
		NiftyDialogBuilder();
	}

	// ��������
	private void Setting_about_us() {
		Intent intent = new Intent(getActivity(), AboutUsActivity.class);
		startActivity(intent);
	}

	// �û�����
	private void Setting_feedback() {
		Intent intent = new Intent(getActivity(), FeedbackActivity.class);
		startActivity(intent);
	}

	// ���ֻ�����
	private void Phone_NiftyDialogBuilder() {
		final NiftyDialogBuilder niftyDialogBuilder = NiftyDialogBuilder
				.getInstance(getActivity());
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
						Intent intent = new Intent(getActivity(),
								BindingPhoneActivity.class);
						intent.putExtra(IConstant.UserData, Setting_readuser);
						startActivity(intent);
						niftyDialogBuilder.cancel();
					}
				}).show();

	}

	// �˳���¼
	private void NiftyDialogBuilder() {
		final NiftyDialogBuilder niftyDialogBuilder = NiftyDialogBuilder
				.getInstance(getActivity());
		Effectstype effectstype = Effectstype.Shake;
		niftyDialogBuilder.withTitle("����").withTitleColor("#000000")
				// ���ñ���������ɫ
				.withDividerColor("#ffffff")
				// ���öԻ��򱳾���ɫ
				.withMessage("ȷ��Ҫ�ǳ��˺���" + "\n" + "��������Ͻ��ᱻ���Ŷ~")
				// �Ի�����ʾ����
				.withMessageColor("#000000")
				// ��ʾ����������ɫ
				.withIcon(getResources().getDrawable(R.drawable.about_us))
				// ���öԻ�����ʾͼƬ
				.isCancelableOnTouchOutside(true).withDuration(700)
				// ����ʱ��
				.withEffect(effectstype).withButton1Text("��������")
				.withButton2Text("�ǵ�").setButton1Click(new OnClickListener() {

					@Override
					public void onClick(View v) {
						niftyDialogBuilder.cancel();
					}
				}).setButton2Click(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),BeforeLoginActivity.class);
						startActivity(intent);
						getActivity().finish();
						niftyDialogBuilder.cancel();
					}
				}).show();

	}

	// �ϴ�ͼƬ
	private void upload(final User user) {
		String mFilePath = SdPkUser.getFilePath;
		UploadTask task = new PhotoUploadTask(mFilePath,
				new IUploadTaskListener() {
					@Override
					public void onUploadSucceed(final FileInfo result) {
						Log.e("�ϴ����", "upload succeed: " + result.fileId);
						// �ϴ���ɺ�ע��
						user.setAvatar_path(result.fileId);
						Setting_readuserinter.updateUser(getActivity(), user);
						Log.e("SettingFragment",
								"������ͼƬ�ϴ���ʱ��====================");
					}

					@Override
					public void onUploadStateChange(TaskState state) {
					}

					@Override
					public void onUploadProgress(long totalSize, long sendSize) {
						final long p = (long) ((sendSize * 100) / (totalSize * 1.0f));
						// Log.e("�ϴ�����", "�ϴ�����: " + p + "%");
						mSetting_progress.post(new Runnable() {

							@Override
							public void run() {
								mSetting_progress.setVisibility(View.VISIBLE);
								mSetting_progress.setText(p + "%");
								if (p == 100) {
									mSetting_progress.setVisibility(View.GONE);
								}
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

	private void initUpload() {
		Setting_readuserinter.getPicSign(getActivity(), new User());
	}

}
