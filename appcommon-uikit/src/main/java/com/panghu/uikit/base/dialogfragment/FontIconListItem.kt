package com.panghu.uikit.base.dialogfragment

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import com.panghu.uikit.R
import com.panghu.uikit.base.FontIconFactory
import com.glip.widgets.icon.IconDrawable

/**
 * @author locke.peng on 2019-09-04.
 */
class FontIconListItem : ListItem {

    private val iconResId: Int
    private val iconSizeResId: Int
    private val iconColorResId: Int

    constructor(
        caption: String,
        value: String,
        isValueVisible: Boolean = false,
        textColorResId: Int = 0,
        @StringRes iconResId: Int,
        @DimenRes iconSizeResId: Int = R.dimen.font_icon_display_size,
        @ColorRes iconColorResId: Int = R.color.color_night_selector
    ) : super(caption, value, isValueVisible, textColorResId) {
        this.iconResId = iconResId
        this.iconSizeResId = iconSizeResId
        this.iconColorResId = iconColorResId
    }

    constructor(parcel: Parcel) : super(parcel) {
        iconResId = parcel.readInt()
        iconSizeResId = parcel.readInt()
        iconColorResId = parcel.readInt()
    }

    override fun getItemIcon(context: Context): Drawable? {
        val colorStateList =
            ResourcesCompat.getColorStateList(context.resources, iconColorResId, null)
        return if (colorStateList != null) {
            getStateListDrawable(context, colorStateList)
        } else {
            return FontIconFactory.getDrawableIcon(
                context, iconResId, iconSizeResId, iconColorResId
            )
        }
    }

    private fun getStateListDrawable(context: Context, colorStateList: ColorStateList): Drawable {
        val drawable = StateListDrawable()
        val defaultStateSet =
            intArrayOf(-android.R.attr.state_selected, -android.R.attr.state_pressed)
        val pressedStateSet = intArrayOf(android.R.attr.state_pressed)
        val selectedStateSet = intArrayOf(android.R.attr.state_selected)

        arrayListOf(defaultStateSet, pressedStateSet, selectedStateSet).forEach { stateSet ->
            val color = colorStateList.getColorForState(stateSet, colorStateList.defaultColor)
            drawable.addState(stateSet, IconDrawable(context, iconResId).apply {
                sizeRes(iconSizeResId)
                color(color)
                alpha = Color.alpha(color)
            })
        }
        return drawable
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeInt(iconResId)
        parcel.writeInt(iconSizeResId)
        parcel.writeInt(iconColorResId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FontIconListItem> {
        override fun createFromParcel(parcel: Parcel): FontIconListItem {
            return FontIconListItem(parcel)
        }

        override fun newArray(size: Int): Array<FontIconListItem?> {
            return arrayOfNulls(size)
        }
    }
}