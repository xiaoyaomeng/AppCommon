package com.panghu.uikit.utils;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;

import com.glip.widgets.span.BackgroundColorWithoutLineHeightSpan;
import com.glip.widgets.span.CustomHtmlTagHandler;
import com.glip.widgets.span.LongClickableURLSpan;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by barry.li on 7/8/16.
 */
public class StringUtil {
    public static Spannable highlightStringKey(@NonNull Context context, @NonNull String content, @NonNull String query, @ColorRes int colorRes) {
        Spannable spannable = new SpannableString(content);
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(query)) {
            return spannable;
        }
        String[] singleKeyArrays = query.split("\\s+");
        if (singleKeyArrays.length > 0) {
            for (String keyValue : singleKeyArrays) {
                Map<Integer, Integer> indexMap = getIndexes(content, keyValue);
                for (Integer key : indexMap.keySet()) {
                    Integer value = indexMap.get(key);
                    spannable.setSpan(new BackgroundColorSpan(ContextCompat.getColor(context, colorRes)), key, value, 0);
                }
            }
        }
        return spannable;
    }

    public static SpannableString highlightHtmlStringKey(@NonNull String content, @NonNull String query, @ColorInt int color, int textHeight) {
        SpannableString spannableString = new SpannableString(fromHtml(content));
        String newText = spannableString.toString();

        String[] singleKeyArrays = query.split("\\s+");
        if (singleKeyArrays.length > 0) {
            for (String keyValue : singleKeyArrays) {
                Map<Integer, Integer> indexMap = getIndexes(newText, keyValue);
                for (Integer key : indexMap.keySet()) {
                    Integer value = indexMap.get(key);
                    spannableString.setSpan(new BackgroundColorWithoutLineHeightSpan(color, textHeight), key, value, 0);
                }
            }
        }

        return spannableString;
    }

    public static SpannableString createSpannableFromHtml(@NonNull String content) {
        return new SpannableString(fromHtml(content));
    }

    private static Map<Integer, Integer> getIndexes(String content, String query) {
        Map<Integer, Integer> indexMap = new TreeMap<>();
        if (TextUtils.isEmpty(query)) {
            return indexMap;
        }
        int queryLength = query.length();
        String text = content.toLowerCase(Locale.US);
        String queryText = query.toLowerCase(Locale.US);
        int i, y;

        i = text.indexOf(queryText);
        if (i == -1) {
            return indexMap;
        }
        y = i + queryLength;
        indexMap.put(i, y);

        while (i >= 0) {
            i = text.indexOf(queryText, y);
            if (i != -1 && i < content.length()) {
                y = i + queryLength;
                indexMap.put(i, y);
            }
        }
        return indexMap;
    }

    public static String getLastPathSegment(String content) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        String[] segments = content.split("/");
        if (segments.length > 0) {
            return segments[segments.length - 1];
        }
        return "";
    }

    private static Spanned fromHtml(@NonNull String content) {
        CustomHtmlTagHandler tagHandler = new CustomHtmlTagHandler();
        String commentsRemoved = content.replaceAll("(?s)<!--.*?-->", "");
        return new SpannableString(HtmlCompat.fromHtml(commentsRemoved, null, tagHandler));
    }

    public static String getBodyFromHtml(String html) {
        if (TextUtils.isEmpty(html)) {
            return "";
        }
        if (html.startsWith("<body>")) {
            return html;
        }
        if (!html.startsWith("<html>")) {
            return "<body>" + html + "</body>";
        }
        Document doc = Jsoup.parse(html);
        return doc.select("body").first().toString();
    }

    public static String getBodyContent(String body) {
        body = body.replace("<body>", "");
        body = body.replace("</body>", "");
        return body.trim();
    }

    public static String getDomainFromEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return "";
        }
        int index = email.lastIndexOf("@");
        if (index < 0 || index == email.length() - 1) {
            return "";
        }
        return email.substring(index + 1, email.length());
    }

    public static String removeSpaces(String source) {
        return source != null ? source.replace(" ", "") : null;
    }

    public static void removeUrlUnderlines(Spannable spannable, int linkFontColor, int linkFontBgColor) {
        if (spannable == null) {
            return;
        }
        URLSpan[] spans = spannable.getSpans(0, spannable.length(), URLSpan.class);

        for (URLSpan span : spans) {
            int start = spannable.getSpanStart(span);
            int end = spannable.getSpanEnd(span);
            spannable.removeSpan(span);
            span = new LongClickableURLSpan(span.getURL(), linkFontColor, linkFontBgColor);
            spannable.setSpan(span, start, end, 0);
        }
    }

    public static void setOnClickURLSpanListener(Spannable spannable, LongClickableURLSpan.OnClickUrlListener onClickUrlListener) {
        if (spannable == null) {
            return;
        }
        LongClickableURLSpan[] spans = spannable.getSpans(0, spannable.length(), LongClickableURLSpan.class);

        for (LongClickableURLSpan span : spans) {
            span.setClickUrlListener(onClickUrlListener);
        }
    }

    public static SpannableString createBoldSpannableString(String text) {
        SpannableString spannableString = new SpannableString(text);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(boldSpan, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

}
