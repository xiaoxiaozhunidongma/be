package com.BJ.utils;

import android.content.Context;
import android.widget.ImageView;

import com.biju.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageLoaderUtils {
	private static ImageLoaderUtils imageLoaderUtils = new ImageLoaderUtils() {
	};
	private static DisplayImageOptions options;

	public static ImageLoaderUtils getInstance() {
		return imageLoaderUtils;
	}

	public static void clearCache() {
		ImageLoader.getInstance().clearDiskCache();
	}

	private ImageLoaderUtils() {// 私有化构造
		initUILOptions();
	}

	public void LoadImage(Context context, String url, ImageView imageView) {
		//图片要设置大小！
		ImageLoader.getInstance().displayImage(url, imageView, options, null);
	}

	private void initUILOptions() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true).build();
	}

}
