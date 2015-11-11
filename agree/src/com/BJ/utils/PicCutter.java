package com.BJ.utils;

import android.graphics.Bitmap;
import android.util.Log;

public class PicCutter {
	/**
	                                                                     
	   * @param bitmap      ԭͼ
	   * @param edgeLength  ϣ���õ��������β��ֵı߳�
	   * @return  ���Ž�ȡ���в��ֺ��λͼ��
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
	   Log.e("PicCutter", "ԭͼ��=="+widthOrg);
	   Log.e("PicCutter", "ԭͼ��=="+heightOrg);
	                                                                                
	   if(widthOrg > edgeLength && heightOrg > edgeLength)
	   {
	    //ѹ����һ����С������edgeLength��bitmap
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
	                                                                                      
	       //��ͼ�н�ȡ���м�������β��֡�
	       int xTopLeft = (scaledWidth - edgeLength) / 2;
	       int yTopLeft = (scaledHeight - edgeLength) / 2;
	                                                                                    
	       try{
	        result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
      	  int width = result.getWidth();
      	  int height = result.getHeight();
      	  Log.e("PicCutter", "result�����Ƿ��"+result);
      	  Log.e("PicCutter", "����ͼ��=="+width);
      	  Log.e("PicCutter", "����ͼ��=="+height);
	        scaledBitmap.recycle();
	       }
	       catch(Exception e){
	        return null;
	       }       
	   }
	                                                                                     
	   return result;
	  }
}

