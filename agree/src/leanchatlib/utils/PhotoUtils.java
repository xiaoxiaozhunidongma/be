package leanchatlib.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.BJ.utils.CircleBitmapDisplayer;
import com.biju.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * Created by lzw on 15/4/24.
 */
public class PhotoUtils {
  public static DisplayImageOptions avatarImageOptions = new DisplayImageOptions.Builder()
      .showImageOnLoading(R.drawable.chat_default_user_avatar)
      .showImageForEmptyUri(R.drawable.chat_default_user_avatar)
      .showImageOnFail(R.drawable.chat_default_user_avatar)
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
          //.displayer(new RoundedBitmapDisplayer(20))
      .displayer(new FadeInBitmapDisplayer(100))// ����
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

  public static void recycle(Bitmap bitmap) {
    // ���ж��Ƿ��Ѿ�����
    if (bitmap != null && !bitmap.isRecycled()) {
      // ���ղ�����Ϊnull
      bitmap.recycle();
    }
    System.gc();
  }
}
