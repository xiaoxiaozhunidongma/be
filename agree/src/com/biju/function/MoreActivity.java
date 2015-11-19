package com.biju.function;

import java.util.Calendar;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract.Reminders;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.BJ.javabean.Moreback;
import com.BJ.javabean.Party;
import com.BJ.javabean.Party2;
import com.BJ.javabean.UserAllParty;
import com.BJ.utils.RefreshActivity;
import com.BJ.utils.SdPkUser;
import com.biju.IConstant;
import com.biju.Interface;
import com.biju.Interface.userCanclePartyListenner;
import com.biju.R;
import com.biju.wechatshare.Util;
import com.github.volley_examples.utils.GsonUtils;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

@SuppressLint("NewApi")
public class MoreActivity extends Activity implements OnClickListener {

	private Party2 moreparty;
	private Interface moreinterface;
	private UserAllParty userAllParty;
	private boolean userAll;
	private Integer sD_pk_user;
	private LinearLayout mMore_creator_layout;
	private LinearLayout mMore_member_layout;
	private Integer fk_user;
	private String calId;
	private String name;
	private String address;
	private String startTime;
	private String endTime;
	private String month_2;
	private String day_2;
	private String hour_2;
	private String minute_2;
	private String fk_party;
	private RelativeLayout mMoreLayout;

	private String app_id = "wx2ffba147560de2ff";
	private IWXAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		sD_pk_user = SdPkUser.getsD_pk_user();
		api = WXAPIFactory.createWXAPI(MoreActivity.this, "wx2ffba147560de2ff",false);

		Intent intent = getIntent();
		userAll = intent.getBooleanExtra(IConstant.UserAll, false);
		if (userAll) {
			userAllParty = (UserAllParty) intent.getSerializableExtra(IConstant.UserAllUoreParty);
			fk_user = userAllParty.getFk_user();
			name = userAllParty.getName();
			address = userAllParty.getLocation();
			startTime = userAllParty.getBegin_time();
			endTime = userAllParty.getEnd_time();
			fk_party = userAllParty.getPk_party();
		} else {
			moreparty = (Party2) intent.getSerializableExtra(IConstant.MoreParty);
			fk_user = moreparty.getFk_user();
			name = moreparty.getName();
			address = moreparty.getLocation();
			startTime = moreparty.getBegin_time();
			endTime = moreparty.getEnd_time();
			fk_party = moreparty.getPk_party();
		}

		initUI();
		initInterface();
	}

	private void initInterface() {
		moreinterface = Interface.getInstance();
		moreinterface.setPostListener(new userCanclePartyListenner() {

			@Override
			public void success(String A) {
				Moreback moreback = GsonUtils.parseJson(A, Moreback.class);
				Integer status = moreback.getStatusMsg();
				if (status == 1) {
					Log.e("MoreActivity", "�����Ƿ�ɾ���ɹ�" + A);
					if (userAll) {
						finish();
						for (int i = 0; i < RefreshActivity.activList_1.size(); i++) {
							RefreshActivity.activList_1.get(i).finish();
						}
					} else {
						SharedPreferences more_sp = getSharedPreferences(IConstant.MoreRefresh, 0);
						Editor editor = more_sp.edit();
						editor.putBoolean(IConstant.Morecancle, true);
						editor.commit();
						Intent intent = new Intent(MoreActivity.this,GroupActivity.class);
						startActivity(intent);
					}
				}
			}

			@Override
			public void defail(Object B) {

			}
		});
	}

	private void initUI() {
		mMoreLayout = (RelativeLayout) findViewById(R.id.MoreLayout);
		findViewById(R.id.More_member_sharecircle).setOnClickListener(this);// ��������Ȧ
		findViewById(R.id.More_member_sharefriends).setOnClickListener(this);// ���������

		findViewById(R.id.More_creator_shareCircle).setOnClickListener(this);// ��������Ȧ
		findViewById(R.id.More_creator_sharefriends).setOnClickListener(this);// ���������

		findViewById(R.id.More_member_add_1).setOnClickListener(this);// ��ӵ�ϵͳ����
		findViewById(R.id.More_member_add).setOnClickListener(this);// ��ӵ�ϵͳ����
		findViewById(R.id.More_creator_cancel_layout).setOnClickListener(this);// ȡ���ۻ�
		findViewById(R.id.More_creator_cancel).setOnClickListener(this);
		mMore_creator_layout = (LinearLayout) findViewById(R.id.More_creator_layout);// ������
		mMore_member_layout = (LinearLayout) findViewById(R.id.More_member_layout);// ��ͨ��Ա
		if (String.valueOf(fk_user).equals(String.valueOf(sD_pk_user))) {
			mMore_creator_layout.setVisibility(View.VISIBLE);
			mMore_member_layout.setVisibility(View.GONE);
		} else {
			mMore_creator_layout.setVisibility(View.GONE);
			mMore_member_layout.setVisibility(View.VISIBLE);
		}

		findViewById(R.id.More_title_cancel_layout).setOnClickListener(this);
		findViewById(R.id.More_title_cancel).setOnClickListener(this);// ȡ��
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.More_creator_cancel_layout:
		case R.id.More_creator_cancel:
			more_cancel_layout();
			break;
		case R.id.More_title_cancel_layout:
		case R.id.More_title_cancel:
			More_title_cancel();
			break;
		case R.id.More_member_add:
		case R.id.More_member_add_1:
			More_member_add();
			break;
		case R.id.More_member_sharecircle:
		case R.id.More_creator_shareCircle:
			More_creator_shareCircle();
			break;
		case R.id.More_member_sharefriends:
		case R.id.More_creator_sharefriends:
			More_creator_sharefriends();
			break;
		default:
			break;
		}
	}

	// ���������
	private void More_creator_sharefriends() {
		SdPkUser.setGetWeSource(true);// ˵����΢�ŷ����ȥ��
		WEChatShaerCircleFriends(0);
		finish();
	}

	// ��������Ȧ
	private void More_creator_shareCircle() {
		SdPkUser.setGetWeSource(true);// ˵����΢�ŷ����ȥ��
		WEChatShaerCircleFriends(1);
		finish();
	}

	private void WEChatShaerCircleFriends(int flag) {
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = "http://www.beagree.com/biju/party.html?party="+ fk_party;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = "���Աؾ۵ľۻ�";
		msg.description = name;
		try {
			Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.about_us);
			Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
			bmp.recycle();
			// msg.setThumbImage(thumbBmp);
			msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("biju");// webpage
		req.message = msg;
		if (flag == 0) {
			// ����΢�ŻỰ
			req.scene = SendMessageToWX.Req.WXSceneSession;
		} else {
			// ����΢������Ȧ
			req.scene = SendMessageToWX.Req.WXSceneTimeline;
		}
		api.sendReq(req);
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()): type + System.currentTimeMillis();
	}

	// ���ۻ����Ӹ��Ƶ��������
	// @SuppressWarnings("deprecation")
	// private void More_creator_share() {
	// String Url="http://www.beagree.com/biju/party.html?party="+fk_party;
	// ClipboardManager cm = (ClipboardManager)
	// getSystemService(Context.CLIPBOARD_SERVICE);
	// cm.setText(Url);//s����Ҫ���Ƶ�����
	//
	// SweetAlertDialog sd=new SweetAlertDialog(MoreActivity.this);
	// sd.setTitleText("��ʾ");
	// sd.setContentText("�ۻ������Ѿ����Ƶ������������~");
	// sd.show();
	//
	// finish();
	// }

	private void More_member_add() {
		String calanderURL;
		String calanderEventURL;
		String calanderRemiderURL;
		if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
			calanderURL = "content://com.android.calendar/calendars";
			calanderEventURL = "content://com.android.calendar/events";
			calanderRemiderURL = "content://com.android.calendar/reminders";
		} else {
			calanderURL = "content://calendar/calendars";
			calanderEventURL = "content://calendar/events";
			calanderRemiderURL = "content://calendar/reminders";
		}
		// ��ȡҪ�����gmail�˻���id
		String calId = "";
		Cursor userCursor = getContentResolver().query(Uri.parse(calanderURL),null, null, null, null);
		if (userCursor.getCount() > 0) {
			userCursor.moveToFirst();
			calId = userCursor.getString(userCursor.getColumnIndex("_id"));
			android.util.Log.e("cxq", calId);

		}
		ContentValues event = new ContentValues();
		event.put("title", name);
		event.put("description", "���Աؾ۵ľۻ�");
		event.put("calendar_id", calId);
		event.put("eventLocation", address);

		String year = startTime.substring(0, 4);
		String month = startTime.substring(5, 7);
		String day = startTime.substring(8, 10);
		String hour = startTime.substring(11, 13);
		String minute = startTime.substring(14, 16);
		Log.e("MoreActivity", "��===" + year + "\n��===" + month + "\n��===" + day
				+ "\nʱ===" + hour + "\n��===" + minute);

		String months_1 = month.substring(0, 1);
		Log.e("MoreActivity", "��ʱ���months_1===" + months_1);
		if (Integer.valueOf(months_1) > 0) {
			month_2 = month;
		} else {
			month_2 = month.substring(1, 2);
		}
		Log.e("MoreActivity", "��ʱ���month_2===" + month_2);

		String day_1 = day.substring(0, 1);
		Log.e("MoreActivity", "��ʱ���day_1===" + day_1);
		if (Integer.valueOf(day_1) > 0) {
			day_2 = day;
		} else {
			day_2 = day.substring(1, 2);
		}
		Log.e("MoreActivity", "��ʱ���day_2===" + day_2);

		String hour_1 = hour.substring(0, 1);
		Log.e("MoreActivity", "��ʱ���hour_1===" + hour_1);
		if (Integer.valueOf(hour_1) > 0) {
			hour_2 = hour;
		} else {
			hour_2 = hour.substring(1, 2);
		}
		Log.e("MoreActivity", "��ʱ���hour_2===" + hour_2);

		String minute_1 = minute.substring(0, 1);
		Log.e("MoreActivity", "��ʱ���minute_1===" + minute_1);
		if (Integer.valueOf(minute_1) > 0) {
			minute_2 = minute;
		} else {
			minute_2 = minute.substring(1, 2);
		}
		Log.e("MoreActivity", "��ʱ���minute_2===" + minute_2);

		Calendar mCalendar = Calendar.getInstance();
		mCalendar.set(Integer.valueOf(year), (Integer.valueOf(month_2) - 1),
				Integer.valueOf(day), Integer.valueOf(hour),
				Integer.valueOf(minute));
		long start = mCalendar.getTime().getTime();
		mCalendar.set(Integer.valueOf(year), (Integer.valueOf(month_2) - 1),
				Integer.valueOf(day), Integer.valueOf(hour),
				(Integer.valueOf(minute) + 30));
		long end = mCalendar.getTime().getTime();

		event.put("dtstart", start);
		event.put("dtend", end);

		event.put("hasAlarm", 1);
		// ����ʱ��
		event.put("eventTimezone", TimeZone.getDefault().getID().toString());

		Uri newEvent = getContentResolver().insert(Uri.parse(calanderEventURL),event);
		long id = Long.parseLong(newEvent.getLastPathSegment());
		ContentValues values = new ContentValues();
		values.put("event_id", id);
		// ��ǰһ������
		values.put(Reminders.MINUTES, 1 * 60 * 24);
		values.put(Reminders.EVENT_ID, id);
		values.put(Reminders.METHOD, Reminders.METHOD_ALERT);
		ContentResolver cr1 = getContentResolver(); // Ϊ�ղ�����ӵ�event���reminder
		cr1.insert(Reminders.CONTENT_URI, values);

		mMoreLayout.setVisibility(View.GONE);
		final SweetAlertDialog sd = new SweetAlertDialog(MoreActivity.this);
		sd.setTitleText("��ʾ");
		sd.setContentText("�ۻ��Ѿ��ɹ���ӵ��ճ�~");
		sd.setConfirmText("�õ�");
		sd.showCancelButton(true);
		sd.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sDialog) {
				sd.cancel();
				finish();
			}
		}).show();
	}

	private void More_title_cancel() {
		PartyDetailsActivity.partyDetailsBackground.PartyDetailsBackground();
		finish();
		Intent intent=new Intent(MoreActivity.this, PartyDetailsActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.top_1_item, R.anim.bottom_1_item);
	}

	private void more_cancel_layout() {
		Party more_party = new Party();
		if (userAll) {
			more_party.setPk_party(userAllParty.getPk_party());
			moreinterface.userCancleParty(MoreActivity.this, more_party);
		} else {
			more_party.setPk_party(moreparty.getPk_party());
			moreinterface.userCancleParty(MoreActivity.this, more_party);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			More_title_cancel();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
