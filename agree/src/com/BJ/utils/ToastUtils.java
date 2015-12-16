package com.BJ.utils;

import com.biju.R;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtils {
	
//	View toastRoot = getLayoutInflater().inflate(R.layout.my_prompt_toast, null);设置时需要的控件
	public static void ShowMsg(Context context, String msg, int Height,
			View toastRoot) {
		Toast toast = new Toast(context);
		toast.setGravity(Gravity.TOP, 0, Height);
		toast.setView(toastRoot);
		toast.setDuration(100);
		TextView tv = (TextView) toastRoot.findViewById(R.id.TextViewInfo);
		tv.setText(msg);
		toast.show();
	}
	
	public static void ShowMsgCENTER(Context context, String msg, int Height,
			View toastRoot,int duration) {
		Toast toast = new Toast(context);
		toast.setGravity(Gravity.CENTER, 0, Height);
		toast.setView(toastRoot);
		toast.setDuration(duration);
		TextView tv = (TextView) toastRoot.findViewById(R.id.TextViewInfo);
		tv.setText(msg);
		toast.show();
	}
}
