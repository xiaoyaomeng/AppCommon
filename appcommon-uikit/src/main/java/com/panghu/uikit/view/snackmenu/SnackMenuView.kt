package com.panghu.uikit.view.snackmenu;

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.panghu.uikit.R
import com.panghu.uikit.utils.DisplayUtil
import com.glip.widgets.button.FontIconButton
import com.glip.widgets.recyclerview.AbstractBaseAdapter
import com.glip.widgets.recyclerview.FullAdapter
import com.glip.widgets.recyclerview.OnItemClickListener
import kotlinx.android.synthetic.main.snake_menu_layout.view.*

/**
 * A custom layout that for display snack menu.
 * @author by panghu
 * @date 08/04/2020
 */
class SnackMenuView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val snackMenuAdapter: SnackMenuAdapter
    private val fullAdapter: FullAdapter

    private var menuItems = listOf<SnackMenuItem>()
        set(value) {
            field = value

            snackMenuAdapter.notifyDataSetChanged()
        }

    var onSnackMenuItemClickListener: OnSnackMenuItemClickListener? = null
    var onSnackMenuCallback: OnSnackMenuCallback? = null
    var maxItemCount: Int = 0

    init {
        inflate(
            context,
            R.layout.snake_menu_layout, this
        )

        closeBtn.setOnClickListener {
            onSnackMenuCallback?.onCloseMenu()
        }

        snackMenuAdapter = SnackMenuAdapter()
            .apply {
                setOnItemClickListener(OnItemClickListener { _, position ->
                    onSnackMenuItemClickListener?.onItemClick(
                        getItem(position)
                    )
                })
            }

        fullAdapter = FullAdapter(snackMenuAdapter)
        initRecyclerView()
        updateMaxItemCount()
    }

    fun updateMenu(
        menuItems: List<SnackMenuItem>,
        hasMore: Boolean = false
    ) {
        this.menuItems = menuItems

        if (hasMore) {
            val footerView = buildMoreView().apply {
                setOnClickListener {
                    onSnackMenuCallback?.onOpenSubMenu()
                }
            }

            fullAdapter.addFooterView(footerView)
        } else {
            fullAdapter.clearFooterViews()
        }
    }

    private fun buildMoreView(): View {
        val itemView = (LayoutInflater.from(context)
            .inflate(R.layout.snake_menu_item, snackRecyclerView, false))

        return itemView.findViewById<FontIconButton>(R.id.icon).apply {
            setText(R.string.icon_call_actions)
            contentDescription = context.getString(R.string.accessibility_more)
            setTextColor(getColor(context))
            background = getBackgroundDrawable(context)
        }
    }

    /**
     * Cal max item count
     */
    private fun updateMaxItemCount() {
        val iconView =  LayoutInflater.from(context)
            .inflate(R.layout.snake_menu_item , snackRecyclerView, false)
        val itemView = FrameLayout(context).apply {
            addView(iconView)
            measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
        }
        val itemWidth = itemView.measuredWidth
        if (itemWidth == 0) {
            return
        }

        measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
        maxItemCount = (DisplayUtil.getScreenWidth(context) - measuredWidth) / itemWidth
    }

    private fun initRecyclerView() {
        with(snackRecyclerView) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            isNestedScrollingEnabled = false
            adapter = fullAdapter
        }
    }

    private inner class SnackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView: FontIconButton = itemView.findViewById(R.id.icon)

        fun bindView(snackMenuItem: SnackMenuItem) {
            val context = itemView.context
            with(titleView) {
                setText(snackMenuItem.iconRes)
                setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    context.resources.getDimensionPixelSize(snackMenuItem.sizeRes).toFloat()
                )
                setTextColor(getColor(context))
                background = getBackgroundDrawable(context)
                isSelected = snackMenuItem.isAlert
                contentDescription = snackMenuItem.text
            }
        }
    }

    private inner class SnackMenuAdapter :
        AbstractBaseAdapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SnackViewHolder {
            return SnackViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.snake_menu_item, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return menuItems.size
        }

        override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
            super.onBindViewHolder(viewHolder, position)
            (viewHolder as? SnackViewHolder)?.bindView(menuItems[position])
        }

        fun getItem(position: Int): SnackMenuItem {
            return menuItems[position]
        }

    }

    companion object {
        private const val BACKGROUND_ALPHA_VALUE = 0.06f

        private fun getColor(context: Context): ColorStateList? {
            return ResourcesCompat.getColorStateList(
                context.resources,
                R.color.color_snack_menu_icon,
                null
            )
        }

        private fun getBackgroundDrawable(context: Context): Drawable? {
            return getColor(context)?.let {
                getStateListDrawable(context, it.withAlpha((255 * BACKGROUND_ALPHA_VALUE).toInt()))
            }
        }

        private fun getStateListDrawable(
            context: Context,
            colorStateList: ColorStateList
        ): Drawable {
            val drawable = StateListDrawable()
            val defaultStateSet =
                intArrayOf(-android.R.attr.state_selected, -android.R.attr.state_pressed)
            val unselectedStateSet = intArrayOf(-android.R.attr.state_selected)
            val selectedStateSet = intArrayOf(android.R.attr.state_selected)

            val rippleColor = ContextCompat.getColor(context, R.color.colorPaletteRippleOnColorful)

            val rippleColors = ColorStateList(
                arrayOf(defaultStateSet, unselectedStateSet, selectedStateSet),
                intArrayOf(rippleColor, rippleColor, rippleColor)
            )
            val mask = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(Color.WHITE)
            }

            arrayListOf(defaultStateSet, unselectedStateSet, selectedStateSet).forEach { stateSet ->
                val color = colorStateList.getColorForState(stateSet, colorStateList.defaultColor)
                val shapeDrawable = GradientDrawable().apply {
                    shape = GradientDrawable.OVAL
                    setColor(color)
                }
                drawable.addState(stateSet, shapeDrawable)
            }
            return RippleDrawable(rippleColors, drawable, mask)
        }
    }
}