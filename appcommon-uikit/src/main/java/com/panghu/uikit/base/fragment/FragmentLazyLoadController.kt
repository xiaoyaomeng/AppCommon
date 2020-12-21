package com.panghu.uikit.base.fragment

import android.os.Bundle

class FragmentLazyLoadController(private val lazyLoader: ILazyLoad) {

    private var isVisibleToUser = false
    private var isViewCreated = false
    private var savedState: Bundle? = null

    fun onFragmentViewCreated(bundle: Bundle?) {
        isViewCreated = true
        savedState = bundle
        tryLazyLoad()
    }

    fun onFragmentUserVisibleHintSet(isVisible: Boolean) {
        isVisibleToUser = isVisible
        tryLazyLoad()
    }

    fun onFragmentDestroyView() {
        isViewCreated = false
    }

    private fun tryLazyLoad() {
        if (isViewCreated && isVisibleToUser) {
            isViewCreated = false
            isVisibleToUser = false
            lazyLoader.onLazyLoad(savedState)
        }
    }

    interface ILazyLoad {
        /**
         * Avoid the fragment transaction is commit() after onSaveInstanceState() is called.
         * This is something undesirable, as that means your final state is not saved before you change Fragment.
         * It causes an exception: "This is something undesirable, as that means your final state is not saved before you change Fragment."
         * You should use commitAllowingStateLoss() instead of commit()
         */
        fun onLazyLoad(savedInstanceState: Bundle?)
    }

}