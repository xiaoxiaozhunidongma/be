package com.biju.function;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapDoubleClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMapTouchListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.biju.R;
import com.github.volley_examples.utils.NotifiUtils;

public class MapActivity extends Activity implements
		OnGetGeoCoderResultListener, OnClickListener {

	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private LatLng currentPt;
	private String touchType;
//	public MyLocationListenner myListener = new MyLocationListenner();
	private BDLocation mLocation;
	boolean isFirstLoc = true;// 是否首次定位
	private double mLng;
	private double mLat;
	private float scale = 15.0f;
	private LocationClient mLocClient;
	private ArrayList<BitmapDescriptor> mOverLayList = new ArrayList<BitmapDescriptor>();
	private GeoCoder mSearch;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private float y;
	private float x;

	/**
	 * 定位SDK监听函数
	 */
	/*public class MyLocationListenner implements BDLocationListener {

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
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				// 设置动画
				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 地图初始化
		/*
		 * mMapView = (MapView) findViewById(R.id.bmapView); mBaiduMap =
		 * mMapView.getMap(); initMap(); MapStatusUpdate msu =
		 * MapStatusUpdateFactory.zoomTo(scale); mBaiduMap.setMapStatus(msu);
		 * initListener(); // 初始化搜索模块，注册事件监听 mSearch = GeoCoder.newInstance();
		 * mSearch.setOnGetGeoCodeResultListener(this);
		 */
		initUI();
	}

	private void initUI() {
		findViewById(R.id.map_back_layout).setOnClickListener(this);// 返回
		findViewById(R.id.map_back).setOnClickListener(this);
		findViewById(R.id.map_next_layout).setOnClickListener(this);// 下一步
		findViewById(R.id.map_next).setOnClickListener(this);
	}

	/*private void initListener() {

		mBaiduMap.setOnMapTouchListener(new OnMapTouchListener() {

			@Override
			public void onTouch(MotionEvent event) {
				y = event.getY();
				x = event.getX();
			}
		});

		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

			public void onMapClick(LatLng point) {
				touchType = "单击";
				currentPt = point;
				updateMapState();
			}

			public boolean onMapPoiClick(MapPoi poi) {
				return false;
			}
		});
		mBaiduMap.setOnMapLongClickListener(new OnMapLongClickListener() {
			public void onMapLongClick(LatLng point) {
				currentPt = point;
				updateMapState();
			}
		});
		mBaiduMap.setOnMapDoubleClickListener(new OnMapDoubleClickListener() {
			public void onMapDoubleClick(LatLng point) {
				touchType = "双击";
				currentPt = point;
				updateMapState();

			}
		});
		mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {
			public void onMapStatusChangeStart(MapStatus status) {
				// updateMapState();
			}

			public void onMapStatusChangeFinish(MapStatus status) {
				// updateMapState();
			}

			public void onMapStatusChange(MapStatus status) {
				// updateMapState();
			}
		});

	}*/

	/*private void updateMapState() {

		String state = "";
		if (currentPt == null) {
			state = "点击、长按、双击地图以获取经纬度和地图状态";
		} else {
			state = String.format("当前经度： %f 当前纬度：%f", currentPt.longitude,
					currentPt.latitude);
		}
		state += "\n";
		// MapStatus ms = mBaiduMap.getMapStatus();
		// NotifiUtils.showToast(MainActivity.this, state);
		if (currentPt != null) {
			mLng = currentPt.longitude;
			mLat = currentPt.latitude;

			LatLng ptCenter = new LatLng(currentPt.latitude,
					currentPt.longitude);
			// 反Geo搜索
			mSearch.reverseGeoCode(new ReverseGeoCodeOption()
					.location(ptCenter));

			initMap();
		}
	}

	private void initMap() {

		// // 设置比例尺，15.0f(500米)，14.0f(1公里)，10.0f(20公里)
		// 这个设置了会出错
		// MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(scale);
		// mBaiduMap.setMapStatus(msu);
		// 添加店铺坐标
		addOverlay(mLat, mLng, R.drawable.shop_icon);

		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
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

	private void addOverlay(double lat, double lng, int drawableRes) {
		LatLng llA = new LatLng(lat, lng);
		// 设置悬浮的图案
		BitmapDescriptor bdA = BitmapDescriptorFactory
				.fromResource(drawableRes);
		mOverLayList.add(bdA);

		OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
				.zIndex(9).draggable(true);
		// 添加之前先清空
		mBaiduMap.clear();
		// 在地图中添加悬浮图案
		mBaiduMap.addOverlay(ooA);
		// 设置悬浮物点击监听
		// mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
		// public boolean onMarkerClick(final Marker marker) {
		// Button button = new Button(getApplicationContext());
		// button.setBackgroundResource(R.drawable.popup);
		// button.setText("更改图标");
		// button.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// marker.setIcon(bd);
		// mBaiduMap.hideInfoWindow();
		// }
		// });
		// LatLng ll = marker.getPosition();
		// InfoWindow mInfoWindow = new InfoWindow(button, ll, -47);
		// mBaiduMap.showInfoWindow(mInfoWindow);
		// return true;
		// }
		// });
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/*@Override
	protected void onPause() {
		// MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
		for (int i = 0; i < mOverLayList.size(); i++) {
			mOverLayList.get(i).recycle();
		}
		mSearch.destroy();
		super.onDestroy();
	}*/

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(MapActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		NotifiUtils.showToast(MapActivity.this, result.getAddress());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.map_back_layout:
		case R.id.map_back:
			map_back();
			break;
		case R.id.map_next_layout:
		case R.id.map_next:
			map_next();
			break;
		default:
			break;
		}
	}

	private void map_next() {
		Intent intent = new Intent(MapActivity.this, TimeActivity.class);
		startActivity(intent);
	}

	private void map_back() {
		finish();
	}

}
