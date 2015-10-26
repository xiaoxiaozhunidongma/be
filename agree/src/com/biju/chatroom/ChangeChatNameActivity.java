package com.biju.chatroom;

import com.biju.R;
import com.example.testleabcloud.ChatActivityLean;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class ChangeChatNameActivity extends Activity implements OnClickListener {

	private EditText edit_chatName;
	private String chatName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_room);
		iniUI();
	}

	private void iniUI() {
		edit_chatName = (EditText) findViewById(R.id.ChangeChatNameEdit);
		findViewById(R.id.ChangeChatNameOKBut).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat_room, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ChangeChatNameOKBut:
			completeName();
			break;

		default:
			break;
		}
	}

	private void completeName() {
		chatName = edit_chatName.getText().toString();
		ChatActivityLean.conversation.setName(chatName);
		finish();
	}

}
