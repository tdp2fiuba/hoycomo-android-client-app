package com.ar.tdp2fiuba.hoycomo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.model.Order;

import java.util.ArrayList;
import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    private final List<Order> mValues;
    private final Context mContext;

    private int loadingItemIndex = -1;    // Not loading

    private final static int ITEM = 0;
    private final static int LOADING = 1;

    public MyOrderAdapter(Context context) {
        mValues = new ArrayList<>();
        this.mContext = context;
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

            holder.mStatusView.setText(holder.mItem.getState().getState().toString(mContext));
            holder.mStoreNameView.setText(holder.mItem.getStore().getName());
            holder.mCommentsView.setText(holder.mItem.getDescription());
            holder.mPriceView.setText(String.valueOf(holder.mItem.getPrice()));
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Order mItem;
        public final View mView;
        public final TextView mStoreNameView;
        public final TextView mStatusView;
        public final TextView mCommentsView;
        public final TextView mPriceView;

        public final LinearLayout mContentView;
        public final ProgressBar mProgressBar;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mStoreNameView = view.findViewById(R.id.order_item_row_name);
            mStatusView = view.findViewById(R.id.order_item_row_quantity);
            mCommentsView = view.findViewById(R.id.order_item_row_comments);
            mPriceView = view.findViewById(R.id.order_item_row_price);

            mContentView = (LinearLayout) view.findViewById(R.id.order_row_content);
            mProgressBar = (ProgressBar) view.findViewById(R.id.order_row_loading);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mStoreNameView.getText() + "'";
        }
    }
}
