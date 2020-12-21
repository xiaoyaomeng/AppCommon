package com.panghu.uikit.base.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.WebView;

import com.panghu.uikit.R;
import com.panghu.uikit.base.analytics.ScreenCategory;
import com.panghu.uikit.base.analytics.ScreenLocation;
import com.panghu.uikit.base.fragment.WebViewFragment;
import com.panghu.uikit.base.fragment.WebViewParams;

/**
 * An activity to show simple web page in web view.
 *
 * <p>Created by panghu on 5/11/16.
 */
public class WebViewActivity extends AbstractBaseActivity
        implements WebViewFragment.WebViewClientListener {

    private static final String EXTRA_SCREEN_NAME = "SCREEN_NAME";
    private String mUrl = "";
    private String mScreenName;

    public static boolean start(Context context, Uri uri, WebViewParams params) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.setData(uri);
        intent.putExtra(EXTRA_TITLE, params.getTitle());
        intent.putExtra(EXTRA_SCREEN_NAME, params.getScreenName());
        context.startActivity(intent);
        return true;
    }

    public static void start(Context context, Uri uri, String title, String screenName) {
        WebViewParams browserParams = new WebViewParams();
        browserParams.setTitle(title);
        browserParams.setScreenName(screenName);
        start(context, uri, browserParams);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.common_web_view_container);
        parseIntent();
        if (savedInstanceState == null) {
            loadWebFragment();
        }
    }

    protected final void loadWebFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.web_view_fragment_container, onCreateFragment())
                .commit();
    }

    protected Fragment onCreateFragment() {
        return new WebViewFragment.Builder(mUrl).build();
    }

    @Override
    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.web_view_fragment_container);
    }

    @Override
    public ScreenLocation screenCrumb() {
        ScreenLocation screenLocation = super.screenCrumb();
        if (screenLocation == null && !TextUtils.isEmpty(mScreenName)) {
            screenLocation = new ScreenLocation(ScreenCategory.AUTHENTICATION, mScreenName);
        }
        return screenLocation;
    }

    protected void removeSessionCookies() {
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookies(null);
        } else {
            cookieManager.removeSessionCookie();
        }
    }

    protected void parseIntent() {
        Uri uri = null;
        String title = null;
        String screenName = null;
        Intent intent = getIntent();
        if (intent != null) {
            uri = intent.getData();
            title = intent.getStringExtra(EXTRA_TITLE);
            screenName = intent.getStringExtra(EXTRA_SCREEN_NAME);
        }
        if (uri != null) {
            mUrl = uri.toString();
        }

        mScreenName = screenName;

        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }
    }

    @Override
    public boolean shouldOverrideOpenWindow(
            WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        return false;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }

    @Override
    public void onPageFinished(WebView view, String url) {}

    @Override
    public void onReceivedError(
            WebView view, int errorCode, String description, String failingUrl) {}
}
