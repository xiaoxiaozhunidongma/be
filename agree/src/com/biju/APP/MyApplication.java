package com.biju.APP;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

import com.baidu.mapapi.SDKInitializer;
import com.biju.MainActivity;
import com.biju.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MyApplication extends Application {

	private static String regId;

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
		//初始化imageloader
		ImageLoaderConfiguration config = new
				ImageLoaderConfiguration .Builder(getApplicationContext())
				.threadPoolSize(5)
				.threadPriority(Thread.MIN_PRIORITY + 3)
				.denyCacheImageMultipleSizesInMemory()//强制不能存重复的图片
//				.memoryCache(new WeakMemoryCache()) //设置。。。
				.build();
//		initImageLoader(getApplicationContext());
		ImageLoader.getInstance().init(config);
		// 地图初始化
		SDKInitializer.initialize(this);
		// 极光推送
		JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush
		
		regId = JPushInterface.getRegistrationID(MyApplication.this);
		Log.e("MyApplication", "得到的ID===================" + regId);
		
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
