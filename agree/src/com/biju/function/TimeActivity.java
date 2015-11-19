package com.biju.function;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.BJ.utils.DensityUtil;
import com.BJ.utils.KCalendar;
import com.BJ.utils.Weeks;
import com.BJ.utils.KCalendar.OnCalendarClickListener;
import com.BJ.utils.KCalendar.OnCalendarDateChangedListener;
import com.biju.IConstant;
import com.biju.R;

@SuppressLint("SimpleDateFormat")
public class TimeActivity extends Activity implements OnClickListener {

	String date = null;// 设置默认选中的日期 格式为 “2014-04-05” 标准DATE格式
	private KCalendar calendar;
	private TextView popupwindow_calendar_month;
	private TimePicker mTimePicker;
	private TextView mTime_next;
//	private int mHour;
//	private int mMinute;
	private int h;
	private int m;
	private Integer currentMonth_2;
	private Integer oldMonth_2;
	private Integer currentDay_2;
	private Integer oldDay_2;
	private TextView mTimePicker_Time_show;
	private Integer chooseCurrentHour;
	private Integer chooseCurrentMinute;
	private String currentChoose;
	private Intent intent;
	private Integer endCurrentMonth_1;
	private Integer startCurrentMonths_1;
	private Integer endcurrentDay_2;
	private Integer startCurrentDay_2;
	private boolean isNoChoose;
	private String timeString;
	private String partytimeString;
	private String mCurrentYears;
	private String mCurrentMonth;
	private String mCurrentDay;
	private String mCurrentHour;
	private String mCurrentMinute;
	private String week;
	private boolean isTimeChoose;
	private String mNoyears;
	private String mNomonth;
	private String mNoday;
	private String mNoWeek;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time);
		intent = getIntent();
		currentChoose = intent.getStringExtra(IConstant.Time);
		initUI();
		initDate();
		int px2dip = DensityUtil.px2dip(TimeActivity.this, 90);
		Log.e("TimeActivity", "px2dip============="+px2dip);
		if(IConstant.EndTimeChoose.equals(currentChoose)){
			String startTimeString=intent.getStringExtra(IConstant.StartTimeString);
			mTimePicker_Time_show.setText("开始时间:"+startTimeString);
		}
	}

	private void initDate() {
		PPopupWindows();
		SharedPreferences sp = getSharedPreferences("isdate", 0);
		boolean date1 = sp.getBoolean("date", false);
		if (date1) {
			String dateFormat = sp.getString("dateFormat", "");
			calendar.setCalendarDayBgColor(dateFormat,R.drawable.yuan_6,false);//设置背景红色圆圈
			date = dateFormat;// 最后返回给全局 date
			Log.e("TimeActivity", "进保存后的"+date);
			if (null != date) {

				int years = Integer.parseInt(date.substring(0,date.indexOf("-")));
				int month = Integer.parseInt(date.substring(date.indexOf("-") + 1, date.lastIndexOf("-")));
				popupwindow_calendar_month.setText(years + "年" + month + "月");

				calendar.showCalendar(years, month);
				calendar.setCalendarDayBgColor(date,R.drawable.yuan_6,true);//设置背景红色圆圈
			}
			
			SharedPreferences time_sp = getSharedPreferences(IConstant.IsTime, 0);
			boolean IsTimeChoose=time_sp.getBoolean(IConstant.IsTimeChoose, false);
			if(IsTimeChoose){
				String date=time_sp.getString(IConstant.IsCalendar, "");
				String OldYears=date.substring(0, 4);
				String OldMonth=date.substring(5, 7);
				String OldDay=date.substring(8, 10);
				Integer y = Integer.valueOf(OldYears);
				Integer m = Integer.valueOf(OldMonth);
				Integer d = Integer.valueOf(OldDay);
				// 调用计算星期几的方法
				Weeks.CaculateWeekDay(y, m, d);
				String week = Weeks.getweek();
				Integer hour=time_sp.getInt(IConstant.Hour, 0);
				Integer Minute=time_sp.getInt(IConstant.Minute, 0);
				
				mTimePicker.setCurrentHour(hour);
				mTimePicker.setCurrentMinute(Minute);
				mTimePicker_Time_show.setText("开始时间:"+OldYears+"年"+OldMonth+"月"+OldDay+"日"+" "+week+" "+hour+":"+Minute);
			}
		}else {
			SharedPreferences time_sp = getSharedPreferences(IConstant.IsTime, 0);
			isTimeChoose = time_sp.getBoolean(IConstant.IsTimeChoose, false);
			if(isTimeChoose){
				isNoChoose=true;
				mNoyears = time_sp.getString("mCurrentYears", "");
				mCurrentYears=mNoyears;
				mNomonth = time_sp.getString("mCurrentMonth", "");
				mCurrentMonth=mNomonth;
				mNoday = time_sp.getString("mCurrentDay", "");
				mCurrentDay=mNoday;
				mNoWeek = time_sp.getString("week", "");
				week=mNoWeek;
				String timeString=time_sp.getString("TimeString", "");
				mTimePicker_Time_show.setText("开始时间:"+mNoyears+"年"+timeString);
			}else {
				isNoChoose=true;
				SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time=dateFormat.format(new Date());
				Log.e("TimeActivity", "当前时间========="+time);
				mCurrentYears = time.substring(0, 4);
				mCurrentMonth = time.substring(5, 7);
				mCurrentDay = time.substring(8, 10);
				mCurrentHour = time.substring(11, 13);
				mCurrentMinute = time.substring(14, 16);
				// 计算星期几
				int y = Integer.valueOf(mCurrentYears);
				int m = Integer.valueOf(mCurrentMonth);
				int d = Integer.valueOf(mCurrentHour);
				// 调用计算星期几的方法
				Weeks.CaculateWeekDay(y, m, d);
				week = Weeks.getweek();
				mTimePicker_Time_show.setText("开始时间: "+mCurrentYears + "年" + mCurrentMonth + "月"
						+ mCurrentDay + "日" + " " + week + " " +(Integer.valueOf(mCurrentHour)+3) + ":" +mCurrentMinute);
				timeString = mCurrentMonth + "月"+ mCurrentDay + "日" + " " + week + " " +(Integer.valueOf(mCurrentHour)+3) + ":" +mCurrentMinute;
				partytimeString = mCurrentYears+"-"+mCurrentMonth+"-"+mCurrentDay+"  "+(Integer.valueOf(mCurrentHour)+3)+":"+mCurrentMinute;
			}
		}
	}

	private void NoChoose() {
		SharedPreferences time_sp = getSharedPreferences(IConstant.IsTime, 0);
		Editor editor = time_sp.edit();
		editor.putBoolean(IConstant.IsTimeChoose, true);
		editor.putBoolean("isNoChoose", true);
		editor.putString("TimeString", timeString);
		editor.putString("partytimeString", partytimeString);
		editor.putInt(IConstant.Hour, chooseCurrentHour);
		editor.putInt(IConstant.Minute, chooseCurrentMinute);
		editor.putString("mCurrentYears", mCurrentYears);
		editor.putString("mCurrentMonth", mCurrentMonth);
		editor.putString("mCurrentDay", mCurrentDay);
		editor.putString("week", week);
		editor.commit();
		
		finish();
	}
	
	private void initUI() {
		mTimePicker_Time_show = (TextView) findViewById(R.id.TimePicker_Time_show);
		mTimePicker_Time_show.setText("聚会开始时间");
		findViewById(R.id.Time_back_layout).setOnClickListener(this);
		findViewById(R.id.Time_back).setOnClickListener(this);// 返回
		findViewById(R.id.Time_back_layout).setOnClickListener(this);// 下一步
		mTime_next = (TextView) findViewById(R.id.Time_OK);
		mTime_next.setOnClickListener(this);
		mTimePicker = (TimePicker) findViewById(R.id.timePicker);
		Integer currentHour = mTimePicker.getCurrentHour();
		Integer currentMinute = mTimePicker.getCurrentMinute();
		mTimePicker.setIs24HourView(false);
		mTimePicker.setCurrentHour(currentHour+3);
		mTimePicker.setCurrentMinute(currentMinute);
		chooseCurrentHour = mTimePicker.getCurrentHour();
		chooseCurrentMinute = mTimePicker.getCurrentMinute();
		// OnChangeListener buc = new OnChangeListener();
		// mTime_next.setOnClickListener(buc);
		// 是否使用24小时制
		SharedPreferences time_sp = getSharedPreferences(IConstant.IsTime, 0);
		boolean istimechoose = time_sp.getBoolean(IConstant.IsTimeChoose, false);
		mTimePicker.setIs24HourView(false);
		if(istimechoose)
		{
			boolean isNoChoose = time_sp.getBoolean("isNoChoose", false);
			if(isNoChoose){
				Integer hour = time_sp.getInt(IConstant.Hour, 0);
				Integer minute = time_sp.getInt(IConstant.Minute, 0);
				mTimePicker.setCurrentHour(hour);
				mTimePicker.setCurrentMinute(minute);
			}else {
				Integer hour = time_sp.getInt(IConstant.Hour, 0);
				Integer minute = time_sp.getInt(IConstant.Minute, 0);
				mTimePicker.setCurrentHour(hour);
				mTimePicker.setCurrentMinute(minute);
			}
		}
		
		TimeListener times = new TimeListener();
		mTimePicker.setOnTimeChangedListener(times);
	}

	class OnChangeListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			h = mTimePicker.getCurrentHour();
			m = mTimePicker.getCurrentMinute();
			Log.e("TimeActivity", "h:" + h + "   m:" + m);
		}
	}

	class TimeListener implements OnTimeChangedListener {

		/**
		 * view 当前选中TimePicker控件 hourOfDay 当前控件选中TimePicker 的小时 minute
		 * 当前选中控件TimePicker 的分钟
		 */
		@Override
		public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
			Log.e("TimeActivity", "最后选定的h:" + hourOfDay + "最后选定的m:" + minute);
			chooseCurrentHour = hourOfDay;
			chooseCurrentMinute = minute;
			if(date!=null)
			{
				String OldYears=date.substring(0, 4);
				String OldMonth=date.substring(5, 7);
				String OldDay=date.substring(8, 10);
				Integer y = Integer.valueOf(OldYears);
				Integer m = Integer.valueOf(OldMonth);
				Integer d = Integer.valueOf(OldDay);
				// 调用计算星期几的方法
				Weeks.CaculateWeekDay(y, m, d);
				String week = Weeks.getweek();
				if(IConstant.StartTimeChoose.equals(currentChoose)){
					mTimePicker_Time_show.setText("开始时间:"+OldYears+"年"+OldMonth+"月"+OldDay+"日"+" "+week+" "+chooseCurrentHour+":"+chooseCurrentMinute);
				}
			}else
			{
				if(isTimeChoose){
					mTimePicker_Time_show.setText("开始时间: "+mNoyears + "年" + mNomonth + "月"
							+ mNoday + "日" + " " + mNoWeek + " " +chooseCurrentHour + ":" +chooseCurrentMinute);
					timeString = mNomonth + "月"+ mNoday + "日" + " " + mNoWeek + " " +chooseCurrentHour + ":" +chooseCurrentMinute;
					partytimeString = mNoyears+"-"+mNomonth+"-"+mNoday+"  "+chooseCurrentHour+":"+chooseCurrentMinute;
				}else {
					mTimePicker_Time_show.setText("开始时间: "+mCurrentYears + "年" + mCurrentMonth + "月"
							+ mCurrentDay + "日" + " " + week + " " +chooseCurrentHour + ":" +chooseCurrentMinute);
					timeString = mCurrentMonth + "月"+ mCurrentDay + "日" + " " + week + " " +chooseCurrentHour + ":" +chooseCurrentMinute;
					partytimeString = mCurrentYears+"-"+mCurrentMonth+"-"+mCurrentDay+"  "+chooseCurrentHour+":"+chooseCurrentMinute;
				}
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Time_back_layout:
		case R.id.Time_back:
			time_back();
			break;
		case R.id.Time_OK_layout:
		case R.id.Time_OK:
			Time_OK();
			break;
		default:
			break;
		}
	}

	private void Time_OK() {
		if(IConstant.StartTimeChoose.equals(currentChoose)){
			if(isNoChoose){
				NoChoose();
			}else {
				StartTimeChoose();
			}
		}else if(IConstant.EndTimeChoose.equals(currentChoose)){
			EndTimeChoose();
		}else if(IConstant.DeadlineTimeChoose.equals(currentChoose)){
			
		}
	}

	//选择结束时间的判断过程
	private void EndTimeChoose() {
		String StartMonths=intent.getStringExtra(IConstant.StartMonths);
		String StartDay=intent.getStringExtra(IConstant.StartDay);
		Integer StartHour=intent.getIntExtra(IConstant.StartHour, 0);
		if(date!=null){
			String EndMonth=date.substring(5, 7);
			String EndDay=date.substring(8, 10);
			EndMonthsChoose(StartMonths,StartDay,StartHour,EndMonth,EndDay);
		}else {
			SweetAlertDialog sd=new SweetAlertDialog(TimeActivity.this);
			sd.setTitleText("提示");
			sd.setContentText("活动日期不能为空哦~");
			sd.show();
		}
	}

	//从月开始判断
	private void EndMonthsChoose(String startMonths, String startDay,Integer startHour, String endMonth, String endDay) {
		//判断月份是否超过10
		String EndCurrentMonth=endMonth.substring(0);
		if(Integer.valueOf(EndCurrentMonth)>0){
			endCurrentMonth_1 = Integer.valueOf(endMonth);
		}else{
			endCurrentMonth_1=Integer.valueOf(endMonth.substring(1));
		}
				
		String startCurrentMonths=startMonths.substring(0);
		if(Integer.valueOf(startCurrentMonths)>0)
		{
			startCurrentMonths_1 = Integer.valueOf(startMonths);
		}else
		{
			startCurrentMonths_1 = Integer.valueOf(startMonths.substring(1));
		}
		//判断月开始时间与结束时间
		if(endCurrentMonth_1>startCurrentMonths_1){
			//传值完成
			EndOK();
		}else if(endCurrentMonth_1==startCurrentMonths_1){
			EndDayChoose(startDay,startHour,endDay);//判断日
		}else if(endCurrentMonth_1<startCurrentMonths_1){
			SweetAlertDialog();
		}
		
		
	}

	//进行传值
	private void EndOK() {
		SharedPreferences time_sp = getSharedPreferences(IConstant.EndTime, 0);
		Editor editor = time_sp.edit();
		editor.putBoolean(IConstant.IsEndTimeChoose, true);
		editor.putInt(IConstant.EndTimeHour, chooseCurrentHour);
		editor.putInt(IConstant.EndTimeMinute, chooseCurrentMinute);
		editor.putString(IConstant.EndTimeDate, date);
		editor.commit();
		finish();
	}

	private void SweetAlertDialog() {
		SweetAlertDialog sd=new SweetAlertDialog(TimeActivity.this);
		sd.setTitleText("提示");
		sd.setContentText("聚会的结束时间应该是在开始时间之后哦~");
		sd.show();
	}
	//判断日开始时间与结束时间
	private void EndDayChoose(String startDay, Integer startHour, String endDay) {
		//判断日是否超过10
		String EndCurrentDay=endDay.substring(0);
		if(Integer.valueOf(EndCurrentDay)>0)
		{
			endcurrentDay_2 = Integer.valueOf(endDay);
		}else
		{
			endcurrentDay_2=Integer.valueOf(endDay.substring(1));
		}
			
		String startCurrentDay=startDay.substring(0);
		if(Integer.valueOf(startCurrentDay)>0)
		{
			startCurrentDay_2 = Integer.valueOf(startDay);
		}else
		{
			startCurrentDay_2 = Integer.valueOf(startDay.substring(1));
		}
		if(endcurrentDay_2>startCurrentDay_2){
			//传值完成
			EndOK();
		}else if(endcurrentDay_2==startCurrentDay_2){
			EndHourChoose(startHour);
		}else if(endcurrentDay_2<startCurrentDay_2){
			SweetAlertDialog();
		}
	}
	//判断时开始时间与结束时间
	private void EndHourChoose(Integer startHour) {
		Integer chooseCurrentHour = mTimePicker.getCurrentHour();
		if(chooseCurrentHour>Integer.valueOf(startHour)){
			//传值完成
			EndOK();
		}else if(chooseCurrentHour<=Integer.valueOf(startHour)){
			SweetAlertDialog();
		}
	}

	//选择开始时间时的判断过程
	private void StartTimeChoose() {
		if (!(date != null)) {
			SweetAlertDialog sd=new SweetAlertDialog(TimeActivity.this);
			sd.setTitleText("提示");
			sd.setContentText("活动日期不能为空哦~");
			sd.show();
		} else {
			if(chooseCurrentHour==0)
			{
				SweetAlertDialog sd=new SweetAlertDialog(TimeActivity.this);
				sd.setTitleText("提示");
				sd.setContentText("活动时间不能为空哦~");
				sd.show();
			}else
			{
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
				String time=sdf.format(new Date());
				String CurrentYears=time.substring(0, 4);
				String CurrentMonth=time.substring(5, 7);
				String CurrentDay=time.substring(8, 10);
				String CurrentHour=time.substring(11, 13);
				String CurrentMinute=time.substring(14, 16);
				Log.e("TimeActivity", "获取当前的年==========="+CurrentYears);
				Log.e("TimeActivity", "获取当前的月==========="+CurrentMonth);
				Log.e("TimeActivity", "获取当前的日==========="+CurrentDay);
				Log.e("TimeActivity", "获取当前的时==========="+CurrentHour);
				Log.e("TimeActivity", "获取当前的分==========="+CurrentMinute);
				
				String OldYears=date.substring(0, 4);
				String OldMonth=date.substring(5, 7);
				String OldDay=date.substring(8, 10);
				Log.e("TimeActivity", "获取用户选择的年==========="+OldYears);
				Log.e("TimeActivity", "获取用户选择的月==========="+OldMonth);
				Log.e("TimeActivity", "获取用户选择的日==========="+OldDay);
				
				if(Integer.valueOf(OldYears)<Integer.valueOf(CurrentYears))
				{
					OldTime();
					Log.e("TimeActivity", "判断年的时候的=========进入了选择的小于当前的");
				}else
				{
					Log.e("TimeActivity", "判断年的时候的=========进入了选择的大于当前的");
					Month(CurrentMonth, OldMonth);
					//判断当前是月和用户选择的月
					if(oldMonth_2<currentMonth_2)
					{
						Log.e("TimeActivity", "判断月的时候的=========进入了选择的小于当前的");
						OldTime();
					}else if(oldMonth_2==currentMonth_2)
					{

						Log.e("TimeActivity", "判断月的时候的=========进入了选择的等于当前的");
						Day(CurrentDay, OldDay);
						
						if(oldDay_2<currentDay_2)
						{
							Log.e("TimeActivity", "判断日的时候的=========进入了选择的小于当前的");
							OldTime();
						}else if(oldDay_2==currentDay_2)
						{
							Log.e("TimeActivity", "判断日的时候的=========进入了选择的等于当前的");
							if(chooseCurrentHour<(Integer.valueOf(CurrentHour)+3))
							{
								Log.e("TimeActivity", "判断时的时候的=========进入了选择的小于当前的");
								SweetAlertDialog sd=new SweetAlertDialog(TimeActivity.this);
								sd.setTitleText("提示");
								sd.setContentText("聚会开始的时间必须是当前时间的三个小时后哦~");
								sd.show();
							}
							else
							{
								Log.e("TimeActivity", "判断时的时候的=========进入了选择的大于当前的");
								//做完成聚会的过程
								timesp();
							}
						}else
						{
							Log.e("TimeActivity", "判断日的时候的=========进入了选择的大于当前的");
							//做完成聚会的过程
							timesp();
						}
					
					}else
					{
						Log.e("TimeActivity", "判断月的时候的=========进入了选择的大于当前的");
						//做完成聚会的过程
						timesp();
					}
					
				}
			}
		}
	}

	private void Day(String CurrentDay, String OldDay) {
		//判断日是否超过10
		String CurrentDay_1=CurrentDay.substring(0);
		if(Integer.valueOf(CurrentDay_1)>0)
		{
			currentDay_2 = Integer.valueOf(CurrentDay);
		}else
		{
			currentDay_2=Integer.valueOf(CurrentDay.substring(1));
		}
		
		String OldDay_1=OldDay.substring(0);
		if(Integer.valueOf(OldDay_1)>0)
		{
			oldDay_2 = Integer.valueOf(OldDay);
		}else
		{
			oldDay_2 = Integer.valueOf(OldDay.substring(1));
		}
	}

	private void timesp() {
		SharedPreferences time_sp = getSharedPreferences(IConstant.IsTime, 0);
		Editor editor = time_sp.edit();
		editor.putBoolean(IConstant.IsTimeChoose, true);
		editor.putBoolean("isNoChoose", false);
		editor.putInt(IConstant.Hour, chooseCurrentHour);
		editor.putInt(IConstant.Minute, chooseCurrentMinute);
		editor.putString(IConstant.IsCalendar, date);
		editor.commit();
		
		SharedPreferences sp = getSharedPreferences("isdate", 0);
		Editor editor1 = sp.edit();
		editor1.putString("dateFormat", date);
		editor1.putBoolean("date", true);
		editor1.commit();
		
		finish();
	}

	private void Month(String CurrentMonth, String OldMonth) {
		//判断月份是否超过10
		String CurrentMonth_1=CurrentMonth.substring(0);
		if(Integer.valueOf(CurrentMonth_1)>0)
		{
			currentMonth_2 = Integer.valueOf(CurrentMonth);
		}else
		{
			currentMonth_2=Integer.valueOf(CurrentMonth.substring(1));
		}
		
		String OldMonth_1=OldMonth.substring(0);
		if(Integer.valueOf(OldMonth_1)>0)
		{
			oldMonth_2 = Integer.valueOf(OldMonth);
		}else
		{
			oldMonth_2 = Integer.valueOf(OldMonth.substring(1));
		}
	}

	private void OldTime() {
		SweetAlertDialog sd=new SweetAlertDialog(TimeActivity.this);
		sd.setTitleText("提示");
		sd.setContentText("不能选择过去的时间哦~");
		sd.show();
	}

	private void time_back() {
		finish();
	}

	private void PPopupWindows() {
		popupwindow_calendar_month = (TextView) findViewById(R.id.popupwindow_calendar_month);
		calendar = (KCalendar) findViewById(R.id.popupwindow_calendar);

		popupwindow_calendar_month.setText(calendar.getCalendarYear() + "年"+ calendar.getCalendarMonth() + "月");

		if (null != date) {
			int years = Integer.parseInt(date.substring(0, date.indexOf("-")));
			int month = Integer.parseInt(date.substring(date.indexOf("-") + 1,date.lastIndexOf("-")));
			popupwindow_calendar_month.setText(years + "年" + month + "月");

			calendar.showCalendar(years, month);
			calendar.setCalendarDayBgColor(date,R.drawable.yuan_6,true);
		}

		List<String> list = new ArrayList<String>(); // 设置标记列表
		list.add("2014-04-01");
		list.add("2014-04-02");
		calendar.addMarks(list, 0);

		// 监听所选中的日期
		calendar.setOnCalendarClickListener(new OnCalendarClickListener() {

			public void onCalendarClick(int row, int col, String dateFormat) {
				int month = Integer.parseInt(dateFormat.substring(
						dateFormat.indexOf("-") + 1,
						dateFormat.lastIndexOf("-")));

				if (calendar.getCalendarMonth() - month == 1// 跨年跳转
						|| calendar.getCalendarMonth() - month == -11) {
					calendar.lastMonth();

				} else if (month - calendar.getCalendarMonth() == 1 // 跨年跳转
						|| month - calendar.getCalendarMonth() == -11) {
					calendar.nextMonth();

				} else {
					calendar.removeAllBgColor();
					calendar.setCalendarDayBgColor(dateFormat,R.drawable.yuan_6,true);
					date = dateFormat;// 最后返回给全局 date
					// Log.e("date", "date=========" + date);
					Log.e("date", "date=========" + date);
					
					String OldYears=date.substring(0, 4);
					String OldMonth=date.substring(5, 7);
					String OldDay=date.substring(8, 10);
					Integer y = Integer.valueOf(OldYears);
					Integer m = Integer.valueOf(OldMonth);
					Integer d = Integer.valueOf(OldDay);
					// 调用计算星期几的方法
					Weeks.CaculateWeekDay(y, m, d);
					String week = Weeks.getweek();
					if(IConstant.StartTimeChoose.equals(currentChoose)){
						mTimePicker.setCurrentHour(chooseCurrentHour);
						mTimePicker.setCurrentMinute(chooseCurrentMinute);
						mTimePicker_Time_show.setText("开始时间:"+OldYears+"年"+OldMonth+"月"+OldDay+"日"+" "+week+" "+chooseCurrentHour+":"+chooseCurrentMinute);
					}
					isNoChoose=false;
					SharedPreferences sp = getSharedPreferences("isdate", 0);
					Editor editor = sp.edit();
					editor.putString("dateFormat", dateFormat);
					editor.putBoolean("date", true);
					editor.commit();
				}
			}
		});

		// 监听当前月份
		calendar.setOnCalendarDateChangedListener(new OnCalendarDateChangedListener() {
			public void onCalendarDateChanged(int year, int month) {
				popupwindow_calendar_month.setText(year + "年" + month + "月");
			}
		});

		// 上月监听按钮
		RelativeLayout popupwindow_calendar_last_month = (RelativeLayout) findViewById(R.id.popupwindow_calendar_last_month);
		popupwindow_calendar_last_month
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						calendar.lastMonth();
					}

				});

		// 下月监听按钮
		RelativeLayout popupwindow_calendar_next_month = (RelativeLayout) findViewById(R.id.popupwindow_calendar_next_month);
		popupwindow_calendar_next_month
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						calendar.nextMonth();
					}
				});
	}

}
