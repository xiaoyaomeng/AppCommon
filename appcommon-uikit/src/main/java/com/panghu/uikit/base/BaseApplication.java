package com.panghu.uikit.base;

import android.app.Application;
import android.content.ContextWrapper;

public abstract class BaseApplication extends Application {

    private static BaseApplication sBaseApplication;

    //Warning: Avoid to use this unless it is really needed globally.
    public static ContextWrapper getAppContext() {
        return sBaseApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sBaseApplication = this;
    }
}
