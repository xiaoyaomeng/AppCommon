package com.panghu.uikit.utils;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.RemoteException;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.panghu.uikit.permission.EasyPermission;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static android.content.Context.TELECOM_SERVICE;

public class NativeCallUtil {

    public static final String CONFERENCE_CALL_ACCESSCODE_FORMAT = ",,%s#";
    private static final String COM_ANDROID_PHONE = "com.android.phone";
    private static final String COM_ANDROID_TELECOMM = "com.android.telecomm";
    private static final String COM_DIALER_FOR_ANDROID_L = "com.android.server.telecom";

    public static void gotoNativeDialer(final Context context, String telUri) {
        if (telUri == null) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(telUri));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public static boolean hasCall(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
        return telephonyManager != null && telephonyManager.getCallState() != TelephonyManager.CALL_STATE_IDLE;
    }

    public static boolean hasActiveCall(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager != null && telephonyManager.getCallState() == TelephonyManager.CALL_STATE_OFFHOOK;
    }

    public static boolean hasNativeDialer(Context context) {
        if (Build.VERSION.SDK_INT >= 22) {
            return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
        }
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return manager != null && manager.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    public static boolean isSetAsDefaultDialer(Context context) {
        TelecomManager telecomManager = (TelecomManager) context.getSystemService(TELECOM_SERVICE);
        if (telecomManager != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getPackageName().equals(telecomManager.getDefaultDialerPackage());
        }
        return false;
    }

    @SuppressLint("MissingPermission")
    public static String getNativeCallNumber(Context context) {
        boolean hasPermission = EasyPermission.hasPermission(context, Manifest.permission.READ_PHONE_STATE);
        if (!hasPermission) {
            hasPermission = EasyPermission.hasPermission(context, Manifest.permission.READ_SMS);
        }
        if (hasPermission) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {
                return telephonyManager.getLine1Number();
            }
        }
        return null;
    }

    public static boolean endNativeCall(Context context) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, RemoteException {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Class<?> clazz = Class.forName(telephonyManager.getClass().getName());
        Method method = clazz.getDeclaredMethod("getITelephony");
        method.setAccessible(true);
        ITelephony iTelephony = (ITelephony) method.invoke(telephonyManager);
        return iTelephony.endCall();
    }


    @SuppressLint("MissingPermission")
    public static void useNativePhoneAppToCall(final Context context, String telUri) {
        if (context == null || telUri == null) {
            return;
        }

        if (!EasyPermission.hasPermission(context, Manifest.permission.CALL_PHONE)) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(telUri));

        if (intent.resolveActivity(context.getPackageManager()) == null) {
            return;
        }

        List<ResolveInfo> availableSoft = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (availableSoft.size() == 1) {
            intent.setComponent(new ComponentName(availableSoft.get(0).activityInfo.packageName, availableSoft.get(0).activityInfo.name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            for (ResolveInfo info : availableSoft) {
                if (isNativePhoneCall(info.activityInfo.packageName.trim())) {
                    intent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;
                }
            }
        }
    }

    private static boolean isNativePhoneCall(String packageName) {
        return COM_ANDROID_PHONE.equalsIgnoreCase(packageName) ||
                COM_ANDROID_TELECOMM.equalsIgnoreCase(packageName) ||
                COM_DIALER_FOR_ANDROID_L.equalsIgnoreCase(packageName);
    }
}
