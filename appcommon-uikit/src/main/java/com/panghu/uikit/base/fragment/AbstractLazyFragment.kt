package com.panghu.uikit.base.fragment

import android.os.Bundle
import android.view.View

/**
 * The fragment extend base fragment.
 * It support lazy load when using ViewPager to bind fragment,
 * to reduce load time when ViewPager first load after data changed.
 */

abstract class AbstractLazyFragment : AbstractBaseFragment(), FragmentLazyLoadController.ILazyLoad {

    private val lazyLoadController by lazy { FragmentLazyLoadController( this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lazyLoadController.onFragmentViewCreated(savedInstanceState)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        lazyLoadController.onFragmentUserVisibleHintSet(isVisibleToUser)
    }

    override fun onDestroyView() {
        lazyLoadController.onFragmentDestroyView()
        super.onDestroyView()
    }
}