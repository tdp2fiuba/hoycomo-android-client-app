package com.ar.tdp2fiuba.hoycomo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.model.OrderItem;

import java.util.List;

public class MyOrderItemAdapter extends RecyclerView.Adapter<MyOrderItemAdapter.ViewHolder> {

    private final List<OrderItem> mValues;

    public MyOrderItemAdapter(List<OrderItem> items) {
        this.mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item_row, parent, false);
        return new MyOrderItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mQuantityView.setText(String.valueOf(holder.mItem.getQuantity()));
        holder.mNameView.setText(holder.mItem.getName());
        holder.mPriceView.setText("$" + String.valueOf(holder.mItem.getPrice()));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public Integer getItemPriceSum() {
        Integer sum = 0;
        for (OrderItem item : mValues) {
            sum += item.getPrice();
        }
        return sum;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public OrderItem mItem;
        public final View mView;
        public final TextView mQuantityView;
        public final TextView mNameView;
        public final TextView mPriceView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mQuantityView = view.findViewById(R.id.order_item_row_quantity);
            mNameView = view.findViewById(R.id.order_item_row_name);
            mPriceView = view.findViewById(R.id.order_item_row_price);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
