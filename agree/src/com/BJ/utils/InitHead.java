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
	// ��ͼƬ�����޸ģ����Բ��
	public static Bitmap initHead(Bitmap bm) {
		// �ü�ͼƬ
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// �˿����Ŀ��ImageViewϣ���Ĵ�С��������Զ���ImageView��Ȼ����ImageView�Ŀ�ȡ�
		int dstWidth = 150;
		// ������Ҫ���ص�ͼƬ���ܴܺ������ȶ�ԭ�е�ͼƬ���вü�
		int sampleSize = calculateInSampleSize(options, dstWidth, dstWidth);
		options.inSampleSize = sampleSize;
		options.inJustDecodeBounds = false;
		options.inPurgeable = true; 
		options.inInputShareable = true; 
		Bitmap bmp = bm;
		// ����ͼƬ
		Bitmap resultBmp = Bitmap.createBitmap(dstWidth, dstWidth,
				Bitmap.Config.ARGB_8888);//�����565������Բ��
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		Canvas canvas = new Canvas(resultBmp);
		// ��Բ
		canvas.drawCircle(dstWidth / 2, dstWidth / 2, dstWidth / 2, paint);
		// ѡ�񽻼�ȥ�ϲ�ͼƬ
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
