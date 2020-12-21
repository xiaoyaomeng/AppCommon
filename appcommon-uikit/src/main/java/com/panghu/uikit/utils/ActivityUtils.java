package com.panghu.uikit.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.util.List;

public class ActivityUtils {

    private static final String TAG = "ActivityUtils";

    public static void startActivity(final Context context,
                                      final Bundle extras,
                                      final String pkg,
                                      final String cls,
                                      int flags) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.setComponent(new ComponentName(pkg, cls));
        if (!isIntentAvailable(context, intent)) {
            Log.e(TAG, "Intent is unavailable.");
            return;
        }
        intent.addFlags(flags);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    private static boolean isIntentAvailable(final Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List activities =
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return activities.size() > 0;
    }
}
