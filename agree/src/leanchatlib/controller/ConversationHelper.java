package leanchatlib.controller;

import java.util.List;

import leanchatlib.model.ConversationType;
import leanchatlib.model.UserInfo;
import leanchatlib.utils.LogUtils;

import com.avos.avoscloud.im.v2.AVIMConversation;

/**
 * Created by lzw on 15/4/26.
 */
public class ConversationHelper {

  public static boolean isValidConversation(AVIMConversation conversation) {
    if (conversation == null) {
      LogUtils.d("invalid reason : conversation is null");
      return false;
    }
    if (conversation.getMembers() == null || conversation.getMembers().size() == 0) {
      LogUtils.d("invalid reason : conversation members null or empty");
      return false;
    }
    Object type = conversation.getAttribute(ConversationType.TYPE_KEY);
    if (type == null) {
      LogUtils.d("invalid reason : type is null");
      return false;
    }

    int typeInt = (Integer) type;
    if (typeInt == ConversationType.Single.getValue()) {
      if (conversation.getMembers().size() != 2 ||
          conversation.getMembers().contains(ChatManager.getInstance().getSelfId()) == false) {
        LogUtils.d("invalid reason : oneToOne conversation not correct");
        return false;
      }
    } else if (typeInt == ConversationType.Group.getValue()) {

    } else {
      LogUtils.d("invalid reason : typeInt wrong");
      return false;
    }
    return true;
  }

  public static ConversationType typeOfConversation(AVIMConversation conversation) {
    if (isValidConversation(conversation)) {
      Object typeObject = conversation.getAttribute(ConversationType.TYPE_KEY);
      int typeInt = (Integer) typeObject;
      return ConversationType.fromInt(typeInt);
    } else {
      LogUtils.e("invalid conversation ");
      // ��Ϊ Group ����Ҫȡ otherId�����û��ô�ϸ񣬱��⵼�±���
      return ConversationType.Group;
    }
  }

  /**
   * ��ȡ���ĶԻ�������һ���˵� userId
   *
   * @param conversation
   * @return ����Ƿ��Ի�����Ϊ selfId
   */
  public static String otherIdOfConversation(AVIMConversation conversation) {
    if (isValidConversation(conversation)) {
      if (typeOfConversation(conversation) == ConversationType.Single) {
        List<String> members = conversation.getMembers();
        if (members.size() == 2) {
          if (members.get(0).equals(ChatManager.getInstance().getSelfId())) {
            return members.get(1);
          } else {
            return members.get(0);
          }
        }
      }
    }
    // �����쳣�����ؿ���ʹ�õ� userId
    return ChatManager.getInstance().getSelfId();
  }

  public static String nameOfConversation(AVIMConversation conversation) {
    if (isValidConversation(conversation)) {
      if (typeOfConversation(conversation) == ConversationType.Single) {
        String otherId = otherIdOfConversation(conversation);
        UserInfo user = ChatManager.getInstance().getChatManagerAdapter().getUserInfoById(otherId);
        if (user != null) {
          return user.getUsername();
        } else {
          LogUtils.e("use is null");
          return "�Ի�";
        }
      } else {
        return conversation.getName();
      }
    } else {
      return "";
    }
  }

  public static String titleOfConversation(AVIMConversation conversation) {
    if (isValidConversation(conversation)) {
      if (typeOfConversation(conversation) == ConversationType.Single) {
        return nameOfConversation(conversation);
      } else {
        List<String> members = conversation.getMembers();
        return nameOfConversation(conversation) + " (" + members.size() + ")";
      }
    } else {
      return "";
    }
  }
}
