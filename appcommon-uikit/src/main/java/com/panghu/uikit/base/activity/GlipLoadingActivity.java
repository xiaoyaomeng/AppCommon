package com.panghu.uikit.base.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;


/**
 * @author locke.peng on 11/17/17.
 */

public class GlipLoadingActivity extends AbstractBaseActivity {

    private static final String EXTRA_CANCELED = "CANCELED";
    private static final String EXTRA_CANCELABLE = "CANCELABLE";
    private boolean mCancelable;

    public static void start(Context context, boolean canceled) {
        Intent intent = new Intent(context, GlipLoadingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_CANCELED, canceled);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!dismissLoading(getIntent())) {
            showProgressDialog();
        }
    }

    @Override
    protected int onCreateAppBarLayout() {
        return NO_APP_BAR;
    }

    @Override
    protected void handleIntent(Intent intent) {
        super.handleIntent(intent);
        mCancelable = intent.getBooleanExtra(EXTRA_CANCELABLE, false);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        dismissLoading(intent);
    }

    private boolean dismissLoading(Intent intent) {
        if (intent != null && intent.getBooleanExtra(EXTRA_CANCELED, false)) {
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (mCancelable) {
            finish();
        }
    }
}
