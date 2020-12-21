package com.panghu.uikit.utils;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.StringRes;

import com.panghu.uikit.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;

/**
 * Created by locke.peng on 9/19/17.
 */

public class SnackbarUtil {
    public enum SnackbarType {
        COMMON, ERROR, WARNING, SUCCESS
    }

    private static final int SNACKBAR_BACKGROUND_COLOR = R.color.colorPaletteBgVI;

    private static HashMap<SnackbarType, Integer> SNACKBAR_TYPE_STYLE_MAP = new HashMap<SnackbarType, Integer>() {{
        put(SnackbarType.COMMON, R.style.GlipWidget_TextView_Banner);
        put(SnackbarType.ERROR, R.style.GlipWidget_TextView_Banner_Error);
        put(SnackbarType.WARNING, R.style.GlipWidget_TextView_Banner_Warning);
        put(SnackbarType.SUCCESS, R.style.GlipWidget_TextView_Banner_Success);
    }};

    public static Snackbar makeSnackbar(View parentView, SnackbarType snackbarType, @StringRes int stringId, boolean toShow) {
        if (parentView == null|| snackbarType == null || stringId == 0) {
            return null;
        }
        Snackbar snackbar = Snackbar.make(parentView, stringId, Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.setBackgroundColor(ResourceUtil.getColor(snackbar.getContext(), SNACKBAR_BACKGROUND_COLOR));
        TextView titleView = snackbarLayout.findViewById(R.id.snackbar_text);
        if (titleView != null) {
            int style = SNACKBAR_TYPE_STYLE_MAP.get(snackbarType);
            ResourceUtil.setTextAppearance(titleView, style);
        }
        if (toShow) {
            snackbar.show();
        }
        return snackbar;
    }

    public static Snackbar makeSnackbar(View parentView, @StringRes int stringId, boolean toShow) {
        return makeSnackbar(parentView, SnackbarType.COMMON, stringId, toShow);
    }

    public static Snackbar makeErrorSnackbar(View parentView, @StringRes int stringId, boolean toShow) {
        return makeSnackbar(parentView, SnackbarType.ERROR, stringId, toShow);
    }

    public static Snackbar makeWarningSnackbar(View parentView, @StringRes int stringId, boolean toShow) {
        return makeSnackbar(parentView, SnackbarType.WARNING, stringId, toShow);
    }

    public static Snackbar makeSuccessSnackbar(View parentView, @StringRes int stringId, boolean toShow) {
        return makeSnackbar(parentView, SnackbarType.SUCCESS, stringId, toShow);
    }
}
