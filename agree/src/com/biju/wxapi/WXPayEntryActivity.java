package com.biju.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.biju.R;
import com.biju.pay.PayBaseActivity;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{

	 private IWXAPI api;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wxpay_entry);
		api = WXAPIFactory.createWXAPI(this, "wx2ffba147560de2ff",true);
        api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
		finish();
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.e("WXPayEntryActivity", "当前的resp.errCode========== " + resp.errCode);
		switch (resp.errCode) {
		case 0:
			PayBaseActivity.getApply.PartyApply();
			break;
		case -1:
			Toast();
			break;
		case -2:
			Toast();
			break;
		default:
			break;
		}
		finish();
	}

	private void Toast() {
		//自定义Toast
		View toastRoot = getLayoutInflater().inflate(R.layout.my_error_toast, null);
		Toast toast=new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER, 0, 100);
		toast.setView(toastRoot);
		toast.setDuration(100);
		TextView tv=(TextView)toastRoot.findViewById(R.id.TextViewInfo);
		tv.setText("支付失败");
		toast.show();
	}
}
