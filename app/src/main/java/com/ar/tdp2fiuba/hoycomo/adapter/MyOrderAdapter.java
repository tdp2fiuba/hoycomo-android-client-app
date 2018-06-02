package com.ar.tdp2fiuba.hoycomo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.model.Order;
import com.ar.tdp2fiuba.hoycomo.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    public interface OnMyOrdersInteractionListener {
        void onOrderTap(Order item);
    }

    private final List<Order> mValues;
    private final OnMyOrdersInteractionListener mListener;

    private int loadingItemIndex = -1;    // Not loading

    private final static int ITEM = 0;
    private final static int LOADING = 1;

    public MyOrderAdapter(OnMyOrdersInteractionListener listener) {
        this.mValues = new ArrayList<>();
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_row, parent, false);
        return new MyOrderAdapter.ViewHolder(view);
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

            holder.mStatusView.setText(holder.mItem.getState().getState().toString((Context) mListener));
            holder.mStoreNameView.setText(holder.mItem.getStore().getName());
            setFinalPrice(holder);
            holder.mTimestampView.setText(
                    DateUtils.elapsedTimeFromUTC(holder.mItem.getRegisterTimestamp(), (Context) mListener)
            );

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onOrderTap(holder.mItem);
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

    public void add(Order item) {
        mValues.add(item);
        if (mValues.size() == 1) {
            notifyDataSetChanged();
        } else {
            notifyItemInserted(mValues.size() - 1);
        }
    }

    public void addLoadingFooter() {
        final Order dummyItem = new Order();
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

    private void setFinalPrice(final ViewHolder holder) {
        Double finalPrice = holder.mItem.getPrice() - (holder.mItem.getPrice() * (holder.mItem.getDiscount() / 100));
        holder.mPriceView.setText("$" + String.valueOf(Math.round(finalPrice)));
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public Order mItem;
        public final View mView;
        public final TextView mStoreNameView;
        public final TextView mStatusView;
        public final TextView mPriceView;
        public final TextView mTimestampView;

        public final RelativeLayout mContentView;
        public final ProgressBar mProgressBar;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mStoreNameView = view.findViewById(R.id.order_row_store_name);
            mStatusView = view.findViewById(R.id.order_row_status);
            mPriceView = view.findViewById(R.id.order_row_price);
            mTimestampView = view.findViewById(R.id.order_row_timestamp);

            mContentView = (RelativeLayout) view.findViewById(R.id.order_row_content);
            mProgressBar = (ProgressBar) view.findViewById(R.id.order_row_loading);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mStoreNameView.getText() + "'";
        }
    }
}
