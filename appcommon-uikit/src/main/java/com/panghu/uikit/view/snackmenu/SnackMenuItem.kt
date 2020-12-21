package com.panghu.uikit.view.snackmenu;

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import com.panghu.uikit.R

/**
 * Menu item for create snack menu.
 * @author by panghu
 * @date 08/04/2020
 */
data class SnackMenuItem(
    val itemId: Int,
    val groupId: Int,
    var text: String? = null,
    @StringRes val iconRes: Int = R.string.icon_share,
    @DimenRes val sizeRes: Int = R.dimen.font_icon_display_size,
    val isAlert: Boolean = false,
    val isInMore: Boolean = false
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(itemId)
        parcel.writeInt(groupId)
        parcel.writeString(text)
        parcel.writeInt(iconRes)
        parcel.writeInt(sizeRes)
        parcel.writeByte(if (isAlert) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SnackMenuItem> {
        override fun createFromParcel(parcel: Parcel): SnackMenuItem {
            return SnackMenuItem(parcel)
        }

        override fun newArray(size: Int): Array<SnackMenuItem?> {
            return arrayOfNulls(size)
        }
    }
}