package com.biju.function;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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
//	private RelativeLayout mBinding_phone_OK_layout;
	private int toastHeight;
	public static GetBindingPhone bindingPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_binding_phone);
		DisplayMetrics();
		Intent intent = getIntent();
		user = (User) intent.getSerializableExtra(IConstant.UserData);
		initUI();
		initInterface();
		OKBindingPhone();//�ص�
	}
	
	@SuppressWarnings("static-access")
	private void OKBindingPhone() {
		GetBindingPhone bindingPhone=new GetBindingPhone(){

			@Override
			public void BindingPhone() {
				binding_phone_OK();
			}
			
		};
		this.bindingPhone=bindingPhone;
	}

	private void DisplayMetrics() {
		android.util.DisplayMetrics metric = new android.util.DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
//        int width = metric.widthPixels;     // ��Ļ��ȣ����أ�
        int height = metric.heightPixels;   // ��Ļ�߶ȣ����أ�
        toastHeight = height/4;
	}

	private void initInterface() {
		mBinding_phone_interface = Interface.getInstance();
		mBinding_phone_interface.setPostListener(new requestVerCodeListenner() {

			@Override
			public void success(String A) {
				Codeback codeback = GsonUtils.parseJson(A, Codeback.class);
				Integer code_statusmsg = codeback.getStatusMsg();
				if (code_statusmsg == 1) {
					Log.e("BindingPhoneActivity", "������֤���Ƿ��ͳɹ�======" + A);
					Code code = (Code) codeback.getReturnData();
					mBinding_phone_codeback = code.getCode();
					Log.e("BindingPhoneActivity","������֤���Ƿ��ͳɹ�======" + code.getCode());
					mBinding_phone_before.setVisibility(View.GONE);// �����ֻ������������
					mBinding_phone_after.setVisibility(View.VISIBLE);// ������֤�������ʾ

					mBinding_phone_send_layout.setVisibility(View.GONE);// �����ֻ����밴ť����
//					mBinding_phone_OK_layout.setVisibility(View.VISIBLE);// ������֤�밴ť��ʾ
					// �������뷨
//					InputMethodManager imm = (InputMethodManager) mBinding_phone_send_layout
//							.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//					imm.hideSoftInputFromWindow(mBinding_phone_send_layout.getWindowToken(), 0);
				}
			}

			@Override
			public void defail(Object B) {
			}
		});

		mBinding_phone_interface.setPostListener(new updateUserListenner() {

			@Override
			public void success(String A) {
				// �����û����ϳɹ�
				updateback usersetting_updateback = GsonUtils.parseJson(A,updateback.class);
				int a = usersetting_updateback.getStatusMsg();
				if (a == 1) {
					Log.e("UserSettingActivity", "���³ɹ�" + A);
					finish();
				} else {
					Toast.makeText(BindingPhoneActivity.this, "��ʧ�ܣ������°�",Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void defail(Object B) {

			}
		});

	}

	private void initUI() {
		findViewById(R.id.binding_phone_back_layout).setOnClickListener(this);
		findViewById(R.id.binding_phone_back).setOnClickListener(this);// ����
		
		mBinding_phone_phone = (EditText) findViewById(R.id.binding_phone_phone);// �����ֻ���
		mBinding_phone_phone.setInputType(EditorInfo.TYPE_CLASS_PHONE);// ����绰����ʱֱ�ӵ������ּ���
		mBinding_phone_phone.setFocusable(true);
		mBinding_phone_phone.setFocusableInTouchMode(true);
		mBinding_phone_phone.requestFocus();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				InputMethodManager inputManager = (InputMethodManager) mBinding_phone_phone
						.getContext().getSystemService(NewteamActivity.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(mBinding_phone_phone, 0);
			}
		}, 998);
		
		
		findViewById(R.id.binding_phone_send).setOnClickListener(this);// �����ֻ�����
		mBinding_phone_send_layout = (RelativeLayout) findViewById(R.id.binding_phone_send_layout);
		mBinding_phone_send_layout.setOnClickListener(this);
		
		mBinding_phone_before = (RelativeLayout) findViewById(R.id.binding_phone_before);// ����ǰ
		mBinding_phone_after = (RelativeLayout) findViewById(R.id.binding_phone_after);// ���ͺ�
		
//		findViewById(R.id.binding_phone_OK).setOnClickListener(this);
//		mBinding_phone_OK_layout = (RelativeLayout) findViewById(R.id.binding_phone_OK_layout);
//		mBinding_phone_OK_layout.setOnClickListener(this);// �����֤����֤
		
		TextView mBinding_phone_prompt = (TextView) findViewById(R.id.binding_phone_prompt);
		mBinding_phone_prompt.setText("С��ʾ:" + "\n" + "�ֻ�����Ϊ11λ������," + "\n"+ "����Ҫ�ں���ǰ����0����+86");
		mBinding_phone_phone_listener();
	}

	private void mBinding_phone_phone_listener() {
		mBinding_phone_phone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,int count) {
				phone = s.toString();
				Log.e("BindingPhoneActivity", "���õ��ĵ绰����phone======" + phone);
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.binding_phone_send:
		case R.id.binding_phone_send_layout:
			binding_phone_send();
			break;
//		case R.id.binding_phone_OK:
//		case R.id.binding_phone_OK_layout:
//			binding_phone_OK();
//			break;
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
		overridePendingTransition(R.anim.left, R.anim.right);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			binding_phone_back();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	public interface GetBindingPhone
	{
		void BindingPhone();
	}
	
	private void binding_phone_OK() {
		if (isagain) {
			Phone binding_phone_1 = new Phone();
			binding_phone_1.setPhone(binding_phone);
			mBinding_phone_interface.requestVerCode(BindingPhoneActivity.this,binding_phone_1);
		} else {
			String code_1 = SdPkUser.bindingphonecode;
			Log.e("BindingPhoneActivity", "���õ�����֤��1111======" + code_1);
			Log.e("BindingPhoneActivity", "���õ�����֤��222222======"+ mBinding_phone_codeback);
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
				//�Զ���Toast
				View toastRoot = getLayoutInflater().inflate(R.layout.my_error_toast, null);
				Toast toast=new Toast(getApplicationContext());
				toast.setGravity(Gravity.TOP, 0, toastHeight);
				toast.setView(toastRoot);
				toast.setDuration(100);
				TextView tv=(TextView)toastRoot.findViewById(R.id.TextViewInfo);
				tv.setText("��֤�����,����������");
				toast.show();
			}
		}
	}

	private void binding_phone_send() {
		if (phone == null) {
			//�Զ���Toast
			View toastRoot = getLayoutInflater().inflate(R.layout.my_prompt_toast, null);
			Toast toast=new Toast(getApplicationContext());
			toast.setGravity(Gravity.TOP, 0, toastHeight);
			toast.setView(toastRoot);
			toast.setDuration(50);
			TextView tv=(TextView)toastRoot.findViewById(R.id.TextViewInfo);
			tv.setText("��������绰���룡");
			toast.show();
		} else {
			String phone1 = phone.substring(0, 3);
			String phone2 = phone.substring(4, 8);
			String phone3 = phone.substring(9, 13);
			binding_phone = phone1 + phone2 + phone3;
			Log.e("BindingPhoneActivity", "���õ��ĵ绰����======" + binding_phone);
			Phone binding_phone_1 = new Phone();
			binding_phone_1.setPhone(binding_phone);
			mBinding_phone_interface.requestVerCode(BindingPhoneActivity.this,binding_phone_1);
			
			//�Զ���Toast
			View toastRoot = getLayoutInflater().inflate(R.layout.my_toast, null);
			Toast toast=new Toast(getApplicationContext());
			toast.setGravity(Gravity.TOP, 0, toastHeight);
			toast.setView(toastRoot);
			toast.setDuration(50);
			TextView tv=(TextView)toastRoot.findViewById(R.id.TextViewInfo);
			tv.setText("��֤�뷢�ͳɹ���");
			toast.show();
		}
	}

}
