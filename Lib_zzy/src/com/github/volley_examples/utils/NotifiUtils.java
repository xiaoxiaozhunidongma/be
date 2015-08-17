package com.github.volley_examples.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lib_zzy.R;

public final class NotifiUtils {
//
	private static Toast toast;
	public  static void showToast(Context context, String text) {
//		Toast toast = Toast.makeText(this, "toast", Toast.LENGTH_LONG);
		// 如果之前有任务，就取消
		if (toast != null){
			toast.cancel();
		}
		toast = new Toast(context);
		View view = LayoutInflater.from(context).inflate(R.layout.custom_toast, null);
		TextView tvText = (TextView) view.findViewById(R.id.textView1);
		tvText.setText(text);
		// toast.setBackground();
		toast.setView(view);
		toast.setGravity(Gravity.TOP, 0, 150);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.show();
	}
//	
//	public static NiftyDialogBuilder showDialog(Context context,Effectstype effectstype,String Title,
//			String Message,int drawableId,String withButton1Text,String withButton2Text){
//
//		final NiftyDialogBuilder dialogBuilder=NiftyDialogBuilder.getInstance(context);
//		new NiftyDialogBuilder(context, R.style.MyDialog);
//
//		dialogBuilder
//
//		.withTitle(Title)
//
//	.withTitleColor("#FFFFFF")
//
//	 .withDividerColor("#11000000")
//
//		   .withMessage(Message)
//
//		    .withMessageColor("#FFFFFF")
//
//		   .withIcon(context.getResources().getDrawable(drawableId))
//
//		    .withEffect(effectstype)
//
//		   .withButton1Text("确定")
//
//		   .withButton2Text("取消")
//		   .isCancelableOnTouchOutside(false)
//		   .withDuration(1000);
////		   .setCustomView(R.layout.custom_dialog	,this)
////		   .show();
//		return dialogBuilder;
//
//	}
//	
//	private NotifiUtils(){}
}
