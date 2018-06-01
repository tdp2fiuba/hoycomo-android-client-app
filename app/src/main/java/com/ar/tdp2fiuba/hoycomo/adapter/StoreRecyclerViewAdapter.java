package com.ar.tdp2fiuba.hoycomo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.fragment.StoreListFragment;
import com.ar.tdp2fiuba.hoycomo.model.Store;
import com.ar.tdp2fiuba.hoycomo.utils.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Store} and makes a call to the
 * specified {@link StoreListFragment.OnStoreListFragmentInteractionListener}.
 */
public class StoreRecyclerViewAdapter extends RecyclerView.Adapter<StoreRecyclerViewAdapter.ViewHolder> {

    private final List<Store> mValues;
    private final StoreListFragment.OnStoreListFragmentInteractionListener mListener;
    private final Context mContext;

    private int loadingItemIndex = -1;    // Not loading

    private final static int ITEM = 0;
    private final static int LOADING = 1;

    public StoreRecyclerViewAdapter(StoreListFragment.OnStoreListFragmentInteractionListener listener) {
        mValues = new ArrayList<>();
        mListener = listener;
        mContext = (Context) mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_row, parent, false);
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
            holder.mFoodTypesView.setText(holder.mItem.getParsedFoodTypesAsString());
            loadImage(holder);
            setDelayTime(holder);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onStoreTap(holder.mItem);
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

    public void add(Store item) {
        mValues.add(item);
        if (mValues.size() == 1) {
            notifyDataSetChanged();
        } else {
            notifyItemInserted(mValues.size() - 1);
        }
    }

    public void addLoadingFooter() {
        final Store dummyItem = new Store(null, null, null, null, null, null, null);
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

    private void loadImage(final ViewHolder holder) {
        Picasso.get()
                .load(holder.mItem.getAvatar())
                .fit()
                .into(holder.mImageView);
    }

    private void setDelayTime(final ViewHolder holder) {
        Double storeDelayTime = holder.mItem.getDelayTime();
        if (storeDelayTime != null) {
            holder.mDelayTimeView.setVisibility(View.VISIBLE);
            Integer avgDelayTime = DateUtils.secToRoundedMin(storeDelayTime);
            String delayTime = mContext.getResources().getString(R.string.avg_delay_time_minutes).replace(":avg", Integer.toString(avgDelayTime));
            holder.mDelayTimeView.setText(delayTime);
        } else {
            holder.mDelayTimeView.setVisibility(View.GONE);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mDelayTimeView;
        public final TextView mFoodTypesView;
        public final ImageView mImageView;
        public final LinearLayout mContentView;
        public final ProgressBar mProgressBar;
        public Store mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.store_row_name);
            mFoodTypesView = (TextView) view.findViewById(R.id.store_row_food_types);
            mDelayTimeView = (TextView) view.findViewById(R.id.store_row_delay_time);
            mImageView = (ImageView) view.findViewById(R.id.store_row_image);

            mContentView = (LinearLayout) view.findViewById(R.id.store_row_content);
            mProgressBar = (ProgressBar) view.findViewById(R.id.store_row_loading);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
