package com.biju.function;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.BJ.javabean.Party;
import com.BJ.javabean.Party2;
import com.BJ.javabean.Party_User;
import com.BJ.javabean.ReadPartyback;
import com.BJ.javabean.Relation;
import com.BJ.javabean.ReturnData;
import com.BJ.javabean.UserAllParty;
import com.BJ.utils.DensityUtil;
import com.BJ.utils.SdPkUser;
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
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.readPartyJoinMsgListenner;
import com.biju.Interface.updateUserJoinMsgListenner;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;

public class PartyDetailsActivity extends Activity implements OnGetGeoCoderResultListener, OnClickListener {
	
	
	public static PartyDetailsActivity PartyDetails;
	
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
	private Interface readpartyInterface;
	private String pk_party;
	private Party2 oneParty;
	private Integer pk_party_user;
	private Integer fk_user1;
	private UserAllParty allParty;
	private boolean userAll;

	private Integer sD_pk_user;


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
		PartyDetails=this;
		sD_pk_user = SdPkUser.getsD_pk_user();
		initUI();
		initInterface();
		initOneParty();
		initReadParty();
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
		RelativeLayout.LayoutParams params_show = new LayoutParams(LayoutParams.MATCH_PARENT, DensityUtil.dip2px(this, 50));
		edit_show.setGravity(Gravity.CENTER);
		params_show.setMargins(0, DensityUtil.dip2px(this, 200), 0, 0);
		edit_show.setBackgroundColor(android.graphics.Color.parseColor("#aaffffff"));
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

		// 初始化地图
		initMap();
	}


	private void initInterface() {
		readpartyInterface = Interface.getInstance();
		readpartyInterface.setPostListener(new updateUserJoinMsgListenner() {

			@Override
			public void success(String A) {
				Log.e("PartyDetailsActivity", "返回的是否更新成功" + A);
				initReadParty();
			}

			@Override
			public void defail(Object B) {

			}
		});

		
		readpartyInterface.setPostListener(new readPartyJoinMsgListenner() {
			private int partakeNum;
			private int refuseNum;
			private int not_sayNum;

			@Override
			public void success(String A) {
				Log.e("PartyDetailsActivity", "返回的用户参与信息" + A);
				java.lang.reflect.Type type = new TypeToken<ReadPartyback>() {
				}.getType();
				ReadPartyback partyback = GsonUtils.parseJsonArray(A, type);
				ReturnData returnData = partyback.getReturnData();
				Log.e("PartyDetailsActivity","当前returnData:" + returnData.toString());
				List<Relation> relationList = returnData.getRelation();
				if (relationList.size() > 0) {
					for (int i = 0; i < relationList.size(); i++) {
						Relation relation = relationList.get(i);
						Integer read_pk_user = relation.getPk_user();
						// 判断参与、拒绝数
						Integer relationship = relation.getRelationship();
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
						if (String.valueOf(fk_user1).equals(String.valueOf(read_pk_user))) {
							Integer read_relationship = relation.getRelationship();
							switch (read_relationship) {
							case 0:
								mPartyDetails_partake.setBackgroundResource(R.drawable.ok_2);
								mPartyDetails_refuse.setBackgroundResource(R.drawable.ok_2);
								Log.e("PartyDetailsActivity", "用户未表态======"+ relationship);
								break;
							case 1:
								mPartyDetails_partake.setBackgroundResource(R.drawable.ok_1);
								mPartyDetails_refuse.setBackgroundResource(R.drawable.ok_2);
								Log.e("PartyDetailsActivity", "用户已参与======="+ relationship);
								break;
							case 2:
								mPartyDetails_partake.setBackgroundResource(R.drawable.ok_2);
								mPartyDetails_refuse.setBackgroundResource(R.drawable.ok_1);
								Log.e("PartyDetailsActivity", "用户已拒绝=========="+ relationship);
								break;
							default:
								break;
							}
						}
					}
					Log.e("PartyDetailsActivity", "当前partakeNum的数量"+ partakeNum);
					Log.e("PartyDetailsActivity", "当前refuseNum的数量"+ refuseNum);
					Log.e("PartyDetailsActivity", "当前not_sayNum的数量"+ not_sayNum);
					mPartyDetails_tv_partake.setText(String.valueOf(partakeNum));
					mPartyDetails_tv_refuse.setText(String.valueOf(refuseNum));
					mPartyDetails_did_not_say.setText(String.valueOf(not_sayNum));
					partakeNum = 0;
					refuseNum = 0;
					not_sayNum = 0;
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	//读取聚会详情
	private void initReadParty() {
		Party readparty = new Party();
		readparty.setPk_party(pk_party);
		readpartyInterface.readPartyJoinMsg(PartyDetailsActivity.this,readparty);
	}

	//首次进来时传值
	private void initOneParty() {
		Intent intent = getIntent();
		userAll = intent.getBooleanExtra(IConstant.UserAll, false);
		if (userAll) {
			allParty = (UserAllParty) intent.getSerializableExtra(IConstant.UserAllParty);
			Integer allrelationship = allParty.getRelationship();
			pk_party_user = allParty.getPk_party_user();
			pk_party = allParty.getPk_party();
			fk_user1 = allParty.getFk_user();
			Log.e("PartyDetailsActivity", "有进入到所有的聚会当中======");
			Log.e("PartyDetailsActivity", "有进入到所有的聚会当中的allrelationship======"+ allrelationship);
			switch (allrelationship) {
			case 0:
				mPartyDetails_partake.setBackgroundResource(R.drawable.ok_2);
				mPartyDetails_refuse.setBackgroundResource(R.drawable.ok_2);
				Log.e("PartyDetailsActivity", "用户未表态======" + allrelationship);
				break;
			case 1:
				mPartyDetails_partake.setBackgroundResource(R.drawable.ok_1);
				mPartyDetails_refuse.setBackgroundResource(R.drawable.ok_2);
				Log.e("PartyDetailsActivity", "用户已参与=======" + allrelationship);
				break;
			case 2:
				mPartyDetails_partake.setBackgroundResource(R.drawable.ok_2);
				mPartyDetails_refuse.setBackgroundResource(R.drawable.ok_1);
				Log.e("PartyDetailsActivity", "用户已拒绝=========="+ allrelationship);
				break;
			default:
				break;
			}
			String Begin_time = allParty.getBegin_time();
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
			mPartyDetails_name.setText(allParty.getName());
			mPartyDetails_time.setText(years + "年" + months + "月" + days + "日"
					+ "  " + week + "  " + hour + ":" + minute);

			Double latitude = allParty.getLatitude();
			Double longitude = allParty.getLongitude();
			String location = allParty.getLocation();
			mLat = latitude;
			mLng = longitude;
			edit_show.setText(location);
		} else {
			oneParty = (Party2) intent.getSerializableExtra(IConstant.OneParty);
			boolean isRelationship=intent.getBooleanExtra(IConstant.IsRelationship, false);
			Integer relationship = oneParty.getRelationship();
			if(isRelationship)
			{
				pk_party_user =SdPkUser.getGetPk_party_user();
				Log.e("PartyDetailsActivity", "得到的getPk_party_user111111111" + pk_party_user);
			}else
			{
				pk_party_user=oneParty.getPk_party_user();
			}
			pk_party = oneParty.getPk_party();
			fk_user1 = oneParty.getFk_user();
			if (relationship == null) {
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
					Log.e("PartyDetailsActivity", "用户已拒绝=========="+ relationship);
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
					+ "月" + days + "日" + "  " + week + "  " + hour + ":"
					+ minute);
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
		findViewById(R.id.PartyDetails_tv_partake_prompt).setOnClickListener(this);// 参与提示字符
		findViewById(R.id.PartyDetails_tv_refuse_prompt).setOnClickListener(this);// 拒绝提示字符
		findViewById(R.id.PartyDetails_did_not_say_prompt).setOnClickListener(this);// 未表态提示字符
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
		BitmapDescriptor bdA = BitmapDescriptorFactory.fromResource(drawableRes);
		mOverLayList.add(bdA);

		OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA).zIndex(0).draggable(true);
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
			Toast.makeText(PartyDetailsActivity.this, "抱歉，未能找到结果",Toast.LENGTH_LONG).show();
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
		case R.id.PartyDetails_tv_partake_prompt:
		case R.id.PartyDetails_tv_partake:
			PartyDetails_tv_partake_prompt();
			break;
		case R.id.PartyDetails_tv_refuse_prompt:
		case R.id.PartyDetails_tv_refuse:
			PartyDetails_tv_refuse_prompt();
			break;
		case R.id.PartyDetails_did_not_say_prompt:
		case R.id.PartyDetails_did_not_say:
			PartyDetails_did_not_say_prompt();
			break;
		default:
			break;
		}
	}

	// 未表态
	private void PartyDetails_did_not_say_prompt() {
		Intent intent = new Intent(PartyDetailsActivity.this,CommentsListActivity.class);
		intent.putExtra(IConstant.CommentsList, 0);
		intent.putExtra(IConstant.Not_Say, pk_party);
		startActivity(intent);
	}

	// 拒绝
	private void PartyDetails_tv_refuse_prompt() {
		Intent intent = new Intent(PartyDetailsActivity.this,CommentsListActivity.class);
		intent.putExtra(IConstant.CommentsList, 2);
		intent.putExtra(IConstant.Refuse, pk_party);
		startActivity(intent);
	}

	// 参与
	private void PartyDetails_tv_partake_prompt() {
		Intent intent = new Intent(PartyDetailsActivity.this,CommentsListActivity.class);
		intent.putExtra(IConstant.CommentsList, 1);
		intent.putExtra(IConstant.ParTake, pk_party);
		startActivity(intent);

	}

	private void PartyDetails_refuse() {
		mPartyDetails_refuse.setBackgroundResource(R.drawable.ok_2);
		mPartyDetails_partake.setBackgroundResource(R.drawable.ok_1);
		Party_User party_user = new Party_User();
		party_user.setPk_party_user(pk_party_user);
		Log.e("PartyDetailsActivity", "得到的getPk_party_user2222222222" + pk_party_user);
		party_user.setRelationship(1);
		party_user.setStatus(1);
		party_user.setFk_party(pk_party);
		readpartyInterface.updateUserJoinMsg(PartyDetailsActivity.this,party_user);
		
		
		Toast toast = Toast.makeText(getApplicationContext(), "已参与",Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		LinearLayout toastView = (LinearLayout) toast.getView();
		ImageView imageCodeProject = new ImageView(getApplicationContext());
		imageCodeProject.setImageResource(R.drawable.checked);
		toastView.addView(imageCodeProject, 0);
		toast.show();
	}

	private void PartyDetails_partake() {
		mPartyDetails_refuse.setBackgroundResource(R.drawable.ok_1);
		mPartyDetails_partake.setBackgroundResource(R.drawable.ok_2);
		Party_User party_user = new Party_User();
		party_user.setPk_party_user(pk_party_user);
		Log.e("PartyDetailsActivity", "得到的getPk_party_user333333333" + pk_party_user);
		party_user.setRelationship(2);
		party_user.setFk_party(pk_party);
		party_user.setStatus(1);
		readpartyInterface.updateUserJoinMsg(PartyDetailsActivity.this,party_user);

		
		Toast toast = Toast.makeText(getApplicationContext(), "已拒绝",Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		LinearLayout toastView = (LinearLayout) toast.getView();
		ImageView imageCodeProject = new ImageView(getApplicationContext());
		imageCodeProject.setImageResource(R.drawable.chahao);
		toastView.addView(imageCodeProject, 0);
		toast.show();
	}

	private void PartyDetails_more() {
		Intent intent = new Intent(PartyDetailsActivity.this,MoreActivity.class);
		if (userAll) {
			intent.putExtra(IConstant.UserAllUoreParty, allParty);
			intent.putExtra(IConstant.UserAll, true);
		} else {
			intent.putExtra(IConstant.MoreParty, oneParty);
		}
		startActivity(intent);
	}

	private void PartyDetails_back() {
		if (userAll) {
			finish();
		} else {
			finish();
			SharedPreferences PartyDetails_sp = getSharedPreferences(IConstant.IsPartyDetails_, 0);
			Editor editor = PartyDetails_sp.edit();
			editor.putBoolean(IConstant.PartyDetails, true);
			editor.commit();
		}

	}
}
