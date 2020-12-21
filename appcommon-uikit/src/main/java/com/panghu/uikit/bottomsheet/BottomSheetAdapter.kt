package com.panghu.uikit.bottomsheet

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.panghu.uikit.R

class BottomSheetAdapter(
    private val models: List<BottomItemModel>,
    private val callback: (itemModel: BottomItemModel) -> Unit
) : RecyclerView.Adapter<BottomSheetItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetItemViewHolder {
        return BottomSheetItemViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.bottom_sheet_image_text_item, parent, false))
    }

    override fun getItemCount(): Int {
        return models.size
    }

    override fun onBindViewHolder(viewholder: BottomSheetItemViewHolder, position: Int) {
        viewholder.bindData(models[position])
        viewholder.itemView.setOnClickListener {
            callback.invoke(models[position])
        }
    }
}
