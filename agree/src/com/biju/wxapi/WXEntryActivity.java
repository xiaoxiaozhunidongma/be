package com.biju.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.Toast;

import com.BJ.javabean.Access_Token;
import com.BJ.javabean.User;
import com.BJ.javabean.updateback;
import com.BJ.utils.SdPkUser;
import com.android.volley.VolleyError;
import com.biju.Interface;
import com.biju.R;
import com.biju.APP.MyApplication;
import com.biju.Interface.updateUserListenner;
import com.biju.function.UserSettingActivity;
import com.github.volley_examples.app.MyVolley;
import com.github.volley_examples.app.VolleyListenner;
import com.github.volley_examples.utils.GsonUtils;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	@SuppressWarnings("unused")
	private Context context = WXEntryActivity.this;
	private String code;
	private boolean weixinLogin;
	private Interface mWeiXinInterface;

	@SuppressWarnings("unused")
	private void handleIntent(Intent paramIntent) {
		MyApplication.api.handleIntent(paramIntent, this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wxentry);
		handleIntent(getIntent());
		initInterface();
	}

	private void initInterface() {
		mWeiXinInterface = Interface.getInstance();
		mWeiXinInterface.setPostListener(new updateUserListenner() {

			@Override
			public void success(String A) {
				// 更新用户资料成功
				updateback usersetting_updateback = GsonUtils.parseJson(A,
						updateback.class);
				int a = usersetting_updateback.getStatusMsg();
				if (a == 1) {
					Log.e("WXEntryActivity", "更新成功" + A);
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wxentry, menu);
		return true;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		handleIntent(intent);
	}

	@Override
	public void onReq(BaseReq arg0) {
		finish();
	}

	@Override
	public void onResp(BaseResp resp) {

		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			code = ((SendAuth.Resp) resp).code;
			Log.e("WXEntryActivity", "获取的code======" + code);
			initdata();
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			finish();
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			finish();
			break;
		default:
			break;
		}
		finish();
	}

	private void initdata() {
		String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx9be30a70fcb480ae&secret=5afb616b4c62f245508643e078735bfb&code="
				+ code + "&grant_type=authorization_code";
		Log.e("WXEntryActivity", "路径===============" + path);

		MyVolley.get(WXEntryActivity.this, path, new VolleyListenner() {

			@Override
			public void onErrorResponse(VolleyError error) {
			}

			@Override
			public void onResponse(String response) {
				Log.e("WXEntryActivity", "获取回来的东西======" + response);
				Access_Token access_Token = GsonUtils.parseJson(response,Access_Token.class);
				String openid=access_Token.getOpenid();
				Log.e("WXEntryActivity", "返回来的openid========"+openid);
				if(!("".equals(openid)))
				{
					boolean Binding_weixin=SdPkUser.isGetweixinBinding();
					if(Binding_weixin)
					{
						//获取SD卡中的pk_user
						Integer SD_pk_user = SdPkUser.getsD_pk_user();
						User user=SdPkUser.getUser;
						
						User usersetting = new User();
						usersetting.setPk_user(SD_pk_user);
						usersetting.setJpush_id(user.getJpush_id());
						usersetting.setNickname(user.getNickname());
						usersetting.setPassword(user.getPassword());
						usersetting.setSex(user.getSex());
						usersetting.setStatus(1);
						usersetting.setPhone(user.getPhone());
						usersetting.setSetup_time(user.getSetup_time());
						usersetting.setLast_login_time(user.getLast_login_time());
						usersetting.setAvatar_path(user.getAvatar_path());
						usersetting.setWechat_id(openid);//微信的唯一识别码
						Log.e("WXEntryActivity", "第二次得到的用户信息2222222===="+SD_pk_user+"\n"+user.getJpush_id()+"\n"+
								user.getNickname()+"\n"+user.getPassword()+"\n"+user.getSex()+"\n"+user.getPhone()+"\n"+user.getSetup_time()+"\n"
								+user.getLast_login_time()+"\n"+user.getAvatar_path()+"\n"+openid);
						mWeiXinInterface.updateUser(WXEntryActivity.this, usersetting);
					}
				}else
				{
					Toast.makeText(WXEntryActivity.this, "绑定失败", Toast.LENGTH_SHORT).show();
				}
			}
		});

	}
}
