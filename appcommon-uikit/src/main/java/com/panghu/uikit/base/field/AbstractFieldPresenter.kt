package com.panghu.uikit.base.field

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.annotation.LayoutRes
import com.panghu.uikit.R
import com.panghu.uikit.base.FontIconFactory


/**
 * Created by panghu on 8/30/16.
 */
abstract class AbstractFieldPresenter constructor(@field:LayoutRes
                                                  val layoutResId: Int = R.layout.common_field_view) {

    fun onCreateViewHolder(parent: ViewGroup): FieldViewHolder {
        val fieldView = LayoutInflater.from(parent.context)
                .inflate(layoutResId, parent, false) as ViewGroup
        val customViewStub = fieldView.findViewById<ViewStub>(R.id.custom_view_stub)
        customViewStub.layoutResource = onInflateCustomView()
        val customView = customViewStub.inflate()
        val viewHolder = FieldViewHolder(fieldView, customView, this)
        onCreateCustomView(viewHolder)
        return viewHolder
    }

    protected open fun onCreateCustomView(viewHolder: FieldViewHolder) {

    }

    @JvmOverloads
    fun onBindViewHolder(viewHolder: FieldViewHolder,
                         field: AbstractField,
                         payloads: List<Any> = EMPTY_PAYLOADS) {
        var iconResId = field.iconResId
        if (iconResId <= 0 && field.isLocked) {
            iconResId = R.string.icon_private
        }
        if (viewHolder.mIconView != null) {
            if (iconResId > 0) {
                val dimen = R.dimen.font_icon_display_size
                viewHolder.mIconView.setImageDrawable(
                        FontIconFactory.getDrawableIcon(viewHolder.itemView.context,
                            iconResId, dimen,
                                if (field.iconColor != -1) {
                                    field.iconColor
                                } else {
                                    R.color.colorPaletteOnBgIII300
                                }))
            } else {
                viewHolder.mIconView.setImageDrawable(null)
            }
        }
        viewHolder.mDividerView.visibility = if (field.hasDivider()) View.VISIBLE else ViewGroup.GONE
        onBindCustomView(viewHolder.mCustomView, field, payloads)
    }

    protected abstract fun onBindCustomView(customView: View, field: AbstractField)

    protected open fun onBindCustomView(customView: View, field: AbstractField, payloads: List<Any>) {
        onBindCustomView(customView, field)
    }

    @LayoutRes
    protected abstract fun onInflateCustomView(): Int

    companion object {

        private val EMPTY_PAYLOADS = emptyList<Any>()
    }

}
