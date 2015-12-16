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
	            Log.e(TAG, "[MyReceiver] ����Registration Id 111111111: " + regId);

	        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
	            processCustomMessage(context, bundle);
	            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
	            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
	            Log.e(TAG, "[MyReceiver] ���յ������������Զ�����Ϣ: =========" + message);
	            Log.e(TAG, "[MyReceiver] ���յ������������Զ�����Ϣ: =========" + extras);
	            
	            
//	            if (!ExampleUtil.isEmpty(extras)) {
//					try {
//						JSONObject extraJson = new JSONObject(extras);
//						if (null != extraJson && extraJson.length() > 0) {
//							Log.e(TAG, "���ó���ֵΪ1111: =========" + extras);
//							Log.e(TAG, "���ó���ֵΪ2222: =========" + extraJson);
//							JPush jPush=GsonUtils.parseJson(extras, JPush.class);
//							String jPush_pk_group = jPush.getPk_group();
//							String jPush_type_tag = jPush.getType_tag();
//							Log.e(TAG, "�õ���jPush_pk_group====="+jPush_pk_group);
//							Log.e(TAG, "�õ���jPush_type_tag====="+jPush_type_tag);
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
	            Log.e(TAG, "[MyReceiver] ���յ�����������֪ͨ=========");
	            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
	            Log.e(TAG, "[MyReceiver] ���յ�����������֪ͨ��ID:========= " + notifactionId);
	        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
	            Log.e(TAG, "[MyReceiver] �û��������֪ͨ=========");
	            //���Զ����Activity����Ȼ���������ǿ��Ҫ��ģ����û����������Ϣ�ǣ����Բ��������
	            Intent i = new Intent(context, WelComeActivity.class);
	            i.putExtras(bundle);
	            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
	            context.startActivity(i);

	        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
	            Log.e(TAG, "[MyReceiver] �û��յ���RICH PUSH CALLBACK: ===========" + bundle.getString(JPushInterface.EXTRA_EXTRA));
	            //��������� JPushInterface.EXTRA_EXTRA �����ݴ�����룬������µ�Activity�� ��һ����ҳ��..
	        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
	            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
	            Log.e(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
	            //�����ȡ���ǵ�ǰ��¼�û�������״̬�����������״̬��ָ�û���Jpush���ͷ�����������״̬��

	        } else {
	            Log.e(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
	        }
	    }

	    // ��ӡ���е� intent extra ���� ����ֻ�Ǵ�ӡ��ȡ�����ݵķ���
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
	    *��������ǻ�ȡ�����͵���Ϣ֮���ٴη��͹㲥�������Ĺ㲥���������У�
	    *Ŀ�ľ��Ǵ��ݻ�ȡ�Ĺ㲥���ݣ�������Ӧ�Ĵ���ʵ����Ӧ�Ĺ��ܣ�������������
	    *��ȫ�����Լ�ʵ���Լ���Ҫ�Ĺ��ܡ���һ����Ҫʹ�����ַ�ʽ��
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
	            context.sendBroadcast(msgIntent);//���͹㲥
	        }
	    }
	    
}
