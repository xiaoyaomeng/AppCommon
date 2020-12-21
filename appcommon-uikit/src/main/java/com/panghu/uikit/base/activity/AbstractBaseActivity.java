package com.panghu.uikit.base.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.LinearLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.glip.crumb.model.ScreenMeta;
import com.glip.crumb.template.IScreenMetaProvider;
import com.panghu.uikit.R;
import com.panghu.uikit.base.IFabView;
import com.panghu.uikit.base.IProgressBar;
import com.panghu.uikit.base.IProgressDialog;
import com.panghu.uikit.base.IUIView;
import com.panghu.uikit.base.analytics.AnalyticsHelper;
import com.panghu.uikit.base.analytics.IScreenCrumb;
import com.panghu.uikit.base.analytics.ScreenLocation;
import com.panghu.uikit.base.analytics.ScreenOrientationAnalytics;
import com.panghu.uikit.base.fragment.AbstractBaseFragment;
import com.panghu.uikit.utils.Log;
/**
 * The base activity that provides appbar, banner, progress bar, progress dialog, fab, permissions,
 * screen analytics, etc.
 * Created by panghu on 5/6/16.
 */
public abstract class AbstractBaseActivity extends AppCompatActivity
        implements IUIView, IBannerHostView, IProgressBar, IProgressDialog, IFabView, IScreenCrumb {

    private static final String TAG = "AbstractBaseActivity";
    public static final int NO_APP_BAR = 0;
    public static final int RESULT_ERROR = 101;
    public static final String EXTRA_TITLE = "TITLE";
    public static final String EXTRA_TITLE_ID = "TITLE_ID";

    private AppBarLayout mAppBar;
    private Toolbar mToolbar;
    private ViewGroup mContentContainerView;
    private View mProgressBarContainer;
    private ProgressDialog mProgressDialog;
    private boolean mIsProgressDialogShowed;
    private IFab mFab;
    private IBannerController mBannerController;
    private int mScreenOrientation;

    protected int onCreateBaseLayout() {
        return R.layout.base_activity;
    }

    protected int onCreateAppBarLayout() {
        return R.layout.base_app_bar_view;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(onCreateBaseLayout());
        initAppBarView();
        initToolbar();
        initContentContainerView();
        handleIntent(getIntent());
        mScreenOrientation = getResources().getConfiguration().orientation;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        getLayoutInflater().inflate(layoutResID, mContentContainerView);
    }

    @Override
    public void setBannerController(IBannerController bannerController) {
        mBannerController = bannerController;
        ViewGroup bannerContainer = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.base_banner_container_view, mContentContainerView, false);
        mContentContainerView.addView(bannerContainer, 0);
        mBannerController.init(this, bannerContainer);
    }

    @Override
    public void setBannerFlags(int flags) {
        if (mBannerController != null) {
            mBannerController.setBannerFlags(flags);
        }
    }

    @Override
    public void onBannerRefreshed() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mBannerController != null) {
            mBannerController.onStart();
        }
        leaveScreenCrumb(screenCrumb());
    }

    @Override
    protected void onStop() {
        if (mBannerController != null) {
            mBannerController.onStop();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        hideProgressDialog();
        super.onDestroy();
    }

    @Override
    public boolean isUiReady() {
        return !isDestroyed() && !isFinishing();
    }

    @Override
    public void showProgressBar() {
        if (!mIsProgressDialogShowed) {
            if (mProgressBarContainer == null) {
                inflateProgressBar();
            }
            mProgressBarContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgressBar() {
        if (mProgressBarContainer != null) {
            mProgressBarContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void showProgressDialog() {
        showMessageProgressDialog(R.string.please_wait);
    }

    @Override
    public void showProgressDialog(@StringRes int stringRes) {
        showMessageProgressDialog(stringRes);
    }

    private void showMessageProgressDialog(@StringRes int stringRes) {
        String message = getString(stringRes);
        if (mProgressDialog == null) {
            createProgressDialog(message);
        } else {
            mProgressDialog.setMessage(message);
        }
        mProgressDialog.show();
        mIsProgressDialogShowed = true;
    }

    @Override
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mIsProgressDialogShowed = false;
    }

    @Override
    public void showFab(boolean animation) {
        if (mFab != null && mFab.getVisibility() != View.VISIBLE) {
            if (animation) {
                mFab.show();
            } else {
                mFab.setVisibility(View.VISIBLE);
            }
        }

        showBottomEmptyLine();
    }

    @Override
    public void hideFab(boolean animation) {
        if (mFab != null && mFab.getVisibility() == View.VISIBLE) {
            if (animation) {
                mFab.hide();
            } else {
                mFab.setVisibility(View.GONE);
            }
        }

        hideBottomEmptyLine();
    }

    protected void onFabClick() {
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment instanceof AbstractBaseFragment) {
            ((AbstractBaseFragment) currentFragment).onFabClick();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getCurrentFragment();
        if (fragment instanceof AbstractBaseFragment) {
            AbstractBaseFragment baseFragment = (AbstractBaseFragment) fragment;
            if (baseFragment.onBackPressed()) {
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public ScreenLocation screenCrumb() {
        if (this instanceof IScreenMetaProvider) {
            ScreenMeta meta = ((IScreenMetaProvider) this).screenMeta();
            if (meta != null) {
                return new ScreenLocation(meta.getScreenCategory(), meta.getScreenName());
            }
        }
        return null;
    }

    @Override
    public void leaveScreenCrumb(ScreenLocation screenLocation) {
        AnalyticsHelper.logScreenCrumb(screenLocation);
    }

    @Override
    @CallSuper
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation != mScreenOrientation) {
            mScreenOrientation = newConfig.orientation;
            ScreenOrientationAnalytics.logOrientationChanged(mScreenOrientation, getPageNameForOrientationAnalytics());
        }
    }

    protected String getPageNameForOrientationAnalytics() {
        return "others";
    }

    public final AppBarLayout getAppBar() {
        return mAppBar;
    }

    public final Toolbar getToolBar() {
        return mToolbar;
    }

    public Fragment getCurrentFragment() {
        return null;
    }

    public final void setFitsSystemWindows(boolean flag) {
        CoordinatorLayout rootView = findViewById(R.id.root_view);
        rootView.setFitsSystemWindows(flag);
    }

    public final void setDisplayHomeAsUpEnabled(boolean enabled) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(enabled);
            actionBar.setDisplayHomeAsUpEnabled(enabled);
        }
    }

    public final void setHomeAsUpIndicator(Drawable drawable) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(drawable);
        }
    }

    public IFab getFab() {
        return mFab;
    }

    public void enableFab(boolean enable) {
        if (mFab != null) {
            mFab.setEnabled(enable);
        }
    }

    public void animateFab(@LayoutRes int layoutResource, Drawable fabIconDrawable) {
        boolean showBottomEmptyLine = false;

        if (mFab == null) {
            inflateFab(layoutResource, fabIconDrawable);
            mFab.show();
            showBottomEmptyLine = true;
        } else {
            if (fabIconDrawable != null) {
                if (mFab.getVisibility() == View.VISIBLE) {
                    FloatingActionButton fab;
                    if (mFab instanceof FloatingActionButton) {
                        fab = (FloatingActionButton) mFab;
                    } else if (mFab instanceof FabSpeedDial) {
                        fab = ((FabSpeedDial) mFab).getFab();
                    } else {
                        Log.w(TAG, "Invalid fab instance.");
                        hideBottomEmptyLine();
                        return;
                    }

                    FABUtil.scaleAnimateFab(fab, fabIconDrawable);
                } else {
                    mFab.setImageDrawable(fabIconDrawable);
                    mFab.show();
                }

                showBottomEmptyLine = true;
            } else {
                if (mFab.getVisibility() == View.VISIBLE) {
                    mFab.hide();
                }
            }
        }

        if (showBottomEmptyLine) {
            showBottomEmptyLine();
        } else {
            hideBottomEmptyLine();
        }
    }

    public void inflateFab(@LayoutRes int layoutResource, Drawable fabIconDrawable) {
        if (mFab == null) {
            ViewStub viewStub = findViewById(R.id.stub_fab_view);
            viewStub.setLayoutResource(layoutResource);
            mFab = (IFab) viewStub.inflate();
            mFab.setOnFabClickListener(view -> onFabClick());
        }
        setFab(fabIconDrawable);
    }

    public void setFabAnchor(int id, int anchorGravity) {
        CoordinatorLayout.LayoutParams layoutParams;
        layoutParams = (CoordinatorLayout.LayoutParams) mFab.getLayoutParams();
        layoutParams.setAnchorId(id);
        layoutParams.gravity = Gravity.NO_GRAVITY;
        layoutParams.anchorGravity = anchorGravity;
        mFab.setLayoutParams(layoutParams);
    }

    protected void handleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        int resId = intent.getIntExtra(EXTRA_TITLE_ID, 0);
        if (resId != 0) {
            setTitle(getResources().getString(resId));
        } else if (intent.hasExtra(EXTRA_TITLE)) {
            setTitle(intent.getStringExtra(EXTRA_TITLE));
        }
    }

    protected int getBottomEmptyViewHeight() {
        return 0;
    }

    private void initAppBarView() {
        int appBarResID = onCreateAppBarLayout();
        CoordinatorLayout rootView = findViewById(R.id.root_view);
        if (appBarResID > 0) {
            mAppBar = (AppBarLayout) getLayoutInflater().inflate(appBarResID, rootView, false);
            rootView.addView(mAppBar, 0);
            rootView.setFitsSystemWindows(true);
        } else {
            rootView.setFitsSystemWindows(false);
        }
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationContentDescription(R.string.accessibility_back);
        }
    }

    private void initContentContainerView() {
        mContentContainerView = findViewById(R.id.content_container_view);
    }

    private void inflateProgressBar() {
        ViewStub viewStub = findViewById(R.id.stub_progress_bar_view);
        mProgressBarContainer = viewStub.inflate();
    }

    private void createProgressDialog(String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(message);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    private void setFab(Drawable fabIconDrawable) {
        if (mFab != null) {
            mFab.setImageDrawable(fabIconDrawable);
            mFab.setVisibility(fabIconDrawable != null ? View.VISIBLE : View.GONE);
            if (fabIconDrawable != null) {
                showBottomEmptyLine();
            } else {
                hideBottomEmptyLine();
            }

            if (fabIconDrawable != null) {
                ((View) mFab).setScaleX(1f);
                ((View) mFab).setScaleY(1f);
                ((View) mFab).setAlpha(1f);
            }
        } else {
            Log.w(TAG, "Fab is not inflated yet.");
        }
    }

    private void showBottomEmptyLine() {
        Fragment fragment = getCurrentFragment();
        if (fragment instanceof IBottomMarginConfigurable) {
            ((IBottomMarginConfigurable) fragment).setBottomMargin(getBottomEmptyViewHeight());
        }
    }

    private void hideBottomEmptyLine() {
        Fragment fragment = getCurrentFragment();
        if (fragment instanceof IBottomMarginConfigurable) {
            ((IBottomMarginConfigurable) fragment).hideBottomMargin();
        }
    }


}
