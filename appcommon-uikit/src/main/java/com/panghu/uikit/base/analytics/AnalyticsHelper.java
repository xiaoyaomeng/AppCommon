package com.panghu.uikit.base.analytics;

import android.content.Context;

import com.glip.mobile.analytics.AnalyticsWrapper;
import com.glip.mobile.appsflyer.AppsFlyerLibWraper;
import com.panghu.uikit.base.BaseApplication;
import com.panghu.uikit.utils.Log;
import com.segment.analytics.Traits;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by panghu on 9/12/17.
 */

public class AnalyticsHelper {
    private static final String TAG = "AnalyticsHelper";

    public static void logScreenCrumb(ScreenLocation screenLocation) {
        Context context = BaseApplication.getAppContext();
        if (context == null || screenLocation == null) {
            return;
        }

        AnalyticsWrapper.with(context).screen(null, screenLocation.getScreenName(), screenLocation.getProperties());
        Log.v(TAG, "Screen: " + screenLocation);
    }

    public static void logEventCrumb(EventCrumb crumbEvent) {
        Context context = BaseApplication.getAppContext();
        if (context == null || crumbEvent == null) {
            return;
        }
        AnalyticsWrapper.with(context).track(crumbEvent.getEventName(), crumbEvent.getProperties());
        AppsFlyerLibWraper.getInstance().trackEvent(context, crumbEvent.getEventName(), crumbEvent.getProperties());
        Log.v(TAG, "Event: " + crumbEvent);
    }

    public static void logIdentify(String identity, HashMap<String, String> traits) {
        Context context = BaseApplication.getAppContext();
        if (context == null || traits == null) {
            return;
        }
        AnalyticsWrapper.with(context).identify(identity, convertMapToTraits(traits), null);
        AppsFlyerLibWraper.getInstance().setCustomerUserId(identity);
        Log.v(TAG, "Identify: " + traits);
    }

    private static Traits convertMapToTraits(Map<String, String> stringMap) {
        Traits traits = new Traits();
        if (stringMap != null) {
            for (Map.Entry<String, String> entry : stringMap.entrySet()) {
                traits.putValue(entry.getKey(), entry.getValue());
            }
        }
        return traits;
    }
}
