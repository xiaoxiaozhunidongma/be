package com.biju.login;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
import android.view.inputmethod.EditorInfo;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.BJ.javabean.Code;
import com.BJ.javabean.Codeback;
import com.BJ.javabean.Loginback;
import com.BJ.javabean.Phone;
import com.BJ.javabean.PicSignBack;
import com.BJ.javabean.User;
import com.BJ.javabean.updateback;
import com.BJ.utils.Person;
import com.BJ.utils.RefreshActivity;
import com.BJ.utils.SdPkUser;
import com.biju.Interface;
import com.biju.Interface.findUserListenner;
import com.biju.Interface.getPicSignListenner;
import com.biju.Interface.readUserListenner;
import com.biju.Interface.requestVerCodeListenner;
import com.biju.Interface.updateUserListenner;
import com.biju.IConstant;
import com.biju.MainActivity;
import com.biju.R;
import com.biju.APP.MyApplication;
import com.fragment.HomeFragment;
import com.github.volley_examples.utils.GsonUtils;

@SuppressLint("ResourceAsColor")
public class PhoneRegisteredActivity extends Activity implements OnClickListener{

	private RelativeLayout mPhoneRegistered_before_layout;
	private EditText mPhoneRegistered_phone;
	private TextView mPhoneRegistered_send;
	private RelativeLayout mPhoneRegistered_after_layout;
	private EditText mPhoneRegistered_code;
	private TextView mPhoneRegistered_OK;
	private Interface phoneRegisteredInterface;
	private String phone;
	private String phoneRegistered_phone;
	private Integer phoneRegistered_codeback;
	private int sum=60;
	private boolean isOK;
	private boolean isagain;
	
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_registered);
		RefreshActivity.activList_3.add(PhoneRegisteredActivity.this);
		initUI();
		initInterface();
	}

	private void initInterface() {
		phoneRegisteredInterface = Interface.getInstance();
		//发送验证码监听
		phoneRegisteredInterface.setPostListener(new requestVerCodeListenner() {

			@Override
			public void success(String A) {
				Codeback codeback = GsonUtils.parseJson(A, Codeback.class);
				Integer code_statusmsg = codeback.getStatusMsg();
				if (code_statusmsg == 1) {
					Log.e("PhoneRegisteredActivity", "返回验证码是否发送成功======" + A);
					Code code = (Code) codeback.getReturnData();
					phoneRegistered_codeback = code.getCode();
					Log.e("PhoneRegisteredActivity","返回验证码是否发送成功======" + code.getCode());
					//发送一次后再60s内不能再发送第二次
					mPhoneRegistered_OK.post(new Runnable() {
						
						@Override
						public void run() {
							sum--;
							if (sum > 0) {
								mPhoneRegistered_OK.setText(sum + "秒后重新发送验证码");
								mPhoneRegistered_OK.postDelayed(this, 1000);
								mPhoneRegistered_OK.setEnabled(false);
								mPhoneRegistered_code.addTextChangedListener(new TextWatcher() {
									
									@Override
									public void onTextChanged(CharSequence s, int start, int before, int count) {
										String code=s.toString();
										if(code!=null)
										{
											isOK = true;
											sum=0;
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
							} else {
								if(isOK)
								{
									mPhoneRegistered_OK.setText("完成验证");
									mPhoneRegistered_OK.setEnabled(true);
									isagain=false;
								}else
								{
									mPhoneRegistered_OK.setText("发送验证码");
									isagain=true;
									sum = 60;
									mPhoneRegistered_OK.setEnabled(true);
								}
							}
						}
					});
					mPhoneRegistered_before_layout.setVisibility(View.GONE);
					mPhoneRegistered_after_layout.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void defail(Object B) {
			}
		});
		
		//根据手机号码进行查找用户监听
		phoneRegisteredInterface.setPostListener(new findUserListenner() {

			@Override
			public void success(String A) {
				Loginback phoneRegistered_statusmsg = GsonUtils.parseJson(A,
						Loginback.class);
				int statusmsg = phoneRegistered_statusmsg.getStatusMsg();
				if (statusmsg == 1) {
					Log.e("PhoneRegisteredActivity", "根据手机号码查找的结果========"+A);
					// 取第一个Users[0]
					List<User> Users = phoneRegistered_statusmsg.getReturnData();
					int size=Users.size()-1;
					if (Users.size() >= 1) {
						User user = Users.get(size);
						if(user.getPk_user()!=null)
						{
							//进行登录
							Log.e("PhoneRegisteredActivity", "得到已绑定该手机号码的用户"+user.getPk_user());
							Integer Phone_pk_user=user.getPk_user();
							User readuser = new User();
							readuser.setPk_user(Phone_pk_user);
							phoneRegisteredInterface.readUser(PhoneRegisteredActivity.this, readuser);
							
							//把pk_user保存进一个工具类中
							SdPkUser.setsD_pk_user(Phone_pk_user);
							//保存进sd卡
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
					//获取图片签名字符串
					phoneRegisteredInterface.getPicSign(PhoneRegisteredActivity.this, new User());
					phoneRegisteredInterface.setPostListener(new getPicSignListenner() {

						@Override
						public void success(String A) {
							Log.e("PhoneRegisteredActivity", "签名字符串：" + A);
							PicSignBack picSignBack = GsonUtils.parseJson(A,PicSignBack.class);
							String returnData = picSignBack.getReturnData();
							RegisteredActivity.setSIGN(returnData);

							//进行注册
							Intent intent=new Intent(PhoneRegisteredActivity.this, RegisteredActivity.class);
							intent.putExtra("phoneRegistered_phone", phoneRegistered_phone);
							intent.putExtra("PhoneLogin", true);
							startActivity(intent);
							finish();
						}

						@Override
						public void defail(Object B) {

						}
					});
				}

			}

			@Override
			public void defail(Object B) {

			}
		});
		
		phoneRegisteredInterface.setPostListener(new readUserListenner() {


			@Override
			public void success(String A) {
				Log.e("PhoneRegisteredActivity", "用户资料" + A);
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
		phoneRegisteredInterface.setPostListener(new updateUserListenner() {

			@Override
			public void success(String A) {
				
				updateback usersetting_updateback = GsonUtils.parseJson(A,
						updateback.class);
				int a = usersetting_updateback.getStatusMsg();
				if (a == 1) {
					Log.e("PhoneRegisteredActivity", "更新成功" + A);
					Intent intent = new Intent(PhoneRegisteredActivity.this,MainActivity.class);
					intent.putExtra(IConstant.Sdcard, true);
					startActivity(intent);
				}
			}

			@Override
			public void defail(Object B) {

			}
		});

	}
	
	//用户进行登录
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
		phoneRegisteredInterface.updateUser(PhoneRegisteredActivity.this, usersetting);
	}

	private void initUI() {
		findViewById(R.id.PhoneRegistered_back_layout).setOnClickListener(this);
		findViewById(R.id.PhoneRegistered_back).setOnClickListener(this);//关闭
		mPhoneRegistered_before_layout = (RelativeLayout) findViewById(R.id.PhoneRegistered_before_layout);//发送验证码前布局
		mPhoneRegistered_phone = (EditText) findViewById(R.id.PhoneRegistered_phone);//输入手机号码
		mPhoneRegistered_phone.setInputType(EditorInfo.TYPE_CLASS_PHONE);// 输入手机号码时直接弹出数字键盘
		mPhoneRegistered_send = (TextView) findViewById(R.id.PhoneRegistered_send);//发送验证码
		mPhoneRegistered_send.setOnClickListener(this);
		mPhoneRegistered_after_layout = (RelativeLayout) findViewById(R.id.PhoneRegistered_after_layout);//发送验证码之后布局
		mPhoneRegistered_code = (EditText) findViewById(R.id.PhoneRegistered_code);//输入验证码
		mPhoneRegistered_code.setInputType(EditorInfo.TYPE_CLASS_PHONE);// 输入验证码时直接弹出数字键盘
		mPhoneRegistered_OK = (TextView) findViewById(R.id.PhoneRegistered_OK);//完成验证
		mPhoneRegistered_OK.setOnClickListener(this);
		mPhoneRegistered_phone();
	}

	private void mPhoneRegistered_phone() {
		mPhoneRegistered_phone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				phone = s.toString();
				Log.e("PhoneRegisteredActivity", "所得到的电话号码phone======" + phone);
				if (phone == null) {
					mPhoneRegistered_send.setTextColor(R.color.lightgray1);

				} else {
					mPhoneRegistered_send.setTextColor(Color.RED);

					int length = phone.length();
					if (length == 4) {
						if (phone.substring(3).equals(new String(" "))) {
							phone = phone.substring(0, 3);
							mPhoneRegistered_phone.setText(phone);
							mPhoneRegistered_phone.setSelection(phone.length());
						} else {
							phone = phone.substring(0, 3) + " "
									+ phone.substring(3);
							mPhoneRegistered_phone.setText(phone);
							mPhoneRegistered_phone.setSelection(phone.length());
						}
					} else if (length == 9) {
						if (phone.substring(8).equals(new String(" "))) {
							phone = phone.substring(0, 8);
							mPhoneRegistered_phone.setText(phone);
							mPhoneRegistered_phone.setSelection(phone.length());
						} else {
							phone = phone.substring(0, 8) + " "
									+ phone.substring(8);
							mPhoneRegistered_phone.setText(phone);
							mPhoneRegistered_phone.setSelection(phone.length());
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
		case R.id.PhoneRegistered_back_layout:
		case R.id.PhoneRegistered_back:
			PhoneRegistered_back();
			break;
		case R.id.PhoneRegistered_send:
			PhoneRegistered_send();
			break;
		case R.id.PhoneRegistered_OK:
			PhoneRegistered_OK();
			break;
		default:
			break;
		}
	}

	private void PhoneRegistered_OK() {
		if(isagain)
		{
			Phone phoneRegistered_1 = new Phone();
			phoneRegistered_1.setPhone(phoneRegistered_phone);
			phoneRegisteredInterface.requestVerCode(PhoneRegisteredActivity.this,
					phoneRegistered_1);
		}else
		{
			String code_1 = mPhoneRegistered_code.getText().toString().trim();
			Log.e("PhoneRegisteredActivity", "所得到的验证码1111======" + code_1);
			Log.e("PhoneRegisteredActivity", "所得到的验证码222222======"+ phoneRegistered_codeback);
			if (Integer.valueOf(code_1).equals(phoneRegistered_codeback)) {
				User user=new User();
				user.setPhone(phoneRegistered_phone);
				phoneRegisteredInterface.findUser(PhoneRegisteredActivity.this, user);
				
				
				
			} else {
				Toast.makeText(PhoneRegisteredActivity.this, "验证码错误！",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void PhoneRegistered_send() {
		if (phone == null) {
			Toast.makeText(PhoneRegisteredActivity.this, "请先输入电话号码",
					Toast.LENGTH_SHORT).show();
		} else {
			String phone1 = phone.substring(0, 3);
			String phone2 = phone.substring(4, 8);
			String phone3 = phone.substring(9, 13);
			phoneRegistered_phone = phone1 + phone2 + phone3;
			Log.e("PhoneRegisteredActivity", "所得到的电话号码======" + phoneRegistered_phone);
			Phone phoneRegistered_1 = new Phone();
			phoneRegistered_1.setPhone(phoneRegistered_phone);
			phoneRegisteredInterface.requestVerCode(PhoneRegisteredActivity.this,
					phoneRegistered_1);
			
		}
		
	}

	private void PhoneRegistered_back() {
		finish();
	}

}
