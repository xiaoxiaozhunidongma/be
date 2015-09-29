package com.BJ.utils;

import android.graphics.Bitmap;
import android.util.Log;

public class LimitLong {
	/**
	                                                                     
	   * @param bitmap      原图
	   * @param edgeLength  希望得到的正方形部分的边长
	   * @return  缩放截取正中部分后的位图。
	   */
	  public static Bitmap limitLongScaleBitmap(Bitmap bitmap, int edgeLength)
	  {
	   if(null == bitmap || edgeLength <= 0)
	   {
	    return  null;
	   }
	                                                                                
	   Bitmap result = bitmap;
	   int widthOrg = bitmap.getWidth();
	   int heightOrg = bitmap.getHeight();
	   Log.e("", "原图长=="+widthOrg);
	   Log.e("", "原图高=="+heightOrg);
	                                                                                
	   if(widthOrg > edgeLength || heightOrg > edgeLength)
	   {
	    //压缩到一个最小长度是edgeLength的bitmap
	    int longerEdge = (int)(edgeLength * Math.min(widthOrg, heightOrg) / Math.max(widthOrg, heightOrg));
	    int scaledWidth = widthOrg > heightOrg ? edgeLength : longerEdge;
	    int scaledHeight = widthOrg > heightOrg ? longerEdge : edgeLength;
	                                                                                 
	          try{
	        	  result = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
	        	  int width = result.getWidth();
	        	  int height = result.getHeight();
	        	  Log.e("", "result对象是否空"+result);
	        	  Log.e("", "限制图长=="+width);
	        	  Log.e("", "限制图高=="+height);
	        	  bitmap.recycle();
	          }
	          catch(Exception e){
	           return null;
	          }
	                                                                                      
	   }
	                                                                                     
	   return result;
	  }
}

