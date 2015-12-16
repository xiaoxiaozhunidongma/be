package com.biju.function;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.BJ.javabean.Group;
import com.BJ.javabean.GroupHome;
import com.BJ.javabean.GroupNumber;
import com.BJ.javabean.GroupNumberback;
import com.BJ.javabean.Group_Code;
import com.BJ.javabean.Groupback;
import com.BJ.javabean.User;
import com.BJ.utils.InitPkUser;
import com.BJ.utils.SdPkUser;
import com.BJ.utils.ToastUtils;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.readUserGroupMsgListenner;
import com.biju.Interface.useRequestCode2JoinListenner;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;

@SuppressLint("NewApi")
public class RequestCodeActivity extends Activity implements OnClickListener{

	public static InterActivity interActivity;
	private boolean isAdd;
	private Interface requestcode_interface;
	private ArrayList<GroupHome> requestcode_readuesrlist = new ArrayList<GroupHome>();
	private GroupNumber requestcode_readhomeuser;
	private int toastHeight;
	private Integer group_count;
	private Integer init_pk_user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_code);
		findViewById(R.id.Requestcode2_back).setOnClickListener(this);
		findViewById(R.id.Requestcode2_back_layout).setOnClickListener(this);// ����
		initActivity();
		SdPkUser.setRequestcode(true);//˵���Ǵ���������ô��ȥ�ģ������ǰ��ֻ�
		DisplayMetrics();//��ȡ��Ļ�ĸ߶ȺͿ��
	}

	@Override
	protected void onRestart() {
		initActivity();
		SdPkUser.setRequestcode(true);//˵���Ǵ���������ô��ȥ�ģ������ǰ��ֻ�
		DisplayMetrics();//��ȡ��Ļ�ĸ߶ȺͿ��
		super.onRestart();
	}
	
	private void DisplayMetrics() {
		com.BJ.utils.DisplayMetrics.DisplayMetrics(RequestCodeActivity.this);
		int height = com.BJ.utils.DisplayMetrics.Height();
        toastHeight = height/4;
	}

	private void initInterface() {
		requestcode_interface = Interface.getInstance();
		requestcode_interface.setPostListener(new useRequestCode2JoinListenner() {

					@Override
					public void success(String A) {
						Log.e("RequestCodeActivity", "ʹ������������С��" + A);
						GroupNumberback requestcode = GsonUtils.parseJson(A,GroupNumberback.class);
						int StatusMsg = requestcode.getStatusMsg();
						if (StatusMsg == 1) {
							List<GroupNumber> users = requestcode.getReturnData();
							if (users.size() > 0) {
								requestcode_readhomeuser = users.get(0);
								group_count = requestcode_readhomeuser.getGroup_count();
								// �����Ƿ�����ӹ���С��
								User homeuser = new User();
								homeuser.setPk_user(init_pk_user);
								Log.e("RequestCodeActivity", "sD_pk_user=====" +init_pk_user);
								requestcode_interface.readUserGroupMsg(RequestCodeActivity.this, homeuser);
							}
						}else
						{
							//�Զ���Toast
							View toastRoot = getLayoutInflater().inflate(R.layout.my_error_toast, null);
							ToastUtils.ShowMsg(getApplicationContext(), "δ�ҵ��������", toastHeight, toastRoot);
							
						}
					}

					@Override
					public void defail(Object B) {

					}
				});


		// ��ȡ�����û�С����ϢȻ������ж��Ƿ������
		requestcode_interface.setPostListener(new readUserGroupMsgListenner() {

			@Override
			public void success(String A) {
				Groupback homeback = GsonUtils.parseJson(A, Groupback.class);
				int homeStatusMsg = homeback.getStatusMsg();
				if (homeStatusMsg == 1) {
					Log.e("RequestCodeActivity", "��ȡ�����û�С����Ϣ==========" + A);
					List<GroupHome> users = homeback.getReturnData();
					if (users.size() > 0) {
						for (int i = 0; i < users.size(); i++) {
							GroupHome readhomeuser_1 = users.get(i);
							requestcode_readuesrlist.add(readhomeuser_1);
						}
					}
					for (int i = 0; i < requestcode_readuesrlist.size(); i++) {
						Integer pk_group = requestcode_readuesrlist.get(i).getPk_group();
						if (String.valueOf(pk_group).equals(String.valueOf(requestcode_readhomeuser.getPk_group()))) {
							isAdd = true;
						}
					}
					if (isAdd) {
						//�Զ���Toast
						View toastRoot = getLayoutInflater().inflate(R.layout.my_prompt_toast, null);
						ToastUtils.ShowMsg(getApplicationContext(), "�Ѿ��������С��", toastHeight, toastRoot);
					} else {
						FindSuccess();
					}
				}else {
					FindSuccess();//���ҳɹ�
				}

			}

			private void FindSuccess() {
				Intent intent=new Intent(RequestCodeActivity.this, RequestCode3Activity.class);
				intent.putExtra(IConstant.Requestcode_readhomeuser, requestcode_readhomeuser);
				intent.putExtra("RequestCodeNumber", group_count);
				RequestCodeActivity.this.startActivity(intent);
				//�Զ���Toast
				View toastRoot = getLayoutInflater().inflate(R.layout.my_toast, null);
				ToastUtils.ShowMsg(getApplicationContext(), "�ҵ�С��", toastHeight, toastRoot);
			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	@SuppressWarnings("static-access")
	private void initActivity() {
		InterActivity interActivity = new InterActivity(){

			@Override
			public void startActivity() {
				isAdd=false;
				init_pk_user = InitPkUser.InitPkUser();
				initInterface();
				String code=SdPkUser.getCode;
				Group_Code group_Code = new Group_Code();
				group_Code.setPk_group_code(code);
				requestcode_interface.useRequestCode2Join(RequestCodeActivity.this, group_Code);
			}
			
		};
		this.interActivity=interActivity;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Requestcode2_back:
		case R.id.Requestcode2_back_layout:
			Requestcode2_back();
			break;

		default:
			break;
		}
	}

	private void Requestcode2_back() {
		finish();
	}

	public interface InterActivity
	{
		void startActivity();
	}
	
	@Override
	protected void onStop() {
		SdPkUser.setRequestcode(false);//�˳�����ʱ��false,��ֹ����4λ������ʱͻȻ�˳��ý���Ҳ�ᵯ��Toast
		super.onStop();
	}
	//��back�����м���
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			SdPkUser.setRequestcode(false);//�˳�����ʱ��false,��ֹ����4λ������ʱͻȻ�˳��ý���Ҳ�ᵯ��Toast
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
