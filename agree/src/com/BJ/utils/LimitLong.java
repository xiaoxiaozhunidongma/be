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
	   Log.e("LimitLong", "ԭͼ��=="+widthOrg);
	   Log.e("LimitLong", "ԭͼ��=="+heightOrg);
	                                                                                
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
	        	  Log.e("LimitLong", "result�����Ƿ��"+result);
	        	  Log.e("LimitLong", "����ͼ��=="+width);
	        	  Log.e("LimitLong", "����ͼ��=="+height);
//	        	  bitmap.recycle();//�ͷ�ͼƬ�������ﲻ���ͷţ�����Ļ���Compass�����Ѿ����ͷŵ�����
	          }
	          catch(Exception e){
	           return null;
	          }
	                                                                                      
	   }
	                                                                                     
	   return result;
	  }
}

