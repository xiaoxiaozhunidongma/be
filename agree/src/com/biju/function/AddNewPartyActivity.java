package com.biju.function;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.BJ.javabean.MapAddParty;
import com.BJ.javabean.Party;
import com.BJ.javabean.PartyOkback;
import com.BJ.utils.SdPkUser;
import com.BJ.utils.Weeks;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.addPartyListenner;
import com.biju.MainActivity;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;

public class AddNewPartyActivity extends Activity implements OnClickListener {

	private AddNewPartyActivity context;
	private RelativeLayout mAdd_New_Party_back_layout;
	private TextView mAdd_New_Party_back;
	private RelativeLayout mAdd_New_Party_OK_layout;
	private TextView mAdd_New_Party_OK;
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

	private void initInterface() {
		addNewParty_Interface = Interface.getInstance();
		addNewParty_Interface.setPostListener(new addPartyListenner() {

			@Override
			public void success(String A) {
				PartyOkback partyOkback = GsonUtils.parseJson(A,PartyOkback.class);
				Integer status = partyOkback.getStatusMsg();
				if (status == 1) {
					Log.e("AddNewPartyActivity", "�ճ��Ƿ񴴽��ɹ�======" + A);
					// ��ԭ�Ƿ�������ͼѡ��
					SharedPreferences map_sp = getSharedPreferences(IConstant.IsMap, 0);
					Editor editor = map_sp.edit();
					editor.putBoolean(IConstant.IsMapChoose, false);
					editor.commit();
					// ��ԭ�Ƿ�����ʱ��ѡ��
					SharedPreferences time_sp = getSharedPreferences(IConstant.IsTime, 0);
					Editor editor1 = time_sp.edit();
					editor1.putBoolean(IConstant.IsTimeChoose, false);
					editor1.commit();
					
					//��ԭѡ�����ں��
					SharedPreferences date_sp = getSharedPreferences("isdate", 0);
					Editor editor3 = date_sp.edit();
					editor3.putBoolean("date", false);
					editor3.commit();
					
					SharedPreferences refresh_sp=getSharedPreferences(IConstant.AddRefresh, 0);
					Editor editor2=refresh_sp.edit();
					editor2.putBoolean(IConstant.IsAddRefresh, true);
					editor2.commit();
					finish();
					if(!source)
					{
						Intent intent=new Intent(AddNewPartyActivity.this, MainActivity.class);
						startActivity(intent);
					}
				}
			}

			@Override
			public void defail(Object B) {
				finish();
			}
		});
	}

	@Override
	protected void onStart() {
		initAddress();
		initTime();
		super.onStart();
	}

	// �õ�pk_party
	public static String getMyUUID() {
		UUID uuid = UUID.randomUUID();
		String uniqueId = uuid.toString();
		return uniqueId;
	}

	// ��ȡ��������ʱ��
	private void initTime() {
		// �õ������պ�ѡ��ʱ��
		SharedPreferences time_sp = getSharedPreferences(IConstant.IsTime, 0);
		boolean istimechoose = time_sp.getBoolean(IConstant.IsTimeChoose, false);
		if (istimechoose) {
			isCalendar = time_sp.getString(IConstant.IsCalendar, "");
			String years = isCalendar.substring(0, 4);
			String months = isCalendar.substring(5, 7);
			String days = isCalendar.substring(8, 10);
			// String times = years + "��" + months + "��" + days + "��";
			// �������ڼ�
			int y = Integer.valueOf(years);
			int m = Integer.valueOf(months);
			int d = Integer.valueOf(days);
			// ���ü������ڼ��ķ���
			Weeks.CaculateWeekDay(y, m, d);
			String week = Weeks.getweek();
			hour = time_sp.getInt(IConstant.Hour, 0);
			minute = time_sp.getInt(IConstant.Minute, 0);
			if(hour<10)
			{
				if(minute<10)
				{
					mAdd_New_Party_time_details.setText(years + "��" + months + "��"
							+ days + "��" + " " + week + " " +"0"+hour + ":" +"0"+minute);
				}else
				{
					mAdd_New_Party_time_details.setText(years + "��" + months + "��"
							+ days + "��" + " " + week + " " +"0"+hour + ":" + minute);
				}
			}else
			{
				if(minute<10)
				{
					mAdd_New_Party_time_details.setText(years + "��" + months + "��"
							+ days + "��" + " " + week + " " + hour + ":" +"0"+minute);
				}else
				{
					mAdd_New_Party_time_details.setText(years + "��" + months + "��"
							+ days + "��" + " " + week + " " + hour + ":" + minute);
				}
			}
			
		}
	}

	// ��ȡ�������ĵ�ַ
	private void initAddress() {
		SharedPreferences map_sp = getSharedPreferences(IConstant.IsMap, 0);
		boolean ischoose = map_sp.getBoolean(IConstant.IsMapChoose, false);
		if (ischoose) {
			address = map_sp.getString(IConstant.IsAddress, "");
			float mLng_1 = map_sp.getFloat(IConstant.MLng, 0);
			float mLat_1 = map_sp.getFloat(IConstant.MLat, 0);
			mLng = mLng_1;
			mLat = mLat_1;
			mAdd_New_Party_address_details.setText(address);
		}
	}

	private void initUI() {
		mAdd_New_Party_back_layout = (RelativeLayout) findViewById(R.id.Add_New_Party_back_layout);
		mAdd_New_Party_back = (TextView) findViewById(R.id.Add_New_Party_back);// ����
		mAdd_New_Party_OK_layout = (RelativeLayout) findViewById(R.id.Add_New_Party_OK_layout);
		mAdd_New_Party_OK = (TextView) findViewById(R.id.Add_New_Party_OK);// ���
		mAdd_New_Party_back_layout.setOnClickListener(this);
		mAdd_New_Party_back.setOnClickListener(this);
		mAdd_New_Party_OK_layout.setOnClickListener(this);
		mAdd_New_Party_OK.setOnClickListener(this);
		mAdd_New_Party_OK.setEnabled(true);
		mAdd_New_Party_name = (EditText) findViewById(R.id.Add_New_Party_name);// �ۻ�����
		mAdd_New_Party_time = (RelativeLayout) findViewById(R.id.Add_New_Party_time);// �ۻ�ʱ��
		mAdd_New_Party_address = (RelativeLayout) findViewById(R.id.Add_New_Party_address);// �ۻ�ص�
		mAdd_New_Party_time.setOnClickListener(this);
		mAdd_New_Party_address.setOnClickListener(this);
		mAdd_New_Party_time_details = (TextView) findViewById(R.id.Add_New_Party_time_details);// ʱ������
		mAdd_New_Party_address_details = (TextView) findViewById(R.id.Add_New_Party_address_details);// ��ַ����
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_new_party, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Add_New_Party_back_layout:
		case R.id.Add_New_Party_back:
			Add_New_Party_back();
			break;
		case R.id.Add_New_Party_OK_layout:
		case R.id.Add_New_Party_OK:
			Add_New_Party_OK();
			break;
		case R.id.Add_New_Party_time:
			Add_New_Party_time();
			break;
		case R.id.Add_New_Party_address:
			Add_New_Party_address();
			break;
		default:
			break;
		}
	}

	// �ۻ�ص�
	private void Add_New_Party_address() {
		Intent intent = new Intent(AddNewPartyActivity.this, MapActivity.class);
		startActivity(intent);
	}

	// �ۻ�ʱ��
	private void Add_New_Party_time() {
		Intent intent = new Intent(AddNewPartyActivity.this, TimeActivity.class);
		startActivity(intent);
	}

	private void TotalCount(String Ems) {
		Pattern p1 = Pattern.compile("[0-9]*"); 
		Matcher m1 = p1.matcher(Ems);
		if(m1.matches())
		{
			Log.e("AddNewPartyActivity", "�����������===========");
			TotalCount1=TotalCount1+1;
		}else
		{
			Pattern p2=Pattern.compile("[a-zA-Z]");
			Matcher m2=p2.matcher(Ems);
			if(m2.matches())
			{
				Log.e("AddNewPartyActivity", "���������ĸ===========");
				TotalCount2=TotalCount2+1;
			}else
			{
				Pattern p3=Pattern.compile("[\u4e00-\u9fa5]");
				Matcher m3=p3.matcher(Ems);
				if(m3.matches())
				{
					Log.e("AddNewPartyActivity", "������Ǻ���===========");
					TotalCount3=TotalCount3+2;
				}
			}
		}
	}
	
	
	// ���
	private void Add_New_Party_OK() {
		String ems=mAdd_New_Party_name.getText().toString().trim();
		if("".equals(ems))
		{
		}else
		{
			TotalCount1=0;
			TotalCount2=0;
			TotalCount3=0;
			for (int i = 0; i < ems.length(); i++) {
				String Ems=String.valueOf(ems.charAt(i));
				TotalCount(Ems);
			}
		}
		int TotalCount=TotalCount1+TotalCount2+TotalCount3;
		if(TotalCount<2)
		{
			SweetAlerDialog();
		}else
		{
			String pk_party = AddNewPartyActivity.getMyUUID();
			String party_name = mAdd_New_Party_name.getText().toString().trim();
			
			Party party = new Party();
			party.setPk_party(pk_party);
			party.setFk_group(fk_group);
			party.setFk_user(sD_pk_user);
			party.setName(party_name);
			party.setRemark("");
			party.setBegin_time(isCalendar + "   " + hour + ":" + minute);
			party.setLongitude(mLng);
			party.setLatitude(mLat);
			party.setLocation(address);
			party.setStatus(1);
			Log.e("AddNewPartyActivity", "�½��ճ̵�UUID=====" + pk_party);
			Log.e("AddNewPartyActivity", "�½��ճ̵�name=====" + party_name);
			Log.e("AddNewPartyActivity", "�½��ճ̵�fk_group=====" + fk_group);
			Log.e("AddNewPartyActivity", "�½��ճ̵�ʱ��=====" + isCalendar + "    " + hour+ ":" + minute);
			Log.e("AddNewPartyActivity", "�½��ճ̵�mLng=====" + mLng);
			Log.e("AddNewPartyActivity", "�½��ճ̵�mLat=====" + mLat);
			Log.e("AddNewPartyActivity", "�½��ճ̵ĵ�ַ=====" + address);
			addNewParty_Interface.addParty(AddNewPartyActivity.this, new MapAddParty(null, party));
			mAdd_New_Party_OK.setEnabled(false);
		}

	}

	private void SweetAlerDialog() {
		SweetAlertDialog sd=new SweetAlertDialog(context);
		sd.setTitleText("��ʾ");
		sd.setContentText("����Ʋ���Ϊ�ջ���С��2���ַ�Ŷ~");
		sd.show();
	}

	// ����
	private void Add_New_Party_back() {
		finish();
	}

}
