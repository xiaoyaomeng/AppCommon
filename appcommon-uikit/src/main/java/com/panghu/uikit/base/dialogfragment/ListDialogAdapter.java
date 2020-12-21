package com.panghu.uikit.base.dialogfragment;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.panghu.uikit.R;
import com.panghu.uikit.utils.Log;
import com.glip.widgets.utils.AccessibilityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by panghu on 4/10/17.
 */

public class ListDialogAdapter extends RecyclerView.Adapter<ListDialogAdapter.AbstractViewHolder> {
    private static final int LIST_DIALOG_VIEW_TYPE_SINGLE_LINE = 0;
    private static final int LIST_DIALOG_VIEW_TYPE_DOUBLE_LINE = LIST_DIALOG_VIEW_TYPE_SINGLE_LINE + 1;
    private static final String TAG = "ListDialogAdapter";
    private final boolean mIsSelectable;

    private List<ListItem> mListItems = new ArrayList<>();
    private int mSelection;
    private int mIconSize;
    private OnItemClickListener mOnItemClickListener;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View itemView) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(itemView, (int) itemView.getTag());
            }
        }
    };

    public ListDialogAdapter(boolean selectable) {
        mIsSelectable = selectable;
    }

    public void setListItems(List<ListItem> listItems) {
        mListItems = listItems;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    void setIconSize(int size) {
        mIconSize = size;
    }

    public int getSelection() {
        return mSelection;
    }

    public void setSelection(int selection) {
        if (selection < mListItems.size() && selection >= 0) {
            mSelection = selection;
        } else {
            Log.w(TAG, "Invalid selection " + selection);
        }
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case LIST_DIALOG_VIEW_TYPE_DOUBLE_LINE:
                return new DoubleLineViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.common_dialog_list_item_double_lines_view, parent, false), mIsSelectable);
            case LIST_DIALOG_VIEW_TYPE_SINGLE_LINE:
            default:
                return new SingleLineViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.common_dialog_list_item_single_line_view, parent, false), mIsSelectable);
        }
    }

    @Override
    public int getItemViewType(int position) {
        ListItem listItem = mListItems.get(position);
        return listItem.isValueVisible() ? LIST_DIALOG_VIEW_TYPE_DOUBLE_LINE : LIST_DIALOG_VIEW_TYPE_SINGLE_LINE;
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        ListItem listItem = mListItems.get(position);
        holder.bindViewHolder(listItem, position == getSelection() && mIsSelectable, mIconSize);
        holder.itemView.setOnClickListener(mOnClickListener);
        holder.itemView.setTag(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mListItems.size();
    }

    public ListItem getItem(int position) {
        return mListItems.get(position);
    }

    static abstract class AbstractViewHolder extends RecyclerView.ViewHolder {

        private AbstractViewHolder(View itemView) {
            super(itemView);
        }

        protected abstract void bindViewHolder(ListItem listItem, boolean isSelected, int iconSize);

    }

    private static class SingleLineViewHolder extends AbstractViewHolder {
        ImageView mIconView;
        TextView mCaptionTextView;
        View mCheckedView;
        boolean mIsSelectable;
        private SingleLineViewHolder(View itemView, boolean isSelectable) {
            super(itemView);
            mIconView = itemView.findViewById(R.id.icon_view);
            mCaptionTextView = itemView.findViewById(R.id.caption_text_view);
            mCheckedView = itemView.findViewById(R.id.checked_view);
            mIsSelectable = isSelectable;
        }

        @Override
        protected void bindViewHolder(ListItem listItem, boolean isSelected, int iconSize) {
            Drawable iconDrawable = listItem.getItemIcon(mIconView.getContext());
            if (iconDrawable != null) {
                mIconView.setVisibility(View.VISIBLE);
                mIconView.setImageDrawable(iconDrawable);
            } else {
                mIconView.setVisibility(View.GONE);
            }

            if (listItem.getTextColorResId() > 0) {
                mCaptionTextView.setTextColor(mCaptionTextView.getContext().getResources().getColor(listItem.getTextColorResId()));
            }

            if (TextUtils.isEmpty(listItem.getCaption())){
                mCaptionTextView.setVisibility(View.GONE);
            }else {
                mCaptionTextView.setVisibility(View.VISIBLE);
                mCaptionTextView.setText(listItem.getCaption());
            }

            mCheckedView.setVisibility(isSelected ? View.VISIBLE : View.GONE);
            itemView.setSelected(isSelected);

            if(!listItem.getCaption().equals(itemView.getContext().getString(R.string.cancel))) {
                setContentDescription(listItem, isSelected);
            }
        }

        protected void setContentDescription(ListItem listItem, boolean isSelected) {
            if (isSelected || !mIsSelectable) {
                itemView.setContentDescription(listItem.getCaption());
            } else {
                itemView.setContentDescription(itemView.getContext().getString(R.string.accessibility_not_selected_item, listItem.getCaption()));
            }
        }
    }

    private static class DoubleLineViewHolder extends SingleLineViewHolder {
        TextView mValueTextView;

        private DoubleLineViewHolder(View itemView, boolean isSeletable) {
            super(itemView, isSeletable);
            mValueTextView = itemView.findViewById(R.id.value_text_view);
        }

        @Override
        protected void bindViewHolder(ListItem listItem, boolean isSelected, int iconSize) {
            super.bindViewHolder(listItem, isSelected, iconSize);
            mValueTextView.setText(listItem.getValue());

            setContentDescription(listItem, isSelected);
        }

        @Override
        protected void setContentDescription(ListItem listItem, boolean isSelected) {
            if (isSelected || !mIsSelectable) {
                itemView.setContentDescription(
                        listItem.getCaption()
                                + ", "
                                + AccessibilityUtils.addSpaceToDigital(listItem.getValue()));
            } else {
                itemView.setContentDescription(
                        itemView
                                .getContext()
                                .getString(
                                        R.string.accessibility_not_selected_item,
                                        listItem.getCaption()
                                                + ", "
                                                + AccessibilityUtils.addSpaceToDigital(listItem.getValue())));
            }
        }
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}


