package com.ar.tdp2fiuba.hoycomo.adapter;

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

    private int loadingItemIndex = -1;    // Not loading

    private final static int ITEM = 0;
    private final static int LOADING = 1;

    public MenuItemRecyclerViewAdapter(MenuFragment.OnMenuFragmentInteractionListener listener) {
        mValues = new ArrayList<>();
        mListener = listener;
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
            holder.mPriceView.setText(String.valueOf(holder.mItem.getPrice()));
            loadImage(holder);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onMenuItemTap(holder.mItem);
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
            notifyItemInserted(mValues.size() - 1);
        } else {
            notifyDataSetChanged();
        }
    }

    public void addLoadingFooter() {
        final MenuItem dummyItem = new MenuItem(null, null, null, null, null, null);
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

    // TODO: 22/4/18 Load all images in a carousel.
    private void loadImage(final ViewHolder holder) {
        if (holder.mItem.getPictures() != null && !holder.mItem.getPictures().isEmpty()) {
            Picasso.get()
                    .load(holder.mItem.getPictures().get(0))
                    .fit()
                    .into(holder.mImageView);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mPriceView;
        public final ImageView mImageView;
        public final LinearLayout mContentView;
        public final ProgressBar mProgressBar;
        public MenuItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.menu_item_row_name);
            mPriceView = (TextView) view.findViewById(R.id.menu_item_row_price);
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
