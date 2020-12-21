package com.panghu.uikit.utils;


import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.preference.PreferenceFragment;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.panghu.uikit.R;
import com.panghu.uikit.base.BaseApplication;
import com.panghu.uikit.customtabs.CustomTabActivityHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExternalAppUtil {
    private static final String TAG = "ExternalAppUtil";

    public static void go2SystemAppSetting(final Context context) {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void go2SystemAppSettingForResult(final Activity activity, int requestCode) {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivityForResult(intent, requestCode);
    }

    public static void go2SystemAppSettingForResult(final PreferenceFragment fragment, int requestCode) {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + fragment.getActivity().getPackageName()));
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void go2SystemAppSettingForResult(final Fragment fragment, int requestCode) {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + fragment.getActivity().getPackageName()));
        fragment.startActivityForResult(intent, requestCode);
    }

    public static boolean canHandleIntent(final Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolvedActivities = packageManager.queryIntentActivities(intent, 0);
        return resolvedActivities.size() > 0;
    }

    public static boolean canHandleSchema(final Context context, String uriString) {
        Intent intent = new Intent();
        intent.setData(Uri.parse(uriString));
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolvedActivities = packageManager.queryIntentActivities(intent, 0);
        return resolvedActivities.size() > 0;
    }

    public static boolean canHandleSchema(final Context context, String scheme, String action) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(action, Uri.parse(scheme));
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL);
        return list.size() > 0;
    }

    public static boolean canHandleByThirdPartApp(final Context context, String scheme) {
        try {
            PackageManager packageManager = context.getPackageManager();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(scheme));
            List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER);
            if (list == null || list.size() == 0) {
                return false;
            }
            for (ResolveInfo resolveInfo : list) {
                IntentFilter filter = resolveInfo.filter;
                if (filter == null) {
                    continue;
                }
                if (filter.countDataAuthorities() == 0 && filter.countDataPaths() == 0) {
                    continue;
                }
                if (resolveInfo.activityInfo == null) {
                    continue;
                }
                return true;
            }
        } catch (RuntimeException e) {
            Log.e(TAG, "Runtime exception while getting specialized handlers");
        }
        return false;
    }

    public static boolean takePhoto(final Fragment fragment, int requestCode, Uri outputUri) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        grantUriPermissionFor19(fragment.getActivity(), takePictureIntent, outputUri);
        if (takePictureIntent.resolveActivity(fragment.getActivity().getPackageManager()) != null) {
            fragment.startActivityForResult(takePictureIntent, requestCode);
            return true;
        } else {
            return false;
        }
    }

    public static boolean takePhoto(final Activity activity, int requestCode, Uri outputUri) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_FRONT);
        grantUriPermissionFor19(activity, takePictureIntent, outputUri);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(takePictureIntent, requestCode);
            return true;
        } else {
            return false;
        }
    }


    private static void grantUriPermissionFor19(final Activity activity, Intent intent, Uri outputUri) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                activity.grantUriPermission(packageName, outputUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        }
    }

    public static void pickFile(final Fragment fragment, int requestCode) {
        Intent filePickerIntent;
        filePickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        filePickerIntent.setType("*/*");
        filePickerIntent.addCategory(Intent.CATEGORY_OPENABLE);
        filePickerIntent.setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        fragment.startActivityForResult(filePickerIntent, requestCode);
    }

    public static void sendMailTo(final Context context, String uri) {
        Intent mailto = new Intent(Intent.ACTION_SENDTO);
        mailto.setData(Uri.parse(uri));
        context.startActivity(mailto);
    }

    public static void pasteToClipboard(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (!TextUtils.isEmpty(text)) {
            ClipData clip = ClipData.newPlainText(text, text);
            clipboard.setPrimaryClip(clip);
        } else {
            //MTR-11350
            clipboard.setPrimaryClip(ClipData.newPlainText(null, null));
        }
    }

    public static String copyFromClipboard(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard.hasPrimaryClip()) {
            ClipData clip = clipboard.getPrimaryClip();
            if (clip == null) {
                return "";
            }
            CharSequence charSequence = clip.getItemAt(0).getText();
            if (charSequence != null) {
                return charSequence.toString();
            }
        }
        return "";
    }

    public static boolean isContentProviderValid(final Context context, Uri uri) {
        if (uri == null) {
            return false;
        }
        for (PackageInfo packageInfo : context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS)) {
            ProviderInfo[] providers = packageInfo.providers;
            if (providers != null) {
                for (ProviderInfo provider : providers) {

                    if (TextUtils.equals(provider.authority, uri.getAuthority())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //    https://play.google.com/store/apps/details?id=com.ringcentral.android
    public static void go2PlayStore(final Context context, String appPackageName) {
        String playStoreUri = "market://details?id=" + appPackageName;
        if (!canHandleSchema(context, playStoreUri)) {
            playStoreUri = "https://play.google.com/store/apps/details?id=" + appPackageName;
        }
        go2App(context, playStoreUri);
    }

    public static void go2App(final Context context, String uri) {
        if (!canHandleSchema(context, uri, Intent.ACTION_VIEW)) {
            DialogUtil.showInstallBrowserAlert(context);
            return;
        }
        Intent intent = getGo2AppIntent(uri);
        try {
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.open_in)));
        } catch (Exception e) {
            Log.e(TAG, "Failed to go2App with uri: " + uri, e);
        }
    }

    public static Intent getGo2AppIntent(String uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public static void go2GoogleStoreOrBrowser(Context context, List<String> urls) {
        for (String url : urls) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            List<ResolveInfo> availableSoft = BaseApplication.getAppContext().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (availableSoft.size() > 0) {
                context.startActivity(intent);
                return;
            }
        }
    }

    public static void sendSMS(String text, Context context, List<String> recipients) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SENDTO);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String joinedRecipients = recipients != null ? TextUtils.join(", ", recipients) : "";
        intent.setData(Uri.parse("smsto:" + joinedRecipients));
        intent.putExtra("sms_body", text);
        context.startActivity(intent);
    }

    public static void shareToOtherApp(Context context, File file) {
        Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName(), file);
        shareToOtherApp(context, contentUri);
    }

    public static void shareToOtherApp(Context context, Uri contentUri) {
        Intent shareIntent = getShareFileIntent(context, contentUri);
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        Log.d(TAG, "Uri: " + contentUri);
        try {
            context.startActivity(Intent.createChooser(shareIntent, context.getResources().getText(R.string.open_in)));
        } catch (Exception e) {
            Log.e(TAG, "Failed to shareToOtherApp with uri: " + contentUri, e);
        }
    }

    public static void viewInOtherApp(Context context, File file) {
        Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName(), file);
        Intent shareIntent = getShareFileIntent(context, contentUri);
        shareIntent.setAction(Intent.ACTION_VIEW);
        shareIntent.setData(contentUri);
        Log.d(TAG, "Uri: " + contentUri);
        try {
            context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.open_in)));
        } catch (Exception e) {
            Log.e(TAG, "Failed to viewInOtherApp with uri: " + contentUri, e);
        }
    }

    public static void sendEmail(Context context, String[] addresses, String subject, String body, File file) {
        Intent intent = new Intent(file == null ? Intent.ACTION_SENDTO : Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        Uri contentUri = null;
        if (file != null) {
            contentUri = FileProvider.getUriForFile(context, context.getPackageName(), file);
            intent.setType("vnd.android.cursor.item/email");
        }
        setEmailIntentValues(intent, addresses, subject, body, contentUri);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(Intent.createChooser(intent, context.getResources().getText(R.string.send_email_ellipsis)));
        } else {
            DialogUtil.showInstallEmailAlert(context);
        }
    }

    public static void sendEmailWitchAttachment(
            @NonNull Context context, String[] addresses, String subject, String body, File file) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        Uri contentUri = null;
        if (file != null) {
            contentUri = FileProvider.getUriForFile(context, context.getPackageName(), file);
        }
        List<ResolveInfo> resolveInfos =
                context.getPackageManager().queryIntentActivities(intent, 0);
        if (resolveInfos.size() == 0) {
            DialogUtil.showInstallEmailAlert(context);
        } else {
            List<LabeledIntent> emailIntents = new ArrayList<>();
            for (ResolveInfo info : resolveInfos) {
                LabeledIntent emailIntent =
                        createLabeledIntent(
                                context, info, addresses, subject, body, contentUri);
                emailIntents.add(emailIntent);
            }
            Intent chooser =
                    Intent.createChooser(
                            emailIntents.remove(emailIntents.size() - 1),
                            context.getResources().getText(R.string.send_email_ellipsis));
            chooser.putExtra(
                    Intent.EXTRA_INITIAL_INTENTS,
                    emailIntents.toArray(new LabeledIntent[emailIntents.size()]));
            context.startActivity(chooser);
        }
    }

    private static void setEmailIntentValues(
            @NonNull Intent intent,
            String[] addresses,
            String subject,
            String body,
            Uri contentUri) {
        if (addresses != null) {
            intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        if (contentUri != null) {
            intent.putExtra(Intent.EXTRA_STREAM, contentUri);
        }
    }

    private static LabeledIntent createLabeledIntent(
            @NonNull Context context,
            ResolveInfo info,
            String[] addresses,
            String subject,
            String body,
            Uri contentUri) {
        LabeledIntent LabeledIntent =
                new LabeledIntent(
                        new Intent(Intent.ACTION_SEND),
                        info.activityInfo.packageName,
                        info.loadLabel(context.getPackageManager()),
                        info.icon);
        setEmailIntentValues(LabeledIntent, addresses, subject, body, contentUri);
        LabeledIntent.setComponent(
                new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
        return LabeledIntent;
    }

    private static Intent getShareFileIntent(Context context, Uri contentUri) {
        Intent shareIntent = new Intent();
        shareIntent.setType(context.getContentResolver().getType(contentUri));
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return shareIntent;
    }

    public static void shareToOtherApps(Context context, String text, @StringRes int customTitleRes) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");
        String customTitle = (customTitleRes == -1 || customTitleRes == 0) ? null :
                context.getString(customTitleRes);
        Intent chooserIntent = Intent.createChooser(intent, customTitle);
        List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(chooserIntent, 0);
        if (!resolveInfoList.isEmpty()) {
            context.startActivity(chooserIntent);
        } else {
            Log.w(TAG, "No other Apps found to share");
        }
    }

    /**
     * Do not use this function, Intent.EXTRA_INITIAL_INTENTS not work after Android Q (MTR-24444)
     *
     * @param context
     * @param text
     * @param excludePackages
     * @param chooseTitle
     */
    public static void sendToOtherApps(Context context, String text, List<String> excludePackages, String chooseTitle) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");
        ArrayList<Intent> shareIntents = new ArrayList<>();
        List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : resolveInfoList) {
            ActivityInfo activityInfo = info.activityInfo;
            if (!excludePackages.contains(activityInfo.packageName)) {
                LabeledIntent appIntent = new LabeledIntent(intent, activityInfo.packageName, info.labelRes, info.getIconResource());
                appIntent.setPackage(activityInfo.packageName);
                appIntent.setClassName(activityInfo.packageName, activityInfo.name);
                shareIntents.add(appIntent);
            }
        }
        if (!shareIntents.isEmpty()) {
            Intent chooserIntent = Intent.createChooser(shareIntents.remove(0), chooseTitle);
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, shareIntents.toArray(new Parcelable[]{}));
            context.startActivity(chooserIntent);
        } else {
            // TODO need PM provider the alert copy.
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void sendToOtherApps(Context context, String text, String chooseTitle, ComponentName excludedComponentName) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");
        ArrayList<ComponentName> componentNames = new ArrayList<>();
        componentNames.add(excludedComponentName);
        Intent chooserIntent = Intent.createChooser(intent, chooseTitle);
        chooserIntent.putExtra(Intent.EXTRA_EXCLUDE_COMPONENTS, componentNames.toArray(new Parcelable[]{}));
        List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(chooserIntent, 0);
        if (!resolveInfoList.isEmpty()) {
            context.startActivity(chooserIntent);
        } else {
            Log.w(TAG, "No other Apps found to share");
        }
    }

    public static List<ResolveInfo> getMailApps(Context context) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        return context.getPackageManager().queryIntentActivities(intent, 0);
    }

    public static void openUrlByChromeCustomTabs(@NonNull Activity activity, @NonNull String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder()
                .setStartAnimations(activity, R.anim.slide_right_in, R.anim.slide_left_out)
                .setExitAnimations(activity, R.anim.slide_left_in, R.anim.slide_right_out)
                .setShowTitle(true);
        CustomTabsIntent customTabsIntent = builder.build();
        CustomTabActivityHelper.openCustomTab(
                activity,
                customTabsIntent,
                Uri.parse(url),
                (act, uri) -> ExternalAppUtil.go2App(act, url)
        );
    }

    public static List<ResolveInfo> getCalendarApps(Context context) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        return context.getPackageManager().queryIntentActivities(intent, 0);
    }
}
