package com.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
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

import com.BJ.javabean.Group;
import com.BJ.javabean.Photo;
import com.BJ.javabean.Photosback;
import com.BJ.javabean.PicSignBack;
import com.BJ.javabean.User;
import com.BJ.photo.AlbumActivity;
import com.BJ.photo.Bimp;
import com.BJ.photo.FileUtils;
import com.BJ.photo.GalleryActivity;
import com.BJ.photo.ImageItem;
import com.BJ.photo.PublicWay;
import com.BJ.photo.Res;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.SdPkUser;
import com.biju.Interface;
import com.biju.Interface.getPicSignListenner;
import com.biju.Interface.readPartyPhotosListenner;
import com.biju.Interface.uploadingPhotoListenner;
import com.biju.R;
import com.biju.function.GroupActivity;
import com.github.volley_examples.utils.GsonUtils;
import com.tencent.upload.UploadManager;
import com.tencent.upload.task.ITask.TaskState;
import com.tencent.upload.task.IUploadTaskListener;
import com.tencent.upload.task.UploadTask;
import com.tencent.upload.task.data.FileInfo;
import com.tencent.upload.task.impl.PhotoUploadTask;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class PhotoFragment extends Fragment  {

	private GridView noScrollgridview;
	private GridAdapter adapter;
	private View mLayout;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	public static Bitmap bimap;

	public static String APP_VERSION = "1.0.0";
	public static String APPID = "201139";
	public static String USERID = "";
	public static String SIGN;
	private UploadManager uploadManager;
	String fileId = "";
//	HashMap<Integer, UploadTask> hashMap = new HashMap<Integer, UploadTask>();
	private static final int TAKE_PICTURE = 0x000001;
//	private boolean isbeginUpload;
	private Interface instance;
	private ArrayList<Photo> listphotos=new ArrayList<Photo>();
	// 完整路径completeURL=beginStr+result.filepath+endStr;
	private String completeURL;
	private String beginStr = "http://201139.image.myqcloud.com/201139/0/";
	private String endStr = "/original";
	private Integer SD_pk_user;


	public PhotoFragment() {
		// Required empty public constructor
	}

	public static BeginUpload beginUpload;

	public interface BeginUpload {
		void begin();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (mLayout == null) {

			mLayout = inflater.inflate(R.layout.activity_selectimg, container,false);
			
			//获取sd卡中的pk_user
			SD_pk_user = SdPkUser.getsD_pk_user();
			Log.e("PhotoFragment", "从SD卡中获取到的Pk_user" + SD_pk_user);
			
			Res.init(getActivity());
			bimap = BitmapFactory.decodeResource(getResources(),
					R.drawable.icon_addpic_unfocused);
			// mLayout =inflater.inflate(R.layout.activity_selectimg, null);
			// setContentView(mLayout);
			Init(inflater);
			get4PicSign();
			initBeginUplistener();
			initPhotoUplisten();
			initGroupPhotoListen();
		}
		return mLayout;
	}
	

	private void initGroupPhotoListen() {
		instance.setPostListener(new readPartyPhotosListenner() {
			

			@Override
			public void success(String A) {
				Log.e("PhotoFragment", "返回的图片数组："+A);

				Photosback photosback = GsonUtils.parseJsonArray(A, Photosback.class);
				listphotos = photosback.getReturnData();
				if(listphotos!=null&&listphotos.size()>0){
					String pk_photo = listphotos.get(0).getPk_photo();
					Log.e("PhotoFragment", "第一个图片路径："+pk_photo);
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
				Log.e("PhotoFragment", "图片是否上传："+A);
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
	public void onDestroyView() {
		super.onDestroyView();
		ViewGroup parent = (ViewGroup) mLayout.getParent();
		parent.removeView(mLayout);
	}
	
	@Override
	public void onStart() {
		SharedPreferences sp=getActivity().getSharedPreferences("isPhoto", 0);
		boolean photo = sp.getBoolean("Photo", false);
		if(photo)
		{
			adapter.notifyDataSetChanged();
		}
		
		super.onStart();
		//读取小组相册
		Group group=new Group();
		group.setPk_group(GroupActivity.getPk_group());
		group.setStatus(1);
		instance.readPartyPhotos(getActivity(), group);
//		//销毁界面
//		for (int i = 0; i < PublicWay.activityList.size(); i++) {
//			if (null != PublicWay.activityList.get(i)) {
//				PublicWay.activityList.get(i).finish();
//			}
//		}
	}

	private void initBeginUplistener() {
		BeginUpload upload = new BeginUpload() {

			private TextView textView;

			@Override
			public void begin() {
				// 刷新立即显示照片
				adapter.notifyDataSetChanged();
				for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
					String imagePath = Bimp.tempSelectBitmap.get(i)
							.getImagePath();
					Log.e("PhotoFragment", "每个图片路径：" + imagePath);
					 upload(imagePath);
				}
			}
		};
		beginUpload = upload;
	}

	private void upload(String imagePath) {
		 UploadTask task = new PhotoUploadTask(imagePath, new IUploadTaskListener() {
			@Override
			public void onUploadSucceed(final FileInfo result) {
				Log.e("上传结果", "upload succeed: " + result.fileId);
				
				Photo photo = new Photo();
				photo.setFk_group(GroupActivity.getPk_group());
				photo.setFk_user(SD_pk_user);
//				photo.setPath(result.fileId);
				photo.setPk_photo(result.fileId);
				photo.setStatus(1);
				instance.uploadingPhoto(getActivity(), photo);
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						adapter.notifyDataSetChanged();
					}
				});
			}

			@Override
			public void onUploadStateChange(TaskState state) {
			}

			@Override
			public void onUploadProgress(long totalSize, long sendSize) {
				final long p = (long) ((sendSize * 100) / (totalSize * 1.0f));
				// Log.e("上传进度", "上传进度: " + p + "%");
			}

			@Override
			public void onUploadFailed(final int errorCode,
					final String errorMsg) {
				Log.e("Demo", "上传结果:失败! ret:" + errorCode + " msg:" + errorMsg);
//				textView.post(new Runnable() {
//
//					@Override
//					public void run() {
//						textView.setVisibility(View.VISIBLE);
//						textView.setText("上传失败");
//						//上传失败就删除那个任务重新上传
//						UploadTask uploadTask = hashMap.get(position);
//						hashMap.remove(uploadTask);
//					}
//				});
			}
		});
//		 //存入容器
//		hashMap.put(position, task);
		 
		uploadManager.upload(task); // 开始上传

	}

	private void get4PicSign() {
		Interface interface1 = Interface.getInstance();
		interface1.setPostListener(new getPicSignListenner() {

			@Override
			public void success(String A) {
				PicSignBack picSignBack = GsonUtils.parseJson(A,
						PicSignBack.class);
				String returnData = picSignBack.getReturnData();
				SIGN = returnData;
				initUpload();
			}

			@Override
			public void defail(Object B) {

			}
		});
		interface1.getPicSign(getActivity(), new User());
	}

	private void initUpload() {
		// 注册签名
		UploadManager.authorize(APPID, USERID, SIGN);
		uploadManager = new UploadManager(getActivity(), "persistenceId");

	}

	public void Init(LayoutInflater inflater) {
		//上传图片
//		mPhoto_upload_layout = (RelativeLayout) mLayout.findViewById(R.id.photo_upload_layout);
//		mPhoto_upload_layout.setOnClickListener(this);
//		mLayout.findViewById(R.id.photo_upload).setOnClickListener(this);
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
				
//				Bimp.tempSelectBitmap.clear();
				Intent intent = new Intent(getActivity(), AlbumActivity.class);
				startActivity(intent);
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
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == listphotos.size()) {
					Log.i("ddddddd", "----------");
					ll_popup.startAnimation(AnimationUtils.loadAnimation(
							getActivity(), R.anim.activity_translate_in));
					pop.showAtLocation(mLayout, Gravity.BOTTOM, 0, 0);
				} else {
					Intent intent = new Intent(getActivity(),
							GalleryActivity.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
//				if (arg2 == Bimp.tempSelectBitmap.size()) {
//					Log.i("ddddddd", "----------");
//					ll_popup.startAnimation(AnimationUtils.loadAnimation(
//							getActivity(), R.anim.activity_translate_in));
//					pop.showAtLocation(mLayout, Gravity.BOTTOM, 0, 0);
//				} else {
//					Intent intent = new Intent(getActivity(),
//							GalleryActivity.class);
//					intent.putExtra("position", "1");
//					intent.putExtra("ID", arg2);
//					startActivity(intent);
//				}
			}
		});

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

		public void update() {
			loading();
		}

		public int getCount() {
//			if(Bimp.tempSelectBitmap.size()!=0){
//				if (Bimp.tempSelectBitmap.size() == 9) {
//					return 9;
//				}
//				return (Bimp.tempSelectBitmap.size() + 1);
//			}else{
//				if(listphotos!=null){
//					return listphotos.size()+1;
//				}else{
//					return 1;
//				}
//			}
			
			if(listphotos!=null){
				return listphotos.size()+1;
			}else{
				return 1;
			}
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
//			if(Bimp.tempSelectBitmap.size()!=0){
//				
//				if (position == Bimp.tempSelectBitmap.size()) {
//					holder.image.setImageBitmap(BitmapFactory.decodeResource(
//							getResources(), R.drawable.icon_addpic_unfocused));
//					if (position == 9) {
//						holder.image.setVisibility(View.GONE);
//					}
//				} else {
//					holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position)
//							.getBitmap());
//					//上传部分
////					if(isbeginUpload){
////						UploadTask uploadTask = hashMap.get(position);
////						if (uploadTask == null) {
////							Log.e("photofragment", "position:" + position);
////							final String imagePath = Bimp.tempSelectBitmap.get(
////									position).getImagePath();
////							upload(imagePath, holder.tv_progress,position);
////						}
////					}
//				}
//				
//			}
			
			if(listphotos!=null){
				if(position==listphotos.size()){
					holder.image.setImageBitmap(BitmapFactory.decodeResource(
							getResources(), R.drawable.icon_addpic_unfocused));
				}else{
					String url=beginStr+listphotos.get(position).getPk_photo()+endStr;//完整路径
					ImageLoaderUtils.getInstance().LoadImage(getActivity(), url, holder.image);
				}
			}
			//默认为加号图片
			if(listphotos==null){
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
			}

			return convertView;
		}

		public class ViewHolder {
			public TextView tv_progress;
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.tempSelectBitmap.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							Bimp.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						}
					}
				}
			}).start();
		}
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

	protected void onRestart() {
		adapter.update();
		super.onResume();
	}


	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	@SuppressWarnings("static-access")
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.tempSelectBitmap.size() < 9
					&& resultCode == getActivity().RESULT_OK) {

				String fileName = String.valueOf(System.currentTimeMillis());
				Bitmap bm = (Bitmap) data.getExtras().get("data");
				FileUtils.saveBitmap(bm, fileName);

				ImageItem takePhoto = new ImageItem();
				takePhoto.setBitmap(bm);
				Bimp.tempSelectBitmap.add(takePhoto);
				// 拍完照片让其马上显示出来
				SharedPreferences sp = getActivity().getSharedPreferences(
						"isPhoto", 0);
				Editor editor = sp.edit();
				editor.putBoolean("Photo", true);
				editor.commit();
//				getActivity().finish();
//				Intent intent = new Intent(getActivity(), GroupActivity.class);
//				startActivity(intent);
			}
			break;
		}
	}

//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			for (int i = 0; i < PublicWay.activityList.size(); i++) {
//				if (null != PublicWay.activityList.get(i)) {
//					PublicWay.activityList.get(i).finish();
//				}
//			}
//			System.exit(0);
//		}
//		return true;
//	}
	
	
	@Override
	public void onStop() {
		super.onStop();
//		isbeginUpload=false;
		//此处情况临时列表
	}
	
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.photo_upload_layout:
//			for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
//				String imagePath = Bimp.tempSelectBitmap.get(i)
//						.getImagePath();
//				Log.e("PhotoFragment", "每个图片路径：" + imagePath);
//			}
//			isbeginUpload = true;
//			adapter.notifyDataSetChanged();
//			break;
//
//		default:
//			break;
//		}
//	}

}
