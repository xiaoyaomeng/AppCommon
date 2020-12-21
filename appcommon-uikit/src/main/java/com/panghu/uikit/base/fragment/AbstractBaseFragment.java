package com.panghu.uikit.base.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.glip.crumb.model.ScreenMeta;
import com.glip.crumb.template.IScreenMetaProvider;
import com.panghu.uikit.R;
import com.panghu.uikit.base.IFabView;
import com.panghu.uikit.base.IProgressBar;
import com.panghu.uikit.base.IProgressDialog;
import com.panghu.uikit.base.IUIView;
import com.panghu.uikit.base.activity.AbstractBaseActivity;
import com.panghu.uikit.base.activity.IBannerController;
import com.panghu.uikit.base.activity.IBannerHostView;
import com.panghu.uikit.base.analytics.AnalyticsHelper;
import com.panghu.uikit.base.analytics.IScreenCrumb;
import com.panghu.uikit.base.analytics.ScreenLocation;
import com.panghu.uikit.base.fragment.FragmentVisibilityController.OnVisibilityChangeListener;
import com.google.android.material.appbar.AppBarLayout;
/**
 * The base fragment that provides progress bar, progress dialog, fab, screen analytics, etc.
 */
public abstract class AbstractBaseFragment extends Fragment
        implements IUIView, IProgressBar, IProgressDialog, IFabView, IScreenCrumb,
        OnVisibilityChangeListener, IBannerHostView {

    protected View mProgressBarContainer;
    private CoordinatorLayout mContainerView;
    private FragmentVisibilityController mFragmentVisibilityController;
    private IBannerController mBannerController;

    public AbstractBaseFragment() {
        mFragmentVisibilityController = new FragmentVisibilityController(this, this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initArguments(getArguments());
        mFragmentVisibilityController.onCreate();
    }

    protected void initArguments(Bundle arguments) {

    }


    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                   @Nullable Bundle savedInstanceState) {
        mContainerView = new CoordinatorLayout(requireContext());
        mContainerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mContainerView.setId(R.id.fragment_container);

        View contentView = onCreateContentView(inflater, mContainerView, savedInstanceState);
        if (contentView != null) {
            mContainerView.addView(contentView, 0);
        }
        return mContainerView;
    }

    @Nullable
    protected View onCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container,
                                       @Nullable Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mBannerController != null) {
            mBannerController.onStart();
        }
        mFragmentVisibilityController.onStart();
    }

    @Override
    public void onStop() {
        if (mBannerController != null) {
            mBannerController.onStop();
        }
        super.onStop();
        mFragmentVisibilityController.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFragmentVisibilityController.onDestroy();
    }

    @Override
    public void setHasOptionsMenu(boolean hasMenu) {
        super.setHasOptionsMenu(hasMenu);
        if (hasMenu) {
            AbstractBaseActivity activity = getBaseActivity();
            if (activity == null) {
                return;
            }

            AppBarLayout appBarLayout = activity.getAppBar();
            if (appBarLayout == null) {
                return;
            }

            View titleBar = appBarLayout.findViewById(R.id.toolbar_scroll_view);
            if (titleBar == null) {
                return;
            }

            ViewGroup.LayoutParams params = titleBar.getLayoutParams();
            if (params instanceof FrameLayout.LayoutParams) {
                FrameLayout.LayoutParams frameLayoutParams = (FrameLayout.LayoutParams) params;
                frameLayoutParams.setMarginEnd(getResources().getDimensionPixelSize(R.dimen.app_bar_menu_option_padding));
                titleBar.setLayoutParams(frameLayoutParams);
            }
        }
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
    public boolean isUiReady() {
        return isAdded() && getActivity() != null && !getActivity().isFinishing()
                && getView() != null;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mFragmentVisibilityController.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mFragmentVisibilityController.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onVisibilityChanged(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            leaveScreenCrumb(screenCrumb());
        }
    }

    @Override
    public void onDispatchControllerChanged(FragmentVisibilityController controller) {
        mFragmentVisibilityController.setChildVisibilityController(controller);
    }

    @Override
    public void showProgressBar() {
        makeSureInitProgressBar();
        mProgressBarContainer.setVisibility(View.VISIBLE);
        mProgressBarContainer.announceForAccessibility(getString(R.string.please_wait));
    }

    @Override
    public void hideProgressBar() {
        if (mProgressBarContainer != null) {
            mProgressBarContainer.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showProgressDialog() {
        if (getBaseActivity() != null) {
            getBaseActivity().showProgressDialog();
        }
    }

    @Override
    public void showProgressDialog(@StringRes int stringRes) {
        if (getBaseActivity() != null) {
            getBaseActivity().showProgressDialog(stringRes);
        }
    }

    @Override
    public void hideProgressDialog() {
        if (getBaseActivity() != null) {
            getBaseActivity().hideProgressDialog();
        }
    }

    @Override
    public void showFab(boolean animation) {
        getBaseActivity().showFab(animation);
    }

    @Override
    public void hideFab(boolean animation) {
        getBaseActivity().hideFab(animation);
    }

    @Override
    public void setBannerController(@NonNull IBannerController bannerController) {
        ViewGroup bannerContainer = getContainerView().findViewById(R.id.banner_container);
        if (bannerContainer != null) {
            mBannerController = bannerController;
            mBannerController.init(this, bannerContainer);
        }
    }

    @Override
    public void setBannerFlags(int flags) {
        if (mBannerController != null) {
            mBannerController.onStop();
            mBannerController.setBannerFlags(flags);
            mBannerController.onStart();
        }
    }

    @Override
    public void onBannerRefreshed() {}

    public Boolean isRealVisible() {
        return mFragmentVisibilityController.isRealVisible();
    }

    public void onFabClick() {
    }

    public boolean onBackPressed() {
        return false;
    }

    public void finish() {
        FragmentActivity host = getActivity();
        if (host != null) {
            host.finish();
        }
    }

    protected AbstractBaseActivity getBaseActivity() {
        if (getActivity() instanceof AbstractBaseActivity) {
            return (AbstractBaseActivity) getActivity();
        }

        return null;
    }

    protected CoordinatorLayout getContainerView() {
        return mContainerView;
    }

    private void makeSureInitProgressBar() {
        if (mProgressBarContainer == null) {
            ViewStub progressbarStub = mContainerView.findViewById(R.id.stub_progress_bar);
            if (progressbarStub != null) {
                mProgressBarContainer = progressbarStub.inflate();
            } else {
                setProcessBarLayout();
            }
        }
    }

    protected void setProcessBarLayout() {
        mProgressBarContainer =
                LayoutInflater.from(getContext()).inflate(R.layout.base_progress_bar,
                mContainerView, false);
        CoordinatorLayout.LayoutParams layoutParams =
                new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        mContainerView.addView(mProgressBarContainer, layoutParams);
    }




}
