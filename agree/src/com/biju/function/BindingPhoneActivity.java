package com.biju.function;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.BJ.javabean.Code;
import com.BJ.javabean.Codeback;
import com.BJ.javabean.Phone;
import com.BJ.javabean.User;
import com.BJ.javabean.updateback;
import com.BJ.utils.SdPkUser;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.requestVerCodeListenner;
import com.biju.Interface.updateUserListenner;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;

@SuppressLint("ResourceAsColor")
public class BindingPhoneActivity extends Activity implements OnClickListener {

	private User user;
	private EditText mBinding_phone_phone;
	private RelativeLayout mBinding_phone_before;
	private RelativeLayout mBinding_phone_after;
	private Interface mBinding_phone_interface;
	private Integer mBinding_phone_codeback;
	private String phone;
	private String binding_phone;
	private boolean isagain;
	private RelativeLayout mBinding_phone_send_layout;
	private RelativeLayout mBinding_phone_OK_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_binding_phone);
		Intent intent = getIntent();
		user = (User) intent.getSerializableExtra(IConstant.UserData);
		initUI();
		initInterface();
	}

	private void initInterface() {
		mBinding_phone_interface = Interface.getInstance();
		mBinding_phone_interface.setPostListener(new requestVerCodeListenner() {

			@Override
			public void success(String A) {
				Codeback codeback = GsonUtils.parseJson(A, Codeback.class);
				Integer code_statusmsg = codeback.getStatusMsg();
				if (code_statusmsg == 1) {
					Log.e("BindingPhoneActivity", "返回验证码是否发送成功======" + A);
					Code code = (Code) codeback.getReturnData();
					mBinding_phone_codeback = code.getCode();
					Log.e("BindingPhoneActivity","返回验证码是否发送成功======" + code.getCode());
					mBinding_phone_before.setVisibility(View.GONE);// 输入手机号码界面隐藏
					mBinding_phone_after.setVisibility(View.VISIBLE);// 输入验证码界面显示

					mBinding_phone_send_layout.setVisibility(View.GONE);// 发送手机号码按钮隐藏
					mBinding_phone_OK_layout.setVisibility(View.VISIBLE);// 发送验证码按钮显示
					// 隐藏输入法
					InputMethodManager imm = (InputMethodManager) mBinding_phone_send_layout
							.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(mBinding_phone_send_layout.getWindowToken(), 0);
				}
			}

			@Override
			public void defail(Object B) {
			}
		});

		mBinding_phone_interface.setPostListener(new updateUserListenner() {

			@Override
			public void success(String A) {
				// 更新用户资料成功
				updateback usersetting_updateback = GsonUtils.parseJson(A,updateback.class);
				int a = usersetting_updateback.getStatusMsg();
				if (a == 1) {
					Log.e("UserSettingActivity", "更新成功" + A);
					finish();
				} else {
					Toast.makeText(BindingPhoneActivity.this, "绑定失败，请重新绑定",Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void defail(Object B) {

			}
		});

	}

	private void initUI() {
		findViewById(R.id.binding_phone_back_layout).setOnClickListener(this);
		findViewById(R.id.binding_phone_back).setOnClickListener(this);// 返回
		mBinding_phone_phone = (EditText) findViewById(R.id.binding_phone_phone);// 输入手机号
		mBinding_phone_phone.setInputType(EditorInfo.TYPE_CLASS_PHONE);// 点击电话号码时直接弹出数字键盘
		findViewById(R.id.binding_phone_send).setOnClickListener(this);// 发送手机号码
		mBinding_phone_send_layout = (RelativeLayout) findViewById(R.id.binding_phone_send_layout);
		mBinding_phone_send_layout.setOnClickListener(this);
		mBinding_phone_before = (RelativeLayout) findViewById(R.id.binding_phone_before);// 发送前
		mBinding_phone_after = (RelativeLayout) findViewById(R.id.binding_phone_after);// 发送后
		findViewById(R.id.binding_phone_OK).setOnClickListener(this);
		mBinding_phone_OK_layout = (RelativeLayout) findViewById(R.id.binding_phone_OK_layout);
		mBinding_phone_OK_layout.setOnClickListener(this);// 完成验证码验证
		TextView mBinding_phone_prompt = (TextView) findViewById(R.id.binding_phone_prompt);
		mBinding_phone_prompt.setText("小提示:" + "\n" + "手机号码为11位纯数字," + "\n"+ "不需要在号码前加上0或者+86");
		mBinding_phone_phone_listener();
	}

	private void mBinding_phone_phone_listener() {
		mBinding_phone_phone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,int count) {
				phone = s.toString();
				Log.e("BindingPhoneActivity", "所得到的电话号码phone======" + phone);
				if (phone == null) {

				} else {
					int length = phone.length();
					if (length == 4) {
						if (phone.substring(3).equals(new String(" "))) {
							phone = phone.substring(0, 3);
							mBinding_phone_phone.setText(phone);
							mBinding_phone_phone.setSelection(phone.length());
						} else {
							phone = phone.substring(0, 3) + " "+ phone.substring(3);
							mBinding_phone_phone.setText(phone);
							mBinding_phone_phone.setSelection(phone.length());
						}
					} else if (length == 9) {
						if (phone.substring(8).equals(new String(" "))) {
							phone = phone.substring(0, 8);
							mBinding_phone_phone.setText(phone);
							mBinding_phone_phone.setSelection(phone.length());
						} else {
							phone = phone.substring(0, 8) + " "+ phone.substring(8);
							mBinding_phone_phone.setText(phone);
							mBinding_phone_phone.setSelection(phone.length());
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
		getMenuInflater().inflate(R.menu.binding_phone, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.binding_phone_send:
		case R.id.binding_phone_send_layout:
			binding_phone_send();
			break;
		case R.id.binding_phone_OK:
		case R.id.binding_phone_OK_layout:
			binding_phone_OK();
			break;
		case R.id.binding_phone_back_layout:
		case R.id.binding_phone_back:
			binding_phone_back();
			break;
		default:
			break;
		}
	}

	private void binding_phone_back() {
		finish();
	}

	private void binding_phone_OK() {
		if (isagain) {
			Phone binding_phone_1 = new Phone();
			binding_phone_1.setPhone(binding_phone);
			mBinding_phone_interface.requestVerCode(BindingPhoneActivity.this,binding_phone_1);
		} else {
			String code_1 = SdPkUser.bindingphonecode;
			Log.e("BindingPhoneActivity", "所得到的验证码1111======" + code_1);
			Log.e("BindingPhoneActivity", "所得到的验证码222222======"+ mBinding_phone_codeback);
			if (Integer.valueOf(code_1).equals(mBinding_phone_codeback)) {
				User usersetting = new User();
				usersetting.setPk_user(user.getPk_user());
				usersetting.setJpush_id(user.getJpush_id());
				usersetting.setNickname(user.getNickname());
				usersetting.setPassword(user.getPassword());
				usersetting.setSex(user.getSex());
				usersetting.setPhone(binding_phone);
				usersetting.setSetup_time(user.getSetup_time());
				usersetting.setLast_login_time(user.getLast_login_time());
				usersetting.setAvatar_path(user.getAvatar_path());
				usersetting.setStatus(user.getStatus());
				usersetting.setWechat_id(user.getWechat_id());
				mBinding_phone_interface.updateUser(BindingPhoneActivity.this,usersetting);

			} else {
				Toast.makeText(BindingPhoneActivity.this, "验证码错误！",Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void binding_phone_send() {
		if (phone == null) {
			Toast.makeText(BindingPhoneActivity.this, "请先输入电话号码",Toast.LENGTH_SHORT).show();
		} else {
			String phone1 = phone.substring(0, 3);
			String phone2 = phone.substring(4, 8);
			String phone3 = phone.substring(9, 13);
			binding_phone = phone1 + phone2 + phone3;
			Log.e("BindingPhoneActivity", "所得到的电话号码======" + binding_phone);
			Phone binding_phone_1 = new Phone();
			binding_phone_1.setPhone(binding_phone);
			mBinding_phone_interface.requestVerCode(BindingPhoneActivity.this,binding_phone_1);

		}
	}

}
