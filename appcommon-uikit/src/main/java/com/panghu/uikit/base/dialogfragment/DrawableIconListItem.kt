package com.panghu.uikit.base.dialogfragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.DrawableRes

/**
 * @author locke.peng on 2019-09-04.
 */
class DrawableIconListItem : ListItem {

    private val iconResId: Int

    constructor(
        caption: String,
        value: String,
        isValueVisible: Boolean = false,
        textColorResId: Int = 0,
        @DrawableRes iconResId: Int
    ) : super(caption, value, isValueVisible, textColorResId) {
        this.iconResId = iconResId
    }

    constructor(parcel: Parcel) : super(parcel) {
        iconResId = parcel.readInt()
    }

    override fun getItemIcon(context: Context): Drawable? {
        return context.getDrawable(iconResId)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeInt(iconResId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DrawableIconListItem> {
        override fun createFromParcel(parcel: Parcel): DrawableIconListItem {
            return DrawableIconListItem(parcel)
        }

        override fun newArray(size: Int): Array<DrawableIconListItem?> {
            return arrayOfNulls(size)
        }
    }
}