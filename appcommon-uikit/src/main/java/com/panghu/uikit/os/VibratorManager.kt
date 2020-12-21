package com.panghu.uikit.os

import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.panghu.uikit.utils.Log
import java.util.*

class VibratorManager private constructor() {

    private val vibrateDuration = 400L
    private val vibratePauseDuration = 2500L
    private val vibratePattern = longArrayOf(0, vibrateDuration, vibratePauseDuration)
    private var vibrator: Vibrator? = null
    private var isInitialized = false
    private var isVibrating = false
    private var context: Context? = null

    private var vibrateTokens: MutableSet<Int> = mutableSetOf()

    fun init(context: Context) {
        if (!isInitialized) {
            this.context = context.applicationContext
            isInitialized = true
        }
    }

    fun requestToken(): Int {
        val random = Random()

        while (true) {
            val token = random.nextInt()
            if (!vibrateTokens.contains(token) && -1 != token) {
                return token
            }
        }
    }

    fun startVibrate(token: Int) {
        Log.d(TAG, "Token is $token")
        context?.getSystemService(Context.AUDIO_SERVICE)?.let {
            if ((it as AudioManager).ringerMode == AudioManager.RINGER_MODE_SILENT) {
                Log.w(TAG, "Ringer mode is silent")
                return
            }
        }

        if (!vibrateTokens.contains(token) && -1 != token) {
            vibrateTokens.add(token)
            checkTokensSize()

            if (vibrateTokens.size == 1) {
                startVibrate()
            } else {
                Log.w(TAG, "Vibrate tokens size(${vibrateTokens.size})")
            }
        }
    }

    fun stopVibrate(token: Int) {
        Log.d(TAG, "Token is $token")
        if (vibrateTokens.contains(token)) {
            vibrateTokens.remove(token)

            if (vibrateTokens.isEmpty()) {
                stopVibrate()
            } else {
                Log.w(TAG, "Vbrate tokens is not empty.")
            }
        } else {
            Log.w(TAG, "The token is not contain in tokens.")
        }
    }

    fun forceStopVibrate() {
        Log.i(TAG, "Enter")
        vibrateTokens.clear()
        stopVibrate()
    }

    fun vibrateOneShot(millis: Long, amplitude: Int) {
        checkIfInitialized()

        vibrator?.vibrateCompat(millis, amplitude)
    }

    private fun startVibrate() {
        checkIfInitialized()

        if (!isVibrating) {
            vibrator?.vibrateCompat(vibratePattern, 0)
            isVibrating = true
        }
    }

    private fun stopVibrate() {
        checkIfInitialized()

        if (isVibrating) {
            vibrator?.cancel()
            isVibrating = false
        } else {
            Log.w(TAG, "Vibrate is not start.")
        }
    }

    private fun initVibrator() {
        if (vibrator == null) {
            vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    private fun checkIfInitialized() {
        if (!isInitialized) {
            throw IllegalStateException("VibratorManager should be init(context) first")
        }

        initVibrator()
    }

    private fun checkTokensSize() {
        if (vibrateTokens.size > 100) {
            Log.w(TAG, "Vibrate tokens size(${vibrateTokens.size}) > 100")
        }
    }

    companion object {
        const val INVALID_TOKEN = -1
        private const val TAG = "VibratorManager"

        @JvmStatic
        val instance: VibratorManager by lazy { VibratorManager() }
    }
}

private fun Vibrator.vibrateCompat(pattern: LongArray, repeat: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrate(VibrationEffect.createWaveform(pattern, repeat))
    } else {
        @Suppress("DEPRECATION")
        vibrate(pattern, repeat)
    }
}

private fun Vibrator.vibrateCompat(millis: Long, amplitude: Int = 10) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrate(VibrationEffect.createOneShot(millis, amplitude))
    } else {
        @Suppress("DEPRECATION")
        vibrate(millis)
    }
}