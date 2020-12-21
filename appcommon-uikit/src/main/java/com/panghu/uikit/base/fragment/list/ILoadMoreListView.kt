package com.panghu.uikit.base.fragment.list

import com.panghu.uikit.base.IUIView

/**
 * @author by panghu
 * @date 23/01/2018
 */
interface ILoadMoreListView : IUIView {

    fun updateData(direction: DataUpdate, count: Int, dataSourceChanged: Boolean)

    fun completeLoadMoreData(direction: DataUpdate, count: Int, hasMore: Boolean, code: Long)
}
