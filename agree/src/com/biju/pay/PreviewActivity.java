package com.biju.pay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.BJ.javabean.ImageText;
import com.BJ.utils.LimitLong;
import com.BJ.utils.Path2Bitmap;
import com.activeandroid.query.Select;
import com.biju.R;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PreviewActivity extends Activity implements OnClickListener{

	private ListView mPreviewListView;
	private List<ImageText> PreviewList=new ArrayList<ImageText>();
	private MyPreviewAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview);
		initUI();
		initDB();
	}

	//查表
	private void initDB() {
		PreviewList.clear();
		PreviewList = new Select().from(ImageText.class).execute();
		Log.e("PreviewActivity", "PreviewList的长度======="+PreviewList.size());
		if(PreviewList.size()>0){
			mPreviewListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
	}

	private void initUI() {
		findViewById(R.id.PreviewBackLayout).setOnClickListener(this);
		findViewById(R.id.PreviewBack).setOnClickListener(this);//返回
		findViewById(R.id.PreviewOKLayout).setOnClickListener(this);
		findViewById(R.id.PreviewOK).setOnClickListener(this);//完成
		mPreviewListView = (ListView) findViewById(R.id.PreviewListView);
		mPreviewListView.setDividerHeight(0);
		adapter = new MyPreviewAdapter();
	}

	class ViewHolder{
		RelativeLayout PreviewImageLayout;
		RelativeLayout PreviewTextLayout;
		ImageView PreviewImage;
		TextView PreviewTextShow;
	}
	
	class MyPreviewAdapter extends BaseAdapter{

		private ImageText imageText;

		@Override
		public int getCount() {
			return PreviewList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View inflater = null;
			ViewHolder holder=null;
			if(convertView==null){
				holder=new ViewHolder();
				LayoutInflater layoutInflater = getLayoutInflater();
				inflater = layoutInflater.inflate(R.layout.preview_itrem, null);
				holder.PreviewImageLayout=(RelativeLayout) inflater.findViewById(R.id.PreviewImageLayout);
				holder.PreviewTextLayout=(RelativeLayout) inflater.findViewById(R.id.PreviewTextLayout);
				holder.PreviewImage=(ImageView) inflater.findViewById(R.id.PreviewImage);
				holder.PreviewTextShow=(TextView) inflater.findViewById(R.id.PreviewTextShow);
				inflater.setTag(holder);
			}else {
				inflater=convertView;
				holder=(ViewHolder) inflater.getTag();
			}
			
			imageText = PreviewList.get(position);
			Integer type=imageText.getType();
			if(1==type){
				holder.PreviewTextLayout.setVisibility(View.VISIBLE);
				holder.PreviewImageLayout.setVisibility(View.GONE);
				Integer sing=Integer.valueOf(imageText.getFont_color());
				switch (sing) {
				case 1:
					holder.PreviewTextShow.setTextColor(holder.PreviewTextShow.getResources().getColor(R.drawable.EditTextLightGrayColor));
					break;
				case 2:
					holder.PreviewTextShow.setTextColor(holder.PreviewTextShow.getResources().getColor(R.drawable.EditTextDarkGrayColor));
					break;
				case 3:
					holder.PreviewTextShow.setTextColor(holder.PreviewTextShow.getResources().getColor(R.drawable.EditTextBlackColor));
					break;
				case 4:
					holder.PreviewTextShow.setTextColor(holder.PreviewTextShow.getResources().getColor(R.drawable.EditTextRedColor));
					break;
				case 5:
					holder.PreviewTextShow.setTextColor(holder.PreviewTextShow.getResources().getColor(R.drawable.EditTextBlueColor));
					break;
				case 6:
					holder.PreviewTextShow.setTextColor(holder.PreviewTextShow.getResources().getColor(R.drawable.EditTextGreenColor));
					break;

				default:
					break;
				}
				holder.PreviewTextShow.setText(imageText.getText()+"");
			}else if(2==type){
				holder.PreviewTextLayout.setVisibility(View.GONE);
				holder.PreviewImageLayout.setVisibility(View.VISIBLE);
				String mPath=imageText.getImage_path();
				Bitmap convertToBitmap = null;
				try {
					convertToBitmap = Path2Bitmap.convertToBitmap(mPath);
					Log.e("GraphicDetailsActivity", "获取的原来图片的宽和高======"+convertToBitmap.getWidth()+"        "+convertToBitmap.getHeight());
					Bitmap limitLongScaleBitmap = LimitLong.limitLongScaleBitmap(convertToBitmap, 1280);
					holder.PreviewImage.setImageBitmap(limitLongScaleBitmap);
					Log.e("GraphicDetailsActivity", "获取的截取后图片的宽和高======"+limitLongScaleBitmap.getWidth()+"          "+limitLongScaleBitmap.getHeight());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			return inflater;
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.preview, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.PreviewBack:
		case R.id.PreviewBackLayout:
			PreviewBack();
			break;

		default:
			break;
		}
	}

	private void PreviewBack() {
		finish();
	}

}
