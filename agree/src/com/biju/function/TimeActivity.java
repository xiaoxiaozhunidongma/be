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
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;
import com.BJ.utils.KCalendar;
import com.BJ.utils.RefreshActivity;
import com.BJ.utils.KCalendar.OnCalendarClickListener;
import com.BJ.utils.KCalendar.OnCalendarDateChangedListener;
import com.biju.IConstant;
import com.biju.R;

@SuppressLint("SimpleDateFormat")
public class TimeActivity extends Activity implements OnClickListener {

	String date = null;// ����Ĭ��ѡ�е����� ��ʽΪ ��2014-04-05�� ��׼DATE��ʽ
	private KCalendar calendar;
	private TextView popupwindow_calendar_month;
	private TimePicker mTimePicker;
	private TextView mTime_next;
	private int mHour;
	private int mMinute;
	private int h;
	private int m;
	private Integer currentMonth_2;
	private Integer oldMonth_2;
	private Integer currentDay_2;
	private Integer oldDay_2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time);
		//����List��
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
			date = dateFormat;// ��󷵻ظ�ȫ�� date
			Log.e("MainActivity", "��������");
			if (null != date) {

				int years = Integer.parseInt(date.substring(0,date.indexOf("-")));
				int month = Integer.parseInt(date.substring(date.indexOf("-") + 1, date.lastIndexOf("-")));
				popupwindow_calendar_month.setText(years + "��" + month + "��");

				calendar.showCalendar(years, month);
				calendar.setCalendarDayBgColor(date,R.drawable.calendar_date_focused);
			}
		}
	}

	private void initUI() {
		findViewById(R.id.Time_back_layout).setOnClickListener(this);
		findViewById(R.id.Time_back).setOnClickListener(this);// ����
		findViewById(R.id.Time_back_layout).setOnClickListener(this);// ��һ��
		mTime_next = (TextView) findViewById(R.id.Time_OK);
		mTime_next.setOnClickListener(this);
		mTimePicker = (TimePicker) findViewById(R.id.timePicker);
		// OnChangeListener buc = new OnChangeListener();
		// mTime_next.setOnClickListener(buc);
		// �Ƿ�ʹ��24Сʱ��
		SharedPreferences time_sp = getSharedPreferences(IConstant.IsTime, 0);
		boolean istimechoose = time_sp.getBoolean(IConstant.IsTimeChoose, false);
		mTimePicker.setIs24HourView(false);
		if(istimechoose)
		{
			Integer hour = time_sp.getInt(IConstant.Hour, 0);
			Integer minute = time_sp.getInt(IConstant.Minute, 0);
			mTimePicker.setCurrentHour(hour);
			mTimePicker.setCurrentMinute(minute);
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
		 * view ��ǰѡ��TimePicker�ؼ� hourOfDay ��ǰ�ؼ�ѡ��TimePicker ��Сʱ minute
		 * ��ǰѡ�пؼ�TimePicker �ķ���
		 */
		@Override
		public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
			Log.e("TimeActivity", "���ѡ����h:" + hourOfDay + "���ѡ����m:" + minute);
			mHour = hourOfDay;
			mMinute = minute;
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
		if (!(date != null)) {
			SweetAlertDialog sd=new SweetAlertDialog(TimeActivity.this);
			sd.setTitleText("��ʾ");
			sd.setContentText("����ڲ���Ϊ��Ŷ~");
			sd.show();
		} else {
			if(mHour==0)
			{
				SweetAlertDialog sd=new SweetAlertDialog(TimeActivity.this);
				sd.setTitleText("��ʾ");
				sd.setContentText("�ʱ�䲻��Ϊ��Ŷ~");
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
				Log.e("TimeActivity", "��ȡ��ǰ����==========="+CurrentYears);
				Log.e("TimeActivity", "��ȡ��ǰ����==========="+CurrentMonth);
				Log.e("TimeActivity", "��ȡ��ǰ����==========="+CurrentDay);
				Log.e("TimeActivity", "��ȡ��ǰ��ʱ==========="+CurrentHour);
				Log.e("TimeActivity", "��ȡ��ǰ�ķ�==========="+CurrentMinute);
				
				String OldYears=date.substring(0, 4);
				String OldMonth=date.substring(5, 7);
				String OldDay=date.substring(8, 10);
				Log.e("TimeActivity", "��ȡ�û�ѡ�����==========="+OldYears);
				Log.e("TimeActivity", "��ȡ�û�ѡ�����==========="+OldMonth);
				Log.e("TimeActivity", "��ȡ�û�ѡ�����==========="+OldDay);
				
				if(Integer.valueOf(OldYears)<Integer.valueOf(CurrentYears))
				{
					OldTime();
					Log.e("TimeActivity", "�ж����ʱ���=========������ѡ���С�ڵ�ǰ��");
				}else
				{
					Log.e("TimeActivity", "�ж����ʱ���=========������ѡ��Ĵ��ڵ�ǰ��");
					Month(CurrentMonth, OldMonth);
					//�жϵ�ǰ���º��û�ѡ�����
					if(oldMonth_2<currentMonth_2)
					{
						Log.e("TimeActivity", "�ж��µ�ʱ���=========������ѡ���С�ڵ�ǰ��");
						OldTime();
					}else if(oldMonth_2==currentMonth_2)
					{

						Log.e("TimeActivity", "�ж��µ�ʱ���=========������ѡ��ĵ��ڵ�ǰ��");
						Day(CurrentDay, OldDay);
						
						if(oldDay_2<currentDay_2)
						{
							Log.e("TimeActivity", "�ж��յ�ʱ���=========������ѡ���С�ڵ�ǰ��");
							OldTime();
						}else if(oldDay_2==currentDay_2)
						{
							Log.e("TimeActivity", "�ж��յ�ʱ���=========������ѡ��ĵ��ڵ�ǰ��");
							if(mHour<(Integer.valueOf(CurrentHour)+3))
							{
								Log.e("TimeActivity", "�ж�ʱ��ʱ���=========������ѡ���С�ڵ�ǰ��");
								SweetAlertDialog sd=new SweetAlertDialog(TimeActivity.this);
								sd.setTitleText("��ʾ");
								sd.setContentText("�ۻῪʼ��ʱ������ǵ�ǰʱ�������Сʱ��Ŷ~");
								sd.show();
							}
							else
							{
								Log.e("TimeActivity", "�ж�ʱ��ʱ���=========������ѡ��Ĵ��ڵ�ǰ��");
								//����ɾۻ�Ĺ���
								timesp();
							}
						}else
						{
							Log.e("TimeActivity", "�ж��յ�ʱ���=========������ѡ��Ĵ��ڵ�ǰ��");
							//����ɾۻ�Ĺ���
							timesp();
						}
					
					}else
					{
						Log.e("TimeActivity", "�ж��µ�ʱ���=========������ѡ��Ĵ��ڵ�ǰ��");
						//����ɾۻ�Ĺ���
						timesp();
					}
					
				}
			}
		}
	}

	private void Day(String CurrentDay, String OldDay) {
		//�ж����Ƿ񳬹�10
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
		editor.putInt(IConstant.Hour, mHour);
		editor.putInt(IConstant.Minute, mMinute);
		editor.putString(IConstant.IsCalendar, date);
		Log.e("TimeActivity", "date=========" + date);
		editor.commit();
		finish();
	}

	private void Month(String CurrentMonth, String OldMonth) {
		//�ж��·��Ƿ񳬹�10
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
		sd.setTitleText("��ʾ");
		sd.setContentText("����ѡ���ȥ��ʱ��Ŷ~");
		sd.show();
	}

	private void time_back() {
		finish();
	}

	private void PPopupWindows() {
		popupwindow_calendar_month = (TextView) findViewById(R.id.popupwindow_calendar_month);
		calendar = (KCalendar) findViewById(R.id.popupwindow_calendar);

		popupwindow_calendar_month.setText(calendar.getCalendarYear() + "��"
				+ calendar.getCalendarMonth() + "��");

		if (null != date) {

			int years = Integer.parseInt(date.substring(0, date.indexOf("-")));
			int month = Integer.parseInt(date.substring(date.indexOf("-") + 1,date.lastIndexOf("-")));
			popupwindow_calendar_month.setText(years + "��" + month + "��");

			calendar.showCalendar(years, month);
			calendar.setCalendarDayBgColor(date,R.drawable.calendar_date_focused);
		}

		List<String> list = new ArrayList<String>(); // ���ñ���б�
		list.add("2014-04-01");
		list.add("2014-04-02");
		calendar.addMarks(list, 0);

		// ������ѡ�е�����
		calendar.setOnCalendarClickListener(new OnCalendarClickListener() {

			public void onCalendarClick(int row, int col, String dateFormat) {
				int month = Integer.parseInt(dateFormat.substring(
						dateFormat.indexOf("-") + 1,
						dateFormat.lastIndexOf("-")));

				if (calendar.getCalendarMonth() - month == 1// ������ת
						|| calendar.getCalendarMonth() - month == -11) {
					calendar.lastMonth();

				} else if (month - calendar.getCalendarMonth() == 1 // ������ת
						|| month - calendar.getCalendarMonth() == -11) {
					calendar.nextMonth();

				} else {
					calendar.removeAllBgColor();
					calendar.setCalendarDayBgColor(dateFormat,
							R.drawable.calendar_date_focused);
					date = dateFormat;// ��󷵻ظ�ȫ�� date
					// Log.e("date", "date=========" + date);
					Log.e("date", "date=========" + date);
					SharedPreferences sp = getSharedPreferences("isdate", 0);
					Editor editor = sp.edit();
					editor.putString("dateFormat", dateFormat);
					editor.putBoolean("date", true);
					editor.commit();
				}
			}
		});

		// ������ǰ�·�
		calendar.setOnCalendarDateChangedListener(new OnCalendarDateChangedListener() {
			public void onCalendarDateChanged(int year, int month) {
				popupwindow_calendar_month.setText(year + "��" + month + "��");
			}
		});

		// ���¼�����ť
		RelativeLayout popupwindow_calendar_last_month = (RelativeLayout) findViewById(R.id.popupwindow_calendar_last_month);
		popupwindow_calendar_last_month
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						calendar.lastMonth();
					}

				});

		// ���¼�����ť
		RelativeLayout popupwindow_calendar_next_month = (RelativeLayout) findViewById(R.id.popupwindow_calendar_next_month);
		popupwindow_calendar_next_month
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						calendar.nextMonth();
					}
				});
	}

}
