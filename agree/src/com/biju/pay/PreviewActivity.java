package com.biju.pay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BJ.javabean.ImageText;
import com.BJ.utils.LimitLong;
import com.BJ.utils.Path2Bitmap;
import com.activeandroid.query.Select;
import com.biju.R;
import com.biju.function.AddNewPartyActivity;

public class PreviewActivity extends Activity implements OnClickListener {

	private ListView mPreviewListView;
	private List<ImageText> PreviewList = new ArrayList<ImageText>();
	private MyPreviewAdapter adapter;
	private String LIGHTGRAYCOLOR = "#C4C4C4";
	private String DARKGRAYCOLOR = "#43434C";
	private String BLACKCOLOR = "#040404";
	private String REDCOLOR = "#B6000A";
	private String BLUECOLOR = "#2B61D5";
	private String GREENCOLOR = "#6FCE1B";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview);
		initUI();
		initDB();
	}

	// 查表
	private void initDB() {
		PreviewList.clear();
		PreviewList = new Select().from(ImageText.class).execute();
		Log.e("PreviewActivity", "PreviewList的长度=======" + PreviewList.size());
		if (PreviewList.size() > 0) {
			mPreviewListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
	}

	private void initUI() {
		findViewById(R.id.PreviewBackLayout).setOnClickListener(this);
		findViewById(R.id.PreviewBack).setOnClickListener(this);// 返回
		findViewById(R.id.PreviewOK).setOnClickListener(this);// 完成
		mPreviewListView = (ListView) findViewById(R.id.PreviewListView);
		mPreviewListView.setDividerHeight(0);
		adapter = new MyPreviewAdapter();
	}

	class ViewHolder {
		RelativeLayout PreviewImageLayout;
		RelativeLayout PreviewTextLayout;
		ImageView PreviewImage;
		TextView PreviewTextShow;
	}

	class MyPreviewAdapter extends BaseAdapter {

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
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				LayoutInflater layoutInflater = getLayoutInflater();
				inflater = layoutInflater.inflate(R.layout.preview_itrem, null);
				holder.PreviewImageLayout = (RelativeLayout) inflater.findViewById(R.id.PreviewImageLayout);
				holder.PreviewTextLayout = (RelativeLayout) inflater.findViewById(R.id.PreviewTextLayout);
				holder.PreviewImage = (ImageView) inflater.findViewById(R.id.PreviewImage);
				holder.PreviewTextShow = (TextView) inflater.findViewById(R.id.PreviewTextShow);
				inflater.setTag(holder);
			} else {
				inflater = convertView;
				holder = (ViewHolder) inflater.getTag();
			}

			imageText = PreviewList.get(position);
			Integer type = imageText.getType();
			if (1 == type) {
				holder.PreviewTextLayout.setVisibility(View.VISIBLE);
				holder.PreviewImageLayout.setVisibility(View.GONE);
				String SINGCOLOR = imageText.getFont_color();
				if(LIGHTGRAYCOLOR.equals(SINGCOLOR)){
					holder.PreviewTextShow.setTextColor(holder.PreviewTextShow.getResources().getColor(R.drawable.EditTextLightGrayColor));
				}else if(DARKGRAYCOLOR.equals(SINGCOLOR)){
					holder.PreviewTextShow.setTextColor(holder.PreviewTextShow.getResources().getColor(R.drawable.EditTextDarkGrayColor));
				}else if(BLACKCOLOR.equals(SINGCOLOR)){
					holder.PreviewTextShow.setTextColor(holder.PreviewTextShow.getResources().getColor(R.drawable.EditTextBlackColor));
				}else if(REDCOLOR.equals(SINGCOLOR)){
					holder.PreviewTextShow.setTextColor(holder.PreviewTextShow.getResources().getColor(R.drawable.EditTextRedColor));
				}else if(BLUECOLOR.equals(SINGCOLOR)){
					holder.PreviewTextShow.setTextColor(holder.PreviewTextShow.getResources().getColor(R.drawable.EditTextBlueColor));
				}else if(GREENCOLOR.equals(SINGCOLOR)){
					holder.PreviewTextShow.setTextColor(holder.PreviewTextShow.getResources().getColor(R.drawable.EditTextGreenColor));
				}
				//字体大小
				Integer fontsize=imageText.getFont_size();
				holder.PreviewTextShow.setTextSize(fontsize);
				holder.PreviewTextShow.setText(imageText.getText() + "");
			} else if (2 == type) {
				holder.PreviewTextLayout.setVisibility(View.GONE);
				holder.PreviewImageLayout.setVisibility(View.VISIBLE);
				String mPath = imageText.getImage_path();
				Bitmap convertToBitmap = null;
				try {
					convertToBitmap = Path2Bitmap.convertToBitmap(mPath);
					Bitmap limitLongScaleBitmap = LimitLong.limitLongScaleBitmap(convertToBitmap, 1280);
					holder.PreviewImage.setImageBitmap(limitLongScaleBitmap);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			return inflater;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.PreviewBack:
		case R.id.PreviewBackLayout:
			PreviewBack();
			break;
		case R.id.PreviewOK:
			PreviewOK();
			break;
		default:
			break;
		}
	}

	private void PreviewOK() {
		SharedPreferences GraphicDetails_sp=getSharedPreferences("GraphicDetails", 0);
		Editor editor=GraphicDetails_sp.edit();
		editor.putBoolean("IsGraphicDetails", true);
		editor.putInt("GraphicDetailsNumber", PreviewList.size());
		editor.commit();
		finish();
		Intent intent=new Intent(PreviewActivity.this, AddNewPartyActivity.class);
		startActivity(intent);
	}

	private void PreviewBack() {
		finish();
	}

}
