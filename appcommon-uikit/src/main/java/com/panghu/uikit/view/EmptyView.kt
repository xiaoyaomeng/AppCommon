package com.panghu.uikit.view

import android.content.Context
import android.content.res.Configuration
import android.content.res.TypedArray
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.widget.NestedScrollView
import com.panghu.uikit.R
import com.glip.widgets.utils.AccessibilityUtils


/**
 * Empty view contain a PairLayout to handle the orientation change.
 * First subview is an ImageView, set the image res by "app:image_src".
 * Second subview is an custom view, set the layout by "app:description_layout".
 *
 * For a common empty view, you can set "app:description_title" & "app:description_content" &
 * "app:description_button" and DO NOT set "app:description_layout" to use the common empty view.
 *
 * @author justin.wu
 * @date 10/24/2018
 */
open class EmptyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {

    private var verticalPadding: Int = 0
    private var horizontalPadding: Int = 0
    private var descriptionLeftPadding: Int = 0
    private var descriptionRightPadding: Int = 0

    private var container: PairLayout
    private var descriptionLayoutId: Int = 0
    private var imageView: ImageView? = null
    private var imageResId: Int = 0

    private var titleTextView: TextView? = null
    private var contentTextView: TextView? = null
    private var button: Button? = null

    var onVisibilityChangedListener: OnVisibilityChangedListener? = null

    init {
        var title: String? = null
        var content: String? = null
        var buttonContent: String? = null
        var imageSourceId = 0
        var isScrollable = false
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.EmptyView)
            var stdTa: TypedArray? = null
            try {
                // set default padding for EmptyView if not set
                val attributes = intArrayOf(
                    android.R.attr.paddingLeft,
                    android.R.attr.paddingTop,
                    android.R.attr.paddingBottom,
                    android.R.attr.paddingRight,
                    android.R.attr.padding
                )
                stdTa = context.obtainStyledAttributes(attrs, attributes)
                var isPaddingSet = false
                for (i in 0 until attributes.size) {
                    isPaddingSet = isPaddingSet or (stdTa.getDimensionPixelOffset(i, -1) != -1)
                }
                if (!isPaddingSet) {
                    val defPadding = resources.getDimension(R.dimen.empty_view_padding).toInt()
                    setPadding(defPadding, defPadding, defPadding, defPadding)
                }

                verticalPadding = ta.getDimensionPixelSize(
                    R.styleable.EmptyView_vertical_divider_padding,
                    resources.getDimension(R.dimen.dimen_24dp).toInt()
                )
                horizontalPadding =
                    ta.getDimensionPixelSize(R.styleable.EmptyView_horizontal_divider_padding, 0)
                descriptionLeftPadding =
                    ta.getDimensionPixelSize(R.styleable.EmptyView_description_left_padding, 0)
                descriptionRightPadding =
                    ta.getDimensionPixelSize(R.styleable.EmptyView_description_right_padding, 0)

                isScrollable = ta.getBoolean(R.styleable.EmptyView_scrollable, false)
                imageSourceId = ta.getResourceId(R.styleable.EmptyView_image_src, 0)
                descriptionLayoutId = ta.getResourceId(R.styleable.EmptyView_description_layout, 0)

                title = ta.getString(R.styleable.EmptyView_description_title)
                content = ta.getString(R.styleable.EmptyView_description_content)
                buttonContent = ta.getString(R.styleable.EmptyView_description_button)
            } finally {
                ta.recycle()
                stdTa?.recycle()
            }
        }

        if (descriptionLayoutId != 0
            && (!TextUtils.isEmpty(title)
                    || !TextUtils.isEmpty(content)
                    || !TextUtils.isEmpty(buttonContent))
        ) {
            throw RuntimeException("Should not set descriptionLayoutId and (title, content, button) at the same time")
        }

        container = PairLayout(context)
        container.layoutParams = this.generateDefaultLayoutParams()
        container.horizontalPadding = horizontalPadding
        container.verticalPadding = verticalPadding
        container.secondContainerLeftPadding = descriptionLeftPadding
        container.secondContainerRightPadding = descriptionRightPadding

        this.addView(container)

        setImageResource(imageSourceId)

        if (descriptionLayoutId == 0) {
            descriptionLayoutId = R.layout.empty_view_description_layout
            setDescriptionLayout(descriptionLayoutId)
            titleTextView = findViewById(R.id.empty_title_text)
            contentTextView = findViewById(R.id.empty_content_text)
            button = findViewById(R.id.empty_button)
            setTitle(title)
            setContent(content)
            setButton(buttonContent)
        } else {
            setDescriptionLayout(descriptionLayoutId)
        }
        visibility = View.GONE

        clipToPadding = false
        isFillViewport = true
        isNestedScrollingEnabled = isScrollable
    }

    fun setDescriptionLayout(@LayoutRes layoutId: Int) {
        container.setSecondLayout(layoutId)
        titleTextView = null
        contentTextView = null
        button = null
    }

    fun setImageResource(@DrawableRes imageResId: Int) {
        this.imageResId = imageResId
        if (imageView != null) {
            initImageView()
        }
    }

    override fun setVisibility(visibility: Int) {
        val oldVisibility = getVisibility()
        if (oldVisibility == visibility) {
            return
        }
        if (visibility == View.VISIBLE) {
            initImageView()
        }
        super.setVisibility(visibility)
        onVisibilityChangedListener?.onVisibilityChanged(this, oldVisibility, visibility)
    }

    override fun onHoverEvent(event: MotionEvent?): Boolean {
        if (AccessibilityUtils.isAccessibilityOn(context)) {
            return true
        }
        return super.onHoverEvent(event)
    }

    private fun initImageView() {
        if (imageResId != 0) {
            if (imageView == null) {
                imageView = LayoutInflater.from(context).inflate(
                    R.layout.empty_view_image_layout,
                    container.firstContainer, false
                ) as ImageView?
                container.setFirstView(imageView)
            }
            imageView?.setImageResource(imageResId)
        } else {
            container.removeFirstView()
            imageView = null
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if (imageView != null) {
            initImageView()
        }
    }

    fun setButton(@StringRes text: Int) {
        setViewText(button, text)
    }

    fun setButton(text: String?) {
        setViewText(button, text)
    }

    fun setContent(@StringRes content: Int) {
        setViewText(contentTextView, content)
    }

    fun setContent(content: String?) {
        setViewText(contentTextView, content)
    }

    fun setTitle(@StringRes title: Int) {
        setViewText(titleTextView, title)
    }

    fun setTitle(title: String?) {
        setViewText(titleTextView, title)
    }

    fun setContainerGravity(gravity: Int) {
        container.gravity = gravity
    }

    private fun setViewText(view: TextView?, @StringRes textRes: Int) {
        view?.let {
            it.visibility = if (textRes != 0) View.VISIBLE else View.GONE
            if (textRes != 0) {
                it.setText(textRes)
            }
        }
    }

    private fun setViewText(view: TextView?, text: String?) {
        view?.let {
            if (TextUtils.isEmpty(text)) {
                it.visibility = View.GONE
            } else {
                it.visibility = View.VISIBLE
                it.text = text
            }
        }
    }

    fun setButtonClickListener(clickListener: OnClickListener?) {
        button?.setOnClickListener(clickListener)
    }

    fun announceForAccessibility() {
        announceForAccessibility(
            titleTextView?.text
                ?: contentTextView?.text
                ?: contentDescription
        )
    }

    fun setTitleViewContentDescription(contentDescription: String) {
        titleTextView?.contentDescription = contentDescription
    }

    interface OnVisibilityChangedListener {
        fun onVisibilityChanged(view: EmptyView, oldVisibility: Int, newVisibility: Int)
    }
}