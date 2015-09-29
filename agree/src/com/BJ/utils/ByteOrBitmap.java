package com.BJ.utils;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ByteOrBitmap {
	 public static byte[] Bitmap2Bytes(Bitmap bm) {
	       ByteArrayOutputStream baos = new ByteArrayOutputStream();
	       bm.compress(Bitmap.CompressFormat.JPEG, 75, baos);//Õº∆¨÷ ¡ø
	        return baos.toByteArray();
	  }
	 public static Bitmap Bytes2Bimap(byte[] b) {
	     if (b.length != 0) {
	         return BitmapFactory.decodeByteArray(b, 0, b.length);
	     } else {
	         return null;
	     }
	 }
}

