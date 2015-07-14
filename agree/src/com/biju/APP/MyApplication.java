package com.biju.APP;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.BJ.javabean.PicSignBack;
import com.BJ.javabean.User;
import com.BJ.utils.Utils;
import com.baidu.mapapi.SDKInitializer;
import com.biju.Interface;
import com.biju.Interface.UserInterface;
import com.biju.MainActivity;
import com.biju.login.RegisteredActivity;
import com.github.volley_examples.utils.GsonUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MyApplication extends Application {

	private static String regId;
	private Interface getsignInter;
	private String GetSign;

	public static String getRegId() {
		return regId;
	}


	public static void setRegId(String regId) {
		MyApplication.regId = regId;
	}


	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		initImageLoader(getApplicationContext());
		// 地图初始化
		SDKInitializer.initialize(this);
		// 极光推送
		JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush
		
		regId = JPushInterface.getRegistrationID(MyApplication.this);
		Log.e("MyApplication", "得到的ID===================" + regId);
		
		getsignInter = new Interface();
		getsignInter.setPostListener(new UserInterface() {
			
			@Override
			public void success(String A) {
				Log.e("MyApplication", "签名字符串StringSign:"+A);
				PicSignBack picSignBack = GsonUtils.parseJson(A, PicSignBack.class);
				String returnData = picSignBack.getReturnData();
				RegisteredActivity.setSIGN(returnData);
			}
			
			@Override
			public void defail(Object B) {
				
			}
		});
		getsignInter.getPicSign(this, new User());
	}


	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024)
				// 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

}
