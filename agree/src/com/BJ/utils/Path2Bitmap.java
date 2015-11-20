package com.BJ.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.SoftReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Path2Bitmap {
	public static Bitmap convertToBitmap(String path) throws IOException {
		
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;//颜色模式
		options.inJustDecodeBounds = true;
		options.inPurgeable = true;  
		options.inInputShareable = true;// 以上options的两个属性必须联合使用才会有效果
		options.inDither=false;   
		options.inTempStorage=new byte[32 * 1024]; 
		
		BitmapFactory.decodeStream(in, null, options);
		
		in.close();
//		Bitmap bitmap = null;
		
		in = new BufferedInputStream(new FileInputStream(new File(path)));
		options.inSampleSize = computeSampleSize(options, 768, 768*1280);
		Log.e("~convertToBitmap", "inSampleSize==="+computeSampleSize(options, 768, 768*1280));
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
//		SoftReference<Bitmap> weak = new SoftReference<Bitmap>(bitmap);//软引用会自动释放
//		Bitmap createBitmap = Bitmap.createBitmap(weak.get());
//		bitmap.recycle();//不能立马回收，byteorbitmap会问题
		in.close();
		return bitmap ;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

}
