package com.panghu.uikit.base.analytics;

import com.segment.analytics.Properties;

/**
 * Created by dennis.jiang on 10/13/16.
 */

public class EventCrumb {
    public String getEventName() {
        return mEventName;
    }

    public Properties getProperties() {
        return mProperties;
    }

    private String mEventName;
    private Properties mProperties;

    public EventCrumb(String eventName) {
        mEventName = eventName;
        initProperties();
    }

    private void initProperties() {
        mProperties = new Properties();
        addProperty("endPoint", "Android");
    }

    public EventCrumb addProperty(String key, Object value) {
        mProperties.putValue(key, value);
        return this;
    }

    @Override
    public String toString() {
        return (mEventName != null ? mEventName : "null") +
                " " +
                (mProperties != null ? mProperties : "null");
    }
}
