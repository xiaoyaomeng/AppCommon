package com.panghu.uikit.base.field

import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.panghu.uikit.R

/**
 * Created by panghu on 9/1/16.
 */
open class SwitchFieldPresenter(@LayoutRes layoutResId: Int = R.layout.common_field_view) :
    AbstractFieldPresenter(layoutResId) {
    private var summaryView: TextView? = null

    override fun onBindCustomView(customView: View, field: AbstractField) {
        if (field !is SwitchField) {
            return
        }
        customView.findViewById<Switch>(R.id.switch_view).apply {
            isClickable = false
            isChecked = field.switch
            importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
            isEnabled = !field.isLocked
        }
        customView.findViewById<TextView>(R.id.title_text).apply {
            text = field.title
            isEnabled = !field.isLocked
        }
        summaryView = customView.findViewById<TextView>(R.id.summary_text).apply {
            text = field.summary ?: ""
            visibility = if (field.summary.isNullOrEmpty() || !field.switch) {
                View.GONE
            } else {
                View.VISIBLE
            }
            isEnabled = !field.isLocked
        }
        customView.setFieldContentDescription(
            field.title,
            field.summary,
            field.switch,
            field.isLocked
        )
    }

    override fun onBindCustomView(customView: View, field: AbstractField, payloads: List<Any>) {
        if (field !is SwitchField) {
            return
        }
        payloads.firstOrNull { it is Boolean }?.let {
            customView.findViewById<Switch>(R.id.switch_view).apply {
                isClickable = false
                isChecked = it as Boolean
                importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
            }
            customView.setFieldContentDescription(
                field.title,
                field.summary,
                it as Boolean,
                field.isLocked
            )
        } ?: onBindCustomView(customView, field)
    }

    fun toggleSummaryVisible(isVisible: Boolean) {
        summaryView?.let {
            it.visibility = if (isVisible && it.text.isNotEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    override fun onInflateCustomView(): Int {
        return R.layout.common_field_custom_switch_view
    }

    private fun View.setFieldContentDescription(
        title: String,
        summary: String?,
        isOn: Boolean,
        isLocked: Boolean
    ) {
        val isSummaryVisible =
            summary?.let { it.isNotEmpty() && summaryView?.visibility == View.VISIBLE } ?: false
        contentDescription = if (isSummaryVisible) {
            resources.getString(
                if (isOn) {
                    R.string.summary_item_with_toggle_on_description
                } else {
                    R.string.summary_item_with_toggle_off_description
                }, title, summary
            )
        } else {
            resources.getString(
                if (isOn) {
                    R.string.single_item_with_toggle_on_description
                } else {
                    R.string.single_item_with_toggle_off_description
                }, title
            )
        }
        if (isLocked) {
            contentDescription =
                resources.getString(R.string.accessibility_item_disable, contentDescription)
        }
    }
}
