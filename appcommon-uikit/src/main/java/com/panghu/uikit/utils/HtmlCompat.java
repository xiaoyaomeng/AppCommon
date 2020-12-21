package com.panghu.uikit.utils;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

/**
 * @author mason.su
 */
public class HtmlCompat {
    private static final String TAG = "HtmlCompat";

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {

        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY) : Html.fromHtml(html);
    }

    public static Spanned fromHtml(String html, Html.ImageGetter imageGetter, Html.TagHandler tagHandler) {
        try {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                    Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY, imageGetter, tagHandler) : Html.fromHtml(html, imageGetter, tagHandler);
        } catch (Exception e) {
            Log.e(TAG, "Error in fromHtml", e);
            return fromHtml(html);
        }
    }
}
