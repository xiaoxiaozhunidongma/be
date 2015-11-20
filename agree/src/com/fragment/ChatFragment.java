package com.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import leanchatlib.adapter.ChatEmotionGridAdapter;
import leanchatlib.adapter.ChatEmotionPagerAdapter;
import leanchatlib.adapter.ChatMessageAdapter;
import leanchatlib.controller.AVIMTypedMessagesArrayCallback;
import leanchatlib.controller.ChatManager;
import leanchatlib.controller.ConversationHelper;
import leanchatlib.controller.EmotionHelper;
import leanchatlib.controller.MessageAgent;
import leanchatlib.controller.MessageHelper;
import leanchatlib.controller.RoomsTable;
import leanchatlib.model.ConversationType;
import leanchatlib.model.MessageEvent;
import leanchatlib.utils.LogUtils;
import leanchatlib.utils.PathUtils;
import leanchatlib.utils.ProviderPathUtils;
import leanchatlib.utils.Utils;
import leanchatlib.view.PasteEditText;
import leanchatlib.view.RecordButton;
import leanchatlib.view.RefreshableView;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.BJ.utils.SdPkUser;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMReservedMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMLocationMessage;
import com.biju.R;
import com.example.testleabcloud.ChatActivityEventListener;
import com.example.testleabcloud.PhotoViewActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class ChatFragment extends Fragment implements OnClickListener,ChatActivityEventListener {
	
	  public static final String CONVID = "convid";
	  private static final int PAGE_SIZE = 8;
	  private static final int TAKE_CAMERA_REQUEST = 2;
	  private static final int GALLERY_REQUEST = 0;
	  private static final int GALLERY_KITKAT_REQUEST = 3;
		public static final String COPY_IMAGE = "EASEMOBIMG";
		public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
		protected static final String INPUT_METHOD_SERVICE = "input_method";

	  private volatile static ChatFragment chatInstance;
	  protected ConversationType conversationType;
	  protected AVIMConversation conversation;
	  protected MessageAgent messageAgent;
	  protected MessageAgent.SendCallback defaultSendCallback = new DefaultSendCallback();
	  protected EventBus eventBus;
	  protected ChatManager chatManager = ChatManager.getInstance();
	  protected ChatMessageAdapter adapter;
	  protected RoomsTable roomsTable;
	  protected View chatTextLayout, chatAudioLayout, chatAddLayout, chatEmotionLayout;
	  protected View turnToTextBtn, turnToAudioBtn, sendBtn, addImageBtn, showAddBtn, addLocationBtn, showEmotionBtn;
	  protected ViewPager emotionPager;
	  protected PasteEditText contentEdit;
	  protected RefreshableView refreshableView;
	  protected ListView messageListView;
	  protected RecordButton recordBtn;
	  protected String localCameraPath = PathUtils.getPicturePathByCurrentTime();
	  protected View addCameraBtn;
	private RelativeLayout picture_source_rela;
	private Button send;
	//private PasteEditText input;

	private View mLayout;
	public static onActivityResultInterface onActivityResultInterface;
	private RelativeLayout mChatPromptLayout;

	public ChatFragment() {
		// Required empty public constructor
	}
	
	  class DefaultSendCallback implements MessageAgent.SendCallback {

		    @Override
		    public void onError(AVIMTypedMessage message, Exception e) {
		      LogUtils.i();
		      Log.e("ChatFragment", "DefaultSendCallback--onError");
		      Toast.makeText(getActivity(), "无可用的网络"+e.toString(), Toast.LENGTH_SHORT).show();
//		      addMessageAndScroll(message);
		    }

		    @Override
		    public void onSuccess(AVIMTypedMessage message) {
//		      Utils.i();
		    	Log.e("ChatFragment", "DefaultSendCallback--onSuccess");
		      addMessageAndScroll(message);
		    }
		  }
	  
	  public void addMessageAndScroll(AVIMTypedMessage message) {
		    AVIMTypedMessage foundMessage = findMessage(message.getMessageId());
		    if (foundMessage == null) {
		      adapter.add(message);
		      scrollToLast();
		    }
		  }
	  
	  public void scrollToLast() {
		    messageListView.post(new Runnable() {
		      @Override
		      public void run() {
//		        messageListView.smoothScrollToPosition(messageListView.getAdapter().getCount() - 1);
				messageListView.setSelection(messageListView
						.getAdapter().getCount() - 1);
		      }
		    });
		  }
	  
	  private AVIMTypedMessage findMessage(String messageId) {
		    for (AVIMTypedMessage originMessage : adapter.getDatas()) {
		      if (originMessage.getMessageId() != null && originMessage.getMessageId().equals(messageId)) {
		        return originMessage;
		      }
		    }
		    return null;
		  }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		if (mLayout == null) {
			mLayout = inflater.inflate(R.layout.leanchat_layout_groupchat, container, false);
			
		    commonInit();
		    findView();
		    initEmotionPager();
		    initRecordBtn();
		    setEditTextChangeListener();
		    initListView();
//
		    Intent intent = getActivity().getIntent();
			initByIntent(intent);
			initOnActivityResult();
			Log.e("ChatFragment", "进入了onCreateView()========");
		}
		return mLayout;
	}
	
	private void initOnActivityResult() {
	onActivityResultInterface onActivityResultInterface = new onActivityResultInterface() {
		
		@SuppressLint("NewApi")
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			// TODO Auto-generated method stub
			Log.e("群聊的onActivityResult", "");
		    if (resultCode == getActivity().RESULT_OK) {
		      switch (requestCode) {
		        case GALLERY_REQUEST:
		        case GALLERY_KITKAT_REQUEST:
		          if (data == null) {
		            toast("return intent is null");
		            return;
		          }
		          Uri uri;
		          if (requestCode == GALLERY_REQUEST) {
		            uri = data.getData();
		          } else {
		        	  Log.e("", "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
		            //for Android 4.4
		            uri = data.getData();
		            final int takeFlags = data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION
		                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
		            getActivity().getContentResolver().takePersistableUriPermission(uri, takeFlags);
		          }
		          String localSelectPath = ProviderPathUtils.getPath(getActivity(), uri);
		          messageAgent.sendImage(localSelectPath,getActivity());
		          hideBottomLayout();
		          break;
		        case TAKE_CAMERA_REQUEST:
		          messageAgent.sendImage(localCameraPath,getActivity());
		          hideBottomLayout();
		          break;
		      }
		    }
		}
	};
	
	this.onActivityResultInterface=onActivityResultInterface;
	}

//	  public void onNewIntent(Intent intent) {
////	    super.onNewIntent(intent);
//	    initByIntent(intent);
//	  }

	  private void initByIntent(Intent intent) {
		    initData(intent);
		    loadMessagesWhenInit(PAGE_SIZE);
		  }

	private void initData(Intent intent) {
//		ReadUserAllFriends mAllFriends = (ReadUserAllFriends) intent.getSerializableExtra("allFriends");
		  String conName = intent.getStringExtra("conName");
//		  String otherAvaUrl = intent.getStringExtra("otherAvaUrl");
		  @SuppressWarnings("unchecked")
		  HashMap<Integer, String> FromAvaUrlMap= (HashMap<Integer, String>) intent.getSerializableExtra("FromAvaUrlMap");
		String CurrUserUrl = intent.getStringExtra("CurrUserUrl");
	    String convid = intent.getStringExtra(CONVID);
	    conversation = chatManager.lookUpConversationById(convid);
	    if (isConversationEmpty(conversation)) {
	      return;
	    }
	    initActionBar(ConversationHelper.titleOfConversation(conversation));
	    messageAgent = new MessageAgent(conversation);
	    messageAgent.setSendCallback(defaultSendCallback);//回调监听！！！！！！！！！！！！！！！！！！
	    roomsTable.clearUnread(conversation.getConversationId());
	    conversationType = ConversationHelper.typeOfConversation(conversation);
	    bindAdapterToListView(conversationType,FromAvaUrlMap,CurrUserUrl);
	  }

	private void bindAdapterToListView(ConversationType conversationType2,
			HashMap<Integer, String> fromAvaUrlMap, String CurrUserUrl) {
	    adapter = new ChatMessageAdapter(getActivity(), conversationType,fromAvaUrlMap,CurrUserUrl);
	    adapter.setClickListener(new ChatMessageAdapter.ClickListener() {
	      @Override
	      public void onFailButtonClick(AVIMTypedMessage msg) {
	        messageAgent.resendMessage(msg, new MessageAgent.SendCallback() {
	          @Override
	          public void onError(AVIMTypedMessage message, Exception e) {
	            LogUtils.d("resend message error");
	            Log.e("ChatActivity", "==resend message error");
	            // 应该只重新加载一�? Todo
	            loadMessagesWhenInit(adapter.getCount());
	          }

	          @Override
	          public void onSuccess(AVIMTypedMessage message) {
	            LogUtils.d("resend message success");
	            // 应该只重新加载一�? Todo
	            loadMessagesWhenInit(adapter.getCount());
	          }
	        });
	      }

	      @Override
	      public void onLocationViewClick(AVIMLocationMessage locMsg) {
	        onLocationMessageViewClicked(locMsg);
	      }

	      private void onLocationMessageViewClicked(AVIMLocationMessage locMsg) {
			// TODO Auto-generated method stub
			
		}

		@Override
	      public void onImageViewClick(AVIMImageMessage imageMsg) {
	        onImageMessageViewClicked(imageMsg, MessageHelper.getFilePath(imageMsg));
	      }
	    });
	    messageListView.setAdapter(adapter);
	  }

	  public void loadMessagesWhenInit(int limit) {
		    ChatManager.getInstance().queryMessages(conversation, null, System.currentTimeMillis(), limit, new
		        AVIMTypedMessagesArrayCallback() {
		          @Override
		          public void done(final List<AVIMTypedMessage> typedMessages, AVException e) {
		            if (filterException(e)) {
		              new CacheMessagesTask(getActivity(), typedMessages) {
		                @Override
		                void onSucceed(List<AVIMTypedMessage> messages) { 
		                	if(messages.size()==0){
		                		//显示
		                		getActivity().runOnUiThread(new Runnable() {
		        					
		        					@Override
		        					public void run() {
		        						mChatPromptLayout.setVisibility(View.VISIBLE);
		        					}
		        				});
		                	}else{
		                		//隐藏
		                		getActivity().runOnUiThread(new Runnable() {
		        					
		        					@Override
		        					public void run() {
		        						mChatPromptLayout.setVisibility(View.GONE);
		        					}
		        				});
		                	}
		                  adapter.setDatas(typedMessages);
		                  adapter.notifyDataSetChanged();
		                  scrollToLast();
		                }
		              }.execute();
		            }
		          }
		        });
		  
		  
		  }
	  
	@SuppressLint("NewApi")
	private void initActionBar(String title) {
	    ActionBar actionBar = getActivity().getActionBar();
	    if (actionBar != null) {
	      if (title != null) {
	        actionBar.setTitle(title);
	      }
	      actionBar.setDisplayUseLogoEnabled(false);
	      actionBar.setDisplayHomeAsUpEnabled(true);
	    } else {
	      Log.e("ChatFragment", "action bar is null, so no title, please set an ActionBar style for activity");
	    }
	  }

	private boolean isConversationEmpty(AVIMConversation conversation2) {
	    if (conversation == null) {
	        toast("未找到对话，请�?出重试�?请检查是否调用了 ChatManager.registerConversation()");
	        getActivity().finish();
	        return true;
	      }
	      return false;
	    }

	private void initListView() {
		    refreshableView.setRefreshListener(new RefreshableView.ListRefreshListener(messageListView) {
		        @Override
		        public void onRefresh() {
		          loadOldMessages();
		        }

				private void loadOldMessages() {
					//显示
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							mChatPromptLayout.setVisibility(View.VISIBLE);
						}
					});
				    if (adapter.getDatas().size() == 0) {
				        refreshableView.finishRefreshing();
				        return;
				      } else {
				    	  //隐藏
				    	  getActivity().runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									mChatPromptLayout.setVisibility(View.GONE);
								}
							});
				        AVIMTypedMessage firstMsg = adapter.getDatas().get(0);
				        String msgId = firstMsg.getMessageId();
				        long time = firstMsg.getTimestamp();
				        ChatManager.getInstance().queryMessages(conversation, msgId, time, PAGE_SIZE, new AVIMTypedMessagesArrayCallback() {
				          @Override
				          public void done(List<AVIMTypedMessage> typedMessages, AVException e) {
				            refreshableView.finishRefreshing();
				            if (filterException(e)) {
				              new CacheMessagesTask(getActivity(), typedMessages) {
				                @Override
				                void onSucceed(List<AVIMTypedMessage> typedMessages) {
				                  List<AVIMTypedMessage> newMessages = new ArrayList<AVIMTypedMessage>(PAGE_SIZE);
				                  newMessages.addAll(typedMessages);
				                  newMessages.addAll(adapter.getDatas());
				                  adapter.setDatas(newMessages);
				                  adapter.notifyDataSetChanged();
				                  if (typedMessages.size() > 0) {
				                    messageListView.setSelection(typedMessages.size() - 1);
				                  } else {
				                    toast(R.string.chat_activity_loadMessagesFinish);
				                  }
				                }
				              }.execute();
				            }
				          }

				        });
				      }

				    }
		      });
		      messageListView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
		      messageListView.setOnTouchListener(new OnTouchListener() {

		  		@Override
		  		public boolean onTouch(View v, MotionEvent event) {
		  			//隐藏软键盘
		  			hideSoftInputView();
		  			return false;
		  		}

				private void hideSoftInputView() {
				    if (getActivity().getWindow().getAttributes().softInputMode !=
				            WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
				          InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
				          View currentFocus = getActivity().getCurrentFocus();
				          if (currentFocus != null) {
				            manager.hideSoftInputFromWindow(currentFocus.getWindowToken(),
				                InputMethodManager.HIDE_NOT_ALWAYS);
				          }
				        }
				      }
		  	});
		      
		    }
	
	  @SuppressLint("NewApi")
	  public abstract class CacheMessagesTask extends AsyncTask<Void, Void, Void> {
	      private List<AVIMTypedMessage> messages;
	      private volatile Exception e;

	      public CacheMessagesTask(Context context, List<AVIMTypedMessage> messages) {
	        this.messages = messages;
	      }

	      @Override
	      protected Void doInBackground(Void... voids) {
	        Set<String> userIds = new HashSet<String>();
	        for (AVIMTypedMessage msg : messages) {
	          AVIMReservedMessageType type = AVIMReservedMessageType.getAVIMReservedMessageType(msg.getMessageType());
	          if (type == AVIMReservedMessageType.AudioMessageType) {
	            File file = new File(MessageHelper.getFilePath(msg));
	            if (!file.exists()) {
	              AVIMAudioMessage audioMsg = (AVIMAudioMessage) msg;
	              String url = audioMsg.getFileUrl();
//	              if (audioMsg.getFileMetaData() != null) {
//	                int size = (Integer) audioMsg.getFileMetaData().get("size");
//	                LogUtils.d("metaData size", size + "");
//	              }
	              Utils.downloadFileIfNotExists(url, file);
	            }
	          }
	          userIds.add(msg.getFrom());
	        }
	        if (chatManager.getChatManagerAdapter() == null) {
	          throw new IllegalStateException("please set ChatManagerAdapter in ChatManager to provide userInfo");
	        }
	        try {
	          chatManager.getChatManagerAdapter().cacheUserInfoByIdsInBackground(new ArrayList<String>(    ));
	        } catch (Exception e1) {
	          LogUtils.logException(e1);
	        }
	        return null;
	      }

	      @Override
	      protected void onPostExecute(Void aVoid) {
	        if (filterException(e)) {
	          onSucceed(messages);
	        }
	      }

	      abstract void onSucceed(List<AVIMTypedMessage> messages);
	    }
	
	public boolean filterException(Exception e) {
	    if (e != null) {
	        LogUtils.logException(e);
	        toast(e.getMessage());
	        return false;
	      } else {
	        return true;
	      }
	    }
	  
	  protected void toast(String str) {
		    Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
		  }
	
	  protected void toast(int id) {
		    Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();
		  }

	private void setEditTextChangeListener() {
		    contentEdit.addTextChangedListener(new TextWatcher() {
		        @Override
		        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

		        }

		        @Override
		        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
		          if (charSequence.length() > 0) {
		            sendBtn.setEnabled(true);
		            showSendBtn();
		          } else {
		            sendBtn.setEnabled(false);
		            showTurnToRecordBtn();
		          }
		        }

		        private void showTurnToRecordBtn() {
		            sendBtn.setVisibility(View.GONE);
		            turnToAudioBtn.setVisibility(View.VISIBLE);
		          }

		          private void showSendBtn() {
		        	  sendBtn.setVisibility(View.VISIBLE);
		            turnToAudioBtn.setVisibility(View.GONE);
		          }

				@Override
		        public void afterTextChanged(Editable editable) {

		        }
		      });
		    }

	private void initRecordBtn() {
		    recordBtn.setSavePath(PathUtils.getRecordPathByCurrentTime());
		    recordBtn.setRecordEventListener(new RecordButton.RecordEventListener() {
		      @Override
		      public void onFinishedRecord(final String audioPath, int secs) {
//		        LogUtils.d("audioPath = ", audioPath);
		        messageAgent.sendAudio(audioPath);
		      }

		      @Override
		      public void onStartRecord() {

		      }
		    });
		  }

	private void initEmotionPager() {
		    List<View> views = new ArrayList<View>();
		    for (int i = 0; i < EmotionHelper.emojiGroups.size(); i++) {
		      views.add(getEmotionGridView(i));
		    }
		    ChatEmotionPagerAdapter pagerAdapter = new ChatEmotionPagerAdapter(views);
		    emotionPager.setOffscreenPageLimit(3);
		    emotionPager.setAdapter(pagerAdapter);
		  }

	private View getEmotionGridView(int pos) {
	    LayoutInflater inflater = LayoutInflater.from(getActivity());//????
	    View emotionView = inflater.inflate(R.layout.chat_emotion_gridview, null, false);
	    GridView gridView = (GridView) emotionView.findViewById(R.id.gridview);
	    final ChatEmotionGridAdapter chatEmotionGridAdapter = new ChatEmotionGridAdapter(getActivity());//???????????
	    List<String> pageEmotions = EmotionHelper.emojiGroups.get(pos);
	    chatEmotionGridAdapter.setDatas(pageEmotions);
	    gridView.setAdapter(chatEmotionGridAdapter);
	    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	      @Override
	      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        String emotionText = (String) parent.getAdapter().getItem(position);
	        int start = contentEdit.getSelectionStart();
	        StringBuffer sb = new StringBuffer(contentEdit.getText());
	        sb.replace(contentEdit.getSelectionStart(), contentEdit.getSelectionEnd(), emotionText);
	        contentEdit.setText(sb.toString());

	        CharSequence info = contentEdit.getText();
	        if (info instanceof Spannable) {
	          Spannable spannable = (Spannable) info;
	          Selection.setSelection(spannable, start + emotionText.length());
	        }
	      }
	    });
	    return gridView;
	  }

	private void findView() {
		mChatPromptLayout = (RelativeLayout) mLayout.findViewById(R.id.ChatPromptLayout);//没有；聊天记录时显示
		TextView ChatPromptText=(TextView) mLayout.findViewById(R.id.ChatPromptText);
		ChatPromptText.setText("这里还没有人说过话...点击下"+"\n"+"方聊天框开始发言");//"暂无对话信息  点击"+"\"添加\""+"增加"+"\n"+"新的对话信息"
		
		  mLayout.findViewById(R.id.rela_more).setOnClickListener(this);
			picture_source_rela = (RelativeLayout) mLayout.findViewById(R.id.picture_source_rela);
			picture_source_rela.setOnClickListener(this);
			mLayout.findViewById(R.id.select_picsource).setOnClickListener(this);
			mLayout.findViewById(R.id.take_photo_rela).setOnClickListener(this);
			mLayout.findViewById(R.id.locpicture_rela).setOnClickListener(this);
			mLayout.findViewById(R.id.rela_cancle).setOnClickListener(this);
			mLayout.findViewById(R.id.iv_more).setOnClickListener(this);//拍照、 图片库
			
			send = (Button) mLayout.findViewById(R.id.send);
			send.setOnClickListener(this);
	    refreshableView = (RefreshableView) mLayout.findViewById(R.id.refreshableView);
	    messageListView = (ListView) mLayout.findViewById(R.id.messageListView);
	    addImageBtn = mLayout.findViewById(R.id.addImageBtn);

	    contentEdit = (PasteEditText) mLayout.findViewById(R.id.et_sendmessage);
	    chatTextLayout =mLayout.findViewById(R.id.chatTextLayout);
	    chatAudioLayout = mLayout.findViewById(R.id.chatRecordLayout);
	    turnToAudioBtn =mLayout. findViewById(R.id.turnToAudioBtn);
	    turnToTextBtn = mLayout.findViewById(R.id.turnToTextBtn);
	    recordBtn = (RecordButton) mLayout.findViewById(R.id.recordBtn);
	    chatTextLayout = mLayout.findViewById(R.id.chatTextLayout);
	    chatAddLayout = mLayout.findViewById(R.id.chatAddLayout);
	    addLocationBtn = mLayout.findViewById(R.id.addLocationBtn);
	    chatEmotionLayout = mLayout.findViewById(R.id.chatEmotionLayout);
	    showAddBtn = mLayout.findViewById(R.id.showAddBtn);
	    showEmotionBtn = mLayout.findViewById(R.id.showEmotionBtn);
	    sendBtn = mLayout.findViewById(R.id.sendBtn);
	    emotionPager = (ViewPager) mLayout.findViewById(R.id.emotionPager);
	    addCameraBtn = mLayout.findViewById(R.id.addCameraBtn);

	    sendBtn.setOnClickListener(this);
	    contentEdit.setOnClickListener(this);
	    addImageBtn.setOnClickListener(this);
	    addLocationBtn.setOnClickListener(this);
	    turnToAudioBtn.setOnClickListener(this);
	    turnToTextBtn.setOnClickListener(this);
	    showAddBtn.setOnClickListener(this);
	    showEmotionBtn.setOnClickListener(this);
	    addCameraBtn.setOnClickListener(this);

	    addLocationBtn.setVisibility(View.GONE);
	    
	    contentEdit.addTextChangedListener(new TextWatcher() {
			
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
	  }

	public void commonInit() {
//		    chatInstance = this;//??????
		    roomsTable = ChatManager.getInstance().getRoomsTable();
		    eventBus = EventBus.getDefault();
		    eventBus.register(this);
		    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		  }

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ViewGroup parent = (ViewGroup) mLayout.getParent();
		parent.removeView(mLayout);
	}

	@Override
	public void onClick(View v) {
		  
		  boolean sendSt;
		switch (v.getId()) {
			
			case R.id.rela_cancle:
				picture_source_rela.setVisibility(View.GONE);
				break;
			case R.id.locpicture_rela:
				selectImageFromLocal();
				picture_source_rela.setVisibility(View.GONE);
//				selectPicFromLocal();
				break;
			case R.id.take_photo_rela:
				selectImageFromCamera();
				picture_source_rela.setVisibility(View.GONE);
//				selectPicFromCamera();
				break;
			case R.id.picture_source_rela:
				picture_source_rela.setVisibility(View.GONE);
				break;
			case R.id.iv_more:
			case R.id.rela_more:
//				hideKeyboard();
				hideSoftInputView();
				picture_source_rela.setVisibility(View.VISIBLE);
				break;
				
			case R.id.send:
//				SharedPreferences sp = getSharedPreferences("text.xml", 0);
//				sendSt = sp.getBoolean("sendSt", true);
////				String s = input.getText().toString();
//				if(sendSt){
////					sendText(s);
//				}
				hideSoftInputView();
				sendText();
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						mChatPromptLayout.setVisibility(View.GONE);
					}
				});
				break;
			default:
				break;
			}
		  
	    //之所以不�?switch 是因为在Library�?R.id.btn 不是常量，不能用在switch case中�?
	    // 如果主项目有重名，Library 的会被重命名。，�?��不能是常量�?
	    if (v.getId() == R.id.sendBtn) {
	      sendText();
	    } else if (v.getId() == R.id.addImageBtn) {
	      selectImageFromLocal();
	    } else if (v.getId() == R.id.turnToAudioBtn) {
	      showAudioLayout();
	    } else if (v.getId() == R.id.turnToTextBtn) {
	      showTextLayout();
	    } else if (v.getId() == R.id.showAddBtn) {
	      toggleBottomAddLayout();
	    } else if (v.getId() == R.id.showEmotionBtn) {
	      toggleEmotionLayout();
	    } else if (v.getId() == R.id.addLocationBtn) {
	      onAddLocationButtonClicked(v);
	    } else if (v.getId() == R.id.textEdit) {
	      hideBottomLayoutAndScrollToLast();
	    } else if (v.getId() == R.id.addCameraBtn) {
	      selectImageFromCamera();
	    }
	  }
	
	  private void hideBottomLayoutAndScrollToLast() {
		    hideBottomLayout();
		    scrollToLast();
		  }

		  protected void hideBottomLayout() {
		    hideAddLayout();
		    chatEmotionLayout.setVisibility(View.GONE);
		  }

		  private void toggleEmotionLayout() {
		    if (chatEmotionLayout.getVisibility() == View.VISIBLE) {
		      chatEmotionLayout.setVisibility(View.GONE);
		    } else {
		      chatEmotionLayout.setVisibility(View.VISIBLE);
		      hideAddLayout();
		      showTextLayout();
		      hideSoftInputView();
		    }
		  }

		  protected void hideSoftInputView() {
		    if (getActivity().getWindow().getAttributes().softInputMode !=
		        WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
		      InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
		      View currentFocus = getActivity().getCurrentFocus();
		      if (currentFocus != null) {
		        manager.hideSoftInputFromWindow(currentFocus.getWindowToken(),
		            InputMethodManager.HIDE_NOT_ALWAYS);
		      }
		    }
		  }

		  private void toggleBottomAddLayout() {
		    if (chatAddLayout.getVisibility() == View.VISIBLE) {
		      hideAddLayout();
		    } else {
		      chatEmotionLayout.setVisibility(View.GONE);
		      hideSoftInputView();
		      showAddLayout();
		    }
		  }

		  private void hideAddLayout() {
		    chatAddLayout.setVisibility(View.GONE);
		  }

		  private void showAddLayout() {
		    chatAddLayout.setVisibility(View.VISIBLE);
		  }

		  private void showTextLayout() {
		    chatTextLayout.setVisibility(View.VISIBLE);
		    chatAudioLayout.setVisibility(View.GONE);
		  }

		  private void showAudioLayout() {
		    chatTextLayout.setVisibility(View.GONE);
		    chatAudioLayout.setVisibility(View.VISIBLE);
		    chatEmotionLayout.setVisibility(View.GONE);
		    hideSoftInputView();
		  }

		  public void selectImageFromLocal() {
//		    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//		      Intent intent = new Intent();
//		      intent.setType("image/*");
//		      intent.setAction(Intent.ACTION_GET_CONTENT);
//		      startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.chat_activity_select_picture)),
//		          GALLERY_REQUEST);
//		    } else {
//		      Intent intent = new Intent(Intent.ACTION_ _DOCUMENT);
//		      intent.addCategory(Intent.CATEGORY_OPENABLE);
//		      intent.setType("image/*");
//		      startActivityForResult(intent, GALLERY_KITKAT_REQUEST);
//		    }
				Intent intent;
				if (Build.VERSION.SDK_INT < 19) {
					intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setType("image/*");
					//片段中的
					getActivity().startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.chat_activity_select_picture)),
		          GALLERY_REQUEST);
				} else {
					intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					//片段中的
					getActivity().startActivityForResult(intent, GALLERY_REQUEST);
					Log.e("", "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
				}
		  }

		  public void selectImageFromCamera() {
		    Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		    Uri imageUri = Uri.fromFile(new File(localCameraPath));
		    takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
		    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
		    	//片段中的
		      getActivity().startActivityForResult(takePictureIntent, TAKE_CAMERA_REQUEST);
		    }
		  }

		  private void sendText() {
		    final String content = contentEdit.getText().toString();
//		    String content = input.getText().toString();
		    if (!TextUtils.isEmpty(content)) {
		      messageAgent.sendText(content);
		      contentEdit.setText("");
		    }
		  }
		  
	  public void onImageMessageViewClicked(AVIMImageMessage imageMessage, String localImagePath) {
		  Log.e("ChatActivity", "imageMessage.URL=="+imageMessage.getFileUrl());
		  Log.e("ChatActivity", "localImagePath=="+localImagePath);
		  Intent intent=new Intent(getActivity(), PhotoViewActivity.class);
		  intent.putExtra("FileUrl", imageMessage.getFileUrl());
		  intent.putExtra("localImagePath", localImagePath);
		startActivityForResult(intent, 910);
		  
	  }

	@Override
	public void onAddLocationButtonClicked(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationMessageViewClicked(AVIMLocationMessage locationMessage) {
		
	}
	
	
	public interface onActivityResultInterface{
		void onActivityResult(int requestCode, int resultCode, Intent data);
	}
	
//	@SuppressLint("NewApi")
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, intent);
//		Log.e("群聊的onActivityResult", "");
//	    if (resultCode == getActivity().RESULT_OK) {
//	      switch (requestCode) {
//	        case GALLERY_REQUEST:
//	        case GALLERY_KITKAT_REQUEST:
//	          if (intent == null) {
//	            toast("return intent is null");
//	            return;
//	          }
//	          Uri uri;
//	          if (requestCode == GALLERY_REQUEST) {
//	            uri = intent.getData();
//	          } else {
//	        	  Log.e("", "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
//	            //for Android 4.4
//	            uri = intent.getData();
//	            final int takeFlags = intent.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION
//	                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//	            getActivity().getContentResolver().takePersistableUriPermission(uri, takeFlags);
//	          }
//	          String localSelectPath = ProviderPathUtils.getPath(getActivity(), uri);
//	          messageAgent.sendImage(localSelectPath,getActivity());
//	          hideBottomLayout();
//	          break;
//	        case TAKE_CAMERA_REQUEST:
//	          messageAgent.sendImage(localCameraPath,getActivity());
//	          hideBottomLayout();
//	          break;
//	      }
//	    }
//	}
	
	  @Override
	public void onDestroy() {
	    chatInstance = null;
	    eventBus.unregister(this);
	    super.onDestroy();
	  }
	  
	  @Override
	public void onResume() {
	    super.onResume();
	    if (isConversationEmpty(conversation)) {
	      return;
	    }
	    ChatManager.setCurrentChattingConvid(conversation.getConversationId());
	    SdPkUser.setGetSource(1);//从新传值1说明是群聊来的
	  }

	  @Override
	public void onPause() {
	    super.onPause();
	    roomsTable.clearUnread(conversation.getConversationId());
	    ChatManager.setCurrentChattingConvid(null);
	  }
	  
	  public void onEvent(MessageEvent messageEvent) {
		    final AVIMTypedMessage message = messageEvent.getMessage();
		    if (message.getConversationId().equals(conversation
		        .getConversationId())) {
		      if (messageEvent.getType() == MessageEvent.Type.Come) {
		        new CacheMessagesTask(getActivity(), Arrays.asList(message)) {
		          @Override
		          void onSucceed(List<AVIMTypedMessage> messages) {
		            addMessageAndScroll(message);
		          }
		        }.execute();
		      } else if (messageEvent.getType() == MessageEvent.Type.Receipt) {
		        //Utils.i("receipt");
		        AVIMTypedMessage originMessage = findMessage(message.getMessageId());
		        if (originMessage != null) {
		          originMessage.setMessageStatus(message.getMessageStatus());
		          originMessage.setReceiptTimestamp(message.getReceiptTimestamp());
		          adapter.notifyDataSetChanged();
		        }
		      }
		    }
		  }
	  
}
