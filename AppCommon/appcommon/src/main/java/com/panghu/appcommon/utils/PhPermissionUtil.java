package com.panghu.appcommon.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

/**
 * PhPermissionUtil
 *
 * @desc
 * @autor lijiangping
 * @wechat ljphhj
 **/
public class PhPermissionUtil {


    /**
     * 判断是否权限被允许
     * @param context
     * @param permission
     * @return
     */
    public static boolean hasPermission(Context context, String permission) {
        if (context != null && !TextUtils.isEmpty(permission)) {
            try {
                PackageManager packageManager = context.getPackageManager();
                if (packageManager != null) {
                    if (PackageManager.PERMISSION_GRANTED == packageManager.checkPermission(permission, context
                            .getPackageName())) {
                        return true;
                    }
                    PhLogUtil.d("PhPermissionUtil", "Have you  declared permission " + permission + " in AndroidManifest.xml ?");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

}
