package com.BJ.utils;

import com.biju.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FooterView extends LinearLayout {
	private Context mContext;

	public static final int HIDE = 0;
	public static final int MORE = 1;
	public static final int LOADING = 2;
	public static final int BADNETWORK = 3;

	private TextView textView;

	private OnClickListener ml;

	public FooterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public FooterView(Context context) {
		super(context);
		mContext = context;
		init();
	}

	private void init() {
		LayoutInflater.from(mContext).inflate(R.layout.home_gridview_foot,
				this, true);
		textView = (TextView) findViewById(R.id.home_requestcode);
		textView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ml != null) {
					ml.onClick(v);
				}

			}
		});
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		ml = l;
		super.setOnClickListener(ml);
	}

}
