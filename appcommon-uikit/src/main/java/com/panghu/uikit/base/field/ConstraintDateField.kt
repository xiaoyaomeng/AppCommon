package com.panghu.uikit.base.field

import com.panghu.uikit.utils.TimeUtil

data class ConstraintDateField(val fieldId: FieldId, val time: Long, val constraintStartDate: Long = -1L, val constraintEndDate: Long = 0L)
    : DateField(fieldId, 0, false, true,
        -1, TimeUtil.getCalendarDate(time), false, false)