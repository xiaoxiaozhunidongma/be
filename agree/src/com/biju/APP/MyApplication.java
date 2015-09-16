package com.biju.APP;

import java.util.Map;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.BJ.photo.Res;
import com.baidu.mapapi.SDKInitializer;
import com.easemob.EMCallBack;
import com.example.takephoto.DemoHXSDKHelper;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class MyApplication extends Application {

	private MyApplication myapp;
	private static String regId;

	public static String getRegId() {
		return regId;
	}

	public static void setRegId(String regId) {
		MyApplication.regId = regId;
	}


	@Override
	public void onCreate() {
		super.onCreate();
		myapp=this;
		
		// ��ʼ�����ݿ�
//		ActiveAndroid.initialize(this);

		Res.init(this);// ��ʼ��RES

		// ��ʼ��imageloader
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(false).imageScaleType(ImageScaleType.EXACTLY)
				.cacheOnDisk(true).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(

		getApplicationContext())
				.threadPoolSize(5)
				// .threadPriority(Thread.MIN_PRIORITY + 3)
				.denyCacheImageMultipleSizesInMemory()
				// ǿ�Ʋ��ܴ��ظ���ͼƬ
				// .memoryCache(new WeakMemoryCache()) //���á�����

				// .threadPriority(Thread.NORM_PRIORITY - 2)
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// .memoryCache(new LruMemoryCache((int) (6 * 1024 * 1024)))
				.memoryCache(new WeakMemoryCache())
				.memoryCacheSize((int) (2 * 1024 * 1024))
				.memoryCacheSizePercentage(13)
				// default
				// .diskCache(new UnlimitedDiscCache(cacheDir))
				// default
				.diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100)
				.diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
				.defaultDisplayImageOptions(defaultOptions).writeDebugLogs() // Remove
				.build();
		// initImageLoader(getApplicationContext());
		ImageLoader.getInstance().init(config);
		// ��ͼ��ʼ��
		SDKInitializer.initialize(this);
		// ��������
		JPushInterface.setDebugMode(true); // ���ÿ�����־,����ʱ��ر���־
		JPushInterface.init(this); // ��ʼ�� JPush

		regId = JPushInterface.getRegistrationID(MyApplication.this);
		Log.e("MyApplication", "�õ���ID===================" + regId);

		// ����
//		applicationContext = this;
//		instance = this;
//
//		/**
//		 * this function will initialize the HuanXin SDK
//		 * 
//		 * @return boolean true if caller can continue to call HuanXin related
//		 *         APIs after calling onInit, otherwise false.
//		 * 
//		 *         ���ų�ʼ��SDK��������
//		 *         ����true�����ȷ��ʼ��������false���������Ϊfalse�����ں����ĵ����в�Ҫ�����κκͻ�����صĴ���
//		 * 
//		 *         for example: ���ӣ�
//		 * 
//		 *         public class DemoHXSDKHelper extends HXSDKHelper
//		 * 
//		 *         HXHelper = new DemoHXSDKHelper();
//		 *         if(HXHelper.onInit(context)){ // do HuanXin related work }
//		 */
//		hxSDKHelper.onInit(applicationContext);

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

	// ���ų�ʼ��
	public static Context applicationContext;
	private static MyApplication instance;
	// login user name
	public final String PREF_USERNAME = "username";

	/**
	 * ��ǰ�û�nickname,Ϊ��ƻ�����Ͳ���userid�����ǳ�
	 */
	public static String currentUserNick = "";
	public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();

	public static MyApplication getInstance() {
		return instance;
	}

	/**
	 * ��ȡ�ڴ��к���user list
	 *
	 * @return
	 */
	public Map<String, com.example.domain.User> getContactList() {
		return hxSDKHelper.getContactList();
	}

	/**
	 * ���ú���user list���ڴ���
	 *
	 * @param contactList
	 */
	public void setContactList(Map<String, com.example.domain.User> contactList) {
		hxSDKHelper.setContactList(contactList);
	}

	/**
	 * ��ȡ��ǰ��½�û���
	 *
	 * @return
	 */
	public String getUserName() {
		return hxSDKHelper.getHXId();
	}

	/**
	 * ��ȡ����
	 *
	 * @return
	 */
	public String getPassword() {
		return hxSDKHelper.getPassword();
	}

	/**
	 * �����û���
	 *
	 * @param user
	 */
	public void setUserName(String username) {
		hxSDKHelper.setHXId(username);
	}

	/**
	 * �������� �����ʵ������ ֻ��demo��ʵ�ʵ�Ӧ������Ҫ��password ���ܺ���� preference ����sdk
	 * �ڲ����Զ���¼��Ҫ�����룬�Ѿ����ܴ洢��
	 *
	 * @param pwd
	 */
	public void setPassword(String pwd) {
		hxSDKHelper.setPassword(pwd);
	}

	/**
	 * �˳���¼,�������
	 */
	public void logout(final EMCallBack emCallBack) {
		// �ȵ���sdk logout��������app���Լ�������
		hxSDKHelper.logout(emCallBack);
	}

}
