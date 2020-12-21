package com.panghu.uikit.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.panghu.uikit.utils.Log;

import java.lang.ref.WeakReference;

/**
 * Class to handle the event when proximity to device changed.
 */
public class ProximitySensor {
    private static final String TAG = "ProximitySensor";

    private SensorManager mSensorManager;
    private Sensor mProximitySensor;
    private ProximitySensorListener mProximitySensorListener;

    private WeakReference<OnProximityChangedListener> mOnProximityChangedListenerRef;

    private static class InstanceHolder {
        private static final ProximitySensor sInstance = new ProximitySensor();
    }

    private ProximitySensor() {
    }

    public static ProximitySensor getInstance() {
        return InstanceHolder.sInstance;
    }

    public void init(Context context, OnProximityChangedListener onProximityChangedListener) {
        mOnProximityChangedListenerRef = new WeakReference<>(onProximityChangedListener);
        try {
            mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            mProximitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            if (mProximitySensorListener == null) {
                mProximitySensorListener = new ProximitySensorListener();
            }
            if (mProximitySensor != null) {
                mSensorManager.registerListener(
                        mProximitySensorListener,
                        mProximitySensor,
                        SensorManager.SENSOR_DELAY_NORMAL);
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "Exception when initProximity: ", throwable);
        }
    }

    public void release() {
        if (mSensorManager != null) {
            try {
                mSensorManager.unregisterListener(mProximitySensorListener);
            } catch (Throwable t) {
                Log.e(TAG, "Error in unregister SensorListener", t);
            }
        }
        mProximitySensorListener = null;
        mSensorManager = null;
        mProximitySensor = null;
    }

    private class ProximitySensorListener implements SensorEventListener {
        private static final float PROXIMITY_THRESHOLD = 5.0f;
        private ProximityMode mPreProximityMode;

        @Override
        public void onSensorChanged(SensorEvent event) {
            float distance = event.values[0];
            ProximityMode proximityMode =
                    getProximityMode(distance, event.sensor.getMaximumRange());
            OnProximityChangedListener onProximityChangedListener =
                    mOnProximityChangedListenerRef != null
                            ? mOnProximityChangedListenerRef.get()
                            : null;
            if (onProximityChangedListener != null && mPreProximityMode != proximityMode) {
                mPreProximityMode = proximityMode;
                onProximityChangedListener.onProximityModeChanged(proximityMode);
                Log.d(TAG, "Distance : " + distance + " , proximityMode : " + proximityMode);
            }
        }

        private ProximityMode getProximityMode(float distance, float maximumRange) {
            if (distance >= 0.0 && distance < PROXIMITY_THRESHOLD && distance < maximumRange) {
                return ProximityMode.NEAR;
            } else {
                return ProximityMode.FAR;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }
}
