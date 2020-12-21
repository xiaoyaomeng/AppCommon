package com.panghu.uikit.executors

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

class MainThreadExecutor private constructor() : Executor {

    private val handler = Handler(Looper.getMainLooper())

    override fun execute(command: Runnable?) {
        command?.let {
            if (isMainThread()) {
                it.run()
            } else {
                handler.post(it)
            }
        }
    }

    fun post(command: Runnable?) {
        command?.let {
            handler.post(it)
        }
    }

    fun postDelayed(command: Runnable?, delayMillis: Long): Boolean {
        return if (command == null) {
            false
        } else {
            handler.postDelayed(command, delayMillis)
        }
    }

    /**
     * Remove any pending posts of command
     */
    fun cancel(command: Runnable) {
        handler.removeCallbacks(command)
    }

    /**
     * Remove all pending posts
     */
    fun cancelAll() {
        handler.removeCallbacksAndMessages(null)
    }

    private fun isMainThread(): Boolean {
        return Thread.currentThread() === handler.looper.thread
    }

    companion object {
        @JvmStatic
        val instance: MainThreadExecutor by lazy {
            MainThreadExecutor()
        }
    }
}