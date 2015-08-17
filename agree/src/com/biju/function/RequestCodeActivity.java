package com.biju.function;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.BJ.javabean.Group;
import com.BJ.javabean.Group_Code;
import com.BJ.javabean.Group_User;
import com.BJ.javabean.Groupback;
import com.BJ.javabean.RequestCodeback;
import com.BJ.javabean.User;
import com.BJ.utils.SdPkUser;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.readUserGroupMsgListenner;
import com.biju.Interface.useRequestCode2JoinListenner;
import com.biju.Interface.userJoin2gourpListenner;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;

public class RequestCodeActivity extends Activity implements OnClickListener {

	private EditText mRequest_edt_code;
	private TextView mRequest_tv_code;
	private TextView mRequest_OK;
	private Interface requestcode_interface;
	private Integer sD_pk_user;
	private ArrayList<Group> readuesrlist = new ArrayList<Group>();
	private Integer pk_group;
	private Group readhomeuser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_code);
		//��SD���л�ȡ����Pk_user
		sD_pk_user = SdPkUser.getsD_pk_user();
		Log.e("RequestCodeActivity", "��SD���л�ȡ����Pk_user" + sD_pk_user);
		
		initUI();
		initInterface();
	}

	private void initInterface() {
		requestcode_interface = Interface.getInstance();
		requestcode_interface.setPostListener(new useRequestCode2JoinListenner() {

			@Override
			public void success(String A) {
				Log.e("RequestCodeActivity", "ʹ������������С��" + A);
				Groupback requestcode = GsonUtils.parseJson(A, Groupback.class);
				int StatusMsg = requestcode.getStatusMsg();
				if (StatusMsg == 1) {
					List<Group> users = requestcode.getReturnData();
					if(users.size()>0)
					{
						readhomeuser = users.get(0);
						//�����Ƿ�����ӹ���С��
						User homeuser = new User();
						homeuser.setPk_user(sD_pk_user);
						requestcode_interface.readUserGroupMsg(RequestCodeActivity.this, homeuser);
					}
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
		
		//��ȡ�û�С����Ϣʹ����������Ӻ�ļ���
		requestcode_interface.setPostListener(new userJoin2gourpListenner() {

			@Override
			public void success(String A) {
				RequestCodeback requestCodeback=GsonUtils.parseJson(A, RequestCodeback.class);
				Integer status=requestCodeback.getStatusMsg();
				if(status==1)
				{
					Log.e("RequestCodeActivity", "��ȡ�û�С����Ϣʹ����������Ӻ��===" + A);
					SharedPreferences requestcode_sp=getSharedPreferences(IConstant.RequestCode, 0);
					Editor editor=requestcode_sp.edit();
					editor.putBoolean(IConstant.Refresh, true);
					editor.commit();
					finish();
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
		
		//��ȡ�����û�С����ϢȻ������ж��Ƿ������
		requestcode_interface.setPostListener(new readUserGroupMsgListenner() {

			@Override
			public void success(String A) {
				Groupback homeback = GsonUtils.parseJson(A, Groupback.class);
				int homeStatusMsg = homeback.getStatusMsg();
				if (homeStatusMsg == 1) {
					Log.e("RequestCodeActivity", "��ȡ�����û�С����Ϣ==========" + A);
					List<Group> users = homeback.getReturnData();
					if (users.size() > 0) {
						for (int i = 0; i < users.size(); i++) {
							Group readhomeuser_1 = users.get(i);
							readuesrlist.add(readhomeuser_1);
						}
					}
					for (int i = 0; i < readuesrlist.size(); i++) {
						pk_group = readuesrlist.get(i).getPk_group();
						if (String.valueOf(pk_group).equals(String.valueOf(readhomeuser.getPk_group()))) {
							Toast.makeText(RequestCodeActivity.this, "�Ѿ��������С��",Toast.LENGTH_SHORT).show();
						}else
						{
							Integer fk_group=readhomeuser.getPk_group();
							Group_User group_User = new Group_User();
							group_User.setFk_group(fk_group);
							group_User.setFk_user(sD_pk_user);
							group_User.setRole(2);
							group_User.setStatus(1);
							requestcode_interface.userJoin2gourp(RequestCodeActivity.this, group_User);
						}
					}
				}

			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	private void initUI() {
		mRequest_tv_code = (TextView) findViewById(R.id.request_tv_code);
		mRequest_tv_code.setOnClickListener(this);
		mRequest_edt_code = (EditText) findViewById(R.id.request_edt_code);// ������֤��
		mRequest_OK = (TextView) findViewById(R.id.request_OK);// ���
		mRequest_OK.setOnClickListener(this);
		mRequest_edt_code.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if ("".equals(s.toString())) {
					mRequest_OK.setVisibility(View.GONE);
				} else {
					mRequest_OK.setVisibility(View.VISIBLE);
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
		getMenuInflater().inflate(R.menu.request_code, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.request_tv_code:
			request_tv_code();
			break;
		case R.id.request_OK:
			request_OK();
			break;
		default:
			break;
		}
	}

	private void request_OK() {
		String pk_group_code = mRequest_edt_code.getText().toString().trim();
		Group_Code group_Code = new Group_Code();
		group_Code.setPk_group_code(pk_group_code);
		requestcode_interface.useRequestCode2Join(RequestCodeActivity.this,group_Code);
	}

	private void request_tv_code() {
		mRequest_tv_code.setVisibility(View.GONE);
		mRequest_edt_code.setVisibility(View.VISIBLE);
		mRequest_OK.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return super.onTouchEvent(event);
	}
}
