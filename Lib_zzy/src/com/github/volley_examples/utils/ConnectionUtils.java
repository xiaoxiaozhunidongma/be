package com.github.volley_examples.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class ConnectionUtils {
	public static boolean isWifiConnected(Context context) {
		String type = getNetApn(context);
		return WIFI.equals(type);
	}

	public static boolean isMobileConnected(Context context) {
		String type = getNetApn(context);
		return !WIFI.equals(type);
	}

	// ÍøÂç×´Ì¬
	private static final String UNKNWON = "unkwon";
	private static final String NOT_AVAILABLE = "not_avaible";
	private static final String WIFI = "wifi";
	private static final String G3NET = "3gnet";
	private static final String G3WAP = "3gwap";
	private static final String UNINET = "uninet";
	private static final String UNIWAP = "uniwap";
	private static final String CMNET = "cmnet";
	private static final String CMWAP = "cmwap";
	private static final String CTNET = "ctnet";
	private static final String CTWAP = "ctwap";
	private static final String MOBILE = "mobile";

	private static String getNetApn(Context context) {
		if (context
				.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == PackageManager.PERMISSION_DENIED) {
			return UNKNWON;
		}
		ConnectivityManager connectivitymanager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			return NOT_AVAILABLE;
		}

		if (networkinfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return WIFI;
		}
		String netInfo = networkinfo.getExtraInfo();
		if (netInfo == null) {
			return UNKNWON;
		}
		netInfo = netInfo.toLowerCase();
		if (netInfo.equals("cmnet")) {
			return CMNET;
		} else if (netInfo.equals("cmwap")) {
			return CMWAP;
		} else if (netInfo.equals("3gnet")) {
			return G3NET;
		} else if (netInfo.equals("3gwap")) {
			return G3WAP;
		} else if (netInfo.equals("uninet")) {
			return UNINET;
		} else if (netInfo.equals("uniwap")) {
			return UNIWAP;
		} else if (netInfo.equals("ctnet")) {
			return CTNET;
		} else if (netInfo.equals("ctwap")) {
			return CTWAP;
		} else {
			return MOBILE;
		}
	}
}
