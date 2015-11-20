package com.biju.APP;

import java.util.List;
import java.util.Map;

import leanchatlib.controller.ChatManager;
import leanchatlib.controller.ChatManagerAdapter;
import leanchatlib.model.UserInfo;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.BJ.photo.Res;
import com.activeandroid.ActiveAndroid;
import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.OSSServiceProvider;
import com.alibaba.sdk.android.oss.model.AccessControlList;
import com.alibaba.sdk.android.oss.model.AuthenticationType;
import com.alibaba.sdk.android.oss.model.ClientConfiguration;
import com.alibaba.sdk.android.oss.model.TokenGenerator;
import com.alibaba.sdk.android.oss.storage.OSSBucket;
import com.alibaba.sdk.android.oss.util.OSSToolKit;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
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

public class MyApplication extends Application {

	private static String regId;

	public static String getRegId() {
		return regId;
	}

	public static void setRegId(String regId) {
		MyApplication.regId = regId;
	}
	
	private static OSSService ossService;
	private static OSSBucket sampleBucket;

	public static  OSSService getOssService() {
		return ossService;
	}

	public  void setOssService(OSSService ossService) {
		this.ossService = ossService;
	}

	public static OSSBucket getSampleBucket() {
		return sampleBucket;
	}
	
	public void setSampleBucket(OSSBucket sampleBucket) {
		this.sampleBucket = sampleBucket;
	}


	@Override
	public void onCreate() {
		super.onCreate();
		
		//初始化OSS
		initOSS();
		// leanchat
		 AVOSCloud.initialize(this,"n5wu1wotc8hhyu6x87hktwzfzd2o3ptvpe6cvhkwnwl50a0f","at9f7qy4fp02ajzzmem1ybnp8lehjb27plh902h76lw562le");
//		 AVIMMessageManager.registerDefaultMessageHandler(new CustomMessageHandler());
		 Log.e("MyApplication", " AVOSCloud.initialize有调用~");
		    ChatManager.setDebugEnabled(true);// tag leanchatlib
		    AVOSCloud.setDebugLogEnabled(true);  // set false when release
		    ChatManager.getInstance().init(this);
		    ChatManager.getInstance().setChatManagerAdapter(new ChatManagerAdapter() {
		      @Override
		      public UserInfo getUserInfoById(String userId) {
		        UserInfo userInfo = new UserInfo();
		        userInfo.setUsername(userId);
		        userInfo.setAvatarUrl("http://ac-x3o016bx.clouddn.com/86O7RAPx2BtTW5zgZTPGNwH9RZD5vNDtPm1YbIcu");
		        return userInfo;
		      }

		      @Override
		      public void cacheUserInfoByIdsInBackground(List<String> userIds) throws Exception {
		      }

			    //关于这个方法请见 leanchat 应用中的 ChatManagerAdapterImpl.java
		      @Override
		      public void shouldShowNotification(Context context, String selfId, AVIMConversation conversation, AVIMTypedMessage message) {
//		    	  Toast.makeText(context, "收到了一条消息但并未打开相应的对话。可以触发系统通知。", Toast.LENGTH_LONG).show();
		      }
		    });
		
		// 初始化数据库
		ActiveAndroid.initialize(this);

		Res.init(this);// 初始化RES

		// 初始化imageloader
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY)
				.cacheOnDisk(true).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
		getApplicationContext())
				.threadPoolSize(5)
				// .threadPriority(Thread.MIN_PRIORITY + 3)
				.denyCacheImageMultipleSizesInMemory()
				// 强制不能存重复的图片
				// .memoryCache(new WeakMemoryCache()) //设置。。。

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
		ImageLoader.getInstance().init(config);
		// 地图初始化
		SDKInitializer.initialize(this);
		// 极光推送
		JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush

		regId = JPushInterface.getRegistrationID(MyApplication.this);
		Log.e("MyApplication", "得到的ID===================" + regId);

		// 环信
//		applicationContext = this;
//		instance = this;
//
//		/**
//		 * this function will initialize the HuanXin SDK
//		 * 
//		 * @return boolean true if caller can continue to call HuanXin related
//		 *         APIs after calling onInit, otherwise false.
//		 * 
//		 *         环信初始化SDK帮助函数
//		 *         返回true如果正确初始化，否则false，如果返回为false，请在后续的调用中不要调用任何和环信相关的代码
//		 * 
//		 *         for example: 例子：
//		 * 
//		 *         public class DemoHXSDKHelper extends HXSDKHelper
//		 * 
//		 *         HXHelper = new DemoHXSDKHelper();
//		 *         if(HXHelper.onInit(context)){ // do HuanXin related work }
//		 */
//		hxSDKHelper.onInit(applicationContext);

	}

	private void initOSS() {
		ossService = OSSServiceProvider.getService();

		ossService.setApplicationContext(getApplicationContext());
		ossService.setGlobalDefaultHostId("oss-cn-hangzhou.aliyuncs.com");
//		ossService.setAuthenticationType(AuthenticationType.FEDERATION_TOKEN); // 设置加签方式为STS Token鉴权方式
		ossService.setAuthenticationType(AuthenticationType.ORIGIN_AKSK);
//		ossService.setCustomStandardTimeWithEpochSec(100); // epoch时间，从1970年1月1日00:00:00 UTC经过的秒数??????????????????
		ClientConfiguration conf = new ClientConfiguration();
		conf.setConnectTimeout(15 * 1000); // 设置建连超时时间，默认为30s
		conf.setSocketTimeout(15 * 1000); // 设置socket超时时间，默认为30s
		conf.setMaxConnections(50); // 设置全局最大并发连接数，默认为50个
//		conf.setMaxConcurrentTaskNum(10); // 设置全局最大并发任务数，默认10个
		ossService.setClientConfiguration(conf);
		
		final String accessKey = "HyDprHu2BQHp7edn"; // 实际使用中，AK/SK不应明文保存在代码中
		final String secretKey = "loWKqemVvcWH7u2RSn4EncVCkRuQcJ";
		ossService.setGlobalDefaultTokenGenerator(new TokenGenerator() {
		    @Override
		    public String generateToken(String httpMethod, String md5, String type, String date, String ossHeaders,
		            String resource) {
		        String content = httpMethod + "\n" + md5 + "\n" + type + "\n" + date
		                + "\n" + ossHeaders + resource;
		        Log.e("MyAPP", "content==="+content);
		        String generateToken = OSSToolKit.generateToken(accessKey, secretKey, content);
		        Log.e("MyAPP", "generateToken==="+generateToken);
				return generateToken;
		    }
		});
		
		
		sampleBucket = ossService.getOssBucket("agree");
		sampleBucket.setBucketACL(AccessControlList.PRIVATE); // 指明该Bucket的访问权限
		sampleBucket.setBucketHostId("image.beagree.com"); // 指明该Bucket所在数据中心的域名或已经绑定Bucket的CNAME域名
		sampleBucket.setCdnAccelerateHostId("imagecdn.beagree.com"); // 设置指向CDN加速域名的CNAME域名
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

	// 环信初始化
	public static Context applicationContext;
	private static MyApplication instance;
	// login user name
	public final String PREF_USERNAME = "username";

	/**
	 * 当前用户nickname,为了苹果推送不是userid而是昵称
	 */
	public static String currentUserNick = "";
	public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();

	public static MyApplication getInstance() {
		return instance;
	}

	/**
	 * 获取内存中好友user list
	 *
	 * @return
	 */
	public Map<String, com.example.domain.User> getContactList() {
		return hxSDKHelper.getContactList();
	}

	/**
	 * 设置好友user list到内存中
	 *
	 * @param contactList
	 */
	public void setContactList(Map<String, com.example.domain.User> contactList) {
		hxSDKHelper.setContactList(contactList);
	}

	/**
	 * 获取当前登陆用户名
	 *
	 * @return
	 */
	public String getUserName() {
		return hxSDKHelper.getHXId();
	}

	/**
	 * 获取密码
	 *
	 * @return
	 */
	public String getPassword() {
		return hxSDKHelper.getPassword();
	}

	/**
	 * 设置用户名
	 *
	 * @param user
	 */
	public void setUserName(String username) {
		hxSDKHelper.setHXId(username);
	}

	/**
	 * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
	 * 内部的自动登录需要的密码，已经加密存储了
	 *
	 * @param pwd
	 */
	public void setPassword(String pwd) {
		hxSDKHelper.setPassword(pwd);
	}

	/**
	 * 退出登录,清空数据
	 */
	public void logout(final EMCallBack emCallBack) {
		// 先调用sdk logout，在清理app中自己的数据
		hxSDKHelper.logout(emCallBack);
	}

}
