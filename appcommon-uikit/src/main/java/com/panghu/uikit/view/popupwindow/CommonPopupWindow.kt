package com.panghu.uikit.view.popupwindow

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.*
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.PopupWindow
import androidx.annotation.LayoutRes
import androidx.core.widget.PopupWindowCompat
import com.panghu.uikit.utils.DisplayUtil
import com.panghu.uikit.utils.activity
import kotlin.math.min

/**
 *
 *  A Common popup window that can popup at anchor view's Left/Start, Top, Right/End, Bottom;
 *  @author by panghu
 *  @date 23/03/2020
 *
 */
class CommonPopupWindow private constructor(var context: Context) :
    PopupWindow.OnDismissListener {

    @LayoutRes
    private var resLayoutId = -1
    private var customView: View? = null
    private var widthMode = 0
    private var heightMode = 0
    private var touchX = 0
    private var touchY = 0
    private var focusable = true
    private var overlapAnchor = true
    private var touchable = true
    private var clippingEnable = true
    private var outsideTouchable = true
    private var darkEnable = false
    private var darkAlphaValue = 0f
    private var animationStyle = -1
    private var popupWindow: PopupWindow = PopupWindow()
    private var window: Window? = null
    private var onDismissListener: PopupWindow.OnDismissListener? = null
    private var anchorView: View? = null
    private var anchorXoff: Int = 0
    private var anchorYoff: Int = 0

    @XGravity
    private var xGravity = XGravity.RIGHT

    @YGravity
    private var yGravity = YGravity.TOP

    private fun build(): PopupWindow {
        setCustomView()
        setConfig()
        setOutsideTouchable()
        setDark()
        setAnimationStyle()
        popupWindow.update()
        return popupWindow
    }

    fun updateCustomView(customView: View) {
        this.customView = customView
        popupWindow.contentView = customView
        initCustomViewAndWH()
        popupWindow.update()
    }

    fun updateTouchPosition(touchX: Int, touchY: Int) {
        this.touchX = touchX
        this.touchY = touchY
    }

    private fun setCustomView() {
        if (customView == null) {
            if (resLayoutId != 0) {
                customView = LayoutInflater.from(context).inflate(resLayoutId, null)
            } else {
                throw IllegalArgumentException("The content view is null")
            }
        }

        customView?.let {
            popupWindow.contentView = it
            initCustomViewAndWH()
        }
    }

    private fun initCustomViewAndWH() {
        var measureWidth = 0
        var measureHeight = 0
        if (widthMode <= 0 || heightMode <= 0) {
            customView?.let {
                val widthSpec = View.MeasureSpec.makeMeasureSpec(
                    DisplayUtil.getScreenWidth(context),
                    View.MeasureSpec.AT_MOST
                )
                val heightSpec = View.MeasureSpec.makeMeasureSpec(
                    DisplayUtil.getScreenHeight(context),
                    View.MeasureSpec.AT_MOST
                )
                it.measure(widthSpec, heightSpec)
                measureWidth = min(it.measuredWidth, DisplayUtil.getScreenWidth(context))
                measureHeight = min(it.measuredHeight, DisplayUtil.getScreenHeight(context))
            }
        }

        popupWindow.width = when {
            widthMode < 0 -> {
                widthMode
            }
            measureWidth >= DisplayUtil.getScreenWidth(context) -> {
                ViewGroup.LayoutParams.MATCH_PARENT
            }
            else -> {
                measureWidth
            }
        }

        popupWindow.height = when {
            heightMode < 0 -> {
                heightMode
            }
            measureHeight >= DisplayUtil.getScreenHeight(context) -> {
                ViewGroup.LayoutParams.MATCH_PARENT
            }
            else -> {
                measureHeight
            }
        }
    }

    private fun setConfig() {
        with(popupWindow) {
            isTouchable = touchable
            isFocusable = focusable
            isClippingEnabled = clippingEnable
            setOnDismissListener(this@CommonPopupWindow)
        }
    }

    private fun setOutsideTouchable() {
        with(popupWindow) {
            if (outsideTouchable) {
                isFocusable = focusable
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                isOutsideTouchable = outsideTouchable
            } else {
                isFocusable = true
                isOutsideTouchable = outsideTouchable
                setBackgroundDrawable(null)
                contentView.isFocusable = true
                contentView.isFocusableInTouchMode = true
                contentView
                    .setOnKeyListener(View.OnKeyListener { _, keyCode, _ ->
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            dismiss()
                            return@OnKeyListener true
                        }
                        false
                    })
                setTouchInterceptor(OnTouchListener { _, event ->
                    val x = event.x.toInt()
                    val y = event.y.toInt()
                    if (event.action == MotionEvent.ACTION_DOWN
                        && (x < 0 || x >= width || y < 0 || y >= height)
                    ) {
                        return@OnTouchListener true
                    } else if (event.action == MotionEvent.ACTION_OUTSIDE) {
                        return@OnTouchListener true
                    }
                    false
                })
            }
        }
    }

    private fun setDark() {
        if (darkEnable) {
            val alpha =
                if (darkAlphaValue > 0 && darkAlphaValue < 1) darkAlphaValue else DEFAULT_ALPHA
            window = context.activity()?.window?.apply {
                val params = attributes
                params.alpha = alpha

                addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                attributes = params
            }
        }
    }

    private fun clearDark() {
        window?.let {
            val params = it.attributes
            params.alpha = 1.0f

            it.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            it.attributes = params
        }
    }

    private fun setAnimationStyle() {
        if (animationStyle != -1) {
            popupWindow.animationStyle = animationStyle
        }
    }

    fun isShowing(): Boolean {
        return popupWindow.isShowing
    }

    /**
     * Display the content view in a popup window anchored to
     * the anchor view offset by the specified x and y coordinates.
     *
     * @param anchor the view on which to pin the popup window
     * @param xoff A horizontal offset from the anchor in pixels
     * @param yoff A vertical offset from the anchor in pixels
     * @param xGravity Alignment of the popup x position relative to the anchor
     * @param yGravity Alignment of the popup y position relative to the anchor
     *
     * @see #dismiss()
     */
    @JvmOverloads
    fun showAtAnchorView(
        anchor: View,
        xOff: Int = 0,
        yOff: Int = 0,
        @XGravity xGravity: Int = XGravity.RIGHT,
        @YGravity yGravity: Int = YGravity.TOP
    ): CommonPopupWindow {
        this.anchorView = anchor
        this.anchorXoff = xOff
        this.anchorYoff = yOff
        this.xGravity = xGravity
        this.yGravity = yGravity

        val contentMenu = popupWindow.contentView
        contentMenu?.viewTreeObserver
            ?.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    contentMenu.viewTreeObserver
                        .removeOnGlobalLayoutListener(this)

                    with(contentMenu) {
                        //Only when pre measured width < screen width, will request real width
                        if (popupWindow.width < DisplayUtil.getScreenWidth(context)) {
                            popupWindow.width = width
                        }
                        //Only when pre measured height < screen height, will request real height
                        if (popupWindow.height < DisplayUtil.getScreenHeight(context)) {
                            popupWindow.height = height
                        }
                    }
                    showOrUpdatePopupWindow(anchor, xOff, yOff, xGravity, yGravity)
                }
            })
        showOrUpdatePopupWindow(anchor, xOff, yOff, xGravity, yGravity)
        return this
    }

    private fun showOrUpdatePopupWindow(
        anchor: View,
        xOff: Int,
        yOff: Int,
        @XGravity xGravity: Int,
        @YGravity yGravity: Int
    ) {
        val offsetX = calculateX(anchor, xOff, xGravity)
        val offsetY = calculateY(anchor, yOff, yGravity)


        //Below Android M, the showAsDropDown show position may be incorrect
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            val location = IntArray(2)
            anchor.getLocationOnScreen(location)
            popupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, offsetX, location[1] + anchor.height + offsetY)
        } else {
            if (popupWindow.isShowing) {
                popupWindow.update(anchor, offsetX, offsetY, popupWindow.width, popupWindow.height)
            } else {
                PopupWindowCompat.showAsDropDown(
                    popupWindow,
                    anchor,
                    offsetX,
                    offsetY,
                    Gravity.NO_GRAVITY
                )
            }
        }

    }

    private fun calculateX(
        anchor: View,
        xOff: Int,
        @XGravity xGravity: Int
    ): Int {
        var offsetX = when (xGravity) {
            XGravity.LEFT -> {
                if (touchX > 0) {
                    xOff + touchX - popupWindow.width
                } else {
                    xOff - popupWindow.width
                }
            }
            XGravity.RIGHT -> {
                if (touchX > 0) {
                    xOff + touchX
                } else {
                    xOff + anchor.width
                }
            }
            XGravity.CENTER -> {
                if (touchX > 0) {
                    xOff + touchX - popupWindow.width / 2
                } else {
                    xOff + anchor.width / 2 - popupWindow.width / 2
                }
            }
            XGravity.ALIGN_LEFT -> {
                if (touchX > 0) {
                    xOff + touchX
                } else {
                    xOff
                }
            }
            XGravity.ALIGN_RIGHT -> {
                if (touchX > 0) {
                    xOff - popupWindow.width + touchX
                } else {
                    xOff - popupWindow.width + anchor.width
                }
            }
            else -> xOff
        }
        val location = IntArray(2)
        anchor.getLocationOnScreen(location)

        if (isOutOfScreenLeft(location[0], offsetX)) {
            offsetX = 0
        }

        return offsetX
    }

    private fun calculateY(
        anchor: View,
        yOff: Int,
        @YGravity yGravity: Int
    ): Int {

        var offsetY = when (yGravity) {
            YGravity.TOP -> {
                if (touchY > 0) {
                    yOff - (anchor.height - touchY) - popupWindow.height
                } else {
                    yOff - anchor.height - popupWindow.height
                }
            }
            YGravity.BOTTOM -> {
                if (touchY > 0) {
                    yOff - (anchor.height - touchY)
                } else {
                    yOff
                }
            }
            YGravity.CENTER -> {
                if (touchY > 0) {
                    yOff - (anchor.height - touchY) - popupWindow.height / 2
                } else {
                    yOff - anchor.height / 2 - popupWindow.height / 2
                }
            }
            YGravity.ALIGN_TOP -> {
                if (touchY > 0) {
                    yOff - (anchor.height - touchY)
                } else {
                    yOff - anchor.height
                }
            }
            YGravity.ALIGN_BOTTOM -> {
                if (touchY > 0) {
                    yOff - (anchor.height - touchY) - popupWindow.height
                } else {
                    yOff - popupWindow.height
                }
            }
            else -> yOff
        }

        val location = IntArray(2)
        anchor.getLocationOnScreen(location)

        //Check the anchor is out of screen top
        if (isOutOfScreenTop(anchor.height, location[1], offsetY)
            && (yGravity == YGravity.TOP
                    || yGravity == YGravity.CENTER)
        ) {
            offsetY = if (overlapAnchor) {
                -((location[1] - DisplayUtil.getStatusBarHeight(context))) - anchor.height
            } else {
                0
            }
        }

        //Check the anchor is out of screen bottom
        if (isOutOfScreenBottom(anchor.height, location[1], offsetY)) {
            offsetY = if (overlapAnchor) {
                //Check the anchor view has space
                val outsideScreenHeight =
                    ((location[1] + anchor.height) - DisplayUtil.getScreenHeight(context))
                -popupWindow.height - outsideScreenHeight
            } else {
                //Check the top has space
                val topOffsetY = -popupWindow.height - anchor.height
                if (!isOutOfScreenTop(anchor.height, location[1], topOffsetY)) {
                    topOffsetY
                } else {
                    -popupWindow.height
                }
            }
        }

        return offsetY
    }

    private fun isOutOfScreenLeft(anchorLocationX: Int, offsetX: Int): Boolean {
        return anchorLocationX + offsetX <= 0
    }

    private fun isOutOfScreenTop(anchorHeight: Int, anchorLocationY: Int, offsetY: Int): Boolean {
        return anchorLocationY + anchorHeight + offsetY < DisplayUtil.getStatusBarHeight(context)
    }

    private fun isOutOfScreenBottom(
        anchorHeight: Int,
        anchorLocationY: Int,
        offsetY: Int
    ): Boolean {
        return anchorHeight + popupWindow.height + DisplayUtil.getStatusBarHeight(context) + anchorLocationY + offsetY >= DisplayUtil.getScreenHeight(
            context
        )
    }

    override fun onDismiss() {
        dismiss()
    }

    fun dismiss() {
        onDismissListener?.onDismiss()

        clearDark()
        if (popupWindow.isShowing) {
            popupWindow.dismiss()
        }
    }

    class Builder(context: Context) {
        private val commonPopupWindow: CommonPopupWindow = CommonPopupWindow(context)

        fun setView(@LayoutRes resLayoutId: Int): Builder {
            commonPopupWindow.resLayoutId = resLayoutId
            commonPopupWindow.customView = null
            return this
        }

        fun setView(view: View): Builder {
            if (view.parent != null) {
                ((view.parent) as ViewGroup).removeView(view)
            }
            commonPopupWindow.customView = view
            commonPopupWindow.resLayoutId = -1
            return this
        }

        fun setSize(width: Int, height: Int): Builder {
            commonPopupWindow.widthMode = width
            commonPopupWindow.heightMode = height
            return this
        }

        fun setTouchPosition(touchX: Int, touchY: Int): Builder {
            commonPopupWindow.touchX = touchX
            commonPopupWindow.touchY = touchY
            return this
        }

        fun setOverlapAnchor(overlapAnchor: Boolean): Builder {
            commonPopupWindow.overlapAnchor = overlapAnchor
            return this
        }

        fun setFocusable(focusable: Boolean): Builder {
            commonPopupWindow.focusable = focusable
            return this
        }

        fun setTouchable(touchable: Boolean): Builder {
            commonPopupWindow.touchable = touchable
            return this
        }

        fun setClippingEnable(clippingEnable: Boolean): Builder {
            commonPopupWindow.clippingEnable = clippingEnable
            return this
        }

        fun setOutsideTouchable(outsideTouchable: Boolean): Builder {
            commonPopupWindow.outsideTouchable = outsideTouchable
            return this
        }

        fun setDarkEnable(darkEnable: Boolean): Builder {
            commonPopupWindow.darkEnable = darkEnable
            return this
        }

        fun setDarkAlphaValue(darkAlphaValue: Float): Builder {
            commonPopupWindow.darkAlphaValue = darkAlphaValue
            return this
        }

        fun setAnimationStyle(animationStyle: Int): Builder {
            commonPopupWindow.animationStyle = animationStyle
            return this
        }

        fun setOnDismissListener(onDismissListener: PopupWindow.OnDismissListener): Builder {
            commonPopupWindow.onDismissListener = onDismissListener
            return this
        }

        fun create(): CommonPopupWindow {
            commonPopupWindow.build()
            return commonPopupWindow
        }

    }

    companion object {
        private const val DEFAULT_ALPHA = 0.7f
    }
}