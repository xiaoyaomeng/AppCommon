package com.panghu.uikit.permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.core.content.PermissionChecker
import com.panghu.uikit.utils.Log

object EasyPermission {
    private const val TAG = "EasyPermission"
    private const val ACTION_PERMISSION_CHANGED = "PERMISSION_CHANGED"
    private const val EXTRA_PERMISSION = "PERMISSION"
    private const val EXTRA_GRANTED = "GRANTED"

    @JvmStatic
    fun with(activity: FragmentActivity): PermissionRequest {
        return PermissionRequest(activity, activity.supportFragmentManager)
    }

    @JvmStatic
    fun with(fragment: Fragment): PermissionRequest {
        return PermissionRequest(fragment.requireContext(), fragment.childFragmentManager)
    }

    @JvmStatic
    fun sendBroadcastWhenPermissionChanged(context: Context, permission: String) {
        val intent = Intent(ACTION_PERMISSION_CHANGED)
        intent.putExtra(EXTRA_PERMISSION, permission)
        intent.putExtra(EXTRA_GRANTED, hasPermission(context, permission))
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

    @JvmStatic
    fun hasPermission(context: Context, permission: PermissionData): Boolean {
        val perms = permission.getPermissions()
        return perms.none { !hasPermission(context, it) }
    }

    @JvmStatic
    fun hasPermission(context: Context, permissions: Array<String>): Boolean {
        return permissions.none { !hasPermission(context, it) }
    }

    @JvmStatic
    fun hasPermission(context: Context, permission: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && "HTC".equals(Build.MANUFACTURER, ignoreCase = true)
        ) {
            try {
                val result = PermissionChecker.checkSelfPermission(context, permission)
                return result == PermissionChecker.PERMISSION_GRANTED
            } catch (e: RuntimeException) {
                Log.e(TAG, e.message)
            }
        }
        val result = ContextCompat.checkSelfPermission(context, permission)
        return result == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Gets whether you should show UI with rationale for requesting a permission.
     * You should do this only if you do not have the permission and the context
     * in which the permission is requested does not clearly communicate to the user
     * what would be the benefit from granting this permission.
     *
     * Note:
     * Returns true if the app has requested this permission previously and the user denied the request
     *
     * @param activity Activity
     * @param permission A permission your app wants to request.
     * @return Whether you can show permission rationale UI.
     */
    @JvmStatic
    fun shouldShowRequestPermissionRationale(activity: Activity, permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }
}