package com.biju.function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.biju.R;
import com.biju.search.util.CharacterParser;
import com.biju.search.util.ContactsSortAdapter;
import com.biju.search.util.ContactsSortAdapter.ViewHolder;
import com.biju.search.util.PinyinComparator;
import com.biju.search.util.SortModel;
import com.biju.search.util.SortToken;


public class ContactsActivity extends Activity {

	ListView mListView;
	EditText etSearch;
	ImageView ivClearText;
	private ArrayList<SortModel> fileterList;

//	private SideBar sideBar;
//	private TextView dialog;

	private List<SortModel> mAllContactsList;
	private ContactsSortAdapter adapter;
	/**
	 * ����ת����ƴ������
	 */
	private CharacterParser characterParser;

	/**
	 * ����ƴ��������ListView�����������
	 */
	private PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_contacts);
		init();
	}

	private void init() {
		initView();
		initListener();
		loadContacts();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return super.onTouchEvent(event);
	}

	private void initListener() {

		/**��������ַ�**/
		ivClearText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				etSearch.setText("");
			}
		});
		etSearch.addTextChangedListener(new TextWatcher() {


			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				String s=arg0.toString();
				if("".equals(s))
				{
					mListView.setVisibility(View.GONE);
//					lv_contacts_layout.setVisibility(View.GONE);
				}else
				{
//					lv_contacts_layout.setVisibility(View.VISIBLE);
					mListView.setVisibility(View.VISIBLE);
					mListView.setAdapter(adapter);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable e) {

				String content = etSearch.getText().toString();
				if ("".equals(content)) {
					ivClearText.setVisibility(View.INVISIBLE);
				} else {
					ivClearText.setVisibility(View.VISIBLE);
				}
				if (content.length() > 0) {
					fileterList = (ArrayList<SortModel>) search(content);
					adapter.updateListView(fileterList);
					//mAdapter.updateData(mContacts);
				} else {
					adapter.updateListView(mAllContactsList);
				}
				mListView.setSelection(0);

			}

		});

		//�����Ҳ�[A-Z]���ٵ�������������
//		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
//
//			@Override
//			public void onTouchingLetterChanged(String s) {
//				//����ĸ�״γ��ֵ�λ��
//				int position = adapter.getPositionForSection(s.charAt(0));
//				if (position != -1) {
//					mListView.setSelection(position);
//				}
//			}
//		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
				ViewHolder viewHolder = (ViewHolder) view.getTag();
				viewHolder.cbChecked.performClick();
				adapter.toggleChecked(position);
				String name=fileterList.get(position).name;
				String phone=fileterList.get(position).number;
				Toast.makeText(ContactsActivity.this, "         "+name+"       "+phone+"          ", Toast.LENGTH_SHORT).show();
			}
		});

	}

	private void initView() {
//		sideBar = (SideBar) findViewById(R.id.sidrbar);
//		dialog = (TextView) findViewById(R.id.dialog);
//		sideBar.setTextView(dialog);
		ivClearText = (ImageView) findViewById(R.id.ivClearText);
		etSearch = (EditText) findViewById(R.id.et_search);
		mListView = (ListView) findViewById(R.id.lv_contacts);
		lv_contacts_layout = (FrameLayout) findViewById(R.id.lv_contacts_layout);

		/** ��ListView����adapter **/
		characterParser = CharacterParser.getInstance();
		mAllContactsList = new ArrayList<SortModel>();
		pinyinComparator = new PinyinComparator();
		Collections.sort(mAllContactsList, pinyinComparator);// ����a-z��������Դ����
		adapter = new ContactsSortAdapter(this, mAllContactsList);
	}

	private void loadContacts() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ContentResolver resolver = getApplicationContext().getContentResolver();
					Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, new String[] { Phone.DISPLAY_NAME, Phone.NUMBER, "sort_key" }, null, null, "sort_key COLLATE LOCALIZED ASC");
					if (phoneCursor == null || phoneCursor.getCount() == 0) {
						Toast.makeText(getApplicationContext(), "δ��ö�ȡ��ϵ��Ȩ�� �� δ�����ϵ������", Toast.LENGTH_SHORT).show();
						return;
					}
					int PHONES_NUMBER_INDEX = phoneCursor.getColumnIndex(Phone.NUMBER);
					int PHONES_DISPLAY_NAME_INDEX = phoneCursor.getColumnIndex(Phone.DISPLAY_NAME);
					int SORT_KEY_INDEX = phoneCursor.getColumnIndex("sort_key");
					if (phoneCursor.getCount() > 0) {
						mAllContactsList = new ArrayList<SortModel>();
						while (phoneCursor.moveToNext()) {
							String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
							if (TextUtils.isEmpty(phoneNumber))
								continue;
							String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
							String sortKey = phoneCursor.getString(SORT_KEY_INDEX);
							//System.out.println(sortKey);
							SortModel sortModel = new SortModel(contactName, phoneNumber, sortKey);
							//����ʹ��ϵͳsortkeyȡ,ȡ������ʹ�ù���ȡ
							String sortLetters = getSortLetterBySortKey(sortKey);
							if (sortLetters == null) {
								sortLetters = getSortLetter(contactName);
							}
							sortModel.sortLetters = sortLetters;
							sortModel.sortToken = parseSortKey(sortKey);
							mAllContactsList.add(sortModel);
						}
					}
					phoneCursor.close();
					runOnUiThread(new Runnable() {
						public void run() {
							Collections.sort(mAllContactsList, pinyinComparator);
							adapter.updateListView(mAllContactsList);
						}
					});
				} catch (Exception e) {
					Log.e("xbc", e.getLocalizedMessage());
				}
			}
		}).start();
	}

	/**
	 * ����תƴ��,ȡ����ĸ
	 * @param name
	 * @return
	 */
	private String getSortLetter(String name) {
		String letter = "#";
		if (name == null) {
			return letter;
		}
		//����ת����ƴ��
		String pinyin = characterParser.getSelling(name);
		String sortString = pinyin.substring(0, 1).toUpperCase(Locale.CHINESE);

		// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
		if (sortString.matches("[A-Z]")) {
			letter = sortString.toUpperCase(Locale.CHINESE);
		}
		return letter;
	}

	/**
	 * ȡsort_key������ĸ
	 * @param sortKey
	 * @return
	 */
	private String getSortLetterBySortKey(String sortKey) {
		if (sortKey == null || "".equals(sortKey.trim())) {
			return null;
		}
		String letter = "#";
		//����ת����ƴ��
		String sortString = sortKey.trim().substring(0, 1).toUpperCase(Locale.CHINESE);
		// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
		if (sortString.matches("[A-Z]")) {
			letter = sortString.toUpperCase(Locale.CHINESE);
		}
		return letter;
	}

	/**
	 * ģ����ѯ
	 * @param str
	 * @return
	 */
	private List<SortModel> search(String str) {
		List<SortModel> filterList = new ArrayList<SortModel>();// ���˺��list
		//if (str.matches("^([0-9]|[/+])*$")) {// ������ʽ ƥ�����
		if (str.matches("^([0-9]|[/+]).*")) {// ������ʽ ƥ�������ֻ��߼Ӻſ�ͷ���ַ���(�����˴��ո�-�ָ�ĺ���)
			String simpleStr = str.replaceAll("\\-|\\s", "");
			for (SortModel contact : mAllContactsList) {
				if (contact.number != null && contact.name != null) {
					if (contact.simpleNumber.contains(simpleStr) || contact.name.contains(str)) {
						if (!filterList.contains(contact)) {
							filterList.add(contact);
						}
					}
				}
			}
		}else {
			for (SortModel contact : mAllContactsList) {
				if (contact.number != null && contact.name != null) {
					//����ȫƥ��,��������ĸ��ƴƥ��,����ȫ��ĸƥ��
					if (contact.name.toLowerCase(Locale.CHINESE).contains(str.toLowerCase(Locale.CHINESE))
							|| contact.sortKey.toLowerCase(Locale.CHINESE).replace(" ", "").contains(str.toLowerCase(Locale.CHINESE))
							|| contact.sortToken.simpleSpell.toLowerCase(Locale.CHINESE).contains(str.toLowerCase(Locale.CHINESE))
							|| contact.sortToken.wholeSpell.toLowerCase(Locale.CHINESE).contains(str.toLowerCase(Locale.CHINESE))) {
						if (!filterList.contains(contact)) {
							filterList.add(contact);
						}
					}
				}
			}
		}
		return filterList;
	}

	String chReg = "[\\u4E00-\\u9FA5]+";//�����ַ���ƥ��
	private FrameLayout lv_contacts_layout;

	//String chReg="[^\\u4E00-\\u9FA5]";//����������ַ�ƥ��
	/**
	 * ����sort_key,��װ��ƴ,ȫƴ
	 * @param sortKey
	 * @return
	 */
	public SortToken parseSortKey(String sortKey) {
		SortToken token = new SortToken();
		if (sortKey != null && sortKey.length() > 0) {
			//���а����������ַ�
			String[] enStrs = sortKey.replace(" ", "").split(chReg);
			for (int i = 0, length = enStrs.length; i < length; i++) {
				if (enStrs[i].length() > 0) {
					//ƴ�Ӽ�ƴ
					token.simpleSpell += enStrs[i].charAt(0);
					token.wholeSpell += enStrs[i];
				}
			}
		}
		return token;
	}

}