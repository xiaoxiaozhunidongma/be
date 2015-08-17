package com.BJ.photo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.biju.R;
import com.biju.function.GroupActivity;
import com.fragment.PhotoFragment;


/**
 * ����ǽ��������ʾ����ͼƬ�Ľ���
 * 
 * @author king
 * @QQ:595163260
 * @version 2014��10��18�� ����11:47:15
 */
public class AlbumActivity extends Activity {
	// ��ʾ�ֻ��������ͼƬ���б�ؼ�
	private GridView gridView;
	// ���ֻ���û��ͼƬʱ����ʾ�û�û��ͼƬ�Ŀؼ�
	private TextView tv;
	// gridView��adapter
	private AlbumGridViewAdapter gridImageAdapter;
	// ��ɰ�ť
	private Button okButton;
	private Intent intent;
	// Ԥ����ť
	private Button preview;
	private Context mContext;
	private ArrayList<ImageItem> dataList;
	private AlbumHelper helper;
	public static List<ImageBucket> contentList;
	public static Bitmap bitmap;

	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plugin_camera_album);
		PublicWay.activityList.add(this);
		mContext = AlbumActivity.this;
		// ע��һ���㲥������㲥��Ҫ��������GalleryActivity����Ԥ��ʱ����ֹ������ͼƬ��ɾ������ٻص���ҳ��ʱ��ȡ��ѡ�е�ͼƬ�Դ���ѡ��״̬
		IntentFilter filter = new IntentFilter("data.broadcast.action");
		registerReceiver(broadcastReceiver, filter);
		bitmap = BitmapFactory.decodeResource(getResources(),
				Res.getDrawableID("plugin_camera_no_pictures"));
		init();
		initListener();
		// ���������Ҫ��������Ԥ������ɰ�ť��״̬
		isShowOkBt();
		Log.e("AlbumActivity", "������onCreate=======");
	}
	
	
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			gridImageAdapter.notifyDataSetChanged();
		}
	};
	private TextView mPhoto_num;

	// Ԥ����ť�ļ���
	private class PreviewListener implements OnClickListener {
		public void onClick(View v) {
			if (Bimp.tempSelectBitmap.size() > 0) {
				intent.putExtra("position", "1");
				intent.setClass(AlbumActivity.this, GalleryActivity.class);
				startActivity(intent);
			}
		}

	}

	// ��ɰ�ť�ļ���
	private class AlbumSendListener implements OnClickListener {
		public void onClick(View v) {
//			PhotoFragment.beginUpload.begin();
			
			SharedPreferences sp=getSharedPreferences("isPhoto", 0);
			Editor editor=sp.edit();
			editor.putBoolean("Photo", true);
			editor.commit();
			overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
//			intent.setClass(mContext, GroupActivity.class);
//			startActivity(intent);
			setResult(111);
			finish();
		}

	}

	// ��ʼ������һЩ����ֵ
	private void init() {
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		contentList = helper.getImagesBucketList(false);
		dataList = new ArrayList<ImageItem>();
		for (int i = 0; i < contentList.size(); i++) {
			dataList.addAll(contentList.get(i).imageList);
		}

		mPhoto_num = (TextView) findViewById(R.id.photo_num);

		preview = (Button) findViewById(Res.getWidgetID("preview"));
		preview.setOnClickListener(new PreviewListener());
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		gridView = (GridView) findViewById(Res.getWidgetID("myGrid"));
		gridImageAdapter = new AlbumGridViewAdapter(this, dataList,
				Bimp.tempSelectBitmap);
		gridView.setAdapter(gridImageAdapter);
		tv = (TextView) findViewById(Res.getWidgetID("myText"));
		gridView.setEmptyView(tv);
		okButton = (Button) findViewById(Res.getWidgetID("ok_button"));
		mPhoto_num.setText(Res.getString("finish") + "("
				+ Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
	}

	private void initListener() {

		gridImageAdapter
				.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(final ToggleButton toggleButton,
							int position, boolean isChecked, Button chooseBt) {
						if (Bimp.tempSelectBitmap.size() >= PublicWay.num) {
							toggleButton.setChecked(false);
							chooseBt.setVisibility(View.GONE);
							if (!removeOneData(dataList.get(position))) {
								Toast.makeText(AlbumActivity.this,
										Res.getString("only_choose_num"), 200)
										.show();
							}
							return;
						}
						if (isChecked) {
							chooseBt.setVisibility(View.VISIBLE);
							Bimp.tempSelectBitmap.add(dataList.get(position));
							mPhoto_num.setText(Res.getString("finish") + "("
									+ Bimp.tempSelectBitmap.size() + "/"
									+ PublicWay.num + ")");
						} else {
							Bimp.tempSelectBitmap.remove(dataList.get(position));
							chooseBt.setVisibility(View.GONE);
							mPhoto_num.setText(Res.getString("finish") + "("
									+ Bimp.tempSelectBitmap.size() + "/"
									+ PublicWay.num + ")");
						}
						isShowOkBt();
					}
				});

		okButton.setOnClickListener(new AlbumSendListener());

	}

	private boolean removeOneData(ImageItem imageItem) {
		if (Bimp.tempSelectBitmap.contains(imageItem)) {
			Bimp.tempSelectBitmap.remove(imageItem);
			mPhoto_num.setText(Res.getString("finish") + "("
					+ Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
			return true;
		}
		return false;
	}

	public void isShowOkBt() {
		if (Bimp.tempSelectBitmap.size() > 0) {
			mPhoto_num.setText(Res.getString("finish") + "("
					+ Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
			preview.setPressed(true);
			okButton.setPressed(true);
			preview.setClickable(true);
			okButton.setClickable(true);
			okButton.setTextColor(Color.WHITE);
			preview.setTextColor(Color.BLACK);
		} else {
			mPhoto_num.setText(Res.getString("finish") + "("
					+ Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
			preview.setPressed(false);
			preview.setClickable(false);
			okButton.setPressed(false);
			okButton.setClickable(false);
			okButton.setTextColor(Color.parseColor("#E1E0DE"));
			preview.setTextColor(Color.parseColor("#E1E0DE"));
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			SharedPreferences sp = getSharedPreferences("isPhoto", 0);
			Editor editor = sp.edit();
			editor.putBoolean("Photo", true);
			editor.commit();
			//??????????????????
//			Bimp.tempSelectBitmap.clear();
			finish();
		}
		return false;

	}
	@Override
	protected void onRestart() {
		isShowOkBt();
		Log.e("AlbumActivity", "������onRestart=======");
		super.onRestart();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(broadcastReceiver);
		super.onDestroy();
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		contentList = helper.getImagesBucketList(false);
		dataList = new ArrayList<ImageItem>();
		for (int i = 0; i < contentList.size(); i++) {
			dataList.addAll(contentList.get(i).imageList);
		}
		super.onStart();
	}
}
