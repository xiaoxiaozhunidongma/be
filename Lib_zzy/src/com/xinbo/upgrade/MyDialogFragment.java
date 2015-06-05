//package com.xinbo.upgrade;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.SocketTimeoutException;
//import java.net.URL;
//
//import android.app.Dialog;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Environment;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.NotificationCompat;
//import android.text.style.LineHeightSpan.WithDensity;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.RemoteViews;
//import android.widget.TextView;
//
//import com.example.lib_zzy.R;
//import com.github.volley_examples.utils.NotifiUtils;
//
//public class MyDialogFragment extends DialogFragment {
//	private static final String APK_UPGRADE = Environment
//			.getExternalStorageDirectory() + "/lashou/upgrade/upgrade.apk";
//
//	private static final int NOTIFY_ID = 12345;
//	private NotificationManager mNotifyMgr;
//	private RemoteViews views;
//	private Notification nofity;
//	private FragmentActivity mActivity;
//	private VersionInfo verInfo;
//
//	public Dialog onCreateDialog(Bundle savedInstanceState) {
//		mActivity = getActivity();
//		 final Dialog dlg = new Dialog(getActivity(), R.style.MyDialog);
//		 dlg.setContentView(R.layout.custom_dialog);
//		TextView mTv_detailtext = (TextView) dlg.findViewById(R.id.tv_detailtext);
//		mTv_detailtext.setText(verInfo.featrue);
//		dlg.findViewById(R.id.rela_OK).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				NotifiUtils.showToast(mActivity, "确定");
//				upgrade();
//				dlg.dismiss();
//			}
//			
//		});
//		dlg.findViewById(R.id.rela_cancle).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				dlg.dismiss();
//			}
//		});
////		.setTitle("title").setIcon(R.drawable.ic_launcher)
////				.setMessage(verInfo.featrue)
////				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
////					public void onClick(DialogInterface dialog, int which) {
////						NotifiUtils.showToast(mActivity, "确定");
////						upgrade();
////					}
////				}).setNegativeButton("取消", null).create();
////
//		return dlg;
//	}
//
//	class UpgradeTask extends AsyncTask<String, Integer, String> {
//		private static final int TIME_OUT = 25000;
//
//		@Override
//		protected void onPreExecute() {
//			showNotify();
//		}
//
//		protected String doInBackground(String... params) {
//			InputStream is = null;
//			FileOutputStream fos = null;
//			try {
//				URL url = new URL(params[0]);
//				HttpURLConnection conn = (HttpURLConnection) url
//						.openConnection();
//				// conn.getContentLength()// 注意，虚拟地址不能使用这个方法获得文件大小
//				// 为http请求设置超时时间
//				conn.setConnectTimeout(TIME_OUT);
//				conn.setReadTimeout(TIME_OUT);
//				if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
//					NotifiUtils.showToast(mActivity, "服务端正在维护");
//					return null;
//				}
//				is = conn.getInputStream();
////				 conn.getContentLength()
//				File upgradeApk = new File(APK_UPGRADE);
//				if (!upgradeApk.exists()) {
//					upgradeApk.getParentFile().mkdirs();
//				}
//				fos = new FileOutputStream(upgradeApk);
//				byte[] buffer = new byte[1024];
//				int loaded = 0;
//				int len = 0;
//				int times = 0;
//				while (-1 != (len = is.read(buffer))) {
//					fos.write(buffer, 0, len);
//					// 每7%更新一次
//					loaded += len;
//					int percent = loaded * 100 /verInfo.file_len;
//					if (percent >= 7 * times){
//						times++;
//						publishProgress(loaded);
//					}
//				}
//				fos.flush();
//			} catch (SocketTimeoutException e) {
//				// 处理超时事件
//			} catch (MalformedURLException e) {
//			} catch (IOException e) {
//			} finally {
//				if (is != null) {
//					try {
//						is.close();
//					} catch (IOException e) {
//					}
//				}
//				if (fos != null) {
//					try {
//						fos.close();
//					} catch (IOException e) {
//					}
//				}
//			}
//			return null;
//		}
//
//		@Override
//		protected void onProgressUpdate(Integer... values) {
//			updateNotify(values[0]);
//		}
//
//		protected void onPostExecute(String result) {
//			// 提示安装apk
//			NotifiUtils.showToast(mActivity, "下载完成，请点击通知安装");
//			finishNotify();
//		}
//	}
//
//	protected void upgrade() {
//		UpgradeTask task = new UpgradeTask();
//		task.execute(verInfo.apk_url);
//	}
//
//	public void setVersionInfo(VersionInfo verInfo) {
//		this.verInfo = verInfo;
//	}
//
//	private void showNotify() {
//		mNotifyMgr = (NotificationManager) getActivity().getSystemService(
//				Context.NOTIFICATION_SERVICE);
//
//		Intent intent = new Intent();
//		PendingIntent contentIntent = PendingIntent.getActivity(getActivity(),
//				0, intent, 0);
//		views = new RemoteViews(getActivity().getPackageName(),
//				R.layout.custom_notify);
//		RemoteViews viewticker=new RemoteViews(getActivity().getLocalClassName(), R.layout.custom_ticker);
//		views.setTextViewText(R.id.textView1, "准备下载");
//		views.setProgressBar(R.id.progressBar1, verInfo.file_len, 0, false);
//		nofity = new NotificationCompat.Builder(getActivity())
//				.setSmallIcon(R.drawable.ic_launcher)
//				.setTicker("正在下载...",viewticker)
//				.setWhen(System.currentTimeMillis())
//				// .setContentTitle("contentTitle")
//				// .setContentText("contentText")
//				.setAutoCancel(true).setContent(views)
//				.setContentIntent(contentIntent).build();
//		mNotifyMgr.notify(NOTIFY_ID, nofity);
//
//	}
//
//	private void updateNotify(int currLen) {
//		views.setTextViewText(R.id.textView1, currLen * 100 / verInfo.file_len
//				+ "%");
//		views.setProgressBar(R.id.progressBar1, verInfo.file_len, currLen,
//				false);
//		mNotifyMgr.notify(NOTIFY_ID, nofity);
//	}
//
//	private void finishNotify() {
//		Intent intent = new Intent(Intent.ACTION_VIEW);
//		intent.setDataAndType(Uri.fromFile(new File(APK_UPGRADE)),
//				"application/vnd.android.package-archive");
//		PendingIntent contentIntent = PendingIntent.getActivity(mActivity, 0,
//				intent, 0);
//		nofity.contentIntent = contentIntent;
//		views.setTextViewText(R.id.textView1, "下载完成，点击更新");
//		// views.setImageViewResource(R.id.imageView1,
//		// android.R.drawable.ic_media_next);
//		views.setViewVisibility(R.id.progressBar1, View.INVISIBLE);
//		mNotifyMgr.notify(NOTIFY_ID, nofity);
//	}
//}
