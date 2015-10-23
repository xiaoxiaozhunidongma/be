package com.biju.withdrawal;

import com.biju.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

public class HistoryListActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_list);
		initUI();
	}


	private void initUI() {
		findViewById(R.id.HistoryListBack).setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.HistoryListBack:
			HistoryListBack();
			break;

		default:
			break;
		}
	}


	private void HistoryListBack() {
		finish();
		overridePendingTransition(R.anim.left, R.anim.right);
	}

}
