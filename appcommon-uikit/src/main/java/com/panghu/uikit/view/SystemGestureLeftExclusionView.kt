package com.panghu.uikit.view

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.widget.LinearLayout

class SystemGestureLeftExclusionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val boundingBox: Rect = Rect()
    private val exclusions = listOf(boundingBox)

    @TargetApi(Build.VERSION_CODES.Q)
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && changed) {
            boundingBox.set(left, top, right / 2, bottom)
            systemGestureExclusionRects = exclusions
        }
    }
}