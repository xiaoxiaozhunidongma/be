package com.biju.pay;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.TextView;

import com.BJ.javabean.ImageText;
import com.BJ.javabean.ImageTextBack;
import com.BJ.javabean.Party;
import com.BJ.utils.PreferenceUtils;
import com.BJ.utils.homeImageLoaderUtils;
import com.biju.Interface;
import com.biju.Interface.ReadGraphicListenner;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;

public class GraphicPreviewActivity extends Activity implements OnClickListener{

	private Interface mGraphicInterface;
	private String pk_party;
	private ListView mGraphicPreviewListView;
	private List<ImageText> GraphicPreviewList=new ArrayList<ImageText>();
	private MyGraphicPreviewAdapter adapter;
	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "@!";
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
		setContentView(R.layout.activity_graphic_preview);
		Intent intent = getIntent();
		pk_party = intent.getStringExtra("Pk_party");
		initUI();
		initInterface();
		initGraphic();
		
	}


	private void initUI() {
		findViewById(R.id.GraphicPreviewBack).setOnClickListener(this);
		mGraphicPreviewListView = (ListView) findViewById(R.id.GraphicPreviewListView);
		adapter = new MyGraphicPreviewAdapter();
		mGraphicPreviewListView.setAdapter(adapter);
	}

	class ViewHolder{
		ImageView GraphicPreviewImageView;
		TextView GraphicPreviewText;
	}
	
	class MyGraphicPreviewAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return GraphicPreviewList.size();
		}

		@Override
		public Object getItem(int arg0) {
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
				inflater = layoutInflater.inflate(R.layout.graphicpreview_item, null);
				holder.GraphicPreviewImageView=(ImageView) inflater.findViewById(R.id.GraphicPreviewImageView);
				holder.GraphicPreviewText=(TextView) inflater.findViewById(R.id.GraphicPreviewText);
				inflater.setTag(holder);
			}else {
				inflater=convertView;
				holder=(ViewHolder) inflater.getTag();
			}
			
			ImageText imageText = GraphicPreviewList.get(position);
			Integer type=imageText.getType();
			if(1==type){
				holder.GraphicPreviewText.setVisibility(View.VISIBLE);
				holder.GraphicPreviewImageView.setVisibility(View.GONE);
				String SINGCOLOR=imageText.getFont_color();
				if(LIGHTGRAYCOLOR.equals(SINGCOLOR)){
					holder.GraphicPreviewText.setTextColor(holder.GraphicPreviewText.getResources().getColor(R.drawable.EditTextLightGrayColor));
				}else if(DARKGRAYCOLOR.equals(SINGCOLOR)){
					holder.GraphicPreviewText.setTextColor(holder.GraphicPreviewText.getResources().getColor(R.drawable.EditTextDarkGrayColor));
				}else if(BLACKCOLOR.equals(SINGCOLOR)){
					holder.GraphicPreviewText.setTextColor(holder.GraphicPreviewText.getResources().getColor(R.drawable.EditTextBlackColor));
				}else if(REDCOLOR.equals(SINGCOLOR)){
					holder.GraphicPreviewText.setTextColor(holder.GraphicPreviewText.getResources().getColor(R.drawable.EditTextRedColor));
				}else if(BLUECOLOR.equals(SINGCOLOR)){
					holder.GraphicPreviewText.setTextColor(holder.GraphicPreviewText.getResources().getColor(R.drawable.EditTextBlueColor));
				}else if(GREENCOLOR.equals(SINGCOLOR)){
					holder.GraphicPreviewText.setTextColor(holder.GraphicPreviewText.getResources().getColor(R.drawable.EditTextGreenColor));
				}
				holder.GraphicPreviewText.setText(imageText.getText()+"");
			}else if(2==type){
				holder.GraphicPreviewText.setVisibility(View.GONE);
				holder.GraphicPreviewImageView.setVisibility(View.VISIBLE);
				String mPath=imageText.getImage_path();
				Log.e("GraphicPreviewActivity", "获取到的图片路径======"+mPath);
				String completeURL = beginStr + mPath + endStr+ "group-front-cover";
				PreferenceUtils.saveImageCache(GraphicPreviewActivity.this,completeURL);
				homeImageLoaderUtils.getInstance().LoadImage(GraphicPreviewActivity.this, completeURL, holder.GraphicPreviewImageView);
				
			}
			
			return inflater;
		}
		
	}

	private void initGraphic() {
		Party party=new Party();
		party.setPk_party(pk_party);
		mGraphicInterface.ReadGraphic(GraphicPreviewActivity.this, party);
	}


	private void initInterface() {
		mGraphicInterface = Interface.getInstance();
		mGraphicInterface.setPostListener(new ReadGraphicListenner() {
			
			@Override
			public void success(String A) {
				Log.e("GraphicPreviewActivity", "获取回来的图文信息========="+A);
				ImageTextBack imageTextBack=GsonUtils.parseJson(A, ImageTextBack.class);
				Integer StatusMsg=imageTextBack.getStatusMsg();
				if(1==StatusMsg){
					List<ImageText> imageTextslist=imageTextBack.getReturnData();
					if(imageTextslist.size()>0){
						for (int i = 0; i < imageTextslist.size(); i++) {
							ImageText imageText=imageTextslist.get(i);
							GraphicPreviewList.add(imageText);
						}
						adapter.notifyDataSetChanged();
					}
				}
			}
			
			@Override
			public void defail(Object B) {
				
			}
		});
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.GraphicPreviewBack:
			GraphicPreviewBack();
			break;

		default:
			break;
		}
	}


	private void GraphicPreviewBack() {
		finish();
		overridePendingTransition(R.anim.left, R.anim.right);
	}

}
