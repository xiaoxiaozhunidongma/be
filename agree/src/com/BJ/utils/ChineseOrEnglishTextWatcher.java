package com.BJ.utils;

import com.biju.function.NicknameActivity;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class ChineseOrEnglishTextWatcher implements TextWatcher {
	/** Ҫ���������������Ƶ�EditText */
	private EditText mFilter;
	/** ���Ƶ��ַ����� */
	private int length;
	/** �ո���ƣ���ֹ��Щ�ֻ��ڽ��б༭��Ϣʱ ��λ����ǰ�� */
	private boolean onceLoad = true;

	public ChineseOrEnglishTextWatcher(EditText mFilter, int length) {
		super();
		this.mFilter = mFilter;
		this.length = length;
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		int countSize = 0;
		Editable editable = mFilter.getText();
		String str = editable.toString();
		char[] array = str.toCharArray();
		int i = 0;
		if (onceLoad) {
			mFilter.setSelection(s.length());
			onceLoad = false;
		}
		for (i = 0; i < array.length; i++) {
			if ((char) (byte) array[i] != array[i])/** �ж��Ƿ�Ϊ���� */
			{
				countSize += 2;
				/** ���Ϊ���Ļ����������������ռ�����ֽ� */
			} else {
				countSize += 1;
				/** Ӣ����ռһ���ֽ� */
			}
			if (countSize >= length+1) {
				Log.e("ChineseOrEnglishTextWatcher", "���뵽����������===========");
				/** �ﵽ���ֵ */
				int selEndIndex = Selection.getSelectionEnd(editable);
				str = editable.toString();
				String newStr = str.substring(0, i);
				mFilter.setText(newStr);
				editable = mFilter.getText();
				int newLen = editable.length();
				if (selEndIndex > newLen) {
					selEndIndex = editable.length();
				}
				Selection.setSelection(editable, selEndIndex);
				NicknameActivity.mNickname_warn_layout.setVisibility(View.VISIBLE);
				break;
			}else
			{
				NicknameActivity.mNickname_warn_layout.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}
}
