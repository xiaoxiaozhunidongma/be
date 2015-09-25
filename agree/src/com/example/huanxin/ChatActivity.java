package com.example.huanxin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.BJ.javabean.ReadUserAllFriends;
import com.BJ.utils.ImageLoaderUtils;
import com.BJ.utils.PreferenceUtils;
import com.biju.R;
import com.easemob.EMCallBack;
import com.easemob.EMChatRoomChangeListener;
import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Direct;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.FileMessageBody;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.LocationMessageBody;
import com.easemob.chat.NormalFileMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VideoMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.FileUtils;
import com.easemob.util.LatLng;
import com.easemob.util.PathUtil;
import com.easemob.util.TextFormater;
import com.easemob.util.VoiceRecorder;
import com.example.takephoto.BaiduMapActivity;
import com.example.takephoto.DateUtils;
import com.example.takephoto.DemoHXSDKHelper;
import com.example.takephoto.ForwardMessageActivity;
import com.example.takephoto.HXSDKHelper;
import com.example.takephoto.ImageCache;
import com.example.takephoto.ImageUtils;
import com.example.takephoto.LoadImageTask;
import com.example.takephoto.LoadVideoImageTask;
import com.example.takephoto.ShowNormalFileActivity;
import com.example.takephoto.ShowVideoActivity;
import com.example.takephoto.VideoCallActivity;
import com.example.takephoto.VoiceCallActivity;
import com.example.takephoto.VoicePlayClickListener;

public class ChatActivity extends Activity implements OnClickListener, EMEventListener{
	public static ChatActivity activityInstance;
	EMConversation conversation;
	private String toChatUsername;
	private ListView listView;
	private MyList adapter;
	private PasteEditText input;
	private ImageView send_expression;
	private LinearLayout display_expression;
	private ViewPager expressionViewpager;
	private Button send;
	public static final String IMAGE_DIR = "chat/image/";
	int chatType;
	private List<String> reslist;
	static int resendPos;
	private View buttonSetModeKeyboard;
	public static final int RESULT_CODE_OPEN = 4;
	public static final int RESULT_CODE_DWONLOAD = 5;
	public static final int RESULT_CODE_TO_CLOUD = 6;
	public static final int REQUEST_CODE_GROUP_DETAIL = 21;
	private static final int REQUEST_CODE_MAP = 4;
	public static final int REQUEST_CODE_SELECT_VIDEO = 23;
	public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
	private static final int MESSAGE_TYPE_RECV_TXT = 0;
	private static final int MESSAGE_TYPE_SENT_TXT = 1;
	private static final int MESSAGE_TYPE_SENT_IMAGE = 2;
	private static final int MESSAGE_TYPE_SENT_LOCATION = 3;
	private static final int MESSAGE_TYPE_RECV_LOCATION = 4;
	private static final int MESSAGE_TYPE_RECV_IMAGE = 5;
	private static final int MESSAGE_TYPE_SENT_VOICE = 6;
	private static final int MESSAGE_TYPE_RECV_VOICE = 7;
	private static final int MESSAGE_TYPE_SENT_VIDEO = 8;
	private static final int MESSAGE_TYPE_RECV_VIDEO = 9;
	private static final int MESSAGE_TYPE_SENT_FILE = 10;
	private static final int MESSAGE_TYPE_RECV_FILE = 11;
	private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 12;
	private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 13;
	private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 14;
	private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 15;
	public static final int REQUEST_CODE_FILE = 10;
	public static final int REQUEST_CODE_VIDEO = 14;
	public static final int REQUEST_CODE_TEXT = 5;
	public static final int REQUEST_CODE_VOICE = 6;
	public static final int REQUEST_CODE_PICTURE = 7;
	public static final int REQUEST_CODE_LOCATION = 8;
	public static final int REQUEST_CODE_CAMERA = 18;
	public static final int CHATTYPE_SINGLE = 1;
	public static final int CHATTYPE_GROUP = 2;
	public static final int CHATTYPE_CHATROOM = 3;
	public static final int REQUEST_CODE_LOCAL = 19;
	public static final int REQUEST_CODE_ADD_TO_BLACKLIST = 25;
	public static final int REQUEST_CODE_CONTEXT_MENU = 3;
	public static final int REQUEST_CODE_SELECT_FILE = 24;
	public static final int RESULT_CODE_EXIT_GROUP = 7;
	public static final int RESULT_CODE_COPY = 1;
	public static final int RESULT_CODE_DELETE = 2;
	public static final int RESULT_CODE_FORWARD = 3;
	private static final int REQUEST_CODE_EMPTY_HISTORY = 2;
	public static final String COPY_IMAGE = "EASEMOBIMG";
	private LinearLayout moreItem;
	private File cameraFile;
	private Map<String, Timer> timers = new Hashtable<String, Timer>();
	public String playMsgId;
	private ClipboardManager clipboard;
	private SwipeRefreshLayout swipeRefreshLayout;
	private InputMethodManager manager;
	private WakeLock wakeLock;
	private EMGroup group;
	private ImageView micImage;
	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "";
	private Handler micImageHandler = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {
			// 切换msg切换图片
			micImage.setImageDrawable(micImages[msg.what]);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		activityInstance=this;
		//...............................    ...........................
		Intent intent = getIntent();
		mAllFriends = (ReadUserAllFriends) intent.getSerializableExtra("allFriends");
		
		if(mAllFriends!=null){
			int pk_user = mAllFriends.getPk_user();
			nickname = mAllFriends.getNickname();
			avatar_path = mAllFriends.getAvatar_path();
			toChatUsername=String.valueOf(pk_user);
//			toChatUsername="a";
		}
		unitUI();

//				unitGroup();
		//		clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		//
		//
//				conversation = EMChatManager.getInstance().getConversation(toChatUsername);
		listView = (ListView) findViewById(R.id.list1);
//		adapter = new MyList(this,toChatUsername,chatType);
		adapter = new MyList(this,toChatUsername,CHATTYPE_SINGLE);
		listView.setAdapter(adapter);
		listView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//隐藏软键盘
				hideKeyboard();
				display_expression.setVisibility(View.GONE);
				moreItem.setVisibility(View.GONE);
				return false;
			}
		});

		setUpView();
		send.setOnClickListener(this);


		diplayMsg();
	}
	private void setUpView() {

		send_expression.setOnClickListener(this);
		// position = getIntent().getIntExtra("position", -1);
		clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(
				PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
		//................................................................................
//		// 判断单聊还是群聊
//		chatType = getIntent().getIntExtra("chatType", CHATTYPE_SINGLE);
		chatType=CHATTYPE_SINGLE;

		if (chatType == CHATTYPE_SINGLE) { // 单聊
			int pk_user = mAllFriends.getPk_user();
			toChatUsername=String.valueOf(pk_user);
			((TextView) findViewById(R.id.name)).setText(nickname);
		} else {
			// 群聊
			//            findViewById(R.id.container_to_group).setVisibility(View.VISIBLE);
			//            findViewById(R.id.container_remove).setVisibility(View.GONE);
			//            findViewById(R.id.container_voice_call).setVisibility(View.GONE);
			//            findViewById(R.id.container_video_call).setVisibility(View.GONE);
			toChatUsername = getIntent().getStringExtra("groupId");

			if(chatType == CHATTYPE_GROUP){
				onGroupViewCreation();
			}
		}

		// for chatroom type, we only init conversation and create view adapter on success
		Log.e("chatType113", ""+chatType);
		if(chatType != CHATTYPE_CHATROOM){
			onConversationInit();

			onListViewCreation();

			// show forward message if the message is not null
			String forward_msg_id = getIntent().getStringExtra("forward_msg_id");
			if (forward_msg_id != null) {
				// 显示发送要转发的消息
				forwardMessage(forward_msg_id);
			}
		}

	}
	private void onConversationInit() {

		if(chatType == CHATTYPE_SINGLE){
			conversation = EMChatManager.getInstance().getConversationByType(toChatUsername,EMConversationType.Chat);
		}else if(chatType == CHATTYPE_GROUP){
			conversation = EMChatManager.getInstance().getConversationByType(toChatUsername,EMConversationType.GroupChat);
		}else if(chatType == CHATTYPE_CHATROOM){
			conversation = EMChatManager.getInstance().getConversationByType(toChatUsername,EMConversationType.ChatRoom);
		}

		// 把此会话的未读数置为0
		conversation.markAllMessagesAsRead();

		// 初始化db时，每个conversation加载数目是getChatOptions().getNumberOfMessagesLoaded
		// 这个数目如果比用户期望进入会话界面时显示的个数不一样，就多加载一些
		final List<EMMessage> msgs = conversation.getAllMessages();
		int msgCount = msgs != null ? msgs.size() : 0;
		if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
			String msgId = null;
			if (msgs != null && msgs.size() > 0) {
				msgId = msgs.get(0).getMsgId();
			}
			if (chatType == CHATTYPE_SINGLE) {
				conversation.loadMoreMsgFromDB(msgId, pagesize);
			} else {
				conversation.loadMoreGroupMsgFromDB(msgId, pagesize);
			}
		}

		EMChatManager.getInstance().addChatRoomChangeListener(new EMChatRoomChangeListener(){

			@Override
			public void onChatRoomDestroyed(String roomId, String roomName) {
				if(roomId.equals(toChatUsername)){
					finish();
				}
			}

			@Override
			public void onMemberJoined(String roomId, String participant) {                
			}

			@Override
			public void onMemberExited(String roomId, String roomName,
					String participant) {

			}

			@Override
			public void onMemberKicked(String roomId, String roomName,
					String participant) {
				if(roomId.equals(toChatUsername)){
					String curUser = EMChatManager.getInstance().getCurrentUser();
					if(curUser.equals(participant)){
						EMChatManager.getInstance().leaveChatRoom(toChatUsername);
						finish();
					}
				}
			}

		});

	}
	private void onGroupViewCreation() {

		group = EMGroupManager.getInstance().getGroup(toChatUsername);

		if (group != null){
			((TextView) findViewById(R.id.name)).setText(group.getGroupName());
		}else{
			((TextView) findViewById(R.id.name)).setText(toChatUsername);
		}

		// 监听当前会话的群聊解散被T事件
		//        groupListener = new GroupListener();
		//        EMGroupManager.getInstance().addGroupChangeListener(groupListener);

	}
	private void diplayMsg() {
		EMChatManager.getInstance().registerEventListener(new EMEventListener() {

			@Override
			public void onEvent(EMNotifierEvent event) {

				switch (event.getEvent()) {	
				case EventNewMessage: // 接收新消息
				{
					EMMessage message = (EMMessage) event.getData();
					adapter.refreshSelectLast();
					break;
				}
				case EventDeliveryAck:{//接收已发送回执

					break;
				}

				case EventNewCMDMessage:{//接收透传消息

					break;
				}

				case EventReadAck:{//接收已读回执

					break;
				}

				case EventOfflineMessage: {//接收离线消息
					List<EMMessage> messages = (List<EMMessage>) event.getData();
					adapter.refreshSelectLast();
					break;
				}

				case EventConversationListChanged: {//通知会话列表通知event注册（在某些特殊情况，SDK去删除会话的时候会收到回调监听）

					break;
				}

				default:
					break;
				}
			}
		});
		EMChat.getInstance().setAppInited();
		refresh();
	}
	private void unitGroup() {
		chatType = getIntent().getIntExtra("chatType", CHATTYPE_SINGLE);

		if (chatType == CHATTYPE_SINGLE) { // 单聊
			toChatUsername = getIntent().getStringExtra("userId");
			((TextView) findViewById(R.id.name)).setText(toChatUsername);
		} else {
			// 群聊
			findViewById(R.id.container_to_group).setVisibility(View.VISIBLE);
			findViewById(R.id.container_remove).setVisibility(View.GONE);
			findViewById(R.id.container_voice_call).setVisibility(View.GONE);
			findViewById(R.id.container_video_call).setVisibility(View.GONE);
			toChatUsername = getIntent().getStringExtra("groupId");

			if(chatType == CHATTYPE_GROUP){
				//	                onGroupViewCreation();
			}
		}
	}
	private boolean isloading;
	private boolean haveMoreData = true;
	private final int pagesize = 20;
	private ImageView locationImgview;
	private RelativeLayout edittext_layout;
	private View more;
	private Button btn_more;
	private View buttonPressToSpeak;
	private View recordingContainer;
	private TextView recordingHint;
	private VoiceRecorder voiceRecorder;
	private Drawable[] micImages;
	private void refresh() {
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.chat_swipe_layout);
		swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
				android.R.color.holo_orange_light, android.R.color.holo_red_light);

		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						if (listView.getFirstVisiblePosition() == 0 && !isloading && haveMoreData) {
							List<EMMessage> messages;
							try {
								if (chatType == CHATTYPE_SINGLE){
									messages = conversation.loadMoreMsgFromDB(adapter.getItem(0).getMsgId(), pagesize);
									Log.e("message +  conversation", "   "+   conversation);
								}
								else{
									messages = conversation.loadMoreGroupMsgFromDB(adapter.getItem(0).getMsgId(), pagesize);
								}
							} catch (Exception e1) {
								swipeRefreshLayout.setRefreshing(false);
								return;
							}

							if (messages.size() > 0) {
								adapter.notifyDataSetChanged();
								adapter.refreshSeekTo(messages.size() - 1);
								if (messages.size() != pagesize){
									haveMoreData = false;
								}
							} else {
								haveMoreData = false;
							}

							isloading = false;

						}else{
//							Toast.makeText(ChatActivity.this, getResources().getString(R.string.no_more_messages), Toast.LENGTH_SHORT).show();
							Toast.makeText(ChatActivity.this,"抱歉，没有更多的信息哦~", Toast.LENGTH_SHORT).show();
						}
						swipeRefreshLayout.setRefreshing(false);
					}
				}, 1000);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		activityInstance = null;
		//	        if(groupListener != null){
		//	            EMGroupManager.getInstance().removeGroupChangeListener(groupListener);
		//	        }
	}

	@Override
	protected void onResume() {
		super.onResume();


		//        if (group != null)
		//            ((TextView) findViewById(R.id.name)).setText(group.getGroupName());
		//        voiceCallBtn.setEnabled(true);
		//        videoCallBtn.setEnabled(true);

		if(adapter != null){
			adapter.refresh();
		}

		DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper.getInstance();
		sdkHelper.pushActivity(this);
		// register the event listener when enter the foreground
		EMChatManager.getInstance().registerEventListener(
				this,
				new EMNotifierEvent.Event[] { EMNotifierEvent.Event.EventNewMessage,EMNotifierEvent.Event.EventOfflineMessage,
						EMNotifierEvent.Event.EventDeliveryAck, EMNotifierEvent.Event.EventReadAck });
		EMChat.getInstance().setAppInited();
	}

	private void unitUI() {
		
		findViewById(R.id.tv_detail).setOnClickListener(this);//详情
		findViewById(R.id.rela_more).setOnClickListener(this);
		findViewById(R.id.tv_back).setOnClickListener(this);
		picture_source_rela = (RelativeLayout) findViewById(R.id.picture_source_rela);
		picture_source_rela.setOnClickListener(this);
		findViewById(R.id.select_picsource).setOnClickListener(this);
		findViewById(R.id.take_photo_rela).setOnClickListener(this);
		findViewById(R.id.locpicture_rela).setOnClickListener(this);
		findViewById(R.id.rela_cancle).setOnClickListener(this);
		findViewById(R.id.iv_more).setOnClickListener(this);//拍照、 图片库
		
		send = (Button) findViewById(R.id.send);
		input = (PasteEditText) findViewById(R.id.et_sendmessage);
		input.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if("".equals(s.toString())){
					send.setVisibility(View.GONE);
				}else{
					send.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		send_expression = (ImageView) findViewById(R.id.send_expression);
		display_expression = (LinearLayout) findViewById(R.id.display_expression);
		btn_more = (Button) findViewById(R.id.btn_more);
		btn_press_to_speak = (LinearLayout) findViewById(R.id.btn_press_to_speak);
		recordingContainer = findViewById(R.id.recording_container);
		recordingHint = (TextView) findViewById(R.id.recording_hint);
		micImage = (ImageView) findViewById(R.id.mic_image);
		more = findViewById(R.id.more);
		moreItem = (LinearLayout) findViewById(R.id.moreItem);
		expressionViewpager = (ViewPager) findViewById(R.id.vPager);
		buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
		buttonSetModeKeyboard.setOnClickListener(this);
		edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
		buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
		btn_more.setOnClickListener(this);
		send_expression.setOnClickListener(this);
		findViewById(R.id.photo).setOnClickListener(this);
		findViewById(R.id.picture).setOnClickListener(this);
		findViewById(R.id.file).setOnClickListener(this);
		findViewById(R.id.btn_video).setOnClickListener(this);
		findViewById(R.id.btn_voice_call).setOnClickListener(this);
		findViewById(R.id.container_remove).setOnClickListener(this);
		findViewById(R.id.btn_video_call).setOnClickListener(this);
		btn_set_mode_voice = (Button) findViewById(R.id.btn_set_mode_voice);
		btn_set_mode_voice.setOnClickListener(this);
		locationImgview = (ImageView) findViewById(R.id.btn_location);
		locationImgview.setOnClickListener(this);
		buttonPressToSpeak.setOnTouchListener(new PressToSpeakListen());
		display_expression.setVisibility(View.GONE);
		moreItem.setVisibility(View.GONE);
		input.setOnClickListener(this);

		// 动画资源文件,用于录制语音时
		micImages = new Drawable[] { getResources().getDrawable(R.drawable.record_animate_01),
				getResources().getDrawable(R.drawable.record_animate_02),
				getResources().getDrawable(R.drawable.record_animate_03),
				getResources().getDrawable(R.drawable.record_animate_04),
				getResources().getDrawable(R.drawable.record_animate_05),
				getResources().getDrawable(R.drawable.record_animate_06),
				getResources().getDrawable(R.drawable.record_animate_07),
				getResources().getDrawable(R.drawable.record_animate_08),
				getResources().getDrawable(R.drawable.record_animate_09),
				getResources().getDrawable(R.drawable.record_animate_10),
				getResources().getDrawable(R.drawable.record_animate_11),
				getResources().getDrawable(R.drawable.record_animate_12),
				getResources().getDrawable(R.drawable.record_animate_13),
				getResources().getDrawable(R.drawable.record_animate_14), };


		reslist = getExpressionRes(35);
		// 初始化表情viewpager
		List<View> views = new ArrayList<View>();
		View gv1 = getGridChildView(1);
		View gv2 = getGridChildView(2);
		views.add(gv1);
		views.add(gv2);
		voiceRecorder = new VoiceRecorder(micImageHandler);
		expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));
	}
	class PressToSpeakListen implements View.OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (!CommonUtils.isExitsSdcard()) {
					String st4 = getResources().getString(R.string.Send_voice_need_sdcard_support);
					Toast.makeText(ChatActivity.this, st4, Toast.LENGTH_SHORT).show();
					return false;
				}
				try {
					v.setPressed(true);
					wakeLock.acquire();
					if (VoicePlayClickListener.isPlaying)
						VoicePlayClickListener.currentPlayListener.stopPlayVoice();
					recordingContainer.setVisibility(View.VISIBLE);
					recordingHint.setText(getString(R.string.move_up_to_cancel));
					recordingHint.setBackgroundColor(Color.TRANSPARENT);
					voiceRecorder.startRecording(null, toChatUsername, getApplicationContext());
				} catch (Exception e) {
					e.printStackTrace();
					v.setPressed(false);
					if (wakeLock.isHeld())
						wakeLock.release();
					if (voiceRecorder != null)
						voiceRecorder.discardRecording();
					recordingContainer.setVisibility(View.INVISIBLE);
					Toast.makeText(ChatActivity.this, R.string.recoding_fail, Toast.LENGTH_SHORT).show();
					return false;
				}

				return true;
			case MotionEvent.ACTION_MOVE: {
				if (event.getY() < 0) {
					recordingHint.setText(getString(R.string.release_to_cancel));
					recordingHint.setBackgroundResource(R.drawable.recording_text_hint_bg);
				} else {
					recordingHint.setText(getString(R.string.move_up_to_cancel));
					recordingHint.setBackgroundColor(Color.TRANSPARENT);
				}
				return true;
			}
			case MotionEvent.ACTION_UP:
				v.setPressed(false);
				recordingContainer.setVisibility(View.INVISIBLE);
				if (wakeLock.isHeld())
					wakeLock.release();
				if (event.getY() < 0) {
					// discard the recorded audio.
					voiceRecorder.discardRecording();

				} else {
					// stop recording and send voice file
					String st1 = getResources().getString(R.string.Recording_without_permission);
					String st2 = getResources().getString(R.string.The_recording_time_is_too_short);
					String st3 = getResources().getString(R.string.send_failure_please);
					try {
						int length = voiceRecorder.stopRecoding();
						if (length > 0) {
							sendVoice(voiceRecorder.getVoiceFilePath(), voiceRecorder.getVoiceFileName(toChatUsername),
									Integer.toString(length), false);
						} else if (length == EMError.INVALID_FILE) {
							Toast.makeText(getApplicationContext(), st1, Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(), st2, Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(ChatActivity.this, st3, Toast.LENGTH_SHORT).show();
					}

				}
				return true;
			default:
				recordingContainer.setVisibility(View.INVISIBLE);
				if (voiceRecorder != null)
					voiceRecorder.discardRecording();
				return false;
			}
		}
	}

	public List<String> getExpressionRes(int getSum) {
		List<String> reslist = new ArrayList<String>();
		for (int x = 1; x <= getSum; x++) {
			String filename = "ee_" + x;

			reslist.add(filename);

		}
		return reslist;

	}

	private View getGridChildView(int i) {
		View view = View.inflate(this, R.layout.expression_gridview, null);
		GridView gv = (GridView) view.findViewById(R.id.gridview);
		List<String> list = new ArrayList<String>();
		if (i == 1) {
			List<String> list1 = reslist.subList(0, 20);
			list.addAll(list1);
		} else if (i == 2) {
			list.addAll(reslist.subList(20, reslist.size()));
		}
		list.add("delete_expression");
		final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this, 1, list);
		gv.setAdapter(expressionAdapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String filename = expressionAdapter.getItem(position);
				try {
					// 文字输入框可见时，才可输入表情
					// 按住说话可见，不让输入表情
					if (buttonSetModeKeyboard.getVisibility() != View.VISIBLE) {

						if (filename != "delete_expression") { // 不是删除键，显示表情
							// 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
							Class clz = Class.forName("com.example.huanxin.SmileUtils");
							Field field = clz.getField(filename);
							input.append(SmileUtils.getSmiledText(ChatActivity.this,
									(String) field.get(null)));
						} else { // 删除文字或者表情
							if (!TextUtils.isEmpty(input.getText())) {

								int selectionStart = input.getSelectionStart();// 获取光标的位置
								if (selectionStart > 0) {
									String body = input.getText().toString();
									String tempStr = body.substring(0, selectionStart);
									int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
									if (i != -1) {
										CharSequence cs = tempStr.substring(i, selectionStart);
										if (SmileUtils.containsKey(cs.toString()))
											input.getEditableText().delete(i, selectionStart);
										else
											input.getEditableText().delete(selectionStart - 1,
													selectionStart);
									} else {
										input.getEditableText().delete(selectionStart - 1, selectionStart);
									}
								}
							}

						}
					}
				} catch (Exception e) {
				}

			}
		});
		return view;
	}

	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			//消息id
			String msgId = intent.getStringExtra("msgid");
			//发消息的人的username(userid)
			String msgFrom = intent.getStringExtra("from");
			//消息类型，文本，图片，语音消息等,这里返回的值为msg.type.ordinal()。
			//所以消息type实际为是enum类型
			int msgType = intent.getIntExtra("type", 0);
			Log.d("main", "new message id:" + msgId + " from:" + msgFrom + " type:" + msgType);
			//更方便的方法是通过msgId直接获取整个message
			EMMessage message = EMChatManager.getInstance().getMessage(msgId);
			adapter.refreshSelectLast();
			conversation.addMessage(message);
			adapter.notifyDataSetChanged();
			listView.setAdapter(adapter);
			listView.setSelection(listView.getCount()-1);
		}
	}


	class MyList extends BaseAdapter{

		private final static String TAG = "msg";

		private static final int MESSAGE_TYPE_RECV_TXT = 0;
		private static final int MESSAGE_TYPE_SENT_TXT = 1;
		private static final int MESSAGE_TYPE_SENT_IMAGE = 2;
		private static final int MESSAGE_TYPE_SENT_LOCATION = 3;
		private static final int MESSAGE_TYPE_RECV_LOCATION = 4;
		private static final int MESSAGE_TYPE_RECV_IMAGE = 5;
		private static final int MESSAGE_TYPE_SENT_VOICE = 6;
		private static final int MESSAGE_TYPE_RECV_VOICE = 7;
		private static final int MESSAGE_TYPE_SENT_VIDEO = 8;
		private static final int MESSAGE_TYPE_RECV_VIDEO = 9;
		private static final int MESSAGE_TYPE_SENT_FILE = 10;
		private static final int MESSAGE_TYPE_RECV_FILE = 11;
		private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 12;
		private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 13;
		private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 14;
		private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 15;

		public static final String IMAGE_DIR = "chat/image/";
		public static final String VOICE_DIR = "chat/audio/";
		public static final String VIDEO_DIR = "chat/video";

		private String username;
		private LayoutInflater inflater;
		private Activity activity;

		private static final int HANDLER_MESSAGE_REFRESH_LIST = 0;
		private static final int HANDLER_MESSAGE_SELECT_LAST = 1;
		private static final int HANDLER_MESSAGE_SEEK_TO = 2;

		// reference to conversation object in chatsdk
		private EMConversation conversation;
		EMMessage[] messages = null;

		private Context context;

		private Map<String, Timer> timers = new Hashtable<String, Timer>();

		public MyList(Context context, String username, int chatType) {
			this.username = username;
			this.context = context;
			inflater = LayoutInflater.from(context);
			activity = (Activity) context;
			this.conversation = EMChatManager.getInstance().getConversation(username);
		}

		Handler handler = new Handler() {
			private void refreshList() {
				// UI线程不能直接使用conversation.getAllMessages()
				// 否则在UI刷新过程中，如果收到新的消息，会导致并发问题
				messages = (EMMessage[]) conversation.getAllMessages().toArray(new EMMessage[conversation.getAllMessages().size()]);
				for (int i = 0; i < messages.length; i++) {
					// getMessage will set message as read status
					conversation.getMessage(i);
				}
				notifyDataSetChanged();
			}

			@Override
			public void handleMessage(android.os.Message message) {
				switch (message.what) {
				case HANDLER_MESSAGE_REFRESH_LIST:
					refreshList();
					break;
				case HANDLER_MESSAGE_SELECT_LAST:
					if (activity instanceof ChatActivity) {
						ListView listView = ((ChatActivity)activity).getListView();
						if (messages.length > 0) {
							listView.setSelection(messages.length - 1);
						}
					}
					break;
				case HANDLER_MESSAGE_SEEK_TO:
					int position = message.arg1;
					if (activity instanceof ChatActivity) {
						ListView listView = ((ChatActivity)activity).getListView();
						listView.setSelection(position);
					}
					break;
				default:
					break;
				}
			}
		};


		/**
		 * 获取item数
		 */
		public int getCount() {
			return messages == null ? 0 : messages.length;
		}

		/**
		 * 刷新页面
		 */
		public void refresh() {
			if (handler.hasMessages(HANDLER_MESSAGE_REFRESH_LIST)) {
				return;
			}
			android.os.Message msg = handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST);
			handler.sendMessage(msg);
		}

		/**
		 * 刷新页面, 选择最后一个
		 */
		public void refreshSelectLast() {
			handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST));
			handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_SELECT_LAST));
		}

		/**
		 * 刷新页面, 选择Position
		 */
		public void refreshSeekTo(int position) {
			handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST));
			android.os.Message msg = handler.obtainMessage(HANDLER_MESSAGE_SEEK_TO);
			msg.arg1 = position;
			handler.sendMessage(msg);
		}

		public EMMessage getItem(int position) {
			if (messages != null && position < messages.length) {
				return messages[position];
			}
			return null;
		}

		public long getItemId(int position) {
			return position;
		}

		/**
		 * 获取item类型数
		 */
		public int getViewTypeCount() {
			return 16;
		}

		/**
		 * 获取item类型
		 */
		public int getItemViewType(int position) {
			EMMessage message = getItem(position); 
			if (message == null) {
				return -1;
			}
			if (message.getType() == EMMessage.Type.TXT) {
				if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false))
					return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL : MESSAGE_TYPE_SENT_VOICE_CALL;
				else if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false))
					return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO_CALL : MESSAGE_TYPE_SENT_VIDEO_CALL;
				return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_TXT : MESSAGE_TYPE_SENT_TXT;
			}
			if (message.getType() == EMMessage.Type.IMAGE) {
				return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_IMAGE : MESSAGE_TYPE_SENT_IMAGE;

			}
			if (message.getType() == EMMessage.Type.LOCATION) {
				return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_LOCATION : MESSAGE_TYPE_SENT_LOCATION;
			}
			if (message.getType() == EMMessage.Type.VOICE) {
				return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE : MESSAGE_TYPE_SENT_VOICE;
			}
			if (message.getType() == EMMessage.Type.VIDEO) {
				return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO : MESSAGE_TYPE_SENT_VIDEO;
			}
			if (message.getType() == EMMessage.Type.FILE) {
				return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_FILE : MESSAGE_TYPE_SENT_FILE;
			}

			return -1;// invalid
		}


		private View createViewByMessage(EMMessage message, int position) {
			switch (message.getType()) {
			case LOCATION:
				return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_location, null) : inflater.inflate(
						R.layout.row_sent_location, null);
			case IMAGE:
				return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_picture, null) : inflater.inflate(
						R.layout.row_sent_picture, null);

			case VOICE:
				return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_voice, null) : inflater.inflate(
						R.layout.row_sent_voice, null);
			case VIDEO:
				return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_video, null) : inflater.inflate(
						R.layout.row_sent_video, null);
			case FILE:
				return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_file, null) : inflater.inflate(
						R.layout.row_sent_file, null);
			default:
				// 语音通话
				if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false))
					return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_voice_call, null) : inflater
							.inflate(R.layout.row_sent_voice_call, null);
					// 视频通话
					else if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false))
						return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_video_call, null) : inflater
								.inflate(R.layout.row_sent_video_call, null);
						return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_message, null) : inflater.inflate(
								R.layout.row_sent_message, null);
			}
		}

		@SuppressLint({ "NewApi", "SimpleDateFormat" })
		public View getView(final int position, View convertView, ViewGroup parent) {
			final EMMessage message = getItem(position);
			ChatType chatType = message.getChatType();
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = createViewByMessage(message, position);
				if (message.getType() == EMMessage.Type.IMAGE) {
					try {
						holder.iv = ((ImageView) convertView.findViewById(R.id.iv_sendPicture));
						holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
						holder.tv = (TextView) convertView.findViewById(R.id.percentage);
						holder.pb = (ProgressBar) convertView.findViewById(R.id.progressBar);
						holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
						holder.tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);
						holder.tv_sendtime = (TextView) convertView.findViewById(R.id.tv_sendtime);
						holder.tv_yymmdd = (TextView) convertView.findViewById(R.id.tv_yymmdd);
						holder.tv_hhmm = (TextView) convertView.findViewById(R.id.tv_hhmm);
					} catch (Exception e) {
					}

				} else if (message.getType() == EMMessage.Type.TXT) {

					try {
						holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
						holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
						holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
						// 这里是文字内容
						holder.tv = (TextView) convertView.findViewById(R.id.tv_chatcontent);
						holder.tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);
						holder.tv_sendtime = (TextView) convertView.findViewById(R.id.tv_sendtime);
						holder.tv_yymmdd = (TextView) convertView.findViewById(R.id.tv_yymmdd);
						holder.tv_hhmm = (TextView) convertView.findViewById(R.id.tv_hhmm);
					} catch (Exception e) {
					}

					// 语音通话及视频通话
					if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)
							|| message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
						holder.iv = (ImageView) convertView.findViewById(R.id.iv_call_icon);
						holder.tv = (TextView) convertView.findViewById(R.id.tv_chatcontent);
					}

				} else if (message.getType() == EMMessage.Type.VOICE) {
					try {
						holder.iv = ((ImageView) convertView.findViewById(R.id.iv_voice));
						holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
						holder.tv = (TextView) convertView.findViewById(R.id.tv_length);
						holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
						holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
						holder.tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);
						holder.iv_read_status = (ImageView) convertView.findViewById(R.id.iv_unread_voice);
					} catch (Exception e) {
					}
				} else if (message.getType() == EMMessage.Type.LOCATION) {
					try {
						holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
						holder.tv = (TextView) convertView.findViewById(R.id.tv_location);
						holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
						holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
						holder.tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);
					} catch (Exception e) {
					}
				} else if (message.getType() == EMMessage.Type.VIDEO) {
					try {
						holder.iv = ((ImageView) convertView.findViewById(R.id.chatting_content_iv));
						holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
						holder.tv = (TextView) convertView.findViewById(R.id.percentage);
						holder.pb = (ProgressBar) convertView.findViewById(R.id.progressBar);
						holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
						holder.size = (TextView) convertView.findViewById(R.id.chatting_size_iv);
						holder.timeLength = (TextView) convertView.findViewById(R.id.chatting_length_iv);
						holder.playBtn = (ImageView) convertView.findViewById(R.id.chatting_status_btn);
						holder.container_status_btn = (LinearLayout) convertView.findViewById(R.id.container_status_btn);
						holder.tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);

					} catch (Exception e) {
					}
				} else if (message.getType() == EMMessage.Type.FILE) {
					try {
						holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_userhead);
						holder.tv_file_name = (TextView) convertView.findViewById(R.id.tv_file_name);
						holder.tv_file_size = (TextView) convertView.findViewById(R.id.tv_file_size);
						holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
						holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
						holder.tv_file_download_state = (TextView) convertView.findViewById(R.id.tv_file_state);
						holder.ll_container = (LinearLayout) convertView.findViewById(R.id.ll_file_container);
						// 这里是进度值
						holder.tv = (TextView) convertView.findViewById(R.id.percentage);
					} catch (Exception e) {
					}
					try {
						holder.tv_usernick = (TextView) convertView.findViewById(R.id.tv_userid);
					} catch (Exception e) {
					}

				}

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			// 群聊时，显示接收的消息的发送人的名称
			if ((chatType == ChatType.GroupChat || chatType == chatType.ChatRoom) && message.direct == EMMessage.Direct.RECEIVE){
				//demo里使用username代码nick
				holder.tv_usernick.setText(message.getFrom());
			}
			// 单聊时，显示接收的消息的发送人的名称
			if ((chatType == ChatType.Chat) && message.direct == EMMessage.Direct.RECEIVE){
				//demo里使用username代码nick
				holder.tv_usernick.setText(message.getFrom());
			}
			// 如果是发送的消息并且不是群聊消息，显示已读textview
			if (!(chatType == ChatType.GroupChat || chatType == chatType.ChatRoom) && message.direct == EMMessage.Direct.SEND) {
				holder.tv_ack = (TextView) convertView.findViewById(R.id.tv_ack);
				holder.tv_delivered = (TextView) convertView.findViewById(R.id.tv_delivered);
				
				if (holder.tv_ack != null) {
					if (message.isAcked) {
						if (holder.tv_delivered != null) {
							holder.tv_delivered.setVisibility(View.INVISIBLE);
						}
//						不显示读取状态
//						holder.tv_ack.setVisibility(View.VISIBLE);
					} else {
						holder.tv_ack.setVisibility(View.INVISIBLE);
						// check and display msg delivered ack status
						if (holder.tv_delivered != null) {
							if (message.isDelivered) {
								holder.tv_delivered.setVisibility(View.VISIBLE);
							} else {
								holder.tv_delivered.setVisibility(View.INVISIBLE);
							}
						}
					}
				}
			} else {
				// 如果是文本或者地图消息并且不是group messgae,chatroom message，显示的时候给对方发送已读回执
				if ((message.getType() == Type.TXT || message.getType() == Type.LOCATION) && !message.isAcked && chatType != ChatType.GroupChat && chatType != ChatType.ChatRoom) {
					// 不是语音通话记录
					if (!message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
						try {
							EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
							// 发送已读回执
							message.isAcked = true;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}

			//设置用户头像
			setUserAvatar(message, holder.iv_avatar);

			switch (message.getType()) {
			// 根据消息type显示item
			case IMAGE: // 图片
				handleImageMessage(message, holder, position, convertView);
				break;
			case TXT: // 文本
				if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)
						|| message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false))
					// 音视频通话
					handleCallMessage(message, holder, position);
				else
					handleTextMessage(message, holder, position);
				break;
			case LOCATION: // 位置
				handleLocationMessage(message, holder, position, convertView);
				break;
			case VOICE: // 语音
				handleVoiceMessage(message, holder, position, convertView);
				break;
			case VIDEO: // 视频
				handleVideoMessage(message, holder, position, convertView);
				break;
			case FILE: // 一般文件
				handleFileMessage(message, holder, position, convertView);
				break;
			default:
				// not supported
			}

			if (message.direct == EMMessage.Direct.SEND) {
				View statusView = convertView.findViewById(R.id.msg_status);
				// 重发按钮点击事件
				statusView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						// 显示重发消息的自定义alertdialog
						Intent intent = new Intent(activity, AlertDialog.class);
						intent.putExtra("msg", activity.getString(R.string.confirm_resend));
						intent.putExtra("title", activity.getString(R.string.resend));
						intent.putExtra("cancel", true);
						intent.putExtra("position", position);
						if (message.getType() == EMMessage.Type.TXT)
							activity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_TEXT);
						else if (message.getType() == EMMessage.Type.VOICE)
							activity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_VOICE);
						else if (message.getType() == EMMessage.Type.IMAGE)
							activity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_PICTURE);
						else if (message.getType() == EMMessage.Type.LOCATION)
							activity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_LOCATION);
						else if (message.getType() == EMMessage.Type.FILE)
							activity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_FILE);
						else if (message.getType() == EMMessage.Type.VIDEO)
							activity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_VIDEO);

					}
				});

			} else {
				final String st = context.getResources().getString(R.string.Into_the_blacklist);
				if(chatType != ChatType.ChatRoom){
					// 长按头像，移入黑名单
					holder.iv_avatar.setOnLongClickListener(new OnLongClickListener() {

						@Override
						public boolean onLongClick(View v) {
							Intent intent = new Intent(activity, AlertDialog.class);
							intent.putExtra("msg", st);
							intent.putExtra("cancel", true);
							intent.putExtra("position", position);
							activity.startActivityForResult(intent, ChatActivity.REQUEST_CODE_ADD_TO_BLACKLIST);
							return true;
						}
					});
				}
			}

			TextView timestamp = (TextView) convertView.findViewById(R.id.timestamp);
			
			SimpleDateFormat format=new SimpleDateFormat("yy/M/d");
			String yymmdd=format.format(new Date(message.getMsgTime()));
			SimpleDateFormat format2=new SimpleDateFormat("HH:mm");
			String HHmm=format2.format(new Date(message.getMsgTime()));
			if(holder.tv_yymmdd!=null){
				holder.tv_yymmdd.setText(yymmdd);
			}
			if(holder.tv_hhmm!=null){
				holder.tv_hhmm.setText(HHmm);
			}

//			if (position == 0) {
//				timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
//				timestamp.setVisibility(View.VISIBLE);
//			} else {
//				// 两条消息时间离得如果稍长，显示时间
//				EMMessage prevMessage = getItem(position - 1);
//				if (prevMessage != null && DateUtils.isCloseEnough(message.getMsgTime(), prevMessage.getMsgTime())) {
//					timestamp.setVisibility(View.GONE);
//				} else {
//					timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
//					timestamp.setVisibility(View.VISIBLE);
//				}
//			}
			return convertView;
		}


		/**
		 * 显示用户头像
		 * @param message
		 * @param imageView
		 */
		private void setUserAvatar(EMMessage message, ImageView imageView){
			if(message.direct == Direct.SEND){
				//显示自己头像
				//	            UserUtils.setUserAvatar(context, EMChatManager.getInstance().getCurrentUser(), imageView);
				//?????????????????????????????????????????????????????????????????????????????????????????????????
				String completeURL1 = PreferenceUtils.readImageCache(ChatActivity.this);
				Log.e("ChatActivity", "imageCachePath=="+completeURL1);
//				String completeURL1=beginStr+imageCachePath+endStr;
				ImageLoaderUtils.getInstance().LoadImage(ChatActivity.this, completeURL1, imageView);
			}else{
				//	            UserUtils.setUserAvatar(context, message.getFrom(), imageView);
				String completeUrl2=beginStr+avatar_path+endStr;
				ImageLoaderUtils.getInstance().LoadImage(ChatActivity.this, completeUrl2, imageView);
			}
		}

		/**
		 * 文本消息
		 * 
		 * @param message
		 * @param holder
		 * @param position
		 */
		private void handleTextMessage(EMMessage message, ViewHolder holder, final int position) {
			TextMessageBody txtBody = (TextMessageBody) message.getBody();
			Spannable span = SmileUtils.getSmiledText(context, txtBody.getMessage());
			// 设置内容
			holder.tv.setText(span, BufferType.SPANNABLE);
			// 设置长按事件监听
			holder.tv.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					activity.startActivityForResult(
							(new Intent(activity, ContextMenu.class)).putExtra("position", position).putExtra("type",
									EMMessage.Type.TXT.ordinal()), ChatActivity.REQUEST_CODE_CONTEXT_MENU);
					return true;
				}
			});

			if (message.direct == EMMessage.Direct.SEND) {
				switch (message.status) {
				case SUCCESS: // 发送成功
					holder.pb.setVisibility(View.GONE);
					holder.staus_iv.setVisibility(View.GONE);
					//显示发送时间
					SimpleDateFormat format2=new SimpleDateFormat("HH:mm");
					String HHmm=format2.format(new Date(message.getMsgTime()));
					holder.tv_sendtime .setVisibility(View.VISIBLE);
					if(holder.tv_sendtime !=null){
						holder.tv_sendtime .setText(HHmm);
					}

					break;
				case FAIL: // 发送失败
					holder.pb.setVisibility(View.GONE);
					holder.staus_iv.setVisibility(View.VISIBLE);
					//不显示发送时间
					if(holder.tv_sendtime !=null){
						holder.tv_sendtime .setVisibility(View.GONE);
					}
					break;
				case INPROGRESS: // 发送中
					Log.e("ChatAc", "发送中=======");//此处并没有调用
					holder.pb.setVisibility(View.VISIBLE);
					holder.staus_iv.setVisibility(View.GONE);
//					不显示发送时间
					if(holder.tv_sendtime !=null){
						Log.e("ChatAc", "holder.tv_sendtime+++++++holder.tv_sendtime+++++++holder.tv_sendtime");
						holder.tv_sendtime .setVisibility(View.GONE);
					}
					break;
				default:
					// 发送消息
					sendMsgInBackground(message, holder);
				}
			}
		}

		/**
		 * 音视频通话记录
		 * 
		 * @param message
		 * @param holder
		 * @param position
		 */
		private void handleCallMessage(EMMessage message, ViewHolder holder, final int position) {
			TextMessageBody txtBody = (TextMessageBody) message.getBody();
			holder.tv.setText(txtBody.getMessage());

		}

		/**
		 * 图片消息
		 * 
		 * @param message
		 * @param holder
		 * @param position
		 * @param convertView
		 */
		private void handleImageMessage(final EMMessage message, final ViewHolder holder, final int position, View convertView) {
			holder.pb.setTag(position);
			holder.iv.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					activity.startActivityForResult(
							(new Intent(activity, ContextMenu.class)).putExtra("position", position).putExtra("type",
									EMMessage.Type.IMAGE.ordinal()), ChatActivity.REQUEST_CODE_CONTEXT_MENU);
					return true;
				}
			});

			// 接收方向的消息
			if (message.direct == EMMessage.Direct.RECEIVE) {
				// "it is receive msg";
				if (message.status == EMMessage.Status.INPROGRESS) {
					// "!!!! back receive";
					holder.iv.setImageResource(R.drawable.default_image);
					showDownloadImageProgress(message, holder);
					// downloadImage(message, holder);
//					不显示发送时间
					if(holder.tv_sendtime !=null){
						holder.tv_sendtime .setVisibility(View.GONE);
					}
				} else {
					// "!!!! not back receive, show image directly");
					holder.pb.setVisibility(View.GONE);
					holder.tv.setVisibility(View.GONE);
					holder.iv.setImageResource(R.drawable.default_image);
					ImageMessageBody imgBody = (ImageMessageBody) message.getBody();
					if (imgBody.getLocalUrl() != null) {
						// String filePath = imgBody.getLocalUrl();
						String remotePath = imgBody.getRemoteUrl();
						String filePath = ImageUtils.getImagePath(remotePath);
						String thumbRemoteUrl = imgBody.getThumbnailUrl();
						if(TextUtils.isEmpty(thumbRemoteUrl)&&!TextUtils.isEmpty(remotePath)){
							thumbRemoteUrl = remotePath;
						}
						String thumbnailPath = ImageUtils.getThumbnailImagePath(thumbRemoteUrl);
						showImageView(thumbnailPath, holder.iv, filePath, imgBody.getRemoteUrl(), message);
					}
				}
				return;
			}

			// 发送的消息
			// process send message
			// send pic, show the pic directly
			ImageMessageBody imgBody = (ImageMessageBody) message.getBody();
			String filePath = imgBody.getLocalUrl();
			if (filePath != null && new File(filePath).exists()) {
				showImageView(ImageUtils.getThumbnailImagePath(filePath), holder.iv, filePath, null, message);
			} else {
				showImageView(ImageUtils.getThumbnailImagePath(filePath), holder.iv, filePath, IMAGE_DIR, message);
			}

			switch (message.status) {
			case SUCCESS:
				holder.pb.setVisibility(View.GONE);
				holder.tv.setVisibility(View.GONE);
				holder.staus_iv.setVisibility(View.GONE);
				if(holder.tv_sendtime !=null){
					holder.tv_sendtime .setVisibility(View.VISIBLE);
				}
				break;
			case FAIL:
				holder.pb.setVisibility(View.GONE);
				holder.tv.setVisibility(View.GONE);
				holder.staus_iv.setVisibility(View.VISIBLE);
				if(holder.tv_sendtime !=null){
					holder.tv_sendtime .setVisibility(View.GONE);
				}
				break;
			case INPROGRESS:
//				不显示发送时间
				if(holder.tv_sendtime !=null){
					holder.tv_sendtime .setVisibility(View.GONE);
				}
				holder.staus_iv.setVisibility(View.GONE);
				holder.pb.setVisibility(View.VISIBLE);
				holder.tv.setVisibility(View.VISIBLE);
				if (timers.containsKey(message.getMsgId()))
					return;
				// set a timer
				final Timer timer = new Timer();
				timers.put(message.getMsgId(), timer);
				timer.schedule(new TimerTask() {

					@Override
					public void run() {
						activity.runOnUiThread(new Runnable() {
							public void run() {
								holder.pb.setVisibility(View.VISIBLE);
								holder.tv.setVisibility(View.VISIBLE);
								holder.tv.setText(message.progress + "%");
								if (message.status == EMMessage.Status.SUCCESS) {
									holder.pb.setVisibility(View.GONE);
									holder.tv.setVisibility(View.GONE);
									// message.setSendingStatus(Message.SENDING_STATUS_SUCCESS);
									timer.cancel();
								} else if (message.status == EMMessage.Status.FAIL) {
									holder.pb.setVisibility(View.GONE);
									holder.tv.setVisibility(View.GONE);
									// message.setSendingStatus(Message.SENDING_STATUS_FAIL);
									// message.setProgress(0);
									holder.staus_iv.setVisibility(View.VISIBLE);
									Toast.makeText(activity,
											activity.getString(R.string.send_fail) + activity.getString(R.string.connect_failuer_toast), 0)
											.show();
									timer.cancel();
								}

							}
						});

					}
				}, 0, 500);
				break;
			default:
				sendPictureMessage(message, holder);
			}
		}

		/**
		 * 视频消息
		 * 
		 * @param message
		 * @param holder
		 * @param position
		 * @param convertView
		 */
		private void handleVideoMessage(final EMMessage message, final ViewHolder holder, final int position, View convertView) {

			VideoMessageBody videoBody = (VideoMessageBody) message.getBody();
			// final File image=new File(PathUtil.getInstance().getVideoPath(),
			// videoBody.getFileName());
			String localThumb = videoBody.getLocalThumb();

			holder.iv.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					activity.startActivityForResult(
							new Intent(activity, ContextMenu.class).putExtra("position", position).putExtra("type",
									EMMessage.Type.VIDEO.ordinal()), ChatActivity.REQUEST_CODE_CONTEXT_MENU);
					return true;
				}
			});

			if (localThumb != null) {

				showVideoThumbView(localThumb, holder.iv, videoBody.getThumbnailUrl(), message);
			}
			if (videoBody.getLength() > 0) {
				String time = DateUtils.toTimeBySecond(videoBody.getLength());
				holder.timeLength.setText(time);
			}
			holder.playBtn.setImageResource(R.drawable.video_download_btn_nor);

			if (message.direct == EMMessage.Direct.RECEIVE) {
				if (videoBody.getVideoFileLength() > 0) {
					String size = TextFormater.getDataSize(videoBody.getVideoFileLength());
					holder.size.setText(size);
				}
			} else {
				if (videoBody.getLocalUrl() != null && new File(videoBody.getLocalUrl()).exists()) {
					String size = TextFormater.getDataSize(new File(videoBody.getLocalUrl()).length());
					holder.size.setText(size);
				}
			}

			if (message.direct == EMMessage.Direct.RECEIVE) {

				// System.err.println("it is receive msg");
				if (message.status == EMMessage.Status.INPROGRESS) {
					// System.err.println("!!!! back receive");
					holder.iv.setImageResource(R.drawable.default_image);
					showDownloadImageProgress(message, holder);

				} else {
					// System.err.println("!!!! not back receive, show image directly");
					holder.iv.setImageResource(R.drawable.default_image);
					if (localThumb != null) {
						showVideoThumbView(localThumb, holder.iv, videoBody.getThumbnailUrl(), message);
					}

				}

				return;
			}
			holder.pb.setTag(position);

			// until here ,deal with send video msg
			switch (message.status) {
			case SUCCESS:
				holder.pb.setVisibility(View.GONE);
				holder.staus_iv.setVisibility(View.GONE);
				holder.tv.setVisibility(View.GONE);
				break;
			case FAIL:
				holder.pb.setVisibility(View.GONE);
				holder.tv.setVisibility(View.GONE);
				holder.staus_iv.setVisibility(View.VISIBLE);
				break;
			case INPROGRESS:
				if (timers.containsKey(message.getMsgId()))
					return;
				// set a timer
				final Timer timer = new Timer();
				timers.put(message.getMsgId(), timer);
				timer.schedule(new TimerTask() {

					@Override
					public void run() {
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								holder.pb.setVisibility(View.VISIBLE);
								holder.tv.setVisibility(View.VISIBLE);
								holder.tv.setText(message.progress + "%");
								if (message.status == EMMessage.Status.SUCCESS) {
									holder.pb.setVisibility(View.GONE);
									holder.tv.setVisibility(View.GONE);
									// message.setSendingStatus(Message.SENDING_STATUS_SUCCESS);
									timer.cancel();
								} else if (message.status == EMMessage.Status.FAIL) {
									holder.pb.setVisibility(View.GONE);
									holder.tv.setVisibility(View.GONE);
									// message.setSendingStatus(Message.SENDING_STATUS_FAIL);
									// message.setProgress(0);
									holder.staus_iv.setVisibility(View.VISIBLE);
									Toast.makeText(activity,
											activity.getString(R.string.send_fail) + activity.getString(R.string.connect_failuer_toast), 0)
											.show();
									timer.cancel();
								}

							}
						});

					}
				}, 0, 500);
				break;
			default:
				// sendMsgInBackground(message, holder);
				sendPictureMessage(message, holder);

			}

		}

		/**
		 * 语音消息
		 * 
		 * @param message
		 * @param holder
		 * @param position
		 * @param convertView
		 */
		private void handleVoiceMessage(final EMMessage message, final ViewHolder holder, final int position, View convertView) {
			VoiceMessageBody voiceBody = (VoiceMessageBody) message.getBody();
			holder.tv.setText(voiceBody.getLength() + "\"");
			holder.iv.setOnClickListener(new VoicePlayClickListener(message, holder.iv, holder.iv_read_status, this, activity, username));
			holder.iv.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					activity.startActivityForResult(
							(new Intent(activity, ContextMenu.class)).putExtra("position", position).putExtra("type",
									EMMessage.Type.VOICE.ordinal()), ChatActivity.REQUEST_CODE_CONTEXT_MENU);
					return true;
				}
			});
			if (((ChatActivity)activity).playMsgId != null
					&& ((ChatActivity)activity).playMsgId.equals(message
							.getMsgId())&&VoicePlayClickListener.isPlaying) {
				AnimationDrawable voiceAnimation;
				if (message.direct == EMMessage.Direct.RECEIVE) {
					holder.iv.setImageResource(R.anim.voice_from_icon);
				} else {
					holder.iv.setImageResource(R.anim.voice_to_icon);
				}
				voiceAnimation = (AnimationDrawable) holder.iv.getDrawable();
				voiceAnimation.start();
			} else {
				if (message.direct == EMMessage.Direct.RECEIVE) {
					holder.iv.setImageResource(R.drawable.chatfrom_voice_playing);
				} else {
					holder.iv.setImageResource(R.drawable.chatto_voice_playing);
				}
			}


			if (message.direct == EMMessage.Direct.RECEIVE) {
				if (message.isListened()) {
					// 隐藏语音未听标志
					holder.iv_read_status.setVisibility(View.INVISIBLE);
				} else {
					holder.iv_read_status.setVisibility(View.VISIBLE);
				}
				EMLog.d(TAG, "it is receive msg");
				if (message.status == EMMessage.Status.INPROGRESS) {
					holder.pb.setVisibility(View.VISIBLE);
					EMLog.d(TAG, "!!!! back receive");
					((FileMessageBody) message.getBody()).setDownloadCallback(new EMCallBack() {

						@Override
						public void onSuccess() {
							activity.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									holder.pb.setVisibility(View.INVISIBLE);
									notifyDataSetChanged();
								}
							});

						}

						@Override
						public void onProgress(int progress, String status) {
						}

						@Override
						public void onError(int code, String message) {
							activity.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									holder.pb.setVisibility(View.INVISIBLE);
								}
							});

						}
					});

				} else {
					holder.pb.setVisibility(View.INVISIBLE);

				}
				return;
			}

			// until here, deal with send voice msg
			switch (message.status) {
			case SUCCESS:
				holder.pb.setVisibility(View.GONE);
				holder.staus_iv.setVisibility(View.GONE);
				break;
			case FAIL:
				holder.pb.setVisibility(View.GONE);
				holder.staus_iv.setVisibility(View.VISIBLE);
				break;
			case INPROGRESS:
				holder.pb.setVisibility(View.VISIBLE);
				holder.staus_iv.setVisibility(View.GONE);
				break;
			default:
				sendMsgInBackground(message, holder);
			}
		}

		/**
		 * 文件消息
		 * 
		 * @param message
		 * @param holder
		 * @param position
		 * @param convertView
		 */
		private void handleFileMessage(final EMMessage message, final ViewHolder holder, int position, View convertView) {
			final NormalFileMessageBody fileMessageBody = (NormalFileMessageBody) message.getBody();
			final String filePath = fileMessageBody.getLocalUrl();
			holder.tv_file_name.setText(fileMessageBody.getFileName());
			holder.tv_file_size.setText(TextFormater.getDataSize(fileMessageBody.getFileSize()));
			holder.ll_container.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					File file = new File(filePath);
					if (file != null && file.exists()) {
						// 文件存在，直接打开
						FileUtils.openFile(file, (Activity) context);
					} else {
						// 下载
						context.startActivity(new Intent(context, ShowNormalFileActivity.class).putExtra("msgbody", fileMessageBody));
					}
					if (message.direct == EMMessage.Direct.RECEIVE && !message.isAcked && message.getChatType() != ChatType.GroupChat && message.getChatType() != ChatType.ChatRoom) {
						try {
							EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
							message.isAcked = true;
						} catch (EaseMobException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
			String st1 = context.getResources().getString(R.string.Have_downloaded);
			String st2 = context.getResources().getString(R.string.Did_not_download);
			if (message.direct == EMMessage.Direct.RECEIVE) { // 接收的消息
				EMLog.d(TAG, "it is receive msg");
				File file = new File(filePath);
				if (file != null && file.exists()) {
					holder.tv_file_download_state.setText(st1);
				} else {
					holder.tv_file_download_state.setText(st2);
				}
				return;
			}

			// until here, deal with send voice msg
			switch (message.status) {
			case SUCCESS:
				holder.pb.setVisibility(View.INVISIBLE);
				holder.tv.setVisibility(View.INVISIBLE);
				holder.staus_iv.setVisibility(View.INVISIBLE);
				break;
			case FAIL:
				holder.pb.setVisibility(View.INVISIBLE);
				holder.tv.setVisibility(View.INVISIBLE);
				holder.staus_iv.setVisibility(View.VISIBLE);
				break;
			case INPROGRESS:
				if (timers.containsKey(message.getMsgId()))
					return;
				// set a timer
				final Timer timer = new Timer();
				timers.put(message.getMsgId(), timer);
				timer.schedule(new TimerTask() {

					@Override
					public void run() {
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								holder.pb.setVisibility(View.VISIBLE);
								holder.tv.setVisibility(View.VISIBLE);
								holder.tv.setText(message.progress + "%");
								if (message.status == EMMessage.Status.SUCCESS) {
									holder.pb.setVisibility(View.INVISIBLE);
									holder.tv.setVisibility(View.INVISIBLE);
									timer.cancel();
								} else if (message.status == EMMessage.Status.FAIL) {
									holder.pb.setVisibility(View.INVISIBLE);
									holder.tv.setVisibility(View.INVISIBLE);
									holder.staus_iv.setVisibility(View.VISIBLE);
									Toast.makeText(activity,
											activity.getString(R.string.send_fail) + activity.getString(R.string.connect_failuer_toast), 0)
											.show();
									timer.cancel();
								}

							}
						});

					}
				}, 0, 500);
				break;
			default:
				// 发送消息
				sendMsgInBackground(message, holder);
			}

		}

		/**
		 * 处理位置消息
		 * 
		 * @param message
		 * @param holder
		 * @param position
		 * @param convertView
		 */
		private void handleLocationMessage(final EMMessage message, final ViewHolder holder, final int position, View convertView) {
			TextView locationView = ((TextView) convertView.findViewById(R.id.tv_location));
			LocationMessageBody locBody = (LocationMessageBody) message.getBody();
			locationView.setText(locBody.getAddress());
			LatLng loc = new LatLng(locBody.getLatitude(), locBody.getLongitude());
			locationView.setOnClickListener(new MapClickListener(loc, locBody.getAddress()));
			locationView.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					activity.startActivityForResult(
							(new Intent(activity, ContextMenu.class)).putExtra("position", position).putExtra("type",
									EMMessage.Type.LOCATION.ordinal()), ChatActivity.REQUEST_CODE_CONTEXT_MENU);
					return false;
				}
			});

			if (message.direct == EMMessage.Direct.RECEIVE) {
				return;
			}
			// deal with send message
			switch (message.status) {
			case SUCCESS:
				holder.pb.setVisibility(View.GONE);
				holder.staus_iv.setVisibility(View.GONE);
				break;
			case FAIL:
				holder.pb.setVisibility(View.GONE);
				holder.staus_iv.setVisibility(View.VISIBLE);
				break;
			case INPROGRESS:
				holder.pb.setVisibility(View.VISIBLE);
				break;
			default:
				sendMsgInBackground(message, holder);
			}
		}

		/**
		 * 发送消息
		 * 
		 * @param message
		 * @param holder
		 * @param position
		 */
		public void sendMsgInBackground(final EMMessage message, final ViewHolder holder) {
			//这里为发送中....
			holder.staus_iv.setVisibility(View.GONE);
			holder.pb.setVisibility(View.VISIBLE);
			holder.tv_sendtime.setVisibility(View.GONE);

			final long start = System.currentTimeMillis();
			// 调用sdk发送异步发送方法
			EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

				@Override
				public void onSuccess() {

					updateSendedView(message, holder);
				}

				@Override
				public void onError(int code, String error) {

					updateSendedView(message, holder);
				}

				@Override
				public void onProgress(int progress, String status) {
					Log.e("ChatAcs", "progress==="+progress);
				}

			});

		}

		/*
		 * chat sdk will automatic download thumbnail image for the image message we
		 * need to register callback show the download progress
		 */
		private void showDownloadImageProgress(final EMMessage message, final ViewHolder holder) {
			EMLog.d(TAG, "!!! show download image progress");
			// final ImageMessageBody msgbody = (ImageMessageBody)
			// message.getBody();
			final FileMessageBody msgbody = (FileMessageBody) message.getBody();
			if(holder.pb!=null)
				holder.pb.setVisibility(View.VISIBLE);
			if(holder.tv!=null)
				holder.tv.setVisibility(View.VISIBLE);

			msgbody.setDownloadCallback(new EMCallBack() {

				@Override
				public void onSuccess() {
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// message.setBackReceive(false);
							if (message.getType() == EMMessage.Type.IMAGE) {
								holder.pb.setVisibility(View.GONE);
								holder.tv.setVisibility(View.GONE);
							}
							notifyDataSetChanged();
						}
					});
				}

				@Override
				public void onError(int code, String message) {

				}

				@Override
				public void onProgress(final int progress, String status) {
					if (message.getType() == EMMessage.Type.IMAGE) {
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								holder.tv.setText(progress + "%");

							}
						});
					}

				}

			});
		}

		/*
		 * send message with new sdk
		 */
		private void sendPictureMessage(final EMMessage message, final ViewHolder holder) {
			try {
				String to = message.getTo();

				// before send, update ui
				holder.staus_iv.setVisibility(View.GONE);
				holder.pb.setVisibility(View.VISIBLE);
				holder.tv.setVisibility(View.VISIBLE);
				holder.tv.setText("0%");

				final long start = System.currentTimeMillis();
				// if (chatType == ChatActivity.CHATTYPE_SINGLE) {
				EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

					@Override
					public void onSuccess() {
						Log.d(TAG, "send image message successfully");
						activity.runOnUiThread(new Runnable() {
							public void run() {
								// send success
								holder.pb.setVisibility(View.GONE);
								holder.tv.setVisibility(View.GONE);
								if(holder.tv_sendtime !=null){
									holder.tv_sendtime .setVisibility(View.VISIBLE);
								}
							}
						});
					}

					@Override
					public void onError(int code, String error) {

						activity.runOnUiThread(new Runnable() {
							public void run() {
								holder.pb.setVisibility(View.GONE);
								holder.tv.setVisibility(View.GONE);
								// message.setSendingStatus(Message.SENDING_STATUS_FAIL);
								holder.staus_iv.setVisibility(View.VISIBLE);
								Toast.makeText(activity,
										activity.getString(R.string.send_fail) + activity.getString(R.string.connect_failuer_toast), 0).show();
								if(holder.tv_sendtime !=null){
									holder.tv_sendtime .setVisibility(View.VISIBLE);
								}
							}
						});
					}

					@Override
					public void onProgress(final int progress, String status) {
						activity.runOnUiThread(new Runnable() {
							public void run() {
								holder.tv.setText(progress + "%");
								//此处为发送中
								if(holder.tv_sendtime !=null){
									holder.tv_sendtime .setVisibility(View.GONE);
								}
							}
						});
					}

				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * 更新ui上消息发送状态
		 * 
		 * @param message
		 * @param holder
		 */
		private void updateSendedView(final EMMessage message, final ViewHolder holder) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// send success
					if (message.getType() == EMMessage.Type.VIDEO) {
						holder.tv.setVisibility(View.GONE);
					}
					EMLog.d(TAG, "message status : " + message.status);
					if (message.status == EMMessage.Status.SUCCESS) {
						// if (message.getType() == EMMessage.Type.FILE) {
						// holder.pb.setVisibility(View.INVISIBLE);
						// holder.staus_iv.setVisibility(View.INVISIBLE);
						// } else {
						// holder.pb.setVisibility(View.GONE);
						// holder.staus_iv.setVisibility(View.GONE);
						// }

					} else if (message.status == EMMessage.Status.FAIL) {
						// if (message.getType() == EMMessage.Type.FILE) {
						// holder.pb.setVisibility(View.INVISIBLE);
						// } else {
						// holder.pb.setVisibility(View.GONE);
						// }
						// holder.staus_iv.setVisibility(View.VISIBLE);

						if(message.getError() == EMError.MESSAGE_SEND_INVALID_CONTENT){
							Toast.makeText(activity, activity.getString(R.string.send_fail) + activity.getString(R.string.error_send_invalid_content), 0)
							.show();
						}else if(message.getError() == EMError.MESSAGE_SEND_NOT_IN_THE_GROUP){
							Toast.makeText(activity, activity.getString(R.string.send_fail) + activity.getString(R.string.error_send_not_in_the_group), 0)
							.show();
						}else{
							Toast.makeText(activity, activity.getString(R.string.send_fail) + activity.getString(R.string.connect_failuer_toast), 0)
							.show();
						}
					}else if(message.status == EMMessage.Status.INPROGRESS){
						
					}

					notifyDataSetChanged();
				}
			});
		}

		/**
		 * load image into image view
		 * 
		 * @param thumbernailPath
		 * @param iv
		 * @param position
		 * @return the image exists or not
		 */
		private boolean showImageView(final String thumbernailPath, final ImageView iv, final String localFullSizePath, String remoteDir,
				final EMMessage message) {
			// String imagename =
			// localFullSizePath.substring(localFullSizePath.lastIndexOf("/") + 1,
			// localFullSizePath.length());
			// final String remote = remoteDir != null ? remoteDir+imagename :
			// imagename;
			final String remote = remoteDir;
			EMLog.d("###", "local = " + localFullSizePath + " remote: " + remote);
			// first check if the thumbnail image already loaded into cache
			Bitmap bitmap = ImageCache.getInstance().get(thumbernailPath);
			if (bitmap != null) {
				// thumbnail image is already loaded, reuse the drawable
				iv.setImageBitmap(bitmap);
				iv.setClickable(true); 
				iv.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						EMLog.d(TAG, "image view on click");
						Intent intent = new Intent(activity, ShowBigImage.class);
						File file = new File(localFullSizePath);
						if (file.exists()) {
							Uri uri = Uri.fromFile(file);
							intent.putExtra("uri", uri);
							EMLog.d(TAG, "here need to check why download everytime");
						} else {
							// The local full size pic does not exist yet.
							// ShowBigImage needs to download it from the server
							// first
							// intent.putExtra("", message.get);
							ImageMessageBody body = (ImageMessageBody) message.getBody();
							intent.putExtra("secret", body.getSecret());
							intent.putExtra("remotepath", remote);
						}
						if (message != null && message.direct == EMMessage.Direct.RECEIVE && !message.isAcked
								&& message.getChatType() != ChatType.GroupChat && message.getChatType() != ChatType.ChatRoom) {
							try {
								EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
								message.isAcked = true;
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						activity.startActivity(intent);
					}
				});
				return true;
			} else {

				new LoadImageTask().execute(thumbernailPath, localFullSizePath, remote, message.getChatType(), iv, activity, message);
				return true;
			}

		}

		/**
		 * 展示视频缩略图
		 * 
		 * @param localThumb
		 *            本地缩略图路径
		 * @param iv
		 * @param thumbnailUrl
		 *            远程缩略图路径
		 * @param message
		 */
		private void showVideoThumbView(String localThumb, ImageView iv, String thumbnailUrl, final EMMessage message) {
			// first check if the thumbnail image already loaded into cache
			Bitmap bitmap = ImageCache.getInstance().get(localThumb);
			if (bitmap != null) {
				// thumbnail image is already loaded, reuse the drawable
				iv.setImageBitmap(bitmap);
				iv.setClickable(true);
				iv.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						VideoMessageBody videoBody = (VideoMessageBody) message.getBody();
						EMLog.d(TAG, "video view is on click");
						Intent intent = new Intent(activity, ShowVideoActivity.class);
						intent.putExtra("localpath", videoBody.getLocalUrl());
						intent.putExtra("secret", videoBody.getSecret());
						intent.putExtra("remotepath", videoBody.getRemoteUrl());
						if (message != null && message.direct == EMMessage.Direct.RECEIVE && !message.isAcked
								&& message.getChatType() != ChatType.GroupChat && message.getChatType() != ChatType.ChatRoom) {
							message.isAcked = true;
							try {
								EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						activity.startActivity(intent);

					}
				});

			} else {
				new LoadVideoImageTask().execute(localThumb, thumbnailUrl, iv, activity, message, this);
			}

		}


		/*
		 * 点击地图消息listener
		 */
		class MapClickListener implements View.OnClickListener {

			LatLng location;
			String address;

			public MapClickListener(LatLng loc, String address) {
				location = loc;
				this.address = address;

			}

			@Override
			public void onClick(View v) {
				Intent intent;
				intent = new Intent(context, BaiduMapActivity.class);
				intent.putExtra("latitude", location.latitude);
				intent.putExtra("longitude", location.longitude);
				intent.putExtra("address", address);
				activity.startActivity(intent);
			}

		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.tv_back:
			finish();
			break;
		case R.id.rela_cancle:
			picture_source_rela.setVisibility(View.GONE);
			break;
		case R.id.locpicture_rela:
			picture_source_rela.setVisibility(View.GONE);
			selectPicFromLocal();
			break;
		case R.id.take_photo_rela:
			picture_source_rela.setVisibility(View.GONE);
			selectPicFromCamera();
			break;
		case R.id.picture_source_rela:
			picture_source_rela.setVisibility(View.GONE);
			break;
		case R.id.iv_more:
		case R.id.rela_more:
			hideKeyboard();
			picture_source_rela.setVisibility(View.VISIBLE);
			break;
			
		case R.id.send:
			SharedPreferences sp = getSharedPreferences("text.xml", 0);
			sendSt = sp.getBoolean("sendSt", true);
			String s = input.getText().toString();
			if(sendSt){
				sendText(s);
			}
			break;
		case R.id.send_expression:
			hideKeyboard();
			display_expression.setVisibility(View.VISIBLE);
			moreItem.setVisibility(View.GONE);
			break;
		case R.id.btn_more:
			hideKeyboard();
			display_expression.setVisibility(View.GONE);
			moreItem.setVisibility(View.VISIBLE);
			break;
		case R.id.photo:
			selectPicFromCamera();
			break;
		case R.id.picture:
			selectPicFromLocal();
			break;
		case R.id.file:
			selectFileFromLocal();
			break;
		case R.id.container_remove:
			remove();
			break;
		case R.id.btn_video_call:
			video();
			break;
		case R.id.btn_location:
			startActivityForResult(new Intent(this, BaiduMapActivity.class), REQUEST_CODE_MAP);
			break;
		case R.id.btn_voice_call:
			phone();
			break;
		case R.id.btn_video:
//			Intent intent = new Intent(ChatActivity.this, ImageGridActivity.class);
//			startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
			break;
		case R.id.btn_set_mode_voice:
			setModeVoice();
			btn_set_mode_voice.setVisibility(View.GONE);
			break;
		case R.id.et_sendmessage:
			display_expression.setVisibility(View.GONE);
			moreItem.setVisibility(View.GONE);
			break;
		case R.id.btn_set_mode_keyboard:
			//自动显示影藏软键盘
			//			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
			//			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
			buttonSetModeKeyboard.setVisibility(View.GONE);
			btn_press_to_speak.setVisibility(View.GONE);
			edittext_layout.setVisibility(View.VISIBLE);
			display_expression.setVisibility(View.GONE);
			moreItem.setVisibility(View.GONE);
			btn_set_mode_voice.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}

	}

	private void video() {
		String st1 = getResources().getString(R.string.not_connect_to_server);
		if (!EMChatManager.getInstance().isConnected())
			Toast.makeText(this, st1, 0).show();
		else{
			startActivity(new Intent(this, VideoCallActivity.class).putExtra("username", toChatUsername).putExtra(
					"isComingCall", false));
			//                videoCallBtn.setEnabled(false);
			//                toggleMore(null);
		}
	}

	private void phone() {
		String st1 = getResources().getString(R.string.not_connect_to_server);
		if (!EMChatManager.getInstance().isConnected())
			Toast.makeText(this, st1, 0).show();
		else{
			startActivity(new Intent(ChatActivity.this, VoiceCallActivity.class).putExtra("username",
					toChatUsername).putExtra("isComingCall", false));
			//                voiceCallBtn.setEnabled(false);
			//                toggleMore(null);
		}
	}

	private void remove() {
		String st5 = getResources().getString(R.string.Whether_to_empty_all_chats);
		startActivityForResult(new Intent(this, AlertDialog.class).putExtra("titleIsCancel", true).putExtra("msg", st5)
				.putExtra("cancel", true), REQUEST_CODE_EMPTY_HISTORY);
	}

	private void selectFileFromLocal() {
		Intent intent = null;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("*/*");
			intent.addCategory(Intent.CATEGORY_OPENABLE);

		} else {
			intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
	}

	public void selectPicFromLocal() {
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");

		} else {
			intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, REQUEST_CODE_LOCAL);
	}

	public void selectPicFromCamera() { 
		if (!CommonUtils.isExitsSdcard()) {
			String st = getResources().getString(R.string.sd_card_does_not_exist);
			Toast.makeText(ChatActivity.this, st, Toast.LENGTH_LONG).show();
			return;
		}

		File imagePath = PathUtil.getInstance().getImagePath();
//		String string = ColorHolderApplication.getInstance().getUserName()
//				+ System.currentTimeMillis() + ".jpg";
		String string="暂时的String";
		cameraFile = new File(imagePath, string);
		cameraFile.getParentFile().mkdirs();
		startActivityForResult(
				new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
				REQUEST_CODE_CAMERA);
	}

	protected void onListViewCreation(){
		adapter = new MyList(ChatActivity.this, toChatUsername, chatType);
		// 显示消息
		listView.setAdapter(adapter);

		listView.setOnScrollListener(new ListScrollListener());
		adapter.refreshSelectLast();

		//        listView.setOnTouchListener(new OnTouchListener() {
		//
		//            @Override
		//            public boolean onTouch(View v, MotionEvent event) {
		//                hideKeyboard();
		//                more.setVisibility(View.GONE);
		//                iv_emoticons_normal.setVisibility(View.VISIBLE);
		//                iv_emoticons_checked.setVisibility(View.INVISIBLE);
		//                emojiIconContainer.setVisibility(View.GONE);
		//                btnContainer.setVisibility(View.GONE);
		//                return false;
		//            }
		//        });
	}

	private class ListScrollListener implements OnScrollListener {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_IDLE:
				/*if (view.getFirstVisiblePosition() == 0 && !isloading && haveMoreData && conversation.getAllMessages().size() != 0) {
					isloading = true;
					loadmorePB.setVisibility(View.VISIBLE);
					// sdk初始化加载的聊天记录为20条，到顶时去db里获取更多					
					List<EMMessage> messages;
					EMMessage firstMsg = conversation.getAllMessages().get(0);
					try {
						// 获取更多messges，调用此方法的时候从db获取的messages
						// sdk会自动存入到此conversation中
						if (chatType == CHATTYPE_SINGLE)
							messages = conversation.loadMoreMsgFromDB(firstMsg.getMsgId(), pagesize);
						else
							messages = conversation.loadMoreGroupMsgFromDB(firstMsg.getMsgId(), pagesize);
					} catch (Exception e1) {
						loadmorePB.setVisibility(View.GONE);
						return;
					}
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
					}
					if (messages.size() != 0) {
						// 刷新ui
						if (messages.size() > 0) {
							adapter.refreshSeekTo(messages.size() - 1);
						}

						if (messages.size() != pagesize)
							haveMoreData = false;
					} else {
						haveMoreData = false;
					}
					loadmorePB.setVisibility(View.GONE);
					isloading = false;

				}*/
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

		}

	}

	private void sendText(String content) {

		if (content.length() > 0) {
			EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
			// 如果是群聊，设置chattype,默认是单聊
			if (chatType == CHATTYPE_GROUP){
				message.setChatType(ChatType.GroupChat);
			}else if(chatType == CHATTYPE_CHATROOM){
				message.setChatType(ChatType.ChatRoom);
			}

			TextMessageBody txtBody = new TextMessageBody(content);
			// 设置消息body
			message.addBody(txtBody);
			// 设置要发给谁,用户username或者群聊groupid
			message.setReceipt(toChatUsername);
			// 把messgage加到conversation中
			Log.e("conversation", ""+conversation);
			if(conversation==null){
				conversation = EMChatManager.getInstance().getConversationByType(toChatUsername,EMConversationType.Chat);
			}
			conversation.addMessage(message);
			// 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
			//			listView = (ListView) findViewById(R.id.list1);
			//			adapter = new MyList(this,toChatUsername,chatType);
			//			listView.setAdapter(adapter);
			if(adapter!=null){
				adapter.refreshSelectLast();
			}
			input.setText("");
			SharedPreferences sp = getSharedPreferences("text.xml", 0);
			Editor edit = sp.edit();
			edit.putBoolean("sendSt", true);
			edit.commit();

			setResult(RESULT_OK);

		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CODE_EXIT_GROUP) {
			setResult(RESULT_OK);
			finish();
			return;
		}
		if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
			switch (resultCode) {
			case RESULT_CODE_COPY: // 复制消息
				EMMessage copyMsg = ((EMMessage) adapter.getItem(data.getIntExtra("position", -1)));
				// clipboard.setText(SmileUtils.getSmiledText(ChatActivity.this,
				// ((TextMessageBody) copyMsg.getBody()).getMessage()));
				clipboard.setText(((TextMessageBody) copyMsg.getBody()).getMessage());
				break;
			case RESULT_CODE_DELETE: // 删除消息
				EMMessage deleteMsg = (EMMessage) adapter.getItem(data.getIntExtra("position", -1));
				conversation.removeMessage(deleteMsg.getMsgId());
				adapter.refreshSeekTo(data.getIntExtra("position", adapter.getCount()) - 1);
				break;

			case RESULT_CODE_FORWARD: // 转发消息
				EMMessage forwardMsg = (EMMessage) adapter.getItem(data.getIntExtra("position", 0));
				Intent intent = new Intent(this, ForwardMessageActivity.class);
				intent.putExtra("forward_msg_id", forwardMsg.getMsgId());
				startActivity(intent);

				break;

			default:
				break;
			}
		}
		if (resultCode == RESULT_OK) { // 清空消息
			if (requestCode == REQUEST_CODE_EMPTY_HISTORY) {
				// 清空会话
				EMChatManager.getInstance().clearConversation(toChatUsername);
				adapter.refresh();
				//                conversation.addMessage(message);
				adapter.notifyDataSetChanged();
				listView.setAdapter(adapter);
				listView.setSelection(listView.getCount()-1);
			} else if (requestCode == REQUEST_CODE_CAMERA) { // 发送照片
				if (cameraFile != null && cameraFile.exists())
					sendPicture(cameraFile.getAbsolutePath());
			} else if (requestCode == REQUEST_CODE_SELECT_VIDEO) { // 发送本地选择的视频

				int duration = data.getIntExtra("dur", 0);
				String videoPath = data.getStringExtra("path");
				File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
				Bitmap bitmap = null;
				FileOutputStream fos = null;
				try {
					if (!file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
					bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
					if (bitmap == null) {
						EMLog.d("chatactivity", "problem load video thumbnail bitmap,use default icon");
						bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.app_panel_video_icon);
					}
					fos = new FileOutputStream(file);

					bitmap.compress(CompressFormat.JPEG, 100, fos);

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (fos != null) {
						try {
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						fos = null;
					}
					if (bitmap != null) {
						bitmap.recycle();
						bitmap = null;
					}

				}
				sendVideo(videoPath, file.getAbsolutePath(), duration / 1000);

			} else if (requestCode == REQUEST_CODE_LOCAL) { // 发送本地图片
				if (data != null) {
					Uri selectedImage = data.getData();
					if (selectedImage != null) {
						sendPicByUri(selectedImage);
					}
				}
			} else if (requestCode == REQUEST_CODE_SELECT_FILE) { // 发送选择的文件
				if (data != null) {
					Uri uri = data.getData();
					if (uri != null) {
						sendFile(uri);
					}
				}

			} else if (requestCode == REQUEST_CODE_MAP) { // 地图
				double latitude = data.getDoubleExtra("latitude", 0);
				double longitude = data.getDoubleExtra("longitude", 0);
				String locationAddress = data.getStringExtra("address");
				Log.e("latitude111rrrrrrrr", ""+latitude);
				Log.e("longitude111rrrrrrr", ""+longitude);
				Log.e("address111rrrrrrrrrr", ""+locationAddress);
				if (locationAddress != null && !locationAddress.equals("")) {
					//                    toggleMore(more);
					sendLocationMsg(latitude, longitude, "", locationAddress);
				} else {
					String st = getResources().getString(R.string.unable_to_get_loaction);
					Toast.makeText(this, st, 0).show();
				}
				// 重发消息
			} else if (requestCode == REQUEST_CODE_TEXT || requestCode == REQUEST_CODE_VOICE
					|| requestCode == REQUEST_CODE_PICTURE || requestCode == REQUEST_CODE_LOCATION
					|| requestCode == REQUEST_CODE_VIDEO || requestCode == REQUEST_CODE_FILE) {
				resendMessage();
			} else if (requestCode == REQUEST_CODE_COPY_AND_PASTE) {
				// 粘贴
				if (!TextUtils.isEmpty(clipboard.getText())) {
					String pasteText = clipboard.getText().toString();
					if (pasteText.startsWith(COPY_IMAGE)) {
						// 把图片前缀去掉，还原成正常的path
						sendPicture(pasteText.replace(COPY_IMAGE, ""));
					}

				}
			} else if (requestCode == REQUEST_CODE_ADD_TO_BLACKLIST) { // 移入黑名单
				EMMessage deleteMsg = (EMMessage) adapter.getItem(data.getIntExtra("position", -1));
				addUserToBlacklist(deleteMsg.getFrom());
			} else if (conversation.getMsgCount() > 0) {
				adapter.refresh();
				setResult(RESULT_OK);
			} else if (requestCode == REQUEST_CODE_GROUP_DETAIL) {
				adapter.refresh();
			}
		}
	}

	private void addUserToBlacklist(final String username) {
		final ProgressDialog pd = new ProgressDialog(this);
		pd.setMessage(getString(R.string.Is_moved_into_blacklist));
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		new Thread(new Runnable() {
			public void run() {
				try {
					EMContactManager.getInstance().addUserToBlackList(username, false);
					runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							Toast.makeText(getApplicationContext(), R.string.Move_into_blacklist_success, 0).show();
						}
					});
				} catch (EaseMobException e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							Toast.makeText(getApplicationContext(), R.string.Move_into_blacklist_failure, 0).show();
						}
					});
				}
			}
		}).start();
	}


	private void resendMessage() {
		EMMessage msg = null;
		msg = conversation.getMessage(resendPos);
		// msg.setBackSend(true);
		msg.status = EMMessage.Status.CREATE;

		adapter.refreshSeekTo(resendPos);
	}

	private void sendLocationMsg(double latitude, double longitude, String imagePath, String locationAddress) {
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.LOCATION);
		// 如果是群聊，设置chattype,默认是单聊
		if (chatType == CHATTYPE_GROUP){
			message.setChatType(ChatType.GroupChat);
		}else if(chatType == CHATTYPE_CHATROOM){
			message.setChatType(ChatType.ChatRoom);
		}
		LocationMessageBody locBody = new LocationMessageBody(locationAddress, latitude, longitude);
		message.addBody(locBody);
		message.setReceipt(toChatUsername);
		conversation.addMessage(message);
		listView.setAdapter(adapter);
		adapter.refreshSelectLast();
		setResult(RESULT_OK);

	}

	private void sendVideo(final String filePath, final String thumbPath, final int length) {
		final File videoFile = new File(filePath);
		if (!videoFile.exists()) {
			return;
		}
		try {
			EMMessage message = EMMessage.createSendMessage(EMMessage.Type.VIDEO);
			// 如果是群聊，设置chattype,默认是单聊
			if (chatType == CHATTYPE_GROUP){
				message.setChatType(ChatType.GroupChat);
			}else if(chatType == CHATTYPE_CHATROOM){
				message.setChatType(ChatType.ChatRoom);
			}
			String to = toChatUsername;
			message.setReceipt(to);
			VideoMessageBody body = new VideoMessageBody(videoFile, thumbPath, length, videoFile.length());
			message.addBody(body);
			conversation.addMessage(message);
			listView.setAdapter(adapter);
			adapter.refreshSelectLast();
			setResult(RESULT_OK);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void sendFile(Uri uri) {
		String filePath = null;
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;

			try {
				cursor = getContentResolver().query(uri, projection, null, null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					filePath = cursor.getString(column_index);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {
			filePath = uri.getPath();
		}
		File file = new File(filePath);
		if (file == null || !file.exists()) {
			String st7 = getResources().getString(R.string.File_does_not_exist);
			Toast.makeText(getApplicationContext(), st7, 0).show();
			return;
		}
		if (file.length() > 10 * 1024 * 1024) {
			String st6 = getResources().getString(R.string.The_file_is_not_greater_than_10_m);
			Toast.makeText(getApplicationContext(), st6, 0).show();
			return;
		}

		// 创建一个文件消息
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.FILE);
		// 如果是群聊，设置chattype,默认是单聊
		if (chatType == CHATTYPE_GROUP){
			message.setChatType(ChatType.GroupChat);
		}else if(chatType == CHATTYPE_CHATROOM){
			message.setChatType(ChatType.ChatRoom);
		}

		message.setReceipt(toChatUsername);
		// add message body
		NormalFileMessageBody body = new NormalFileMessageBody(new File(filePath));
		message.addBody(body);
		conversation.addMessage(message);
		listView.setAdapter(adapter);
		adapter.refreshSelectLast();
		setResult(RESULT_OK);
	}

	private void sendPicture(final String filePath) {
		String to = toChatUsername;
		// create and add image message in view
		final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.IMAGE);
		// 如果是群聊，设置chattype,默认是单聊
		if (chatType == CHATTYPE_GROUP){
			message.setChatType(ChatType.GroupChat);
		}else if(chatType == CHATTYPE_CHATROOM){
			message.setChatType(ChatType.ChatRoom);
		}

		message.setReceipt(to);
		ImageMessageBody body = new ImageMessageBody(new File(filePath));
		// 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
		// body.setSendOriginalImage(true);
		message.addBody(body);
		if(conversation==null){
			conversation = EMChatManager.getInstance().getConversationByType(toChatUsername,EMConversationType.Chat);
		}
		conversation.addMessage(message);

		//		listView.setAdapter(adapter);
		//		adapter.refreshSelectLast();
		//		conversation.addMessage(message);
		//		adapter.notifyDataSetChanged();
		//		listView.setAdapter(adapter);
		//		listView.setSelection(listView.getCount()-1);
		setResult(RESULT_OK);
		// more(more);
	}

	private void sendPicByUri(Uri selectedImage) {
		// String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(selectedImage, null, null, null, null);
		String st8 = getResources().getString(R.string.cant_find_pictures);
		if (cursor != null) {
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex("_data");
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			cursor = null;

			if (picturePath == null || picturePath.equals("null")) {
				Toast toast = Toast.makeText(this, st8, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;
			}
			sendPicture(picturePath);
		} else {
			File file = new File(selectedImage.getPath());
			if (!file.exists()) {
				Toast toast = Toast.makeText(this, st8, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;

			}
			sendPicture(file.getAbsolutePath());
		}

	}


	public ListView getListView() {
		return listView;
	}
	public static class ViewHolder {
		public TextView tv_hhmm;
		public TextView tv_yymmdd;
		public TextView tv_sendtime;
		ImageView iv;
		TextView tv;
		ProgressBar pb;
		ImageView staus_iv;
		ImageView iv_avatar;
		TextView tv_usernick;
		ImageView playBtn;
		TextView size;
		TextView timeLength;
		LinearLayout container_status_btn;
		LinearLayout ll_container;
		ImageView iv_read_status;
		// 显示已读回执状态
		TextView tv_ack;
		// 显示送达回执状态
		TextView tv_delivered;

		TextView tv_file_name;
		TextView tv_file_size;
		TextView tv_file_download_state;
	}
	boolean sendSt=true;
	private LinearLayout btn_press_to_speak;
	private Button btn_set_mode_voice;
	private String chatpeople;
	private RelativeLayout picture_source_rela;
	private String nickname;
	private String avatar_path;
	private ReadUserAllFriends mAllFriends;
	protected void forwardMessage(String forward_msg_id) {
		final EMMessage forward_msg = EMChatManager.getInstance().getMessage(forward_msg_id);
		EMMessage.Type type = forward_msg.getType();
		switch (type) {
		case TXT:
			// 获取消息内容，发送消息
			String content = ((TextMessageBody) forward_msg.getBody()).getMessage();
			SharedPreferences sp = getSharedPreferences("text.xml", 0);
			Editor edit = sp.edit();
			edit.putBoolean("sendSt", false);
			edit.commit();
			sendText(content);
			break;
		case IMAGE:
			// 发送图片
			String filePath = ((ImageMessageBody) forward_msg.getBody()).getLocalUrl();
			if (filePath != null) {
				File file = new File(filePath);
				if (!file.exists()) {
					// 不存在大图发送缩略图
					filePath = ImageUtils.getThumbnailImagePath(filePath);
				}
				sendPicture(filePath);
			}
			break;
		default:
			break;
		}

		if(forward_msg.getChatType() == EMMessage.ChatType.ChatRoom){
			EMChatManager.getInstance().leaveChatRoom(forward_msg.getTo());
		}
	}
	public void setModeVoice() {
		hideKeyboard();
		edittext_layout.setVisibility(View.GONE);
		buttonSetModeKeyboard.setVisibility(View.VISIBLE);
		btn_more.setVisibility(View.VISIBLE);
		buttonPressToSpeak.setVisibility(View.VISIBLE);
		send_expression.setVisibility(View.VISIBLE);
		moreItem.setVisibility(View.VISIBLE);
		display_expression.setVisibility(View.GONE);
		moreItem.setVisibility(View.GONE);

	}
	public void undisplay_down() {
		hideKeyboard();
		edittext_layout.setVisibility(View.VISIBLE);
		more.setVisibility(View.GONE);
		buttonSetModeKeyboard.setVisibility(View.GONE);
		btn_more.setVisibility(View.VISIBLE);
		buttonPressToSpeak.setVisibility(View.GONE);
		send_expression.setVisibility(View.VISIBLE);
		moreItem.setVisibility(View.GONE);
		display_expression.setVisibility(View.GONE);

	}

	/**
	 * 发送语音
	 * 
	 * @param filePath
	 * @param fileName
	 * @param length
	 * @param isResend
	 */
	private void sendVoice(String filePath, String fileName, String length, boolean isResend) {
		if (!(new File(filePath).exists())) {
			return;
		}
		try {
			final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.VOICE);
			// 如果是群聊，设置chattype,默认是单聊
			if (chatType == CHATTYPE_GROUP){
				message.setChatType(ChatType.GroupChat);
			}else if(chatType == CHATTYPE_CHATROOM){
				message.setChatType(ChatType.ChatRoom);
			}
			message.setReceipt(toChatUsername);
			int len = Integer.parseInt(length);
			VoiceMessageBody body = new VoiceMessageBody(new File(filePath), len);
			message.addBody(body);

			conversation.addMessage(message);
			adapter.refreshSelectLast();
			setResult(RESULT_OK);
			// send file
			// sendVoiceSub(filePath, fileName, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void hideKeyboard() {
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	@Override
	protected void onPause() {
		super.onPause();
		if (wakeLock.isHeld())
			wakeLock.release();
		if (VoicePlayClickListener.isPlaying && VoicePlayClickListener.currentPlayListener != null) {
			// 停止语音播放
			VoicePlayClickListener.currentPlayListener.stopPlayVoice();
		}

		try {
			// 停止录音
			if (voiceRecorder.isRecording()) {
				voiceRecorder.discardRecording();
				recordingContainer.setVisibility(View.INVISIBLE);
			}
		} catch (Exception e) {
		}
	}
	@Override
	public void onEvent(EMNotifierEvent event) {
		switch (event.getEvent()) {
		case EventNewMessage:
		{
			//获取到message
			EMMessage message = (EMMessage) event.getData();

			String username = null;
			//群组消息
			if(message.getChatType() == ChatType.GroupChat || message.getChatType() == ChatType.ChatRoom){
				username = message.getTo();
			}
			else{
				//单聊消息
				username = message.getFrom();
			}

			//如果是当前会话的消息，刷新聊天页面
			if(username.equals(getToChatUsername())){
				refreshUIWithNewMessage();
				//声音和震动提示有新消息
				HXSDKHelper.getInstance().getNotifier().viberateAndPlayTone(message);
			}else{
				//如果消息不是和当前聊天ID的消息
				HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
			}

			break;
		}
		case EventDeliveryAck:
		{
			//获取到message
			EMMessage message = (EMMessage) event.getData();
			refreshUI();
			break;
		}
		case EventReadAck:
		{
			//获取到message
			EMMessage message = (EMMessage) event.getData();
			refreshUI();
			break;
		}
		case EventOfflineMessage:
		{
			//a list of offline messages 
			//List<EMMessage> offlineMessages = (List<EMMessage>) event.getData();
			refreshUI();
			break;
		}
		default:
			break;
		}

	}
	public void back(View view) {
		EMChatManager.getInstance().unregisterEventListener(this);
		if(chatType == CHATTYPE_CHATROOM){
			EMChatManager.getInstance().leaveChatRoom(toChatUsername);
		}
		finish();
	}
	private void refreshUIWithNewMessage(){
		if(adapter == null){
			return;
		}

		runOnUiThread(new Runnable() {
			public void run() {
				adapter.refreshSelectLast();
			}
		});
	}
	public String getToChatUsername() {
		return toChatUsername;
	}
	private void refreshUI() {
		if(adapter == null){
			return;
		}

		runOnUiThread(new Runnable() {
			public void run() {
				adapter.refresh();
			}
		});
	}

}
