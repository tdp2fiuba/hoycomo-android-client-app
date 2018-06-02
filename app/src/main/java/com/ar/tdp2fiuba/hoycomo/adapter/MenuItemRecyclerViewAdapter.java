package com.ar.tdp2fiuba.hoycomo.adapter;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.fragment.MenuFragment;
import com.ar.tdp2fiuba.hoycomo.model.MenuItem;
import com.ar.tdp2fiuba.hoycomo.model.Store;
import com.ar.tdp2fiuba.hoycomo.utils.NumberUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MenuItem} and makes a call to the
 * specified {@link MenuFragment.OnMenuFragmentInteractionListener}.
 */
public class MenuItemRecyclerViewAdapter extends RecyclerView.Adapter<MenuItemRecyclerViewAdapter.ViewHolder> {

    private final List<MenuItem> mValues;
    private final MenuFragment.OnMenuFragmentInteractionListener mListener;
    private final Store mStore;

    private int loadingItemIndex = -1;    // Not loading

    private final static int ITEM = 0;
    private final static int LOADING = 1;

    public MenuItemRecyclerViewAdapter(MenuFragment.OnMenuFragmentInteractionListener listener, Store store) {
        mValues = new ArrayList<>();
        mListener = listener;
        mStore = store;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        if (getItemViewType(position) == LOADING) {
            holder.mContentView.setVisibility(View.GONE);
            holder.mProgressBar.setVisibility(View.VISIBLE);
        } else {
            holder.mContentView.setVisibility(View.VISIBLE);
            holder.mProgressBar.setVisibility(View.GONE);

            holder.mNameView.setText(holder.mItem.getName());
            holder.mDescriptionView.setText(holder.mItem.getDescription());
            setPrice(holder);
            loadImage(holder);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onMenuItemTap(holder.mItem, mStore);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == loadingItemIndex) ? LOADING : ITEM;
    }

    public void add(MenuItem item) {
        mValues.add(item);
        if (mValues.size() == 1) {
            notifyDataSetChanged();
        } else {
            notifyItemInserted(mValues.size() - 1);
        }
    }

    public void addLoadingFooter() {
        final MenuItem dummyItem = new MenuItem();
        mValues.add(dummyItem);
        loadingItemIndex = mValues.size() - 1;
        if (loadingItemIndex == 0) {
            notifyDataSetChanged();
        } else {
            notifyItemInserted(loadingItemIndex);
        }
    }

    public void removeLoadingFooter() {
        if (loadingItemIndex >= 0) {
            mValues.remove(loadingItemIndex);
            if (loadingItemIndex == 0) {
                notifyDataSetChanged();
            } else {
                notifyItemRemoved(loadingItemIndex);
            }
            loadingItemIndex = -1;
        }
    }

    private void setPrice(final ViewHolder holder) {
        MenuItem item = holder.mItem;

        Double finalPrice = item.getPrice();
        if (item.getDiscount() != null && item.getDiscount() != 0) {
            finalPrice = NumberUtils.subtractPercentage(item.getPrice(), item.getDiscount());
            holder.mDiscountView.setVisibility(View.VISIBLE);
            holder.mDiscountView.setText("$" + String.valueOf(Math.round(item.getDiscount())));
            holder.mDiscountView.setPaintFlags(holder.mDiscountView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.mDiscountView.setVisibility(View.GONE);
        }

        holder.mPriceView.setText("$" + String.valueOf(Math.round(finalPrice)));
    }

    private void loadImage(final ViewHolder holder) {
        MenuItem item = holder.mItem;
        ImageView imageView = holder.mImageView;

        if (item.getPictures() != null && !item.getPictures().isEmpty()) {
            imageView.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(item.getPictures().get(0))
                    .into(imageView);
        } else {
            imageView.setVisibility(View.GONE);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mDescriptionView;
        public final TextView mPriceView;
        public final TextView mDiscountView;
        public final ImageView mImageView;
        public final LinearLayout mContentView;
        public final ProgressBar mProgressBar;
        public MenuItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.menu_item_row_name);
            mDescriptionView = (TextView) view.findViewById(R.id.menu_item_row_description);
            mPriceView = (TextView) view.findViewById(R.id.menu_item_row_price);
            mDiscountView = (TextView) view.findViewById(R.id.menu_item_row_price_without_discount);
            mImageView = (ImageView) view.findViewById(R.id.menu_item_row_image);

            mContentView = (LinearLayout) view.findViewById(R.id.menu_item_row_content);
            mProgressBar = (ProgressBar) view.findViewById(R.id.menu_item_row_loading);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
