package com.panghu.uikit.base.fragment.urlhandler

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.WebView

class TelUrlHandler : IUrlHandler {
    override fun handle(context: Context, view: WebView, url: String): Boolean {
        if (url.startsWith(SCHEME_TEL)) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
            return true
        }
        return false
    }

    companion object {
        const val SCHEME_TEL = "tel:"
    }
}