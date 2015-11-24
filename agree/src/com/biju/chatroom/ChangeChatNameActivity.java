package com.biju.chatroom;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;

import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.biju.R;
import com.example.testleabcloud.ChatActivityLean;

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
		ChatActivityLean.conversation.setAttribute("isupdate", true);
		ChatActivityLean.conversation
				.updateInfoInBackground(new AVIMConversationCallback() {

					@Override
					public void done(AVIMException e) {
						if (e == null) {
							ChatActivityLean.chatRoomNameInter
									.updateSuccess(chatName);
							Log.e("ChangeChatNameActivity", "更新对话名字成功");
						}
					}
				});
		finish();
	}

}
