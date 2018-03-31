package com.ar.tdp2fiuba.hoycomo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.fragment.BusinessListFragment.OnBusinessListFragmentInteractionListener;
import com.ar.tdp2fiuba.hoycomo.model.Business;
import com.ar.tdp2fiuba.hoycomo.service.BusinessService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link com.ar.tdp2fiuba.hoycomo.model.Business} and makes a call to the
 * specified {@link OnBusinessListFragmentInteractionListener}.
 */
public class BusinessRecyclerViewAdapter extends RecyclerView.Adapter<BusinessRecyclerViewAdapter.ViewHolder> {

    private final List<Business> mValues;
    private final OnBusinessListFragmentInteractionListener mListener;
    private final Context mContext;

    public BusinessRecyclerViewAdapter(OnBusinessListFragmentInteractionListener listener) {
        mValues = new ArrayList<>();
        mListener = listener;
        mContext = (Context) mListener;

        this.add(BusinessService.getBusinesses());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.business_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mNameView.setText(holder.mItem.getName());
        loadImage(holder);
        setDelayTime(holder);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onTap(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void add(List<Business> items) {
        mValues.addAll(items);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mDelayTimeView;
        public final ImageView mImageView;
        public Business mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.business_row_name);
            mDelayTimeView = (TextView) view.findViewById(R.id.business_row_delay_time);
            mImageView = (ImageView) view.findViewById(R.id.business_row_image);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }

    private void loadImage(final ViewHolder holder) {
        Picasso.get()
                .load(holder.mItem.getImageUrl())
                .fit()
                .into(holder.mImageView);
    }

    private void setDelayTime(final ViewHolder holder) {
        String minDelayTime = holder.mItem.getMinDelayTime() != null ? holder.mItem.getMinDelayTime().toString() : null;
        String maxDelayTime = holder.mItem.getMaxDelayTime().toString();
        String delayTime = minDelayTime != null ?
                mContext.getResources().getString(R.string.minutes_range)
                        .replace(":min", minDelayTime)
                        .replace(":max", maxDelayTime) :
                mContext.getResources().getString(R.string.up_to_minutes)
                        .replace(":max", maxDelayTime);
        holder.mDelayTimeView.setText(delayTime);
    }
}
