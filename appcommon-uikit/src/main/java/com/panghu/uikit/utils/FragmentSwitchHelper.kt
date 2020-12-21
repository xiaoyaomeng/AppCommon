package com.panghu.uikit.utils

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.glip.widgets.viewpage.AbstractPageItem

class FragmentSwitchHelper(val fragmentManager: FragmentManager?, @IdRes val container: Int) {

    fun navigateToPage(toPage: AbstractPageItem, bundle: Bundle? = null): Boolean {
        return navigateToPage(toPage.tag, { toPage.item }, bundle)
    }

    fun navigateToPage(tag: String, clazz: Class<out Fragment>, bundle: Bundle? = null): Boolean {
        return navigateToPage(tag, { clazz.newInstance() }, bundle)
    }

    fun navigateToPage(tag: String, action: () -> Fragment, bundle: Bundle? = null): Boolean {
        Log.d(TAG, "Tag: $tag")

        fragmentManager?.let { manager ->
            val transaction = manager.beginTransaction()

            if (manager.primaryNavigationFragment?.tag == tag) {
                Log.d(TAG, "Already in the current page.")
                return false
            }

            //Hide current page
            manager.primaryNavigationFragment?.let {
                it.setMenuVisibility(false)
                transaction.hide(it)
            }

            //Show toPage
            var toPageFragment = manager
                    .findFragmentByTag(tag)
            if (toPageFragment == null) {
                toPageFragment = action.invoke()
                bundle?.let {
                    toPageFragment.arguments = it
                }
                transaction.add(
                        container,
                        toPageFragment,
                        tag
                )
            } else {
                bundle?.let {
                    toPageFragment.arguments = it
                }
                transaction.show(toPageFragment)
            }
            toPageFragment.setMenuVisibility(true)

            //record toPage
            transaction.setPrimaryNavigationFragment(toPageFragment)
            transaction.commitNowAllowingStateLoss()
            return true
        } ?: return false
    }

    fun replacePage(toPage: AbstractPageItem, bundle: Bundle? = null): Boolean {
        fragmentManager?.let { manager ->
            val transaction = manager.beginTransaction()
            if (manager.primaryNavigationFragment?.tag === toPage.tag) {
                Log.d(TAG, "Already in the current page.")
                return false
            }

            //replace toPage
            val toPageFragment = manager
                    .findFragmentByTag(toPage.tag) ?: toPage.item
            toPageFragment.arguments = bundle
            transaction.replace(container, toPageFragment, toPage.tag)

            //record toPage
            transaction.setPrimaryNavigationFragment(toPageFragment)
            transaction.commitNowAllowingStateLoss()
            return true
        } ?: return false
    }

    fun getCurrentFragment(): Fragment? {
        return fragmentManager?.primaryNavigationFragment
    }

    fun getCurrentTag(): String? {
        return getCurrentFragment()?.tag
    }


    companion object {
        const val TAG = "FragmentSwitchHelper"
    }
}