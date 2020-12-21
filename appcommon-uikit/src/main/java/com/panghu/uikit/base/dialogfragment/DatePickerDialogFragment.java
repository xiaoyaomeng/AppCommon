package com.panghu.uikit.base.dialogfragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.DatePicker;

import com.panghu.uikit.R;
import com.panghu.uikit.base.field.ConstraintDateField;
import com.panghu.uikit.base.field.DateField;
import com.panghu.uikit.utils.TimeUtil;

import java.util.Calendar;

/**
 * Created by panghu on 9/2/16.
 */
public class DatePickerDialogFragment extends FieldDialogFragment implements DatePickerDialog.OnClickListener {

    private static final String ARG_DATE_FIELD = "DATE_FIELD";
    private DateField mDateField;

    @NonNull
    public static DatePickerDialogFragment newInstance(@NonNull DateField dateField) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE_FIELD, dateField);
        DatePickerDialogFragment fragment = new DatePickerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDateField = (DateField) getArguments().getSerializable(ARG_DATE_FIELD);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        if (mDateField.getDate() > 0) {
            calendar.setTimeInMillis(mDateField.getDate());
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(), null, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), this);
        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), this);
        if (mDateField.isClearable()) {
            datePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.clear), this);
        }
        if (mDateField instanceof ConstraintDateField) {
            long startDate = ((ConstraintDateField)mDateField).getConstraintStartDate();
            if (startDate >= 0) {
                datePickerDialog.getDatePicker().setMinDate(startDate);
                long endDate = ((ConstraintDateField)mDateField).getConstraintEndDate();
                datePickerDialog.getDatePicker().setMaxDate(endDate);
            }
        }
        return datePickerDialog;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                DatePicker datePicker = ((DatePickerDialog) dialog).getDatePicker();
                mDateField.setDate(TimeUtil.getCalendarDate(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth()));
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                mDateField.setDate(0);
                break;
            default:
                return;
        }
        if (mOnFieldCompletedListener != null) {
            mOnFieldCompletedListener.onFieldCompleted(mDateField);
        }
    }

}
