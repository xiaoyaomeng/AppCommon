package com.panghu.uikit.base.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebHistoryItem;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.panghu.uikit.BuildConfig;
import com.panghu.uikit.R;
import com.panghu.uikit.base.fragment.urlhandler.IUrlHandler;
import com.panghu.uikit.base.fragment.urlhandler.LocalDefaultHandler;
import com.panghu.uikit.executors.BgThreadPoolExecutor;
import com.panghu.uikit.utils.Log;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A fragment that displays a WebView.
 *
 */
public class WebViewFragment extends AbstractBaseFragment implements View.OnClickListener {

    private static final String TAG = "WebViewFragment";
    private static final boolean DEBUG = BuildConfig.DEBUG;
    private static final String ARG_PARAMS = "params";
    public static final String BLANK_URL = "about:blank";
    private static final String REGULAR_HTTP_ERROR_CODE = "^[4-5][0-9]{2}";
    private static final int DEFAULT_ERROR_CODE = 500;
    private static final boolean DEFAULT_BACK_TO_PREVIOUS_ENABLE = false;
    private static final boolean DEFAULT_CHECK_URL_ENABLE = false;
    private WebView mWebView;
    private View mBrowserErrorView;
    private WebViewClientListener mWebViewClientListener;
    private WebViewSetupListener mWebViewSetupListener;
    private OnErrorViewClickListener mOnErrorViewClickListener;
    private String mUrlToReload = "";
    private Params mParams;
    private Boolean mIsLoading = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseArguments(getArguments());
    }

    private void parseArguments(Bundle arguments) {
        if (arguments != null) {
            mParams = arguments.getParcelable(ARG_PARAMS);
        }
        if (mParams == null) {
            mParams = new Params();
        }
    }

    @Nullable
    @Override
    protected View onCreateContentView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.base_browser_screen_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View container) {
        mWebView = container.findViewById(R.id.browser_web_view);
        initWebView();
        mBrowserErrorView = container.findViewById(R.id.browser_error_view);
        mBrowserErrorView.setOnClickListener(this);
    }

    @SuppressLint("JavascriptInterface")
    private void initWebView() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setCacheMode(mParams.getCacheMode());
        if (mParams.isSupportZoom()) {
            webSettings.setSupportZoom(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setDisplayZoomControls(false);
        }

        //Mixed content using HTTP and HTTPS on WebViews are disabled by default start Lollipop
        //So we need change to default WebView setting on Lollipop using:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.setWebChromeClient(new GlipWebChromeClient());
        mWebView.setWebViewClient(new GlipWebViewClient());
        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
        mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        if (mWebViewSetupListener != null) {
            WebViewSetter setter = new WebViewSetter(mWebView);
            mWebViewSetupListener.onWebViewSetup(setter);
        }
        if (!TextUtils.isEmpty(mParams.mHtmlData)) {
            loadHtml(mParams.mHtmlData);
        } else {
            loadPage(mParams.getUrl());
        }
    }

    private void showErrorView() {
        mBrowserErrorView.setVisibility(View.VISIBLE);
        mWebView.setVisibility(View.GONE);
    }

    private void hideErrorView() {
        mBrowserErrorView.setVisibility(View.GONE);
        mWebView.setVisibility(View.VISIBLE);
    }

    public boolean isErrorViewVisible() {
        return mBrowserErrorView.getVisibility() == View.VISIBLE;
    }

    @Override
    public boolean onBackPressed() {
        if (mParams.isBackToPrevious() && !isOriginalPage()) {
            mWebView.goBack();
            return true;
        }
        return super.onBackPressed();
    }

    public boolean isOriginalPage() {
        if (mWebView != null && mWebView.canGoBack()) {
            WebBackForwardList webBackForwardList = mWebView.copyBackForwardList();
            WebHistoryItem webHistoryItem =
                    webBackForwardList.getItemAtIndex(webBackForwardList.getCurrentIndex() - 1);
            return BLANK_URL.equals(webHistoryItem.getUrl());
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.browser_error_view) {
            if (mOnErrorViewClickListener != null) {
                mOnErrorViewClickListener.onClick();
            } else {
                loadPage(mUrlToReload);
            }
            hideErrorView();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof WebViewClientListener) {
            mWebViewClientListener = (WebViewClientListener) context;
        }
        if (context instanceof WebViewSetupListener) {
            mWebViewSetupListener = (WebViewSetupListener) context;
        }
        if (context instanceof OnErrorViewClickListener) {
            mOnErrorViewClickListener = (OnErrorViewClickListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mWebViewClientListener = null;
    }

    @Override
    public void showProgressBar() {
        super.showProgressBar();
        mProgressBarContainer.setClickable(false);
    }

    @Override
    public void hideProgressBar() {
        if (mProgressBarContainer != null) {
            mProgressBarContainer.setClickable(true);
        }
        super.hideProgressBar();
    }

    public void loadPage(final String url) {
        Log.d(TAG, DEBUG ? ("Url: " + url) : "Enter");
        showProgressBar();
        if (mParams.isNeedCheckUrl()) {
            new DetectUrlTask(this, url)
                    .executeOnExecutor(BgThreadPoolExecutor.getInstance());
        } else {
            openUrlByWebView(url);
        }
    }

    public String getCurrentWebUrl() {
        return mWebView.getUrl();
    }

    public void reload() {
        if (mWebView != null) {
            mWebView.reload();
        }
    }

    public Boolean isLoading() {
        return mIsLoading;
    }

    private WebView getWebView() {
        return mWebView;
    }

    private void openUrlByWebView(String url) {
        mWebView.loadUrl(url);
    }

    private boolean checkUrl(String url) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setUseCaches(false);
            int statusCode = conn.getResponseCode();
            if (urlCanOpen(statusCode)) {
                return true;
            } else {
                String msg = "Status code:" + statusCode;
                if (DEBUG) {
                    msg += ", Url:" + url;
                }
                Log.d(TAG, msg);
            }
        } catch (Exception e) {
            String msg = DEBUG ? "Error in check url: " + url : "Error in check url";
            Log.e(TAG, msg, e);
        }
        return false;
    }

    private boolean urlCanOpen(int statusCode) {
        Pattern p = Pattern.compile(REGULAR_HTTP_ERROR_CODE);
        Matcher m = p.matcher(String.valueOf(statusCode));
        return !m.matches();
    }

    private void handleError(WebView view, int errorCode, String description, String failingUrl) {
        if (!isUiReady()) {
            return;
        }
        if (errorCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
            // already handle this error code.
            Log.i(TAG, "handleError(): HTTP_UNAUTHORIZED");
            return;
        }
        if (TextUtils.isEmpty(mParams.getLocalHtml())) {
            String msg = "ErrorCode: " + errorCode + ", description: " + description;
            if (DEBUG) {
                msg +=  ", failingUrl: " + failingUrl;
            }
            Log.d(TAG, msg);
            if (!TextUtils.isEmpty(failingUrl) && !TextUtils.equals(failingUrl, BLANK_URL)) {
                mUrlToReload = failingUrl;
            } else if (!TextUtils.isEmpty(view.getUrl()) && !TextUtils.equals(view.getUrl(), BLANK_URL)) {
                mUrlToReload = view.getUrl();
            } else {
                mUrlToReload = mParams.getUrl();
            }
            //Replace error page with custom error view.
            view.loadUrl(BLANK_URL);
            showErrorView();
            if (mWebViewClientListener != null) {
                mWebViewClientListener.onReceivedError(view, errorCode, description, failingUrl);
            }
        } else {
            Log.d(TAG, "Use local html content. ");
            loadHtml(mParams.getLocalHtml());
        }
    }

    private void loadHtml(String data) {
        mWebView.loadDataWithBaseURL(
                LocalDefaultHandler.LOCAL_DEFAULT_URL, data, "text/html", "utf-8", null);
    }

    private class GlipWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onCreateWindow(
                WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            return mWebViewClientListener != null
                    && mWebViewClientListener.shouldOverrideOpenWindow(
                    view, isDialog, isUserGesture, resultMsg);
        }
    }

    private class GlipWebViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            Log.e(TAG, error.toString());
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, DEBUG ? ("Url: " + url) : "Enter");
            IUrlHandler urlHandler = IUrlHandler.Companion.from(url);
            if (urlHandler != null && urlHandler.handle(requireContext(), view, url)) {
                return true;
            }
            return mWebViewClientListener != null
                    && mWebViewClientListener.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d(TAG, DEBUG ? ("Url: " + url) : "Enter");
            super.onPageFinished(view, url);
            mIsLoading = false;
            hideProgressBar();
            if (mWebViewClientListener != null) {
                mWebViewClientListener.onPageFinished(view, url);
            }
        }

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(
                WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (request.isForMainFrame()) {
                handleError(
                        view,
                        error.getErrorCode(),
                        error.getDescription().toString(),
                        request.getUrl().toString());
            }
        }

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceivedHttpError(
                WebView view, WebResourceRequest request, WebResourceResponse error) {
            super.onReceivedHttpError(view, request, error);
            if (request.isForMainFrame()) {
                handleError(
                        view,
                        error.getStatusCode(),
                        error.getReasonPhrase(),
                        request.getUrl().toString());
            }
        }

        @Override
        public void onReceivedError(
                WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return;
            }
            handleError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(TAG, DEBUG ? ("Url: " + url) : "Enter");
            super.onPageStarted(view, url, favicon);
            mIsLoading = true;
            if (isUiReady()) {
                showProgressBar();
            }
        }

        @Override
        public void onReceivedHttpAuthRequest(
                final WebView view,
                final HttpAuthHandler handler,
                final String host,
                final String realm) {
            Log.d(TAG, "onReceivedHttpAuthRequest() host: " + host + " isUiReady: " + isUiReady());
            if (isUiReady()) {
                showHttpAuthDialog(handler, host);
            } else {
                handler.cancel();
            }
        }
    }

    private void showHttpAuthDialog(HttpAuthHandler handler, String host) {
        View contentView =
                getLayoutInflater().inflate(R.layout.sso_login_authentication_dialog, null, false);
        TextView contentTextView = contentView.findViewById(R.id.content_text);
        EditText userNameEditView = contentView.findViewById(R.id.username_edit);
        EditText passwordEditView = contentView.findViewById(R.id.password_edit);

        contentTextView.setText(getString(R.string.sso_login_authentication_content, "https://" + host));

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle(R.string.authentication_required)
                .setView(contentView)
                .setCancelable(false)
                .setPositiveButton(R.string.sign_in, (dialog1, which) -> {
                    handler.proceed(userNameEditView.getText().toString(), passwordEditView.getText().toString());
                })
                .setNegativeButton(R.string.cancel, (dialog1, which) -> {
                    handler.cancel();
                    finish();
                })
                .create();
        dialog.show();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isSignInEnabled = !TextUtils.isEmpty(userNameEditView.getText().toString())
                        && !TextUtils.isEmpty(passwordEditView.getText().toString());
                dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(isSignInEnabled);
            }
        };

        userNameEditView.addTextChangedListener(watcher);
        passwordEditView.addTextChangedListener(watcher);
    }

    /**
     * For MTR-2659, we need to show custom error instead of http error page. Because there is no
     * error callback for HTTP status code 4xx vs 5xx, we need to detect ourselves.
     */
    private static class DetectUrlTask extends AsyncTask<Void, Void, Boolean> {
        private WeakReference<WebViewFragment> mFragmentReference;
        private String mUrl;

        DetectUrlTask(WebViewFragment fragment, String url) {
            mFragmentReference = new WeakReference<>(fragment);
            mUrl = url;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return mFragmentReference.get() != null && mFragmentReference.get().checkUrl(mUrl);
        }

        @Override
        protected void onPostExecute(Boolean shouldLoad) {
            super.onPostExecute(shouldLoad);
            WebViewFragment fragment = mFragmentReference.get();
            if (fragment != null) {
                if (shouldLoad) {
                    fragment.openUrlByWebView(mUrl);
                } else {
                    fragment.hideProgressBar();
                    fragment.handleError(fragment.getWebView(), DEFAULT_ERROR_CODE, "", "");
                }
            }
        }
    }

    public interface WebViewClientListener {
        boolean shouldOverrideOpenWindow(
                WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg);

        boolean shouldOverrideUrlLoading(WebView view, String url);

        void onPageFinished(WebView view, String url);

        void onReceivedError(WebView view, int errorCode, String description, String failingUrl);
    }

    public interface WebViewSetupListener {
        void onWebViewSetup(WebViewSetter webViewSetter);
    }

    public interface OnErrorViewClickListener {
        void onClick();
    }

    public static class WebViewSetter {

        private WebView mWebView;

        private WebViewSetter(WebView webView) {
            this.mWebView = webView;
        }

        @SuppressLint("JavascriptInterface")
        public void addJavascriptInterface(Object javascriptInterfaceObject, String name) {
            mWebView.addJavascriptInterface(javascriptInterfaceObject, name);
        }

        public void setSaveFormData(boolean save) {
            mWebView.getSettings().setSaveFormData(save);
        }
    }

    private static class Params implements Parcelable {

        private String mUrl = BLANK_URL;
        private boolean mBackToPrevious = DEFAULT_BACK_TO_PREVIOUS_ENABLE;
        private boolean mNeedCheckUrl = DEFAULT_CHECK_URL_ENABLE;
        private String mLocalHtml = null;
        private int mCacheMode = WebSettings.LOAD_DEFAULT;
        private String mHtmlData = null;
        private boolean mSupportZoom = false;

        public Params() {
        }

        public String getUrl() {
            return mUrl;
        }

        public String getLocalHtml() {
            return mLocalHtml;
        }

        public boolean isBackToPrevious() {
            return mBackToPrevious;
        }

        public boolean isNeedCheckUrl() {
            return mNeedCheckUrl;
        }

        public int getCacheMode() {
            return mCacheMode;
        }

        public boolean isSupportZoom() {
            return mSupportZoom;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mUrl);
            dest.writeByte(this.mBackToPrevious ? (byte) 1 : (byte) 0);
            dest.writeByte(this.mNeedCheckUrl ? (byte) 1 : (byte) 0);
            dest.writeString(this.mLocalHtml);
            dest.writeInt(this.mCacheMode);
            dest.writeString(this.mHtmlData);
        }

        protected Params(Parcel in) {
            this.mUrl = in.readString();
            this.mBackToPrevious = in.readByte() != 0;
            this.mNeedCheckUrl = in.readByte() != 0;
            this.mLocalHtml = in.readString();
            this.mCacheMode = in.readInt();
            this.mHtmlData = in.readString();
        }

        public static final Creator<Params> CREATOR = new Creator<Params>() {
            @Override
            public Params createFromParcel(Parcel source) {
                return new Params(source);
            }

            @Override
            public Params[] newArray(int size) {
                return new Params[size];
            }
        };
    }

    public static class Builder {
        private Params mParams;

        public Builder(String url) {
            mParams = new Params();
            mParams.mUrl = url;
        }

        public Builder setBackToPrevious(boolean backToPrevious) {
            mParams.mBackToPrevious = backToPrevious;
            return this;
        }

        public Builder setNeedCheckUrl(boolean needCheckUrl) {
            mParams.mNeedCheckUrl = needCheckUrl;
            return this;
        }

        public Builder setLocalHtml(String localHtml) {
            mParams.mLocalHtml = localHtml;
            return this;
        }

        public Builder setCacheMode(int cacheMode) {
            mParams.mCacheMode = cacheMode;
            return this;
        }

        public Builder setHtmlData(String htmlData) {
            mParams.mHtmlData = htmlData;
            return this;
        }

        public Builder setSupportZoom(boolean supportZoom) {
            mParams.mSupportZoom = supportZoom;
            return this;
        }

        public WebViewFragment build() {
            WebViewFragment fragment = new WebViewFragment();
            Bundle arguments = new Bundle();
            arguments.putParcelable(ARG_PARAMS, mParams);
            fragment.setArguments(arguments);
            return fragment;
        }
    }

}
