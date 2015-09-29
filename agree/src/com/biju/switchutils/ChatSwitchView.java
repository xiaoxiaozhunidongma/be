package com.biju.switchutils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.biju.R;
import com.biju.function.TeamSetting2Activity;

public class ChatSwitchView extends FrameLayout {
	protected boolean isChecked;
	protected View onBgView;
	protected View offBgView;
	protected View circleView;
	protected boolean autoForPerformClick = true;
	protected OnCheckedChangedListener onCheckedChangedListener;
	public static OnCheckedChangedListener changedListener;

	public ChatSwitchView(Context context) {
		super(context);
		initialize();
	}

	@SuppressWarnings("static-access")
	private void on() {
		OnCheckedChangedListener changedListener = new OnCheckedChangedListener() {

			@Override
			public void onChanged(View view, boolean checked) {
				isChecked = checked;
				Log.e("ChatSwitchView", "这时候的选择情况333333========" + isChecked);
				float targetX = 0;
				if (getWidth() != 0) {
					targetX = getWidth() - circleView.getWidth();
				} else {
					measure(0, 0);
					targetX = getMeasuredWidth()- circleView.getMeasuredWidth();
				}

				if (isChecked) {
					onBgView.bringToFront();
					onBgView.setVisibility(View.VISIBLE);
					offBgView.setVisibility(View.VISIBLE);
					Log.e("ChatSwitchView", "这时候的选择情况1111111========" + isChecked);
					TranslateAnimation an1 = new TranslateAnimation(0, targetX,0, 0);
					an1.setFillAfter(true);
					an1.setDuration(100);
					circleView.startAnimation(an1);

					AlphaAnimation an2 = new AlphaAnimation(0, 1);
					an2.setFillAfter(true);
					an2.setDuration(100);
					onBgView.startAnimation(an2);
				} else {
					offBgView.bringToFront();
					onBgView.setVisibility(View.VISIBLE);
					offBgView.setVisibility(View.VISIBLE);
					Log.e("SwitchView", "这时候的选择情况222222222========" + isChecked);
					TranslateAnimation an1 = new TranslateAnimation(targetX, 0,0, 0);
					an1.setFillAfter(true);
					an1.setDuration(100);
					circleView.startAnimation(an1);

					AlphaAnimation an2 = new AlphaAnimation(0, 1);
					an2.setFillAfter(true);
					an2.setDuration(100);
					offBgView.startAnimation(an2);
				}

			}
		};
		this.changedListener = changedListener;
	}

	public ChatSwitchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public ChatSwitchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}

	protected void initialize() {
		setClickable(true);
		LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.switch_view, this);
		onBgView = findViewById(R.id.on_bg_view);
		offBgView = findViewById(R.id.off_bg_view);
		circleView = findViewById(R.id.circle_view);
		on();
	}

	protected void setText(String onText, String offText) {

	}

	public void setAutoForPerformClick(boolean autoForPerformClick) {
		this.autoForPerformClick = autoForPerformClick;
	}

	@Override
	public boolean performClick() {
		if (!autoForPerformClick)
			return super.performClick();
		setChecked(!isChecked, true);
		if (onCheckedChangedListener != null) {
			Log.e("SwitchView", "到不为空的地方了========" + isChecked);
			onCheckedChangedListener.onChanged(this, isChecked);
		} else {
			Log.e("SwitchView", "到为空的地方了========" + isChecked);
		}
		return super.performClick();
	}

	public void setChecked(boolean value) {
		setChecked(value, true);
	}

	public void setChecked(boolean value, boolean needAnimate) {
		if (isChecked == value)
			return;
		isChecked = value;

		float targetX = 0;
		if (getWidth() != 0) {
			targetX = getWidth() - circleView.getWidth();
		} else {
			measure(0, 0);
			targetX = getMeasuredWidth() - circleView.getMeasuredWidth();
		}

		long durationMillis = needAnimate ? 200 : 0;
		if (isChecked) {
			onBgView.bringToFront();
			onBgView.setVisibility(View.VISIBLE);
			offBgView.setVisibility(View.VISIBLE);
			Log.e("SwitchView", "这时候的选择情况1111111========" + isChecked);
			TranslateAnimation an1 = new TranslateAnimation(0, targetX, 0, 0);
			an1.setFillAfter(true);
			an1.setDuration(durationMillis);
			circleView.startAnimation(an1);

			AlphaAnimation an2 = new AlphaAnimation(0, 1);
			an2.setFillAfter(true);
			an2.setDuration(durationMillis);
			onBgView.startAnimation(an2);
			TeamSetting2Activity.getChat.Chat(1,true);
		} else {
			offBgView.bringToFront();
			onBgView.setVisibility(View.VISIBLE);
			offBgView.setVisibility(View.VISIBLE);
			Log.e("SwitchView", "这时候的选择情况222222222========" + isChecked);
			TranslateAnimation an1 = new TranslateAnimation(targetX, 0, 0, 0);
			an1.setFillAfter(true);
			an1.setDuration(durationMillis);
			circleView.startAnimation(an1);

			AlphaAnimation an2 = new AlphaAnimation(0, 1);
			an2.setFillAfter(true);
			an2.setDuration(durationMillis);
			offBgView.startAnimation(an2);
			TeamSetting2Activity.getChat.Chat(0,true);
		}
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setOnCheckedChangedListener(OnCheckedChangedListener l) {
		onCheckedChangedListener = l;
	}

	public interface OnCheckedChangedListener {
		void onChanged(View view, boolean checked);
	}
}
