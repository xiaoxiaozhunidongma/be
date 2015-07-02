package com.biju.function;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
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
import android.widget.Toast;

import com.BJ.utils.KCalendar;
import com.BJ.utils.KCalendar.OnCalendarClickListener;
import com.BJ.utils.KCalendar.OnCalendarDateChangedListener;
import com.biju.R;

public class TimeActivity extends Activity implements OnClickListener {

	String date = null;// 设置默认选中的日期 格式为 “2014-04-05” 标准DATE格式
	private KCalendar calendar;
	private TextView popupwindow_calendar_month;
	private TimePicker mTimePicker;
	private TextView mTime_next;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time);
		initUI();
		initDate();
	}

	private void initDate() {
		PPopupWindows();
		SharedPreferences sp = getSharedPreferences("isdate", 0);
		boolean date1 = sp.getBoolean("date", false);
		if (date1) {
			String dateFormat = sp.getString("dateFormat", "");
			calendar.setCalendarDayBgColor(dateFormat,
					R.drawable.calendar_date_focused);
			date = dateFormat;// 最后返回给全局 date
			Log.e("MainActivity", "进保存后的");
			if (null != date) {

				int years = Integer.parseInt(date.substring(0,
						date.indexOf("-")));
				int month = Integer.parseInt(date.substring(
						date.indexOf("-") + 1, date.lastIndexOf("-")));
				popupwindow_calendar_month.setText(years + "年" + month + "月");

				calendar.showCalendar(years, month);
				calendar.setCalendarDayBgColor(date,
						R.drawable.calendar_date_focused);
			}
		}
	}

	private void initUI() {
		findViewById(R.id.time_back_layout).setOnClickListener(this);
		findViewById(R.id.time_back).setOnClickListener(this);// 返回
		findViewById(R.id.time_next_layout).setOnClickListener(this);// 下一步
		mTime_next = (TextView) findViewById(R.id.time_next);
		mTime_next.setOnClickListener(this);
		mTimePicker = (TimePicker) findViewById(R.id.timePicker);
		OnChangeListener  buc=new OnChangeListener();  
		mTime_next.setOnClickListener(buc); 
		//是否使用24小时制  
		mTimePicker.setIs24HourView(true);  
        TimeListener times=new TimeListener();  
        mTimePicker.setOnTimeChangedListener(times);
	}
	
	class OnChangeListener implements OnClickListener{  
        @Override  
        public void onClick(View v) {  
            // TODO Auto-generated method stub  
            int h=mTimePicker.getCurrentHour();  
            int m=mTimePicker.getCurrentMinute();  
            Log.e("TimeActivity", "h:"+h+"   m:"+m);
        }  
    }  
	
	 class TimeListener implements OnTimeChangedListener{  
         
	        /** 
	         * view 当前选中TimePicker控件 
	         *  hourOfDay 当前控件选中TimePicker 的小时 
	         * minute 当前选中控件TimePicker  的分钟 
	         */  
	        @Override  
	        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {  
	            // TODO Auto-generated method stub  
	            Log.e("TimeActivity", "h:"+ hourOfDay +" m:"+minute);
	        }  
	          
	    }  
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.time, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.time_back_layout:
		case R.id.time_back:
			time_back();
			break;
		case R.id.time_next_layout:
		case R.id.time_next:
			time_next();
			break;
		default:
			break;
		}
	}

	private void time_next() {

	}

	private void time_back() {
		finish();
	}

	private void PPopupWindows() {
		popupwindow_calendar_month = (TextView) findViewById(R.id.popupwindow_calendar_month);
		calendar = (KCalendar) findViewById(R.id.popupwindow_calendar);

		popupwindow_calendar_month.setText(calendar.getCalendarYear() + "年"
				+ calendar.getCalendarMonth() + "月");

		if (null != date) {

			int years = Integer.parseInt(date.substring(0, date.indexOf("-")));
			int month = Integer.parseInt(date.substring(date.indexOf("-") + 1,
					date.lastIndexOf("-")));
			popupwindow_calendar_month.setText(years + "年" + month + "月");

			calendar.showCalendar(years, month);
			calendar.setCalendarDayBgColor(date,
					R.drawable.calendar_date_focused);
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
					calendar.setCalendarDayBgColor(dateFormat,
							R.drawable.calendar_date_focused);
					date = dateFormat;// 最后返回给全局 date
					Log.e("date", "date=========" + date);
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
