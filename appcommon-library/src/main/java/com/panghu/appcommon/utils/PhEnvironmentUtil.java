package com.panghu.appcommon.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * PhEnvironmentUtil
 *
 * @desc apk运行环境判断
 * @autor lijiangping
 * @wechat ljphhj
 * @email lijiangping.zz@gmail.com
 *
 **/
public class PhEnvironmentUtil {


    /**
     * 自身app
     *
     * Judge whether an app is dubuggable
     *
     * @param context
     * @return
     */
    public static boolean isApkDebuggable(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {

        }
        return false;
    }

    /**
     * 其他app
     *
     * Judge whether an app is dubuggable by package name
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isApkDebugable(Context context, String packageName) {
        try {
            PackageInfo pkginfo = context.getPackageManager().getPackageInfo(
                    packageName, PackageManager.GET_ACTIVITIES);
            if (pkginfo != null) {
                ApplicationInfo info = pkginfo.applicationInfo;
                return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            }
        } catch (Exception e) {
        }
        return false;
    }

}
