package com.BJ.photo;


import com.biju.function.GroupActivity;
import com.fragment.PhotoFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;


/**
 * �������Ҫ������������ʾ����ͼƬ���ļ���
 *
 * @author king
 * @QQ:595163260
 * @version 2014��10��18��  ����11:48:06
 */
public class ImageFile extends Activity {

	private FolderAdapter folderAdapter;
	private Button bt_cancel;
	private Context mContext;

	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(Res.getLayoutID("plugin_camera_image_file"));
		PublicWay.activityList.add(this);
		mContext = this;
		bt_cancel = (Button) findViewById(Res.getWidgetID("cancel"));
		bt_cancel.setOnClickListener(new CancelListener());
		GridView gridView = (GridView) findViewById(Res.getWidgetID("fileGridView"));
		TextView textView = (TextView) findViewById(Res.getWidgetID("headerTitle"));
		textView.setText(Res.getString("photo"));
		folderAdapter = new FolderAdapter(this);
		gridView.setAdapter(folderAdapter);
	}

	private class CancelListener implements OnClickListener {// ȡ����ť�ļ���
		public void onClick(View v) {
			SharedPreferences sp=getSharedPreferences("isPhoto", 0);
			Editor editor=sp.edit();
			editor.putBoolean("Photo", true);
			editor.commit();
			//���ѡ���ͼƬ
			Bimp.tempSelectBitmap.clear();
			finish();
			Intent intent = new Intent();
			intent.setClass(mContext,GroupActivity.class);
			startActivity(intent);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(mContext, PhotoFragment.class);
			startActivity(intent);
		}
		
		return true;
	}

}
