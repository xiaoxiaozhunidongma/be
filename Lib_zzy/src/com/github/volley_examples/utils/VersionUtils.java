package com.github.volley_examples.utils;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

public class VersionUtils {
	private static PackageInfo info;

	public static int getCurrVersion(Context context){
		PackageManager pm = context.getPackageManager();
		try {
			info = pm.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return info.versionCode;
	}
	
	
	public static void install(Context context,String apkPath){
		Intent intent=new Intent();
		Uri data=Uri.fromFile(new File(apkPath));
		String type="application/vnd.android.package-archive";
		intent.setDataAndType(data, type);
		context.startActivity(intent);
		
	}
}
