package com.panghu.uikit.base.dialogfragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.annotation.LayoutRes
import com.panghu.uikit.R
import com.panghu.uikit.base.field.InputField
import com.glip.widgets.text.CleanableEditText

/**
 * @author locke.peng on 4/27/18.
 */
open class InputDialogFragment : FieldDialogFragment(), DialogInterface.OnClickListener {

    private lateinit var inputField: InputField
    private var customLayoutResId: Int? = null
    private lateinit var cleanableEditText: CleanableEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inputField = arguments?.getSerializable(ARG_INPUT_FIELD) as InputField
        if (arguments?.containsKey(ARG_LAYOUT_RES_ID) == true) {
            customLayoutResId = arguments?.getInt(ARG_LAYOUT_RES_ID)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layoutResId = customLayoutResId ?: R.layout.input_dialog_fragment
        val view = LayoutInflater.from(context).inflate(layoutResId, null)
        checkNotNull(view.findViewById<CleanableEditText>(R.id.edit_text)) {
            "Custom layout can't find R.id.edit_text as CleanableEditText."
        }
        cleanableEditText = view.findViewById(R.id.edit_text)
        cleanableEditText.maxLines = inputField.maxLines
        val filters = ArrayList<InputFilter>()
        if (inputField.maxLength > 0) {
            filters.add(InputFilter.LengthFilter(inputField.maxLength))
        }
        if (inputField.acceptedChars > 0) {
            filters.add(InputAcceptedFilter(getString(inputField.acceptedChars)))
        }
        if (filters.isNotEmpty()) {
            cleanableEditText.filters = filters.toTypedArray()
        }
        if (inputField.editHintResId > 0) {
            cleanableEditText.setHint(inputField.editHintResId)
        }
        cleanableEditText.inputType = inputField.inputType
        cleanableEditText.setText(inputField.text)
        cleanableEditText.setSelection(cleanableEditText.text?.length ?: 0)
        cleanableEditText.requestFocus()

        return AlertDialog.Builder(context)
            .setTitle(inputField.titleResId)
            .setPositiveButton(inputField.positiveButtonResId, this)
            .setNegativeButton(inputField.negativeButtonResId, this)
            .setView(view)
            .create()
            .also {
                it.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            }
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            inputField.text = cleanableEditText.text.toString()
            callFieldCompleted()
        } else {
            callFieldCanceled()
        }
    }

    private fun callFieldCompleted() {
        when (targetFragment) {
            is OnFieldCompletedListener -> {
                (targetFragment as OnFieldCompletedListener).onFieldCompleted(inputField)
            }
            else -> {
                mOnFieldCompletedListener?.onFieldCompleted(inputField)
            }
        }
    }

    private fun callFieldCanceled() {
        when (targetFragment) {
            is OnFieldCompletedListener -> {
                (targetFragment as OnFieldCompletedListener).onFieldCanceled(inputField)
            }
            else -> {
                mOnFieldCompletedListener?.onFieldCanceled(inputField)
            }
        }
    }


    internal class InputAcceptedFilter(private val acceptedChars: String) : InputFilter {

        override fun filter(
            source: CharSequence?,
            start: Int,
            end: Int,
            dest: Spanned?,
            dstart: Int,
            dend: Int
        ): CharSequence {
            val buffer = StringBuffer()
            source?.forEach {
                if (acceptedChars.indexOf(it) >= 0) {
                    buffer.append(it)
                }
            }
            return buffer
        }

    }

    companion object {
        private const val ARG_INPUT_FIELD = "INPUT_FIELD"
        private const val ARG_LAYOUT_RES_ID = "LAYOUT_RES_ID"

        @JvmStatic
        @JvmOverloads
        fun newInstance(dateField: InputField, @LayoutRes layoutResId: Int? = null): InputDialogFragment {
            return InputDialogFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_INPUT_FIELD, dateField)
                    layoutResId?.let { putInt(ARG_LAYOUT_RES_ID, it) }
                }
            }
        }
    }
}