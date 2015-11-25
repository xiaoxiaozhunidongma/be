package com.biju.chatroom;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.BJ.javabean.Party4;
import com.BJ.javabean.Photo;
import com.BJ.photo.AlbumActivity;
import com.BJ.photo.Bimp;
import com.BJ.utils.ByteOrBitmap;
import com.BJ.utils.ImageLoaderUtils4Photos;
import com.BJ.utils.LimitLong;
import com.BJ.utils.Path2Bitmap;
import com.BJ.utils.SdPkUser;
import com.BJ.utils.Weeks;
import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.alibaba.sdk.android.oss.storage.OSSBucket;
import com.alibaba.sdk.android.oss.storage.OSSData;
import com.biju.Interface;
import com.biju.Interface.uploadingPhotoListenner;
import com.biju.R;
import com.biju.APP.MyApplication;
import com.biju.function.GroupActivity;
import com.example.imageselected.photo.SelectPhotoActivity;

public class PhotoActivity extends Activity implements OnClickListener, OnItemClickListener {
	
	private GridView noScrollgridview;
	private GridAdapter adapter;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;

	public static String APP_VERSION = "1.0.0";
	public static String APPID = "201139";
	public static String USERID = "";
	public static String SIGN;
//	private UploadManager uploadManager;
	String fileId = "";
//	HashMap<Integer, UploadTask> hashMap = new HashMap<Integer, UploadTask>();
	private static final int TAKE_PICTURE = 0x000001;
//	private boolean isbeginUpload;
	private Interface instance;
	private ArrayList<Photo> listphotos=new ArrayList<Photo>();
	// 完整路径completeURL=beginStr+result.filepath+endStr;
	private String completeURL;
	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "@!";
	private Integer SD_pk_user;
	private RelativeLayout mPhoto_upload_layout;
	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 110; // 这里的IMAGE_CODE是自己任意定义的
	private String mFilePath;
	public static ArrayList<Bitmap> bitmaps=new ArrayList<Bitmap>();
	private boolean hasloaded;
	private OSSData ossData;
	private OSSService ossService;
	private OSSBucket sampleBucket;
	private byte[] bitmap2Bytes;
	private String uUid;
	private String pk_party;
	private Party4 party4;
	private TextView tv_partyname;
	private TextView tv_partytime;
	private TextView tv_partyphotonum;
	private Bitmap convertToBitmap;
	private Bitmap limitLongScaleBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_photo);
		
		//获取sd卡中的pk_user
		SD_pk_user = SdPkUser.getsD_pk_user();
		Log.e("PhotoFragment", "从SD卡中获取到的Pk_user" + SD_pk_user);
		
		Intent intent = getIntent();
		pk_party = intent.getStringExtra("pk_party");
		party4 = (Party4)intent.getSerializableExtra("party4");
		listphotos = (ArrayList<Photo>) intent.getSerializableExtra("photos");
		
		InitUI();
		initPhotoUplisten();
//		initGroupPhotoListen();
//		initOnActivityResult();
		// 获取ossService和sampleBucket
		ossService = MyApplication.getOssService();
		sampleBucket = MyApplication.getSampleBucket();
	}

//	private void initGroupPhotoListen() {
//		instance.setPostListener(new readPartyPhotosListenner() {
//			
//
//			@Override
//			public void success(String A) {
//				Log.e("PhotoFragment2", "返回的图片数组："+A);
//
//				Photosback photosback = GsonUtils.parseJsonArray(A, Photosback.class);
//				listphotos = photosback.getReturnData();
//				
//				//先清空
//				bitmaps.clear();
//				//先清空
//				MyGalleryActivity.netpath.clear();
//				
//				for (int i = 0; i < listphotos.size(); i++) {
//					String path = listphotos.get(i).getPath();
//					String pk_photo = listphotos.get(i).getPk_photo();
//					final String completeUrl=beginStr+pk_photo+endStr+"album-thumbnail";
//					Log.e("PhotoFragment2", "completeUrl"+completeUrl);
//					//将路径全部加入容器
//					MyGalleryActivity.netpath.add(completeUrl);
//				}
//				
//				
//				if(listphotos!=null&&listphotos.size()>0){
//					String pk_photo = listphotos.get(0).getPk_photo();
//					Log.e("PhotoFragment2", "第一个图片路径："+pk_photo);
//				}
//				//刷新
//				adapter.notifyDataSetChanged();
//			}
//			
//			@Override
//			public void defail(Object B) {
//				
//			}
//		});
//	}

	private void initPhotoUplisten() {
		 instance = Interface.getInstance();
		 instance.setPostListener(new uploadingPhotoListenner() {
			
			@Override
			public void success(String A) {
				Log.e("PhotoActivity", "图片是否上传："+A);
//				listphotos.add(photo);
				runOnUiThread( new Runnable() {
					public void run() {
						tv_partyphotonum.setText(String.valueOf(listphotos.size()));
					}
				});
				adapter.notifyDataSetChanged();
				
				//先清空
				MyGalleryActivity.netFullpath.clear();
				
				for (int i = 0; i < listphotos.size(); i++) {
					String pk_photo = listphotos.get(i).getPk_photo();
					final String completeUrl=beginStr+pk_photo;	//原图路径
					Log.e("PhotoActivity", "completeUrl"+completeUrl);
					//将路径全部加入容器
					MyGalleryActivity.netFullpath.add(completeUrl);
				}
			} 
			
			@Override
			public void defail(Object B) {
				
			}
		});
	}

	private void InitUI() {
		tv_partyname = (TextView) findViewById(R.id.tv_partyname);
		tv_partytime = (TextView) findViewById(R.id.tv_partytime);
		tv_partyphotonum = (TextView) findViewById(R.id.tv_partyphotonum);
		findViewById(R.id.PhotoActivity_back_layout).setOnClickListener(this);;
		findViewById(R.id.PhotoActivity_back).setOnClickListener(this);;
		mPhoto_upload_layout = (RelativeLayout) findViewById(R.id.photo_upload_layout);
		mPhoto_upload_layout.setOnClickListener(this);
		findViewById(R.id.photo_upload).setOnClickListener(this);
		pop = new PopupWindow(this);//

		View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);//

		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);

		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo();
				pop.dismiss();
				ll_popup.clearAnimation();
			}

			private void photo() {
				Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(openCameraIntent, TAKE_PICTURE);
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// // 点击图库的时候关闭自身
				// getActivity().finish();
				Bimp.tempSelectBitmap.clear();
				Intent intent = new Intent(PhotoActivity.this, AlbumActivity.class);
				startActivityForResult(intent, 110);
				PhotoActivity.this.overridePendingTransition(
						R.anim.activity_translate_in,
						R.anim.activity_translate_out);
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});

		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
//		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(this);

	}
	
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}


		public int getCount() {
			
				return listphotos.size();
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				holder.tv_progress = (TextView) convertView
						.findViewById(R.id.tv_progress);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			if(listphotos!=null){
					String url=beginStr+listphotos.get(position).getPk_photo()+endStr+"album-thumbnail";//完整路径
					ImageLoaderUtils4Photos.getInstance().LoadImage(PhotoActivity.this, url, holder.image);
			}

			return convertView;
		}

		public class ViewHolder {
			public TextView tv_progress;
			public ImageView image;
		}

	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		//先清空
		MyGalleryActivity.netFullpath.clear();
		
		for (int i = 0; i < listphotos.size(); i++) {
			String pk_photo = listphotos.get(i).getPk_photo();
			final String completeUrl=beginStr+pk_photo;	//原图路径
			Log.e("PhotoActivity", "completeUrl"+completeUrl);
			//将路径全部加入容器
			MyGalleryActivity.netFullpath.add(completeUrl);
		}
		tv_partyname.setText(party4.getName());
		tv_partytime.setText(party4.getBegin_time());
		tv_partyphotonum.setText(String.valueOf(listphotos.size()));
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Photo photo = listphotos.get(position);
		Intent intent = new Intent(PhotoActivity.this,
				MyGalleryActivity.class);
		intent.putExtra("position", position);
		intent.putExtra("ID", position);
		intent.putExtra("photo", photo);
		intent.putExtra("listphotos", listphotos);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.photo_upload_layout:
		case R.id.photo_upload:
			checkphoto();
			break;
		case R.id.PhotoActivity_back_layout:
		case R.id.PhotoActivity_back:
			finish();
			break;

		default:
			break;
		}
	}
	
	private void checkphoto() {
//		// 使用intent调用系统提供的相册功能，使用startActivityForResult是为了获取用户选择的图片
//		Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
//		getAlbum.setType(IMAGE_TYPE);
//		startActivityForResult(getAlbum, IMAGE_CODE);
		
		Intent getAlbum = new Intent(this, SelectPhotoActivity.class);
		getAlbum.putExtra("SelectType", 1);//多张上传模式
		startActivityForResult(getAlbum, IMAGE_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.e("PhotoActivity.this", "onActivityResult");
		Log.e("PhotoActivity.this", "requestCode==="+requestCode);
		Log.e("PhotoActivity.this", "data==="+data);
//		if (requestCode != 110 || data == null){
//			return;
//		}
//			Uri selectedImage = data.getData();
//			String[] filePathColumn = { MediaStore.Images.Media.DATA };
//			Cursor cursor = PhotoActivity.this.getContentResolver().query(
//					selectedImage, filePathColumn, null, null, null);
//			if(cursor!=null){
//				cursor.moveToFirst();
//				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//				mFilePath = cursor.getString(columnIndex);
//				cursor.close();
//				cursor = null;
//				
//			}else{
//				File file = new File(selectedImage.getPath());
//				mFilePath=file.getAbsolutePath();
//				if (!file.exists()) {
//					Toast toast = Toast.makeText(PhotoActivity.this, "找不到图片", Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
//					return;
//				}
//			}
//			Log.e("PhotoActivity.this", "mFilePath======"+mFilePath);
////				Bitmap bmp = Utils.decodeSampledBitmap(mFilePath, 2);
////				Bitmap bmp = Bimp.revitionImageSize(mFilePath);
//			//这个mFilePath不可以用缩略图路径
////				Bitmap bmp = MyBimp.revitionImageSize(mFilePath);
		
		if (requestCode != 110 || data == null){
			return;
		}
		@SuppressWarnings("unchecked")
		ArrayList<String> mSelectedImageList = (ArrayList<String>) data.getSerializableExtra("mSelectedImageList");
//		mFilePath=mSelectedImageList.get(0);
		Log.e("PhotoActivity", "mSelectedImageList.size()======" + mSelectedImageList.size());
		Log.e("PhotoActivity", "mFilePath======" + mFilePath);
		
		//多张上传
		for (int z = 0; z < mSelectedImageList.size(); z++) {
			mFilePath=mSelectedImageList.get(z);
			if(listphotos.size()==0){
				Log.e("PhotoActivity.this", "上传第一张图片");
				try {
					convertToBitmap = Path2Bitmap.convertToBitmap(mFilePath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				limitLongScaleBitmap = LimitLong.limitLongScaleBitmap(
						convertToBitmap, 1080);
				bitmap2Bytes = ByteOrBitmap.Bitmap2Bytes(limitLongScaleBitmap);
				UUID randomUUID = UUID.randomUUID();
				uUid = randomUUID.toString();
				OSSupload(ossData, bitmap2Bytes, uUid,mFilePath);
				
			}else{
				hasloaded=false;//默认没有上传
				for (int i = 0; i < listphotos.size(); i++) {
					String path = listphotos.get(i).getPath();
					if(mFilePath.equals(path)){
						Toast.makeText(PhotoActivity.this, "已经有重复的图片了哦", Toast.LENGTH_SHORT).show();
						hasloaded = true;
					}
				}
				if(hasloaded==false){
					Log.e("正在上传的图片路径", ""+mFilePath);
//					upload(mFilePath);
					//开始Oss上传
					Bitmap convertToBitmap = null;
					try {
						convertToBitmap = Path2Bitmap.convertToBitmap(mFilePath);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//上传原图
					Bitmap limitLongScaleBitmap = LimitLong.limitLongScaleBitmap(
							convertToBitmap, 1080);// 最长边限制为1080
					bitmap2Bytes = ByteOrBitmap.Bitmap2Bytes(limitLongScaleBitmap);
					UUID randomUUID = UUID.randomUUID();
					uUid = randomUUID.toString();
					OSSupload(ossData, bitmap2Bytes, uUid,mFilePath);
					
				}
			}
		}
		
	
	}
	
//	@Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//		Log.e("PhotoActivity", "onDestroy()方法被调用");
//		if(convertToBitmap!=null){
//			if(!convertToBitmap.isRecycled()){
//				convertToBitmap.recycle();
//			}
//		}
//		if(limitLongScaleBitmap!=null){
//			if(!limitLongScaleBitmap.isRecycled()){
//				limitLongScaleBitmap.recycle();
//			}
//		}
//	       System.gc();  //提醒系统及时回收
//		super.onDestroy();
//	}
	
	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}
	
	private void OSSupload(OSSData ossData, byte[] data, String UUid, final String imagePath) {
		ossData = ossService.getOssData(sampleBucket, UUid);
		ossData.setData(data, "jpg"); // 指定需要上传的数据和它的类型
		ossData.enableUploadCheckMd5sum(); // 开启上传MD5校验
		ossData.uploadInBackground(new SaveCallback() {

			@Override
			public void onSuccess(String objectKey) {
				Log.e("PhotoActivity.this", "图片上传成功");
				Log.e("PhotoActivity.this", "objectKey==" + objectKey);
				
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");
				String format = sdf.format(new Date());
				
				SimpleDateFormat sdf2=new SimpleDateFormat("yyyyMMdd");
				String format2 = sdf2.format(new Date());
				
				SimpleDateFormat sdf3=new SimpleDateFormat("HH:mm:ss");
				String format3 = sdf3.format(new Date());
				
				String years = format2.substring(0, 4);
				String months = format2.substring(4, 6);
				String days = format2.substring(6, 8);
				Log.e("", "years=="+years);
				Log.e("", "months=="+months);
				Log.e("", "days=="+days);
				// 计算星期几
				int y = Integer.valueOf(years);
				int m = Integer.valueOf(months);
				int d = Integer.valueOf(days);
				// 调用计算星期几的方法
				Weeks.CaculateWeekDay(y, m, d);
				String week = Weeks.getweek();
				Log.e("", "week=="+week);
				
				String compleTime=format+" "+week+" "+format3;
				
				Photo photo = new Photo();
				photo.setFk_group(GroupActivity.getPk_group());
				photo.setFk_user(SD_pk_user);
				photo.setPk_photo(objectKey);//pk_photo
				photo.setStatus(1);
				photo.setPath(imagePath);//设置内存路径
				photo.setFk_party(pk_party);//设置pk_party
				photo.setCreate_time(compleTime);//设置创建时间
				Log.e("PhotoActivity", "pk_party=="+pk_party);
				instance.uploadingPhoto(PhotoActivity.this, photo);
				listphotos.add(photo);
				
			}

			@Override
			public void onProgress(String objectKey, int byteCount,
					int totalSize) {
			}

			@Override
			public void onFailure(String objectKey, OSSException ossException) {
				Log.e("PhotoActivity.this", "上传失败，请重新上传" + ossException.toString());
				//这里OSS失败，有UI线程的问题~~~~~~
//				new Handler().post(new Runnable() {
//					
//					@Override
//					public void run() {
//						Toast.makeText(PhotoActivity.this, "上传失败，请重新上传",Toast.LENGTH_SHORT).show();
//					}
//				});
			}
		});
	}
	

}
