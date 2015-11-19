package com.biju.function;

import com.biju.R;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

public class AboutUsActivity extends Activity implements OnClickListener {

	private ImageView mAbout_us_biju;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		initUI();
	}

	private void initUI() {
		findViewById(R.id.aboutus_back_layout).setOnClickListener(this);
		findViewById(R.id.aboutus_back).setOnClickListener(this);
		mAbout_us_biju = (ImageView) findViewById(R.id.about_us_biju);
		Bitmap photo = BitmapFactory.decodeResource(getResources(),
				R.drawable.about_us);
		mAbout_us_biju.setImageBitmap(createFramedPhoto(500, 400, photo, 10));
	}
	
	/**
    *
    * @param x ͼ��Ŀ��
    * @param y ͼ��ĸ߶�
    * @param image ԴͼƬ
    * @param outerRadiusRat Բ�ǵĴ�С
    * @return Բ��ͼƬ
    */
   Bitmap createFramedPhoto(int x, int y, Bitmap image, float outerRadiusRat) {
       //����Դ�ļ��½�һ��darwable����
       Drawable imageDrawable = new BitmapDrawable(image);

       // �½�һ���µ����ͼƬ
       Bitmap output = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
       Canvas canvas = new Canvas(output);

       // �½�һ������
       RectF outerRect = new RectF(0, 0, x, y);

       // ����һ����ɫ��Բ�Ǿ���
       Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
       paint.setColor(Color.RED);
       canvas.drawRoundRect(outerRect, outerRadiusRat, outerRadiusRat, paint);

       // ��ԴͼƬ���Ƶ����Բ�Ǿ�����
       paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
       imageDrawable.setBounds(0, 0, x, y);
       canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
       imageDrawable.draw(canvas);
       canvas.restore();

       return output;
   }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.aboutus_back_layout:
		case R.id.aboutus_back:
			aboutus_back();
			break;

		default:
			break;
		}
	}

	private void aboutus_back() {
		finish();
		overridePendingTransition(R.anim.left, R.anim.right);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			aboutus_back();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

}
