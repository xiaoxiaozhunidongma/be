package com.biju.pay;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.BJ.javabean.ImageText;
import com.BJ.utils.LimitLong;
import com.BJ.utils.Path2Bitmap;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.biju.R;

public class GraphicDetailsActivity extends Activity implements OnClickListener{

	private ListView mGraphicDetailsListview;
	private MyGraphicDetailsAdapter myGraphicDetailsAdapter;
	private List<ImageText> DetailsList=new ArrayList<ImageText>();
	private boolean isEdit;
	private int SING = -1;
	private int LIGHTGRAY = 1;
	private int DARKGRAY = 2;
	private int BLACK = 3;
	private int RED = 4;
	private int BLUE = 5;
	private int GREEN = 6;
	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0; // 这里的IMAGE_CODE是自己任意定义的
	protected String mFilePath = null;
	
	private RelativeLayout mGraphicDetailsOKLayout;
	private RelativeLayout mGraphicDetailsPreviewLayout;
	private RelativeLayout mEditTextEditLayout;
	private RelativeLayout mEditImageTextLayout;
	private EditText mEditText_EditText;
	private ImageView mEditTextLightGrayImage;
	private ImageView mEditTextDarkGrayImage;
	private ImageView mEditTextBlackImage;
	private ImageView mEditTextRedImage;
	private ImageView mEditTextBlueImage;
	private ImageView mEditTextGreenImage;
	private ImageText imageText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graphic_details);
		SharedPreferences GraphicDetails_sp=getSharedPreferences("GraphicDetails", 0);
		boolean IsGraphicDetails=GraphicDetails_sp.getBoolean("IsGraphicDetails", false);
		initUI();
		if(IsGraphicDetails){
			initDB();
		}
	}

	private void initDB() {
		// 查表
		DetailsList = new Select().from(ImageText.class).execute();
		if(DetailsList.size()>0){
			for (int i = 0; i < DetailsList.size(); i++) {
				Integer Type=DetailsList.get(i).getType();
				if(1==Type){
					String mEditText=DetailsList.get(i).getText();
					String SING=DetailsList.get(i).getFont_color();
					//存入数据库
					ImageText text=new ImageText(null, null, 1, mEditText, null, null, 13, SING, null, null, null, 1);
					text.save();
				}else if (2==Type){
					String mFilePath=DetailsList.get(i).getImage_path();
					Integer image_height=DetailsList.get(i).getImage_height();
					Integer image_width=DetailsList.get(i).getImage_width();
					//存入数据库
					ImageText text=new ImageText(null, null, 2, null, mFilePath, null, null, null, image_height, image_width, null, 1);
					text.save();
				}
			}
			myGraphicDetailsAdapter.notifyDataSetChanged();
		}
		Log.e("GraphicDetailsActivity", "ImageTextList的长度======"+DetailsList.size());
	}

	private void initUI() {
		mGraphicDetailsPreviewLayout = (RelativeLayout) findViewById(R.id.GraphicDetailsPreviewLayout);//预览
		mGraphicDetailsPreviewLayout.setOnClickListener(this);//预览
		findViewById(R.id.GraphicDetailsPreview).setOnClickListener(this);
		findViewById(R.id.GraphicDetailsBackLayout).setOnClickListener(this);
		findViewById(R.id.GraphicDetailsBack).setOnClickListener(this);
		mGraphicDetailsOKLayout = (RelativeLayout) findViewById(R.id.GraphicDetailsOKLayout);//保存
		mGraphicDetailsOKLayout.setOnClickListener(this);
		findViewById(R.id.GraphicDetailsOK).setOnClickListener(this);
		
		mGraphicDetailsListview = (ListView) findViewById(R.id.GraphicDetailsListview);
		mGraphicDetailsListview.setDividerHeight(0);
		mGraphicDetailsListview.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
		
		View mFooterView=View.inflate(GraphicDetailsActivity.this, R.layout.edit_image_text_item, null);
		mGraphicDetailsListview.addFooterView(mFooterView);
		mFooterView.findViewById(R.id.EditImageAndTextLayout).setOnClickListener(this);//文字编辑
		mFooterView.findViewById(R.id.EditImageLayout).setOnClickListener(this);//图片编辑
		mEditTextEditLayout = (RelativeLayout) mFooterView.findViewById(R.id.EditTextEditLayout);//弹出文字编辑
		mEditImageTextLayout = (RelativeLayout) mFooterView.findViewById(R.id.EditImageTextLayout);//显示图文编辑栏
		mEditText_EditText = (EditText) mFooterView.findViewById(R.id.EditText_EditText);//文字编辑框
		
		InputMethodManager();//隐藏键盘
		
		myGraphicDetailsAdapter = new MyGraphicDetailsAdapter();
		mGraphicDetailsListview.setAdapter(myGraphicDetailsAdapter);
		
		initTextColorUI();
		
	}

	private void initTextColorUI() {
		findViewById(R.id.EditTextLightGrayLayout).setOnClickListener(this);//浅灰色
		findViewById(R.id.EditTextDarkGrayLayout).setOnClickListener(this);//深灰色
		findViewById(R.id.EditTextBlackLayout).setOnClickListener(this);//黑色
		findViewById(R.id.EditTextRedLayout).setOnClickListener(this);//红色
		findViewById(R.id.EditTextBlueLayout).setOnClickListener(this);//蓝色
		findViewById(R.id.EditTextGreenLayout).setOnClickListener(this);//绿色
		
		mEditTextLightGrayImage = (ImageView) findViewById(R.id.EditTextLightGrayImage);//浅灰色打钩
		mEditTextDarkGrayImage = (ImageView) findViewById(R.id.EditTextDarkGrayImage);//深灰色打钩
		mEditTextBlackImage = (ImageView) findViewById(R.id.EditTextBlackImage);//黑色打钩
		mEditTextRedImage = (ImageView) findViewById(R.id.EditTextRedImage);//红色打钩
		mEditTextBlueImage = (ImageView) findViewById(R.id.EditTextBlueImage);//蓝色打钩
		mEditTextGreenImage = (ImageView) findViewById(R.id.EditTextGreenImage);//绿色打钩
		
		mEditTextLightGrayImage.setOnClickListener(this);
		mEditTextDarkGrayImage.setOnClickListener(this);
		mEditTextBlackImage.setOnClickListener(this);
		mEditTextRedImage.setOnClickListener(this);
		mEditTextBlueImage.setOnClickListener(this);
		mEditTextGreenImage.setOnClickListener(this);
	}

	class ViewHolder{
		TextView Text_show;
		RelativeLayout EditText_TextLayout;
		RelativeLayout EditText_ImageLayout;
		ImageView InsertImage;
		RelativeLayout TextMinusLayout;
		RelativeLayout InsertImageMinusLayout;
	}

	public class MyGraphicDetailsAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return DetailsList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View inflater=null;
			ViewHolder holder=null;
			if(convertView == null){
				holder=new ViewHolder();
				LayoutInflater layoutInflater = getLayoutInflater();
				inflater=layoutInflater.inflate(R.layout.insert_image_itrem, null);
				holder.Text_show=(TextView) inflater.findViewById(R.id.Text_show);
				holder.EditText_TextLayout=(RelativeLayout) inflater.findViewById(R.id.EditText_TextLayout);
				holder.EditText_ImageLayout=(RelativeLayout) inflater.findViewById(R.id.EditText_ImageLayout);
				holder.InsertImage=(ImageView) inflater.findViewById(R.id.InsertImage);
				holder.TextMinusLayout=(RelativeLayout) inflater.findViewById(R.id.TextMinusLayout);
				holder.InsertImageMinusLayout=(RelativeLayout) inflater.findViewById(R.id.InsertImageMinusLayout);
				inflater.setTag(holder);
			}else {
				inflater=convertView;
				holder=(ViewHolder) inflater.getTag();
			}
			imageText = DetailsList.get(position);
			Integer type = imageText.getType(); 
			Log.e("GraphicDetailsActivity", "当前的类型===="+type);
			if(1 == type){
				holder.EditText_TextLayout.setVisibility(View.VISIBLE);
				holder.EditText_ImageLayout.setVisibility(View.GONE);
				Integer sing=Integer.valueOf(imageText.getFont_color());
				switch (sing) {
				case 1:
					holder.Text_show.setTextColor(holder.Text_show.getResources().getColor(R.drawable.EditTextLightGrayColor));
					break;
				case 2:
					holder.Text_show.setTextColor(holder.Text_show.getResources().getColor(R.drawable.EditTextDarkGrayColor));
					break;
				case 3:
					holder.Text_show.setTextColor(holder.Text_show.getResources().getColor(R.drawable.EditTextBlackColor));
					break;
				case 4:
					holder.Text_show.setTextColor(holder.Text_show.getResources().getColor(R.drawable.EditTextRedColor));
					break;
				case 5:
					holder.Text_show.setTextColor(holder.Text_show.getResources().getColor(R.drawable.EditTextBlueColor));
					break;
				case 6:
					holder.Text_show.setTextColor(holder.Text_show.getResources().getColor(R.drawable.EditTextGreenColor));
					break;

				default:
					break;
				}
				holder.Text_show.setText(imageText.getText()+"");
				holder.TextMinusLayout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						DetailsList.remove(position);
						myGraphicDetailsAdapter.notifyDataSetChanged();
						Log.e("GraphicDetailsActivity", "删除了文字的========="+position);
						Log.e("GraphicDetailsActivity", "删除了文字的DetailsList的长度========="+DetailsList.size());
					}
				});
			}else if(2 == type){
				holder.EditText_TextLayout.setVisibility(View.GONE);
				holder.EditText_ImageLayout.setVisibility(View.VISIBLE);
				String mPath=imageText.getImage_path();
				Bitmap convertToBitmap = null;
				try {
					convertToBitmap = Path2Bitmap.convertToBitmap(mPath);
					Log.e("GraphicDetailsActivity", "获取的原来图片的宽和高======"+convertToBitmap.getWidth()+"        "+convertToBitmap.getHeight());
					Bitmap limitLongScaleBitmap = LimitLong.limitLongScaleBitmap(convertToBitmap, 1280);
//					Bitmap centerSquareScaleBitmap = PicCutter.centerSquareScaleBitmap(limitLongScaleBitmap, 180);
					holder.InsertImage.setImageBitmap(limitLongScaleBitmap);
					Log.e("GraphicDetailsActivity", "获取的截取后图片的宽和高======"+limitLongScaleBitmap.getWidth()+"          "+limitLongScaleBitmap.getHeight());
				} catch (IOException e) {
					e.printStackTrace();
				}
				holder.InsertImageMinusLayout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						DetailsList.remove(position);
						myGraphicDetailsAdapter.notifyDataSetChanged();
						Log.e("GraphicDetailsActivity", "删除了图片的========="+position);
					}
				});
			}
			return inflater;
		}
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.EditImageAndTextLayout:
			EditImageAndTextLayout();
			break;
		case R.id.EditImageLayout:
			EditImageLayout();
			break;
		case R.id.GraphicDetailsOK:
		case R.id.GraphicDetailsOKLayout:
			GraphicDetailsOK();
			break;
		case R.id.GraphicDetailsBack:
			GraphicDetailsBack();
			break;
		case R.id.EditTextLightGrayLayout:
		case R.id.EditTextLightGrayImage:
			EditTextLightGrayLayout();
			break;
		case R.id.EditTextDarkGrayLayout:
		case R.id.EditTextDarkGrayImage:
			EditTextDarkGrayLayout();
			break;
		case R.id.EditTextBlackLayout:
		case R.id.EditTextBlackImage:
			EditTextBlackLayout();
			break;
		case R.id.EditTextRedLayout:
		case R.id.EditTextRedImage:
			EditTextRedLayout();
			break;
		case R.id.EditTextBlueLayout:
		case R.id.EditTextBlueImage:
			EditTextBlueLayout();
			break;
		case R.id.EditTextGreenLayout:
		case R.id.EditTextGreenImage:
			EditTextGreenLayout();
			break;
		case R.id.GraphicDetailsPreviewLayout:
		case R.id.GraphicDetailsPreview:
			GraphicDetailsPreviewLayout();
			break;
		default:
			break;
		}
	}
	
	//预览
	private void GraphicDetailsPreviewLayout() {
		GraphicDetailsPreview2();
		Intent intent=new Intent(GraphicDetailsActivity.this, PreviewActivity.class);
		startActivity(intent);
	}

	//预览时保存
	private void GraphicDetailsPreview2() {
		GraphicDetailsPreview3();
		SharedPreferences GraphicDetails_sp=getSharedPreferences("GraphicDetails", 0);
		Editor editor=GraphicDetails_sp.edit();
		editor.putBoolean("IsGraphicDetails", false);
		editor.commit();
	}

	private void GraphicDetailsPreview3() {
		//先清除数据库,无条件全部删除，然后再保存
		new Delete().from(ImageText.class).execute();
		//进行保存
		if(DetailsList.size()>0){
			for (int i = 0; i < DetailsList.size(); i++) {
				Integer Type=DetailsList.get(i).getType();
				if(1==Type){
					String mEditText=DetailsList.get(i).getText();
					String SING=DetailsList.get(i).getFont_color();
					//存入数据库
					ImageText text=new ImageText(null, null, 1, mEditText, null, null, 13, SING, null, null, null, 1);
					text.save();
				}else if (2==Type){
					String mFilePath=DetailsList.get(i).getImage_path();
					Integer image_height=DetailsList.get(i).getImage_height();
					Integer image_width=DetailsList.get(i).getImage_width();
					//存入数据库
					ImageText text=new ImageText(null, null, 2, null, mFilePath, null, null, null, image_height, image_width, null, 1);
					text.save();
				}
			}
		}
	}

	//返回时保存
	private void GraphicDetailsPreview1() {
		GraphicDetailsPreview3();
		SharedPreferences GraphicDetails_sp=getSharedPreferences("GraphicDetails", 0);
		Editor editor=GraphicDetails_sp.edit();
		editor.putBoolean("IsGraphicDetails", true);
		editor.putInt("GraphicDetailsNumber", DetailsList.size());
		editor.commit();
	}

	//绿色字体颜色
	private void EditTextGreenLayout() {
		SING=GREEN;
		mEditTextGreenImage.setBackgroundResource(R.drawable.tick_green);
		mEditTextLightGrayImage.setBackgroundResource(R.drawable.round_light_gray);
		mEditTextDarkGrayImage.setBackgroundResource(R.drawable.round_dark_gray);
		mEditTextBlackImage.setBackgroundResource(R.drawable.round_black);
		mEditTextRedImage.setBackgroundResource(R.drawable.round_red);
		mEditTextBlueImage.setBackgroundResource(R.drawable.round_blue);
		mEditText_EditText.setTextColor(mEditText_EditText.getResources().getColor(R.drawable.EditTextGreenColor));
	}

	//蓝色字体颜色
	private void EditTextBlueLayout() {
		SING=BLUE;
		mEditTextBlueImage.setBackgroundResource(R.drawable.tick_blue);
		mEditTextLightGrayImage.setBackgroundResource(R.drawable.round_light_gray);
		mEditTextDarkGrayImage.setBackgroundResource(R.drawable.round_dark_gray);
		mEditTextBlackImage.setBackgroundResource(R.drawable.round_black);
		mEditTextRedImage.setBackgroundResource(R.drawable.round_red);
		mEditTextGreenImage.setBackgroundResource(R.drawable.round_green);
		mEditText_EditText.setTextColor(mEditText_EditText.getResources().getColor(R.drawable.EditTextBlueColor));
	}

	//红色字体颜色
	private void EditTextRedLayout() {
		SING=RED;
		mEditTextRedImage.setBackgroundResource(R.drawable.tick_red);
		mEditTextLightGrayImage.setBackgroundResource(R.drawable.round_light_gray);
		mEditTextDarkGrayImage.setBackgroundResource(R.drawable.round_dark_gray);
		mEditTextBlackImage.setBackgroundResource(R.drawable.round_black);
		mEditTextBlueImage.setBackgroundResource(R.drawable.round_blue);
		mEditTextGreenImage.setBackgroundResource(R.drawable.round_green);
		mEditText_EditText.setTextColor(mEditText_EditText.getResources().getColor(R.drawable.EditTextRedColor));
	}

	//黑色字体颜色
	private void EditTextBlackLayout() {
		SING=BLACK;
		mEditTextBlackImage.setBackgroundResource(R.drawable.tick_black);
		mEditTextLightGrayImage.setBackgroundResource(R.drawable.round_light_gray);
		mEditTextDarkGrayImage.setBackgroundResource(R.drawable.round_dark_gray);
		mEditTextRedImage.setBackgroundResource(R.drawable.round_red);
		mEditTextBlueImage.setBackgroundResource(R.drawable.round_blue);
		mEditTextGreenImage.setBackgroundResource(R.drawable.round_green);
		mEditText_EditText.setTextColor(mEditText_EditText.getResources().getColor(R.drawable.EditTextBlackColor));
	}

	//深灰色字体颜色
	private void EditTextDarkGrayLayout() {
		SING=DARKGRAY;
		mEditTextDarkGrayImage.setBackgroundResource(R.drawable.tick_dark_gray);
		mEditTextLightGrayImage.setBackgroundResource(R.drawable.round_light_gray);
		mEditTextBlackImage.setBackgroundResource(R.drawable.round_black);
		mEditTextRedImage.setBackgroundResource(R.drawable.round_red);
		mEditTextBlueImage.setBackgroundResource(R.drawable.round_blue);
		mEditTextGreenImage.setBackgroundResource(R.drawable.round_green);
		mEditText_EditText.setTextColor(mEditText_EditText.getResources().getColor(R.drawable.EditTextDarkGrayColor));
	}

	//设置浅灰色字体颜色
	private void EditTextLightGrayLayout() {
		SING=LIGHTGRAY;
		mEditTextLightGrayImage.setBackgroundResource(R.drawable.tick_light_gray);
		mEditTextDarkGrayImage.setBackgroundResource(R.drawable.round_dark_gray);
		mEditTextBlackImage.setBackgroundResource(R.drawable.round_black);
		mEditTextRedImage.setBackgroundResource(R.drawable.round_red);
		mEditTextBlueImage.setBackgroundResource(R.drawable.round_blue);
		mEditTextGreenImage.setBackgroundResource(R.drawable.round_green);
		mEditText_EditText.setTextColor(mEditText_EditText.getResources().getColor(R.drawable.EditTextLightGrayColor));
	}

	private void GraphicDetailsBack() {
		GraphicDetailsPreview1();//进行保存
		finish();
	}

	//保存
	private void GraphicDetailsOK() {
		isEdit=!isEdit;
		String mEditText = mEditText_EditText.getText().toString().trim();
		ImageText imageText=new ImageText();
		imageText.setType(1);
		imageText.setText(mEditText);
		imageText.setFont_color(String.valueOf(SING));
		DetailsList.add(imageText);
		mEditTextEditLayout.setVisibility(View.GONE);
		mGraphicDetailsPreviewLayout.setVisibility(View.VISIBLE);
		mGraphicDetailsOKLayout.setVisibility(View.GONE);
		InputMethodManager();//隐藏键盘
		myGraphicDetailsAdapter.notifyDataSetChanged();
		Log.e("GraphicDetailsActivity", "点击了保存========");
	}

	//编辑图片
	private void EditImageLayout() {
		// 使用intent调用系统提供的相册功能，使用startActivityForResult是为了获取用户选择的图片
		Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
		getAlbum.setType(IMAGE_TYPE);
		startActivityForResult(getAlbum, IMAGE_CODE);
		Log.e("GraphicDetailsActivity", "点击了编辑图片========");
	}

	//编辑文字
	private void EditImageAndTextLayout() {
		isEdit=!isEdit;
		if(isEdit){
			mEditTextEditLayout.setVisibility(View.VISIBLE);
			mGraphicDetailsPreviewLayout.setVisibility(View.GONE);
			mGraphicDetailsOKLayout.setVisibility(View.VISIBLE);
			mEditText_EditText.setFocusable(true);
			mEditText_EditText.setFocusableInTouchMode(true);
			mEditText_EditText.requestFocus();
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				public void run() {
					InputMethodManager inputManager = (InputMethodManager) mEditText_EditText
							.getContext().getSystemService(GraphicDetailsActivity.INPUT_METHOD_SERVICE);
					inputManager.showSoftInput(mEditText_EditText, 0);
				}
			}, 10);
			mEditText_EditText.setText("");
			EditTextDarkGrayLayout();//每次打开时初始化字体颜色
			Log.e("GraphicDetailsActivity", "点击了编辑文字========");
		}else {
			//做保存
			String mEditText = mEditText_EditText.getText().toString().trim();
			ImageText imageText=new ImageText();
			imageText.setType(1);
			imageText.setText(mEditText);
			imageText.setFont_color(String.valueOf(SING));
			DetailsList.add(imageText);
			mEditTextEditLayout.setVisibility(View.GONE);
			mGraphicDetailsPreviewLayout.setVisibility(View.VISIBLE);
			mGraphicDetailsOKLayout.setVisibility(View.GONE);
			InputMethodManager();//隐藏键盘
			myGraphicDetailsAdapter.notifyDataSetChanged();
			Log.e("GraphicDetailsActivity", "点击了编辑文字的保存========");
		}
	}

	//隐藏键盘
	private void InputMethodManager() {
		InputMethodManager imm = (InputMethodManager) mEditText_EditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mEditText_EditText.getWindowToken(), 0);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK || data == null)
			return;
		try {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = GraphicDetailsActivity.this.getContentResolver().query(
					selectedImage, filePathColumn, null, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				mFilePath = cursor.getString(columnIndex);
				cursor.close();
				cursor = null;

			} else {
				File file = new File(selectedImage.getPath());
				mFilePath = file.getAbsolutePath();
				if (!file.exists()) {
					Toast toast = Toast.makeText(this, "找不到图片",
							Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					return;
				}
			}
			ImageText imageText=new ImageText();
			imageText.setType(2);
			imageText.setImage_path(mFilePath);
			DetailsList.add(imageText);
			InputMethodManager();//隐藏键盘
			myGraphicDetailsAdapter.notifyDataSetChanged();
		} catch (Exception e) {
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			GraphicDetailsBack();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
