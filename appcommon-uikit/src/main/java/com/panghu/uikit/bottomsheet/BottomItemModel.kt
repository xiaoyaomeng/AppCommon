package com.panghu.uikit.bottomsheet

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import com.panghu.uikit.R
import com.panghu.uikit.base.FontIconFactory
import com.glip.widgets.icon.IconDrawable

class BottomItemModel : Parcelable {
    var id: Int = 0
    @StringRes
    var iconRes: Int = 0
    @DimenRes
    var sizeRes: Int = ICON_SIZE_RES_ID
    @StringRes
    var textRes: Int = 0
    var isSelected: Boolean = false
    var text: String? = null

    @JvmOverloads
    constructor(
        id: Int, @StringRes iconRes: Int, @StringRes textRes: Int,
        isSelected: Boolean = false
    ) {
        this.id = id
        this.iconRes = iconRes
        this.textRes = textRes
        this.isSelected = isSelected
    }

    @JvmOverloads
    constructor(id: Int, @StringRes iconRes: Int, text: String, isSelected: Boolean = false) {
        this.id = id
        this.iconRes = iconRes
        this.text = text
        this.isSelected = isSelected
    }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    ) {
        text = parcel.readString()
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(iconRes)
        parcel.writeInt(textRes)
        parcel.writeByte(if (isSelected) 1 else 0)
        parcel.writeString(text)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun getIcon(context: Context): Drawable? {
        if (iconRes == 0) {
            return null
        }
        return ResourcesCompat.getColorStateList(
            context.resources,
            R.color.color_bottom_sheet_icon,
            null
        )?.let {
            getStateListDrawable(context, it)
        } ?: FontIconFactory.getDrawableIcon(
            context, iconRes, ICON_SIZE_RES_ID, ICON_COLOR_RES_ID
        )
    }

    private fun getStateListDrawable(context: Context, colorStateList: ColorStateList): Drawable {
        val drawable = StateListDrawable()
        val defaultStateSet =
            intArrayOf(-android.R.attr.state_selected, -android.R.attr.state_pressed)
        val unselectedStateSet = intArrayOf(-android.R.attr.state_selected)
        val selectedStateSet = intArrayOf(android.R.attr.state_selected)

        arrayListOf(defaultStateSet, unselectedStateSet, selectedStateSet).forEach { stateSet ->
            val color = colorStateList.getColorForState(stateSet, colorStateList.defaultColor)
            drawable.addState(stateSet, IconDrawable(context, iconRes).apply {
                sizeRes(sizeRes)
                color(color)
                alpha = Color.alpha(color)
            })
        }
        return drawable
    }

    companion object CREATOR : Parcelable.Creator<BottomItemModel> {
        override fun createFromParcel(parcel: Parcel): BottomItemModel {
            return BottomItemModel(parcel)
        }

        override fun newArray(size: Int): Array<BottomItemModel?> {
            return arrayOfNulls(size)
        }

        private val ICON_SIZE_RES_ID = R.dimen.font_icon_display_size
        private val ICON_COLOR_RES_ID = R.color.color_bottom_sheet_icon

    }
}