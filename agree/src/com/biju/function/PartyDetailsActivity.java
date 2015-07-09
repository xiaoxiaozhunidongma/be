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
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapDoubleClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMapTouchListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.biju.R;
import com.biju.function.MapActivity.MyLocationListenner;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.BJ.javabean.Party;
import com.BJ.javabean.Party_User;
import com.biju.Interface;
import com.biju.Interface.UserInterface;
import com.biju.login.LoginActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class PartyDetailsActivity extends Activity implements OnGetGeoCoderResultListener, OnClickListener {
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	public MyLocationListenner myListener = new MyLocationListenner();
	private BDLocation mLocation;
	boolean isFirstLoc = true;// 是否首次定位
	private double mLng;
	private double mLat;
	private float scale = 15.0f;
	private LocationClient mLocClient;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private ArrayList<BitmapDescriptor> mOverLayList = new ArrayList<BitmapDescriptor>();
	private Marker mMarkerD;
	private GeoCoder mSearch;
	private EditText edit_show;
	
	private TextView mPartyDetails_name;
	private TextView mPartyDetails_time;
	private TextView mPartyDetails_tv_partake;
	private TextView mPartyDetails_tv_refuse;
	private TextView mPartyDetails_did_not_say;
	private TextView mPartyDetails_partake;
	private TextView mPartyDetails_refuse;
	private int returndata;
	private boolean isRegistered_one;
	private boolean login;
	private int fk_group;
	private Interface readpartyInterface;
	private boolean iscreateParty;
	private boolean isreadparty;
	private String uUID;
	
	/**
	 * 定位SDK监听函数
	 */
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
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
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
		setContentView(R.layout.activity_party_details);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		//是否设置缩放控件
		mMapView.showZoomControls(false);
		initMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(scale);
		mBaiduMap.setMapStatus(msu);
		initListener(); 
		// 初始化搜索模块，注册事件监听 
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		
		SharedPreferences sp = getSharedPreferences("Registered", 0);
		isRegistered_one = sp.getBoolean("isRegistered_one", false);
		returndata = sp.getInt("returndata", returndata);
		SharedPreferences sp1 = getSharedPreferences("isLogin", 0);
		login = sp1.getBoolean("Login", false);

		// 得到fk_group
		SharedPreferences sp3 = getSharedPreferences("isParty_fk_group", 0);
		fk_group = sp3.getInt("fk_group", 0);
		
		initUI();
		initOneParty();
		initInterface();
//		initcreatePartyRelation(uUID);
		initReadParty(uUID);
	}
	
	private void initcreatePartyRelation(String pk_party) {
		isreadparty=false;
		Party_User readuserparty = new Party_User();
		readuserparty.setFk_party(pk_party);
		if(isRegistered_one)
		{
			readuserparty.setFk_user(returndata);
		}else
		{
			if(login)
			{
				int pk_user = LoginActivity.pk_user;
				readuserparty.setFk_user(pk_user);
			}else
			{
				readuserparty.setFk_user(returndata);
			}
		}
		readuserparty.setType(1);
		readuserparty.setStatus(1);
		readpartyInterface.createPartyRelation(PartyDetailsActivity.this, readuserparty);
	}

	private void initInterface() {
		readpartyInterface = new Interface();
		readpartyInterface.setPostListener(new UserInterface() {

			@Override
			public void success(String A) {
				if(isreadparty)
				{
					Log.e("PartyDetailsActivity", "返回的用户参与信息"+A);
					
				}else
				{
					Log.e("PartyDetailsActivity", "返回新建立的聚会关系"+A);
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	private void initReadParty(String pk_party) {
		isreadparty=true;
		Party readparty = new Party();
		readparty.setPk_party(pk_party);
		if(isRegistered_one)
		{
			readparty.setFk_user(returndata);
		}else
		{
			if(login)
			{
				int pk_user = LoginActivity.pk_user;
				readparty.setFk_user(pk_user);
			}else
			{
				readparty.setFk_user(returndata);
			}
		}
		readparty.setFk_group(fk_group);
		readparty.setStatus(1);
		readpartyInterface.readPartyJoinMsg(PartyDetailsActivity.this, readparty);
		initcreatePartyRelation(uUID);
	}

	private void initOneParty() {
		Intent intent = getIntent();
		Party oneParty = (Party) intent.getSerializableExtra("oneParty");
		uUID = oneParty.getPk_party();
		mPartyDetails_name.setText(oneParty.getName());
		mPartyDetails_time.setText(oneParty.getBegin_time());
	}

	private void initUI() {
		mPartyDetails_name = (TextView) findViewById(R.id.PartyDetails_name);// 聚会名称
		mPartyDetails_time = (TextView) findViewById(R.id.PartyDetails_time);// 聚会时间
		mPartyDetails_tv_partake = (TextView) findViewById(R.id.PartyDetails_tv_partake);// 参与数量
		mPartyDetails_tv_refuse = (TextView) findViewById(R.id.PartyDetails_tv_refuse);// 拒绝数量
		mPartyDetails_did_not_say = (TextView) findViewById(R.id.PartyDetails_did_not_say);// 未表态
		mPartyDetails_partake = (TextView) findViewById(R.id.PartyDetails_partake);// 选择参与
		mPartyDetails_partake.setOnClickListener(this);
		mPartyDetails_refuse = (TextView) findViewById(R.id.PartyDetails_refuse);// 选择拒绝
		mPartyDetails_refuse.setOnClickListener(this);
		edit_show = (EditText) findViewById(R.id.edit_show);
		findViewById(R.id.PartyDetails_back_layout).setOnClickListener(this);
		findViewById(R.id.PartyDetails_back).setOnClickListener(this);
		findViewById(R.id.PartyDetails_more_layout).setOnClickListener(this);
		findViewById(R.id.PartyDetails_more).setOnClickListener(this);
	}

	


	private void initListener() {

		mBaiduMap.setOnMapTouchListener(new OnMapTouchListener() {

			@Override
			public void onTouch(MotionEvent event) {
			}
		});

		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

			public void onMapClick(LatLng point) {
			}

			public boolean onMapPoiClick(MapPoi poi) {
				return false;
			}
		});
		mBaiduMap.setOnMapLongClickListener(new OnMapLongClickListener() {
			public void onMapLongClick(LatLng point) {
			}
		});
		mBaiduMap.setOnMapDoubleClickListener(new OnMapDoubleClickListener() {
			public void onMapDoubleClick(LatLng point) {

			}
		});
		mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {
			public void onMapStatusChangeStart(MapStatus status) {
			}

			public void onMapStatusChangeFinish(MapStatus status) {
			}

			public void onMapStatusChange(MapStatus status) {
			}
		});

	}


	private void initMap() {
		// 添加地图标点
		addOverlay(mLat, mLng, R.drawable.iconfont2);
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
	
	private void addOverlay(double lat, double lng, final int drawableRes) {
		LatLng llA = new LatLng(lat, lng);
				
		//设置悬浮的图案
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


	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(PartyDetailsActivity .this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.PartyDetails_back_layout:
		case R.id.PartyDetails_back:
			PartyDetails_back();
			break;
		case R.id.PartyDetails_more_layout:
		case R.id.PartyDetails_more:
			PartyDetails_more();
			break;
		default:
			break;
		}
	}

	private void PartyDetails_more() {
		Intent intent=new Intent(PartyDetailsActivity.this, MoreActivity.class);
		startActivity(intent);
	}

	private void PartyDetails_back() {
		finish();
	}

}
