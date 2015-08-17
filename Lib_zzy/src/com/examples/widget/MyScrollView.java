package com.examples.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {

	public interface OnScrollListener{
		void onScroll(int scrollHeight);
	}

	private OnScrollListener listener;
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		Log.e("MyScrollView", "t: " + t);
		if (listener != null){
			listener.onScroll(t);
		}
	}
	
	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setOnScrollListener(OnScrollListener listener) {
		this.listener = listener;
	}
	
	
	
	
	
	
	
	
	
	
	

}
