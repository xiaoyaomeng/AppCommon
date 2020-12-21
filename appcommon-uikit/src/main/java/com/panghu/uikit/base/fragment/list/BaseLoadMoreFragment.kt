package com.panghu.uikit.base.fragment.list

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.panghu.uikit.R
import com.panghu.uikit.base.IUIView
import com.glip.widgets.recyclerview.RecyclerViewUtil
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshFooter
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState

abstract class BaseLoadMoreFragment : AbstractBaseListFragment(), ILoadMoreListView, IUIView {
    protected var loadMorePresenter: BaseLoadMorePresenter? = null

    private var refreshLayout: SmartRefreshLayout? = null

    private lateinit var refreshHeader: RefreshHeader

    private lateinit var refreshFooter: RefreshFooter

    abstract fun createLoadMorePresenter(): BaseLoadMorePresenter

    abstract fun getListSort(): ListSort

    private val POST_RV_REFRESH_DELAY = 200L
    private val RETRY_DELAY_MILLIS = 2000L


    protected var isFirstComplete: Boolean = false //First load complete

    fun createRefreshHeader(): RefreshHeader {
        return SimpleHeader(context)
    }

    fun createRefreshFooter(): RefreshFooter {
        return SimpleFooter(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (loadMorePresenter == null) {
            loadMorePresenter = createLoadMorePresenter()
        }

        //Will load data onCreate when isAutoLoad
        if (isAutoLoad()) {
            loadData()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshLayout = view.findViewById<View>(R.id.refresh_layout) as SmartRefreshLayout
        if (refreshLayout == null) {
            throw IllegalStateException("A RecyclerView in " +
                    BaseLoadMoreFragment::class.java.simpleName +
                    " requires a " + RefreshLayout::class.java.simpleName)
        }

        refreshHeader = createRefreshHeader()

        refreshFooter = createRefreshFooter()

        refreshLayout?.let {
            it.setHeaderTriggerRate(0f)
            it.setFooterTriggerRate(0f)
            it.setHeaderMaxDragRate(1f)
            it.setFooterMaxDragRate(1f)
            it.setHeaderHeight(60f)
            it.setFooterHeight(60f)

            it.setRefreshHeader(refreshHeader)
            it.setRefreshFooter(refreshFooter)

            it.setEnableLoadMoreWhenContentNotFull(true)

            it.setOnRefreshListener {
                //Delay to request data, that avoid the loading spinner may overlap the item.
                recyclerView?.postDelayed({
                    if (isFirstComplete && !isLoadingData) {
                        loadMorePresenter?.loadHeaderMoreData(getListSort())
                        isLoadingData = true
                    }
                }, 300)
            }

            it.setOnLoadMoreListener {
                loadMorePresenter?.loadFooterMoreData(getListSort())
            }

        }

        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (RecyclerViewUtil.isInTop(recyclerView)) {
                    if (allowAutoRefreshOnScrollListener() && hasLoadMoreHeader()) {
                        refreshLayout?.autoRefresh()
                    }
                }
            }
        })
        //Will show progressbar when the load request is show
        if (isLoadingData) {
            showProgressBar()
        }
    }

    open fun isAutoLoad(): Boolean {
        return true
    }

    open fun allowAutoRefreshOnScrollListener(): Boolean {
        return true
    }

    override fun loadData() {
        super.loadData()
        loadMorePresenter?.loadData()
        isLoadingData = true
    }

    override fun updateData(direction: DataUpdate, count: Int, dataSourceChanged: Boolean) {

        if (!isFirstComplete) {
            isLoadingData = false
        }

        getAdapter()?.notifyDataSetChanged()

        enableLoadMoreStatus()

        //CoreLib may be recall twice, so we need show refresh view until complete load more callback
        if (!isFirstComplete) {
            refreshLayout?.postDelayed({
                if (RecyclerViewUtil.isInTop(recyclerView)) {
                    if (hasLoadMoreHeader()) {
                        refreshLayout?.autoRefresh()
                    }
                }
            }, POST_RV_REFRESH_DELAY)
        }
    }

    override fun completeLoadMoreData(direction: DataUpdate, count: Int, hasMore: Boolean, code: Long) {
        isLoadingData = false

        getAdapter()?.notifyDataSetChanged()

        enableLoadMoreStatus()

        val isSuccess = code == 0L
        val listSort = getListSort()

        if (!isSuccess) {
            //Try to reload when is not success request,use the direction to retry data
            if (loadMorePresenter?.isHeaderMoreData(direction, listSort) == true) {
                if (RecyclerViewUtil.isInTop(recyclerView)) {
                    recyclerView?.postDelayed({ loadMorePresenter?.loadHeaderMoreData(getListSort()) }, RETRY_DELAY_MILLIS)
                } else {
                    refreshLayout?.finishRefresh(0)
                }

            } else if (loadMorePresenter?.isFooterMoreData(direction, listSort) == true) {
                if (RecyclerViewUtil.isInBottom(recyclerView)) {
                    recyclerView?.postDelayed({ loadMorePresenter?.loadFooterMoreData(getListSort()) }, RETRY_DELAY_MILLIS)
                } else {
                    refreshLayout?.finishLoadMore(0)
                }
            }
            isFirstComplete = true
            return
        }

        //We will disappear refresh loading and set first load is complete
        if (!isFirstComplete) {
            if (refreshLayout?.state != RefreshState.None) {
                refreshLayout?.RefreshKernelImpl()?.setState(RefreshState.RefreshFinish)
                refreshLayout?.RefreshKernelImpl()?.setState(RefreshState.None)
            }

            isFirstComplete = true
        } else {
            when {
                direction == DataUpdate.BOTH -> {
                    refreshLayout?.finishRefresh(0)
                    refreshLayout?.finishLoadMore(0)
                }
                loadMorePresenter?.isHeaderMoreData(direction, listSort) ?: false -> {
                    refreshLayout?.finishRefresh(0)
                }
                loadMorePresenter?.isFooterMoreData(direction, listSort) ?: false -> {
                    refreshLayout?.finishLoadMore(0)
                }
            }
        }
    }

    fun finishRefresh() {
        refreshLayout?.finishRefresh(0)
    }

    fun finishLoadMore() {
        refreshLayout?.finishLoadMore(0)
    }

    fun enableLoadMoreStatus() {
        enableLoadMoreHeader(hasLoadMoreHeader())
        enableLoadMoreFooter(hasLoadMoreFooter())
    }

    fun enableLoadMoreHeader(enable: Boolean) {
        if (!enable && refreshLayout?.state == RefreshState.Refreshing) {
            refreshLayout?.finishRefresh(0)
        }
        refreshLayout?.isEnableRefresh = enable
    }

    fun enableLoadMoreFooter(enable: Boolean) {
        if (!enable && refreshLayout?.state == RefreshState.Loading) {
            refreshLayout?.finishLoadMore(0)
        }
        refreshLayout?.isEnableLoadMore = enable
    }

    fun hasLoadMoreHeader(): Boolean {
        return loadMorePresenter?.hasHeaderMoreData(getListSort()) ?: true
    }

    fun hasLoadMoreFooter(): Boolean {
        return loadMorePresenter?.hasFooterMoreData(getListSort()) ?: true
    }

    private fun hideRefreshLayout() {
        refreshLayout?.visibility = View.INVISIBLE
    }

    private fun showRefreshLayout() {
        refreshLayout?.visibility = View.VISIBLE
    }
}
