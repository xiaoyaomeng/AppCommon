package com.panghu.uikit.base.fragment.list

/**
 * @author by panghu
 * @date 25/01/2018
 */
interface ILoadMoreListUiController : IBaseListUiController {

    fun loadMoreData(direction: DataUpdate)

    fun hasMoreData(direction: DataUpdate): Boolean
}