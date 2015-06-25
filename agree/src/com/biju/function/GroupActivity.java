package com.biju.function;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.biju.R;
import com.fragment.ChatFragment;
import com.fragment.PhotoFragment;
import com.fragment.ScheduleFragment;

public class GroupActivity extends FragmentActivity implements OnClickListener {

	private FragmentTabHost mTabhost;
	private int pk_group;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_tab);
		initUI();
		Intent intent = getIntent();
		pk_group = intent.getIntExtra("pk_group",pk_group);
	}

	private void initUI() {
		mTabhost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabhost.setup(GroupActivity.this, getSupportFragmentManager(),
				R.id.realtabcontent);
		AddTab("1", "聊天", ChatFragment.class);
		AddTab("2", "日程", ScheduleFragment.class);
		AddTab("3", "相册", PhotoFragment.class);

		findViewById(R.id.group_back_layout).setOnClickListener(this);// 返回
		findViewById(R.id.group_back).setOnClickListener(this);
		findViewById(R.id.group_setting_layout).setOnClickListener(this);
		findViewById(R.id.group_setting).setOnClickListener(this);// 设置
	}

	@SuppressWarnings("rawtypes")
	private void AddTab(String tag, String title, Class clas) {
		TabSpec tabSpec = mTabhost.newTabSpec(tag);
		View view = getLayoutInflater().inflate(R.layout.group_tab_item, null);
		TextView tab_text = (TextView) view.findViewById(R.id.group_tab_name);
		tab_text.setText(title);
		tab_text.setTextSize(17);
		mTabhost.addTab(tabSpec.setIndicator(view), clas, null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.group_back_layout:
		case R.id.group_back:
			group_back();
			break;
		case R.id.group_setting_layout:
		case R.id.group_setting:
			group_setting();
			break;
		default:
			break;
		}
	}

	private void group_setting() {
		Intent intent=new Intent(GroupActivity.this, TeamSettingActivity.class);
		intent.putExtra("Group",pk_group);
		startActivity(intent);
	}

	private void group_back() {
		finish();
	}

}
