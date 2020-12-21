package com.panghu.uikit.base.dialogfragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.ColorRes
import java.io.Serializable

/**
 * Created by panghu on 4/10/17.
 */
open class ListItem @JvmOverloads constructor(
    val caption: String,
    val value: String,
    val isValueVisible: Boolean = false,
    @ColorRes val textColorResId: Int = 0
) : Serializable, Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readInt()
    )

    open fun getItemIcon(context: Context): Drawable? {
        return null
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(caption)
        parcel.writeString(value)
        parcel.writeByte(if (isValueVisible) 1 else 0)
        parcel.writeInt(textColorResId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ListItem> {
        override fun createFromParcel(parcel: Parcel): ListItem {
            return ListItem(parcel)
        }

        override fun newArray(size: Int): Array<ListItem?> {
            return arrayOfNulls(size)
        }
    }
}
