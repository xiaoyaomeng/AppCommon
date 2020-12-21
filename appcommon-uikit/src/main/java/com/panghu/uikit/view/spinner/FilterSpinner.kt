package com.panghu.uikit.view.spinner

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.widget.RCSpinner
import com.panghu.uikit.R

/**
 * An Filter spinner support show text with icon and count.
 * @author by panghu
 * @date 2019-07-04
 */
class FilterSpinner @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.spinnerStyle,
    mode: Int = MODE_DROPDOWN
) : RCSpinner(context, attrs, defStyleAttr, mode) {
    private var filterSpinnerAdapter: DefaultSpinnerAdapter? = null

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.FilterSpinner)
        val accessibilitySuffix =
            typedArray.getString(R.styleable.FilterSpinner_accessibilitySuffix) ?: ""
        val icons = typedArray.getTextArray(R.styleable.FilterSpinner_fontIcons)
        val entries = typedArray.getTextArray(R.styleable.FilterSpinner_android_entries)
        val values = typedArray.getTextArray(R.styleable.FilterSpinner_android_entryValues)
        typedArray.recycle()

        if (entries != null && values != null) {
            setFilterData(entries, values, icons, accessibilitySuffix)
        }
    }

    fun setFilterData(
        entries: Array<CharSequence>,
        values: Array<CharSequence>,
        icons: Array<CharSequence>? = null,
        accessibilitySuffix: String = ""
    ) {
        val items =
            if (entries.isEmpty() || entries.size != values.size) {
                arrayListOf()
            } else {
                entries.mapIndexed { index, charSequence ->
                    if (icons != null && icons.size == values.size) {
                        SpinnerItem(charSequence, values[index], icons[index])
                    } else {
                        SpinnerItem(charSequence, values[index])
                    }
                }
            }

        if (items.isNotEmpty()) {
            filterSpinnerAdapter = DefaultSpinnerAdapter(items, accessibilitySuffix).also {
                val spinnerAdapter = SpinnerAdapter(
                    context,
                    R.layout.filter_spinner_simple_view,
                    R.id.filterTextView,
                    it
                )
                spinnerAdapter.setDropDownViewResource(R.layout.filter_spinner_drop_down_view)
                adapter = spinnerAdapter
            }
        }
    }

    fun updateCount(key: String, count: Int) {
        filterSpinnerAdapter?.updateCount(key, count)
        (adapter as? BaseAdapter)?.notifyDataSetChanged()
    }

    override fun setSelection(position: Int) {
        if (selectedItemPosition == position) {
            onItemSelectedListener?.apply {
                onItemSelected(this@FilterSpinner, selectedView, position, selectedItemId)
                return
            }
        }
        super.setSelection(position)
    }

    inner class DefaultSpinnerAdapter(
        var items: List<SpinnerItem>,
        var accessibilitySuffix: String = ""
    ) :
        IFilterSpinnerAdapter<SpinnerItem, SpinnerViewHolder> {

        override fun onCreateDropdownViewHolder(itemView: View): SpinnerViewHolder? {
            return ItemDropdownViewHolder(itemView)
        }

        override fun onBindDropdownViewHolder(holder: SpinnerViewHolder?, position: Int) {
            val dropdownViewHolder = holder as? ItemDropdownViewHolder ?: return
            dropdownViewHolder.bindData(getItem(position))
        }

        override fun onCreateDisplayViewHolder(itemView: View): SpinnerViewHolder? {
            return ItemDisplayViewHolder(itemView)
        }

        override fun onBindDisplayViewHolder(holder: SpinnerViewHolder?, position: Int) {
            val displayViewHolder = holder as? ItemDisplayViewHolder ?: return
            displayViewHolder.bindData(getItem(position))
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun getItem(position: Int): SpinnerItem {
            return items[position]
        }

        fun updateCount(key: String, count: Int) {
            items.firstOrNull { it.value == key }?.also {
                it.count = count
            }
        }

        inner class ItemDisplayViewHolder(itemView: View) :
            SpinnerViewHolder(itemView) {
            private val titleTv: TextView = itemView.findViewById(R.id.filterTextView)

            init {
                titleTv.accessibilityDelegate = object : View.AccessibilityDelegate() {
                    override fun onInitializeAccessibilityNodeInfo(
                        host: View?,
                        info: AccessibilityNodeInfo?
                    ) {
                        super.onInitializeAccessibilityNodeInfo(host, info)

                        info?.contentDescription = "${info?.text} $accessibilitySuffix"
                    }
                }
            }

            fun bindData(data: SpinnerItem) {
                titleTv.text = data.title
            }
        }

        inner class ItemDropdownViewHolder(val itemView: View) :
            SpinnerViewHolder(itemView) {
            private val titleTv: TextView = itemView.findViewById(R.id.filterTextView)
            private val iconTv: TextView = itemView.findViewById(R.id.filterIconView)

            fun bindData(data: SpinnerItem) {
                if (data.count > 0) {
                    titleTv.text = itemView.context.getString(
                        R.string.title_with_badge,
                        data.title,
                        formatUnreadCountText(data.count.toLong())
                    )
                } else {
                    titleTv.text = data.title
                }

                if (data.fontIcon.isNullOrEmpty()) {
                    iconTv.visibility = View.GONE
                } else {
                    iconTv.text = data.fontIcon
                    iconTv.visibility = View.VISIBLE
                }
            }

            private fun formatUnreadCountText(count: Long): String {
                return when {
                    count > 99 -> "99+"
                    count > 0 -> count.toString()
                    else -> ""
                }
            }
        }

    }
}
