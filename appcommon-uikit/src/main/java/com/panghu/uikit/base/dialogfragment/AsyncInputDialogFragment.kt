package com.panghu.uikit.base.dialogfragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.panghu.uikit.R
import com.panghu.uikit.base.field.InputField
import com.panghu.uikit.utils.KeyboardUtil
import kotlinx.android.synthetic.main.input_dialog_fragment.*

class AsyncInputDialogFragment : InputDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return super.onCreateDialog(savedInstanceState).apply {
            setCanceledOnTouchOutside(false)
            setOnShowListener {
                val alertDialog = this as AlertDialog
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    it.isEnabled = false
                    onClick(this, AlertDialog.BUTTON_POSITIVE)
                }

                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
                    onClick(this, AlertDialog.BUTTON_NEGATIVE)
                }

                edit_text.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(editable: Editable?) {

                    }

                    override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                        updateErrorVisibility(true)
                    }
                })

                edit_text.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        onClick(this, AlertDialog.BUTTON_POSITIVE)
                    }
                    return@setOnEditorActionListener false
                }
            }

            setOnKeyListener { _, keyCode, keyEvent ->
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    onClick(this, AlertDialog.BUTTON_NEGATIVE)
                }
                false
            }
            @Suppress("DEPRECATION")
            window?.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            )
        }
    }

    fun showError(error: String) {
        dialog?.edit_text?.announceForAccessibility(error)
        (dialog as? AlertDialog)?.let {
            it.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = true
            it.edit_text.requestFocus()
            it.invalidMessageText.text = error
        }
        updateErrorVisibility(false)
        KeyboardUtil.showKeyboard(requireContext(), dialog?.edit_text)
    }

    private fun updateErrorVisibility(isValid: Boolean) {
        val view = dialog?.edit_text ?: return
        ViewCompat.setBackgroundTintList(
            view,
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    if (isValid) R.color.colorPalettePrimary else R.color.colorPaletteAlert
                )
            )
        )
        dialog?.invalidMessageText?.visibility = if (isValid) View.GONE else View.VISIBLE
    }

    companion object {
        private const val ARG_INPUT_FIELD = "INPUT_FIELD"

        @JvmStatic
        fun newInstance(dateField: InputField): AsyncInputDialogFragment {
            return AsyncInputDialogFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_INPUT_FIELD, dateField)
                }
            }
        }
    }
}