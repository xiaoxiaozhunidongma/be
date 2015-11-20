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
	// ����·��completeURL=beginStr+result.filepath+endStr;
	private String completeURL = "";

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

	// �ṩ���׽��������
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
					Log.e("PhoneLoginActivity", "������֤���Ƿ��ͳɹ�======" + A);
					Code code = (Code) codeback.getReturnData();
					phoneLogin_codeback = code.getCode();
					Log.e("PhoneLoginActivity",
							"������֤���Ƿ��ͳɹ�======" + code.getCode());
					// ����һ�κ���60s�ڲ����ٷ��͵ڶ���
					mPhoneLogin_OK.post(new Runnable() {

						@Override
						public void run() {
							sum--;
							if (sum > 0) {
								mPhoneLogin_OK.setText(sum + "������·�����֤��");
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
									mPhoneLogin_OK.setText("�����֤");
									mPhoneLogin_OK.setEnabled(true);
									isagain = false;
								} else {
									mPhoneLogin_OK.setText("������֤��");
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
					Log.e("PhoneLoginActivity", "�����ֻ�������ҵĽ��========" + A);
					// ȡ��һ��Users[0]
					List<User> Users = phoneRegistered_statusmsg.getReturnData();
					int size = Users.size() - 1;
					if (Users.size() >= 1) {
						User user = Users.get(size);
						if (user.getPk_user() != null) {
							// ���е�¼
							Log.e("PhoneLoginActivity","�õ��Ѱ󶨸��ֻ�������û�" + user.getPk_user());
							Integer Phone_pk_user = user.getPk_user();

							Intent intent = new Intent(PhoneLoginActivity.this,MainActivity.class);
							intent.putExtra(IConstant.Sdcard, true);
							startActivity(intent);
							//���˳�С������µ�¼ʱҪ���¸�ֵΪfalse
							SdPkUser.setExit(false);
							
							//��ȡ�û�����
							User readuser = new User();
							readuser.setPk_user(Phone_pk_user);
							phoneLoginInterface.readUser(PhoneLoginActivity.this, readuser);

							// ��pk_user�����һ����������
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
					// ����ֻ�δע�ᵯ���Ի������ѡ��
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
				Log.e("PhoneLoginActivity", "�û�����" + A);
				Loginback loginbackread = GsonUtils.parseJson(A,
						Loginback.class);
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
					// ÿ�ε�½�������û�����Ϣ����Ҫ�Ǽ������͵�ID
					updateLogin();
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
		// �����û����ϳɹ�
		phoneLoginInterface.setPostListener(new updateUserListenner() {

			@Override
			public void success(String A) {

				updateback usersetting_updateback = GsonUtils.parseJson(A,
						updateback.class);
				int a = usersetting_updateback.getStatusMsg();
				if (a == 1) {
					Log.e("PhoneLoginActivity", "���³ɹ�" + A);
				}
			}

			@Override
			public void defail(Object B) {

			}
		});

	}

	// �û����е�¼
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

	// ����ֻ�δע�ᵯ���Ի������ѡ��
	private void NiftyDialogBuilder() {
		// final NiftyDialogBuilder niftyDialogBuilder = NiftyDialogBuilder
		// .getInstance(this);
		// Effectstype effectstype = Effectstype.Shake;
		// niftyDialogBuilder.withTitle("��ʾ").withTitleColor("#000000")
		// // ���ñ���������ɫ
		// .withDividerColor("#ffffff")
		// // ���öԻ��򱳾���ɫ
		// .withMessage("��¼ʧ�ܣ����ֻ���δע��!"+"\n"+"�Ƿ����ע�᣿")
		// // �Ի�����ʾ����
		// .withMessageColor("#000000")
		// // ��ʾ����������ɫ
		// .withIcon(getResources().getDrawable(R.drawable.about_us))
		// // ���öԻ�����ʾͼƬ
		// .isCancelableOnTouchOutside(true).withDuration(700)
		// // ����ʱ��
		// .withEffect(effectstype).withButton1Text("ȡ��")
		// .withButton2Text("ȷ��").setButton1Click(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// niftyDialogBuilder.cancel();
		// finish();
		// //�رյ�¼����
		// RefreshActivity.activList_3.get(1).finish();
		// }
		// }).setButton2Click(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// //��ȡͼƬǩ���ַ���
		// phoneLoginInterface.getPicSign(PhoneLoginActivity.this, new User());
		// phoneLoginInterface.setPostListener(new getPicSignListenner() {
		//
		// @Override
		// public void success(String A) {
		// Log.e("PhoneLoginActivity", "ǩ���ַ�����" + A);
		// PicSignBack picSignBack = GsonUtils.parseJson(A,PicSignBack.class);
		// String returnData = picSignBack.getReturnData();
		// RegisteredActivity.setSIGN(returnData);
		//
		// //����ע��
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
		sd.setTitleText("��ʾ");
		sd.setContentText("��¼ʧ�ܣ����ֻ���δע��!" + "\n" + "�Ƿ����ע�᣿");
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
				phoneLoginInterface.getPicSign(PhoneLoginActivity.this,new User());
				phoneLoginInterface.setPostListener(new getPicSignListenner() {

					@Override
					public void success(String A) {
						Log.e("PhoneLoginActivity", "ǩ���ַ�����" + A);
						PicSignBack picSignBack = GsonUtils.parseJson(A,PicSignBack.class);
						String returnData = picSignBack.getReturnData();
						RegisteredActivity.setSIGN(returnData);

						// ����ע��
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
		findViewById(R.id.PhoneLogin_back).setOnClickListener(this);// �ر�
		mPhoneLogin_before_layout = (RelativeLayout) findViewById(R.id.PhoneLogin_before_layout);// ������֤��ǰ����
		mPhoneLogin_phone = (EditText) findViewById(R.id.PhoneLogin_phone);// �����ֻ�����
		mPhoneLogin_phone.setInputType(EditorInfo.TYPE_CLASS_PHONE);// �����ֻ�����ʱֱ�ӵ������ּ���
		mPhoneLogin_send = (TextView) findViewById(R.id.PhoneLogin_send);// ������֤��
		mPhoneLogin_send.setOnClickListener(this);
		mPhoneLogin_after_layout = (RelativeLayout) findViewById(R.id.PhoneLogin_after_layout);// ������֤��֮�󲼾�
		mPhoneLogin_code = (EditText) findViewById(R.id.PhoneLogin_code);// ������֤��
		mPhoneLogin_code.setInputType(EditorInfo.TYPE_CLASS_PHONE);// ������֤��ʱֱ�ӵ������ּ���
		mPhoneLogin_OK = (TextView) findViewById(R.id.PhoneLogin_OK);// �����֤
		mPhoneLogin_OK.setOnClickListener(this);
		mPhoneLogin_phone();
	}

	private void mPhoneLogin_phone() {
		mPhoneLogin_phone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				phone = s.toString();
				Log.e("PhoneLoginActivity", "���õ��ĵ绰����phone======" + phone);
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
			Log.e("PhoneLoginActivity", "���õ�����֤��1111======" + code_1);
			Log.e("PhoneLoginActivity", "���õ�����֤��222222======"+ phoneLogin_codeback);
			if (Integer.valueOf(code_1).equals(phoneLogin_codeback)) {
				User user = new User();
				user.setPhone(phoneLogin_phone);
				phoneLoginInterface.findUser(PhoneLoginActivity.this, user);

			} else {
				Toast.makeText(PhoneLoginActivity.this, "��֤�����",Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void PhoneLogin_send() {
		if (phone == null) {
			Toast.makeText(PhoneLoginActivity.this, "��������绰����",Toast.LENGTH_SHORT).show();
		} else {
			if (phone.length() == 13) {
				String phone1 = phone.substring(0, 3);
				String phone2 = phone.substring(4, 8);
				String phone3 = phone.substring(9, 13);
				phoneLogin_phone = phone1 + phone2 + phone3;
				Log.e("PhoneLoginActivity", "���õ��ĵ绰����======" + phoneLogin_phone);
				Phone phoneLogin_1 = new Phone();
				phoneLogin_1.setPhone(phoneLogin_phone);
				phoneLoginInterface.requestVerCode(PhoneLoginActivity.this,phoneLogin_1);
			} else {
				Toast.makeText(PhoneLoginActivity.this, "�绰�����ʽ������������ȷ�ĵ绰����",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void PhoneLogin_back() {
		finish();
	}

}
