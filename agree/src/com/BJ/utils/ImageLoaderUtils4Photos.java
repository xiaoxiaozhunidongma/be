package com.BJ.utils;

import uk.co.senab.photoview.PhotoView;
import android.content.Context;
import android.widget.ImageView;

import com.BJ.photo.AlbumGridViewAdapter;
import com.BJ.photo.FolderAdapter;
import com.biju.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.graphics.Bitmap;

public class ImageLoaderUtils4Photos {
	private static ImageLoaderUtils4Photos imageLoaderUtils = new ImageLoaderUtils4Photos() {
	};
	private static DisplayImageOptions options;

	public static ImageLoaderUtils4Photos getInstance() {
		return imageLoaderUtils;
	}
	//清理缓存
	public static void clearCache() {
		ImageLoader.getInstance().clearDiskCache();
		ImageLoader.getInstance().clearMemoryCache();
	}

	private ImageLoaderUtils4Photos() {// 私有化构造
		initUILOptions();
	}

	public void LoadImage(Context context, String url, ImageView imageView) {
		//图片要设置大小！
		ImageLoader.getInstance().displayImage(url, imageView, options, null);
	}

	private void initUILOptions() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.plugin_camera_no_pictures)
				.showImageForEmptyUri(R.drawable.plugin_camera_no_pictures)
				.showImageOnFail(R.drawable.ic_error)
				.cacheInMemory(true)//是否缓存在内存中
//				.displayer(new RoundedBitmapDisplayer(10))//设置圆角
				.cacheOnDisk(true)//是否缓存在SD卡中
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)  //格式
//				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)//ImageScaleType
				.imageScaleType(ImageScaleType.EXACTLY)
				.build();
		
				
	}
	public void LoadImage2(String url, ImageView imageView) {
		//图片要设置大小！
		ImageLoader.getInstance().displayImage(url, imageView, options, null);
	}

}
