package com.fragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
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

import com.BJ.javabean.Group;
import com.BJ.javabean.Photo;
import com.BJ.javabean.Photosback;
import com.BJ.photo.AlbumActivity;
import com.BJ.photo.Bimp;
import com.BJ.utils.ByteOrBitmap;
import com.BJ.utils.ImageLoaderUtils4Photos;
import com.BJ.utils.LimitLong;
import com.BJ.utils.Path2Bitmap;
import com.BJ.utils.PicCutter;
import com.BJ.utils.SdPkUser;
import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.alibaba.sdk.android.oss.storage.OSSBucket;
import com.alibaba.sdk.android.oss.storage.OSSData;
import com.biju.Interface;
import com.biju.Interface.readPartyPhotosListenner;
import com.biju.Interface.uploadingPhotoListenner;
import com.biju.R;
import com.biju.APP.MyApplication;
import com.biju.chatroom.MyGalleryActivity;
import com.biju.function.GroupActivity;
import com.github.volley_examples.utils.GsonUtils;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class PhotoFragment2 extends Fragment implements OnClickListener, OnItemClickListener  {

	private GridView noScrollgridview;
	private GridAdapter adapter;
	private View mLayout;
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
	public static onActivityResultInterface onActivityResultInterface;
	private OSSData ossData;
	private OSSService ossService;
	private OSSBucket sampleBucket;
	private byte[] bitmap2Bytes;
	private String uUid;


	public PhotoFragment2() {
		// Required empty public constructor
	}
	


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			if(mLayout==null){
				mLayout = inflater.inflate(R.layout.activity_selectimg, container,false);
				
				//获取sd卡中的pk_user
				SD_pk_user = SdPkUser.getsD_pk_user();
				Log.e("PhotoFragment", "从SD卡中获取到的Pk_user" + SD_pk_user);
				
				Init(inflater);
				initPhotoUplisten();
				initGroupPhotoListen();
//			Interface.getInstance().setOnActivityResultListener(new OnArticleSelectedListener() {
//				
//				@Override
//				public void MyOnActivityResult(String mFilePath, Bitmap bmp) {
//					Log.e("PhotoFragment2", "回调");
//					for (int i = 0; i < listphotos.size(); i++) {
//						String path = listphotos.get(i).getPath();
//						if(!mFilePath.equals(path)){
//							upload(mFilePath);
//							return;
//						}
//					}
//				}
//			});
				initOnActivityResult();
				// 获取ossService和sampleBucket
				ossService = MyApplication.getOssService();
				sampleBucket = MyApplication.getSampleBucket();
			}
		return mLayout;
	}
	



	private void initOnActivityResult() {
		onActivityResultInterface onActivityResultInterface = new onActivityResultInterface() {
			
			@Override
			public void onActivityResult(int requestCode, int resultCode, Intent data) {

				Log.e("PhotoFragment2", "onActivityResult");
				Log.e("PhotoFragment2", "requestCode==="+requestCode);
				Log.e("PhotoFragment2", "data==="+data);
				if (requestCode != 110 || data == null){
					return;
				}
					Uri selectedImage = data.getData();
					String[] filePathColumn = { MediaStore.Images.Media.DATA };
					Cursor cursor = getActivity().getContentResolver().query(
							selectedImage, filePathColumn, null, null, null);
					if(cursor!=null){
						cursor.moveToFirst();
						int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
						mFilePath = cursor.getString(columnIndex);
						cursor.close();
						cursor = null;
						
					}else{
						File file = new File(selectedImage.getPath());
						mFilePath=file.getAbsolutePath();
						if (!file.exists()) {
							Toast toast = Toast.makeText(getActivity(), "找不到图片", Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
							return;
						}
					}
					Log.e("NewteamActivity", "mFilePath======"+mFilePath);
//						Bitmap bmp = Utils.decodeSampledBitmap(mFilePath, 2);
//						Bitmap bmp = Bimp.revitionImageSize(mFilePath);
					//这个mFilePath不可以用缩略图路径
//						Bitmap bmp = MyBimp.revitionImageSize(mFilePath);
					
				switch (requestCode) {
				case 110:
					
					if(listphotos.size()==0){
						Log.e("Photofragment2", "上传第一张图片");
//						upload(mFilePath);
						//开始Oss上传
						Bitmap convertToBitmap = null;
						try {
							convertToBitmap = Path2Bitmap.convertToBitmap(mFilePath);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Bitmap limitLongScaleBitmap = LimitLong.limitLongScaleBitmap(
								convertToBitmap, 1080);// 最长边限制为1080
						Bitmap centerSquareScaleBitmap = PicCutter.centerSquareScaleBitmap(
								limitLongScaleBitmap, 180);// 截取中间正方形
						bitmap2Bytes = ByteOrBitmap.Bitmap2Bytes(centerSquareScaleBitmap);
						UUID randomUUID = UUID.randomUUID();
						uUid = randomUUID.toString();
						OSSupload(ossData, bitmap2Bytes, uUid,mFilePath);
						
						byte[] bitmap2Bytes2 = ByteOrBitmap.Bitmap2Bytes(convertToBitmap);//原图
						UUID randomUUID2 = UUID.randomUUID();
						String uUid2 = randomUUID2.toString();
						OSSuploadFull(ossData, bitmap2Bytes2, uUid2, mFilePath);
					}else{
						hasloaded=false;//默认没有上传
						for (int i = 0; i < listphotos.size(); i++) {
							String path = listphotos.get(i).getPath();
							if(mFilePath.equals(path)){
								Toast.makeText(getActivity(), "已经有重复的图片了哦", Toast.LENGTH_SHORT).show();
								hasloaded = true;
							}
						}
						if(hasloaded==false){
							Log.e("正在上传的图片路径", ""+mFilePath);
//							upload(mFilePath);
							//开始Oss上传
							Bitmap convertToBitmap = null;
							try {
								convertToBitmap = Path2Bitmap.convertToBitmap(mFilePath);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Bitmap limitLongScaleBitmap = LimitLong.limitLongScaleBitmap(
									convertToBitmap, 1080);// 最长边限制为1080
							bitmap2Bytes = ByteOrBitmap.Bitmap2Bytes(limitLongScaleBitmap);
							UUID randomUUID = UUID.randomUUID();
							uUid = randomUUID.toString();
							OSSupload(ossData, bitmap2Bytes, uUid,mFilePath);
							
							byte[] bitmap2Bytes2 = ByteOrBitmap.Bitmap2Bytes(convertToBitmap);//原图
							UUID randomUUID2 = UUID.randomUUID();
							String uUid2 = randomUUID2.toString();
							OSSuploadFull(ossData, bitmap2Bytes2, uUid2, mFilePath);
						}
					}
				
					break;
				}
			
			}
		};
		this.onActivityResultInterface=onActivityResultInterface;
	}



	private void initGroupPhotoListen() {
		instance.setPostListener(new readPartyPhotosListenner() {
			

			@Override
			public void success(String A) {
				Log.e("PhotoFragment2", "返回的图片数组："+A);

				Photosback photosback = GsonUtils.parseJsonArray(A, Photosback.class);
				listphotos = photosback.getReturnData();
				
				//先清空
				bitmaps.clear();
				//先清空
				MyGalleryActivity.netFullpath.clear();
				
				for (int i = 0; i < listphotos.size(); i++) {
					String path = listphotos.get(i).getPath();
					String pk_photo = listphotos.get(i).getPk_photo();
					final String completeUrl=beginStr+pk_photo+endStr+"album-thumbnail";
					Log.e("PhotoFragment2", "completeUrl"+completeUrl);
					//将路径全部加入容器
					MyGalleryActivity.netFullpath.add(completeUrl);
				}
				
				
				if(listphotos!=null&&listphotos.size()>0){
					String pk_photo = listphotos.get(0).getPk_photo();
					Log.e("PhotoFragment2", "第一个图片路径："+pk_photo);
				}
				//刷新
				adapter.notifyDataSetChanged();
			}
			
			@Override
			public void defail(Object B) {
				
			}
		});
	}


	private void initPhotoUplisten() {
		 instance = Interface.getInstance();
		 instance.setPostListener(new uploadingPhotoListenner() {
			
			@Override
			public void success(String A) {
				Log.e("PhotoFragment2", "图片是否上传："+A);
				//读取小组相册
				Group group=new Group();
				group.setPk_group(GroupActivity.getPk_group());
				group.setStatus(1);
				instance.readPartyPhotos(getActivity(), group);
			}
			
			@Override
			public void defail(Object B) {
				
			}
		});
	}


	
	@Override
	public void onStart() {
		super.onStart();
		//读取小组相册
		Group group=new Group();
		group.setPk_group(GroupActivity.getPk_group());
		group.setStatus(1);
		instance.readPartyPhotos(getActivity(), group);
		
	}


//	private void upload(final String imagePath) {
//		 UploadTask task = new PhotoUploadTask(imagePath, new IUploadTaskListener() {
//			@Override
//			public void onUploadSucceed(final FileInfo result) {
//				Log.e("上传结果", "upload succeed: " + result.fileId);
//				
//				Photo photo = new Photo();
//				photo.setFk_group(GroupActivity.getPk_group());
//				photo.setFk_user(SD_pk_user);
//				photo.setPk_photo(result.fileId);//pk_photo
//				photo.setStatus(1);
//				photo.setPath(imagePath);//设置内存路径
//				instance.uploadingPhoto(getActivity(), photo);
//				
//				new Thread(new Runnable() {
//					
//					@Override
//					public void run() {
//						adapter.notifyDataSetChanged();
//					}
//				});
//			}
//
//			@Override
//			public void onUploadStateChange(TaskState state) {
//			}
//
//			@Override
//			public void onUploadProgress(long totalSize, long sendSize) {
//				final long p = (long) ((sendSize * 100) / (totalSize * 1.0f));
//				// Log.e("上传进度", "上传进度: " + p + "%");
//			}
//
//			@Override
//			public void onUploadFailed(final int errorCode,
//					final String errorMsg) {
//				Log.e("Demo", "上传结果:失败! ret:" + errorCode + " msg:" + errorMsg);
//			}
//		});
//		 
//		uploadManager.upload(task); // 开始上传
//
//	}



	public void Init(LayoutInflater inflater) {
		mPhoto_upload_layout = (RelativeLayout) mLayout.findViewById(R.id.photo_upload_layout);
		mPhoto_upload_layout.setOnClickListener(this);
		mLayout.findViewById(R.id.photo_upload).setOnClickListener(this);
		pop = new PopupWindow(getActivity());

		View view = inflater.inflate(R.layout.item_popupwindows, null);

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
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// // 点击图库的时候关闭自身
				// getActivity().finish();
				Bimp.tempSelectBitmap.clear();
				Intent intent = new Intent(getActivity(), AlbumActivity.class);
				startActivityForResult(intent, 110);
				getActivity().overridePendingTransition(
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

		noScrollgridview = (GridView) mLayout
				.findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(getActivity());
//		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(this);

	}

	@SuppressLint("HandlerLeak")
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

//		public void update() {
//			loading();
//		}

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
					ImageLoaderUtils4Photos.getInstance().LoadImage(getActivity(), url, holder.image);
//					AsynImageLoader asynImageLoader = new AsynImageLoader();
//					asynImageLoader.showImageAsyn(holder.image, url, R.drawable.preview_2,getActivity());
			}

			return convertView;
		}

		public class ViewHolder {
			public TextView tv_progress;
			public ImageView image;
		}

//		Handler handler = new Handler() {
//			public void handleMessage(Message msg) {
//				switch (msg.what) {
//				case 1:
//					adapter.notifyDataSetChanged();
//					break;
//				}
//				super.handleMessage(msg);
//			}
//		};

//		public void loading() {
//			new Thread(new Runnable() {
//				public void run() {
//					while (true) {
//						if (Bimp.max == Bimp.tempSelectBitmap.size()) {
//							Message message = new Message();
//							message.what = 1;
//							handler.sendMessage(message);
//							break;
//						} else {
//							Bimp.max += 1;
//							Message message = new Message();
//							message.what = 1;
//							handler.sendMessage(message);
//						}
//					}
//				}
//			}).start();
//		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

//	protected void onRestart() {
//		adapter.update();
//		super.onResume();
//	}


	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}
	
	public interface onActivityResultInterface{
		void onActivityResult(int requestCode, int resultCode, Intent data);
	}
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		Log.e("PhotoFragment2", "onActivityResult");
//		if (requestCode != 110 || data == null){
//			return;
//		}
//			Uri selectedImage = data.getData();
//			String[] filePathColumn = { MediaStore.Images.Media.DATA };
//			Cursor cursor = getActivity().getContentResolver().query(
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
//					Toast toast = Toast.makeText(getActivity(), "找不到图片", Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
//					return;
//				}
//			}
//			Log.e("NewteamActivity", "mFilePath======"+mFilePath);
////				Bitmap bmp = Utils.decodeSampledBitmap(mFilePath, 2);
////				Bitmap bmp = Bimp.revitionImageSize(mFilePath);
//			//这个mFilePath不可以用缩略图路径
////				Bitmap bmp = MyBimp.revitionImageSize(mFilePath);
//			
//		switch (requestCode) {
//		case 110:
//			
//			if(listphotos.size()==0){
//				Log.e("Photofragment2", "上传第一张图片");
////				upload(mFilePath);
//				//开始Oss上传
//				Bitmap convertToBitmap = null;
//				try {
//					convertToBitmap = Path2Bitmap.convertToBitmap(mFilePath);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				Bitmap limitLongScaleBitmap = LimitLong.limitLongScaleBitmap(
//						convertToBitmap, 1080);// 最长边限制为1080
//				Bitmap centerSquareScaleBitmap = PicCutter.centerSquareScaleBitmap(
//						limitLongScaleBitmap, 600);// 截取中间正方形
//				bitmap2Bytes = ByteOrBitmap.Bitmap2Bytes(centerSquareScaleBitmap);
//				UUID randomUUID = UUID.randomUUID();
//				uUid = randomUUID.toString();
//				OSSupload(ossData, bitmap2Bytes, uUid,mFilePath);
//			}else{
//				hasloaded=false;//默认没有上传
//				for (int i = 0; i < listphotos.size(); i++) {
//					String path = listphotos.get(i).getPath();
//					if(mFilePath.equals(path)){
//						Toast.makeText(getActivity(), "已经有重复的图片了哦", Toast.LENGTH_SHORT).show();
//						hasloaded = true;
//					}
//				}
//				if(hasloaded==false){
//					Log.e("正在上传的图片路径", ""+mFilePath);
////					upload(mFilePath);
//					//开始Oss上传
//					Bitmap convertToBitmap = null;
//					try {
//						convertToBitmap = Path2Bitmap.convertToBitmap(mFilePath);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					Bitmap limitLongScaleBitmap = LimitLong.limitLongScaleBitmap(
//							convertToBitmap, 1080);// 最长边限制为1080
//					Bitmap centerSquareScaleBitmap = PicCutter.centerSquareScaleBitmap(
//							limitLongScaleBitmap, 600);// 截取中间正方形
//					bitmap2Bytes = ByteOrBitmap.Bitmap2Bytes(centerSquareScaleBitmap);
//					UUID randomUUID = UUID.randomUUID();
//					uUid = randomUUID.toString();
//					OSSupload(ossData, bitmap2Bytes, uUid,mFilePath);
//				}
//			}
//		
//			break;
//		}
//	}




	private void OSSupload(OSSData ossData, byte[] data, String UUid, final String imagePath) {
		ossData = ossService.getOssData(sampleBucket, UUid);
		ossData.setData(data, "jpg"); // 指定需要上传的数据和它的类型
		ossData.enableUploadCheckMd5sum(); // 开启上传MD5校验
		ossData.uploadInBackground(new SaveCallback() {
			@Override
			public void onSuccess(String objectKey) {
				Log.e("", "图片上传成功");
				Log.e("Main", "objectKey==" + objectKey);
				
				Photo photo = new Photo();
				photo.setFk_group(GroupActivity.getPk_group());
				photo.setFk_user(SD_pk_user);
				photo.setPk_photo(objectKey);//pk_photo
				photo.setStatus(1);
				photo.setPath(imagePath);//设置内存路径
				instance.uploadingPhoto(getActivity(), photo);
				
			}

			@Override
			public void onProgress(String objectKey, int byteCount,
					int totalSize) {
			}

			@Override
			public void onFailure(String objectKey, OSSException ossException) {
				Log.e("", "图片上传失败" + ossException.toString());
				Toast.makeText(getActivity(), "上传失败，请重新上传",Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void OSSuploadFull(OSSData ossData, byte[] data, String UUid, final String imagePath) {
		ossData = ossService.getOssData(sampleBucket, UUid);
		ossData.setData(data, "jpg"); // 指定需要上传的数据和它的类型
		ossData.enableUploadCheckMd5sum(); // 开启上传MD5校验
		ossData.uploadInBackground(new SaveCallback() {
			@Override
			public void onSuccess(String objectKey) {
				Log.e("", "完整原图片上传成功2");
				Log.e("Main", "objectKey2==" + objectKey);
				
				final String completeUrl=beginStr+objectKey;
				MyGalleryActivity.netFullpath.add(completeUrl);
				
			}
			
			@Override
			public void onProgress(String objectKey, int byteCount,
					int totalSize) {
			}
			
			@Override
			public void onFailure(String objectKey, OSSException ossException) {
				Log.e("", "图片上传失败2" + ossException.toString());
				Toast.makeText(getActivity(), "上传失败，请重新上传2",Toast.LENGTH_SHORT).show();
				MyGalleryActivity.netFullpath.add("失败");
			}
		});
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.photo_upload_layout:
		case R.id.photo_upload:
			checkphoto();
			break;

		default:
			break;
		}
	}


	private void checkphoto() {
		// 使用intent调用系统提供的相册功能，使用startActivityForResult是为了获取用户选择的图片
		Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
		getAlbum.setType(IMAGE_TYPE);
		getActivity().startActivityForResult(getAlbum, IMAGE_CODE);
//		startActivityForResult(getAlbum, IMAGE_CODE);
	}



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(getActivity(),
				MyGalleryActivity.class);
		intent.putExtra("position", position);
		intent.putExtra("ID", position);
		startActivity(intent);
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ViewGroup parent = (ViewGroup) mLayout.getParent();
		parent.removeView(mLayout);
	}
	
}
