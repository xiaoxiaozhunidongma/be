package com.biju.function;

import java.util.ArrayList;
import java.util.List;
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

import com.BJ.javabean.GroupCodeback2;
import com.BJ.javabean.Party;
import com.BJ.javabean.Party_User;
import com.BJ.javabean.ReadPartyback;
import com.BJ.javabean.Relation;
import com.BJ.javabean.ReturnData;
import com.BJ.javabean.User;
import com.biju.Interface;
import com.biju.Interface.UserInterface;
import com.biju.login.LoginActivity;
import com.github.volley_examples.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class PartyDetailsActivity extends Activity implements OnGetGeoCoderResultListener, OnClickListener {
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	public MyLocationListenner myListener = new MyLocationListenner();
	private BDLocation mLocation;
	boolean isFirstLoc = true;// �Ƿ��״ζ�λ
	private double mLat=24.497572;
	private double mLng=118.17276;
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
//				LatLng ll = new LatLng(location.getLatitude(),
//						location.getLongitude());
				LatLng ll = new LatLng(mLat,
						mLng);
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
		setContentView(R.layout.activity_party_details);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		//�Ƿ��������ſؼ�
		mMapView.showZoomControls(false);
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(scale);
		mBaiduMap.setMapStatus(msu);
		initListener(); 
		// ��ʼ������ģ�飬ע���¼����� 
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		
		SharedPreferences sp = getSharedPreferences("Registered", 0);
		isRegistered_one = sp.getBoolean("isRegistered_one", false);
		returndata = sp.getInt("returndata", returndata);
		SharedPreferences sp1 = getSharedPreferences("isLogin", 0);
		login = sp1.getBoolean("Login", false);

		// �õ�fk_group
		SharedPreferences sp3 = getSharedPreferences("isParty_fk_group", 0);
		fk_group = sp3.getInt("fk_group", 0);
		
		initUI();
		initOneParty();
		initInterface();
//		initcreatePartyRelation(uUID);
		initReadParty(uUID);
		//��ʼ����ͼ
		initMap();
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
					Log.e("PartyDetailsActivity", "���ص��û�������Ϣ"+A);
					java.lang.reflect.Type type = new TypeToken<ReadPartyback>() {
					}.getType();
					ReadPartyback partyback = GsonUtils.parseJsonArray(A, type);
					 ReturnData returnData = partyback.getReturnData();
					Log.e("PartyActivity", "��ǰreturnData:"+returnData.toString());
					 List<Relation> relationList = returnData.getRelation();
					for (int i = 0; i < relationList.size(); i++) {
						Relation relation = relationList.get(0);
						String relationship = relation.getRelationship();
						Log.e("PartyActivity", "ÿ��relationship:"+relationship);
					}
					
				}else
				{
					Log.e("PartyDetailsActivity", "�����½����ľۻ��ϵ"+A);
					
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
//		initcreatePartyRelation(uUID);
	}

	private void initOneParty() {
		Intent intent = getIntent();
		Party oneParty = (Party) intent.getSerializableExtra("oneParty");
		uUID = oneParty.getPk_party();
		mPartyDetails_name.setText(oneParty.getName());
		mPartyDetails_time.setText(oneParty.getBegin_time());
		
		Double latitude = oneParty.getLatitude();
		Double longitude = oneParty.getLongitude();
		String location = oneParty.getLocation();
		mLat=latitude;
		mLng=longitude;
		edit_show.setText(location);
		
	}

	private void initUI() {
		mPartyDetails_name = (TextView) findViewById(R.id.PartyDetails_name);// �ۻ�����
		mPartyDetails_time = (TextView) findViewById(R.id.PartyDetails_time);// �ۻ�ʱ��
		mPartyDetails_tv_partake = (TextView) findViewById(R.id.PartyDetails_tv_partake);// ��������
		mPartyDetails_tv_refuse = (TextView) findViewById(R.id.PartyDetails_tv_refuse);// �ܾ�����
		mPartyDetails_did_not_say = (TextView) findViewById(R.id.PartyDetails_did_not_say);// δ��̬
		mPartyDetails_partake = (TextView) findViewById(R.id.PartyDetails_partake);// ѡ�����
		mPartyDetails_partake.setOnClickListener(this);
		mPartyDetails_refuse = (TextView) findViewById(R.id.PartyDetails_refuse);// ѡ��ܾ�
		mPartyDetails_refuse.setOnClickListener(this);
		edit_show = (EditText) findViewById(R.id.edit_show);
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
		// ��ӵ�ͼ���
		addOverlay(mLat, mLng, R.drawable.iconfont2);
		// ������λͼ��
		mBaiduMap.setMyLocationEnabled(false);
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


	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(PartyDetailsActivity .this, "��Ǹ��δ���ҵ����", Toast.LENGTH_LONG)
					.show();
			return;
		}
	}
	@Override
	public void onClick(View v) {

	}

}
