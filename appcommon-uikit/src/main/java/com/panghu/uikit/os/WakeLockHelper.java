package com.panghu.uikit.os;

import android.content.Context;
import android.os.PowerManager;

import com.panghu.uikit.base.BaseApplication;

public class WakeLockHelper {

    private WakeLockHelper() {
    }

    public static PowerManager.WakeLock newWakeLock(int levelAndFlags, String tag) {
        PowerManager powerManager =
                (PowerManager) BaseApplication.getAppContext().getSystemService(Context.POWER_SERVICE);
        return powerManager.newWakeLock(levelAndFlags, tag);
    }

    public static void acquire(PowerManager.WakeLock wakeLock) {
        if (wakeLock != null && !wakeLock.isHeld()) {
            wakeLock.acquire();
        }
    }

    public static void release(PowerManager.WakeLock wakeLock) {
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
    }
}
