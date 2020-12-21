package com.panghu.uikit.base.field;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.panghu.uikit.R;


/**
 * Created by panghu on 9/2/16.
 */
public class FieldViewHolder extends RecyclerView.ViewHolder {
    public final AbstractFieldPresenter mPresenter;
    public final ImageView mIconView;
    public final View mDividerView;
    public final View mCustomView;

    public FieldViewHolder(View fieldView, View customView, AbstractFieldPresenter presenter) {
        super(fieldView);
        mIconView = (ImageView) fieldView.findViewById(R.id.icon_view);
        mDividerView = fieldView.findViewById(R.id.divider_view);
        mCustomView = customView;
        mPresenter = presenter;
    }
}
