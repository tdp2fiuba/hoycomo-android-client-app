package com.ar.tdp2fiuba.hoycomo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.adapter.StoreRecyclerViewAdapter;
import com.ar.tdp2fiuba.hoycomo.model.Store;
import com.ar.tdp2fiuba.hoycomo.service.StoreService;
import com.ar.tdp2fiuba.hoycomo.utils.PaginationScrollListener;
import com.ar.tdp2fiuba.hoycomo.utils.RecyclerViewEmptySupport;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnStoreListFragmentInteractionListener}
 * interface.
 */
public class StoreListFragment extends Fragment {

    private OnStoreListFragmentInteractionListener mListener;

    private StoreRecyclerViewAdapter mAdapter;

    private final StoreService storeService;  // TODO: 31/03/18 Don't instantiate it.
    private boolean isLoading = false;
    private static final int paginationCount = 20;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StoreListFragment() {
        storeService = new StoreService();
    }

    public static StoreListFragment newInstance() {
        return new StoreListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_store_list, container, false);

        if (rootView != null) {
            Context context = rootView.getContext();
            RecyclerViewEmptySupport recyclerView = (RecyclerViewEmptySupport) rootView.findViewById(R.id.store_list);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    layoutManager.getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);
            recyclerView.setEmptyView(rootView.findViewById(R.id.store_empty_list));
            recyclerView.addOnScrollListener(new PaginationScrollListener(layoutManager) {
                @Override
                protected void loadMoreItems() {
                    retrieveMoreStores();
                }

                @Override
                public boolean isLoading() {
                    return isLoading;
                }

                @Override
                protected boolean isLastPage() {
                    // TODO: 31/03/18 We should handle status code on API response to know when pagination is over.
                    return false;
                }

                @Override
                protected int getLoadingOffset() {
                    return 5;
                }
            });
            mAdapter = new StoreRecyclerViewAdapter(mListener);
            recyclerView.setAdapter(mAdapter);
            retrieveMoreStores();

            rootView.findViewById(R.id.store_empty_list).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refreshData();
                }
            });
        }

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStoreListFragmentInteractionListener) {
            mListener = (OnStoreListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStoreListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        storeService.resetCount();
    }

    private void retrieveMoreStores() {
        startLoading();

        // TODO: 01/04/18 Replace this delay with actual request to API.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                stopLoading();
                mAdapter.add(storeService.getStores(paginationCount));
            }
        }, 2000);
    }

    private void refreshData() {
        storeService.resetCount();
        retrieveMoreStores();
    }

    private void startLoading() {
        isLoading = true;
        if (mAdapter != null) {
            mAdapter.addLoadingFooter();
        }
    }

    private void stopLoading() {
        isLoading = false;
        if (mAdapter != null) {
            mAdapter.removeLoadingFooter();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnStoreListFragmentInteractionListener {
        void onTap(Store item);
    }
}
