//package com.xinbo.upgrade;
//
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.content.pm.PackageManager.NameNotFoundException;
//import android.support.v4.app.FragmentActivity;
//
//import com.android.volley.VolleyError;
//import com.github.volley_examples.app.MyVolley;
//import com.github.volley_examples.app.VolleyListenner;
//import com.github.volley_examples.utils.GsonUtils;
//
//public final class UpgradeUtils {
//	private static FragmentActivity mFragAct;
//
//	public static void check(FragmentActivity fragAct, String url){
//		mFragAct = fragAct; 
//		MyVolley.get(mFragAct, url, new VolleyListenner() {
//			public void onErrorResponse(VolleyError arg0) {
//			}
//
//			public void onResponse(String response) {
//				VersionInfo verInfo = GsonUtils.parseJson(response,
//						VersionInfo.class);
//				int currVersion = getCurrVersion();
//				if (verInfo.new_version > currVersion) {
//					// json
//					MyDialogFragment dlg = new MyDialogFragment();
//					dlg.setVersionInfo(verInfo);
//					dlg.show(mFragAct.getSupportFragmentManager(), null);
//				}
//			}
//		});
//	}
//
//
//	public static int getCurrVersion() {
//		PackageManager pm = mFragAct.getPackageManager();
//		try {
//			PackageInfo packageInfo = pm.getPackageInfo(mFragAct.getPackageName(),
//					0);
//			return packageInfo.versionCode;
//		} catch (NameNotFoundException e) {
//		}
//		return 0;
//	}
//	
//	private UpgradeUtils(){}
//}
