package com.panghu.uikit.base.activity;

import android.view.ViewGroup;

/**
 * The interface of banner controller.
 *
 * @author panghu
 */

public interface IBannerController {
    void init(IBannerHostView bannerHostView, ViewGroup bannerContainerView);
    void setBannerFlags(int flags);
    void onStart();
    void onStop();
}
