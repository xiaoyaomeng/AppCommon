package com.panghu.uikit.view.popupwindow;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        YGravity.CENTER,
        YGravity.TOP,
        YGravity.BOTTOM,
        YGravity.ALIGN_TOP,
        YGravity.ALIGN_BOTTOM,
})
@Retention(RetentionPolicy.SOURCE)
public @interface YGravity {
    int CENTER = 0;
    int TOP = 1;
    int BOTTOM = 2;
    int ALIGN_TOP = 3;
    int ALIGN_BOTTOM = 4;
}
