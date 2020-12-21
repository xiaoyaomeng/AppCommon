package com.panghu.uikit.view.snackmenu;

/**
 * Called by menu implementation to notify another component of open/close events.
 * @author by panghu
 * @date 08/04/2020
 */
interface OnSnackMenuCallback {
    fun onOpenSubMenu()
    fun onCloseMenu()
}