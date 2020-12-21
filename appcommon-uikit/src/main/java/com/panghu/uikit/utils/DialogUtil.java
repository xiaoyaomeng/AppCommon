package com.panghu.uikit.utils;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import com.panghu.uikit.R;


public class DialogUtil {

    public static void showSimpleAlert(final Context context,
                                       @StringRes int titleRes,
                                       @StringRes int messageRes) {
        new AlertDialog.Builder(context)
                .setTitle(titleRes)
                .setMessage(messageRes)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    public static void showSimpleAlert(final Context context, String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    public static void showSimpleAlert(final Context context,
                                       @StringRes int titleRes,
                                       @StringRes int messageRes,
                                       Dialog.OnDismissListener onDismissListener) {
        new AlertDialog.Builder(context)
                .setTitle(titleRes)
                .setMessage(messageRes)
                .setOnDismissListener(onDismissListener)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    public static void showNoInternetAlert(final Context context) {
        showNoInternetAlert(context, null);
    }

    public static void showNoInternetAlert(final Context context,
                                           Dialog.OnDismissListener onDismissListener) {
        showSimpleAlert(context, R.string.no_internet_connection,
                R.string.no_internet_connection_message, onDismissListener);
    }

    public static void showInstallBrowserAlert(final Context context) {
        showSimpleAlert(context,
                R.string.no_browser_app_found,
                R.string.please_install_a_browser_app);
    }

    public static void showInstallEmailAlert(final Context context) {
        showSimpleAlert(context, R.string.no_email_app_found, R.string.please_install_an_email_app);
    }
}
