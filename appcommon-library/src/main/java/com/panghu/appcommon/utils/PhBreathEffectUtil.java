package com.panghu.appcommon.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 *
 * PhBreathEffectUtil
 * 呼吸动效类
 *
 * @autor lijiangping
 * @wechat ljphhj
 * @email lijiangping.zz@gmail.com
 *
 */
public class PhBreathEffectUtil {

    /**
     * 呼吸灯效果
     *
     * @param v
     */
    public static void breath(View v) {
        breath(v, 0.0f, 1.0f, 1000);
    }

    /**
     * 呼吸灯效果
     *
     * @param v
     * @param fromRange
     * @param toRange
     * @param duration
     */
    public static void breath(View v, float fromRange, float toRange, long duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, View.ALPHA, fromRange, toRange);
        animator.setDuration(duration);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
    }

}
