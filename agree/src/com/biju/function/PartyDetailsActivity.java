package com.biju.function;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.BJ.javabean.Party;
import com.BJ.javabean.Party2;
import com.BJ.javabean.PartyRelationshipback;
import com.BJ.javabean.Party_User;
import com.BJ.javabean.ReadPartyback;
import com.BJ.javabean.Relation;
import com.BJ.javabean.ReturnData;
import com.BJ.utils.DensityUtil;
import com.BJ.utils.Weeks;
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
import com.baidu.mapapi.map.BaiduMapOptions;
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
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.biju.Interface;
import com.biju.Interface.UserInterface;
import com.biju.R;
import com.biju.login.LoginActivity;
import com.github.volley_examples.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;

public class PartyDetailsActivity extends Activity implements
		OnGetGeoCoderResultListener, OnClickListener {
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	public MyLocationListenner myListener = new MyLocationListenner();
	private BDLocation mLocation;
	boolean isFirstLoc = true;// 是否首次定位
	private double mLat = 24.497572;
	private double mLng = 118.17276;
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
	private boolean isreadparty;
	private String pk_party;
	private boolean updateUserJoinMsg;
	private Party2 oneParty;
	private MyReceiver receiver;
	private Integer relationship;
	private Integer pk_party_user;
	private Integer fk_user1;
	private Integer status;
	private boolean isParty;;
	private boolean finish_1;

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
				// LatLng ll = new LatLng(location.getLatitude(),
				// location.getLongitude());
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
		setContentView(R.layout.activity_party_details);
		SharedPreferences sp = getSharedPreferences("Registered", 0);
		isRegistered_one = sp.getBoolean("isRegistered_one", false);
		returndata = sp.getInt("returndata", returndata);
		SharedPreferences sp1 = getSharedPreferences("isLogin", 0);
		login = sp1.getBoolean("Login", false);

		initUI();
		initInterface();
		initOneParty();
		// addview 百度地图
		BaiduMapOptions options = new BaiduMapOptions();
		options.zoomGesturesEnabled(false);
		options.scaleControlEnabled(false);
		options.scrollGesturesEnabled(false);
		mMapView = new MapView(this, options);
		RelativeLayout.LayoutParams params_map = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		RelativeLayout bd_mapView_container = (RelativeLayout) findViewById(R.id.map_layout);
		bd_mapView_container.addView(mMapView, params_map);
		// addview edittext
		RelativeLayout.LayoutParams params_show = new LayoutParams(
				LayoutParams.MATCH_PARENT, DensityUtil.dip2px(this, 50));
		edit_show.setGravity(Gravity.CENTER);
		params_show.setMargins(0, DensityUtil.dip2px(this, 200), 0, 0);
		edit_show.setBackgroundColor(android.graphics.Color
				.parseColor("#aaffffff"));
		edit_show.setTextColor(android.graphics.Color.parseColor("#cdcdcd"));
		bd_mapView_container.addView(edit_show, params_show);

		mBaiduMap = mMapView.getMap();
		// 是否设置显示缩放控件
		mMapView.showZoomControls(false);
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(scale);
		mBaiduMap.setMapStatus(msu);

		initListener();
		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);

		// 得到fk_group
		SharedPreferences sp3 = getSharedPreferences("isParty_fk_group", 0);
		fk_group = sp3.getInt("fk_group", 0);

		if (relationship != null) {
			isreadparty = true;
			initReadParty();
			Log.e("PartyDetailsActivity", "已经有关系的小组进入=========");
		}
		// 初始化地图
		initMap();
		initFinish();
	}

	private void initFinish() {
		IntentFilter filter = new IntentFilter();
		filter.addAction("isFinish");
		receiver = new MyReceiver();
		registerReceiver(receiver, filter);
	}

	@Override
	protected void onStop() {
		unregisterReceiver(receiver);
		super.onStop();
	}

	class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish_1 = intent.getBooleanExtra("finish", false);
			if (finish_1) {
				PartyDetails_back();
			}
		}

	}

	private void initcreatePartyRelation(Integer pk_party_user) {
		SharedPreferences sp = getSharedPreferences("isPk_user", 0);
		Integer pk_user_1 = sp.getInt("Pk_user", 0);
		isParty = true;
		Party_User readuserparty = new Party_User();
		readuserparty.setPk_party_user(pk_party_user);
		readuserparty.setFk_party(pk_party);
		readuserparty.setFk_user(pk_user_1);
		Log.e("PartyDetailsActivity", "返回得到的用户ID111========" + pk_user_1);
		readuserparty.setStatus(1);
		readpartyInterface.createPartyRelation(PartyDetailsActivity.this,
				readuserparty);
	}

	private void initInterface() {
		readpartyInterface = new Interface();
		readpartyInterface.setPostListener(new UserInterface() {
			private int partakeNum;
			private int refuseNum;
			private int not_sayNum;

			@Override
			public void success(String A) {
				if (updateUserJoinMsg) {
					Log.e("PartyDetailsActivity", "返回的是否更新成功" + A);
					initReadParty();
				} else {
					if (isParty) {
						PartyRelationshipback partyRelationshipback = GsonUtils
								.parseJson(A, PartyRelationshipback.class);
						Integer statusMsg = partyRelationshipback
								.getStatusMsg();
						if (statusMsg == 1) {
							Log.e("PartyDetailsActivity", "返回新建立的聚会关系========"
									+ A);
							Log.e("PartyDetailsActivity", "应该是进入这里的========");
						}
					}

					if (isreadparty) {
						Log.e("PartyDetailsActivity", "返回的用户参与信息" + A);
						java.lang.reflect.Type type = new TypeToken<ReadPartyback>() {
						}.getType();
						ReadPartyback partyback = GsonUtils.parseJsonArray(A,
								type);
						ReturnData returnData = partyback.getReturnData();
						Log.e("PartyActivity",
								"当前returnData:" + returnData.toString());
						List<Relation> relationList = returnData.getRelation();
						if (relationList.size() > 0) {
							for (int i = 0; i < relationList.size(); i++) {
								Relation relation = relationList.get(i);
								Integer read_pk_user = relation.getPk_user();
								// 判断参与、拒绝数
								Integer relationship = relation
										.getRelationship();
								switch (relationship) {
								case 0:
									not_sayNum++;
									break;
								case 1:
									partakeNum++;
									break;
								case 2:
									refuseNum++;
									break;
								default:
									break;
								}
								// 当前用户
								if (String.valueOf(fk_user1).equals(
										String.valueOf(read_pk_user))) {
									Log.e("PartyActivity", "可以进行判断=======");
									Integer read_relationship = relation
											.getRelationship();
									switch (read_relationship) {
									case 0:
										mPartyDetails_partake
												.setBackgroundResource(R.drawable.ok_2);
										mPartyDetails_refuse
												.setBackgroundResource(R.drawable.ok_2);
										Log.e("PartyDetailsActivity",
												"用户未表态======" + relationship);
										break;
									case 1:
										mPartyDetails_partake
												.setBackgroundResource(R.drawable.ok_1);
										mPartyDetails_refuse
												.setBackgroundResource(R.drawable.ok_2);
										Log.e("PartyDetailsActivity",
												"用户已参与=======" + relationship);
										break;
									case 2:
										mPartyDetails_partake
												.setBackgroundResource(R.drawable.ok_2);
										mPartyDetails_refuse
												.setBackgroundResource(R.drawable.ok_1);
										Log.e("PartyDetailsActivity",
												"用户已拒绝=========="
														+ relationship);
										break;
									default:
										break;
									}

									Log.e("PartyActivity", "每个relationship:"
											+ read_relationship);
									Log.e("PartyActivity", "每个read_pk_user:"
											+ read_pk_user);
								}
							}
							mPartyDetails_tv_partake.setText(String.valueOf(partakeNum));
							mPartyDetails_tv_refuse.setText(String.valueOf(refuseNum));
							mPartyDetails_did_not_say.setText(String.valueOf(not_sayNum));
							partakeNum=0;
							refuseNum=0;
							not_sayNum=0;
						}
					}
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	private void initReadParty() {
		isreadparty = true;
		updateUserJoinMsg = false;
		Party readparty = new Party();
		readparty.setPk_party(pk_party);
		readpartyInterface.readPartyJoinMsg(PartyDetailsActivity.this,
				readparty);
	}

	private void initOneParty() {
		Intent intent = getIntent();
		oneParty = (Party2) intent.getSerializableExtra("oneParty");
		relationship = oneParty.getRelationship();
		pk_party_user = oneParty.getPk_party_user();
		pk_party = oneParty.getPk_party();
		status = oneParty.getStatus();
		if (relationship == null) {
			Log.e("PartyDetailsActivity", "用户未建立关系===");
			initcreatePartyRelation(pk_party_user);
		} else {
			switch (relationship) {
			case 0:
				mPartyDetails_partake.setBackgroundResource(R.drawable.ok_2);
				mPartyDetails_refuse.setBackgroundResource(R.drawable.ok_2);
				Log.e("PartyDetailsActivity", "用户未表态======" + relationship);
				break;
			case 1:
				mPartyDetails_partake.setBackgroundResource(R.drawable.ok_1);
				mPartyDetails_refuse.setBackgroundResource(R.drawable.ok_2);
				Log.e("PartyDetailsActivity", "用户已参与=======" + relationship);
				break;
			case 2:
				mPartyDetails_partake.setBackgroundResource(R.drawable.ok_2);
				mPartyDetails_refuse.setBackgroundResource(R.drawable.ok_1);
				Log.e("PartyDetailsActivity", "用户已拒绝==========" + relationship);
				break;
			default:
				break;
			}
		}
		String Begin_time = oneParty.getBegin_time();
		String years = Begin_time.substring(0, 4);
		String months = Begin_time.substring(5, 7);
		String days = Begin_time.substring(8, 10);
		String hour = Begin_time.substring(11, 13);
		String minute = Begin_time.substring(14, 16);
		Integer y = Integer.valueOf(years);
		Integer m = Integer.valueOf(months);
		Integer d = Integer.valueOf(days);
		// 调用计算星期几的方法
		Weeks.CaculateWeekDay(y, m, d);
		String week = Weeks.getweek();
		Log.e("PartyDetailsActivity", "获取的时间======" + years + "年" + months
				+ "月" + days + "日" + "  " + week + "  " + hour + ":" + minute);
		mPartyDetails_name.setText(oneParty.getName());
		mPartyDetails_time.setText(years + "年" + months + "月" + days + "日"
				+ "  " + week + "  " + hour + ":" + minute);

		Double latitude = oneParty.getLatitude();
		Double longitude = oneParty.getLongitude();
		String location = oneParty.getLocation();
		mLat = latitude;
		mLng = longitude;
		edit_show.setText(location);

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
		edit_show = new EditText(this);
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
				startActivity(new Intent(PartyDetailsActivity.this,
						BigMapActivity.class));
			}

			public boolean onMapPoiClick(MapPoi poi) {
				return false;
			}
		});
		mBaiduMap.setOnMapLongClickListener(new OnMapLongClickListener() {
			public void onMapLongClick(LatLng point) {
				startActivity(new Intent(PartyDetailsActivity.this,
						BigMapActivity.class));
			}
		});
		mBaiduMap.setOnMapDoubleClickListener(new OnMapDoubleClickListener() {
			public void onMapDoubleClick(LatLng point) {
				startActivity(new Intent(PartyDetailsActivity.this,
						BigMapActivity.class));

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
		mBaiduMap.setMyLocationEnabled(false);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.party_details, menu);
		return true;
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(PartyDetailsActivity.this, "抱歉，未能找到结果",
					Toast.LENGTH_LONG).show();
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
		case R.id.PartyDetails_partake:// 参与
			PartyDetails_refuse();
			break;
		case R.id.PartyDetails_refuse:// 拒绝
			PartyDetails_partake();
			break;
		default:
			break;
		}
	}

	private void PartyDetails_refuse() {
		updateUserJoinMsg = true;
		mPartyDetails_refuse.setBackgroundResource(R.drawable.ok_2);
		mPartyDetails_partake.setBackgroundResource(R.drawable.ok_1);
		Party_User party_user = new Party_User();
		party_user.setPk_party_user(pk_party_user);
		party_user.setRelationship(1);
		readpartyInterface.updateUserJoinMsg(PartyDetailsActivity.this,
				party_user);
	}

	private void PartyDetails_partake() {
		updateUserJoinMsg = true;
		mPartyDetails_refuse.setBackgroundResource(R.drawable.ok_1);
		mPartyDetails_partake.setBackgroundResource(R.drawable.ok_2);
		Party_User party_user = new Party_User();
		party_user.setPk_party_user(pk_party_user);
		party_user.setRelationship(2);
		readpartyInterface.updateUserJoinMsg(PartyDetailsActivity.this,
				party_user);
	}

	private void PartyDetails_more() {
		Intent intent = new Intent(PartyDetailsActivity.this,
				MoreActivity.class);
		intent.putExtra("moreparty", oneParty);
		startActivity(intent);
	}

	private void PartyDetails_back() {
		SharedPreferences PartyDetails_sp = getSharedPreferences(
				"isPartyDetails_", 0);
		Editor editor = PartyDetails_sp.edit();
		editor.putBoolean("PartyDetails", true);
		editor.commit();
		finish();
	}
}
