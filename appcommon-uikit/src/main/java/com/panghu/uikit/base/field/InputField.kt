package com.panghu.uikit.base.field

import androidx.annotation.StringRes

/**
 * @author locke.peng on 4/27/18.
 */
class InputField(
    id: FieldId, @StringRes iconResId: Int,
    hasDivider: Boolean,
    isVisible: Boolean,
    @StringRes val titleResId: Int, @StringRes val hintResId: Int, @StringRes val editHintResId: Int,
    var text: String,
    @StringRes val positiveButtonResId: Int, @StringRes val negativeButtonResId: Int,
    val maxLines: Int,
    val maxLength: Int,
    val inputType: Int, @StringRes val acceptedChars: Int
) : AbstractField(id, iconResId, hasDivider, isVisible)