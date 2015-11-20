package com.biju.function;

import com.BJ.javabean.Group;
import com.biju.Interface;
import com.biju.Interface.ChangeGroupNameListenner;
import com.biju.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

public class ChangeGroupNameActivity extends Activity implements OnClickListener{

	private EditText mChangeGroupNameEdit;
	private Integer pk_group;
	private Interface mChangeNameInterface;
	private String group_name;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_group_name);
		pk_group = GroupActivity.getPk_group();
		initUI();
		initInterface();
	}

	private void initInterface() {
		mChangeNameInterface = Interface.getInstance();
		mChangeNameInterface.setPostListener(new ChangeGroupNameListenner() {
			
			@Override
			public void success(String A) {
				Log.e("ChangeGroupNameActivity", "С�������Ƿ���ĳɹ�===="+A);
				SharedPreferences ChangeName_sp=getSharedPreferences("ChangeGroupName", 0);
				Editor editor=ChangeName_sp.edit();
				editor.putBoolean("ChangeName", true);
				editor.commit();
				finish();
			}
			
			@Override
			public void defail(Object B) {
				
			}
		});
	}

	private void initUI() {
		findViewById(R.id.ChangeGroupNameBack).setOnClickListener(this);//����
		findViewById(R.id.ChangeGroupNameOK).setOnClickListener(this);//���
		mChangeGroupNameEdit = (EditText) findViewById(R.id.ChangeGroupNameEdit);
		TextView ChangeGroupNamePrompt=(TextView) findViewById(R.id.ChangeGroupNamePrompt);
		ChangeGroupNamePrompt.setText("С�������Ȳ���С��2���ַ�,"+"\n"+"���ֻ�ܰ���7���ַ�,����ʹ��Ӣ����ĸ,"
		+"\n"+"���ֻ������Ĺ���,���ǲ���ʹ��������ַ�,"+"\n"+"��:'/[]:;|=,+*?<>'��,"+"\n"+"Ҳ����ʹ�ð����пո�");
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ChangeGroupNameBack:
			ChangeGroupNameBack();
			break;
		case R.id.ChangeGroupNameOK:
			ChangeGroupNameOK();
			break;
		default:
			break;
		}
	}

	private void ChangeGroupNameOK() {
		group_name = mChangeGroupNameEdit.getText().toString().trim();
		Group group=new Group();
		group.setPk_group(pk_group);
		Log.e("ChangeGroupNameActivity", "��ǰ��С��ID===="+pk_group);
		group.setName(group_name);
		mChangeNameInterface.ChangeGroupName(ChangeGroupNameActivity.this, group);
	}

	private void ChangeGroupNameBack() {
		finish();
		overridePendingTransition(R.anim.left, R.anim.right);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			ChangeGroupNameBack();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
