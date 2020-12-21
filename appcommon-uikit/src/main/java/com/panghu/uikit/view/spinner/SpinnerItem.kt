package com.panghu.uikit.view.spinner

/**
 * @author by panghu
 * @date 2019-07-08
 */

data class SpinnerItem(
    val title: CharSequence,
    val value: CharSequence,
    val fontIcon: CharSequence? = null,
    var count: Int = 0
)