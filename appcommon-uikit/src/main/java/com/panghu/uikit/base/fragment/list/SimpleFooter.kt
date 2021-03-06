package com.panghu.uikit.base.fragment.list

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.panghu.uikit.R
import com.scwang.smartrefresh.layout.api.RefreshFooter
import com.scwang.smartrefresh.layout.api.RefreshKernel
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.constant.SpinnerStyle

/**
 * @author by panghu
 * @date 27/03/2018
 */
//todo: fix this
@SuppressLint("RestrictedApi")
class SimpleFooter
@JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr), RefreshFooter {

    private var finishDuration = 200
    private var noMoreData = false

    private var progressView: ProgressBar? = null

    init {
        initView(context)
    }

    private fun initView(context: Context?) {
        View.inflate(context, R.layout.common_loading_more, this)
        progressView = findViewById(R.id.progress_bar)
        progressView?.visibility = View.GONE
    }

    override fun getSpinnerStyle(): SpinnerStyle {
        return SpinnerStyle.Translate
    }

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        if (!noMoreData) {
            progressView?.visibility = View.GONE
            return finishDuration
        }
        return 0
    }

    override fun onInitialized(kernel: RefreshKernel, height: Int, extendHeight: Int) {
    }

    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {
    }

    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
    }

    override fun getView(): View {
        return this
    }

    override fun setPrimaryColors(vararg colors: Int) {
    }


    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, extendHeight: Int) {
        if (!noMoreData) {
            progressView?.visibility = View.VISIBLE
        }
    }

    override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
        if (!noMoreData) {
            when (newState) {
                RefreshState.None -> {
                    progressView?.visibility = View.GONE
                }
                else ->{
                    progressView?.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onMoving(isDragging: Boolean, percent: Float, offset: Int, height: Int, maxDragHeight: Int) {
    }


    override fun isSupportHorizontalDrag(): Boolean {
        return false
    }

    override fun setNoMoreData(noMoreData: Boolean): Boolean {
        if (this.noMoreData != noMoreData) {
            this.noMoreData = noMoreData
            progressView?.visibility = View.GONE
        }
        return true
    }

}