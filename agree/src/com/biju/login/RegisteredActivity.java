package com.biju.login;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.BJ.javabean.User;
import com.BJ.utils.Utils;
import com.biju.Interface;
import com.biju.Interface.UserInterface;
import com.biju.MainActivity;
import com.biju.R;
import com.tencent.upload.UploadManager;
import com.tencent.upload.task.IUploadTaskListener;
import com.tencent.upload.task.UploadTask;
import com.tencent.upload.task.ITask.TaskState;
import com.tencent.upload.task.data.FileInfo;
import com.tencent.upload.task.impl.PhotoUploadTask;

public class RegisteredActivity extends Activity implements OnClickListener {

	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0; // 这里的IMAGE_CODE是自己任意定义的
	private ImageView registered_head;
	private EditText mNickname;
	private TextView registered_tv_nickname;
	protected String mFilePath = null;
	public static String APP_VERSION = "1.0.0";
	public static String APPID = "201139";
	public static String USERID = "";
	public static String SIGN="3lXtRSAlZuWqzRczFPIjqrcHJCBhPTIwMTEzOSZrPUFLSUQ5eUFramtVTUhFQzFJTGREbFlvMndmaW1mOThUaUltRyZlPTE0MzY0OTk2NjcmdD0xNDMzOTA3NjY3JnI9MTk5MDE3ODExNSZ1PSZmPQ==";
	private UploadManager uploadManager;
	private TextView textView;
	private Interface regInter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registered);
//		get4Sign();
		initUI();
		initUpload();
	}

	
	private void get4Sign() {
		Interface interface2 = new Interface();
		User user=new User();
		interface2.getPicSign(RegisteredActivity.this, user);
	}


	private void initUpload() {
		// 注册签名
		UploadManager.authorize(APPID, USERID, SIGN);
		uploadManager = new UploadManager(RegisteredActivity.this, "persistenceId");

	}


	private void initUI() {
		registered_head = (ImageView) findViewById(R.id.registered_head);
		registered_head.setOnClickListener(this);
		mNickname = (EditText) findViewById(R.id.registered_nickname);
		findViewById(R.id.registered_back).setOnClickListener(this);
		findViewById(R.id.registered_OK).setOnClickListener(this);
		registered_tv_nickname = (TextView) findViewById(R.id.registered_tv_nickname);
		registered_tv_nickname.setOnClickListener(this);
		regInter = new Interface();
		regInter.setPostListener(new UserInterface() {
			
			@Override
			public void success(String A) {
				Log.e("RegisteredActivity", "注册成功"+A);
			}
			
			@Override
			public void defail(Object B) {
				
			}
		});
		textView = (TextView) findViewById(R.id.textView1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registered, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.registered_head:
			registered_head();
			break;
		case R.id.registered_back:
			registered_back();
			break;
		case R.id.registered_OK:
			registered_OK();
			break;
		case R.id.registered_tv_nickname:
			registered_tv_nickname();
			break;
		default:
			break;
		}
	}

	private void registered_tv_nickname() {
		registered_tv_nickname.setVisibility(View.GONE);
		mNickname.setVisibility(View.VISIBLE);
		
	}


	private void registered_OK() {
		// 把昵称传到接口
		String nickname = mNickname.getText().toString().trim();
		User user = new User();
		user.setNickname(nickname);
		
		upload(user);
		
	}

	private void upload(final User user) {
		UploadTask task = new PhotoUploadTask(mFilePath,new IUploadTaskListener() {			
			 @Override
			  public void onUploadSucceed(final FileInfo result) {
			  Log.e("上传结果", "upload succeed: " + result.fileId);
				 textView.post(new Runnable() {
						
						@Override
						public void run() {
							textView.setText(result.fileId);
						}
					});
			  //上传完成后注册
			  user.setAvatar_path(result.fileId);
			  regInter.regNewAccount(RegisteredActivity.this, user);
			  //跳转至主界面
			  Intent intent=new Intent(RegisteredActivity.this, MainActivity.class);
			  startActivity(intent);
			  
			  }
			  @Override
			  public void onUploadStateChange(TaskState state) {
			   }
			  
			  @Override
			  public void onUploadProgress(long totalSize, long sendSize){
			 final long p = (long) ((sendSize * 100) / (totalSize * 1.0f));
//			 Log.e("上传进度", "上传进度: " + p + "%");
			 textView.post(new Runnable() {
				
				@Override
				public void run() {
					textView.setText("上传进度: " + p + "%");
				}
			});
			  }
			  @Override
			   public void onUploadFailed(final int errorCode, final String errorMsg) {
			 Log.e("Demo", "上传结果:失败! ret:" + errorCode + " msg:" + errorMsg);
			  }
			  });
			 uploadManager.upload(task);  // 开始上传

	}


	private void registered_back() {
		finish();
	}

	// 打开图库，选择图片
	private void registered_head() {
		// 使用intent调用系统提供的相册功能，使用startActivityForResult是为了获取用户选择的图片
		Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
		getAlbum.setType(IMAGE_TYPE);
		startActivityForResult(getAlbum, IMAGE_CODE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || data == null)
            return;
            try
            {
            	Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = RegisteredActivity.this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mFilePath = cursor.getString(columnIndex);
                cursor.close();
                Bitmap bmp = Utils.decodeSampledBitmap(mFilePath, 2);
                initHead(bmp);// 画圆形头像
            }
            catch (Exception e)
            {
                Log.e("Demo", "choose file error!", e);
            }
	}

	// 对图片进行修改，变成圆形
	private void initHead(Bitmap bm) {
		// 裁剪图片
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 此宽度是目标ImageView希望的大小，你可以自定义ImageView，然后获得ImageView的宽度。
		int dstWidth = 150;
		// 我们需要加载的图片可能很大，我们先对原有的图片进行裁剪
		int sampleSize = calculateInSampleSize(options, dstWidth, dstWidth);
		options.inSampleSize = sampleSize;
		options.inJustDecodeBounds = false;
		Bitmap bmp = bm;
		// 绘制图片
		Bitmap resultBmp = Bitmap.createBitmap(dstWidth, dstWidth,
				Bitmap.Config.ARGB_8888);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		Canvas canvas = new Canvas(resultBmp);
		// 画圆
		canvas.drawCircle(dstWidth / 2, dstWidth / 2, dstWidth / 2, paint);
		// 选择交集去上层图片
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bmp, new Rect(0, 0, bmp.getWidth(), bmp.getWidth()),
				new Rect(0, 0, dstWidth, dstWidth), paint);
		registered_head.setImageBitmap(resultBmp);
		bmp.recycle();
	}

	private int calculateInSampleSize(BitmapFactory.Options options,
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
