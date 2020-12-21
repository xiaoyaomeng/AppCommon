package com.panghu.uikit.base.fragment.list

/**
 * @author by panghu
 * @date 25/01/2018
 */
interface IBaseListViewModel {
    fun getDataAtIndex(index: Int, preload: Boolean): Any

    fun getCount(): Int
}