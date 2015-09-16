package leanchatlib.utils;

import java.io.ByteArrayOutputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Path2Bitmap {
	public static Bitmap convertToBitmap(String path, int w, int h) {
		BitmapFactory.Options opts = null;
		if (w > 0 && h > 0) {
			opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, opts);
			final int minSideLength = Math.max(w, h);
			opts.inSampleSize = computeSampleSize(opts, minSideLength, w * h);
			Log.e("Path2Bitmap", "computeSampleSize=="+computeSampleSize(opts, minSideLength, w * h));
			opts.inJustDecodeBounds = false;
			opts.inInputShareable = true;
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			opts.inPurgeable = true;
		}


		Bitmap decodeFile = BitmapFactory.decodeFile(path, opts);
		WeakReference<Bitmap> weak=null;
		if(decodeFile!=null){
			 weak = new WeakReference<Bitmap>(decodeFile);
		}
//		SoftReference<Bitmap> weak = new SoftReference<Bitmap>(decodeFile);
//		// return BitmapFactory.decodeFile(path, opts);
			 
		 Bitmap createBitmap = null;
		 if(weak!=null){
			  createBitmap = Bitmap.createBitmap(weak.get());
		 }
		//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		createBitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
//		return decodeFile;
		return createBitmap;
		// return Bitmap.createScaledBitmap(weak.get(), w, h, true);
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
