package com.biju.function;

import java.util.ArrayList;

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
import com.biju.R;
import com.biju.function.MapActivity.MyLocationListenner;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class PartyDetailsActivity extends Activity {
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	public MyLocationListenner myListener = new MyLocationListenner();
	private BDLocation mLocation;
	boolean isFirstLoc = true;// �Ƿ��״ζ�λ
	private double mLng;
	private double mLat;
	private float scale = 15.0f;
	private LocationClient mLocClient;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private ArrayList<BitmapDescriptor> mOverLayList = new ArrayList<BitmapDescriptor>();
	private Marker mMarkerD;
	
	/**
	 * ��λSDK��������
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			mLocation = location;
			// map view ���ٺ��ڴ����½��յ�λ��
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				// ���ö�λ������
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				// ���ö���
				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_party_details);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		//�Ƿ��������ſؼ�
		mMapView.showZoomControls(false);
		initMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(scale);
		mBaiduMap.setMapStatus(msu);
	}

	private void initMap() {
		// ��ӵ�ͼ���
		addOverlay(mLat, mLng, R.drawable.iconfont2);
		// ������λͼ��
		mBaiduMap.setMyLocationEnabled(true);
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		option.setLocationMode(tempMode);// ���ö�λģʽ
		mLocClient.start();
	}
	
	private void addOverlay(double lat, double lng, final int drawableRes) {
		LatLng llA = new LatLng(lat, lng);
				
		//����������ͼ��
		BitmapDescriptor bdA = BitmapDescriptorFactory
						.fromResource(drawableRes);
		mOverLayList.add(bdA);
				
		OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
						.zIndex(0).draggable(true);
		mMarkerD = (Marker)mBaiduMap.addOverlay(ooA);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.party_details, menu);
		return true;
	}

}
