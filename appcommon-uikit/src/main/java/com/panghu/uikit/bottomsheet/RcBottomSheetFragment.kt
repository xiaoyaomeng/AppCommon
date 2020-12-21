package com.panghu.uikit.bottomsheet

import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.panghu.uikit.R
import com.panghu.uikit.utils.Log
import com.glip.widgets.utils.AccessibilityUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_fragment.*
import java.util.*

class RcBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var bottomSheetTag: String
    private lateinit var contentView: View
    private var itemClickListener: OnBottomSheetItemClickListener? = null
    private var dialogDismissListener: OnBottomSheetDismissListener? = null
    private var cancelItemModel: BottomItemModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_fragment, container, false)
            .also { contentView = it }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arguments = this.arguments
            ?: throw IllegalStateException("You need to create this via the builder")
        if (arguments.getBoolean(ARG_UNLOCK_DIALOG_WINDOW)) {
            unlockDialogWindowScreen()
        }
        initTitle(arguments.getInt(ARG_TITLE_RES), arguments.getString(ARG_TITLE_TEXT))
        recyclerView.adapter = initAdapter(arguments)
        recyclerView.layoutManager = initLayoutManager(arguments.getInt(ARG_COLUMNS))
    }

    override fun onStart() {
        super.onStart()
        /*
        * google's bug: https://issuetracker.google.com/issues/37132390#c6
        * when changed to landscape mode, bottom sheet will collapse.
        * Advice solution: set peekHight
        */
        val behavior = BottomSheetBehavior.from(contentView.parent as View)
        contentView.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    contentView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    behavior.peekHeight = contentView.measuredHeight
                }
            })
    }

    private fun initTitle(titleRes: Int, titleString: String?) {
        when {
            titleRes == -1 && titleString == null -> {
                titleText.visibility = View.GONE
            }
            titleRes == -1 && titleString != null -> {
                titleText.visibility = View.VISIBLE
                titleText.setText(titleString)
            }
            else -> {
                titleText.visibility = View.VISIBLE
                titleText.setText(titleRes)
            }
        }
    }

    private fun unlockDialogWindowScreen() {
        dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1) {
            @Suppress("DEPRECATION")
            dialog?.window?.addFlags(
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            )

        }
    }

    private fun initAdapter(arguments: Bundle): BottomSheetAdapter {
        bottomSheetTag = arguments.getString(ARG_TAG)
            ?: throw IllegalStateException("You need to create this via the builder")
        val models = arguments.getParcelableArrayList<BottomItemModel>(ARG_MODELS)
            ?: ArrayList<BottomItemModel>()
        if (AccessibilityUtils.isAccessibilityOn(requireContext())) {
            models.add(getAccessibilityCancelItem())
        }
        return BottomSheetAdapter(models) {
            if (it.id != ACCESSIBILITY_CANCEL_ID) {
                itemClickListener?.onBottomSheetItemClicked(it.id, bottomSheetTag)
            }
            dismissAllowingStateLoss()
        }
    }

    private fun getAccessibilityCancelItem(): BottomItemModel {
        return cancelItemModel?.let {
            return it
        } ?: BottomItemModel(ACCESSIBILITY_CANCEL_ID, R.string.icon_cancel, R.string.cancel).also {
            cancelItemModel = it
        }
    }

    private fun initLayoutManager(columns: Int): RecyclerView.LayoutManager {
        return if (columns == 1) {
            LinearLayoutManager(context)
        } else {
            GridLayoutManager(context, columns).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return columns
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is OnBottomSheetItemClickListener) {
            itemClickListener = parentFragment as OnBottomSheetItemClickListener
        } else if (activity is OnBottomSheetItemClickListener) {
            itemClickListener = activity as OnBottomSheetItemClickListener
        }

        if (parentFragment is OnBottomSheetDismissListener) {
            dialogDismissListener = parentFragment as OnBottomSheetDismissListener
        } else if (activity is OnBottomSheetDismissListener) {
            dialogDismissListener = activity as OnBottomSheetDismissListener
        }
    }

    override fun onDetach() {
        super.onDetach()
        itemClickListener = null
        dialogDismissListener = null
    }

    override fun onDismiss(dialog: DialogInterface) {
        dialogDismissListener?.onBottomSheetDismiss(dialog)
        super.onDismiss(dialog)
    }

    companion object {
        private const val TAG = "RcBottomSheetFragment"
        private const val ARG_TAG = "bottomSheetTag"
        private const val ARG_MODELS = "models"
        private const val ARG_COLUMNS = "columns"
        private const val ARG_TITLE_RES = "titleRes"
        private const val ARG_TITLE_TEXT = "titleText"
        private const val ARG_UNLOCK_DIALOG_WINDOW = "unLock_dialog_window"
        private const val ACCESSIBILITY_CANCEL_ID = Int.MAX_VALUE

        private fun newInstance(builder: Builder): RcBottomSheetFragment {
            val fragment = RcBottomSheetFragment()
            Bundle().apply {
                putParcelableArrayList(ARG_MODELS, builder.models)
                putInt(ARG_COLUMNS, builder.columns)
                putInt(ARG_TITLE_RES, builder.titleRes)
                putString(ARG_TAG, builder.tag)
                putBoolean(ARG_UNLOCK_DIALOG_WINDOW, builder.unlockDialogWindow)
                if (!builder.titleText.isNullOrEmpty()) {
                    putString(ARG_TITLE_TEXT, builder.titleText)
                }
            }.also { fragment.arguments = it }
            return fragment
        }
    }

    class Builder(internal var models: ArrayList<BottomItemModel> = ArrayList()) {
        internal var columns = 1
        @StringRes
        internal var titleRes: Int = -1
        internal var titleText: String? = null
        internal var unlockDialogWindow: Boolean = false
        internal var tag: String = "RcBottomSheetFragment"

        fun add(model: BottomItemModel): Builder {
            this.models.add(model)
            return this
        }

        fun columns(columns: Int): Builder {
            this.columns = columns
            return this
        }

        fun title(@StringRes titleRes: Int): Builder {
            this.titleRes = titleRes
            return this
        }

        fun title(titleText: String): Builder {
            this.titleText = titleText
            return this
        }

        fun setDialogWindowUnlocked(unlock: Boolean): Builder {
            unlockDialogWindow = unlock

            return this
        }

        fun tag(tag: String): Builder {
            this.tag = tag
            return this
        }

        fun show(fragmentManager: FragmentManager): RcBottomSheetFragment {
            return newInstance(this).apply {
                try {
                    show(fragmentManager, tag)
                } catch (e: IllegalStateException) {
                    Log.e(TAG, e.message)
                }
            }
        }

        fun showWithAllowingStateLoss(fragmentManager: FragmentManager): RcBottomSheetFragment {
            val fragment = newInstance(this)
            fragmentManager.beginTransaction()
                .add(fragment, tag)
                .commitAllowingStateLoss()
            return fragment
        }
    }
}