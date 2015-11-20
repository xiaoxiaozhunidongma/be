package leanchatlib.adapter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import leanchatlib.controller.ChatManager;
import leanchatlib.controller.MessageHelper;
import leanchatlib.model.ConversationType;
import leanchatlib.model.UserInfo;
import leanchatlib.utils.PhotoUtils;
import leanchatlib.view.PlayButton;
import leanchatlib.view.ViewHolder;

import org.ocpsoft.prettytime.PrettyTime;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.BJ.javabean.Group_ReadAllUser;
import com.BJ.javabean.ReadUserAllFriends;
import com.BJ.javabean.User;
import com.BJ.utils.SdPkUser;
import com.activeandroid.query.Select;
import com.avos.avoscloud.im.v2.AVIMReservedMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMLocationMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.biju.R;
import com.biju.function.GroupActivity;
import com.example.testleabcloud.ChatActivityLean;
import com.fragment.CommonFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ChatMessageAdapter extends BaseAdapter {
  private static PrettyTime prettyTime = new PrettyTime();
  private ConversationType conversationType;
  private int msgViewTypes = 8;
  private ClickListener clickListener;
  private Context context;
  private List<AVIMTypedMessage> datas = new ArrayList<AVIMTypedMessage>();
  private String CurrUserUrl;
	private String beginStr = "http://picstyle.beagree.com/";
	private String endStr = "@!";
	private HashMap<Integer, String> otherAvaurl;

  public ChatMessageAdapter(Context context, ConversationType conversationType, HashMap<Integer, String> fromAvaUrlMap, String CurrUserUrl) {
    this.context = context;
    this.conversationType = conversationType;
	this.otherAvaurl = fromAvaUrlMap;
	this.CurrUserUrl = CurrUserUrl;
  }

  public List<AVIMTypedMessage> getDatas() {
    return datas;
  }

  public void setDatas(List<AVIMTypedMessage> datas) {
    this.datas = datas;
  }

  // time
  public static String millisecsToDateString(long timestamp) {
    long gap = System.currentTimeMillis() - timestamp;
    if (gap < 1000 * 60 * 60 * 24) {
      String s = prettyTime.format(new Date(timestamp));
      //return s.replace(" ", "");
      return s;
    } else {
      SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
      return format.format(new Date(timestamp));
    }
  }

  public static boolean haveTimeGap(long lastTime, long time) {
    int gap = 1000 * 60 * 3;
    return time - lastTime > gap;
  }

  public void setClickListener(ClickListener clickListener) {
    this.clickListener = clickListener;
  }

  @Override
  public int getItemViewType(int position) {
    AVIMTypedMessage msg = datas.get(position);
    boolean comeMsg = isComeMsg(msg);

    MsgViewType viewType;
    AVIMReservedMessageType msgType = AVIMReservedMessageType.getAVIMReservedMessageType(msg.getMessageType());
    switch (msgType) {
      case TextMessageType:
        viewType = comeMsg ? MsgViewType.ComeText : MsgViewType.ToText;
        break;
      case ImageMessageType:
        viewType = comeMsg ? MsgViewType.ComeImage : MsgViewType.ToImage;
        break;
      case AudioMessageType:
        viewType = comeMsg ? MsgViewType.ComeAudio : MsgViewType.ToAudio;
        break;
      case LocationMessageType:
        viewType = comeMsg ? MsgViewType.ComeLocation : MsgViewType.ToLocation;
        break;
      default:
        viewType = comeMsg ? MsgViewType.ComeText : MsgViewType.ToText;
        break;
    }
    return viewType.getValue();
  }

  @Override
  public int getViewTypeCount() {
    return msgViewTypes;
  }

  boolean isComeMsg(AVIMTypedMessage msg) {
    return !MessageHelper.fromMe(msg);
  }

  @Override
  public int getCount() {
    return datas.size();
  }

  @Override
  public Object getItem(int i) {
    return datas.get(i);
  }

  @Override
  public long getItemId(int i) {
    return i;
  }

  public View getView(int position, View conView, ViewGroup parent) {
    AVIMTypedMessage msg = datas.get(position);
    boolean isComMsg = isComeMsg(msg);
    if (conView == null) {
      conView = createViewByType(AVIMReservedMessageType.getAVIMReservedMessageType(msg.getMessageType()), isComMsg);
    }
//    TextView sendTimeView = ViewHolder.findViewById(conView, R.id.sendTimeView);
    TextView tv_sendtime = ViewHolder.findViewById(conView, R.id.tv_sendtime);
    TextView tv_yymmdd = (TextView) conView.findViewById(R.id.tv_yymmdd);
    TextView tv_hhmm = (TextView) conView.findViewById(R.id.tv_hhmm);
//    TextView contentView = ViewHolder.findViewById(conView, R.id.textContent);
    TextView contentView = ViewHolder.findViewById(conView, R.id.tv_chatcontent);
//    View contentLayout = ViewHolder.findViewById(conView, R.id.contentLayout);
//    ImageView imageView = ViewHolder.findViewById(conView, R.id.imageView);
    ImageView imageView = ViewHolder.findViewById(conView, R.id.iv_sendPicture);
//    ImageView avatarView = ViewHolder.findViewById(conView, R.id.avatar);
    ImageView avatarView = ViewHolder.findViewById(conView, R.id.iv_userhead);
    PlayButton playBtn = ViewHolder.findViewById(conView, R.id.playBtn);
    
    TextView locationView = ViewHolder.findViewById(conView, R.id.locationView);
//    TextView usernameView = ViewHolder.findViewById(conView, R.id.username);
    TextView usernameView = ViewHolder.findViewById(conView, R.id.tv_userid);

//    View statusSendFailed = ViewHolder.findViewById(conView, R.id.status_send_failed);
    View statusSendFailed = ViewHolder.findViewById(conView, R.id.msg_status);
    View statusSendSucceed = ViewHolder.findViewById(conView, R.id.status_send_succeed);
    View statusSendStart = ViewHolder.findViewById(conView, R.id.pb_sending);

//    // timestamp
//    if (position == 0 || haveTimeGap(datas.get(position - 1).getTimestamp(),
//        msg.getTimestamp())) {
//      sendTimeView.setVisibility(View.VISIBLE);
//      sendTimeView.setText(millisecsToDateString(msg.getTimestamp()));
//    } else {
//      sendTimeView.setVisibility(View.GONE);
//    }
    SimpleDateFormat format2=new SimpleDateFormat("HH:mm");
	String HHmm=format2.format(new Date(msg.getTimestamp()));
	if(tv_sendtime !=null){
		tv_sendtime .setVisibility(View.VISIBLE);
		tv_sendtime .setText(HHmm);
	}
	
	SimpleDateFormat format=new SimpleDateFormat("yy/M/d");
	String yymmdd=format.format(new Date(msg.getTimestamp()));//??
	String HHmm2=format2.format(new Date(msg.getTimestamp()));
	if(tv_yymmdd!=null){
		tv_yymmdd.setText(yymmdd);
	}
	if(tv_hhmm!=null){
		tv_hhmm.setText(HHmm2);
	}

    final UserInfo user = ChatManager.getInstance().getChatManagerAdapter().getUserInfoById(msg.getFrom());
    
	//查表所有数据(小组群聊成员)
	List<Group_ReadAllUser> Group_ReadAllUserList = new Select().from(Group_ReadAllUser.class).execute();
	
	//查表所有数据(聊天室所有好友)
	List<User> DBuserList = new Select().from(User.class).execute();
	
	String NicName=user.getUsername();//默认为ID数字
	//以下两个方法可覆盖实现昵称更改。其中一个for能进入就可以设置NicName,两个都没有才是默认的数字ID
	for (int i = 0; i < Group_ReadAllUserList.size(); i++) {
		Group_ReadAllUser group_ReadAllUser = Group_ReadAllUserList.get(i);
		Integer fk_user = group_ReadAllUser.getFk_user();
		if(String.valueOf(fk_user).equals(user.getUsername())){
			NicName=group_ReadAllUser.getNickname();
		}
	}
	
	for (int i = 0; i < DBuserList.size(); i++) {
		User user222 = DBuserList.get(i);
		Integer pk_user = user222.getPk_user();
		if(String.valueOf(pk_user).equals(user.getUsername())){
			NicName=user222.getNickname();
		}
	}
    if (user == null) {
      throw new IllegalStateException("user is null, please implement ChatManagetAdapter.cacheUserInfoById()");
    }
    if (isComMsg) {
      if (conversationType == null) {
        return conView;
      }
      if (conversationType == ConversationType.Single) {
//        usernameView.setVisibility(View.GONE);
        usernameView.setVisibility(View.VISIBLE);
//        usernameView.setText(user.getUsername());
        usernameView.setText(NicName);
      } else {
        usernameView.setVisibility(View.VISIBLE);
//        usernameView.setText(user.getUsername());
        usernameView.setText(NicName);
      }
    }
    
    
    if(avatarView!=null){
    	if(isComMsg==false){
    		ImageLoader.getInstance().displayImage(CurrUserUrl, avatarView, PhotoUtils.avatarImageOptions);
//    		Log.e("ChatMessageAdapter", "completeURL==="+CurrUserUrl);
    	}else{
    		String from = msg.getFrom();
    		String FriendUrl = otherAvaurl.get(Integer.valueOf(from));
//    		String FriendUrl = beginStr+avatar_path+endStr+"mini-avatar";
			ImageLoader.getInstance().displayImage(FriendUrl, avatarView, PhotoUtils.avatarImageOptions);
    	}
    	avatarView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String Click_user=user.getUsername();
				int source=SdPkUser.GetSource;
				switch (source) {
				case 1:
					//群聊来的
					List<Group_ReadAllUser> Group_AllUser=SdPkUser.getHomeClickUser();
					for (int i = 0; i < Group_AllUser.size(); i++) {
						Group_ReadAllUser Group_user=Group_AllUser.get(i);
						Integer pk_user=Group_user.getPk_user();
						if(Click_user.equals(String.valueOf(pk_user))){
							Integer sd_pk_user=SdPkUser.getsD_pk_user();
							if(!(Click_user.equals(String.valueOf(sd_pk_user)))){
								SdPkUser.setGroupChatUser(Group_user);
								CommonFragment.getOpen.Open();
								GroupActivity.getGroupChat.GroupChat();
							}
						}
					}
					break;
				case 2:
					//聊天室来的
					List<User> AllUser=SdPkUser.getUser();
					for (int i = 0; i < AllUser.size(); i++) {
						User user=AllUser.get(i);
						Integer pk_user=user.getPk_user();
						Log.e("============", " 获取到当前的pk_user==="+pk_user);
						if(Click_user.equals(String.valueOf(pk_user))){
							Integer sd_pk_user=SdPkUser.getsD_pk_user();
							if(!(Click_user.equals(String.valueOf(sd_pk_user)))){
								SdPkUser.setClickUser(user);
								Log.e("============", " 进入了IF==="+pk_user);
								ChatActivityLean.getOpen.Open();
								ChatActivityLean.getPersonal.PersonalData();
							}
						}
					}
					break;
				default:
					break;
				}
				Log.e("============", " 获取到当前的头像ID==="+user.getUsername());
			}
		});
    	
//    	ImageLoader.getInstance().displayImage(user.getAvatarUrl(), avatarView, PhotoUtils.avatarImageOptions);
    }

    AVIMReservedMessageType type = AVIMReservedMessageType.getAVIMReservedMessageType(msg.getMessageType());
    switch (type) {
      case TextMessageType:
        AVIMTextMessage textMsg = (AVIMTextMessage) msg;
//        	contentView.setText(EmotionHelper.replace(ChatManager.getContext(), textMsg.getText()));
        contentView.setText(textMsg.getText());
//        contentLayout.requestLayout();
        break;
      case ImageMessageType:
        AVIMImageMessage imageMsg = (AVIMImageMessage) msg;
//        Log.e("ChatmessageAdapter", "imageView对象=="+imageView);
        PhotoUtils.displayImageCacheElseNetwork(imageView, MessageHelper.getFilePath(imageMsg),
            imageMsg.getFileUrl());
        setImageOnClickListener(imageView, imageMsg);
        break;
      case AudioMessageType:
        initPlayBtn(msg, playBtn);
        break;
      case LocationMessageType:
        setLocationView(msg, locationView);
        break;
      default:
    	    	 contentView.setText("鏈煡娑堟伅");
//        contentLayout.requestLayout();
        break;
    }
    if (isComMsg == false) {
      hideStatusViews(statusSendStart, statusSendFailed, statusSendSucceed);
      setSendFailedBtnListener(statusSendFailed, msg);
      switch (msg.getMessageStatus()) {
        case AVIMMessageStatusFailed:
          statusSendFailed.setVisibility(View.VISIBLE);
      	if(tv_sendtime !=null){
    		tv_sendtime .setVisibility(View.GONE);
    	}
          break;
        case AVIMMessageStatusSent:
          if (conversationType == ConversationType.Single) {
//            statusSendSucceed.setVisibility(View.VISIBLE);
        	  if(statusSendSucceed!=null){
        		  statusSendSucceed.setVisibility(View.GONE);
        	  }
          }
          break;
        case AVIMMessageStatusSending:
          statusSendStart.setVisibility(View.VISIBLE);
        	if(tv_sendtime !=null){
        		tv_sendtime .setVisibility(View.GONE);
        	}
          break;
        case AVIMMessageStatusNone:
        case AVIMMessageStatusReceipt:
          break;
      }
    }
    return conView;
  }

  private void setSendFailedBtnListener(View statusSendFailed, final AVIMTypedMessage msg) {
	if(statusSendFailed!=null){
		  
    statusSendFailed.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (clickListener != null) {
          clickListener.onFailButtonClick(msg);
        }
      }
    });
	}
  }

  private void hideStatusViews(View statusSendStart, View statusSendFailed, View statusSendSucceed) {
	  if(statusSendFailed!=null){
		  statusSendFailed.setVisibility(View.GONE);
	  }
	  if(statusSendStart!=null){
		  statusSendStart.setVisibility(View.GONE);
	  }
	  if(statusSendSucceed!=null){
		  statusSendSucceed.setVisibility(View.GONE);
	  }
  }

  public void setLocationView(AVIMTypedMessage msg, TextView locationView) {
    final AVIMLocationMessage locMsg = (AVIMLocationMessage) msg;
    locationView.setText(locMsg.getText());
    locationView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View arg0) {
        if (clickListener != null) {
          clickListener.onLocationViewClick(locMsg);
        }
      }
    });
  }

  private void initPlayBtn(AVIMTypedMessage msg, PlayButton playBtn) {
    playBtn.setLeftSide(isComeMsg(msg));
    playBtn.setPath(MessageHelper.getFilePath(msg));
  }

  private void setImageOnClickListener(ImageView imageView, final AVIMImageMessage imageMsg) {
    imageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (clickListener != null) {
          clickListener.onImageViewClick(imageMsg);
        }
      }
    });
  }

  public View createViewByType(AVIMReservedMessageType type, boolean comeMsg) {
    View baseView = null;
  switch (type) {
  case TextMessageType:
	    if (comeMsg) {
	        baseView = View.inflate(context, R.layout.chat_item_base_left2, null);
	      } else {
	        baseView = View.inflate(context, R.layout.chat_item_base_right2, null);
	      }
    break;
  case ImageMessageType:
	    if (comeMsg) {
	        baseView = View.inflate(context, R.layout.row_received_picture2, null);
	      } else {
	        baseView = View.inflate(context, R.layout.row_sent_picture2, null);
	      }
    break;
  default:
    break;
}
//    if (comeMsg) {
//    	
//      baseView = View.inflate(context, R.layout.chat_item_base_left, null);
//    } else {
//      baseView = View.inflate(context, R.layout.chat_item_base_right, null);
//    }
//    LinearLayout contentView = (LinearLayout) baseView.findViewById(R.id.contentLayout);
//    int contentId;
//    switch (type) {
//      case TextMessageType:
//        contentId = R.layout.chat_item_text;
//        break;
//      case AudioMessageType:
//        contentId = R.layout.chat_item_audio;
//        break;
//      case ImageMessageType:
//        contentId = R.layout.chat_item_image;
//        break;
//      case LocationMessageType:
//        contentId = R.layout.chat_item_location;
//        break;
//      default:
//        contentId = R.layout.chat_item_text;
//        break;
//    }
//    contentView.removeAllViews();
//    View content = View.inflate(context, contentId, null);
//    if (type == AVIMReservedMessageType.AudioMessageType) {
//      PlayButton btn = (PlayButton) content;
//      btn.setLeftSide(comeMsg);
//    } else if (type == AVIMReservedMessageType.TextMessageType) {
//      TextView textView = (TextView) content;
//      if (comeMsg) {
//        textView.setTextColor(Color.BLACK);
//      } else {
//        textView.setTextColor(Color.WHITE);
//      }
//    }
//    contentView.addView(content);
    return baseView;
  }

  public void add(AVIMTypedMessage message) {
    datas.add(message);
    notifyDataSetChanged();
  }

  private enum MsgViewType {
    ComeText(0), ToText(1), ComeImage(2), ToImage(3), ComeAudio(4), ToAudio(5), ComeLocation(6), ToLocation(7);
    int value;

    MsgViewType(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }

  public interface ClickListener {
    void onFailButtonClick(AVIMTypedMessage msg);

    void onLocationViewClick(AVIMLocationMessage locMsg);

    void onImageViewClick(AVIMImageMessage imageMsg);
  }
}
