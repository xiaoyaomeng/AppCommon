package com.panghu.uikit.view.spinner

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

/**
 * @author by panghu
 * @date 2019-07-08
 */
interface IFilterSpinnerAdapter<T, VH : SpinnerViewHolder> {

    /**
     * Creates a new ViewHolder for a dropdown.  This works the same way onCreateViewHolder in
     * Recycler.Adapter, ViewHolders can be reused for different views.  This is usually a good place
     * to inflate the layout for the dropdown.
     *
     * @param itemView the view to create a header view holder for
     * @return the view holder
     */
    fun onCreateDropdownViewHolder(itemView: View): VH?

    /**
     * Binds an existing ViewHolder to the specified adapter position for a dropdown.
     *
     * @param holder   the view holder
     * @param position the adapter position
     */
    fun onBindDropdownViewHolder(holder: VH?, position: Int)

    /**
     * Creates a new ViewHolder for a display.  This works the same way onCreateViewHolder in
     * Recycler.Adapter, ViewHolders can be reused for different views.  This is usually a good place
     * to inflate the layout for the display.
     *
     * @param itemView the view to create a header view holder for
     * @return the view holder
     */
    fun onCreateDisplayViewHolder(itemView: View): VH?

    /**
     * Binds an existing ViewHolder to the specified adapter position for a display.
     *
     * @param holder   the view holder
     * @param position the adapter position
     */
    fun onBindDisplayViewHolder(holder: VH?, position: Int)

    /**
     * @return the number of views in the adapter
     */
    fun getItemCount(): Int

    /**
     * @return the item data
     */
    fun getItem(position: Int): T

}