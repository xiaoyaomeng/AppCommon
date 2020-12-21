package com.panghu.uikit.base.analytics

import android.content.res.Configuration

object ScreenOrientationAnalytics {
    private const val GLIP_MOBILE_LANDSCAPE_PORTRAIT = "Glip_Mobile_landscapePortrait"
    private const val ACTION = "action"
    private const val PAGE = "page"

    @JvmStatic
    fun logOrientationChanged(orientation: Int, page: String) {
        val action = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            "portrait to landscape"
        } else {
            "landscape to portrait"
        }
        val eventCrumb = EventCrumb(GLIP_MOBILE_LANDSCAPE_PORTRAIT)
        eventCrumb
            .addProperty(ACTION, action)
            .addProperty(PAGE, page)
        AnalyticsHelper.logEventCrumb(eventCrumb)
    }
}