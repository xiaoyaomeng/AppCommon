package com.panghu.uikit.view.snackmenu;

import android.graphics.Point
import android.view.View
import androidx.fragment.app.FragmentManager
import com.panghu.uikit.R
import com.panghu.uikit.bottomsheet.BottomItemModel
import com.panghu.uikit.bottomsheet.RcBottomSheetFragment
import com.panghu.uikit.view.popupwindow.CommonPopupWindow
import com.panghu.uikit.view.popupwindow.XGravity
import com.panghu.uikit.view.popupwindow.YGravity
import kotlin.math.min

/**
 * Helper for menus that appear as snackbar (context and submenus).
 * @author by panghu
 * @date 08/04/2020
 */
class SnackMenuHelper(val fragmentManager: FragmentManager) : OnSnackMenuItemClickListener,
    OnSnackMenuCallback {
    var popupWindow: CommonPopupWindow? = null
    var actionItems: List<SnackMenuItem> = arrayListOf()

    var itemClickListener: OnSnackMenuItemClickListener? = null
    var callback: OnSnackMenuCallback? = null

    fun dismiss() {
        popupWindow?.dismiss()
    }

    @JvmOverloads
    fun showMenu(
        anchorView: View,
        point: Point,
        actionItems: List<SnackMenuItem>,
        maxCount: Int = MAX_ACTION_COUNT
    ) {
        if (actionItems.isEmpty()) {
            return
        }
        this.actionItems = actionItems

        val context = anchorView.context
        val snackView = SnackMenuView(context)
        snackView.onSnackMenuItemClickListener = this
        snackView.onSnackMenuCallback = this

        val maxSnackItemCount = min(snackView.maxItemCount, maxCount)
        // If the max size is less than 1(include more action), will direct show more menu.
        if (maxSnackItemCount == 0 || (maxSnackItemCount == 1 && actionItems.size > 1)) {
            showMoreMenu()
            callback?.onOpenSubMenu()
            return
        }

        // Some actions will always show in more menu.
        val snackItems = actionItems
            .filterNot { it.isInMore }.take(maxSnackItemCount - 1)

        // Will always show more option
        snackView.updateMenu(
            snackItems,
            true
        )

        if (popupWindow == null) {
            popupWindow = CommonPopupWindow.Builder(context)
                .setView(snackView)
                .setFocusable(true)
                .setTouchable(true)
                .setTouchPosition(point.x, point.y)
                .setOverlapAnchor(true)
                .setClippingEnable(true)
                .setOutsideTouchable(true)
                .setDarkEnable(false)
                .setDarkAlphaValue(0.5f)
                .setAnimationStyle(R.style.GlipWidget_SnackMenuAnimation)
                .create()
        } else {
            popupWindow?.let {
                it.updateTouchPosition(point.x, point.y)
                it.updateCustomView(snackView)
            }
        }

        val yOff = context.resources.getDimension(R.dimen.dimen_8dp)
        popupWindow?.showAtAnchorView(anchorView, 0, -yOff.toInt(), XGravity.CENTER, YGravity.TOP)
    }

    private fun showMoreMenu() {
        RcBottomSheetFragment.Builder(ArrayList(actionItems.map {
            BottomItemModel(it.itemId, it.iconRes, it.text ?: "", it.isAlert).apply {
                sizeRes = it.sizeRes
            }
        }))
            .tag(SNACK_MENU_TAG)
            .show(fragmentManager)
    }

    override fun onOpenSubMenu() {
        dismiss()
        showMoreMenu()
        callback?.onOpenSubMenu()
    }

    override fun onCloseMenu() {
        dismiss()
        callback?.onCloseMenu()
    }

    override fun onItemClick(snackMenuItem: SnackMenuItem) {
        dismiss()
        itemClickListener?.onItemClick(snackMenuItem)
    }

    companion object {
        const val SNACK_MENU_TAG = "snack_menu"

        const val MAX_ACTION_COUNT = 5
    }

}