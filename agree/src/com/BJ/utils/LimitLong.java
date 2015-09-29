package com.BJ.utils;

import android.graphics.Bitmap;
import android.util.Log;

public class LimitLong {
	/**
	                                                                     
	   * @param bitmap      ԭͼ
	   * @param edgeLength  ϣ���õ��������β��ֵı߳�
	   * @return  ���Ž�ȡ���в��ֺ��λͼ��
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
	   Log.e("", "ԭͼ��=="+widthOrg);
	   Log.e("", "ԭͼ��=="+heightOrg);
	                                                                                
	   if(widthOrg > edgeLength || heightOrg > edgeLength)
	   {
	    //ѹ����һ����С������edgeLength��bitmap
	    int longerEdge = (int)(edgeLength * Math.min(widthOrg, heightOrg) / Math.max(widthOrg, heightOrg));
	    int scaledWidth = widthOrg > heightOrg ? edgeLength : longerEdge;
	    int scaledHeight = widthOrg > heightOrg ? longerEdge : edgeLength;
	                                                                                 
	          try{
	        	  result = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
	        	  int width = result.getWidth();
	        	  int height = result.getHeight();
	        	  Log.e("", "result�����Ƿ��"+result);
	        	  Log.e("", "����ͼ��=="+width);
	        	  Log.e("", "����ͼ��=="+height);
	        	  bitmap.recycle();
	          }
	          catch(Exception e){
	           return null;
	          }
	                                                                                      
	   }
	                                                                                     
	   return result;
	  }
}

