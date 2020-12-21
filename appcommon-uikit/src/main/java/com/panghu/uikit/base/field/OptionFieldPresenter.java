package com.panghu.uikit.base.field;

import android.annotation.SuppressLint;
import androidx.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.panghu.uikit.R;


/**
 * Created by panghu on 8/31/16.
 */
public class OptionFieldPresenter extends AbstractFieldPresenter{

    @SuppressLint("ResourceType")
    @Override
    protected void onBindCustomView(View customView, AbstractField field) {
        AbstractOptionField optionField = (AbstractOptionField) field;
        TextView titleView = (TextView) customView.findViewById(R.id.title_view);
        TextView summaryView = (TextView) customView.findViewById(R.id.summary_view);
        if (optionField.getTitleResource() > 0) {
            titleView.setText(optionField.getTitleResource());
        }
        summaryView.setText(optionField.getSummary(summaryView.getContext()));
    }

    @LayoutRes
    @Override
    protected int onInflateCustomView() {
        return R.layout.common_field_custom_option_view;
    }

}
