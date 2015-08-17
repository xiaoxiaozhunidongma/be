package com.BJ.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

public class Utils {
	private static Bitmap destBitmap;

	public static class CompressOptions {
		public static final int DEFAULT_WIDTH = 400;
		public static final int DEFAULT_HEIGHT = 800;

		public static int maxWidth = DEFAULT_WIDTH;
		public static int maxHeight = DEFAULT_HEIGHT;
		/**
		 * 压缩后图片保存的文件
		 */
		public static File destFile;
		/**
		 * 图片压缩格式,默认为jpg格式
		 */
		public CompressFormat imgFormat = CompressFormat.PNG;

		/**
		 * 图片压缩比例 默认为30
		 */
		public int quality = 50;

		public Uri uri;
	}

	public static Bitmap decodeSampledBitmap(String path, int sample) {
		// final BitmapFactory.Options options = new BitmapFactory.Options();
		// options.inJustDecodeBounds = true;
		//
		// // Calculate inSampleSize
		// options.inSampleSize = sample;
		//
		// // Decode bitmap with inSampleSize set
		// options.inJustDecodeBounds = false;
		// return BitmapFactory.decodeFile(path, options);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPurgeable = true;//这个参数不知道干嘛的。
		options.inTempStorage = new byte[16*1024];

		int actualWidth = options.outWidth;
		int actualHeight = options.outHeight;

		int desiredWidth = getResizedDimension(CompressOptions.maxWidth,
				CompressOptions.maxHeight, actualWidth, actualHeight);
		int desiredHeight = getResizedDimension(CompressOptions.maxHeight,
				CompressOptions.maxWidth, actualHeight, actualWidth);

		options.inJustDecodeBounds = false;
		options.inSampleSize = findBestSampleSize(actualWidth, actualHeight,
				desiredWidth, desiredHeight);

		Bitmap bitmap = null;
//?????????????????????????????????????????????????????????????????????????????????????????
		FileInputStream fis;
		try {
			Log.e("UUUUUUUUUUUUUUUUU", "path===="+path);
			Log.e("UUUUUUUUUUUUUUUUU!!!!", "path===="+path+"test.jpg");
			fis = new FileInputStream(new File(path));
			destBitmap = BitmapFactory.decodeStream(fis, null, options);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Bitmap destBitmap = BitmapFactory.decodeFile(path, options);

		// If necessary, scale down to the maximal acceptable size.
		if (destBitmap.getWidth() > desiredWidth
				|| destBitmap.getHeight() > desiredHeight) {
			bitmap = Bitmap.createScaledBitmap(destBitmap, desiredWidth,
					desiredHeight, true);
			destBitmap.recycle();
		} else {
			bitmap = destBitmap;
		}

		// compress file if need
		if (null != CompressOptions.destFile) {
			compressFile(new CompressOptions(), bitmap);
		}

		return bitmap;

	}

	private static void compressFile(CompressOptions compressOptions,
			Bitmap bitmap) {
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(compressOptions.destFile);
		} catch (FileNotFoundException e) {
			Log.e("ImageCompress", e.getMessage());
		}

		bitmap.compress(compressOptions.imgFormat, compressOptions.quality,
				stream);
	}

	private static int findBestSampleSize(int actualWidth, int actualHeight,
			int desiredWidth, int desiredHeight) {
		double wr = (double) actualWidth / desiredWidth;
		double hr = (double) actualHeight / desiredHeight;
		double ratio = Math.min(wr, hr);
		float n = 1.0f;
		while ((n * 2) <= ratio) {
			n *= 2;
		}

		return (int) n;
	}

	private static int getResizedDimension(int maxPrimary, int maxSecondary,
			int actualPrimary, int actualSecondary) {
		// If no dominant value at all, just return the actual.
		if (maxPrimary == 0 && maxSecondary == 0) {
			return actualPrimary;
		}

		// If primary is unspecified, scale primary to match secondary's scaling
		// ratio.
		if (maxPrimary == 0) {
			double ratio = (double) maxSecondary / (double) actualSecondary;
			return (int) (actualPrimary * ratio);
		}

		if (maxSecondary == 0) {
			return maxPrimary;
		}

		double ratio = (double) actualSecondary / (double) actualPrimary;
		int resized = maxPrimary;
		if (resized * ratio > maxSecondary) {
			resized = (int) (maxSecondary / ratio);
		}
		return resized;
	}

	public static String secondToDate(long second) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		java.util.Date dt = new Date(second * 1000);
		return sdf.format(dt);
	}
}
