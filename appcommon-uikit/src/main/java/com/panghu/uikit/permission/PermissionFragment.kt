package com.panghu.uikit.permission

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import android.util.ArrayMap
import android.util.SparseArray
import com.panghu.uikit.R
import com.panghu.uikit.utils.ExternalAppUtil
import com.panghu.uikit.utils.Log

class PermissionFragment : Fragment() {

    private val requestList: SparseArray<PermissionRequest> = SparseArray()
    private val permissionShowRequestRationaleMap: ArrayMap<String, Boolean> = ArrayMap()
    private var permissionRequestCode: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissions(request: PermissionRequest) {
        val permissions = request.permissionData.getPermissions()
        permissions.forEach {
            permissionShowRequestRationaleMap[it] = shouldShowRequestPermissionRationale(it)
        }
        val requestCode = permissionRequestCode++
        requestList.put(requestCode, request)
        val unGrantedPermissions = permissions.filter { !isGranted(it) }.toTypedArray()
        // Some HTC devices(U-3w) will popup the request permission window when we checking the permission.
        // In this case, if the user grant the request, [unGrantedPermissions] would be empty.
        if (unGrantedPermissions.isNotEmpty()) {
            Log.d(TAG, "requestCode:$requestCode, permissions:[${unGrantedPermissions.joinToString()}]")
            requestPermissions(unGrantedPermissions, requestCode)
        } else {
            request.granted?.onAction()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "requestCode:$requestCode, permissions:[${permissions.joinToString()}], " +
                "results:[${grantResults.joinToString()}]")
        if (requestList.indexOfKey(requestCode) < 0) {
            Log.d(TAG, "Invoked but didn't find the corresponding permission request.")
            return
        }

        val request = requestList[requestCode]
        val granted = grantResults.none { it == PackageManager.PERMISSION_DENIED }
        if (granted && permissions.isNotEmpty()) {
            request.granted?.onAction()
        } else {
            onDenied(request, permissions)
        }
        requestList.remove(requestCode)
    }

    private fun onDenied(request: PermissionRequest, permissions: Array<String>) {
        val hintFlag = request.permissionData.hintFlag
        if (hintFlag != PermissionData.NO_HINT) {
            val shouldShowEnableHint = !permissions.none {
                !isGranted(it) && !shouldShowRequestPermissionRationale(it)
                        && !(permissionShowRequestRationaleMap[it] ?: true)
            }
            Log.d(TAG, "ShouldShowEnableHint: $shouldShowEnableHint")
            if (shouldShowEnableHint) {
                val perms = request.permissionData.getPermissions()
                val isAll = perms.size == permissions.size && permissions.size > 1
                val model = if (isAll) {
                    getAllHintModel(request.permissionData)
                } else {
                    getSingleHintModel(request.permissionData, permissions[0])
                }
                when (hintFlag) {
                    PermissionData.SHOW_HINT_ALERT -> showPermissionHintAlert(model, request)
                    PermissionData.SHOW_HINT_SCREEN -> {
                        activity?.let { showPermissionHintScreen(it, model) }
                        request.denied?.onAction()
                    }
                }
            } else {
                request.denied?.onAction()
            }
        } else {
            request.denied?.onAction()
        }
        permissionShowRequestRationaleMap.clear()
    }

    private fun showPermissionHintScreen(context: Context, hintModel: HintData) {
        val intent = Intent(context, PermissionHintActivity::class.java)
        intent.putExtra(PermissionHintActivity.EXTRA_MODEL, hintModel)
        context.startActivity(intent)
    }

    private fun showPermissionHintAlert(model: HintData, request: PermissionRequest) {
        activity?.apply {
            AlertDialog.Builder(this as Context)
                .setTitle(model.hintTitle)
                .setMessage(model.description)
                .setPositiveButton(R.string.ok) { _, _ ->
                    ExternalAppUtil.go2SystemAppSetting(activity)
                    request.denied?.onAction()
                }
                .setNegativeButton(R.string.cancel) { _, _ ->
                    request.denied?.onAction()
                }
                .setCancelable(false)
                .show()
        }
    }

    private fun getAllHintModel(group: PermissionData): HintData {
        return group.hintModel
    }

    private fun getSingleHintModel(group: PermissionData, perm: String): HintData {
        return group.getHintByPermission(perm)
    }

    private fun isGranted(permission: String): Boolean {
        val fragmentActivity = activity
            ?: throw IllegalStateException("This fragment must be attached to an activity.")
        return EasyPermission.hasPermission(fragmentActivity, permission)
    }

    companion object {
        private const val TAG = "PermissionFragment"
    }
}