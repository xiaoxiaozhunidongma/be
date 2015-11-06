package com.example.imageselected.photo;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biju.R;
import com.example.imageselected.photo.adapter.GirdItemAdapter;
import com.example.imageselected.photo.adapter.GirdItemAdapter.OnPhotoSelectedListener;
import com.example.imageselected.photo.adapter.ImageFloderAdapter;
/**
 * ���ͼƬѡ��
 * @author Administrator
 *
 */
public class SelectPhotoActivity extends Activity  {

	private GridView photoGrid;//ͼƬ�б�

	private Button photoBtn;//�ײ���һ����ť

	private TextView titleName;//ͷ���ı���

	private ImageView titleIcon;//ͷ��������ͼ��

	private ProgressDialog mProgressDialog;

	private HashSet<String> mDirPaths = new HashSet<String>();// ��ʱ�ĸ����࣬���ڷ�ֹͬһ���ļ��еĶ��ɨ��

	private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();// ɨ���õ����е�ͼƬ�ļ���

	int totalCount = 0;

	private File mImgDir=new File("");// ͼƬ���������ļ���

	private int mPicsSize;//�洢�ļ����е�ͼƬ����

	private List<String> mImgs=new ArrayList<String>();//���е�ͼƬ

	private int mScreenHeight;

	private LinearLayout dirLayout;

	private TextView quxiaoBtn;

	private ListView dirListView;

	private RelativeLayout headLayout;

	private int mScreenWidth;

	private ImageFloderAdapter floderAdapter;

	private GirdItemAdapter girdItemAdapter;

	private PopupWindow popupWindow;

	private View dirview;

	private static final int TAKE_CAMERA_PICTURE = 1000;//����

	private String path;

	private File dir;

	private String imagename;
	
	private int selectType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.photo_select_view);
		Intent intent = getIntent();
		selectType = intent.getIntExtra("SelectType", 1);
		GirdItemAdapter.mSelectedImage.clear();
		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		mScreenHeight = outMetrics.heightPixels;
		mScreenWidth = outMetrics.widthPixels;
		setView();
		getImages();
		initEvent();
	}
	/**
	 * ��ȡ�ؼ�
	 */
	private void setView() {
		photoGrid=(GridView)findViewById(R.id.gird_photo_list);
		photoBtn=(Button)findViewById(R.id.selected_photo_btn);
		titleName=(TextView)this.findViewById(R.id.selected_photo_name_text);
		quxiaoBtn=(TextView)findViewById(R.id.quxiao_btn);
		titleIcon=(ImageView)findViewById(R.id.selected_photo_icon);
		headLayout=(RelativeLayout)findViewById(R.id.selected_photo_header_layout);
		titleIcon.setBackgroundResource(R.drawable.navigationbar_arrow_down);
	}
	/**
	 * ����ContentProviderɨ���ֻ��е�ͼƬ���˷��������������߳��� ���ͼƬ��ɨ�裬���ջ��jpg�����Ǹ��ļ���
	 */
	private void getImages(){
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			Toast.makeText(this, "�����ⲿ�洢", Toast.LENGTH_SHORT).show();
			return;
		}
		// ��ʾ������
		mProgressDialog = ProgressDialog.show(this, null, "���ڼ���...");
		new Thread(new Runnable(){
			@Override
			public void run(){
				String firstImage = null;
				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = SelectPhotoActivity.this.getContentResolver();
				// ֻ��ѯjpeg��png��ͼƬ
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
								new String[] { "image/jpeg", "image/png" },
								MediaStore.Images.Media.DATE_MODIFIED);
				Log.e("TAG", mCursor.getCount() + "");
				while (mCursor.moveToNext()){
					// ��ȡͼƬ��·��
					String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
					Log.e("TAG", path);
					// �õ���һ��ͼƬ��·��
					if (firstImage == null)
						firstImage = path;
					// ��ȡ��ͼƬ�ĸ�·����
					File parentFile = new File(path).getParentFile();
					if (parentFile == null)
						continue;
					String dirPath = parentFile.getAbsolutePath();
					ImageFloder imageFloder = null;
					// ����һ��HashSet��ֹ���ɨ��ͬһ���ļ��У���������жϣ�ͼƬ�����������൱�ֲ���~~��
					if (mDirPaths.contains(dirPath)){
						continue;
					} else{
						mDirPaths.add(dirPath);
						// ��ʼ��imageFloder
						imageFloder = new ImageFloder();
						imageFloder.setDir(dirPath);
						imageFloder.setFirstImagePath(path);
					}
					if(parentFile.list()==null)continue ;
					int picSize = parentFile.list(new FilenameFilter(){
						@Override
						public boolean accept(File dir, String filename){
							if (filename.endsWith(".jpg")
									|| filename.endsWith(".png")
									|| filename.endsWith(".jpeg"))
								return true;
							return false;
						}
					}).length;
					totalCount += picSize;
					imageFloder.setCount(picSize);
					mImageFloders.add(imageFloder);

					if (picSize > mPicsSize){
						mPicsSize = picSize;
						mImgDir = parentFile;
					}
				}
				mCursor.close();

				// ɨ����ɣ�������HashSetҲ�Ϳ����ͷ��ڴ���
				mDirPaths = null;

				// ֪ͨHandlerɨ��ͼƬ���
				mHandler.sendEmptyMessage(0x110);

			}
		}).start();

	}
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg){
			mProgressDialog.dismiss();
			setDataView();// ΪView������
		}
	};



	/**
	 * ΪView������
	 */
	public void setDataView(){
		path=Environment.getExternalStorageDirectory() + "/"+"test/photo/";
		dir=new File(path); 
		if(!dir.exists())dir.mkdirs(); 
		if (mImgDir == null){
			Toast.makeText(getApplicationContext(), "һ��ͼƬûɨ�赽",Toast.LENGTH_SHORT).show();
			return;
		}

		if(mImgDir.exists()){
			mImgs = Arrays.asList(mImgDir.list());
		}
		
		
		girdItemAdapter=new GirdItemAdapter(this, mImgs, mImgDir.getAbsolutePath(), selectType);
		photoGrid.setAdapter(girdItemAdapter);
		girdItemAdapter.setOnPhotoSelectedListener(new OnPhotoSelectedListener() {
			@Override
			public void takePhoto() {
				imagename=getTimeName(System.currentTimeMillis())+".jpg";
				String state = Environment.getExternalStorageState();  
				if (state.equals(Environment.MEDIA_MOUNTED)) {  
					Intent intent=new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
					File f=new File(dir, imagename);//localTempImgDir��localTempImageFileName���Լ���������� 
					Uri u=Uri.fromFile(f); 
					intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0); 
					intent.putExtra(MediaStore.EXTRA_OUTPUT, u); 
					startActivityForResult(intent, TAKE_CAMERA_PICTURE); 
					Log.e("888888", "-------------0-------------------");
				} else {  
					Log.e("888888", "------------�����SD��-------------------");
					Toast.makeText(SelectPhotoActivity.this, "�����SD��",1).show();  
				}  
			}
			@Override
			public void photoClick(List<String> number) {
				photoBtn.setText("���("+number.size() + "��)");
			}
		});
	}
	/**
	 * ��ʼ��չʾ�ļ��е�popupWindw
	 */
	private void initListDirPopupWindw(){
		if(popupWindow==null){
			dirview=LayoutInflater.from(SelectPhotoActivity.this).inflate(R.layout.image_select_dirlist, null);
			dirListView=(ListView)dirview.findViewById(R.id.id_list_dirs);
			popupWindow =new PopupWindow(dirview, LayoutParams.MATCH_PARENT, mScreenHeight*3/5);
			floderAdapter=new ImageFloderAdapter(SelectPhotoActivity.this, mImageFloders);
			dirListView.setAdapter(floderAdapter);
		}
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		// �����Ϊ�˵��������Back��Ҳ��ʹ����ʧ�����Ҳ�����Ӱ����ı���
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// ��ʾ��λ��Ϊ:��Ļ�Ŀ�ȵ�һ��-PopupWindow�ĸ߶ȵ�һ��
		titleIcon.setBackgroundResource(R.drawable.navigationbar_arrow_up);
		popupWindow.showAsDropDown(headLayout,0,0);
		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				titleIcon.setBackgroundResource(R.drawable.navigationbar_arrow_down);
			}
		});
		dirListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				titleName.setText(mImageFloders.get(position).getName());
				mImgDir = new File(mImageFloders.get(position).getDir());
				mImgs = Arrays.asList(mImgDir.list(new FilenameFilter(){
					@Override
					public boolean accept(File dir, String filename)
					{
						if (filename.endsWith(".jpg") || filename.endsWith(".png")
								|| filename.endsWith(".jpeg"))
							return true;
						return false;
					}
				}));
				for (int i = 0; i < mImageFloders.size(); i++) {
					mImageFloders.get(i).setSelected(false);
				}
				mImageFloders.get(position).setSelected(true);
				floderAdapter.changeData(mImageFloders);
				girdItemAdapter.changeData(mImgs, mImageFloders.get(position).getDir());
				if(popupWindow!=null){
					popupWindow.dismiss();
				}
			}
		});

	}

	/**
	 * �����¼�
	 */
	private void initEvent(){
		quxiaoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		/**
		 * Ϊ�ײ��Ĳ������õ���¼�������popupWindow
		 */
		titleName.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				// ��ʼ��չʾ�ļ��е�popupWindw
				initListDirPopupWindw();
			}
		});
		photoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(SelectPhotoActivity.this, GirdItemAdapter.mSelectedImage.toString(), 1).show();
				Intent data=new Intent();
				data.putExtra("mSelectedImageList", GirdItemAdapter.mSelectedImage);
				setResult(Activity.RESULT_OK, data);
				finish();
			}
		});
	}
	public static String getTimeName(long time){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date(time);
		return formatter.format(date);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("888888", "-------------1-------------------");
		switch (requestCode) {
		case TAKE_CAMERA_PICTURE:
			Log.e("888888", "-------------2-------------------");
			Toast.makeText(this, path+imagename, 1).show();
			break;
		}
	}
	
}
