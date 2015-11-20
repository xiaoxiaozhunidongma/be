package leanchatlib.controller;

import java.io.File;
import java.io.IOException;

import leanchatlib.utils.LogUtils;
import leanchatlib.utils.PathUtils;
import leanchatlib.utils.PhotoUtils;
import leanchatlib.utils.Utils;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMLocationMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

/**
 * Created by lzw on 14/11/23.
 */
public class MessageAgent {
  private AVIMConversation conversation;
  private ChatManager chatManager;
  private SendCallback sendCallback = new SendCallback() {
    @Override
    public void onError(AVIMTypedMessage message, Exception e) {

    }

    @Override
    public void onSuccess(AVIMTypedMessage message) {

    }
  };

  public MessageAgent(AVIMConversation conversation) {
    this.conversation = conversation;
    chatManager = ChatManager.getInstance();
  }

  public void setSendCallback(SendCallback sendCallback) {
    this.sendCallback = sendCallback;
  }

  private void sendMsg(final AVIMTypedMessage msg, final String originPath, final SendCallback callback) {
    if (!chatManager.isConnect()) {
      LogUtils.i("im not connect");
    }
    conversation.sendMessage(msg, AVIMConversation.RECEIPT_MESSAGE_FLAG, new AVIMConversationCallback() {

	@Override
	public void done(AVIMException e) {
		//����ὫͼƬ�ļ��Ƴ���������
//        if (e == null && originPath != null) {
//            File tmpFile = new File(originPath);
//            File newFile = new File(PathUtils.getChatFilePath(msg.getMessageId()));
//            boolean result = tmpFile.renameTo(newFile);
//            if (!result) {
//              LogUtils.e("move file failed, can't use local cache");
//            }
//          }
          if (callback != null) {
            if (e != null) {
              callback.onError(msg, e);
            } else {
              ChatManager.getInstance().getRoomsTable().insertRoom(conversation.getConversationId());
              ChatManager.getInstance().putLatestMessage(msg);
              callback.onSuccess(msg);
            }
          }
        }
    });
  }

  public void resendMessage(final AVIMTypedMessage msg, final SendCallback sendCallback) {
    conversation.sendMessage(msg, AVIMConversation.RECEIPT_MESSAGE_FLAG, new AVIMConversationCallback() {

	@Override
	public void done(AVIMException e) {
        if (sendCallback != null) {
            if (e != null) {
              sendCallback.onError(msg, e);
            } else {
              sendCallback.onSuccess(msg);
            }
          }
        }
    });
  }

  public void sendText(String content) {
    AVIMTextMessage textMsg = new AVIMTextMessage();
    textMsg.setText(content);
    sendMsg(textMsg, null, sendCallback);
  }

  public void sendImage(String imagePath,Context context ) {
	  //newPath��ͼƬ�о���ѹ���������ͼʱ������,����getChatFilePath()����ɾ������ͼƬ����
    final String newPath = PathUtils.getChatFilePath(Utils.uuid());
//    PhotoUtils.compressImage(imagePath, newPath, context);
    String compressPath = PhotoUtils.MycompressImage(imagePath,newPath,context,1280);
	Log.e("sendImage", "compressPath=="+compressPath);
	
	BitmapFactory.Options options = new BitmapFactory.Options();
	options.inJustDecodeBounds = true;
	BitmapFactory.decodeFile(compressPath, options);
	Log.e("sendImage", "ѹ����options.outWidth=="+options.outWidth);
	Log.e("sendImage", "ѹ����options.outHeight=="+options.outHeight);
    Log.e("sendImage", "ѹ����newPath=="+compressPath);
    
    BitmapFactory.Options options2 = new BitmapFactory.Options();
    options2.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(imagePath, options2);
    Log.e("sendImage", "ԭͼoptions.outWidth=="+options2.outWidth);
    Log.e("sendImage", "ԭͼoptions.outHeight=="+options2.outHeight);
    Log.e("sendImage", "ԭͼnewPath=="+compressPath);
    
    try {
//      AVIMImageMessage imageMsg = new AVIMImageMessage(newPath);
//      sendMsg(imageMsg, newPath, sendCallback);
    	//ʹ��ѹ����·��
      AVIMImageMessage imageMsg = new AVIMImageMessage(compressPath);
      sendMsg(imageMsg, compressPath, sendCallback);
    } catch (IOException e) {
      LogUtils.logException(e);
    }
  }

  public void sendLocation(double latitude, double longitude, String address) {
    AVIMLocationMessage locationMsg = new AVIMLocationMessage();
    AVGeoPoint geoPoint = new AVGeoPoint(latitude, longitude);
    locationMsg.setLocation(geoPoint);
    locationMsg.setText(address);
    sendMsg(locationMsg, null, sendCallback);
  }

  public void sendAudio(String audioPath) {
    try {
      AVIMAudioMessage audioMsg = new AVIMAudioMessage(audioPath);
      sendMsg(audioMsg, audioPath, sendCallback);
    } catch (IOException e) {
      LogUtils.logException(e);
    }
  }

  public interface SendCallback {

    void onError(AVIMTypedMessage message, Exception e);

    void onSuccess(AVIMTypedMessage message);

  }
}
