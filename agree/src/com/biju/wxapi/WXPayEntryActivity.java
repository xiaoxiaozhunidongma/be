package com.biju.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.BJ.javabean.CreateOrder;
import com.biju.Interface;
import com.biju.Interface.CreateOrderListenner;
import com.biju.R;
import com.biju.pay.PayBaseActivity;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private IWXAPI api;
	private Interface mWxPayInterface;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wxpay_entry);
		api = WXAPIFactory.createWXAPI(this, "wx2ffba147560de2ff", true);
		api.registerApp("wx2ffba147560de2ff");
		api.handleIntent(getIntent(), this);
	}

	private void initInterface() {
		mWxPayInterface = Interface.getInstance();
		
		SharedPreferences PayBase_sp=getSharedPreferences("PayBase", 0);
		String order_id=PayBase_sp.getString("CreateOrder", "");
		Integer order_type=PayBase_sp.getInt("Order_type", 0);
		String create_time=PayBase_sp.getString("Create_time", "");
		String arrival_time=PayBase_sp.getString("Arrival_time", "");
		Integer from_user=PayBase_sp.getInt("From_user", 0);
		Integer to_user=PayBase_sp.getInt("To_user", 0);
		String fk_party=PayBase_sp.getString("Fk_party", "");
		float amount=PayBase_sp.getFloat("Amount", 0);
		
		Log.e("WXPayEntryActivity", "生成财务订单的参数order_id====="+order_id+
				"\norder_type===="+order_type+"\ncreate_time===="+create_time+
				"\narrival_time===="+arrival_time+"\nfrom_user===="+
				from_user+"\nto_user===="+to_user+"\nfk_party===="+fk_party+
				"\namount===="+amount);
		
		CreateOrder createOrder = new CreateOrder();
		createOrder.setOrder_id(order_id);
		createOrder.setOrder_type(order_type);
		createOrder.setCreate_time(create_time);
		createOrder.setArrival_time(arrival_time);
		createOrder.setFrom_user(from_user);
		createOrder.setTo_user(to_user);
		createOrder.setFk_party(fk_party);
		createOrder.setArrival_type(1);
		createOrder.setAmount(amount);
		createOrder.setStatus(1);
		mWxPayInterface.CreateOrder(WXPayEntryActivity.this, createOrder);
		
		mWxPayInterface.setPostListener(new CreateOrderListenner() {
			
			@Override
			public void success(String A) {
				Log.e("WXPayEntryActivity", "返回创建财务订单的结果====="+A);
			}
			
			@Override
			public void defail(Object B) {
				
			}
		});
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
			CreateOrder();
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

	// 生成财务订单
	private void CreateOrder() {
		initInterface();
	}

	private void Toast() {
		// 自定义Toast
		View toastRoot = getLayoutInflater().inflate(R.layout.my_error_toast,
				null);
		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER, 0, 100);
		toast.setView(toastRoot);
		toast.setDuration(100);
		TextView tv = (TextView) toastRoot.findViewById(R.id.TextViewInfo);
		tv.setText("支付失败");
		toast.show();
	}
}
