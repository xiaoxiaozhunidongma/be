package com.biju.wxapi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.BJ.javabean.Access_Token;
import com.BJ.javabean.Loginback;
import com.BJ.javabean.PicSignBack;
import com.BJ.javabean.User;
import com.BJ.javabean.updateback;
import com.BJ.utils.Person;
import com.BJ.utils.PreferenceUtils;
import com.BJ.utils.RefreshActivity;
import com.BJ.utils.SdPkUser;
import com.android.volley.VolleyError;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.getPicSignListenner;
import com.biju.Interface.updateUserListenner;
import com.biju.Interface.weixinLoginListenner;
import com.biju.MainActivity;
import com.biju.R;
import com.biju.APP.MyApplication;
import com.biju.login.RegisteredActivity;
import com.github.volley_examples.app.MyVolley;
import com.github.volley_examples.app.VolleyListenner;
import com.github.volley_examples.utils.GsonUtils;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	@SuppressWarnings("unused")
	private Context context = WXEntryActivity.this;
	private int code;
	private Interface mWeiXinInterface;

	private Integer pk_user;
	private String mNickname;
	private String mAvatar_path;
	private String mPhone;
	private String mPassword;
	private Integer sex;
	private String setup_time;
	private String last_login_time;
	private String device_id;
	private String beginStr = "http://201139.image.myqcloud.com/201139/0/";
	private String endStr = "/original";
	// ����·��completeURL=beginStr+result.filepath+endStr;
	private String completeURL = "";
	private Integer sD_pk_user;
	private String openid;
	public static IWXAPI api;

	private String fileName = getSDPath() + "/" + "saveData";

	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		// �ж�sd���Ƿ����
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// ��ȡ��Ŀ¼
		}
		return sdDir.toString();

	}

	private void handleIntent(Intent paramIntent) {
		api.handleIntent(paramIntent, this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wxentry);
		api = WXAPIFactory.createWXAPI(this, "wx2ffba147560de2ff", true);
		api.registerApp("wx2ffba147560de2ff");
		
		handleIntent(getIntent());
		initInterface();
		sD_pk_user = SdPkUser.getsD_pk_user();
		Log.e("WXEntryActivity", "������onCreate()====" );
	}

	private void initInterface() {
		mWeiXinInterface = Interface.getInstance();
		mWeiXinInterface.setPostListener(new updateUserListenner() {

			@Override
			public void success(String A) {
				// �����û����ϳɹ�
				updateback usersetting_updateback = GsonUtils.parseJson(A,updateback.class);
				int a = usersetting_updateback.getStatusMsg();
				if (a == 1) {
					Log.e("WXEntryActivity", "���³ɹ�" + A);
				}
			}

			@Override
			public void defail(Object B) {

			}
		});

		// ΢�ŵ�¼
		mWeiXinInterface.setPostListener(new weixinLoginListenner() {

			@Override
			public void success(String A) {
				Log.e("WXEntryActivity", "�û�����" + A);
				Loginback loginbackread = GsonUtils.parseJson(A,Loginback.class);
				int aa = loginbackread.getStatusMsg();
				if (aa == 1) {
					// ȡ��һ��Users[0]
					List<User> Users = loginbackread.getReturnData();
					if (Users.size() >= 1) {
						User readuser = Users.get(0);
						pk_user = readuser.getPk_user();
						mNickname = readuser.getNickname();
						mAvatar_path = readuser.getAvatar_path();
						completeURL = beginStr + mAvatar_path + endStr;
						PreferenceUtils.saveImageCache(WXEntryActivity.this,completeURL);
						mPhone = readuser.getPhone();
						mPassword = readuser.getPassword();
						sex = readuser.getSex();
						setup_time = readuser.getSetup_time();
						last_login_time = readuser.getLast_login_time();
						device_id = readuser.getDevice_id();

						Log.e("WXEntryActivity", "�ڶ��εõ����û���Ϣ2222222===="
								+ pk_user + "\n" + MyApplication.getRegId()
								+ "\n" + mNickname + "\n" + mPassword + "\n"
								+ sex + "\n" + mPhone + "\n" + setup_time
								+ "\n" + last_login_time + "\n" + mAvatar_path
								+ "\n" + openid);

						// ���е�¼
						if (pk_user != null) {
							finish();
							Intent intent = new Intent(WXEntryActivity.this,MainActivity.class);
							intent.putExtra(IConstant.Sdcard, true);
							startActivity(intent);
							overridePendingTransition(0, 0);

							// ��pk_user�����һ����������
							SdPkUser.setsD_pk_user(pk_user);
							
							//���˳�С������µ�¼ʱҪ���¸�ֵΪfalse
							SdPkUser.setExit(false);

							Person person = new Person(pk_user);
							try {
								ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
								oos.writeObject(person);
								oos.close();
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
							// ÿ�ε�½�������û�����Ϣ����Ҫ�Ǽ������͵�ID
							updateLogin();
						}
					}
				}else
				{
					// ���΢��δע�ᵯ���Ի������ѡ��
					NiftyDialogBuilder();
				}
			}

			private void NiftyDialogBuilder() {
				final SweetAlertDialog sd = new SweetAlertDialog(
						WXEntryActivity.this, SweetAlertDialog.WARNING_TYPE);
				sd.setTitleText("��ʾ");
				sd.setContentText("��¼ʧ�ܣ���΢�Ż�δע��!" + "\n" + "�Ƿ����ע�᣿");
				sd.setCancelText("ȡ��");
				sd.setConfirmText("ȷ��");
				sd.showCancelButton(true);
				sd.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sDialog) {
						sd.cancel();
						finish();
						// �رյ�¼����
						RefreshActivity.activList_3.get(1).finish();
					}
				}).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sDialog) {
						// ��ȡͼƬǩ���ַ���
						mWeiXinInterface.getPicSign(WXEntryActivity.this,
								new User());
						mWeiXinInterface.setPostListener(new getPicSignListenner() {

							@Override
							public void success(String A) {
								Log.e("PhoneLoginActivity", "ǩ���ַ�����" + A);
								PicSignBack picSignBack = GsonUtils.parseJson(A,PicSignBack.class);
								String returnData = picSignBack.getReturnData();
								RegisteredActivity.setSIGN(returnData);

								finish();
								//΢��ע��
								SdPkUser.setOpenid(openid);
								//��ת�ֻ�ע�����
								Intent intent=new Intent(WXEntryActivity.this, RegisteredActivity.class);
								intent.putExtra("weixinLogin", true);
								startActivity(intent);
							}

							@Override
							public void defail(Object B) {

							}
						});
						sd.cancel();
					}
				}).show();
			}

			private void updateLogin() {
				String jpush_id = MyApplication.getRegId();
				User usersetting = new User();
				usersetting.setPk_user(pk_user);
				usersetting.setJpush_id(jpush_id);
				usersetting.setNickname(mNickname);
				usersetting.setPassword(mPassword);
				usersetting.setSex(sex);
				usersetting.setStatus(1);
				usersetting.setPhone(mPhone);
				usersetting.setSetup_time(setup_time);
				usersetting.setLast_login_time(last_login_time);
				usersetting.setAvatar_path(mAvatar_path);
				usersetting.setWechat_id(openid);// ΢�ŵ�Ψһʶ����
				mWeiXinInterface.updateUser(WXEntryActivity.this, usersetting);
			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		handleIntent(intent);
	}

	@Override
	public void onReq(BaseReq arg0) {
		finish();
	}
	
	@Override
	public void onResp(BaseResp resp) {
		Log.e("WXEntryActivity", "��ȡ��resp.errCode======" + resp.errCode);
		boolean  wesource=SdPkUser.GetWeSource;
		if(wesource){
			switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				Log.e("WXEntryActivity", "���ͳɹ�======"+resp.errCode);
				//�Զ���Toast
				View toastRoot = getLayoutInflater().inflate(R.layout.my_toast, null);
				Toast toast=new Toast(getApplicationContext());
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.setView(toastRoot);
				toast.setDuration(100);
				TextView tv=(TextView)toastRoot.findViewById(R.id.TextViewInfo);
				tv.setText("����ɹ�");
				toast.show();
				finish();
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				Log.e("WXEntryActivity", "����ȡ��======"+resp.errCode);
				finish();
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				Log.e("WXEntryActivity", "���ͱ��ܾ�======"+resp.errCode);
				finish();
				break;
			default:
				break;
			}
		}else {
			switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				code = ((SendAuth.Resp) resp).errCode;
				Log.e("WXEntryActivity", "��ȡ��code======" + code);
				initdata();
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				finish();
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				finish();
				break;
			default:
				break;
			}
			boolean Binding_weixin = SdPkUser.isGetweixinBinding();
			if(Binding_weixin)
			{
				finish();
				overridePendingTransition(0, 0);
			}
		}
	}

	private void initdata() {
		String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx2ffba147560de2ff&secret=fc78e16cf3c7522a2b4b5784fa6c6b40&code="
				+ code + "&grant_type=authorization_code";
		Log.e("WXEntryActivity", "·��===============" + path);

		MyVolley.get(WXEntryActivity.this, path, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
			}

			@Override
			public void onResponse(String response) {
				Log.e("WXEntryActivity", "��ȡ�����Ķ���======" + response);
				Access_Token access_Token = GsonUtils.parseJson(response,Access_Token.class);
				openid = access_Token.getOpenid();
				Log.e("WXEntryActivity", "��������openid========" + openid);
				if (!("".equals(openid))) {
					//�ж���΢�ŵ�¼����΢��ע��
					boolean weixinRegistered=SdPkUser.weixinRegistered;
					if(weixinRegistered)
					{
						Log.e("WXEntryActivity", "����΢��ע��==============");
						User user = new User();
						user.setWechat_id(openid);
						mWeiXinInterface.weixinLogin(WXEntryActivity.this, user);
					}else
					{
						boolean Binding_weixin = SdPkUser.isGetweixinBinding();
						if (Binding_weixin) {
							Log.e("WXEntryActivity", "����΢�Ű�==============");
							User user = SdPkUser.getUser;
							
							User usersetting = new User();
							usersetting.setPk_user(sD_pk_user);
							usersetting.setJpush_id(user.getJpush_id());
							usersetting.setNickname(user.getNickname());
							usersetting.setPassword(user.getPassword());
							usersetting.setSex(user.getSex());
							usersetting.setStatus(1);
							usersetting.setPhone(user.getPhone());
							usersetting.setSetup_time(user.getSetup_time());
							usersetting.setLast_login_time(user.getLast_login_time());
							usersetting.setAvatar_path(user.getAvatar_path());
							usersetting.setWechat_id(openid);// ΢�ŵ�Ψһʶ����
							Log.e("WXEntryActivity","�ڶ��εõ����û���Ϣ2222222====" + sD_pk_user + "\n"
											+ user.getJpush_id() + "\n"
											+ user.getNickname() + "\n"
											+ user.getPassword() + "\n"
											+ user.getSex() + "\n"
											+ user.getPhone() + "\n"
											+ user.getSetup_time() + "\n"
											+ user.getLast_login_time() + "\n"
											+ user.getAvatar_path() + "\n" + openid);
							mWeiXinInterface.updateUser(WXEntryActivity.this,usersetting);
						} else {
							Log.e("WXEntryActivity", "����΢�ŵ�¼==============");
							User user = new User();
							user.setWechat_id(openid);
							mWeiXinInterface.weixinLogin(WXEntryActivity.this, user);
						}
					}
				} else {
					Toast.makeText(WXEntryActivity.this, "��Ȩʧ��",Toast.LENGTH_SHORT).show();
				}
			}
		});

	}
}
