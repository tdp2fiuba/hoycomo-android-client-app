package com.ar.tdp2fiuba.hoycomo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.model.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Review} and makes a call to the
 * specified {@link OnReviewInteractionListener}.
 */
public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.ViewHolder> {

    public interface OnReviewInteractionListener {
        void onExpandReview(Review item);
    }

    private final List<Review> mValues;
    private final OnReviewInteractionListener mListener;

    private int loadingItemIndex = -1;    // Not loading

    private final static int ITEM = 0;
    private final static int LOADING = 1;

    public ReviewRecyclerViewAdapter(OnReviewInteractionListener listener) {
        mValues = new ArrayList<>();
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_row, parent, false);
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

            holder.mUsernameView.setText(holder.mItem.getUser().getFirstName() + ' ' + holder.mItem.getUser().getLastName());
            holder.mRatingView.setText(String.format("%.1f", holder.mItem.getRating()));
            holder.mTimestampView.setText(holder.mItem.getTimestamp());
            holder.mBodyView.setText(holder.mItem.getBody());

            holder.mContentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onExpandReview(holder.mItem);
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

    public void add(Review item) {
        mValues.add(item);
        if (mValues.size() == 1) {
            notifyItemInserted(mValues.size() - 1);
        } else {
            notifyDataSetChanged();
        }
    }

    public void addLoadingFooter() {
        final Review dummyItem = new Review(null, null);
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
        public final View mView;
        public final TextView mUsernameView;
        public final TextView mRatingView;
        public final TextView mTimestampView;
        public final TextView mBodyView;
        public final LinearLayout mContentView;
        public final ProgressBar mProgressBar;
        public Review mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mUsernameView = (TextView) view.findViewById(R.id.review_row_username);
            mRatingView = (TextView) view.findViewById(R.id.review_row_rating);
            mTimestampView = (TextView) view.findViewById(R.id.review_row_timestamp);
            mBodyView = (TextView) view.findViewById(R.id.review_row_body);

            mContentView = (LinearLayout) view.findViewById(R.id.review_row_content);
            mProgressBar = (ProgressBar) view.findViewById(R.id.review_row_loading);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mBodyView.getText() + "'";
        }
    }
}
