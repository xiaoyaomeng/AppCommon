package com.panghu.uikit.permission

import android.content.Context
import androidx.fragment.app.FragmentManager

class PermissionRequest(private val context: Context, private val manager: FragmentManager) {
    lateinit var permissionData: PermissionData
    var granted: RequestResultAction? = null
    var denied: RequestResultAction? = null

    fun setPermission(data: PermissionData): PermissionRequest {
        this.permissionData = data
        return this
    }

    fun setPermission(permissions: List<String>): PermissionRequest {
        this.permissionData =
            PermissionDataGroup(permissions.map { PermissionData(it) }.toTypedArray())
        return this
    }

    fun setPermission(vararg permissions: String): PermissionRequest {
        this.permissionData =
            PermissionDataGroup(permissions.map { PermissionData(it) }.toTypedArray())
        return this
    }

    fun setHintFlag(flag: Int): PermissionRequest {
        this.permissionData.hintFlag = flag
        return this
    }

    fun onGranted(granted: RequestResultAction): PermissionRequest {
        this.granted = RequestResultActionWrapper(granted)
        return this
    }

    fun onGranted(action: () -> Unit): PermissionRequest {
        this.granted = RequestResultActionWrapper(object : RequestResultAction {
            override fun onAction() {
                action.invoke()
            }
        })
        return this
    }

    fun onDenied(denied: RequestResultAction): PermissionRequest {
        this.denied = RequestResultActionWrapper(denied)
        return this
    }

    fun onDenied(action: () -> Unit): PermissionRequest {
        this.denied = RequestResultActionWrapper(object : RequestResultAction {
            override fun onAction() {
                action.invoke()
            }
        })
        return this
    }

    fun request() {
        PermissionDelegate(context, manager).request(this)
    }

    inner class RequestResultActionWrapper(val action: RequestResultAction) : RequestResultAction {
        override fun onAction() {
            sendPermissionsChanged(context)
            action.onAction()
        }

        private fun sendPermissionsChanged(context: Context?) {
            context?.let {
                val perms = permissionData.getPermissions()
                for (permission in perms) {
                    EasyPermission.sendBroadcastWhenPermissionChanged(it, permission)
                }
            }
        }
    }

}