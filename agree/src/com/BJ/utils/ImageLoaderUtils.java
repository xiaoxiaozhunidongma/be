package com.BJ.utils;

import android.content.Context;
import android.widget.ImageView;

import com.biju.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

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

	private ImageLoaderUtils() {// ˽�л�����
		initUILOptions();
	}

	public void LoadImage(Context context, String url, ImageView imageView) {
		//ͼƬҪ���ô�С��
		ImageLoader.getInstance().displayImage(url, imageView, options, null);
	}

	private void initUILOptions() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.login_1)
				.showImageForEmptyUri(R.drawable.login_1)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.displayer(new CircleBitmapDisplayer())//Բ��
				.cacheOnDisk(true).considerExifParams(true).build();
		
				
	}

}
