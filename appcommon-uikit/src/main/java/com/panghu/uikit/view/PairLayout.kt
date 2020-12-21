package com.panghu.uikit.view

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.LayoutRes
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.panghu.uikit.R
import com.panghu.uikit.utils.ResourceUtil

/**
 * A layout for maintaining one or two subviews.
 *
 * <p>when screen is vertical or is a tablet device, two views will be arranged vertically.
 * <p>When screen is horizontal and a phone device, two views will be arranged horizontally.
 *
 * @author justin.wu
 * @date 10/24/2018
 */
class PairLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "PairLayout"
        private const val MAX_SCALE = 0.2f
    }

    var verticalPadding: Int = 0
    var horizontalPadding: Int = 0
    var secondContainerLeftPadding: Int = 0
    var secondContainerRightPadding: Int = 0

    var firstContainer: FrameLayout
    var secondContainer: FrameLayout

    private var firstLayoutResId: Int = 0
    private var secondLayoutResId: Int = 0

    init {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.PairLayout)
            try {
                verticalPadding =
                    ta.getDimensionPixelSize(R.styleable.PairLayout_vertical_divider_padding, 0)
                horizontalPadding =
                    ta.getDimensionPixelSize(R.styleable.PairLayout_horizontal_divider_padding, 0)

                firstLayoutResId = ta.getResourceId(R.styleable.PairLayout_first_layout, 0)
                secondLayoutResId = ta.getResourceId(R.styleable.PairLayout_second_layout, 0)
            } finally {
                ta.recycle()
            }
        }

        if (childCount > 0) {
            throw RuntimeException("Should not add child view in PairLayout.")
        }

        firstContainer = FrameLayout(this.context)
        secondContainer = FrameLayout(this.context)

        addView(firstContainer)
        addView(secondContainer)

        setFirstLayout(firstLayoutResId)
        setSecondLayout(secondLayoutResId)

        setOrientation(resources.configuration)
        gravity = Gravity.CENTER
    }

    fun setFirstLayout(@LayoutRes layoutResId: Int) {
        if (firstContainer.childCount > 0) {
            firstContainer.removeAllViews()
        }
        if (layoutResId == 0) {
            firstContainer.visibility = View.GONE
        } else {
            firstContainer.visibility = View.VISIBLE
            View.inflate(context, layoutResId, firstContainer)
        }
        requestLayout()
    }

    fun setFirstView(view: View?) {
        if (firstContainer.childCount > 0) {
            firstContainer.removeAllViews()
        }
        if (view == null) {
            firstContainer.visibility = View.GONE
        } else {
            firstContainer.visibility = View.VISIBLE
            firstContainer.addView(view)
        }
        requestLayout()
    }

    fun removeFirstView() {
        setFirstLayout(0)
    }

    fun setSecondLayout(@LayoutRes layoutResId: Int) {
        if (secondContainer.childCount > 0) {
            secondContainer.removeAllViews()
        }
        if (layoutResId == 0) {
            secondContainer.visibility = View.GONE
        } else {
            secondContainer.visibility = View.VISIBLE
            View.inflate(context, layoutResId, secondContainer)
        }
        requestLayout()
    }

    fun setSecondView(view: View?) {
        if (secondContainer.childCount > 0) {
            secondContainer.removeAllViews()
        }
        if (view == null) {
            secondContainer.visibility = View.GONE
        } else {
            secondContainer.visibility = View.VISIBLE
            secondContainer.addView(view)
        }
        requestLayout()
    }

    fun removeSecondView() {
        setSecondLayout(0)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (orientation == VERTICAL) {
            val contentHeight = MeasureSpec.getSize(heightMeasureSpec) - paddingTop - paddingBottom

            updateLayoutParams(true)

            val firstLayoutHeight = getChildViewMeasuredHeight(
                firstContainer,
                widthMeasureSpec, heightMeasureSpec
            )
            val secondLayoutHeight = getChildViewMeasuredHeight(
                secondContainer,
                widthMeasureSpec, heightMeasureSpec
            )

            if (contentHeight < firstLayoutHeight + secondLayoutHeight) {
                val newImageLayoutHeight = Math.max(
                    firstLayoutHeight * MAX_SCALE,
                    (contentHeight - secondLayoutHeight).toFloat()
                ).toInt()
                if (newImageLayoutHeight + secondLayoutHeight <= contentHeight) {
                    val lp = firstContainer.layoutParams
                    lp.height = newImageLayoutHeight
                    firstContainer.layoutParams = lp
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setOrientation(newConfig)
    }

    private fun getChildViewMeasuredHeight(
        childView: View,
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ): Int {
        return if (childView.visibility == View.VISIBLE) {
            measureChild(childView, widthMeasureSpec, heightMeasureSpec)
            childView.measuredHeight
        } else {
            0
        }
    }

    private fun setOrientation(newConfig: Configuration) {
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
            && !ResourceUtil.isTablet(context)
        ) {
            orientation = HORIZONTAL
            updateLayoutParams(false)
        } else {
            orientation = VERTICAL
            updateLayoutParams(true)
        }
    }

    private fun updateLayoutParams(vertical: Boolean) {
        if (vertical) {
            updateContainerView(firstContainer, false)
            updateContainerView(secondContainer, false)
            val verticalPadding =
                if (firstContainer.visibility == View.GONE) 0 else this.verticalPadding
            secondContainer.setPadding(
                secondContainerLeftPadding,
                verticalPadding,
                secondContainerRightPadding,
                0
            )
        } else {
            updateContainerView(firstContainer, true)
            updateContainerView(secondContainer, true)
            val horizontalPadding =
                if (firstContainer.visibility == View.GONE) 0 else this.horizontalPadding
            secondContainer.setPadding(horizontalPadding, 0, secondContainerRightPadding, 0)
        }
    }

    private fun updateContainerView(view: FrameLayout, isHalfWith: Boolean) {
        val layoutParams = generateDefaultLayoutParams()
        if (isHalfWith) {
            layoutParams.weight = 1f
            layoutParams.width = 0
        } else {
            layoutParams.weight = 0f
            layoutParams.width = LayoutParams.MATCH_PARENT
        }
        view.layoutParams = layoutParams
    }
}