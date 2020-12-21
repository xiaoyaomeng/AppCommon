package com.panghu.uikit.utils

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Handler
import android.os.Message
import android.widget.Toast
import java.lang.reflect.Field
import java.lang.reflect.Modifier

/**
 * Apps targeting SDK above N MR1 cannot arbitrary add toast windows,
 * otherwise it will throw BadTokenException for some cases.
 * Android catch this exception since O, when the version is N_MR1, we should
 * delegate to catch the exception.
 */

open class ToastCompat(context: Context) : Toast(context) {

    override fun show() {
        if (checkIfNeedToHack()) {
            tryToHack()
        }
        super.show()
    }

    private fun checkIfNeedToHack(): Boolean {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1
    }

    private fun tryToHack() {
        try {
            getFieldValue(this, FIELD_M_TN)?.let { tn ->
                var isSuccess = false

                //a hack to some device which use the code between android 6.0 and android 7.1.1
                (getFieldValue(tn, FIELD_M_SHOW) as? Runnable)?.let { isSuccess = setFieldValue(tn, FIELD_M_SHOW, InternalRunnable(it)) }

                if (!isSuccess) {
                    (getFieldValue(tn, FIELD_M_HANDLER) as? Handler)?.let { isSuccess = setFieldValue(it, FIELD_M_CALLBACK, InternalHandlerCallback(it)) }
                }

                if (!isSuccess) {
                    Log.w(TAG, "TryToHack error.")
                }
            }
        } catch (e: Throwable) {
            Log.e(TAG, e.message)
        }

    }

    private inner class InternalRunnable(private val runnable: Runnable) : Runnable {
        override fun run() {
            try {
                this.runnable.run()
            } catch (e: Throwable) {
                Log.d(TAG, e.message)
            }
        }
    }

    private inner class InternalHandlerCallback(private val handler: Handler) : Handler.Callback {
        override fun handleMessage(msg: Message): Boolean {
            try {
                handler.handleMessage(msg)
            } catch (e: Throwable) {
                Log.d(TAG, e.message)
            }
            return true
        }
    }

    companion object {

        private const val TAG = "ToastCompat"
        private const val FIELD_M_SHOW = "mShow"
        private const val FIELD_M_TN = "mTN"
        private const val FIELD_M_HANDLER = "mHandler"
        private const val FIELD_M_CALLBACK = "mCallback"
        private const val FIELD_ACCESS_FLAGS = "accessFlags"

        fun makeText(context: Context, text: CharSequence, duration: Int): Toast {
            return Toast.makeText(context, text, duration)
        }

        @Throws(Resources.NotFoundException::class)
        fun makeText(context: Context, resId: Int, duration: Int): Toast {
            return makeText(context, context.resources.getText(resId), duration)
        }

        private fun setFieldValue(obj: Any, fieldName: String, newFieldValue: Any): Boolean {
            getDeclaredField(obj, fieldName)?.let {
                try {
                    val accessFlags = it.modifiers
                    if (Modifier.isFinal(accessFlags)) {
                        Field::class.java.getDeclaredField(FIELD_ACCESS_FLAGS).apply {
                            isAccessible = true
                            setInt(it, it.modifiers and Modifier.FINAL.inv())
                        }
                    }
                    if (!it.isAccessible) { it.isAccessible = true }
                    it.set(obj, newFieldValue)
                    return true
                } catch (e: Exception) {
                    Log.d(TAG, e.message)
                }
            }
            return false
        }

        private fun getFieldValue(obj: Any, fieldName: String): Any? {
            val field = getDeclaredField(obj, fieldName)
            return getFieldValue(obj, field)
        }

        private fun getFieldValue(obj: Any, field: Field?): Any? {
            field?.let {
                try {
                    if (!it.isAccessible) { it.isAccessible = true }
                    return it.get(obj)
                } catch (e: Exception) {
                    Log.d(TAG, e.message)
                }
            }
            return null
        }

        private fun getDeclaredField(obj: Any, fieldName: String): Field? {
            var superClass: Class<*>? = obj.javaClass
            while (superClass != Any::class.java) {
                try {
                    return superClass?.getDeclaredField(fieldName)
                } catch (e: NoSuchFieldException) {
                    superClass = superClass?.superclass
                    continue
                }
            }
            return null
        }
    }
}