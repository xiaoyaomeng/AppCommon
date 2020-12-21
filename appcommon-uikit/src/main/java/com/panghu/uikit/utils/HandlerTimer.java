/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.panghu.uikit.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

public class HandlerTimer extends Handler {
    private final Runnable mCallback;
    private final Runnable mInternalDelayCallTimerCallback;
    private final Runnable mInternalPeriodicCallTimerCallback;
    private long mLastReportedTime;
    private long mInterval;
    private boolean mRunning;

    public HandlerTimer(Looper looper, Runnable callback) {
        super(looper);

        mInterval = 0;
        mLastReportedTime = 0;
        mRunning = false;
        mCallback = callback;
        mInternalPeriodicCallTimerCallback = new PeriodicCallTimerCallback();
        mInternalDelayCallTimerCallback = new DelayCallTimerCallback();
    }

    public HandlerTimer(Runnable callback) {
        this(Looper.myLooper(), callback);
    }

    public boolean start(long interval) {
        if (interval <= 0) {
            return false;
        }

        cancel();

        mInterval = interval;
        mLastReportedTime = SystemClock.uptimeMillis();

        mRunning = true;
        periodicUpdateTimer();

        return true;
    }

    public boolean startAfter(long delay, final long interval) {
        if (delay <= 0 || interval <= 0) {
            return false;
        }

        cancel();
        mInterval = interval;
        postDelayed(mInternalDelayCallTimerCallback, delay);
        return true;
    }


    public void cancel() {
        removeCallbacks(mInternalDelayCallTimerCallback);
        removeCallbacks(mInternalPeriodicCallTimerCallback);
        mRunning = false;
    }

    private void periodicUpdateTimer() {
        if (!mRunning) {
            return;
        }

        final long now = SystemClock.uptimeMillis();
        long nextReport = mLastReportedTime + mInterval;
        while (now >= nextReport) {
            nextReport += mInterval;
        }

        postAtTime(mInternalPeriodicCallTimerCallback, nextReport);
        mLastReportedTime = nextReport;

        mCallback.run();
    }

    public boolean isRunning(){
        return mRunning;
    }

    private class PeriodicCallTimerCallback implements Runnable {
        @Override
        public void run() {
            periodicUpdateTimer();
        }
    }

    private class DelayCallTimerCallback implements Runnable {

        @Override
        public void run() {
            start(mInterval);
        }
    }
}
