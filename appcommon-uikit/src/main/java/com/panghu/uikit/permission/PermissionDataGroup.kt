package com.panghu.uikit.permission


class PermissionDataGroup(
    val permissions: Array<PermissionData>, var flag: Int = NO_HINT,
    var model: HintData = HintData()
) : PermissionData("group", flag, model) {

    override fun getHintByPermission(perm: String): HintData {
        for (permission in permissions) {
            if (permission.perm == perm) return permission.hintModel
        }
        return HintData()
    }

    override fun getPermissions(): Array<out String> {
        return permissions.map { it.perm }.toTypedArray()
    }
}