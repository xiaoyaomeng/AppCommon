package com.panghu.uikit.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.panghu.uikit.R


class DisableAppearanceTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    private val STATE_ENABLE_APPEARANCE = intArrayOf(R.attr.state_dim_appearance_enable)
    var isDimAppearanceEnabled = false
        set(value) {
            if (value != field) {
                field = value
                refreshDrawableState()
            }
        }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isDimAppearanceEnabled) {
            View.mergeDrawableStates(drawableState, STATE_ENABLE_APPEARANCE)
        }
        return drawableState
    }
}