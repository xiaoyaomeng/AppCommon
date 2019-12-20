package com.panghu.appcommon.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import java.io.File;
import java.util.List;

/**
 * PhPackageUtil
 *
 * @desc
 * @autor lijiangping
 * @wechat ljphhj
 * @email lijiangping.zz@gmail.com
 *
 **/
public class PhPackageUtil {

    /**
     * App安装位置Flag
     */
    public static final int APP_INSTALL_AUTO = 0;
    public static final int APP_INSTALL_INTERNAL = 1;
    public static final int APP_INSTALL_EXTERNAL = 2;

    /**
     * 调用系统安装应用
     * @param context
     * @param file 要安装的apk对应file
     * @return
     */
    public static boolean install(Context context, File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return false;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return true;
    }

    /**
     * 调用系统卸载应用
     * @param context
     * @param packageName 要卸载的app的包名
     */
    public static void uninstall(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        context.startActivity(intent);
    }

    /**
     * 打开已安装应用的详情
     * @param context
     * @param packageName 要打开的app的包名
     */
    public static void gotoAppDetailActivity(Context context, String packageName) {
        Intent intent = new Intent();
        int sdkVersion = Build.VERSION.SDK_INT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", packageName, null));
        } else {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra((sdkVersion == Build.VERSION_CODES.FROYO ? "pkg"
                    : "com.android.settings.ApplicationPkgName"), packageName);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 获取顶部task任务栈的信息
     * @param context
     * @return
     */
    public static ActivityManager.RunningTaskInfo getTopRunningTask(Context context) {
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            // 得到当前正在运行的任务栈
            List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(1);
            // 得到前台显示的任务栈
            ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
            return runningTaskInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前系统安装应用的默认位置
     *
     * @return APP_INSTALL_AUTO or APP_INSTALL_INTERNAL or APP_INSTALL_EXTERNAL.
     */
    public static int getInstallLocation() {
        PhShellUtil.CommandResult commandResult = PhShellUtil.execCommand(
                "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm get-install-location", false, true);
        if (commandResult.result == 0 && commandResult.responseMsg != null && commandResult.responseMsg.length() > 0) {
            try {
                return Integer.parseInt(commandResult.responseMsg.substring(0, 1));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return APP_INSTALL_AUTO;
    }

    /**
     * 判断是否是系统App
     * @param context
     * @param packageName 要检测的app包名
     * @return
     */
    public static boolean isSystemApplication(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null || packageName == null || packageName.length() == 0) {
            return false;
        }
        try {
            ApplicationInfo app = packageManager.getApplicationInfo(packageName, 0);
            return (app != null && (app.flags & ApplicationInfo.FLAG_SYSTEM) > 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取已安装的全部应用信息
     * @param context
     * @return 全部已安装的App应用信息数组
     */
    public static List<PackageInfo> getInsatalledPackages(Context context) {
        return context.getPackageManager().getInstalledPackages(0);
    }

    /**
     * 判断app是否已安装
     * @param context
     * @param packageName app的包名
     * @return
     */
    public static boolean isInstall(Context context, String packageName) {
        boolean isInstall = false;
        if (!PhStringUtil.isEmpty(packageName)) {
            return false;
        }
        try {
            PackageInfo packageInfo;
            try {
                packageInfo = context.getPackageManager().getPackageInfo(
                        packageName, PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                packageInfo = null;
                e.printStackTrace();
            }
            if (packageInfo == null) {
                isInstall = false;
            } else {
                isInstall = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isInstall;
    }

    /**
     * 获取指定程序的application信息
     * @param context
     * @param packageName
     * @return
     */
    public static ApplicationInfo getApplicationInfo(Context context, String packageName) {
        try {
            return context.getPackageManager().getApplicationInfo(packageName, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定程序的包信息
     * @param context
     * @param packageName
     * @return
     */
    public static PackageInfo getPackageInfo(Context context, String packageName) {
        try {
            return context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
