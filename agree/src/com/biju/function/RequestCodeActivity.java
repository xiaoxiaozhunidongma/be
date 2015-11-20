package com.biju.function;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.BJ.javabean.Group;
import com.BJ.javabean.GroupNumber;
import com.BJ.javabean.GroupNumberback;
import com.BJ.javabean.Group_Code;
import com.BJ.javabean.Groupback;
import com.BJ.javabean.User;
import com.BJ.utils.SdPkUser;
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
	private Integer sD_pk_user;
	private ArrayList<Group> requestcode_readuesrlist = new ArrayList<Group>();
	private GroupNumber requestcode_readhomeuser;
	private int toastHeight;
	private Integer group_count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_code);
		findViewById(R.id.Requestcode2_back).setOnClickListener(this);
		findViewById(R.id.Requestcode2_back_layout).setOnClickListener(this);// 返回
		initActivity();
		SdPkUser.setRequestcode(true);//说明是从邀请码这么过去的，而不是绑定手机
		DisplayMetrics();//获取屏幕的高度和宽度
	}

	private void DisplayMetrics() {
		android.util.DisplayMetrics metric = new android.util.DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
//        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）
        toastHeight = height/4;
	}

	private void initInterface() {
		requestcode_interface = Interface.getInstance();
		requestcode_interface.setPostListener(new useRequestCode2JoinListenner() {

					@Override
					public void success(String A) {
						Log.e("RequestCodeActivity", "使用邀请码加入的小组" + A);
						GroupNumberback requestcode = GsonUtils.parseJson(A,GroupNumberback.class);
						int StatusMsg = requestcode.getStatusMsg();
						if (StatusMsg == 1) {
							List<GroupNumber> users = requestcode.getReturnData();
							if (users.size() > 0) {
								requestcode_readhomeuser = users.get(0);
								group_count = requestcode_readhomeuser.getGroup_count();
								// 查找是否已添加过该小组
								User homeuser = new User();
								homeuser.setPk_user(sD_pk_user);
								Log.e("RequestCodeActivity", "sD_pk_user=====" +sD_pk_user);
								requestcode_interface.readUserGroupMsg(RequestCodeActivity.this, homeuser);
							}
						}else
						{
							//自定义Toast
							View toastRoot = getLayoutInflater().inflate(R.layout.my_error_toast, null);
							Toast toast=new Toast(getApplicationContext());
							toast.setGravity(Gravity.TOP, 0, toastHeight);
							toast.setView(toastRoot);
							toast.setDuration(100);
							TextView tv=(TextView)toastRoot.findViewById(R.id.TextViewInfo);
							tv.setText("未找到相关数据");
							toast.show();
							
							
						}
					}

					@Override
					public void defail(Object B) {

					}
				});


		// 读取出的用户小组信息然后进行判断是否已添加
		requestcode_interface.setPostListener(new readUserGroupMsgListenner() {

			@Override
			public void success(String A) {
				Groupback homeback = GsonUtils.parseJson(A, Groupback.class);
				int homeStatusMsg = homeback.getStatusMsg();
				if (homeStatusMsg == 1) {
					Log.e("RequestCodeActivity", "读取出的用户小组信息==========" + A);
					List<Group> users = homeback.getReturnData();
					if (users.size() > 0) {
						for (int i = 0; i < users.size(); i++) {
							Group readhomeuser_1 = users.get(i);
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
						//自定义Toast
						View toastRoot = getLayoutInflater().inflate(R.layout.my_prompt_toast, null);
						Toast toast=new Toast(getApplicationContext());
						toast.setGravity(Gravity.TOP, 0, toastHeight);
						toast.setView(toastRoot);
						toast.setDuration(100);
						TextView tv=(TextView)toastRoot.findViewById(R.id.TextViewInfo);
						tv.setText("已经加入过该小组");
						toast.show();
					} else {
						FindSuccess();
					}
				}else {
					FindSuccess();//查找成功
				}

			}

			private void FindSuccess() {
				Intent intent=new Intent(RequestCodeActivity.this, RequestCode3Activity.class);
				intent.putExtra(IConstant.Requestcode_readhomeuser, requestcode_readhomeuser);
				intent.putExtra("RequestCodeNumber", group_count);
				RequestCodeActivity.this.startActivity(intent);
				//自定义Toast
				View toastRoot = getLayoutInflater().inflate(R.layout.my_toast, null);
				Toast toast=new Toast(getApplicationContext());
				toast.setGravity(Gravity.TOP, 0, toastHeight);
				toast.setView(toastRoot);
				toast.setDuration(100);
				TextView tv=(TextView)toastRoot.findViewById(R.id.TextViewInfo);
				tv.setText("找到小组");
				toast.show();
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
				sD_pk_user = SdPkUser.getsD_pk_user();
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
		SdPkUser.setRequestcode(false);//退出界面时传false,防止输入4位邀请码时突然退出该界面也会弹出Toast
		super.onStop();
	}
	//对back键进行监听
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			SdPkUser.setRequestcode(false);//退出界面时传false,防止输入4位邀请码时突然退出该界面也会弹出Toast
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
