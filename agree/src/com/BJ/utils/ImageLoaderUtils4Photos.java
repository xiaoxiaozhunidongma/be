package com.BJ.utils;

import android.content.Context;
import android.widget.ImageView;

import com.biju.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class ImageLoaderUtils4Photos {
	private static ImageLoaderUtils4Photos imageLoaderUtils = new ImageLoaderUtils4Photos() {
	};
	private static DisplayImageOptions options;

	public static ImageLoaderUtils4Photos getInstance() {
		return imageLoaderUtils;
	}

	public static void clearCache() {
		ImageLoader.getInstance().clearDiskCache();
	}

	private ImageLoaderUtils4Photos() {// ˽�л�����
		initUILOptions();
	}

	public void LoadImage(Context context, String url, ImageView imageView) {
		//ͼƬҪ���ô�С��
		ImageLoader.getInstance().displayImage(url, imageView, options, null);
	}

	private void initUILOptions() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.preview_2)
				.showImageForEmptyUri(R.drawable.preview_2)
				.showImageOnFail(R.drawable.ic_error)
				.cacheInMemory(false)//�Ƿ񻺴����ڴ���
				.displayer(new RoundedBitmapDisplayer(50))//����Բ��
				.cacheOnDisk(true)//�Ƿ񻺴���SD����
				.considerExifParams(true)
				.build();
		
				
	}

}
