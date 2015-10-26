package com.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.BJ.javabean.Group;
import com.BJ.javabean.Party2;
import com.BJ.javabean.Party4;
import com.BJ.javabean.PartyAllback;
import com.BJ.javabean.Photo;
import com.BJ.utils.ImageLoaderUtils4Photos;
import com.BJ.utils.MyGridView;
import com.biju.Interface;
import com.biju.PhotoActivity;
import com.biju.R;
import com.biju.Interface.ReadGroupPartyAlllistenner;
import com.biju.R.layout;
import com.biju.function.GroupActivity;
import com.github.volley_examples.utils.GsonUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class PhotosPreviewFragment extends Fragment implements OnItemClickListener {

	private View mlayout;
	private ListView listpreview;
	private List<Party4> returnData=new ArrayList<Party4>();
	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "@!";
	private MyAdapter adapter;
	ArrayList<MyAdapter2> MyAdapterList=new ArrayList<PhotosPreviewFragment.MyAdapter2>();
	HashMap<Integer, ListAdapter> MyAdapter2Map=new HashMap<Integer, ListAdapter>();

	public PhotosPreviewFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(mlayout==null){
			mlayout = inflater.inflate(R.layout.fragment_photos_preview, container,
					false);
			initUI();
			iniListener();
		}
		return mlayout;
	}

	private void iniListener() {
		Interface instance = Interface.getInstance();
		Group group=new Group();
		group.setPk_group(GroupActivity.getPk_group());
		instance.readUserGroupPartyAll(getActivity(), group);
		instance.setPostListener(new ReadGroupPartyAlllistenner() {
			

			@Override
			public void success(String A) {
				Log.e("PhotosPreviewFragment", "包含未过期的所有聚会=="+A);
				PartyAllback partyAllback = GsonUtils.parseJson(A, PartyAllback.class);
				returnData = partyAllback.getReturnData();
				Log.e("PhotosPreviewFragment", "returnData.size="+returnData.size());
				adapter.notifyDataSetChanged();
			}
			
			@Override
			public void defail(Object B) {
				
			}
		});
		
		listpreview.setOnItemClickListener(this);
	}

	private void initUI() {
		listpreview = (ListView) mlayout.findViewById(R.id.listpreview);
		adapter = new MyAdapter();
		listpreview.setAdapter(adapter);
		
	}
	
	class MyAdapter extends BaseAdapter{


		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return returnData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Party4 party4 = returnData.get(position);
			// TODO Auto-generated method stub
			View inflate = getActivity().getLayoutInflater().inflate(R.layout.preview_item,null);
			MyGridView gridView = (MyGridView) inflate.findViewById(R.id.gv_partypreview);
			 MyAdapter2 myAdapter2 = new MyAdapter2();
			gridView.setAdapter(myAdapter2);
			
			TextView tv_partyname = (TextView) inflate.findViewById(R.id.tv_partyname);
			TextView tv_partytime = (TextView) inflate.findViewById(R.id.tv_partytime);
			TextView tv_partyphotonum = (TextView) inflate.findViewById(R.id.tv_partyphotonum);
			
			tv_partyname.setText(party4.getName());
			tv_partytime.setText(party4.getBegin_time());
			tv_partyphotonum.setText(String.valueOf(party4.getPhotos().size()));
			
			List<Photo> photos = party4.getPhotos();
			myAdapter2.setPhotos(photos);
			myAdapter2.notifyDataSetChanged();
			
			
			return inflate;
		}
		
	}
	
//	/**
//	 * 计算gridview高度
//	 * @param gridView
//	 */
//	public static void setGridViewHeightBasedOnChildren(GridView gridView) {
//		// 获取GridView对应的Adapter
//		ListAdapter listAdapter = gridView.getAdapter();
//		if (listAdapter == null) {
//			return;
//		}
//		int rows;
//		int columns = 10;
//		int horizontalBorderHeight = 0;
//		Class<?> clazz = gridView.getClass();
//		try {
//			// 利用反射，取得每行显示的个数
//			Field column = clazz.getDeclaredField("mRequestedNumColumns");
//			column.setAccessible(true);
//			columns = (Integer) column.get(gridView);
//			// 利用反射，取得横向分割线高度
//			Field horizontalSpacing = clazz
//					.getDeclaredField("mRequestedHorizontalSpacing");
//			horizontalSpacing.setAccessible(true);
//			horizontalBorderHeight = (Integer) horizontalSpacing.get(gridView);
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		// 判断数据总数除以每行个数是否整除。不能整除代表有多余，需要加一行
//		if (listAdapter.getCount() % columns > 0) {
//			rows = listAdapter.getCount() / columns + 1;
//		} else {
//			rows = listAdapter.getCount() / columns;
//		}
//		//行数最大为3行！
//		if(rows>=3){
//			rows=3;
//		}
//		int totalHeight = 0;
//		for (int i = 0; i < rows; i++) { // 只计算每项高度*行数
//			View listItem = listAdapter.getView(i, null, gridView);
//			listItem.measure(0, 0); // 计算子项View 的宽高
//			totalHeight += 90; // 统计所有子项的总高度
//		}
//		ViewGroup.LayoutParams params = gridView.getLayoutParams();
//		params.height = totalHeight + horizontalBorderHeight * (rows - 1);// 最后加上分割线总高度
//		Log.e("rows==="+rows, "rows==="+rows);
//		Log.e("totalHeight==="+totalHeight, "horizontalBorderHeight * (rows - 1)==="+horizontalBorderHeight * (rows - 1));
//		gridView.setLayoutParams(params);
//	}
	
	class MyAdapter2 extends BaseAdapter{
		List<Photo> photos=new ArrayList<Photo>();
		
		public List<Photo> getPhotos() {
			return photos;
		}

		public void setPhotos(List<Photo> photos) {
			this.photos = photos;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return photos.size();
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View inflate = getActivity().getLayoutInflater().inflate(R.layout.item_smallpicture,null);
				Photo photo = photos.get(position);
			String pk_photo = photo.getPk_photo();
			String completeUrl=beginStr+pk_photo+endStr+"mini-avatar";
			// TODO Auto-generated method stub
			ImageView imageView = (ImageView) inflate.findViewById(R.id.imageView1);
			
			ImageLoaderUtils4Photos.getInstance().LoadImage(getActivity(), completeUrl, imageView);
			
			
			return inflate;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Party4 party4 = returnData.get(position);
		String pk_party = party4.getPk_party();
		ArrayList<Photo> photos = (ArrayList<Photo>) party4.getPhotos();
		
		Log.e("", "点击了跳转PhotoActivity");
		Intent intent=new Intent(getActivity(), PhotoActivity.class);
		intent.putExtra("pk_party", pk_party);
		intent.putExtra("photos", photos);
		startActivity(intent);
	}

}
