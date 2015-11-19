package com.biju.function;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.biju.IConstant;
import com.biju.R;

public class MapActivity extends Activity implements OnGetGeoCoderResultListener, OnClickListener {

	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private LatLng currentPt;
	private String touchType;
	public MyLocationListenner myListener = new MyLocationListenner();
	private BDLocation mLocation;
	boolean isFirstLoc = true;// �Ƿ��״ζ�λ
	private double mLng;
	private double mLat;
	private float scale = 14.0f;
	private LocationClient mLocClient;
	private ArrayList<BitmapDescriptor> mOverLayList = new ArrayList<BitmapDescriptor>();
	private GeoCoder mSearch;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private Marker mMarkerD;
	private float x;
	private float y;
	private EditText edit_address;
	private String strCity;
	private String strGeocodekey;

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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// ��ͼ��ʼ��
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		initMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(scale);
		mBaiduMap.setMapStatus(msu);
		initListener(); 
		// ��ʼ������ģ�飬ע���¼����� 
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);

		initUI();
	}

	private void initUI() {
		findViewById(R.id.Map_back_layout).setOnClickListener(this);// ����
		findViewById(R.id.Map_back).setOnClickListener(this);
		findViewById(R.id.Map_OK_layout).setOnClickListener(this);// ��һ��
		findViewById(R.id.Map_OK).setOnClickListener(this);
		edit_address = (EditText) findViewById(R.id.edit_address);
	}

	private void initListener() {

		mBaiduMap.setOnMapTouchListener(new OnMapTouchListener() {

			@Override
			public void onTouch(MotionEvent event) {
				y = event.getY();
				x = event.getX();
			}
		});

		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

			public void onMapClick(LatLng point) {
				touchType = "����";
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
				touchType = "˫��";
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

	}

	@SuppressLint("DefaultLocale")
	@SuppressWarnings("unused")
	private void updateMapState() {

		String state = "";
		if (currentPt == null) {
			state = "�����������˫����ͼ�Ի�ȡ��γ�Ⱥ͵�ͼ״̬";
		} else {
			state = String.format("��ǰ���ȣ� %f ��ǰγ�ȣ�%f", currentPt.longitude,
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
			// ��Geo����
			mSearch.reverseGeoCode(new ReverseGeoCodeOption()
					.location(ptCenter));

			initMap();
		}
	}

	private void initMap() {

		// // ���ñ����ߣ�15.0f(500��)��14.0f(1����)��10.0f(20����)
		// ��������˻����
		// MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(scale);
		// mBaiduMap.setMapStatus(msu);
		// ��ӵ�������
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
		final LatLng llA = new LatLng(lat, lng);
		
		//���ø������µ�����
		final ImageView imageView = (ImageView) findViewById(R.id.imageView1);
		int[] location=new int[2];
		imageView.getLocationInWindow(location);
		int offheight=location[1];
		int offleft=location[0];
		int IMGheight = imageView.getHeight();
		int IMGwidth = imageView.getWidth();
		Log.e("Maina", "offheight:"+offheight);
		Log.e("Maina", "offleft:"+offleft);
		
		Animation animation=new TranslateAnimation(x-IMGwidth/2, x-IMGwidth/2, 0, y-offheight+IMGheight/2);
		animation.setDuration(500);
//		animation.setFillAfter(true);
		//��һ�ν�������������
		if(isFirstLoc==false){
			imageView.startAnimation(animation);
		}
		//���֮ǰ�����
		if(mMarkerD!=null){
			mMarkerD.remove();
		}
		animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				
				//����������ͼ��
				BitmapDescriptor bdA = BitmapDescriptorFactory
						.fromResource(drawableRes);
				mOverLayList.add(bdA);
				
				OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
						.zIndex(0).draggable(true);
				mMarkerD = (Marker)mBaiduMap.addOverlay(ooA);
			}
		});
		
		// ����������������
		// mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
		// public boolean onMarkerClick(final Marker marker) {
		// Button button = new Button(getApplicationContext());
		// button.setBackgroundResource(R.drawable.popup);
		// button.setText("����ͼ��");
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
	}

	@Override
	protected void onPause() {
		// MapView������������Activityͬ������activity����ʱ�����MapView.onPause()
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// MapView������������Activityͬ������activity�ָ�ʱ�����MapView.onResume()
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// MapView������������Activityͬ������activity����ʱ�����MapView.destroy()
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
		for (int i = 0; i < mOverLayList.size(); i++) {
			mOverLayList.get(i).recycle();
		}
		mSearch.destroy();
		super.onDestroy();
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(MapActivity.this, "��Ǹ��δ���ҵ����", Toast.LENGTH_LONG)
					.show();
			return;
		}
		mLat=result.getLocation().latitude;
		mLng=result.getLocation().longitude;
//		//���Զ�λͼ��
//		initMap();
		float mLng_1=(float) mLng;
		float mLat_1=(float) mLat;
		Log.e("MapActivity", "mLng_1:"+mLng_1);
		Log.e("MapActivity", "mLat_1:"+mLat_1);
		//���д���ַ��γ�Ⱥ;����ַ
		SharedPreferences sp=getSharedPreferences("isParty", 0);
		Editor editor=sp.edit();
		editor.putString("isAddress", result.getAddress());
		editor.putFloat("mLng", mLng_1);
		editor.putFloat("mLat", mLat_1);
		editor.commit();
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(MapActivity.this, "��Ǹ��δ���ҵ����", Toast.LENGTH_LONG)
					.show();
			return;
		}
//		NotifiUtils.showToast(MapActivity.this, result.getAddress());
		edit_address.setText(result.getAddress());
		float mLng_1=(float) mLng;
		float mLat_1=(float) mLat;
		Log.e("MapActivity", "���mLng_1:"+mLng_1);
		Log.e("MapActivity", "���mLat_1:"+mLat_1);
		SharedPreferences map_sp=getSharedPreferences(IConstant.IsMap, 0);
		Editor editor=map_sp.edit();
		editor.putBoolean(IConstant.IsMapChoose, true);
		editor.putString(IConstant.IsAddress, result.getAddress());
		editor.putFloat(IConstant.MLng, mLng_1);
		editor.putFloat(IConstant.MLat, mLat_1);
		editor.commit();
		
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Map_back_layout:
		case R.id.Map_back:
			map_back();
			break;
		case R.id.Map_OK_layout:
		case R.id.Map_OK:
			Map_OK();
			break;
		default:
			break;
		}
	}

	private void Map_OK() {
		String StrAddress = edit_address.getText().toString();
		if(StrAddress.length()>=9){
			strCity = StrAddress.substring(0, 6);
			strGeocodekey = StrAddress.substring(6);
		}else{
//			Toast.makeText(MapActivity.this, "���ȸ�ʽ������ʡ�����к�����Сѧ·", Toast.LENGTH_SHORT).show();
			return;
		}
		Log.e("MapActivity", "StrAddress:"+StrAddress);
		Log.e("MapActivity", "StrCity:"+strCity);
		Log.e("MapActivity", "StrGeocodekey:"+strGeocodekey);
		if(strCity!=null&&strGeocodekey!=null){
			mSearch.geocode(new GeoCodeOption().city(strCity).address(strGeocodekey));
		}
		finish();
	}

	private void map_back() {
		finish();
	}



}
