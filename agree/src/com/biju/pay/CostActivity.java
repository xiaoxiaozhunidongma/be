package com.biju.pay;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.biju.IConstant;
import com.biju.R;

public class CostActivity extends Activity implements OnClickListener{

	private RelativeLayout mCostFreeLayout;
	private ImageView mCostFreeChooseImage;
	private EditText mCostPayMoneyEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cost);
		initUI();
	}

	private void initUI() {
		findViewById(R.id.CostBackLayout).setOnClickListener(this);//返回
		findViewById(R.id.CostBackTv).setOnClickListener(this);
		findViewById(R.id.CostCompleteLayout).setOnClickListener(this);//完成
		findViewById(R.id.CostCompleteTv).setOnClickListener(this);
		mCostFreeLayout = (RelativeLayout) findViewById(R.id.CostFreeLayout);
		mCostFreeLayout.setOnClickListener(this);//免费
		mCostFreeChooseImage = (ImageView) findViewById(R.id.CostFreeChooseImage);//选择后打钩图片
		mCostPayMoneyEditText = (EditText) findViewById(R.id.CostPayMoneyEditText);//所需支付
		mCostPayMoneyEditText.setInputType(EditorInfo.TYPE_CLASS_PHONE);// 点击电话号码时直接弹出数字键盘
		mCostPayMoneyEditText.setFocusable(true);
		mCostPayMoneyEditText.setText("");
		mCostPayMoneyEditText.setFocusableInTouchMode(true);
		mCostPayMoneyEditText.requestFocus();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				InputMethodManager inputManager = (InputMethodManager) mCostPayMoneyEditText
						.getContext().getSystemService(CostActivity.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(mCostPayMoneyEditText, 0);
			}
		}, 998);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.CostBackLayout:
		case R.id.CostBackTv:
			CostBackLayout();
			break;
		case R.id.CostFreeLayout:
			CostFreeLayout();
			break;
		case R.id.CostCompleteLayout:
		case R.id.CostCompleteTv:
			CostCompleteLayout();
		default:
			break;
		}
	}

	private void CostBackLayout() {
		String mCurrentPayMoney = mCostPayMoneyEditText.getText().toString().trim();
		SharedPreferences CostSp=getSharedPreferences(IConstant.Cost, 0);
		Editor editor=CostSp.edit();
		editor.putBoolean(IConstant.IsCost, true);
		if("".equals(mCurrentPayMoney)){
			editor.putString(IConstant.PayMoney, "0");
		}else {
			editor.putString(IConstant.PayMoney, mCurrentPayMoney);
		}
		editor.commit();
		finish();
	}

	private void CostCompleteLayout() {
		String mCurrentPayMoney = mCostPayMoneyEditText.getText().toString().trim();
		SharedPreferences CostSp=getSharedPreferences(IConstant.Cost, 0);
		Editor editor=CostSp.edit();
		editor.putBoolean(IConstant.IsCost, true);
		if("".equals(mCurrentPayMoney)){
			editor.putString(IConstant.PayMoney, "0");
		}else {
			editor.putString(IConstant.PayMoney, mCurrentPayMoney);
		}
		editor.commit();
		finish();
	}

	private void CostFreeLayout() {
		mCostFreeLayout.setBackgroundResource(R.drawable.CostChooseColor);
		mCostFreeChooseImage.setVisibility(View.VISIBLE);
		mCostPayMoneyEditText.setText("");
	}

}
