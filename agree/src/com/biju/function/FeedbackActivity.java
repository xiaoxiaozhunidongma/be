package com.biju.function;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.BJ.javabean.FeedBack;
import com.biju.Interface;
import com.biju.Interface.feedBackListenner;
import com.biju.R;

@SuppressLint("NewApi")
public class FeedbackActivity extends Activity implements OnClickListener {

	private EditText mFeedback_question;
	private Interface feedbackinterface;
	private RelativeLayout mFeedback_question_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		initUI();
		initInterface();
	}

	private void initInterface() {
		feedbackinterface = Interface.getInstance();
		feedbackinterface.setPostListener(new feedBackListenner() {

			@Override
			public void success(String A) {
				Log.e("FeedbackActivity", "sdasdf aesrfdraedfaed"+A);
			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	private void initUI() {
		findViewById(R.id.feedback_back_layout).setOnClickListener(this);// 返回
		findViewById(R.id.feedback_back).setOnClickListener(this);
		findViewById(R.id.feedback_send_layout).setOnClickListener(this);// 发送
		findViewById(R.id.feedback_send).setOnClickListener(this);
		mFeedback_question = (EditText) findViewById(R.id.feedback_question);// 问题
		
		mFeedback_question.setFocusable(true);
		mFeedback_question.setFocusableInTouchMode(true);
		mFeedback_question.requestFocus();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				InputMethodManager inputManager = (InputMethodManager) mFeedback_question
						.getContext().getSystemService(NewteamActivity.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(mFeedback_question, 0);
			}
		}, 998);
		
		mFeedback_question_layout = (RelativeLayout) findViewById(R.id.feedback_question_layout);
		mFeedback_question.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String question=s.toString();
				if(!("".equals(question)))
				{
					//得到布局中设置的行数
					int num=mFeedback_question.getLineCount();
					if(num>=4)
					{
						mFeedback_question.setLines(num+1);
					}
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.feedback_back_layout:
		case R.id.feedback_back:
			feedback_back();
			break;
		case R.id.feedback_send_layout:
		case R.id.feedback_send:
			feedback_send();
			break;
		default:
			break;
		}
	}

	private void feedback_send() {
		String question = mFeedback_question.getText().toString().trim();
		FeedBack feedBack = new FeedBack();
		feedBack.setContent(question);
		feedbackinterface.feedBack(FeedbackActivity.this, feedBack);
	}

	private void feedback_back() {
		finish();
		overridePendingTransition(R.anim.left, R.anim.right);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			feedback_back();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
