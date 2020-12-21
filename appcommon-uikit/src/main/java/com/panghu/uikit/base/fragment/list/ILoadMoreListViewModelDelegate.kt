package com.panghu.uikit.base.fragment.list

/**
 * @author by panghu
 * @date 25/01/2018
 */
interface ILoadMoreListViewModelDelegate {

    fun onDataUpdate(direction: DataUpdate, count: Int, dataSourceChanged: Boolean)

    fun onLoadMoreDataComplete(direction: DataUpdate, count: Int, hasMore: Boolean, code: Long)
}