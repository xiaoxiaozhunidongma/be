package com.biju.withdrawal;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.BJ.javabean.PaymentAccount;
import com.BJ.utils.SdPkUser;
import com.biju.Interface;
import com.biju.Interface.PayMentAccountListenner;
import com.biju.R;

public class NewAccountActivity extends Activity implements OnClickListener{

	private RelativeLayout mNewAccountChooseWayLayout;
	private RelativeLayout mNewAccountInformationLayout;
	private Button mNewAccountChooseAccountBut;
	private EditText mNewAccountAccountEdit;
	private int SING=-1;
	private Integer sd_pk_user;
	private Interface mNewAccountInterface;
	private RelativeLayout mNewAccountQuickPaymentLayout;
	private RelativeLayout mNewAccountBankPaymentLayout;
	private EditText mNewAccountBankPaymentNameEdit;
	private EditText mNewAccountBankPaymentBankEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		sd_pk_user = SdPkUser.getsD_pk_user();
		setContentView(R.layout.activity_new_account);
		initUI();
		initInterface();
	}

	private void initInterface() {
		mNewAccountInterface = Interface.getInstance();
		mNewAccountInterface.setPostListener(new PayMentAccountListenner() {
			
			@Override
			public void success(String A) {
				Log.e("NewAccountActivity", "创建账号是否成功======="+A);
				TouchBalanceActivity.getBalancePayMentAccount.BalancePayMentAccount();
				finish();
			}
			
			@Override
			public void defail(Object B) {
				
			}
		});
	}

	private void initUI() {
		
		mNewAccountQuickPaymentLayout = (RelativeLayout) findViewById(R.id.NewAccountQuickPaymentLayout);
		mNewAccountQuickPaymentLayout.setOnClickListener(this);//除银行卡外填写账号布局
		mNewAccountBankPaymentLayout = (RelativeLayout) findViewById(R.id.NewAccountBankPaymentLayout);
		mNewAccountBankPaymentLayout.setOnClickListener(this);//银行卡填写布局
		mNewAccountBankPaymentNameEdit = (EditText) findViewById(R.id.NewAccountBankPaymentNameEdit);//填写姓名
		mNewAccountBankPaymentBankEdit = (EditText) findViewById(R.id.NewAccountBankPaymentBankEdit);//填写银行卡
		
		findViewById(R.id.NewAccountBackBut).setOnClickListener(this);//返回
		findViewById(R.id.NewAccountSaveBut).setOnClickListener(this);//保存
		mNewAccountChooseWayLayout = (RelativeLayout) findViewById(R.id.NewAccountChooseWayLayout);//选择账户方式布局
		findViewById(R.id.NewAccountWeChatAccountBut).setOnClickListener(this);//微信账户
		findViewById(R.id.NewAccountAliPayAccountBut).setOnClickListener(this);//支付宝账户
		findViewById(R.id.NewAccountUnionPayAccountBut).setOnClickListener(this);//银联账户
		mNewAccountInformationLayout = (RelativeLayout) findViewById(R.id.NewAccountInformationLayout);//填写账号信息布局
		mNewAccountChooseAccountBut = (Button) findViewById(R.id.NewAccountChooseAccountBut);//显示所选择的账户方式
		mNewAccountChooseAccountBut.setOnClickListener(this);
		mNewAccountAccountEdit = (EditText) findViewById(R.id.NewAccountAccountEdit);//填写账号资料
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.NewAccountBackBut:
			NewAccountBackBut();
			break;
		case R.id.NewAccountWeChatAccountBut:
			NewAccountWeChatAccountBut();
			break;
		case R.id.NewAccountAliPayAccountBut:
			NewAccountAliPayAccountBut();
			break;
		case R.id.NewAccountUnionPayAccountBut:
			NewAccountUnionPayAccountBut();
			break;
		case R.id.NewAccountChooseAccountBut:
			NewAccountChooseAccountBut();
			break;
		case R.id.NewAccountSaveBut:
			NewAccountSaveBut();
			break;
		default:
			break;
		}
	}

	private void NewAccountSaveBut() {
		switch (SING) {
		case 1:
			QuickPayment();
			break;
		case 2:
			QuickPayment();
			break;
		case 3:
			BamkPayment();
			break;

		default:
			break;
		}
		
	}

	private void BamkPayment() {
		String name=mNewAccountBankPaymentNameEdit.getText().toString().trim();
		String bank=mNewAccountBankPaymentBankEdit.getText().toString().trim();
		PaymentAccount paymentAccount=new PaymentAccount();
		paymentAccount.setFk_user(sd_pk_user);
		paymentAccount.setAccount(bank);
		paymentAccount.setType(SING);
		paymentAccount.setStatus(1);
		paymentAccount.setName(name);
		mNewAccountInterface.PayMentAccount(NewAccountActivity.this, paymentAccount);
	}

	private void QuickPayment() {
		String mInformation=mNewAccountAccountEdit.getText().toString().trim();
		PaymentAccount paymentAccount=new PaymentAccount();
		paymentAccount.setFk_user(sd_pk_user);
		paymentAccount.setAccount(mInformation);
		paymentAccount.setType(SING);
		paymentAccount.setStatus(1);
		mNewAccountInterface.PayMentAccount(NewAccountActivity.this, paymentAccount);
	}

	private void NewAccountChooseAccountBut() {
		SING=-1;
		mNewAccountChooseWayLayout.setVisibility(View.VISIBLE);
		mNewAccountInformationLayout.setVisibility(View.GONE);
	}

	//银联账户
	private void NewAccountUnionPayAccountBut() {
		SING=3;
		Show();
	}

	private void Show() {
		mNewAccountChooseWayLayout.setVisibility(View.GONE);
		mNewAccountInformationLayout.setVisibility(View.VISIBLE);
		switch (SING) {
		case 1:
			mNewAccountChooseAccountBut.setText("微信账户");
			mNewAccountQuickPaymentLayout.setVisibility(View.VISIBLE);
			mNewAccountBankPaymentLayout.setVisibility(View.GONE);
			break;
		case 2:
			mNewAccountChooseAccountBut.setText("支付宝账户");
			mNewAccountQuickPaymentLayout.setVisibility(View.VISIBLE);
			mNewAccountBankPaymentLayout.setVisibility(View.GONE);
			break;
		case 3:
			mNewAccountChooseAccountBut.setText("银联账户");
			mNewAccountQuickPaymentLayout.setVisibility(View.GONE);
			mNewAccountBankPaymentLayout.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	//支付宝账户
	private void NewAccountAliPayAccountBut() {
		SING=2;
		Show();
	}

	//微信账户
	private void NewAccountWeChatAccountBut() {
		SING=1;
		Show();
	}

	private void NewAccountBackBut() {
		finish();
		overridePendingTransition(R.anim.left, R.anim.right);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			NewAccountBackBut();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
