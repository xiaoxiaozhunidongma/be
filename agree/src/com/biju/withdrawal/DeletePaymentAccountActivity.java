package com.biju.withdrawal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.BJ.javabean.Order;
import com.BJ.javabean.OrderBack;
import com.BJ.javabean.PaymentAccount;
import com.biju.Interface;
import com.biju.Interface.DeletePayMentAccountListenner;
import com.biju.Interface.OrderListenner;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;

public class DeletePaymentAccountActivity extends Activity implements OnClickListener{

	private PaymentAccount paymentAccount;
	private TextView mDeletePaymentWay;
	private TextView mDeletePaymentAccountText;
	private EditText mDeletePaymentWithdrawalEdit;
	private TextView mDeletePaymentNameText;
	private Interface mDeletePaymentInterface;
	private float userbalance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delete_payment_account);
		Intent intent = getIntent();
		paymentAccount = (PaymentAccount) intent.getSerializableExtra("PayMentAccount");
		userbalance = intent.getFloatExtra("Balance", 0);
		initUI();
		initInterface();
	}

	private void initInterface() {
		mDeletePaymentInterface = Interface.getInstance();
		//ɾ���˻�����
		mDeletePaymentInterface.setPostListener(new DeletePayMentAccountListenner() {
			
			@Override
			public void success(String A) {
				Log.e("DeletePaymentAccountActivity", "����ɾ���˻��Ľ��======"+A);
				finish();
				TouchBalanceActivity.getBalancePayMentAccount.BalancePayMentAccount();
			}
			
			@Override
			public void defail(Object B) {
				
			}
		});
		//�û����ּ���
		mDeletePaymentInterface.setPostListener(new OrderListenner() {
			
			@Override
			public void success(String A) {
				Log.e("DeletePaymentAccountActivity", "�û����ֵĽ��======"+A);	
				OrderBack orderBack=GsonUtils.parseJson(A, OrderBack.class);
				Integer StatusMsg=orderBack.getStatusMsg();
				if(1==StatusMsg){
					TouchBalanceActivity.getBalancePayMentAccount.BalancePayMentAccount();
					finish();
				}else {
					SweetAlertDialog sd=new SweetAlertDialog(DeletePaymentAccountActivity.this);
					sd.setTitleText("��ʾ");
					sd.setContentText("����ʧ�ܣ�����������!");
					sd.show();
				}
			}
			
			@Override
			public void defail(Object B) {
			}
		});
	}

	private void initUI() {
		findViewById(R.id.DeletePaymentBack).setOnClickListener(this);//����
		findViewById(R.id.DeletePaymentWithdrawal).setOnClickListener(this);//����
		mDeletePaymentWay = (TextView) findViewById(R.id.DeletePaymentWay);//֧����ʽ
		mDeletePaymentAccountText = (TextView) findViewById(R.id.DeletePaymentAccountText);//֧���˻�
		mDeletePaymentWithdrawalEdit = (EditText) findViewById(R.id.DeletePaymentWithdrawalEdit);//���ֽ��
		mDeletePaymentWithdrawalEdit.setInputType(EditorInfo.TYPE_CLASS_PHONE);// ����绰����ʱֱ�ӵ������ּ���
		findViewById(R.id.DeletePayment_delete).setOnClickListener(this);
		mDeletePaymentNameText = (TextView) findViewById(R.id.DeletePaymentNameText);//���п����û���
		
		int type=paymentAccount.getType();
		switch (type) {
		case 1:
			mDeletePaymentWay.setText("΢��Ǯ��");
			mDeletePaymentNameText.setVisibility(View.GONE);
			break;
		case 2:
			mDeletePaymentWay.setText("֧����");
			mDeletePaymentNameText.setVisibility(View.GONE);
			break;
		case 3:
			mDeletePaymentWay.setText("����");
			mDeletePaymentNameText.setVisibility(View.VISIBLE);
			String name=paymentAccount.getName();
			mDeletePaymentNameText.setText(name);
			break;

		default:
			break;
		}
		mDeletePaymentAccountText.setText(paymentAccount.getAccount());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.DeletePaymentBack:
			DeletePaymentBack();
			break;
		case R.id.DeletePaymentWithdrawal:
			DeletePaymentWithdrawal();
			break;
		case R.id.DeletePayment_delete:
			DeletePayment_delete();
			break;
		default:
			break;
		}
	}

	//ɾ���˻�
	private void DeletePayment_delete() {
		final SweetAlertDialog sd = new SweetAlertDialog(DeletePaymentAccountActivity.this,SweetAlertDialog.WARNING_TYPE);
		sd.setTitleText("��ʾ");
		sd.setContentText("�Ƿ�Ҫɾ����ǰ�˻�?");
		sd.setCancelText("��������");
		sd.setConfirmText("ȷ��");
		sd.showCancelButton(true);
		sd.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sDialog) {
				sd.cancel();
			}
		}).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sDialog) {
				sd.cancel();
				Integer pk_payment_account=paymentAccount.getPk_payment_account();
				Integer fk_user=paymentAccount.getFk_user();
				PaymentAccount account=new PaymentAccount();
				account.setFk_user(fk_user);
				account.setPk_payment_account(pk_payment_account);
				mDeletePaymentInterface.DeletePayMentAccount(DeletePaymentAccountActivity.this, account);
			}
		}).show();
	}

	//����
	private void DeletePaymentWithdrawal() {
		String withdrawal=mDeletePaymentWithdrawalEdit.getText().toString().trim();
		if(Integer.valueOf(withdrawal)>userbalance){
			SweetAlertDialog sd=new SweetAlertDialog(DeletePaymentAccountActivity.this);
			sd.setTitleText("��ʾ");
			sd.setContentText("���ֵĽ��ܴ��ڵ�ǰ�˻������Ŷ~");
			sd.show();
		}else {
			Integer pk_payment_account=paymentAccount.getPk_payment_account();
			Integer amount=Integer.valueOf(withdrawal);
			Order user_order=new Order();
			user_order.setAmount(amount);
			user_order.setFk_payment_account(pk_payment_account);
			mDeletePaymentInterface.Order(DeletePaymentAccountActivity.this, user_order);
		}
	}

	//����
	private void DeletePaymentBack() {
		finish();
		overridePendingTransition(R.anim.left, R.anim.right);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			DeletePaymentBack();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
