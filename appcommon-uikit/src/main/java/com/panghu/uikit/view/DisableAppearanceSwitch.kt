package com.panghu.uikit.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Switch
import com.panghu.uikit.R

class DisableAppearanceSwitch @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : Switch(context, attrs) {

    private val stateEnableAppearance = intArrayOf(R.attr.state_dim_appearance_enable)
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
            View.mergeDrawableStates(drawableState, stateEnableAppearance)
        }
        return drawableState
    }
}