package com.BJ.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.SoftReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Path2Bitmap {
	public static Bitmap convertToBitmap(String path) throws IOException {
//		BitmapFactory.Options opts = null;
////		if (w > 0 && h > 0) {
//			opts = new BitmapFactory.Options();
//			opts.inJustDecodeBounds = true;
//			BitmapFactory.decodeFile(path, opts);
//			opts.inSampleSize = 1;
////			final int minSideLength = Math.max(w, h);
////			opts.inSampleSize = computeSampleSize(opts, minSideLength, w * h);
////			Log.e("Path2Bitmap", "computeSampleSize=="+computeSampleSize(opts, minSideLength, w * h));
//			opts.inJustDecodeBounds = false;
//			opts.inInputShareable = true;
//			opts.inPreferredConfig = Bitmap.Config.RGB_565;
//			opts.inPurgeable = true;
////		}
//
//
//		Bitmap decodeFile = BitmapFactory.decodeFile(path, opts);
//		WeakReference<Bitmap> weak=null;
//		if(decodeFile!=null){
//			 weak = new WeakReference<Bitmap>(decodeFile);
//			 decodeFile.recycle();
//		}
////		SoftReference<Bitmap> weak = new SoftReference<Bitmap>(decodeFile);
////		// return BitmapFactory.decodeFile(path, opts);
//			 
//		 Bitmap createBitmap = null;
//		 if(weak!=null){
//			  createBitmap = Bitmap.createBitmap(weak.get());
//		 }
//		//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
////		createBitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
////		return decodeFile;
//		return createBitmap;
		// return Bitmap.createScaledBitmap(weak.get(), w, h, true);
		
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;//颜色模式
		options.inJustDecodeBounds = true;
		
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		Bitmap bitmap = null;
		
		in = new BufferedInputStream(new FileInputStream(new File(path)));
		options.inSampleSize = 1;
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeStream(in, null, options);
		SoftReference<Bitmap> weak = new SoftReference<Bitmap>(bitmap);//软引用会自动释放
		Bitmap createBitmap = Bitmap.createBitmap(weak.get());
		return createBitmap ;
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
