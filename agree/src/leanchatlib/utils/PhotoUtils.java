package leanchatlib.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.BJ.utils.CircleBitmapDisplayer;
import com.biju.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * Created by lzw on 15/4/24.
 */
public class PhotoUtils {
  public static DisplayImageOptions avatarImageOptions = new DisplayImageOptions.Builder()
      .showImageOnLoading(R.drawable.agree_tabbar_setting_icon_green)
      .showImageForEmptyUri(R.drawable.agree_tabbar_setting_icon_green)
      .showImageOnFail(R.drawable.agree_tabbar_setting_icon_green)
      .cacheInMemory(true)
      .cacheOnDisc(true)
      .displayer(new CircleBitmapDisplayer())//圆形
      .considerExifParams(true)
      .imageScaleType(ImageScaleType.EXACTLY)
      .bitmapConfig(Bitmap.Config.RGB_565)
      .resetViewBeforeLoading(true)// ����ͼƬ������ǰ�Ƿ����ã���λ
          //.displayer(new RoundedBitmapDisplayer(20))
          //.displayer(new FadeInBitmapDisplayer(100))// ����
      .build();

  private static DisplayImageOptions normalImageOptions = new DisplayImageOptions.Builder()
      .showImageOnLoading(R.drawable.chat_common_empty_photo)
      .showImageForEmptyUri(R.drawable.chat_common_empty_photo)
      .showImageOnFail(R.drawable.chat_common_image_load_fail)
      .cacheInMemory(true)
      .cacheOnDisc(true)
      .considerExifParams(true)
//      .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
      .imageScaleType(ImageScaleType.EXACTLY)
      .bitmapConfig(Bitmap.Config.RGB_565)
      .resetViewBeforeLoading(true)// ����ͼƬ������ǰ�Ƿ����ã���λ
//          .displayer(new RoundedBitmapDisplayer(5))
//      .displayer(new FadeInBitmapDisplayer(100))// ����
      .build();

  public static void displayImageCacheElseNetwork(ImageView imageView,
                                                  String path, String url) {
    ImageLoader imageLoader = ImageLoader.getInstance();
    if (path != null) {
      File file = new File(path);
      if (file.exists()) {
        imageLoader.displayImage("file://" + path, imageView, normalImageOptions);
        return;
      }
    }
    imageLoader.displayImage(url, imageView, normalImageOptions);
  }

  public static String compressImage(String path, String newPath,Context context) {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(path, options);
    int inSampleSize = 1;
//    int maxSize = 3000;
    int maxSize=DensityUtil.dip2px(context, 150);
    if (options.outWidth > maxSize || options.outHeight > maxSize) {
      int widthScale = (int) Math.ceil(options.outWidth * 1.0 / maxSize);
      int heightScale = (int) Math.ceil(options.outHeight * 1.0 / maxSize);
      inSampleSize = Math.max(widthScale, heightScale);
    }
    options.inJustDecodeBounds = false;
    options.inSampleSize = inSampleSize;
    Bitmap bitmap = BitmapFactory.decodeFile(path, options);
    int w = bitmap.getWidth();
    int h = bitmap.getHeight();
    int newW = w;
    int newH = h;
    if (w > maxSize || h > maxSize) {
      if (w > h) {
        newW = maxSize;
        newH = (int) (newW * h * 1.0 / w);
      } else {
        newH = maxSize;
        newW = (int) (newH * w * 1.0 / h);
      }
    }
    Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, newW, newH, false);
    FileOutputStream outputStream = null;
    try {
      outputStream = new FileOutputStream(newPath);
      newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
    } catch (FileNotFoundException e) {
      LogUtils.logException(e);
    } finally {
      Utils.closeQuietly(outputStream);
    }
    recycle(newBitmap);
    recycle(bitmap);
    return newPath;
  }
  public static String MycompressImage(String path,String newPath,Context context,int edgeLength) {
		try {
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(
					new File(path)));
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			options.inPurgeable = true;  
//			BitmapFactory.decodeFile(path, options);
			BitmapFactory.decodeStream(in, null, options);
			in.close();
			int inSampleSize = 1;
//    int maxSize = 3000;
			int maxSize=DensityUtil.dip2px(context, 150);
			if (options.outWidth > maxSize || options.outHeight > maxSize) {
				int widthScale = (int) Math.ceil(options.outWidth * 1.0 / maxSize);
				int heightScale = (int) Math.ceil(options.outHeight * 1.0 / maxSize);
				//此处计算适合于聊天的inSampleSize！！！
				inSampleSize = Math.max(widthScale, heightScale);
			}
			
			in = new BufferedInputStream(new FileInputStream(new File(path)));
			options.inJustDecodeBounds = false;
//			options.inSampleSize = inSampleSize;
			options.inSampleSize = computeSampleSize(options, 768, 768*1280);    //测试!!!!!!!!!!!!!computeSampleSize模式
			Log.e("MycompressImage", "inSampleSize==="+inSampleSize);
			Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
//			Bitmap bitmap = BitmapFactory.decodeFile(path, options);
			int widthOrg = bitmap.getWidth();
			int heightOrg = bitmap.getHeight();
			
			//如果计算出来的还是很大 就会经过1280的截图模式
			if(widthOrg > edgeLength || heightOrg > edgeLength)
			{
				//压缩到一个最小长度是edgeLength的bitmap
				int longerEdge = (int)(edgeLength * Math.min(widthOrg, heightOrg) / Math.max(widthOrg, heightOrg));
				int scaledWidth = widthOrg > heightOrg ? edgeLength : longerEdge;
				int scaledHeight = widthOrg > heightOrg ? longerEdge : edgeLength;
				Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, false);
	        	  Log.e("MycompressImage", "newBitmap对象是否空"+newBitmap);
	        	  Log.e("MycompressImage", "限制图长=="+newBitmap.getWidth());
	        	  Log.e("MycompressImage", "限制图高=="+newBitmap.getHeight());
				FileOutputStream outputStream = null;
				try {
					outputStream = new FileOutputStream(newPath);
					newBitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream);
				} catch (FileNotFoundException e) {
					LogUtils.logException(e);
				} finally {
					Utils.closeQuietly(outputStream);
				}
				recycle(newBitmap);//回收
			}else{
				//否则就是适合于聊天的inSampleSize或者computeSampleSize的模式
				FileOutputStream outputStream = null;
				try {
					outputStream = new FileOutputStream(newPath);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream);
				} catch (FileNotFoundException e) {
					LogUtils.logException(e);
				} finally {
					Utils.closeQuietly(outputStream);
				}
				
			}
			recycle(bitmap);//回收
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return newPath;
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


  public static void recycle(Bitmap bitmap) {
    if (bitmap != null && !bitmap.isRecycled()) {
      bitmap.recycle();
    }
    System.gc();
  }
}
