package com.panghu.uikit.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import androidx.annotation.ArrayRes;
import androidx.annotation.ColorRes;
import androidx.annotation.StyleRes;
import android.widget.TextView;

import com.panghu.uikit.R;


/**
 * Created by panghu on 9/1/16.
 */
public class ResourceUtil {

    @SuppressLint("ResourceType")
    public static int[] getResourceIdArray(Context context, @ArrayRes int arrayResId) {
        if (arrayResId > 0) {
            TypedArray typedArray = context.getResources().obtainTypedArray(arrayResId);
            if (typedArray != null) {
                int[] drawableResIdArray = new int[typedArray.length()];
                for (int i = 0; i < typedArray.length(); ++i) {
                    drawableResIdArray[i] = typedArray.getResourceId(i, 0);
                }
                return drawableResIdArray;
            }
            typedArray.recycle();
        }
        return null;
    }

    public static void setTextAppearance(TextView textView, @StyleRes int resId) {
        if (Build.VERSION.SDK_INT < 23) {
            textView.setTextAppearance(textView.getContext(), resId);
        } else {
            textView.setTextAppearance(resId);
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static boolean isTablet(Context context) {
        return context.getResources().getBoolean(R.bool.isTablet);
    }

    public static int getColor(Context context, @ColorRes int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(resId);
        } else {
            return context.getResources().getColor(resId);
        }
    }

    public static int getDimenSize(Context context, int dimenId) {
        return context.getResources().getDimensionPixelSize(dimenId);
    }

    public static String getString(Context context, int stringId){
        return context.getResources().getString(stringId);
    }

}
