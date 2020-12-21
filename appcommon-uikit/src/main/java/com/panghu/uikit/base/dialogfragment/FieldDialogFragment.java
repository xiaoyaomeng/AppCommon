package com.panghu.uikit.base.dialogfragment;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.glip.crumb.model.ScreenMeta;
import com.glip.crumb.template.IScreenMetaProvider;
import com.panghu.uikit.base.analytics.AnalyticsHelper;
import com.panghu.uikit.base.analytics.IScreenCrumb;
import com.panghu.uikit.base.analytics.ScreenLocation;

/**
 * Created by panghu on 9/2/16.
 */
public class FieldDialogFragment extends DialogFragment implements IScreenCrumb{

    @Nullable
    protected OnFieldCompletedListener mOnFieldCompletedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getTargetFragment() instanceof OnFieldCompletedListener) {
            mOnFieldCompletedListener = (OnFieldCompletedListener) getTargetFragment();
        } else if (context instanceof OnFieldCompletedListener) {
            mOnFieldCompletedListener = (OnFieldCompletedListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnFieldCompletedListener = null;
    }

    @Override
    @Nullable
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
    public void leaveScreenCrumb(@Nullable ScreenLocation screenLocation) {
        AnalyticsHelper.logScreenCrumb(screenLocation);
    }

}
