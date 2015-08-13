package com.biju.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.BJ.utils.SdPkUser;
import com.android.volley.VolleyError;
import com.biju.R;
import com.biju.APP.MyApplication;
import com.biju.function.UserSettingActivity;
import com.github.volley_examples.app.MyVolley;
import com.github.volley_examples.app.VolleyListenner;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	@SuppressWarnings("unused")
	private Context context = WXEntryActivity.this;
	private String code;
	private boolean weixinLogin;

	@SuppressWarnings("unused")
	private void handleIntent(Intent paramIntent) {
		MyApplication.api.handleIntent(paramIntent, this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wxentry);
		handleIntent(getIntent());
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
			weixinLogin = SdPkUser.isGetweixinLogin();
			code = ((SendAuth.Resp) resp).code;
			initdata();
			Log.e("WXEntryActivity", "获取的code======"+code);
			if(!weixinLogin)
			{
				if(code!=null)
				{
					Log.e("WXEntryActivity", "有进入到这来了111111========");
					Intent intent=new Intent(WXEntryActivity.this, UserSettingActivity.class);
					startActivity(intent);
					overridePendingTransition(0, 0);
				}
			}
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			if(!weixinLogin)
			{
				Log.e("WXEntryActivity", "有进入到这来了222222222========");
				Intent intent=new Intent(WXEntryActivity.this, UserSettingActivity.class);
				startActivity(intent);
				overridePendingTransition(0, 0);
			}
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			break;
		default:
			break;
		}
		finish();
		}

	private void initdata() {
		String path="https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx9be30a70fcb480ae&secret=5afb616b4c62f245508643e078735bfb&code="+code+"&grant_type=authorization_code";
		Log.e("WXEntryActivity", "路径==============="+path);
		
		MyVolley.get(WXEntryActivity.this, path, new VolleyListenner() {
			
			@Override
			public void onErrorResponse(VolleyError error) {
			}
			
			@Override
			public void onResponse(String response) {
				Log.e("WXEntryActivity", "获取回来的东西======"+response);
			}
		});
	
	}
}
