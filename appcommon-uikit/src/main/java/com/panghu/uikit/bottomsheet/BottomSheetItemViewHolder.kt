package com.panghu.uikit.bottomsheet

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.panghu.uikit.R

class BottomSheetItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val container = view.findViewById<LinearLayout>(R.id.bottom_item_container)
    private val iconView = view.findViewById<ImageView>(R.id.imageView)
    private val textView = view.findViewById<TextView>(R.id.textView)
    fun bindData(model: BottomItemModel) {
        container.isSelected = model.isSelected
        iconView.setImageDrawable(model.getIcon(iconView.context))
        if (model.textRes == 0) {
            textView.text = model.text
        } else {
            textView.setText(model.textRes)
        }
        iconView.contentDescription = textView.text
    }
}