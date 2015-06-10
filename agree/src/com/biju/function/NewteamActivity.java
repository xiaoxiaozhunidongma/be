package com.biju.function;

import com.biju.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

public class NewteamActivity extends Activity implements OnClickListener{

	private EditText mNewteam_name;
	private ImageView mNewteam_head;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newteam);
		initUI();
	}

	private void initUI() {
		mNewteam_name = (EditText) findViewById(R.id.newteam_name);//С������
		mNewteam_head = (ImageView) findViewById(R.id.newteam_head);//��ʾС��ͷ��
		findViewById(R.id.newteam_tv_head).setOnClickListener(this);//ѡ��С��ͷ��
		findViewById(R.id.newteam_requsetcode).setOnClickListener(this);//С��������
		findViewById(R.id.newteam_back_layout).setOnClickListener(this);//����
		findViewById(R.id.newteam_back).setOnClickListener(this);//����
		findViewById(R.id.newteam_OK_layout).setOnClickListener(this);//���
		findViewById(R.id.newteam_OK_layout).setOnClickListener(this);//���
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.newteam, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.newteam_back_layout:
		case R.id.newteam_back:
			newteam_back();
			break;
		case R.id.newteam_OK_layout:
		case R.id.newteam_OK:
			newteam_OK();
			break;
		default:
			break;
		}
	}

	private void newteam_OK() {
		String newteam_name=mNewteam_name.getText().toString().trim();
	}

	private void newteam_back() {
		finish();
	}

}
