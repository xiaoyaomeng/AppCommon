package com.panghu.uikit.view.spinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.glip.widgets.utils.setAccessibilityDelegateCompat

/**
 * @author by panghu
 * @date 2019-07-08
 */
class SpinnerAdapter<T>(
    context: Context,
    @LayoutRes val resourceId: Int,
    @IdRes val textViewId: Int,
    private val filterSpinner: IFilterSpinnerAdapter<T, SpinnerViewHolder>
) : ArrayAdapter<T>(context, resourceId, textViewId, mutableListOf<T>().apply {
    repeat(filterSpinner.getItemCount()) {
        add(filterSpinner.getItem(it))
    }
}) {
    private val inflater = LayoutInflater.from(context)

    @LayoutRes
    private var dropDownResourceId: Int = resourceId

    override fun setDropDownViewResource(resource: Int) {
        super.setDropDownViewResource(resource)
        dropDownResourceId = resource
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val contentView =
            view ?: inflater.inflate(resourceId, parent, false).apply {
                tag = filterSpinner.onCreateDisplayViewHolder(this)
            }

        val viewHolder = (contentView.tag as SpinnerViewHolder).apply {
            this.position = position
        }
        filterSpinner.onBindDisplayViewHolder(viewHolder, position)

        return contentView
    }

    override fun getDropDownView(position: Int, view: View?, parent: ViewGroup): View {
        val contentView =
            view ?: inflater.inflate(dropDownResourceId, parent, false).apply {
                tag = filterSpinner.onCreateDropdownViewHolder(this).apply {
                    this?.position = position
                }
            }

        val viewHolder = (contentView.tag as SpinnerViewHolder).apply {
            this.position = position
        }
        filterSpinner.onBindDropdownViewHolder(viewHolder, position)

        return contentView
    }

}