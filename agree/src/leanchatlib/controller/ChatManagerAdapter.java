package leanchatlib.controller;

import java.util.List;

import leanchatlib.model.UserInfo;
import android.content.Context;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;

/**
 * �����û���Ϣ��֪ͨ����
 */
public interface ChatManagerAdapter {
  /**
   * ͬ����ȡ�û���Ϣ����������ҳ��� MessageAdapter
   * @param userId
   * @return
   */
  UserInfo getUserInfoById(String userId);

  /**
   * Ϊ��֧���ܹ�ͬ����ȡ�û���Ϣ�����Ȼ����û���Ϣ�����ں�̨�̵߳��ô˺���
   * @param userIds �����ܱ� getUserInfoById() �õ���userId��Ҳ������ҳ����Ϣ�ķ�������
   * @throws Exception ���׳������쳣
   */
  void cacheUserInfoByIdsInBackground(List<String> userIds) throws Exception;

  /**
   * ��������Ϣ��������������������˺�����
   * Ӧ���ں�̨ʱ��Ӧ����ǰ̨����������ҳ��ʱ��������ҳ�浫����Ŀ��Ի�ʱ
   * @param context ���㵯��
   * @param selfId �Լ��� userid
   * @param conversation ��Ϣ�����ĶԻ�
   * @param message ��������Ϣ
   */
  void shouldShowNotification(Context context, String selfId, AVIMConversation conversation, AVIMTypedMessage message);
}
