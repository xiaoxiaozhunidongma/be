package com.BJ.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.BJ.utils.Utils.CompressOptions;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class MyBimp {
	

	public static Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		
		BitmapFactory.decodeStream(in, null, options);
		in.close();
//		int i = 0;
		Bitmap bitmap = null;
//		while (true) {
//			//??????????????????????????????????设置500 16分之一；256 32分之一
//			if ((options.outWidth >> i <= 500)						
//					&& (options.outHeight >> i <= 500)) {
//				in = new BufferedInputStream(
//						new FileInputStream(new File(path)));
//				options.inSampleSize = (int) Math.pow(2.0D, i);
//				Log.e("MyBimp", "Math.pow(2.0D, i)=="+(int) Math.pow(2.0D, i));
////				options.inSampleSize =findBestSampleSize(actualWidth, actualHeight,
////						desiredWidth, desiredHeight);
//				options.inJustDecodeBounds = false;
//				bitmap = BitmapFactory.decodeStream(in, null, options);
//				break;
//			}
//			i += 1;
//		}
		
		in = new BufferedInputStream(new FileInputStream(new File(path)));
		options.inSampleSize = 20;
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeStream(in, null, options);
		
		return bitmap;
	}
	
	
	//decodeByteArray
//	public Bitmap getImage(String path) throws Exception {
//        URL url = new URL(path);
//        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//        conn.setReadTimeout(10 * 1000);
//        conn.setConnectTimeout(10 * 1000);
//        conn.setRequestMethod("GET");
//        InputStream is = null;
//        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
//            is = conn.getInputStream();
//        } else {
//            is = null;
//        }
//        if (is == null){
//            throw new RuntimeException("stream is null");
//        } else {
//            try {
//                byte[] data=readStream(is);
//                if(data!=null){
//                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//                    return bitmap;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            is.close();
//            return null;
//        }
//    }

/*
     * 得到图片字节流 数组大小
     * */
//    public static byte[] readStream(InputStream inStream) throws Exception{      
//        ByteArrayOutputStream outStream = new ByteArrayOutputStream();      
//        byte[] buffer = new byte[1024];      
//        int len = 0;      
//        while( (len=inStream.read(buffer)) != -1){      
//            outStream.write(buffer, 0, len);      
//        }      
//        outStream.close();      
//        inStream.close();      
//        return outStream.toByteArray();      
//    }
}
