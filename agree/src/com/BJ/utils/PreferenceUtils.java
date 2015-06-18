package com.BJ.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {

	public static void saveShowSummary(Context context, boolean isChecked) {
		SharedPreferences sp = context.getSharedPreferences(
				Constants.Settings.FILE_NAME, Context.MODE_PRIVATE);
		sp.edit().putBoolean(Constants.Settings.KEY_LIST_MODE, isChecked)
				.commit();
	}

	public static boolean readShowSummary(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				Constants.Settings.FILE_NAME, Context.MODE_PRIVATE);
		boolean isShowSummary = sp.getBoolean(Constants.Settings.KEY_LIST_MODE,
				false);
		return isShowSummary;

	}

	public static void saveWIFIMode(Context context, int mode) {
		SharedPreferences sp = context.getSharedPreferences(
				Constants.Settings.FILE_NAME, Context.MODE_PRIVATE);
		sp.edit().putInt(Constants.Settings.KEY_WIFI_MODE, mode).commit();
	}

	public static int readWIFIMode(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				Constants.Settings.FILE_NAME, Context.MODE_PRIVATE);
		int wifiMode = sp.getInt(Constants.Settings.KEY_WIFI_MODE, 1);
		return wifiMode;

	}
	
	public static void saveImageCache(Context context, String url) {
		SharedPreferences sp = context.getSharedPreferences(
				Constants.Settings.FILE_NAME, Context.MODE_PRIVATE);
		sp.edit().putString(Constants.Settings.KEY_WIFI_MODE, url).commit();
	}
	
	public static String readImageCache(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				Constants.Settings.FILE_NAME, Context.MODE_PRIVATE);
		String url = sp.getString(Constants.Settings.KEY_WIFI_MODE, "kong");
		return url;

	}

}
