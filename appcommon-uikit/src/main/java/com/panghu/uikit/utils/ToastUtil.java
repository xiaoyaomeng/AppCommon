package com.panghu.uikit.utils;

import android.content.Context;
import androidx.annotation.StringRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.panghu.uikit.R;

import java.util.HashMap;

import static com.glip.widgets.utils.AccessibilityUtils.isAccessibilityOn;

/**
 * Customized Toast Widget Used For Glip
 * Created by dennis.jiang on 6/23/17.
 */

public class ToastUtil {
    public enum ToastType {
        COMMON, ERROR, WARNING, SUCCESS
    }

    public enum ToastTextAlign {
        DEFAULT {
            @Override
            int getTextAlign() {
                return -1;
            }
        },
        START {
            @Override
            int getTextAlign() {
                return View.TEXT_ALIGNMENT_TEXT_START;
            }
        },
        CENTER {
            @Override
            int getTextAlign() {
                return View.TEXT_ALIGNMENT_CENTER;
            }
        };

        abstract int getTextAlign();
    }

    public enum ToastStyle {
        TOP {
            @Override
            int getViewLayout(ToastType type) {
                return R.layout.park_location_toast_view;
            }

            @Override
            int getGravity() {
                return Gravity.TOP | Gravity.FILL_HORIZONTAL;
            }
        },
        BOTTOM {
            @Override
            int getViewLayout(ToastType type) {
                return TOAST_TYPE_LAYOUT_MAP.get(type);
            }

            @Override
            int getGravity() {
                return Gravity.BOTTOM | Gravity.FILL_HORIZONTAL;
            }
        },
        CENTER {
            @Override
            int getViewLayout(ToastType type) {
                // support ToastType.COMMON only
                return R.layout.base_center_toast_view;
            }

            @Override
            int getGravity() {
                return Gravity.CENTER;
            }
        };

        abstract int getViewLayout(ToastType type);

        abstract int getGravity();
    }

    private static HashMap<ToastType, Integer> TOAST_TYPE_LAYOUT_MAP = new HashMap<ToastType, Integer>() {{
        put(ToastType.COMMON, R.layout.base_common_toast_view);
        put(ToastType.ERROR, R.layout.base_error_toast_view);
        put(ToastType.WARNING, R.layout.base_warning_toast_view);
        put(ToastType.SUCCESS, R.layout.base_success_toast_view);
    }};

    public static Toast showCommonToast(Context context, @StringRes int stringId) {
        Toast toast = makeToast(context, ToastStyle.BOTTOM, ToastType.COMMON, context.getString(stringId));
        toast.show();
        return toast;
    }

    public static Toast showErrorToast(Context context, @StringRes int stringId) {
        Toast toast = makeToast(context, ToastStyle.BOTTOM, ToastType.ERROR, context.getString(stringId));
        toast.show();
        return toast;
    }

    public static Toast showWarningToast(Context context, @StringRes int stringId) {
        Toast toast = makeToast(context, ToastStyle.BOTTOM, ToastType.WARNING, context.getString(stringId));
        toast.show();
        return toast;
    }

    public static Toast showWarningToast(Context context, String text) {
        Toast toast = makeToast(context, ToastStyle.BOTTOM, ToastType.WARNING, text);
        toast.show();
        return toast;
    }

    public static Toast showSuccessToast(Context context, @StringRes int stringId) {
        Toast toast = makeToast(context, ToastStyle.BOTTOM, ToastType.SUCCESS, context.getString(stringId));
        toast.show();
        return toast;
    }

    public static Toast showCustomToast(Context context, @StringRes int stringId) {
        CustomDurationToastCompat toast = new CustomDurationToastCompat(context);
        toast.setCustomIcon(R.drawable.ic_thumb)
                .setCustomText(stringId);
        if (isAccessibilityOn(context)) {
            toast.setCustomDurationTime(TimeUtil.secondsToMillis(3L));
        } else {
            toast.setCustomDurationTime(TimeUtil.secondsToMillis(1L));
        }
        toast.show();
        return toast;
    }

    public static Toast makeToast(Context context, ToastStyle style, ToastType type, String text) {
        return makeToast(context, style, type, ToastTextAlign.DEFAULT, text);
    }

    public static Toast makeToast(Context context, ToastStyle style, ToastType type, ToastTextAlign textAlign, String text) {
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(style.getViewLayout(type), null);
        TextView textView = view.findViewById(R.id.toast_text_view);
        if (textAlign != ToastTextAlign.DEFAULT) {
            textView.setTextAlignment(textAlign.getTextAlign());
        }
        textView.setText(text);
        return makeToast(context, view, style.getGravity());
    }

    public static Toast makeToast(Context context, View view, int gravity) {
        Toast toast = new ToastCompat(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(gravity, 0, 0);
        toast.setView(view);
        return toast;
    }
}
