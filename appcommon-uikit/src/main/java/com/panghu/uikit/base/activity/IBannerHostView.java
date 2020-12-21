package com.panghu.uikit.base.activity;

import com.panghu.uikit.base.IUIView;

/**
 * Created by panghu on 5/31/17.
 */

public interface IBannerHostView extends IUIView{
    void setBannerController(IBannerController bannerController);
    void setBannerFlags(int flags);
    void onBannerRefreshed();
    default void addBannerFlag(int flag) {}
    default void removeBannerFlag(int flag) {}
}
