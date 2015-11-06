package com.example.imageselected.photo.adapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.biju.R;
import com.example.imageselected.photo.ImageLoader;
import com.example.imageselected.photo.ImageLoader.Type;

public class GirdItemAdapter extends BaseAdapter{
	final int VIEW_TYPE = 2;
	final int TYPE_1 = 0;
	final int TYPE_2 = 1;

	/**
	 * �û�ѡ���ͼƬ���洢ΪͼƬ������·��
	 */
	public static ArrayList<String> mSelectedImage = new ArrayList<String>();
	

	/**
	 * �ļ���·��
	 */
	private String mDirPath;

	private Context context;

	private List<String> mDatas=new ArrayList<String>();//���е�ͼƬ
	private int selectType;


	public GirdItemAdapter(Context context, List<String> mDatas,String dirPath, int selectType) {
		super();
		this.context = context;
		this.mDatas = mDatas;
		this.mDirPath=dirPath;
		this.selectType = selectType;
	}

	public void changeData(List<String> mDatas,String dirPath){
		this.mDatas = mDatas;
		this.mDirPath=dirPath;
		notifyDataSetChanged();

	}

	@Override
	public int getCount() {
//		return mDatas.size()+1;
		return mDatas.size();
	}

	@Override
	public String getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public int getItemViewType(int position) {
//		if(position==0){
//			return TYPE_1;
//		}else{
//			return TYPE_2;
//		}
		return TYPE_2;
	}
	@Override
	public int getViewTypeCount() {
		return VIEW_TYPE;
	}
	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		ViewHolder2 holder2 = null;
		int type = getItemViewType(position);
		if(convertView==null){
			switch (type) {
			case TYPE_1:
				convertView=LayoutInflater.from(context).inflate(R.layout.grid_item2, null);
				holder2=new ViewHolder2();
				holder2.id_item_image2=(LinearLayout)convertView.findViewById(R.id.id_item_image2);
				convertView.setTag(holder2);
				break;
			case TYPE_2:
				convertView=LayoutInflater.from(context).inflate(R.layout.grid_item, null);
				holder=new ViewHolder();
				holder.id_item_image=(ImageView)convertView.findViewById(R.id.id_item_image);
				holder.id_item_select=(ImageButton)convertView.findViewById(R.id.id_item_select);
				convertView.setTag(holder);
				break;
			}
		}else{
			switch (type) {
			case TYPE_1:
				holder2=(ViewHolder2) convertView.getTag();
				break;
			case TYPE_2:
				holder=(ViewHolder) convertView.getTag();
				break;

			}
		}
		switch (type) {
		case TYPE_1:
			holder2.id_item_image2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onPhotoSelectedListener.takePhoto();
				}
			});
			break;
		case TYPE_2:
			holder.id_item_select.setBackgroundResource(R.drawable.picture_unselected);
			holder.id_item_image.setBackgroundResource(R.drawable.pictures_no);
			ImageLoader.getInstance(3,Type.LIFO).loadImage(mDirPath + "/" + mDatas.get(position), holder.id_item_image);
			holder.id_item_image.setColorFilter(null);
			//����ImageView�ĵ���¼�
			holder.id_item_image.setOnClickListener(new MyOnClickListener(holder, position));
			/**
			 * �Ѿ�ѡ�����ͼƬ����ʾ��ѡ�����Ч��
			 */
			if (mSelectedImage.contains(mDirPath + "/" + mDatas.get(position))){
				holder.id_item_select.setImageResource(R.drawable.pictures_selected);
				holder.id_item_image.setColorFilter(Color.parseColor("#77000000"));
			}else{
				holder.id_item_select.setImageResource(R.drawable.picture_unselected);
				holder.id_item_image.setColorFilter(Color.parseColor("#00000000"));
			}
			break;

		default:
			break;
		}
		return convertView;
	}
	class MyOnClickListener implements OnClickListener{
		ViewHolder holder;
		int position;
		MyOnClickListener(ViewHolder holder,int  position){
			this.holder=holder;
			this.position=position;
		}
		@Override
		public void onClick(View v) {
			// �Ѿ�ѡ�����ͼƬ
			if (mSelectedImage.contains(mDirPath + "/" + mDatas.get(position))){
				mSelectedImage.remove(mDirPath + "/" + mDatas.get(position));
				holder.id_item_select.setImageResource(R.drawable.picture_unselected);
				holder.id_item_image.setColorFilter(null);
			} else{// δѡ���ͼƬ
				switch (selectType) {
				case 0:
					if(mSelectedImage.size()>=1){
						Toast.makeText(context, "�����ѡһ��", Toast.LENGTH_SHORT).show();
						return ;
					}
					
					break;
				case 1:
					if(mSelectedImage.size()>=9){
						Toast.makeText(context, "�����ѡ����", Toast.LENGTH_SHORT).show();
						return ;
					}
					
					break;

				default:
					break;
				}
				mSelectedImage.add(mDirPath + "/" + mDatas.get(position));
				holder.id_item_select.setImageResource(R.drawable.pictures_selected);
				holder.id_item_image.setColorFilter(Color.parseColor("#77000000"));
			}
			onPhotoSelectedListener.photoClick(mSelectedImage);
		}
		
	}
	class ViewHolder{
		ImageView id_item_image;
		ImageButton id_item_select;
	}
	class ViewHolder2{
		LinearLayout id_item_image2;
	}
	public OnPhotoSelectedListener onPhotoSelectedListener;
	public void setOnPhotoSelectedListener(OnPhotoSelectedListener onPhotoSelectedListener){
		this.onPhotoSelectedListener=onPhotoSelectedListener;
	}
	public  interface OnPhotoSelectedListener{
		public void photoClick(List<String> number);
		public void takePhoto();
	}

}
