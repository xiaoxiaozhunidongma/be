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
	boolean isFirstLoc = true;// �Ƿ��״ζ�λ
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
				// LatLng ll = new LatLng(location.getLatitude(),
//				 location.getLongitude());
				mlocLat=location.getLatitude();
				mlocLng=location.getLongitude();
				LatLng ll = new LatLng(mLat, mLng);
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_big_map);
		
		Intent intent = getIntent();
		boolean isWay=intent.getBooleanExtra("BigMap", false);
		Log.e("BigMapActivity", "��ʱ���isWay=========="+isWay);
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
		// ��ӵ�ͼ���
		addOverlay(mLat, mLng, R.drawable.iconfont2);
		// ������λͼ��
		mBaiduMap.setMyLocationEnabled(true);//�Ƿ���ʾ��ǰλ��Ĭ��ͼ��
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

		// ����������ͼ��
		BitmapDescriptor bdA = BitmapDescriptorFactory
				.fromResource(drawableRes);
		mOverLayList.add(bdA);

		OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
				.zIndex(0).draggable(true);
		mMarkerD = (Marker) mBaiduMap.addOverlay(ooA);
	}

	/**
	 * ��ʼ����
	 * 
	 * @param view
	 */
	public void startNavi(View view) {
		LatLng pt1 = new LatLng(mLat, mLng);
		LatLng pt2 = new LatLng(mlocLat, mlocLng);
		// ���� ��������
		NaviPara para = new NaviPara();
		para.startPoint = pt1;
		para.startName = "�����￪ʼ";
		para.endPoint = pt2;
		para.endName = "���������";

		try {

			BaiduMapNavigation.openBaiduMapNavi(para, this);

		} catch (BaiduMapAppNotSupportNaviException e) {
			e.printStackTrace();
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("����δ��װ�ٶȵ�ͼapp��app�汾���ͣ����ȷ�ϰ�װ��");
			builder.setTitle("��ʾ");
			builder.setPositiveButton("ȷ��", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					BaiduMapNavigation.getLatestBaiduMapApp(BigMapActivity.this);
				}
			});

			builder.setNegativeButton("ȡ��", new OnClickListener() {
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
		// ���� ��������
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
