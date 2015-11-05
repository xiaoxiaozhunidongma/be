package com.BJ.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import com.biju.R;
import com.biju.function.NewteamActivity;
import com.biju.function.RequestCode3Activity;
import com.biju.function.RequestCodeActivity;

@SuppressLint("NewApi")
public class VerifyCodeView extends View {

	StringBuilder verifyCodeBuilder;
	// 一个字符或横线占用的宽度
	private int characterWidth;
	// 一个数字后中间的间隔
	private int centerSpacing;
	// 两字符间隔
	private int characterSpacing;
	private int textSize;
	Paint textPaint;
	float textBaseY;
	private String code;

	public VerifyCodeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 能获取焦点才能弹出软键盘
		setFocusableInTouchMode(true);
		setFocusable(true);
		requestFocus();
		verifyCodeBuilder = new StringBuilder(6);
		
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		centerSpacing = getResources().getDimensionPixelSize(R.dimen.reg_verifycode_center_spacing);
		characterSpacing = getResources().getDimensionPixelSize(R.dimen.reg_verifycode_character_spacing);
		textSize = getResources().getDimensionPixelSize(R.dimen.reg_verifycode_textsize);
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 在View上点击时弹出软键盘
		InputMethodManager imm = (InputMethodManager) getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		// 反射调隐藏的focusIn方法，如果不调，在VerifyCodeView之前有EditText等可输入控件时，不会弹出输入法
		// 可能有其他google提供的方法，但我没找到
		Class<? extends InputMethodManager> immClass = imm.getClass();
		Method focusIn;
		try {
			focusIn = immClass.getDeclaredMethod("focusIn", View.class);
			focusIn.invoke(imm, this);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		imm.viewClicked(this);
		imm.showSoftInput(this, 0);
		return super.onTouchEvent(event);
	}

//	@Override
//	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
//		// 定义软键盘样式为数字键盘
//		outAttrs.inputType = InputType.TYPE_CLASS_TEXT;
//		return super.onCreateInputConnection(outAttrs);
//	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.e("VerifyCodeView", "这时候的keyCode=================="+keyCode);
		// 接收按键事件，67是删除键(backspace),7-16就是0-9
		if (keyCode == 67 && verifyCodeBuilder.length() > 0) {
			verifyCodeBuilder.deleteCharAt(verifyCodeBuilder.length() - 1);
			// 重新绘图
			invalidate();
		} else if (keyCode >= 7 && keyCode <= 16&& verifyCodeBuilder.length() < 4) {
			verifyCodeBuilder.append(keyCode - 7);
			invalidate();
		}else
		{
			//A、B、C、D、E、F、G、H、I、J、K、L、M、N、O、P、Q、R、S、T、U、V、W、X、Y、Z
			switch (keyCode) {
			case 29:
				verifyCodeBuilder.append("a");
				break;
			case 30:
				verifyCodeBuilder.append("b");
				break;
			case 31:
				verifyCodeBuilder.append("c");
				break;
			case 32:
				verifyCodeBuilder.append("d");
				break;
			case 33:
				verifyCodeBuilder.append("e");
				break;
			case 34:
				verifyCodeBuilder.append("f");
				break;
			case 35:
				verifyCodeBuilder.append("g");
				break;
			case 36:
				verifyCodeBuilder.append("h");
				break;
			case 37:
				verifyCodeBuilder.append("i");
				break;
			case 38:
				verifyCodeBuilder.append("j");
				break;
			case 39:
				verifyCodeBuilder.append("k");
				break;
			case 40:
				verifyCodeBuilder.append("l");
				break;
			case 41:
				verifyCodeBuilder.append("m");
				break;
			case 42:
				verifyCodeBuilder.append("n");
				break;
			case 43:
				verifyCodeBuilder.append("o");
				break;
			case 44:
				verifyCodeBuilder.append("p");
				break;
			case 45:
				verifyCodeBuilder.append("q");
				break;
			case 46:
				verifyCodeBuilder.append("r");
				break;
			case 47:
				verifyCodeBuilder.append("s");
				break;
			case 48:
				verifyCodeBuilder.append("t");
				break;
			case 49:
				verifyCodeBuilder.append("u");
				break;
			case 50:
				verifyCodeBuilder.append("v");
				break;
			case 51:
				verifyCodeBuilder.append("w");
				break;
			case 52:
				verifyCodeBuilder.append("x");
				break;
			case 53:
				verifyCodeBuilder.append("y");
				break;
			case 54:
				verifyCodeBuilder.append("z");
				break;
			default:
				break;
			}
			invalidate();
		}
		// 到了4位自动隐藏软键盘
		if (verifyCodeBuilder.length() >= 4) {
			InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getWindowToken(), 0);
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 获取输入的校验码
	 * 
	 * @return
	 */
	public String getVerifyCodeStr() {
		if (verifyCodeBuilder.toString() != null) {
			code = verifyCodeBuilder.toString();
			if(code.length()==4)
			{
				boolean requestcode=SdPkUser.requestcode;//只有从邀请码过来时才进入
				Log.e("VerifyCodeView","获取requestcode=========" + requestcode);
				if(requestcode)
				{
					Log.e("VerifyCodeView","获取输入的校验码=========" + verifyCodeBuilder.toString());
					SdPkUser.setGetCode(code);
					RequestCodeActivity.interActivity.startActivity();
				}
			}
		}
		return verifyCodeBuilder.toString();
	}

	/**
	 * 设置显示的校验码
	 * 
	 * @param verifyCode
	 */
	public void setVerifyCode(String verifyCode) {
		if (verifyCodeBuilder.length() > 0) {
			verifyCodeBuilder.delete(0, verifyCodeBuilder.length());
		}
		verifyCodeBuilder.append(verifyCode);
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 计算一个字符的宽度
		if (characterWidth == 0) {
			int w = getWidth() - getPaddingLeft() - getPaddingRight();
			characterWidth = (w - centerSpacing - 2 * characterSpacing) / 4;
		}
		textPaint.setTextSize(textSize);
		textPaint.setTextAlign(Paint.Align.CENTER);
		Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
		float fontHeight = fontMetrics.bottom - fontMetrics.top;
		if (textBaseY == 0)
			textBaseY = getHeight() - (getHeight() - fontHeight) / 2
					- fontMetrics.bottom;
		// 写已输入的验证码
		if (verifyCodeBuilder.length() > 0) {
			textPaint.setColor(getResources().getColor(R.color.black));
			String verifyCodeStr = getVerifyCodeStr();
			char[] chars = verifyCodeStr.toCharArray();
			int x, y = (int) textBaseY;
			for (int i = 0; i < chars.length; i++) {
				// 计算x,y
				if (i <= 2) {
					x = (characterWidth + characterSpacing) * i
							+ characterWidth / 2;
				} else {
					x = (characterWidth + characterSpacing) * 2
							+ characterWidth + centerSpacing
							+ (characterWidth + characterSpacing) * (i - 3)
							+ characterWidth / 2;
				}
				canvas.drawText(chars, i, 1, x, y, textPaint);
			}
		}
		// 画白线
		if (verifyCodeBuilder.length() < 4) {
			for (int i = verifyCodeBuilder.length(); i < 4; i++) {
				textPaint.setColor(getResources().getColor(R.color.lightgray1));
				textPaint.setStrokeWidth(10);// 改变线的粗细
				int x, y = (int) textBaseY;
				if (i <= 2) {
					x = (characterWidth + characterSpacing) * i;
				} else {
					x = (characterWidth + characterSpacing) * 2
							+ characterWidth + centerSpacing
							+ (characterWidth + characterSpacing) * (i - 3);
				}
				canvas.drawLine(x, y, x + characterWidth, y, textPaint);
			}
		}
	}

}
