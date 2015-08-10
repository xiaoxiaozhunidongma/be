package com.example.huanxin;

import android.app.Application;

import com.easemob.chat.EMChat;

public class TestUserApplication extends Application{
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		EMChat.getInstance().init(this);
	}
}
