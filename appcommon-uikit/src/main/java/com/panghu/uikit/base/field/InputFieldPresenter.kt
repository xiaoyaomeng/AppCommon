package com.panghu.uikit.base.field

import android.view.View
import android.widget.TextView
import com.panghu.uikit.R

/**
 * @author locke.peng on 4/27/18.
 */
class InputFieldPresenter : AbstractFieldPresenter() {

    override fun onBindCustomView(customView: View, field: AbstractField) {
        val inputField = field as InputField
        val titleTextView = customView.findViewById<TextView>(R.id.title_view)
        titleTextView.setText(inputField.titleResId)
        val summaryTextView = customView.findViewById<TextView>(R.id.summary_view)
        summaryTextView.text = inputField.text
        if (inputField.hintResId > 0) {
            summaryTextView.setHint(inputField.hintResId)
        }
        customView.setFieldContentDescription(
            titleTextView.text, summaryTextView.text, summaryTextView.hint
        )
    }

    override fun onInflateCustomView(): Int {
        return R.layout.common_field_custom_option_view
    }

    private fun View.setFieldContentDescription(
        title: CharSequence?,
        summary: CharSequence?,
        summaryHint: CharSequence?
    ) {
        contentDescription = if (summary.isNullOrEmpty()) {
            if (summaryHint.isNullOrEmpty()) {
                title
            } else {
                "$title, $summaryHint"
            }
        } else {
            "$title, $summary"
        }
    }
}