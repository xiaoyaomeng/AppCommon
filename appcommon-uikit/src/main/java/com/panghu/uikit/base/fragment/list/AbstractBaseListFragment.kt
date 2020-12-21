package com.panghu.uikit.base.fragment.list

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.panghu.uikit.R
import com.panghu.uikit.base.fragment.AbstractBaseFragment
import com.panghu.uikit.utils.Log

abstract class AbstractBaseListFragment : AbstractBaseFragment() {
    private val TAG = "AbstractBaseListFragment"

    protected var isLoadingData: Boolean = false //Load data request
    private var baseAdapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>? = null
    private var dataObserver: RecyclerView.AdapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            onListChanged()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            onListItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            onListItemRangeRemoved(positionStart, itemCount)
        }
    }

    protected var recyclerView: RecyclerView? = null

    abstract fun createAdapter(): RecyclerView.Adapter<out RecyclerView.ViewHolder>
    abstract fun getLayoutId(): Int
    abstract fun initEmptyView()
    abstract fun showEmptyView()
    abstract fun hideEmptyView()

    private var isViewCreated: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true

        initRecyclerView(view)
        initEmptyView()

        baseAdapter?.registerAdapterDataObserver(dataObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        baseAdapter?.unregisterAdapterDataObserver(dataObserver)
    }

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    open fun loadData() {
        //Request first data may before viewCreated, so we need check the fragment isDetached
        if (isUiReady && isViewCreated)
            showProgressBar()
    }

    open fun getRecyclerViewId(): Int {
        return R.id.recycler_view
    }

    open fun getLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context)
    }

    protected fun addItemDecoration(itemDecoration: RecyclerView.ItemDecoration) {
        recyclerView?.addItemDecoration(itemDecoration)
    }

    protected fun getAdapter(): RecyclerView.Adapter<out RecyclerView.ViewHolder>? {
        if (baseAdapter == null) {
            baseAdapter = createAdapter()
        }
        return baseAdapter
    }

    private fun initRecyclerView(view: View?) {
        recyclerView = view?.findViewById(getRecyclerViewId())
        recyclerView?.layoutManager = getLayoutManager()
        recyclerView?.adapter = getAdapter()
    }

    protected fun onListItemRangeRemoved(positionStart: Int, itemCount: Int) {
        Log.v(TAG, "PositionStart: $positionStart, itemCount: $itemCount")
        onListChanged()
    }

    protected fun onListItemRangeInserted(positionStart: Int, itemCount: Int) {
        Log.v(TAG, "PositionStart: $positionStart, itemCount: $itemCount")
        onListChanged()
    }

    @CallSuper
    open fun onListChanged() {
        val count = getItemCount()
        val isUpdateEmptyView = count > 0 || !isLoadingData
        if (isUpdateEmptyView) {
            hideProgressBar()
            toggleEmptyView()
        }
    }

    private fun toggleEmptyView() {
        if (getItemCount() == 0) {
            showEmptyView()
            recyclerView?.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
        } else {
            hideEmptyView()
            recyclerView?.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
        }
    }

    protected fun getItemCount(): Int {
        return baseAdapter?.itemCount ?: 0
    }

}
