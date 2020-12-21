package com.panghu.uikit.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.StringRes;

import com.panghu.uikit.R;
import com.glip.widgets.icon.IconDrawable;

/**
 * Created by steve.chen on 6/12/16.
 */

public class FontIconFactory {

    public static IconDrawable getDrawableIcon(
            Context context,
            @StringRes int iconRes,
            @DimenRes int sizeRes,
            @ColorRes int colorRes) {

        IconDrawable iconDrawable = new IconDrawable(context, iconRes);
        int color = context.getResources().getColor(colorRes);
        iconDrawable.sizeRes(sizeRes);
        iconDrawable.color(color);
        iconDrawable.setAlpha(Color.alpha(color));
        return iconDrawable;
    }

    public static StateListDrawable getStateListIcon(
            Context context,
            @StringRes int iconRes,
            @DimenRes int sizeRes,
            @ColorRes int colorRes,
            @ColorRes int disableColorRes) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(
                new int[]{-android.R.attr.state_enabled},
                getDrawableIcon(context, iconRes, sizeRes, disableColorRes));
        drawable.addState(new int[0], getDrawableIcon(context, iconRes, sizeRes, colorRes));
        return drawable;
    }

    public static IconDrawable getMenuIcon(Context context, @StringRes int iconRes) {
        return getDrawableIcon(
                context, iconRes, R.dimen.font_icon_display_size, R.color.colorPaletteOnBgIII300);
    }

    public static IconDrawable getMenuIcon(
            Context context, @StringRes int iconRes, boolean isEnable) {
        return getDrawableIcon(
                context,
                iconRes,
                R.dimen.font_icon_display_size,
                isEnable ? R.color.colorPaletteOnBgIII300 : R.color.colorPaletteOnBgIII400);
    }

    public static IconDrawable getMenuIconWithSize(
            Context context, @DimenRes int sizeRes, @StringRes int iconRes) {
        return getDrawableIcon(context, iconRes, sizeRes, R.color.colorPaletteOnBgIII300);
    }

    public static IconDrawable getMenuIcon(
            Context context, @StringRes int iconRes, @ColorRes int color) {
        return getDrawableIcon(context, iconRes, R.dimen.font_icon_display_size, color);
    }

    public static Drawable getStateMenuIcon(Context context, @StringRes int iconRes) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(
                new int[]{android.R.attr.state_enabled},
                getDrawableIcon(
                        context, iconRes, R.dimen.font_icon_display_size, R.color.colorPaletteOnBgIII300));
        drawable.addState(
                new int[0],
                getDrawableIcon(
                        context, iconRes, R.dimen.font_icon_display_size, R.color.colorPaletteOnBgIII400));
        return drawable;
    }

    public static Drawable getFABIcon(Context context, @StringRes int iconRes) {
        return getDrawableIcon(context, iconRes, R.dimen.fab_icon_size, R.color.colorPaletteOnBgVI100);
    }

    public static Drawable getFABIcon(
            Context context, @StringRes int iconRes, @ColorRes int iconColor) {
        return getDrawableIcon(context, iconRes, R.dimen.fab_icon_size, iconColor);
    }
}
