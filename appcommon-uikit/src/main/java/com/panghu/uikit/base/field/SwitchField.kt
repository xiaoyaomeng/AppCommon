package com.panghu.uikit.base.field

import androidx.annotation.StringRes

/**
 * Created by dennis.jiang on 9/29/16.
 */
open class SwitchField(
    id: FieldId, @StringRes iconResId: Int,
    hasDivider: Boolean,
    isVisible: Boolean,
    var title: String,
    var switch: Boolean,
    isLocked: Boolean = false
) : AbstractField(id, iconResId, hasDivider, isVisible, isLocked) {
    var summary: String? = null
}
