package com.panghu.uikit.base.field;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.panghu.uikit.R;
import com.panghu.uikit.utils.DialogUtil;
import com.panghu.uikit.utils.Log;
import java.util.List;

/**
 * Created by panghu on 8/31/16.
 */
public class FieldListAdapter extends RecyclerView.Adapter<FieldViewHolder> {

    private static final String TAG = "FieldListAdapter";
    protected final static int PERSONS_FIELD_PRESENTER = 0;
    private final static int OPTION_FIELD_PRESENTER = 1;
    private final static int EDIT_FIELD_PRESENTER = 2;
    private final static int SWITCH_FIELD_PRESENTER = 3;
    private final static int INPUT_FIELD_PRESENTER = 4;

    private AbstractFieldList mFieldList;
    private List<AbstractField> mVisibleFieldItems;
    protected OnFieldClickListener mOnFieldClickListener;

    public FieldListAdapter(AbstractFieldList fieldList) {
        this.mFieldList = fieldList;
        this.mVisibleFieldItems = fieldList.getVisibleFieldItems();
    }

    public AbstractField getItem(int position) {
        return mVisibleFieldItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mVisibleFieldItems.get(position).getId().ordinal();
    }

    private int getItemPosition(FieldId fieldId) {
        for (int i = 0; i < mVisibleFieldItems.size(); ++i) {
            if (mVisibleFieldItems.get(i).getId() == fieldId) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return mVisibleFieldItems.size();
    }


    public void notifyFieldChanged(FieldId fieldId) {
        notifyFieldChanged(fieldId, null);
    }

    public void notifyFieldChanged(FieldId fieldId, Object payload) {
        int position = getItemPosition(fieldId);
        if (position >= 0) {
            notifyItemChanged(position, payload);
        }
    }

    public void notifyFieldVisibilityChange(FieldId fieldId, boolean isVisible) {
        Log.v(TAG, "FieldId: " + fieldId + ", isVisible: " + isVisible);
        if (isVisible) {
            mVisibleFieldItems = mFieldList.getVisibleFieldItems();
            int position = getItemPosition(fieldId);
            if (position >= 0) {
                notifyItemInserted(position);
            }
        } else {
            int position = getItemPosition(fieldId);
            if (position >= 0) {
                mVisibleFieldItems = mFieldList.getVisibleFieldItems();
                notifyItemRemoved(position);
            }
        }
    }

    public void setOnFieldClickListener(OnFieldClickListener onFieldClickListener) {
        mOnFieldClickListener = onFieldClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        AbstractField field = getItem(position);
        if (field instanceof PersonsField) {
            return PERSONS_FIELD_PRESENTER;
        } else if (field instanceof AbstractOptionField) {
            return OPTION_FIELD_PRESENTER;
        } else if (field instanceof EditTextField) {
            return EDIT_FIELD_PRESENTER;
        } else if (field instanceof SwitchField) {
            return SWITCH_FIELD_PRESENTER;
        } else if (field instanceof InputField) {
            return INPUT_FIELD_PRESENTER;
        } else {
            Log.e(TAG, "Unsupported field " + field);
            return -1;
        }
    }

    @Override
    public FieldViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AbstractFieldPresenter presenter;
        switch (viewType) {
            case PERSONS_FIELD_PRESENTER:
                presenter = new PersonsFieldPresenter();
                break;
            case OPTION_FIELD_PRESENTER:
                presenter = new OptionFieldPresenter();
                break;
            case EDIT_FIELD_PRESENTER:
                presenter = new EditFieldPresenter();
                break;
            case SWITCH_FIELD_PRESENTER:
                presenter = new SwitchFieldPresenter();
                break;
            case INPUT_FIELD_PRESENTER:
                presenter = new InputFieldPresenter();
                break;
            default:
                Log.e(TAG, "Unsupported field type " + viewType);
                return null;
        }
        return presenter.onCreateViewHolder(parent);
    }

    public void updateFieldList(AbstractFieldList fieldList) {
        this.mFieldList = fieldList;
        this.mVisibleFieldItems = fieldList.getVisibleFieldItems();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(FieldViewHolder holder, int position) {
        AbstractField field = getItem(position);
        holder.itemView.setOnClickListener(mOnItemClickListener);
        holder.itemView.setTag(field.getId());
        holder.mPresenter.onBindViewHolder(holder, field);
    }

    @Override
    public void onBindViewHolder(@NonNull FieldViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            AbstractField field = getItem(position);
            holder.itemView.setOnClickListener(mOnItemClickListener);
            holder.itemView.setTag(field.getId());
            holder.mPresenter.onBindViewHolder(holder, field, payloads);
        }
    }

    private View.OnClickListener mOnItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FieldId fieldId = (FieldId) view.getTag();
            int position = getItemPosition(fieldId);
            if (getItem(position).isLocked()) {
                alertSettingLocked(view.getContext());
                return;
            }
            if (mOnFieldClickListener != null) {
                mOnFieldClickListener.onFieldClick(getItem(position), position);
            }
        }
    };

    protected void alertSettingLocked(Context context) {
        DialogUtil.showSimpleAlert(
            context,
            R.string.setting_locked,
            R.string.setting_managed_by_company_admin
        );
    }

    public interface OnFieldClickListener {
        void onFieldClick(AbstractField field, int position);
    }

    public interface OnTitleChangedListener {
        void onTitleChanged(String title);
    }

}
