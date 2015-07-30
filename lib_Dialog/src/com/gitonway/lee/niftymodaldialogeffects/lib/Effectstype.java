package com.gitonway.lee.niftymodaldialogeffects.lib;

import com.gitonway.lee.niftymodaldialogeffects.lib.effects.BaseEffects;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.FadeIn;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.Fall;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.FlipH;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.FlipV;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.NewsPaper;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.RotateLeft;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.Shake;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.SideFall;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.RotateBottom;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.SlideBottom;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.SlideLeft;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.SlideRight;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.SlideTop;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.Slit;

/**
 * Created by lee on 2014/7/30.
 */
public enum  Effectstype {

    Fadein(FadeIn.class),//淡入淡出，背景变灰色
    Slideleft(SlideLeft.class),//左滑进来
    Slidetop(SlideTop.class),//从上进来
    SlideBottom(SlideBottom.class),//右滑进来
    Slideright(SlideRight.class),//从下进来
    Fall(Fall.class),//从大变小进来
    Newspager(NewsPaper.class),//旋转进来
    Fliph(FlipH.class),//左翻转进来
    Flipv(FlipV.class),//右翻转
    RotateBottom(RotateBottom.class),//从下翻转
    RotateLeft(RotateLeft.class),//从左翻转
    Slit(Slit.class),//右翻转
    Shake(Shake.class),//震动
    Sidefill(SideFall.class);//斜着进来
    private Class<? extends BaseEffects> effectsClazz;

    private Effectstype(Class<? extends BaseEffects> mclass) {
        effectsClazz = mclass;
    }

    public BaseEffects getAnimator() {
        BaseEffects bEffects=null;
	try {
		bEffects = effectsClazz.newInstance();
	} catch (ClassCastException e) {
		throw new Error("Can not init animatorClazz instance");
	} catch (InstantiationException e) {
		// TODO Auto-generated catch block
		throw new Error("Can not init animatorClazz instance");
	} catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		throw new Error("Can not init animatorClazz instance");
	}
	return bEffects;
    }
}
