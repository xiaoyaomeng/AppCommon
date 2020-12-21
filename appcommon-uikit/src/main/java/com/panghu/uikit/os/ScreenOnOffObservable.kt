package com.panghu.uikit.os

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Observable

class ScreenOnOffObservable(val context: Context) :
    Observable<ScreenOnOffObservable.ScreenOnOffObserver>() {

    private var isReceiverRegistered = false

    private val broadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    Intent.ACTION_SCREEN_ON -> {
                        notifyScreenOn()
                    }
                    Intent.ACTION_SCREEN_OFF -> {
                        notifyScreenOff()
                    }
                    else -> {

                    }
                }
            }
        }
    }

    override fun registerObserver(observer: ScreenOnOffObserver?) {
        observer?.also {
            if (!mObservers.contains(it)) {
                super.registerObserver(it)

                if (mObservers.count() > 0) {
                    registerScreenOffReceiver()
                }
            }
        }
    }

    override fun unregisterAll() {
        super.unregisterAll()

        unregisterScreenOffReceiver()
    }

    override fun unregisterObserver(observer: ScreenOnOffObserver?) {
        observer?.also {
            if (mObservers.contains(it)) {
                super.unregisterObserver(it)

                if (mObservers.count() == 0) {
                    unregisterScreenOffReceiver()
                }
            }
        }
    }

    private fun registerScreenOffReceiver() {
        if (!isReceiverRegistered) {
            val intentFilter =
                IntentFilter(Intent.ACTION_SCREEN_OFF)
            intentFilter.addAction(Intent.ACTION_SCREEN_ON)

            context.registerReceiver(broadcastReceiver, intentFilter)
            isReceiverRegistered = true
        }
    }

    private fun unregisterScreenOffReceiver() {
        context.unregisterReceiver(broadcastReceiver)
        isReceiverRegistered = false
    }

    private fun notifyScreenOn() {
        mObservers.toList().forEach {
            it.onScreenOn()
        }
    }

    private fun notifyScreenOff() {
        mObservers.toList().forEach {
            it.onScreenOff()
        }
    }

    interface ScreenOnOffObserver {
        fun onScreenOn()
        fun onScreenOff()
    }
}