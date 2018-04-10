package com.ar.tdp2fiuba.hoycomo.utils.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {

    private final LinearLayoutManager layoutManager;

    public PaginationScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (dy > 0) {
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!isLoading() && !isLastPage()) {
                if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount - getLoadingOffset())
                        && firstVisibleItemPosition >= 0) {
                    loadMoreItems();
                }
            }
        }
    }

    protected abstract void loadMoreItems();

    protected abstract boolean isLoading();

    protected abstract boolean isLastPage();

    protected abstract int getLoadingOffset();
}
