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
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.BJ.javabean.Group;
import com.BJ.javabean.Group_ReadAllUser;
import com.BJ.javabean.Group_ReadAllUserback;
import com.BJ.javabean.Loginback;
import com.BJ.javabean.Party;
import com.BJ.javabean.Party2;
import com.BJ.javabean.Party3;
import com.BJ.javabean.Party_User;
import com.BJ.javabean.ReadPartyback;
import com.BJ.javabean.Relation;
import com.BJ.javabean.ReturnData;
import com.BJ.javabean.User;
import com.BJ.javabean.UserAllParty;
import com.BJ.utils.DensityUtil;
import com.BJ.utils.RefreshActivity;
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
import com.biju.Interface.findUserListenner;
import com.biju.Interface.readAllPerRelationListenner;
import com.biju.Interface.readPartyJoinMsgListenner;
import com.biju.Interface.updateUserJoinMsgListenner;
import com.biju.R;
import com.biju.pay.PayBaseActivity;
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
	private ArrayList<Group_ReadAllUser> PartyDetailsList = new ArrayList<Group_ReadAllUser>();
	private ArrayList<Relation> partakeNumList = new ArrayList<Relation>();
	private Marker mMarkerD;
	private GeoCoder mSearch;
	private EditText edit_show;

	private Interface readpartyInterface;
	private Party2 oneParty;
	private Integer pk_party_user;
	private UserAllParty allParty;
	private boolean userAll;

	private Integer sD_pk_user;

	private TextView mPartyDetails_partyaddress;
	private TextView mPartyDetails_partyname;
	private TextView mPartyDetails_partytime;
	private TextView mPartyDetails_partypayment;
	private TextView mPartyDetails_partake_number;
	private TextView mPartyDetails_did_not_say_number;
	private TextView mPartyDetails_party_organizer;
	private TextView mPartyDetails_apply;
	private String pk_party;
	private Integer fk_group;
	private int not_sayNum;
	private Integer current_relationship;
	private RelativeLayout mPartyDetails_apply_layout;
	private Integer mPay_type;
	private Integer mPay_amount;
	private String mPayName;
	public static GetWeChatPay getWeChatPay;
	public static GetAliPay getAliPay;

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
		// 加入list中
		RefreshActivity.activList_1.add(PartyDetailsActivity.this);
		// 获取sd卡中的sD_pk_user
		sD_pk_user = SdPkUser.getsD_pk_user();

		initUI();
		initInterface();
		initOneParty();
		returndata();
			
		// addview 百度地图
		BaiduMapOptions options = new BaiduMapOptions();
		options.zoomGesturesEnabled(false);

		options.scaleControlEnabled(false);
		options.scrollGesturesEnabled(false);
		mMapView = new MapView(this, options);
		RelativeLayout.LayoutParams params_map = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		RelativeLayout bd_mapView_container = (RelativeLayout) findViewById(R.id.PartyDetails_map_layout);
		
		
		bd_mapView_container.addView(mMapView, params_map);
		// addview edittext
		RelativeLayout.LayoutParams params_show = new LayoutParams(
				LayoutParams.MATCH_PARENT, DensityUtil.dip2px(this, 40));
		edit_show.setGravity(Gravity.CENTER);
		params_show.setMargins(0, DensityUtil.dip2px(this, 210), 0, 0);
		edit_show.setBackgroundColor(android.graphics.Color.parseColor("#aaffffff"));
		edit_show.setTextColor(android.graphics.Color.parseColor("#535353"));
		edit_show.setTextSize(15);
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
		initGetWeChatPay();//微信支付
		initAliPay();//支付宝支付
	}
	
	private void initAliPay() {
		GetAliPay getAliPay=new GetAliPay() {
			
			@Override
			public void AliPay() {
				Party_User party_user = new Party_User();
				party_user.setPk_party_user(pk_party_user);
				party_user.setRelationship(4);
				party_user.setStatus(1);
				party_user.setFk_party(pk_party);
				party_user.setFk_user(sD_pk_user);
				readpartyInterface.updateUserJoinMsg(PartyDetailsActivity.this,party_user);
				Toast();
			}
		};
		this.getAliPay=getAliPay;
	}

	@SuppressWarnings("static-access")
	private void initGetWeChatPay() {
		GetWeChatPay getWeChatPay=new GetWeChatPay() {
			
			@Override
			public void WeChatPay() {
				Party_User party_user = new Party_User();
				party_user.setPk_party_user(pk_party_user);
				party_user.setRelationship(4);
				party_user.setStatus(1);
				party_user.setFk_party(pk_party);
				party_user.setFk_user(sD_pk_user);
				readpartyInterface.updateUserJoinMsg(PartyDetailsActivity.this,party_user);
				Toast();
			}
		};
		this.getWeChatPay=getWeChatPay;
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		initInterface();
		if(userAll)
		{
			SharedPreferences PartyDetails_sp=getSharedPreferences(IConstant.Partyfragmnet, 0);
			pk_party_user=PartyDetails_sp.getInt(IConstant.Partyfragmnet_Pk_party_user, 0);
			pk_party=PartyDetails_sp.getString(IConstant.Partyfragmnet_Pk_party, "");
			fk_group=PartyDetails_sp.getInt(IConstant.Partyfragmnet_fk_group, 0);
		}else
		{
			SharedPreferences PartyDetails_sp=getSharedPreferences(IConstant.Schedule, 0);
			pk_party_user=PartyDetails_sp.getInt(IConstant.Pk_party_user, 0);
			pk_party=PartyDetails_sp.getString(IConstant.Pk_party, "");
			fk_group=PartyDetails_sp.getInt(IConstant.fk_group, 0);
		}
	}
	
	private void returndata() {
		Group readAllPerRelation_group = new Group();
		readAllPerRelation_group.setPk_group(fk_group);
		readpartyInterface.readAllPerRelation(PartyDetailsActivity.this,readAllPerRelation_group);
	}
	
	// 读取聚会详情
	private void initReadParty() {
		Party readparty = new Party();
		readparty.setPk_party(pk_party);
		readpartyInterface.readPartyJoinMsg(PartyDetailsActivity.this,readparty);
	}

	private void initInterface() {
		readpartyInterface = Interface.getInstance();
		readpartyInterface.setPostListener(new updateUserJoinMsgListenner() {

			@Override
			public void success(String A) {
				returndata();
				Log.e("PartyDetailsActivity", "返回的是否更新成功" + A);
			}

			@Override
			public void defail(Object B) {

			}
		});

		readpartyInterface.setPostListener(new readPartyJoinMsgListenner() {

			@Override
			public void success(String A) {
				partakeNumList.clear();
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
						// 判断参与、拒绝数
						Integer relationship = relation.getRelationship();
						switch (relationship) {
						case 4:
							partakeNumList.add(relation);
							break;
						default:
							break;
						}
						//查找当前用户的参与信息
						Integer pk_user=relation.getPk_user();
						if(String.valueOf(pk_user).equals(String.valueOf(sD_pk_user))){
							current_relationship = relationList.get(i).getRelationship();
							if(current_relationship==4){
								mPartyDetails_apply.setText("已参与");
								mPartyDetails_apply_layout.setBackgroundResource(R.drawable.PartyDetails_noapply_layout_color);//已报名背景为淡灰色
							}else{
								mPartyDetails_apply.setText("报名");
								mPartyDetails_apply_layout.setBackgroundResource(R.drawable.PartyDetails_apply_layout_color);//未报名背景为绿色
							}
						}
					}
					Log.e("PartyDetailsActivity", "当前partakeNum的数量"+ partakeNumList.size());
					Log.e("PartyDetailsActivity", "当前not_sayNum的数量"+ not_sayNum);
					mPartyDetails_partake_number.setText(String.valueOf(partakeNumList.size()));// 显示参与数量
					if(partakeNumList.size()>0){
						not_sayNum=PartyDetailsList.size()-partakeNumList.size();
					}else{
						not_sayNum=PartyDetailsList.size();
					}
					mPartyDetails_did_not_say_number.setText(String.valueOf(not_sayNum));// 显示未表态数量
					
					
					List<Party3> partylist=returnData.getParty();
					if(partylist.size()>0){
						Party3 readparty=partylist.get(0);
						Integer pk_user=readparty.getFk_user();
						//查找聚会创建者
						User user = new User();
						user.setPhone(String.valueOf(pk_user));
						readpartyInterface.findUser(PartyDetailsActivity.this,user);
					}
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
		//查找创建者监听
		readpartyInterface.setPostListener(new findUserListenner() {

			@Override
			public void success(String A) {
				Loginback findfriends_statusmsg = GsonUtils.parseJson(A,
						Loginback.class);
				int statusmsg = findfriends_statusmsg.getStatusMsg();
				if (statusmsg == 1) {
					// 取第一个Users[0]
					List<User> Users = findfriends_statusmsg.getReturnData();
					if (Users.size() >= 1) {
						User user = Users.get(0);
						mPartyDetails_party_organizer.setText("组织者: "+user.getNickname());

					}
				} 

			}

			@Override
			public void defail(Object B) {

			}
		});
		
		readpartyInterface.setPostListener(new readAllPerRelationListenner() {

			@Override
			public void success(String A) {
				PartyDetailsList.clear();
				Group_ReadAllUserback group_ReadAllUserback = GsonUtils.parseJson(A, Group_ReadAllUserback.class);
				int status = group_ReadAllUserback.getStatusMsg();
				if (status == 1) {
					Log.e("PartyDetailsActivity", "读取出小组中的所有用户========" + A);
					List<Group_ReadAllUser> allUsers = group_ReadAllUserback.getReturnData();
					if (allUsers.size() > 0) {
						for (int i = 0; i < allUsers.size(); i++) {
							Group_ReadAllUser readAllUser = allUsers.get(i);
							PartyDetailsList.add(readAllUser);
						}
						initReadParty();// 读取聚会详情
					}
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
		
	}

	// 首次进来时传值
	private void initOneParty() {
		Intent intent = getIntent();
		userAll = intent.getBooleanExtra(IConstant.UserAll, false);
		if (userAll) {
			allParty = (UserAllParty) intent.getSerializableExtra(IConstant.UserAllParty);
			pk_party_user = allParty.getPk_party_user();
			pk_party = allParty.getPk_party();
			fk_group = allParty.getFk_group();
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
			mPartyDetails_partyname.setText(allParty.getName());//显示聚会名称
			mPartyDetails_partytime.setText(years + "年" + months + "月" + days + "日"
					+ "  " + week + "  " + hour + ":" + minute);//显示聚会时间
			mPartyDetails_partyaddress.setText(allParty.getLocation());//显示聚会地点
			mPay_type = allParty.getPay_type();//支付类型
			mPay_amount = allParty.getPay_amount();//支付金额
			mPayName = allParty.getName();//聚会名称
			if(1==mPay_type){
				mPartyDetails_partypayment.setText("免费");
			}else {
				mPartyDetails_partypayment.setText("预支付");
			}
			Double latitude = allParty.getLatitude();
			Double longitude = allParty.getLongitude();
			String location = allParty.getLocation();
			if(latitude!=null&longitude!=null)
			{
				mLat = latitude;
				mLng = longitude;
				edit_show.setText(location);
			}else{
				mLat = 24.497572;
				mLng = 118.17276;
			}
			edit_show.setText(location);
		} else {
			oneParty = (Party2) intent.getSerializableExtra(IConstant.OneParty);
			SharedPreferences partydetails_sp=getSharedPreferences(IConstant.Schedule, 0);
			pk_party_user = partydetails_sp.getInt(IConstant.Pk_party_user, 0);
			Log.e("PartyDetailsActivity", "得到的第二个getPk_party_user========="+ pk_party_user);
			pk_party = oneParty.getPk_party();
			fk_group = oneParty.getFk_group();
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
			mPartyDetails_partyname.setText(oneParty.getName());//显示聚会名称
			mPartyDetails_partytime.setText(years + "年" + months + "月" + days + "日"
					+ "  " + week + "  " + hour + ":" + minute);//显示聚会时间
			mPartyDetails_partyaddress.setText(oneParty.getLocation());//显示聚会地点
			mPay_type = oneParty.getPay_type();//支付类型
			mPay_amount = oneParty.getPay_amount();//支付金额
			mPayName = oneParty.getName();//聚会名称
			if(1==mPay_type){
				mPartyDetails_partypayment.setText("免费");
			}else {
				mPartyDetails_partypayment.setText("预支付");
			}
			Double latitude = oneParty.getLatitude();
			Double longitude = oneParty.getLongitude();
			String location = oneParty.getLocation();
			if(longitude!=null)
			{
				mLng = longitude;
			}else{
				mLng=118.17276;
			}
			if(latitude!=null){
				mLat = latitude;
			}else {
				mLat = 24.497572;
			}
			edit_show.setText(location);
		}
	}

	private void initUI() {
		mPartyDetails_partyaddress = (TextView) findViewById(R.id.PartyDetails_partyaddress);// 聚会地址
		mPartyDetails_partyname = (TextView) findViewById(R.id.PartyDetails_partyname);// 聚会名称
		mPartyDetails_partytime = (TextView) findViewById(R.id.PartyDetails_partytime);// 聚会时间
		mPartyDetails_partypayment = (TextView) findViewById(R.id.PartyDetails_partypayment);// 付款方式
		findViewById(R.id.PartyDetails_partake).setOnClickListener(this);// 参与
		findViewById(R.id.PartyDetails_partake_layout).setOnClickListener(this);
		findViewById(R.id.PartyDetails_partake_number_layout).setOnClickListener(this);// 参与数量
		mPartyDetails_partake_number = (TextView) findViewById(R.id.PartyDetails_partake_number);
		findViewById(R.id.PartyDetails_did_not_say).setOnClickListener(this);// 未表态
		findViewById(R.id.PartyDetails_did_not_say_layout).setOnClickListener(this);
		findViewById(R.id.PartyDetails_did_not_say_number_layout).setOnClickListener(this);// 未表态数量
		mPartyDetails_did_not_say_number = (TextView) findViewById(R.id.PartyDetails_did_not_say_number);
		mPartyDetails_party_organizer = (TextView) findViewById(R.id.PartyDetails_party_organizer);// 组织者
		mPartyDetails_apply_layout = (RelativeLayout) findViewById(R.id.PartyDetails_apply_layout);
		mPartyDetails_apply_layout.setOnClickListener(this);// 报名
		mPartyDetails_apply = (TextView) findViewById(R.id.PartyDetails_apply);// 报名支付金额

		edit_show = new EditText(this);
		edit_show.setFocusable(false);
		findViewById(R.id.PartyDetails_back_layout).setOnClickListener(this);// 返回
		findViewById(R.id.PartyDetails_back).setOnClickListener(this);
		findViewById(R.id.PartyDetails_more_layout).setOnClickListener(this);// 设置
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
				Intent intent = new Intent(PartyDetailsActivity.this,BigMapActivity.class);
				intent.putExtra("mLat", mLat);
				intent.putExtra("mLng", mLng);
				Log.e("111", "mLat=="+mLat);
				Log.e("111", "mLng=="+mLng);
				startActivity(intent);
			}

			public boolean onMapPoiClick(MapPoi poi) {
				return false;
			}
		});
		mBaiduMap.setOnMapLongClickListener(new OnMapLongClickListener() {
			public void onMapLongClick(LatLng point) {
				Intent intent = new Intent(PartyDetailsActivity.this,BigMapActivity.class);
				intent.putExtra("mLat", mLat);
				intent.putExtra("mLng", mLng);
				Log.e("222", "mLat=="+mLat);
				Log.e("222", "mLng=="+mLng);
				startActivity(intent);
			}
		});
		mBaiduMap.setOnMapDoubleClickListener(new OnMapDoubleClickListener() {
			public void onMapDoubleClick(LatLng point) {
				Intent intent = new Intent(PartyDetailsActivity.this,BigMapActivity.class);
				intent.putExtra("mLat", mLat);
				intent.putExtra("mLng", mLng);
				Log.e("333", "mLat=="+mLat);
				Log.e("333", "mLng=="+mLng);
				startActivity(intent);

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
		Log.e("PartyDetailsActivity", "地图标点mLat="+mLat+"mLng="+mLng);
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(false);//是否显示当前位置默认图标
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
		case R.id.PartyDetails_partake_layout:
		case R.id.PartyDetails_partake:
		case R.id.PartyDetails_partake_number_layout:
		case R.id.PartyDetails_partake_number:
			PartyDetails_partake_number();
			break;
		case R.id.PartyDetails_did_not_say:
		case R.id.PartyDetails_did_not_say_layout:
		case R.id.PartyDetails_did_not_say_number:
		case R.id.PartyDetails_did_not_say_number_layout:
			PartyDetails_did_not_say_number();
			break;
		case R.id.PartyDetails_apply_layout:
		case R.id.PartyDetails_apply:
			PartyDetails_apply_layout();
		default:
			break;
		}
	}

	// 未表态
	private void PartyDetails_did_not_say_number() {
		Intent intent = new Intent(PartyDetailsActivity.this,CommentsListActivity.class);
		intent.putExtra(IConstant.CommentsList, 3);
		intent.putExtra(IConstant.Not_Say, pk_party);
		intent.putExtra(IConstant.All_fk_group, fk_group);
		startActivity(intent);
	}

	// 参与
	private void PartyDetails_partake_number() {
		Intent intent = new Intent(PartyDetailsActivity.this,CommentsListActivity.class);
		intent.putExtra(IConstant.CommentsList, 4);
		intent.putExtra(IConstant.ParTake, pk_party);
		intent.putExtra(IConstant.All_fk_group, fk_group);
		startActivity(intent);

	}

	private void PartyDetails_apply_layout() {
		if(current_relationship==4)
		{
			if(1==mPay_type){
				final SweetAlertDialog sd = new SweetAlertDialog(PartyDetailsActivity.this,SweetAlertDialog.WARNING_TYPE);
				sd.setTitleText("警告");
				sd.setContentText("你确定要取消报名？");
				sd.setCancelText("我再想想");
				sd.setConfirmText("是的");
				sd.showCancelButton(true);
				sd.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sDialog) {
						sd.cancel();
					}
				}).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sDialog) {
						sd.cancel();
						Party_User party_user = new Party_User();
						party_user.setPk_party_user(pk_party_user);
						party_user.setRelationship(0);
						party_user.setStatus(1);
						party_user.setFk_party(pk_party);
						party_user.setFk_user(sD_pk_user);
						readpartyInterface.updateUserJoinMsg(PartyDetailsActivity.this,party_user);
					}
				}).show();
			}
		}else
		{
			if(1==mPay_type){
				Party_User party_user = new Party_User();
				party_user.setPk_party_user(pk_party_user);
				party_user.setRelationship(4);
				party_user.setStatus(1);
				party_user.setFk_party(pk_party);
				party_user.setFk_user(sD_pk_user);
				readpartyInterface.updateUserJoinMsg(PartyDetailsActivity.this,party_user);
				Log.e("PartyDetailsActivity", "得到的getPk_party_user2222222222"+ pk_party_user);
				Toast();
			}else if (3==mPay_type) {
				Intent intent=new Intent(PartyDetailsActivity.this, PayBaseActivity.class);
				intent.putExtra(IConstant.Paymount, mPay_amount);
				intent.putExtra(IConstant.Payname, mPayName);
				startActivity(intent);
			}
		}
	}

	private void Toast() {
		//自定义Toast
		View toastRoot = getLayoutInflater().inflate(R.layout.my_toast, null);
		Toast toast=new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER, 0, 100);
		toast.setView(toastRoot);
		toast.setDuration(100);
		TextView tv=(TextView)toastRoot.findViewById(R.id.TextViewInfo);
		tv.setText("报名成功");
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
	
	//微信支付报名
	public interface GetWeChatPay{
		void WeChatPay();
	}
	
	//支付宝支付
	public interface GetAliPay{
		void AliPay();
	}
}
