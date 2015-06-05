package com.github.volley_examples.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

public final class TextViewUtils {
	
	
	public static void setTextDrawable(Context context, int drawableRes, TextView tvName) {
		Drawable drawableTop = context.getResources()
			.getDrawable(drawableRes);
		//必须设置图片大小，否则不显示
		drawableTop.setBounds(0, 0, 
			drawableTop.getMinimumWidth(), 
			drawableTop.getMinimumHeight());
		tvName.setCompoundDrawables(null, drawableTop, null, null);
	}
}

