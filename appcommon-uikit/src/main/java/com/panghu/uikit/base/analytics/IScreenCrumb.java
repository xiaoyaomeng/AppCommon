package com.panghu.uikit.base.analytics;

/**
 * Created by dennis.jiang on 8/24/16.
 */
public interface IScreenCrumb {
    ScreenLocation screenCrumb();
    void leaveScreenCrumb(ScreenLocation screenLocation);
}
