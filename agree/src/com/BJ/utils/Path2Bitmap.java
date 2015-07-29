package com.BJ.utils;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Path2Bitmap {
	public static Bitmap convertToBitmap(String path, int w, int h) { 
//		BitmapFactory.Options opts = null;  
//		        if (w > 0 && h > 0) {  
//		          opts = new BitmapFactory.Options();  
//		           opts.inJustDecodeBounds = true;  
//		            BitmapFactory.decodeFile(path, opts);  
//		            // ����ͼƬ���ű���  
//	             final int minSideLength = Math.min(w, h);  
//		            opts.inSampleSize = computeSampleSize(opts, minSideLength,  
//		                    w * h);  
//		           opts.inJustDecodeBounds = false;  
//		            opts.inInputShareable = true;  
//	            opts.inPurgeable = true;  
//		        }  

		BitmapFactory.Options opts = new BitmapFactory.Options();
		// ����Ϊtureֻ��ȡͼƬ��С
		opts.inJustDecodeBounds = true;
//		opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		// ����Ϊ��
		BitmapFactory.decodeFile(path, opts);
		int width = opts.outWidth;
		int height = opts.outHeight;
		float scaleWidth = 0.f, scaleHeight = 0.f;
		if (width > w || height > h) {
			// ����
			scaleWidth = ((float) width) / w;
			scaleHeight = ((float) height) / h;
		}
		opts.inJustDecodeBounds = false;
		float scale = Math.max(scaleWidth, scaleHeight);
//		opts.inSampleSize = (int)scale;
		opts.inSampleSize = 4;
		opts.inTempStorage = new byte[5*1024]; //����16MB����ʱ�洢�ռ䣨�������û�û������������֤��   
		        
//		WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
		return BitmapFactory.decodeFile(path, opts);
//		return Bitmap.createBitmap(weak.get());
//		return Bitmap.createScaledBitmap(weak.get(), w, h, true);
	}

	
//	public static int computeSampleSize(BitmapFactory.Options options,  
//		         int minSideLength, int maxNumOfPixels) {  
//		    int initialSize = computeInitialSampleSize(options, minSideLength,  
//		             maxNumOfPixels);  
//		   
//		   int roundedSize;  
//	     if (initialSize <= 8) {  
//			        roundedSize = 1;  
//		         while (roundedSize < initialSize) {  
//		             roundedSize <<= 1;  
//			        }  
//		     } else {  
//		        roundedSize = (initialSize + 7) / 8 * 8;  
//		    }  
//		  
//		    return roundedSize;  
//		 }
//
//	
//	private static int computeInitialSampleSize(BitmapFactory.Options options,  
//		         int minSideLength, int maxNumOfPixels) {  
//			    double w = options.outWidth;  
//		    double h = options.outHeight;  
//			   
//		   int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math  
//			            .sqrt(w * h / maxNumOfPixels));  
//			    int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math  
//			             .floor(w / minSideLength), Math.floor(h / minSideLength));  
//			  
//			    if (upperBound < lowerBound) {  
//			         // return the larger one when there is no overlapping zone.  
//			        return lowerBound;  
//		     }  
//			   
//			   if ((maxNumOfPixels == -1) && (minSideLength == -1)) {  
//			         return 1;  
//			   } else if (minSideLength == -1) {  
//			         return lowerBound;  
//			     } else {  
//			         return upperBound;  
//			     }  
//			 } 

}
