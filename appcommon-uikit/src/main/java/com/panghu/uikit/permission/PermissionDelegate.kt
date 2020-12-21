package com.panghu.uikit.permission

import android.content.Context
import android.os.Build
import androidx.fragment.app.FragmentManager

class PermissionDelegate(
    private val context: Context,
    private val fragmentManager: FragmentManager
) {

    private val permissionFragment by lazy {
        findOrCreateFragment()
    }

    private fun findOrCreateFragment(): PermissionFragment {
        var permissionFragment: PermissionFragment? = findPermissionFragment(fragmentManager)
        if (permissionFragment == null) {
            permissionFragment = PermissionFragment()
            fragmentManager
                .beginTransaction()
                .add(permissionFragment, TAG)
                .commitNowAllowingStateLoss()
        }
        return permissionFragment
    }

    private fun isGranted(perm: String): Boolean {
        return !isMarshmallow() || EasyPermission.hasPermission(context, perm)
    }

    private fun isMarshmallow(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    fun request(request: PermissionRequest) {
        val perms = request.permissionData.getPermissions()
        val allGranted = perms.none { !isGranted(it) }
        if (allGranted) {
            request.granted?.onAction()
        } else {
            permissionFragment.requestPermissions(request)
        }
    }

    companion object {
        private const val TAG = "PermissionDelegate"

        fun findPermissionFragment(fragmentManager: FragmentManager): PermissionFragment? {
            return fragmentManager.findFragmentByTag(TAG) as? PermissionFragment
        }
    }
}

