package com.panghu.uikit.view.popupwindow;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@IntDef({
        XGravity.CENTER,
        XGravity.LEFT,
        XGravity.RIGHT,
        XGravity.ALIGN_LEFT,
        XGravity.ALIGN_RIGHT
})
@Retention(RetentionPolicy.SOURCE)
public @interface XGravity {
    int CENTER = 0;
    int LEFT = 1;
    int RIGHT = 2;
    int ALIGN_LEFT = 3;
    int ALIGN_RIGHT = 4;
}