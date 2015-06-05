package com.examples.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.GridView;

public class GridView4ScrollView extends GridView {
	public GridView4ScrollView(Context context) {
		super(context);
	}

	public GridView4ScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GridView4ScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	/**
	 * 重写该方法，达到使ListView适应ScrollView的效果
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(
			Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
	
	
	
	
	
	
	
	
	
	
	
}
