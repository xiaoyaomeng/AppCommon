package com.panghu.uikit.view

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.ProgressBar

/**
 * This class is used in RecyclerView to make sure Progressbar's animation work well.
 * when RecyclerView notifyItemChange, it will attach and detach item view from parent.
 * meanwhile, setting ProgressBar's visibility does not trigger onVisibilityAggregated sometimes.
 * then the property mAggregatedIsVisible status is not right.
 * This will leads to ProgressBar's animation not starting for next visibility change.
 * when item is detached from RecyclerView, set the ProgressBar's aggregated visibility.
 */
class RecyclerViewItemProgressBar : ProgressBar {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onDetachedFromWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            onVisibilityAggregated(false)
        }
        super.onDetachedFromWindow()
    }
}