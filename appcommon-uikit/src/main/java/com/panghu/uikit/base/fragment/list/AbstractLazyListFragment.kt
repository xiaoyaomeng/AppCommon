package com.panghu.uikit.base.fragment.list

import android.os.Bundle
import android.view.View
import com.panghu.uikit.base.fragment.FragmentLazyLoadController

abstract class AbstractLazyListFragment : AbstractBaseListFragment(),
    FragmentLazyLoadController.ILazyLoad {

    private val lazyLoadController by lazy { FragmentLazyLoadController(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lazyLoadController.onFragmentViewCreated(savedInstanceState)
    }

    @Suppress("DEPRECATION")
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        lazyLoadController.onFragmentUserVisibleHintSet(isVisibleToUser)
    }

    override fun onDestroyView() {
        lazyLoadController.onFragmentDestroyView()
        super.onDestroyView()
    }
}