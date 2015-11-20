package com.biju.function;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;

import com.BJ.javabean.Party2;
import com.BJ.javabean.UserAllParty;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviPara;
import com.biju.R;

public class BigMapActivity extends Activity implements android.view.View.OnClickListener {

	private MapView mMapView;
	private BaiduMap mBaiduMap;
	public MyLocationListenner myListener = new MyLocationListenner();
	private BDLocation mLocation;
	boolean isFirstLoc = true;// 是否首次定位
	private double mLat = 24.497572;
	private double mLng = 118.17276;
	private double mlocLat ;
	private double mlocLng ;
	private float scale = 15.0f;
	private LocationClient mLocClient;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private ArrayList<BitmapDescriptor> mOverLayList = new ArrayList<BitmapDescriptor>();
	private Marker mMarkerD;

	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			mLocation = location;
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				// 设置定位的坐标
				isFirstLoc = false;
				// LatLng ll = new LatLng(location.getLatitude(),
//				 location.getLongitude());
				mlocLat=location.getLatitude();
				mlocLng=location.getLongitude();
				LatLng ll = new LatLng(mLat, mLng);
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				// 设置动画
				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_big_map);
		
		Intent intent = getIntent();
		boolean isWay=intent.getBooleanExtra("BigMap", false);
		Log.e("BigMapActivity", "这时候的isWay=========="+isWay);
		if(isWay){
			UserAllParty allParty=(UserAllParty) intent.getSerializableExtra("AllBigMap");
			mLat=allParty.getLatitude();
			mLng=allParty.getLongitude();
		}else {
			Party2 party2=(Party2) intent.getSerializableExtra("OneBigMap");
			mLat=party2.getLatitude();
			mLng=party2.getLongitude();
		}
				
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();

		mMapView.showZoomControls(false);
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(scale);
		mBaiduMap.setMapStatus(msu);

		initMap();
		initUI();
	}

	private void initUI() {
		findViewById(R.id.Bigmap__navi).setOnClickListener(this);
		findViewById(R.id.Bigmap__navi_layout).setOnClickListener(this);
		findViewById(R.id.Bigmap_navigation_layout).setOnClickListener(this);
		findViewById(R.id.Bigmap_navigation).setOnClickListener(this);
		findViewById(R.id.Bigmap_back_layout).setOnClickListener(this);
		findViewById(R.id.Bigmap_back).setOnClickListener(this);
	}

	private void initMap() {
		// 添加地图标点
		addOverlay(mLat, mLng, R.drawable.iconfont2);
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);//是否显示当前位置默认图标
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		option.setLocationMode(tempMode);// 设置定位模式
		mLocClient.start();
	}

	private void addOverlay(double lat, double lng, final int drawableRes) {
		LatLng llA = new LatLng(lat, lng);

		// 设置悬浮的图案
		BitmapDescriptor bdA = BitmapDescriptorFactory
				.fromResource(drawableRes);
		mOverLayList.add(bdA);

		OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
				.zIndex(0).draggable(true);
		mMarkerD = (Marker) mBaiduMap.addOverlay(ooA);
	}

	/**
	 * 开始导航
	 * 
	 * @param view
	 */
	public void startNavi(View view) {
		LatLng pt1 = new LatLng(mLat, mLng);
		LatLng pt2 = new LatLng(mlocLat, mlocLng);
		// 构建 导航参数
		NaviPara para = new NaviPara();
		para.startPoint = pt1;
		para.startName = "从这里开始";
		para.endPoint = pt2;
		para.endName = "到这里结束";

		try {

			BaiduMapNavigation.openBaiduMapNavi(para, this);

		} catch (BaiduMapAppNotSupportNaviException e) {
			e.printStackTrace();
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
			builder.setTitle("提示");
			builder.setPositiveButton("确认", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					BaiduMapNavigation.getLatestBaiduMapApp(BigMapActivity.this);
				}
			});

			builder.setNegativeButton("取消", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			builder.create().show();
		}
	}

	public void startWebNavi(View view) {
		LatLng pt1 = new LatLng(mLat, mLng);
		LatLng pt2 = new LatLng(mlocLat, mlocLng);
		// 构建 导航参数
		NaviPara para = new NaviPara();
		para.startPoint = pt1;
		para.endPoint = pt2;
		BaiduMapNavigation.openWebBaiduMapNavi(para, this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Bigmap__navi:
		case R.id.Bigmap__navi_layout:
		case R.id.Bigmap_navigation:
		case R.id.Bigmap_navigation_layout:
			startNavi(v);
			break;
		case R.id.Bigmap_back_layout:
		case R.id.Bigmap_back:
			Bigmap_back();
			break;
		default:
			break;
		}
	}

	private void Bigmap_back() {
		finish();
	}

}
