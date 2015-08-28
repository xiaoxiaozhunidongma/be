package com.BJ.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.content.Context;
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

@SuppressLint("NewApi")
public class VerifyCodeView extends View {

	StringBuilder verifyCodeBuilder;
	// һ���ַ������ռ�õĿ��
	private int characterWidth;
	// һ�����ֺ��м�ļ��
	private int centerSpacing;
	// ���ַ����
	private int characterSpacing;
	private int textSize;
	Paint textPaint;
	float textBaseY;

	public VerifyCodeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// �ܻ�ȡ������ܵ��������
		setFocusableInTouchMode(true);
		verifyCodeBuilder = new StringBuilder(6);
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		centerSpacing = getResources().getDimensionPixelSize(
				R.dimen.reg_verifycode_center_spacing);
		characterSpacing = getResources().getDimensionPixelSize(
				R.dimen.reg_verifycode_character_spacing);
		textSize = getResources().getDimensionPixelSize(
				R.dimen.reg_verifycode_textsize);
	}

	@Override
    public boolean onTouchEvent(MotionEvent event) {
        //��View�ϵ��ʱ���������
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //��������ص�focusIn�����������������VerifyCodeView֮ǰ��EditText�ȿ�����ؼ�ʱ�����ᵯ�����뷨
        //����������google�ṩ�ķ���������û�ҵ�
        Class<? extends InputMethodManager> immClass = imm.getClass();
            Method focusIn;
			try {
				focusIn = immClass.getDeclaredMethod("focusIn", View.class);
				focusIn.invoke(imm,this);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        imm.viewClicked(this);
        imm.showSoftInput(this, 0);
        return super.onTouchEvent(event);
    }

	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
		// �����������ʽΪ���ּ���
		outAttrs.inputType = InputType.TYPE_CLASS_NUMBER;
		return super.onCreateInputConnection(outAttrs);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// ���հ����¼���67��ɾ����(backspace),7-16����0-9
		if (keyCode == 67 && verifyCodeBuilder.length() > 0) {
			verifyCodeBuilder.deleteCharAt(verifyCodeBuilder.length() - 1);
			// ���»�ͼ
			invalidate();
		} else if (keyCode >= 7 && keyCode <= 16
				&& verifyCodeBuilder.length() < 4) {
			verifyCodeBuilder.append(keyCode - 7);
			invalidate();
		}
		// ����6λ�Զ����������
		if (verifyCodeBuilder.length() >= 4) {
			InputMethodManager imm = (InputMethodManager) getContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getWindowToken(), 0);
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * ��ȡ�����У����
	 * 
	 * @return
	 */
	public String getVerifyCodeStr() {
		if(verifyCodeBuilder.toString()!=null)
		{
			String code=verifyCodeBuilder.toString();
			Log.e("VerifyCodeView", "��ȡ�����У����========="+verifyCodeBuilder.toString());
			SdPkUser.setGetCode(code);
		}
		return verifyCodeBuilder.toString();
	}

	/**
	 * ������ʾ��У����
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
		// ����һ���ַ��Ŀ��
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
		// д���������֤��
		if (verifyCodeBuilder.length() > 0) {
			textPaint.setColor(getResources().getColor(R.color.black));
			String verifyCodeStr = getVerifyCodeStr();
			char[] chars = verifyCodeStr.toCharArray();
			int x, y = (int) textBaseY;
			for (int i = 0; i < chars.length; i++) {
				// ����x,y
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
		// ������
		if (verifyCodeBuilder.length() < 4) {
			for (int i = verifyCodeBuilder.length(); i < 4; i++) {
				textPaint.setColor(getResources().getColor(R.color.red));
				textPaint.setStrokeWidth(20);//�ı��ߵĴ�ϸ
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
