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
				Log.e("NewAccountActivity", "�����˺��Ƿ�ɹ�======="+A);
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
		mNewAccountQuickPaymentLayout.setOnClickListener(this);//�����п�����д�˺Ų���
		mNewAccountBankPaymentLayout = (RelativeLayout) findViewById(R.id.NewAccountBankPaymentLayout);
		mNewAccountBankPaymentLayout.setOnClickListener(this);//���п���д����
		mNewAccountBankPaymentNameEdit = (EditText) findViewById(R.id.NewAccountBankPaymentNameEdit);//��д����
		mNewAccountBankPaymentBankEdit = (EditText) findViewById(R.id.NewAccountBankPaymentBankEdit);//��д���п�
		
		findViewById(R.id.NewAccountBackBut).setOnClickListener(this);//����
		findViewById(R.id.NewAccountSaveBut).setOnClickListener(this);//����
		mNewAccountChooseWayLayout = (RelativeLayout) findViewById(R.id.NewAccountChooseWayLayout);//ѡ���˻���ʽ����
		findViewById(R.id.NewAccountWeChatAccountBut).setOnClickListener(this);//΢���˻�
		findViewById(R.id.NewAccountAliPayAccountBut).setOnClickListener(this);//֧�����˻�
		findViewById(R.id.NewAccountUnionPayAccountBut).setOnClickListener(this);//�����˻�
		mNewAccountInformationLayout = (RelativeLayout) findViewById(R.id.NewAccountInformationLayout);//��д�˺���Ϣ����
		mNewAccountChooseAccountBut = (Button) findViewById(R.id.NewAccountChooseAccountBut);//��ʾ��ѡ����˻���ʽ
		mNewAccountChooseAccountBut.setOnClickListener(this);
		mNewAccountAccountEdit = (EditText) findViewById(R.id.NewAccountAccountEdit);//��д�˺�����
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

	//�����˻�
	private void NewAccountUnionPayAccountBut() {
		SING=3;
		Show();
	}

	private void Show() {
		mNewAccountChooseWayLayout.setVisibility(View.GONE);
		mNewAccountInformationLayout.setVisibility(View.VISIBLE);
		switch (SING) {
		case 1:
			mNewAccountChooseAccountBut.setText("΢���˻�");
			mNewAccountQuickPaymentLayout.setVisibility(View.VISIBLE);
			mNewAccountBankPaymentLayout.setVisibility(View.GONE);
			break;
		case 2:
			mNewAccountChooseAccountBut.setText("֧�����˻�");
			mNewAccountQuickPaymentLayout.setVisibility(View.VISIBLE);
			mNewAccountBankPaymentLayout.setVisibility(View.GONE);
			break;
		case 3:
			mNewAccountChooseAccountBut.setText("�����˻�");
			mNewAccountQuickPaymentLayout.setVisibility(View.GONE);
			mNewAccountBankPaymentLayout.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	//֧�����˻�
	private void NewAccountAliPayAccountBut() {
		SING=2;
		Show();
	}

	//΢���˻�
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
