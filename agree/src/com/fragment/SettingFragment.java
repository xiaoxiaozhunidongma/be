package com.fragment;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.BJ.javabean.Loginback;
import com.BJ.javabean.User;
import com.BJ.javabean.updateback;
import com.BJ.utils.ByteOrBitmap;
import com.BJ.utils.Ifwifi;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.LimitLong;
import com.BJ.utils.Path2Bitmap;
import com.BJ.utils.PicCutter;
import com.BJ.utils.PreferenceUtils;
import com.BJ.utils.SdPkUser;
import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.alibaba.sdk.android.oss.storage.OSSBucket;
import com.alibaba.sdk.android.oss.storage.OSSData;
import com.biju.IConstant;
import com.biju.Interface;
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
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class SettingFragment extends Fragment implements OnClickListener {

	private View mLayout;
	public ImageView mSetting_head;
	public static ImageView mSetting_head_1;

	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "";
	private String mUserAvatar_path;
	private String completeURL;
	private String TestcompleteURL = beginStr
			+ "1ddff6cf-35ac-446b-8312-10f4083ee13d" + endStr;

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

//	private UploadManager uploadManager;

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

	public static IWXAPI api;
	private TextView mSetting_weixin;
	private OSSData ossData;
	private OSSService ossService;
	private OSSBucket sampleBucket;
	private byte[] bitmap2Bytes;
	private String uUid;

	// ����·��completeURL=beginStr+result.filepath+endStr;

	public SettingFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mLayout == null) {
			mLayout = inflater.inflate(R.layout.fragment_setting, container,false);
			Log.e("SettingFragment", "������onCreateView()====================");
		}
		// ��ȡossService��sampleBucket
		ossService = MyApplication.getOssService();
		sampleBucket = MyApplication.getSampleBucket();
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
			ImageLoaderUtils.getInstance().LoadImage(getActivity(),completeURL, mSetting_head);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		if (!isShow) {
			User4head();
		}
	}

	@Override
	public void onResume() {
		if (isShow) {
//			// ��ʼ��ͼƬǩ��
//			initUpload();
			
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
//			upload(usersetting);// �ϴ�
			//�ϴ�
			String mFilePath = SdPkUser.getFilePath;//�ص�SD��·��
			Bitmap convertToBitmap = null;
			try {
				convertToBitmap = Path2Bitmap.convertToBitmap(mFilePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Bitmap limitLongScaleBitmap = LimitLong.limitLongScaleBitmap(
					convertToBitmap, 1080);// �������Ϊ1080
			Bitmap centerSquareScaleBitmap = PicCutter.centerSquareScaleBitmap(
					limitLongScaleBitmap, 600);// ��ȡ�м�������
			bitmap2Bytes = ByteOrBitmap.Bitmap2Bytes(centerSquareScaleBitmap);
			UUID randomUUID = UUID.randomUUID();
			uUid = randomUUID.toString();
			OSSupload(ossData, bitmap2Bytes, uUid,usersetting);
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
//					mSetting_User_ID.setText("" + SD_pk_user);
					mSetting_User_ID.setText(mUserNickname);
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
					ImageLoaderUtils.getInstance().LoadImageCricular(getActivity(),completeURL, mSetting_head);
					if(!("".equals(mUserWechat_id)))
					{
						mSetting_weixin.setText("�Ѱ�");
					}
				}
			}

			@Override
			public void defail(Object B) {

			}
		});

		// ͼ��ǩ����ʼ��
//		Setting_readuserinter.setPostListener(new getPicSignListenner() {
//
//			@Override
//			public void success(String A) {
//				Log.e("SettingFragment", "�µķ���ǩ���ַ�����" + A);
//				PicSignBack picSignBack = GsonUtils.parseJson(A,
//						PicSignBack.class);
//				Integer status = picSignBack.getStatusMsg();
//				if (status == 1) {
//					String returnData = picSignBack.getReturnData();
//					SettingFragment.setSIGN(returnData);
//
////					UploadManager.authorize(APPID, USERID, SIGN);
////					uploadManager = new UploadManager(getActivity(),"persistenceId");
//					Log.e("SettingFragment","�µķ���ǩ���ַ���returnData=================="+ returnData);
//					if (!("".equals(returnData))) {
//						// ��ȡ��ͼƬ��ֱ�ӽ����ϴ�
//						User usersetting = new User();
//						usersetting.setPk_user(SD_pk_user);
//						usersetting.setJpush_id(mUserJpush_id);
//						usersetting.setNickname(mUserNickname);
//						usersetting.setPassword(mUserPassword);
//						usersetting.setSex(mUserSex);
//						usersetting.setStatus(1);
//						usersetting.setPhone(mUserPhone);
//						usersetting.setWechat_id(mUserWechat_id);
//						usersetting.setSetup_time(mUserSetup_time);
//						usersetting.setLast_login_time(mUserLast_login_time);
////						upload(usersetting);// �ϴ�
//						//�ϴ�
//						String mFilePath = SdPkUser.getFilePath;
//						Bitmap convertToBitmap = Path2Bitmap.convertToBitmap(mFilePath);
//						Bitmap limitLongScaleBitmap = LimitLong.limitLongScaleBitmap(
//								convertToBitmap, 1080);// �������Ϊ1080
//						Bitmap centerSquareScaleBitmap = PicCutter.centerSquareScaleBitmap(
//								limitLongScaleBitmap, 600);// ��ȡ�м�������
//						bitmap2Bytes = ByteOrBitmap.Bitmap2Bytes(centerSquareScaleBitmap);
//						UUID randomUUID = UUID.randomUUID();
//						uUid = randomUUID.toString();
//						OSSupload(ossData, bitmap2Bytes, uUid,usersetting);
//					} else {
//						Toast.makeText(getActivity(), "����ͷ��ʧ�ܣ������¸���",Toast.LENGTH_SHORT).show();
//					}
//				}
//			}
//
//
//
//			@Override
//			public void defail(Object B) {
//
//			}
//		});

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
		mSetting_weixin = (TextView) mLayout.findViewById(R.id.Setting_weixin);//��ʾ�û��Ƿ��Ѱ�΢��
		mSetting_Phone = (TextView) mLayout.findViewById(R.id.Setting_Phone);// �û����ֻ�������ʾ
		mSetting_Sex = (TextView) mLayout.findViewById(R.id.Setting_Sex);// �û����Ա���ʾ
		mSetting_Nickname = (TextView) mLayout.findViewById(R.id.Setting_Nickname);// �û����ǳ���ʾ
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

		mLayout.findViewById(R.id.Setting_next_1);
		mLayout.findViewById(R.id.Setting_next_2);
		mLayout.findViewById(R.id.Setting_next_3);
		mLayout.findViewById(R.id.Setting_next_4);
		mLayout.findViewById(R.id.Setting_next_5);
		mLayout.findViewById(R.id.Setting_next_6);
		mLayout.findViewById(R.id.Setting_next_7);
		mLayout.findViewById(R.id.Setting_next_8);
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

	// �޸��Ա�
	private void Setting_Sex_change() {
		SdPkUser.setGetSexUser(Setting_readuser);
		Intent intent = new Intent(getActivity(), SexActivity.class);
		startActivity(intent);
	}

	// �û����ǳ��޸�
	private void Setting_Nickname_change() {
		SdPkUser.setGetNicknameUser(Setting_readuser);
		Intent intent = new Intent(getActivity(), NicknameActivity.class);
		startActivity(intent);
	}

	private void Setting_head() {
		isShow = true;
		// ʹ��intent����ϵͳ�ṩ����Ṧ�ܣ�ʹ��startActivityForResult��Ϊ�˻�ȡ�û�ѡ���ͼƬ
		Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
		getAlbum.setType(IMAGE_TYPE);
		startActivityForResult(getAlbum, IMAGE_CODE);
		mSetting_head.setVisibility(View.GONE);
		mSetting_head_1.setVisibility(View.VISIBLE);
	}

	// ��΢���˺�
	private void Setting_Binding_weixin() {
		if (api == null) {
			api = WXAPIFactory.createWXAPI(getActivity(), "wx2ffba147560de2ff",false);
		}

		if (!api.isWXAppInstalled()) {
			// �����û�û�а���΢��
			Toast.makeText(getActivity(), "��û�а�װ΢��,���Ȱ�װ΢��!", Toast.LENGTH_SHORT).show();
			return;
		}

		api.registerApp("wx2ffba147560de2ff");

		if (!("".equals(mUserWechat_id))) {
			WeiXin_NiftyDialogBuilder();
		} else {
			// ��ת΢�Ű󶨽���
			final SendAuth.Req req = new SendAuth.Req();
			req.scope = "snsapi_userinfo";
			req.state = "agree_weixin_login";
			api.sendReq(req);
			SdPkUser.setGetUser(Setting_readuser);
			SdPkUser.setGetweixinBinding(true);
		}
	}

	private void WeiXin_NiftyDialogBuilder() {
		final SweetAlertDialog sd = new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE);
		sd.setTitleText("��ʾ");
		sd.setContentText("���Ѿ�����΢���˺ţ��Ƿ����°���һ���˺ţ�");
		sd.setCancelText("ȡ��");
		sd.setConfirmText("ȷ��");
		sd.showCancelButton(true);
		sd.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sDialog) {
				sd.cancel();
			}
		}).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sDialog) {
				sd.cancel();
				// ��ת΢�Ű󶨽���
				final SendAuth.Req req = new SendAuth.Req();
				req.scope = "snsapi_userinfo";
				req.state = "agree_weixin_login";
				api.sendReq(req);
				SdPkUser.setGetUser(Setting_readuser);
				SdPkUser.setGetweixinBinding(true);
			}
		}).show();
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
		final SweetAlertDialog sd = new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE);
		sd.setTitleText("��ʾ");
		sd.setContentText("���Ѿ������ֻ����룬�Ƿ����°���һ�����룿");
		sd.setCancelText("ȡ��");
		sd.setConfirmText("ȷ��");
		sd.showCancelButton(true);
		sd.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sDialog) {
				sd.cancel();
			}
		}).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sDialog) {
				sd.cancel();
				Intent intent = new Intent(getActivity(),BindingPhoneActivity.class);
				intent.putExtra(IConstant.UserData, Setting_readuser);
				startActivity(intent);
			}
		}).show();

	}

	// �˳���¼
	private void NiftyDialogBuilder() {
		final SweetAlertDialog sd = new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE);
		sd.setTitleText("����");
		sd.setContentText("ȷ��Ҫ�ǳ��˺���" + "\n" + "��������Ͻ��ᱻ���Ŷ~");
		sd.setCancelText("��������");
		sd.setConfirmText("�ǵ�");
		sd.showCancelButton(true);
		sd.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sDialog) {
				sd.cancel();
			}
		}).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sDialog) {
				sd.cancel();
				getActivity().finish();
				SdPkUser.setExit(true);
				Intent intent = new Intent(getActivity(),BeforeLoginActivity.class);
				startActivity(intent);
			}
		}).show();

	}

	// �ϴ�ͼƬ
//	private void upload(final User user) {
//		String mFilePath = SdPkUser.getFilePath;
//		UploadTask task = new PhotoUploadTask(mFilePath,
//				new IUploadTaskListener() {
//					@Override
//					public void onUploadSucceed(final FileInfo result) {
//						Log.e("�ϴ����", "upload succeed: " + result.fileId);
//						// �ϴ���ɺ�ע��
//						user.setAvatar_path(result.fileId);
//						Setting_readuserinter.updateUser(getActivity(), user);
//						Log.e("SettingFragment","������ͼƬ�ϴ���ʱ��====================");
//					}
//
//					@Override
//					public void onUploadStateChange(TaskState state) {
//					}
//
//					@Override
//					public void onUploadProgress(long totalSize, long sendSize) {
//						final long p = (long) ((sendSize * 100) / (totalSize * 1.0f));
//						// Log.e("�ϴ�����", "�ϴ�����: " + p + "%");
//						mSetting_progress.post(new Runnable() {
//
//							@Override
//							public void run() {
//								mSetting_progress.setVisibility(View.VISIBLE);
//								mSetting_progress.setText(p + "%");
//								if (p == 100) {
//									mSetting_progress.setVisibility(View.GONE);
//								}
//							}
//						});
//					}
//
//					@Override
//					public void onUploadFailed(final int errorCode,
//							final String errorMsg) {
//						Log.e("Demo", "�ϴ����:ʧ��! ret:" + errorCode + " msg:"+ errorMsg);
//					}
//				});
//		uploadManager.upload(task); // ��ʼ�ϴ�
//
//	}

//	private void initUpload() {
//		Setting_readuserinter.getPicSign(getActivity(), new User());
//	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ViewGroup parent = (ViewGroup) mLayout.getParent();
		parent.removeView(mLayout);

		Drawable(mSetting_head);
		Drawable(mSetting_head_1);
	}

	@SuppressWarnings("deprecation")
	private void Drawable(ImageView mSetting_head_12) {
		Drawable d = mSetting_head_12.getDrawable();
		if (d != null)
		{
			d.setCallback(null);
			mSetting_head_12.setImageDrawable(null);
			mSetting_head_12.setBackgroundDrawable(null);
		}
	}
	
	public void OSSupload(OSSData ossData, byte[] data, String UUid, final User user) {
		ossData = ossService.getOssData(sampleBucket, UUid);
		ossData.setData(data, "jpg"); // ָ����Ҫ�ϴ������ݺ���������
		ossData.enableUploadCheckMd5sum(); // �����ϴ�MD5У��
		ossData.uploadInBackground(new SaveCallback() {
			@Override
			public void onSuccess(String objectKey) {
				Log.e("", "ͼƬ�ϴ��ɹ�");
				Log.e("Main", "objectKey==" + objectKey);
				//�ϴ���ɺ�ע��
				user.setAvatar_path(objectKey);
				Setting_readuserinter.updateUser(getActivity(), user);
				Log.e("SettingFragment","������ͼƬ�ϴ���ʱ��====================");
				
				
			}
			
			@Override
			public void onProgress(String objectKey, int byteCount,
					int totalSize) {
				final long p = (long) ((byteCount * 100) / (totalSize * 1.0f));
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
			public void onFailure(String objectKey, OSSException ossException) {
				Log.e("", "ͼƬ�ϴ�ʧ��" + ossException.toString());
			}
		});
	}

}
