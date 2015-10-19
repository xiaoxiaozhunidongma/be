package com.BJ.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.biju.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class ImageLoaderUtils {
	private static ImageLoaderUtils imageLoaderUtils = new ImageLoaderUtils() {
	};
	private static DisplayImageOptions options;
	private static DisplayImageOptions optionssquare;
	private static DisplayImageOptions optionsCricular;

	public static ImageLoaderUtils getInstance() {
		return imageLoaderUtils;
	}

	public static void clearCache() {
		ImageLoader.getInstance().clearDiskCache();
		ImageLoader.getInstance().clearMemoryCache();
	}

	private ImageLoaderUtils() {// ˽�л�����
		initUILOptions();//����
		initUILOptionsSquare();//����
		initUILOptionsCricular();//Բ��
	}

	public void LoadImage(Context context, String url, ImageView imageView) {
		//ͼƬҪ���ô�С��
		ImageLoader.getInstance().displayImage(url, imageView, options, null);
	}

	private void initUILOptions() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.login_1)
				.showImageForEmptyUri(R.drawable.login_1)
				.showImageOnFail(R.drawable.ic_error)
				.cacheInMemory(true)
				.displayer(new CircleBitmapDisplayer())//Բ��
				.cacheOnDisk(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)  //��ʽ
//				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)//ImageScaleType
				.imageScaleType(ImageScaleType.EXACTLY)
				.build();
		
				
	}
	
	//���÷��ε�ͼƬ
	public void LoadImageSquare(Context context, String url, ImageView imageView) {
		//ͼƬҪ���ô�С��
		ImageLoader.getInstance().displayImage(url, imageView, optionssquare, null);
	}
	
	private void initUILOptionsSquare() {
		optionssquare = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.preview_2)
				.showImageForEmptyUri(R.drawable.preview_2)
				.showImageOnFail(R.drawable.ic_error)
				.cacheInMemory(true)
				.displayer(new RoundedBitmapDisplayer(10))//Բ��
				.cacheOnDisk(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)  //��ʽ
//				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)//ImageScaleType
				.imageScaleType(ImageScaleType.EXACTLY)
				.build();
		
				
	}
	
	//����Բ��ͼƬ
	public void LoadImageCricular(Context context, String url, ImageView imageView) {
		//ͼƬҪ���ô�С��
		ImageLoader.getInstance().displayImage(url, imageView, optionsCricular, null);
	}

	private void initUILOptionsCricular() {
		optionsCricular = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.login_1)
				.showImageForEmptyUri(R.drawable.login_1)
				.showImageOnFail(R.drawable.login_1)
				.cacheInMemory(true)
				.displayer(new CircleBitmapDisplayer())//Բ��
				.cacheOnDisk(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)  //��ʽ
//				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)//ImageScaleType
				.imageScaleType(ImageScaleType.EXACTLY)
				.build();
		
				
	}
	
}
