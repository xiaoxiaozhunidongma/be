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
	// ����·��completeURL=beginStr+result.filepath+endStr;
	private String completeURL;
	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "@!";
	private Integer SD_pk_user;
	private RelativeLayout mPhoto_upload_layout;
	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 110; // �����IMAGE_CODE���Լ����ⶨ���
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
		
		//��ȡsd���е�pk_user
		SD_pk_user = SdPkUser.getsD_pk_user();
		Log.e("PhotoFragment", "��SD���л�ȡ����Pk_user" + SD_pk_user);
		
		Intent intent = getIntent();
		pk_party = intent.getStringExtra("pk_party");
		party4 = (Party4)intent.getSerializableExtra("party4");
		listphotos = (ArrayList<Photo>) intent.getSerializableExtra("photos");
		
		InitUI();
		initPhotoUplisten();
//		initGroupPhotoListen();
//		initOnActivityResult();
		// ��ȡossService��sampleBucket
		ossService = MyApplication.getOssService();
		sampleBucket = MyApplication.getSampleBucket();
	}

//	private void initGroupPhotoListen() {
//		instance.setPostListener(new readPartyPhotosListenner() {
//			
//
//			@Override
//			public void success(String A) {
//				Log.e("PhotoFragment2", "���ص�ͼƬ���飺"+A);
//
//				Photosback photosback = GsonUtils.parseJsonArray(A, Photosback.class);
//				listphotos = photosback.getReturnData();
//				
//				//�����
//				bitmaps.clear();
//				//�����
//				MyGalleryActivity.netpath.clear();
//				
//				for (int i = 0; i < listphotos.size(); i++) {
//					String path = listphotos.get(i).getPath();
//					String pk_photo = listphotos.get(i).getPk_photo();
//					final String completeUrl=beginStr+pk_photo+endStr+"album-thumbnail";
//					Log.e("PhotoFragment2", "completeUrl"+completeUrl);
//					//��·��ȫ����������
//					MyGalleryActivity.netpath.add(completeUrl);
//				}
//				
//				
//				if(listphotos!=null&&listphotos.size()>0){
//					String pk_photo = listphotos.get(0).getPk_photo();
//					Log.e("PhotoFragment2", "��һ��ͼƬ·����"+pk_photo);
//				}
//				//ˢ��
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
				Log.e("PhotoActivity", "ͼƬ�Ƿ��ϴ���"+A);
//				listphotos.add(photo);
				runOnUiThread( new Runnable() {
					public void run() {
						tv_partyphotonum.setText(String.valueOf(listphotos.size()));
					}
				});
				adapter.notifyDataSetChanged();
				
				//�����
				MyGalleryActivity.netFullpath.clear();
				
				for (int i = 0; i < listphotos.size(); i++) {
					String pk_photo = listphotos.get(i).getPk_photo();
					final String completeUrl=beginStr+pk_photo;	//ԭͼ·��
					Log.e("PhotoActivity", "completeUrl"+completeUrl);
					//��·��ȫ����������
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
				// // ���ͼ���ʱ��ر�����
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
					String url=beginStr+listphotos.get(position).getPk_photo()+endStr+"album-thumbnail";//����·��
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
		
		//�����
		MyGalleryActivity.netFullpath.clear();
		
		for (int i = 0; i < listphotos.size(); i++) {
			String pk_photo = listphotos.get(i).getPk_photo();
			final String completeUrl=beginStr+pk_photo;	//ԭͼ·��
			Log.e("PhotoActivity", "completeUrl"+completeUrl);
			//��·��ȫ����������
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
//		// ʹ��intent����ϵͳ�ṩ����Ṧ�ܣ�ʹ��startActivityForResult��Ϊ�˻�ȡ�û�ѡ���ͼƬ
//		Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
//		getAlbum.setType(IMAGE_TYPE);
//		startActivityForResult(getAlbum, IMAGE_CODE);
		
		Intent getAlbum = new Intent(this, SelectPhotoActivity.class);
		getAlbum.putExtra("SelectType", 1);//�����ϴ�ģʽ
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
//					Toast toast = Toast.makeText(PhotoActivity.this, "�Ҳ���ͼƬ", Toast.LENGTH_SHORT);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
//					return;
//				}
//			}
//			Log.e("PhotoActivity.this", "mFilePath======"+mFilePath);
////				Bitmap bmp = Utils.decodeSampledBitmap(mFilePath, 2);
////				Bitmap bmp = Bimp.revitionImageSize(mFilePath);
//			//���mFilePath������������ͼ·��
////				Bitmap bmp = MyBimp.revitionImageSize(mFilePath);
		
		if (requestCode != 110 || data == null){
			return;
		}
		@SuppressWarnings("unchecked")
		ArrayList<String> mSelectedImageList = (ArrayList<String>) data.getSerializableExtra("mSelectedImageList");
//		mFilePath=mSelectedImageList.get(0);
		Log.e("PhotoActivity", "mSelectedImageList.size()======" + mSelectedImageList.size());
		Log.e("PhotoActivity", "mFilePath======" + mFilePath);
		
		//�����ϴ�
		for (int z = 0; z < mSelectedImageList.size(); z++) {
			mFilePath=mSelectedImageList.get(z);
			if(listphotos.size()==0){
				Log.e("PhotoActivity.this", "�ϴ���һ��ͼƬ");
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
				hasloaded=false;//Ĭ��û���ϴ�
				for (int i = 0; i < listphotos.size(); i++) {
					String path = listphotos.get(i).getPath();
					if(mFilePath.equals(path)){
						Toast.makeText(PhotoActivity.this, "�Ѿ����ظ���ͼƬ��Ŷ", Toast.LENGTH_SHORT).show();
						hasloaded = true;
					}
				}
				if(hasloaded==false){
					Log.e("�����ϴ���ͼƬ·��", ""+mFilePath);
//					upload(mFilePath);
					//��ʼOss�ϴ�
					Bitmap convertToBitmap = null;
					try {
						convertToBitmap = Path2Bitmap.convertToBitmap(mFilePath);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//�ϴ�ԭͼ
					Bitmap limitLongScaleBitmap = LimitLong.limitLongScaleBitmap(
							convertToBitmap, 1080);// �������Ϊ1080
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
//		Log.e("PhotoActivity", "onDestroy()����������");
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
//	       System.gc();  //����ϵͳ��ʱ����
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
		ossData.setData(data, "jpg"); // ָ����Ҫ�ϴ������ݺ���������
		ossData.enableUploadCheckMd5sum(); // �����ϴ�MD5У��
		ossData.uploadInBackground(new SaveCallback() {

			@Override
			public void onSuccess(String objectKey) {
				Log.e("PhotoActivity.this", "ͼƬ�ϴ��ɹ�");
				Log.e("PhotoActivity.this", "objectKey==" + objectKey);
				
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy��MM��dd��");
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
				// �������ڼ�
				int y = Integer.valueOf(years);
				int m = Integer.valueOf(months);
				int d = Integer.valueOf(days);
				// ���ü������ڼ��ķ���
				Weeks.CaculateWeekDay(y, m, d);
				String week = Weeks.getweek();
				Log.e("", "week=="+week);
				
				String compleTime=format+" "+week+" "+format3;
				
				Photo photo = new Photo();
				photo.setFk_group(GroupActivity.getPk_group());
				photo.setFk_user(SD_pk_user);
				photo.setPk_photo(objectKey);//pk_photo
				photo.setStatus(1);
				photo.setPath(imagePath);//�����ڴ�·��
				photo.setFk_party(pk_party);//����pk_party
				photo.setCreate_time(compleTime);//���ô���ʱ��
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
				Log.e("PhotoActivity.this", "�ϴ�ʧ�ܣ��������ϴ�" + ossException.toString());
				//����OSSʧ�ܣ���UI�̵߳�����~~~~~~
//				new Handler().post(new Runnable() {
//					
//					@Override
//					public void run() {
//						Toast.makeText(PhotoActivity.this, "�ϴ�ʧ�ܣ��������ϴ�",Toast.LENGTH_SHORT).show();
//					}
//				});
			}
		});
	}
	

}
