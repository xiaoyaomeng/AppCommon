package com.panghu.uikit.base.fragment.urlhandler

import android.content.Context
import android.webkit.WebView

interface IUrlHandler {

    fun handle(context: Context, view: WebView, url: String): Boolean

    companion object {
        fun from(url: String): IUrlHandler? {
            if (url.startsWith(TelUrlHandler.SCHEME_TEL)) {
                return TelUrlHandler()
            } else if (url.startsWith(LocalDefaultHandler.LOCAL_DEFAULT_URL)) {
                return LocalDefaultHandler();
            }
            return null
        }
    }

}