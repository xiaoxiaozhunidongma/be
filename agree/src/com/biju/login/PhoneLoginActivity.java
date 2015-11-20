package com.biju.login;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.BJ.javabean.Code;
import com.BJ.javabean.Codeback;
import com.BJ.javabean.Group;
import com.BJ.javabean.Loginback;
import com.BJ.javabean.Phone;
import com.BJ.javabean.PicSignBack;
import com.BJ.javabean.User;
import com.BJ.javabean.updateback;
import com.BJ.utils.Person;
import com.BJ.utils.PreferenceUtils;
import com.BJ.utils.RefreshActivity;
import com.BJ.utils.SdPkUser;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.findUserListenner;
import com.biju.Interface.getPicSignListenner;
import com.biju.Interface.readUserListenner;
import com.biju.Interface.requestVerCodeListenner;
import com.biju.Interface.updateUserListenner;
import com.biju.MainActivity;
import com.biju.R;
import com.biju.APP.MyApplication;
import com.fragment.HomeFragment;
import com.github.volley_examples.utils.GsonUtils;

@SuppressLint("ResourceAsColor")
public class PhoneLoginActivity extends Activity implements OnClickListener {

	private RelativeLayout mPhoneLogin_before_layout;
	private EditText mPhoneLogin_phone;
	private TextView mPhoneLogin_send;
	private RelativeLayout mPhoneLogin_after_layout;
	private EditText mPhoneLogin_code;
	private TextView mPhoneLogin_OK;
	private String phone;
	private String phoneLogin_phone;
	private Integer phoneLogin_codeback;
	private int sum = 60;
	private boolean isOK;
	private boolean isagain;
	private Interface phoneLoginInterface;
	private HomeFragment mHomeFragmen;

	private Integer pk_user;
	private String mNickname;
	private String mAvatar_path;
	private String mPhone;
	private String mPassword;
	private Integer sex;
	private String setup_time;
	private String last_login_time;
	private String device_id;
	private Integer status;
	private String wechat_id;
	private String beginStr = "http://201139.image.myqcloud.com/201139/0/";
	private String endStr = "/original";
	// 完整路径completeURL=beginStr+result.filepath+endStr;
	private String completeURL = "";

	private String fileName = getSDPath() + "/" + "saveData";

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

	// 提供给首界面的容器
	public static ArrayList<Group> list = new ArrayList<Group>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_login);
		initUI();
		initInterface();
		RefreshActivity.activList_3.add(PhoneLoginActivity.this);
	}

	private void initInterface() {
		phoneLoginInterface = Interface.getInstance();
		phoneLoginInterface.setPostListener(new requestVerCodeListenner() {

			@Override
			public void success(String A) {
				Codeback codeback = GsonUtils.parseJson(A, Codeback.class);
				Integer code_statusmsg = codeback.getStatusMsg();
				if (code_statusmsg == 1) {
					Log.e("PhoneLoginActivity", "返回验证码是否发送成功======" + A);
					Code code = (Code) codeback.getReturnData();
					phoneLogin_codeback = code.getCode();
					Log.e("PhoneLoginActivity",
							"返回验证码是否发送成功======" + code.getCode());
					// 发送一次后再60s内不能再发送第二次
					mPhoneLogin_OK.post(new Runnable() {

						@Override
						public void run() {
							sum--;
							if (sum > 0) {
								mPhoneLogin_OK.setText(sum + "秒后重新发送验证码");
								mPhoneLogin_OK.postDelayed(this, 1000);
								mPhoneLogin_OK.setEnabled(false);
								mPhoneLogin_code
										.addTextChangedListener(new TextWatcher() {

											@Override
											public void onTextChanged(
													CharSequence s, int start,
													int before, int count) {
												String code = s.toString();
												if (code != null) {
													isOK = true;
													sum = 0;
												}
											}

											@Override
											public void beforeTextChanged(
													CharSequence s, int start,
													int count, int after) {
											}

											@Override
											public void afterTextChanged(
													Editable s) {
											}
										});
							} else {
								if (isOK) {
									mPhoneLogin_OK.setText("完成验证");
									mPhoneLogin_OK.setEnabled(true);
									isagain = false;
								} else {
									mPhoneLogin_OK.setText("发送验证码");
									isagain = true;
									sum = 60;
									mPhoneLogin_OK.setEnabled(true);
								}
							}
						}
					});
					mPhoneLogin_before_layout.setVisibility(View.GONE);
					mPhoneLogin_after_layout.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void defail(Object B) {
			}
		});

		phoneLoginInterface.setPostListener(new findUserListenner() {

			@Override
			public void success(String A) {
				Loginback phoneRegistered_statusmsg = GsonUtils.parseJson(A,
						Loginback.class);
				int statusmsg = phoneRegistered_statusmsg.getStatusMsg();
				if (statusmsg == 1) {
					Log.e("PhoneLoginActivity", "根据手机号码查找的结果========" + A);
					// 取第一个Users[0]
					List<User> Users = phoneRegistered_statusmsg.getReturnData();
					int size = Users.size() - 1;
					if (Users.size() >= 1) {
						User user = Users.get(size);
						if (user.getPk_user() != null) {
							// 进行登录
							Log.e("PhoneLoginActivity","得到已绑定该手机号码的用户" + user.getPk_user());
							Integer Phone_pk_user = user.getPk_user();

							Intent intent = new Intent(PhoneLoginActivity.this,MainActivity.class);
							intent.putExtra(IConstant.Sdcard, true);
							startActivity(intent);
							//从退出小组后重新登录时要重新赋值为false
							SdPkUser.setExit(false);
							
							//读取用户资料
							User readuser = new User();
							readuser.setPk_user(Phone_pk_user);
							phoneLoginInterface.readUser(PhoneLoginActivity.this, readuser);

							// 把pk_user保存进一个工具类中
							SdPkUser.setsD_pk_user(Phone_pk_user);

							Person person = new Person(Phone_pk_user);
							try {
								ObjectOutputStream oos = new ObjectOutputStream(
										new FileOutputStream(fileName));
								oos.writeObject(person);
								oos.close();
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				} else {
					// 如果手机未注册弹出对话框进行选择
					NiftyDialogBuilder();
				}

			}

			@Override
			public void defail(Object B) {

			}
		});

		phoneLoginInterface.setPostListener(new readUserListenner() {

			@Override
			public void success(String A) {
				Log.e("PhoneLoginActivity", "用户资料" + A);
				Loginback loginbackread = GsonUtils.parseJson(A,
						Loginback.class);
				int aa = loginbackread.getStatusMsg();
				if (aa == 1) {
					// 取第一个Users[0]
					List<User> Users = loginbackread.getReturnData();
					if (Users.size() >= 1) {
						User readuser = Users.get(0);
						pk_user = readuser.getPk_user();
						mNickname = readuser.getNickname();
						mAvatar_path = readuser.getAvatar_path();
						completeURL = beginStr + mAvatar_path + endStr;
						PreferenceUtils.saveImageCache(PhoneLoginActivity.this,completeURL);
						mPhone = readuser.getPhone();
						mPassword = readuser.getPassword();
						sex = readuser.getSex();
						setup_time = readuser.getSetup_time();
						last_login_time = readuser.getLast_login_time();
						device_id = readuser.getDevice_id();
						status = readuser.getStatus();
						wechat_id = readuser.getWechat_id();
					}
					// 每次登陆都更新用户的信息，主要是极光推送的ID
					updateLogin();
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
		// 更新用户资料成功
		phoneLoginInterface.setPostListener(new updateUserListenner() {

			@Override
			public void success(String A) {

				updateback usersetting_updateback = GsonUtils.parseJson(A,
						updateback.class);
				int a = usersetting_updateback.getStatusMsg();
				if (a == 1) {
					Log.e("PhoneLoginActivity", "更新成功" + A);
				}
			}

			@Override
			public void defail(Object B) {

			}
		});

	}

	// 用户进行登录
	private void updateLogin() {
		User usersetting = new User();
		usersetting.setPk_user(pk_user);
		usersetting.setJpush_id(MyApplication.getRegId());
		usersetting.setNickname(mNickname);
		usersetting.setPassword(mPassword);
		usersetting.setSex(sex);
		usersetting.setStatus(status);
		usersetting.setPhone(mPhone);
		usersetting.setSetup_time(setup_time);
		usersetting.setLast_login_time(last_login_time);
		usersetting.setAvatar_path(mAvatar_path);
		usersetting.setDevice_id(device_id);
		usersetting.setWechat_id(wechat_id);
		phoneLoginInterface.updateUser(PhoneLoginActivity.this, usersetting);
	}

	// 如果手机未注册弹出对话框进行选择
	private void NiftyDialogBuilder() {
		// final NiftyDialogBuilder niftyDialogBuilder = NiftyDialogBuilder
		// .getInstance(this);
		// Effectstype effectstype = Effectstype.Shake;
		// niftyDialogBuilder.withTitle("提示").withTitleColor("#000000")
		// // 设置标题字体颜色
		// .withDividerColor("#ffffff")
		// // 设置对话框背景颜色
		// .withMessage("登录失败，该手机还未注册!"+"\n"+"是否进行注册？")
		// // 对话框提示内容
		// .withMessageColor("#000000")
		// // 提示内容字体颜色
		// .withIcon(getResources().getDrawable(R.drawable.about_us))
		// // 设置对话框显示图片
		// .isCancelableOnTouchOutside(true).withDuration(700)
		// // 设置时间
		// .withEffect(effectstype).withButton1Text("取消")
		// .withButton2Text("确定").setButton1Click(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// niftyDialogBuilder.cancel();
		// finish();
		// //关闭登录界面
		// RefreshActivity.activList_3.get(1).finish();
		// }
		// }).setButton2Click(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// //获取图片签名字符串
		// phoneLoginInterface.getPicSign(PhoneLoginActivity.this, new User());
		// phoneLoginInterface.setPostListener(new getPicSignListenner() {
		//
		// @Override
		// public void success(String A) {
		// Log.e("PhoneLoginActivity", "签名字符串：" + A);
		// PicSignBack picSignBack = GsonUtils.parseJson(A,PicSignBack.class);
		// String returnData = picSignBack.getReturnData();
		// RegisteredActivity.setSIGN(returnData);
		//
		// //进行注册
		// Intent intent=new Intent(PhoneLoginActivity.this,
		// RegisteredActivity.class);
		// intent.putExtra("phoneRegistered_phone", phoneLogin_phone);
		// intent.putExtra("PhoneLogin", true);
		// startActivity(intent);
		// finish();
		// }
		//
		// @Override
		// public void defail(Object B) {
		//
		// }
		// });
		//
		// niftyDialogBuilder.cancel();
		// }
		// }).show();

		final SweetAlertDialog sd = new SweetAlertDialog(
				PhoneLoginActivity.this, SweetAlertDialog.WARNING_TYPE);
		sd.setTitleText("提示");
		sd.setContentText("登录失败，该手机还未注册!" + "\n" + "是否进行注册？");
		sd.setCancelText("取消");
		sd.setConfirmText("确定");
		sd.showCancelButton(true);
		sd.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sDialog) {
				sd.cancel();
				finish();
				// 关闭登录界面
				RefreshActivity.activList_3.get(1).finish();
			}
		}).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sDialog) {
				// 获取图片签名字符串
				phoneLoginInterface.getPicSign(PhoneLoginActivity.this,new User());
				phoneLoginInterface.setPostListener(new getPicSignListenner() {

					@Override
					public void success(String A) {
						Log.e("PhoneLoginActivity", "签名字符串：" + A);
						PicSignBack picSignBack = GsonUtils.parseJson(A,PicSignBack.class);
						String returnData = picSignBack.getReturnData();
						RegisteredActivity.setSIGN(returnData);

						// 进行注册
						Intent intent = new Intent(PhoneLoginActivity.this,RegisteredActivity.class);
						intent.putExtra("phoneRegistered_phone",phoneLogin_phone);
						intent.putExtra("PhoneLogin", true);
						startActivity(intent);
						finish();
					}

					@Override
					public void defail(Object B) {

					}
				});
				sd.cancel();
			}
		}).show();

	}

	private void initUI() {
		findViewById(R.id.PhoneLogin_back_layout).setOnClickListener(this);
		findViewById(R.id.PhoneLogin_back).setOnClickListener(this);// 关闭
		mPhoneLogin_before_layout = (RelativeLayout) findViewById(R.id.PhoneLogin_before_layout);// 发送验证码前布局
		mPhoneLogin_phone = (EditText) findViewById(R.id.PhoneLogin_phone);// 输入手机号码
		mPhoneLogin_phone.setInputType(EditorInfo.TYPE_CLASS_PHONE);// 输入手机号码时直接弹出数字键盘
		mPhoneLogin_send = (TextView) findViewById(R.id.PhoneLogin_send);// 发送验证码
		mPhoneLogin_send.setOnClickListener(this);
		mPhoneLogin_after_layout = (RelativeLayout) findViewById(R.id.PhoneLogin_after_layout);// 发送验证码之后布局
		mPhoneLogin_code = (EditText) findViewById(R.id.PhoneLogin_code);// 输入验证码
		mPhoneLogin_code.setInputType(EditorInfo.TYPE_CLASS_PHONE);// 输入验证码时直接弹出数字键盘
		mPhoneLogin_OK = (TextView) findViewById(R.id.PhoneLogin_OK);// 完成验证
		mPhoneLogin_OK.setOnClickListener(this);
		mPhoneLogin_phone();
	}

	private void mPhoneLogin_phone() {
		mPhoneLogin_phone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				phone = s.toString();
				Log.e("PhoneLoginActivity", "所得到的电话号码phone======" + phone);
				if (phone == null) {
					mPhoneLogin_send.setTextColor(R.color.lightgray1);

				} else {
					mPhoneLogin_send.setTextColor(Color.RED);

					int length = phone.length();
					if (length == 4) {
						if (phone.substring(3).equals(new String(" "))) {
							phone = phone.substring(0, 3);
							mPhoneLogin_phone.setText(phone);
							mPhoneLogin_phone.setSelection(phone.length());
						} else {
							phone = phone.substring(0, 3) + " "+ phone.substring(3);
							mPhoneLogin_phone.setText(phone);
							mPhoneLogin_phone.setSelection(phone.length());
						}
					} else if (length == 9) {
						if (phone.substring(8).equals(new String(" "))) {
							phone = phone.substring(0, 8);
							mPhoneLogin_phone.setText(phone);
							mPhoneLogin_phone.setSelection(phone.length());
						} else {
							phone = phone.substring(0, 8) + " "+ phone.substring(8);
							mPhoneLogin_phone.setText(phone);
							mPhoneLogin_phone.setSelection(phone.length());
						}
					}
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.PhoneLogin_back_layout:
		case R.id.PhoneLogin_back:
			PhoneLogin_back();
			break;
		case R.id.PhoneLogin_send:
			PhoneLogin_send();
			break;
		case R.id.PhoneLogin_OK:
			PhoneLogin_OK();
			break;
		default:
			break;
		}
	}

	private void PhoneLogin_OK() {
		if (isagain) {
			Phone phoneRegistered_1 = new Phone();
			phoneRegistered_1.setPhone(phoneLogin_phone);
			phoneLoginInterface.requestVerCode(PhoneLoginActivity.this,phoneRegistered_1);
		} else {
			String code_1 = mPhoneLogin_code.getText().toString().trim();
			Log.e("PhoneLoginActivity", "所得到的验证码1111======" + code_1);
			Log.e("PhoneLoginActivity", "所得到的验证码222222======"+ phoneLogin_codeback);
			if (Integer.valueOf(code_1).equals(phoneLogin_codeback)) {
				User user = new User();
				user.setPhone(phoneLogin_phone);
				phoneLoginInterface.findUser(PhoneLoginActivity.this, user);

			} else {
				Toast.makeText(PhoneLoginActivity.this, "验证码错误！",Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void PhoneLogin_send() {
		if (phone == null) {
			Toast.makeText(PhoneLoginActivity.this, "请先输入电话号码",Toast.LENGTH_SHORT).show();
		} else {
			if (phone.length() == 13) {
				String phone1 = phone.substring(0, 3);
				String phone2 = phone.substring(4, 8);
				String phone3 = phone.substring(9, 13);
				phoneLogin_phone = phone1 + phone2 + phone3;
				Log.e("PhoneLoginActivity", "所得到的电话号码======" + phoneLogin_phone);
				Phone phoneLogin_1 = new Phone();
				phoneLogin_1.setPhone(phoneLogin_phone);
				phoneLoginInterface.requestVerCode(PhoneLoginActivity.this,phoneLogin_1);
			} else {
				Toast.makeText(PhoneLoginActivity.this, "电话号码格式错误，请输入正确的电话号码",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void PhoneLogin_back() {
		finish();
	}

}
