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
	//������
	public static void clearCache() {
		ImageLoader.getInstance().clearDiskCache();
		ImageLoader.getInstance().clearMemoryCache();
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
				.showImageOnLoading(R.drawable.plugin_camera_no_pictures)
				.showImageForEmptyUri(R.drawable.plugin_camera_no_pictures)
				.showImageOnFail(R.drawable.ic_error)
				.cacheInMemory(true)//�Ƿ񻺴����ڴ���
//				.displayer(new RoundedBitmapDisplayer(10))//����Բ��
				.cacheOnDisk(true)//�Ƿ񻺴���SD����
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)  //��ʽ
//				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)//ImageScaleType
				.imageScaleType(ImageScaleType.EXACTLY)
				.build();
		
				
	}
	public void LoadImage2(String url, ImageView imageView) {
		//ͼƬҪ���ô�С��
		ImageLoader.getInstance().displayImage(url, imageView, options, null);
	}

}
