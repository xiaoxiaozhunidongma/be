package com.biju.pay;

import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.BJ.javabean.AliPayBack;
import com.BJ.javabean.AliPayMode;
import com.BJ.javabean.CreateOrder;
import com.BJ.javabean.Party2;
import com.BJ.javabean.UnionPay;
import com.BJ.javabean.UnionPayBack;
import com.BJ.javabean.UserAllParty;
import com.BJ.javabean.WeChatPay;
import com.BJ.javabean.WeChatPayBack;
import com.BJ.javabean.WeChatPayMode;
import com.BJ.utils.SdPkUser;
import com.alipay.sdk.app.PayTask;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.AliPayListenner;
import com.biju.Interface.CreateOrderListenner;
import com.biju.Interface.UnionPayListenner;
import com.biju.Interface.WeChatPayListenner;
import com.biju.R;
import com.biju.alipayutils.PayResult;
import com.biju.alipayutils.SignUtils;
import com.biju.function.PartyDetailsActivity;
import com.biju.wechatpayutils.MD5;
import com.biju.wechatpayutils.Util;
import com.github.volley_examples.utils.GsonUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

@SuppressLint("HandlerLeak")
public class PayBaseActivity extends Activity implements OnClickListener,Callback,Runnable  {

	private TextView mPayTreasureTv;
	private TextView mPayWeChatWalletTv;
	private TextView mPayUnionpayTv;
	private float mPaymount;
	private Interface mPayInterface;
	// 微信支付，以下
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(PayBaseActivity.this, null);
	Map<String, String> resultunifiedorder;
	private String app_id;
	private String app_secret;
	private String mch_id;
	private String partner_id;
	private String mPayName;
	private Integer mWeChatPayMount;
	StringBuffer sb;
	PayReq req;
	public static GetApply getApply;
	private String mAliPayMount;
	// 微信支付，以上
	
	// 支付宝，以下
	// 商户PID
	public String PARTNER = "";
	// 商户收款账号
	public String SELLER = "";
	// 商户私钥，pkcs8格式
	public String RSA_PRIVATE = "";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "";
	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_CHECK_FLAG = 2;
	//支付宝，以上
	
	//银联，以下
	 private Context mContext = null;
	 private Handler mUnionHandler = null;
	 private ProgressDialog mLoadingDialog = null;
	 public static final int PLUGIN_VALID = 0;
	 public static final int PLUGIN_NOT_INSTALLED = -1;
	 public static final int PLUGIN_NEED_UPGRADE = 2;

	 /*****************************************************************
	  * mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
	  *****************************************************************/
	  private final String mMode = "00";
	  private static String TN_URL_00 = "";
	  private float mUnionPayMount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay);
		//银联支付
		mContext = PayBaseActivity.this;
		mUnionHandler = new Handler(PayBaseActivity.this);
		//微信支付
		req = new PayReq();
		sb = new StringBuffer();
		
		Intent intent = getIntent();
		boolean userAll=intent.getBooleanExtra(IConstant.UserAll, false);
		if (userAll) {
			UserAllParty userAllParty = (UserAllParty) intent.getSerializableExtra(IConstant.UserAllUoreParty);
			to_user = userAllParty.getFk_user();
			startTime = userAllParty.getBegin_time();
			fk_party = userAllParty.getPk_party();
		} else {
			Party2 moreparty = (Party2) intent.getSerializableExtra(IConstant.MoreParty);
			to_user = moreparty.getFk_user();
			startTime = moreparty.getBegin_time();
			fk_party = moreparty.getPk_party();
		}
		
		mPaymount = intent.getFloatExtra(IConstant.Paymount, 0);
		mWeChatPayMount = (int) (mPaymount);// 微信支付金额// 测试完要*100，单位是分
		mAliPayMount = String.valueOf(mPaymount/100);// 支付宝金额//测试完要把除100去掉
		mUnionPayMount = mPaymount;
		mPayName = intent.getStringExtra(IConstant.Payname);
		initUI();
		initInterface();
		initApply();
	}

	private void initApply() {
		GetApply getApply = new GetApply() {
			@Override
			public void PartyApply() {
				PartyDetailsActivity.getWeChatPay.WeChatPay();
				finish();
			}
		};
		this.getApply = getApply;
	}

	private void initInterface() {
		mPayInterface = Interface.getInstance();
		// 微信支付监听
		mPayInterface.setPostListener(new WeChatPayListenner() {

			@Override
			public void success(String A) {
				Log.e("PayActivity", "微信返回的信息=========" + A);
				WeChatPayBack chatPayBack = GsonUtils.parseJson(A,WeChatPayBack.class);
				Integer statusMsg = chatPayBack.getStatusMsg();
				if (1 == statusMsg) {
					WeChatPayMode chatPayModes = chatPayBack.getReturnData();
					app_id = chatPayModes.getApp_id();
					app_secret = chatPayModes.getApp_secret();
					mch_id = chatPayModes.getMch_id();
					partner_id = chatPayModes.getPartner_id();
					Log.e("PayActivity", "所得到的结果app_id========"+ app_id);
					Log.e("PayActivity", "所得到的结果app_secret========"+ app_secret);
					Log.e("PayActivity", "所得到的结果mch_id========"+ mch_id);
					Log.e("PayActivity", "所得到的结果partner_id========"+ partner_id);
					if (app_id != null) {
						msgApi.registerApp(app_id);
						GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
						getPrepayId.execute();
						Log.e("PayActivity", "第一步========");
					}
				}
			}

			@Override
			public void defail(Object B) {
			}
		});

		// 支付宝支付监听
		mPayInterface.setPostListener(new AliPayListenner() {

			@Override
			public void success(String A) {
				Log.e("PayActivity", "支付宝返回的信息=========" + A);
				AliPayBack aliPayBack = GsonUtils.parseJson(A, AliPayBack.class);
				Integer StatusMsg = aliPayBack.getStatusMsg();
				if (1 == StatusMsg) {
					AliPayMode aliPayMode = aliPayBack.getReturnData();
					String partner = aliPayMode.getPartner();
					String seller = aliPayMode.getSeller();
					String privateKey = aliPayMode.getPrivateKey();
					check();// 检查手机是否安装了支付宝
					PARTNER = partner;
					SELLER = seller;
					RSA_PRIVATE = privateKey;
				}
			}

			@Override
			public void defail(Object B) {
			}
		});
		
		//银联支付监听
		mPayInterface.setPostListener(new UnionPayListenner() {
			
			@Override
			public void success(String A) {
				Log.e("PayActivity", "银联支付返回的信息=========" + A);
				UnionPayBack unionPayBack=GsonUtils.parseJson(A, UnionPayBack.class);
				Integer StatusMsg=unionPayBack.getStatusMsg();
				if(1==StatusMsg){
					String Url=unionPayBack.getReturnData();
					Log.e("PayActivity", "银联支付返回的URL=========" + Url);
					if(Url!=null){
						TN_URL_00=Url;
						 mLoadingDialog = ProgressDialog.show(mContext, // context
					                "", // title
					                "正在努力的获取tn中,请稍候...", // message
					                true); // 进度是否是不确定的，这只和创建进度条有关

					             //步骤1：从网络开始,获取交易流水号即TN
					          new Thread(PayBaseActivity.this).start();
					}
				}
			}
			
			@Override
			public void defail(Object B) {
			}
		});
		
		//创建财务订单监听
		mPayInterface.setPostListener(new CreateOrderListenner() {
			
			@Override
			public void success(String A) {
				Log.e("PayActivity", "返回创建财务订单的结果====="+A);
			}
			
			@Override
			public void defail(Object B) {
				
			}
		});
	}

	private void initUI() {
		findViewById(R.id.PayCancle).setOnClickListener(this);
		findViewById(R.id.PayTreasureLayout).setOnClickListener(this);// 支付宝
		findViewById(R.id.PayWeChatWalletLayout).setOnClickListener(this);// 微信钱包
		findViewById(R.id.PayUnionpayLayout).setOnClickListener(this);// 银联
		mPayTreasureTv = (TextView) findViewById(R.id.PayTreasureTv);
		mPayWeChatWalletTv = (TextView) findViewById(R.id.PayWeChatWalletTv);
		mPayUnionpayTv = (TextView) findViewById(R.id.PayUnionpayTv);
		mPayTreasureTv.setText("支付宝支付    ¥" + mPaymount);
		mPayWeChatWalletTv.setText("微信钱包支付    ¥" + mPaymount);
		mPayUnionpayTv.setText("银联在线支付    ¥" + mPaymount);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.PayWeChatWalletLayout:
			PayWeChatWalletLayout();
			break;
		case R.id.PayTreasureLayout:
			PayTreasureLayout();
			break;
		case R.id.PayCancle:
			PayCancle();
			break;
		case R.id.PayUnionpayLayout:
			PayUnionpayLayout();
			break;
		default:
			break;
		}
	}

	//取消
	private void PayCancle() {
		PartyDetailsActivity.partyDetailsBackground.PartyDetailsBackground();
		finish();
		Intent intent=new Intent(PayBaseActivity.this, PartyDetailsActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.top_1_item, R.anim.bottom_1_item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			PayCancle();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	// //////////////////////////////////////////微信支付，以下
	private void PayWeChatWalletLayout() {
		WeChatPay mWeChatPay = new WeChatPay();
		mPayInterface.WeChatPay(PayBaseActivity.this, mWeChatPay);
	}

	private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>> {
		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(PayBaseActivity.this,getString(R.string.app_tip),getString(R.string.getting_prepayid));
		}

		@Override
		protected void onPostExecute(Map<String, String> result) {
			if (dialog != null) {
				dialog.dismiss();
				Log.e("PayActivity", "第2步========");
			}
			sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");
			resultunifiedorder = result;
			Log.e("PayActivity", "第3步========");
			if (result.get("prepay_id").toString() != null) {
				genPayReq();
				Log.e("PayActivity", "第4步========");
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected Map<String, String> doInBackground(Void... params) {
			String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			String entity = genProductArgs();
			byte[] buf = Util.httpPost(url, entity);
			String content = new String(buf);
			Map<String, String> xml = decodeXml(content);
			return xml;
		}
	}

	public Map<String, String> decodeXml(String content) {
		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String nodeName = parser.getName();
				switch (event) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if ("xml".equals(nodeName) == false) {
						// 实例化student对象
						xml.put(nodeName, parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				event = parser.next();
			}
			return xml;
		} catch (Exception e) {
		}
		return null;

	}
	
	private String genProductArgs() {
		StringBuffer xml = new StringBuffer();
		try {
			Log.e("PayActivity", "所要支付的金额mWeChatPayMount======="+mWeChatPayMount);
			String nonceStr = genNonceStr();
			xml.append("</xml>");
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();//device_info=APP-001
			packageParams.add(new BasicNameValuePair("appid", app_id));
			packageParams.add(new BasicNameValuePair("body", mPayName));
			packageParams.add(new BasicNameValuePair("input_charset", "UTF-8"));
			packageParams.add(new BasicNameValuePair("mch_id", mch_id));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			packageParams.add(new BasicNameValuePair("notify_url","http://121.40.35.3/test"));
			packageParams.add(new BasicNameValuePair("out_trade_no",genOutTradNo()));
			packageParams.add(new BasicNameValuePair("spbill_create_ip","127.0.0.1"));
			packageParams.add(new BasicNameValuePair("total_fee",mWeChatPayMount+""));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));

			String sign = genPackageSign(packageParams);
			Log.e("PayActivity", "sign========" + sign);
			packageParams.add(new BasicNameValuePair("sign", sign));
			String xmlstring = toXml(packageParams);
			return new String(xmlstring.toString().getBytes(), "ISO-8859-1");
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 生成签名
	 */

	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(partner_id);// app_key
		Log.e("PayActivity", "app_key==========" + sb.toString());
		String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		return packageSign;
	}

	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}

	private String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<" + params.get(i).getName() + ">");
			sb.append(params.get(i).getValue());
			sb.append("</" + params.get(i).getName() + ">");
		}
		sb.append("</xml>");
		return sb.toString();
	}

	private String genOutTradNo() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}

	private void genPayReq() {
		req.appId = app_id;
		req.partnerId = mch_id;
		req.prepayId = resultunifiedorder.get("prepay_id");
		req.packageValue = "Sign=WXPay";
		req.nonceStr = genNonceStr();//订单号
		req.timeStamp = String.valueOf(genTimeStamp());
		Log.e("PayActivity", "第5步========");
		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

		req.sign = genAppSign(signParams);
		sendPayReq();
	}

	private void sendPayReq() {
		msgApi.registerApp(app_id);
		msgApi.sendReq(req);
		Log.e("PayActivity", "第6步========");
		//以下是传值做财务订单
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String Create_time=sdf.format(new Date());
		Integer from_user=SdPkUser.getsD_pk_user();
		SharedPreferences PayBase_sp=getSharedPreferences("PayBase", 0);
		Editor editor=PayBase_sp.edit();
		editor.putString("CreateOrder", req.nonceStr);
		editor.putInt("Order_type", 1);
		editor.putString("Create_time", Create_time);
		editor.putString("Arrival_time", startTime);
		editor.putInt("From_user", from_user);
		editor.putInt("To_user", to_user);
		editor.putString("Fk_party", fk_party);
		editor.putInt("fk_party", 1);
		editor.putFloat("Amount", mPaymount);
		editor.commit();
	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(partner_id);
		this.sb.append("sign str\n" + sb.toString() + "\n\n");
		String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		return appSign;
	}

	// //////////////////////////////////////////微信支付，以上
	// ////////////////////////////////////////////支付宝支付，以下
	private void PayTreasureLayout() {
		WeChatPay wechatpay = new WeChatPay();
		mPayInterface.AliPay(PayBaseActivity.this, wechatpay);
	}

	// 检查手机是否安装了支付宝
	private void check() {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask(PayBaseActivity.this);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();
				Log.e("PayActivity", "是否安装了支付宝=========" + isExist);
				if (isExist) {
					// 有支付宝则调起支付宝界面
					AliPay();
				} else {
					// 自定义Toast
					View toastRoot = getLayoutInflater().inflate(R.layout.my_error_toast, null);
					Toast toast = new Toast(getApplicationContext());
					toast.setGravity(Gravity.CENTER, 0, 100);
					toast.setView(toastRoot);
					toast.setDuration(100);
					TextView tv = (TextView) toastRoot.findViewById(R.id.TextViewInfo);
					tv.setText("你还没有安装支付宝，请先安装支付宝!");
					toast.show();
				}

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();
	}

	// 支付宝支付界面
	private void AliPay() {
		if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE)|| TextUtils.isEmpty(SELLER)) {
			new AlertDialog.Builder(PayBaseActivity.this)
					.setTitle("警告")
					.setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {
									finish();
								}
							}).show();
			return;
		}
		// 订单
		String orderInfo = getOrderInfo(mPayName, "必聚聚会", mAliPayMount);// mAliPayMount
		order_id = orderInfo;//传订单号
		Log.e("PayActivity", "该商品的订单=========" + orderInfo);

		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
			Log.e("PayActivity", "该商品的sign=========" + sign);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"+ getSignType();
		Log.e("PayActivity", "完整的符合支付宝参数规范的订单信息=========" + payInfo);
		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(PayBaseActivity.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";
		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";
		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";
		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";
		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";
		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";
		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"+ "\"";
		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";
		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";
		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";
		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";
		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";
		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";
		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);
		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();
				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					PartyDetailsActivity.getAliPay.AliPay();
					
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String Create_time=sdf.format(new Date());
					Integer from_user=SdPkUser.getsD_pk_user();
					CreateOrder createOrder=new CreateOrder();
					createOrder.setOrder_id(order_id);
					createOrder.setOrder_type(2);
					createOrder.setCreate_time(Create_time);
					createOrder.setArrival_time(startTime);
					createOrder.setFrom_user(from_user);
					createOrder.setTo_user(to_user);
					createOrder.setFk_party(fk_party);
					createOrder.setArrival_type(1);
					createOrder.setAmount(mPaymount);
					createOrder.setStatus(1);
					mPayInterface.CreateOrder(PayBaseActivity.this, createOrder);
					
					finish();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(PayBaseActivity.this, "支付结果确认中",Toast.LENGTH_SHORT).show();
					} else {
						// 自定义Toast
						View toastRoot = getLayoutInflater().inflate(R.layout.my_error_toast, null);
						Toast toast = new Toast(getApplicationContext());
						toast.setGravity(Gravity.CENTER, 0, 100);
						toast.setView(toastRoot);
						toast.setDuration(100);
						TextView tv = (TextView) toastRoot.findViewById(R.id.TextViewInfo);
						tv.setText("支付失败");
						toast.show();
					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				// Toast.makeText(PayActivity.this, "检查结果为：" + msg.obj,Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};
	private Integer to_user;
	private String startTime;
	private String fk_party;
	private String order_id;

	// ////////////////////////////////////////////支付宝支付，以上
	// ////////////////////////////////////////////银联支付,以下
	private void PayUnionpayLayout() {
		long currentTimeMillis = System.currentTimeMillis();
	    String mCurrentTimeMillis=String.valueOf(currentTimeMillis);
	    Log.e("PayActivity", "========="+mCurrentTimeMillis);
		UnionPay unionpay=new UnionPay();
		unionpay.setOrder_id(mCurrentTimeMillis);
		unionpay.setAmount(mUnionPayMount);
		mPayInterface.UnionPay(PayBaseActivity.this, unionpay);
	}
	
    @Override
    public boolean handleMessage(Message msg) {
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        
        String tn = "";
        if (msg.obj == null || ((String) msg.obj).length() == 0) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("错误提示");
//            builder.setMessage("网络连接失败,请重试!");
//            builder.setNegativeButton("确定",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//            builder.create().show();
        	final SweetAlertDialog sd = new SweetAlertDialog(PayBaseActivity.this,SweetAlertDialog.WARNING_TYPE);
    		sd.setTitleText("错误提示");
    		sd.setContentText("网络连接失败,请重试!");
    		sd.setConfirmText("确定");
    		sd.showCancelButton(true);
    		sd.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
    			@Override
    			public void onClick(SweetAlertDialog sDialog) {
    				sd.cancel();
    			}
    		}).show();
        } else {
            tn = (String) msg.obj;
            /*************************************************
             * 步骤2：通过银联工具类启动支付插件
             ************************************************/
            UPPayAssistEx.startPayByJAR(PayBaseActivity.this, PayActivity.class, null, null, tn, mMode);
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*************************************************
         * 步骤3：处理银联手机支付控件返回的支付结果
         ************************************************/
        if (data == null) {
            return;
        }

        String msg = "";
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
        String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {
        	PartyDetailsActivity.getAliPay.AliPay();
			finish();
//            msg = "支付成功！";
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
        }

        // 自定义Toast
		View toastRoot = getLayoutInflater().inflate(R.layout.my_error_toast, null);
		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER, 0, 100);
		toast.setView(toastRoot);
		toast.setDuration(200);
		TextView tv = (TextView) toastRoot.findViewById(R.id.TextViewInfo);
		tv.setText(msg);
		toast.show();
    }

    @Override
    public void run() {
        String tn = null;
        try {
            String url = TN_URL_00;
            if(url!=null){
            	URL myURL = new URL(url);
            	HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
            	conn.setConnectTimeout(120000);
            	conn.setRequestMethod("GET"); 
            	if (conn.getResponseCode() == 200) { 
            	InputStream in = conn.getInputStream(); 
            	XmlPullParser parser = Xml.newPullParser(); 
            	parser.setInput(in, "UTF-8"); 
            	int event = parser.getEventType(); 
            	while (event != XmlPullParser.END_DOCUMENT) { 
            	switch (event) { 
            	case XmlPullParser.START_TAG: 
            	if (("tn").equals(parser.getName())) { 
            	tn=parser.nextText(); 
            	Log.e("PayActivity", "获取到的tn1111111111=========="+tn);
            	} 
            	break; 
            	case XmlPullParser.END_TAG: 
            	break; 
            	} 
            	event = parser.next(); 
            	} 
            	} 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Message msg = mUnionHandler.obtainMessage();
        msg.obj = tn;
        mUnionHandler.sendMessage(msg);
    }
	// 进行报名
	public interface GetApply {
		void PartyApply();
	}
}
