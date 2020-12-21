package com.panghu.uikit.base.fragment.urlhandler

import android.content.Context
import android.webkit.WebView

class LocalDefaultHandler : IUrlHandler {
    override fun handle(context: Context, view: WebView, url: String): Boolean {
        return url.startsWith(LOCAL_DEFAULT_URL)
    }

    companion object {
        const val LOCAL_DEFAULT_URL = "http://default"
    }
}