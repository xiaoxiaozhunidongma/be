package com.example.testleabcloud;

import android.view.View;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMLocationMessage;

/**
 * Created by lzw on 15/7/16.
 */
public interface ChatActivityEventListener {

  /**
   * 当发送地理位置按钮被点击�?   *
   * @param v
   */
  void onAddLocationButtonClicked(View v);

  /**
   * 当地图消息view被点击时
   *
   * @param locationMessage
   */
  void onLocationMessageViewClicked(AVIMLocationMessage locationMessage);

  /**
   * 当图片消�?view 被点击时
   *
   * @param imageMessage
   * @param localImagePath 本地缓存的路径，可能并没有文�?   */
  void onImageMessageViewClicked(AVIMImageMessage imageMessage, String localImagePath);
}
