package com.biju.function;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;

import com.BJ.javabean.FeedBack;
import com.biju.Interface;
import com.biju.Interface.feedBackListenner;
import com.biju.R;

public class FeedbackActivity extends Activity implements OnClickListener {

	private EditText mFeedback_question;
	private Interface feedbackinterface;

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
				Log.e("FeedbackActivity", "sdasdf aesrfdraedfaed");
			}

			@Override
			public void defail(Object B) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void initUI() {
		findViewById(R.id.feedback_back_layout).setOnClickListener(this);// 返回
		findViewById(R.id.feedback_back).setOnClickListener(this);
		findViewById(R.id.feedback_send_layout).setOnClickListener(this);// 发送
		findViewById(R.id.feedback_send).setOnClickListener(this);
		mFeedback_question = (EditText) findViewById(R.id.feedback_question);// 问题
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.feedback, menu);
		return true;
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
	}

}
