package com.fragment;

import java.io.IOException;
import java.util.ArrayList;
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
import com.biju.withdrawal.TouchBalanceActivity;
import com.example.imageselected.photo.SelectPhotoActivity;
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
	private String endStr = "@!";
	private String mUserAvatar_path;
	private String completeURL;
	private String TestcompleteURL = beginStr
			+ "1ddff6cf-35ac-446b-8312-10f4083ee13d" + endStr;

	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0; // 这里的IMAGE_CODE是自己任意定义的
	public static String APP_VERSION = "1.0.0";
	public static String APPID = "201139";
	public static String USERID = "";
	public static String SIGN;
	
	public ArrayList<Bitmap> recycleBitmaps=new ArrayList<Bitmap>();

	public static String getSIGN() {
		return SIGN;
	}

	public static void setSIGN(String sIGN) {
		SIGN = sIGN;
	}

	private Integer SD_pk_user;
	private TextView mSetting_User_ID;
	private RelativeLayout mSetting_Nickname_change;
	private RelativeLayout mSetting_Sex_change;
	private RelativeLayout mSetting_Binding_phone;
	private RelativeLayout mSetting_Binding_weixin;
	private RelativeLayout mSetting_Binding_balance_layout;
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
	private TextView mSetting_Binding_balance_show;
	private float mUserAmount;
	int loadnum=0;
	private Bitmap convertToBitmap;
	private Bitmap limitLongScaleBitmap;
	private Bitmap centerSquareScaleBitmap;

	// 完整路径completeURL=beginStr+result.filepath+endStr;

	public SettingFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mLayout == null) {
			mLayout = inflater.inflate(R.layout.fragment_setting, container,false);
			Log.e("SettingFragment", "进入了onCreateView()====================");
			SD_pk_user = SdPkUser.getsD_pk_user();
		}
		// 获取ossService和sampleBucket
		ossService = MyApplication.getOssService();
		sampleBucket = MyApplication.getSampleBucket();
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
			//上传
			String mFilePath = SdPkUser.getFilePath;//回调SD卡路径
			if(mFilePath!=null){
				try {
					convertToBitmap = Path2Bitmap.convertToBitmap(mFilePath);
				} catch (IOException e) {
					e.printStackTrace();
				}
				limitLongScaleBitmap = LimitLong.limitLongScaleBitmap(
						convertToBitmap, 1080);
				centerSquareScaleBitmap = PicCutter.centerSquareScaleBitmap(
						limitLongScaleBitmap, 180);
				bitmap2Bytes = ByteOrBitmap.Bitmap2Bytes(centerSquareScaleBitmap);
				recycleBitmaps.add(convertToBitmap);
				recycleBitmaps.add(limitLongScaleBitmap);
				recycleBitmaps.add(centerSquareScaleBitmap);
				UUID randomUUID = UUID.randomUUID();
				uUid = randomUUID.toString();
				OSSupload(ossData, bitmap2Bytes, uUid,usersetting);
			}else{
				mSetting_head.setVisibility(View.VISIBLE);
				mSetting_head_1.setVisibility(View.GONE);
			}
		}
		super.onResume();
	}
	
	@Override
	public void onStop() {
		for (int i = 0; i < recycleBitmaps.size(); i++) {
			Bitmap bitmap = recycleBitmaps.get(i);
			bitmap.recycle();
		}
		recycleBitmaps.clear();
		super.onStop();
	}
	
	private void returndata() {
		// 获取SD卡中的pk_user
		SD_pk_user = SdPkUser.getsD_pk_user();
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
						mUserAmount = Setting_readuser.getAmount();

						Log.e("SettingFragment", "第一次得到的用户信息11111111===="
								+ SD_pk_user + "\n" + mUserJpush_id + "\n"
								+ mUserNickname + "\n" + mUserPassword + "\n"
								+ mUserSex + "\n" + mUserPhone + "\n"
								+ mUserSetup_time + "\n" + mUserLast_login_time
								+ "\n" + mUserAvatar_path + "\n"
								+ mUserWechat_id);

					}
					mSetting_User_ID.setText(mUserNickname);
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
					completeURL = beginStr + mUserAvatar_path + endStr+"avatar";
					PreferenceUtils.saveImageCache(getActivity(), completeURL);// 存SP
					ImageLoaderUtils.getInstance().LoadImageCricular(getActivity(),completeURL, mSetting_head);
					if(!("".equals(mUserWechat_id)))
					{
						mSetting_weixin.setText("已绑定");
					}
					
					mSetting_Binding_balance_show.setText("¥"+mUserAmount+"0");
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
		mSetting_weixin = (TextView) mLayout.findViewById(R.id.Setting_weixin);//显示用户是否已绑定微信
		mSetting_Phone = (TextView) mLayout.findViewById(R.id.Setting_Phone);// 用户的手机号码显示
		mSetting_Sex = (TextView) mLayout.findViewById(R.id.Setting_Sex);// 用户的性别显示
		mSetting_Nickname = (TextView) mLayout.findViewById(R.id.Setting_Nickname);// 用户的昵称显示
		mSetting_head_1 = (ImageView) mLayout.findViewById(R.id.Setting_head_1);// 上传图片时显示
		mSetting_head = (ImageView) mLayout.findViewById(R.id.Setting_head);
		mSetting_progress = (TextView) mLayout.findViewById(R.id.Setting_progress);// 上传图片时 的进度
		mSetting_User_ID = (TextView) mLayout.findViewById(R.id.Setting_User_id);// 用户的ID
		mSetting_Nickname_change = (RelativeLayout) mLayout.findViewById(R.id.Setting_Nickname_change);// 用户昵称进行修改
		mSetting_Sex_change = (RelativeLayout) mLayout.findViewById(R.id.Setting_Sex_change);// 用户的性别修改
		mSetting_Binding_phone = (RelativeLayout) mLayout.findViewById(R.id.Setting_Binding_phone);// 用户绑定手机
		mSetting_Binding_weixin = (RelativeLayout) mLayout.findViewById(R.id.Setting_Binding_weixin);// 绑定微信
		mSetting_Binding_balance_layout = (RelativeLayout) mLayout.findViewById(R.id.Setting_Binding_balance_layout);// 余额
		mSetting_feedback = (RelativeLayout) mLayout.findViewById(R.id.Setting_feedback);// 用户反馈
		mSetting_about_us = (RelativeLayout) mLayout.findViewById(R.id.Setting_about_us);// 关于我们
		mSetting_Exit = (RelativeLayout) mLayout.findViewById(R.id.Setting_Exit);// 退出登录
		mSetting_Binding_balance_show = (TextView) mLayout.findViewById(R.id.Setting_Binding_balance_show);//显示余额
		
		mSetting_head.setOnClickListener(this);
		mSetting_Nickname_change.setOnClickListener(this);
		mSetting_Sex_change.setOnClickListener(this);
		mSetting_Binding_phone.setOnClickListener(this);
		mSetting_Binding_weixin.setOnClickListener(this);
		mSetting_Binding_balance_layout.setOnClickListener(this);
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
		case R.id.Setting_Binding_balance_layout:
			Setting_Binding_balance_layout();
			break;
		default:
			break;
		}
	}

	//余额提现
	private void Setting_Binding_balance_layout() {
		Intent intent=new Intent(getActivity(), TouchBalanceActivity.class);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.in_item, R.anim.out_item);
	}

	// 修改性别
	private void Setting_Sex_change() {
		SdPkUser.setGetSexUser(Setting_readuser);
		Intent intent = new Intent(getActivity(), SexActivity.class);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.in_item, R.anim.out_item);
	}

	// 用户的昵称修改
	private void Setting_Nickname_change() {
		SdPkUser.setGetNicknameUser(Setting_readuser);
		Intent intent = new Intent(getActivity(), NicknameActivity.class);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.in_item, R.anim.out_item);
	}

	private void Setting_head() {
		isShow = true;
//		// 使用intent调用系统提供的相册功能，使用startActivityForResult是为了获取用户选择的图片
//		Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
//		getAlbum.setType(IMAGE_TYPE);
//		startActivityForResult(getAlbum, IMAGE_CODE);
		
		Intent getAlbum = new Intent(getActivity(), SelectPhotoActivity.class);
		getAlbum.putExtra("SelectType", 0);
		startActivityForResult(getAlbum, IMAGE_CODE);
		
		mSetting_head.setVisibility(View.GONE);
		mSetting_head_1.setVisibility(View.VISIBLE);
	}

	// 绑定微信账号
	private void Setting_Binding_weixin() {
		if (api == null) {
			api = WXAPIFactory.createWXAPI(getActivity(), "wx2ffba147560de2ff",false);
		}

		if (!api.isWXAppInstalled()) {
			// 提醒用户没有按照微信
			Toast.makeText(getActivity(), "还没有安装微信,请先安装微信!", Toast.LENGTH_SHORT).show();
			return;
		}

		api.registerApp("wx2ffba147560de2ff");

		if (!("".equals(mUserWechat_id))) {
			WeiXin_NiftyDialogBuilder();
		} else {
			// 跳转微信绑定界面
			final SendAuth.Req req = new SendAuth.Req();
			req.scope = "snsapi_userinfo";
			req.state = "agree_weixin_login";
			api.sendReq(req);
			SdPkUser.setGetUser(Setting_readuser);
			SdPkUser.setGetweixinBinding(true);
			SdPkUser.setWeixinRegistered(true);
			SdPkUser.setGetWeSource(false);//说明是绑定微信过去的
		}
	}

	private void WeiXin_NiftyDialogBuilder() {
		final SweetAlertDialog sd = new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE);
		sd.setTitleText("提示");
		sd.setContentText("您已经绑定了微信账号，是否重新绑定另一个账号？");
		sd.setCancelText("取消");
		sd.setConfirmText("确定");
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
				// 跳转微信绑定界面
				final SendAuth.Req req = new SendAuth.Req();
				req.scope = "snsapi_userinfo";
				req.state = "agree_weixin_login";
				api.sendReq(req);
				SdPkUser.setGetUser(Setting_readuser);
				SdPkUser.setGetweixinBinding(true);
				SdPkUser.setWeixinRegistered(true);
			}
		}).show();
	}

	// 绑定手机
	private void Setting_Binding_phone() {
		if (!("".equals(mUserPhone))) {
			Phone_NiftyDialogBuilder();
		} else {
			Intent intent = new Intent(getActivity(),BindingPhoneActivity.class);
			intent.putExtra(IConstant.UserData, Setting_readuser);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.in_item, R.anim.out_item);
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
		getActivity().overridePendingTransition(R.anim.in_item, R.anim.out_item);
	}

	// 用户反馈
	private void Setting_feedback() {
		Intent intent = new Intent(getActivity(), FeedbackActivity.class);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.in_item, R.anim.out_item);
	}

	// 绑定手机号码
	private void Phone_NiftyDialogBuilder() {
		final SweetAlertDialog sd = new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE);
		sd.setTitleText("提示");
		sd.setContentText("您已经绑定了手机号码，是否重新绑定另一个号码？");
		sd.setCancelText("取消");
		sd.setConfirmText("确定");
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
				getActivity().overridePendingTransition(R.anim.in_item, R.anim.out_item);
			}
		}).show();

	}

	// 退出登录
	private void NiftyDialogBuilder() {
		final SweetAlertDialog sd = new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE);
		sd.setTitleText("警告");
		sd.setContentText("确定要登出账号吗？" + "\n" + "保存的资料将会被清空哦~");
		sd.setCancelText("我再想想");
		sd.setConfirmText("是的");
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
		ossData.setData(data, "jpg"); // 指定需要上传的数据和它的类型
		ossData.enableUploadCheckMd5sum(); // 开启上传MD5校验
		ossData.uploadInBackground(new SaveCallback() {
			@Override
			public void onSuccess(String objectKey) {
				Log.e("", "图片上传成功");
				loadnum=0;//初始化为0
				Log.e("Main", "objectKey==" + objectKey);
				//上传完成后注册
				user.setAvatar_path(objectKey);
				Setting_readuserinter.updateUser(getActivity(), user);
				Log.e("SettingFragment","进入了图片上传的时候====================");
				
				
			}
			
			@Override
			public void onProgress(String objectKey, int byteCount,
					int totalSize) {
				final long p = (long) ((byteCount * 100) / (totalSize * 1.0f));
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
			public void onFailure(String objectKey, OSSException ossException) {
				Log.e("", "图片上传失败" + ossException.toString());
				loadnum++;
				if(loadnum<=3){
					
					if (isShow) {
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
						recycleBitmaps.add(convertToBitmap);
						recycleBitmaps.add(limitLongScaleBitmap);
						recycleBitmaps.add(centerSquareScaleBitmap);
						UUID randomUUID = UUID.randomUUID();
						uUid = randomUUID.toString();
						OSSupload(SettingFragment.this.ossData, bitmap2Bytes, uUid,usersetting);
					}
					
				}
				
			}
		});
	}

}
