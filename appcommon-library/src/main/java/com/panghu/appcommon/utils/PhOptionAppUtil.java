package com.panghu.appcommon.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * PhOptionAppUtil
 * 提供一系列操作App的方法，跳转UI等
 *
 * @desc
 * @autor lijiangping
 * @wechat ljphhj
 * @email lijiangping.zz@gmail.com
 *
 **/
public class PhOptionAppUtil {


    /**
     * 调用系统自带的分享操作
     * @param context
     * @param title
     * @param content
     * @param dialogTitle
     */
    public static void gotoSystemShareUI(Context context, String title, String content, String dialogTitle) {
        Intent intentItem = new Intent(Intent.ACTION_SEND);
        intentItem.setType("text/plain");
        intentItem.putExtra(Intent.EXTRA_SUBJECT, title);
        intentItem.putExtra(Intent.EXTRA_TEXT, content);
        intentItem.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intentItem, dialogTitle));
    }


    /**
     * 跳转到系统Launcher界面
     *
     * @param context
     */
    public static void gotoLauncherUI(Context context) {
        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(mHomeIntent);
    }

    /**
     * 文件分享页
     *
     * @param context
     * @param title
     * @param filePath
     */
    public static void gotoShareFile(Context context, String title, String filePath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        Uri uri = Uri.parse("file://" + filePath);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(intent, title));
    }

    /**
     * 打开网络设置页
     * @param act
     */
    public static void openNetworkSetting(Activity act) {
        Intent intent = new Intent();
        ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        act.startActivityForResult(intent, 0);
    }

    /**
     * 打开设置页面
     * @param context
     * @param index 0:wifi 1:亮度 2:sync 3:gps 4:蓝牙
     */
    public static void openSetting(Context context, int index) {
        Intent intent = new Intent();
        intent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
        intent.addCategory("android.intent.category.ALTERNATIVE");
        Uri uri = Uri.parse("custom:" + index);
        intent.setData(uri);
        try {
            PendingIntent.getBroadcast(context, 0, intent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用系统相册打开某个图片
     *
     * @param mContext
     * @param imagePath
     */
    public static void openImage(Context mContext, String imagePath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(imagePath));
        intent.setDataAndType(uri, "image/*");
        mContext.startActivity(intent);
    }

    public static final int PICTURE_REQ_CODE = 0x0001;

    /**
     * 打开图库
     *
     * @param activity
     */
    public void openPictureUI(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(intent, PICTURE_REQ_CODE);
    }

    /**
     * 在浏览器中搜索
     *
     * @param context
     * @param string
     */
    public static void search(Context context, String string) {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, string);
        context.startActivity(intent);
    }

    /**
     * 在浏览器中打开一个Url
     *
     * @param context
     * @param url
     */
    public static void openUrl(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    /**
     * 打开拨号app
     *
     * @param context
     */
    public static void openDial(Context context) {
        Intent intent = new Intent(Intent.ACTION_CALL_BUTTON);
        context.startActivity(intent);
    }

    /**
     * 直接拨打电话
     *
     * @param context
     * @param number
     */
    public static void openDial(Context context, String number) {
        Uri uri = Uri.parse("tel:" + number);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        context.startActivity(intent);
    }

    /**
     * 直接拨打电话
     * requires Permission "android.permission.CALL_PHONE"
     *
     * @param context
     * @param number
     */
    @SuppressLint("MissingPermission")
    public static void call(Context context, String number) {
        Uri uri = Uri.parse("tel:" + number);
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        context.startActivity(intent);
    }

    /**
     * 打开相机app
     *
     * @param context
     */
    public static void openCamera(Context context) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        context.startActivity(intent);
    }

    /**
     * 拍照
     * this photo data will be returned in onActivityResult()
     *
     * @param activity
     * @param requestCode
     */
    public static void takeCamera(Activity activity, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 选择照片
     * this photo data will be returned in onActivityResult()
     *
     * @param activity
     * @param requestCode
     */
    public static void choosePhoto(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 调用系统相册打开视频
     *
     * @param mContext
     * @param videoPath
     */
    public static void openVideo(Context mContext, String videoPath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(videoPath));
        intent.setDataAndType(uri, "video/*");
        mContext.startActivity(intent);
    }

    /**
     * 通过系统打开Url
     *
     * @param mContext
     * @param url
     */
    public static void openURL(Context mContext, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        mContext.startActivity(intent);
    }

    /**
     * 通过系统下载服务下载文件
     *
     * @param context
     * @param fileurl
     */
    public static void downloadFile(Context context, String fileurl) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileurl));
        request.setDestinationInExternalPublicDir("/Download/", fileurl.substring(fileurl.lastIndexOf("/") + 1));
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
    }


    /**
     * 启动某个App程序
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean startApp(Context context, String packageName) {
        return startApp(context, packageName, null);
    }

    /**
     * 启动某个App程序
     *
     * @param context
     * @param packageName
     * @param param intent要带的参数信息
     * @return
     */
    public static boolean startApp(Context context, String packageName, Map<String, String> param) {
        PackageInfo pi = null;
        try {
            pi = context.getPackageManager().getPackageInfo(packageName, 0);
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
                resolveIntent.setPackage(pi.packageName);
            }

            List<ResolveInfo> apps = context.getPackageManager().queryIntentActivities(resolveIntent, 0);

            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                String packageName1 = ri.activityInfo.packageName;
                String className = ri.activityInfo.name;

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);

                ComponentName cn = new ComponentName(packageName1, className);

                intent.setComponent(cn);
                if (param != null) {
                    for (Map.Entry<String, String> en : param.entrySet()) {
                        intent.putExtra(en.getKey(), en.getValue());
                    }
                }
                context.startActivity(intent);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context.getApplicationContext(), "启动失败",
                    Toast.LENGTH_LONG).show();
        }
        return false;
    }

    /**
     * 启动某个App程序方式2
     *
     * @param context
     * @param packagename
     */
    public static void startApp2(Context context, String packagename) {
        context.startActivity(new Intent(context.getPackageManager().getLaunchIntentForPackage(packagename)));
    }
}
