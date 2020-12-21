package com.panghu.uikit.utils

import android.widget.TextView

/**
 * Extension functions for TextView
 *
 * @author hugh.wang
 * @date 11/22/2019
 */
fun TextView.setStylingText(resId: Int, vararg formatArgs: Any?) {
    text = TextStylingHelper.parseStylingText(context, resId, *formatArgs)
}