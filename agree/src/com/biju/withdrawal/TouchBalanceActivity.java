package com.biju.withdrawal;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.BJ.javabean.PaymentAccount;
import com.BJ.javabean.PaymentAccountBack;
import com.BJ.javabean.User;
import com.BJ.javabean.UserBalance;
import com.BJ.javabean.UserBalanceBack;
import com.BJ.utils.SdPkUser;
import com.biju.Interface;
import com.biju.Interface.BalanceListenner;
import com.biju.Interface.ReadPayMentAccountListenner;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;

public class TouchBalanceActivity extends Activity implements OnClickListener{

	private TextView mTouchBalanceMoney;
	private ListView mTouchBalanceLixtview;
	private Integer sd_pk_user;
	private Interface mTouchBalanceInterface;
	private List<PaymentAccount> PaymentAccountList=new ArrayList<PaymentAccount>();
	private MyTouchBalanceAdapter adapter;
	private View mFooterView;
	private RelativeLayout mAccountFooter;
	private float userbalance;
	public static GetBalancePayMentAccount getBalancePayMentAccount;
	private TextView mTouchBalanceUnArr_money;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_touch_balance);
		sd_pk_user = SdPkUser.getsD_pk_user();
		initUI();
		initInterFace();
		initBalance();//获取用户的余额
		initReadPayMnetAccount();//获取用户的提现账户
		initPayMentAccount();
	}

	private void initPayMentAccount() {
		GetBalancePayMentAccount getBalancePayMentAccount=new GetBalancePayMentAccount(){

			@Override
			public void BalancePayMentAccount() {
				initBalance();//获取用户的余额
				initReadPayMnetAccount();//获取用户的提现账户
			}
			
		};
		this.getBalancePayMentAccount=getBalancePayMentAccount;
	}

	//获取用户的提现账户
	private void initReadPayMnetAccount() {
		User user=new User();
		user.setPk_user(sd_pk_user);
		mTouchBalanceInterface.ReadPayMentAccount(TouchBalanceActivity.this, user);
	}

	private void initInterFace() {
		mTouchBalanceInterface = Interface.getInstance();
		//获取新建账户的监听
		mTouchBalanceInterface.setPostListener(new BalanceListenner() {
			
			@Override
			public void success(String A) {
				Log.e("TouchBalanceActivity", "获取回来的余额信息========="+A);
				UserBalanceBack userBalanceBack=GsonUtils.parseJson(A, UserBalanceBack.class);
				Integer StatusMsg=userBalanceBack.getStatusMsg();
				if(1==StatusMsg){
					UserBalance balance=userBalanceBack.getReturnData();
					userbalance = balance.getAmount();
					float unArr_money=balance.getUnArr_money();
					mTouchBalanceMoney.setText("¥"+userbalance+"0");
					mTouchBalanceUnArr_money.setText("未到账金额: ¥"+unArr_money+"0");
					}
			}
			
			@Override
			public void defail(Object B) {
			}
		});
		//获取用户的提现账户的监听
		mTouchBalanceInterface.setPostListener(new ReadPayMentAccountListenner() {
			
			@Override
			public void success(String A) {
				PaymentAccountList.clear();
				Log.e("TouchBalanceActivity", "获取回来的账户========="+A);
				PaymentAccountBack paymentAccountBack=GsonUtils.parseJson(A, PaymentAccountBack.class);
				Integer StatusMsg=paymentAccountBack.getStatusMsg();
				if(1==StatusMsg){
					List<PaymentAccount> paymentAccountslist=paymentAccountBack.getReturnData();
					if(paymentAccountslist.size()>0){
						for (int i = 0; i < paymentAccountslist.size(); i++) {
							PaymentAccount paymentAccount=paymentAccountslist.get(i);
							PaymentAccountList.add(paymentAccount);
						}
						adapter.notifyDataSetChanged();
					}
					Log.e("TouchBalanceActivity", "删除账户第1步======="+PaymentAccountList.size());
					if(PaymentAccountList.size()>0){
						mAccountFooter.setVisibility(View.GONE);
					}
				}else {
					Log.e("TouchBalanceActivity", "删除账户第2步======="+PaymentAccountList.size());
					mAccountFooter.setVisibility(View.VISIBLE);
					adapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void defail(Object B) {
				
			}
		});
	}

	//获取新建账户的回调
	private void initBalance() {
		User user=new User();
		user.setPk_user(sd_pk_user);
		mTouchBalanceInterface.Balance(TouchBalanceActivity.this, user);
	}

	private void initUI() {
		findViewById(R.id.TouchBalanceBack).setOnClickListener(this);//返回
		findViewById(R.id.TouchBalanceNewAccount).setOnClickListener(this);//新建账户
		
		mTouchBalanceLixtview = (ListView) findViewById(R.id.TouchBalanceLixtview);//显示选择方式
		mTouchBalanceLixtview.setDividerHeight(0);
		
		mFooterView = View.inflate(TouchBalanceActivity.this, R.layout.account_footer_item, null);
		mTouchBalanceLixtview.addFooterView(mFooterView);
		mAccountFooter = (RelativeLayout) mFooterView.findViewById(R.id.AccountFooter);
		mAccountFooter.setOnClickListener(this);
		mFooterView.findViewById(R.id.TouchBalanceHistoryList).setOnClickListener(this);//历史账单
		mFooterView.findViewById(R.id.TouchBalanceAccountPrompt1).setOnClickListener(this);
		mFooterView.findViewById(R.id.TouchBalanceAccountPrompt2).setOnClickListener(this);
		
		
		View mHeadView=View.inflate(TouchBalanceActivity.this, R.layout.account_head_iten, null);
		mTouchBalanceLixtview.addHeaderView(mHeadView);
		mTouchBalanceMoney = (TextView) mHeadView.findViewById(R.id.TouchBalanceMoney);//显示余额
		mTouchBalanceUnArr_money = (TextView) mHeadView.findViewById(R.id.TouchBalanceUnArr_money);
		
		mTouchBalanceLixtview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int pos=arg2-mTouchBalanceLixtview.getHeaderViewsCount();
				Log.e("TouchBalanceActivity", "这时候的pos====="+pos);
				if(pos>=0){
					Log.e("TouchBalanceActivity", "点击到了第====="+pos+"行");
					PaymentAccount paymentAccount=PaymentAccountList.get(pos);
					Intent intent=new Intent(TouchBalanceActivity.this, DeletePaymentAccountActivity.class);
					intent.putExtra("PayMentAccount", paymentAccount);
					intent.putExtra("Balance", userbalance);
					startActivity(intent);
					overridePendingTransition(R.anim.in_item, R.anim.out_item);
				}
			}
		});
		
		adapter = new MyTouchBalanceAdapter();
		mTouchBalanceLixtview.setAdapter(adapter);
	}

	class ViewHolder{
		TextView TouchBalanceAccountShowText;
		TextView TouchBalanceChooseWayText;
		TextView TouchBalanceLine1;
		TextView TouchBalanceLine2;
	}
	
	class MyTouchBalanceAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return PaymentAccountList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View inflater = null;
			ViewHolder holder=null;
			if(convertView==null){
				holder=new ViewHolder();
				LayoutInflater layoutInflater = getLayoutInflater();
				inflater =layoutInflater.inflate(R.layout.chooseaccount_item, null);
				holder.TouchBalanceAccountShowText=(TextView) inflater.findViewById(R.id.TouchBalanceAccountShowText);
				holder.TouchBalanceChooseWayText=(TextView) inflater.findViewById(R.id.TouchBalanceChooseWayText);
				holder.TouchBalanceLine1=(TextView) inflater.findViewById(R.id.TouchBalanceLine1);
				holder.TouchBalanceLine2=(TextView) inflater.findViewById(R.id.TouchBalanceLine2);
				inflater.setTag(holder);
			}else {
				inflater=convertView;
				holder=(ViewHolder) inflater.getTag();
			}
			Log.e("TouchBalanceActivity", "删除账户第6步======="+PaymentAccountList.size());
			PaymentAccount account=PaymentAccountList.get(position);
			String UserAccount=account.getAccount();
			holder.TouchBalanceAccountShowText.setText(UserAccount);
			Integer type=account.getType();
			switch (type) {
			case 1:
				holder.TouchBalanceChooseWayText.setText("微信账号");
				break;
			case 2:
				holder.TouchBalanceChooseWayText.setText("支付宝账号");
				break;
			case 3:
				holder.TouchBalanceChooseWayText.setText("银联账号");
				break;

			default:
				break;
			}
			
			if(position==PaymentAccountList.size()-1){
				holder.TouchBalanceLine1.setVisibility(View.GONE);
				holder.TouchBalanceLine2.setVisibility(View.VISIBLE);
			}else {
				holder.TouchBalanceLine1.setVisibility(View.VISIBLE);
				holder.TouchBalanceLine2.setVisibility(View.GONE);
			}
			
			return inflater;
		}
		
	}
		
		
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.TouchBalanceBack:
			TouchBalanceBack();
			break;
		case R.id.TouchBalanceHistoryList:
			TouchBalanceHistoryList();
			break;
		case R.id.TouchBalanceNewAccount:
			TouchBalanceNewAccount();
			break;
		case R.id.AccountFooter:
			AccountFooter();
			break;
		default:
			break;
		}
	}

	
	private void AccountFooter() {
		SweetAlertDialog sd=new SweetAlertDialog(TouchBalanceActivity.this);
		sd.setTitleText("提示");
		sd.setContentText("请点击右上角新建账户按钮"+"\n"+"添加新的提现账号");
		sd.show();
	}

	//新建账户
	private void TouchBalanceNewAccount() {
		Intent intent=new Intent(TouchBalanceActivity.this, NewAccountActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.in_item, R.anim.out_item);
	}

	//详单
	private void TouchBalanceHistoryList() {
		Intent intent=new Intent(TouchBalanceActivity.this, HistoryListActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.in_item, R.anim.out_item);
	}

	private void TouchBalanceBack() {
		finish();
		overridePendingTransition(R.anim.left, R.anim.right);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			TouchBalanceBack();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public interface GetBalancePayMentAccount{
		void BalancePayMentAccount();
	}
}
