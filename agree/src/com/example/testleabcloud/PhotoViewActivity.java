package com.example.testleabcloud;

import java.io.IOException;

import leanchatlib.utils.DensityUtil;
import uk.co.senab.photoview.PhotoView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;

import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.ImageLoaderUtils4Photos;
import com.BJ.utils.Path2Bitmap;
import com.BJ.utils.homeImageLoaderUtils;
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
		
		Bitmap convertToBitmap = null;
		try {
			convertToBitmap = Path2Bitmap.convertToBitmap(localImagePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PhotoView photoView = (PhotoView) findViewById(R.id.image);
		if(convertToBitmap!=null){
			photoView.setImageBitmap(convertToBitmap);
		}else{
		//可能是裁剪后的图
		ImageLoaderUtils4Photos.getInstance().LoadImage(this, FileUrl, photoView);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

}
