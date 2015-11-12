package com.biju.wechatshare;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.BJ.utils.SdPkUser;
import com.biju.MainActivity;
import com.biju.R;
import com.biju.function.GroupActivity;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WEChatShaerActivity extends Activity implements OnClickListener {

	private String app_id = "wx2ffba147560de2ff";
	private IWXAPI api;
	private boolean source;
	private String fk_party;
	private String party_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wechat_shaer);
		api = WXAPIFactory.createWXAPI(WEChatShaerActivity.this, app_id, false);
		Intent intent = getIntent();
		source = intent.getBooleanExtra("Source", false);
		party_name = intent.getStringExtra("Party_name");
		Log.e("WEChatShaerActivity", "party_name=========" + party_name);
		initUI();
	}

	private void initUI() {
		findViewById(R.id.WEChatShaerOK).setOnClickListener(this);// 完成
		findViewById(R.id.WEChatShaerCircleFriends).setOnClickListener(this);// 朋友圈
		findViewById(R.id.WEChatShaerFriends).setOnClickListener(this);// 好友
		findViewById(R.id.WEChatShaerCollection).setOnClickListener(this);// 收藏
	}

	private void wechatShare(int flag) {
		api.registerApp(app_id);
		fk_party = SdPkUser.Getpk_party;
		Log.e("WEChatShaerActivity", "fk_party=========" + fk_party);
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = "http://www.beagree.com/biju/party.html?party="+ fk_party;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = "来自必聚的聚会";
		msg.description = party_name;
		try {
			Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.about_us);
			Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
			bmp.recycle();
			// msg.setThumbImage(thumbBmp);
			msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("biju");// webpage
		req.message = msg;
		// req.scene = flag==1 ? SendMessageToWX.Req.WXSceneTimeline :
		// SendMessageToWX.Req.WXSceneSession;
		if (flag == 0) {
			// 分享到微信会话
			req.scene = SendMessageToWX.Req.WXSceneSession;
		} else {
			// 分享到微信朋友圈
			req.scene = SendMessageToWX.Req.WXSceneTimeline;
		}
		api.sendReq(req);
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()): type + System.currentTimeMillis();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.WEChatShaerCircleFriends:
			WEChatShaerCircleFriends();
			break;
		case R.id.WEChatShaerFriends:
			WEChatShaerFriends();
			break;
		case R.id.WEChatShaerOK:
			WEChatShaerOK();
			break;
		default:
			break;
		}
	}

	private void WEChatShaerOK() {
		finish();
		if (!source) {
			Intent intent = new Intent(WEChatShaerActivity.this,MainActivity.class);
			startActivity(intent);
		} else {
			Intent intent = new Intent(WEChatShaerActivity.this,GroupActivity.class);
			startActivity(intent);
		}
	}

	// 分享给好友
	private void WEChatShaerFriends() {
		wechatShare(0);
		SdPkUser.setGetWeSource(true);// 说明是微信分享过去的
		Log.e("WEChatShaerActivity", "点击到好友的=========");
	}

	// 分享到朋友圈
	private void WEChatShaerCircleFriends() {
		wechatShare(1);
		SdPkUser.setGetWeSource(true);// 说明是微信分享过去的
		Log.e("WEChatShaerActivity", "朋友圈的=========");
	}

}
