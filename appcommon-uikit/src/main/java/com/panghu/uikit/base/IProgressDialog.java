package com.panghu.uikit.base;

import androidx.annotation.StringRes;

/**
 * Created by jerry.cai on 17/05/2017.
 */

public interface IProgressDialog {
    void showProgressDialog();
    void showProgressDialog(@StringRes int stringRes);
    void hideProgressDialog();
}
