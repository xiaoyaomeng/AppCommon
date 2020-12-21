package com.panghu.uikit.base.fragment.list

import android.content.Context
import androidx.annotation.CallSuper

abstract class BaseLoadMorePresenter(val loadMoreListView: ILoadMoreListView,
                                     val context: Context?) : ILoadMoreListUiController, ILoadMoreListViewModelDelegate {

    fun loadHeaderMoreData(listSort: ListSort) {
        when (listSort) {
            ListSort.ASC -> loadMoreData(DataUpdate.OLDER)
            ListSort.DESC -> loadMoreData(DataUpdate.NEWER)
        }
    }

    fun loadFooterMoreData(listSort: ListSort) {
        when (listSort) {
            ListSort.ASC -> loadMoreData(DataUpdate.NEWER)
            ListSort.DESC -> loadMoreData(DataUpdate.OLDER)
        }
    }

    fun isHeaderMoreData(dataUpdate: DataUpdate, listSort: ListSort): Boolean {
        return when (listSort) {
            ListSort.ASC -> dataUpdate == DataUpdate.OLDER
            ListSort.DESC -> dataUpdate == DataUpdate.NEWER
        }
    }

    fun isFooterMoreData(dataUpdate: DataUpdate, listSort: ListSort): Boolean {
        return when (listSort) {
            ListSort.ASC -> dataUpdate == DataUpdate.NEWER
            ListSort.DESC -> dataUpdate == DataUpdate.OLDER
        }
    }


    fun getRefreshDataUpdate(listSort: ListSort): DataUpdate {
        return when (listSort) {
            ListSort.ASC -> DataUpdate.OLDER
            ListSort.DESC -> DataUpdate.NEWER
        }
    }

    fun getLoadMoreDataUpdate(listSort: ListSort): DataUpdate {
        return when (listSort) {
            ListSort.ASC -> DataUpdate.NEWER
            ListSort.DESC -> DataUpdate.OLDER
        }
    }

    fun hasHeaderMoreData(listSort: ListSort): Boolean {
        return when (listSort) {
            ListSort.ASC -> hasMoreData(DataUpdate.OLDER)
            ListSort.DESC -> hasMoreData(DataUpdate.NEWER)
        }
    }

    fun hasFooterMoreData(listSort: ListSort): Boolean {
        return when (listSort) {
            ListSort.ASC -> hasMoreData(DataUpdate.NEWER)
            ListSort.DESC -> hasMoreData(DataUpdate.OLDER)
        }
    }

    @CallSuper
    override fun onDataUpdate(direction: DataUpdate, count: Int, dataSourceChanged: Boolean) {
        loadMoreListView.updateData(direction, count, dataSourceChanged)
    }

    @CallSuper
    override fun onLoadMoreDataComplete(direction: DataUpdate, count: Int, hasMore: Boolean, code: Long) {
        loadMoreListView.completeLoadMoreData(direction, count, hasMore, code)
    }


}
