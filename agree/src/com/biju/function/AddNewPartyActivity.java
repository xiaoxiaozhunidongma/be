package com.biju.function;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.BJ.javabean.ImageText;
import com.BJ.javabean.MapAddParty;
import com.BJ.javabean.Party;
import com.BJ.javabean.PartyOkback;
import com.BJ.utils.ByteOrBitmap;
import com.BJ.utils.LimitLong;
import com.BJ.utils.Path2Bitmap;
import com.BJ.utils.SdPkUser;
import com.BJ.utils.Weeks;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.alibaba.sdk.android.oss.storage.OSSBucket;
import com.alibaba.sdk.android.oss.storage.OSSData;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.addPartyListenner;
import com.biju.R;
import com.biju.APP.MyApplication;
import com.biju.pay.CostActivity;
import com.biju.pay.GraphicDetailsActivity;
import com.biju.pay.LimitNumberActivity;
import com.biju.wechatshare.WEChatShaerActivity;
import com.github.volley_examples.utils.GsonUtils;

public class AddNewPartyActivity extends Activity implements OnClickListener {

	private AddNewPartyActivity context;
	private RelativeLayout mAdd_New_Party_back_layout;
	private TextView mAdd_New_Party_back;
	private EditText mAdd_New_Party_name;
	private RelativeLayout mAdd_New_Party_time;
	private RelativeLayout mAdd_New_Party_address;
	private TextView mAdd_New_Party_time_details;
	private TextView mAdd_New_Party_address_details;

	private double mLng;
	private double mLat;
	private Integer fk_group;
	private Integer sD_pk_user;
	private String isCalendar;
	private String address;
	private Interface addNewParty_Interface;
	
	private String partyname;
	private Integer hour;
	private Integer minute;

	private int TotalCount1=0;
	private int TotalCount2=0;
	private int TotalCount3=0;
	private boolean source;
	private TextView mAdd_New_Party_activity_cost;
	private TextView mAdd_New_Party_end_time;
	private TextView mAdd_New_Party_registration_deadline;
	private TextView mAdd_New_Party_limit_number;
	private TextView mAdd_New_Party_details;
	private String months;
	private String days;
	private Integer endtimehour;
	private Integer endtimeminute;
	private String startTime;
	
	private List<ImageText> GraphicDetailsList=new ArrayList<ImageText>();
	private List<ImageText> GraphicDetailsList2=new ArrayList<ImageText>();
	private List<ImageText> ImageDetailsList=new ArrayList<ImageText>();
	private List<String> objectKeyList=new ArrayList<String>();
	private OSSData ossData;
	private OSSService ossService;
	private OSSBucket sampleBucket;
	private byte[] bitmap2Bytes;
	private String uUid;
	private float Pay_amount=0;
	private Integer type=1;
	private int reUploadNum=3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_party);
		context=this;
		sD_pk_user = SdPkUser.getsD_pk_user();
		Intent intent = getIntent();
		fk_group = intent.getIntExtra(IConstant.Fk_group, 0);
		source = intent.getBooleanExtra(IConstant.IsSchedule, false);
		initUI();
		initInterface();
	}

	@Override
	protected void onRestart() {
		initCost();
		initLimitNumber();
		initGraphicDetails();
		super.onRestart();
	}
	
	private void initGraphicDetails() {
		SharedPreferences GraphicDetails_sp=getSharedPreferences("GraphicDetails", 0);
		boolean IsGraphicDetails=GraphicDetails_sp.getBoolean("IsGraphicDetails", false);
		if(IsGraphicDetails){
			// 获取ossService和sampleBucket
			ossService = MyApplication.getOssService();
			sampleBucket = MyApplication.getSampleBucket();
			Integer GraphicDetailsNumber=GraphicDetails_sp.getInt("GraphicDetailsNumber", 0);
			mAdd_New_Party_details.setText(GraphicDetailsNumber+"个图文内容");
		}
	}

	private String uniqueId;
	private String partytimeString;
	private boolean isNoChoose;
	private String party_name;
	private boolean ischoose;
	private Button mAdd_New_Party_OK;
	//查表
	private void initDB() {
		GraphicDetailsList = new Select().from(ImageText.class).execute();
		Log.e("AddNewPartyActivity", "GraphicDetailsList的长度======"+GraphicDetailsList.size());
		ImageDetailsList.clear();//先清空
		for (int i = 0; i < GraphicDetailsList.size(); i++) {
			ImageText imageText=GraphicDetailsList.get(i);
			Integer type=imageText.getType();
			Log.e("AddNewPartyActivity", "GraphicDetailsList的长度======"+GraphicDetailsList.size());
			if(2==type){
				ImageDetailsList.add(imageText);
			}
		}
		Log.e("AddNewPartyActivity", "ImageDetailsList的长度======"+ImageDetailsList.size());
		if(ImageDetailsList.size()>0){
			ImageOSSQueue();//然后进行图片上传
		}else {
			FoundParty();//直接进行聚会的创建
		}
	}

	private void FoundParty() {
		String fk_party=AddNewPartyActivity.getMyUUID();
		PartyComplete(fk_party);
	}
	//队列模式上传
	private void ImageOSSQueue() {
		if(ImageDetailsList.size()>0){
			ImageText text=ImageDetailsList.get(0);
			String imagePath=text.getImage_path();
			String fk_party = text.getFk_party();
			Bitmap convertToBitmap;
			try {
				convertToBitmap = Path2Bitmap.convertToBitmap(imagePath);
				Bitmap limitLongScaleBitmap = LimitLong.limitLongScaleBitmap(convertToBitmap, 1280);// 最长边限制为1280
				bitmap2Bytes = ByteOrBitmap.Bitmap2Bytes(limitLongScaleBitmap);
				UUID uuid = UUID.randomUUID();
				uniqueId = uuid.toString();
				OSSupload(ossData, bitmap2Bytes, uniqueId,imagePath,fk_party);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void OSSupload(OSSData ossData, byte[] bitmap2Bytes2, final String uniqueId, final String imagePath, final String fk_party) {
		ossData = ossService.getOssData(sampleBucket, uniqueId);
		ossData.setData(bitmap2Bytes2, "jpg"); // 指定需要上传的数据和它的类型
		ossData.enableUploadCheckMd5sum(); // 开启上传MD5校验
		ossData.uploadInBackground(new SaveCallback() {
			@Override
			public void onSuccess(String objectKey) {
				Log.e("AddNewPartyActivity", "图片上传成功"+ImageDetailsList.size());
				Log.e("AddNewPartyActivity", "objectKey==" + objectKey);
					objectKeyList.add(objectKey);
					ImageText imageText2=new Select().from(ImageText.class).where("image_path=?", imagePath).executeSingle();
					imageText2.setImage_path(objectKey);
					imageText2.save();
					reUploadNum=3;//如果上传成功,恢复次数为3
					
					ImageDetailsList.remove(0);//删除第一个
					Log.e("AddNewPartyActivity", "成功：ImageDetailsList.size()=="+ImageDetailsList.size());
					if(ImageDetailsList.size()>0){
						ImageOSSQueue();//递归模式调用自身
					}else{
						//第二次查表
						GraphicDetailsList2.clear();
						GraphicDetailsList2 = new Select().from(ImageText.class).execute();
						PartyComplete(fk_party);
					}
			}

			@Override
			public void onProgress(String objectKey, int byteCount,
					int totalSize) {
			}

			@Override
			public void onFailure(String objectKey, OSSException ossException) {
				Log.e("AddNewPartyActivity", "图片上传失败" + ossException.toString());
				Log.e("AddNewPartyActivity", "失败：ImageDetailsList.size()=="+ImageDetailsList.size());
				Log.e("AddNewPartyActivity", "图片上传失败objectKey==" + objectKey);
				//失败时候 自动重新上传
				if(ImageDetailsList.size()>0){
					if(reUploadNum>0){
						ImageOSSQueue();//递归模式调用自身
						reUploadNum--;
					}
				}
			}
		});
	
	}

	private void initLimitNumber() {
		SharedPreferences Limit_sp=getSharedPreferences(IConstant.LimitNumber, 0);
		boolean isNumber=Limit_sp.getBoolean(IConstant.IsNumber, false);
		if(isNumber){
			String limitnumber=Limit_sp.getString(IConstant.Number, "");
			if("0".equals(limitnumber)){
				mAdd_New_Party_limit_number.setText("不限");
			}else {
				mAdd_New_Party_limit_number.setText(limitnumber+"人");
			}
		}
	}

	private void initCost() {
		SharedPreferences CostSp=getSharedPreferences(IConstant.Cost, 0);
		boolean iscost=CostSp.getBoolean(IConstant.IsCost, false);
		if(iscost){
			String CurrentPayMoney=CostSp.getString(IConstant.PayMoney, "");
			if("0".equals(CurrentPayMoney)){
				mAdd_New_Party_activity_cost.setText("免费");
				Pay_amount=0;
				type=1;
			}else {
				mAdd_New_Party_activity_cost.setText(CurrentPayMoney+"元");
				Pay_amount=Float.valueOf(CurrentPayMoney);
				type=3;
			}
		}
	}

	private void initInterface() {
		addNewParty_Interface = Interface.getInstance();
		addNewParty_Interface.setPostListener(new addPartyListenner() {

			@Override
			public void success(String A) {
				Log.e("AddNewPartyActivity", "日程是否创建成功======" + A);
				PartyOkback partyOkback = GsonUtils.parseJson(A,PartyOkback.class);
				Integer status = partyOkback.getStatusMsg();
				if (status == 1) {
					Log.e("AddNewPartyActivity", "日程是否创建成功======" + A);
					SharedPreferences refresh_sp=getSharedPreferences(IConstant.AddRefresh, 0);
					Editor editor2=refresh_sp.edit();
					editor2.putBoolean(IConstant.IsAddRefresh, true);
					editor2.commit();
					Intent intent=new Intent(AddNewPartyActivity.this, WEChatShaerActivity.class);
					intent.putExtra("Source", source);
					intent.putExtra("Party_name", party_name);
					startActivity(intent);
					mAdd_New_Party_OK.setEnabled(true);
					OKToast();
					finish();
				}
			}

			@Override
			public void defail(Object B) {
				finish();
			}
		});
	}

	private void OKToast() {
		//自定义Toast
		View toastRoot = getLayoutInflater().inflate(R.layout.my_toast, null);
		Toast toast=new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(toastRoot);
		toast.setDuration(100);
		TextView tv=(TextView)toastRoot.findViewById(R.id.TextViewInfo);
		tv.setText("创建成功");
		toast.show();
	}
	
	@Override
	protected void onStart() {
		initAddress();
		initTime();
		initEndTime();
		super.onStart();
	}
	//获取结束时间
	private void initEndTime() {
		SharedPreferences endtime_sp = getSharedPreferences(IConstant.EndTime, 0);
		boolean isEndTimeChoose=endtime_sp.getBoolean(IConstant.IsEndTimeChoose, false);
		if(isEndTimeChoose){
			String EndCalendar=endtime_sp.getString(IConstant.EndTimeDate, "");
			String years = EndCalendar.substring(0, 4);
			String months = EndCalendar.substring(5, 7);
			String days = EndCalendar.substring(8, 10);
			// 计算星期几
			int y = Integer.valueOf(years);
			int m = Integer.valueOf(months);
			int d = Integer.valueOf(days);
			// 调用计算星期几的方法
			Weeks.CaculateWeekDay(y, m, d);
			String week = Weeks.getweek();
			endtimehour = endtime_sp.getInt(IConstant.EndTimeHour, 0);
			endtimeminute = endtime_sp.getInt(IConstant.EndTimeMinute, 0);
			if(endtimehour<10)
			{
				if(endtimeminute<10)
				{
					mAdd_New_Party_end_time.setText(years + "年" + months + "月"
							+ days + "日" + " " + week + " " +"0"+endtimehour + ":" +"0"+endtimeminute);
				}else
				{
					mAdd_New_Party_end_time.setText(years + "年" + months + "月"
							+ days + "日" + " " + week + " " +"0"+endtimehour + ":" + endtimeminute);
				}
			}else
			{
				if(endtimeminute<10)
				{
					mAdd_New_Party_end_time.setText(years + "年" + months + "月"
							+ days + "日" + " " + week + " " + endtimehour + ":" +"0"+endtimeminute);
				}else
				{
					mAdd_New_Party_end_time.setText(years + "年" + months + "月"
							+ days + "日" + " " + week + " " + endtimehour + ":" + endtimeminute);
				}
			}
		}
	}

	// 得到pk_party
	public static String getMyUUID() {
		UUID uuid = UUID.randomUUID();
		String uniqueId = uuid.toString();
		return uniqueId;
	}

	// 获取返回来的时间
	private void initTime() {
		// 得到年月日和选的时间
		SharedPreferences time_sp = getSharedPreferences(IConstant.IsTime, 0);
		boolean istimechoose = time_sp.getBoolean(IConstant.IsTimeChoose, false);
		if (istimechoose) {
			isNoChoose = time_sp.getBoolean("isNoChoose", false);
			if(isNoChoose){
				String timeString=time_sp.getString("TimeString", "");
				partytimeString = time_sp.getString("partytimeString", "");
				mAdd_New_Party_time_details.setText(timeString);
			}else {
				isCalendar = time_sp.getString(IConstant.IsCalendar, "");
				String years = isCalendar.substring(0, 4);
				months = isCalendar.substring(5, 7);
				days = isCalendar.substring(8, 10);
				// String times = years + "年" + months + "月" + days + "日";
				// 计算星期几
				int y = Integer.valueOf(years);
				int m = Integer.valueOf(months);
				int d = Integer.valueOf(days);
				// 调用计算星期几的方法
				Weeks.CaculateWeekDay(y, m, d);
				String week = Weeks.getweek();
				hour = time_sp.getInt(IConstant.Hour, 0);
				Log.e("AddNewPartyActivity", "所得到的时间==========="+hour);
				minute = time_sp.getInt(IConstant.Minute, 0);
				if(hour<10)
				{
					if(minute<10)
					{
						mAdd_New_Party_time_details.setText(months + "月"
								+ days + "日" + " " + week + " " +"0"+hour + ":" +"0"+minute);
					}else
					{
						mAdd_New_Party_time_details.setText(months + "月"
								+ days + "日" + " " + week + " " +"0"+hour + ":" + minute);
					}
				}else
				{
					if(minute<10)
					{
						mAdd_New_Party_time_details.setText(months + "月"
								+ days + "日" + " " + week + " " + hour + ":" +"0"+minute);
					}else
					{
						mAdd_New_Party_time_details.setText(months + "月"
								+ days + "日" + " " + week + " " + hour + ":" + minute);
					}
				}
				startTime = years + "年" + months + "月"+ days + "日" + " " + week + " " + hour + ":" + minute;
			}
		}
	}

	// 获取返回来的地址
	private void initAddress() {
		SharedPreferences map_sp = getSharedPreferences(IConstant.IsMap, 0);
		ischoose = map_sp.getBoolean(IConstant.IsMapChoose, false);
		if (ischoose) {
			address = map_sp.getString(IConstant.IsAddress, "");
			float mLng_1 = map_sp.getFloat(IConstant.MLng, 0);
			float mLat_1 = map_sp.getFloat(IConstant.MLat, 0);
			mLng = Double.valueOf(mLng_1);
			mLat = Double.valueOf(mLat_1);
			Log.e("AddNewPartyActivity", "获取回来的mLng========"+mLng);
			Log.e("AddNewPartyActivity", "获取回来的mLat========"+mLat);
			mAdd_New_Party_address_details.setText(address);
		}
	}

	private void initUI() {
		mAdd_New_Party_back_layout = (RelativeLayout) findViewById(R.id.Add_New_Party_back_layout);
		mAdd_New_Party_back = (TextView) findViewById(R.id.Add_New_Party_back);// 返回
		mAdd_New_Party_OK = (Button) findViewById(R.id.Add_New_Party_OK);
		
		mAdd_New_Party_back_layout.setOnClickListener(this);
		mAdd_New_Party_back.setOnClickListener(this);
		mAdd_New_Party_OK.setOnClickListener(this);
		
		mAdd_New_Party_name = (EditText) findViewById(R.id.Add_New_Party_name);// 聚会名称
		mAdd_New_Party_time = (RelativeLayout) findViewById(R.id.Add_New_Party_time);// 聚会时间
		mAdd_New_Party_address = (RelativeLayout) findViewById(R.id.Add_New_Party_address);// 聚会地点
		mAdd_New_Party_time.setOnClickListener(this);
		mAdd_New_Party_address.setOnClickListener(this);
		mAdd_New_Party_time_details = (TextView) findViewById(R.id.Add_New_Party_time_details);// 时间详情
		mAdd_New_Party_address_details = (TextView) findViewById(R.id.Add_New_Party_address_details);// 地址详情
	
		findViewById(R.id.Add_New_Party_activity_cost_layout).setOnClickListener(this);//活动花费
		mAdd_New_Party_activity_cost = (TextView) findViewById(R.id.Add_New_Party_activity_cost);
		findViewById(R.id.Add_New_Party_end_time_layout).setOnClickListener(this);//活动结束时间
		mAdd_New_Party_end_time = (TextView) findViewById(R.id.Add_New_Party_end_time);
		findViewById(R.id.Add_New_Party_registration_deadline_layout).setOnClickListener(this);//报名截止时间
		mAdd_New_Party_registration_deadline = (TextView) findViewById(R.id.Add_New_Party_registration_deadline);
		findViewById(R.id.Add_New_Party_limit_number_layout).setOnClickListener(this);//限制人数
		mAdd_New_Party_limit_number = (TextView) findViewById(R.id.Add_New_Party_limit_number);
		findViewById(R.id.Add_New_Party_details_layout).setOnClickListener(this);//编辑详情
		mAdd_New_Party_details = (TextView) findViewById(R.id.Add_New_Party_details);
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Add_New_Party_back_layout:
		case R.id.Add_New_Party_back:
			Add_New_Party_back();
			break;
		case R.id.Add_New_Party_OK:
			Add_New_Party_OK();
			break;
		case R.id.Add_New_Party_time:
			Add_New_Party_time();
			break;
		case R.id.Add_New_Party_address:
			Add_New_Party_address();
			break;
		case R.id.Add_New_Party_activity_cost_layout:
			Add_New_Party_activity_cost_layout();
			break;
		case R.id.Add_New_Party_end_time_layout:
			Add_New_Party_end_time_layout();
			break;
		case R.id.Add_New_Party_registration_deadline_layout:
			Add_New_Party_registration_deadline_layout();
			break;
		case R.id.Add_New_Party_limit_number_layout:
			Add_New_Party_limit_number_layout();
			break;
		case R.id.Add_New_Party_details_layout:
			Add_New_Party_details_layout();
			break;
		default:
			break;
		}
	}

	//图文详情
	private void Add_New_Party_details_layout() {
		Intent intent=new Intent(AddNewPartyActivity.this, GraphicDetailsActivity.class);
		startActivity(intent);
	}

	//限制人数
	private void Add_New_Party_limit_number_layout() {
		Intent intent=new Intent(AddNewPartyActivity.this, LimitNumberActivity.class);
		startActivity(intent);
	}

	//报名截止时间
	private void Add_New_Party_registration_deadline_layout() {
		
	}

	//活动结束时间
	private void Add_New_Party_end_time_layout() {
		// 得到年月日和选的时间
		String StartTime=mAdd_New_Party_time_details.getText().toString().trim();
		if(!("".equals(StartTime))){
			Intent intent = new Intent(AddNewPartyActivity.this, TimeActivity.class);
			intent.putExtra(IConstant.Time, IConstant.EndTimeChoose);
			intent.putExtra(IConstant.StartTimeString, startTime);
			intent.putExtra(IConstant.StartMonths, months);
			intent.putExtra(IConstant.StartDay, days);
			intent.putExtra(IConstant.StartHour, hour);
			startActivity(intent);
			Log.e("AddNewPartyActivity", "要传的月，日，时==========="+months+"  "+days+"   "+hour);
		}else{
			//弹出对话框
			SweetAlertDialog();
		}
	}

	private void SweetAlertDialog() {
		final SweetAlertDialog sd = new SweetAlertDialog(AddNewPartyActivity.this);
		sd.setTitleText("提示");
		sd.setContentText("在输入结束时间和截止时间之前,应该先"+"\n"+"输入活动的开始时间哦~");
		sd.setConfirmText("好的");
		sd.showCancelButton(true);
		sd.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sDialog) {
				sd.cancel();
			}
		}).show();
	}

	//活动花费
	private void Add_New_Party_activity_cost_layout() {
		Intent intent=new Intent(AddNewPartyActivity.this, CostActivity.class);
		startActivity(intent);
	}

	// 聚会地点
	private void Add_New_Party_address() {
		Intent intent = new Intent(AddNewPartyActivity.this, MapActivity.class);
		startActivity(intent);
	}

	// 聚会时间
	private void Add_New_Party_time() {
		Intent intent = new Intent(AddNewPartyActivity.this, TimeActivity.class);
		intent.putExtra(IConstant.Time, IConstant.StartTimeChoose);
		startActivity(intent);
	}

	private void TotalCount(String Ems) {
		Pattern p1 = Pattern.compile("[0-9]*"); 
		Matcher m1 = p1.matcher(Ems);
		if(m1.matches()){
			Log.e("AddNewPartyActivity", "输入的是数字===========");
			TotalCount1=TotalCount1+1;
		}else{
			Pattern p2=Pattern.compile("[a-zA-Z]");
			Matcher m2=p2.matcher(Ems);
			if(m2.matches()){
				Log.e("AddNewPartyActivity", "输入的是字母===========");
				TotalCount2=TotalCount2+1;
			}else{
				Pattern p3=Pattern.compile("[\u4e00-\u9fa5]");
				Matcher m3=p3.matcher(Ems);
				if(m3.matches()){
					Log.e("AddNewPartyActivity", "输入的是汉字===========");
					TotalCount3=TotalCount3+2;
				}
			}
		}
	}
	
	
	// 完成
	private void Add_New_Party_OK() {
		initDB();
		mAdd_New_Party_OK.setEnabled(false);
		
	}

	private void PartyComplete(String fk_party) {
		String ems=mAdd_New_Party_name.getText().toString().trim();
		if("".equals(ems)){
		}else{
			TotalCount1=0;
			TotalCount2=0;
			TotalCount3=0;
			for (int i = 0; i < ems.length(); i++) {
				String Ems=String.valueOf(ems.charAt(i));
				TotalCount(Ems);
			}
		}
		int TotalCount=TotalCount1+TotalCount2+TotalCount3;
		if(TotalCount<2){
			SweetAlerDialog();
		}else{
			party_name = mAdd_New_Party_name.getText().toString().trim();
			Party party = new Party();
			party.setPk_party(fk_party);
			party.setFk_group(fk_group);
			party.setFk_user(sD_pk_user);
			party.setName(party_name);
			party.setRemark("sd");
			if(isNoChoose){
				party.setBegin_time(partytimeString);
			}else {
				party.setBegin_time(isCalendar + "   " + hour + ":" + minute);
			}
			party.setLongitude(mLng);
			party.setLatitude(mLat);
			party.setLocation(address);
			party.setStatus(1);
			party.setPay_type(type);
			party.setPay_amount(Pay_amount);
			party.setPay_fk_user(sD_pk_user);
			party.setParty_interval(1);
			Log.e("AddNewPartyActivity", "新建日程的UUID=====" + uUid);
			Log.e("AddNewPartyActivity", "新建日程的name=====" + party_name);
			Log.e("AddNewPartyActivity", "新建日程的fk_group=====" + fk_group);
			Log.e("AddNewPartyActivity", "新建日程的时间=====" + isCalendar + "    " + hour+ ":" + minute);
			Log.e("AddNewPartyActivity", "新建日程的mLng=====" + mLng);
			Log.e("AddNewPartyActivity", "新建日程的mLat=====" + mLat);
			Log.e("AddNewPartyActivity", "新建日程的地址=====" + address);
			addNewParty_Interface.addParty(AddNewPartyActivity.this, new MapAddParty(GraphicDetailsList2, party));
			Log.e("PartyComplete~", "GraphicDetailsList.size="+GraphicDetailsList.size());
			SdPkUser.setGetpk_party(fk_party);
		}
	}

	private void SweetAlerDialog() {
		SweetAlertDialog sd=new SweetAlertDialog(context);
		sd.setTitleText("提示");
		sd.setContentText("活动名称不能为空或者小于2个字符哦~");
		sd.show();
	}

	// 返回
	private void Add_New_Party_back() {
		finish();
		//清楚数据库,无条件全部删除
		new Delete().from(ImageText.class).execute();
	}

	@Override
	protected void onStop() {
		// 复原是否进入过地图选择
		SharedPreferences map_sp = getSharedPreferences(IConstant.IsMap, 0);
		Editor editor = map_sp.edit();
		editor.putBoolean(IConstant.IsMapChoose, false);
		editor.commit();
		
		// 复原是否进入过时间选择
		SharedPreferences time_sp = getSharedPreferences(IConstant.IsTime, 0);
		Editor editor1 = time_sp.edit();
		editor1.putBoolean(IConstant.IsTimeChoose, false);
		editor1.commit();
		
		//复原选过日期后的
		SharedPreferences date_sp = getSharedPreferences("isdate", 0);
		Editor editor3 = date_sp.edit();
		editor3.putBoolean("date", false);
		editor3.commit();
		
		// 复原是否进入过结束时间选择
		SharedPreferences endtime_sp = getSharedPreferences(IConstant.EndTime, 0);
		Editor endtimeeditor = endtime_sp.edit();
		endtimeeditor.putBoolean(IConstant.IsEndTimeChoose, false);
		endtimeeditor.commit();
		
		//是否进入过限制人数的限制
		SharedPreferences Limit_sp=getSharedPreferences(IConstant.LimitNumber, 0);
		Editor Limit_editor=Limit_sp.edit();
		Limit_editor.putBoolean(IConstant.IsNumber, false);
		Limit_editor.commit();
		
		//是否进入过金额界面
		SharedPreferences CostSp=getSharedPreferences(IConstant.Cost, 0);
		Editor Costeditor=CostSp.edit();
		Costeditor.putBoolean(IConstant.IsCost, false);
		Costeditor.commit();
		
		//是否进入过图文信息界面
		SharedPreferences GraphicDetails_sp=getSharedPreferences("GraphicDetails", 0);
		Editor GraphicDetails_editor=GraphicDetails_sp.edit();
		GraphicDetails_editor.putBoolean("IsGraphicDetails", false);
		GraphicDetails_editor.commit();
		super.onStop();
	}
	
}
