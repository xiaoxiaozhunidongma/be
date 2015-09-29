package com.BJ.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.biju.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class homeImageLoaderUtils {
	private static homeImageLoaderUtils imageLoaderUtils = new homeImageLoaderUtils() {
	};
	private static DisplayImageOptions options;

	public static homeImageLoaderUtils getInstance() {
		return imageLoaderUtils;
	}

	public static void clearCache() {
		Log.e("homeImageLoaderUtils", "�е��õ������==============================");
		ImageLoader.getInstance().clearDiskCache();
		ImageLoader.getInstance().clearMemoryCache();
	}

	private homeImageLoaderUtils() {// ˽�л�����
		initUILOptions();
	}

	public void LoadImage(Context context, String url, ImageView imageView) {
		//ͼƬҪ���ô�С��
		ImageLoader.getInstance().displayImage(url, imageView, options);
	}

	private void initUILOptions() {
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.preview_2)
		.showImageForEmptyUri(R.drawable.preview_2)
		.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory(true)//�Ƿ񻺴����ڴ���
		.displayer(new RoundedBitmapDisplayer(10))//����Բ��
		.cacheOnDisk(true)//�Ƿ񻺴���SD����
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)  //��ʽ
//		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)//ImageScaleType
		.imageScaleType(ImageScaleType.EXACTLY)
		.build();
				
	}

}
