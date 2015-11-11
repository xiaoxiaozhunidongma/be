package com.BJ.utils;

import android.graphics.Bitmap;
import android.util.Log;

public class PicCutter {
	/**
	                                                                     
	   * @param bitmap      原图
	   * @param edgeLength  希望得到的正方形部分的边长
	   * @return  缩放截取正中部分后的位图。
	   */
	  public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength)
	  {
	   if(null == bitmap || edgeLength <= 0)
	   {
	    return  null;
	   }
	                                                                                
	   Bitmap result = bitmap;
	   int widthOrg = bitmap.getWidth();
	   int heightOrg = bitmap.getHeight();
	   Log.e("PicCutter", "原图长=="+widthOrg);
	   Log.e("PicCutter", "原图高=="+heightOrg);
	                                                                                
	   if(widthOrg > edgeLength && heightOrg > edgeLength)
	   {
	    //压缩到一个最小长度是edgeLength的bitmap
	    int longerEdge = (int)(edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
	    int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
	    int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
	    Bitmap scaledBitmap;
	                                                                                 
	          try{
	           scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
	          }
	          catch(Exception e){
	           return null;
	          }
	                                                                                      
	       //从图中截取正中间的正方形部分。
	       int xTopLeft = (scaledWidth - edgeLength) / 2;
	       int yTopLeft = (scaledHeight - edgeLength) / 2;
	                                                                                    
	       try{
	        result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
      	  int width = result.getWidth();
      	  int height = result.getHeight();
      	  Log.e("PicCutter", "result对象是否空"+result);
      	  Log.e("PicCutter", "限制图长=="+width);
      	  Log.e("PicCutter", "限制图高=="+height);
	        scaledBitmap.recycle();
	       }
	       catch(Exception e){
	        return null;
	       }       
	   }
	                                                                                     
	   return result;
	  }
}

