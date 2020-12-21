package com.panghu.uikit.os

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Observable
import android.text.format.DateFormat

class DateFormatObservable(private val context: Context) :
    Observable<DateFormatObservable.DateFormatChangeListener>() {


    private var isReceiverRegistered = false
    private var is24HourFormat = false
    private val broadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action.equals(Intent.ACTION_TIME_CHANGED)) {
                    if (checkDateFormatChange()) {
                        notifyDateFormatChanged()
                    }
                }
            }
        }
    }

    private fun checkDateFormatChange(): Boolean {
        val is24HourFormatCurrent = DateFormat.is24HourFormat(context)
        if (is24HourFormatCurrent != is24HourFormat) {
            is24HourFormat = is24HourFormatCurrent
            return true
        }
        return false
    }

    override fun registerObserver(observer: DateFormatChangeListener?) {
        observer?.let {
            if (!mObservers.contains(it)) {
                super.registerObserver(it)

                if (mObservers.count() > 0) {
                    registerDateChangeReceiver()
                }
            }
        }
    }

    override fun unregisterAll() {
        super.unregisterAll()

        unregisterDateChangedReceiver()
    }

    override fun unregisterObserver(observer: DateFormatChangeListener?) {
        observer?.let {
            if (mObservers.contains(it)) {
                super.unregisterObserver(it)

                if (mObservers.count() == 0) {
                    unregisterDateChangedReceiver()
                }
            }
        }
    }

    private fun registerDateChangeReceiver() {
        if (!isReceiverRegistered) {
            val intentFilter = IntentFilter(Intent.ACTION_TIME_CHANGED)

            context.registerReceiver(broadcastReceiver, intentFilter)
            isReceiverRegistered = true
            checkDateFormatChange()
        }
    }

    private fun unregisterDateChangedReceiver() {
        context.unregisterReceiver(broadcastReceiver)
        isReceiverRegistered = false
    }

    private fun notifyDateFormatChanged() {
        mObservers.forEach {
            it.onDateFormatChanged(is24HourFormat)
        }
    }


    interface DateFormatChangeListener {
        fun onDateFormatChanged(is24Hours: Boolean)
    }
}
