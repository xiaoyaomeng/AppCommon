package com.panghu.uikit.base.dialogfragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.panghu.uikit.R;
import com.panghu.uikit.base.field.TimeField;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by panghu on 9/2/16.
 */
public class TimePickerDialogFragment extends FieldDialogFragment implements
        DialogInterface.OnClickListener, TimePickerDialog.OnTimeSetListener {

    private static final String ARG_TIME_FIELD = "TIME_FIELD";
    private TimeField mTimeField;
    private TimePickerDialog mTimePickerDialog;
    private boolean mWantSetTime = false;

    @NonNull
    public static TimePickerDialogFragment newInstance(@NonNull TimeField timeField) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME_FIELD, timeField);
        TimePickerDialogFragment fragment = new TimePickerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTimeField = (TimeField) getArguments().getSerializable(ARG_TIME_FIELD);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        int hour;
        int minute;
        if (!mTimeField.isNoneable() || mTimeField.getTime() > 0) {
            hour = (int) TimeUnit.MILLISECONDS.toHours(mTimeField.getTime());
            minute = (int) (TimeUnit.MILLISECONDS.toMinutes(mTimeField.getTime() - TimeUnit.HOURS.toMillis(hour)));
        } else {
            final Calendar calendar = Calendar.getInstance();
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
        }

        mTimePickerDialog = new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        mTimePickerDialog.setTitle("");
        mTimePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), this);
        mTimePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), this);
        if (mTimeField.isClearable()) {
            mTimePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.clear), this);
        }
        return mTimePickerDialog;
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            if (!mWantSetTime) {
                return;
            }
        }
        long timeInMillis = ((long) hourOfDay * 60 + minute) * 60 * 1000;
        mTimeField.setTime(timeInMillis);
        if (mOnFieldCompletedListener != null) {
            mOnFieldCompletedListener.onFieldCompleted(mTimeField);
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                mWantSetTime = true;
                mTimePickerDialog.onClick(dialog, which);
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                mWantSetTime = false;
                dismiss();
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                mWantSetTime = false;
                mTimeField.reset();
                if (mOnFieldCompletedListener != null) {
                    mOnFieldCompletedListener.onFieldCompleted(mTimeField);
                }
                break;
            default:
                mWantSetTime = false;
                break;
        }
    }
}
