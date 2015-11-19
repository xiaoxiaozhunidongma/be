package com.biju.function;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BJ.javabean.User;
import com.BJ.javabean.updateback;
import com.BJ.utils.ChineseOrEnglishTextWatcher;
import com.BJ.utils.SdPkUser;
import com.biju.Interface;
import com.biju.Interface.updateUserListenner;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;

public class NicknameActivity extends Activity implements OnClickListener{


	private EditText mNickname_nickname;
	public static RelativeLayout mNickname_warn_layout;
	private Integer sD_pk_user;
    private Interface nicknameInterface;

	public RelativeLayout getmNickname_warn_layout() {
		return mNickname_warn_layout;
	}

	@SuppressWarnings("static-access")
	public void setmNickname_warn_layout(RelativeLayout mNickname_warn_layout) {
		this.mNickname_warn_layout = mNickname_warn_layout;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nickname);
		sD_pk_user = SdPkUser.getsD_pk_user();
		initUI();
		initInterface();
	}

	private void initInterface() {
		nicknameInterface = Interface.getInstance();
		nicknameInterface.setPostListener(new updateUserListenner() {

			@Override
			public void success(String A) {
				// �����û����ϳɹ�
				updateback usersetting_updateback = GsonUtils.parseJson(A,updateback.class);
				int a = usersetting_updateback.getStatusMsg();
				if (a == 1) {
					Log.e("NicknameActivity", "���³ɹ�" + A);
					finish();
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	private void initUI() {
		findViewById(R.id.Nickname_back_layout).setOnClickListener(this);
		findViewById(R.id.Nickname_back).setOnClickListener(this);//����
		findViewById(R.id.Nickname_OK_layout).setOnClickListener(this);
		findViewById(R.id.Nickname_OK).setOnClickListener(this);//���
		mNickname_nickname = (EditText) findViewById(R.id.Nickname_nickname);//�޸��ǳ�
		TextView Nickname_prompt=(TextView) findViewById(R.id.Nickname_prompt);//��ʾ
		Nickname_prompt.setVisibility(View.VISIBLE);
		Nickname_prompt.setText("�û��������Ȳ���С��2���ַ�,"+"\n"+"���ֻ�ܰ���14���ַ�,����ʹ��Ӣ����ĸ,"+"\n"+"���ֻ������Ĺ���,���ǲ���ʹ��������ַ�,"+"\n"+"��:'[]:;|=,+#?<>'��,"+"\n"+"Ҳ����ʹ�ð����пո�");
		mNickname_nickname.addTextChangedListener((TextWatcher) new ChineseOrEnglishTextWatcher(mNickname_nickname,14));//��������14���ַ�
		mNickname_warn_layout = (RelativeLayout) findViewById(R.id.Nickname_warn_layout);//�ַ����ڻ�С��ʱ��ʾ
	
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Nickname_OK_layout:
		case R.id.Nickname_OK:
			Nickname_OK();
			break;
		case R.id.Nickname_back_layout:
		case R.id.Nickname_back:
			Nickname_back();
			break;
		default:
			break;
		}
	}

	//��ɽ��и����ǳ�
	private void Nickname_OK() {
		String nickname=mNickname_nickname.getText().toString().trim();
		User user=SdPkUser.getNicknameUser;
		
		User usersetting = new User();
		usersetting.setPk_user(sD_pk_user);
		usersetting.setJpush_id(user.getJpush_id());
		usersetting.setNickname(nickname);
		usersetting.setPassword(user.getPassword());
		usersetting.setSex(user.getSex());
		usersetting.setStatus(1);
		usersetting.setPhone(user.getPhone());
		usersetting.setWechat_id(user.getWechat_id());
		usersetting.setSetup_time(user.getSetup_time());
		usersetting.setLast_login_time(user.getLast_login_time());
		usersetting.setAvatar_path(user.getAvatar_path());
		nicknameInterface.updateUser(NicknameActivity.this, usersetting);

	}

	private void Nickname_back() {
		finish();
		overridePendingTransition(R.anim.left, R.anim.right);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			Nickname_back();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
