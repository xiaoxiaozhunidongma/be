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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BJ.javabean.ImageText;
import com.BJ.javabean.ImageTextBack;
import com.BJ.javabean.Party;
import com.BJ.utils.GraphicImageLoaderUtils;
import com.BJ.utils.PreferenceUtils;
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
	private String endStr = "";
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
		Log.e("GraphicPreviewActivity", "得到的pk_party=========="+pk_party);
		initUI();
		initInterface();
		initGraphic();
	}

	private void initUI() {
		findViewById(R.id.GraphicPreviewBack).setOnClickListener(this);
		mGraphicPreviewListView = (ListView) findViewById(R.id.GraphicPreviewListView);
		mGraphicPreviewListView.setDividerHeight(0);
		adapter = new MyGraphicPreviewAdapter();
	}

	class ViewHolder{
		ImageView GraphicPreviewImageView;
		TextView GraphicPreviewText;
		RelativeLayout GraphicPreviewTextLayout;
		RelativeLayout GraphicPreviewImageViewLayout;
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
				holder.GraphicPreviewTextLayout=(RelativeLayout) inflater.findViewById(R.id.GraphicPreviewTextLayout);
				holder.GraphicPreviewImageViewLayout=(RelativeLayout) inflater.findViewById(R.id.GraphicPreviewImageViewLayout);
				inflater.setTag(holder);
			}else {
				inflater=convertView;
				holder=(ViewHolder) inflater.getTag();
			}
			
			ImageText imageText = GraphicPreviewList.get(position);
			Integer type=imageText.getType();
			if(1==type){
				holder.GraphicPreviewTextLayout.setVisibility(View.VISIBLE);
				holder.GraphicPreviewImageViewLayout.setVisibility(View.GONE);
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
				//字体大小
				Integer fontsize=imageText.getFont_size();
				holder.GraphicPreviewText.setTextSize(fontsize);
				holder.GraphicPreviewText.setText(imageText.getText()+"");
			}else if(2==type){
				holder.GraphicPreviewTextLayout.setVisibility(View.GONE);
				holder.GraphicPreviewImageViewLayout.setVisibility(View.VISIBLE);
				
//				int px2dip_left = DensityUtil.dip2px(GraphicPreviewActivity.this, 15);
//				int px2dip_right = DensityUtil.dip2px(GraphicPreviewActivity.this, 15);
//				int px2dip_top = DensityUtil.dip2px(GraphicPreviewActivity.this, 10);
//				int px2dip_bottom = DensityUtil.dip2px(GraphicPreviewActivity.this, 10);
//				Log.e("GraphicPreviewActivity", "px2dip_left======"+px2dip_left);
//				Log.e("GraphicPreviewActivity", " px2dip_right ======"+ px2dip_right );
//				int columnWidth=imageText.getImage_width();
//				int columnHeight=imageText.getImage_height();
//				int Width=DensityUtil.px2dip(GraphicPreviewActivity.this, columnWidth);
//				int height=DensityUtil.px2dip(GraphicPreviewActivity.this, columnHeight);
//				Log.e("GraphicPreviewActivity", " height ======"+ height );
//				Log.e("GraphicPreviewActivity", " columnHeight ======"+ columnHeight );
				// 设置图片的位置
//				MarginLayoutParams margin9 = new MarginLayoutParams(holder.GraphicPreviewImageView.getLayoutParams());
//				margin9.setMargins(px2dip_left, px2dip_top, px2dip_right,px2dip_bottom);
//				RelativeLayout.LayoutParams layoutParams9 = new RelativeLayout.LayoutParams(margin9);
//				layoutParams9.height = columnHeight;// 设置图片的高度
//				layoutParams9.width = columnWidth; // 设置图片的宽度
////				holder.GraphicPreviewImageView.setAdjustViewBounds(true);
//				holder.GraphicPreviewImageView.setLayoutParams(layoutParams9);
				
//				RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(Width,height);
////            	param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
//				param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE); 
//				param.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE); 
//            	param.setMargins(px2dip_left, px2dip_top, px2dip_right, px2dip_bottom);
//            	holder.GraphicPreviewImageViewLayout.setLayoutParams(param);
				
				String mPath=imageText.getImage_path();
				Log.e("GraphicPreviewActivity", "获取到的图片路径======"+mPath);
				String completeURL = beginStr + mPath + endStr;
				Log.e("GraphicPreviewActivity", "获取到的图片路径completeURL======"+completeURL);
				PreferenceUtils.saveImageCache(GraphicPreviewActivity.this, completeURL);// 存SP
				GraphicImageLoaderUtils.getInstance().LoadImage(GraphicPreviewActivity.this,completeURL, holder.GraphicPreviewImageView);
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
				GraphicPreviewList.clear();
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
						if(GraphicPreviewList.size()>0){
							mGraphicPreviewListView.setAdapter(adapter);
							adapter.notifyDataSetChanged();
						}
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
