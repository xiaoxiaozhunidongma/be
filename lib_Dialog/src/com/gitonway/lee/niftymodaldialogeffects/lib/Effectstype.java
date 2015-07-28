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

    Fadein(FadeIn.class),//���뵭�����������ɫ
    Slideleft(SlideLeft.class),//�󻬽���
    Slidetop(SlideTop.class),//���Ͻ���
    SlideBottom(SlideBottom.class),//�һ�����
    Slideright(SlideRight.class),//���½���
    Fall(Fall.class),//�Ӵ��С����
    Newspager(NewsPaper.class),//��ת����
    Fliph(FlipH.class),//��ת����
    Flipv(FlipV.class),//�ҷ�ת
    RotateBottom(RotateBottom.class),//���·�ת
    RotateLeft(RotateLeft.class),//����ת
    Slit(Slit.class),//�ҷ�ת
    Shake(Shake.class),//��
    Sidefill(SideFall.class);//б�Ž���
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
