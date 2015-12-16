package com.BJ.utils;

import android.app.Activity;

public class DisplayMetrics {
	private static android.util.DisplayMetrics metric;

	public static void DisplayMetrics(Activity activity) {
		metric = new android.util.DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
	}

	public static int Width() {
		int width = metric.widthPixels; // ÆÁÄ»¿í¶È£¨ÏñËØ£©
		return width;

	}

	public static int Height() {
		int height = metric.heightPixels; // ÆÁÄ»¸ß¶È£¨ÏñËØ£©
		return height;

	}
}
