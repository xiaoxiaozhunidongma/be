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
	private final int IMAGE_CODE = 0; // 这里的IMAGE_CODE是自己任意定义的
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

	// 完整路径completeURL=beginStr+result.filepath+endStr;

	public SettingFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayout = inflater.inflate(R.layout.fragment_setting, container, false);
		User4head();
		Log.e("SettingFragment", "进入了onCreateView()====================");
		return mLayout;
	}

	private void User4head() {
		String Cacheurl = PreferenceUtils.readImageCache(getActivity());
		completeURL = Cacheurl;
		// 获取SD卡中的pk_user
		SD_pk_user = SdPkUser.getsD_pk_user();
		Log.e("SettingFragment", "从SD卡中获取到的Pk_user" + SD_pk_user);

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
		Log.e("SettingFragment", "进入了onStart() 时的isShow为什么===================="+ isShow);
		if (!isShow) {
			User4head();
		}
		Log.e("SettingFragment", "进入了onStart() ====================");
	}

	@Override
	public void onResume() {
		Log.e("SettingFragment", "进入了onResume() ====================");
		Log.e("SettingFragment", "此时的isShow为什么111111 ===================="+ isShow);
		if (isShow) {
			// 初始化图片签名
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

				// 读取用户资料成功
				Loginback usersettingback = GsonUtils.parseJson(A,Loginback.class);
				int userStatusmsg = usersettingback.getStatusMsg();
				if (userStatusmsg == 1) {
					Log.e("SettingFragment", "用户资料" + A);
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

						Log.e("SettingFragment", "第一次得到的用户信息11111111===="
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
						mSetting_Sex.setText("其他");
						break;
					case 1:
						mSetting_Sex.setText("男");
						break;
					case 2:
						mSetting_Sex.setText("女");
						break;

					default:
						break;
					}
					mSetting_Phone.setText(mUserPhone);
					completeURL = beginStr + mUserAvatar_path + endStr;
					PreferenceUtils.saveImageCache(getActivity(), completeURL);// 存SP
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

		// 图像签名初始化
		Setting_readuserinter.setPostListener(new getPicSignListenner() {

			@Override
			public void success(String A) {
				Log.e("SettingFragment", "新的方法签名字符串：" + A);
				PicSignBack picSignBack = GsonUtils.parseJson(A,PicSignBack.class);
				Integer status = picSignBack.getStatusMsg();
				if (status == 1) {
					String returnData = picSignBack.getReturnData();
					SettingFragment.setSIGN(returnData);

					UploadManager.authorize(APPID, USERID, SIGN);
					uploadManager = new UploadManager(getActivity(),"persistenceId");
					Log.e("SettingFragment","新的方法签名字符串returnData=================="+ returnData);
					if (!("".equals(returnData))) {
						// 获取到图片后直接进行上传
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
						upload(usersetting);// 上传
						Log.e("SettingFragment","进入了更新的方法里 ====================");
					} else {
						Toast.makeText(getActivity(), "更换头像失败，请重新更换",Toast.LENGTH_SHORT).show();
					}
				}
			}

			@Override
			public void defail(Object B) {

			}
		});

		// 更新的监听
		Setting_readuserinter.setPostListener(new updateUserListenner() {

			@Override
			public void success(String A) {
				// 更新用户资料成功
				updateback usersetting_updateback = GsonUtils.parseJson(A,updateback.class);
				int a = usersetting_updateback.getStatusMsg();
				if (a == 1) {
					Log.e("SettingFragment", "更新成功" + A);
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
		mSetting_Phone = (TextView) mLayout.findViewById(R.id.Setting_Phone);//用户的手机号码显示
		mSetting_Sex = (TextView) mLayout.findViewById(R.id.Setting_Sex);//用户的性别显示
		mSetting_Nickname = (TextView) mLayout.findViewById(R.id.Setting_Nickname);//用户的昵称显示
		mSetting_head_1 = (ImageView) mLayout.findViewById(R.id.Setting_head_1);// 上传图片时显示
		mSetting_head = (ImageView) mLayout.findViewById(R.id.Setting_head);
		mSetting_progress = (TextView) mLayout.findViewById(R.id.Setting_progress);// 上传图片时 的进度
		mSetting_User_ID = (TextView) mLayout.findViewById(R.id.Setting_User_id);// 用户的ID
		mSetting_Nickname_change = (RelativeLayout) mLayout.findViewById(R.id.Setting_Nickname_change);// 用户昵称进行修改
		mSetting_Sex_change = (RelativeLayout) mLayout.findViewById(R.id.Setting_Sex_change);// 用户的性别修改
		mSetting_Binding_phone = (RelativeLayout) mLayout.findViewById(R.id.Setting_Binding_phone);// 用户绑定手机
		mSetting_Binding_weixin = (RelativeLayout) mLayout.findViewById(R.id.Setting_Binding_weixin);// 绑定微信
		mSetting_Binding_weixin_wallet = (RelativeLayout) mLayout.findViewById(R.id.Setting_Binding_weixin_wallet);// 绑定微信钱包
		mSetting_Binding_alipay = (RelativeLayout) mLayout.findViewById(R.id.Setting_Binding_alipay);// 绑定支付宝
		mSetting_feedback = (RelativeLayout) mLayout.findViewById(R.id.Setting_feedback);// 用户反馈
		mSetting_about_us = (RelativeLayout) mLayout.findViewById(R.id.Setting_about_us);// 关于我们
		mSetting_Exit = (RelativeLayout) mLayout.findViewById(R.id.Setting_Exit);// 退出登录
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

	//修改性别
	private void Setting_Sex_change() {
		SdPkUser.setGetSexUser(Setting_readuser);
		Intent intent=new Intent(getActivity(), SexActivity.class);
		startActivity(intent);
	}

	//用户的昵称修改
	private void Setting_Nickname_change() {
		SdPkUser.setGetNicknameUser(Setting_readuser);
		Intent intent=new Intent(getActivity(), NicknameActivity.class);
		startActivity(intent);
	}

	private void Setting_head() {
		isShow = true;
		Log.e("SettingFragment", "刚点下时的isShow为什么222222222 ===================="+ isShow);
		// 使用intent调用系统提供的相册功能，使用startActivityForResult是为了获取用户选择的图片
		Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
		getAlbum.setType(IMAGE_TYPE);
		startActivityForResult(getAlbum, IMAGE_CODE);
		mSetting_head.setVisibility(View.GONE);
		mSetting_head_1.setVisibility(View.VISIBLE);
	}

	// 绑定微信账号
	private void Setting_Binding_weixin() {
		// 跳转微信绑定界面
		final SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = "carjob_wx_login";
		MyApplication.api.sendReq(req);
		SdPkUser.setGetUser(Setting_readuser);
		SdPkUser.setGetweixinBinding(true);
	}

	// 绑定手机
	private void Setting_Binding_phone() {
		if (!("".equals(mUserPhone))) {
			Phone_NiftyDialogBuilder();
		} else {
			Intent intent = new Intent(getActivity(),BindingPhoneActivity.class);
			intent.putExtra(IConstant.UserData, Setting_readuser);
			startActivity(intent);
		}
	}

	// 退出登录
	private void Setting_Exit() {
		NiftyDialogBuilder();
	}

	// 关于我们
	private void Setting_about_us() {
		Intent intent = new Intent(getActivity(), AboutUsActivity.class);
		startActivity(intent);
	}

	// 用户反馈
	private void Setting_feedback() {
		Intent intent = new Intent(getActivity(), FeedbackActivity.class);
		startActivity(intent);
	}

	// 绑定手机号码
	private void Phone_NiftyDialogBuilder() {
		final NiftyDialogBuilder niftyDialogBuilder = NiftyDialogBuilder
				.getInstance(getActivity());
		Effectstype effectstype = Effectstype.Shake;
		niftyDialogBuilder.withTitle("提示").withTitleColor("#000000")
				// 设置标题字体颜色
				.withDividerColor("#ffffff")
				// 设置对话框背景颜色
				.withMessage("您已经绑定了手机号码，是否重新绑定另一个号码？")
				// 对话框提示内容
				.withMessageColor("#000000")
				// 提示内容字体颜色
				.withIcon(getResources().getDrawable(R.drawable.about_us))
				// 设置对话框显示图片
				.isCancelableOnTouchOutside(true).withDuration(700)
				// 设置时间
				.withEffect(effectstype).withButton1Text("取消")
				.withButton2Text("确定").setButton1Click(new OnClickListener() {

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

	// 退出登录
	private void NiftyDialogBuilder() {
		final NiftyDialogBuilder niftyDialogBuilder = NiftyDialogBuilder
				.getInstance(getActivity());
		Effectstype effectstype = Effectstype.Shake;
		niftyDialogBuilder.withTitle("警告").withTitleColor("#000000")
				// 设置标题字体颜色
				.withDividerColor("#ffffff")
				// 设置对话框背景颜色
				.withMessage("确定要登出账号吗？" + "\n" + "保存的资料将会被清空哦~")
				// 对话框提示内容
				.withMessageColor("#000000")
				// 提示内容字体颜色
				.withIcon(getResources().getDrawable(R.drawable.about_us))
				// 设置对话框显示图片
				.isCancelableOnTouchOutside(true).withDuration(700)
				// 设置时间
				.withEffect(effectstype).withButton1Text("我再想想")
				.withButton2Text("是的").setButton1Click(new OnClickListener() {

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

	// 上传图片
	private void upload(final User user) {
		String mFilePath = SdPkUser.getFilePath;
		UploadTask task = new PhotoUploadTask(mFilePath,
				new IUploadTaskListener() {
					@Override
					public void onUploadSucceed(final FileInfo result) {
						Log.e("上传结果", "upload succeed: " + result.fileId);
						// 上传完成后注册
						user.setAvatar_path(result.fileId);
						Setting_readuserinter.updateUser(getActivity(), user);
						Log.e("SettingFragment",
								"进入了图片上传的时候====================");
					}

					@Override
					public void onUploadStateChange(TaskState state) {
					}

					@Override
					public void onUploadProgress(long totalSize, long sendSize) {
						final long p = (long) ((sendSize * 100) / (totalSize * 1.0f));
						// Log.e("上传进度", "上传进度: " + p + "%");
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
						Log.e("Demo", "上传结果:失败! ret:" + errorCode + " msg:"
								+ errorMsg);
					}
				});
		uploadManager.upload(task); // 开始上传

	}

	private void initUpload() {
		Setting_readuserinter.getPicSign(getActivity(), new User());
	}

}
