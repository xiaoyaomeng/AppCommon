package com.panghu.uikit.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.app.MediaRouteButton


/**
 *
 * Gross way of unwrapping the Activity
 * It's just a way to bubble up trough all the base context,
 * till the activity is found, or exit the loop when the root context is found.
 * Cause the root context will have a null baseContext, leading to the end of the loop.
 *
 * @see MediaRouteButton#getActivity()
 *
 */
tailrec fun Context?.activity(): Activity? = when (this) {
    is Activity -> this
    else -> (this as? ContextWrapper)?.baseContext?.activity()
}
