package com.panghu.uikit.base.fragment;

import androidx.fragment.app.Fragment;

public class FragmentVisibilityController {

    private final int INVISIBLE = 0;
    private final int VISIBLE = 1;

    private Fragment mTargetFragment;
    private OnVisibilityChangeListener mOnVisibilityChangeListener;
    private FragmentVisibilityController mChildVisibilityController;
    private int mCurrentVisibility = INVISIBLE;
    private boolean mIsStopped = false;
    private boolean mIsCreated = false;
    private boolean mIsWaitCreate = false;

    public FragmentVisibilityController(Fragment fragment, OnVisibilityChangeListener listener) {
        mTargetFragment = fragment;
        mOnVisibilityChangeListener = listener;
    }

    public void onCreate() {
        if (mTargetFragment.getUserVisibleHint() && !hasParentFragment() || mIsWaitCreate) {
            setCurrentVisibility(VISIBLE);
        }
        mIsCreated = true;
        mIsWaitCreate = false;
    }

    public void onStart() {
        if (!mIsStopped) {
            return;
        }

        if (mTargetFragment.isVisible()
                && mTargetFragment.getUserVisibleHint()
                && !hasParentFragment()) {
            setCurrentVisibility(VISIBLE);
        }
        mIsStopped = false;
    }

    public void onStop() {
        if (mTargetFragment.isVisible()
                && mTargetFragment.getUserVisibleHint()
                && !hasParentFragment()) {
            mIsStopped = setCurrentVisibility(INVISIBLE);
        }
    }

    public void onDestroy() {
        setCurrentVisibility(INVISIBLE);
    }

    public void onHiddenChanged(boolean hidden) {
        setCurrentVisibility(hidden ? INVISIBLE : VISIBLE);
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (mIsCreated) {
            setCurrentVisibility(isVisibleToUser ? VISIBLE : INVISIBLE);
        } else if (isVisibleToUser) {
            mIsWaitCreate = true;
        }
    }

    public void setChildVisibilityController(FragmentVisibilityController controller) {
        mChildVisibilityController = controller;
    }

    public boolean isRealVisible() {
        return mCurrentVisibility == VISIBLE;
    }

    private boolean hasParentFragment() {
        return mTargetFragment.getParentFragment() != null;
    }

    private boolean setCurrentVisibility(int visibility) {
        if (mCurrentVisibility == visibility) {
            return false;
        }
        mCurrentVisibility = visibility;
        if (mOnVisibilityChangeListener != null) {
            mOnVisibilityChangeListener.onVisibilityChanged(mCurrentVisibility == VISIBLE);
            if (mCurrentVisibility == VISIBLE && hasParentFragment()) {
                Fragment parent = mTargetFragment.getParentFragment();
                if (parent instanceof OnVisibilityChangeListener) {
                    ((OnVisibilityChangeListener) parent).onDispatchControllerChanged(this);
                }
            }
            if (mChildVisibilityController != null) {
                mChildVisibilityController.setUserVisibleHint(mCurrentVisibility == VISIBLE);
            }
        }
        return true;
    }

    public interface OnVisibilityChangeListener {
        void onVisibilityChanged(boolean isVisibleToUser);

        void onDispatchControllerChanged(FragmentVisibilityController controller);
    }
}
