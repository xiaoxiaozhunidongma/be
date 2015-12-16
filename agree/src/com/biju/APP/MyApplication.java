package com.biju.APP;

import java.util.List;
import java.util.Map;

import leanchatlib.controller.ChatManager;
import leanchatlib.controller.ChatManagerAdapter;
import leanchatlib.model.UserInfo;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.BJ.javabean.Group_ReadAllUser;
import com.BJ.photo.Res;
import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.OSSServiceProvider;
import com.alibaba.sdk.android.oss.model.AccessControlList;
import com.alibaba.sdk.android.oss.model.AuthenticationType;
import com.alibaba.sdk.android.oss.model.ClientConfiguration;
import com.alibaba.sdk.android.oss.model.TokenGenerator;
import com.alibaba.sdk.android.oss.storage.OSSBucket;
import com.alibaba.sdk.android.oss.util.OSSToolKit;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMReservedMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.baidu.mapapi.SDKInitializer;
import com.biju.MainActivity;
import com.biju.R;
import com.biju.login.WelComeActivity;
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
		
		//��ʼ��OSS
		initOSS();
		// leanchat
		 AVOSCloud.initialize(this,"n5wu1wotc8hhyu6x87hktwzfzd2o3ptvpe6cvhkwnwl50a0f","at9f7qy4fp02ajzzmem1ybnp8lehjb27plh902h76lw562le");
//		 AVIMMessageManager.registerDefaultMessageHandler(new CustomMessageHandler());
		 Log.e("MyApplication", " AVOSCloud.initialize�е���~");
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

			    //�������������� leanchat Ӧ���е� ChatManagerAdapterImpl.java
		      @Override
		      public void shouldShowNotification(Context context, String selfId, AVIMConversation conversation, AVIMTypedMessage message) {
//		    	  Toast.makeText(context, "�յ���һ����Ϣ����δ����Ӧ�ĶԻ������Դ���ϵͳ֪ͨ��", Toast.LENGTH_LONG).show();
//		    		List<Group_ReadAllUser> Group_ReadAllUserList = new Select().from(Group_ReadAllUser.class).execute();
//		    	  String from = message.getFrom();
//		    	  String FromNickname="������ϵ��";
//		    	  for (int i = 0; i < Group_ReadAllUserList.size(); i++) {
//		    		  Group_ReadAllUser group_ReadAllUser = Group_ReadAllUserList.get(i);
//		    		  Integer pk_user = group_ReadAllUser.getPk_user();
//		    		  if(String.valueOf(pk_user).equals(from)){
//		    			   FromNickname = group_ReadAllUser.getNickname();                                         
//		    		  }
//		    	  }
//		    	  String content = "�յ�������";
//		    	    AVIMReservedMessageType type = AVIMReservedMessageType.getAVIMReservedMessageType(message.getMessageType());
//		    	    switch (type) {
//		    	      case TextMessageType:
//		    	        AVIMTextMessage textMsg = (AVIMTextMessage) message;
//		    	        content=textMsg.getText();
//		    	        break;
//		    	      case ImageMessageType:
//		    	        AVIMImageMessage imageMsg = (AVIMImageMessage) message;
//		    	        content="ͼƬ";
//		    	        break;
//		    	      case AudioMessageType:
//		    	        break;
//		    	      case LocationMessageType:
//		    	        break;
//		    	      default:
//		    	    	    	 content="���յ���δ֪���͵���Ϣ";
////		    	        contentLayout.requestLayout();
//		    	        break;
//		    	    }                                                  
//		    	  
//		    	    // 1���ȵõ�֪ͨ����������
//		    	   NotificationManager mNotify = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//				// 3��֪ͨ�������
//		    	  Notification notification = new Notification(R.drawable.about_us, FromNickname+":"+content, System.currentTimeMillis());
//		    	  Object attribute = conversation.getAttribute("type");
//				Intent intent = new Intent(context, MainActivity.class);
//		    	  PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent , Notification.FLAG_AUTO_CANCEL);
//		    	    // 4��������������ʾ
//		    	  notification.setLatestEventInfo(context, "�ؾ�", "�����µ���Ϣ", contentIntent );
//		    	    // 2������֪ͨ
//		    	   mNotify.notify(888, notification);
		    	   
		      }
		    });
		    
		    
 		// ��ʼ�����ݿ�
		ActiveAndroid.initialize(this);

		Res.init(this);// ��ʼ��RES

		// ��ʼ��imageloader
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY)
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

	private void initOSS() {
		ossService = OSSServiceProvider.getService();

		ossService.setApplicationContext(getApplicationContext());
		ossService.setGlobalDefaultHostId("oss-cn-hangzhou.aliyuncs.com");
//		ossService.setAuthenticationType(AuthenticationType.FEDERATION_TOKEN); // ���ü�ǩ��ʽΪSTS Token��Ȩ��ʽ
		ossService.setAuthenticationType(AuthenticationType.ORIGIN_AKSK);
//		ossService.setCustomStandardTimeWithEpochSec(100); // epochʱ�䣬��1970��1��1��00:00:00 UTC����������??????????????????
		ClientConfiguration conf = new ClientConfiguration();
		conf.setConnectTimeout(15 * 1000); // ���ý�����ʱʱ�䣬Ĭ��Ϊ30s
		conf.setSocketTimeout(15 * 1000); // ����socket��ʱʱ�䣬Ĭ��Ϊ30s
		conf.setMaxConnections(50); // ����ȫ����󲢷���������Ĭ��Ϊ50��
//		conf.setMaxConcurrentTaskNum(10); // ����ȫ����󲢷���������Ĭ��10��
		ossService.setClientConfiguration(conf);
		
		final String accessKey = "HyDprHu2BQHp7edn"; // ʵ��ʹ���У�AK/SK��Ӧ���ı����ڴ�����
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
		sampleBucket.setBucketACL(AccessControlList.PRIVATE); // ָ����Bucket�ķ���Ȩ��
		sampleBucket.setBucketHostId("image.beagree.com"); // ָ����Bucket�����������ĵ��������Ѿ���Bucket��CNAME����
		sampleBucket.setCdnAccelerateHostId("imagecdn.beagree.com"); // ����ָ��CDN����������CNAME����
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
