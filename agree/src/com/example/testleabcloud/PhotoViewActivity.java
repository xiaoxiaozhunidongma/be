package com.example.testleabcloud;

import uk.co.senab.photoview.PhotoView;
import leanchatlib.utils.DensityUtil;
import leanchatlib.utils.Path2Bitmap;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;

import com.BJ.utils.ImageLoaderUtils;
import com.biju.R;

public class PhotoViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_view);
		Intent intent = getIntent();
		String FileUrl = intent.getStringExtra("FileUrl");
		String localImagePath = intent.getStringExtra("localImagePath");
		
		Bitmap convertToBitmap = Path2Bitmap.convertToBitmap(localImagePath, DensityUtil.dip2px(this, 480),  DensityUtil.dip2px(this, 800));
		
		PhotoView photoView = (PhotoView) findViewById(R.id.image);
		if(convertToBitmap!=null){
			photoView.setImageBitmap(convertToBitmap);
		}else{
				//如果SD为空，网络不空
//				photoView.setImageResource(R.drawable.ic_error);
		ImageLoaderUtils.getInstance().LoadImage(this, FileUrl, photoView);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

}
