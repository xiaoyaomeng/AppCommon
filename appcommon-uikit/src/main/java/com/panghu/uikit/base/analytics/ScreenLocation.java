package com.panghu.uikit.base.analytics;

import com.glip.crumb.model.ScreenMeta;
import com.segment.analytics.Properties;

/**
 * Created by dennis.jiang on 8/24/16.
 */
public class ScreenLocation extends ScreenMeta {

    private Properties mProperties;

    public Properties getProperties() {
        return mProperties;
    }

    public ScreenLocation(String screenCategory, String screenName) {
        super(screenCategory, screenName);
        initProperties();
    }

    private void initProperties() {
        mProperties = new Properties();
        addProperty("endPoint", "Android");
    }

    public ScreenLocation addProperty(String key, Object value) {
        mProperties.putValue(key, value);
        return this;
    }

    @Override
    public String toString() {
        return (mScreenCategory != null ? mScreenCategory : "null") +
                ":" +
                (mScreenName != null ? mScreenName : "null") +
                ":" +
                mProperties;
    }
}
