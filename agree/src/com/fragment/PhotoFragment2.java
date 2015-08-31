package com.fragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.BJ.javabean.PicSignBack;
import com.BJ.javabean.User;
import com.BJ.photo.AlbumActivity;
import com.BJ.photo.Bimp;
import com.BJ.photo.GalleryActivity;
import com.BJ.utils.AsynImageLoader;
import com.BJ.utils.AsynImageLoader.ImageCallback;
import com.BJ.utils.ImageLoaderUtils4Photos;
import com.BJ.utils.MyBimp;
import com.BJ.utils.Path2Bitmap;
import com.BJ.utils.PicUtil;
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
	private UploadManager uploadManager;
	String fileId = "";
//	HashMap<Integer, UploadTask> hashMap = new HashMap<Integer, UploadTask>();
	private static final int TAKE_PICTURE = 0x000001;
//	private boolean isbeginUpload;
	private Interface instance;
	private ArrayList<Photo> listphotos=new ArrayList<Photo>();
	// ����·��completeURL=beginStr+result.filepath+endStr;
	private String completeURL;
	private String beginStr = "http://201139.image.myqcloud.com/201139/0/";
	private String endStr = "/original";
	private Integer SD_pk_user;
	private RelativeLayout mPhoto_upload_layout;
	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 110; // �����IMAGE_CODE���Լ����ⶨ���
	private String mFilePath;
	public static ArrayList<Bitmap> bitmaps=new ArrayList<Bitmap>();
	private boolean hasloaded;


	public PhotoFragment2() {
		// Required empty public constructor
	}
	


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			mLayout = inflater.inflate(R.layout.activity_selectimg, container,false);
			
			//��ȡsd���е�pk_user
			SD_pk_user = SdPkUser.getsD_pk_user();
			Log.e("PhotoFragment", "��SD���л�ȡ����Pk_user" + SD_pk_user);
			
			Init(inflater);
			get4PicSign();
			initPhotoUplisten();
			initGroupPhotoListen();
//			Interface.getInstance().setOnActivityResultListener(new OnArticleSelectedListener() {
//				
//				@Override
//				public void MyOnActivityResult(String mFilePath, Bitmap bmp) {
//					Log.e("PhotoFragment2", "�ص�");
//					for (int i = 0; i < listphotos.size(); i++) {
//						String path = listphotos.get(i).getPath();
//						if(!mFilePath.equals(path)){
//							upload(mFilePath);
//							return;
//						}
//					}
//				}
//			});
		return mLayout;
	}
	



	private void initGroupPhotoListen() {
		instance.setPostListener(new readPartyPhotosListenner() {
			

			@Override
			public void success(String A) {
				Log.e("PhotoFragment", "���ص�ͼƬ���飺"+A);

				Photosback photosback = GsonUtils.parseJsonArray(A, Photosback.class);
				listphotos = photosback.getReturnData();
				
				//��ȡ���ͼƬbitmap����
				for (int i = 0; i < bitmaps.size(); i++) {
					Bitmap bitmap = bitmaps.get(i);
					//�����ڴ�
					if(!bitmap.isRecycled()){
						bitmap.recycle();
					}
				}
				//�����
				bitmaps.clear();
				
				for (int i = 0; i < listphotos.size(); i++) {
					String path = listphotos.get(i).getPath();
					if(!"".equals(path)){
						//??????????????????
						Log.e("PhotoFragment2", "����ȡ�ĵ�·��path============"+path);
						Bitmap convertToBitmap = Path2Bitmap.convertToBitmap(path, 400, 400);
						bitmaps.add(convertToBitmap);
					}
					
				}
				
				
				if(listphotos!=null&&listphotos.size()>0){
					String pk_photo = listphotos.get(0).getPk_photo();
					Log.e("PhotoFragment", "��һ��ͼƬ·����"+pk_photo);
				}
				//ˢ��
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
				Log.e("PhotoFragment", "ͼƬ�Ƿ��ϴ���"+A);
				//��ȡС�����
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
		//��ȡ���ͼƬbitmap����
		for (int i = 0; i < bitmaps.size(); i++) {
			Bitmap bitmap = bitmaps.get(i);
			//�����ڴ�
			if(!bitmap.isRecycled()){
				bitmap.recycle();
			}
		}
		//�����
		bitmaps.clear();
		super.onDestroyView();
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
		//��ȡС�����
		Group group=new Group();
		group.setPk_group(GroupActivity.getPk_group());
		group.setStatus(1);
		instance.readPartyPhotos(getActivity(), group);
		
	}


	private void upload(final String imagePath) {
		 UploadTask task = new PhotoUploadTask(imagePath, new IUploadTaskListener() {
			@Override
			public void onUploadSucceed(final FileInfo result) {
				Log.e("�ϴ����", "upload succeed: " + result.fileId);
				
				Photo photo = new Photo();
				photo.setFk_group(GroupActivity.getPk_group());
				photo.setFk_user(SD_pk_user);
				photo.setPk_photo(result.fileId);//pk_photo
				photo.setStatus(1);
				photo.setPath(imagePath);//�����ڴ�·��
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
				// Log.e("�ϴ�����", "�ϴ�����: " + p + "%");
			}

			@Override
			public void onUploadFailed(final int errorCode,
					final String errorMsg) {
				Log.e("Demo", "�ϴ����:ʧ��! ret:" + errorCode + " msg:" + errorMsg);
			}
		});
		 
		uploadManager.upload(task); // ��ʼ�ϴ�

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
		// ע��ǩ��
		UploadManager.authorize(APPID, USERID, SIGN);
		uploadManager = new UploadManager(getActivity(), "persistenceId");

	}

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
				// // ���ͼ���ʱ��ر�����
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
		adapter.update();
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

		public void update() {
			loading();
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
					String url=beginStr+listphotos.get(position).getPk_photo()+endStr;//����·��
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

//	protected void onRestart() {
//		adapter.update();
//		super.onResume();
//	}


	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("PhotoFragment2", "onActivityResult");
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
					Toast toast = Toast.makeText(getActivity(), "�Ҳ���ͼƬ", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					return;
				}
			}
			Log.e("NewteamActivity", "mFilePath======"+mFilePath);
//				Bitmap bmp = Utils.decodeSampledBitmap(mFilePath, 2);
//				Bitmap bmp = Bimp.revitionImageSize(mFilePath);
			//���mFilePath������������ͼ·��
//				Bitmap bmp = MyBimp.revitionImageSize(mFilePath);
			
		switch (requestCode) {
		case 110:
			
			if(listphotos.size()==0){
				Log.e("Photofragment2", "�ϴ���һ��ͼƬ");
				upload(mFilePath);
			}else{
				hasloaded=false;//Ĭ��û���ϴ�
				for (int i = 0; i < listphotos.size(); i++) {
					String path = listphotos.get(i).getPath();
					if(mFilePath.equals(path)){
						Toast.makeText(getActivity(), "�Ѿ����ظ���ͼƬ��Ŷ", Toast.LENGTH_SHORT).show();
						hasloaded = true;
					}
				}
				if(hasloaded==false){
					Log.e("�����ϴ���ͼƬ·��", ""+mFilePath);
					upload(mFilePath);
				}
			}
			
			break;
		}
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
		// ʹ��intent����ϵͳ�ṩ����Ṧ�ܣ�ʹ��startActivityForResult��Ϊ�˻�ȡ�û�ѡ���ͼƬ
		Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
		getAlbum.setType(IMAGE_TYPE);
//		getActivity().startActivityForResult(getAlbum, IMAGE_CODE);
		startActivityForResult(getAlbum, IMAGE_CODE);
	}



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(getActivity(),
				GalleryActivity.class);
		intent.putExtra("position", "1");
		intent.putExtra("ID", position);
		startActivity(intent);
	}
	
	
	
}
