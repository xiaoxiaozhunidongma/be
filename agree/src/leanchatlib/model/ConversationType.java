package leanchatlib.model;

/**
 * �Ի����͡���Ϊ�����˶Ի������ǵ��ģ�Ҳ������Ⱥ�ġ�������Ҫ�������������
 * Created by lzw on 14/11/18.
 */
public enum ConversationType {
  Single(0), Group(1);
  /**
   * ������ʱ��ֱ������ type �ֶ�
   */
  public static final String TYPE_KEY = "type";
  /**
   * ���ҶԻ���ʱ��Ҫ��ǰ׺ attr. ��ʵtype������conversation��attr��
   * ��¼��վ��̨��_Conversation ��ɿ���
   */
  public static final String ATTR_TYPE_KEY = "attr.type";

  int value;

  ConversationType(int value) {
    this.value = value;
  }

  public static ConversationType fromInt(int i) {
    if (i < 2) {
      return values()[i];
    } else {
      return Group;
    }
  }

  public int getValue() {
    return value;
  }
}
