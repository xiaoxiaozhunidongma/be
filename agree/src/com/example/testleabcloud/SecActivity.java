package com.example.testleabcloud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.biju.R;
import com.example.huanxin.ChatActivity;

public class SecActivity extends Activity {

	private AVIMClient imClient;
	public static AVIMConversation mConversation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sec);
		imClient = AVIMClient.getInstance("A");
		createMyConversation();
	}

	private void createMyConversation() {
		List<String> clientIds = new ArrayList<String>();
		clientIds.add("A");
		clientIds.add("B");

		// 锟斤拷锟角革拷曰锟斤拷锟斤拷锟揭伙拷锟斤拷远锟斤拷锟斤拷锟斤拷锟�type锟斤拷锟斤拷示锟斤拷锟侥伙拷锟斤拷群锟斤拷
		// 锟斤拷锟斤拷锟斤拷锟藉：
		 int ConversationType_OneOne = 0; // 锟斤拷锟斤拷锟斤拷之锟斤拷牡锟斤拷锟�
		 int ConversationType_Group = 1;  // 锟斤拷锟斤拷之锟斤拷锟饺猴拷锟�
		Map<String, Object> attr = new HashMap<String, Object>();
		attr.put("type", ConversationType_OneOne);

		imClient.createConversation(clientIds, attr, new AVIMConversationCreatedCallback() {
		  @Override
		  public void done(AVIMConversation conversation, AVIMException e) {
		    if (null != conversation) {
		      // 锟缴癸拷锟剿ｏ拷锟斤拷时锟斤拷锟斤拷锟斤拷锟绞撅拷曰锟斤拷锟�Activity 页锟芥（锟劫讹拷为 ChatActivity锟斤拷锟剿★拷
		    	Log.e("SecActivity", "imClientimClientimClient!!!");
		      Intent intent = new Intent(SecActivity.this, ChatActivity.class);
		     mConversation=conversation;
		      startActivity(intent);
		    }
		  }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

}
