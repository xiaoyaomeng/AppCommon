package com.panghu.uikit.base.fragment

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import android.os.ResultReceiver
import android.view.ViewTreeObserver
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

class AlertDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alertDialog = createBuilder().create()
        arguments?.let {
            if (it.containsKey(CANCELED_ON_TOUCH_OUTSIDE)) {
                alertDialog.setCanceledOnTouchOutside(it.getBoolean(CANCELED_ON_TOUCH_OUTSIDE))
            }
            if (it.containsKey(CANCELABLE)) {
                isCancelable = it.getBoolean(CANCELABLE)
            }
        }
        return alertDialog
    }

    private fun createBuilder(): AlertDialog.Builder {
        return AlertDialog.Builder(requireContext()).apply {
            arguments?.let {
                if (it.containsKey(TITLE)) {
                    setTitle(it.getString(TITLE))
                }
                if (it.containsKey(MESSAGE)) {
                    setMessage(it.getString(MESSAGE))
                }
                if (it.containsKey(POSITIVE_BUTTON_TEXT)) {
                    val text = it.getString(POSITIVE_BUTTON_TEXT)
                    val listener = if (it.containsKey(POSITIVE_BUTTON_LISTENER))
                        it.getParcelable<ParcelableOnClickListener?>(POSITIVE_BUTTON_LISTENER)
                    else null
                    setPositiveButton(text, listener)
                }

                if (it.containsKey(NEGATIVE_BUTTON_TEXT)) {
                    val text = it.getString(NEGATIVE_BUTTON_TEXT)
                    val listener = if (it.containsKey(NEGATIVE_BUTTON_LISTENER))
                        it.getParcelable<ParcelableOnClickListener?>(NEGATIVE_BUTTON_LISTENER)
                    else null
                    setNegativeButton(text, listener)
                }

                if (it.containsKey(NEUTRAL_BUTTON_TEXT)) {
                    val text = it.getString(NEUTRAL_BUTTON_TEXT)
                    val listener = if (it.containsKey(NEUTRAL_BUTTON_LISTENER))
                        it.getParcelable<ParcelableOnClickListener?>(NEUTRAL_BUTTON_LISTENER)
                    else null
                    setNeutralButton(text, listener)
                }

                if (it.containsKey(DISMISS_LISTENER)) {
                    val listener = it.getParcelable<ParcelableOnDismissListener?>(DISMISS_LISTENER)
                    setOnDismissListener(listener)
                }
            }
        }
    }

    class Builder(context: Context) {
        private val params: Params

        init {
            params = Params(context)
        }

        fun setTitle(title: String?): Builder {
            params.title = title
            return this
        }

        fun setTitle(@StringRes resId: Int): Builder {
            params.title = params.context.getString(resId)
            return this
        }

        fun setMessage(message: String?): Builder {
            params.message = message
            return this
        }

        fun setMessage(@StringRes resId: Int): Builder {
            params.message = params.context.getString(resId)
            return this
        }

        @JvmOverloads
        fun setPositiveButton(
            text: String,
            listener: DialogInterface.OnClickListener? = null
        ): Builder {
            params.positiveButtonText = text
            params.positiveListener = createParcelableOnClickListener(listener)
            return this
        }

        fun setPositiveButton(
            text: String,
            action: (dialog: DialogInterface?, which: Int) -> Unit
        ): Builder {
            params.positiveButtonText = text
            params.positiveListener = createParcelableOnClickListener(action)
            return this
        }

        @JvmOverloads
        fun setPositiveButton(
            @StringRes resId: Int,
            listener: DialogInterface.OnClickListener? = null
        ): Builder {
            params.positiveButtonText = params.context.getString(resId)
            params.positiveListener = createParcelableOnClickListener(listener)
            return this
        }

        fun setPositiveButton(
            @StringRes resId: Int,
            action: (dialog: DialogInterface?, which: Int) -> Unit
        ): Builder {
            params.positiveButtonText = params.context.getString(resId)
            params.positiveListener = createParcelableOnClickListener(action)
            return this
        }

        @JvmOverloads
        fun setNegativeButton(
            text: String,
            listener: DialogInterface.OnClickListener? = null
        ): Builder {
            params.negativeButtonText = text
            params.negativeListener = createParcelableOnClickListener(listener)
            return this
        }

        fun setNegativeButton(
            text: String,
            action: (dialog: DialogInterface?, which: Int) -> Unit
        ): Builder {
            params.positiveButtonText = text
            params.positiveListener = createParcelableOnClickListener(action)
            return this
        }

        @JvmOverloads
        fun setNegativeButton(
            @StringRes resId: Int,
            listener: DialogInterface.OnClickListener? = null
        ): Builder {
            params.negativeButtonText = params.context.getString(resId)
            params.negativeListener = createParcelableOnClickListener(listener)
            return this
        }

        fun setNegativeButton(
            @StringRes resId: Int,
            action: (dialog: DialogInterface?, which: Int) -> Unit
        ): Builder {
            params.positiveButtonText = params.context.getString(resId)
            params.positiveListener = createParcelableOnClickListener(action)
            return this
        }

        @JvmOverloads
        fun setNeutralButton(
            text: String,
            listener: DialogInterface.OnClickListener? = null
        ): Builder {
            params.neutralButtonText = text
            params.neutralListener = createParcelableOnClickListener(listener)
            return this
        }

        fun setNeutralButton(
            text: String,
            action: (dialog: DialogInterface?, which: Int) -> Unit
        ): Builder {
            params.neutralButtonText = text
            params.neutralListener = createParcelableOnClickListener(action)
            return this
        }

        @JvmOverloads
        fun setNeutralButton(
            @StringRes resId: Int,
            listener: DialogInterface.OnClickListener? = null
        ): Builder {
            params.neutralButtonText = params.context.getString(resId)
            params.neutralListener = createParcelableOnClickListener(listener)
            return this
        }

        fun setNeutralButton(
            @StringRes resId: Int,
            action: (dialog: DialogInterface?, which: Int) -> Unit
        ): Builder {
            params.neutralButtonText = params.context.getString(resId)
            params.neutralListener = createParcelableOnClickListener(action)
            return this
        }

        @JvmOverloads
        fun setOnDismissListener(listener: DialogInterface.OnDismissListener? = null): Builder {
            params.dismissListener = createParcelableOnDismissListener(listener)
            return this
        }

        fun setOnDismissListener(action: (dialog: DialogInterface?) -> Unit): Builder {
            params.dismissListener = createParcelableOnDismissListener(action)
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder {
            params.cancelable = cancelable
            return this
        }

        fun setCanceledOnTouchOutside(canceledOnTouchOutSide: Boolean): Builder {
            params.canceledOnTouchOutSide = canceledOnTouchOutSide
            return this
        }

        fun create(): AlertDialogFragment {
            return AlertDialogFragment().apply {
                arguments = params.toBundle()
            }
        }

        fun show(): AlertDialogFragment {
            return AlertDialogFragment().apply {
                arguments = params.toBundle()
            }.also { df ->
                val fm = when (params.context) {
                    is FragmentActivity -> params.context.supportFragmentManager
                    is Fragment -> params.context.childFragmentManager
                    else -> throw IllegalArgumentException("wrong context parameter exception")
                }
                if (fm.isStateSaved) {
                    showFragmentAfterResume(fm, df)
                } else {
                    df.show(fm, params.title)
                }
            }
        }

        private fun showFragmentAfterResume(fm: FragmentManager, df: DialogFragment) {
            val viewTreeObserver = when (params.context) {
                is FragmentActivity -> params.context.window.decorView.viewTreeObserver
                is Fragment -> params.context.requireActivity().window.decorView.viewTreeObserver
                else -> throw IllegalArgumentException("wrong context parameter exception")
            }
            var windowFocusChangeListener: ViewTreeObserver.OnWindowFocusChangeListener? = null
            windowFocusChangeListener =
                ViewTreeObserver.OnWindowFocusChangeListener {
                    if (it && !df.isAdded) {
                        df.show(fm, params.title)
                        viewTreeObserver.removeOnWindowFocusChangeListener(windowFocusChangeListener)
                    }
                }
            viewTreeObserver.addOnWindowFocusChangeListener(windowFocusChangeListener)
        }

        private fun createParcelableOnClickListener(
            listener: DialogInterface.OnClickListener?
        ): ParcelableOnClickListener? {
            listener ?: return null
            return object : ParcelableOnClickListener() {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    listener.onClick(dialog, which)
                }
            }
        }

        private fun createParcelableOnClickListener(
            action: (dialog: DialogInterface?, which: Int) -> Unit
        ): ParcelableOnClickListener? {
            return object : ParcelableOnClickListener() {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    action.invoke(dialog, which)
                }
            }
        }

        private fun createParcelableOnDismissListener(
            listener: DialogInterface.OnDismissListener?
        ): ParcelableOnDismissListener? {
            listener ?: return null
            return object : ParcelableOnDismissListener() {
                override fun onDismiss(dialog: DialogInterface?) {
                    listener.onDismiss(dialog)
                }
            }
        }

        private fun createParcelableOnDismissListener(
            action: (dialog: DialogInterface?) -> Unit
        ): ParcelableOnDismissListener? {
            return object : ParcelableOnDismissListener() {
                override fun onDismiss(dialog: DialogInterface?) {
                    action.invoke(dialog)
                }
            }
        }

        private data class Params(
            val context: Context,
            var title: String? = null,
            var message: String? = null,
            var positiveButtonText: String? = null,
            var positiveListener: ParcelableOnClickListener? = null,
            var negativeButtonText: String? = null,
            var negativeListener: ParcelableOnClickListener? = null,
            var neutralButtonText: String? = null,
            var neutralListener: ParcelableOnClickListener? = null,
            var dismissListener: ParcelableOnDismissListener? = null,
            var cancelable: Boolean? = null,
            var canceledOnTouchOutSide: Boolean? = null
        ) {
            fun toBundle(): Bundle {
                return Bundle().apply {
                    title?.let { putString(TITLE, it) }
                    message?.let { putString(MESSAGE, it) }
                    positiveButtonText?.let { putString(POSITIVE_BUTTON_TEXT, it) }
                    positiveListener?.let { putParcelable(POSITIVE_BUTTON_LISTENER, it) }
                    negativeButtonText?.let { putString(NEGATIVE_BUTTON_TEXT, it) }
                    negativeListener?.let { putParcelable(NEGATIVE_BUTTON_LISTENER, it) }
                    neutralButtonText?.let { putString(NEUTRAL_BUTTON_TEXT, it) }
                    neutralListener?.let { putParcelable(NEUTRAL_BUTTON_LISTENER, it) }
                    dismissListener?.let { putParcelable(DISMISS_LISTENER, it) }
                    cancelable?.let { putBoolean(CANCELABLE, it) }
                    canceledOnTouchOutSide?.let { putBoolean(CANCELED_ON_TOUCH_OUTSIDE, it) }
                }
            }
        }
    }

    private open class ParcelableOnClickListener : ResultReceiver(null),
        DialogInterface.OnClickListener {
        @JvmField
        val CREATOR: Parcelable.Creator<ResultReceiver> = ResultReceiver.CREATOR

        override fun onClick(dialog: DialogInterface?, which: Int) {}
    }

    private open class ParcelableOnDismissListener : ResultReceiver(null),
        DialogInterface.OnDismissListener {
        @JvmField
        val CREATOR: Parcelable.Creator<ResultReceiver> = ResultReceiver.CREATOR

        override fun onDismiss(dialog: DialogInterface?) {}
    }

    companion object {
        private const val TITLE = "title"
        private const val MESSAGE = "message"
        private const val POSITIVE_BUTTON_TEXT = "positive_button_text"
        private const val NEGATIVE_BUTTON_TEXT = "negative_button_text"
        private const val NEUTRAL_BUTTON_TEXT = "neutral_button_text"
        private const val POSITIVE_BUTTON_LISTENER = "positive_button_listener"
        private const val NEGATIVE_BUTTON_LISTENER = "negative_button_listener"
        private const val NEUTRAL_BUTTON_LISTENER = "neutral_button_listener"
        private const val DISMISS_LISTENER = "dismiss_listener"
        private const val CANCELABLE = "cancelable"
        private const val CANCELED_ON_TOUCH_OUTSIDE = "canceled_on_touch_outside"
    }
}