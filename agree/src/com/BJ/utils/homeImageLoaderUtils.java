package com.BJ.utils;

import android.content.Context;
import android.widget.ImageView;

import com.biju.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class homeImageLoaderUtils {
	private static homeImageLoaderUtils imageLoaderUtils = new homeImageLoaderUtils() {
	};
	private static DisplayImageOptions options;

	public static homeImageLoaderUtils getInstance() {
		return imageLoaderUtils;
	}

	public static void clearCache() {
		ImageLoader.getInstance().clearDiskCache();
	}

	private homeImageLoaderUtils() {// 私有化构造
		initUILOptions();
	}

	public void LoadImage(Context context, String url, ImageView imageView) {
		//图片要设置大小！
		ImageLoader.getInstance().displayImage(url, imageView, options, null);
	}

	private void initUILOptions() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.login_1)
				.showImageForEmptyUri(R.drawable.login_1)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
//				.displayer(new CircleBitmapDisplayer())//圆形
				.cacheOnDisk(true).considerExifParams(true).build();
		
				
	}

}
