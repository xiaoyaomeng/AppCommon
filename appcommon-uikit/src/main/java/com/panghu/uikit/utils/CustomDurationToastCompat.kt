package com.panghu.uikit.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.panghu.uikit.R
import java.util.*

/**
 * A custom toast supports:
 * 1. custom icon.
 * 2. custom text.
 * 3. custom duration.
 */
class CustomDurationToastCompat(private val context: Context) : ToastCompat(context) {

    private var lastTime = INVALID_LAST_TIME
    private val customView: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.base_toast_custom, null, false)
    }

    fun setCustomIcon(@DrawableRes resourceId: Int): CustomDurationToastCompat {
        val imageView = customView.findViewById<ImageView>(R.id.toast_icon)
        imageView.setBackgroundResource(resourceId)
        return this
    }

    fun setCustomText(@StringRes stringId: Int): CustomDurationToastCompat {
        val textView = customView.findViewById<TextView>(R.id.toast_text)
        textView.setText(stringId)
        return this
    }

    fun setCustomDurationTime(lastTime: Long): CustomDurationToastCompat {
        if (lastTime > 0) {
            this.lastTime = lastTime
        }
        return this
    }

    override fun show() {
        setGravity(Gravity.FILL_HORIZONTAL, 0, 0)
        view = customView
        if (lastTime == INVALID_LAST_TIME) {
            super.show()
            return
        }
        duration = LENGTH_LONG
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                super@CustomDurationToastCompat.show()
            }
        }, 0, LONG_DELAY)
        timer.schedule(object : TimerTask() {
            override fun run() {
                this@CustomDurationToastCompat.cancel()
                timer.cancel()
            }
        }, lastTime)
    }

    companion object {
        private const val LONG_DELAY = 3500L
        private const val INVALID_LAST_TIME = -1L
    }
}