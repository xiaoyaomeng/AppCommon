package com.panghu.uikit.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

object DeviceUtil {
    @JvmStatic
    fun isRunningOnChromebook(context: Context): Boolean {
        return context.packageManager.hasSystemFeature("org.chromium.arc.device_management")
    }

    @JvmStatic
    fun isSamsungDevice() : Boolean {
        return Build.MANUFACTURER.contains("samsung", ignoreCase = true)
    }

    @JvmStatic
    fun isGoogleDevice() : Boolean {
        return Build.MANUFACTURER.contains("Google", ignoreCase = true)
    }

    @JvmStatic
    fun isNativeCallSupported(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)
    }

    @JvmStatic
    fun isInMultiWindowMode(activity: Activity): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            activity.isInMultiWindowMode
        } else {
            false
        }
    }
}