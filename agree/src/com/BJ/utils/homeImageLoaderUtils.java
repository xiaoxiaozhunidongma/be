package com.BJ.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.biju.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class homeImageLoaderUtils {
	private static homeImageLoaderUtils imageLoaderUtils = new homeImageLoaderUtils() {
	};
	private static DisplayImageOptions options;

	public static homeImageLoaderUtils getInstance() {
		return imageLoaderUtils;
	}

	public static void clearCache() {
		ImageLoader.getInstance().clearDiskCache();
		ImageLoader.getInstance().clearMemoryCache();
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
		.showImageOnLoading(R.drawable.preview_2)
		.showImageForEmptyUri(R.drawable.preview_2)
		.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory(false)//是否缓存在内存中
//		.displayer(new RoundedBitmapDisplayer(50))//设置圆角
		.cacheOnDisk(true)//是否缓存在SD卡中
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)  //格式
//		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)//ImageScaleType
		.imageScaleType(ImageScaleType.EXACTLY)
		.build();
				
	}

}
