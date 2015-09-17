package com.biju.search.util;

/**
 *
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.Relation;
import android.provider.ContactsContract.CommonDataKinds.SipAddress;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.CommonDataKinds.Website;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.Groups;

public class GetContactsInfo {
	private List<Object> list;
	private Context context;
	private JSONObject contactData;
	private JSONObject jsonObject;

	public GetContactsInfo(Context context) {
		this.context = context;
	}

	/**
	 * 1.������Ϣ-StructuredName  
	 * 2.��֯(��˾,ְλ)-
	 * 3.����
	 * 4.�ʼ�
	 * 5.��ַ
	 * 6.�ƺ�
	 * 7.��վ
	 * 8.��ʱ��Ϣ
	 *-- 9.������ͨ��
	 * 10.����
	 * 11.ũ������
	 * 12.��ע
	 * 13.Ⱥ��
	 * 
	 * 
	 * @return
	 * @throws JSONException
	 */
	@SuppressWarnings("unused")
	public String getContactInfo() throws JSONException {
		//���ͨѶ¼��Ϣ ��URI��ContactsContract.Contacts.CONTENT_URI
		list = new ArrayList<Object>();
		contactData = new JSONObject();
		String mimetype = "";
		int oldrid = -1;
		int contactId = -1;
		HashMap<String, String> groupsMap = new HashMap<String, String>();

		Cursor cursorGroup = context.getContentResolver().query(Groups.CONTENT_URI, null, null, null, null);
		while (cursorGroup.moveToNext()) {
			groupsMap.put(cursorGroup.getString(cursorGroup.getColumnIndex(Groups._ID)), cursorGroup.getString(cursorGroup.getColumnIndex("title")));
		}
		if (cursorGroup != null) {
			cursorGroup.close();
		}
		Cursor cursor = context.getContentResolver().query(Data.CONTENT_URI, null, null, null, Data.RAW_CONTACT_ID);
		//System.out.println(cursor.getCount());
		int numm = 0;
		while (cursor.moveToNext()) {
			contactId = cursor.getInt(cursor.getColumnIndex(Data.RAW_CONTACT_ID));
			if (oldrid != contactId) {
				jsonObject = new JSONObject();
				contactData.putOpt("contact" + numm, jsonObject);
				numm++;
				oldrid = contactId;
			}
			if (contactId == 1892) {
				int i = 0;
				i++;
			}

			/*
			 * ȡ��mimetype����
			 * MIME types are:
			 * CommonDataKinds.StructuredName StructuredName.CONTENT_ITEM_TYPE
			 * CommonDataKinds.Phone Phone.CONTENT_ITEM_TYPE
			 * CommonDataKinds.Email Email.CONTENT_ITEM_TYPE
			 * CommonDataKinds.Photo Photo.CONTENT_ITEM_TYPE
			 * CommonDataKinds.Organization Organization.CONTENT_ITEM_TYPE
			 * CommonDataKinds.Im Im.CONTENT_ITEM_TYPE
			 * CommonDataKinds.Nickname Nickname.CONTENT_ITEM_TYPE
			 * CommonDataKinds.Note Note.CONTENT_ITEM_TYPE
			 * CommonDataKinds.StructuredPostal StructuredPostal.CONTENT_ITEM_TYPE
			 * CommonDataKinds.GroupMembership GroupMembership.CONTENT_ITEM_TYPE
			 * CommonDataKinds.Website Website.CONTENT_ITEM_TYPE
			 * CommonDataKinds.Event Event.CONTENT_ITEM_TYPE
			 * CommonDataKinds.Relation Relation.CONTENT_ITEM_TYPE
			 * CommonDataKinds.SipAddress SipAddress.CONTENT_ITEM_TYPE
			 */
			mimetype = cursor.getString(cursor.getColumnIndex(Data.MIMETYPE));

			if (mimetype.equals(StructuredName.CONTENT_ITEM_TYPE)) {//姓名
				JSONObject name = jsonObject.optJSONObject("name");
				if (name == null) {
					name = new JSONObject();
					jsonObject.putOpt("name", name);
				}

				//����ǰ׺
				String prefix = cursor.getString(cursor.getColumnIndex(StructuredName.PREFIX));
				name.putOpt("prefix", prefix);

				//����
				String family_name = cursor.getString(cursor.getColumnIndex(StructuredName.FAMILY_NAME));
				name.putOpt("family_name", family_name);

				//�м���
				String middleName = cursor.getString(cursor.getColumnIndex(StructuredName.MIDDLE_NAME));
				name.putOpt("middle_name", middleName);

				//����
				String given_name = cursor.getString(cursor.getColumnIndex(StructuredName.GIVEN_NAME));
				name.putOpt("given_name", given_name);

				//���ƺ�׺
				String suffix = cursor.getString(cursor.getColumnIndex(StructuredName.SUFFIX));
				name.putOpt("suffix", suffix);

				String phonetic_given_name = cursor.getString(cursor.getColumnIndex(StructuredName.PHONETIC_GIVEN_NAME));
				name.putOpt("phonetic_given_name", phonetic_given_name);

				String phonetic_middle_name = cursor.getString(cursor.getColumnIndex(StructuredName.PHONETIC_MIDDLE_NAME));
				name.putOpt("phonetic_middle_name", phonetic_middle_name);

				String phonetic_family_name = cursor.getString(cursor.getColumnIndex(StructuredName.PHONETIC_FAMILY_NAME));
				name.putOpt("phonetic_family_name", phonetic_family_name);

			} else if (mimetype.equals(Phone.CONTENT_ITEM_TYPE)) {//电话
				//JSONObject phone=new JSONObject();
				JSONObject phone = jsonObject.optJSONObject("phone");
				if (phone == null) {
					phone = new JSONObject();
					jsonObject.putOpt("phone", phone);
				}

				// ȡ���绰����
				int phoneType = cursor.getInt(cursor.getColumnIndex(Phone.TYPE));
				String number = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));

				switch (phoneType) {
				case Phone.TYPE_MOBILE:
					phone.accumulate("mobile", number);
					break;
				case Phone.TYPE_WORK:
					phone.accumulate("work", number);
					break;
				case Phone.TYPE_HOME:
					phone.accumulate("home", number);
					break;
				case Phone.TYPE_MAIN:
					phone.accumulate("main", number);
					break;
				case Phone.TYPE_FAX_WORK:
					phone.accumulate("fax_work", number);
					break;
				case Phone.TYPE_FAX_HOME:
					phone.accumulate("fax_home", number);
					break;
				case Phone.TYPE_PAGER:
					phone.accumulate("pager", number);
					break;
				case Phone.TYPE_CALLBACK:
					phone.accumulate("callback", number);
					break;
				case Phone.TYPE_CAR:
					phone.accumulate("car", number);
					break;
				case Phone.TYPE_COMPANY_MAIN:
					phone.accumulate("company_main", number);
					break;
				case Phone.TYPE_ISDN:
					phone.accumulate("isdn", number);
					break;
				case Phone.TYPE_OTHER_FAX:
					phone.accumulate("other_fax", number);
					break;
				case Phone.TYPE_RADIO:
					phone.accumulate("radio", number);
					break;
				case Phone.TYPE_TELEX:
					phone.accumulate("telex", number);
					break;
				case Phone.TYPE_TTY_TDD:
					phone.accumulate("tty_tdd", number);
					break;
				case Phone.TYPE_WORK_MOBILE:
					phone.accumulate("work_mobile", number);
					break;
				case Phone.TYPE_WORK_PAGER:
					phone.accumulate("work_pager", number);
					break;
				case Phone.TYPE_ASSISTANT:
					phone.accumulate("assistant", number);
					break;
				case Phone.TYPE_MMS:
					phone.accumulate("mms", number);
					break;
				case Phone.TYPE_OTHER:
					phone.accumulate("other", number);
					break;
				case Phone.TYPE_CUSTOM:
					String label = cursor.getString(cursor.getColumnIndex(Phone.LABEL));
					if (label == null) {
						label = "custom";
					}
					phone.accumulate(label, number);
					break;
				default:
					break;
				}
			} else if (mimetype.equals(Email.CONTENT_ITEM_TYPE)) {//�ʼ�
				JSONObject email = jsonObject.optJSONObject("email");
				if (email == null) {
					email = new JSONObject();
					jsonObject.putOpt("email", email);
				}

				// ȡ���ʼ�����
				int emailType = cursor.getInt(cursor.getColumnIndex(Email.TYPE));

				String address = cursor.getString(cursor.getColumnIndex(Email.DATA));
				switch (emailType) {
				case Email.TYPE_WORK:
					email.accumulate("work", address);
					break;
				case Email.TYPE_HOME:
					email.accumulate("home", address);
					break;
				case Email.TYPE_MOBILE:
					email.accumulate("mobile", address);
					break;
				case Email.TYPE_OTHER:
					email.accumulate("other", address);
					break;
				case Email.TYPE_CUSTOM:
					String label = cursor.getString(cursor.getColumnIndex(Email.LABEL));
					if (label == null) {
						label = "custom";
					}
					email.accumulate(label, address);
					break;
				}

			} else if (mimetype.equals(Photo.CONTENT_ITEM_TYPE)) {//��Ƭ

			} else if (mimetype.equals(Organization.CONTENT_ITEM_TYPE)) {//��֯
				JSONObject company = jsonObject.optJSONObject("company");
				if (company == null) {
					company = new JSONObject();
					jsonObject.putOpt("company", company);
				}
				company.accumulate("company", cursor.getString(cursor.getColumnIndex(Organization.COMPANY)));
				company.accumulate("department", cursor.getString(cursor.getColumnIndex(Organization.DEPARTMENT)));
				company.accumulate("position", cursor.getString(cursor.getColumnIndex(Organization.TITLE)));
				company.accumulate("job_description", cursor.getString(cursor.getColumnIndex(Organization.JOB_DESCRIPTION)));
				company.accumulate("office_location", cursor.getString(cursor.getColumnIndex(Organization.OFFICE_LOCATION)));

			} else if (mimetype.equals(Im.CONTENT_ITEM_TYPE)) {//��ʱͨѶ
				JSONObject im = jsonObject.optJSONObject("im");
				if (im == null) {
					im = new JSONObject();
					jsonObject.putOpt("im", im);
				}
				int protocalType = cursor.getInt(cursor.getColumnIndex(Im.PROTOCOL));

				String data = cursor.getString(cursor.getColumnIndex(Im.DATA));

				switch (protocalType) {
				case Im.PROTOCOL_QQ:
					im.accumulate("qq", data);
					break;
				case Im.PROTOCOL_AIM:
					im.accumulate("aim", data);
					break;
				case Im.PROTOCOL_MSN:
					im.accumulate("msn", data);
					break;
				case Im.PROTOCOL_YAHOO:
					im.accumulate("yahoo", data);
					break;
				case Im.PROTOCOL_SKYPE:
					im.accumulate("skype", data);
					break;
				case Im.PROTOCOL_GOOGLE_TALK:
					im.accumulate("google_talk", data);
					break;
				case Im.PROTOCOL_ICQ:
					im.accumulate("icq", data);
					break;
				case Im.PROTOCOL_JABBER:
					im.accumulate("jabber", data);
					break;
				case Im.PROTOCOL_NETMEETING:
					im.accumulate("netmeeting", data);
					break;
				case Im.PROTOCOL_CUSTOM:
					String label = cursor.getString(cursor.getColumnIndex(Im.LABEL));
					if (label == null) {
						label = cursor.getString(cursor.getColumnIndex("DATA6"));
					}
					if (label == null) {
						label = "custom";
					}
					im.accumulate(label, data);
					break;
				}

			} else if (mimetype.equals(Nickname.CONTENT_ITEM_TYPE)) {//�ƺ�
				JSONObject nickname = jsonObject.optJSONObject("nickname");
				if (nickname == null) {
					nickname = new JSONObject();
					jsonObject.putOpt("nickname", nickname);
				}
				String data = cursor.getString(cursor.getColumnIndex(Nickname.NAME));
				nickname.accumulate("nickname", data);

			} else if (mimetype.equals(Note.CONTENT_ITEM_TYPE)) {//��ע

				JSONObject note = jsonObject.optJSONObject("note");
				if (note == null) {
					note = new JSONObject();
					jsonObject.putOpt("note", note);
				}
				String data = cursor.getString(cursor.getColumnIndex(Note.NOTE));
				note.accumulate("note", data);

			} else if (mimetype.equals(StructuredPostal.CONTENT_ITEM_TYPE)) {//��ַ
				JSONObject address = jsonObject.optJSONObject("address");
				if (address == null) {
					address = new JSONObject();
					jsonObject.putOpt("address", address);
				}
				String data = cursor.getString(cursor.getColumnIndex(StructuredPostal.STREET));
				int postalType = cursor.getInt(cursor.getColumnIndex(StructuredPostal.TYPE));

				switch (postalType) {
				case StructuredPostal.TYPE_WORK:
					address.accumulate("work", data);
					break;
				case StructuredPostal.TYPE_HOME:
					address.accumulate("home", data);
					break;
				case StructuredPostal.TYPE_OTHER:
					address.accumulate("other", data);
					break;
				case StructuredPostal.TYPE_CUSTOM:
					String label = cursor.getString(cursor.getColumnIndex(Im.LABEL));
					if (label == null) {
						label = "custom";
					}
					address.accumulate(label, data);
					break;
				}

			} else if (mimetype.equals(GroupMembership.CONTENT_ITEM_TYPE)) {//Ⱥ���ϵ
				JSONObject group = jsonObject.optJSONObject("group");
				if (group == null) {
					group = new JSONObject();
					jsonObject.putOpt("group", group);
				}
				String data = cursor.getString(cursor.getColumnIndex(GroupMembership.GROUP_ROW_ID));
				if (groupsMap != null) {
					data = groupsMap.get(data);
					group.accumulate("group", data);
				}

			} else if (mimetype.equals(Website.CONTENT_ITEM_TYPE)) {//��վ
				JSONObject url = jsonObject.optJSONObject("url");
				if (url == null) {
					url = new JSONObject();
					jsonObject.putOpt("url", url);
				}
				int webType = cursor.getInt(cursor.getColumnIndex(Website.TYPE));
				String data = cursor.getString(cursor.getColumnIndex(Website.URL));

				switch (webType) {
				case Website.TYPE_HOME:
					url.accumulate("home", data);
					break;
				case Website.TYPE_HOMEPAGE:
					url.accumulate("homepage", data);
					break;
				case Website.TYPE_WORK:
					url.accumulate("work", data);
					break;
				case Website.TYPE_BLOG:
					url.accumulate("blog", data);
					break;
				case Website.TYPE_PROFILE:
					url.accumulate("profile", data);
					break;
				case Website.TYPE_FTP:
					url.accumulate("ftp", data);
					break;
				case Website.TYPE_OTHER:
					//website.accumulate("other", data);
					url.accumulate("other", data);
					break;
				case Website.TYPE_CUSTOM:
					String label = cursor.getString(cursor.getColumnIndex(Website.LABEL));
					if (label == null) {
						label = "custom";
					}
					url.accumulate(label, data);
					break;
				}
			} else if (mimetype.equals(Event.CONTENT_ITEM_TYPE)) {//�¼�
				JSONObject dates = jsonObject.optJSONObject("dates");
				if (dates == null) {
					dates = new JSONObject();
					jsonObject.putOpt("dates", dates);
				}

				int eventType = cursor.getInt(cursor.getColumnIndex(Event.TYPE));
				String data = cursor.getString(cursor.getColumnIndex(Event.START_DATE));

				switch (eventType) {
				case Event.TYPE_BIRTHDAY:
					dates.accumulate("birthday", data);
					break;
				case Event.TYPE_ANNIVERSARY:
					dates.accumulate("anniversary", data);
					break;
				case Event.TYPE_OTHER:
					dates.accumulate("other", data);
					break;
				case Event.TYPE_CUSTOM:
					String label = cursor.getString(cursor.getColumnIndex(Event.LABEL));
					if (label == null) {
						label = "custom";
					}
					dates.accumulate(label, data);
					break;
				}

			} else if (mimetype.equals(Relation.CONTENT_ITEM_TYPE)) {//��ϵ
				JSONObject relation = jsonObject.optJSONObject("relation");
				if (relation == null) {
					relation = new JSONObject();
					jsonObject.putOpt("relation", relation);
				}

				int eventType = cursor.getInt(cursor.getColumnIndex(Relation.TYPE));
				String data = cursor.getString(cursor.getColumnIndex(Relation.NAME));

				switch (eventType) {
				case Relation.TYPE_ASSISTANT:
					relation.accumulate("assistant", data);
					break;
				case Relation.TYPE_BROTHER:
					relation.accumulate("brother", data);
					break;
				case Relation.TYPE_CHILD:
					relation.accumulate("child", data);
					break;
				case Relation.TYPE_DOMESTIC_PARTNER:
					relation.accumulate("domestic_partner", data);
					break;
				case Relation.TYPE_FATHER:
					relation.accumulate("father", data);
					break;
				case Relation.TYPE_FRIEND:
					relation.accumulate("friend", data);
					break;
				case Relation.TYPE_MANAGER:
					relation.accumulate("manager", data);
					break;
				case Relation.TYPE_MOTHER:
					relation.accumulate("mother", data);
					break;
				case Relation.TYPE_PARENT:
					relation.accumulate("parent", data);
					break;
				case Relation.TYPE_PARTNER:
					relation.accumulate("partner", data);
					break;
				case Relation.TYPE_REFERRED_BY:
					relation.accumulate("referred_by", data);
					break;
				case Relation.TYPE_RELATIVE:
					relation.accumulate("relative", data);
					break;
				case Relation.TYPE_SISTER:
					relation.accumulate("sister", data);
					break;
				case Relation.TYPE_SPOUSE:
					relation.accumulate("spouse", data);
					break;
				case Relation.TYPE_CUSTOM:
					String label = cursor.getString(cursor.getColumnIndex(Event.LABEL));
					if (label == null) {
						label = "custom";
					}
					relation.accumulate(label, data);
					break;
				}

			} else if (mimetype.equals(SipAddress.CONTENT_ITEM_TYPE)) {//������ͨ��
				JSONObject sip = jsonObject.optJSONObject("sip");
				if (sip == null) {
					sip = new JSONObject();
					jsonObject.putOpt("sip", sip);
				}
				String data = cursor.getString(cursor.getColumnIndex(SipAddress.SIP_ADDRESS));
				sip.accumulate("sip", data);

			}

			if (1 == 1) {
				continue;
			}

			/**
			 * ���ͨѶ¼����ϵ�˵�����
			 * (ǰ׺,��,�м���,��,��׺,ƴ��[��,�м���,��],��˾,ְλ)
			 */
			if (StructuredName.CONTENT_ITEM_TYPE.equals(mimetype)) {

				//ȫ��
				String display_name = cursor.getString(cursor.getColumnIndex(StructuredName.DISPLAY_NAME));

				//����ǰ׺
				String prefix = cursor.getString(cursor.getColumnIndex(StructuredName.PREFIX));
				jsonObject.putOpt("name_prefix", prefix);

				//����
				String firstName = cursor.getString(cursor.getColumnIndex(StructuredName.FAMILY_NAME));
				jsonObject.putOpt("name_firstName", firstName);

				//�м���
				String middleName = cursor.getString(cursor.getColumnIndex(StructuredName.MIDDLE_NAME));
				jsonObject.putOpt("name_middleName", middleName);

				//����
				String lastname = cursor.getString(cursor.getColumnIndex(StructuredName.GIVEN_NAME));
				jsonObject.putOpt("name_lastname", lastname);

				//���ƺ�׺
				String suffix = cursor.getString(cursor.getColumnIndex(StructuredName.SUFFIX));
				jsonObject.putOpt("name_suffix", suffix);

				/*
				 * String phoneticFirstName = cursor.getString(cursor.getColumnIndex(StructuredName.PHONETIC_FAMILY_NAME));
				 * jsonObject.putOpt("phoneticFirstName", phoneticFirstName);
				 * String phoneticMiddleName = cursor.getString(cursor.getColumnIndex(StructuredName.PHONETIC_MIDDLE_NAME));
				 * jsonObject.putOpt("phoneticMiddleName", phoneticMiddleName);
				 * String phoneticLastName = cursor.getString(cursor.getColumnIndex(StructuredName.PHONETIC_GIVEN_NAME));
				 * jsonObject.putOpt("phoneticLastName", phoneticLastName);
				 */
			}

			/**
			 * ��ȡ��֯��Ϣ
			 * (��˾,����,ְλ)
			 */
			if (Organization.CONTENT_ITEM_TYPE.equals(mimetype)) {
				// ȡ����֯����
				int orgType = cursor.getInt(cursor.getColumnIndex(Organization.TYPE));

				if (orgType == Organization.TYPE_CUSTOM) {
					//     if (orgType == Organization.TYPE_WORK) {

					//��˾
					String company = cursor.getString(cursor.getColumnIndex(Organization.COMPANY));
					jsonObject.putOpt("company_company", company);

					//����
					String department = cursor.getString(cursor.getColumnIndex(Organization.DEPARTMENT));
					jsonObject.putOpt("company_department", department);

					//ְλ
					String jobTitle = cursor.getString(cursor.getColumnIndex(Organization.TITLE));
					jsonObject.putOpt("company_jobTitle", jobTitle);
				}
			}

			/**
			 * ��ȡ�绰��Ϣ
			 * (�ֻ�,��λ,סլ,�ܻ�,��λ����,סլ����,Ѱ����,����,�Զ���)
			 */
			if (Phone.CONTENT_ITEM_TYPE.equals(mimetype)) {
				// ȡ���绰����
				int phoneType = cursor.getInt(cursor.getColumnIndex(Phone.TYPE));

				// �ֻ�
				if (phoneType == Phone.TYPE_MOBILE) {
					String mobile = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
					jsonObject.putOpt("number_mobile", mobile);
				}

				// ��λ�绰
				if (phoneType == Phone.TYPE_WORK) {
					String jobNum = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
					jsonObject.putOpt("number_jobNum", jobNum);
				}

				// סլ�绰
				if (phoneType == Phone.TYPE_HOME) {
					String homeNum = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
					jsonObject.putOpt("number_homeNum", homeNum);
				}

				// �ܻ�
				if (phoneType == Phone.TYPE_MAIN) {
					String jobTel = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
					jsonObject.putOpt("number_mainTel", jobTel);
				}

				/*
				 * // ��˾�ܻ�
				 * if (phoneType == Phone.TYPE_COMPANY_MAIN) {
				 * String jobTel = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
				 * jsonObject.putOpt("number_jobTel", jobTel);
				 * }
				 */

				// ��λ����
				if (phoneType == Phone.TYPE_FAX_WORK) {
					String workFax = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
					jsonObject.putOpt("number_workFax", workFax);
				}
				// סլ����
				if (phoneType == Phone.TYPE_FAX_HOME) {
					String homeFax = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
					jsonObject.putOpt("number_homeFax", homeFax);
				}
				// Ѱ����
				if (phoneType == Phone.TYPE_PAGER) {
					String pager = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
					jsonObject.putOpt("number_pager", pager);
				}

				// ����
				if (phoneType == Phone.TYPE_OTHER) {
					String pager = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
					jsonObject.putOpt("number_other", pager);
				}

				// �Զ���
				if (phoneType == Phone.TYPE_CUSTOM) {
					String pager = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
					jsonObject.putOpt("number_custom", pager);
				}

				/*
				 * // 回拨号码
				 * if (phoneType == Phone.TYPE_CALLBACK) {
				 * String quickNum = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
				 * jsonObject.putOpt("quickNum", quickNum);
				 * }
				 * 
				 * // 车载电话
				 * if (phoneType == Phone.TYPE_CAR) {
				 * String carNum = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
				 * jsonObject.putOpt("carNum", carNum);
				 * }
				 * // ISDN
				 * if (phoneType == Phone.TYPE_ISDN) {
				 * String isdn = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
				 * jsonObject.putOpt("isdn", isdn);
				 * }
				 * 
				 * // 无线装置
				 * if (phoneType == Phone.TYPE_RADIO) {
				 * String wirelessDev = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
				 * jsonObject.putOpt("wirelessDev", wirelessDev);
				 * }
				 * // 电报
				 * if (phoneType == Phone.TYPE_TELEX) {
				 * String telegram = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
				 * jsonObject.putOpt("telegram", telegram);
				 * }
				 * // TTY_TDD
				 * if (phoneType == Phone.TYPE_TTY_TDD) {
				 * String tty_tdd = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
				 * jsonObject.putOpt("tty_tdd", tty_tdd);
				 * }
				 * // 单位手机
				 * if (phoneType == Phone.TYPE_WORK_MOBILE) {
				 * String jobMobile = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
				 * jsonObject.putOpt("jobMobile", jobMobile);
				 * }
				 * // 单位寻呼�?
				 * if (phoneType == Phone.TYPE_WORK_PAGER) {
				 * String jobPager = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
				 * jsonObject.putOpt("jobPager", jobPager);
				 * }
				 * // 助理
				 * if (phoneType == Phone.TYPE_ASSISTANT) {
				 * String assistantNum = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
				 * jsonObject.putOpt("assistantNum", assistantNum);
				 * }
				 * // 彩信
				 * if (phoneType == Phone.TYPE_MMS) {
				 * String mms = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
				 * jsonObject.putOpt("mms", mms);
				 * }
				 */
			}

			/**
			 * ����email��ַ
			 * (����,����,����,�ֻ�,�Զ���)
			 */
			if (Email.CONTENT_ITEM_TYPE.equals(mimetype)) {
				// ȡ���ʼ�����
				int emailType = cursor.getInt(cursor.getColumnIndex(Email.TYPE));

				// ��λ�ʼ���ַ
				if (emailType == Email.TYPE_WORK) {
					String jobEmail = cursor.getString(cursor.getColumnIndex(Email.DATA));
					jsonObject.putOpt("email_jobEmail", jobEmail);
				}

				// �����ʼ���ַ
				if (emailType == Email.TYPE_HOME) {
					String homeEmail = cursor.getString(cursor.getColumnIndex(Email.DATA));
					jsonObject.putOpt("email_homeEmail", homeEmail);
				}

				// �ֻ��ʼ���ַ
				if (emailType == Email.TYPE_MOBILE) {
					String mobileEmail = cursor.getString(cursor.getColumnIndex(Email.DATA));
					jsonObject.putOpt("email_mobileEmail", mobileEmail);
				}

				// �����ʼ���ַ
				if (emailType == Email.TYPE_OTHER) {
					String mobileEmail = cursor.getString(cursor.getColumnIndex(Email.DATA));
					jsonObject.putOpt("email_otherEmail", mobileEmail);
				}

				// �Զ����ʼ���ַ
				if (emailType == Email.TYPE_CUSTOM) {
					String mobileEmail = cursor.getString(cursor.getColumnIndex(Email.DATA));
					jsonObject.putOpt("email_customEmail", mobileEmail);
				}

			}

			/**
			 *  ��ʱ��Ϣ
			 *  (QQ,AIM,Windows Live,�Ż�,Skype,����,ICQ,Jabber)
			 */
			if (Im.CONTENT_ITEM_TYPE.equals(mimetype)) {
				// ȡ����ʱ��Ϣ����
				int protocal = cursor.getInt(cursor.getColumnIndex(Im.PROTOCOL));

				//QQ
				if (protocal == Im.PROTOCOL_QQ) {
					String instantsMsg = cursor.getString(cursor.getColumnIndex(Im.DATA));
					jsonObject.putOpt("PROTOCOL_QQ", instantsMsg);
				}

				//AIM
				if (protocal == Im.PROTOCOL_AIM) {
					String workMsg = cursor.getString(cursor.getColumnIndex(Im.DATA));
					jsonObject.putOpt("PROTOCOL_AIM", workMsg);
				}

				//MSN
				if (protocal == Im.PROTOCOL_MSN) {
					String msg = cursor.getString(cursor.getColumnIndex(Im.DATA));
					jsonObject.putOpt("PROTOCOL_MSN", msg);
				}

				//YAHOO
				if (protocal == Im.PROTOCOL_YAHOO) {
					String msg = cursor.getString(cursor.getColumnIndex(Im.DATA));
					jsonObject.putOpt("PROTOCOL_YAHOO", msg);
				}

				//SKYPE
				if (protocal == Im.PROTOCOL_SKYPE) {
					String msg = cursor.getString(cursor.getColumnIndex(Im.DATA));
					jsonObject.putOpt("PROTOCOL_SKYPE", msg);
				}

				//����
				if (protocal == Im.PROTOCOL_GOOGLE_TALK) {
					String msg = cursor.getString(cursor.getColumnIndex(Im.DATA));
					jsonObject.putOpt("PROTOCOL_GOOGLE_TALK", msg);
				}

				//ICQ
				if (protocal == Im.PROTOCOL_ICQ) {
					String msg = cursor.getString(cursor.getColumnIndex(Im.DATA));
					jsonObject.putOpt("PROTOCOL_ICQ", msg);
				}

				//JABBER
				if (protocal == Im.PROTOCOL_JABBER) {
					String msg = cursor.getString(cursor.getColumnIndex(Im.DATA));
					jsonObject.putOpt("PROTOCOL_JABBER", msg);
				}

				//�Զ���
				if (protocal == Im.TYPE_CUSTOM) {
					String msg = cursor.getString(cursor.getColumnIndex(Im.DATA));
					jsonObject.putOpt("PROTOCOL_CUSTOM", msg);
				}
			}

			/**
			 * ���ҵ�ַ
			 * (��λ,סլ,����,�Զ���)
			 */
			if (StructuredPostal.CONTENT_ITEM_TYPE.equals(mimetype)) {
				// ȡ����ַ����
				int postalType = cursor.getInt(cursor.getColumnIndex(StructuredPostal.TYPE));

				// ��λͨѶ��ַ
				if (postalType == StructuredPostal.TYPE_WORK) {
					String address = cursor.getString(cursor.getColumnIndex(StructuredPostal.FORMATTED_ADDRESS));
					jsonObject.putOpt("address", address);

					String street = cursor.getString(cursor.getColumnIndex(StructuredPostal.STREET));
					jsonObject.putOpt("street", street);
					String ciry = cursor.getString(cursor.getColumnIndex(StructuredPostal.CITY));
					jsonObject.putOpt("ciry", ciry);
					String box = cursor.getString(cursor.getColumnIndex(StructuredPostal.POBOX));
					jsonObject.putOpt("box", box);
					String area = cursor.getString(cursor.getColumnIndex(StructuredPostal.NEIGHBORHOOD));
					jsonObject.putOpt("area", area);
					String state = cursor.getString(cursor.getColumnIndex(StructuredPostal.REGION));
					jsonObject.putOpt("state", state);
					String zip = cursor.getString(cursor.getColumnIndex(StructuredPostal.POSTCODE));
					jsonObject.putOpt("zip", zip);
					String country = cursor.getString(cursor.getColumnIndex(StructuredPostal.COUNTRY));
					jsonObject.putOpt("country", country);
				}
				// סլͨѶ��ַ
				if (postalType == StructuredPostal.TYPE_HOME) {
					String address = cursor.getString(cursor.getColumnIndex(StructuredPostal.FORMATTED_ADDRESS));
					jsonObject.putOpt("address", address);
					String homeStreet = cursor.getString(cursor.getColumnIndex(StructuredPostal.STREET));
					jsonObject.putOpt("homeStreet", homeStreet);
					String homeCity = cursor.getString(cursor.getColumnIndex(StructuredPostal.CITY));
					jsonObject.putOpt("homeCity", homeCity);
					String homeBox = cursor.getString(cursor.getColumnIndex(StructuredPostal.POBOX));
					jsonObject.putOpt("homeBox", homeBox);
					String homeArea = cursor.getString(cursor.getColumnIndex(StructuredPostal.NEIGHBORHOOD));
					jsonObject.putOpt("homeArea", homeArea);
					String homeState = cursor.getString(cursor.getColumnIndex(StructuredPostal.REGION));
					jsonObject.putOpt("homeState", homeState);
					String homeZip = cursor.getString(cursor.getColumnIndex(StructuredPostal.POSTCODE));
					jsonObject.putOpt("homeZip", homeZip);
					String homeCountry = cursor.getString(cursor.getColumnIndex(StructuredPostal.COUNTRY));
					jsonObject.putOpt("homeCountry", homeCountry);
				}
				// ����ͨѶ��ַ
				if (postalType == StructuredPostal.TYPE_OTHER) {
					String address = cursor.getString(cursor.getColumnIndex(StructuredPostal.FORMATTED_ADDRESS));
					jsonObject.putOpt("address", address);
					String otherStreet = cursor.getString(cursor.getColumnIndex(StructuredPostal.STREET));
					jsonObject.putOpt("otherStreet", otherStreet);
					String otherCity = cursor.getString(cursor.getColumnIndex(StructuredPostal.CITY));
					jsonObject.putOpt("otherCity", otherCity);
					String otherBox = cursor.getString(cursor.getColumnIndex(StructuredPostal.POBOX));
					jsonObject.putOpt("otherBox", otherBox);
					String otherArea = cursor.getString(cursor.getColumnIndex(StructuredPostal.NEIGHBORHOOD));
					jsonObject.putOpt("otherArea", otherArea);
					String otherState = cursor.getString(cursor.getColumnIndex(StructuredPostal.REGION));
					jsonObject.putOpt("otherState", otherState);
					String otherZip = cursor.getString(cursor.getColumnIndex(StructuredPostal.POSTCODE));
					jsonObject.putOpt("otherZip", otherZip);
					String otherCountry = cursor.getString(cursor.getColumnIndex(StructuredPostal.COUNTRY));
					jsonObject.putOpt("otherCountry", otherCountry);
				}
				// �Զ���ͨѶ��ַ
				if (postalType == StructuredPostal.TYPE_CUSTOM) {
					String address = cursor.getString(cursor.getColumnIndex(StructuredPostal.FORMATTED_ADDRESS));
					jsonObject.putOpt("address", address);
					String otherStreet = cursor.getString(cursor.getColumnIndex(StructuredPostal.STREET));
					jsonObject.putOpt("customStreet", otherStreet);
					String otherCity = cursor.getString(cursor.getColumnIndex(StructuredPostal.CITY));
					jsonObject.putOpt("otherCity", otherCity);
					String otherBox = cursor.getString(cursor.getColumnIndex(StructuredPostal.POBOX));
					jsonObject.putOpt("otherBox", otherBox);
					String otherArea = cursor.getString(cursor.getColumnIndex(StructuredPostal.NEIGHBORHOOD));
					jsonObject.putOpt("otherArea", otherArea);
					String otherState = cursor.getString(cursor.getColumnIndex(StructuredPostal.REGION));
					jsonObject.putOpt("otherState", otherState);
					String otherZip = cursor.getString(cursor.getColumnIndex(StructuredPostal.POSTCODE));
					jsonObject.putOpt("otherZip", otherZip);
					String otherCountry = cursor.getString(cursor.getColumnIndex(StructuredPostal.COUNTRY));
					jsonObject.putOpt("otherCountry", otherCountry);
				}

				/**
				 * ��ȡ�ǳ���Ϣ
				 * (�ƺ�)
				 */
				if (Nickname.CONTENT_ITEM_TYPE.equals(mimetype)) {
					String nickName = cursor.getString(cursor.getColumnIndex(Nickname.NAME));
					jsonObject.putOpt("nickName", nickName);
				}

				/**
				 *��ȡ��վ��Ϣ
				 *(��վ)
				 */
				if (Website.CONTENT_ITEM_TYPE.equals(mimetype)) {
					// ȡ����֯����
					int webType = cursor.getInt(cursor.getColumnIndex(Website.TYPE));

					// ��ҳ
					if (webType == Website.TYPE_CUSTOM) {
						String home = cursor.getString(cursor.getColumnIndex(Website.URL));
						jsonObject.putOpt("web_custom", home);
					}
					// ��ҳ
					if (webType == Website.TYPE_HOME) {
						String home = cursor.getString(cursor.getColumnIndex(Website.URL));
						jsonObject.putOpt("web_home", home);
					}

					// ������ҳ
					if (webType == Website.TYPE_HOMEPAGE) {
						String homePage = cursor.getString(cursor.getColumnIndex(Website.URL));
						jsonObject.putOpt("web_homePage", homePage);
					}
					// ������ҳ
					if (webType == Website.TYPE_WORK) {
						String workPage = cursor.getString(cursor.getColumnIndex(Website.URL));
						jsonObject.putOpt("web_workPage", workPage);
					}
				}

				/**
				 * ������ͨ��
				 */

				/**
				 * ����event(����,���������)
				 * (����,���������,����)
				 */
				if (Event.CONTENT_ITEM_TYPE.equals(mimetype)) {
					// ȡ��ʱ������
					int eventType = cursor.getInt(cursor.getColumnIndex(Event.TYPE));
					// ����
					if (eventType == Event.TYPE_BIRTHDAY) {
						String birthday = cursor.getString(cursor.getColumnIndex(Event.START_DATE));
						jsonObject.putOpt("birthday", birthday);
					}
					// ���������
					if (eventType == Event.TYPE_ANNIVERSARY) {
						String anniversary = cursor.getString(cursor.getColumnIndex(Event.START_DATE));
						jsonObject.putOpt("anniversary", anniversary);
					}
					// ����
					if (eventType == Event.TYPE_OTHER) {
						String anniversary = cursor.getString(cursor.getColumnIndex(Event.START_DATE));
						jsonObject.putOpt("event_other", anniversary);
					}

					// �Զ���
					if (eventType == Event.TYPE_CUSTOM) {
						String anniversary = cursor.getString(cursor.getColumnIndex(Event.START_DATE));
						jsonObject.putOpt("event_custom", anniversary);
					}

				}

				/**
				 * ��ȡ��ע��Ϣ
				 * (��ע)
				 */
				if (Note.CONTENT_ITEM_TYPE.equals(mimetype)) {
					String remark = cursor.getString(cursor.getColumnIndex(Note.NOTE));
					jsonObject.putOpt("remark", remark);
				}

				/**
				 * Ⱥ��
				 */
				if (GroupMembership.CONTENT_ITEM_TYPE.equals(mimetype)) {
					String remark = cursor.getString(cursor.getColumnIndex(GroupMembership.GROUP_ROW_ID));
					jsonObject.putOpt("GroupMembership", remark);
				}
			}
		}
		cursor.close();
		//Log.i("contactData", contactData.toString());
		CLogUtils.write(contactData.toString());
		return contactData.toString();
	}
}