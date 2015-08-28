package com.BJ.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import com.biju.login.RegisteredActivity;
import com.fragment.SettingFragment;

public class InitHead {
	// 对图片进行修改，变成圆形
	public static Bitmap initHead(Bitmap bm) {
		// 裁剪图片
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 此宽度是目标ImageView希望的大小，你可以自定义ImageView，然后获得ImageView的宽度。
		int dstWidth = 150;
		// 我们需要加载的图片可能很大，我们先对原有的图片进行裁剪
		int sampleSize = calculateInSampleSize(options, dstWidth, dstWidth);
		options.inSampleSize = sampleSize;
		options.inJustDecodeBounds = false;
		options.inPurgeable = true; 
		options.inInputShareable = true; 
		Bitmap bmp = bm;
		// 绘制图片
		Bitmap resultBmp = Bitmap.createBitmap(dstWidth, dstWidth,
				Bitmap.Config.ARGB_8888);//如果是565不会是圆形
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		Canvas canvas = new Canvas(resultBmp);
		// 画圆
		canvas.drawCircle(dstWidth / 2, dstWidth / 2, dstWidth / 2, paint);
		// 选择交集去上层图片
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bmp, new Rect(0, 0, bmp.getWidth(), bmp.getWidth()),
				new Rect(0, 0, dstWidth, dstWidth), paint);
//		Registered(resultBmp);
//		usersetting(resultBmp);
//		bmp.recycle();
		return resultBmp;
	}


	private static void Registered(Bitmap resultBmp) {
		if(RegisteredActivity.mRegistered_head!=null)
		{
			RegisteredActivity.mRegistered_head.setImageBitmap(resultBmp);
		}
	}
	private static void usersetting(Bitmap resultBmp) {
		if(SettingFragment.mSetting_head_1!=null)
		{
			SettingFragment.mSetting_head_1.setImageBitmap(resultBmp);
		}
	}
	

	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}
}
