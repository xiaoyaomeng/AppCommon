package com.panghu.uikit.permission

open class PermissionData(
    val perm: String,
    var hintFlag: Int = NO_HINT,
    val hintModel: HintData = HintData()
) {
    open fun getHintByPermission(perm: String): HintData {
        return if (this.perm == perm) hintModel else HintData()
    }

    open fun getPermissions(): Array<out String> {
        return arrayOf(perm)
    }

    companion object {
        const val NO_HINT = 0
        const val SHOW_HINT_ALERT = 1
        const val SHOW_HINT_SCREEN = 2
    }
}