package com.biju.function;

import java.util.UUID;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.BJ.javabean.Party;
import com.BJ.javabean.PartyOkback;
import com.BJ.utils.SdPkUser;
import com.BJ.utils.Weeks;
import com.biju.Interface;
import com.biju.Interface.addPartyListenner;
import com.biju.R;
import com.github.volley_examples.utils.GsonUtils;

public class OkPartyActivity extends Activity implements OnClickListener {

	private TextView mOkParty_address;
	private TextView mOkParty_time;
	private TextView mOkParty_feedback;
	private boolean isFeedback;
	private EditText mOkParty_name;
	private EditText mOkParty_note;
	private int fk_group;
	private String address;
	private String isCalendar;
	private int hour;
	private int minute;
	private double mLng;
	private double mLat;
	private Interface okPartyInterface;
	private int y;
	private int m;
	private int d;
	private Integer sD_pk_user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ok_party);
		
		//��ȡsd���е�pk_user
		sD_pk_user = SdPkUser.getsD_pk_user();
		Log.e("OkPartyActivity", "��SD���л�ȡ����Pk_user" + sD_pk_user);

		// �õ�fk_group
		SharedPreferences sp3 = getSharedPreferences("isParty_fk_group", 0);
		fk_group = sp3.getInt("fk_group", 0);

		initUI();
		initData();
		// �жϷ��������Ƿ���
		SharedPreferences sp2 = getSharedPreferences("isFeedback", 0);
		isFeedback = sp2.getBoolean("feedback", false);
		getMyUUID();
		initInterface();
	}

	private void initInterface() {
		okPartyInterface = Interface.getInstance();
		okPartyInterface.setPostListener(new addPartyListenner() {

			@Override
			public void success(String A) {
				PartyOkback partyOkback=GsonUtils.parseJson(A, PartyOkback.class);
				Integer status=partyOkback.getStatusMsg();
				if(status==1)
				{
					Log.e("OkPartyActivity", "�ճ��Ƿ񴴽��ɹ�======" + A);
					finish();
					//�ر��½�С�����
					NewPartyActivity.NewParty.finish();
					//�رյ�ͼ����
					MapActivity.Map.finish();
					//�ر�ʱ�����ڽ���
					TimeActivity.Time.finish();
				}
			}

			@Override
			public void defail(Object B) {
			}
		});
	}

	public static String getMyUUID() {
		UUID uuid = UUID.randomUUID();
		String uniqueId = uuid.toString();
		return uniqueId;
	}

	private void initData() {
		// �õ���ַ�;�γ��
		SharedPreferences sp = getSharedPreferences("isParty", 0);
		address = sp.getString("isAddress", "");
		Log.e("OkPartyActivity", "address======"+address);
		float mLng_1 = sp.getFloat("mLng", 0);
		float mLat_1 = sp.getFloat("mLat", 0);
		mLng = mLng_1;
		mLat = mLat_1;
		// �õ������պ�ѡ��ʱ��
		isCalendar = sp.getString("isCalendar", "");
		String years = isCalendar.substring(0, 4);
		String months = isCalendar.substring(5, 7);
		String days = isCalendar.substring(8, 10);
//		String times = years + "��" + months + "��" + days + "��";
		// �������ڼ�
		y = Integer.valueOf(years);
		m = Integer.valueOf(months);
		d = Integer.valueOf(days);
		// ���ü������ڼ��ķ���
		Weeks.CaculateWeekDay(y, m, d);
		String week = Weeks.getweek();
		// �õ���������ʱ��
		hour = sp.getInt("hour", 0);
		minute = sp.getInt("minute", 0);
		mOkParty_time.setText(years + "��" + months + "��" + days + "��" + " "
				+ week + " " + hour + ":" + minute);
		mOkParty_address.setText(address);
		if (isFeedback) {
			mOkParty_feedback.setText("�ر�");
		} else {
			mOkParty_feedback.setText("����");
		}
	}

	private void initUI() {
		mOkParty_address = (TextView) findViewById(R.id.OkParty_address);// ��ʾ��ַ
		mOkParty_time = (TextView) findViewById(R.id.OkParty_time);// ��ʾ�����պ�ʱ��
		mOkParty_feedback = (TextView) findViewById(R.id.OkParty_feedback);// ��������
		mOkParty_feedback.setOnClickListener(this);
		mOkParty_name = (EditText) findViewById(R.id.OkParty_name);// �ۻ�����
		mOkParty_note = (EditText) findViewById(R.id.OkParty_note);// �ۻᱸע
		findViewById(R.id.OkParty_complete_layout).setOnClickListener(this);
		findViewById(R.id.OkParty_complete).setOnClickListener(this);// ���
		findViewById(R.id.OkParty_back_layout).setOnClickListener(this);// ����
		findViewById(R.id.OkParty_back).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ok_party, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.OkParty_feedback:
			OkParty_feedback();
			break;
		case R.id.OkParty_complete_layout:
		case R.id.OkParty_complete:
			OkParty_complete();
			break;
		case R.id.OkParty_back_layout:
		case R.id.OkParty_back:
			OkParty_back();
			break;
		default:
			break;
		}
	}

	private void OkParty_back() {
		finish();
	}

	private void OkParty_complete() {
		String pk_party = OkPartyActivity.getMyUUID();
		String name = mOkParty_name.getText().toString().trim();
		String remark = mOkParty_note.getText().toString().trim();

		Party party = new Party();
		party.setPk_party(pk_party);
		party.setFk_group(fk_group);
		party.setFk_user(sD_pk_user);
		party.setName(name);
		party.setRemark(remark);
		party.setBegin_time(isCalendar + "   " + hour + ":" + minute);
		party.setLongitude(mLng);
		party.setLatitude(mLat);
		party.setLocation(address);
		party.setStatus(1);
		Log.e("OkPartyActivity", "�½��ճ̵�UUID=====" + pk_party);
		Log.e("OkPartyActivity", "�½��ճ̵�name=====" + name);
		Log.e("OkPartyActivity", "�½��ճ̵�remark=====" + remark);
		Log.e("OkPartyActivity", "�½��ճ̵�fk_group=====" + fk_group);
		Log.e("OkPartyActivity", "�½��ճ̵�ʱ��=====" + isCalendar + "    " + hour+ ":" + minute);
		Log.e("OkPartyActivity", "�½��ճ̵�mLng=====" + mLng);
		Log.e("OkPartyActivity", "�½��ճ̵�mLat=====" + mLat);
		Log.e("OkPartyActivity", "�½��ճ̵ĵ�ַ=====" + address);
		okPartyInterface.addParty(OkPartyActivity.this, party);
	}

	private void OkParty_feedback() {
		isFeedback = !isFeedback;
		if (isFeedback) {
			mOkParty_feedback.setText("�ر�");
		} else {
			mOkParty_feedback.setText("����");
		}
	}

	@Override
	protected void onStop() {
		SharedPreferences sp = getSharedPreferences("isFeedback", 0);
		Editor editor = sp.edit();
		editor.putBoolean("feedback", isFeedback);
		editor.commit();
		super.onStop();
	}

}
