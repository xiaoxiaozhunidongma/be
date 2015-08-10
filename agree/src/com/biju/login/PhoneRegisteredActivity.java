package com.biju.login;

import java.util.ArrayList;
import java.util.List;

import com.BJ.javabean.Code;
import com.BJ.javabean.Codeback;
import com.BJ.javabean.Loginback;
import com.BJ.javabean.Phone;
import com.BJ.javabean.User;
import com.BJ.utils.ImageLoaderUtils;
import com.biju.Interface;
import com.biju.R;
import com.biju.Interface.findUserListenner;
import com.biju.Interface.requestVerCodeListenner;
import com.biju.function.BindingPhoneActivity;
import com.biju.function.FindFriendsActivity;
import com.github.volley_examples.utils.GsonUtils;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_registered);
		initUI();
		initInterface();
	}

	private void initInterface() {
		phoneRegisteredInterface = Interface.getInstance();
		phoneRegisteredInterface.setPostListener(new requestVerCodeListenner() {

			@Override
			public void success(String A) {
				Codeback codeback = GsonUtils.parseJson(A, Codeback.class);
				Integer code_statusmsg = codeback.getStatusMsg();
				if (code_statusmsg == 1) {
					Log.e("PhoneRegisteredActivity", "������֤���Ƿ��ͳɹ�======" + A);
					Code code = (Code) codeback.getReturnData();
					phoneRegistered_codeback = code.getCode();
					Log.e("PhoneRegisteredActivity","������֤���Ƿ��ͳɹ�======" + code.getCode());
					//����һ�κ���60s�ڲ����ٷ��͵ڶ���
					mPhoneRegistered_OK.post(new Runnable() {
						
						@Override
						public void run() {
							sum--;
							if (sum > 0) {
								mPhoneRegistered_OK.setText(sum + "");
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
									mPhoneRegistered_OK.setText("�����֤");
									mPhoneRegistered_OK.setEnabled(true);
									isagain=false;
								}else
								{
									mPhoneRegistered_OK.setText("������֤��");
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
		
		
		phoneRegisteredInterface.setPostListener(new findUserListenner() {

			@Override
			public void success(String A) {
				Loginback phoneRegistered_statusmsg = GsonUtils.parseJson(A,
						Loginback.class);
				int statusmsg = phoneRegistered_statusmsg.getStatusMsg();
				if (statusmsg == 1) {
					// ȡ��һ��Users[0]
					List<User> Users = phoneRegistered_statusmsg.getReturnData();
					if (Users.size() >= 1) {
						User user = Users.get(0);
						if(user.getPk_user()!=null)
						{
							//���е�¼
						}else
						{
							//����ע��
						}
					}
				} else {
					Toast.makeText(PhoneRegisteredActivity.this, "ע��ʧ�ܣ�������ע��!",
							Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public void defail(Object B) {

			}
		});

	}

	private void initUI() {
		findViewById(R.id.PhoneRegistered_back_layout).setOnClickListener(this);
		findViewById(R.id.PhoneRegistered_back).setOnClickListener(this);//�ر�
		mPhoneRegistered_before_layout = (RelativeLayout) findViewById(R.id.PhoneRegistered_before_layout);//������֤��ǰ����
		mPhoneRegistered_phone = (EditText) findViewById(R.id.PhoneRegistered_phone);//�����ֻ�����
		mPhoneRegistered_send = (TextView) findViewById(R.id.PhoneRegistered_send);//������֤��
		mPhoneRegistered_send.setOnClickListener(this);
		mPhoneRegistered_after_layout = (RelativeLayout) findViewById(R.id.PhoneRegistered_after_layout);//������֤��֮�󲼾�
		mPhoneRegistered_code = (EditText) findViewById(R.id.PhoneRegistered_code);//������֤��
		mPhoneRegistered_OK = (TextView) findViewById(R.id.PhoneRegistered_OK);//�����֤
		mPhoneRegistered_OK.setOnClickListener(this);
		mPhoneRegistered_phone();
	}

	private void mPhoneRegistered_phone() {
		mPhoneRegistered_phone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				phone = s.toString();
				Log.e("PhoneRegisteredActivity", "���õ��ĵ绰����phone======" + phone);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.phone_registered, menu);
		return true;
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
			Log.e("PhoneRegisteredActivity", "���õ�����֤��1111======" + code_1);
			Log.e("PhoneRegisteredActivity", "���õ�����֤��222222======"+ phoneRegistered_codeback);
			if (Integer.valueOf(code_1).equals(phoneRegistered_codeback)) {
				User user=new User();
				user.setPhone(phoneRegistered_phone);
				phoneRegisteredInterface.findUser(PhoneRegisteredActivity.this, user);
				
				
				
			} else {
				Toast.makeText(PhoneRegisteredActivity.this, "��֤�����",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void PhoneRegistered_send() {
		if (phone == null) {
			Toast.makeText(PhoneRegisteredActivity.this, "��������绰����",
					Toast.LENGTH_SHORT).show();
		} else {
			String phone1 = phone.substring(0, 3);
			String phone2 = phone.substring(4, 8);
			String phone3 = phone.substring(9, 13);
			phoneRegistered_phone = phone1 + phone2 + phone3;
			Log.e("PhoneRegisteredActivity", "���õ��ĵ绰����======" + phoneRegistered_phone);
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
