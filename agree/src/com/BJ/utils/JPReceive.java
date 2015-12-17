package com.BJ.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.biju.login.WelComeActivity;
import com.fragment.HomeFragment;

public class JPReceive extends BroadcastReceiver{
	  private static final String TAG = "JPReceive";
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        Bundle bundle = intent.getExtras();
	        Log.e(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
	        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
	            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
	            Log.e(TAG, "[MyReceiver] 接收Registration Id 111111111: " + regId);

	        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
	            processCustomMessage(context, bundle);
	            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
	            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
	            Log.e(TAG, "[MyReceiver] 接收到推送下来的自定义消息: =========" + message);
	            Log.e(TAG, "[MyReceiver] 接收到推送下来的自定义消息: =========" + extras);
	            
	            
//	            if (!ExampleUtil.isEmpty(extras)) {
//					try {
//						JSONObject extraJson = new JSONObject(extras);
//						if (null != extraJson && extraJson.length() > 0) {
//							Log.e(TAG, "最后得出的值为1111: =========" + extras);
//							Log.e(TAG, "最后得出的值为2222: =========" + extraJson);
//							JPush jPush=GsonUtils.parseJson(extras, JPush.class);
//							String jPush_pk_group = jPush.getPk_group();
//							String jPush_type_tag = jPush.getType_tag();
//							Log.e(TAG, "得到的jPush_pk_group====="+jPush_pk_group);
//							Log.e(TAG, "得到的jPush_type_tag====="+jPush_type_tag);
////							JPReceive.setjPush_pk_group(Integer.valueOf(jPush_pk_group));
////							JPReceive.setjPush_type_tag(Integer.valueOf(jPush_type_tag));
//							
//						}
//					} catch (JSONException e) {
//
//					}
//
//				}

	        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
	            Log.e(TAG, "[MyReceiver] 接收到推送下来的通知=========");
	            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
	            Log.e(TAG, "[MyReceiver] 接收到推送下来的通知的ID:========= " + notifactionId);
	        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
	            Log.e(TAG, "[MyReceiver] 用户点击打开了通知=========");
	            //打开自定义的Activity，当然这个并不是强制要求的，当用户点击推送消息是，可以不做处理的
	            Intent i = new Intent(context, WelComeActivity.class);
	            i.putExtras(bundle);
	            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
	            context.startActivity(i);

	        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
	            Log.e(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: ===========" + bundle.getString(JPushInterface.EXTRA_EXTRA));
	            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
	        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
	            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
	            Log.e(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
	            //这个获取的是当前登录用户的连接状态。这里的连接状态是指用户与Jpush推送服务器的连接状态。

	        } else {
	            Log.e(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
	        }
	    }

	    // 打印所有的 intent extra 数据 这里只是打印获取的数据的方法
	    private static String printBundle(Bundle bundle) {
	        StringBuilder sb = new StringBuilder();
	        for (String key : bundle.keySet()) {
	            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
	                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
	            }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
	                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
	            } 
	            else {
	                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
	            }
	        }
	        return sb.toString();
	    }

	    /**
	    *send msg to MainActivity
	    *这个方法是获取到推送的消息之后再次发送广播到其他的广播接收器当中，
	    *目的就是传递获取的广播数据，进行相应的处理，实现相应的功能，开发者在这里
	    *完全可以自己实现自己想要的功能。不一定非要使用这种方式。
	    **/
	    private void processCustomMessage(Context context, Bundle bundle) {
	        if (HomeFragment.isForeground) {
	            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
	            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
	            Intent msgIntent = new Intent(HomeFragment.MESSAGE_RECEIVED_ACTION);
	            msgIntent.putExtra(HomeFragment.KEY_MESSAGE, message);
	            if (!ExampleUtil.isEmpty(extras)) {
	                try {
	                    JSONObject extraJson = new JSONObject(extras);
	                    if (null != extraJson && extraJson.length() > 0) {
	                        msgIntent.putExtra(HomeFragment.KEY_EXTRAS, extras);
	                    }
	                } catch (JSONException e) {
	                }
	            }
	            context.sendBroadcast(msgIntent);//发送广播
	        }
	    }
	    
}
